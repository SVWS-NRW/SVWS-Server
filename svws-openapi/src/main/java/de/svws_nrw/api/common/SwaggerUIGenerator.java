package de.svws_nrw.api.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import de.svws_nrw.api.OpenApiDefinition;
import de.svws_nrw.api.OpenApiExternal;
import de.svws_nrw.api.OpenApiSchemaRoot;
import de.svws_nrw.api.OpenApiServer;
import io.swagger.v3.oas.integration.OpenApiConfigurationException;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.apache.commons.io.IOUtils;

/**
 * Diese Klasse generiert alle Ressourcen, die für die Auslieferung einer statischen SwaggerUI Seite benötigt werden. Dabei werden mehrere APIs gleichzeitig
 * in der SwaggerUI inklusive der Json Spezifikation Dateien bereitgestellt.
 */
public class SwaggerUIGenerator {

	// Pfad zum SwaggerUI Dist Package
	private final String swaggerUiDistResourcePath;

	/**
	 * Konstruktor für die Erstellung des SwaggerUI Generators.
	 */
	SwaggerUIGenerator() {
		swaggerUiDistResourcePath = SwaggerUIResourceUtils.getSwaggerUIDistResourcePath();
		Objects.requireNonNull(swaggerUiDistResourcePath);
	}

	/**
	 * Liefert den Pfad zum SwaggerUI Dist Verzeichnis der JAR Ressourcen.
	 *
	 * @return SwaggerUI Dist Ressourcen Pfad
	 */
	String getSwaggerUiDistResourcePath() {
		return swaggerUiDistResourcePath;
	}

	/**
	 * Main-Methode des Generators. <br>
	 * Args (String[]):<br>
	 * 1. Argument: Output Verzeichnis, der SwaggerUI Files <br>
	 * 2. Argument: Kommagetrennte Liste mit Servern, die in der SwaggerUI ausgewählt werden können
	 *
	 * @param args Parameter für den Start des Generators
	 */
	public static void main(final String[] args) throws IOException, OpenApiConfigurationException {
		if (args.length != 2)
			throw new IllegalArgumentException("Es fehlen benötigte Startparameter.");

		final String outputPathArg = args[0];
		final String serverUrlsArg = args[1];
		final Path outputPath = Path.of(outputPathArg);

		final SwaggerUIGenerator generator = new SwaggerUIGenerator();

		// Prüfe den Pfad zur JAR-Datei
		final URL jarURL = ClassLoader.getSystemResource(generator.getSwaggerUiDistResourcePath());
		if (jarURL == null)
			throw new FileNotFoundException("SwaggerUI Ressource-Verzeichnis nicht gefunden.");

		// URI korrekt auflösen
		final String pathToJarDir = URLDecoder.decode(jarURL.getPath().substring("file:".length(), jarURL.getPath().indexOf("!")), StandardCharsets.UTF_8);

		// Hier werden alle API Ressourcen definiert, die in der SwaggerUI enthalten/auswählbar sein sollen
		final List<SwaggerAPIExport> swaggerAPIExportExports = generator.getSwaggerAPIsToExport();

		// Schreibe alle benötigten SwaggerUI Ressourcen aus der SwaggerUI Package Jar in das angegebene Output Verzeichnis
		generator.writeJarResourcesToOutput(swaggerAPIExportExports, pathToJarDir, outputPath);

		// Generiere und schreibe für jede OpenAPI die benötigten Spezifikationsdateien in das Output Verzeichnis
		for (final SwaggerAPIExport output : swaggerAPIExportExports) {
			generator.writeOpenAPIJsonAndYamlToOutput(output.definition, outputPath, output.name, serverUrlsArg);
		}
	}

	/**
	 * Die Methode liefert alle APIs die in der statischen SwaggerUI verfügbar sein sollen.
	 *
	 * @return Liste mit APIs die in der SwaggerUI verfügbar sein sollen
	 */
	private List<SwaggerAPIExport> getSwaggerAPIsToExport() {
		return List.of(new SwaggerAPIExport(new OpenApiServer(), "server"),
				new SwaggerAPIExport(new OpenApiSchemaRoot(), "privileged"),
				new SwaggerAPIExport(new OpenApiExternal(), "external"));
	}

