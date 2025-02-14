package de.svws_nrw.data.schueler;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.schueler.SchuelerSchulbesuchsdaten;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.when;

@DisplayName("Diese Klasse testet die Klasse DataSchuelerSchulbesuchsdaten")
@ExtendWith(MockitoExtension.class)
public class DataSchuelerSchulbesuchsdatenTest {

	public static final String STATUS = "status";

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private DataSchuelerSchulbesuchsdaten schulbesuchsdaten;

	@BeforeAll
	static void setUpAll() {
		ASDCoreTypeUtils.initAll();
	}


	@Test
	@DisplayName("getById | Erfolg")
	void getByIdTest() throws ApiOperationException {
		when(this.conn.queryByKey(DTOSchueler.class, 1L)).thenReturn(new DTOSchueler(1L, "1", true));

		assertThat(this.schulbesuchsdaten.getById(1L))
				.isInstanceOf(SchuelerSchulbesuchsdaten.class)
				.hasFieldOrPropertyWithValue("id", 1L);
	}

	@Test
	@DisplayName("getById | idSchueler ist null")
	void getByIdTest_idSchuelerNull() {
		final var throwable = catchThrowable(() -> schulbesuchsdaten.getById(null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Die ID für den Schüler darf nicht null sein.")
				.hasFieldOrPropertyWithValue(STATUS, Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("getById | falsche idSchueler")
	void getByIdTest_wrongIdSchueler() {
		when(conn.queryByKey(DTOSchueler.class, 1L)).thenReturn(null);

		final var throwable = catchThrowable(() -> schulbesuchsdaten.getById(1L));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Es wurde kein Schüler mit der Id 1 gefunden")
				.hasFieldOrPropertyWithValue(STATUS, Response.Status.NOT_FOUND);
	}
}
