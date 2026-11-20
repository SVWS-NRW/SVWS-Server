package de.svws_nrw.core.types.gost;

import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import de.svws_nrw.asd.types.kurse.ZulaessigeKursart;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;

@DisplayName("Diese Testklasse testet die Zuordnung der zulässigen Kursarten zu den Kursarten der gymnasialen Oberstufe")
class GostKursartTest {

	@BeforeAll
	static void init() {
		ASDCoreTypeUtils.initAll();
	}

	@ParameterizedTest(name = "fromKursart({0}) liefert GK")
	@EnumSource(value = ZulaessigeKursart.class, names = { "GKM", "GKS", "AB3", "AB4", "AB5", "EFSP" })
	@DisplayName("fromKursart | die Kursarten der Grundkurse einschließlich des 5. Abiturfachs gehören zum Grundkurs")
	void testFromKursartGrundkurs(final ZulaessigeKursart kursart) {
		assertSame(GostKursart.GK, GostKursart.fromKursart(kursart));
	}

	@ParameterizedTest(name = "fromKursart({0}) liefert {0}")
	@EnumSource(value = ZulaessigeKursart.class, names = { "PJK", "VTF", "ZK" })
	@DisplayName("fromKursart | Projekt-, Vertiefungs- und Zusatzkurse behalten ihre eigene Kursart")
	void testFromKursartSonstige(final ZulaessigeKursart kursart) {
		assertSame(GostKursart.valueOf(kursart.name()), GostKursart.fromKursart(kursart));
	}

}
