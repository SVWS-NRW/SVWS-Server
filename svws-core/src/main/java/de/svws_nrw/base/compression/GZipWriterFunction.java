package de.svws_nrw.base.compression;

import java.util.zip.GZIPOutputStream;

/**
 * Stellt ein Funktionales Interface für Lambda-Ausdrücke bereit, welche
 * Daten in einen {@link GZIPOutputStream} schreiben.
 */
@FunctionalInterface
public interface GZipWriterFunction {

	/**
	 * Schreibt Daten in den {@link GZIPOutputStream}.
	 *
	 * @param gzipOut   der {@link GZIPOutputStream}
	 *
	 * @throws Exception   eine beliebige Exception, die im Verlauf des Schreibens der Daten geworfen wird
	 */
	void write(GZIPOutputStream gzipOut) throws Exception;

}
