package de.svws_nrw.transpiler.app;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Klasse für die Java-Tests zum Core-Transpilers
 */
@DisplayName("Teste den Core-Transpiler")
public class TranspilerTest {

	/**
	 * Teste den Typescript-Transpiler mit Test-Klassen
	 *
	 * @throws Exception   eine Exception, die beim Aufruf des Transpilers aufgetreten ist.
	 */
	@Test
	@DisplayName("Teste den Core-Transpiler mit Test-Klassen...")
	void test() throws Exception {
		try {
			// Prüfe, ob das Ausgabe-Verzeichnis für die Tests existiert
			Files.createDirectories(Paths.get("build/tests"));
			// Führe den Transpiler aus
			final String[] params = {
					"--output", "build/tests/ts",
					"--ignore", "de.svws_nrw",
					"--files", "src/test/resources/de/svws_nrw/transpiler/app/transpiler-java-files.txt"
			};
			TranspileTs.main(params);
		} catch (final Exception e) {
			e.printStackTrace();
			fail(e);
			throw e;
		}
	}


	/**
	 * Teste den Generator für den API-Code
	 *
	 * @throws Exception   eine Exception, die beim Aufruf des Transpilers aufgetreten ist.
	 */
	@Test
	@DisplayName("Teste den OpenApi-Generator des TypeScript-Transpilers...")
	void testAPIGenerator() throws Exception {
		try {
			// Prüfe, ob das Ausgabe-Verzeichnis für die Tests existiert
			Files.createDirectories(Paths.get("build/tests"));
			// Führe den Transpiler aus
			final String[] params = {
					"--output", "build/tests/tsapi",
					"--ignore", "de.svws_nrw",
					"--files", "src/test/resources/de/svws_nrw/transpiler/app/transpiler-api-files.txt"
			};
			GenerateTsOpenApi.main(params);
		} catch (final Exception e) {
			e.printStackTrace();
			fail(e);
			throw e;
		}
	}

}
