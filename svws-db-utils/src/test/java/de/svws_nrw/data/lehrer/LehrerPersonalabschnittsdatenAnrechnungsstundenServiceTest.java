package de.svws_nrw.data.lehrer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdatenAnrechnungsstunden;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerAbschnittsdaten;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerAnrechnungsstunde;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerEntlastungsstunde;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerMehrleistung;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.repo.lehrer.LehrerAnrechnungRepository;
import de.svws_nrw.repo.lehrer.LehrerMehrleistungRepository;
import de.svws_nrw.repo.lehrer.LehrerMinderleistungRepository;
import de.svws_nrw.repo.schule.SchuljahresabschnitteRepository;

/**
 * Ein Test für den Service zur Aggrgeation der Anrechnungsstunden
 */
@ExtendWith(MockitoExtension.class)
class LehrerPersonalabschnittsdatenAnrechnungsstundenServiceTest {

	@Mock
	private SchuljahresabschnitteRepository repoAbschnitte;

	@Mock
	private LehrerMehrleistungRepository repoMehrleistung;

	@Mock
	private LehrerMinderleistungRepository repoMinderleistung;

	@Mock
	private LehrerAnrechnungRepository repoAnrechnung;

	@InjectMocks
	private LehrerPersonalabschnittsdatenAnrechnungsstundenService service;

	@BeforeAll
	static void init() {
		// Initialisierung der Core-Types, so dass eine Umwandlung möglich ist
		ASDCoreTypeUtils.initAll();
	}

	@Test
	@DisplayName("Test: Prüfe, ob getMapAnrechungen die Daten korrekt umwandelt.")
	void testGetMapAnrechungen() {
		// Ein Schuljahresabschnitt mit einer festen ID
		final long idSchuljahresabschnitt = 10L;
		final DTOSchuljahresabschnitte dtoAbschnitt = new DTOSchuljahresabschnitte(idSchuljahresabschnitt, 2026, 1);
		when(repoAbschnitte.getById(idSchuljahresabschnitt)).thenReturn(dtoAbschnitt);

		// Die Lehrer-Abschnittsdaten
		final long lehrerId = 42L;
		final long lehrerAbschnitt1Id = 100L;
		final DTOLehrerAbschnittsdaten lehrerAbschnitt = new DTOLehrerAbschnittsdaten(lehrerAbschnitt1Id, lehrerId, idSchuljahresabschnitt);

		// Eine Anrechnungsstunde mit gültigen Kürzel für den Grund
		final DTOLehrerAnrechnungsstunde dto1 = new DTOLehrerAnrechnungsstunde(500L, lehrerAbschnitt1Id);
		dto1.AnrechnungsgrundKrz = "500";
		dto1.AnrechnungStd = 2.0;

		// Eine Anrechnungsstunde mit fehlerhaftem Kürzel für den Grund
		final DTOLehrerAnrechnungsstunde dto2 = new DTOLehrerAnrechnungsstunde(500L, lehrerAbschnitt1Id);
		dto2.AnrechnungsgrundKrz = "FALSCH";

		// Und nochmal Lehrer-Abschnittsdaten, wo keine Anrechnungsstunden hinterlegt sind.
		final long lehrerAbschnitt2Id = 101L;
		final DTOLehrerAbschnittsdaten lehrerAbschnitt2 = new DTOLehrerAbschnittsdaten(lehrerAbschnitt2Id, lehrerId, idSchuljahresabschnitt);

		when(repoAnrechnung.getMapByAbschnitt(anyCollection())).thenReturn(
				Map.of(lehrerAbschnitt1Id, List.of(dto1, dto2), lehrerAbschnitt2Id, Collections.emptyList()));

		final Map<Long, List<LehrerPersonalabschnittsdatenAnrechnungsstunden>> result =
				service.getMapAnrechungen(List.of(lehrerAbschnitt, lehrerAbschnitt2));

		// Prüfe, ob zwei Einträge in der Map sind
		assertNotNull(result);
		assertEquals(2, result.size());
		assertTrue(result.containsKey(lehrerAbschnitt1Id));
		assertTrue(result.containsKey(lehrerAbschnitt2Id));

		// Prüfe, ob die Liste für die zweiten Lehrerabschnittsdaten leer ist
		assertTrue(result.get(lehrerAbschnitt2Id).isEmpty());

		// Prüfe, ob die erste Liste die beiden Einträge hat
		final var liste = result.get(lehrerAbschnitt1Id);
		assertEquals(2, liste.size());

		// Prüfe den ersten Eintrag
		final var eintrag1 = liste.get(0);
		assertEquals(500L, eintrag1.id);
		assertEquals(2.0, eintrag1.anzahl);
		assertEquals(500000, eintrag1.idGrund, "Prüfe, ob der Grund auch gemappt wurde.");

		// Prüfe den zweiten Eintrag
		final var eintrag2 = liste.get(1);
		assertNull(eintrag2.idGrund, "Bei einem ungültigem Kürzel für den Grund muss idGrund null sein.");
		assertEquals(0.0, eintrag2.anzahl, "Ein Wert von null muss auf 0.0 umgewandelt werden.");

		verify(repoAbschnitte, atLeastOnce()).getById(idSchuljahresabschnitt);
		verify(repoAnrechnung).getMapByAbschnitt(anyCollection());
	}


