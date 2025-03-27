package de.svws_nrw.base.crypto;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * Diese Klasse stellt Methoden für die Kommunikation mithilfe von SSL/TLS zur Verfügung
 */
public final class TLSUtils {

	private TLSUtils() {
		throw new IllegalStateException("Instantiation not allowed");
	}


	/**
	 * Ein Trust-Manager für TLS-Verbindungen. Diese wird intern verwendet, um die zu überprüfenden
	 * Zertifikate zwischenzuspeichern.
	 */
	private static class InterceptingTrustManager implements X509TrustManager {

		private final X509TrustManager tm;
		private X509Certificate[] chain;

		/**
		 * Erzeugt einen Trust-Manager mit einem anderen Trust-Manager, an welchen
		 * die Überprüfungen weitergeleitet werden.
		 *
		 * @param tm   der Trust-Manager, an den weitergeleitet wird.
		 */
		InterceptingTrustManager(final X509TrustManager tm) {
			this.tm = tm;
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[0];
		}

		@Override
		public void checkClientTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {
			throw new UnsupportedOperationException();
		}

		@Override
		public void checkServerTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {
			this.chain = chain;
			tm.checkServerTrusted(chain, authType);
		}
	}


	/**
	 * Die Methode führt einen TLS-Handshake mit dem Server bei der übergebenen URL durch und gibt die
	 * Zertifikatskette in der übergebebenen Liste zurück. Außerdem wird ein boolean-Wert zurückgegeben,
	 * welcher angibt, ob dem Zertifikat aufgrund der Zertifikatskette vertraut wird. Das Server-Zertifikat
	 * ist das erste Zertifikat in der Kette.
	 *
	 * @param url     die Server-URL mit der der TLS-Handshake durchgeführt werden soll.
	 * @param chain   eine Liste, in der die Zertifikatskette zurückgegeben wird. Diese Liste wird vor dem Befüllen geleert.
	 *
	 * @return true, wenn dem Server-Zertifikat vom Default-Keystore vertraut wird. Ansonsten false
	 *
	 * @throws SSLException   im Fehlerfall
	 */
	public static boolean queryServerCertificates(final String url, final List<X509Certificate> chain) throws SSLException {
		// Bestimme den Host und den Port anhand der URL
		final URI uri = URI.create(url);
		final String host = uri.getHost();
		int port = uri.getPort();
		if (port < 1) {
			port = switch (uri.getScheme()) {
				case "http" -> 80;
				case "https" -> 443;
				default -> 443;
			};
		}

		// Erstelle eine SSLSocketFactory und einen Trust-Manager, welcher das Server-Zertifikat abgreift
		final SSLSocketFactory factory;
		final InterceptingTrustManager trustManager;
		try {
			// Erzeuge einen Trust-Manager mit dem Default-Keystore, welcher die Zertifikatskette zwischenspeichert
			final TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			trustManagerFactory.init((KeyStore) null);
			trustManager = new InterceptingTrustManager((X509TrustManager) trustManagerFactory.getTrustManagers()[0]);

			// Erzeuge einen SSL-Kontext für TLS und verknüpfe diesen mit dem Trust-Manager
			final SSLContext context = SSLContext.getInstance("TLS");
			context.init(null, new InterceptingTrustManager[] { trustManager }, null);
			factory = context.getSocketFactory();
		} catch (final NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
			throw new SSLException("Fehler beim Erstellen der SSLSocketFactory", e);
		}

		// Starte einen Handshake, um das Zertifikat des Servers zu bestimmen. Wenn eine Exception auftritt,
		// dann wird dem Server nicht vertraut oder dieser ist nicht erreichbar
		boolean isTrusted;
		try (SSLSocket socket = (SSLSocket) factory.createSocket(host, port)) {
			socket.setSoTimeout(10000);
			socket.startHandshake();
			isTrusted = true;
		} catch (@SuppressWarnings("unused") final IOException e) {
			isTrusted = false;
		}

		// Lese die Zertifikatskette aus
		if (trustManager.chain == null)
			throw new SSLException("Die Server-Zertifikate konnten bei dem Handshake nicht bestimmt werden.");
		chain.clear();
		chain.addAll(Arrays.asList(trustManager.chain));
		return isTrusted;
	}


