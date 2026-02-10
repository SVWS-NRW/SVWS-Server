package de.svws_nrw.data.lehrer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import de.svws_nrw.asd.data.lehrer.LehrerLehramtEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdaten;
import de.svws_nrw.asd.data.lehrer.LehrerPersonaldaten;
import de.svws_nrw.asd.types.lehrer.LehrerAbgangsgrund;
import de.svws_nrw.asd.types.lehrer.LehrerZugangsgrund;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;
import org.apache.commons.lang3.RandomStringUtils;
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
import static org.assertj.core.api.Assertions.assertThatException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

/**
 * Diese Klasse testet die Klasse {@link DataLehrerPersonaldaten}.
 */
@DisplayName("Diese Klasse testet die Klasse DataLehrerPersonaldaten")
@ExtendWith(MockitoExtension.class)
class DataLehrerPersonaldatenTest {


	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private DataLehrerPersonaldaten data;

	@BeforeAll
	static void setUpAll() {
		ASDCoreTypeUtils.initAll();
	}


	@Test
	@DisplayName("setAttributesNotPatchable: id")
	void setAttributesNotPatchableId() {
		when(this.conn.queryByKey(DTOLehrer.class, 1L)).thenReturn(mock(DTOLehrer.class));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("id", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Folgende Attribute werden für ein Patch nicht zugelassen: id.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("initDTO | erfolgreiches Update der ID")
	void initDTOTest() throws ApiOperationException {
		final var dto = new DTOLehrer(1L, "kuerzel", "name");

		this.data.initDTO(dto, 2L, null);

		assertThat(dto.ID).isEqualTo(2L);
	}

	@Test
	@DisplayName("getById | Erfolg")
	void getById() throws ApiOperationException {
		final var dto = new DTOLehrer(1L, "kuerzel", "name");

		when(this.conn.queryByKey(DTOLehrer.class, 1L)).thenReturn(dto);

		assertThat(this.data.getById(1L))
				.isInstanceOf(LehrerPersonaldaten.class)
				.hasFieldOrPropertyWithValue("id", dto.ID);
	}

	@Test
	@DisplayName("getByID | ID can't be null")
	void getByIdNull() {
		assertThatException()
				.isThrownBy(() -> this.data.getById(null))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die ID des Lehrers darf nicht null sein.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("getByID | id not found")
	void getByIdNotFound() {
		when(this.conn.queryByKey(DTOLehrer.class, 99L)).thenReturn(null);

		assertThatException()
				.isThrownBy(() -> this.data.getById(99L))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Keine Lehrkraft mit der ID 99 gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("map")
	void map() throws ApiOperationException {
		final var dto = new DTOLehrer(1L, "kuerzel", "name");
		dto.ID = 1L;
		dto.identNrTeil1 = "identNrTeil1";
		dto.identNrTeil2SerNr = "identNrTeil2SerNr";
		dto.PANr = "PANr";
		dto.personalNrLBV = "personalNrLBV";
		dto.verguetungsSchluessel = "verguetungsSchluessel";
		dto.DatumZugang = "DatumZugang";
		dto.GrundZugang = "GrundZugang";
		dto.DatumAbgang = "DatumAbgang";
		dto.GrundAbgang = "GrundAbgang";

		final var mockAbschnittsdaten = List.of(new LehrerPersonalabschnittsdaten());
		final var mockLehraemter = List.of(new LehrerLehramtEintrag());

		try (var mockedAbschnittsdaten = mockStatic(DataLehrerPersonalabschnittsdaten.class);
				var mockedLehraemter = mockStatic(DataLehrerLehramt.class)) {

			mockedAbschnittsdaten
					.when(() -> DataLehrerPersonalabschnittsdaten.getByLehrerId(conn, dto.ID))
					.thenReturn(mockAbschnittsdaten);
			mockedLehraemter
					.when(() -> DataLehrerLehramt.getListByLehrerId(conn, dto.ID))
					.thenReturn(mockLehraemter);

			final var result = this.data.map(dto);

			assertThat(result)
					.isInstanceOf(LehrerPersonaldaten.class)
					.hasFieldOrPropertyWithValue("id", dto.ID)
					.hasFieldOrPropertyWithValue("identNrTeil1", dto.identNrTeil1)
					.hasFieldOrPropertyWithValue("identNrTeil2SerNr", dto.identNrTeil2SerNr)
					.hasFieldOrPropertyWithValue("personalaktennummer", dto.PANr)
					.hasFieldOrPropertyWithValue("lbvPersonalnummer", dto.personalNrLBV)
					.hasFieldOrPropertyWithValue("lbvVerguetungsschluessel", dto.verguetungsSchluessel)
					.hasFieldOrPropertyWithValue("zugangsdatum", dto.DatumZugang)
					.hasFieldOrPropertyWithValue("zugangsgrund", dto.GrundZugang)
					.hasFieldOrPropertyWithValue("abgangsdatum", dto.DatumAbgang)
					.hasFieldOrPropertyWithValue("abgangsgrund", dto.GrundAbgang)
					.extracting("abschnittsdaten", "lehraemter")
					.containsExactly(mockAbschnittsdaten, mockLehraemter);
		}
	}

	private static Stream<Arguments> patchStringAttributesTooManyCharacters() {
		return Stream.of(
				arguments("identNrTeil1", "Attribut identNrTeil1: Die Länge des Strings ist auf 10 Zeichen limitiert."),
				arguments("identNrTeil2SerNr", "Attribut identNrTeil2SerNr: Die Länge des Strings ist auf 5 Zeichen limitiert."),
				arguments("personalaktennummer", "Attribut personalaktennummer: Die Länge des Strings ist auf 20 Zeichen limitiert."),
				arguments("lbvPersonalnummer", "Attribut lbvPersonalnummer: Die Länge des Strings ist auf 15 Zeichen limitiert."),
				arguments("lbvVerguetungsschluessel", "Attribut lbvVerguetungsschluessel: Die Länge des Strings ist auf 1 Zeichen limitiert."),
				arguments("zugangsgrund", "Attribut Zugangsgrund: Die Länge des Strings ist auf 10 Zeichen limitiert."),
				arguments("abgangsgrund", "Attribut Abgangsgrund: Die Länge des Strings ist auf 10 Zeichen limitiert.")
		);
	}

	@ParameterizedTest
	@MethodSource("patchStringAttributesTooManyCharacters")
	@DisplayName("patch | String Attributes Too Many Characters")
	void patchAttributeTooManyCharacters(final String attributeName, final String expectedMessage) {
		when(this.conn.queryByKey(DTOLehrer.class, 1L)).thenReturn(mock(DTOLehrer.class));

		assertThatException()
				.isThrownBy(() -> data.patch(1L, Map.of(attributeName, RandomStringUtils.insecure().nextAscii(21))))
				.isInstanceOf(ApiOperationException.class)
				.withMessage(expectedMessage)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	private static Stream<Arguments> patchStringAttributes() {
		return Stream.of(
				arguments("identNrTeil1"),
				arguments("identNrTeil2SerNr"),
				arguments("personalaktennummer"),
				arguments("lbvPersonalnummer"),
				arguments("lbvVerguetungsschluessel"),
				arguments("unknown")
		);
	}

	@ParameterizedTest
	@MethodSource("patchStringAttributes")
	@DisplayName("patch String attributes")
	void patchStringAttributes(final String attributeName) {
		final var dto = new DTOLehrer(1L, "kuerzel", "name");
		dto.ID = 1L;
		when(this.conn.queryByKey(DTOLehrer.class, 1L)).thenReturn(dto);
		lenient().when(this.conn.transactionPersist(any())).thenReturn(true);
		final var newValue = "a";

		final var throwable = catchThrowable(() -> this.data.patch(1L, Map.of(attributeName, newValue)));

		switch (attributeName) {
			case "identNrTeil1" -> assertThat(dto.identNrTeil1).isEqualTo(newValue);
			case "identNrTeil2SerNr" -> assertThat(dto.identNrTeil2SerNr).isEqualTo(newValue);
			case "personalaktennummer" -> assertThat(dto.PANr).isEqualTo(newValue);
			case "lbvPersonalnummer" -> assertThat(dto.personalNrLBV).isEqualTo(newValue);
			case "lbvVerguetungsschluessel" -> assertThat(dto.verguetungsSchluessel).isEqualTo(newValue);
			default -> assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessage("Die Daten des Patches enthalten das unbekannte Attribut unknown.")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
		}
	}

	private static Stream<Arguments> patchStringAttributesBlank() {
		return Stream.of(
				arguments("identNrTeil1"),
				arguments("identNrTeil2SerNr"),
				arguments("personalaktennummer"),
				arguments("lbvPersonalnummer"),
				arguments("lbvVerguetungsschluessel"),
				arguments("unknown")
		);
	}

	@ParameterizedTest
	@MethodSource("patchStringAttributesBlank")
	@DisplayName("patch String attributes blank")
	void patchStringAttributesBlank(final String attributeName) {
		final var dto = new DTOLehrer(1L, "kuerzel", "name");
		dto.ID = 1L;
		when(this.conn.queryByKey(DTOLehrer.class, 1L)).thenReturn(dto);
		lenient().when(this.conn.transactionPersist(any())).thenReturn(true);
		final var newValue = "    ";

		final var throwable = catchThrowable(() -> this.data.patch(1L, Map.of(attributeName, newValue)));

		switch (attributeName) {
			case "identNrTeil1" -> assertThat(dto.identNrTeil1).isBlank();
			case "identNrTeil2SerNr" -> assertThat(dto.identNrTeil2SerNr).isBlank();
			case "personalaktennummer" -> assertThat(dto.PANr).isBlank();
			case "lbvPersonalnummer" -> assertThat(dto.personalNrLBV).isBlank();
			case "lbvVerguetungsschluessel" -> assertThat(dto.verguetungsSchluessel).isBlank();
			default -> assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessage("Die Daten des Patches enthalten das unbekannte Attribut unknown.")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
		}
	}

	private static Stream<Arguments> patchStringAttributesNull() {
		return Stream.of(
				arguments("identNrTeil1"),
				arguments("identNrTeil2SerNr"),
				arguments("personalaktennummer"),
				arguments("lbvPersonalnummer"),
				arguments("lbvVerguetungsschluessel"),
				arguments("unknown")
		);
	}

	@ParameterizedTest
	@MethodSource("patchStringAttributesNull")
	@DisplayName("patch String attributes null")
	void patchStringAttributesNull(final String attributeName) {
		final var dto = new DTOLehrer(1L, "kuerzel", "name");
		dto.ID = 1L;
		when(this.conn.queryByKey(DTOLehrer.class, 1L)).thenReturn(dto);
		lenient().when(this.conn.transactionPersist(any())).thenReturn(true);
		final var map = new HashMap<String, Object>();
		map.put(attributeName, null);

		final var throwable = catchThrowable(() -> this.data.patch(1L, map));

		switch (attributeName) {
			case "identNrTeil1" -> assertThat(dto.identNrTeil1).isNull();
			case "identNrTeil2SerNr" -> assertThat(dto.identNrTeil2SerNr).isNull();
			case "personalaktennummer" -> assertThat(dto.PANr).isNull();
			case "lbvPersonalnummer" -> assertThat(dto.personalNrLBV).isNull();
			case "lbvVerguetungsschluessel" -> assertThat(dto.verguetungsSchluessel).isNull();
			default -> assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessage("Die Daten des Patches enthalten das unbekannte Attribut unknown.")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
		}
	}

	private static Stream<Arguments> patchDateAttributes() {
		return Stream.of(
				arguments("zugangsdatum"),
				arguments("abgangsdatum")
		);
	}

	@ParameterizedTest
	@MethodSource("patchDateAttributes")
	@DisplayName("patch Date attributes")
	void patchDateAttributes(final String attributeName) throws ApiOperationException {
		final var dto = new DTOLehrer(1L, "kuerzel", "name");
		dto.ID = 1L;
		when(this.conn.queryByKey(DTOLehrer.class, 1L)).thenReturn(dto);
		lenient().when(this.conn.transactionPersist(any())).thenReturn(true);
		final var newValue = "2024-12-12";

		this.data.patch(1L, Map.of(attributeName, newValue));

		switch (attributeName) {
			case "zugangsdatum" -> assertThat(dto.DatumZugang).isEqualTo(newValue);
			case "abgangsdatum" -> assertThat(dto.DatumAbgang).isEqualTo(newValue);
			default -> {
				// do nothing
			}
		}
	}

	private static Stream<Arguments> patchDateAttributesWrongFormat() {
		return Stream.of(
				arguments("zugangsdatum", "Das Datumsformat für abc ist ungültig"),
				arguments("abgangsdatum", "Das Datumsformat für abc ist ungültig")
		);
	}

	@ParameterizedTest
	@MethodSource("patchDateAttributesWrongFormat")
	@DisplayName("patch | Date Attributes Wrong Format")
	void patchDateAttributeWrongFormat(final String attributeName, final String expectedMessage) {
		when(this.conn.queryByKey(DTOLehrer.class, 1L)).thenReturn(mock(DTOLehrer.class));

		assertThatException()
				.isThrownBy(() -> data.patch(1L, Map.of(attributeName, "abc")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage(expectedMessage)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | zugangsgrund | null")
	void patchZugangsgrundNull() throws ApiOperationException {
		final var dto = new DTOLehrer(1L, "kuerzel", "name");
		dto.GrundZugang = "old";
		final var map = new HashMap<String, Object>();
		map.put("zugangsgrund", null);
		when(this.conn.queryByKey(DTOLehrer.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, map);

		assertThat(dto.GrundZugang).isNull();
	}

	@Test
	@DisplayName("patch | zugangsgrund | blank")
	void patchZugangsgrundBlank() throws ApiOperationException {
		final var dto = new DTOLehrer(1L, "kuerzel", "name");
		dto.GrundZugang = "old";
		when(this.conn.queryByKey(DTOLehrer.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("zugangsgrund", "  "));

		assertThat(dto.GrundZugang).isBlank();
	}

	@Test
	@DisplayName("patch | zugangsgrund | wrong kuerzel")
	void patchZugangsgrundWrongKuerzel() {
		final var dto = new DTOLehrer(1L, "kuerzel", "name");
		when(this.conn.queryByKey(DTOLehrer.class, 1L)).thenReturn(dto);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("zugangsgrund", "XY")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Kein Zugangsgrund mit dem Kürzel XY gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);

	}

	@Test
	@DisplayName("patch | zugangsgrund")
	void patchZugangsgrund() throws ApiOperationException {
		final var dto = new DTOLehrer(1L, "kuerzel", "name");
		when(this.conn.queryByKey(DTOLehrer.class, 1L)).thenReturn(dto);
		when(this.conn.getUser()).thenReturn(mock(Benutzer.class));
		when(this.conn.getUser().schuleGetSchuljahr()).thenReturn(2024);
		when(this.conn.transactionPersist(any())).thenReturn(true);
		final var kuerzel = LehrerZugangsgrund.AndBuLand.historie().getFirst().kuerzel;

		this.data.patch(1L, Map.of("zugangsgrund", kuerzel));

		assertThat(dto.GrundZugang).isEqualTo(kuerzel);

	}

	@Test
	@DisplayName("patch | abgangsgrund | null")
	void patchAbgangsgrundNull() throws ApiOperationException {
		final var dto = new DTOLehrer(1L, "kuerzel", "name");
		dto.GrundAbgang = "old";
		final var map = new HashMap<String, Object>();
		map.put("abgangsgrund", null);
		when(this.conn.queryByKey(DTOLehrer.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, map);

		assertThat(dto.GrundAbgang).isNull();
	}

	@Test
	@DisplayName("patch | abgangsgrund | blank")
	void patchAbgangsgrundBlank() throws ApiOperationException {
		final var dto = new DTOLehrer(1L, "kuerzel", "name");
		dto.GrundAbgang = "old";
		when(this.conn.queryByKey(DTOLehrer.class, 1L)).thenReturn(dto);
		when(this.conn.transactionPersist(any())).thenReturn(true);

		this.data.patch(1L, Map.of("abgangsgrund", "  "));

		assertThat(dto.GrundAbgang).isBlank();
	}

	@Test
	@DisplayName("patch | abgangsgrund | wrong kuerzel")
	void patchAbgangsgrundWrongKuerzel() {
		final var dto = new DTOLehrer(1L, "kuerzel", "name");
		when(this.conn.queryByKey(DTOLehrer.class, 1L)).thenReturn(dto);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("abgangsgrund", "XY")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Kein Abgangsgrund mit dem Kürzel XY gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | abgangsgrund")
	void patchAbgangsgrund() throws ApiOperationException {
		final var dto = new DTOLehrer(1L, "kuerzel", "name");
		when(this.conn.queryByKey(DTOLehrer.class, 1L)).thenReturn(dto);
		when(this.conn.getUser()).thenReturn(mock(Benutzer.class));
		when(this.conn.getUser().schuleGetSchuljahr()).thenReturn(2024);
		when(this.conn.transactionPersist(any())).thenReturn(true);
		final var kuerzel = LehrerAbgangsgrund.AndBuLand.historie().getFirst().kuerzel;

		this.data.patch(1L, Map.of("abgangsgrund", kuerzel));

		assertThat(dto.GrundAbgang).isEqualTo(kuerzel);
	}

}