	@Test
	@DisplayName("Test: Prüfe, ob getMapMehrleistungen die Daten korrekt umwandelt.")
	void testGetMapMehrleistungen() {
		// Ein Schuljahresabschnitt mit einer festen ID
		final long idSchuljahresabschnitt = 10L;
		final DTOSchuljahresabschnitte dtoAbschnitt = new DTOSchuljahresabschnitte(idSchuljahresabschnitt, 2026, 1);
		when(repoAbschnitte.getById(idSchuljahresabschnitt)).thenReturn(dtoAbschnitt);

		// Die Lehrer-Abschnittsdaten
		final long lehrerId = 42L;
		final long lehrerAbschnitt1Id = 100L;
		final DTOLehrerAbschnittsdaten lehrerAbschnitt = new DTOLehrerAbschnittsdaten(lehrerAbschnitt1Id, lehrerId, idSchuljahresabschnitt);

		// Eine Mehrleistungsstunden mit gültigen Kürzel für den Grund
		final DTOLehrerMehrleistung dto1 = new DTOLehrerMehrleistung(500L, lehrerAbschnitt1Id, "160");
		dto1.MehrleistungStd = 2.0;

		// Eine Mehrleistungsstunden mit fehlerhaftem Kürzel für den Grund
		final DTOLehrerMehrleistung dto2 = new DTOLehrerMehrleistung(500L, lehrerAbschnitt1Id, "FALSCH");

		// Und nochmal Lehrer-Abschnittsdaten, wo keine Mehrleistungsstunden hinterlegt sind.
		final long lehrerAbschnitt2Id = 101L;
		final DTOLehrerAbschnittsdaten lehrerAbschnitt2 = new DTOLehrerAbschnittsdaten(lehrerAbschnitt2Id, lehrerId, idSchuljahresabschnitt);

		when(repoMehrleistung.getMapByAbschnitt(anyCollection())).thenReturn(
				Map.of(lehrerAbschnitt1Id, List.of(dto1, dto2), lehrerAbschnitt2Id, Collections.emptyList()));

		final Map<Long, List<LehrerPersonalabschnittsdatenAnrechnungsstunden>> result =
				service.getMapMehrleistungen(List.of(lehrerAbschnitt, lehrerAbschnitt2));

		// Prüfe, ob zwei Einträge in der Map sind
		assertNotNull(result);
		assertEquals(2, result.size());
		assertTrue(result.containsKey(lehrerAbschnitt1Id));
		assertTrue(result.containsKey(lehrerAbschnitt2Id));

		// Prüfe, ob die Liste für die zweiten Lehrerabschnittsdaten leer ist
		assertTrue(result.get(lehrerAbschnitt2Id).isEmpty());

		// Prüfe, ob die erste Liste die beiden Einträge hat
		final var liste = result.get(lehrerAbschnitt1Id);
		assertEquals(2, liste.size());

		// Prüfe den ersten Eintrag
		final var eintrag1 = liste.get(0);
		assertEquals(500L, eintrag1.id);
		assertEquals(2.0, eintrag1.anzahl);
		assertEquals(4, eintrag1.idGrund, "Prüfe, ob der Grund auch gemappt wurde.");

		// Prüfe den zweiten Eintrag
		final var eintrag2 = liste.get(1);
		assertNull(eintrag2.idGrund, "Bei einem ungültigem Kürzel für den Grund muss idGrund null sein.");
		assertEquals(0.0, eintrag2.anzahl, "Ein Wert von null muss auf 0.0 umgewandelt werden.");

		verify(repoAbschnitte, atLeastOnce()).getById(idSchuljahresabschnitt);
		verify(repoMehrleistung).getMapByAbschnitt(anyCollection());
	}


