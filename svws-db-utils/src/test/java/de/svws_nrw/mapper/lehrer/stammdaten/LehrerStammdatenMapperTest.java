package de.svws_nrw.mapper.lehrer.stammdaten;

import java.util.List;

import de.svws_nrw.asd.types.Geschlecht;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.types.PersonalTyp;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.service.lehrer.stammdaten.LehrerStammdatenCreateRequest;
import de.svws_nrw.service.lehrer.stammdaten.LehrerStammdatenPatchRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests für den {@link LehrerStammdatenMapper}.
 */
class LehrerStammdatenMapperTest {

	private static final long NATIONALITAET_DZA = 68090065L;

	private final LehrerStammdatenMapper mapper = LehrerStammdatenMapper.INSTANCE;

	@BeforeAll
	static void setUpAll() {
		ASDCoreTypeUtils.initAll();
	}

	private static DTOLehrer entity() {
		final var entity = new DTOLehrer(1L, "MUS", "Mustermann");
		entity.kuerzelLID = "ALBE";
		entity.Vorname = "Max";
		entity.PersonTyp = PersonalTyp.LEHRKRAFT;
		entity.Geschlecht = Geschlecht.M;
		entity.Anrede = "Herr";
		entity.Titel = "Dr.";
		entity.Amtsbezeichnung = "OStD";
		entity.Geburtsdatum = "1911-11-11";
		entity.Strassenname = "Musterweg";
		entity.HausNr = "4";
		entity.HausNrZusatz = "a";
		entity.Ort_ID = 100L;
		entity.Ortsteil_ID = 200L;
		entity.telefon = "123";
		entity.telefonMobil = "456";
		entity.eMailDienstlich = "dienst@mail";
		entity.eMailPrivat = "privat@mail";
		entity.Sichtbar = true;
		entity.statistikRelevant = false;

		return entity;
	}

	@Test
	@DisplayName("toApi | mappt alle Felder")
	void toApi() {
		final var result = mapper.toApi(entity());

		assertThat(result)
				.extracting("id", "kuerzel", "kuerzelStatistik", "personalTyp", "nachname", "vorname", "geschlecht", "anrede", "titel", "amtsbezeichnung",
						"geburtsdatum", "strassenname", "hausnummer", "hausnummerZusatz", "wohnortID", "ortsteilID", "telefon", "telefonMobil",
						"emailDienstlich", "emailPrivat", "istSichtbar", "istRelevantFuerStatistik", "foto", "leitungsfunktionen")
				.containsExactly(1L, "MUS", "ALBE", "LEHRKRAFT", "Mustermann", "Max", 3, "Herr", "Dr.", "OStD",
						"1911-11-11", "Musterweg", "4", "a", 100L, 200L, "123", "456",
						"dienst@mail", "privat@mail", true, false, null, List.of());
	}

	@Test
	@DisplayName("toApi | nicht gesetzte Werte erhalten Standardwerte")
	void toApi_standardwerte() {
		final var result = mapper.toApi(new DTOLehrer(1L, "MUS", "Mustermann"));

		assertThat(result)
				.extracting("personalTyp", "anrede", "titel", "amtsbezeichnung", "vorname", "geschlecht", "istSichtbar", "istRelevantFuerStatistik",
						"idStaatsangehoerigkeit")
				.containsExactly("", "", "", "", "", LehrerStammdatenMapper.ID_GESCHLECHT_NICHT_GESETZT, true, true, null);
	}

	@Test
	@DisplayName("toApi | mappt die Staatsangehörigkeit auf ihre ID")
	void toApi_staatsangehoerigkeit() {
		final var entity = entity();
		entity.staatsangehoerigkeit = mapper.mapNationalitaet(NATIONALITAET_DZA);

		assertThat(mapper.toApi(entity).idStaatsangehoerigkeit).isEqualTo(NATIONALITAET_DZA);
	}