	/**
	 * Die Methode führt einen TLS-Handshake mit dem Server bei der übergebenen URL durch und gibt die
	 * Zertifikatskette in der übergebebenen Liste zurück. Außerdem wird ein boolean-Wert zurückgegeben,
	 * welcher angibt, ob dem Zertifikat aufgrund der Zertifikatskette vertraut wird. Das Server-Zertifikat
	 * ist das erste Zertifikat in der Kette.
	 *
	 * @param url     die Server-URL mit der der TLS-Handshake durchgeführt werden soll.
	 * @param chain   eine Liste, in der die Zertifikatskette Base64 kodiert zurückgegeben wird. Diese Liste
	 *                wird vor dem Befüllen geleert.
	 *
	 * @return true, wenn dem Server-Zertifikat vom Default-Keystore vertraut wird. Ansonsten false
	 *
	 * @throws SSLException   im Fehlerfall
	 */
	public static boolean queryServerCertificatesBase64(final String url, final List<String> chain) throws SSLException {
		final List<X509Certificate> chainX509 = new ArrayList<>();
		final boolean isTrusted = queryServerCertificates(url, chainX509);
		try {
			for (final X509Certificate c : chainX509)
				chain.add(encodeCertFileBase64(c));
		} catch (final CertificateException e) {
			throw new SSLException("Fehler bei der Umwandlung der X509-Zertifikate nach Base64", e);
		}
		return isTrusted;
	}


	/**
	 * Wandelt das übergebene X.509-Zertifikat in einen Base64-kodierten String um.
	 *
	 * @param cert   das X.509-Zertifikat
	 *
	 * @return das Base64-kodierte Zertifikat
	 *
	 * @throws CertificateException   falls ein Fehler beim Umwandeln auftritt
	 */
	public static String encodeCertBase64(final X509Certificate cert) throws CertificateException {
		try {
			return Base64.getEncoder().encodeToString(cert.getEncoded());
		} catch (final CertificateEncodingException e) {
			throw new CertificateException("Das Zertifikat kann nicht serialisiert werden.", e);
		}
	}


	/**
	 * Wandelt das übergebene als Base64-kodierter String vorliegende Zertifikat in ein X.509-Zertifikat um.
	 *
	 * @param base64   das Base64-kodierte Zertifikat
	 *
	 * @return das X.509-Zertifikat
	 *
	 * @throws CertificateException   falls ein Fehler beim Umwandeln auftritt
	 */
	public static X509Certificate decodeCertBase64(final String base64) throws CertificateException {
		final byte[] certRaw = Base64.getDecoder().decode(base64);
		final X509Certificate certDecoded;
		try {
			final CertificateFactory cf = CertificateFactory.getInstance("X.509");
			certDecoded = (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(certRaw));
		} catch (final CertificateException e) {
			throw new CertificateException("Fehler beim Dekodieren des Zertifikats.", e);
		}
		return certDecoded;
	}


	/**
	 * Wandelt das übergebene X.509-Zertifikat in ein Base64-kodiertes Dateiformat um.
	 *
	 * @param cert   das X.509-Zertifikat
	 *
	 * @return das Base64-kodierte Zertifikat
	 *
	 * @throws CertificateException   falls ein Fehler beim Umwandeln auftritt
	 */
	public static String encodeCertFileBase64(final X509Certificate cert) throws CertificateException {
		try {
			return "-----BEGIN CERTIFICATE-----\n" + Base64.getMimeEncoder().encodeToString(cert.getEncoded()) + "\n-----END CERTIFICATE-----\n";
		} catch (final CertificateEncodingException e) {
			throw new CertificateException("Das Zertifikat kann nicht serialisiert werden.", e);
		}
	}