	@Test
	@DisplayName("Test: Prüfe, ob getMapMinderleistungen die Daten korrekt umwandelt.")
	void testGetMapMinderleistungen() {
		// Ein Schuljahresabschnitt mit einer festen ID
		final long idSchuljahresabschnitt = 10L;
		final DTOSchuljahresabschnitte dtoAbschnitt = new DTOSchuljahresabschnitte(idSchuljahresabschnitt, 2026, 1);
		when(repoAbschnitte.getById(idSchuljahresabschnitt)).thenReturn(dtoAbschnitt);

		// Die Lehrer-Abschnittsdaten
		final long lehrerId = 42L;
		final long lehrerAbschnitt1Id = 100L;
		final DTOLehrerAbschnittsdaten lehrerAbschnitt = new DTOLehrerAbschnittsdaten(lehrerAbschnitt1Id, lehrerId, idSchuljahresabschnitt);

		// Eine Minderleistungsstunden mit gültigen Kürzel für den Grund
		final DTOLehrerEntlastungsstunde dto1 = new DTOLehrerEntlastungsstunde(500L, lehrerAbschnitt1Id);
		dto1.EntlastungsgrundKrz = "260";
		dto1.EntlastungStd = 2.0;

		// Eine Minderleistungsstunden mit fehlerhaftem Kürzel für den Grund
		final DTOLehrerEntlastungsstunde dto2 = new DTOLehrerEntlastungsstunde(500L, lehrerAbschnitt1Id);
		dto2.EntlastungsgrundKrz = "FALSCH";

		// Und nochmal Lehrer-Abschnittsdaten, wo keine Minderleistungsstunden hinterlegt sind.
		final long lehrerAbschnitt2Id = 101L;
		final DTOLehrerAbschnittsdaten lehrerAbschnitt2 = new DTOLehrerAbschnittsdaten(lehrerAbschnitt2Id, lehrerId, idSchuljahresabschnitt);

		when(repoMinderleistung.getMapByAbschnitt(anyCollection())).thenReturn(
				Map.of(lehrerAbschnitt1Id, List.of(dto1, dto2), lehrerAbschnitt2Id, Collections.emptyList()));

		final Map<Long, List<LehrerPersonalabschnittsdatenAnrechnungsstunden>> result =
				service.getMapMinderleistungen(List.of(lehrerAbschnitt, lehrerAbschnitt2));

		// Prüfe, ob zwei Einträge in der Map sind
		assertNotNull(result);
		assertEquals(2, result.size());
		assertTrue(result.containsKey(lehrerAbschnitt1Id));
		assertTrue(result.containsKey(lehrerAbschnitt2Id));

		// Prüfe, ob die Liste für die zweiten Lehrerabschnittsdaten leer ist
		assertTrue(result.get(lehrerAbschnitt2Id).isEmpty());

		// Prüfe, ob die erste Liste die beiden Einträge hat
		final var liste = result.get(lehrerAbschnitt1Id);
		assertEquals(2, liste.size());

		// Prüfe den ersten Eintrag
		final var eintrag1 = liste.get(0);
		assertEquals(500L, eintrag1.id);
		assertEquals(2.0, eintrag1.anzahl);
		assertEquals(43, eintrag1.idGrund, "Prüfe, ob der Grund auch gemappt wurde.");

		// Prüfe den zweiten Eintrag
		final var eintrag2 = liste.get(1);
		assertNull(eintrag2.idGrund, "Bei einem ungültigem Kürzel für den Grund muss idGrund null sein.");
		assertEquals(0.0, eintrag2.anzahl, "Ein Wert von null muss auf 0.0 umgewandelt werden.");

		verify(repoAbschnitte, atLeastOnce()).getById(idSchuljahresabschnitt);
		verify(repoMinderleistung).getMapByAbschnitt(anyCollection());
	}

}
