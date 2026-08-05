package de.svws_nrw.asd.types.schule;

import java.util.List;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Teste den Core-Type Fachklasse")
class FachklasseTest {

	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}

	@Test
	@DisplayName("getWerte: Prüfe, dass mind. die initiale Anzahl an CoreType Werten vorhanden ist.")
	void coreTypeValues() {
		final List<Fachklasse> result = Fachklasse.data().getWerte();
		assertThat(result).isNotEmpty().hasSize(2175);
	}

	@Test
	@DisplayName("getWertByBezeichnerOrNull: Prüfe ob Wert mit Bezeichner 'fk10_10100' existiert")
	void getWertByBezeichner_fk10_10100() {
		final Fachklasse result = Fachklasse.data().getWertByBezeichnerOrNull("fk10_10100");
		assertThat(result).isNotNull()
				.extracting(Fachklasse::name, Fachklasse::statistikId)
				.containsExactly("fk10_10100", "1010100");

		assertThat(result.historie()).hasSize(1)
				.element(0)
				.extracting(e -> e.id,
						e -> e.schluessel,
						e -> e.kuerzel,
						e -> e.fkSchluessel,
						e -> e.fkSchluessel2,
						e -> e.bkIndex,
						e -> e.dqrNiveau,
						e -> e.istAusgelaufen,
						e -> e.berufsfeldGruppe,
						e -> e.berufsfeld,
						e -> e.ebene1,
						e -> e.ebene2,
						e -> e.ebene3,
						e -> e.text,
						e -> e.bezeichnungM,
						e -> e.bezeichnungW,
						e -> e.gueltigVon,
						e -> e.gueltigBis)
				.containsExactly(5000L,
						"10-10100",
						"Anlagenmechaniker/-in",
						"101",
						"00",
						10,
						"DQR_NIVEAU_4",
						false,
						"T",
						"MT",
						"Technik/Naturwissenschaften",
						null,
						"Anerkannter Ausbildungsberuf (3 1/2-jährig)",
						"Anlagenmechaniker/-in",
						"Anlagenmechaniker",
						"Anlagenmechanikerin",
						2004,
						null);
	}



}
