package de.svws_nrw.api;

import java.io.File;
import java.io.IOException;

import de.svws_nrw.base.FileUtils;
import de.svws_nrw.db.utils.OperationError;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Diese Klasse dient als Cache für Datei-Ressourcen.
 */
public final class ResourceFile {

	/** Der Datei-Pfad dieser Ressource */
	private final String path;

	/** Das Datei-Objekt für diese Ressource */
	private final File file;

	/** Die gecachten Daten dieser Ressource */
	private byte[] cache;

	/** Der Zeitstempel, wann die gecachte Ressource zuletzt verwendet wurde. Dieser dient der Erkennung, ob sich eine Datei verändert hat und deshalb neu geladen werden muss. */
	private long cacheTimestamp;


	/**
	 * Erstellt eine neue Datei-Ressource.
	 *
	 * @param prefix   der Präfix beim Datei-Pfad, der nicht in den Pfad dieser Ressource
	 *                 übernommen werden soll, da der Pfad der Ressource ein relativer Pfad ist.
	 * @param file   das {@link File}-Objekt für den Zugriff auf die Datei-Ressource
	 */
	ResourceFile(final String prefix, final File file) {
		this.file = file;
		final String p = file.getPath().replace('\\', '/');
		this.path = p.substring(prefix.length(), p.length()).replaceFirst("^/", "");
		this.cache = null;
		this.cacheTimestamp = Long.MIN_VALUE;
	}


	/**
	 * Gibt die Daten dieser Ressource zurück. Dabei wird geprüft, ob der
	 * Cache noch gültige Daten besitzt. Ist dies der Fall, so wird der Cache-Inhalt
	 * zurückgegeben. Ist dies nicht der Fall, so wird der Cache durch einen Zugriff
	 * auf die Datei-Ressource aktualisiert.
	 *
	 * @return die Daten dieser Datei-Ressource
	 */
	public byte[] getData() {
		try {
			if ((cache == null) || (cacheTimestamp != file.lastModified())) {
				this.cache = FileUtils.file2ByteArray(file);
				this.cacheTimestamp = file.lastModified();
			}
		} catch (final SecurityException | IOException e) {
			throw OperationError.INTERNAL_SERVER_ERROR.exception(e);
		}
		return this.cache;
	}


	/**
	 * Schreibt die Daten der Datei-Ressource direkt in eine {@link HttpServletResponse}.
	 *
	 * @param response   die HTTP-Response, in welche die Daten der Date-Ressource
	 *                   geschrieben werden sollen
	 *
	 * @throws IOException   diese Exception tritt auf, wenn beim Lesen der Datei-Ressource oder
	 *                       beim Schreiben in die {@link HttpServletResponse} ein Fehler auftritt
	 */
	public void write(final HttpServletResponse response) throws IOException {
		try (ServletOutputStream ostream = response.getOutputStream()) {
			FileUtils.copy(file, ostream);
		}
	}


	/**
	 * Gibt den Pfad der Datei-Ressource zurück.
	 *
	 * @return der Pfad dieser Datei-Ressource
	 */
	public String getPath() {
		return path;
	}


	/**
	 * Gibt den Dateinamen dieser Datei-Ressource zurück.
	 *
	 * @return der Dateiname dieser Datei-Ressource
	 */
	public String getFilename() {
		final int pos = path.lastIndexOf('/') + 1;
		return (pos == 0) ? path : path.substring(pos);
	}

}
