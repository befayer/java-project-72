<%--// Импорт необходимых классов и библиотек--%>
@import hexlet.code.dto.UrlsPage
@import hexlet.code.utils.NamedRoutes
@import java.time.format.DateTimeFormatter
<%--
// Параметры шаблона--%>
@param UrlsPage page

<%--// Определение форматтера для преобразования даты и времени--%>
!{var formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");}

<%--// Использование макета страницы и передача параметров--%>
@template.layout.page(
page = page,
content = @`
    <!-- Основной контейнер с отступом сверху -->
    <div class="container-lg mt-5">
        <!-- Заголовок раздела -->
        <h1>Сайты</h1>

        <!-- Таблица для отображения списка сайтов -->
        <table class="table table-bordered table-hover mt-3">
            <!-- Заголовки таблицы -->
            <thead>
            <tr>
                <th class="col-1">ID</th>
                <th>Имя</th>
                <th class="col-2">Последняя проверка</th>
                <th class="col-1">Код ответа</th>
            </tr>
            </thead>
            <!-- Тело таблицы -->
            <tbody>
            <!-- Цикл для вывода информации о каждом сайте -->
            @for(var url : page.getUrls())
                <!-- Получение последней проверки для текущего сайта -->
                !{var urlCheck = page.getLatestChecks().get(url.getId());}

                <!-- Строка таблицы с информацией о сайте -->
                <tr>
                    <!-- Колонка с ID сайта -->
                    <td>
                        ${url.getId()}
                    </td>
                    <!-- Колонка с именем сайта и ссылкой на детальную информацию -->
                    <td>
                        <a href="${NamedRoutes.urlPath(url.getId())}">${url.getName()}</a>
                    </td>
                    <!-- Колонка с временем последней проверки сайта -->
                    <td>
                        ${urlCheck == null ? "" : urlCheck.getCreatedAt().toLocalDateTime().format(formatter)}
                    </td>
                    <!-- Колонка с кодом ответа от сервера сайта -->
                    <td>
                        ${urlCheck == null ? "" : String.valueOf(urlCheck.getStatusCode())}
                    </td>
                </tr>
            @endfor
            </tbody>
        </table>
    </div>
`
)