	@Test
	@DisplayName("toDomain | mappt die Felder des Create-Requests, Wohnort und Ortsteil bleiben leer")
	void toDomain() {
		final var request = new LehrerStammdatenCreateRequest();
		request.kuerzel = "NEU";
		request.personalTyp = "SEKRETARIAT";
		request.nachname = "Neumann";
		request.vorname = "Nora";
		request.geschlecht = 4;
		request.anrede = "Frau";
		request.geburtsdatum = "1990-01-01";
		request.idStaatsangehoerigkeit = NATIONALITAET_DZA;
		request.hausnummer = "7";
		request.emailDienstlich = "nora@dienst";
		request.istSichtbar = false;
		request.wohnortID = 100L;
		request.ortsteilID = 200L;

		final var result = mapper.toDomain(request);

		assertThat(result)
				.extracting("Kuerzel", "PersonTyp", "Nachname", "Vorname", "Geschlecht", "Anrede", "Geburtsdatum", "HausNr", "eMailDienstlich", "Sichtbar",
						"Ort_ID", "Ortsteil_ID", "staatsangehoerigkeit")
				.containsExactly("NEU", PersonalTyp.SEKRETARIAT, "Neumann", "Nora", Geschlecht.W, "Frau", "1990-01-01", "7", "nora@dienst", false,
						null, null, mapper.mapNationalitaet(NATIONALITAET_DZA));
	}

	@Test
	@DisplayName("patch | überschreibt nur die gesetzten Felder, auch mit null")
	void patch() {
		final var entity = entity();
		final var request = new LehrerStammdatenPatchRequest();
		request.kuerzel = JsonNullable.of("NEU");
		request.nachname = JsonNullable.of("Musterfrau");
		request.personalTyp = JsonNullable.of("EXTERN");
		request.geschlecht = JsonNullable.of(4);
		request.titel = JsonNullable.of(null);
		request.idStaatsangehoerigkeit = JsonNullable.of(NATIONALITAET_DZA);
		request.emailPrivat = JsonNullable.of("neu@mail");
		request.istRelevantFuerStatistik = JsonNullable.of(true);
		request.wohnortID = JsonNullable.of(999L);
		request.ortsteilID = JsonNullable.of(888L);

		mapper.patch(request, entity);

		assertThat(entity)
				.extracting("ID", "Kuerzel", "Nachname", "PersonTyp", "Geschlecht", "Titel", "eMailPrivat", "statistikRelevant", "Anrede", "Vorname",
						"Ort_ID", "Ortsteil_ID", "staatsangehoerigkeit")
				.containsExactly(1L, "NEU", "Musterfrau", PersonalTyp.EXTERN, Geschlecht.W, null, "neu@mail", true, "Herr", "Max",
						100L, 200L, mapper.mapNationalitaet(NATIONALITAET_DZA));
	}

	@Test
	@DisplayName("patch | Staatsangehörigkeit null entfernt sie")
	void patch_staatsangehoerigkeitNull() {
		final var entity = entity();
		entity.staatsangehoerigkeit = mapper.mapNationalitaet(NATIONALITAET_DZA);
		final var request = new LehrerStammdatenPatchRequest();
		request.idStaatsangehoerigkeit = JsonNullable.of(null);

		mapper.patch(request, entity);

		assertThat(entity.staatsangehoerigkeit).isNull();
	}

	@Test
	@DisplayName("Hilfsmethoden | Personal-Typ-Kürzel und Nationalität ohne Wert")
	void hilfsmethoden() {
		assertThat(mapper.mapPersonalTypKuerzel(null)).isEmpty();
		assertThat(mapper.mapNationalitaetId(null)).isNull();
		assertThat(mapper.mapNationalitaet(null)).isNull();
		assertThat(mapper.mapNullToTrue(null)).isTrue();
		assertThat(mapper.mapNullToTrue(false)).isFalse();
	}

}
