package de.nrw.schule.svws.transpiler.app;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Vector;
import java.util.stream.Collectors;

import de.nrw.schule.svws.shell.CommandLineException;
import de.nrw.schule.svws.shell.CommandLineOption;
import de.nrw.schule.svws.shell.CommandLineParser;
import de.nrw.schule.svws.transpiler.Transpiler;
import de.nrw.schule.svws.transpiler.TranspilerException;
import de.nrw.schule.svws.transpiler.typescript.ApiTranspilerTypeScriptPlugin;
import de.nrw.schule.svws.transpiler.typescript.TranspilerTypeScriptPlugin;


/**
 * This class defines the application to transpile the svws-core Java project
 * to TypeScript.
 */
public class CoreTranspiler {

	/** Der Parser für die Kommandozeile */
	private static CommandLineParser cmdLine;


	/**
	 * Starts the transpiler with the above configured input files and output directories
	 * 
	 * @param args   the command line arguments
	 */
	public static void main(String[] args) {
		File[] coreJavaSources = null;
		File[] apiJavaSources = null;
		String tmpDir = null;
		String typeScriptOutputDir = null;
		String typeScriptIgnorePackagePrefix = null;
		cmdLine = new CommandLineParser(args);
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
					String fileNameJavaFiles = cmdLine.getValue("jf", "");
					String data = Files.readString(Paths.get(fileNameJavaFiles));
					tmpJavaFiles = data.split(",");
				} catch (IOException e) {
					tmpJavaFiles = null;
				}
			}
			if ((tmpApiFiles == null) || (tmpApiFiles.length == 0) || ((tmpApiFiles.length == 1) && (tmpApiFiles[0].trim().equals("")))) {
				try {
					String fileNameAPIFiles = cmdLine.getValue("af", "");
					String data = Files.readString(Paths.get(fileNameAPIFiles));
					tmpApiFiles = data.split(",");
				} catch (IOException e) {
					tmpApiFiles = null;
				}
			}
			if ((tmpJavaFiles == null) || (tmpJavaFiles.length == 0) || ((tmpJavaFiles.length == 1) && (tmpJavaFiles[0].trim().equals(""))))
				cmdLine.printOptionsAndExit(1, "Es wurden keine Java-Quellcode-Dateien angegeben.");
			if ((tmpApiFiles == null) || (tmpApiFiles.length == 0) || ((tmpApiFiles.length == 1) && (tmpApiFiles[0].trim().equals(""))))
				cmdLine.printOptionsAndExit(1, "Es wurden keine Api-Dateien angegeben.");
			coreJavaSources = new File[tmpJavaFiles.length];
			for (int i = 0; i < tmpJavaFiles.length; i++) {
				Path p = Paths.get(tmpJavaFiles[i].trim());
				coreJavaSources[i] = p.toFile();
			}
			apiJavaSources = new File[tmpApiFiles.length];
			for (int i = 0; i < tmpApiFiles.length; i++) {
				Path p = Paths.get(tmpApiFiles[i].trim());
				apiJavaSources[i] = p.toFile();
			}
		} catch (CommandLineException e) {
			cmdLine.printOptionsAndExit(1, e.getMessage());
		}

    	// Create a transpiler object with the associated core java source files and use the TS Transpiler Plugin
		Transpiler transpiler = new Transpiler(tmpDir, coreJavaSources);
		
		TranspilerTypeScriptPlugin typescriptPlugin = new TranspilerTypeScriptPlugin(transpiler, typeScriptOutputDir);
		typescriptPlugin.setIgnoreJavaPackagePrefix(typeScriptIgnorePackagePrefix);
		
		transpiler.printSourceFiles();
		transpiler.transpile();
		
    	// Create a transpiler object with the associated api java source files and use the TS API Generator Plugin
// TODO Finish implementing the OpenApi-Plugin after reducing code in Server-API-Classes		
		Transpiler apiTranspiler = new Transpiler(tmpDir, apiJavaSources);
		
		ApiTranspilerTypeScriptPlugin apiGeneratorPlugin = new ApiTranspilerTypeScriptPlugin(apiTranspiler, typeScriptOutputDir);
		apiGeneratorPlugin.setIgnoreJavaPackagePrefix(typeScriptIgnorePackagePrefix);
		
		apiTranspiler.printSourceFiles();
		apiTranspiler.transpile();

		Vector<String> outputs = new Vector<>();
		outputs.addAll(typescriptPlugin.getOutputFiles());
		outputs.addAll(apiGeneratorPlugin.getOutputFiles());
		String strExports = outputs.stream().sorted()
				.map(filename -> {
					String importName = filename.replace(".ts", ""); 
					String classname = importName.replaceFirst(".*/", "");
					return "export { " + classname + " } from './" + importName + "';";
				})
				.collect(Collectors.joining("\n", "", "\n"));
		try {
			Files.writeString(Paths.get(typeScriptOutputDir + "/index.ts"), strExports, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
		} catch (IOException e) {
			throw new TranspilerException("Transpiler Error: Cannot generated index.ts file.");
		}
	}
	
}
