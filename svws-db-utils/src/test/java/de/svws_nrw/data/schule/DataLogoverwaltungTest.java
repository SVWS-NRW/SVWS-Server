package de.svws_nrw.data.schule;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.schule.Logo;
import de.svws_nrw.core.types.reporting.ReportingBildDefinition;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.db.dto.current.schild.schule.DTOLogo;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Diese Testklasse testet die Klasse DataLogoverwaltung")
@ExtendWith(MockitoExtension.class)
class DataLogoverwaltungTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private DataLogoverwaltung data;

	private static final String DATE_TODAY = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE);

	@BeforeAll
	static void setUp() {
		ASDCoreTypeUtils.initAll();
	}

	@Test
	@DisplayName("getAll | Erfolg")
	void getAll() {
		when(conn.querySingle(DTOEigeneSchule.class)).thenReturn(createEigeneSchule());
		final DTOLogo dto1 = createDTOLogo(1L, ReportingBildDefinition.SCHULLOGO_SCHILD);
		final DTOLogo dto2 = createDTOLogo(2L, ReportingBildDefinition.SCHULLOGO_QUADRATISCH);
		when(conn.queryAll(DTOLogo.class)).thenReturn(List.of(dto1, dto2));

		assertThat(data.getAll())
				.hasSize(2)
				.satisfiesExactly(
						l1 -> assertThat(l1)
								.isInstanceOf(Logo.class)
								.hasFieldOrPropertyWithValue("id", 1L)
								.hasFieldOrPropertyWithValue("kennung", "SCHULLOGO_SCHILD"),
						l2 -> assertThat(l2)
								.isInstanceOf(Logo.class)
								.hasFieldOrPropertyWithValue("id", 2L)
								.hasFieldOrPropertyWithValue("kennung", "SCHULLOGO_QUADRATISCH")
				);
	}

	@Test
	@DisplayName("getAll | Leere Datenbank")
	void getAllEmpty() {
		when(conn.querySingle(DTOEigeneSchule.class)).thenReturn(createEigeneSchule());
		when(conn.queryAll(DTOLogo.class)).thenReturn(Collections.emptyList());

		assertThat(data.getAll()).isEmpty();
	}

	@Test
	@DisplayName("getAll | DTOEigeneSchule ist null")
	void getAllEigeneSchuleIsNull() {
		when(conn.querySingle(DTOEigeneSchule.class)).thenReturn(null);

		assertThat(data.getAll()).isEmpty();
	}


	@Test
	@DisplayName("getAll | Alle DTOs werden korrekt gemappt")
	void getAllMapsAllDTOs() {
		when(conn.querySingle(DTOEigeneSchule.class)).thenReturn(createEigeneSchule());
		final DTOLogo dto = createDTOLogo(1L, ReportingBildDefinition.DIN5008_BRIEFKOPF);
		when(conn.queryAll(DTOLogo.class)).thenReturn(List.of(dto));

		assertThat(data.getAll())
				.singleElement()
				.hasFieldOrPropertyWithValue("id", 1L)
				.hasFieldOrPropertyWithValue("kennung", "DIN5008_BRIEFKOPF")
				.hasFieldOrPropertyWithValue("logoBase64", "base64data")
				.hasFieldOrPropertyWithValue("mimeType", "image/png");
	}

	@Test
	@DisplayName("map | Erfolg")
	void map() {
		final DTOLogo dto = createDTOLogo(42L, ReportingBildDefinition.DIN5008_BRIEFKOPF);
		dto.hinzugefuegtAm = "2026-01-01";

		final Logo logo = data.map(dto);

		assertThat(logo)
				.isInstanceOf(Logo.class)
				.hasFieldOrPropertyWithValue("id", 42L)
				.hasFieldOrPropertyWithValue("kennung", "DIN5008_BRIEFKOPF")
				.hasFieldOrPropertyWithValue("logoBase64", "base64data")
				.hasFieldOrPropertyWithValue("mimeType", "image/png")
				.hasFieldOrPropertyWithValue("hinzugefuegtAm", "2026-01-01")
				.hasFieldOrPropertyWithValue("breitePX", 800)
				.hasFieldOrPropertyWithValue("hoehePX", 600)
				.hasFieldOrPropertyWithValue("breiteMM", 45)
				.hasFieldOrPropertyWithValue("hoeheMM", 45);
	}

	@Test
	@DisplayName("map | bezeichnung und beschreibung kommen aus der ReportingBildDefinition")
	void mapBezeichnungAndBeschreibungFromDefinition() {
		final DTOLogo dto = createDTOLogo(1L, ReportingBildDefinition.SCHULLOGO_QUADRATISCH);

		final Logo logo = data.map(dto);

		assertThat(logo.bezeichnung).isEqualTo(ReportingBildDefinition.SCHULLOGO_QUADRATISCH.getBezeichnung());
		assertThat(logo.beschreibung).isEqualTo(ReportingBildDefinition.SCHULLOGO_QUADRATISCH.getBeschreibung());
	}

	@Test
	@DisplayName("add | Erfolg")
	void addSuccess() {
		final DTOLogo dto = createDTOLogo(1L, ReportingBildDefinition.SCHULLOGO_SCHILD);
		when(conn.transactionGetNextID(DTOLogo.class)).thenReturn(1L);
		when(conn.transactionPersist(any(DTOLogo.class))).thenReturn(true);
		when(conn.queryByKey(DTOLogo.class, 1L)).thenReturn(dto);

		final Map<String, Object> map = new HashMap<>();
		map.put("kennung", "SCHULLOGO_SCHILD");
		map.put("logoBase64", "base64data");
		map.put("mimeType", "image/png");
		map.put("hinzugefuegtAm", DATE_TODAY);

		assertThatNoException().isThrownBy(() -> data.add(map));
	}

	@Test
	@DisplayName("add | alle Attribute vorhanden")
	void addSuccessAllAttributes() {
		final DTOLogo dto = createDTOLogo(1L, ReportingBildDefinition.SCHULLOGO_SCHILD);
		when(conn.transactionGetNextID(DTOLogo.class)).thenReturn(1L);
		when(conn.transactionPersist(any(DTOLogo.class))).thenReturn(true);
		when(conn.queryByKey(DTOLogo.class, 1L)).thenReturn(dto);

		final Map<String, Object> map = new HashMap<>();
		map.put("kennung", "SCHULLOGO_SCHILD");
		map.put("logoBase64", "base64data");
		map.put("mimeType", "image/png");
		map.put("breitePX", 800);
		map.put("hoehePX", 600);
		map.put("breiteMM", 45);
		map.put("hoeheMM", 45);
		map.put("hinzugefuegtAm", DATE_TODAY);

		assertThatNoException().isThrownBy(() -> data.add(map));
	}

	@Test
	@DisplayName("add | kennung fehlt")
	void addKennungIsMissing() {
		final Map<String, Object> map = new HashMap<>();
		map.put("logoBase64", "base64data");
		map.put("mimeType", "image/png");
		map.put("hinzugefuegtAm", DATE_TODAY);

		assertThatException()
				.isThrownBy(() -> data.add(map))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Es werden weitere Attribute (kennung) benötigt, damit die Entität erstellt werden kann.")
				.hasFieldOrPropertyWithValue("status", Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("add | logoBase64 fehlt")
	void addLogoBase64IsMissing() {
		final Map<String, Object> map = new HashMap<>();
		map.put("kennung", "SCHULLOGO_SCHILD");
		map.put("mimeType", "image/png");
		map.put("hinzugefuegtAm", DATE_TODAY);

		assertThatException()
				.isThrownBy(() -> data.add(map))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Es werden weitere Attribute (logoBase64) benötigt, damit die Entität erstellt werden kann.")
				.hasFieldOrPropertyWithValue("status", Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("add | mimeType fehlt")
	void addMimeTypeIsMissing() {
		final Map<String, Object> map = new HashMap<>();
		map.put("kennung", "SCHULLOGO_SCHILD");
		map.put("logoBase64", "base64data");
		map.put("hinzugefuegtAm", DATE_TODAY);

		assertThatException()
				.isThrownBy(() -> data.add(map))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Es werden weitere Attribute (mimeType) benötigt, damit die Entität erstellt werden kann.")
				.hasFieldOrPropertyWithValue("status", Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("add | hinzugefuegtAm fehlt")
	void addHinzugefuegtAmIsMissing() {
		final Map<String, Object> map = new HashMap<>();
		map.put("kennung", "SCHULLOGO_SCHILD");
		map.put("logoBase64", "base64data");
		map.put("mimeType", "image/png");

		assertThatException()
				.isThrownBy(() -> data.add(map))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Es werden weitere Attribute (hinzugefuegtAm) benötigt, damit die Entität erstellt werden kann.")
				.hasFieldOrPropertyWithValue("status", Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("add | alle Pflichtattribute fehlen")
	void addAllRequiredAttributesMissing() {
		assertThatException()
				.isThrownBy(() -> data.add(new HashMap<>()))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Es werden weitere Attribute (kennung,hinzugefuegtAm,mimeType,logoBase64) benötigt, damit die Entität erstellt werden kann.")
				.hasFieldOrPropertyWithValue("status", Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("add | hinzugefuegtAm entspricht nicht heutigem Datum")
	void addWrongCreationDate() {
		final Map<String, Object> map = new HashMap<>();
		map.put("kennung", "SCHULLOGO_SCHILD");
		map.put("logoBase64", "base64data");
		map.put("mimeType", "image/png");
		map.put("hinzugefuegtAm", "2026-01-01");

		assertThatException()
				.isThrownBy(() -> data.add(map))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Neues Datum %s entspricht nicht dem heutigen Datum %s.".formatted("2026-01-01", DATE_TODAY))
				.hasFieldOrPropertyWithValue("status", Status.BAD_REQUEST);

	}

	@Test
	@DisplayName("add | hinzugefuegtAm entspricht nicht heutigem Datum")
	void addUnknownAttribute() {
		final Map<String, Object> map = new HashMap<>();
		map.put("kennung", "SCHULLOGO_SCHILD");
		map.put("logoBase64", "base64data");
		map.put("mimeType", "image/png");
		map.put("hinzugefuegtAm", DATE_TODAY);
		map.put("unknown", "unknown");

		assertThatException()
				.isThrownBy(() -> data.add(map))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die Daten des Patches enthalten das unbekannte Attribut unknown.")
				.hasFieldOrPropertyWithValue("status", Status.BAD_REQUEST);

	}

	@Test
	@DisplayName("getByID | ID can't be null")
	void getByIdNull() {
		assertThatException()
				.isThrownBy(() -> this.data.getById(null))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die ID des Logos darf nicht null sein.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("getByID | id not found")
	void getByIdNotFound() {
		when(this.conn.queryByKey(DTOLogo.class, 99L)).thenReturn(null);

		assertThatException()
				.isThrownBy(() -> this.data.getById(99L))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Es wurde kein Logo mit der ID 99 gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("patch | Attribut id nicht patchbar")
	void setAttributesNotPatchableId() {
		final DTOLogo dto = createDTOLogo(1L, ReportingBildDefinition.SCHULLOGO_SCHILD);
		when(conn.queryByKey(DTOLogo.class, 1L)).thenReturn(dto);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("id", 1L)))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Folgende Attribute werden für ein Patch nicht zugelassen: id.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | Attribut kennung nicht patchbar")
	void setAttributesNotPatchableKennung() {
		final DTOLogo dto = createDTOLogo(1L, ReportingBildDefinition.SCHULLOGO_SCHILD);
		when(conn.queryByKey(DTOLogo.class, 1L)).thenReturn(dto);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("kennung", "SCHULLOGO_SCHILD")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Folgende Attribute werden für ein Patch nicht zugelassen: kennung.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | ID nicht gefunden")
	void patchIdNotFound() {
		when(conn.queryByKey(DTOLogo.class, 99L)).thenReturn(null);

		assertThatException()
				.isThrownBy(() -> data.patch(99L, Map.of("logoBase64", "abc", "mimeType", "image/png")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die Entität für die angegebene ID wurden in der Datenbank nicht gefunden.")
				.hasFieldOrPropertyWithValue("status", Status.NOT_FOUND);
	}

	@Test
	@DisplayName("patch | logoBase64 ist leer")
	void patchLogoBase64IsEmpty() {
		when(conn.queryByKey(DTOLogo.class, 1L))
				.thenReturn(createDTOLogo(1L, ReportingBildDefinition.SCHULLOGO_SCHILD));

		assertThatException()
				.isThrownBy(() -> data.patch(1L, Map.of("logoBase64", "", "mimeType", "image/png")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut logoBase64: Ein leerer String ist hier nicht erlaubt.")
				.hasFieldOrPropertyWithValue("status", Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | logoBase64 ist null")
	void patchLogoBase64IsNull() {
		when(conn.queryByKey(DTOLogo.class, 1L))
				.thenReturn(createDTOLogo(1L, ReportingBildDefinition.SCHULLOGO_SCHILD));

		final Map<String, Object> map = new HashMap<>();
		map.put("logoBase64", null);
		map.put("mimeType", "image/png");

		assertThatException()
				.isThrownBy(() -> data.patch(1L, map))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | mimeType ist null")
	void patchMimeTypeIsNull() {
		when(conn.queryByKey(DTOLogo.class, 1L))
				.thenReturn(createDTOLogo(1L, ReportingBildDefinition.SCHULLOGO_SCHILD));

		final Map<String, Object> map = new HashMap<>();
		map.put("logoBase64", "newBase64");
		map.put("mimeType", null);

		assertThatException()
				.isThrownBy(() -> data.patch(1L, map))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut mimeType: Der Wert null ist nicht erlaubt.")
				.hasFieldOrPropertyWithValue("status", Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | mimeType ist leer")
	void patchMimeTypeIsEmpty() {
		when(conn.queryByKey(DTOLogo.class, 1L))
				.thenReturn(createDTOLogo(1L, ReportingBildDefinition.SCHULLOGO_SCHILD));

		assertThatException()
				.isThrownBy(() -> data.patch(1L, Map.of("logoBase64", "newBase64", "mimeType", "")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Attribut mimeType: Ein leerer String ist hier nicht erlaubt.")
				.hasFieldOrPropertyWithValue("status", Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | Erfolg – logoBase64 und mimeType werden aktualisiert")
	void patchSuccessful() {
		final DTOLogo dto = createDTOLogo(1L, ReportingBildDefinition.SCHULLOGO_SCHILD);
		when(conn.queryByKey(DTOLogo.class, 1L)).thenReturn(dto);
		when(conn.transactionPersist(any())).thenReturn(true);

		final Logo result = data.patch(1L, Map.of("logoBase64", "neuesLogo", "mimeType", "image/png"));

		assertThat(dto.logoBase64).isEqualTo("neuesLogo");
		assertThat(dto.mimeType).isEqualTo("image/png");
		assertThat(result)
				.hasFieldOrPropertyWithValue("logoBase64", "neuesLogo")
				.hasFieldOrPropertyWithValue("mimeType", "image/png");
	}

	@Test
	@DisplayName("patch | Erfolg – alle Attribute werden aktualisiert")
	void patchSuccessfulAllAttributes() {
		final DTOLogo dto = createDTOLogo(1L, ReportingBildDefinition.SCHULLOGO_SCHILD);
		when(conn.queryByKey(DTOLogo.class, 1L)).thenReturn(dto);
		when(conn.transactionPersist(any())).thenReturn(true);

		final Map<String, Object> map = new HashMap<>();
		map.put("logoBase64", "neuesLogo");
		map.put("mimeType", "image/png");
		map.put("breitePX", 800);
		map.put("hoehePX", 600);
		map.put("breiteMM", 45);
		map.put("hoeheMM", 45);
		map.put("hinzugefuegtAm", DATE_TODAY);

		final Logo result = data.patch(1L, map);

		assertThat(dto.logoBase64).isEqualTo("neuesLogo");
		assertThat(dto.mimeType).isEqualTo("image/png");
		assertThat(dto.breitePX).isEqualTo(800);
		assertThat(dto.hoehePX).isEqualTo(600);
		assertThat(dto.breiteMM).isEqualTo(45);
		assertThat(dto.hoeheMM).isEqualTo(45);
		assertThat(dto.hinzugefuegtAm).isEqualTo(DATE_TODAY);
		assertThat(result)
				.hasFieldOrPropertyWithValue("logoBase64", "neuesLogo")
				.hasFieldOrPropertyWithValue("mimeType", "image/png");
	}

	@Test
	@DisplayName("patch | Erfolg – transactionPersist wird aufgerufen")
	void patchCallsTransactionPersistOneTime() {
		final DTOLogo dto = createDTOLogo(1L, ReportingBildDefinition.SCHULLOGO_SCHILD);
		when(conn.queryByKey(DTOLogo.class, 1L)).thenReturn(dto);
		when(conn.transactionPersist(any())).thenReturn(true);

		data.patch(1L, Map.of("logoBase64", "neuesLogo", "mimeType", "image/jpeg"));

		verify(conn, times(1)).transactionPersist(dto);
	}

	@Test
	@DisplayName("patch | Erfolg – hinzugefuegtAm wird auf heutiges Datum gesetzt")
	void patchSetsHinzugefuegtAmToToday() {
		final DTOLogo dto = createDTOLogo(1L, ReportingBildDefinition.SCHULLOGO_SCHILD);
		dto.hinzugefuegtAm = "1900-01-01";
		when(conn.queryByKey(DTOLogo.class, 1L)).thenReturn(dto);
		when(conn.transactionPersist(any())).thenReturn(true);

		data.patch(1L, Map.of("logoBase64", "neuesLogo", "mimeType", "image/png", "hinzugefuegtAm", DATE_TODAY));

		assertThat(dto.hinzugefuegtAm)
				.isEqualTo(DATE_TODAY)
				.matches("\\d{4}-\\d{2}-\\d{2}");
	}

	@Test
	@DisplayName("deleteLogoAsResponse | ID nicht gefunden")
	void deleteLogoIdNotFound() {
		when(conn.queryByKey(DTOLogo.class, 99L)).thenReturn(null);

		assertThatException()
				.isThrownBy(() -> data.deleteAsResponse(99L))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Es wurde keine Entität mit der ID 99 gefunden.")
				.hasFieldOrPropertyWithValue("status", Status.NOT_FOUND);
	}


	@Test
	@DisplayName("deleteLogoAsResponse | Erfolg")
	void deleteLogoSuccessfully() {
		final DTOLogo dto = createDTOLogo(1L, ReportingBildDefinition.DIN5008_BRIEFKOPF);
		when(conn.queryByKey(DTOLogo.class, 1L)).thenReturn(dto);
		when(conn.transactionRemove(any())).thenReturn(true);

		try (Response response = data.deleteAsResponse(1L)) {
			assertThat(response.getStatus()).isEqualTo(Status.OK.getStatusCode());
		}
	}

	@Test
	@DisplayName("deleteLogoAsResponse | Erfolg – transactionRemove wird aufgerufen")
	void deleteLogoCallsTransactionRemovetOneTime() {
		final DTOLogo dto = createDTOLogo(1L, ReportingBildDefinition.SCHULLOGO_SCHILD);
		when(conn.queryByKey(DTOLogo.class, 1L)).thenReturn(dto);
		when(conn.transactionRemove(any())).thenReturn(true);

		try (Response response = data.deleteAsResponse(1L)) {
			assertThat(response).isNotNull();
		}

		verify(conn, times(1)).transactionRemove(dto);
	}

	private static DTOLogo createDTOLogo(final long id, final ReportingBildDefinition kennung) {
		final var dtoLogo = new DTOLogo(id, kennung, "base64data", "2026-01-01");
		dtoLogo.mimeType = "image/png";
		dtoLogo.breitePX = 800;
		dtoLogo.hoehePX = 600;
		dtoLogo.breiteMM = 45;
		dtoLogo.hoeheMM = 45;

		return dtoLogo;
	}

	private static DTOEigeneSchule createEigeneSchule() {
		final DTOEigeneSchule dto = mock(DTOEigeneSchule.class);
		dto.SchulformKuerzel = "GY";
		return dto;
	}

}
