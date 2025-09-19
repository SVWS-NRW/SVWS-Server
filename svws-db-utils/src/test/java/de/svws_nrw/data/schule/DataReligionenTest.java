package de.svws_nrw.data.schule;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import de.svws_nrw.asd.types.schule.Religion;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.schule.ReligionEintrag;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOKonfession;
import de.svws_nrw.db.utils.ApiOperationException;
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
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DataReligionenTest {

	@InjectMocks
	private DataReligionen data;

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

		data.initDTO(konfession, 1L, new HashMap<>());
		assertThat(konfession.ID).isEqualTo(1L);
	}

	@Test
	@DisplayName("getById | Religion mit ID existiert")
	void getByIdWithReligionExists() throws ApiOperationException {
		final DTOKonfession dto = createDTO(1L);
		when(conn.queryByKey(DTOKonfession.class, 1L)).thenReturn(dto);

		final ReligionEintrag result = data.getById(1L);

		assertThat(result)
				.hasFieldOrPropertyWithValue("id", 1L)
				.hasFieldOrPropertyWithValue("bezeichnung", "bezeichnung1")
				.hasFieldOrPropertyWithValue("bezeichnungZeugnis", "zeugnisBezeichnung1")
				.hasFieldOrPropertyWithValue("kuerzel", "statistikKrz1")
				.hasFieldOrPropertyWithValue("sortierung", 123)
				.hasFieldOrPropertyWithValue("istSichtbar", true);
	}

	@Test
	@DisplayName("getByID | ID can't be null")
	void getByIdTest_IdNull() {
		final var throwable = catchThrowable(() -> this.data.getById(null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Eine Anfrage mit der ID null ist unzulässig.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("getById | Religion mit ID existiert nicht")
	void getByIdWithReligionNotExists() {
		when(conn.queryByKey(DTOKonfession.class, 1L)).thenReturn(null);

		final Throwable result = catchThrowable(() -> data.getById(1L));

		assertThat(result)
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND)
				.hasMessage("Es wurde kein Eintrag im Katalog der Religionen mit der ID 1 gefunden.");
	}

	@Test
	void getAll() {
		when(conn.queryAll(DTOKonfession.class)).thenReturn(List.of(createDTO(1L), createDTO(2L), createDTO(3L)));

		final List<ReligionEintrag> result = data.getAll();
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

		final ReligionEintrag result = data.map(dto);
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
				arguments("id", 35),
				arguments("bezeichnung", "test"),
				arguments("bezeichnungZeugnis", "asd"),
				arguments("istSichtbar", true),
				arguments("kuerzel", Religion.AR.historie().getFirst().kuerzel),
				arguments("sortierung", 25),
				arguments("unknownArgument", "oh oh ! das wollen wir auf keinen Fall!")
		);
	}

	@ParameterizedTest
	@DisplayName("mapAttribute | erfolgreiches mapping aller Attribute")
	@MethodSource("mapAttributeAttributes")
	void mapAttribute(final String key, final Object value) {
		final var dto = new DTOKonfession(1L, "abc");

		final var throwable = catchThrowable(() -> this.data.mapAttribute(dto, key, value, null));

		switch (key) {
			case "id" -> assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessage("Die ID 35 des Patches ist null oder stimmt nicht mit der ID 1 in der Datenbank überein.")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
			case "bezeichnung" -> assertThat(dto.Bezeichnung).isEqualTo(value);
			case "bezeichnungZeugnis" -> assertThat(dto.ZeugnisBezeichnung).isEqualTo(value);
			case "istSichtbar" -> assertThat(dto.Sichtbar).isEqualTo(value);
			case "kuerzel" -> assertThat(dto.StatistikKrz).isEqualTo(value);
			case "sortierung" -> assertThat(dto.Sortierung).isEqualTo(value);
			default -> assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessageStartingWith("Die Daten des Patches enthalten das unbekannte Attribut")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
		}

	}

	@Test
	void mapAttributeWithStatistikKrzNotExists() {
		final DTOKonfession dto = createDTO(1L);

		final Throwable result = catchThrowable(() -> data.mapAttribute(dto, "kuerzel", "abc", null));
		assertThat(result)
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST)
				.hasMessage("Eine Religion mit dem Kürzel abc existiert in der amtlichen Schulstatistik nicht.");
	}

	@Test
	@DisplayName("mapAttribute | bezeichnung bereits vorhanden")
	void mapAttributeTest_bezeichnungDoppeltVergeben() {
		final var religion = new DTOKonfession(1L, "abc");
		when(this.conn.queryAll(DTOKonfession.class)).thenReturn(List.of(religion));

		final var throwable = catchThrowable(() -> this.data.mapAttribute(new DTOKonfession(2L, "DEF"), "bezeichnung", "ABC", null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Die Bezeichnung ABC ist bereits vorhanden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | bezeichnung unverändert")
	void mapAttributeTest_bezeichnungUnchanging() {
		final var dto = new DTOKonfession(1L, "abc");

		assertThatNoException().isThrownBy(() -> this.data.mapAttribute(dto, "bezeichnung", "abc", null));

		verifyNoInteractions(this.conn);
		assertThat(dto.Bezeichnung).isEqualTo("abc");
	}

	@Test
	@DisplayName("mapAttribute | bezeichnung null")
	void mapAttributeTest_bezeichnungNull() {
		final var throwable = catchThrowable(() -> this.data.mapAttribute(mock(DTOKonfession.class), "bezeichnung", null, null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Attribut bezeichnung: Der Wert null ist nicht erlaubt.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | bezeichnung blank")
	void mapAttributeTest_bezeichnungBlank() {
		final var throwable = catchThrowable(() -> this.data.mapAttribute(mock(DTOKonfession.class), "bezeichnung", "", null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Attribut bezeichnung: Ein leerer String ist hier nicht erlaubt.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
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
