package de.svws_nrw.api.common;

import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

public final class SwaggerUIResourceUtils {

	// Base Pfad zur SwaggerUI Dist Resource
	private static final String RESOURCE_PATH_SWAGGER_UI_DIST = "META-INF/resources/webjars/swagger-ui-dist";

	private SwaggerUIResourceUtils() {
	}

	/**
	 * Die Methode liefert den Pfad zur SwaggerUI Dist Resource, wenn eine Version vorhanden ist. Wenn keine Version gefunden wurde wird <code>null</code>
	 * zurückgegeben.
	 *
	 * @return Pfad zur SwaggerUI Dist Resource
	 */
	public static String getSwaggerUIDistResourcePath() {
		// Ermittle den Pfad zur JAR-Datei
		final URL jarURL = ClassLoader.getSystemResource(RESOURCE_PATH_SWAGGER_UI_DIST);
		if (jarURL == null)
			return null;

		// Versions-Tag ausschneiden und mit dem Base Pfad zusammenfügen
		String version = StringUtils.substringBefore(StringUtils.substringAfter(jarURL.getPath(), "swagger-ui-dist/"), "/");
		if ((version == null) || (version.isBlank()))
			version = StringUtils.substringBefore(StringUtils.substringAfter(jarURL.getPath(), "swagger-ui-dist-"), ".jar!");
		return RESOURCE_PATH_SWAGGER_UI_DIST + '/' + version;
	}

	/**
	 * Die Methode überschreibt in der swagger-initializer.js die Defaultwerte für die Server URLs.
	 *
	 * @param contentStr Inhalt der swagger-initializer.js Datei als String
	 * @param openApiFilePaths Pfade zu den OpenAPI Spezifikationsdateien
	 *
	 * @return überschriebener swagger-initializer.js Inhalt als String
	 */
	public static String customizeSwaggerInitializerJs(final String contentStr, final List<String> openApiFilePaths) {
		// Alle Pfade zu den OpenApi Spezifikationsdateien als Json Array für das Ersetzen des Beispielwerts vorbereiten
		final String replacementUrls = openApiFilePaths.stream()
				.map(path -> "{ url: \"%s\", name: \"%s\" }".formatted(path, extractAPIDefinitionName(path)))
				.collect(Collectors.joining(","));

		// Pfade zur OpenApi Spezifikationsdateien ersetzen
		return contentStr.replace("url: \"https://petstore.swagger.io/v2/swagger.json\"", "urls: [%s]".formatted(replacementUrls));
	}

	/**
	 * Die Methode ermittelt den Namen der API Definition aus dem Pfad zur OpenAPI Spezifikationsdatei.
	 *
	 * @param openApiFilePath Pfad zur OpenAPI Spezifikationsdatei
	 *
	 * @return Name der OpenAPI Definition oder <code>null</code>, wenn <code>null</code> übergeben wurde
	 */
	static String extractAPIDefinitionName(final String openApiFilePath) {
		if (openApiFilePath == null)
			return null;

		String apiNameWithSuffix = openApiFilePath;
		if (openApiFilePath.contains("/"))
			apiNameWithSuffix = StringUtils.substringAfterLast(openApiFilePath, "/");

		return StringUtils.substringBefore(apiNameWithSuffix, ".");
	}
}
