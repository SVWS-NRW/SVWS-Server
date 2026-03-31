package de.svws_nrw.data.schule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.schule.Teilstandort;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schule.DTOTeilstandorte;
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
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/** Diese Klasse testet die Klasse {@link DataTeilstandorte}*/
@DisplayName("Diese Klasse testet die Klasse DataTeilstandorte.")
@ExtendWith(MockitoExtension.class)
class DataTeilstandorteTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private DataTeilstandorte data;

	@BeforeAll
	static void setUp() {
		ASDCoreTypeUtils.initAll();
	}

	@Test
	@DisplayName("setAttributesRequiredOnCreation: adrMerkmal")
	void setAttributesRequiredOnCreationTest() {
		assertThatException()
				.isThrownBy(() -> this.data.add(Map.of("test", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut adrMerkmal: Der Wert null ist nicht erlaubt.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("setAttributesNotPatchable: adrMerkmal")
	void setAttributesNotPatchableAdrMerkmalTest() {
		when(this.conn.queryByKey(DTOTeilstandorte.class, "A")).thenReturn(mock(DTOTeilstandorte.class));

		assertThatException()
				.isThrownBy(() -> this.data.patch("A", Map.of("adrMerkmal", "A")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Folgende Attribute werden für ein Patch nicht zugelassen: adrMerkmal.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("setAttributesNotPatchable: referenziertInAnderenTabellen")
	void setAttributesNotPatchableReferenziertInAnderenTabellen() {
		when(this.conn.queryByKey(DTOTeilstandorte.class, "A")).thenReturn(mock(DTOTeilstandorte.class));

		assertThatException()
				.isThrownBy(() -> this.data.patch("A", Map.of("referenziertInAnderenTabellen", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Folgende Attribute werden für ein Patch nicht zugelassen: referenziertInAnderenTabellen.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("initDTO | Erfolg")
	void initDTOTest() {
		final var dto = getDtoTeilstandorte();

		this.data.initDTO(dto, "B", null);

		assertThat(dto.AdrMerkmal).isEqualTo("B");
	}

	@Test
	@DisplayName("getById | Erfolg")
	void getByIdTest() throws ApiOperationException {
		final var dto = getDtoTeilstandorte();
		when(this.conn.queryByKey(DTOTeilstandorte.class, "A")).thenReturn(dto);

		assertThat(this.data.getById("A"))
				.isInstanceOf(Teilstandort.class)
				.hasFieldOrPropertyWithValue("adrMerkmal", dto.AdrMerkmal)
				.hasFieldOrPropertyWithValue("plz", dto.PLZ)
				.hasFieldOrPropertyWithValue("ort", dto.Ort)
				.hasFieldOrPropertyWithValue("strassenname", dto.Strassenname)
				.hasFieldOrPropertyWithValue("hausNr", dto.HausNr)
				.hasFieldOrPropertyWithValue("hausNrZusatz", dto.HausNrZusatz)
				.hasFieldOrPropertyWithValue("bemerkung", dto.Bemerkung)
				.hasFieldOrPropertyWithValue("kuerzel", dto.Kuerzel);
	}

	@Test
	@DisplayName("getByID | ID can't be null")
	void getByIdTest_IdNull() {
		final var throwable = ThrowableAssert.catchThrowable(() -> this.data.getById(null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Das Adressmerkmal darf nicht leer sein.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("getByID | id not found")
	void getByIdTest_IdNotFound() {
		final var throwable = ThrowableAssert.catchThrowable(() -> this.data.getById("B"));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Teilstandort mit Merkmal B wurde nicht gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("getAll | Erfolg")
	void getAllTest() {
		final var dto1 = new DTOTeilstandorte("A");
		final var dto2 = new DTOTeilstandorte("B");
		when(this.conn.queryAll(DTOTeilstandorte.class)).thenReturn(List.of(dto1, dto2));
		@SuppressWarnings("unchecked") final TypedQuery<String> queryMock = mock(TypedQuery.class);
		when(queryMock.setParameter(eq("adrMerkmale"), any())).thenReturn(queryMock);
		when(queryMock.getResultList()).thenReturn(List.of("A"));
		when(conn.query(anyString(), eq(String.class))).thenReturn(queryMock);

		assertThat(this.data.getAll())
				.hasSize(2)
				.satisfiesExactly(
						f1 -> assertThat(f1)
								.isInstanceOf(Teilstandort.class)
								.hasFieldOrPropertyWithValue("adrMerkmal", "A")
								.hasFieldOrPropertyWithValue("referenziertInAnderenTabellen", true),
						f2 -> assertThat(f2)
								.isInstanceOf(Teilstandort.class)
								.hasFieldOrPropertyWithValue("adrMerkmal", "B")
								.hasFieldOrPropertyWithValue("referenziertInAnderenTabellen", false)
				);
	}

	@Test
	@DisplayName("getAll | Database empty")
	void getAllTest_Empty() {
		assertThat(this.data.getAll()).isNotNull().isEmpty();
	}

	@Test
	@DisplayName("map | Erfolg")
	void mapTest() {
		final var dto = getDtoTeilstandorte();

		assertThat(this.data.map(dto))
				.isInstanceOf(Teilstandort.class)
				.hasFieldOrPropertyWithValue("adrMerkmal", dto.AdrMerkmal)
				.hasFieldOrPropertyWithValue("plz", dto.PLZ)
				.hasFieldOrPropertyWithValue("ort", dto.Ort)
				.hasFieldOrPropertyWithValue("strassenname", dto.Strassenname)
				.hasFieldOrPropertyWithValue("hausNr", dto.HausNr)
				.hasFieldOrPropertyWithValue("hausNrZusatz", dto.HausNrZusatz)
				.hasFieldOrPropertyWithValue("bemerkung", dto.Bemerkung)
				.hasFieldOrPropertyWithValue("kuerzel", dto.Kuerzel);
	}

	private static Stream<Arguments> provideMappingAttributes() {
		return Stream.of(
				arguments("adrMerkmal", "A"),
				arguments("plz", "15616"),
				arguments("ort", "Musterort"),
				arguments("strassenname", "Muster Str."),
				arguments("hausNr", "2"),
				arguments("hausNrZusatz", "b"),
				arguments("bemerkung", "Musterbemerkung"),
				arguments("kuerzel", "LM"),
				arguments("unknownArgument", "oh oh ! das wollen wir auf keinen Fall!")
		);
	}

	@ParameterizedTest
	@DisplayName("mapAttribute | Erfolg")
	@MethodSource("provideMappingAttributes")
	void mapAttributeTest(final String key, final Object value) {
		final var dto = getDtoTeilstandorte();

		final var throwable = ThrowableAssert.catchThrowable(() -> this.data.mapAttribute(dto, key, value, null));

		switch (key) {
			case "adrMerkmal" -> {
				assertThat(throwable).isNull();
				assertThat(dto.AdrMerkmal).isEqualTo(value);
			}
			case "plz" -> assertThat(dto.PLZ).isEqualTo(value);
			case "ort" -> assertThat(dto.Ort).isEqualTo(value);
			case "strassenname" -> assertThat(dto.Strassenname).isEqualTo(value);
			case "hausNr" -> assertThat(dto.HausNr).isEqualTo(value);
			case "hausNrZusatz" -> assertThat(dto.HausNrZusatz).isEqualTo(value);
			case "bemerkung" -> assertThat(dto.Bemerkung).isEqualTo(value);
			case "kuerzel" -> assertThat(dto.Kuerzel).isEqualTo(value);
			default -> assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessageStartingWith("Die Daten des Patches enthalten das unbekannte Attribut")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
		}
	}

	@Test
	@DisplayName("mapAttribute | adrMerkmal Null")
	void mapAttributeTest_adrMerkmalNull() {
		final var expectedDTO = getDtoTeilstandorte();

		final var throwable = ThrowableAssert.catchThrowable(() -> this.data.mapAttribute(expectedDTO, "adrMerkmal", null, null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Attribut adrMerkmal: Der Wert null ist nicht erlaubt.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | adrMerkmal Fehler: Merkmal existiert bereits (Conflict)")
	void mapAttributeTest_adrMerkmalConflict() {
		final String existierendesMerkmal = "A";
		final DTOTeilstandorte mockExistierend = new DTOTeilstandorte(existierendesMerkmal);

		when(this.conn.queryByKey(DTOTeilstandorte.class, existierendesMerkmal)).thenReturn(mockExistierend);

		final var dto = getDtoTeilstandorte();

		final var throwable = ThrowableAssert.catchThrowable(() -> this.data.mapAttribute(dto, "adrMerkmal", existierendesMerkmal, null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Ein Teilstandort mit dem AdrMerkmal %s existiert bereits.".formatted(existierendesMerkmal))
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | adrMerkmal is correct | nothing thrown")
	void mapAttributeTest_adrMerkmalIsCorrect() {
		final var dto = getDtoTeilstandorte();
		assertThatNoException().isThrownBy(() -> this.data.mapAttribute(dto, "adrMerkmal", "A", null));
	}

	@Test
	@DisplayName("deleteMultipleAsSimpleResponseList | IDs null")
	void deleteMultipleAsSimpleResponseList_IdsNull() {
		final var throwable = ThrowableAssert.catchThrowable(() -> this.data.deleteMultipleAsSimpleResponseList(null));

		assertThat(throwable)
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Für das Löschen müssen IDs angegeben werden. Null ist nicht zulässig.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("deleteMultipleAsSimpleResponseList | empty response list")
	void deleteMultiple_EmptyList() {
		final Response response = this.data.deleteMultipleAsSimpleResponseList(List.of());

		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		assertThat((List<?>) response.getEntity()).isEmpty();
	}

	@Test
	@DisplayName("deleteMultipleAsSimpleResponseList | Teilstandort deleted successfully")
	void deleteMultiple_Success() {
		final DTOTeilstandorte dto = getDtoTeilstandorte();
		mockNoReferencedIds();
		when(this.conn.queryByKey(DTOTeilstandorte.class, "A")).thenReturn(dto);
		when(this.conn.transactionRemove(dto)).thenReturn(true);

		final Response response = this.data.deleteMultipleAsSimpleResponseList(List.of("A"));

		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		final List<SimpleOperationResponse> responses = castToSimpleOperationResponseList(response);
		assertThat(responses).hasSize(1);
		assertThat(responses.getFirst().success).isTrue();
		assertThat(responses.getFirst().log).isEmpty();
	}

	@Test
	@DisplayName("deleteAdrMerkmal | error while deletion")
	void deleteMultiple_ExceptionDuringDelete() {
		final DTOTeilstandorte dto = getDtoTeilstandorte();
		mockNoReferencedIds();
		when(this.conn.queryByKey(DTOTeilstandorte.class, "A")).thenReturn(dto);
		when(this.conn.transactionRemove(dto)).thenThrow(new RuntimeException("DB-Fehler"));

		final Response response = this.data.deleteMultipleAsSimpleResponseList(List.of("A"));

		final List<SimpleOperationResponse> responses = castToSimpleOperationResponseList(response);
		assertThat(responses).hasSize(1);
		assertThat(responses.getFirst().success).isFalse();
		assertThat(responses.getFirst().log)
				.hasSize(1)
				.first().asString()
				.contains("Fehler beim Löschen")
				.contains("A")
				.contains("DB-Fehler");
	}

	@Test
	@DisplayName("deleteMultipleAsSimpleResponseList | mixed: one deleted, one referenced, one not found")
	void deleteMultiple_Mixed() {
		final DTOTeilstandorte dto = getDtoTeilstandorte();
		mockReferencedIds(Set.of("B"));
		when(this.conn.queryByKey(DTOTeilstandorte.class, "A")).thenReturn(dto);
		when(this.conn.transactionRemove(dto)).thenReturn(true);
		when(this.conn.queryByKey(DTOTeilstandorte.class, "C")).thenReturn(null);

		final Response response = this.data.deleteMultipleAsSimpleResponseList(List.of("A", "B", "C"));

		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		final List<SimpleOperationResponse> responses = castToSimpleOperationResponseList(response);
		assertThat(responses).hasSize(3);

		// "A" → Erfolg
		assertThat(responses.getFirst().success).isTrue();

		// "B" → referenziert
		assertThat(responses.get(1).success).isFalse();
		assertThat(responses.get(1).log.getFirst()).contains("kann nicht gelöscht werden").contains("B");

		// "C" → nicht gefunden
		assertThat(responses.get(2).success).isFalse();
		assertThat(responses.get(2).log.getFirst()).contains("nicht gefunden").contains("C");
	}

	private void mockNoReferencedIds() {
		mockReferencedIds(Set.of());
	}

	private void mockReferencedIds(final Set<String> referenced) {
		@SuppressWarnings("unchecked") final TypedQuery<String> queryMock = mock(TypedQuery.class);
		when(conn.query(anyString(), eq(String.class))).thenReturn(queryMock);
		when(queryMock.setParameter(eq("adrMerkmale"), any())).thenReturn(queryMock);
		when(queryMock.getResultList()).thenReturn(new ArrayList<>(referenced));
	}

	@SuppressWarnings("unchecked")
	private static List<SimpleOperationResponse> castToSimpleOperationResponseList(final Response response) {
		return (List<SimpleOperationResponse>) response.getEntity();
	}

	private static DTOTeilstandorte getDtoTeilstandorte() {
		final DTOTeilstandorte dto = new DTOTeilstandorte("A");
		dto.PLZ = "01551";
		dto.Ort = "Musterort";
		dto.Strassenname = "Muster Str.";
		dto.HausNr = "2";
		dto.HausNrZusatz = "b";
		dto.Bemerkung = "Musterbemerkung";
		dto.Kuerzel = "KL";
		return dto;
	}

}
