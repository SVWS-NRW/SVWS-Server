package de.svws_nrw.module.reporting.types.schule;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.junit.jupiter.api.Test;
import org.thymeleaf.context.Context;

import de.svws_nrw.core.types.reporting.ReportingBildDefinition;
import de.svws_nrw.module.reporting.builders.ReportBuilderUtils;

/**
 * Tests des Bildes aus der Logoverwaltung und seines Zugangs aus den Report-Vorlagen.
 * Der zweite Teil rendert kleine Vorlagen mit der echten Template-Engine, denn die Vorlagen sprechen die Bilddefinition
 * über den Enum an. Ein Fehler in dieser Angabe fiele sonst erst beim Druck auf.
 */
class TestReportingBild {

	/** Ein gültiges PNG im Base64-Format, an dessen Kopfdaten der MIME-Type erkannt wird. */
	private static final String PNG_BASE64 =
			"iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8z8BQDwAEhQGAhKmMIQAAAABJRU5ErkJggg==";

	/** Die Bildquelle, die zu {@link #PNG_BASE64} erwartet wird. */
	private static final String PNG_BILDQUELLE = "data:image/png;base64," + PNG_BASE64;

	/** Die Bilddefinition des Briefkopfs, so wie eine Vorlage sie anspricht. */
	private static final String AUSDRUCK_BRIEFKOPF = "@de.svws_nrw.core.types.reporting.ReportingBildDefinition@DIN5008_BRIEFKOPF";


	/**
	 * Erzeugt eine Schule ohne Daten. Ihre Bild-Methode ist die der Basisklasse und liefert deshalb kein Bild.
	 *
	 * @return Die Schule.
	 */
	private static ReportingSchule schuleOhneBriefkopf() {
		return new ReportingSchule(null, null, null, null, null, null, 0, null, null, null, null, 0, 0, null, null, null, 0, null, null, null, null,
				null);
	}

	/**
	 * Erzeugt eine Schule, die zu jeder Bilddefinition das übergebene Bild liefert. Sie tritt an die Stelle der Proxy-Klasse, die dafür eine
	 * Datenbankverbindung benötigte.
	 *
	 * @param base64 Das Bild im Base64-Format.
	 *
	 * @return Die Schule.
	 */
	private static ReportingSchule schuleMitBriefkopf(final String base64) {
		return new ReportingSchule(null, null, null, null, null, null, 0, null, null, null, null, 0, 0, null, null, null, 0, null, null, null, null,
				null) {
			@Override
			public ReportingBild bild(final ReportingBildDefinition bildDefinition) {
				return new ReportingBild(bildDefinition, base64);
			}
		};
	}

	/**
	 * Rendert den übergebenen Ausdruck mit der Vorlagen-Variablen "Schule" und gibt den ausgegebenen Text zurück.
	 *
	 * @param ausdruck Der Ausdruck, den die Vorlage auswertet.
	 * @param schule   Die Schule, die der Vorlage unter dem Namen "Schule" bereitsteht.
	 *
	 * @return Der ausgegebene Text oder das gesamte Ergebnis, falls die erwartete Ausgabestelle fehlt.
	 */
	private static String rendere(final String ausdruck, final ReportingSchule schule) {
		final Context context = new Context();
		context.setVariable("Schule", schule);
		final String html = ReportBuilderUtils.getHtmlTemplateEngine()
				.process("<html><body><span th:text=\"" + ausdruck + "\">Platzhalter</span></body></html>", context);
		final int start = html.indexOf("<span>");
		final int ende = html.indexOf("</span>");
		if ((start < 0) || (ende < 0)) {
			return html;
		}
		return html.substring(start + "<span>".length(), ende);
	}


	// ##### Das Bild als Werttyp #####

	@Test
	void testOhneBildIstNichtsVorhanden() {
		final ReportingBild bild = new ReportingBild(ReportingBildDefinition.DIN5008_BRIEFKOPF, "");
		assertFalse(bild.vorhanden());
		assertEquals("", bild.htmlImageSource(), "Ohne Daten darf keine Bildquelle entstehen, sonst zeigte die Ausgabe ein leeres Bild an.");
	}

	@Test
	void testEinFehlendesBildWirdWieEinLeeresBehandelt() {
		final ReportingBild bild = new ReportingBild(ReportingBildDefinition.DIN5008_BRIEFKOPF, null);
		assertFalse(bild.vorhanden());
		assertEquals("", bild.htmlImageSource());
	}

	@Test
	void testDasBildWirdMitSeinemMimeTypeAusgeliefert() {
		final ReportingBild bild = new ReportingBild(ReportingBildDefinition.DIN5008_BRIEFKOPF, PNG_BASE64);
		assertTrue(bild.vorhanden());
		assertEquals(PNG_BILDQUELLE, bild.htmlImageSource());
	}

