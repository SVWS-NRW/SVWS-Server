package de.svws_nrw.data.klassen;

import java.util.Collections;
import java.util.List;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.assertj.core.api.InstanceOfAssertFactories.LIST;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DataKlassenlisteTest {

	@InjectMocks
	private DataKlassenliste cut;

	@Mock
	private DBEntityManager conn;

	@Test
	void getListBySchuljahresabschnittIDAsResponseWithEntitiesFound() {
		final List<DTOKlassen> klassenEntities = List.of(
				createDTOKlasse(1L, "05a"),
				createDTOKlasse(2L, "05b"));

		when(conn.queryList(DTOKlassen.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOKlassen.class, 1L)).thenReturn(klassenEntities);

		final Response result = cut.getListBySchuljahresabschnittIDAsResponse(1L);

		assertThat(result.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		assertThat(result.getEntity()).isNotNull().asInstanceOf(LIST)
				.hasSize(2)
				.extracting("kuerzel", "id", "idSchuljahresabschnitt", "idJahrgang", "beschreibung", "parallelitaet")
				.containsExactly(
						tuple("05a", 1L, 1L, 10L, "Dies ist ein Mock der Klasse 05a", "a"),
						tuple("05b", 2L, 1L, 10L, "Dies ist ein Mock der Klasse 05b", "b")
				);
	}

	@Test
	void getListBySchuljahresabschnittIDAsResponseWithNoEntitiesFound() {
		when(conn.queryList(DTOKlassen.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOKlassen.class, 1L)).thenReturn(Collections.emptyList());

		final Response result = cut.getListBySchuljahresabschnittIDAsResponse(1L);

		assertThat(result.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		assertThat(result.getEntity()).isNotNull().asInstanceOf(LIST).isEmpty();
	}

	@Test
	void getListBySchuljahresabschnittIDAsResponseWithIdSchuljahresabschnittNull() {
		assertThatThrownBy(() -> cut.getListBySchuljahresabschnittIDAsResponse(null))
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Die ID für den Schuljahresabschnitt darf nicht null sein.");
	}

	private static DTOKlassen createDTOKlasse(final long id, final String kuerzel) {
		final DTOKlassen klasse = new DTOKlassen(id, 1L, kuerzel);
		klasse.Jahrgang_ID = 10L;
		klasse.Bezeichnung = "Dies ist ein Mock der Klasse %s".formatted(kuerzel);
		klasse.ASDKlasse = kuerzel;
		return klasse;
	}
}
