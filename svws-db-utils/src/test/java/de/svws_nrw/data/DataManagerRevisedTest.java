package de.svws_nrw.data;

import static de.svws_nrw.core.types.benutzer.BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ALLE_AENDERN;
import static de.svws_nrw.core.types.benutzer.BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_FUNKTIONSBEZOGEN_AENDERN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.klassen.KlassenDaten;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Diese Testklasse testet alle Core Funktionalitäten aus der Klasse DataManagerRevised. Dabei wurden sowohl Unit- als auch
 * Integrationstests genutzt.
 */
@ExtendWith(MockitoExtension.class)
class DataManagerRevisedTest {

	@InjectMocks
	private DataManagerRevisedMock cut;

	@Mock
	private DBEntityManager conn;

	private static KlassenDaten klassenDaten;

	/**
	 * Initialisierung der Core-Types
	 */
	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}

	@BeforeEach
	public void setUpBeforeEach() {
		klassenDaten = new KlassenDaten();
		klassenDaten.id = 1L;
		klassenDaten.kuerzel = "55a";
		klassenDaten.idJahrgang = 1L;
	}


	@Test
	void constructor_DBEntityManagerIsNull() {
		final Throwable result = Assertions.catchThrowable(() -> new DataManagerRevised<>(null) {
			@Override
			protected Object map(final Object o) {
				return null;
			}
		});

		assertThat(result).isInstanceOf(NullPointerException.class).hasMessage("DBEntityManager darf nicht null sein.");
	}

	@Test
	void constructor_ClassDTOKlassen() {
		final DataManagerRevised<Long, DTOKlassen, KlassenDaten> result = new DataManagerRevised<>(mock(DBEntityManager.class)) {
			@Override
			protected KlassenDaten map(final DTOKlassen dtoKlassen) {
				return null;
			}
		};

		assertThat(result).isNotNull().hasFieldOrPropertyWithValue("classDatabaseDTO", DTOKlassen.class);
	}

	@Test
	void mapAttribute() {
		final DataManagerRevised<Long, DTOKlassen, KlassenDaten> dmr = new DataManagerRevised<>(mock(DBEntityManager.class)) {
			@Override
			protected KlassenDaten map(final DTOKlassen dtoKlassen) {
				return null;
			}
		};

		final Throwable result = catchThrowable(() -> dmr.mapAttribute(null, null, null, null));

		assertThat(result).isInstanceOf(ApiOperationException.class)
				.hasMessage("Die Methode mapAttribute() ist standardmäßig nicht implementiert.");
	}

	@Test
	void initDTO() {
		final DataManagerRevised<Long, DTOKlassen, KlassenDaten> dmr = new DataManagerRevised<>(mock(DBEntityManager.class)) {
			@Override
			protected KlassenDaten map(final DTOKlassen dtoKlassen) {
				return null;
			}
		};

		final Throwable result = catchThrowable(() -> dmr.initDTO(null, null, null));

		assertThat(result).isInstanceOf(ApiOperationException.class)
				.hasMessage("Die Methode initDTO() ist standardmäßig nicht implementiert.");
	}

	@Test
	void getID() {
		final DataManagerRevised<Long, DTOKlassen, KlassenDaten> dmr = new DataManagerRevised<>(mock(DBEntityManager.class)) {
			@Override
			protected KlassenDaten map(final DTOKlassen dtoKlassen) {
				return null;
			}
		};

		final Throwable result = catchThrowable(() -> dmr.getID(null));

		assertThat(result).isInstanceOf(ApiOperationException.class)
				.hasMessage("Die Methode getID() ist standardmäßig nicht implementiert.");
	}

	@Test
	void getAll() {
		final DataManagerRevised<Long, DTOKlassen, KlassenDaten> dmr = new DataManagerRevised<>(mock(DBEntityManager.class)) {
			@Override
			protected KlassenDaten map(final DTOKlassen dtoKlassen) {
				return null;
			}
		};

		final Throwable result = catchThrowable(dmr::getAll);

		assertThat(result).isInstanceOf(ApiOperationException.class)
				.hasMessage("Die Methode getAll() ist standardmäßig nicht implementiert.");
	}

	@Test
	void getList() {
		final DataManagerRevised<Long, DTOKlassen, KlassenDaten> dmr = new DataManagerRevised<>(mock(DBEntityManager.class)) {
			@Override
			protected KlassenDaten map(final DTOKlassen dtoKlassen) {
				return null;
			}
		};

		final Throwable result = catchThrowable(dmr::getList);

		assertThat(result).isInstanceOf(ApiOperationException.class)
				.hasMessage("Die Methode getList() ist standardmäßig nicht implementiert.");
	}

	@Test
	void getById() {
		final DataManagerRevised<Long, DTOKlassen, KlassenDaten> dmr = new DataManagerRevised<>(mock(DBEntityManager.class)) {
			@Override
			protected KlassenDaten map(final DTOKlassen dtoKlassen) {
				return null;
			}
		};

		final Throwable result = catchThrowable(() -> dmr.getById(null));

		assertThat(result).isInstanceOf(ApiOperationException.class)
				.hasMessage("Die Methode getById() ist standardmäßig nicht implementiert.");
	}

	@Test
	void setAttributesRequiredOnCreation_Initial() {
		cut.setAttributesRequiredOnCreation("bla", "test");

		assertThat(cut).extracting("attributesRequiredOnCreation", InstanceOfAssertFactories.COLLECTION).containsExactlyInAnyOrder("bla", "test");
	}

	@Test
	void setAttributesRequiredOnCreation_Reset() {
		cut.setAttributesRequiredOnCreation("bla", "test");
		cut.setAttributesRequiredOnCreation("bla2", "test2", "test3");
		assertThat(cut).extracting("attributesRequiredOnCreation", InstanceOfAssertFactories.COLLECTION).containsExactlyInAnyOrder("bla2", "test2", "test3");
	}

	@Test
	void setAttributesNotPatchable_Initial() {
		cut.setAttributesNotPatchable("bla", "test");

		assertThat(cut).extracting("attributesNotPatchable", InstanceOfAssertFactories.COLLECTION).containsExactlyInAnyOrder("bla", "test");
	}

	@Test
	void setAttributesNotPatchable_Reset() {
		cut.setAttributesNotPatchable("bla", "test");
		cut.setAttributesNotPatchable("bla2", "test2", "test3");
		assertThat(cut).extracting("attributesNotPatchable", InstanceOfAssertFactories.COLLECTION).containsExactlyInAnyOrder("bla2", "test2", "test3");
	}

	@Test
	void setAttributesDelayedOnCreation_Initial() {
		cut.setAttributesDelayedOnCreation("bla", "test");

		assertThat(cut).extracting("attributesDelayedOnCreation", InstanceOfAssertFactories.COLLECTION).containsExactlyInAnyOrder("bla", "test");
	}

	@Test
	void setAttributesDelayedOnCreation_Reset() {
		cut.setAttributesDelayedOnCreation("bla", "test");
		cut.setAttributesDelayedOnCreation("bla2", "test2", "test3");
		assertThat(cut).extracting("attributesDelayedOnCreation", InstanceOfAssertFactories.COLLECTION).containsExactlyInAnyOrder("bla2", "test2", "test3");
	}

	@Test
	void patchAsResponse() throws ApiOperationException {
		final Map<String, Object> input = Map.of("kuerzel", "55a");
		final DTOKlassen dto = new DTOKlassen(1L, 1L, "5a");

		when(conn.queryByKey(DTOKlassen.class, 1L)).thenReturn(dto);
		when(conn.transactionPersist(dto)).thenReturn(true);
		doNothing().when(conn).transactionFlush();


		try (MockedStatic<JSONMapper> mapperMock = Mockito.mockStatic(JSONMapper.class)) {
			mapperMock.when(() -> JSONMapper.toMap(any(InputStream.class))).thenReturn(input);
			mapperMock.when(() -> JSONMapper.convertToString(any(Object.class), anyBoolean(), anyBoolean(), anyInt())).thenCallRealMethod();

			final Response result = cut.patchAsResponse(1L, mock(InputStream.class));

			verify(conn, times(1)).transactionPersist(argThat(e -> ((DTOKlassen) e).Klasse.equals("55a")));
			verify(conn, times(1)).transactionFlush();

			assertThat(result.getStatus()).isEqualTo(Response.Status.NO_CONTENT.getStatusCode());
		}
	}

	@Test
	void patchAsResponseWithFailureWhilePersisting() {
		final DTOKlassen dto = new DTOKlassen(1L, 1L, "5a");

		when(conn.queryByKey(DTOKlassen.class, 1L)).thenReturn(dto);
		when(conn.transactionPersist(dto)).thenReturn(false);

		try (MockedStatic<JSONMapper> mapperMock = Mockito.mockStatic(JSONMapper.class)) {
			mapperMock.when(() -> JSONMapper.toMap(any(InputStream.class))).thenReturn(Map.of("kuerzel", "55a"));
			mapperMock.when(() -> JSONMapper.convertToString(any(Object.class), anyBoolean(), anyBoolean(), anyInt())).thenCallRealMethod();

			final Throwable result = catchThrowable(() -> cut.patchAsResponse(1L, mock(InputStream.class)));

			assertThat(result).isInstanceOf(ApiOperationException.class)
					.hasMessage("Fehler beim Persistieren der Entität.")
					.hasFieldOrPropertyWithValue("status", Response.Status.INTERNAL_SERVER_ERROR);

			verify(conn, times(1)).transactionPersist(argThat(e -> ((DTOKlassen) e).Klasse.equals("55a")));
		}
	}

	@Test
	void patchAsResponseWithIdIsNull() {
		try (MockedStatic<JSONMapper> mapperMock = Mockito.mockStatic(JSONMapper.class)) {
			mapperMock.when(() -> JSONMapper.toMap(any(InputStream.class))).thenReturn(Map.of("kuerzel", "55a"));

			final Throwable result = catchThrowable(() -> cut.patchAsResponse(null, mock(InputStream.class)));

			assertThat(result).isInstanceOf(ApiOperationException.class)
					.hasMessage("Für das Patchen muss eine ID angegeben werden. Null ist nicht zulässig.")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
		}
	}

	@Test
	void patchAsResponseWithNoAttribtuesToPatch() {
		try (MockedStatic<JSONMapper> mapperMock = Mockito.mockStatic(JSONMapper.class)) {
			mapperMock.when(() -> JSONMapper.toMap(any(InputStream.class))).thenReturn(new HashMap<>());

			final Throwable result = catchThrowable(() -> cut.patchAsResponse(1L, mock(InputStream.class)));

			assertThat(result).isInstanceOf(ApiOperationException.class)
					.hasMessage("In dem Patch sind keine Daten enthalten.")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
		}
	}

	@Test
	void patchAsResponseWithNoEntityFound() {
		when(conn.queryByKey(DTOKlassen.class, 1L)).thenReturn(null);

		try (MockedStatic<JSONMapper> mapperMock = Mockito.mockStatic(JSONMapper.class)) {
			mapperMock.when(() -> JSONMapper.toMap(any(InputStream.class))).thenReturn(Map.of("kuerzel", "55a"));

			final Throwable result = catchThrowable(() -> cut.patchAsResponse(1L, mock(InputStream.class)));

			assertThat(result).isInstanceOf(ApiOperationException.class)
					.hasMessage("Die Entität für die angegebene ID wurden in der Datenbank nicht gefunden.")
					.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
		}
	}

	@Test
	void patchAsResponseWithAttributIsNotPatchable() {
		cut.setAttributesNotPatchable("id");

		final DTOKlassen dto = new DTOKlassen(1L, 1L, "5a");
		when(conn.queryByKey(DTOKlassen.class, 1L)).thenReturn(dto);

		try (MockedStatic<JSONMapper> mapperMock = Mockito.mockStatic(JSONMapper.class)) {
			mapperMock.when(() -> JSONMapper.toMap(any(InputStream.class))).thenReturn(Map.of("id", "55"));

			final Throwable result = catchThrowable(() -> cut.patchAsResponse(1L, mock(InputStream.class)));

			assertThat(result).isInstanceOf(ApiOperationException.class)
					.hasMessage("Folgende Attribute werden für ein Patch nicht zugelassen: id.")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
		}
	}

	@Test
	void patchMultipleAsResponse() throws ApiOperationException {
		cut = spy(cut);

		final List<Map<String, Object>> input = List.of(Map.of("kuerzel", "55a"), Map.of("kuerzel", "66a"));

	    doReturn(mock(DTOKlassen.class)).when(cut).patch(anyLong(), anyMap()); // Verwende doReturn() mit der CoreDTO-Rückgabe

		try (MockedStatic<JSONMapper> mapperMock = Mockito.mockStatic(JSONMapper.class)) {
			mapperMock.when(() -> JSONMapper.toMultipleMaps(any(InputStream.class))).thenReturn(input);

			final Response result = cut.patchMultipleAsResponse(mock(InputStream.class));

			verify(cut, times(2)).patch(anyLong(), argThat(e -> e.containsValue("55a") || e.containsValue("66a")));
			assertThat(result.getStatus()).isEqualTo(Response.Status.NO_CONTENT.getStatusCode());
		}
	}

	@Test
	void addAsResponse() throws ApiOperationException {
		cut = spy(cut);

		final Map<String, Object> input = Map.of("kuerzel", "55a", "idJahrgang", 1, "sortierung", 99);

		when(conn.transactionGetNextID(DTOKlassen.class)).thenReturn(1L);
		when(conn.transactionPersist(any())).thenReturn(true);
		doNothing().when(conn).transactionFlush();

		try (MockedStatic<JSONMapper> mapperMock = Mockito.mockStatic(JSONMapper.class)) {
			mapperMock.when(() -> JSONMapper.toMap(any(InputStream.class))).thenReturn(input);
			mapperMock.when(() -> JSONMapper.convertToString(any(Object.class), anyBoolean(), anyBoolean(), anyInt())).thenCallRealMethod();
			mapperMock.when(() -> JSONMapper.convertToLong(any(Object.class), anyBoolean())).thenCallRealMethod();
			mapperMock.when(() -> JSONMapper.convertToInteger(any(Object.class), anyBoolean())).thenCallRealMethod();

			final Response result = cut.addAsResponse(mock(InputStream.class));

			verify(conn, times(2)).transactionPersist(argThat(e -> (((DTOKlassen) e).Jahrgang_ID == 1L)
					&& ((DTOKlassen) e).Klasse.equals("55a")));
			verify(conn, times(2)).transactionFlush();
			verify(cut, times(2)).applyPatchMappings(any(), any(), any(), any(), anyBoolean());
			verify(cut, times(1)).checkBeforeCreation(any(), any());

			assertThat(result.getStatus()).isEqualTo(Response.Status.CREATED.getStatusCode());
			assertThat(result.getEntity()).isEqualTo(klassenDaten);
			assertThat(result.getMediaType()).isEqualTo(MediaType.APPLICATION_JSON_TYPE);
		}
	}

	@Test
	void addAsResponseWithErrorInInitDTOMethod() throws ApiOperationException {
		cut = spy(cut);

		final Map<String, Object> input = Map.of("kuerzel", "55a", "idJahrgang", 1, "sortierung", 99);

		doThrow(new ApiOperationException(Response.Status.BAD_REQUEST, "Bad Request Test")).when(cut).initDTO(any(), any(), any());

		try (MockedStatic<JSONMapper> mapperMock = Mockito.mockStatic(JSONMapper.class)) {
			mapperMock.when(() -> JSONMapper.toMap(any(InputStream.class))).thenReturn(input);

			final Throwable result = catchThrowable(() -> cut.addAsResponse(mock(InputStream.class)));

			assertThat(result).isInstanceOf(ApiOperationException.class)
					.hasMessage("Bad Request Test")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
		}
	}

	@Test
	void addAsResponseWithErrorInInitDTOMethod2() throws ApiOperationException {
		cut = spy(cut);

		final Map<String, Object> input = Map.of("kuerzel", "55a", "idJahrgang", 1, "sortierung", 99);

		doThrow(new RuntimeException("Bad Request Test")).when(cut).initDTO(any(), any(), any());

		try (MockedStatic<JSONMapper> mapperMock = Mockito.mockStatic(JSONMapper.class)) {
			mapperMock.when(() -> JSONMapper.toMap(any(InputStream.class))).thenReturn(input);

			final Throwable result = catchThrowable(() -> cut.addAsResponse(mock(InputStream.class)));

			assertThat(result).isInstanceOf(ApiOperationException.class)
					.hasFieldOrPropertyWithValue("status", Response.Status.INTERNAL_SERVER_ERROR);
		}
	}


	@Test
	void addMultipleAsResponse() throws ApiOperationException {
		cut = spy(cut);

		final List<Map<String, Object>> input = List.of(Map.of("kuerzel", "55a", "idJahrgang", 1), Map.of("kuerzel", "66a", "idJahrgang", 1));

		when(conn.transactionGetNextID(DTOKlassen.class)).thenReturn(1L, 2L);
		doReturn(mock(DTOKlassen.class)).when(cut).addBasic(anyLong(), anyMap());

		try (MockedStatic<JSONMapper> mapperMock = Mockito.mockStatic(JSONMapper.class)) {
			mapperMock.when(() -> JSONMapper.toMultipleMaps(any(InputStream.class))).thenReturn(input);

			final Response result = cut.addMultipleAsResponse(mock(InputStream.class));

			verify(cut, times(2)).addBasic(argThat(e -> (e == 1L) || (e == 2L)), argThat(e -> e.containsValue("55a") || e.containsValue("66a")));

			assertThat(result.getStatus()).isEqualTo(Response.Status.CREATED.getStatusCode());
			assertThat(result.getEntity()).asInstanceOf(InstanceOfAssertFactories.LIST).hasSize(2);
			assertThat(result.getMediaType()).isEqualTo(MediaType.APPLICATION_JSON_TYPE);
		}
	}

	@Test
	@SuppressWarnings("resource")
	void deleteAsResponse() throws ApiOperationException {
		cut = spy(cut);

		final DTOKlassen dto = new DTOKlassen(1L, 1L, "5a");

		when(conn.queryByKey(DTOKlassen.class, 1L)).thenReturn(dto);
		when(conn.transactionRemove(dto)).thenReturn(true);
		doNothing().when(conn).transactionFlush();

		final Response result = cut.deleteAsResponse(1L);

		verify(cut, times(1)).checkBeforeDeletion(argThat(e -> e.getFirst() == dto));
		verify(conn, times(1)).transactionRemove(dto);
		verify(conn, times(1)).transactionFlush();

		assertThat(result.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		assertThat(result.getEntity()).isEqualTo(klassenDaten);
		assertThat(result.getMediaType()).isEqualTo(MediaType.APPLICATION_JSON_TYPE);
	}

	@Test
	@SuppressWarnings("resource")
	void deleteAsResponseWithIDIsNull() {
		final Throwable result = catchThrowable(() -> cut.deleteAsResponse(null));

		assertThat(result).isInstanceOf(ApiOperationException.class)
				.hasMessage("Für das Löschen muss eine ID angegeben werden. Null ist nicht zulässig.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@SuppressWarnings("resource")
	void deleteAsResponseWithNoEntityFound() {
		when(conn.queryByKey(DTOKlassen.class, 1L)).thenReturn(null);

		final Throwable result = catchThrowable(() -> cut.deleteAsResponse(1L));

		assertThat(result).isInstanceOf(ApiOperationException.class)
				.hasMessage("Es wurde keine Entität mit der ID 1 gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@SuppressWarnings("resource")
	void deleteMultipleAsResponse() throws ApiOperationException {
		cut = spy(cut);

		final DTOKlassen dto = new DTOKlassen(1L, 1L, "5a");
		final DTOKlassen dto2 = new DTOKlassen(2, 2L, "6a");
		final List<Long> inputIDs = List.of(1L, 2L);

		when(conn.queryByKeyList(DTOKlassen.class, inputIDs)).thenReturn(List.of(dto, dto2));
		when(conn.transactionRemove(any(DTOKlassen.class))).thenReturn(true);
		doNothing().when(conn).transactionFlush();

		final Response result = cut.deleteMultipleAsResponse(inputIDs);

		verify(cut, times(1)).checkBeforeDeletion(argThat(e -> e.contains(dto) && e.contains(dto2)));
		verify(conn, times(2)).transactionRemove(any(DTOKlassen.class));
		verify(conn, times(2)).transactionFlush();

		assertThat(result.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		assertThat(result.getEntity()).asInstanceOf(InstanceOfAssertFactories.LIST).hasSize(2);
		assertThat(result.getMediaType()).isEqualTo(MediaType.APPLICATION_JSON_TYPE);
	}

	@Test
	void getClassDatabaseDTO() {
		final Class<DTOKlassen> result = cut.getClassDatabaseDTO();
		assertThat(result).isAssignableFrom(DTOKlassen.class);
	}

	@Test
	void getClassID() {
		final Class<Long> result = cut.getClassID();
		assertThat(result).isAssignableFrom(Long.class);
	}

	@Test
	void getByIdAsResponse() throws ApiOperationException {
		final Response result = cut.getByIdAsResponse(1L);

		assertThat(result.getEntity()).isEqualTo(klassenDaten);
		assertThat(result.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		assertThat(result.getMediaType()).isEqualTo(MediaType.APPLICATION_JSON_TYPE);
	}

	@Test
	void getAllAsResponse() throws ApiOperationException {
		final Response result = cut.getAllAsResponse();

		assertThat(result.getEntity()).asInstanceOf(InstanceOfAssertFactories.LIST).hasSize(1).containsExactly(klassenDaten);
		assertThat(result.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		assertThat(result.getMediaType()).isEqualTo(MediaType.APPLICATION_JSON_TYPE);
	}

	@Test
	void getListAsResponse() throws ApiOperationException {
		final Response result = cut.getListAsResponse();

		assertThat(result.getEntity()).asInstanceOf(InstanceOfAssertFactories.LIST).hasSize(1).containsExactly(klassenDaten);
		assertThat(result.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		assertThat(result.getMediaType()).isEqualTo(MediaType.APPLICATION_JSON_TYPE);
	}

	private static Stream<Arguments> hatBenutzerNurFunktionsbezogeneKompetenz() {
		return Stream.of(
				Arguments.of(Set.of(SCHUELER_LEISTUNGSDATEN_ALLE_AENDERN), false),
				Arguments.of(Set.of(SCHUELER_LEISTUNGSDATEN_FUNKTIONSBEZOGEN_AENDERN, SCHUELER_LEISTUNGSDATEN_ALLE_AENDERN), false),
				Arguments.of(Collections.emptySet(), false),
				Arguments.of(Set.of(SCHUELER_LEISTUNGSDATEN_FUNKTIONSBEZOGEN_AENDERN), true)
		);
	}

	@MethodSource
	@ParameterizedTest
	void hatBenutzerNurFunktionsbezogeneKompetenz(final Set<BenutzerKompetenz> givenUserKompetenzen, final boolean expectedResult)
			throws IllegalAccessException {
		final Benutzer userMock = mock(Benutzer.class);
		final Field kompetenzField = ReflectionUtils.findFields(Benutzer.class, f -> f.getName().equals("_kompetenzenVerwendet"),
				ReflectionUtils.HierarchyTraversalMode.TOP_DOWN).getFirst();
		kompetenzField.setAccessible(true);
		kompetenzField.set(userMock, givenUserKompetenzen);

		doCallRealMethod().when(userMock).hatVerwendeteKompetenz(any(BenutzerKompetenz.class));
		when(conn.getUser()).thenReturn(userMock);

		final boolean result = cut.hatBenutzerNurFunktionsbezogeneKompetenz(SCHUELER_LEISTUNGSDATEN_FUNKTIONSBEZOGEN_AENDERN,
				Set.of(SCHUELER_LEISTUNGSDATEN_ALLE_AENDERN));

		assertThat(result).isEqualTo(expectedResult);
	}

	private static Stream<Arguments> hatBenutzerNurFunktionsbezogeneKompetenzWithMissingParameter() {
		return Stream.of(
				Arguments.of(null, Set.of(SCHUELER_LEISTUNGSDATEN_ALLE_AENDERN)),
				Arguments.of(SCHUELER_LEISTUNGSDATEN_FUNKTIONSBEZOGEN_AENDERN, null),
				Arguments.of(SCHUELER_LEISTUNGSDATEN_FUNKTIONSBEZOGEN_AENDERN, Collections.emptySet()),
				Arguments.of(null, null),
				Arguments.of(null, Collections.emptySet())
		);
	}

	@MethodSource
	@ParameterizedTest
	void hatBenutzerNurFunktionsbezogeneKompetenzWithMissingParameter(final BenutzerKompetenz givenKompetenzFunktionsbezogen,
			final Set<BenutzerKompetenz> givenKompetenzenUebergreifend) {
		final Throwable result =
				catchThrowable(() -> cut.hatBenutzerNurFunktionsbezogeneKompetenz(givenKompetenzFunktionsbezogen, givenKompetenzenUebergreifend));

		assertThat(result).isInstanceOf(IllegalArgumentException.class)
				.hasMessage("Die Parameter kompetenzFunktionsbezogen und kompetenzenUebergreifend dürfen nicht null oder leer sein.");
	}

	@Test
	void checkBenutzerFunktionsbezogeneKompetenzKlasseWithVorhandenerKompetenz() throws ApiOperationException {
		final Benutzer userMock = mock(Benutzer.class);

		when(conn.getUser()).thenReturn(userMock);
		when(userMock.getKlassenIDs()).thenReturn(Set.of(1L));

		cut.checkBenutzerFunktionsbezogeneKompetenzKlasse(1L);

		verify(userMock, times(1)).getKlassenIDs();
	}

	@Test
	void checkBenutzerFunktionsbezogeneKompetenzKlasseWithFehlenderKompetenzAndOhneWeitereLogInformationen() {
		final Benutzer userMock = mock(Benutzer.class);

		when(conn.getUser()).thenReturn(userMock);
		when(userMock.getKlassenIDs()).thenReturn(Collections.emptySet());

		final Throwable result = catchThrowable(() -> cut.checkBenutzerFunktionsbezogeneKompetenzKlasse(1L));

		assertThat(result).isInstanceOf(ApiOperationException.class)
				.hasMessage(
						"Der Benutzer hat keine funktionsbezogene Kompetenz für den Zugriff auf die Daten der Klasse N/A (ID: 1) des Schuljahresabschnittes N/A")
				.hasFieldOrPropertyWithValue("status", Response.Status.FORBIDDEN);
	}

	@Test
	void checkBenutzerFunktionsbezogeneKompetenzKlasseWithFehlenderKompetenzAndWeitereLogInformationen() {
		final Benutzer userMock = mock(Benutzer.class);
		final DTOKlassen klasseMock = new DTOKlassen(1L, 10L, "5a");
		final DTOSchuljahresabschnitte schuljahresabschnittMock = new DTOSchuljahresabschnitte(10L, 2024, 1);

		when(conn.getUser()).thenReturn(userMock);
		when(userMock.getKlassenIDs()).thenReturn(Collections.emptySet());

		when(conn.queryByKey(DTOKlassen.class, 1L)).thenReturn(klasseMock);
		when(conn.queryByKey(DTOSchuljahresabschnitte.class, 10L)).thenReturn(schuljahresabschnittMock);

		final Throwable result = catchThrowable(() -> cut.checkBenutzerFunktionsbezogeneKompetenzKlasse(1L));

		assertThat(result).isInstanceOf(ApiOperationException.class)
				.hasMessage(
						"Der Benutzer hat keine funktionsbezogene Kompetenz für den Zugriff auf die Daten der Klasse 5a (ID: 1) des Schuljahresabschnittes 2024.1")
				.hasFieldOrPropertyWithValue("status", Response.Status.FORBIDDEN);
	}

	@Test
	void checkBenutzerFunktionsbezogeneKompetenzKlasseWithKlassenIDIsNull() {
		final Throwable result = catchThrowable(() -> cut.checkBenutzerFunktionsbezogeneKompetenzKlasse(null));

		assertThat(result).isInstanceOf(ApiOperationException.class)
				.hasMessage("Der Benutzer kann keine funktionsbezogene Kompetenz nutzen, um auf Daten zuzugreifen, die keiner Klasse zugeordnet sind.")
				.hasFieldOrPropertyWithValue("status", Response.Status.FORBIDDEN);
	}


	/**
	 * Diese Klasse dient als Mock um die Funktionalitäten des DataManagerRevised testen zu können. Der Mock orientiert sich dabei an den Implementierungen
	 * von DataKlassendaten, weicht jedoch in seiner Komplexität stark ab.
	 */
	static class DataManagerRevisedMock extends DataManagerRevised<Long, DTOKlassen, KlassenDaten> {

		DataManagerRevisedMock(final DBEntityManager conn) {
			super(conn);
			setAttributesRequiredOnCreation("kuerzel", "idJahrgang");
			setAttributesDelayedOnCreation("sortierung");
			setAttributesNotPatchable("id", "idSchuljahresabschnitt", "kuerzelVorgaengerklasse", "kuerzelFolgeklasse", "pruefungsordnung");
		}

		@Override
		protected KlassenDaten map(final DTOKlassen dtoKlassen) {
			return klassenDaten;
		}

		@Override
		protected void initDTO(final DTOKlassen dtoKlassen, final Long id, final Map<String, Object> initAttributes) throws ApiOperationException {
			// for testing
		}

		@Override
		protected Long getID(final Map<String, Object> attributes) {
			return 1L;
		}

		@Override
		protected void mapAttribute(final DTOKlassen dto, final String name, final Object value, final Map<String, Object> map) throws ApiOperationException {
			switch (name) {
				case "kuerzel" -> dto.Klasse = JSONMapper.convertToString(value, false, false, 15);
				case "idJahrgang" -> dto.Jahrgang_ID = JSONMapper.convertToLong(value, true);
				case "sortierung" -> dto.Sortierung = JSONMapper.convertToInteger(value, true);
				default -> throw new ApiOperationException(Response.Status.BAD_REQUEST, "Mapping not implemented in mock");
			}
		}

		@Override
		public KlassenDaten getById(final Long id) {
			return klassenDaten;
		}

		@Override
		public List<KlassenDaten> getAll() {
			return List.of(klassenDaten);
		}

		@Override
		public List<KlassenDaten> getList() {
			return List.of(klassenDaten);
		}
	}
}
