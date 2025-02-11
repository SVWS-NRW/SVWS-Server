package de.svws_nrw.data.schule;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.schule.ReligionEintrag;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOKonfession;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.persistence.TypedQuery;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DataReligionenTest {

	@InjectMocks
	private DataReligionen cut;

	@Mock
	private DBEntityManager conn;

	@BeforeAll
	static void setUpAll() {
		ASDCoreTypeUtils.initAll();
	}

	@Test
	@DisplayName("initDTO | erfolgreiche Initialisierung")
	void initDTO() throws ApiOperationException {
		final var konfession = new DTOKonfession(-1L, "test");

		cut.initDTO(konfession, 1L, new HashMap<>());
		assertThat(konfession.ID).isEqualTo(1L);
	}

	@Test
	@DisplayName("getById | Religion mit ID existiert")
	void getByIdWithReligionExists() throws ApiOperationException {
		final DTOKonfession dto = createDTO(1L);
		when(conn.queryByKey(DTOKonfession.class, 1L)).thenReturn(dto);

		ReligionEintrag result = cut.getById(1L);

		assertThat(result)
				.hasFieldOrPropertyWithValue("id", 1L)
				.hasFieldOrPropertyWithValue("bezeichnung", "bezeichnung1")
				.hasFieldOrPropertyWithValue("bezeichnungZeugnis", "zeugnisBezeichnung1")
				.hasFieldOrPropertyWithValue("kuerzel", "statistikKrz1")
				.hasFieldOrPropertyWithValue("sortierung", 123)
				.hasFieldOrPropertyWithValue("istSichtbar", true);
	}

	@Test
	@DisplayName("getById | Religion mit ID existiert nicht")
	void getByIdWithReligionNotExists() {
		when(conn.queryByKey(DTOKonfession.class, 1L)).thenReturn(null);

		final Throwable result = catchThrowable(() -> cut.getById(1L));

		assertThat(result)
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND)
				.hasMessage("Es wurde kein Eintrag im Katalog der Religionen mit der ID 1 gefunden.");
	}

	@Test
	void getAll() {
		when(conn.queryAll(DTOKonfession.class)).thenReturn(List.of(createDTO(1L), createDTO(2L), createDTO(3L)));

		final List<ReligionEintrag> result = cut.getAll();
		assertThat(result)
				.hasSize(3)
				.hasOnlyElementsOfType(ReligionEintrag.class)
				.extracting("id")
				.contains(1L, 2L, 3L);
	}

	@Test
	@DisplayName("map | erfolgreiches mapping")
	void map() {
		final DTOKonfession dto = createDTO(2L);

		final ReligionEintrag result = cut.map(dto);
		assertThat(result)
				.hasFieldOrPropertyWithValue("id", 2L)
				.hasFieldOrPropertyWithValue("bezeichnung", "bezeichnung2")
				.hasFieldOrPropertyWithValue("bezeichnungZeugnis", "zeugnisBezeichnung2")
				.hasFieldOrPropertyWithValue("kuerzel", "statistikKrz2")
				.hasFieldOrPropertyWithValue("sortierung", 123)
				.hasFieldOrPropertyWithValue("istSichtbar", true);
	}

	private static Stream<Arguments> mapAttributeAttributes() {
		return Stream.of(
				arguments("kuerzel", "ER", "StatistikKrz"),
				arguments("bezeichnung", "wunderbar alles klar", "Bezeichnung"),
				arguments("bezeichnungZeugnis", "gutes Zeugnis", "ZeugnisBezeichnung"),
				arguments("sortierung", 1337, "Sortierung"),
				arguments("istSichtbar", true, "Sichtbar")
		);
	}

	@ParameterizedTest
	@DisplayName("mapAttribute | erfolgreiches mapping aller Attribute")
	@MethodSource("mapAttributeAttributes")
	void mapAttribute(final String key, final Object value, final String destAttributeKey) throws ApiOperationException, NoSuchFieldException,
			IllegalAccessException {
		cut = spy(cut);
		final DTOKonfession dto = createDTO(1L);

		// bedingtes stubbing
		if (key.equals("bezeichnung")) {
			doReturn(false).when(cut).bezeichnungExists("wunderbar alles klar");
		}

		cut.mapAttribute(dto, key, value, null);

		// Zielattribut via Reflection auslesen
		final Field field = DTOKonfession.class.getDeclaredField(destAttributeKey);
		field.setAccessible(true);
		assertThat(field.get(dto)).isEqualTo(value);
	}

	@Test
	void mapAttributeWithStatistikKrzNotExists() {
		final DTOKonfession dto = createDTO(1L);

		final Throwable result = catchThrowable(() -> cut.mapAttribute(dto, "kuerzel", "abc", null));
		assertThat(result)
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST)
				.hasMessage("Eine Religion mit dem Kürzel abc existiert in der amtlichen Schulstatistik nicht.");
	}

	@Test
	void mapAttributeWithbezeichnungAlreadyExists() {
		cut = spy(cut);
		final DTOKonfession dto = createDTO(1L);

		doReturn(true).when(cut).bezeichnungExists("bezeichnung1");

		final Throwable result = catchThrowable(() -> cut.mapAttribute(dto, "bezeichnung", "bezeichnung1", null));
		assertThat(result)
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST)
				.hasMessage("Eine Religion mit der Bezeichnung bezeichnung1 existiert bereits. Die Bezeichnung muss eindeutig sein.");
	}

	@Test
	void mapAttributeWithUnsupportedAttribute() {
		final DTOKonfession dto = createDTO(1L);

		final Throwable result = catchThrowable(() -> cut.mapAttribute(dto, "foo", "faa", null));
		assertThat(result)
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST)
				.hasMessage("Für das Attribut foo existiert kein Patch Mapping.");
	}

	@Test
	@DisplayName("bezeichnungExists | mit Bezeichnung die bereits existiert")
	@SuppressWarnings({ "rawtypes", "unchecked" })
	void bezeichnungExistsWithNoBezeichnungExists() {
		final TypedQuery queryMock = mock(TypedQuery.class);

		when(queryMock.getResultList()).thenReturn(List.of(mock(DTOKonfession.class)));
		when(queryMock.setParameter(1, "testBezeichnung")).thenReturn(queryMock);

		when(conn.query(DTOKonfession.QUERY_BY_BEZEICHNUNG, DTOKonfession.class)).thenReturn(queryMock);

		final boolean result = cut.bezeichnungExists("testBezeichnung");
		assertThat(result).isTrue();
	}

	@Test
	@DisplayName("bezeichnungExists | Bezeichnung existiert noch nicht")
	@SuppressWarnings({ "rawtypes", "unchecked" })
	void bezeichnungExistsWithBezeichnungExists() {
		final TypedQuery queryMock = mock(TypedQuery.class);

		when(queryMock.getResultList()).thenReturn(Collections.emptyList());
		when(queryMock.setParameter(1, "testBezeichnung")).thenReturn(queryMock);

		when(conn.query(DTOKonfession.QUERY_BY_BEZEICHNUNG, DTOKonfession.class)).thenReturn(queryMock);

		final boolean result = cut.bezeichnungExists("testBezeichnung");
		assertThat(result).isFalse();
	}

	private DTOKonfession createDTO(final long id) {
		final DTOKonfession konfession = new DTOKonfession(id, "bezeichnung" + id);
		konfession.ZeugnisBezeichnung = "zeugnisBezeichnung" + id;
		konfession.ExportBez = "exportBez" + id;
		konfession.StatistikKrz = "statistikKrz" + id;
		konfession.Sortierung = 123;
		konfession.Aenderbar = true;
		konfession.Sichtbar = true;
		return konfession;
	}
}
