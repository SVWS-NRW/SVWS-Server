package de.svws_nrw.data.email;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import de.svws_nrw.base.email.EmailJob;
import de.svws_nrw.base.email.EmailJobAttachment;
import de.svws_nrw.base.email.EmailJobContext;
import de.svws_nrw.base.email.EmailJobManagerFactory;
import de.svws_nrw.base.email.EmailJobRecipient;
import de.svws_nrw.base.email.EmailJobStatus;
import de.svws_nrw.base.email.EmailUtils;
import de.svws_nrw.base.email.MailSmtpSession;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.email.EmailAttachment;
import de.svws_nrw.core.data.email.EmailSendRequest;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.benutzer.DTOBenutzerMail;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;
import static org.assertj.core.api.Assertions.entry;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Prüft die Regeln für den E-Mail-Versand: Pflichtangaben, Bereinigung der Empfänger und die Anhänge eines API-Auftrags.
 */
@DisplayName("Diese Testklasse testet die Klasse DataEmailSend")
@ExtendWith(MockitoExtension.class)
class DataEmailSendTest {

	private static final String SCHEMA = "DataEmailSendTest";

	/** Der Anfang einer PNG-Datei, an dem Tika den Typ erkennt. */
	private static final byte[] PNG = { (byte) 0x89, 'P', 'N', 'G', '\r', '\n', 0x1A, '\n', 0, 0 };

