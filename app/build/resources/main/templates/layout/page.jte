<%-- Импорт необходимых классов и библиотек--%>
@import gg.jte.Content
@import hexlet.code.dto.BasePage
@import hexlet.code.utils.NamedRoutes

<%--// Параметры шаблона--%>
@param Content content
@param BasePage page = null

<%--// HTML-разметка страницы--%>
<!DOCTYPE html>
<html>

<head>
    <!-- Мета-теги для кодировки и масштабирования -->
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- Заголовок страницы -->
    <title>Анализатор страниц</title>
    <!-- Подключение стилей Bootstrap -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65" crossorigin="anonymous">
    <!-- Подключение скриптов Bootstrap -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-kenU1KFdBIe4zVF0s0G1M5b4hcpxyD9F7jL+jjXkk+Q2h455rYXK/7HAuoJl+0I4"
            crossorigin="anonymous"></script>
</head>

<body class="d-flex flex-column min-vh-100">
<!-- Навигационное меню Bootstrap -->
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container-fluid">
        <!-- Логотип и название сайта -->
        <a class="navbar-brand" href="${NamedRoutes.rootPath()}">Анализатор страниц</a>
        <!-- Кнопка для отображения меню на мобильных устройствах -->
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav"
                aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <!-- Основное меню навигации -->
        <div class="collapse navbar-collapse" id="navbarNav">
            <div class="navbar-nav">
                <!-- Ссылки на разделы сайта -->
                <a class="nav-link" href="${NamedRoutes.rootPath()}">Главная</a>
                <a class="nav-link" href="${NamedRoutes.urlsPath()}">Сайты</a>
            </div>
        </div>
    </div>
</nav>

<!-- Основное содержимое страницы -->
<main class="flex-grow-1">
    <!-- Проверка на наличие Flash-сообщений -->
    @if(page != null && page.getFlash() != null)
        <!-- Вывод Flash-сообщения с возможностью закрытия -->
        <div class="rounded-0 m-0 alert alert-dismissible fade show alert-${page.getFlashType()}" role="alert">
            <p class="m-0">${page.getFlash()}</p>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    @endif
    <!-- Секция с основным содержимым страницы -->
    <section>
        ${content}
    </section>
</main>

<!-- Нижний колонтитул страницы -->
<footer class="footer border-top py-3 mt-5 bg-light">
    <div class="container-xl">
        <!-- Текст в подвале с ссылкой на Hexlet -->
        <div class="text-center">
            created by
            <a href="https://ru.hexlet.io" target="_blank">Hexlet</a>
        </div>
    </div>
</footer>
</body>
</html>
