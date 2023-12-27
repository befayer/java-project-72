package gg.jte.generated.ondemand;
import hexlet.code.dto.MainPage;
import hexlet.code.utils.NamedRoutes;
public final class JteindexGenerated {
	public static final String JTE_NAME = "index.jte";
	public static final int[] JTE_LINE_INFO = {1,1,2,5,5,5,7,7,8,11,11,12,14,16,18,20,23,24,24,24,24,24,24,24,24,24,25,27,29,31,34,38,40,46,51,51,51,52};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, MainPage page) {
		jteOutput.writeContent("\n");
		jteOutput.writeContent("\n");
		gg.jte.generated.ondemand.layout.JtepageGenerated.render(jteOutput, jteHtmlInterceptor, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\n    ");
				jteOutput.writeContent("\n    <div class=\"container-fluid bg-dark p-5\">\n        ");
				jteOutput.writeContent("\n        <div class=\"row\">\n            ");
				jteOutput.writeContent("\n            <div class=\"col-md-10 col-lg-8 mx-auto text-white\">\n                ");
				jteOutput.writeContent("\n                <h1 class=\"display-3 mb-0\">Анализатор страниц</h1>\n                ");
				jteOutput.writeContent("\n                <p class=\"lead\">Бесплатно проверяйте сайты на SEO пригодность</p>\n\n                ");
				jteOutput.writeContent("\n                <form");
				var __jte_html_attribute_0 = NamedRoutes.urlsPath();
				if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_0)) {
					jteOutput.writeContent(" action=\"");
					jteOutput.setContext("form", "action");
					jteOutput.writeUserContent(__jte_html_attribute_0);
					jteOutput.setContext("form", null);
					jteOutput.writeContent("\"");
				}
				jteOutput.writeContent(" method=\"post\" class=\"rss-form text-body\">\n                    ");
				jteOutput.writeContent("\n                    <div class=\"row\">\n                        ");
				jteOutput.writeContent("\n                        <div class=\"col\">\n                            ");
				jteOutput.writeContent("\n                            <div class=\"form-floating\">\n                                ");
				jteOutput.writeContent("\n                                <input id=\"url-input\" autofocus type=\"text\" required name=\"url\" aria-label=\"url\"\n                                       class=\"form-control w-100\" placeholder=\"ссылка\" autocomplete=\"off\">\n                                ");
				jteOutput.writeContent("\n                                <label for=\"url-input\">Ссылка</label>\n                            </div>\n                        </div>\n                        ");
				jteOutput.writeContent("\n                        <div class=\"col-auto\">\n                            ");
				jteOutput.writeContent("\n                            <button type=\"submit\" class=\"h-100 btn btn-lg btn-primary px-sm-5\">Проверить</button>\n                        </div>\n                    </div>\n                </form>\n\n                ");
				jteOutput.writeContent("\n                <p class=\"mt-2 mb-0 text-muted\">Пример: https://www.example.com</p>\n            </div>\n        </div>\n    </div>\n");
			}
		}, page);
		jteOutput.writeContent("\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		MainPage page = (MainPage)params.get("page");
		render(jteOutput, jteHtmlInterceptor, page);
	}
}
