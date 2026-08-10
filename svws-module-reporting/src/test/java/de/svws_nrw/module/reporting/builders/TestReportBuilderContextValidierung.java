package de.svws_nrw.module.reporting.builders;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.types.reporting.ReportingReportvorlage;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Tests der Eingangsprüfungen von {@link ReportBuilderContext}, {@link ReportBuilderContextHtml} und {@link ReportBuilderContextPdf}.
 * <p>Sämtliche Werte dieser Kontexte setzt der Server selbst: Die HTML-Vorlage stammt aus einer Ressourcendatei, der gerenderte Inhalt aus dem Renderer,
 * der Dateiname aus der Dateinamensvorlage und der Root-Pfad aus einer Konstanten. Fehlt einer davon oder ist er leer, liegt ein interner Fehler vor und
 * kein fehlerhafter Client-Input - die Prüfungen müssen deshalb {@code INTERNAL_SERVER_ERROR} melden.</p>
 * <p>Besonderes Gewicht hat die leere HTML-Vorlage: Eine vorhandene, aber leere Vorlagendatei passiert die Leseprüfung der {@code HtmlFactory} nicht mehr
 * und wird hier als zweite Sicherung abgefangen.</p>
 */
class TestReportBuilderContextValidierung {

	/** Der Root-Pfad, den die Kontexte in den Tests erhalten. */
	private static final String ROOT_PFAD = ReportingReportvorlage.getRootPfad();


	/**
	 * Erzeugt einen vollständig gefüllten PDF-Kontext, an dem einzelne Werte für die Fehlerfälle überschrieben werden.
	 *
	 * @return Der PDF-Kontext.
	 */
	private static ReportBuilderContextPdf vollstaendigerPdfKontext() {
		return new ReportBuilderContextPdf()
				.withHtmlInput("<html><body><p>Testinhalt</p></body></html>")
				.withDateiname("Bescheinigung_Meier")
				.withStatischerDateiname("Bescheinigungen")
				.withRootPfad(ROOT_PFAD)
				.withLogger(new Logger());
	}

	/**
	 * Prüft, dass der übergebene Aufruf einen internen Fehler mit der erwarteten Meldung meldet.
	 *
	 * @param aufruf   Der Aufruf, der fehlschlagen muss.
	 * @param meldung  Die erwartete Meldung.
	 */
	private static void pruefeInternerFehler(final Runnable aufruf, final String meldung) {
		final ApiOperationException aoe = assertThrows(ApiOperationException.class, aufruf::run);
		assertEquals(Status.INTERNAL_SERVER_ERROR, aoe.getStatus());
		assertEquals(meldung, aoe.getBody());
	}


	// ##### HTML-Vorlage #####

	@Test
	void testJedeUnbrauchbareHtmlVorlageIstEinInternerFehler() {
		// Alle drei Formen entstehen aus derselben Quelle: ResourceUtils.text() liefert null, wenn die Datei nicht lesbar ist, und ihren Inhalt, wenn sie
		// leer ist oder nur Leerraum enthält.
		for (final String vorlage : Arrays.asList(null, "", "   ")) {
			pruefeInternerFehler(() -> new ReportBuilderContextHtml().withHtmlTemplate(vorlage),
					"Bei der HTML-Erzeugung darf die HTML-Vorlage nicht leer sein");
		}
	}

	@Test
	void testEinHtmlKontextOhneVorlageBestehtDieValidierungNicht() {
		final ReportBuilderContextHtml kontext = new ReportBuilderContextHtml()
				.withStatischerDateiname("Bescheinigungen")
				.withRootPfad(ROOT_PFAD)
				.withLogger(new Logger());
		pruefeInternerFehler(kontext::validiert, "Die HTML-Vorlage des Report-Builders darf nicht leer sein");
	}


	// ##### Gerenderter Inhalt und Dateiname der PDF-Ausgabe #####

	@Test
	void testJederUnbrauchbareHtmlInputIstEinInternerFehler() {
		for (final String htmlInput : Arrays.asList(null, "", "   ")) {
			pruefeInternerFehler(() -> new ReportBuilderContextPdf().withHtmlInput(htmlInput), "Der HTML-Input des Report-Builders darf nicht leer sein");
		}
	}