	/**
	 * Wandelt das übergebene als im Base64-kodierten Dateiformat vorliegende Zertifikat in ein X.509-Zertifikat um.
	 *
	 * @param base64   das Base64-kodierte Zertifikat
	 *
	 * @return das X.509-Zertifikat
	 *
	 * @throws CertificateException   falls ein Fehler beim Umwandeln auftritt
	 */
	public static X509Certificate decodeCertFileBase64(final String base64) throws CertificateException {
		String[] tmp = base64.split("-----BEGIN CERTIFICATE-----\r*\n*");
		if (tmp.length != 2)
			throw new IllegalArgumentException("Das Zertifikat kann nicht eingelesen werden. Überprüfen sie das Dateiformat.");
		tmp = tmp[1].split("\r*\n*-----END CERTIFICATE-----");
		if (tmp.length != 2)
			throw new IllegalArgumentException("Das Zertifikat kann nicht eingelesen werden. Überprüfen sie das Dateiformat.");
		final byte[] certRaw = Base64.getMimeDecoder().decode(tmp[0]);
		final X509Certificate certDecoded;
		try {
			final CertificateFactory cf = CertificateFactory.getInstance("X.509");
			certDecoded = (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(certRaw));
		} catch (final CertificateException e) {
			throw new CertificateException("Fehler beim Dekodieren des Zertifikats.", e);
		}
		return certDecoded;
	}


	/**
	 * Wandelt die übergebene X.509-Zertifikate der Liste jeweils in einen Base64-kodierten String um und gibt das
	 * Ergebnis in einem JSON-Array zurück.
	 *
	 * @param certs   die Liste der X.509-Zertifikate
	 *
	 * @return das JSON-Array mit den Base64 kodierten Zertifikaten
	 *
	 * @throws CertificateException   falls ein Fehler beim Umwandeln auftritt
	 */
	public static String encodeCertListJson(final List<X509Certificate> certs) throws CertificateException {
		boolean first = true;
		final StringBuilder result = new StringBuilder("[");
		for (final X509Certificate cert : certs) {
			if (first)
				first = false;
			else
				result.append(",");
			result.append("\"").append(encodeCertBase64(cert)).append("\"");
		}
		result.append("]");
		return result.toString();
	}


	/**
	 * Wandelt die als JSON-Array in Base64-kodierte in einem String vorliegende Zertifikatsliste in eine
	 * Liste von X.509-Zertifikaten um.
	 *
	 * @param json   das als String vorliegende JSON-Array von Base64-kodierten Zertifikaten
	 *
	 * @return die Liste von X.509-Zertifikaten
	 *
	 * @throws CertificateException   falls ein Fehler beim Umwandeln auftritt
	 */
	public static List<X509Certificate> decodeCertListJson(final String json) throws CertificateException {
		final List<X509Certificate> list = new ArrayList<>();
		final String[] tmpCerts = json.replace("[", "").replace("]", "").trim().split(",");
		for (final String tmpCert : tmpCerts) {
			final String base64 = tmpCert.trim().replace("\"", "");
			final X509Certificate cert = decodeCertBase64(base64);
			list.add(cert);
		}
		return list;
	}


	/**
	 * Erstellt für den übergebenen Keytore einen Trust-Manager und initialisiert damit einen neuen SSL-Kontext
	 *
	 * @param keystore   der Keystore
	 *
	 * @return der SSL-Kontext für das TLS-Protokoll
	 *
	 * @throws GeneralSecurityException   im Fehlerfall
	 */
	public static SSLContext getTLSContextFromKeystore(final KeyStore keystore) throws GeneralSecurityException {
		final TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		tmf.init(keystore);
		SSLContext sslContext = SSLContext.getInstance("TLS");
		sslContext.init(null, tmf.getTrustManagers(), new SecureRandom());
		return sslContext;
	}


}
