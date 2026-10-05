package de.svws_nrw.module.reporting.builders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.thymeleaf.context.Context;

import de.svws_nrw.base.ResourceUtils;
import de.svws_nrw.core.types.reporting.ReportingReportvorlage;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.html.contexts.HtmlContext;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import de.svws_nrw.module.reporting.types.schule.ReportingSchule;
import de.svws_nrw.module.reporting.types.schule.ReportingSchuljahresabschnitt;

/**
 * Tests der Dateinamensvorlage des Grundschulzeugnisses. Das Halbjahr im Namen stammt aus dem ausgewählten Schuljahresabschnitt, denn das Zeugnis deckt
 * beide Halbjahre der Klasse 4 ab. Ein einzelnes Zeugnis nennt zusätzlich Name und ID des Schülers.
 */
class TestReportBuilderDateinameGrundschulzeugnis {

	/** Die Dateinamensvorlage aus den Ressourcen der Reportvorlage. */
	private static String vorlage;

	/** Ein minimaler Context mit den Variablen, die die Dateinamensvorlage anspricht. */
	private static final class TestHtmlContext extends HtmlContext<Object> {

		/**
		 * Erzeugt den Context mit den übergebenen Variablen.
		 *
		 * @param variablen Die Variablen der Auswertung.
		 */
		private TestHtmlContext(final Map<String, Object> variablen) {
			super(null);
			final Context context = new Context();
			context.setVariables(variablen);
			setContext(context);
		}
	}

	@BeforeAll
	static void ladeVorlage() {
		vorlage = ResourceUtils.textOrEmpty(ReportingReportvorlage.getRootPfad()
				+ ReportingReportvorlage.SCHUELER_V_GRUNDSCHULZEUGNIS_KLASSE_4.getPfadDateinamensvorlage());
		assertFalse(vorlage.isBlank(), "Die Dateinamensvorlage des Grundschulzeugnisses fehlt.");
	}

	@Test
	void testEinZeugnisImErstenHalbjahrNenntHalbjahrUndSchueler() throws ApiOperationException {
		assertEquals("Grundschulzeugnis-Klasse4_2025-26_HJ1_Muster__Max_(42)", dateiname("2025/26.1", List.of(schueler("Muster", "Max", 42)), false));
	}

	@Test
	void testEinZeugnisImZweitenHalbjahrNenntDasZweiteHalbjahr() throws ApiOperationException {
		assertEquals("Grundschulzeugnis-Klasse4_2025-26_HJ2_Muster__Max_(42)", dateiname("2025/26.2", List.of(schueler("Muster", "Max", 42)), false));
	}

	@Test
	void testLeerzeichenImNamenWerdenZuUnterstrichen() throws ApiOperationException {
		assertEquals("Grundschulzeugnis-Klasse4_2025-26_HJ1_von_Berg__Anna_Lena_(7)",
				dateiname("2025/26.1", List.of(schueler("von Berg", "Anna Lena", 7)), false));
	}

	@Test
	void testMehrereZeugnisseNennenNurDasHalbjahr() throws ApiOperationException {
		assertEquals("Grundschulzeugnisse-Klasse4_2025-26_HJ1",
				dateiname("2025/26.1", List.of(schueler("Muster", "Max", 42), schueler("Beispiel", "Eva", 43)), false));
	}

	@Test
	void testOhneSchuelerEntstehtDerNameDesHalbjahrs() throws ApiOperationException {
		assertEquals("Grundschulzeugnis-Klasse4_2025-26_HJ1", dateiname("2025/26.1", List.of(), false));
	}

	@Test
	void testDerZeitstempelWirdAufWunschAngehaengt() throws ApiOperationException {
		final String name = dateiname("2025/26.1", List.of(schueler("Muster", "Max", 42)), true);

		assertTrue(name.matches("Grundschulzeugnis-Klasse4_2025-26_HJ1_Muster__Max_\\(42\\)_\\d{8}-\\d{4}"), name);
	}

	private static String dateiname(final String schuljahresabschnitt, final List<ReportingSchueler> schueler, final boolean mitZeitstempel)
			throws ApiOperationException {
		final ReportingSchuljahresabschnitt abschnitt = mock(ReportingSchuljahresabschnitt.class);
		when(abschnitt.textSchuljahresabschnittKurz()).thenReturn(schuljahresabschnitt);
		final ReportingSchule schule = mock(ReportingSchule.class);
		when(schule.auswahlSchuljahresabschnitt()).thenReturn(abschnitt);
		final Map<String, Object> variablen = Map.of("Schule", schule, "Schueler", schueler,
				"VorlageParameter", Map.of("dateinameMitZeitstempel", mitZeitstempel));
		return ReportBuilderUtils.generiereDateinameAusVorlage(vorlage, List.of(new TestHtmlContext(variablen)));
	}

	private static ReportingSchueler schueler(final String nachname, final String vorname, final long id) {
		final ReportingSchueler schueler = mock(ReportingSchueler.class);
		when(schueler.nachname()).thenReturn(nachname);
		when(schueler.vorname()).thenReturn(vorname);
		when(schueler.id()).thenReturn(id);
		return schueler;
	}

}
