// Пакет, в котором находится класс
package hexlet.code.controller;

// Импорты необходимых классов и библиотек
import hexlet.code.dto.MainPage;
import io.javalin.http.Context;

import java.util.Collections;

// Определение класса контроллера для управления корневым маршрутом
public final class RootController {

    // Метод для отображения приветственной страницы
    public static void welcome(Context ctx) {
        // Создание объекта главной страницы для передачи данных в представление
        MainPage page = new MainPage();

        // Установка сообщений Flash для отображения уведомлений пользователю
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashType(ctx.consumeSessionAttribute("flash-type"));

        // Рендеринг страницы с передачей данных в представление
        ctx.render("index.jte", Collections.singletonMap("page", page));
    };
}
