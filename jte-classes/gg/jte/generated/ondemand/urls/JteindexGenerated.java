package gg.jte.generated.ondemand.urls;
import hexlet.code.dto.UrlsPage;
import hexlet.code.utils.NamedRoutes;
import java.time.format.DateTimeFormatter;
public final class JteindexGenerated {
	public static final String JTE_NAME = "urls/index.jte";
	public static final int[] JTE_LINE_INFO = {1,1,2,3,6,6,6,8,8,9,9,11,12,15,15,16,18,21,23,32,34,35,35,36,37,37,39,41,43,43,43,45,47,47,47,47,47,47,47,47,47,47,47,47,49,51,51,51,53,55,55,55,58,58,62,62,62,63};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, UrlsPage page) {
		jteOutput.writeContent("\n");
		jteOutput.writeContent("\n");
		var formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
		jteOutput.writeContent("\n\n");
		jteOutput.writeContent("\n");
		gg.jte.generated.ondemand.layout.JtepageGenerated.render(jteOutput, jteHtmlInterceptor, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\n    ");
				jteOutput.writeContent("\n    <div class=\"container-lg mt-5\">\n        ");
				jteOutput.writeContent("\n        <h1>Сайты</h1>\n\n        ");
				jteOutput.writeContent("\n        <table class=\"table table-bordered table-hover mt-3\">\n            ");
				jteOutput.writeContent("\n            <thead>\n            <tr>\n                <th class=\"col-1\">ID</th>\n                <th>Имя</th>\n                <th class=\"col-2\">Последняя проверка</th>\n                <th class=\"col-1\">Код ответа</th>\n            </tr>\n            </thead>\n            ");
				jteOutput.writeContent("\n            <tbody>\n            ");
				jteOutput.writeContent("\n            ");
				for (var url : page.getUrls()) {
					jteOutput.writeContent("\n                ");
					jteOutput.writeContent("\n                ");
					var urlCheck = page.getLatestChecks().get(url.getId());
					jteOutput.writeContent("\n\n                ");
					jteOutput.writeContent("\n                <tr>\n                    ");
					jteOutput.writeContent("\n                    <td>\n                        ");
					jteOutput.setContext("td", null);
					jteOutput.writeUserContent(url.getId());
					jteOutput.writeContent("\n                    </td>\n                    ");
					jteOutput.writeContent("\n                    <td>\n                        <a");
					var __jte_html_attribute_0 = NamedRoutes.urlPath(url.getId());
					if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_0)) {
						jteOutput.writeContent(" href=\"");
						jteOutput.setContext("a", "href");
						jteOutput.writeUserContent(__jte_html_attribute_0);
						jteOutput.setContext("a", null);
						jteOutput.writeContent("\"");
					}
					jteOutput.writeContent(">");
					jteOutput.setContext("a", null);
					jteOutput.writeUserContent(url.getName());
					jteOutput.writeContent("</a>\n                    </td>\n                    ");
					jteOutput.writeContent("\n                    <td>\n                        ");
					jteOutput.setContext("td", null);
					jteOutput.writeUserContent(urlCheck == null ? "" : urlCheck.getCreatedAt().toLocalDateTime().format(formatter));
					jteOutput.writeContent("\n                    </td>\n                    ");
					jteOutput.writeContent("\n                    <td>\n                        ");
					jteOutput.setContext("td", null);
					jteOutput.writeUserContent(urlCheck == null ? "" : String.valueOf(urlCheck.getStatusCode()));
					jteOutput.writeContent("\n                    </td>\n                </tr>\n            ");
				}
				jteOutput.writeContent("\n            </tbody>\n        </table>\n    </div>\n");
			}
		}, page);
		jteOutput.writeContent("\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		UrlsPage page = (UrlsPage)params.get("page");
		render(jteOutput, jteHtmlInterceptor, page);
	}
}
