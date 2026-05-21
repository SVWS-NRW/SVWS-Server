package de.svws_nrw.mapping;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests für {@link XSchuleMapper}.
 */
@DisplayName("Diese Klasse testet die Klasse XSchuleMapper")
class XSchuleMapperTest {

	private static final String NACHNAME = "Musterfrau";
	private static final String VORNAME = "Marianne";
	private static final String GEBURTSDATUM = "2010-05-15";
	private static final String SCHULNAME = "Test-Gymnasium";
	private static final String ENDDATUM = "2031-07-31";
	private static final String ORT = "Testort";
	private static final String DATUM = "2026-01-01";

	@Test
	@DisplayName("toXSchuleSchueler | Erfolg - Nachname korrekt gesetzt")
	void toXSchuleSchuelerSetztNachnameKorrekt() {
		final var name = XSchuleMapper.toXSchuleNameNatuerlichePerson(NACHNAME, VORNAME);
		final var geburt = XSchuleMapper.toXSchuleGeburt(GEBURTSDATUM);
		final var schueler = XSchuleMapper.toXSchuleSchueler(name, geburt);

		assertThat(schueler.getNameNatuerlichePerson().getFamilienname().getName()).isEqualTo(NACHNAME);
	}

	@Test
	@DisplayName("toXSchuleSchueler | Erfolg - Vorname korrekt gesetzt")
	void toXSchuleSchuelerSetztVornameKorrekt() {
		final var name = XSchuleMapper.toXSchuleNameNatuerlichePerson(NACHNAME, VORNAME);
		final var geburt = XSchuleMapper.toXSchuleGeburt(GEBURTSDATUM);
		final var schueler = XSchuleMapper.toXSchuleSchueler(name, geburt);

		assertThat(schueler.getNameNatuerlichePerson().getVorname().getName()).isEqualTo(VORNAME);
	}

	@Test
	@DisplayName("toXSchuleSchueler | Erfolg - Rufname korrekt gesetzt")
	void toXSchuleSchuelerSetztRufnameKorrekt() {
		final var name = XSchuleMapper.toXSchuleNameNatuerlichePerson(NACHNAME, VORNAME);
		final var geburt = XSchuleMapper.toXSchuleGeburt(GEBURTSDATUM);
		final var schueler = XSchuleMapper.toXSchuleSchueler(name, geburt);

		assertThat(schueler.getNameNatuerlichePerson().getRufname().getName()).isEqualTo(VORNAME);
	}

	@Test
	@DisplayName("toXSchuleSchueler | Erfolg - Geburtsdatum korrekt gesetzt")
	void toXSchuleSchuelerSetztGeburtsdatumKorrekt() {
		final var name = XSchuleMapper.toXSchuleNameNatuerlichePerson(NACHNAME, VORNAME);
		final var geburt = XSchuleMapper.toXSchuleGeburt(GEBURTSDATUM);
		final var schueler = XSchuleMapper.toXSchuleSchueler(name, geburt);

		assertThat(schueler.getGeburt().getDatum().getJahrMonatTag().toXMLFormat()).isEqualTo(GEBURTSDATUM);
	}

	@Test
	@DisplayName("toXSchuleGeburt | Erfolg - Datum korrekt gesetzt")
	void toXSchuleGeburtSetztDatumKorrekt() {
		final var geburt = XSchuleMapper.toXSchuleGeburt(GEBURTSDATUM);

		assertThat(geburt.getDatum().getJahrMonatTag().toXMLFormat()).isEqualTo(GEBURTSDATUM);
	}

	@Test
	@DisplayName("toXBildungStringLocalized | Erfolg - Wert korrekt gesetzt")
	void toXBildungStringLocalizedSetztWertKorrekt() {
		final var result = XSchuleMapper.toXBildungStringLocalized("Schulbescheinigung");

		assertThat(result.getValue()).isEqualTo("Schulbescheinigung");
	}

	@Test
	@DisplayName("toXBildungAusstellung | Erfolg - Datum korrekt gesetzt")
	void toXBildungAusstellungSetztDatumKorrekt() {
		final var ausstellung = XSchuleMapper.toXBildungAusstellung(DATUM, ORT);

		assertThat(ausstellung.getDatum().toXMLFormat()).isEqualTo(DATUM);
	}

	@Test
	@DisplayName("toXBildungAusstellung | Erfolg - Ort korrekt gesetzt")
	void toXBildungAusstellungSetztOrtKorrekt() {
		final var ausstellung = XSchuleMapper.toXBildungAusstellung(DATUM, ORT);

		assertThat(ausstellung.getOrt().getOrt()).isEqualTo(ORT);
	}

	@Test
	@DisplayName("toXSchuleSchule | Erfolg - Name korrekt gesetzt")
	void toXSchuleSchuleSetztNameKorrekt() {
		final var nameOrganisation = XSchuleMapper.toXSchuleNameOrganisation(SCHULNAME);
		final var schule = XSchuleMapper.toXSchuleSchule(nameOrganisation);

		assertThat(schule.getName().getName()).isEqualTo(SCHULNAME);
	}

	@Test
	@DisplayName("toXSchuleZeitraum | Erfolg - Ende korrekt gesetzt")
	void toXSchuleZeitraumSetztEndeKorrekt() {
		final var zeitraum = XSchuleMapper.toXSchuleZeitraum(ENDDATUM);

		assertThat(zeitraum.getEnde().toXMLFormat()).isEqualTo(ENDDATUM);
	}

	@Test
	@DisplayName("toXSchuleZeitraum | Ungültiges Datum")
	void toXSchuleZeitraumUngueltigesDatumWirftException() {

		assertThatThrownBy(() -> XSchuleMapper.toXSchuleZeitraum("kein-datum"))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("toXSchuleSchulbesuch | Erfolg - Zeitraum korrekt gesetzt")
	void toXSchuleSchulbesuchSetztZeitraumKorrekt() {
		final var zeitraum = XSchuleMapper.toXSchuleZeitraum(ENDDATUM);
		final var schulbesuch = XSchuleMapper.toXSchuleSchulbesuch(zeitraum);

		assertThat(schulbesuch.getZeitraum()).isSameAs(zeitraum);
	}

}
