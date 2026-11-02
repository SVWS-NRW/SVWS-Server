package de.svws_nrw.mapper.gost;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.asd.types.schule.Schulgliederung;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;

/**
 * Testet den Mapper für die Bestimmung der Liste der Abiturjahrgänge
 */
@DisplayName("GostJahrgangMapper")
class GostJahrgangMapperTest {

	private static final int SCHULJAHR = 2025;

	private final GostJahrgangMapper mapper = GostJahrgangMapper.INSTANCE;

	@BeforeAll
	static void setUp() {
		ASDCoreTypeUtils.initAll();
	}

	@Test
	@DisplayName("toSchulJahrgang übernimmt das Kürzel des ASD-Jahrgangs und löst die Schulgliederung auf")
	void toSchulJahrgang_uebernimmtKuerzelUndGliederung() {
		final var entity = new DTOJahrgang(1L);
		entity.ASDJahrgang = "EF";
		entity.GliederungKuerzel = Schulgliederung.DEFAULT.daten(SCHULJAHR).kuerzel;

		final var result = mapper.toSchulJahrgang(entity);

		assertThat(result.kuerzelASDJahrgang()).isEqualTo("EF");
		assertThat(result.gliederung()).isNotNull().isEqualTo(Schulgliederung.data().getWertByKuerzel(entity.GliederungKuerzel));
	}

}
