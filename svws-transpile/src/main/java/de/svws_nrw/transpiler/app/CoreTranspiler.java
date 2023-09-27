package de.svws_nrw.transpiler.app;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.svws_nrw.base.shell.CommandLineException;
import de.svws_nrw.base.shell.CommandLineOption;
import de.svws_nrw.base.shell.CommandLineParser;
import de.svws_nrw.core.logger.LogConsumerConsole;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.transpiler.Transpiler;
import de.svws_nrw.transpiler.TranspilerException;
import de.svws_nrw.transpiler.typescript.ApiTranspilerTypeScriptPlugin;
import de.svws_nrw.transpiler.typescript.TranspilerTypeScriptPlugin;


/**
 * This class defines the application to transpile the svws-core Java project
 * to TypeScript.
 */
public class CoreTranspiler {

	/**
	 * Starts the transpiler with the above configured input files and output directories
	 *
	 * @param args   the command line arguments
	 */
	public static void main(final String[] args) {
		File[] coreJavaSources = null;
		File[] apiJavaSources = null;
		String tmpDir = null;
		String typeScriptOutputDir = null;
		String typeScriptIgnorePackagePrefix = null;
		final Logger logger = new Logger();
		logger.addConsumer(new LogConsumerConsole());
		final CommandLineParser cmdLine = new CommandLineParser(args, logger);
		try {
			cmdLine.addOption(new CommandLineOption("j", "java", true, "Die Java-Quellcode-Dateien für den Transpiler"));
			cmdLine.addOption(new CommandLineOption("a", "api", true, "Die Quellcode-Dateien für das Generieren des API-Codes"));
			cmdLine.addOption(new CommandLineOption("jf", "javafiles", true, "Eine Textdatei mit einer Liste aller Java-Quellcode-Dateien für den Transpiler"));
			cmdLine.addOption(new CommandLineOption("af", "apifiles", true, "Eine Textdatei mit einer Liste aller Quellcode-Dateien für das Generieren des API-Codes"));
			cmdLine.addOption(new CommandLineOption("o", "output", true, "Der Ziel-Ordner für den erzeugte TypeScript-Code"));
			cmdLine.addOption(new CommandLineOption("t", "tmpdir", true, "Der Ordner für temporäre Dateien, wie z.B. die class-Dateien dey Java-Compilers"));
			cmdLine.addOption(new CommandLineOption("i", "ignore", true, "Das Package-Präfix, welches bei der Ziel-Verzeichnisstruktur ignoriert werden soll"));
			typeScriptOutputDir = cmdLine.getValue("o", "build/ts");
			tmpDir = cmdLine.getValue("t", "build/tmp/transpiler");
			typeScriptIgnorePackagePrefix = cmdLine.getValue("i", "");
			String[] tmpJavaFiles = cmdLine.getValue("j", "").split(",");
			String[] tmpApiFiles = cmdLine.getValue("a", "").split(",");
			if ((tmpJavaFiles == null) || (tmpJavaFiles.length == 0) || ((tmpJavaFiles.length == 1) && (tmpJavaFiles[0].trim().equals("")))) {
				try {
					final String fileNameJavaFiles = cmdLine.getValue("jf", "");
					final String data = Files.readString(Paths.get(fileNameJavaFiles));
					tmpJavaFiles = data.split(",");
				} catch (@SuppressWarnings("unused") final IOException e) {
					tmpJavaFiles = null;
				}
			}
			if ((tmpApiFiles == null) || (tmpApiFiles.length == 0) || ((tmpApiFiles.length == 1) && (tmpApiFiles[0].trim().equals("")))) {
				try {
					final String fileNameAPIFiles = cmdLine.getValue("af", "");
					final String data = Files.readString(Paths.get(fileNameAPIFiles));
					tmpApiFiles = data.split(",");
				} catch (@SuppressWarnings("unused") final IOException e) {
					tmpApiFiles = null;
				}
			}
			if ((tmpJavaFiles == null) || (tmpJavaFiles.length == 0) || ((tmpJavaFiles.length == 1) && (tmpJavaFiles[0].trim().equals("")))) {
				cmdLine.printOptionsAndExit(1, "Es wurden keine Java-Quellcode-Dateien angegeben.");
				return;
			}
			if ((tmpApiFiles == null) || (tmpApiFiles.length == 0) || ((tmpApiFiles.length == 1) && (tmpApiFiles[0].trim().equals("")))) {
				cmdLine.printOptionsAndExit(1, "Es wurden keine Api-Dateien angegeben.");
				return;
			}
			coreJavaSources = new File[tmpJavaFiles.length];
			for (int i = 0; i < tmpJavaFiles.length; i++) {
				final Path p = Paths.get(tmpJavaFiles[i].trim());
				coreJavaSources[i] = p.toFile();
			}
			apiJavaSources = new File[tmpApiFiles.length];
			for (int i = 0; i < tmpApiFiles.length; i++) {
				final Path p = Paths.get(tmpApiFiles[i].trim());
				apiJavaSources[i] = p.toFile();
			}
		} catch (final CommandLineException e) {
			cmdLine.printOptionsAndExit(1, e.getMessage());
		}

    	// Create a transpiler object with the associated core java source files and use the TS Transpiler Plugin
		final Transpiler transpiler = new Transpiler(tmpDir, coreJavaSources);

		final TranspilerTypeScriptPlugin typescriptPlugin = new TranspilerTypeScriptPlugin(transpiler, typeScriptOutputDir);
		typescriptPlugin.setIgnoreJavaPackagePrefix(typeScriptIgnorePackagePrefix);

		transpiler.printSourceFiles();
		transpiler.transpile();

    	// Create a transpiler object with the associated api java source files and use the TS API Generator Plugin
// TODO Finish implementing the OpenApi-Plugin after reducing code in Server-API-Classes
		final Transpiler apiTranspiler = new Transpiler(tmpDir, apiJavaSources);

		final ApiTranspilerTypeScriptPlugin apiGeneratorPlugin = new ApiTranspilerTypeScriptPlugin(apiTranspiler, typeScriptOutputDir);
		apiGeneratorPlugin.setIgnoreJavaPackagePrefix(typeScriptIgnorePackagePrefix);

		apiTranspiler.printSourceFiles();
		apiTranspiler.transpile();

		final ArrayList<String> outputs = new ArrayList<>();
		outputs.addAll(typescriptPlugin.getOutputFiles());
		outputs.addAll(apiGeneratorPlugin.getOutputFiles());
		final ArrayList<Boolean> outputsTypeOnly = new ArrayList<>();
		outputsTypeOnly.addAll(typescriptPlugin.getOutputFilesTypeOnly());
		outputsTypeOnly.addAll(apiGeneratorPlugin.getOutputFilesTypeOnly());

		if (outputs.size() != outputsTypeOnly.size())
			throw new TranspilerException("Transpiler Error: output arrays do not have the same size");

		final Map<String, Boolean> mapTypeOnly = new HashMap<>();
		for (int i = 0; i < outputs.size(); i++)
			mapTypeOnly.put(outputs.get(i), outputsTypeOnly.get(i));

		final StringBuilder sbExports = new StringBuilder();
		for (final String filename : outputs.stream().sorted().toList()) {
			final String importName = filename.replace(".ts", "");
			final String fullclassname = importName.replace('/', '.');
			final String classname = importName.replaceFirst(".*/", "");
			boolean isTypeOnly = mapTypeOnly.get(filename);
			if (TranspilerTypeScriptPlugin.renamedInterfaces.contains(fullclassname))
				isTypeOnly = true;
			sbExports.append("export ");
			if (isTypeOnly)
					sbExports.append("type ");
			if ("BaseApi".equals(classname))
					sbExports.append("{ ").append(classname).append(", type ApiFile } from './").append(importName).append("';").append("\n");
			else
					sbExports.append("{ ").append(classname).append(" } from './").append(importName).append("';").append("\n");
		}
		try {
			Files.writeString(Paths.get(typeScriptOutputDir + "/index.ts"), sbExports.toString(), StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
		} catch (@SuppressWarnings("unused") final IOException e) {
			throw new TranspilerException("Transpiler Error: Cannot generated index.ts file.");
		}
	}

}