	@Test
	void testDieBildquelleEntstehtNurEinmal() {
		// Eine Ausgabe in einzelne Dateien rendert die Vorlage je Datei erneut. Ohne Zwischenspeicher entstünde die Zeichenkette mit den vollständigen
		// Bilddaten bei jedem Aufruf neu.
		final ReportingBild bild = new ReportingBild(ReportingBildDefinition.DIN5008_BRIEFKOPF, PNG_BASE64);
		assertSame(bild.htmlImageSource(), bild.htmlImageSource());
	}

	@Test
	void testEinSvgWirdErkannt() {
		final String svg = "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"1\" height=\"1\"><rect width=\"1\" height=\"1\"/></svg>";
		final String base64 = Base64.getEncoder().encodeToString(svg.getBytes(StandardCharsets.UTF_8));

		final ReportingBild bild = new ReportingBild(ReportingBildDefinition.DIN5008_BRIEFKOPF, base64);

		assertTrue(bild.vorhanden());
		assertEquals("data:image/svg+xml;base64," + base64, bild.htmlImageSource());
	}

	@Test
	void testEinTiffWirdErkannt() {
		// Die Logoverwaltung nimmt TIFF an. Die zuvor genutzte Erkennung kannte das Format nicht und lieferte den Typ "unknown".
		final String tiff = "SUkqAAgAAAAAAA==";

		final ReportingBild bild = new ReportingBild(ReportingBildDefinition.DIN5008_BRIEFKOPF, tiff);

		assertTrue(bild.vorhanden());
		assertEquals("data:image/tiff;base64," + tiff, bild.htmlImageSource());
	}

	@Test
	void testGueltigesBase64OhneBildinhaltGiltAlsFehlendesBild() {
		// Der Resolver löst auch Daten auf, die kein Bild sind, und liefert dafür etwa text/plain. Als vorhandenes Bild verhinderte das den Rückfall
		// auf das aus SchILD-NRW übernommene Schullogo.
		final String text = Base64.getEncoder().encodeToString("Dies ist kein Bild.".getBytes(StandardCharsets.UTF_8));

		final ReportingBild bild = new ReportingBild(ReportingBildDefinition.DIN5008_BRIEFKOPF, text);

		assertFalse(bild.vorhanden());
		assertEquals("", bild.htmlImageSource());
	}

	@Test
	void testNichtAufloesbareBilddatenGeltenAlsFehlendesBild() {
		// Ein Bild, aus dem sich keine Data-URL bilden lässt, kann keine Vorlage darstellen. Eine Bildquelle mit unbrauchbarem Inhalt zeigte im Druck
		// ein defektes Bild statt einer leeren Stelle.
		final ReportingBild bild = new ReportingBild(ReportingBildDefinition.DIN5008_BRIEFKOPF, "Kein gueltiges Base64!");

		assertFalse(bild.vorhanden());
		assertEquals("", bild.htmlImageSource());
	}

	@Test
	void testDieMasseStammenAusDerBilddefinition() {
		final ReportingBild bild = new ReportingBild(ReportingBildDefinition.DIN5008_BRIEFKOPF, "");
		assertEquals(190, bild.breiteMM());
		assertEquals(45, bild.hoeheMM());
	}

	@Test
	void testOhneBilddefinitionBleibenDieMasseLeer() {
		final ReportingBild bild = new ReportingBild(null, PNG_BASE64);
		assertTrue(bild.vorhanden());
		assertEquals(0, bild.breiteMM());
		assertEquals(0, bild.hoeheMM());
	}


	// ##### Der Zugang aus den Vorlagen #####

	@Test
	void testDieVorlageTrifftDieBilddefinitionUeberDenEnum() {
		// Eine nicht auflösbare Enum-Angabe bricht bereits beim Auswerten des Ausdrucks ab. Der Wert 190 belegt darüber hinaus, dass die Vorlage genau
		// die gemeinte Definition erreicht und nicht eine andere.
		assertEquals("190", rendere("${Schule.bild(" + AUSDRUCK_BRIEFKOPF + ").breiteMM()}", schuleOhneBriefkopf()));
	}

	@Test
	void testOhneHinterlegtesBildBleibtDieBildquelleLeer() {
		assertEquals("", rendere("${Schule.bild(" + AUSDRUCK_BRIEFKOPF + ").htmlImageSource()}", schuleOhneBriefkopf()));
	}

	@Test
	void testDasHinterlegteBildErreichtDieVorlageAlsBildquelle() {
		assertEquals(PNG_BILDQUELLE, rendere("${Schule.bild(" + AUSDRUCK_BRIEFKOPF + ").htmlImageSource()}", schuleMitBriefkopf(PNG_BASE64)));
	}

}
