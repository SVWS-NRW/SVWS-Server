package de.svws_nrw.data.schueler;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.asd.data.schueler.SchuelerStammdaten;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.types.schule.PersonTyp;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.data.schule.DataEinwilligungsarten;
import de.svws_nrw.data.schule.DataLernplattformen;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Diese Klasse testet die Klasse {@link DataSchuelerNeuanlageNeu}.
 */
@DisplayName("Diese Klasse testet die Klasse DataSchuelerNeuanlageNeu")
@ExtendWith(MockitoExtension.class)
class DataSchuelerNeuanlageNeuTest {

	@Mock
	private DataSchuelerStammdaten dataSchuelerStammdaten;

	@Mock
	private DataSchuelerLernabschnittsdaten dataSchuelerLernabschnittsdaten;

	@Mock
	private DataSchuelerSchulbesuchsdaten dataSchuelerSchulbesuchsdaten;

	@Mock
	private DataSchuelerEinwilligungen dataSchuelerEinwilligungen;

	@Mock
	private DataSchuelerLernplattformen dataSchuelerLernplattformen;

	@Mock
	private DataLernplattformen dataLernplattformen;

	@Mock
	private DataEinwilligungsarten dataEinwilligungsarten;

	@InjectMocks
	private DataSchuelerNeuanlageNeu data;

	@BeforeAll
	static void setUpAll() {
		ASDCoreTypeUtils.initAll();
	}

	@Test
	@DisplayName("add")
	void add() {
		final var stammdaten = new HashMap<String, Object>();
		stammdaten.put("nachname", "Test");
		final var lernabschnitt = new HashMap<String, Object>();
		final var schulbesuch = new HashMap<String, Object>();
		final var schuelerNeuanlage = new HashMap<>();
		schuelerNeuanlage.put("schuelerLernabschnittsdaten", lernabschnitt);
		schuelerNeuanlage.put("schuelerSchulbesuchsdaten", schulbesuch);
		schuelerNeuanlage.put("schuelerStammdaten", stammdaten);

		final var generatedId = 100L;
		final var created = new SchuelerStammdaten();
		created.id = generatedId;

		final var idSchuljahresabschnitt = 8L;
		when(dataSchuelerStammdaten.add(any())).thenReturn(created);
		when(dataLernplattformen.getAllIds()).thenReturn(List.of(1L, 2L));
		when(dataEinwilligungsarten.getAllIdsByPersonTyp(PersonTyp.SCHUELER)).thenReturn(List.of(10L, 20L));

		try (MockedStatic<JSONMapper> mocked = mockStatic(JSONMapper.class)) {
			mocked.when(() -> JSONMapper.toMap(any())).thenReturn(schuelerNeuanlage);

			final var response = data.add(mock(InputStream.class), idSchuljahresabschnitt);

			assertThat(response)
					.extracting(Response::getStatus, Response::getEntity)
					.containsExactly(Response.Status.CREATED.getStatusCode(), created);

			verify(dataSchuelerStammdaten)
					.add(argThat(map -> map.get("idSchuljahresabschnitt").equals(idSchuljahresabschnitt)));
			verify(dataSchuelerLernabschnittsdaten)
					.add(argThat(map -> map.get("schuelerID").equals(generatedId)));
			verify(dataSchuelerSchulbesuchsdaten)
					.patch(generatedId, schulbesuch);
			verify(dataSchuelerLernplattformen, times(2))
					.add(argThat(map -> map.get("idSchueler").equals(generatedId)));
			verify(dataSchuelerEinwilligungen, times(2))
					.add(argThat(map -> map.get("idSchueler").equals(generatedId)));
		}
	}

	@Test
	@DisplayName("add | not a map")
	void addNotAMap() {
		final var schuelerStammdaten = Map.of("schuelerStammdaten", "notAMap");

		try (MockedStatic<JSONMapper> mocked = mockStatic(JSONMapper.class)) {
			mocked.when(() -> JSONMapper.toMap(any())).thenReturn(schuelerStammdaten);

			assertThatException()
					.isThrownBy(() -> this.data.add(mock(InputStream.class), 8L))
					.isInstanceOf(ApiOperationException.class)
					.withMessage("Attribut 'schuelerStammdaten' ist keine Map.")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
		}
	}

}
