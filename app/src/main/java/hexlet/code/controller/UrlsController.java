// Пакет, в котором находится класс
package hexlet.code.controller;

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

public final class UrlsController {
    public static void listUrls(Context ctx) throws SQLException {
        List<Url> urls = UrlsRepository.getEntities();
        Map<Long, UrlCheck> urlChecks = UrlChecksRepository.findLatestChecks();
        UrlsPage page = new UrlsPage(urls, urlChecks);
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashType(ctx.consumeSessionAttribute("flash-type"));
        ctx.render("urls/index.jte", Collections.singletonMap("page", page));
    };

    // Метод для создания нового URL-а
    public static void createUrl(Context ctx) throws SQLException {
        String inputUrl = ctx.formParam("url");
        URL parsedUrl;

        try {
            assert inputUrl != null;
            parsedUrl = new URL(inputUrl);
        } catch (Exception e) {
            ctx.sessionAttribute("flash", "Некорректный URL");
            ctx.sessionAttribute("flash-type", "danger");
            ctx.redirect(NamedRoutes.rootPath());
            return;
        }

        // Нормализация URL-а для унификации записи
        String normUrl = String.format(
                "%s://%s%s",
                parsedUrl.getProtocol(),
                parsedUrl.getHost(),
                parsedUrl.getPort() == -1 ? "" : ":" + parsedUrl.getPort()
        ).toLowerCase();

        Url url = UrlsRepository.findByName(normUrl).orElse(null);

        if (url != null) {
            ctx.sessionAttribute("flash", "Страница уже существует");
            ctx.sessionAttribute("flash-type", "info");
        } else {
            Url newUrl = new Url(normUrl);
            UrlsRepository.save(newUrl);
            ctx.sessionAttribute("flash", "Страница успешно добавлена");
            ctx.sessionAttribute("flash-type", "success");
        }
        ctx.redirect("/urls");
    };

    // Метод для отображения информации о конкретном URL-е
    public static void showUrl(Context ctx) throws SQLException {
        long id = ctx.pathParamAsClass("id", Long.class).getOrDefault(null);
        Url url = UrlsRepository.find(id)
                .orElseThrow(() -> new NotFoundResponse("Url with id: " + id + " not found"));
        List<UrlCheck> urlChecks = UrlChecksRepository.findByUrlId(id);
        UrlPage page = new UrlPage(url, urlChecks);
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashType(ctx.consumeSessionAttribute("flash-type"));
        ctx.render("urls/show.jte", Collections.singletonMap("page", page));
    };

    // Метод для проверки URL-а
    public static void checkUrl(Context ctx) throws SQLException {
        long id = ctx.pathParamAsClass("id", Long.class).getOrDefault(null);
        Url url = UrlsRepository.findById(id)
                .orElseThrow(() -> new NotFoundResponse("Url with id: " + id + " not found"));

        try {
            HttpResponse<String> response = Unirest.get(url.getName()).asString();
            Document doc = Jsoup.parse(response.getBody());
            int statusCode = response.getStatus();
            String title = doc.title();
            Element h1Element = doc.selectFirst("h1");
            String h1 = h1Element == null ? "" : h1Element.text();
            Element descriptionElement = doc.selectFirst("meta[name=description]");
            String description = descriptionElement == null ? "" : descriptionElement.attr("content");
            UrlCheck newUrlCheck = new UrlCheck(statusCode, title, h1, description);
            newUrlCheck.setUrlId(id);
            UrlChecksRepository.save(newUrlCheck);
            ctx.sessionAttribute("flash", "Страница успешно проверена");
            ctx.sessionAttribute("flash-type", "success");
        } catch (UnirestException e) {
            ctx.sessionAttribute("flash", "Некорректный адрес");
            ctx.sessionAttribute("flash-type", "danger");
        } catch (Exception e) {
            ctx.sessionAttribute("flash", e.getMessage());
            ctx.sessionAttribute("flash-type", "danger");
        }
        ctx.redirect("/urls/" + url.getId());
    };
}
