package de.svws_nrw.module.reporting.builders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.svws_nrw.core.types.reporting.ReportingReportvorlage;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Tests der Statusklassifikation des {@link ReportRendererPdf}.
 * <p>Der Ressourcen-Root-Pfad und die Schriftarten liegen im Ressourcenverzeichnis des Servers und werden nie vom Client benannt. Fehlt eine dieser
 * Ressourcen, ist das ein serverseitiges Problem und damit {@code INTERNAL_SERVER_ERROR}: Ein {@code NOT_FOUND} würde behaupten, eine angeforderte
 * fachliche Ressource existiere nicht.</p>
 * <p>Ein Abbruch wird über Status und Meldung der Exception geprüft: Er hat eine Meldungsquelle - die Meldung der Exception -, und protokolliert wird an
 * der Abschlussgrenze. Der Renderer selbst protokolliert nicht.</p>
 */
class TestReportRendererPdf {

	/** Der produktive Root-Pfad, unter dem die Schriftarten liegen. */
	private static final String ROOT_PFAD = ReportingReportvorlage.getRootPfad();

	/** Ein vorhandener Ressourcenpfad, der kein Unterverzeichnis mit den Schriftarten besitzt. */
	private static final String ROOT_PFAD_OHNE_SCHRIFTARTEN = ROOT_PFAD + "css/";

	/** Ein Ressourcenpfad, den es nicht gibt. */
	private static final String ROOT_PFAD_UNBEKANNT = ROOT_PFAD + "gibt-es-nicht/";

	/** Ein HTML-Inhalt, der sich fehlerfrei rendern lässt. */
	private static final String HTML = "<html><body><p>Testinhalt</p></body></html>";

	/** Der Renderer, dessen Fehlerverhalten geprüft wird. */
	private ReportRendererPdf renderer;


	/** Ein OutputStream, der jeden Schreibzugriff mit einer IOException beantwortet. */
	private static final class FehlerhafterOutputStream extends OutputStream {

		@Override
		public void write(final int b) throws IOException {
			throw new IOException("Das Ziel der PDF-Ausgabe ist nicht beschreibbar.");
		}
	}


	@BeforeEach
	void setUp() {
		renderer = new ReportRendererPdf();
	}


	// ##### Fehlende interne Ressourcen #####

	@Test
	void testEinFehlenderRessourcenRootPfadIstEinServerfehler() {
		final ByteArrayOutputStream ausgabe = new ByteArrayOutputStream();
		final ApiOperationException aoe = assertThrows(ApiOperationException.class, () -> renderer.renderPdf(HTML, ROOT_PFAD_UNBEKANNT, ausgabe));
		assertEquals(Status.INTERNAL_SERVER_ERROR, aoe.getStatus());
		assertEquals("### FEHLER: Das Ressourcenverzeichnis für die PDF-Erzeugung fehlt auf dem Server.", aoe.getBody(),
				"Die Exception trägt den Abbruchgrund als Meldung.");
		assertFalse(aoe.getMessage().contains(ROOT_PFAD_UNBEKANNT), "Der Pfad liegt auf dem Server und sagt dem Aufrufer nichts: " + aoe.getMessage());
	}

	@Test
	void testEineFehlendeSchriftartIstEinServerfehler() {
		// Der Pfad existiert, enthält aber kein Verzeichnis "fonts/liberation" - damit scheitert erst die Registrierung der Schriftarten.
		final ByteArrayOutputStream ausgabe = new ByteArrayOutputStream();
		final ApiOperationException aoe = assertThrows(ApiOperationException.class, () -> renderer.renderPdf(HTML, ROOT_PFAD_OHNE_SCHRIFTARTEN, ausgabe));
		assertEquals(Status.INTERNAL_SERVER_ERROR, aoe.getStatus());
		assertEquals("### FEHLER: Eine Schriftart für die PDF-Erzeugung fehlt auf dem Server.", aoe.getBody(),
				"Die Exception trägt den Abbruchgrund als Meldung.");
	}


	// ##### Erhalt der Ursache bei einem unerwarteten Fehler #####

	@Test
	void testEinFehlerBeimSchreibenNenntSeineUrsache() {
		final OutputStream ausgabe = new FehlerhafterOutputStream();
		final ApiOperationException aoe = assertThrows(ApiOperationException.class, () -> renderer.renderPdf(HTML, ROOT_PFAD, ausgabe));
		assertEquals(Status.INTERNAL_SERVER_ERROR, aoe.getStatus());
		assertNotNull(aoe.getCause(), "Der unerwartete Fehler muss als Ursache erhalten bleiben.");
		assertNotNull(aoe.getMessage(), "Die Exception muss den Abbruchgrund als Meldung tragen.");
		assertFalse(aoe.getMessage().isBlank(), "Die Meldung darf nicht leer sein - sie wird zur Kopfzeile der Fehlerantwort.");
	}


	// ##### Gegenprobe: der reguläre Weg #####

	@Test
	void testAusDemHtmlWirdEinPdfErzeugt() {
		final ByteArrayOutputStream ausgabe = new ByteArrayOutputStream();
		renderer.renderPdf(HTML, ROOT_PFAD, ausgabe);
		final byte[] pdf = ausgabe.toByteArray();
		assertTrue(new String(pdf, 0, Math.min(pdf.length, 5), StandardCharsets.ISO_8859_1).startsWith("%PDF"), "Die Ausgabe muss ein PDF-Dokument sein.");
	}

}
