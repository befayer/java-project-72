// Пакет, в котором находится класс
package hexlet.code.controller;

// Импорты необходимых классов и библиотек
import hexlet.code.dto.UrlPage;
import hexlet.code.dto.UrlsPage;
import hexlet.code.entity.Url;
import hexlet.code.entity.UrlCheck;
import hexlet.code.repository.UrlChecksRepository;
import hexlet.code.repository.UrlsRepository;
import hexlet.code.utils.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.URL;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

// Определение класса контроллера для управления URL-ами
public final class UrlsController {

    // Метод для отображения списка URL-ов
    public static void listUrls(Context ctx) throws SQLException {
        // Получение списка URL-ов и последних проверок из репозиториев
        List<Url> urls = UrlsRepository.getEntities();
        Map<Long, UrlCheck> urlChecks = UrlChecksRepository.findLatestChecks();

        // Создание объекта страницы для передачи данных в представление
        UrlsPage page = new UrlsPage(urls, urlChecks);

        // Установка сообщений Flash для отображения уведомлений пользователю
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashType(ctx.consumeSessionAttribute("flash-type"));

        // Рендеринг страницы с передачей данных в представление
        ctx.render("urls/index.jte", Collections.singletonMap("page", page));
    };

    // Метод для создания нового URL-а
    public static void createUrl(Context ctx) throws SQLException {
        // Получение введенного URL-а из формы
        String inputUrl = ctx.formParam("url");
        URL parsedUrl;

        // Попытка парсинга URL-а, обработка ошибок
        try {
            assert inputUrl != null;
            parsedUrl = new URL(inputUrl);
        } catch (Exception e) {
            // Установка сообщений Flash в случае ошибки
            ctx.sessionAttribute("flash", "Некорректный URL");
            ctx.sessionAttribute("flash-type", "danger");
            // Редирект на главную страницу
            ctx.redirect(NamedRoutes.rootPath());
            return;
        }

        // Нормализация URL-а для унификации записи
        String normalizedUrl = String.format(
                "%s://%s%s",
                parsedUrl.getProtocol(),
                parsedUrl.getHost(),
                parsedUrl.getPort() == -1 ? "" : ":" + parsedUrl.getPort()
        ).toLowerCase();

        // Поиск URL-а в репозитории
        Url url = UrlsRepository.findByName(normalizedUrl).orElse(null);

        // Проверка наличия URL-а и установка сообщений Flash
        if (url != null) {
            ctx.sessionAttribute("flash", "Страница уже существует");
            ctx.sessionAttribute("flash-type", "info");
        } else {
            // Создание нового URL-а и сохранение в репозитории
            Url newUrl = new Url(normalizedUrl);
            UrlsRepository.save(newUrl);
            ctx.sessionAttribute("flash", "Страница успешно добавлена");
            ctx.sessionAttribute("flash-type", "success");
        }

        // Редирект на страницу со списком URL-ов
        ctx.redirect("/urls");
    };

    // Метод для отображения информации о конкретном URL-е
    public static void showUrl(Context ctx) throws SQLException {
        // Получение идентификатора URL-а из параметров запроса
        long id = ctx.pathParamAsClass("id", Long.class).getOrDefault(null);

        // Поиск URL-а по идентификатору и выброс исключения, если не найден
        Url url = UrlsRepository.find(id)
                .orElseThrow(() -> new NotFoundResponse("Url with id = " + id + " not found"));

        // Получение списка проверок для данного URL-а
        List<UrlCheck> urlChecks = UrlChecksRepository.findByUrlId(id);

        // Создание объекта страницы для передачи данных в представление
        UrlPage page = new UrlPage(url, urlChecks);

        // Установка сообщений Flash для отображения уведомлений пользователю
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashType(ctx.consumeSessionAttribute("flash-type"));

        // Рендеринг страницы с передачей данных в представление
        ctx.render("urls/show.jte", Collections.singletonMap("page", page));
    };

    // Метод для проверки URL-а
    public static void checkUrl(Context ctx) throws SQLException {
        // Получение идентификатора URL-а из параметров запроса
        long id = ctx.pathParamAsClass("id", Long.class).getOrDefault(null);

        // Поиск URL-а по идентификатору и выброс исключения, если не найден
        Url url = UrlsRepository.findById(id)
                .orElseThrow(() -> new NotFoundResponse("Url with id = " + id + " not found"));

        try {
            // Отправка HTTP-запроса к URL-у и получение ответа
            HttpResponse<String> response = Unirest.get(url.getName()).asString();

            // Парсинг HTML-страницы
            Document doc = Jsoup.parse(response.getBody());

            // Извлечение информации из HTML-страницы
            int statusCode = response.getStatus();
            String title = doc.title();
            Element h1Element = doc.selectFirst("h1");
            String h1 = h1Element == null ? "" : h1Element.text();
            Element descriptionElement = doc.selectFirst("meta[name=description]");
            String description = descriptionElement == null ? "" : descriptionElement.attr("content");

            // Создание новой записи о проверке и сохранение в репозитории
            UrlCheck newUrlCheck = new UrlCheck(statusCode, title, h1, description);
            newUrlCheck.setUrlId(id);
            UrlChecksRepository.save(newUrlCheck);

            // Установка сообщений Flash для отображения уведомлений пользователю
            ctx.sessionAttribute("flash", "Страница успешно проверена");
            ctx.sessionAttribute("flash-type", "success");
        } catch (UnirestException e) {
            // Установка сообщений Flash в случае ошибки
            ctx.sessionAttribute("flash", "Некорректный адрес");
            ctx.sessionAttribute("flash-type", "danger");
        } catch (Exception e) {
            // Установка сообщений Flash в случае других ошибок
            ctx.sessionAttribute("flash", e.getMessage());
            ctx.sessionAttribute("flash-type", "danger");
        }

        // Редирект на страницу с информацией о URL-е
        ctx.redirect("/urls/" + url.getId());
    };
}