	@Test
	void testJederUnbrauchbareDateinameIstEinInternerFehler() {
		for (final String dateiname : Arrays.asList(null, "", "   ")) {
			pruefeInternerFehler(() -> new ReportBuilderContextPdf().withDateiname(dateiname), "Der Dateiname des Report-Builders darf nicht leer sein");
		}
	}


	// ##### Gemeinsame Werte aller Kontexte #####

	@Test
	void testEinLeererRootPfadIstEinInternerFehler() {
		pruefeInternerFehler(() -> new ReportBuilderContextPdf().withRootPfad(""), "Der Root-Pfad des Report-Builders darf nicht leer sein");
	}

	@Test
	void testEinLeererStatischerDateinameIstEinInternerFehler() {
		pruefeInternerFehler(() -> new ReportBuilderContextPdf().withStatischerDateiname(""),
				"Der statische Dateiname des Report-Builders darf nicht leer sein");
	}

	@Test
	void testEinFehlenderLoggerIstEinInternerFehler() {
		pruefeInternerFehler(() -> new ReportBuilderContextPdf().withLogger(null), "Der Logger des Report-Builders darf nicht leer sein");
	}


	// ##### Die abschließende Validierung nie gesetzter Werte #####

	@Test
	void testEinLeererKontextBestehtDieValidierungNicht() {
		// Die Setter weisen leere Werte ab; die Prüfungen in validiert() greifen deshalb nur, wenn ein Wert nie gesetzt wurde. Der statische Dateiname
		// wird zuerst geprüft.
		pruefeInternerFehler(() -> new ReportBuilderContextPdf().validiert(), "Der statische Dateiname des Report-Builders nicht leer sein");
	}

	@Test
	void testEinKontextOhneRootPfadBestehtDieValidierungNicht() {
		final ReportBuilderContextPdf kontext = new ReportBuilderContextPdf().withStatischerDateiname("Bescheinigungen");
		pruefeInternerFehler(kontext::validiert, "Der Root-Pfad des Report-Builders nicht leer sein");
	}

	@Test
	void testEinKontextOhneLoggerBestehtDieValidierungNicht() {
		final ReportBuilderContextPdf kontext = new ReportBuilderContextPdf().withStatischerDateiname("Bescheinigungen").withRootPfad(ROOT_PFAD);
		pruefeInternerFehler(kontext::validiert, "Der Logger des Report-Builders nicht leer sein");
	}


	// ##### Der Content-Type des Builders #####

	@Test
	void testEinFehlenderContentTypeIstEinInternerFehler() {
		// Den Content-Type setzt die Implementierung selbst; er ist damit ebenso interner Zustand wie die Werte des Kontexts. Geprüft wird über eine
		// eigene Ableitung, weil die produktiven Builder ihn fest vorgeben.
		for (final String contentType : Arrays.asList(null, "", "   ")) {
			pruefeInternerFehler(() -> new TestBuilder(vollstaendigerPdfKontext(), contentType),
					"Der Content-Type (MIME-Type) des Report-Builders darf nicht leer sein");
		}
	}

	/** Eine minimale Ableitung, über die sich der Content-Type der Basisklasse prüfen lässt. */
	private static final class TestBuilder extends ReportBuilder<byte[]> {

		/**
		 * Erzeugt den Builder mit dem angegebenen Content-Type.
		 *
		 * @param reportBuilderContext Der Kontext des Builders.
		 * @param contentType          Der zu prüfende Content-Type.
		 */
		private TestBuilder(final ReportBuilderContext<?> reportBuilderContext, final String contentType) {
			super(reportBuilderContext, contentType, "Bescheinigung_Meier");
		}

		@Override
		public String getDateinameMitEndung() {
			return dateiname;
		}

		@Override
		public byte[] generate() {
			return new byte[0];
		}

		@Override
		protected byte[] generateInternalByteArray() {
			return new byte[0];
		}
	}


	// ##### Gegenprobe: der vollständige Kontext #####

	@Test
	void testEinVollstaendigerKontextBestehtDieValidierung() {
		assertDoesNotThrow(() -> vollstaendigerPdfKontext().validiert());
	}

	@Test
	void testEinVollstaendigerKontextErzeugtEinenBuilder() {
		assertDoesNotThrow(() -> new ReportBuilderPdf(vollstaendigerPdfKontext()));
	}

}
