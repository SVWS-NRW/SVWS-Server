package de.svws_nrw.service.enm;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import de.svws_nrw.asd.types.kurse.ZulaessigeKursart;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;

@DisplayName("Schriftlichkeit der Kurse der gymnasialen Oberstufe im Notenmodul (ENM V1 und V2)")
class EnmGetServiceSchriftlichkeitTest {

	private static final Long ID_KURS = 1L;

	@BeforeAll
	static void init() {
		ASDCoreTypeUtils.initAll();
	}

	@ParameterizedTest(name = "{0} in {1}.{2} im Schuljahr {3}: schriftlich = {4}")
	@CsvSource({
			// das 4. und 5. Abiturfach sind bis Q2.1 schriftlich, in Q2.2 mündlich
			"AB4, Q2, 1, 2029, true",
			"AB4, Q2, 2, 2029, false",
			"AB5, Q2, 1, 2029, true",
			"AB5, Q2, 2, 2029, false",
			// Projektkurse sind ab dem Abitur 2030 in allen Halbjahren schriftlich (Produkte, § 14 APO-GOSt), davor nicht
			"PJK, Q2, 1, 2029, true",
			"PJK, Q2, 2, 2029, true",
			"PJK, Q1, 2, 2026, false",
			"PJK, Q2, 1, 2027, false",
			// Vertiefungskurse sind nie schriftlich
			"VTF, Q2, 1, 2029, false"
	})
	@DisplayName("pruefeKursSchriftlichkeit")
	void pruefeKursSchriftlichkeit(final ZulaessigeKursart kursart, final String klasse, final int halbjahr, final int schuljahr, final boolean erwartet) {
		assertThat(EnmV1GetService.pruefeKursSchriftlichkeit(ID_KURS, kursart, klasse, halbjahr, schuljahr)).as("ENM V1").isEqualTo(erwartet);
		assertThat(EnmV2GetService.pruefeKursSchriftlichkeit(ID_KURS, kursart, klasse, halbjahr, schuljahr)).as("ENM V2").isEqualTo(erwartet);
	}

}
