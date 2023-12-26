<%--// Импорт необходимых классов и библиотек--%>
@import hexlet.code.dto.UrlPage
@import hexlet.code.utils.NamedRoutes;
@import java.time.format.DateTimeFormatter

<%--// Параметры шаблона--%>
@param UrlPage page

<%--// Определение форматтера для преобразования даты и времени--%>
!{var formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");}

<%--// Использование макета страницы и передача параметров--%>
@template.layout.page(
page = page,
content = @`
    <!-- Основной контейнер с отступом сверху -->
    <div class="container-lg mt-5">
        <!-- Заголовок страницы с именем сайта -->
        <h1>Сайт: ${page.getUrl().getName()}</h1>

        <!-- Таблица с основной информацией о сайте -->
        <table class="table table-bordered table-hover mt-3">
            <!-- Тело таблицы -->
            <tbody>
            <!-- Строки с информацией о сайте (ID, Имя, Дата создания) -->
            <tr>
                <td>ID</td>
                <td>${page.getUrl().getId()}</td>
            </tr>
            <tr>
                <td>Имя</td>
                <td>${page.getUrl().getName()}</td>
            </tr>
            <tr>
                <td>Дата создания</td>
                <td>${page.getUrl().getCreatedAt().toLocalDateTime().format(formatter)}</td>
            </tr>
            </tbody>
        </table>

        <!-- Заголовок раздела с проверками сайта -->
        <h2 class="mt-5">Проверки</h2>

        <!-- Форма для запуска новой проверки сайта -->
        <form method="post" action="${NamedRoutes.urlChecksPath(page.getUrl().getId())}">
            <button type="submit" class="btn btn-primary">Запустить проверку</button>
        </form>

        <!-- Таблица с результатами проверок сайта -->
        <table class="table table-bordered table-hover mt-3">
            <!-- Заголовки таблицы -->
            <thead>
            <th class="col-1">ID</th>
            <th class="col-1">Код ответа</th>
            <th>title</th>
            <th>h1</th>
            <th>description</th>
            <th class="col-2">Дата проверки</th>
            </thead>
            <!-- Тело таблицы -->
            <tbody>
            <!-- Цикл для вывода информации о каждой проверке сайта -->
            @for(var check : page.getChecks())
                <!-- Строка таблицы с результатами проверки -->
                <tr>
                    <td>
                        ${check.getId()}
                    </td>
                    <td>
                        ${check.getStatusCode()}
                    </td>
                    <td>
                        ${check.getTitle()}
                    </td>
                    <td>
                        ${check.getH1()}
                    </td>
                    <td>
                        ${check.getDescription()}
                    </td>
                    <td>
                        ${check.getCreatedAt().toLocalDateTime().format(formatter)}
                    </td>
                </tr>
            @endfor
            </tbody>
        </table>
    </div>
`
)
