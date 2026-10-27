package de.svws_nrw.repo.kurse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKursSchueler;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKursSchuelerPK;

/**
 * Test für die Klasse {@link KursSchuelerRepositoryImpl}
 */
@ExtendWith(MockitoExtension.class)
class KursSchuelerRepositoryImplTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private KursSchuelerRepositoryImpl repository;


	@Test
	@DisplayName("Test: getMapByKurse() gruppiert die Einträge nach der Kurs-ID und ordnet Kursen ohne Schüler eine leere Liste zu.")
	void testGetMapByKurse() {
		final List<Long> idsKurse = List.of(1L, 2L);
		final DTOKursSchueler ks1 = new DTOKursSchueler(1L, 10L, 100L);
		final DTOKursSchueler ks2 = new DTOKursSchueler(1L, 11L, 101L);
		when(conn.queryList(KursSchuelerRepositoryImpl.QUERY_LIST_BY_KURS_ID_AKTUELL, DTOKursSchueler.class, idsKurse)).thenReturn(List.of(ks1, ks2));

		final Map<Long, List<DTOKursSchueler>> result = repository.getMapByKurse(idsKurse);

		assertThat(result).containsOnly(entry(1L, List.of(ks1, ks2)), entry(2L, List.of()));
	}

	@Test
	@DisplayName("Test: getMapByKurse() ignoriert Einträge zu Kursen, die nicht angefragt wurden.")
	void testGetMapByKurseIgnoriertUnbekannteKurse() {
		final List<Long> idsKurse = List.of(1L);
		final DTOKursSchueler ks1 = new DTOKursSchueler(1L, 10L, 100L);
		final DTOKursSchueler ksFremd = new DTOKursSchueler(99L, 10L, 101L);
		when(conn.queryList(KursSchuelerRepositoryImpl.QUERY_LIST_BY_KURS_ID_AKTUELL, DTOKursSchueler.class, idsKurse)).thenReturn(List.of(ks1, ksFremd));

		assertThat(repository.getMapByKurse(idsKurse)).containsOnly(entry(1L, List.of(ks1)));
	}

	@Test
	@DisplayName("Test: getMapByKurse() liefert bei leerer oder fehlender Eingabe eine leere Map ohne Datenbankzugriff.")
	void testGetMapByKurseLeereEingabe() {
		assertThat(repository.getMapByKurse(null)).isEmpty();
		assertThat(repository.getMapByKurse(List.of())).isEmpty();
		verifyNoInteractions(conn);
	}

	@Test
	@DisplayName("Test: getMapBySchueler() ohne Schuljahresabschnitt gruppiert die Einträge nach der Schüler-ID.")
	void testGetMapBySchuelerOhneAbschnitt() {
		final List<Long> idsSchueler = List.of(10L, 11L);
		final DTOKursSchueler ks1 = new DTOKursSchueler(1L, 10L, 100L);
		final DTOKursSchueler ks2 = new DTOKursSchueler(2L, 10L, 101L);
		when(conn.queryList(KursSchuelerRepositoryImpl.QUERY_LIST_BY_SCHUELER_ID_AKTUELL, DTOKursSchueler.class, idsSchueler)).thenReturn(List.of(ks1, ks2));

		final Map<Long, List<DTOKursSchueler>> result = repository.getMapBySchueler(idsSchueler, null);

		assertThat(result).containsOnly(entry(10L, List.of(ks1, ks2)), entry(11L, List.of()));
	}

	@Test
	@DisplayName("Test: getMapBySchueler() mit Schuljahresabschnitt nutzt die auf den Abschnitt eingeschränkte Abfrage.")
	void testGetMapBySchuelerMitAbschnitt() {
		final List<Long> idsSchueler = List.of(10L);
		final long idAbschnitt = 5L;
		final DTOKursSchueler ks1 = new DTOKursSchueler(1L, 10L, 100L);
		when(conn.queryList(KursSchuelerRepositoryImpl.QUERY_LIST_BY_SCHUELER_ID_AND_SCHULJAHRESABSCHNITT_AKTUELL, DTOKursSchueler.class, idAbschnitt,
				idsSchueler)).thenReturn(List.of(ks1));

		assertThat(repository.getMapBySchueler(idsSchueler, idAbschnitt)).containsOnly(entry(10L, List.of(ks1)));
	}

	@Test
	@DisplayName("Test: getMapBySchueler() liefert bei leerer oder fehlender Eingabe eine leere Map ohne Datenbankzugriff.")
	void testGetMapBySchuelerLeereEingabe() {
		assertThat(repository.getMapBySchueler(null, 5L)).isEmpty();
		assertThat(repository.getMapBySchueler(List.of(), null)).isEmpty();
		verifyNoInteractions(conn);
	}

	@Test
	@DisplayName("Test: Die Abfragen filtern auf den aktuellen Lernabschnitt.")
	void testQueriesFilternAufAktuellenLernabschnitt() {
		assertThat(KursSchuelerRepositoryImpl.QUERY_LIST_BY_KURS_ID_AKTUELL).contains("LernabschnittWechselNr = 0");
		assertThat(KursSchuelerRepositoryImpl.QUERY_LIST_BY_SCHUELER_ID_AKTUELL).contains("LernabschnittWechselNr = 0");
		assertThat(KursSchuelerRepositoryImpl.QUERY_LIST_BY_SCHUELER_ID_AND_SCHULJAHRESABSCHNITT_AKTUELL).contains("LernabschnittWechselNr = 0");
	}

	@Test
	@DisplayName("Test: getById() übergibt die vier Teile des Primärschlüssels an die Datenbank.")
	void testMapIdToParameter() {
		final DTOKursSchuelerPK pk = new DTOKursSchuelerPK(1L, 10L, 0, 100L);
		final DTOKursSchueler ks = new DTOKursSchueler(1L, 10L, 100L);
		when(conn.queryByKey(DTOKursSchueler.class, 1L, 10L, 0, 100L)).thenReturn(ks);

		assertThat(repository.getById(pk)).isSameAs(ks);
		verify(conn).queryByKey(DTOKursSchueler.class, 1L, 10L, 0, 100L);
	}

}
