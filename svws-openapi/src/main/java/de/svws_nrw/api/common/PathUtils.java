package de.svws_nrw.api.common;

public final class PathUtils {

	private PathUtils() {
	}

	/**
	 * Die Methode prüft, ob der angegebene Pfad in den Pfad-Spezifikationen enthalten ist.
	 *
	 * @param pathSpecs Pfad-Spezifikationen
	 * @param path      Pfad, der geprüft werden soll
	 *
	 * @return <code>true</code>, falls der Pfad enthalten ist, ansonsten <code>false</code>
	 */
	public static boolean checkIsInPathSpecification(final String[] pathSpecs, final String path) {
		if (path == null)
			return false;
		for (final String pathSpec : pathSpecs)
			if (path.equals(pathSpec) || (pathSpec.endsWith("/*") && (path.equals(pathSpec.substring(0, pathSpec.length() - 2))))
					|| (pathSpec.endsWith("*") && (path.startsWith(pathSpec.substring(0, pathSpec.length() - 1)))))
				return true;
		return false;
	}
}