	/** Der Anfang einer JPEG-Datei, an dem Tika den Typ erkennt. */
	private static final byte[] JPEG = { (byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xE0, 0, 0x10, 'J', 'F', 'I', 'F', 0 };

	@Mock
	private DBEntityManager conn;

	@Mock
	private Benutzer user;

	@InjectMocks
	private DataEmailSend data;

	@AfterEach
	void tearDown() {
		EmailJobManagerFactory.getInstance().freeManager(SCHEMA, 1L);
	}

	private static EmailAttachment attachment(final String filename, final String content) {
		final EmailAttachment attachment = new EmailAttachment();
		attachment.filename = filename;
		attachment.data = content;
		return attachment;
	}

	private static EmailJobAttachment jobAttachment(final String filename) {
		return new EmailJobAttachment(filename, new byte[] { 1 }, "application/pdf");
	}

	private static EmailJobRecipient recipient(final String address, final EmailJobAttachment... attachments) {
		final EmailJobRecipient recipient = new EmailJobRecipient(address);
		recipient.attachments.addAll(Arrays.asList(attachments));
		return recipient;
	}

	private static String base64(final String text) {
		return Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));
	}

	private static void assertBadRequest(final ThrowingRunnable call, final String message) {
		assertThatException()
				.isThrownBy(call::run)
				.isInstanceOf(ApiOperationException.class)
				.withMessage(message)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	/** Ein Aufruf, der eine Exception werfen darf. */
	@FunctionalInterface
	private interface ThrowingRunnable {
		void run() throws Exception;
	}

	@Test
	@DisplayName("send: Ein fehlerhafter Auftrag scheitert, bevor Datenbank oder Job-Manager berührt werden")
	void sendOhneDatenbankzugriffBeiFehler() {
		final EmailSendRequest request = new EmailSendRequest();
		request.subject = "Betreff";
		request.text = "Text";
		request.recipients.addAll(Arrays.asList("keine-adresse", null, " "));
		assertBadRequest(() -> data.send(request), "Für den E-Mail-Versand wurde keine gültige Empfängeradresse übergeben.");
		assertBadRequest(() -> data.send(null), "Für den E-Mail-Versand wurden keine Daten übergeben.");
		request.subject = " ";
		assertBadRequest(() -> data.send(request), "Für die E-Mail fehlt der Betreff oder der Text. Beide sind anzugeben.");
		assertBadRequest(() -> data.send("Betreff", "Text", List.of(recipient("x@smail.de")), new ArrayList<>(), true),
				"Für den E-Mail-Versand wurde keine gültige Empfängeradresse übergeben.");
		verifyNoInteractions(conn);
	}

	@Test
	@DisplayName("send: ein gültiger Auftrag wird eingereiht und an jeden Empfänger einzeln mit dessen Anhängen versendet")
	void sendVersendetJeEmpfaenger() throws Exception {
		when(conn.getDBSchema()).thenReturn(SCHEMA);
		when(conn.getUser()).thenReturn(user);
		when(user.getId()).thenReturn(1L);
		when(conn.queryByKey(DTOBenutzerMail.class, 1L)).thenReturn(new DTOBenutzerMail(1L, " Absender@Schule.de ", "Absender"));
		final MailSmtpSession session = mock(MailSmtpSession.class);
		final EmailJobAttachment a1 = jobAttachment("a1.pdf");
		final EmailJobAttachment a2 = jobAttachment("a2.pdf");
		// Eine Kopie der Anhänge je Empfänger, weil der Manager die Anhänge nach dem Ende des Jobs leert.
		final Map<String, List<EmailJobAttachment>> versand = new ConcurrentHashMap<>();
		doAnswer(invocation -> versand.put(invocation.getArgument(1), List.copyOf(invocation.<List<EmailJobAttachment>>getArgument(4))))
				.when(session).sendTextMessageWithAttachments(anyString(), anyString(), anyString(), anyString(), anyList());
		final Response response;
		try (MockedStatic<DataEmailJobs> jobs = mockStatic(DataEmailJobs.class)) {
			jobs.when(() -> DataEmailJobs.createDefaultJobContext(conn)).thenReturn(new EmailJobContext(session));
			response = data.send(" Betreff ", "Text", List.of(recipient("a@schule.de", a1), recipient("B@schule.de", a2)), new ArrayList<>(), false);
		}

		assertThat(response.getStatus()).isEqualTo(Response.Status.ACCEPTED.getStatusCode());
		final SimpleOperationResponse entity = (SimpleOperationResponse) response.getEntity();
		assertThat(entity.success).isTrue();
		final EmailJob job = EmailJobManagerFactory.getInstance().getManager(SCHEMA, 1L).getJob(entity.id);
		final long ende = System.currentTimeMillis() + 5000;
		while (((job.getStatus() == EmailJobStatus.QUEUED) || (job.getStatus() == EmailJobStatus.SENDING)) && (System.currentTimeMillis() < ende)) {
			Thread.sleep(20);
		}
		assertThat(job.getStatus()).isEqualTo(EmailJobStatus.COMPLETED_SUCCESSFULLY);
		final String body = EmailUtils.createHtmlBody("Text");
		verify(session).sendTextMessageWithAttachments(eq("absender@schule.de"), eq("a@schule.de"), eq("Betreff"), eq(body), anyList());
		verify(session).sendTextMessageWithAttachments(eq("absender@schule.de"), eq("b@schule.de"), eq("Betreff"), eq(body), anyList());
		verifyNoMoreInteractions(session);
		assertThat(versand).containsOnly(entry("a@schule.de", List.of(a1)), entry("b@schule.de", List.of(a2)));
	}

	@Test
	@DisplayName("checkSenderAndSmtpConfig: ohne gültige Absenderadresse scheitert die Prüfung, bevor die SMTP-Konfiguration gelesen wird")
	void checkSenderAndSmtpConfigOhneAbsender() {
		when(conn.getUser()).thenReturn(user);
		when(user.getId()).thenReturn(1L);
		when(conn.queryByKey(DTOBenutzerMail.class, 1L)).thenReturn(new DTOBenutzerMail(1L, "Max <max@schule.de>", "Max"));
		assertBadRequest(() -> data.checkSenderAndSmtpConfig(), "Für den angemeldeten Benutzer ist keine gültige Absender-E-Mail-Adresse hinterlegt. "
				+ "Die E-Mail-Einstellungen des Benutzers sind zu prüfen.");
		verify(user, never()).getAES();
	}

	@Test
	@DisplayName("checkSubjectAndText: Betreff und Text sind Pflicht")
	void checkSubjectAndText() throws ApiOperationException {
		final String message = "Für die E-Mail fehlt der Betreff oder der Text. Beide sind anzugeben.";
		assertBadRequest(() -> DataEmailSend.checkSubjectAndText("Betreff", "  "), message);
		assertBadRequest(() -> DataEmailSend.checkSubjectAndText("", "Text"), message);
		assertBadRequest(() -> DataEmailSend.checkSubjectAndText(null, "Text"), message);
		assertBadRequest(() -> DataEmailSend.checkSubjectAndText("Betreff", null), message);
		DataEmailSend.checkSubjectAndText("Betreff", "Text");
	}

	@Test
	@DisplayName("mergeRecipients: gültige Empfänger bleiben in Reihenfolge, gleiche Adressen werden zusammengefasst, die übrigen vermerkt")
	void mergeRecipients() throws ApiOperationException {
		final EmailJobAttachment a1 = jobAttachment("a1.pdf");
		final EmailJobAttachment a2 = jobAttachment("a2.pdf");
		final EmailJobAttachment a3 = jobAttachment("a3.pdf");
		final List<String> skipped = new ArrayList<>();
		final List<EmailJobRecipient> result = DataEmailSend.mergeRecipients(Arrays.asList(
				recipient(" B@Schule.de ", a1, a2), recipient("a@schule.de", a3), null, recipient("b@schule.de", a2, a3),
				recipient("Max <c@schule.de>", a1), recipient("d@schule.de,e@schule.de", a1), recipient("x@smail.de", a1),
				recipient("gruppe:y@smail.de;", a1)), skipped);
		assertThat(result).extracting(r -> r.email).containsExactly("b@schule.de", "a@schule.de");
		assertThat(result.get(0).attachments).containsExactly(a1, a2, a3);
		assertThat(result.get(1).attachments).containsExactly(a3);
		assertThat(skipped).containsExactly(
				"- Die E-Mail-Adresse 'max <c@schule.de>' ist ungültig und wird übersprungen.",
				"- Die E-Mail-Adresse 'd@schule.de,e@schule.de' ist ungültig und wird übersprungen.",
				"- Die E-Mail an x@smail.de wird nicht versendet, da die Domain als unzulässig markiert ist.",
				"- Die E-Mail-Adresse 'gruppe:y@smail.de;' ist ungültig und wird übersprungen.");
	}

	@Test
	@DisplayName("mergeRecipients: ohne gültigen Empfänger scheitert der Versand")
	void mergeRecipientsOhneGueltigeAdresse() {
		final List<String> skipped = new ArrayList<>();
		assertBadRequest(() -> DataEmailSend.mergeRecipients(List.of(recipient("x@lmail.de")), skipped),
				"Für den E-Mail-Versand wurde keine gültige Empfängeradresse übergeben.");
		assertBadRequest(() -> DataEmailSend.mergeRecipients(List.of(), skipped),
				"Für den E-Mail-Versand wurde keine gültige Empfängeradresse übergeben.");
	}

	@Test
	@DisplayName("createAttachments: PDF, PNG und JPEG sind erlaubt, der MIME-Type folgt aus den Daten")
	void createAttachmentsMimeType() throws ApiOperationException {
		final List<EmailJobAttachment> result = DataEmailSend.createAttachments(List.of(
				attachment(" Einladung.pdf ", base64("%PDF-1.7\n%Inhalt\n")),
				attachment("Bild.PNG", Base64.getEncoder().encodeToString(PNG)),
				attachment("Foto.jpeg", Base64.getEncoder().encodeToString(JPEG))));
		assertThat(result).extracting(a -> a.filename).containsExactly("Einladung.pdf", "Bild.PNG", "Foto.jpeg");
		assertThat(result).extracting(a -> a.mimetype).containsExactly("application/pdf", "image/png", "image/jpeg");
		assertThat(DataEmailSend.createAttachments(null)).isEmpty();
	}

	@Test
	@DisplayName("createAttachments: Anhänge ohne Namen, leer, nicht Base64-kodiert oder zu groß lassen den Auftrag scheitern")
	void createAttachmentsFehler() {
		assertBadRequest(() -> DataEmailSend.createAttachments(List.of(attachment(" ", base64("x")))), "Ein Anhang der E-Mail hat keinen Dateinamen.");
		assertBadRequest(() -> DataEmailSend.createAttachments(List.of(attachment("a.pdf", ""))), "Der Anhang 'a.pdf' ist leer.");
		assertBadRequest(() -> DataEmailSend.createAttachments(List.of(attachment("a.pdf", "kein*base64"))), "Der Anhang 'a.pdf' ist nicht Base64-kodiert.");
		final String half = Base64.getEncoder().encodeToString(new byte[(int) (DataEmailSend.MAX_ATTACHMENT_SIZE / 2) + 1]);
		assertBadRequest(() -> DataEmailSend.createAttachments(List.of(attachment("a.pdf", half), attachment("b.pdf", half))),
				"Die Anhänge sind zusammen größer als 8 MB.");
	}

	@Test
	@DisplayName("createAttachments: die Größe wird vor dem Dekodieren geprüft")
	void createAttachmentsGroesseVorDemDekodieren() {
		// Die Daten sind kein Base64. Meldet die Prüfung trotzdem die Größe, hat sie vor dem Dekodieren gegriffen.
		final String zuGross = "*".repeat((int) (DataEmailSend.MAX_ATTACHMENT_SIZE / 3 * 4) + 8);
		assertBadRequest(() -> DataEmailSend.createAttachments(List.of(attachment("a.pdf", zuGross))), "Die Anhänge sind zusammen größer als 8 MB.");
	}

	@Test
	@DisplayName("createAttachments: Leerraum zählt zur Länge der kodierten Daten, Zeilenumbrüche nach MIME bleiben innerhalb der Grenze")
	void createAttachmentsKodierteLaenge() throws ApiOperationException {
		final String pdf = base64("%PDF-1.7\n%Inhalt\n");
		assertBadRequest(() -> DataEmailSend.createAttachments(List.of(attachment("a.pdf", pdf + " ".repeat((int) DataEmailSend.MAX_ENCODED_LENGTH)))),
				"Die Anhänge sind zusammen größer als 8 MB.");
		final byte[] voll = new byte[(int) DataEmailSend.MAX_ATTACHMENT_SIZE];
		System.arraycopy("%PDF-1.7\n".getBytes(StandardCharsets.US_ASCII), 0, voll, 0, 9);
		assertThat(DataEmailSend.createAttachments(List.of(attachment("a.pdf", Base64.getMimeEncoder().encodeToString(voll))))).hasSize(1);
	}

	@Test
	@DisplayName("decodedLength: ergibt für gültiges Base64 mit und ohne Leerraum die Größe der dekodierten Daten")
	void decodedLength() {
		for (int laenge = 0; laenge < 10; laenge++) {
			final String kodiert = Base64.getMimeEncoder(4, "\r\n".getBytes(StandardCharsets.US_ASCII)).encodeToString(new byte[laenge]);
			assertThat(DataEmailSend.decodedLength(kodiert)).as("Länge %d", laenge).isEqualTo(laenge);
			assertThat(DataEmailSend.decodedLength(kodiert.replaceAll("\\s", ""))).as("Länge %d ohne Leerraum", laenge).isEqualTo(laenge);
		}
		assertThat(DataEmailSend.decodedLength("AAAA" + "=".repeat(100))).isEqualTo(76);
	}

	@Test
	@DisplayName("createAttachments: Dateinamen mit Steuerzeichen, Pfadangaben oder mehr als 255 Bytes werden abgelehnt, ohne den Namen zu wiederholen")
	void createAttachmentsDateiname() throws ApiOperationException {
		final String message = "Der Dateiname eines Anhangs ist zu lang oder enthält Steuerzeichen oder Pfadangaben.";
		final String pdf = base64("%PDF-1.7\n%Inhalt\n");
		for (final String name : List.of("a.pdf\r\nX-Injected: ja\r\n\r\n<html>", "a\u0000.pdf", "a\u0085.pdf", "Rechnung‮fdp.jpg", "a​.pdf",
				"../../etc/a.pdf", "ordner\\a.pdf",
				"a".repeat(DataEmailSend.MAX_FILENAME_LENGTH - 3) + ".pdf", "ä".repeat(126) + ".pdf", "€".repeat(84) + ".pdf")) {
			assertBadRequest(() -> DataEmailSend.createAttachments(List.of(attachment(name, pdf))), message);
		}
		final String laengsterName = "a".repeat(DataEmailSend.MAX_FILENAME_LENGTH - 4) + ".pdf";
		assertThat(DataEmailSend.createAttachments(List.of(attachment(laengsterName, pdf)))).extracting(a -> a.filename).containsExactly(laengsterName);
		// Ein Umlaut belegt in UTF-8 zwei Bytes, der Name hat damit genau die zulässigen 255 Bytes.
		final String laengsterNameMitUmlauten = "a" + "ä".repeat(125) + ".pdf";
		assertThat(DataEmailSend.createAttachments(List.of(attachment(laengsterNameMitUmlauten, pdf)))).extracting(a -> a.filename)
				.containsExactly(laengsterNameMitUmlauten);
	}

	@Test
	@DisplayName("createAttachments: andere Typen als PDF, PNG und JPEG und Inhalte, die nicht zur Endung passen, werden abgelehnt")
	void createAttachmentsDateityp() {
		final String exe = Base64.getEncoder().encodeToString("MZ\u0090\u0000\u0003\u0000\u0000\u0000\u0004\u0000PE".getBytes(StandardCharsets.ISO_8859_1));
		assertBadRequest(() -> DataEmailSend.createAttachments(List.of(attachment("Zeugnis.pdf", exe))),
				"Der Inhalt des Anhangs 'Zeugnis.pdf' passt nicht zu seiner Dateiendung.");
		assertBadRequest(() -> DataEmailSend.createAttachments(List.of(attachment("Bild.pdf", Base64.getEncoder().encodeToString(PNG)))),
				"Der Inhalt des Anhangs 'Bild.pdf' passt nicht zu seiner Dateiendung.");
		assertBadRequest(() -> DataEmailSend.createAttachments(List.of(attachment("login.html", base64("<html><form></form></html>")))),
				"Der Anhang 'login.html' hat keinen zulässigen Dateityp. Erlaubt sind PDF, PNG und JPEG.");
		assertBadRequest(() -> DataEmailSend.createAttachments(List.of(attachment("Zeugnis", base64("%PDF-1.7\n%Inhalt\n")))),
				"Der Anhang 'Zeugnis' hat keinen zulässigen Dateityp. Erlaubt sind PDF, PNG und JPEG.");
	}

	@Test
	@DisplayName("checkSubjectAndText: Betreff und Text haben eine Höchstlänge, ein Betreff nur aus Steuerzeichen fehlt")
	void checkSubjectAndTextLaenge() throws ApiOperationException {
		DataEmailSend.checkSubjectAndText("b".repeat(DataEmailSend.MAX_SUBJECT_LENGTH), "t".repeat(DataEmailSend.MAX_TEXT_LENGTH));
		assertBadRequest(() -> DataEmailSend.checkSubjectAndText("b".repeat(DataEmailSend.MAX_SUBJECT_LENGTH + 1), "Text"),
				"Der Betreff darf höchstens 255 Zeichen lang sein.");
		assertBadRequest(() -> DataEmailSend.checkSubjectAndText("Betreff", "t".repeat(DataEmailSend.MAX_TEXT_LENGTH + 1)),
				"Der Text darf höchstens 100000 Zeichen lang sein.");
		assertBadRequest(() -> DataEmailSend.checkSubjectAndText("\u0000\u0007", "Text"), "Für die E-Mail fehlt der Betreff oder der Text. Beide sind anzugeben.");
	}

	@Test
	@DisplayName("cleanSubject: Steuerzeichen im Betreff werden zu Leerzeichen")
	void cleanSubject() {
		assertThat(DataEmailSend.cleanSubject(" Elternabend\r\nBcc: x@y.de\t\u0000 ")).isEqualTo("Elternabend  Bcc: x@y.de");
		assertThat(DataEmailSend.cleanSubject("Rechnung ‮fdp.exe​")).isEqualTo("Rechnung fdp.exe");
	}

	@Test
	@DisplayName("createAttachments: mehr als 20 Anhänge lassen den Auftrag scheitern")
	void createAttachmentsAnzahl() throws ApiOperationException {
		final List<EmailAttachment> anhaenge = new ArrayList<>();
		for (int i = 0; i < DataEmailSend.MAX_ATTACHMENTS; i++) {
			anhaenge.add(attachment("Bild" + i + ".png", Base64.getEncoder().encodeToString(PNG)));
		}
		assertThat(DataEmailSend.createAttachments(anhaenge)).hasSize(DataEmailSend.MAX_ATTACHMENTS);
		anhaenge.add(attachment("Bild.png", Base64.getEncoder().encodeToString(PNG)));
		assertBadRequest(() -> DataEmailSend.createAttachments(anhaenge), "Eine E-Mail darf höchstens 20 Anhänge haben.");
	}

	@Test
	@DisplayName("mergeRecipients: lange ungültige Adressen werden im Log gekürzt, Unicode-Schreibweisen derselben Domain zusammengefasst und gesperrt")
	void mergeRecipientsLogUndUnicode() throws ApiOperationException {
		final List<String> skipped = new ArrayList<>();
		final List<EmailJobRecipient> result = DataEmailSend.mergeRecipients(List.of(recipient("max@müller.de"),
				recipient("max@xn--mller-kva.de"), recipient("x@ｓmail.de"), recipient("y".repeat(300))), skipped);
		assertThat(result).extracting(r -> r.email).containsExactly("max@müller.de");
		assertThat(skipped).containsExactly(
				"- Die E-Mail an x@ｓmail.de wird nicht versendet, da die Domain als unzulässig markiert ist.",
				"- Die E-Mail-Adresse '" + "y".repeat(100) + "…' ist ungültig und wird übersprungen.");
	}

	@Test
	@DisplayName("send: ein Auftrag mit mehr als 500 Empfängern wird abgelehnt, bevor die Datenbank berührt wird")
	void sendZuVieleEmpfaenger() {
		final EmailSendRequest request = new EmailSendRequest();
		request.subject = "Betreff";
		request.text = "Text";
		for (int i = 0; i <= DataEmailSend.MAX_RECIPIENTS; i++) {
			request.recipients.add("e" + i + "@schule.de");
		}
		assertBadRequest(() -> data.send(request), "Ein Auftrag darf höchstens 500 Empfänger enthalten.");
		verifyNoInteractions(conn);
	}

	@Test
	@DisplayName("send: solange drei Jobs des Benutzers warten oder versendet werden, lehnen API und Reporting weitere Aufträge mit TOO_MANY_REQUESTS ab")
	void sendZuVieleAktiveJobs() throws Exception {
		when(conn.getDBSchema()).thenReturn(SCHEMA);
		when(conn.getUser()).thenReturn(user);
		when(user.getId()).thenReturn(1L);
		when(conn.queryByKey(DTOBenutzerMail.class, 1L)).thenReturn(new DTOBenutzerMail(1L, "absender@schule.de", "Absender"));
		final CountDownLatch gestartet = new CountDownLatch(1);
		final CountDownLatch freigabe = new CountDownLatch(1);
		final MailSmtpSession session = mock(MailSmtpSession.class);
		doAnswer(invocation -> {
			gestartet.countDown();
			return freigabe.await(5, TimeUnit.SECONDS);
		}).when(session).sendTextMessageWithAttachments(anyString(), anyString(), anyString(), anyString(), anyList());
		final EmailSendRequest request = new EmailSendRequest();
		request.subject = "Betreff";
		request.text = "Text";
		request.recipients.add("a@schule.de");
		try (MockedStatic<DataEmailJobs> jobs = mockStatic(DataEmailJobs.class)) {
			jobs.when(() -> DataEmailJobs.createDefaultJobContext(conn)).thenAnswer(invocation -> new EmailJobContext(session));
			for (int i = 0; i < DataEmailSend.MAX_ACTIVE_JOBS; i++) {
				assertThat(data.send(request).getStatus()).isEqualTo(Response.Status.ACCEPTED.getStatusCode());
			}
			// Der Worker steckt im ersten Versand, sonst hinge der Test vom Tempo des Workers ab.
			assertThat(gestartet.await(5, TimeUnit.SECONDS)).isTrue();
			assertThatException()
					.isThrownBy(() -> data.send(request))
					.isInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Response.Status.TOO_MANY_REQUESTS);
			// Ein fehlerhafter Anhang wird gar nicht erst dekodiert, sonst lautete die Antwort BAD_REQUEST.
			request.attachments.add(attachment("a.pdf", "kein*base64"));
			assertThatException()
					.isThrownBy(() -> data.send(request))
					.isInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Response.Status.TOO_MANY_REQUESTS);
			assertThatException()
					.isThrownBy(() -> data.send("Betreff", "Text", List.of(recipient("b@schule.de", jobAttachment("b.pdf"))), new ArrayList<>(), true))
					.isInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Response.Status.TOO_MANY_REQUESTS);
		} finally {
			freigabe.countDown();
		}
	}

}
