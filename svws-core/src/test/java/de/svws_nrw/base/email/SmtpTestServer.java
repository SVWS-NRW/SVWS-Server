package de.svws_nrw.base.email;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;

/**
 * Ein kleiner SMTP-Server auf einem lokalen Socket für Tests der {@link MailSmtpSession}. Er spricht nur so viel SMTP,
 * wie die Mail-Bibliothek für einen Versand braucht, und kann gezielt hängen bleiben. So lassen sich Fehlerfälle prüfen,
 * die ein echter Mailserver nicht auf Bestellung liefert.
 */
final class SmtpTestServer implements AutoCloseable {

	/** Das Verhalten des Servers während einer Verbindung. */
	enum Verhalten {
		/** Der Server führt einen vollständigen Dialog. */
		NORMAL,
		/** Der Server nimmt die Verbindung an, sendet aber nichts. */
		STUMM,
		/** Der Server bestätigt den DATA-Befehl und liest danach nichts mehr. */
		LIEST_NACH_DATA_NICHT
	}

	private final ServerSocket serverSocket;
	private final Verhalten verhalten;
	private final SSLContext starttlsKontext;
	private final Thread thread;
	private final List<String> befehle = new CopyOnWriteArrayList<>();
	private final List<String> befehleUnverschluesselt = new CopyOnWriteArrayList<>();
	private final List<String> nachrichten = new CopyOnWriteArrayList<>();
	private final CountDownLatch geschlossen = new CountDownLatch(1);
	private volatile String[] protokolle = null;
	private volatile String protokoll = null;

	/**
	 * Startet den Server ohne STARTTLS-Angebot auf einem freien Port der Loopback-Adresse.
	 *
	 * @param verhalten   das Verhalten während einer Verbindung
	 *
	 * @throws IOException   falls der Server-Socket nicht geöffnet werden kann
	 */
	SmtpTestServer(final Verhalten verhalten) throws IOException {
		this(verhalten, null);
	}

	/**
	 * Startet den Server auf einem freien Port der Loopback-Adresse.
	 *
	 * @param verhalten         das Verhalten während einer Verbindung
	 * @param starttlsKontext   der SSL-Kontext für STARTTLS oder null, wenn der Server STARTTLS nicht anbietet
	 *
	 * @throws IOException   falls der Server-Socket nicht geöffnet werden kann
	 */
	SmtpTestServer(final Verhalten verhalten, final SSLContext starttlsKontext) throws IOException {
		this(verhalten, starttlsKontext, new ServerSocket(0, 50, InetAddress.getLoopbackAddress()));
	}

	private SmtpTestServer(final Verhalten verhalten, final SSLContext starttlsKontext, final ServerSocket serverSocket) {
		this.verhalten = verhalten;
		this.starttlsKontext = starttlsKontext;
		this.serverSocket = serverSocket;
		this.thread = Thread.ofPlatform().daemon().name("SmtpTestServer").start(this::bedieneVerbindungen);
	}

	/**
	 * Startet einen Server, der wie ein Mailserver auf Port 465 vom ersten Byte an TLS spricht.
	 *
	 * @param tlsKontext   der SSL-Kontext mit dem Zertifikat des Servers
	 *
	 * @return der gestartete Server
	 *
	 * @throws IOException   falls der Server-Socket nicht geöffnet werden kann
	 */
	static SmtpTestServer mitTls(final SSLContext tlsKontext) throws IOException {
		return new SmtpTestServer(Verhalten.NORMAL, null,
				tlsKontext.getServerSocketFactory().createServerSocket(0, 50, InetAddress.getLoopbackAddress()));
	}

	/**
	 * Gibt den Port zurück, auf dem der Server lauscht.
	 *
	 * @return der Port
	 */
	int getPort() {
		return serverSocket.getLocalPort();
	}

	/**
	 * Gibt die Zeilen zurück, die der Server außerhalb des Nachrichteninhalts empfangen hat.
	 *
	 * @return die empfangenen Befehlszeilen
	 */
	List<String> getBefehle() {
		return List.copyOf(befehle);
	}

	/**
	 * Gibt die Zeilen zurück, die der Server vor dem Wechsel zu TLS und damit im Klartext empfangen hat.
	 *
	 * @return die unverschlüsselt empfangenen Befehlszeilen
	 */
	List<String> getBefehleUnverschluesselt() {
		return List.copyOf(befehleUnverschluesselt);
	}

	/**
	 * Beschränkt die TLS-Versionen, die der Server annimmt.
	 *
	 * @param erlaubt   die erlaubten Versionen, etwa {@code TLSv1.3}
	 */
	void setProtokolle(final String... erlaubt) {
		this.protokolle = erlaubt;
		if (serverSocket instanceof final SSLServerSocket sslServerSocket) {
			sslServerSocket.setEnabledProtocols(erlaubt);
		}
	}

