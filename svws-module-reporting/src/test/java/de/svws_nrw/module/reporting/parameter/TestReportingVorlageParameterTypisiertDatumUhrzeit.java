package de.svws_nrw.module.reporting.parameter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import de.svws_nrw.core.data.reporting.ReportingReportvorlageParameter;
import de.svws_nrw.core.types.reporting.ReportingReportvorlageParameterTyp;
import de.svws_nrw.db.utils.ApiOperationException;

/**
 * Tests der Parametertypen DATUM und DATUM_UHRZEIT in {@link ReportingVorlageParameterTypisiert}. Angenommen werden nur echte Kalenderdaten und
 * Uhrzeiten; der ParameterBuilder verwirft alles andere. Das Template erhält Datum und Uhrzeit immer in derselben Form, egal wie sie eingegeben wurden.
 */
class TestReportingVorlageParameterTypisiertDatumUhrzeit {

	private static final int DATUM = ReportingReportvorlageParameterTyp.DATUM.getId();

	private static final int DATUM_UHRZEIT = ReportingReportvorlageParameterTyp.DATUM_UHRZEIT.getId();

	@ParameterizedTest
	@ValueSource(strings = { "15.06.2026", "1.6.2026", "29.02.2028", "2026-06-15", "", "  " })
	void testGueltigeDatumsangaben(final String wert) {
		assertTrue(ReportingVorlageParameterTypisiert.istWertTypkonform(DATUM, wert), wert);
	}

	@ParameterizedTest
	@ValueSource(strings = { "31.02.2026", "29.02.2027", "15.13.2026", "15.06.26", "2026-02-31", "15/06/2026", "morgen" })
	void testUngueltigeDatumsangaben(final String wert) {
		assertFalse(ReportingVorlageParameterTypisiert.istWertTypkonform(DATUM, wert), wert);
	}

	@ParameterizedTest
	@CsvSource({ "15.06.2026, 2026-06-15", "1.6.2026, 2026-06-01", "2026-06-15, 2026-06-15" })
	void testDasTemplateErhaeltDasDatumAlsIso(final String eingabe, final String erwartet) throws ApiOperationException {
		assertEquals(erwartet, typisierterWert(DATUM, eingabe));
	}

	@Test
	void testEinLeeresDatumBleibtLeer() throws ApiOperationException {
		assertEquals("", typisierterWert(DATUM, ""));
	}

	@ParameterizedTest
	@ValueSource(strings = { "2026-06-15T18:00", "2026-06-15T18:00:00", "15.06.2026 18:00", "1.6.2026 8:00", "31.12.2026 23:59", "", "  " })
	void testGueltigeTermine(final String wert) {
		assertTrue(ReportingVorlageParameterTypisiert.istWertTypkonform(DATUM_UHRZEIT, wert), wert);
	}

	@ParameterizedTest
	@ValueSource(strings = { "15.06.2026", "18:00", "2026-06-15", "2026-06-15 18:00", "31.02.2026 18:00", "15.06.2026 24:00", "15.06.2026 18 Uhr",
		"2026-06-15T25:00", "morgen abend" })
	void testUngueltigeTermine(final String wert) {
		assertFalse(ReportingVorlageParameterTypisiert.istWertTypkonform(DATUM_UHRZEIT, wert), wert);
	}

	@ParameterizedTest
	@CsvSource({ "2026-06-15T18:00, 2026-06-15, 18:00", "2026-06-15T18:00:00, 2026-06-15, 18:00", "15.06.2026 18:00, 2026-06-15, 18:00",
		"1.6.2026 8:00, 2026-06-01, 08:00" })
	void testDasTemplateErhaeltDatumUndUhrzeitGetrennt(final String eingabe, final String datum, final String uhrzeit) throws ApiOperationException {
		assertEquals(new ReportingDatumUhrzeit(datum, uhrzeit), typisierterWert(DATUM_UHRZEIT, eingabe));
	}

	@Test
	void testEinLeererTerminIstLeerUndNichtNull() throws ApiOperationException {
		final ReportingDatumUhrzeit termin = (ReportingDatumUhrzeit) typisierterWert(DATUM_UHRZEIT, "");

		assertTrue(termin.istLeer());
		assertEquals("", termin.datum());
		assertEquals("", termin.uhrzeit());
		assertEquals("", termin.toString());
	}

	@Test
	void testEinTerminErscheintAlsTextImFormatDesDatumUhrzeitFelds() throws ApiOperationException {
		assertEquals("2026-06-15T18:00", typisierterWert(DATUM_UHRZEIT, "15.06.2026 18:00").toString());
	}

	private static Object typisierterWert(final int typ, final String wert) throws ApiOperationException {
		final ReportingReportvorlageParameter parameter = new ReportingReportvorlageParameter();
		parameter.name = "termin";
		parameter.typ = typ;
		parameter.wert = wert;
		return new ReportingVorlageParameterTypisiert<>(parameter).getWert();
	}

}
