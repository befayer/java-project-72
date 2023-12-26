<%--// Импорт необходимых классов и библиотек--%>
@import hexlet.code.dto.MainPage
@import hexlet.code.utils.NamedRoutes

<%--// Параметры шаблона--%>
@param MainPage page

<%--// Использование макета страницы и передача параметров--%>
@template.layout.page(
page = page,
content = @`
    <!-- Основной контейнер с фоном и отступами -->
    <div class="container-fluid bg-dark p-5">
        <!-- Строка с элементами разметки -->
        <div class="row">
            <!-- Колонка для выравнивания по центру и установки ширины -->
            <div class="col-md-10 col-lg-8 mx-auto text-white">
                <!-- Заголовок страницы -->
                <h1 class="display-3 mb-0">Анализатор страниц</h1>
                <!-- Подзаголовок страницы -->
                <p class="lead">Бесплатно проверяйте сайты на SEO пригодность</p>

                <!-- Форма для отправки URL-адреса сайта -->
                <form action="${NamedRoutes.urlsPath()}" method="post" class="rss-form text-body">
                    <!-- Строка формы с полем ввода URL и кнопкой отправки -->
                    <div class="row">
                        <!-- Колонка с полем ввода URL -->
                        <div class="col">
                            <!-- Плавающее поле ввода URL -->
                            <div class="form-floating">
                                <!-- Поле ввода URL -->
                                <input id="url-input" autofocus type="text" required name="url" aria-label="url"
                                       class="form-control w-100" placeholder="ссылка" autocomplete="off">
                                <!-- Подпись для поля ввода URL -->
                                <label for="url-input">Ссылка</label>
                            </div>
                        </div>
                        <!-- Колонка с кнопкой отправки формы -->
                        <div class="col-auto">
                            <!-- Кнопка отправки формы -->
                            <button type="submit" class="h-100 btn btn-lg btn-primary px-sm-5">Проверить</button>
                        </div>
                    </div>
                </form>

                <!-- Дополнительная информация и подсказка для пользователей -->
                <p class="mt-2 mb-0 text-muted">Пример: https://www.example.com</p>
            </div>
        </div>
    </div>
`
)
