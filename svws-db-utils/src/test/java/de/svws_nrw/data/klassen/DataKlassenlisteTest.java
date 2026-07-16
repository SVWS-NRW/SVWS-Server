package de.svws_nrw.data.klassen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.svws_nrw.asd.data.klassen.KlassenListeEintrag;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassenLeitung;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;
import static org.assertj.core.api.Assertions.tuple;
import static org.assertj.core.api.InstanceOfAssertFactories.LIST;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DataKlassenlisteTest {

	private static final String ANZAHL_SCHUELER_QUERY = """
				SELECT Klassen_ID, COUNT(*)
				FROM SchuelerLernabschnittsdaten
				WHERE Klassen_ID IS NOT NULL
				  AND WechselNr = 0
				  AND Schuljahresabschnitts_ID = ?1
				GROUP BY Klassen_ID
				""";

	@InjectMocks
	private DataKlassenliste cut;

	@Mock
	private DBEntityManager conn;

	@Test
	void getListBySchuljahresabschnittIDAsResponseWithEntitiesFound() {
		final List<DTOKlassen> klassenEntities = List.of(
				createDTOKlasse(1L, "05a"),
				createDTOKlasse(2L, "05b"));

		when(conn.queryNativeWithParameters(ANZAHL_SCHUELER_QUERY, 1L)).thenReturn(List.of(
				new Object[] { 1L, 25 },
				new Object[] { 2L, 30 }
		));
		when(conn.queryList(DTOKlassen.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOKlassen.class, 1L)).thenReturn(klassenEntities);

		final Response result = cut.getListBySchuljahresabschnittIDAsResponse(1L);

		assertThat(result.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		assertThat(result.getEntity()).isNotNull().asInstanceOf(LIST)
				.hasSize(2)
				.extracting("kuerzel", "id", "idSchuljahresabschnitt", "idJahrgang", "beschreibung", "parallelitaet", "anzahlZugeordneteSchueler")
				.containsExactly(
						tuple("05a", 1L, 1L, 10L, "Dies ist ein Mock der Klasse 05a", "a", 25),
						tuple("05b", 2L, 1L, 10L, "Dies ist ein Mock der Klasse 05b", "b", 30)
				);
	}

	@Test
	void getListBySchuljahresabschnittIDAsResponseWithNoEntitiesFound() {
		when(conn.queryNativeWithParameters(ANZAHL_SCHUELER_QUERY, 1L)).thenReturn(Collections.emptyList());
		when(conn.queryList(DTOKlassen.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOKlassen.class, 1L)).thenReturn(Collections.emptyList());

		final Response result = cut.getListBySchuljahresabschnittIDAsResponse(1L);

		verify(conn, never()).queryList(eq(DTOKlassenLeitung.QUERY_LIST_BY_KLASSEN_ID), eq(DTOKlassenLeitung.class), any());
		assertThat(result.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		assertThat(result.getEntity()).isNotNull().asInstanceOf(LIST).isEmpty();
	}

	@Test
	void getListBySchuljahresabschnittIDAsResponseSchuelerOhneKlassenzuordnung() {
		final List<DTOKlassen> klassenEntities = List.of(
				createDTOKlasse(1L, "05a"),
				createDTOKlasse(2L, "05b"));

		final List<Object[]> nativeQueryResult = new ArrayList<>();
		nativeQueryResult.add(new Object[]{1L, 15});

		doReturn(nativeQueryResult)
				.when(conn).queryNativeWithParameters(ANZAHL_SCHUELER_QUERY, 1L);
		when(conn.queryList(DTOKlassen.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOKlassen.class, 1L))
				.thenReturn(klassenEntities);

		final Response result = cut.getListBySchuljahresabschnittIDAsResponse(1L);

		assertThat(result.getEntity()).isNotNull().asInstanceOf(LIST)
				.extracting("id", "anzahlZugeordneteSchueler")
				.containsExactly(
						tuple(1L, 15),
						tuple(2L, 0)
				);
	}

	@Test
	void getListBySchuljahresabschnittIDAsResponseWithKlassenleitungen() {
		final List<DTOKlassen> klassenEntities = List.of(
				createDTOKlasse(1L, "05a"),
				createDTOKlasse(2L, "05b"));

		when(conn.queryNativeWithParameters(ANZAHL_SCHUELER_QUERY, 1L)).thenReturn(List.of(
				new Object[]{1L, 25},
				new Object[]{2L, 30}
		));
		when(conn.queryList(DTOKlassen.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOKlassen.class, 1L))
				.thenReturn(klassenEntities);

		final DTOKlassenLeitung leitung1 = new DTOKlassenLeitung(1L, 100L, 1);
		final DTOKlassenLeitung leitung2 = new DTOKlassenLeitung(1L, 101L, 2);
		final DTOKlassenLeitung leitung3 = new DTOKlassenLeitung(2L, 200L, 3);

		when(conn.queryList(DTOKlassenLeitung.QUERY_LIST_BY_KLASSEN_ID, DTOKlassenLeitung.class, List.of(1L, 2L)))
				.thenReturn(List.of(leitung1, leitung2, leitung3));

		final Response result = cut.getListBySchuljahresabschnittIDAsResponse(1L);

		assertThat(result.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		assertThat(result.getEntity()).isNotNull().asInstanceOf(LIST)
				.hasSize(2)
				.extracting("id", "idsKlassenleitungen")
				.containsExactly(
						tuple(1L, List.of(100L, 101L)),
						tuple(2L, List.of(200L))
				);
	}

	@Test
	void getListBySchuljahresabschnittIDAsResponseWithKeineKlassenleitungen() {
		final List<DTOKlassen> klassenEntities = List.of(createDTOKlasse(1L, "05a"));

		final List<Object[]> nativeQueryResult = new ArrayList<>();
		nativeQueryResult.add(new Object[]{1L, 10});

		doReturn(nativeQueryResult)
				.when(conn).queryNativeWithParameters(ANZAHL_SCHUELER_QUERY, 1L);
		when(conn.queryList(DTOKlassen.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOKlassen.class, 1L))
				.thenReturn(klassenEntities);
		when(conn.queryList(DTOKlassenLeitung.QUERY_LIST_BY_KLASSEN_ID, DTOKlassenLeitung.class, List.of(1L)))
				.thenReturn(Collections.emptyList());

		final Response result = cut.getListBySchuljahresabschnittIDAsResponse(1L);

		assertThat(result.getEntity()).isNotNull().asInstanceOf(LIST)
				.singleElement()
				.satisfies(element -> {
					final KlassenListeEintrag eintrag = (KlassenListeEintrag) element;
					assertThat(eintrag.id).isEqualTo(1L);
					assertThat(eintrag.idsKlassenleitungen).isEmpty();
				});
	}

	// ---------------------------------------------------------
	// Tests für getKlassenDatenMinimalByIdSchuljahresabschnitt
	// ---------------------------------------------------------

	@Test
	void getKlassenDatenMinimalByIdSchuljahresabschnittWithEntitiesFound() {
		final List<DTOKlassen> klassenEntities = List.of(
				createDTOKlasse(1L, "05a"),
				createDTOKlasse(2L, "05b"));

		when(conn.queryList(DTOKlassen.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOKlassen.class, 1L))
				.thenReturn(klassenEntities);

		final Response result = cut.getKlassenDatenMinimalByIdSchuljahresabschnitt(1L);

		assertThat(result.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		assertThat(result.getEntity()).isNotNull().asInstanceOf(LIST)
				.hasSize(2)
				.extracting("id", "kuerzel", "idSchuljahresabschnitt", "idJahrgang", "beschreibung", "parallelitaet")
				.containsExactly(
						tuple(1L, "05a", 1L, 10L, "Dies ist ein Mock der Klasse 05a", "a"),
						tuple(2L, "05b", 1L, 10L, "Dies ist ein Mock der Klasse 05b", "b")
				);
	}

	@Test
	void getKlassenDatenMinimalByIdSchuljahresabschnittWithNoEntitiesFound() {
		when(conn.queryList(DTOKlassen.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOKlassen.class, 1L))
				.thenReturn(Collections.emptyList());

		final Response result = cut.getKlassenDatenMinimalByIdSchuljahresabschnitt(1L);

		assertThat(result.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		assertThat(result.getEntity()).isNotNull().asInstanceOf(LIST).isEmpty();
	}

// ---------------------------------------------------------
// Tests für Null-ID (Exception-Pfad)
// ---------------------------------------------------------

	@Test
	void getListBySchuljahresabschnittIDAsResponseThrowsExceptionWhenIdIsNull() {
		assertThatException().isThrownBy(() -> cut.getListBySchuljahresabschnittIDAsResponse(null))
				.isInstanceOf(ApiOperationException.class)
				.withMessageContaining("null");
	}

	@Test
	void getKlassenDatenMinimalByIdSchuljahresabschnittThrowsExceptionWhenIdIsNull() {
		assertThatException().isThrownBy(() -> cut.getKlassenDatenMinimalByIdSchuljahresabschnitt(null))
				.isInstanceOf(ApiOperationException.class)
				.withMessageContaining("null");
	}

	// ---------------------------------------------------------
	// Tests für Parallelitaet-Sonderfaelle
	// ---------------------------------------------------------

	@Test
	void getListBySchuljahresabschnittIDAsResponseWithASDKlasseKuerzerAlsDreiZeichen() {
		final DTOKlassen klasse = new DTOKlassen(1L, 1L, "05");
		klasse.Jahrgang_ID = 10L;
		klasse.Bezeichnung = "Klasse ohne Parallelitaet";
		klasse.ASDKlasse = "05"; // kürzer als 3 Zeichen → parallelitaet = null

		final List<Object[]> nativeQueryResult = new ArrayList<>();
		nativeQueryResult.add(new Object[]{1L, 10});

		doReturn(nativeQueryResult)
				.when(conn).queryNativeWithParameters(ANZAHL_SCHUELER_QUERY, 1L);
		when(conn.queryList(DTOKlassen.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOKlassen.class, 1L))
				.thenReturn(List.of(klasse));
		when(conn.queryList(DTOKlassenLeitung.QUERY_LIST_BY_KLASSEN_ID, DTOKlassenLeitung.class, List.of(1L)))
				.thenReturn(Collections.emptyList());

		final Response result = cut.getListBySchuljahresabschnittIDAsResponse(1L);

		assertThat(result.getEntity()).isNotNull().asInstanceOf(LIST)
				.singleElement()
				.extracting("parallelitaet")
				.isNull();
	}

	@Test
	void getListBySchuljahresabschnittIDAsResponseWithASDKlasseNull() {
		final DTOKlassen klasse = new DTOKlassen(1L, 1L, "05");
		klasse.Jahrgang_ID = 10L;
		klasse.Bezeichnung = "Klasse mit null ASDKlasse";
		klasse.ASDKlasse = null; // null → parallelitaet = null

		final List<Object[]> nativeQueryResult = new ArrayList<>();
		nativeQueryResult.add(new Object[]{1L, 10});

		doReturn(nativeQueryResult)
				.when(conn).queryNativeWithParameters(ANZAHL_SCHUELER_QUERY, 1L);
		when(conn.queryList(DTOKlassen.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOKlassen.class, 1L))
				.thenReturn(List.of(klasse));
		when(conn.queryList(DTOKlassenLeitung.QUERY_LIST_BY_KLASSEN_ID, DTOKlassenLeitung.class, List.of(1L)))
				.thenReturn(Collections.emptyList());

		final Response result = cut.getListBySchuljahresabschnittIDAsResponse(1L);

		assertThat(result.getEntity()).isNotNull().asInstanceOf(LIST)
				.singleElement()
				.extracting("parallelitaet")
				.isNull();
	}



	private static DTOKlassen createDTOKlasse(final long id, final String kuerzel) {
		final DTOKlassen klasse = new DTOKlassen(id, 1L, kuerzel);
		klasse.Jahrgang_ID = 10L;
		klasse.Bezeichnung = "Dies ist ein Mock der Klasse %s".formatted(kuerzel);
		klasse.ASDKlasse = kuerzel;
		return klasse;
	}
}
