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
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.svws_nrw.core.logger.LogConsumerList;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.types.reporting.ReportingReportvorlage;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Tests der Statusklassifikation des {@link ReportRendererPdf}.
 * <p>Der Ressourcen-Root-Pfad und die Schriftarten liegen im Ressourcenverzeichnis des Servers und werden nie vom Client benannt. Fehlt eine dieser
 * Ressourcen, ist das ein serverseitiges Problem und damit {@code INTERNAL_SERVER_ERROR}: Ein {@code NOT_FOUND} würde behaupten, eine angeforderte
 * fachliche Ressource existiere nicht.</p>
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

	/** Der Logger, den der Renderer in den Tests verwendet. */
	private Logger logger;

	/** Die Liste, die die Einträge des Loggers sammelt. */
	private LogConsumerList log;

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
		logger = new Logger();
		log = new LogConsumerList();
		logger.addConsumer(log);
		renderer = new ReportRendererPdf(logger);
	}


	/**
	 * Gibt die Texte aller Logeinträge mit dem Level ERROR zurück. Die Einrückung, die der Logger dem Text voranstellt, wird dabei entfernt: Geprüft wird
	 * die Meldung, nicht ihre Formatierung.
	 *
	 * @return Die Texte der ERROR-Logeinträge in der Reihenfolge ihres Auftretens.
	 */
	private List<String> fehlermeldungenImLog() {
		return log.getLogData().stream().filter(eintrag -> eintrag.getLevel() == LogLevel.ERROR).map(eintrag -> eintrag.getText().strip()).toList();
	}


	// ##### Fehlende interne Ressourcen #####

	@Test
	void testEinFehlenderRessourcenRootPfadIstEinServerfehler() {
		final ByteArrayOutputStream ausgabe = new ByteArrayOutputStream();
		final ApiOperationException aoe = assertThrows(ApiOperationException.class, () -> renderer.renderPdf(HTML, ROOT_PFAD_UNBEKANNT, ausgabe));
		assertEquals(Status.INTERNAL_SERVER_ERROR, aoe.getStatus());
		assertEquals(List.of("### FEHLER: Der Root-Pfad zu den Ressourcen wurde nicht gefunden. Angegebener Pfad: " + ROOT_PFAD_UNBEKANNT),
				fehlermeldungenImLog(), "Der Fehler muss genau einmal mit dem Level ERROR protokolliert werden.");
	}

	@Test
	void testEineFehlendeSchriftartIstEinServerfehler() {
		// Der Pfad existiert, enthält aber kein Verzeichnis "fonts/liberation" - damit scheitert erst die Registrierung der Schriftarten.
		final ByteArrayOutputStream ausgabe = new ByteArrayOutputStream();
		final ApiOperationException aoe = assertThrows(ApiOperationException.class, () -> renderer.renderPdf(HTML, ROOT_PFAD_OHNE_SCHRIFTARTEN, ausgabe));
		assertEquals(Status.INTERNAL_SERVER_ERROR, aoe.getStatus());
		assertEquals(List.of("### FEHLER: Schriftart nicht gefunden: " + ROOT_PFAD_OHNE_SCHRIFTARTEN + "fonts/liberation/LiberationSans-Regular.ttf"),
				fehlermeldungenImLog(), "Der Fehler muss genau einmal mit dem Level ERROR protokolliert werden.");
	}


	// ##### Erhalt der Ursache bei einem unerwarteten Fehler #####

	@Test
	void testEinFehlerBeimSchreibenNenntSeineUrsache() {
		final OutputStream ausgabe = new FehlerhafterOutputStream();
		final ApiOperationException aoe = assertThrows(ApiOperationException.class, () -> renderer.renderPdf(HTML, ROOT_PFAD, ausgabe));
		assertEquals(Status.INTERNAL_SERVER_ERROR, aoe.getStatus());
		assertNotNull(aoe.getCause(), "Der unerwartete Fehler muss als Ursache erhalten bleiben.");
		assertFalse(fehlermeldungenImLog().isEmpty(), "Der Fehler muss mit dem Level ERROR protokolliert werden.");
	}


	// ##### Gegenprobe: der reguläre Weg #####

	@Test
	void testAusDemHtmlWirdEinPdfErzeugt() {
		final ByteArrayOutputStream ausgabe = new ByteArrayOutputStream();
		renderer.renderPdf(HTML, ROOT_PFAD, ausgabe);
		final byte[] pdf = ausgabe.toByteArray();
		assertTrue(new String(pdf, 0, Math.min(pdf.length, 5), StandardCharsets.ISO_8859_1).startsWith("%PDF"), "Die Ausgabe muss ein PDF-Dokument sein.");
		assertTrue(fehlermeldungenImLog().isEmpty(), "Ein erfolgreicher Lauf darf keine Fehler protokollieren.");
	}

}