	/**
	 * Gibt die TLS-Version der letzten verschlüsselten Verbindung zurück.
	 *
	 * @return die TLS-Version oder null, wenn noch keine Verbindung verschlüsselt war
	 */
	String getProtokoll() {
		return protokoll;
	}

	/**
	 * Gibt die empfangenen Nachrichten samt Kopfzeilen zurück.
	 *
	 * @return die Nachrichten, je Nachricht ein Text mit CRLF als Zeilenende
	 */
	List<String> getNachrichten() {
		return List.copyOf(nachrichten);
	}

	@Override
	public void close() throws IOException, InterruptedException {
		geschlossen.countDown();
		serverSocket.close();
		thread.join(5000);
	}

	private void bedieneVerbindungen() {
		while (!serverSocket.isClosed()) {
			try (Socket socket = serverSocket.accept()) {
				fuehreDialog(socket);
			} catch (@SuppressWarnings("unused") final IOException | InterruptedException e) {
				// Der Test hat den Server oder der Client die Verbindung geschlossen.
			}
		}
	}

	private void fuehreDialog(final Socket socket) throws IOException, InterruptedException {
		if (verhalten == Verhalten.STUMM) {
			geschlossen.await();
			return;
		}
		BufferedReader in = oeffneEingabe(socket);
		Writer out = oeffneAusgabe(socket);
		boolean verschluesselt = socket instanceof SSLSocket;
		antworte(out, "220 localhost ESMTP SmtpTestServer");
		if (socket instanceof final SSLSocket sslSocket) {
			protokoll = sslSocket.getSession().getProtocol();
		}
		String zeile;
		while ((zeile = in.readLine()) != null) {
			befehle.add(zeile);
			if (!verschluesselt) {
				befehleUnverschluesselt.add(zeile);
			}
			final String befehl = zeile.toUpperCase(Locale.ROOT);
			if (befehl.startsWith("EHLO")) {
				final boolean bieteStarttlsAn = (starttlsKontext != null) && !verschluesselt;
				antworte(out, bieteStarttlsAn ? "250-localhost\r\n250-STARTTLS\r\n250 AUTH PLAIN" : "250-localhost\r\n250 AUTH PLAIN");
			} else if ("STARTTLS".equals(befehl) && (starttlsKontext != null) && !verschluesselt) {
				antworte(out, "220 2.0.0 Ready to start TLS");
				final SSLSocket tlsSocket = (SSLSocket) starttlsKontext.getSocketFactory()
						.createSocket(socket, null, socket.getPort(), true);
				tlsSocket.setUseClientMode(false);
				if (protokolle != null) {
					tlsSocket.setEnabledProtocols(protokolle);
				}
				tlsSocket.startHandshake();
				protokoll = tlsSocket.getSession().getProtocol();
				in = oeffneEingabe(tlsSocket);
				out = oeffneAusgabe(tlsSocket);
				verschluesselt = true;
			} else if ("AUTH PLAIN".equals(befehl)) {
				antworte(out, "334 ");
				befehle.add(in.readLine());
				antworte(out, "235 2.7.0 Authentication successful");
			} else if (befehl.startsWith("AUTH PLAIN ")) {
				antworte(out, "235 2.7.0 Authentication successful");
			} else if ("DATA".equals(befehl)) {
				antworte(out, "354 End data with <CR><LF>.<CR><LF>");
				if (verhalten == Verhalten.LIEST_NACH_DATA_NICHT) {
					geschlossen.await();
					return;
				}
				liesNachricht(in);
				antworte(out, "250 2.0.0 OK");
			} else if ("QUIT".equals(befehl)) {
				antworte(out, "221 2.0.0 Bye");
				return;
			} else {
				antworte(out, "250 2.0.0 OK");
			}
		}
	}

	private static BufferedReader oeffneEingabe(final Socket socket) throws IOException {
		return new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.ISO_8859_1));
	}

	private static Writer oeffneAusgabe(final Socket socket) throws IOException {
		return new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.ISO_8859_1);
	}

	private void liesNachricht(final BufferedReader in) throws IOException {
		final StringBuilder nachricht = new StringBuilder();
		String zeile;
		while (((zeile = in.readLine()) != null) && !".".equals(zeile)) {
			nachricht.append(zeile).append("\r\n");
		}
		nachrichten.add(nachricht.toString());
	}

	private static void antworte(final Writer out, final String antwort) throws IOException {
		out.write(antwort + "\r\n");
		out.flush();
	}

}
