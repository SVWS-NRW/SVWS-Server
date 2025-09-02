package de.svws_nrw.data.kurse;

import de.svws_nrw.asd.data.kurse.KursLehrer;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKursLehrer;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/** Diese Klasse testet die Klasse {@link DataKursLehrer}*/
@DisplayName("Diese Klasse testet die Klasse DataKursLehrer")
@ExtendWith(MockitoExtension.class)
class DataKursLehrerTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private DataKursLehrer data;

	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}

	@Test
	@DisplayName("setAttributesRequiredOnCreation | idLehrer")
	void setAttributesRequiredOnCreationTest_idLehrer() {
		try (var mapperMock = Mockito.mockStatic(JSONMapper.class)) {
			mapperMock.when(() -> JSONMapper.toMap(any(InputStream.class))).thenReturn(Map.of("idKurs", 17, "wochenstundenLehrer", 3));

			final var throwable = catchThrowable(() -> this.data.addAsResponse(mock(InputStream.class)));

			assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessage("Es werden weitere Attribute (idLehrer) benötigt, damit die Entität erstellt werden kann.")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
		}
	}

	@Test
	@DisplayName("setAttributesRequiredOnCreation | idKurs")
	void setAttributesRequiredOnCreationTest_idKurs() {
		try (var mapperMock = Mockito.mockStatic(JSONMapper.class)) {
			mapperMock.when(() -> JSONMapper.toMap(any(InputStream.class))).thenReturn(Map.of("idLehrer", 17, "wochenstundenLehrer", 3));

			final var throwable = catchThrowable(() -> this.data.addAsResponse(mock(InputStream.class)));

			assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessage("Es werden weitere Attribute (idKurs) benötigt, damit die Entität erstellt werden kann.")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
		}
	}

	@Test
	@DisplayName("setAttributesNotPatchable | idLehrer")
	void setAttributesNotPatchableTest_idLehrer() {
		final var dto = getDto();
		when(this.conn.queryByKey(DTOKursLehrer.class, 1L, 2L)).thenReturn(dto);

		try (var mapperMock = Mockito.mockStatic(JSONMapper.class)) {
			mapperMock.when(() -> JSONMapper.toMap(any(InputStream.class))).thenReturn(Map.of("idLehrer", 3L));

			final var throwable = catchThrowable(() -> this.data.patchAsResponse(new Long[]{1L, 2L}, mock(InputStream.class)));

			assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessage("Folgende Attribute werden für ein Patch nicht zugelassen: idLehrer.");
		}
	}

	@Test
	@DisplayName("setAttributesNotPatchable | idKurs")
	void setAttributesNotPatchableTest_idKurs() {
		final var dto = getDto();
		when(this.conn.queryByKey(DTOKursLehrer.class, 1L, 2L)).thenReturn(dto);

		try (var mapperMock = Mockito.mockStatic(JSONMapper.class)) {
			mapperMock.when(() -> JSONMapper.toMap(any(InputStream.class))).thenReturn(Map.of("idKurs", 3L));

			final var throwable = catchThrowable(() -> this.data.patchAsResponse(new Long[]{1L, 2L}, mock(InputStream.class)));

			assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessage("Folgende Attribute werden für ein Patch nicht zugelassen: idKurs.");
		}
	}

	@Test
	@DisplayName("initDTO | Erfolg")
	void initDTOTest() {
		final var dto = getDto();
		this.data = new DataKursLehrer(this.conn, 1L);
		this.data.initDTO(dto, new Long[]{1L, 2L}, null);

		assertThat(dto)
				.hasFieldOrPropertyWithValue("Kurs_ID", 1L)
				.hasFieldOrPropertyWithValue("Lehrer_ID", 2L);
	}

	@Test
	@DisplayName("getLongId | Erfolg")
	void getLongIdTest() {
		final var dto = getDto();

		assertThat(this.data.getLongId(dto)).isEqualTo(2L);
	}

	@Test
	@DisplayName("getById | Erfolg")
	void getByIdTest() throws ApiOperationException {
		final var dto = getDto();
		when(this.conn.queryByKey(DTOKursLehrer.class, 1L, 2L)).thenReturn(dto);

		assertThat(this.data.getById(new Long[]{1L, 2L}))
				.isInstanceOf(KursLehrer.class)
				.hasFieldOrPropertyWithValue("idLehrer", dto.Lehrer_ID)
				.hasFieldOrPropertyWithValue("idKurs", dto.Kurs_ID);
	}

	@Test
	@DisplayName("getAll | Erfolg")
	void getAllTest() {
		final var dto1 = getDto();
		final var dto2 = getDto();
		when(this.conn.queryAll(DTOKursLehrer.class)).thenReturn(List.of(dto1, dto2));

		assertThat(this.data.getAll())
				.isInstanceOf(List.class)
				.isNotNull()
				.isNotEmpty()
				.hasSize(2)
				.allSatisfy(item -> assertThat(item)
						.isInstanceOf(KursLehrer.class)
						.hasFieldOrProperty("idLehrer")
						.hasFieldOrProperty("idKurs")
						.hasFieldOrProperty("wochenstundenLehrer"));
	}

	@Test
	@DisplayName("getAll | Database empty")
	void getAllTest_Empty() {
		assertThat(this.data.getAll()).isNotNull().isEmpty();
	}

	@Test
	@DisplayName("map | Erfolg")
	void mapTest() {
		final var dto = getDto();
		dto.Anteil = 3d;

		assertThat(this.data.map(dto))
				.isInstanceOf(KursLehrer.class)
				.hasFieldOrPropertyWithValue("idLehrer", dto.Lehrer_ID)
				.hasFieldOrPropertyWithValue("idKurs", dto.Kurs_ID)
				.hasFieldOrPropertyWithValue("wochenstundenLehrer", dto.Anteil);
	}

	private static Stream<Arguments> provideMappingAttributes() {
		return Stream.of(
				arguments("idLehrer", 35),
				arguments("idKurs", 42),
				arguments("wochenstundenLehrer", 13d),
				arguments("unknownArgument", "oh oh ! das wollen wir auf keinen Fall!")
		);
	}

	@ParameterizedTest
	@DisplayName("mapAttribute | Erfolg")
	@MethodSource("provideMappingAttributes")
	void mapAttributeTest(final String key, final Object value) {
		final var dto = getDto();

		final var throwable = catchThrowable(() -> this.data.mapAttribute(dto, key, value, null));

		switch (key) {
			case "idKurs" -> assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessage("Die ID 42 des Patches ist null oder stimmt nicht mit der ID 1 in der Datenbank überein.")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
			case "idLehrer" -> assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessage("Die ID 35 des Patches ist null oder stimmt nicht mit der ID 2 in der Datenbank überein.")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
			case "wochenstundenLehrer" -> assertThat(dto.Anteil).isEqualTo(value);
			default -> assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessageStartingWith("Die Daten des Patches enthalten das unbekannte Attribut")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
		}
	}

	@Test
	@DisplayName("mapAttribute | wochenstunden below zero")
	void mapAttributeTest_wochenStundenBelowZero() {
		final var dto = getDto();

		final var throwable = catchThrowable(() -> this.data.mapAttribute(dto, "wochenstundenLehrer", -4, null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Eine negative Anzahl an Wochenstunden ist nicht gestattet")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	private static DTOKursLehrer getDto() {
		return new DTOKursLehrer(1L, 2L);
	}

}
