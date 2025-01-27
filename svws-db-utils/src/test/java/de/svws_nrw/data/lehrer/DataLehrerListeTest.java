package de.svws_nrw.data.lehrer;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.lehrer.LehrerListeEintrag;
import de.svws_nrw.core.types.PersonalTyp;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerAbschnittsdaten;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Diese Klasse testet die Klasse DataLehrerListe")
@ExtendWith(MockitoExtension.class)
class DataLehrerListeTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private DataLehrerliste dataLehrerliste;

	@BeforeAll
	static void setUp() {
		ASDCoreTypeUtils.initAll();
	}

	@Test
	@DisplayName("map | erfolgreiches mapping | check Basic Attributes")
	void mapTest() {
		final var dtoLehrer = getDtoLehrer();

		assertThat(this.dataLehrerliste.map(dtoLehrer))
				.isInstanceOf(LehrerListeEintrag.class)
				.hasFieldOrPropertyWithValue("id", 1L)
				.hasFieldOrPropertyWithValue("kuerzel", "abc")
				.hasFieldOrPropertyWithValue("titel", "abc")
				.hasFieldOrPropertyWithValue("nachname", "abc")
				.hasFieldOrPropertyWithValue("vorname", "abc")
				.hasFieldOrPropertyWithValue("personTyp", PersonalTyp.LEHRKRAFT.kuerzel)
				.hasFieldOrPropertyWithValue("sortierung", 100)
				.hasFieldOrPropertyWithValue("istSichtbar", true)
				.hasFieldOrPropertyWithValue("istRelevantFuerStatistik", true);
	}

	@Test
	@DisplayName("map | erfolgreiches Mapping | some Values null")
	void mapTest_someValuesNull() {
		final var dtoLehrer = getDtoLehrer();
		dtoLehrer.Titel = null;
		dtoLehrer.Nachname = null;
		dtoLehrer.Vorname = null;
		dtoLehrer.PersonTyp = null;
		dtoLehrer.Sortierung = null;
		dtoLehrer.Sichtbar = null;
		dtoLehrer.statistikRelevant = null;

		assertThat(this.dataLehrerliste.map(dtoLehrer))
				.isInstanceOf(LehrerListeEintrag.class)
				.hasFieldOrPropertyWithValue("titel", "")
				.hasFieldOrPropertyWithValue("nachname", "")
				.hasFieldOrPropertyWithValue("vorname", "")
				.hasFieldOrPropertyWithValue("personTyp", "")
				.hasFieldOrPropertyWithValue("sortierung", 32000)
				.hasFieldOrPropertyWithValue("istSichtbar", true)
				.hasFieldOrPropertyWithValue("istRelevantFuerStatistik", true);
	}

	@Test
	@DisplayName("getLehrerListe | ohne Referenz Info | Erfolg")
	void getLehrerListeWithoutReferenceTest() {
		when(conn.queryAll(DTOLehrer.class)).thenReturn(List.of(getDtoLehrer()));

		assertThat(this.dataLehrerliste.getLehrerListe(false))
				.isInstanceOf(List.class)
				.isNotNull()
				.isNotEmpty()
				.hasSize(1)
				.allSatisfy(item -> assertThat(item)
						.isInstanceOf(LehrerListeEintrag.class)
						.hasFieldOrPropertyWithValue("id", 1L)
						.hasFieldOrPropertyWithValue("kuerzel", "abc")
						.hasFieldOrPropertyWithValue("titel", "abc")
						.hasFieldOrPropertyWithValue("nachname", "abc")
						.hasFieldOrPropertyWithValue("vorname", "abc")
						.hasFieldOrPropertyWithValue("personTyp", PersonalTyp.LEHRKRAFT.kuerzel)
						.hasFieldOrPropertyWithValue("sortierung", 100)
						.hasFieldOrPropertyWithValue("istSichtbar", true)
						.hasFieldOrPropertyWithValue("istRelevantFuerStatistik", true)
						.hasFieldOrPropertyWithValue("referenziertInAnderenTabellen", null));
		verify(conn, never()).query(any(), any());
	}

	@Test
	@DisplayName("getLehrerListe | ohne Referenz Info | leere Liste | Erfolg")
	void getLehrerListeWithoutReferenceEmptyListTest() {
		when(conn.queryAll(DTOLehrer.class)).thenReturn(Collections.emptyList());

		assertThat(this.dataLehrerliste.getLehrerListe(false))
				.isInstanceOf(List.class)
				.isEmpty();
		verify(conn, never()).query(any(), any());
	}

	@Test
	@DisplayName("getLehrerListe | mit Referenz Info | Erfolg")
	void getLehrerListeWithReferenceTest() {
		when(conn.queryAll(DTOLehrer.class)).thenReturn(List.of(getDtoLehrer()));
		final TypedQuery<Long> queryMock = mock(TypedQuery.class);
		when(queryMock.setParameter(eq("idsLehrer"), any())).thenReturn(queryMock);
		when(queryMock.getResultList()).thenReturn(List.of(1L));
		when(conn.query(anyString(), eq(Long.class))).thenReturn(queryMock);

		assertThat(this.dataLehrerliste.getLehrerListe(true))
				.isInstanceOf(List.class)
				.isNotNull()
				.isNotEmpty()
				.hasSize(1)
				.allSatisfy(item -> assertThat(item)
						.isInstanceOf(LehrerListeEintrag.class)
						.hasFieldOrPropertyWithValue("id", 1L)
						.hasFieldOrPropertyWithValue("kuerzel", "abc")
						.hasFieldOrPropertyWithValue("titel", "abc")
						.hasFieldOrPropertyWithValue("nachname", "abc")
						.hasFieldOrPropertyWithValue("vorname", "abc")
						.hasFieldOrPropertyWithValue("personTyp", PersonalTyp.LEHRKRAFT.kuerzel)
						.hasFieldOrPropertyWithValue("sortierung", 100)
						.hasFieldOrPropertyWithValue("istSichtbar", true)
						.hasFieldOrPropertyWithValue("istRelevantFuerStatistik", true)
						.hasFieldOrPropertyWithValue("referenziertInAnderenTabellen", true));
	}

	@Test
	@DisplayName("getLehrerListe | mit Referenz Info | leere Liste | Erfolg")
	void getLehrerListeWithReferenceEmptyListTest() {
		when(conn.queryAll(DTOLehrer.class)).thenReturn(Collections.emptyList());

		assertThat(this.dataLehrerliste.getLehrerListe(true))
				.isInstanceOf(List.class)
				.isEmpty();
		verify(conn, never()).query(any(), any());
	}

	@Test
	@DisplayName("getAllListe | Erfolg")
	void getAllTest() {
		when(conn.queryAll(DTOLehrer.class)).thenReturn(List.of(getDtoLehrer()));
		final TypedQuery<Long> queryMock = mock(TypedQuery.class);
		when(queryMock.setParameter(eq("idsLehrer"), any())).thenReturn(queryMock);
		when(queryMock.getResultList()).thenReturn(List.of(1L));
		when(conn.query(anyString(), eq(Long.class))).thenReturn(queryMock);

		assertThat(this.dataLehrerliste.getAll())
				.isInstanceOf(List.class)
				.isNotNull()
				.isNotEmpty()
				.hasSize(1)
				.allSatisfy(item -> assertThat(item)
						.isInstanceOf(LehrerListeEintrag.class)
						.hasFieldOrPropertyWithValue("id", 1L)
						.hasFieldOrPropertyWithValue("kuerzel", "abc")
						.hasFieldOrPropertyWithValue("titel", "abc")
						.hasFieldOrPropertyWithValue("nachname", "abc")
						.hasFieldOrPropertyWithValue("vorname", "abc")
						.hasFieldOrPropertyWithValue("personTyp", PersonalTyp.LEHRKRAFT.kuerzel)
						.hasFieldOrPropertyWithValue("sortierung", 100)
						.hasFieldOrPropertyWithValue("istSichtbar", true)
						.hasFieldOrPropertyWithValue("istRelevantFuerStatistik", true)
						.hasFieldOrPropertyWithValue("referenziertInAnderenTabellen", true));
	}

	@Test
	@DisplayName("getAll | mit Referenz Info | leere Liste | Erfolg")
	void getAllEmptyListTest() {
		when(conn.queryAll(DTOLehrer.class)).thenReturn(Collections.emptyList());

		assertThat(this.dataLehrerliste.getAll())
				.isInstanceOf(List.class)
				.isEmpty();
		verify(conn, never()).query(any(), any());
	}


	@Test
	@DisplayName("getList | Erfolg")
	void getListTest() {
		when(this.conn.queryList(DTOLehrer.QUERY_BY_SICHTBAR, DTOLehrer.class, true)).thenReturn(List.of(getDtoLehrer()));

		assertThat(this.dataLehrerliste.getList())
				.isInstanceOf(List.class)
				.isNotNull()
				.allSatisfy(item -> assertThat(item)
						.isInstanceOf(LehrerListeEintrag.class)
						.hasFieldOrPropertyWithValue("id", 1L)
						.hasFieldOrPropertyWithValue("kuerzel", "abc")
						.hasFieldOrPropertyWithValue("titel", "abc")
						.hasFieldOrPropertyWithValue("nachname", "abc")
						.hasFieldOrPropertyWithValue("vorname", "abc")
						.hasFieldOrPropertyWithValue("personTyp", PersonalTyp.LEHRKRAFT.kuerzel)
						.hasFieldOrPropertyWithValue("sortierung", 100)
						.hasFieldOrPropertyWithValue("istSichtbar", true)
						.hasFieldOrPropertyWithValue("istRelevantFuerStatistik", true));
	}

	@Test
	@DisplayName("getList | emptyList")
	void getListTest_emptyList() {
		when(this.conn.queryList(DTOLehrer.QUERY_BY_SICHTBAR, DTOLehrer.class, true)).thenReturn(Collections.emptyList());

		assertThat(this.dataLehrerliste.getList())
				.isInstanceOf(List.class)
				.isEmpty();
	}

	@Test
	@DisplayName("getDTOMapAbschnittsdatenByID | Erfolg")
	void getDtoMapAbschnittsdatenByIDTest() {
		when(conn.queryList(DTOLehrerAbschnittsdaten.QUERY_BY_SCHULJAHRESABSCHNITTS_ID + " AND e.Lehrer_ID IN ?2", DTOLehrerAbschnittsdaten.class,
				3L, List.of(2L))).thenReturn(List.of(new DTOLehrerAbschnittsdaten(1L, 2L, 3L)));

		assertThat(this.dataLehrerliste.getDTOAbschnittsdatenByID(List.of(2L), 3L))
				.isInstanceOf(Map.class)
				.isNotEmpty()
				.allSatisfy((k, v) -> assertThat(v)
						.hasFieldOrPropertyWithValue("Lehrer_ID", 2L)
						.hasFieldOrPropertyWithValue("Schuljahresabschnitts_ID", 3L));
	}

	@Test
	@DisplayName("getDTOMapAbschnittsdatenByID | emptyList |Erfolg")
	void getDtoMapAbschnittsdatenByIDTest_emptyList() {
		when(conn.queryList(DTOLehrerAbschnittsdaten.QUERY_BY_SCHULJAHRESABSCHNITTS_ID + " AND e.Lehrer_ID IN ?2", DTOLehrerAbschnittsdaten.class,
				3L, List.of(2L))).thenReturn(Collections.emptyList());

		assertThat(this.dataLehrerliste.getDTOAbschnittsdatenByID(List.of(2L), 3L))
				.isInstanceOf(Map.class)
				.isEmpty();
	}

	@Test
	@DisplayName("getDTOMapAbschnittsdatenByID | emptyList ids |Erfolg")
	void getDtoMapAbschnittsdatenByIDTest_emptyListIds() {
		assertThat(this.dataLehrerliste.getDTOAbschnittsdatenByID(Collections.emptyList(), 1))
				.isInstanceOf(Map.class)
				.isEmpty();
		verify(conn, never()).queryList(any(), any(), any(), any());
	}

	@Test
	@DisplayName("getLongId | Erfolg")
	void getLongIdTest() {
		assertThat(this.dataLehrerliste.getLongId(getDtoLehrer()))
				.isInstanceOf(Long.class)
				.isEqualTo(1L);
	}

	@Test
	@DisplayName("checkBeforeDeletionWithSimpleOperationResponseTest | Erfolg")
	void checkBeforeDeletionWithSimpleOperationResponseTest() {
		final TypedQuery<Long> queryMock = mock(TypedQuery.class);
		when(queryMock.setParameter(eq("idsLehrer"), any())).thenReturn(queryMock);
		when(queryMock.getResultList()).thenReturn(List.of(1L));
		when(conn.query(anyString(), eq(Long.class))).thenReturn(queryMock);
		final var lehrer = List.of(getDtoLehrer());
		final var response = new SimpleOperationResponse();
		response.id = 1L;
		response.success = true;
		final var responses = Map.of(response.id, response);

		this.dataLehrerliste.checkBeforeDeletionWithSimpleOperationResponse(lehrer, responses);

		assertThat(responses.get(1L))
				.hasFieldOrPropertyWithValue("success", false)
				.extracting(r -> r.log.getFirst())
				.isEqualTo("Der Lehrer mit dem Kuerzel abc und der id 1 ist in der Datenbank referenziert und kann daher nicht gelöscht werden");
	}

	@Test
	@DisplayName("checkBeforeDeletionWithSimpleOperationResponseTest | Lehrer nicht referenziert | Erfolg")
	void checkBeforeDeletionWithSimpleOperationResponseTest_lehrerNotReferenced() {
		final TypedQuery<Long> queryMock = mock(TypedQuery.class);
		when(queryMock.setParameter(eq("idsLehrer"), any())).thenReturn(queryMock);
		when(queryMock.getResultList()).thenReturn(List.of(2L));
		when(conn.query(anyString(), eq(Long.class))).thenReturn(queryMock);
		final var lehrer = List.of(getDtoLehrer());
		final var response = new SimpleOperationResponse();
		response.id = 1L;
		final var responses = Map.of(response.id, response);

		this.dataLehrerliste.checkBeforeDeletionWithSimpleOperationResponse(lehrer, responses);

		assertThat(response.log).isEmpty();
	}

	private DTOLehrer getDtoLehrer() {
		final var dtoLehrer = new DTOLehrer(1L, "abc", "abc");
		dtoLehrer.Titel = "abc";
		dtoLehrer.Vorname = "abc";
		dtoLehrer.PersonTyp = PersonalTyp.LEHRKRAFT;
		dtoLehrer.Sortierung = 100;
		dtoLehrer.Sichtbar = true;
		dtoLehrer.statistikRelevant = true;
		return dtoLehrer;
	}
}
