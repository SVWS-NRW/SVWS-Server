package de.svws_nrw.schulbescheinigung;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests für {@link SchulbescheinigungBuilder}.
 */
@DisplayName("Diese Klasse testet die Klasse SchulbescheinigungBuilder")
class SchulbescheinigungBuilderTest {

	private SchulbescheinigungBuilder defaultBuilder() {
		return new SchulbescheinigungBuilder()
				.titel("Schulbescheinigung")
				.sprache("deu")
				.ausstellungOrt("Testort")
				.ausstellungDatum("2026-01-01")
				.schuelerNachname("Musterfrau")
				.schuelerVorname("Marianne")
				.schuelerGeburtsdatum("2012-10-23")
				.bildungsgangEnddatum("2031-07-31")
				.schuleName("Test-Gymnasium");
	}

	private SchulbescheinigungBuilder requiredFieldsBuilder() {
		return new SchulbescheinigungBuilder()
				.schuelerNachname("Musterfrau")
				.schuelerVorname("Marianne")
				.schuleName("Test-Gymnasium")
				.bildungsgangEnddatum("2031-07-31")
				.sprache("deu");
	}

	@Test
	@DisplayName("build | Erfolg - Objekt wird erzeugt")
	void buildErzeugtObjekt() {
		final var bescheinigung = defaultBuilder().build();

		assertThat(bescheinigung).isNotNull();
	}

	@Test
	@DisplayName("build | Erfolg - Titel korrekt gesetzt")
	void buildSetztTitelKorrekt() {
		final var bescheinigung = defaultBuilder().build();

		assertThat(bescheinigung.getTitel().getValue()).isEqualTo("Schulbescheinigung");
	}

	@Test
	@DisplayName("build | Erfolg - Nachname korrekt gesetzt")
	void buildSetztNachnameKorrekt() {
		final var bescheinigung = defaultBuilder().build();

		assertThat(bescheinigung.getSchueler().getNameNatuerlichePerson().getFamilienname().getName())
				.isEqualTo("Musterfrau");
	}

	@Test
	@DisplayName("build | Erfolg - Vorname korrekt gesetzt")
	void buildSetztVornameKorrekt() {
		final var bescheinigung = defaultBuilder().build();

		assertThat(bescheinigung.getSchueler().getNameNatuerlichePerson().getVorname().getName())
				.isEqualTo("Marianne");
	}

	@Test
	@DisplayName("build | Erfolg - Geburtsdatum korrekt gesetzt")
	void buildSetztGeburtsdatumKorrekt() {
		final var bescheinigung = defaultBuilder().build();

		assertThat(bescheinigung.getSchueler().getGeburt().getDatum().getJahrMonatTag().toXMLFormat())
				.isEqualTo("2012-10-23");
	}

	@Test
	@DisplayName("build | Erfolg - Schulname korrekt gesetzt")
	void buildSetztSchuleKorrekt() {
		final var bescheinigung = defaultBuilder().build();

		assertThat(bescheinigung.getSchule().getName().getName())
				.isEqualTo("Test-Gymnasium");
	}

	@Test
	@DisplayName("build | Erfolg - Bildungsgang-Enddatum korrekt gesetzt")
	void buildSetztBildungsgangEnddatumKorrekt() {
		final var bescheinigung = defaultBuilder().build();

		assertThat(bescheinigung.getSchulbesuch().getZeitraum().getEnde().toXMLFormat())
				.isEqualTo("2031-07-31");
	}

	@Test
	@DisplayName("build | Erfolg - Ausstellungsdatum korrekt gesetzt")
	void buildSetztAusstellungsdatumKorrekt() {
		final var bescheinigung = defaultBuilder().build();

		assertThat(bescheinigung.getAusstellung().getFirst().getDatum().toXMLFormat())
				.isEqualTo("2026-01-01");
	}

	@Test
	@DisplayName("build | Erfolg - Ausstellungsort korrekt gesetzt")
	void buildSetztAusstellungsortKorrekt() {
		final var bescheinigung = defaultBuilder().build();

		assertThat(bescheinigung.getAusstellung().getFirst().getOrt().getOrt())
				.isEqualTo("Testort");
	}


	@Test
	@DisplayName("build | Erfolg - Sprache korrekt gesetzt")
	void buildSetztSpracheKorrekt() {
		final var bescheinigung = defaultBuilder().build();

		assertThat(bescheinigung.getSprache().getCode()).isEqualTo("deu");
	}

	@Test
	@DisplayName("build | Erfolg - Nur Pflichtfelder gesetzt")
	void buildMitPflichtfeldern() {
		final var bescheinigung = requiredFieldsBuilder().build();

		assertThat(bescheinigung.getSchueler().getNameNatuerlichePerson().getFamilienname().getName())
				.isEqualTo("Musterfrau");
		assertThat(bescheinigung.getSchueler().getNameNatuerlichePerson().getVorname().getName())
				.isEqualTo("Marianne");
		assertThat(bescheinigung.getSchule().getName().getName())
				.isEqualTo("Test-Gymnasium");
		assertThat(bescheinigung.getSprache().getCode())
				.isEqualTo("deu");
		assertThat(bescheinigung.getSchulbesuch().getZeitraum().getEnde().toXMLFormat())
				.isEqualTo("2031-07-31");
	}

	@Test
	@DisplayName("build | Ungültiges Ausstellungsdatum")
	void buildMitUngueltigemDatumWirftException() {
		final var builder = defaultBuilder().ausstellungDatum("kein-datum");

		assertThatThrownBy(builder::build)
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("build | Ungültiges Geburtsdatum")
	void buildMitUngueltigemGeburtsdatumWirftException() {
		final var builder = defaultBuilder().schuelerGeburtsdatum("kein-datum");

		assertThatThrownBy(builder::build)
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("build | Sprache null")
	void buildOhneSpracheWirftException() {
		final var builder = defaultBuilder().sprache(null);

		assertThatThrownBy(builder::build)
				.isInstanceOf(NullPointerException.class);
	}

}
