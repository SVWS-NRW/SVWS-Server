package de.svws_nrw.repo.klassen;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassenLeitung;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassenLeitungPK;

@ExtendWith(MockitoExtension.class)
class KlassenleitungenRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private KlassenleitungenRepositoryImpl repository;

	@Test
	@DisplayName("Test: Prüfe, ob mapIdToParameter der PK korrekt in ein Array umwandelt.")
	void testMapIdToParameter() {
		final DTOKlassenLeitungPK pk = new DTOKlassenLeitungPK(10L, 20L);

		// Die Methode ist protected, wir können sie aber im Test innerhalb des Packages prüfen
		final Object[] params = repository.mapIdToParameter(pk);

		assertNotNull(params);
		assertEquals(2, params.length);
		assertEquals(10L, params[0]);
		assertEquals(20L, params[1]);
	}

	@Test
	@DisplayName("Test: Prüfe, ob getMapKlassenleitungen die Klassenleitungen nach Klassen gruppiert und die Leitungen dann auch sortiert")
	void testGetMapKlassenleitungen() {
		// Szenario: Drei Klassen, bei der ersten Klasse zwei Lehrer, bei der zweiten einer und bei der dritten kein Klassenlehrer
		final List<Long> klassenIds = Arrays.asList(1L, 2L, 3L);
		final DTOKlassenLeitung l1 = new DTOKlassenLeitung(1L, 101L, 2); // Zweiter Lehrer (sollte hinten stehen)
		final DTOKlassenLeitung l2 = new DTOKlassenLeitung(1L, 102L, 1); // Erster Lehrer (sollte vorne stehen)
		final DTOKlassenLeitung l3 = new DTOKlassenLeitung(2L, 103L, 1);

		when(conn.queryList(DTOKlassenLeitung.QUERY_LIST_BY_KLASSEN_ID, DTOKlassenLeitung.class, klassenIds))
				.thenReturn(Arrays.asList(l1, l2, l3));

		final Map<Long, List<Long>> result = repository.getMapKlassenleitungen(klassenIds);

		// Prüfe, ob es drei Klassen gibt...
		assertNotNull(result);
		assertEquals(3, result.size());

		// Prüfe, ob in Klasse 1 die Klassenleitungen sortiert zurückgegeben werden
		final List<Long> lehrerKlasse1 = result.get(1L);
		assertEquals(2, lehrerKlasse1.size());
		assertEquals(102L, lehrerKlasse1.get(0));
		assertEquals(101L, lehrerKlasse1.get(1));

		// Prüfe, ob in Klasse 2 eine Klassenleitung zurückgegeben wird
		assertEquals(1, result.get(2L).size());
		assertEquals(103L, result.get(2L).get(0));

		// Prüfe, ob in Klasse 3, dass kein Eintrag vorliegt
		assertTrue(result.get(3L).isEmpty());

		verify(conn).queryList(DTOKlassenLeitung.QUERY_LIST_BY_KLASSEN_ID, DTOKlassenLeitung.class, klassenIds);
	}

	@Test
	@DisplayName("Test: Prüfe, ob getMapKlassenleitungen bei null oder leerer Eingabe auch eine leere Map liefert.")
	void testGetMapKlassenleitungenEmpty() {
		assertTrue(repository.getMapKlassenleitungen(null).isEmpty());
		assertTrue(repository.getMapKlassenleitungen(List.of()).isEmpty());
		verifyNoInteractions(conn);
	}

}
