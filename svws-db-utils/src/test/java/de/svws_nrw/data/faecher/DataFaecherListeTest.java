package de.svws_nrw.data.faecher;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.fach.FaecherListeEintrag;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
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

@DisplayName("Diese Klasse testet die Klasse DataFaecherListe")
@ExtendWith(MockitoExtension.class)
class DataFaecherListeTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private DataFaecherliste dataFaecherliste;

	@BeforeAll
	static void setUp() {
		ASDCoreTypeUtils.initAll();
	}
	@Test
	@DisplayName("map | erfolgreiches mapping | check Basic Attributes")
	void mapTest() {
		final var dtoFach = getDtoFach();
		assertThat(this.dataFaecherliste.map(dtoFach))
				.isInstanceOf(FaecherListeEintrag.class)
				.hasFieldOrPropertyWithValue("id", 1L)
				.hasFieldOrPropertyWithValue("kuerzel", "M")
				.hasFieldOrPropertyWithValue("kuerzelStatistik", "M")
				.hasFieldOrPropertyWithValue("bezeichnung", "Mathematik")
				.hasFieldOrPropertyWithValue("istOberstufenFach", true)
				.hasFieldOrPropertyWithValue("istPruefungsordnungsRelevant", true)
				.hasFieldOrPropertyWithValue("sortierung", 1)
				.hasFieldOrPropertyWithValue("istSichtbar", true)
				.hasFieldOrPropertyWithValue("aufgabenfeld", null)
				.hasFieldOrPropertyWithValue("bilingualeSprache", null)
				.hasFieldOrPropertyWithValue("istNachpruefungErlaubt", true)
				.hasFieldOrPropertyWithValue("aufZeugnis", true)
				.hasFieldOrPropertyWithValue("bezeichnungZeugnis", "Mathematik")
				.hasFieldOrPropertyWithValue("bezeichnungUeberweisungszeugnis", "Mathematik")
				.hasFieldOrPropertyWithValue("maxZeichenInFachbemerkungen", 2)
				.hasFieldOrPropertyWithValue("istSchriftlichZK", true)
				.hasFieldOrPropertyWithValue("istSchriftlichBA", true)
				.hasFieldOrPropertyWithValue("istFHRFach", false)
				.hasFieldOrPropertyWithValue("holeAusAltenLernabschnitten", true);
	}

	@Test
	@DisplayName("getLongId | Erfolg")
	void getLongIdTest() {
		assertThat(this.dataFaecherliste.getLongId(getDtoFach()))
				.isInstanceOf(Long.class)
				.isEqualTo(1L);
	}

	@Test
	@DisplayName("getFaecherListe | ohne Referenz Info | Erfolg")
	void getFaecherListeWithoutReferenceTest() {
		when(conn.queryAll(DTOFach.class)).thenReturn(List.of(getDtoFach()));

		assertThat(this.dataFaecherliste.getFaecherListe(false))
				.isInstanceOf(List.class)
				.isNotNull()
				.isNotEmpty()
				.hasSize(1)
				.allSatisfy(item -> assertThat(item)
						.isInstanceOf(FaecherListeEintrag.class)
						.hasFieldOrPropertyWithValue("id", 1L)
						.hasFieldOrPropertyWithValue("kuerzel", "M")
						.hasFieldOrPropertyWithValue("kuerzelStatistik", "M")
						.hasFieldOrPropertyWithValue("bezeichnung", "Mathematik")
						.hasFieldOrPropertyWithValue("istOberstufenFach", true)
						.hasFieldOrPropertyWithValue("istPruefungsordnungsRelevant", true)
						.hasFieldOrPropertyWithValue("sortierung", 1)
						.hasFieldOrPropertyWithValue("istSichtbar", true)
						.hasFieldOrPropertyWithValue("aufgabenfeld", null)
						.hasFieldOrPropertyWithValue("bilingualeSprache", null)
						.hasFieldOrPropertyWithValue("istNachpruefungErlaubt", true)
						.hasFieldOrPropertyWithValue("aufZeugnis", true)
						.hasFieldOrPropertyWithValue("bezeichnungZeugnis", "Mathematik")
						.hasFieldOrPropertyWithValue("bezeichnungUeberweisungszeugnis", "Mathematik")
						.hasFieldOrPropertyWithValue("maxZeichenInFachbemerkungen", 2)
						.hasFieldOrPropertyWithValue("istSchriftlichZK", true)
						.hasFieldOrPropertyWithValue("istSchriftlichBA", true)
						.hasFieldOrPropertyWithValue("istFHRFach", false)
						.hasFieldOrPropertyWithValue("holeAusAltenLernabschnitten", true)
						.hasFieldOrPropertyWithValue("referenziertInAnderenTabellen", null));
		verify(conn, never()).query(any(), any());
	}

	@Test
	@DisplayName("getFaecherListe | ohne Referenz Info | leere Liste | Erfolg")
	void getFaecherListeWithoutReferenceEmptyListTest() {
		when(conn.queryAll(DTOFach.class)).thenReturn(Collections.emptyList());

		assertThat(this.dataFaecherliste.getFaecherListe(false))
				.isInstanceOf(List.class)
				.isEmpty();
		verify(conn, never()).query(any(), any());
	}

	@Test
	@SuppressWarnings("unchecked")
	@DisplayName("getFaecherListe | mit Referenz Info | Erfolg")
	void getFaecherListeWithReferenceTest() {
		when(conn.queryAll(DTOFach.class)).thenReturn(List.of(getDtoFach()));
		final TypedQuery<Long> queryMock = mock(TypedQuery.class);
		when(queryMock.setParameter(eq("idsFaecher"), any())).thenReturn(queryMock);
		when(queryMock.getResultList()).thenReturn(List.of(1L));
		when(conn.query(anyString(), eq(Long.class))).thenReturn(queryMock);

		assertThat(this.dataFaecherliste.getFaecherListe(true))
				.isInstanceOf(List.class)
				.isNotNull()
				.isNotEmpty()
				.hasSize(1)
				.allSatisfy(item -> assertThat(item)
						.isInstanceOf(FaecherListeEintrag.class)
						.hasFieldOrPropertyWithValue("id", 1L)
						.hasFieldOrPropertyWithValue("kuerzel", "M")
						.hasFieldOrPropertyWithValue("kuerzelStatistik", "M")
						.hasFieldOrPropertyWithValue("bezeichnung", "Mathematik")
						.hasFieldOrPropertyWithValue("istOberstufenFach", true)
						.hasFieldOrPropertyWithValue("istPruefungsordnungsRelevant", true)
						.hasFieldOrPropertyWithValue("sortierung", 1)
						.hasFieldOrPropertyWithValue("istSichtbar", true)
						.hasFieldOrPropertyWithValue("aufgabenfeld", null)
						.hasFieldOrPropertyWithValue("bilingualeSprache", null)
						.hasFieldOrPropertyWithValue("istNachpruefungErlaubt", true)
						.hasFieldOrPropertyWithValue("aufZeugnis", true)
						.hasFieldOrPropertyWithValue("bezeichnungZeugnis", "Mathematik")
						.hasFieldOrPropertyWithValue("bezeichnungUeberweisungszeugnis", "Mathematik")
						.hasFieldOrPropertyWithValue("maxZeichenInFachbemerkungen", 2)
						.hasFieldOrPropertyWithValue("istSchriftlichZK", true)
						.hasFieldOrPropertyWithValue("istSchriftlichBA", true)
						.hasFieldOrPropertyWithValue("istFHRFach", false)
						.hasFieldOrPropertyWithValue("holeAusAltenLernabschnitten", true)
						.hasFieldOrPropertyWithValue("referenziertInAnderenTabellen", true));
	}

	@Test
	@DisplayName("getFaecherListe | mit Referenz Info | leere Liste | Erfolg")
	void getFaecherListeWithReferenceEmptyListTest() {
		when(conn.queryAll(DTOFach.class)).thenReturn(Collections.emptyList());

		assertThat(this.dataFaecherliste.getFaecherListe(true))
				.isInstanceOf(List.class)
				.isEmpty();
		verify(conn, never()).query(any(), any());
	}

	@Test
	@SuppressWarnings("unchecked")
	@DisplayName("getAllListe | Erfolg")
	void getAllTest() {
		when(conn.queryAll(DTOFach.class)).thenReturn(List.of(getDtoFach()));
		final TypedQuery<Long> queryMock = mock(TypedQuery.class);
		when(queryMock.setParameter(eq("idsFaecher"), any())).thenReturn(queryMock);
		when(queryMock.getResultList()).thenReturn(List.of(1L));
		when(conn.query(anyString(), eq(Long.class))).thenReturn(queryMock);

		assertThat(this.dataFaecherliste.getAll())
				.isInstanceOf(List.class)
				.isNotNull()
				.isNotEmpty()
				.hasSize(1)
				.allSatisfy(item -> assertThat(item)
						.isInstanceOf(FaecherListeEintrag.class)
						.hasFieldOrPropertyWithValue("id", 1L)
						.hasFieldOrPropertyWithValue("kuerzel", "M")
						.hasFieldOrPropertyWithValue("kuerzelStatistik", "M")
						.hasFieldOrPropertyWithValue("bezeichnung", "Mathematik")
						.hasFieldOrPropertyWithValue("istOberstufenFach", true)
						.hasFieldOrPropertyWithValue("istPruefungsordnungsRelevant", true)
						.hasFieldOrPropertyWithValue("sortierung", 1)
						.hasFieldOrPropertyWithValue("istSichtbar", true)
						.hasFieldOrPropertyWithValue("aufgabenfeld", null)
						.hasFieldOrPropertyWithValue("bilingualeSprache", null)
						.hasFieldOrPropertyWithValue("istNachpruefungErlaubt", true)
						.hasFieldOrPropertyWithValue("aufZeugnis", true)
						.hasFieldOrPropertyWithValue("bezeichnungZeugnis", "Mathematik")
						.hasFieldOrPropertyWithValue("bezeichnungUeberweisungszeugnis", "Mathematik")
						.hasFieldOrPropertyWithValue("maxZeichenInFachbemerkungen", 2)
						.hasFieldOrPropertyWithValue("istSchriftlichZK", true)
						.hasFieldOrPropertyWithValue("istSchriftlichBA", true)
						.hasFieldOrPropertyWithValue("istFHRFach", false)
						.hasFieldOrPropertyWithValue("holeAusAltenLernabschnitten", true));
	}

	@Test
	@DisplayName("getAll | mit Referenz Info | leere Liste | Erfolg")
	void getAllEmptyListTest() {
		when(conn.queryAll(DTOFach.class)).thenReturn(Collections.emptyList());

		assertThat(this.dataFaecherliste.getAll())
				.isInstanceOf(List.class)
				.isEmpty();
		verify(conn, never()).query(any(), any());
	}

	@Test
	@SuppressWarnings("unchecked")
	@DisplayName("checkBeforeDeletionWithSimpleOperationResponseTest | Erfolg")
	void checkBeforeDeletionWithSimpleOperationResponseTest() {
		final TypedQuery<Long> queryMock = mock(TypedQuery.class);
		when(queryMock.setParameter(eq("idsFaecher"), any())).thenReturn(queryMock);
		when(queryMock.getResultList()).thenReturn(List.of(1L));
		when(conn.query(anyString(), eq(Long.class))).thenReturn(queryMock);
		final var faecher = List.of(getDtoFach());
		final var response = new SimpleOperationResponse();
		response.id = 1L;
		response.success = true;
		final var responses = Map.of(response.id, response);

		this.dataFaecherliste.checkBeforeDeletionWithSimpleOperationResponse(faecher, responses);

		assertThat(responses.get(1L))
				.hasFieldOrPropertyWithValue("success", false)
				.extracting(r -> r.log.getFirst())
				.isEqualTo("Das Fach mit dem Kuerzel M und der id 1 ist in der Datenbank referenziert und kann daher nicht gelöscht werden");
	}

	@Test
	@SuppressWarnings("unchecked")
	@DisplayName("checkBeforeDeletionWithSimpleOperationResponseTest | Fach nicht referenziert | Erfolg")
	void checkBeforeDeletionWithSimpleOperationResponseTest_fachNotReferenced() {
		final TypedQuery<Long> queryMock = mock(TypedQuery.class);
		when(queryMock.setParameter(eq("idsFaecher"), any())).thenReturn(queryMock);
		when(queryMock.getResultList()).thenReturn(List.of(2L));
		when(conn.query(anyString(), eq(Long.class))).thenReturn(queryMock);
		final var faecher = List.of(getDtoFach());
		final var response = new SimpleOperationResponse();
		response.id = 1L;
		final var responses = Map.of(response.id, response);

		this.dataFaecherliste.checkBeforeDeletionWithSimpleOperationResponse(faecher, responses);

		assertThat(response.log).isEmpty();
	}

	private DTOFach getDtoFach() {
		final var dtoFach = new DTOFach(1L, true);
		dtoFach.Kuerzel = "M";
		dtoFach.StatistikKuerzel = "M";
		dtoFach.Bezeichnung = "Mathematik";
		dtoFach.IstOberstufenFach = true;
		dtoFach.IstPruefungsordnungsRelevant = true;
		dtoFach.SortierungAllg = 1;
		dtoFach.Sichtbar = true;
		dtoFach.Aufgabenfeld = null;
		dtoFach.Unterrichtssprache = null;
		dtoFach.IstNachpruefungErlaubt = true;
		dtoFach.AufZeugnis = true;
		dtoFach.BezeichnungZeugnis = "Mathematik";
		dtoFach.BezeichnungUeberweisungsZeugnis = "Mathematik";
		dtoFach.MaxBemZeichen = 2;
		dtoFach.IstSchriftlichZK = true;
		dtoFach.IstSchriftlichBA = true;
		dtoFach.AbgeschlFaecherHolen = true;
		return dtoFach;
	}
}
