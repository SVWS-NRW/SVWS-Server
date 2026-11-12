package de.svws_nrw.mapper.lehrer.liste;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.types.PersonalTyp;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests für den {@link LehrerListeEintragMapper}.
 */
class LehrerListeEintragMapperTest {

	private final LehrerListeEintragMapper mapper = LehrerListeEintragMapper.INSTANCE;

	@BeforeAll
	static void setUpAll() {
		ASDCoreTypeUtils.initAll();
	}

	private static DTOLehrer entity() {
		final var entity = new DTOLehrer(1L, "MUS", "Mustermann");
		entity.Titel = "Dr.";
		entity.Vorname = "Max";
		entity.PersonTyp = PersonalTyp.LEHRKRAFT;
		entity.Sortierung = 100;
		entity.Sichtbar = false;
		entity.statistikRelevant = false;
		return entity;
	}

	@Test
	@DisplayName("toApi | übernimmt alle gesetzten Werte")
	void toApi_uebernimmtWerte() {
		final var result = mapper.toApi(entity());

		assertThat(result).isNotNull()
				.hasFieldOrPropertyWithValue("id", 1L)
				.hasFieldOrPropertyWithValue("kuerzel", "MUS")
				.hasFieldOrPropertyWithValue("titel", "Dr.")
				.hasFieldOrPropertyWithValue("nachname", "Mustermann")
				.hasFieldOrPropertyWithValue("vorname", "Max")
				.hasFieldOrPropertyWithValue("personTyp", PersonalTyp.LEHRKRAFT.kuerzel)
				.hasFieldOrPropertyWithValue("sortierung", 100)
				.hasFieldOrPropertyWithValue("istSichtbar", false)
				.hasFieldOrPropertyWithValue("istRelevantFuerStatistik", false);
	}

	@Test
	@DisplayName("toApi | setzt Defaults für fehlende Werte")
	void toApi_defaults() {
		final var entity = entity();
		entity.Titel = null;
		entity.Nachname = null;
		entity.Vorname = null;
		entity.PersonTyp = null;
		entity.Sortierung = null;
		entity.Sichtbar = null;
		entity.statistikRelevant = null;

		assertThat(mapper.toApi(entity))
				.hasFieldOrPropertyWithValue("titel", "")
				.hasFieldOrPropertyWithValue("nachname", "")
				.hasFieldOrPropertyWithValue("vorname", "")
				.hasFieldOrPropertyWithValue("personTyp", "")
				.hasFieldOrPropertyWithValue("sortierung", LehrerListeEintragMapper.DEFAULT_SORTIERUNG)
				.hasFieldOrPropertyWithValue("istSichtbar", true)
				.hasFieldOrPropertyWithValue("istRelevantFuerStatistik", true);
	}

	@Test
	@DisplayName("toApi | setzt weder Aktivität noch Referenzinformation")
	void toApi_ignoriertServiceFelder() {
		assertThat(mapper.toApi(entity()))
				.hasFieldOrPropertyWithValue("istAktiv", true)
				.hasFieldOrPropertyWithValue("referenziertInAnderenTabellen", null);
	}

	@Test
	@DisplayName("toApi | liefert null für null")
	void toApi_null() {
		assertThat(mapper.toApi(null)).isNull();
	}

}
