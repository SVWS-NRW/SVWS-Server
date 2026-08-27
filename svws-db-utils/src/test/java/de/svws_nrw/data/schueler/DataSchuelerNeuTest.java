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
import de.svws_nrw.data.util.TestUtils;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.service.schueler.schulbesuch.SchuelerSchulbesuchService;
import de.svws_nrw.service.schueler.stammdaten.SchuelerImportData;
import de.svws_nrw.service.schueler.stammdaten.SchuelerStammdatenService;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Diese Klasse testet die Klasse {@link DataSchuelerNeu}.
 */
@DisplayName("Tests für DataSchuelerNeu")
@ExtendWith(MockitoExtension.class)
class DataSchuelerNeuTest {

	@Mock
	private SchuelerStammdatenService schuelerStammdatenService;

	@Mock
	private DataSchuelerLernabschnittsdaten dataSchuelerLernabschnittsdaten;

	@Mock
	private SchuelerSchulbesuchService schuelerSchulbesuchService;

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
	@DisplayName("add | Schüler wird mit allen Pflichtfeldern angelegt")
	void add_withRequiredFields() {
		final var schuelerNeu = new HashMap<String, Object>();
		schuelerNeu.put("nachname", "Test");
		schuelerNeu.put("vorname", "Max");
		schuelerNeu.put("geburtsdatum", "2000-01-01");
		schuelerNeu.put("geschlecht", 3);
		schuelerNeu.put("status", 2);
		schuelerNeu.put("idSchuljahresabschnitt", 8L);
		schuelerNeu.put("idJahrgang", 5L);
		schuelerNeu.put("idGrundschuleEinschulungsart", 51L);

		final var created = new SchuelerStammdaten();
		created.id = 100L;

		when(schuelerStammdatenService.create(any(SchuelerImportData.class))).thenReturn(created);
		when(dataLernplattformen.getAllIds()).thenReturn(List.of(1L, 2L));
		when(dataEinwilligungsarten.getAllIdsByPersonTyp(PersonTyp.SCHUELER)).thenReturn(List.of(10L, 20L));

		final var response = data.add(TestUtils.fromObject(schuelerNeu));

		assertThat(response.getStatus()).isEqualTo(Response.Status.CREATED.getStatusCode());
		assertThat(response.getEntity()).isEqualTo(created);

		verify(schuelerStammdatenService, times(1)).create(any(SchuelerImportData.class));
		verify(dataSchuelerLernabschnittsdaten).add(argThat(map ->
				map.get("schuelerID").equals(100L)
						&& (((Number) map.get("schuljahresabschnitt")).longValue() == 8L)
						&& (((Number) map.get("jahrgangID")).longValue() == 5L)
		));
		verify(schuelerSchulbesuchService, times(1)).patch(anyLong(), any());
		verify(dataSchuelerLernplattformen, times(2)).add(argThat(map -> map.get("idSchueler").equals(100L)));
		verify(dataSchuelerEinwilligungen, times(2)).add(argThat(map -> map.get("idSchueler").equals(100L)));
	}

	@Test
	@DisplayName("add | null-Werte werden nicht weitergegeben")
	void addNullValuesFiltered() {
		final var schuelerNeu = new HashMap<String, Object>();
		schuelerNeu.put("nachname", "Test");
		schuelerNeu.put("vorname", null);
		schuelerNeu.put("geburtsdatum", "2000-01-01");
		schuelerNeu.put("geschlecht", 3);
		schuelerNeu.put("status", 2);
		schuelerNeu.put("idSchuljahresabschnitt", 8L);

		final var created = new SchuelerStammdaten();
		created.id = 100L;

		when(schuelerStammdatenService.create(any(SchuelerImportData.class))).thenReturn(created);
		when(dataLernplattformen.getAllIds()).thenReturn(List.of());
		when(dataEinwilligungsarten.getAllIdsByPersonTyp(PersonTyp.SCHUELER)).thenReturn(List.of());

		try (MockedStatic<JSONMapper> mocked = mockStatic(JSONMapper.class)) {
			mocked.when(() -> JSONMapper.toMap(any())).thenReturn(schuelerNeu);

			data.add(mock(InputStream.class));

			verify(schuelerStammdatenService).create(any(SchuelerImportData.class));
		}
	}

	@Test
	@DisplayName("add | kein Schüler angelegt → ApiOperationException")
	void addThrowsWhenSchuelerIsNull() {
		final var schuelerNeu = new HashMap<String, Object>();
		schuelerNeu.put("nachname", "Test");
		schuelerNeu.put("geburtsdatum", "2000-01-01");
		schuelerNeu.put("geschlecht", 3);
		schuelerNeu.put("status", 2);
		schuelerNeu.put("idSchuljahresabschnitt", 1L);

		final var inputStream = TestUtils.fromObject(schuelerNeu);
		assertThatThrownBy(() -> data.add(inputStream))
				.isInstanceOf(ApiOperationException.class);
	}

	@Test
	@DisplayName("add | kein idGrundschuleEinschulungsart → schulbesuchService wird nicht aufgerufen")
	void addSkipsSchulbesuchWhenEinschulungsartAbsent() {
		final var schuelerNeu = new HashMap<String, Object>();
		schuelerNeu.put("nachname", "Test");
		schuelerNeu.put("vorname", "Test");
		schuelerNeu.put("geburtsdatum", "2000-01-01");
		schuelerNeu.put("geschlecht", 3);
		schuelerNeu.put("status", 2);
		schuelerNeu.put("idSchuljahresabschnitt", 8L);

		final var created = new SchuelerStammdaten();
		created.id = 100L;

		when(schuelerStammdatenService.create(any(SchuelerImportData.class))).thenReturn(created);
		when(dataLernplattformen.getAllIds()).thenReturn(List.of());
		when(dataEinwilligungsarten.getAllIdsByPersonTyp(PersonTyp.SCHUELER)).thenReturn(List.of());

		data.add(TestUtils.fromObject(schuelerNeu));

		verify(schuelerSchulbesuchService, never()).patch(anyLong(), any());
	}

}
