package de.svws_nrw.transpiler.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.svws_nrw.transpiler.TranspilerException;
import de.svws_nrw.transpiler.TranspilerLanguagePlugin;
import de.svws_nrw.transpiler.typescript.TranspilerTypeScriptPlugin;

/**
 * Hilfsmethoden für die Applikationen des Transpilers
 */
public final class Utils {

	private Utils() {
		/* Eine Instantiierung dieser Klasse ist nicht vorgesehen */
	}


	/**
	 * Generiert die "index.ts" aus den Informationen des Typescript Plugins des Transpilers,
	 * nach dem Lauf des Transpilers
	 *
	 * @param typescriptPlugin   das Plugin
	 *
	 * @return der Inhalt der index.ts
	 */
	public static String generateIndexTs(final TranspilerLanguagePlugin typescriptPlugin) {
		final ArrayList<String> outputs = new ArrayList<>();
		final ArrayList<Boolean> outputsTypeOnly = new ArrayList<>();
		outputs.addAll(typescriptPlugin.getOutputFiles());
		outputsTypeOnly.addAll(typescriptPlugin.getOutputFilesTypeOnly());
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
		return sbExports.toString();
	}

}
