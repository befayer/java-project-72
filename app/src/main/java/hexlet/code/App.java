package hexlet.code;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.ResourceCodeResolver;
import hexlet.code.repository.BaseRepository;
import hexlet.code.utils.NamedRoutes;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;
import lombok.extern.slf4j.Slf4j;

import hexlet.code.controller.RootController;
import hexlet.code.controller.UrlsController;

@Slf4j
public final class App {

    public static void main(String[] args) throws SQLException, IOException {
        Javalin app = getApp();
        app.start(getPort());
    }

    // Метод для получения порта приложения из переменной окружения
    private static int getPort() {
        String port = System.getenv().getOrDefault("PORT", "3000");
        return Integer.parseInt(port);
    }

    // Метод для получения URL базы данных из переменной окружения
    private static String getDatabaseUrl() {
        return System.getenv().getOrDefault("JDBC_DATABASE_URL", "jdbc:h2:mem:project");
    }

    public static boolean isProduction() {
        return System.getenv().getOrDefault("APP_ENV", "development").equals("production");
    }

    // Метод для получения пароля для доступа к базе данных из переменной окружения
    private static String getDatabasePassword() {
        return System.getenv("JDBC_DATABASE_PASSWORD");
    }

    // Метод для получения username для доступа к базе данных из переменной окружения
    private static String getDatabaseUsername() {
        return System.getenv("JDBC_DATABASE_USERNAME");
    }

    // Метод для инициализации Javalin приложения
    public static Javalin getApp() throws IOException, SQLException {
        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setJdbcUrl(getDatabaseUrl());

        if (isProduction()) {
            var username = System.getenv("JDBC_DATABASE_USERNAME");
            var password = System.getenv("JDBC_DATABASE_PASSWORD");
            hikariConfig.setUsername(username);
            hikariConfig.setPassword(password);
        }

        HikariDataSource dataSource = new HikariDataSource(hikariConfig);

        String sql = readResourceFile();
        log.info(sql);

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }

        // Установка источника данных в репозитории
        BaseRepository.dataSource = dataSource;

        Javalin app = Javalin.create(config -> {
            config.plugins.enableDevLogging();
        });

        // Перед каждым запросом установка Content-Type
        app.before(ctx -> {
            ctx.contentType("text/html; charset=utf-8");
        });

        //инициализация JavalinJte для работы с шаблонами
        JavalinJte.init(createTemplateEngine());

        app.get("/", RootController::welcome);
        app.get(NamedRoutes.urlsPath(), UrlsController::listUrls);
        app.post(NamedRoutes.urlsPath(), UrlsController::createUrl);
        app.get(NamedRoutes.urlPath("{id}"), UrlsController::showUrl);
        app.post(NamedRoutes.urlChecksPath("{id}"), UrlsController::checkUrl);
        return app;
    }

    // Метод для создания движка шаблонов
    private static TemplateEngine createTemplateEngine() {
        ClassLoader classLoader = App.class.getClassLoader();
        ResourceCodeResolver codeResolver = new ResourceCodeResolver("templates", classLoader);
        return TemplateEngine.create(codeResolver, ContentType.Html);
    }

    private static String readResourceFile() throws IOException {
        var inputStream = App.class.getClassLoader().getResourceAsStream("schema.sql");
        assert inputStream != null;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }

    }
}
