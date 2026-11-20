package de.svws_nrw.mapper.schueler.kaoa;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.jahrgang.Jahrgaenge;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.schueler.SchuelerKAoADaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerKAoADaten;
import de.svws_nrw.service.schueler.kaoa.SchuelerKAoACreateRequest;
import de.svws_nrw.service.schueler.kaoa.SchuelerKAoAPatchRequest;

/**
 * Tests für den {@link SchuelerKAoAMapper}.
 */
class SchuelerKAoAMapperTest {

	private static final int SCHULJAHR = 2024;
	private static final long ID_ABSCHNITT = 42L;
	private static final long ID_JAHRGANG_07 = 7000001L;

	private final SchuelerKAoAMapper mapper = SchuelerKAoAMapper.INSTANCE;

	@BeforeAll
	static void setUpAll() {
		ASDCoreTypeUtils.initAll();
	}

	private static Schuljahresabschnitt abschnitt() {
		final var abschnitt = new Schuljahresabschnitt();
		abschnitt.id = ID_ABSCHNITT;
		abschnitt.schuljahr = SCHULJAHR;
		return abschnitt;
	}

	private static DTOSchuelerKAoADaten entity() {
		final var entity = new DTOSchuelerKAoADaten(5L, 7L, 10L);
		entity.jahrgang = "07";
		entity.idMerkmal = 51L;
		entity.idZusatzmerkmal = 68L;
		entity.idAnschlussoption = 24L;
		entity.bemerkung = "alt";
		return entity;
	}

	@Test
	@DisplayName("toApi | Übernimmt alle Felder und löst Schuljahresabschnitt und Jahrgang über den Abschnitt auf")
	void toApi_uebernimmtAlleFelder() {
		final var entity = entity();
		entity.idBerufsfeld = 2L;
		entity.idEbene4 = 3L;

		final var result = mapper.toApi(entity, abschnitt());

		assertThat(result)
				.hasFieldOrPropertyWithValue("id", 5L)
				.hasFieldOrPropertyWithValue("idSchuljahresabschnitt", ID_ABSCHNITT)
				.hasFieldOrPropertyWithValue("idJahrgang", ID_JAHRGANG_07)
				.hasFieldOrPropertyWithValue("idKategorie", 10L)
				.hasFieldOrPropertyWithValue("idMerkmal", 51L)
				.hasFieldOrPropertyWithValue("idZusatzmerkmal", 68L)
				.hasFieldOrPropertyWithValue("idAnschlussoption", 24L)
				.hasFieldOrPropertyWithValue("idBerufsfeld", 2L)
				.hasFieldOrPropertyWithValue("idEbene4", 3L)
				.hasFieldOrPropertyWithValue("bemerkung", "alt");
	}

	@Test
	@DisplayName("toApi | Fehlendes Merkmal und Zusatzmerkmal werden als -1 abgebildet")
	void toApi_merkmalNull() {
		final var entity = entity();
		entity.idMerkmal = null;
		entity.idZusatzmerkmal = null;

		final var result = mapper.toApi(entity, abschnitt());

		assertThat(result)
				.hasFieldOrPropertyWithValue("idMerkmal", SchuelerKAoAMapper.ID_NICHT_GESETZT)
				.hasFieldOrPropertyWithValue("idZusatzmerkmal", SchuelerKAoAMapper.ID_NICHT_GESETZT);
	}

	@Test
	@DisplayName("toDomain | Erstellt eine Entität ohne ID mit Lernabschnitt, Jahrgang-Schlüssel und allen Feldern")
	void toDomain_erstelltEntitaet() {
		final var daten = new SchuelerKAoADaten();
		daten.idJahrgang = 10000001L;
		daten.idKategorie = 14L;
		daten.idMerkmal = 69L;
		daten.idZusatzmerkmal = 117L;
		daten.idAnschlussoption = 24L;
		daten.bemerkung = "text";

		final var result = mapper.toDomain(7L, daten);

		assertThat(result)
				.hasFieldOrPropertyWithValue("id", 0L)
				.hasFieldOrPropertyWithValue("idLernabschnitt", 7L)
				.hasFieldOrPropertyWithValue("jahrgang", "10")
				.hasFieldOrPropertyWithValue("idKategorie", 14L)
				.hasFieldOrPropertyWithValue("idMerkmal", 69L)
				.hasFieldOrPropertyWithValue("idZusatzmerkmal", 117L)
				.hasFieldOrPropertyWithValue("idAnschlussoption", 24L)
				.hasFieldOrPropertyWithValue("idBerufsfeld", null)
				.hasFieldOrPropertyWithValue("idEbene4", null)
				.hasFieldOrPropertyWithValue("bemerkung", "text");
	}

