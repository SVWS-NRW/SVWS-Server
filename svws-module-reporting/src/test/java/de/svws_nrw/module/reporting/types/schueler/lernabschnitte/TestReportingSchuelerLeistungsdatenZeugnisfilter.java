package de.svws_nrw.module.reporting.types.schueler.lernabschnitte;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import de.svws_nrw.core.data.reporting.ReportingFilterDefinitionGruppe;
import de.svws_nrw.core.types.reporting.ReportingReportvorlage;

/**
 * Prüft die Regel, dass das Grundschulzeugnis nur Leistungsdaten mit gesetztem Merkmal {@code aufZeugnis} zeigt. Die Vorlage bringt dafür eine
 * unsichtbare, vorausgewählte Filtergruppe mit; der Test wertet sie so aus, wie es die Druckausgabe tut.
 */
class TestReportingSchuelerLeistungsdatenZeugnisfilter {

	@Test
	void testDieFiltergruppeIstUnsichtbarUndVorausgewaehlt() {
		final ReportingFilterDefinitionGruppe gruppe = filtergruppeDerVorlage();

		assertFalse(gruppe.uiIstSichtbar);
		assertEquals(1, gruppe.filterDefinitionen.size());
	}

	@Test
	void testNurZeugnisrelevanteLeistungsdatenPassierenDenFilter() {
		final List<String> validierungsfehler = new ArrayList<>();
		final Predicate<ReportingSchuelerLeistungsdaten> filter =
				ReportingSchuelerLeistungsdaten.FILTER.bedingung(filtergruppeDerVorlage(), validierungsfehler);

		assertTrue(validierungsfehler.isEmpty(), "Unbekannte Filterattribute: " + validierungsfehler);
		assertTrue(filter.test(leistungsdaten(true)));
		assertFalse(filter.test(leistungsdaten(false)));
	}

	private static ReportingFilterDefinitionGruppe filtergruppeDerVorlage() {
		return ReportingReportvorlage.SCHUELER_V_GRUNDSCHULZEUGNIS_KLASSE_4.getReportingParameter().filterDefinitionenGruppen.stream()
				.filter(g -> "ReportingSchuelerLeistungsdaten".equals(g.typ))
				.findFirst()
				.orElseThrow(() -> new AssertionError("Die Vorlage bringt keine Filtergruppe für ReportingSchuelerLeistungsdaten mit."));
	}

	private static ReportingSchuelerLeistungsdaten leistungsdaten(final boolean aufZeugnis) {
		final ReportingSchuelerLeistungsdaten leistungsdaten = mock(ReportingSchuelerLeistungsdaten.class);
		when(leistungsdaten.aufZeugnis()).thenReturn(aufZeugnis);
		return leistungsdaten;
	}

}
