package de.svws_nrw.data.schule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.schule.BetriebeAnsprechpartner;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOBetriebeAnsprechpartner;
import de.svws_nrw.db.dto.current.schild.katalog.DTOBetrieb;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.persistence.TypedQuery;
import jakarta.ws.rs.core.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Diese Klasse testet die Klasse DataBetriebeAnsprechpartner")
@ExtendWith(MockitoExtension.class)
class DataBetriebeAnsprechpartnerTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private DataBetriebeAnsprechpartner data;

	@BeforeAll
	static void setUp() {
		ASDCoreTypeUtils.initAll();
	}

	@BeforeEach
	void setUpEach() {
		lenient().when(this.conn.transactionPersist(any())).thenReturn(true);
	}

	@Test
	@DisplayName("setAttributesRequiredOnCreation: name")
	void setAttributesRequiredOnCreationName() {
		assertThatException()
				.isThrownBy(() -> this.data.add(Map.of("idBetrieb", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Es werden weitere Attribute (name) benötigt, damit die Entität erstellt werden kann.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("setAttributesRequiredOnCreation: idBetrieb")
	void setAttributesRequiredOnCreationIdBetrieb() {
		assertThatException()
				.isThrownBy(() -> this.data.add(Map.of("name", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Es werden weitere Attribute (idBetrieb) benötigt, damit die Entität erstellt werden kann.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("setAttributesNotPatchable: id")
	void setAttributesNotPatchableId() {
		when(this.conn.queryByKey(DTOBetriebeAnsprechpartner.class, 1L)).thenReturn(mock(DTOBetriebeAnsprechpartner.class));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("id", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Folgende Attribute werden für ein Patch nicht zugelassen: id.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("initDTO | Erfolg")
	void initDTO() {
		final var dto = new DTOBetriebeAnsprechpartner(1L, 1L);

		this.data.initDTO(dto, 2L, null);

		assertThat(dto).hasFieldOrPropertyWithValue("ID", 2L);
	}

	@Test
	@DisplayName("getLongId | Erfolg")
	void getLongId() {
		final var dto = new DTOBetriebeAnsprechpartner(1L, 1L);

		assertThat(this.data.getLongId(dto)).isEqualTo(1L);
	}

	@Test
	@DisplayName("getById | Erfolg")
	void getById() throws ApiOperationException {
		final var dto = new DTOBetriebeAnsprechpartner(1L, 1L);
		when(this.conn.queryByKey(DTOBetriebeAnsprechpartner.class, 1L)).thenReturn(dto);

		assertThat(this.data.getById(1L))
				.isInstanceOf(BetriebeAnsprechpartner.class)
				.hasFieldOrPropertyWithValue("id", dto.ID);
	}

	@Test
	@DisplayName("getByID | ID can't be null")
	void getByIdNull() {
		assertThatException()
				.isThrownBy(() -> this.data.getById(null))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die ID des Ansprechpartners darf nicht null sein.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("getByID | id not found")
	void getByIdNotFound() {
		when(this.conn.queryByKey(DTOBetriebeAnsprechpartner.class, 99L)).thenReturn(null);

		assertThatException()
				.isThrownBy(() -> this.data.getById(99L))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Es wurde kein Ansprechpartner mit der ID 99 gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("getAll | Erfolg")
	void getAll() {
		final var dto1 = new DTOBetriebeAnsprechpartner(1L, 1L);
		final var dto2 = new DTOBetriebeAnsprechpartner(2L, 2L);
		when(this.conn.queryAll(DTOBetriebeAnsprechpartner.class)).thenReturn(List.of(dto1, dto2));
		@SuppressWarnings("unchecked")
		final TypedQuery<Long> queryMock = mock(TypedQuery.class);
		when(queryMock.setParameter(eq("ids"), any())).thenReturn(queryMock);
		when(queryMock.getResultList()).thenReturn(List.of(1L));
		when(conn.query(anyString(), eq(Long.class))).thenReturn(queryMock);

		assertThat(this.data.getAll())
				.hasSize(2)
				.satisfiesExactly(
						f1 -> assertThat(f1)
								.isInstanceOf(BetriebeAnsprechpartner.class)
								.hasFieldOrPropertyWithValue("referenziertInAnderenTabellen", true),
						f2 -> assertThat(f2)
								.isInstanceOf(BetriebeAnsprechpartner.class)
								.hasFieldOrPropertyWithValue("referenziertInAnderenTabellen", false)
				);
	}

	@Test
	@DisplayName("getAll | Database empty")
	void getAllEmpty() {
		when(this.conn.queryAll(DTOBetriebeAnsprechpartner.class)).thenReturn(emptyList());

		assertThat(this.data.getAll()).isEmpty();
	}

	@Test
	@DisplayName("map | Erfolg")
	void map() {
		final var dto = new DTOBetriebeAnsprechpartner(1L, 2L);
		dto.Anrede = "test";
		dto.Name = "test";
		dto.Vorname = "test";
		dto.Telefon = "test";
		dto.Email = "test";
		dto.Adresse_ID = 42L;

		assertThat(this.data.map(dto))
				.isInstanceOf(BetriebeAnsprechpartner.class)
				.hasFieldOrPropertyWithValue("id", dto.ID)
				.hasFieldOrPropertyWithValue("anrede", dto.Anrede)
				.hasFieldOrPropertyWithValue("name", dto.Name)
				.hasFieldOrPropertyWithValue("rufname", dto.Vorname)
				.hasFieldOrPropertyWithValue("telefon", dto.Telefon)
				.hasFieldOrPropertyWithValue("idBetrieb", dto.Adresse_ID)
				.hasFieldOrPropertyWithValue("eMail", dto.Email);
	}

	@Test
	@DisplayName("mapAttribute | idWrong")
	void mapAttributeIdIsWrong() {
		assertThatException()
				.isThrownBy(() -> this.data.mapAttribute(mock(DTOBetriebeAnsprechpartner.class), "id", 2L, null))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die ID 2 des Patches ist null oder stimmt nicht mit der ID 0 in der Datenbank überein.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("mapAttribute | id")
	void mapAttributeId() {
		final var dto  = new DTOBetriebeAnsprechpartner(1L, 2L);

		assertDoesNotThrow(() -> this.data.mapAttribute(dto, "id", 1L, null));
	}

	private static Stream<Arguments> patchStringAttributesTooManyCharacters() {
		return Stream.of(
				arguments("anrede", "Attribut anrede: Die Länge des Strings ist auf 10 Zeichen limitiert."),
				arguments("name", "Attribut name: Die Länge des Strings ist auf 120 Zeichen limitiert."),
				arguments("rufname", "Attribut rufname: Die Länge des Strings ist auf 80 Zeichen limitiert."),
				arguments("telefon", "Attribut telefon: Die Länge des Strings ist auf 20 Zeichen limitiert."),
				arguments("eMail", "Attribut eMail: Die Länge des Strings ist auf 100 Zeichen limitiert.")
		);
	}

	@ParameterizedTest
	@MethodSource("patchStringAttributesTooManyCharacters")
	@DisplayName("patch | String Attributes Too Long")
	void patchAttributeTooLong(final String attributeName, final String expectedMessage) {
		when(this.conn.queryByKey(DTOBetriebeAnsprechpartner.class, 1L)).thenReturn(mock(DTOBetriebeAnsprechpartner.class));

		assertThatException()
				.isThrownBy(() -> data.patch(1L, Map.of(attributeName, RandomStringUtils.insecure().nextAscii(121))))
				.isInstanceOf(ApiOperationException.class)
				.withMessage(expectedMessage)
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	private static Stream<Arguments> patchStringAttributes() {
		return Stream.of(
				arguments("anrede"),
				arguments("name"),
				arguments("rufname"),
				arguments("telefon"),
				arguments("eMail"),
				arguments("unknown")
		);
	}

	@ParameterizedTest
	@MethodSource("patchStringAttributes")
	@DisplayName("patch String attributes")
	void patchStringAttributes(final String attributeName) {
		final var dto = new DTOBetriebeAnsprechpartner(1L, 1L);
		when(this.conn.queryByKey(DTOBetriebeAnsprechpartner.class, 1L)).thenReturn(dto);
		lenient().when(this.conn.transactionPersist(any())).thenReturn(true);
		final var newValue = "newValue";

		final var throwable = catchThrowable(() -> this.data.patch(1L, Map.of(attributeName, newValue)));

		switch (attributeName) {
			case "anrede" -> assertThat(dto.Anrede).isEqualTo(newValue);
			case "name" -> assertThat(dto.Name).isEqualTo(newValue);
			case "rufname" -> assertThat(dto.Vorname).isEqualTo(newValue);
			case "telefon" -> assertThat(dto.Telefon).isEqualTo(newValue);
			case "eMail" -> assertThat(dto.Email).isEqualTo(newValue);
			default -> assertThat(throwable)
					.isInstanceOf(ApiOperationException.class)
					.hasMessage("Die Daten des Patches enthalten das unbekannte Attribut unknown.")
					.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
		}
	}

	@Test
	@DisplayName("patch | idBetrieb | null")
	void patchIdBetriebIsNull() {
		when(this.conn.queryByKey(DTOBetriebeAnsprechpartner.class, 1L)).thenReturn(mock(DTOBetriebeAnsprechpartner.class));
		final var map =  new HashMap<String, Object>();
		map.put("idBetrieb", null);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, map))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut idBetrieb: Der Wert null ist nicht erlaubt")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);

	}

	@Test
	@DisplayName("patch | idBetrieb | no changes")
	void patchIdBetriebNoChanges() throws ApiOperationException {
		final var dto = new DTOBetriebeAnsprechpartner(1L, 1L);
		dto.Adresse_ID = 42L;
		when(this.conn.queryByKey(DTOBetriebeAnsprechpartner.class, 1L)).thenReturn(dto);

		this.data.patch(1L, Map.of("idBetrieb", 42L));

		verify(this.conn, never()).queryByKey(DTOBetrieb.class, 42L);
		assertThat(dto.Adresse_ID).isEqualTo(42L);
	}

	@Test
	@DisplayName("patch | idBetrieb | wrong id")
	void patchIdBetriebWrongId() {
		when(this.conn.queryByKey(DTOBetriebeAnsprechpartner.class, 1L)).thenReturn(mock(DTOBetriebeAnsprechpartner.class));
		when(this.conn.queryByKey(DTOBetrieb.class, 42L)).thenReturn(null);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("idBetrieb", 42L)))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Kein Betrieb zur ID 42 gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | idBetrieb")
	void patchIdBetrieb() throws ApiOperationException {
		final var dto = new DTOBetriebeAnsprechpartner(1L, 1L);
		dto.Adresse_ID = 42L;
		when(this.conn.queryByKey(DTOBetriebeAnsprechpartner.class, 1L)).thenReturn(dto);
		when(this.conn.queryByKey(DTOBetrieb.class, 12L)).thenReturn(mock(DTOBetrieb.class));

		this.data.patch(1L, Map.of("idBetrieb", 12L));

		assertThat(dto.Adresse_ID).isEqualTo(12L);
	}

	@Test
	@DisplayName("checkBeforeDeletionWithSimpleOperationResponse")
	void checkBeforeDeletionWithSimpleOperationResponse() {
		@SuppressWarnings("unchecked")
		final TypedQuery<Long> queryMock = mock(TypedQuery.class);
		when(queryMock.setParameter(eq("ids"), any())).thenReturn(queryMock);
		when(queryMock.getResultList()).thenReturn(List.of(1L));
		when(conn.query(anyString(), eq(Long.class))).thenReturn(queryMock);
		final var response = new SimpleOperationResponse();
		response.id = 1L;
		response.success = true;
		final var responses = Map.of(response.id, response);
		final var dto = new DTOBetriebeAnsprechpartner(1L, 1L);
		dto.Name = "abc";

		this.data.checkBeforeDeletionWithSimpleOperationResponse(List.of(dto), responses);

		assertThat(responses.get(1L))
				.hasFieldOrPropertyWithValue("success", false)
				.extracting(r -> r.log.getFirst())
				.isEqualTo("Der Ansprechpartner mit dem Name abc ist in der Datenbank referenziert und kann daher nicht gelöscht werden.");
	}

	@Test
	@DisplayName("checkBeforeDeletionWithSimpleOperationResponseTest | foerderschwerpunkt not referenced")
	void checkBeforeDeletionWithSimpleOperationResponseFoerderschwerpunktNotReferenced() {
		@SuppressWarnings("unchecked")
		final TypedQuery<Long> queryMock = mock(TypedQuery.class);
		when(queryMock.setParameter(eq("ids"), any())).thenReturn(queryMock);
		when(queryMock.getResultList()).thenReturn(List.of(2L));
		when(conn.query(anyString(), eq(Long.class))).thenReturn(queryMock);
		final var response = new SimpleOperationResponse();
		response.id = 1L;
		response.success = true;
		final var responses = Map.of(response.id, response);
		final var dto = new DTOBetriebeAnsprechpartner(1L, 1L);

		this.data.checkBeforeDeletionWithSimpleOperationResponse(List.of(dto), responses);

		assertThat(response)
				.hasFieldOrPropertyWithValue("success", true)
				.satisfies(r -> assertThat(r.log).isEmpty());
	}

}
