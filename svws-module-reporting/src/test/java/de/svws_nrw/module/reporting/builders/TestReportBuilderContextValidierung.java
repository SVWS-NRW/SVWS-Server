package de.svws_nrw.module.reporting.builders;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

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
 * <p>Geprüft wird der Status und der Wert, dessen Fehlen die Meldung benennt - nicht ihr Wortlaut. Die Form der Meldung sichert
 * {@code TestArchitekturFehlermeldungen} für alle Meldungen des Moduls; hier ginge sie nur als Wiederholung ein und machte jede spätere Umformulierung
 * zu einer Teständerung.</p>
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
				.withRootPfad(ROOT_PFAD);
	}

	/**
	 * Prüft, dass der übergebene Aufruf einen internen Fehler mit der erwarteten Meldung meldet.
	 *
	 * @param aufruf     Der Aufruf, der fehlschlagen muss.
	 * @param fehlt      Der Wert, dessen Fehlen die Meldung benennen muss.
	 */
	private static void pruefeInternerFehler(final Runnable aufruf, final String fehlt) {
		final ApiOperationException aoe = assertThrows(ApiOperationException.class, aufruf::run);
		assertEquals(Status.INTERNAL_SERVER_ERROR, aoe.getStatus());
		assertInstanceOf(String.class, aoe.getBody(), "Ohne String-Body bliebe die Kopfzeile der Fehlerantwort ohne Abbruchgrund.");
		assertTrue(((String) aoe.getBody()).contains(fehlt),
				"Die Meldung muss '%s' benennen, sonst unterscheidet sie die Prüfungen nicht: %s".formatted(fehlt, aoe.getBody()));
	}


	// ##### HTML-Vorlage #####

	@Test
	void testJedeUnbrauchbareHtmlVorlageIstEinInternerFehler() {
		// Alle drei Formen entstehen aus derselben Quelle: ResourceUtils.text() liefert null, wenn die Datei nicht lesbar ist, und ihren Inhalt, wenn sie
		// leer ist oder nur Leerraum enthält.
		for (final String vorlage : Arrays.asList(null, "", "   ")) {
			pruefeInternerFehler(() -> new ReportBuilderContextHtml().withHtmlTemplate(vorlage),
					"die HTML-Vorlage");
		}
	}

	@Test
	void testEinHtmlKontextOhneVorlageBestehtDieValidierungNicht() {
		final ReportBuilderContextHtml kontext = new ReportBuilderContextHtml()
				.withStatischerDateiname("Bescheinigungen")
				.withRootPfad(ROOT_PFAD);
		pruefeInternerFehler(kontext::validiert, "die HTML-Vorlage");
	}


	// ##### Gerenderter Inhalt und Dateiname der PDF-Ausgabe #####

	@Test
	void testJederUnbrauchbareHtmlInputIstEinInternerFehler() {
		for (final String htmlInput : Arrays.asList(null, "", "   ")) {
			pruefeInternerFehler(() -> new ReportBuilderContextPdf().withHtmlInput(htmlInput), "der HTML-Inhalt");
		}
	}

	@Test
	void testJederUnbrauchbareDateinameIstEinInternerFehler() {
		for (final String dateiname : Arrays.asList(null, "", "   ")) {
			pruefeInternerFehler(() -> new ReportBuilderContextPdf().withDateiname(dateiname), "der Dateiname");
		}
	}


	// ##### Gemeinsame Werte aller Kontexte #####

	@Test
	void testEinLeererRootPfadIstEinInternerFehler() {
		pruefeInternerFehler(() -> new ReportBuilderContextPdf().withRootPfad(""), "der Pfad zu den Ressourcen");
	}

	@Test
	void testEinLeererStatischerDateinameIstEinInternerFehler() {
		pruefeInternerFehler(() -> new ReportBuilderContextPdf().withStatischerDateiname(""),
				"der feste Dateiname");
	}


	// ##### Die abschließende Validierung nie gesetzter Werte #####

	@Test
	void testEinLeererKontextBestehtDieValidierungNicht() {
		// Die Setter weisen leere Werte ab; die Prüfungen in validiert() greifen deshalb nur, wenn ein Wert nie gesetzt wurde. Der statische Dateiname
		// wird zuerst geprüft.
		pruefeInternerFehler(() -> new ReportBuilderContextPdf().validiert(), "der feste Dateiname");
	}

	@Test
	void testEinKontextOhneRootPfadBestehtDieValidierungNicht() {
		final ReportBuilderContextPdf kontext = new ReportBuilderContextPdf().withStatischerDateiname("Bescheinigungen");
		pruefeInternerFehler(kontext::validiert, "der Pfad zu den Ressourcen");
	}

	@Test
	void testEinKontextOhneDateinameBestehtDieValidierungNicht() {
		final ReportBuilderContextPdf kontext = new ReportBuilderContextPdf().withStatischerDateiname("Bescheinigungen").withRootPfad(ROOT_PFAD);
		pruefeInternerFehler(kontext::validiert, "der Dateiname");
	}

	@Test
	void testEinKontextOhneHtmlInhaltBestehtDieValidierungNicht() {
		final ReportBuilderContextPdf kontext = new ReportBuilderContextPdf().withStatischerDateiname("Bescheinigungen").withRootPfad(ROOT_PFAD)
				.withDateiname("Bescheinigung_Meier");
		pruefeInternerFehler(kontext::validiert, "der HTML-Inhalt");
	}


	// ##### Der Content-Type des Builders #####

	@Test
	void testEinFehlenderContentTypeIstEinInternerFehler() {
		// Den Content-Type setzt die Implementierung selbst; er ist damit ebenso interner Zustand wie die Werte des Kontexts. Geprüft wird über eine
		// eigene Ableitung, weil die produktiven Builder ihn fest vorgeben.
		for (final String contentType : Arrays.asList(null, "", "   ")) {
			pruefeInternerFehler(() -> new TestBuilder(vollstaendigerPdfKontext(), contentType),
					"die Angabe des Dateityps");
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
