package de.svws_nrw.data.lehrer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.asd.data.lehrer.LehrerFachrichtungEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerLehramtEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerLehrbefaehigungEintrag;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerPersonaldatenLehramt;
import de.svws_nrw.repo.lehrer.LehrerPersonaldatenLehramtRepository;

/**
 * Tests für den Service zu den Lehrämtern bei Lehrern.
 */
@ExtendWith(MockitoExtension.class)
class LehrerLehramtServiceTest {

	@Mock
	private LehrerPersonaldatenLehramtRepository repoLehramt;

	@Mock
	private LehrerFachrichtungService serviceFachrichtungen;

	@Mock
	private LehrerLehrbefaehigungService serviceLehrbefaehigungen;

	@InjectMocks
	private LehrerLehramtService service;

	@Test
	@DisplayName("Test: getMapByLehrer führt die Daten aus dem Repository und den beiden Services korrekt zusammen")
	void testGetMapByLehrer() {
		// Die Daten zu dem Lehrer aus dem Repository
		final long lehrerId = 42L;
		final long lehramtId = 1001L;
		final DTOLehrerPersonaldatenLehramt dto = new DTOLehrerPersonaldatenLehramt(lehramtId, lehrerId);
		dto.Lehramt_Katalog_ID = 10L;
		when(repoLehramt.findListByIds(anyCollection())).thenReturn(List.of(dto));

		// Die Daten zu den Fachrichtungen aus dem einen Service
		final LehrerFachrichtungEintrag fachrichtung = new LehrerFachrichtungEintrag();
		fachrichtung.id = 200L;
		fachrichtung.idLehramt = lehramtId;
		when(serviceFachrichtungen.getMapByLehramt(List.of(lehramtId)))
				.thenReturn(Map.of(lehramtId, List.of(fachrichtung)));

		// Die Daten zu den Lehrbefähigungen aus dem anderen Service
		final LehrerLehrbefaehigungEintrag befaehigung = new LehrerLehrbefaehigungEintrag();
		befaehigung.id = 300L;
		befaehigung.idLehramt = lehramtId;
		when(serviceLehrbefaehigungen.getMapByLehramt(List.of(lehramtId)))
				.thenReturn(Map.of(lehramtId, List.of(befaehigung)));

		// Aufruf von getMapByLehrer
		final Map<Long, List<LehrerLehramtEintrag>> result = service.getMapByLehrer(List.of(lehrerId));

		// Prüfe, ob das Ergebnis eine Map mit einem Eintrag zu dem Lehrer ist.
		assertNotNull(result);
		assertEquals(1, result.size());
		assertTrue(result.containsKey(lehrerId));

		// Prüfe, ob genau ein Lehramt in der Rückgabeliste ist.
		final List<LehrerLehramtEintrag> eintraege = result.get(lehrerId);
		assertEquals(1, eintraege.size());

		// Prüfe, ob der eine Lehramt-Eintrag korrekt erstellt wurde.
		final LehrerLehramtEintrag eintrag = eintraege.get(0);
		assertEquals(lehramtId, eintrag.id);
		assertEquals(10L, eintrag.idKatalogLehramt);

		assertEquals(1, eintrag.fachrichtungen.size());
		assertEquals(200L, eintrag.fachrichtungen.get(0).id);

		assertEquals(1, eintrag.lehrbefaehigungen.size());
		assertEquals(300L, eintrag.lehrbefaehigungen.get(0).id);

		// Prüfe, ob die beiden Services mit der korrekten Lehramt-ID aufgerufen wurden
		verify(serviceFachrichtungen).getMapByLehramt(argThat(c -> c.contains(lehramtId)));
		verify(serviceLehrbefaehigungen).getMapByLehramt(argThat(c -> c.contains(lehramtId)));
	}
}
