package de.svws_nrw.transpiler.app;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import de.svws_nrw.base.shell.CommandLineOption;
import de.svws_nrw.base.shell.CommandLineParser;
import de.svws_nrw.core.logger.LogConsumerConsole;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.transpiler.Transpiler;
import de.svws_nrw.transpiler.TranspilerException;
import de.svws_nrw.transpiler.typescript.ApiTranspilerTypeScriptPlugin;


/**
 * This class defines the application to transpile the svws-core Java project
 * to TypeScript.
 */
public class GenerateTsOpenApi {

	/**
	 * Starts the transpiler with the above configured input files and output directories
	 *
	 * @param args   the command line arguments
	 */
	public static void main(final String[] args) {
		File[] javaSources = null;
		String tmpDir = null;
		String typeScriptOutputDir = null;
		String typeScriptIgnorePackagePrefix = null;
		final Logger logger = new Logger();
		logger.addConsumer(new LogConsumerConsole());
		final CommandLineParser cmdLine = new CommandLineParser(args, logger);
		try {
			cmdLine.addOption(new CommandLineOption("f", "files", true, "Eine Textdatei mit einer Liste aller API-Quellcode-Dateien"));
			cmdLine.addOption(new CommandLineOption("o", "output", true, "Der Ziel-Ordner für den erzeugte TypeScript-Code"));
			cmdLine.addOption(new CommandLineOption("t", "tmpdir", true, "Der Ordner für temporäre Dateien, wie z.B. die class-Dateien dey Java-Compilers"));
			cmdLine.addOption(new CommandLineOption("i", "ignore", true, "Das Package-Präfix, welches bei der Ziel-Verzeichnisstruktur ignoriert werden soll"));
			typeScriptOutputDir = cmdLine.getValue("o", "build/tsapi");
			tmpDir = cmdLine.getValue("t", "build/tmp/transpiler");
			typeScriptIgnorePackagePrefix = cmdLine.getValue("i", "");
			final String fileName = cmdLine.getValue("f", "");
			final String data = Files.readString(Paths.get(fileName));
			final String[] tmpFiles = data.split(",");
			if ((tmpFiles == null) || (tmpFiles.length == 0) || ((tmpFiles.length == 1) && (tmpFiles[0].trim().equals("")))) {
				cmdLine.printOptionsAndExit(1, "Es wurden keine Java-Quellcode-Dateien angegeben.");
				return;
			}
			javaSources = new File[tmpFiles.length];
			for (int i = 0; i < tmpFiles.length; i++) {
				final Path p = Paths.get(tmpFiles[i].trim());
				javaSources[i] = p.toFile();
			}
		} catch (final IOException e) {
			cmdLine.printOptionsAndExit(1, e.getMessage());
		}

		// Erstelle ein Transpiler-Objekt mit den übergebenen Java-Dateien und Nutze das API-Typescript-Plugin des Transpilers
		final Transpiler transpiler = new Transpiler(tmpDir, javaSources);
		final ApiTranspilerTypeScriptPlugin apiGeneratorPlugin = new ApiTranspilerTypeScriptPlugin(transpiler, typeScriptOutputDir);
		apiGeneratorPlugin.setIgnoreJavaPackagePrefix(typeScriptIgnorePackagePrefix);
		transpiler.transpile();

		try {
			final String strIndexTs = Utils.generateIndexTs(apiGeneratorPlugin);
			Files.createDirectories(Paths.get(typeScriptOutputDir));
			Files.writeString(Paths.get(typeScriptOutputDir + "/index.ts"), strIndexTs, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
		} catch (@SuppressWarnings("unused") final IOException e) {
			throw new TranspilerException("Transpiler Error: Cannot generated index.ts file.");
		}
	}

}
