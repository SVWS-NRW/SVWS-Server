package de.svws_nrw.module.reporting.parameter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import de.svws_nrw.core.data.reporting.ReportingParameter;
import de.svws_nrw.core.data.reporting.ReportingReportvorlageParameter;
import de.svws_nrw.core.data.reporting.ReportingReportvorlageParameterGruppe;
import de.svws_nrw.core.types.reporting.ReportingReportvorlageParameterTyp;
import de.svws_nrw.db.utils.ApiOperationException;

/**
 * Tests für {@link ReportingParameterTypisiert#vorlageParameterGanzzahl(String)}. Die Methode wandelt wie für die Templates um: Ein leerer
 * Wert ergibt -1, ein fehlender oder nicht ganzzahliger Parameter null.
 */
class TestReportingParameterTypisiertVorlageParameterGanzzahl {

	private static ReportingParameterTypisiert erzeugeParameter(final ReportingReportvorlageParameterTyp typ, final String wert) {
		final ReportingReportvorlageParameter parameter = new ReportingReportvorlageParameter();
		parameter.name = "abiturjahrgangPlanung";
		parameter.typ = typ.getId();
		parameter.wert = wert;
		final ReportingReportvorlageParameterGruppe gruppe = new ReportingReportvorlageParameterGruppe();
		gruppe.reportvorlageParameter.add(parameter);
		final ReportingParameter reportingParameter = new ReportingParameter();
		reportingParameter.reportvorlageParameterGruppen.add(gruppe);
		return new ReportingParameterTypisiert(null, reportingParameter);
	}

	@Test
	void testGanzzahl() {
		assertEquals(2028, erzeugeParameter(ReportingReportvorlageParameterTyp.INTEGER, " 2028 ").vorlageParameterGanzzahl("abiturjahrgangPlanung"));
	}

	@ParameterizedTest
	@ValueSource(strings = { "", "  " })
	void testLeererWertErgibtMinusEins(final String wert) {
		assertEquals(-1, erzeugeParameter(ReportingReportvorlageParameterTyp.INTEGER, wert).vorlageParameterGanzzahl("abiturjahrgangPlanung"));
	}

	@Test
	void testUngueltigerWertIstEinFehlerDerVorlage() {
		final ReportingParameterTypisiert parameter = erzeugeParameter(ReportingReportvorlageParameterTyp.INTEGER, "zweitausend");
		assertThrows(ApiOperationException.class, () -> parameter.vorlageParameterGanzzahl("abiturjahrgangPlanung"));
	}

	@Test
	void testParameterOhneGanzzahlTyp() {
		assertNull(erzeugeParameter(ReportingReportvorlageParameterTyp.STRING, "2028").vorlageParameterGanzzahl("abiturjahrgangPlanung"));
	}

	@Test
	void testParameterFehlt() {
		assertNull(erzeugeParameter(ReportingReportvorlageParameterTyp.INTEGER, "2028").vorlageParameterGanzzahl("nurBelegteFaecher"));
	}

}
