package de.svws_nrw.asd.types.schule;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Teste den Core-Type DQRNiveau")
class DQRNiveauTest {

	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}

	@Test
	@DisplayName("data: Anzahl der CoreType Einträge prüfen")
	void coreTypeSize() {
		assertThat(DQRNiveau.data().getWerte()).hasSize(8);
	}

	@Test
	@DisplayName("getWertByBezeichnerOrNull: Prüfe ob Wert mit Bezeichner 'DQR_NIVEAU_1' existiert")
	void getWertByBezeichnerOrNull_DQR_NIVEAU_1() {
		final DQRNiveau result = DQRNiveau.data().getWertByBezeichnerOrNull("DQR_NIVEAU_1");
		assertThat(result).isNotNull()
				.extracting(DQRNiveau::name, DQRNiveau::statistikId)
				.containsExactly("DQR_NIVEAU_1", "1");

		assertThat(result.historie()).hasSize(1)
				.element(0)
				.extracting(e -> e.id, e -> e.schluessel, e -> e.kuerzel, e -> e.text, e -> e.gueltigVon, e -> e.gueltigBis)
				.containsExactly(1L, "DQR_NIVEAU_1", "DQR Niveau 1", "Niveau 1 des Deutschen Qualifikationsrahmens (DQR)", null, null);
	}

}
