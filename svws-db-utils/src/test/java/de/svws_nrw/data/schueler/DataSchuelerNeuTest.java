package de.svws_nrw.data.schueler;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import de.svws_nrw.asd.data.schueler.SchuelerStammdaten;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.types.schule.PersonTyp;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.data.schule.DataEinwilligungsarten;
import de.svws_nrw.data.schule.DataLernplattformen;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Diese Klasse testet die Klasse {@link DataSchuelerNeu}.
 */
@DisplayName("Diese Klasse testet die Klasse DataSchuelerNeu")
@ExtendWith(MockitoExtension.class)
class DataSchuelerNeuTest {

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
	private DataSchuelerNeu data;

	@BeforeAll
	static void setUpAll() {
		ASDCoreTypeUtils.initAll();
	}

	@Test
	@DisplayName("add")
	void add() {
		final var schuelerNeu = new HashMap<String, Object>();
		schuelerNeu.put("nachname", "Test");
		schuelerNeu.put("vorname", "Max");
		schuelerNeu.put("idSchuljahresabschnitt", 8L);
		schuelerNeu.put("idJahrgang", 5L);
		schuelerNeu.put("idKlasse", 10L);
		schuelerNeu.put("idGrundschuleEinschulungsart", 51L);

		final var generatedId = 100L;
		final var created = new SchuelerStammdaten();
		created.id = generatedId;

		when(dataSchuelerStammdaten.add(any())).thenReturn(created);
		when(dataLernplattformen.getAllIds()).thenReturn(List.of(1L, 2L));
		when(dataEinwilligungsarten.getAllIdsByPersonTyp(PersonTyp.SCHUELER)).thenReturn(List.of(10L, 20L));

		try (MockedStatic<JSONMapper> mocked = mockStatic(JSONMapper.class)) {
			mocked.when(() -> JSONMapper.toMap(any())).thenReturn(schuelerNeu);

			final var response = data.add(mock(InputStream.class));

			assertThat(response)
					.extracting(Response::getStatus, Response::getEntity)
					.containsExactly(Response.Status.CREATED.getStatusCode(), created);

			verify(dataSchuelerStammdaten)
					.add(argThat(map ->
							map.get("nachname").equals("Test")
									&& map.get("vorname").equals("Max")
									&& map.get("idSchuljahresabschnitt").equals(8L)
					));
			verify(dataSchuelerLernabschnittsdaten)
					.add(argThat(map ->
							map.get("schuelerID").equals(generatedId)
									&& map.get("schuljahresabschnitt").equals(8L)
									&& map.get("jahrgangID").equals(5L)
									&& map.get("klassenID").equals(10L)
					));
			verify(dataSchuelerSchulbesuchsdaten)
					.patch(eq(generatedId), argThat(map ->
							map.get("grundschuleEinschulungsartID").equals(51L)
					));
			verify(dataSchuelerLernplattformen, times(2))
					.add(argThat(map -> map.get("idSchueler").equals(generatedId)));
			verify(dataSchuelerEinwilligungen, times(2))
					.add(argThat(map -> map.get("idSchueler").equals(generatedId)));
		}
	}

	@Test
	@DisplayName("add | null values are filtered out")
	void addNullValuesFiltered() {
		final var schuelerNeu = new HashMap<String, Object>();
		schuelerNeu.put("nachname", "Test");
		schuelerNeu.put("vorname", null); // null value should not be added
		schuelerNeu.put("idSchuljahresabschnitt", 8L);

		final var generatedId = 100L;
		final var created = new SchuelerStammdaten();
		created.id = generatedId;

		when(dataSchuelerStammdaten.add(any())).thenReturn(created);
		when(dataLernplattformen.getAllIds()).thenReturn(List.of());
		when(dataEinwilligungsarten.getAllIdsByPersonTyp(PersonTyp.SCHUELER)).thenReturn(List.of());

		try (MockedStatic<JSONMapper> mocked = mockStatic(JSONMapper.class)) {
			mocked.when(() -> JSONMapper.toMap(any())).thenReturn(schuelerNeu);

			data.add(mock(InputStream.class));

			verify(dataSchuelerStammdaten)
					.add(argThat(map ->
							map.containsKey("nachname")
									&& !map.containsKey("vorname")
									&& map.containsKey("idSchuljahresabschnitt")
					));
		}
	}

}
