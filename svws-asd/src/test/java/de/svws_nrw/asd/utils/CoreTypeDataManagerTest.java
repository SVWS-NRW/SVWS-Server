package de.svws_nrw.asd.utils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.svws_nrw.asd.data.CoreTypeData;
import de.svws_nrw.asd.types.CoreType;

@DisplayName("Tests fuer die Gueltigkeitspruefungen im CoreTypeDataManager")
class CoreTypeDataManagerTest {

	private enum TestCoreType implements CoreType<CoreTypeData, TestCoreType> {
		BEGRENZT,
		OFFEN_NACH_OBEN,
		NUR_BIS,
		UNBEGRENZT
	}

	private static CoreTypeData createEintrag(final long id, final Integer gueltigVon, final Integer gueltigBis, final int auslaufdauer) {
		final CoreTypeData eintrag = new CoreTypeData();
		eintrag.id = id;
		eintrag.schluessel = String.valueOf(id);
		eintrag.kuerzel = "K" + id;
		eintrag.text = "Eintrag " + id;
		eintrag.gueltigVon = gueltigVon;
		eintrag.gueltigBis = gueltigBis;
		eintrag.auslaufdauer = auslaufdauer;
		return eintrag;
	}

	private static CoreTypeDataManager<CoreTypeData, TestCoreType> createManager() {
		final Map<String, List<CoreTypeData>> daten = new HashMap<>();
		daten.put(TestCoreType.BEGRENZT.name(), List.of(createEintrag(100L, 2000, 2020, 2)));
		daten.put(TestCoreType.OFFEN_NACH_OBEN.name(), List.of(createEintrag(101L, 2021, null, 0)));
		daten.put(TestCoreType.NUR_BIS.name(), List.of(createEintrag(102L, null, 2022, 1)));
		daten.put(TestCoreType.UNBEGRENZT.name(), List.of(createEintrag(103L, null, null, 0)));
		final Map<String, String> idsStatistik = new HashMap<>();
		idsStatistik.put(TestCoreType.BEGRENZT.name(), "S100");
		idsStatistik.put(TestCoreType.OFFEN_NACH_OBEN.name(), "S101");
		idsStatistik.put(TestCoreType.NUR_BIS.name(), "S102");
		idsStatistik.put(TestCoreType.UNBEGRENZT.name(), "S103");
		return new CoreTypeDataManager<>(1L, TestCoreType.class, TestCoreType.values(), daten, idsStatistik);
	}

	@Test
	@DisplayName("isGueltig prueft ID und aktiven Historieneintrag")
	void testIsGueltig() {
		final CoreTypeDataManager<CoreTypeData, TestCoreType> manager = createManager();

		assertTrue(manager.isGueltig(100L, 2015));
		assertFalse(manager.isGueltig(100L, 2021));
		assertTrue(manager.isGueltig(101L, 2021));
		assertTrue(manager.isGueltig(102L, 2022));
		assertFalse(manager.isGueltig(102L, 2023));
		assertTrue(manager.isGueltig(103L, 2225));
		assertFalse(manager.isGueltig(999L, 2021));
	}

	@Test
	@DisplayName("isGueltigMitAuslaufdauer beruecksichtigt den Verlaengerungszeitraum")
	void testIsGueltigMitAuslaufdauer() {
		final CoreTypeDataManager<CoreTypeData, TestCoreType> manager = createManager();

		assertTrue(manager.isGueltigMitAuslaufdauer(100L, 2022));
		assertFalse(manager.isGueltigMitAuslaufdauer(100L, 2023));
		assertTrue(manager.isGueltigMitAuslaufdauer(101L, 2050));
		assertTrue(manager.isGueltigMitAuslaufdauer(102L, 2023));
		assertFalse(manager.isGueltigMitAuslaufdauer(102L, 2024));
		assertTrue(manager.isGueltigMitAuslaufdauer(103L, 2225));
		assertFalse(manager.isGueltigMitAuslaufdauer(999L, 2022));
	}
}
