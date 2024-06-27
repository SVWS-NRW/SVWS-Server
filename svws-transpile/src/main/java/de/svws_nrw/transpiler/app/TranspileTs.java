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
import de.svws_nrw.transpiler.typescript.TranspilerTypeScriptPlugin;


/**
 * This class defines the application to transpile the svws-core Java project
 * to TypeScript.
 */
public class TranspileTs {

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
			cmdLine.addOption(new CommandLineOption("f", "files", true, "Eine Textdatei mit einer Liste aller Java-Quellcode-Dateien für den Transpiler"));
			cmdLine.addOption(new CommandLineOption("o", "output", true, "Der Ziel-Ordner für den erzeugte TypeScript-Code"));
			cmdLine.addOption(new CommandLineOption("t", "tmpdir", true, "Der Ordner für temporäre Dateien, wie z.B. die class-Dateien dey Java-Compilers"));
			cmdLine.addOption(new CommandLineOption("i", "ignore", true, "Das Package-Präfix, welches bei der Ziel-Verzeichnisstruktur ignoriert werden soll"));
			typeScriptOutputDir = cmdLine.getValue("o", "build/ts");
			tmpDir = cmdLine.getValue("t", "build/tmp/transpiler");
			typeScriptIgnorePackagePrefix = cmdLine.getValue("i", "");
			final String fileNameJavaFiles = cmdLine.getValue("f", "");
			final String data = Files.readString(Paths.get(fileNameJavaFiles));
			final String[] tmpJavaFiles = data.split(",");
			if ((tmpJavaFiles == null) || (tmpJavaFiles.length == 0) || ((tmpJavaFiles.length == 1) && (tmpJavaFiles[0].trim().equals("")))) {
				cmdLine.printOptionsAndExit(1, "Es wurden keine Java-Quellcode-Dateien angegeben.");
				return;
			}
			javaSources = new File[tmpJavaFiles.length];
			for (int i = 0; i < tmpJavaFiles.length; i++) {
				final Path p = Paths.get(tmpJavaFiles[i].trim());
				javaSources[i] = p.toFile();
			}
		} catch (final IOException e) {
			cmdLine.printOptionsAndExit(1, e.getMessage());
		}

		// Erstelle ein Transpiler-Objekt mit den übergebenen Java-Dateien und Nutze das Typescript-Plugin des Transpilers
		final Transpiler transpiler = new Transpiler(tmpDir, javaSources);
		final TranspilerTypeScriptPlugin typescriptPlugin = new TranspilerTypeScriptPlugin(transpiler, typeScriptOutputDir);
		typescriptPlugin.setIgnoreJavaPackagePrefix(typeScriptIgnorePackagePrefix);
		transpiler.transpile();

		// Schreibe die Datei "index.ts" mit den einzelnen Einträgen der transpilierten Dateien
		try {
			final String strIndexTs = Utils.generateIndexTs(typescriptPlugin);
			Files.createDirectories(Paths.get(typeScriptOutputDir));
			Files.writeString(Paths.get(typeScriptOutputDir + "/index.ts"), strIndexTs, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
		} catch (@SuppressWarnings("unused") final IOException e) {
			throw new TranspilerException("Transpiler Error: Cannot generated index.ts file.");
		}
	}

}
