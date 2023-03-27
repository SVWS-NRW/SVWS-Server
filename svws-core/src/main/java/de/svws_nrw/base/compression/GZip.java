package de.svws_nrw.base.compression;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Base64;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Diese Klasse stellt Methoden zum Komprimieren und Dekomprimieren
 * von Daten mithilfe des GZip-Verfahrens zur Verfügung.
 */
public class GZip {

	/**
	 * Komprimiert die Daten, welche in den Output-Stream schrieben werden mit dem GZip-Verfahren
	 * 
	 * @param writer   schreibt die Daten in den Output-Stream
	 * 
	 * @return die komprimierten Daten
	 * 
	 * @throws CompressionException   falls ein Fehler beim Komprimieren ensteht
	 */
	public static byte[] encodeData(GZipWriterFunction writer) throws CompressionException {
		try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
			try (GZIPOutputStream gzipOut = new GZIPOutputStream(output)) {
				writer.write(gzipOut);
			}
			return output.toByteArray();
		} catch (Exception e) {
			throw new CompressionException("Fehler beim Komprimieren der Daten. ", e);
		}
	}


	/**
	 * Dekomprimiert die im Input-Stream übergebenen Daten mit dem GZip-Verfahren
	 * 
	 * @param input   der Input-Stream mit den zu dekomprimierenden Daten
	 * 
	 * @return die dekomprimierten Daten
	 * 
	 * @throws CompressionException   falls ein Fehler beim Dekomprimieren auftritt
	 */
	public static byte[] decodeData(InputStream input) throws CompressionException {
		try (GZIPInputStream gzipIn = new GZIPInputStream(input)) {
			return gzipIn.readAllBytes();
		} catch (Exception e) {
			throw new CompressionException("Fehler beim Dekomprimieren der Daten. ", e);
		}
	}
	
	
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
		return encodeData(gzipOut -> gzipOut.write(data));
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
		try (ByteArrayInputStream input = new ByteArrayInputStream(data)) {
			return decodeData(input);
		} catch (Exception e) {
			if (e instanceof CompressionException ce)
				throw ce;
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