	/**
	 * Die Methode schreibt alle Ressourcen aus der JAR Datei in das angegebene Output Verzeichnis.
	 *
	 * @param swaggerAPIExports Output Liste, für die jeweils OpenAPI Spezifikationsdateien generiert werden sollen
	 * @param jarPath Pfad zur JAR Datei
	 * @param outputDir Output Verzeichnis
	 *
	 * @throws IOException Fehler beim Schreiben der Ressource
	 */
	private void writeJarResourcesToOutput(final List<SwaggerAPIExport> swaggerAPIExports, final String jarPath, final Path outputDir)
			throws IOException {
		try (JarFile jar = new JarFile(jarPath)) {
			Files.createDirectories(outputDir);

			// Dateien in der Jar-Datei durchlaufen
			for (final JarEntry jarEntry : jar.stream().toList()) {
				// Nur Dateien im gewünschten Verzeichnis übernehmen
				if (!jarEntry.getName().startsWith(Objects.requireNonNull(swaggerUiDistResourcePath)) || jarEntry.isDirectory())
					continue;

				writeJarResourcesToOutput(jar, jarEntry, outputDir, swaggerAPIExports);
			}
		}
	}

	/**
	 * Die Methode schreibt eine Ressource aus der JAR Datei in das angegebene Output Verzeichnis und ersetzt dabei ggf. interne Default-Werte.
	 *
	 * @param jar JAR Datei
	 * @param jarEntry Eine Ressource aus der JAR Datei
	 * @param outputDir Output Verzeichnis
	 * @param exports Liste mit OpenAPI Spezifikationen die in der SwaggerUI verfügbar sein sollen
	 *
	 * @throws IOException Fehler beim Schreiben einer Ressource
	 */
	private void writeJarResourcesToOutput(final JarFile jar, final JarEntry jarEntry, final Path outputDir, final List<SwaggerAPIExport> exports)
			throws IOException {
		final String resourceName = jarEntry.getName().substring(Objects.requireNonNull(swaggerUiDistResourcePath).length() + 1);
		final File outputResource = outputDir.resolve(resourceName).toFile();

		// Ressource aus Jar als InputStream holen
		final InputStream inputStream = jar.getInputStream(jarEntry);

		if (jarEntry.getName().endsWith("swagger-initializer.js")) {
			final List<String> openApiFilePaths = exports.stream().map(e -> e.name.concat(".json")).toList();
			Files.writeString(outputResource.toPath(),
					SwaggerUIResourceUtils.customizeSwaggerInitializerJs(IOUtils.toString(inputStream, StandardCharsets.UTF_8), openApiFilePaths));
		} else {
			Files.write(outputResource.toPath(), inputStream.readAllBytes());
		}
	}

	/**
	 * Die Methode erzeugt die OpenAPI JSON- und YAML-Dateien und verschiebt diese in das passende Verzeichnis.
	 *
	 * @param openApiDefinition OpenApiContext
	 * @param outputPath Verzeichnis in das die Dateien abgelegt werden
	 * @param name Name der OpenAPI JSON- und YAML-Dateien ohne Dateiendung
	 * @param serverUrls auswählbare Server URLs in der SwaggerUI
	 *
	 * @throws IOException Fehler beim Erzeugen der OpenAPI Files
	 */
	void writeOpenAPIJsonAndYamlToOutput(final OpenApiDefinition openApiDefinition, final Path outputPath, final String name,
			final String serverUrls) throws IOException, OpenApiConfigurationException {
		final OpenAPI openAPI = openApiDefinition.getOpenApiContext().read();
		setServersForOpenAPI(openAPI, serverUrls);

		// Schreibe OpenAPI Files in Output Verzeichnis
		final String openApiJsonData = openApiDefinition.getOpenApiFile("json").readEntity(String.class);
		final String openApiYamlData = openApiDefinition.getOpenApiFile("yaml").readEntity(String.class);
		Files.writeString(outputPath.resolve(name.concat(".json")), openApiJsonData);
		Files.writeString(outputPath.resolve(name.concat(".yaml")), openApiYamlData);
	}

	/**
	 * Die Methode fügt der OpenAPI Spezifikation die verfügbaren Server URLs hinzu, die auch später für Test Requests in der SwaggerUI ausgewählt werden
	 * können.
	 *
	 * @param openAPI OpenAPI Objekt
	 * @param serverUrls verfügbare Server URLs
	 */
	void setServersForOpenAPI(final OpenAPI openAPI, final String serverUrls) {
		final List<Server> servers = Arrays.stream(serverUrls.split(","))
				.map(String::trim)
				.map(serverUrl -> new Server().url(serverUrl))
				.toList();
		openAPI.setServers(servers);
	}

	/**
	 * Klasse definiert eine API, die als Spezifikation über die SwaggerUI ausgeliefert werden soll.
	 *
	 * @param definition OpenApiDefinition
	 * @param name Name der OpenAPI Spezifikation
	 */
	private record SwaggerAPIExport(OpenApiDefinition definition, String name) {
	}
}