	@Test
	@DisplayName("toApi | Create-Request wird vollständig übertragen, die ID bleibt ungesetzt")
	void toApi_createRequest() {
		final var request = new SchuelerKAoACreateRequest();
		request.idSchuljahresabschnitt = 1L;
		request.idJahrgang = 2L;
		request.idKategorie = 3L;
		request.idMerkmal = 4L;
		request.idZusatzmerkmal = 5L;
		request.idEbene4 = 6L;
		request.bemerkung = "text";

		final var result = mapper.toApi(request);

		assertThat(result)
				.hasFieldOrPropertyWithValue("id", -1L)
				.hasFieldOrPropertyWithValue("idSchuljahresabschnitt", 1L)
				.hasFieldOrPropertyWithValue("idJahrgang", 2L)
				.hasFieldOrPropertyWithValue("idKategorie", 3L)
				.hasFieldOrPropertyWithValue("idMerkmal", 4L)
				.hasFieldOrPropertyWithValue("idZusatzmerkmal", 5L)
				.hasFieldOrPropertyWithValue("idEbene4", 6L)
				.hasFieldOrPropertyWithValue("idAnschlussoption", null)
				.hasFieldOrPropertyWithValue("bemerkung", "text");
	}

	@Test
	@DisplayName("patchDaten | Nur gesetzte Felder werden übernommen, gesetztes null leert das Feld")
	void patchDaten_uebernimmtNurGesetzteFelder() {
		final var daten = new SchuelerKAoADaten();
		daten.idKategorie = 3L;
		daten.idEbene4 = 6L;
		daten.bemerkung = "alt";
		final var request = new SchuelerKAoAPatchRequest();
		request.idMerkmal = JsonNullable.of(9L);
		request.idEbene4 = JsonNullable.of(null);

		mapper.patchDaten(request, daten);

		assertThat(daten)
				.hasFieldOrPropertyWithValue("idKategorie", 3L)
				.hasFieldOrPropertyWithValue("idMerkmal", 9L)
				.hasFieldOrPropertyWithValue("idEbene4", null)
				.hasFieldOrPropertyWithValue("bemerkung", "alt");
	}

	@Test
	@DisplayName("patch | Überträgt alle Felder inklusive Lernabschnitt und Jahrgang-Schlüssel, null leert das Feld, die ID bleibt")
	void patch_uebertraegtAlleFelder() {
		final var entity = entity();
		final var daten = new SchuelerKAoADaten();
		daten.id = 99L;
		daten.idJahrgang = 8000001L;
		daten.idKategorie = 11L;
		daten.idMerkmal = 52L;
		daten.idZusatzmerkmal = 69L;
		daten.idEbene4 = 3L;

		mapper.patch(8L, daten, entity);

		assertThat(entity)
				.hasFieldOrPropertyWithValue("id", 5L)
				.hasFieldOrPropertyWithValue("idLernabschnitt", 8L)
				.hasFieldOrPropertyWithValue("jahrgang", "08")
				.hasFieldOrPropertyWithValue("idKategorie", 11L)
				.hasFieldOrPropertyWithValue("idMerkmal", 52L)
				.hasFieldOrPropertyWithValue("idZusatzmerkmal", 69L)
				.hasFieldOrPropertyWithValue("idEbene4", 3L)
				.hasFieldOrPropertyWithValue("idAnschlussoption", null)
				.hasFieldOrPropertyWithValue("bemerkung", null);
	}

	@Test
	@DisplayName("mapIdJahrgang | Liefert die ID des Jahrgangs im Schuljahr")
	void mapIdJahrgang_liefertIdImSchuljahr() {
		assertThat(mapper)
				.returns(7000000L, m -> m.mapIdJahrgang("07", 2020))
				.returns(ID_JAHRGANG_07, m -> m.mapIdJahrgang("07", SCHULJAHR));
	}

	@Test
	@DisplayName("mapIdJahrgang | Hin und zurück ergibt denselben Schlüssel, beide Wege liefern dieselbe ID")
	void mapIdJahrgang_hinUndZurueck() {
		final var id = mapper.mapIdJahrgang("07", SCHULJAHR);

		assertThat(id)
				.returns("07", mapper::mapAsdJahrgang)
				.returns(id, i -> Jahrgaenge.data().getEintragByID(i).id);
	}

}
