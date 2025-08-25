package de.svws_nrw.api.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import de.svws_nrw.api.OpenApiExternal;
import io.swagger.v3.oas.integration.OpenApiConfigurationException;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class SwaggerUIGeneratorTest {

	@InjectMocks
	private SwaggerUIGenerator cut;

	private Path tempDir;

	@BeforeEach
	void setup() throws IOException {
		tempDir = Files.createTempDirectory(null);
	}

	@AfterEach
	void tearDown() throws IOException {
		FileUtils.deleteDirectory(tempDir.toFile());
	}

	@Test
	void writeOpenAPIJsonAndYamlToOutput() throws OpenApiConfigurationException, IOException {
		cut.writeOpenAPIJsonAndYamlToOutput(new OpenApiExternal(), tempDir, "external", "https://localhost,https://server.de");

		final File[] files = tempDir.toFile().listFiles();
		assertThat(files).isNotNull().hasSize(2);

		final File openApiJsonFile = Arrays.stream(files).filter(f -> f.getName().endsWith(".json")).findFirst().orElse(null);
		final File openApiYamlFile = Arrays.stream(files).filter(f -> f.getName().endsWith(".yaml")).findFirst().orElse(null);

		assertThat(openApiJsonFile).isNotNull().hasFileName("external.json");
		assertThat(Files.readString(openApiJsonFile.toPath())).contains("https://localhost", "https://server.de");
		assertThat(openApiYamlFile).isNotNull().hasFileName("external.yaml");
		assertThat(Files.readString(openApiYamlFile.toPath())).contains("https://localhost", "https://server.de");
	}

	@Test
	void setServersForOpenAPI() throws OpenApiConfigurationException {
		final OpenAPI openApiExternal = new OpenApiExternal().getOpenApiContext().read();
		cut.setServersForOpenAPI(openApiExternal, "https://localhost,https://server.de");

		assertThat(openApiExternal.getServers()).hasSize(2).extracting(Server::getUrl).containsExactly("https://localhost", "https://server.de");
	}

	@Test
	void setServersForOpenAPIWithWhitespaces() throws OpenApiConfigurationException {
		final OpenAPI openApiExternal = new OpenApiExternal().getOpenApiContext().read();
		cut.setServersForOpenAPI(openApiExternal, " https://localhost , https://server.de ");

		assertThat(openApiExternal.getServers()).hasSize(2).extracting(Server::getUrl).containsExactly("https://localhost", "https://server.de");
	}
}
