package de.svws_nrw.transpiler.typescript;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.Collator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ArrayList;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.Tree;

import de.svws_nrw.transpiler.Transpiler;
import de.svws_nrw.transpiler.TranspilerException;
import de.svws_nrw.transpiler.TranspilerLanguagePlugin;

/**
 * This is the OpenAPI transpiler language plugin for Type Script.
 */
public final class ApiTranspilerTypeScriptPlugin extends TranspilerLanguagePlugin {

	/** The output directory where all generated files should be placed */
	private final String outputDir;

	/** A prefix for the java packages that will be removed while generating the output */
	private String strIgnoreJavaPackagePrefix = "";

	/** the indentation state used during transpilation */
	public int indentC = 0;  // TODO find another solution for indent


	/**
	 * Create a Type Script api transpiler language plugin
	 *
	 * @param transpiler   the transpiler that uses this plugin
	 * @param outputDir    the output directory where all generated files should be placed
	 */
	public ApiTranspilerTypeScriptPlugin(final Transpiler transpiler, final String outputDir) {
		super(transpiler);
		if (outputDir == null)
			throw new TranspilerException("Transpiler Error: An output directory for the transpiler language plugin is required.");
		this.outputDir = outputDir;
	}


	/**
	 * Sets the configuration parameter for the prefix part of the java packages
	 * to be ignored while creating the sub directories for the Type Script output.
	 *
	 * @param prefix   the prefix part of the java package to be ignored
	 */
	public void setIgnoreJavaPackagePrefix(final String prefix) {
		this.strIgnoreJavaPackagePrefix = prefix;
	}





