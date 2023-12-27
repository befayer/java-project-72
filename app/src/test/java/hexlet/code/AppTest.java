package hexlet.code;

import hexlet.code.entity.Url;
import hexlet.code.repository.UrlChecksRepository;
import hexlet.code.repository.UrlsRepository;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

class AppTest {

    private static Javalin app;
    private static MockWebServer mockServer;

    @BeforeEach
    void setUp() throws SQLException, IOException {
        app = App.getApp();
    }

    private static Path getFixturePath() {
        return Paths.get("src", "test", "resources", "fixtures", "index.html")
                .toAbsolutePath().normalize();
    }

    private static String readFixture() throws IOException {
        Path filePath = getFixturePath();
        return Files.readString(filePath).trim();
    }

    // инициализация мок-сервера перед запуском всех тестов
    @BeforeAll
    public static void beforeAll() throws IOException {
        mockServer = new MockWebServer();
        MockResponse mockedResponse = new MockResponse()
                .setBody(readFixture());
        mockServer.enqueue(mockedResponse);
        mockServer.start();
    }

    // Завершение работы мок-сервера после завершения всех тестов
    @AfterAll
    public static void afterAll() throws IOException {
        mockServer.shutdown();
    }

    // Вложенный класс для тестирования корневого URL
    @Nested
    class RootTest {
        // Тест проверки кода ответа при запросе к корневому URL
        @Test
        void testIndex() {
            JavalinTest.test(app, (server, client) -> {
                assertThat(client.get("/").code()).isEqualTo(200);
            });
        }
    }

    // Вложенный класс для тестирования проверки URL
    @Nested
    class UrlCheckTest {

        @Test
        void testStore() {
            String url = mockServer.url("/").toString().replaceAll("/$", "");
            JavalinTest.test(app, (server, client) -> {
                var requestBody = "url=" + url;
                assertThat(client.post("/urls", requestBody).code()).isEqualTo(200);

                var actualUrl = UrlsRepository.findByName(url).orElse(null);
                assertThat(actualUrl).isNotNull();
                assertThat(actualUrl.getName()).isEqualTo(url);
                client.post("/urls/" + actualUrl.getId() + "/checks");
                var responce = client.get("/urls/" + actualUrl.getId());
                assertThat(responce.code()).isEqualTo(200);
                assert responce.body() != null;
                assertThat(responce.body().string()).contains(url);
                var actualCheckUrl = UrlChecksRepository
                        .findLatestChecks().get(actualUrl.getId());
                assertThat(actualCheckUrl).isNotNull();
                assertThat(actualCheckUrl.getStatusCode()).isEqualTo(200);
                assertThat(actualCheckUrl.getTitle()).isEqualTo("Test page");
                assertThat(actualCheckUrl.getH1()).isEqualTo("Do not expect a miracle, miracles yourself!");
                assertThat(actualCheckUrl.getDescription()).contains("statements of great people");
            });
        }
    }

    // Вложенный класс для тестирования URL
    @Nested
    class UrlTest {
        // Тест проверки кода ответа при запросе к URL "/urls"
        @Test
        void testIndex() {
            JavalinTest.test(app, (server, client) -> {
                assertThat(client.get("/urls").code()).isEqualTo(200);
            });
        }

        // Тест проверки кода ответа и сохранения URL в репозитории
        @Test
        void testShow() throws SQLException {
            var url = new Url("http://test.io");
            UrlsRepository.save(url);
            JavalinTest.test(app, (server, client) -> {
                var responce = client.get("/urls/" + url.getId());
                assertThat(responce.code()).isEqualTo(200);
            });
        }

        // Тест проверки кода ответа и сохранения нового URL в репозитории
        @Test
        void testStore() throws SQLException {
            String inputUrl = "https://ru.hexlet.io";
            JavalinTest.test(app, (server, client) -> {
                var requestBody = "url=" + inputUrl;
                var response = client.post("/urls", requestBody);
                assertThat(response.code()).isEqualTo(200);
            });
            Url url = UrlsRepository.findByName(inputUrl).orElse(null);
            assertThat(url).isNotNull();
            assertThat(url.getName()).isEqualTo(inputUrl);
        }
    }
}
