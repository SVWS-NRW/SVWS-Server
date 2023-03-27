package de.svws_nrw.base.compression;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Diese Klasse stellt Methoden zum Komprimieren und Dekomprimieren
 * von Daten mithilfe des GZip-Verfahrens zur Verfügung.
 */
public class GZip {

	/**
	 * Komprimiert die Daten mit dem GZip-Verfahren
	 * 
	 * @param data   die zu komprimierenden Daten
	 * 
	 * @return die komprimierten Daten
	 * 
	 * @throws CompressionException   falls ein Fehler beim Komprimieren ensteht
	 */
	public static byte[] encode(byte[] data) throws CompressionException {
		try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
			try (GZIPOutputStream gzipOut = new GZIPOutputStream(output)) {
				gzipOut.write(data);
			}
			return output.toByteArray();
		} catch (Exception e) {
			throw new CompressionException("Fehler beim Komprimieren der Daten. ", e);
		}
	}

	/**
	 * Dekomprimiert die übergebenen Daten mit dem GZip-Verfahren
	 * 
	 * @param data   die zu dekomprimierenden Daten
	 * 
	 * @return die dekomprimierten Daten
	 * 
	 * @throws CompressionException   falls ein Fehler beim Dekomprimieren auftritt
	 */
	public static byte[] decode(byte[] data) throws CompressionException {
		try (ByteArrayInputStream input = new ByteArrayInputStream(data); GZIPInputStream gzipIn = new GZIPInputStream(input)) {
			return gzipIn.readAllBytes();
		} catch (Exception e) {
			throw new CompressionException("Fehler beim Dekomprimieren der Daten. ", e);
		}
	}
	
	
	/**
	 * Komprimiert die Daten mit dem GZip-Verfahren und erstellt einen Base64-kodierten
	 * String aus dem Ergebnis.
	 * 
	 * @param data   die zu komprimierenden Daten
	 * 
	 * @return die komprimierten Daten als Base64-String
	 * 
	 * @throws CompressionException   falls ein Fehler beim Komprimieren ensteht
	 */
	public static String encodeBase64(byte[] data) throws CompressionException {
		return Base64.getEncoder().encodeToString(encode(data));
	}

	/**
	 * Dekomprimiert die übergebenen Base64-kodierten Daten mit dem GZip-Verfahren
	 * 
	 * @param data   die zu dekomprimierenden Base64-kodierten Daten
	 * 
	 * @return die dekomprimierten Daten
	 * 
	 * @throws CompressionException   falls ein Fehler beim Dekomprimieren auftritt
	 */
	public static byte[] decodeBase64(String data) throws CompressionException {
		return decode(Base64.getDecoder().decode(data));
	}

}
