package de.svws_nrw.data.gost;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLeistungsdaten;

@DisplayName("Kursart der Fachwahl beim Übernehmen der Leistungsdaten in die Laufbahnplanung")
class DataGostJahrgangslisteKursartTest {

	@BeforeAll
	static void init() {
		ASDCoreTypeUtils.initAll();
	}

	@ParameterizedTest(name = "{0}/{1} in {2}, Abitur {3}: {4}")
	@CsvSource({
			"GK,  GKS, Q11, 2030, S",
			"GK,  GKM, Q11, 2030, M",
			// das 4. und 5. Abiturfach sind bis Q2.1 schriftlich, in Q2.2 mündlich
			"GK,  AB4, Q21, 2030, S",
			"GK,  AB4, Q22, 2030, M",
			"GK,  AB5, Q21, 2030, S",
			"GK,  AB5, Q22, 2030, M",
			// Projektkurse sind ab dem Abitur 2030 schriftlich (Produkte, § 14 APO-GOSt), davor mündlich
			"PJK, PJK, Q21, 2030, S",
			"PJK, PJK, Q22, 2030, S",
			"PJK, PJK, Q12, 2029, M",
			// Vertiefungskurse sind mündlich
			"VTF, VTF, EF1, 2030, M",
			"LK,  LK1, Q11, 2030, LK"
	})
	@DisplayName("getKursart")
	void getKursart(final String kursartAllg, final String kursart, final GostHalbjahr halbjahr, final int abiturjahr, final String erwartet) {
		final DTOSchuelerLeistungsdaten sld = new DTOSchuelerLeistungsdaten(1L, 2L, 3L);
		sld.KursartAllg = kursartAllg;
		sld.Kursart = kursart;
		sld.NotenKrz = "2";
		assertThat(DataGostJahrgangsliste.getKursart(sld, halbjahr, abiturjahr)).isEqualTo(erwartet);
	}

}