	@Override
	public void transpile() {
		System.out.println("Running TypeScript-API-Transpiler...");

		// Analysiere znächst die Java-Dateien mit den API-Methoden
		final List<ClassTree> allClasses = transpiler.getAllClasses();
		// Eine HashMap zum zuordnen der einzelnen API-Methoden zu den APIs
		final HashMap<String, ArrayList<ApiMethod>> apis = new HashMap<>();
		for (final ClassTree classTree : allClasses) {
			// Beachte nur Klassen. Enums und ähnliches können ignoriert werden
			if (classTree.getKind() != Tree.Kind.CLASS)
				continue;

			// Ermittle die Annotationen der Klasse
			final ApiClassAnnotations classAnnotations = new ApiClassAnnotations(transpiler, classTree);

			// Bestimme die Methoden der API-Definitionen
			final List<? extends Tree> members = classTree.getMembers();
			for (final Tree member : members) {
				// Es müssen nur Methoden beachtet werden.
				if (!((member instanceof final MethodTree method) && (!"<init>".equals(method.getName().toString()))))
					continue;
				// Analsiere die Methode und speichere das Ergebnis der Analyse in der Klasse ApiMethod
				final ApiMethod apiMethod = ApiMethod.getMethod(transpiler, classAnnotations, classTree, method);
				if (apiMethod == null)
					continue;
				System.out.println("    -> " + classTree.getSimpleName().toString() + "." + apiMethod.name);
				// Füge die API-Methode zu der entsprechenden API hinzu
				ArrayList<ApiMethod> apiMethods = apis.get(apiMethod.api);
				if (apiMethods == null) {
					apiMethods = new ArrayList<>();
					apis.put(apiMethod.api, apiMethods);
				}
				apiMethods.add(apiMethod);
			}
		}

		// Schreibe die API in die Typescript-Dateien
		for (final Map.Entry<String, ArrayList<ApiMethod>> entry : apis.entrySet()) {
			// Bestimme den Namen der API und der dazugehörigen API-Klasse
			final String api = entry.getKey();
			final String apiClassName = "Api" + api;

			// Sortiere die API-Methoden anhand des Pfades
			final ArrayList<ApiMethod> apiMethods = entry.getValue();
			apiMethods.sort((a, b) -> {
				final Collator collator = Collator.getInstance(Locale.GERMAN);
				return collator.compare(a.path, b.path);
			});

			// Schreibe den allgemeinen Code der API-Klasse
			String fileData = "export class " + apiClassName + " extends BaseApi {" + System.lineSeparator();
			fileData += System.lineSeparator();

			// Generiere den Konstruktor
			fileData += """
					    \t/**
					    \t *
					    \t * Erstellt eine neue API mit der übergebenen Konfiguration.
					    \t *
					    \t * @param {string} url - die URL des Servers: Alle Pfadangaben sind relativ zu dieser URL
					    \t * @param {string} username - der Benutzername für den API-Zugriff
					    \t * @param {string} password - das Kennwort des Benutzers für den API-Zugriff
					    \t */
					    \tpublic constructor(url : string, username : string, password : string) {
					    \t\tsuper(url, username, password);
					    \t}

					    """;

			// Generiere den Code für die API-Methoden
			for (final ApiMethod apiMethod : apiMethods) {
                System.out.println("    -> " + apiClassName + "." + apiMethod.name);
			    if (apiMethod.isTranspilable()) {
			        fileData += apiMethod.getTSMethod();
			    } else {
			        System.out.println("      Methode kann nicht transpiliert werden -> sie wird ausgelassen...");
			        fileData += "\t// API-Methode " + apiMethod.name + " konnte nicht nach Typescript transpiliert werden" + System.lineSeparator() + System.lineSeparator() + System.lineSeparator();
			    }
			}

			// Schliesse den Code der API-Klasse ab.
			fileData += "}" + System.lineSeparator();

			// Ergänze die benötigten Imports
			final HashMap<String, String> imports = new HashMap<>();
			for (final ApiMethod apiMethod : apiMethods) {
				for (final Map.Entry<String, String> importEntry : apiMethod.getImportsRequired().entrySet()) {
					final String value = imports.get(importEntry.getKey());
					if (value != null) {
						if (!value.equals(importEntry.getValue()))
							throw new TranspilerException("Transpiler Error: Transpiler cannot handle classes in the API with the same name in different packages.");
						continue;
					}
					imports.put(importEntry.getKey(), importEntry.getValue());
				}
			}
			final ArrayList<Map.Entry<String, String>> allImports = new ArrayList<>();
			allImports.addAll(imports.entrySet());
			allImports.sort((a, b) -> {
				final Collator collator = Collator.getInstance(Locale.GERMAN);
				return collator.compare(a.getKey(), b.getKey());
			});
			String fileImports = "import { BaseApi } from '../api/BaseApi';" + System.lineSeparator();
			for (final Map.Entry<String, String> imp : allImports) {
				if (imp.getKey().equals("String"))
					continue;
				final String className = TranspilerTypeScriptPlugin.getImportName(imp.getKey(), imp.getValue());
				final String packageName = TranspilerTypeScriptPlugin.getImportPackageName(imp.getKey(), imp.getValue());
				final String importCast = "cast_" + packageName.replace('.', '_') +  "_" + imp.getKey().replace('.', '_');
				final String importPath = "../" + packageName.replace(strIgnoreJavaPackagePrefix + ".", "").replace('.', '/') + "/";
				final boolean hasClass = fileData.contains(className);
				final boolean hasCast = fileData.contains(importCast);
				if (hasClass && hasCast)
					fileImports += "import { " + className + ", " + importCast + " } from '" + importPath + className + "';" + System.lineSeparator();
				else if (hasClass)
					fileImports += "import { " + className + " } from '" + importPath + className + "';" + System.lineSeparator();
				else if (hasCast)
					fileImports += "import { " + importCast + " } from '" + importPath + className + "';" + System.lineSeparator();
			}
			fileImports += System.lineSeparator();

			// Schreibe den Code der API in
			final String filename = apiClassName + ".ts";
			final Path path = Paths.get(outputDir + "/api/" + filename);
			super.outputFiles.add("api/" + filename);
			try {
				Files.createDirectories(path.getParent());
				Files.writeString(path, fileImports + fileData, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
			} catch (@SuppressWarnings("unused") final IOException e) {
				throw new TranspilerException("Transpiler Error: Cannot write output file " + path.toString());
			}
		}
	}

}
