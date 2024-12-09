package de.svws_nrw.asd.utils.docs;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

import de.svws_nrw.asd.utils.json.JsonReader;
import de.svws_nrw.asd.utils.json.JsonValidatorFehlerartKontextData;
import de.svws_nrw.transpiler.app.CmdLineOption;
import de.svws_nrw.transpiler.app.CmdLineParser;

/**
 * Diese Klasse ist die Startklasse um die Dokumentation der Validatoren aus den JavaDoc-Kommentaren
 * der ValidatorKlassen zu extrahieren und in MD-Dateien abzulegen.
 */
public class ValidatorDocGenerator {

	/**
	 * Leerer Standardkonstruktor.
	 */
	public ValidatorDocGenerator() {
		// leer
	}


	/**
	 * Startet die Erzeugung der Validator-Dokumentation
	 *
	 * @param args   die Argumente der Kommandozeile
	 */
	public static void main(final String[] args) {
		File[] validatorClasses = null;
		String docOutputDir = null;
        String projectDir = null;
		final CmdLineParser cmdLine = new CmdLineParser(args);
		try {
		    // Resourcedatei der Validatoren nutzen als Liste der Validatorklassen
			cmdLine.addOption(new CmdLineOption("o", "output", true, "Der Ziel-Ordner für die erzeugten Dokumentationsdateien"));
			cmdLine.addOption(new CmdLineOption("p", "projectdir", true, "Das Projektverzeichnis, in dem die JAVA-Klassen liegen"));
			docOutputDir = cmdLine.getValue("o", "build/docs/validate");
			projectDir = cmdLine.getValue("p", "src/main/java/");
			final JsonValidatorFehlerartKontextData dataValidatorenFehlerartKontext = JsonReader.fromResourceGetValidatorData("de/svws_nrw/asd/validate/ValidatorenFehlerartKontext.json");
			// Fülle das Array validatorClasses mit den File-Objekten.
			final Set<String> classStrings = dataValidatorenFehlerartKontext.getData().keySet();
			validatorClasses = new File[classStrings.size()];
			int i = 0;
			for (String classname : dataValidatorenFehlerartKontext.getData().keySet()) {
				final Path p = Paths.get((projectDir + classname.replace('.', '/') + ".java").trim());
				validatorClasses[i++] = p.toFile();
			}
		} catch (final IOException e) {
			cmdLine.printOptionsAndExit(1, e.getMessage());
		}


		// Erstelle ein ValidatorJavaDocExtractor-Objekt mit den übergebenen Java-Dateien
		final ValidatorJavaDocExtractor extractor = new ValidatorJavaDocExtractor(validatorClasses);
		extractor.extract();

		// transformiere die extrahierten Kommentare in MD-Notation

	}

}
