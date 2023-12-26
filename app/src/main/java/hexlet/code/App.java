package hexlet.code;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Statement;

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

    // Метод для получения порта приложения из переменной окружения
    private static int getPort() {
        String port = System.getenv().getOrDefault("PORT", "3000");
        return Integer.parseInt(port);
    }

    // Метод для получения URL базы данных из переменной окружения
    private static String getDatabaseUrl() {
        return System.getenv().getOrDefault("JDBC_DATABASE_URL", "jdbc:h2:mem:project");
    }

    // Метод для инициализации Javalin приложения
    public static Javalin getApp() throws IOException, SQLException {

        // Конфигурация Hikari для работы с базой данных
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(getDatabaseUrl());

        //инициализация источника данных Hikari
        HikariDataSource dataSource = new HikariDataSource(hikariConfig);

        // Чтение содержимого файла schema.sql
        String sql = readResourceFile("schema.sql");

        // Логирование SQL-запроса
        log.info(sql);

        // Выполнение SQL-запроса для создания таблицы в базе данных
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }

        // Установка источника данных в репозитории
        BaseRepository.dataSource = dataSource;

        // Создание Javalin приложения
        Javalin app = Javalin.create(config -> {
            config.plugins.enableDevLogging();
        });

        // Перед каждым запросом установка Content-Type
        app.before(ctx -> {
            ctx.contentType("text/html; charset=utf-8");
        });

        //инициализация JavalinJte для работы с шаблонами
        JavalinJte.init(createTemplateEngine());

        // Определение маршрутов и их обработчиков
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
        TemplateEngine templateEngine = TemplateEngine.create(codeResolver, ContentType.Html);
        return templateEngine;
    }

    // Метод для чтения содержимого файла из ресурсов
    private static String readResourceFile(String fileName) throws IOException {
        Path path = Paths.get("app", "src", "main", "resources", fileName);
        return Files.readString(path);
    }

    // Основной метод, запускающий приложение
    public static void main(String[] args) throws SQLException, IOException {
        // Получение и запуск Javalin приложения
        Javalin app = getApp();
        app.start(getPort());
    }
}
