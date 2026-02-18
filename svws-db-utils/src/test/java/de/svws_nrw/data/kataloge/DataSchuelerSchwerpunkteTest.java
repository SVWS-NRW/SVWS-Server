package de.svws_nrw.data.kataloge;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.kataloge.SchuelerSchwerpunkt;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOSchuelerSchwerpunkt;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.persistence.TypedQuery;
import jakarta.ws.rs.core.Response;
import org.assertj.core.api.ThrowableAssert;
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
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Diese Klasse testet die Klasse DataSchuelerSchwerpunkte")
@ExtendWith(MockitoExtension.class)
class DataSchuelerSchwerpunkteTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private DataSchuelerSchwerpunkte data;

	@BeforeAll
	static void setup() {
		ASDCoreTypeUtils.initAll();
	}

	@Test
	@DisplayName("initDTO | Erfolg")
	void initDTOTest() {
		final var dto = getDto();

		this.data.initDTO(dto, 2L, null);

		assertThat(dto.ID).isEqualTo(2L);
	}

	@Test
	@DisplayName("getLongId | Erfolg")
	void getLongIdTest() {
		final var dto = getDto();

		assertThat(this.data.getLongId(dto)).isEqualTo(1L);
	}


	@Test
	@DisplayName("getById | Erfolg")
	void getByIdTest() throws ApiOperationException {
		final var dto = getDto();
		when(this.conn.queryByKey(DTOSchuelerSchwerpunkt.class, 1L)).thenReturn(dto);
		setupReferencedQueryMock(List.of(2L));

		assertThat(this.data.getById(1L))
				.isInstanceOf(SchuelerSchwerpunkt.class)
				.hasFieldOrPropertyWithValue("id", dto.ID);
	}

	@Test
	@DisplayName("getByID | ID can't be null")
	void getByIdTest_IdNull() {
		assertThatException().isThrownBy(() -> this.data.getById(null))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die ID des Schwerpunktes darf nicht null sein.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("getByID | id not found")
	void getByIdTest_IdNotFound() {
		final var throwable = catchThrowable(() -> this.data.getById(99L));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Es wurde kein Schwerpunkt mit der ID 99 gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("getAll | Erfolg")
	void getAllTest() {
		final long firstReferencedId = 1L;
		final long secondReferencedId = 2L;
		final var firstReferencedDto = new DTOSchuelerSchwerpunkt(firstReferencedId, "1");
		final var secondReferencedDto = new DTOSchuelerSchwerpunkt(secondReferencedId, "2");
		final var unreferencedDto = new DTOSchuelerSchwerpunkt(3L, "3");
		when(this.conn.queryAll(DTOSchuelerSchwerpunkt.class)).thenReturn(List.of(firstReferencedDto, secondReferencedDto, unreferencedDto));
		setupReferencedQueryMock(List.of(firstReferencedId, secondReferencedId));


		final List<SchuelerSchwerpunkt> allSchwerpunkte = this.data.getAll();
		assertThat(allSchwerpunkte)
				.hasSize(3)
				.satisfiesExactlyInAnyOrder(
						s1 -> assertThat(s1)
								.isInstanceOf(SchuelerSchwerpunkt.class)
								.hasFieldOrPropertyWithValue("id", firstReferencedId)
								.hasFieldOrPropertyWithValue("bezeichnung", "1")
								.hasFieldOrPropertyWithValue("referenziertInAnderenTabellen", true),
						s2 -> assertThat(s2)
								.isInstanceOf(SchuelerSchwerpunkt.class)
								.hasFieldOrPropertyWithValue("id", secondReferencedId)
								.hasFieldOrPropertyWithValue("bezeichnung", "2")
								.hasFieldOrPropertyWithValue("referenziertInAnderenTabellen", true),
						s3 -> assertThat(s3)
								.isInstanceOf(SchuelerSchwerpunkt.class)
								.hasFieldOrPropertyWithValue("id", 3L)
								.hasFieldOrPropertyWithValue("bezeichnung", "3")
								.hasFieldOrPropertyWithValue("referenziertInAnderenTabellen", false)
				);
	}

	@Test
	@DisplayName("map | Erfolg")
	void mapTest() {
		final var dto = getDto();
		dto.Bezeichnung = "test-bezeichnung";
		dto.Aenderbar = true;
		dto.Sortierung = 25;
		dto.Sichtbar = true;

		assertThat(this.data.map(dto))
				.isInstanceOf(SchuelerSchwerpunkt.class)
				.hasFieldOrPropertyWithValue("id", dto.ID)
				.hasFieldOrPropertyWithValue("bezeichnung", dto.Bezeichnung)
				.hasFieldOrPropertyWithValue("istSichtbar", dto.Sichtbar)
				.hasFieldOrPropertyWithValue("sortierung", dto.Sortierung);
	}

	private static Stream<Arguments> provideMappingAttributes() {
		return Stream.of(
				arguments("id", 35),
				arguments("bezeichnung", "test"),
				arguments("istSichtbar", true),
				arguments("sortierung", 25),
				arguments("unknownArgument", "oh oh ! das wollen wir auf keinen Fall!")
		);
	}

	@ParameterizedTest
	@DisplayName("mapAttribute | Erfolg")
	@MethodSource("provideMappingAttributes")
	void mapAttributeTest(final String key, final Object value) {
		final var dto = getDto();

		final var throwable = ThrowableAssert.catchThrowable(() -> this.data.mapAttribute(dto, key, value, null));

		switch (key) {
			case "id" -> assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessage("Die ID 35 des Patches ist null oder stimmt nicht mit der ID 1 in der Datenbank überein.")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
			case "bezeichnung" -> assertThat(dto.Bezeichnung).isEqualTo(value);
			case "istSichtbar" -> assertThat(dto.Sichtbar).isEqualTo(value);
			case "sortierung" -> assertThat(dto.Sortierung).isEqualTo(value);
			default -> assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessageStartingWith("Die Daten des Patches enthalten das unbekannte Attribut")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
		}
	}

	@Test
	@DisplayName("mapAttribute | id is correct | nothing thrown")
	void mapAttributeTest_idIsCorrect() {
		final var dto = getDto();
		assertThatNoException().isThrownBy(() -> this.data.mapAttribute(dto, "id", 1L, null));
	}

	@Test
	@DisplayName("mapAttribute | bezeichnung bereits vorhanden")
	void mapAttributeTest_bezeichnungDoppeltVergeben() {
		final var existingBezeichnung = "persisted-bezeichnung";
		when(this.conn.existsBy(DTOSchuelerSchwerpunkt.QUERY_BY_BEZEICHNUNG, DTOSchuelerSchwerpunkt.class, existingBezeichnung)).thenReturn(true);

		final var throwable =
				ThrowableAssert
						.catchThrowable(() -> this.data.mapAttribute(new DTOSchuelerSchwerpunkt(2L, "new-bezeichnung"), "bezeichnung", existingBezeichnung,
						null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Die Bezeichnung " + existingBezeichnung + " wird bereits verwendet.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("checkBeforeDeletionWithSimpleOperationResponse | referenced")
	void checkBeforeDeletionWithSimpleOperationResponseUnreferenced() {
		final var response = new SimpleOperationResponse();
		response.id = 1L;
		response.success = true;
		final var responses = Map.of(response.id, response);
		final var bezeichnung = "bezeichnung";
		final long firstReferencedId = 1L;
		final var dto = new DTOSchuelerSchwerpunkt(firstReferencedId, bezeichnung);

		setupReferencedQueryMock(List.of(firstReferencedId));

		this.data.checkBeforeDeletionWithSimpleOperationResponse(List.of(dto), responses);

		assertThat(responses.get(1L))
				.hasFieldOrPropertyWithValue("success", false)
				.extracting(r -> r.log.getFirst())
				.isEqualTo("Schwerpunkt mit dem Namen " + bezeichnung + " ist in der Datenbank referenziert und kann daher nicht gelöscht werden.");
	}

	@Test
	@DisplayName("checkBeforeDeletionWithSimpleOperationResponseTest | not referenced")
	void checkBeforeDeletionWithSimpleOperationResponseNotReferenced() {
		final var response = new SimpleOperationResponse();
		response.id = 1L;
		response.success = true;
		final var responses = Map.of(response.id, response);
		final var bezeichnung = "bezeichnung";
		final var dto = new DTOSchuelerSchwerpunkt(1L, bezeichnung);

		setupReferencedQueryMock(List.of(2L));

		this.data.checkBeforeDeletionWithSimpleOperationResponse(List.of(dto), responses);

		assertThat(responses.get(1L))
				.hasFieldOrPropertyWithValue("success", true)
				.extracting(r -> r.log)
				.satisfies(log -> assertThat(log).isEmpty());
	}


	private static DTOSchuelerSchwerpunkt getDto() {
		return new DTOSchuelerSchwerpunkt(1L, "abc");
	}

	private void setupReferencedQueryMock(final List<Long> referencedIds) {
		@SuppressWarnings("unchecked") final TypedQuery<Long> queryMock = mock(TypedQuery.class);
		when(queryMock.setParameter(eq("referencedIds"), any())).thenReturn(queryMock);
		when(queryMock.getResultList()).thenReturn(referencedIds);
		when(conn.query(anyString(), eq(Long.class))).thenReturn(queryMock);
	}
}
