package de.svws_nrw.data.schueler.betriebe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.svws_nrw.asd.data.schueler.SchuelerBetrieb;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.berufskolleg.DTOBeschaeftigungsart;
import de.svws_nrw.db.dto.current.schild.katalog.DTOBetrieb;
import de.svws_nrw.db.dto.current.schild.katalog.DTOBetriebeAnsprechpartner;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerBetrieb;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Diese Klasse testet die Klasse DataSchuelerBetriebe")
@ExtendWith(MockitoExtension.class)
class DataSchuelerBetriebTest {

	@Mock
	private DBEntityManager conn;

	@InjectMocks
	private DataSchuelerBetriebe data;

	@BeforeAll
	static void setUp() {
		ASDCoreTypeUtils.initAll();
	}

	@BeforeEach
	void setUpEach() {
		lenient().when(this.conn.transactionPersist(any())).thenReturn(true);
	}

	@Test
	@DisplayName("setAttributesRequiredOnCreation | Betrieb")
	void setAttributesRequiredOnCreationBetrieb() {
		assertThatException()
				.isThrownBy(() -> this.data.add(Map.of("idSchueler", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Es werden weitere Attribute (idBetrieb) benötigt, damit die Entität erstellt werden kann.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("setAttributesRequiredOnCreation | Schueler")
	void setAttributesRequiredOnCreationSchueler() {
		assertThatException()
				.isThrownBy(() -> this.data.add(Map.of("idBetrieb", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Es werden weitere Attribute (idSchueler) benötigt, damit die Entität erstellt werden kann.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("setAttributesNotPatchable: id")
	void setAttributesNotPatchableId() {
		when(this.conn.queryByKey(DTOSchuelerBetrieb.class, 1L)).thenReturn(mock(DTOSchuelerBetrieb.class));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("id", "test")))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Folgende Attribute werden für ein Patch nicht zugelassen: id.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("initDTO | Erfolg")
	void initDTO() {
		final var dto = new DTOSchuelerBetrieb(1L, 1L, 1L);

		this.data.initDTO(dto, 2L, null);

		assertThat(dto).hasFieldOrPropertyWithValue("id", 2L);
	}

	@Test
	@DisplayName("getLongId | Erfolg")
	void getLongId() {
		final var dto = new DTOSchuelerBetrieb(1L, 1L, 1L);

		assertThat(this.data.getLongId(dto)).isEqualTo(1L);
	}

	@Test
	@DisplayName("getById | Erfolg")
	void getById() throws ApiOperationException {
		final var dto = new DTOSchuelerBetrieb(1L, 1L, 1L);
		when(this.conn.queryByKey(DTOSchuelerBetrieb.class, 1L)).thenReturn(dto);

		assertThat(this.data.getById(1L))
				.isInstanceOf(SchuelerBetrieb.class)
				.hasFieldOrPropertyWithValue("id", dto.id);
	}

	@Test
	@DisplayName("getByID | ID can't be null")
	void getByIdNull() {
		assertThatException()
				.isThrownBy(() -> this.data.getById(null))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Die Id darf nicht null sein.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("getByID | id not found")
	void getByIdNotFound() {
		when(this.conn.queryByKey(DTOSchuelerBetrieb.class, 99L)).thenReturn(null);

		assertThatException()
				.isThrownBy(() -> this.data.getById(99L))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Keine SchuelerBetriebsdaten mit der ID 99 gefunden")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("getAll")
	void getAll() {
		final var dto1 = new DTOSchuelerBetrieb(1L, 5L, 21L);
		final var dto2 = new DTOSchuelerBetrieb(2L, 5L, 22L);

		when(this.conn.queryList(DTOSchuelerBetrieb.QUERY_BY_IDSCHUELER, DTOSchuelerBetrieb.class, 5L))
				.thenReturn(List.of(dto1, dto2));

		assertThat(this.data.getAllAsResponseByIdSchueler(5L))
				.isInstanceOf(Response.class)
				.extracting(Response::getEntity)
				.asInstanceOf(InstanceOfAssertFactories.LIST)
				.hasSize(2)
				.satisfiesExactly(b1 -> assertThat(b1)
								.isInstanceOf(SchuelerBetrieb.class)
								.hasFieldOrPropertyWithValue("id", 1L)
								.hasFieldOrPropertyWithValue("idBetrieb", 21L),
						b2 -> assertThat(b2)
								.hasFieldOrPropertyWithValue("id", 2L)
								.hasFieldOrPropertyWithValue("idBetrieb", 22L));
	}

	@Test
	@DisplayName("map")
	void map() {
		final var dto = new DTOSchuelerBetrieb(1L, 2L, 3L);
		dto.idAnsprechpartner = 4L;
		dto.idBetreuungslehrer = 5L;
		dto.idBeschaeftigungsart = 6L;
		dto.nameAusbilder = "mueller";
		dto.vertragsbeginn = "12-12-2020";
		dto.vertragende = "01-01-2025";
		dto.erhaeltAnschreiben = true;
		dto.istPraktikum = true;
		dto.sortierung = 12345;

		assertThat(this.data.map(dto))
				.isInstanceOf(SchuelerBetrieb.class)
				.hasFieldOrPropertyWithValue("id", 1L)
				.hasFieldOrPropertyWithValue("idSchueler", 2L)
				.hasFieldOrPropertyWithValue("idBetrieb", 3L)
				.hasFieldOrPropertyWithValue("idAnsprechpartner", 4L)
				.hasFieldOrPropertyWithValue("idBetreuungslehrer", 5L)
				.hasFieldOrPropertyWithValue("idBeschaeftigungsart", 6L)
				.hasFieldOrPropertyWithValue("nameAusbilder", "mueller")
				.hasFieldOrPropertyWithValue("vertragsbeginn", "12-12-2020")
				.hasFieldOrPropertyWithValue("vertragsende", "01-01-2025")
				.hasFieldOrPropertyWithValue("erhaeltAnschreiben", true)
				.hasFieldOrPropertyWithValue("istPraktikum", true)
				.hasFieldOrPropertyWithValue("sortierung", 12345);
	}

	@Test
	@DisplayName("patch | idSchueler | null")
	void patchIdSchuelerNull() {
		when(this.conn.queryByKey(DTOSchuelerBetrieb.class, 1L)).thenReturn(mock(DTOSchuelerBetrieb.class));
		final var map = new HashMap<String, Object>();
		map.put("idSchueler", null);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, map))
				.withMessage("Attribut idSchueler: Der Wert null ist nicht erlaubt");
	}

	@Test
	@DisplayName("patch | idSchueler | not found")
	void patchIdSchuelerNotFound() {
		when(this.conn.queryByKey(DTOSchuelerBetrieb.class, 1L)).thenReturn(mock(DTOSchuelerBetrieb.class));
		when(this.conn.queryByKey(DTOSchueler.class, 22L)).thenReturn(null);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("idSchueler", 22L)))
				.withMessage("Kein Schüler mit der id 22 gefunden.");
	}

	@Test
	@DisplayName("patch | idSchueler")
	void patchIdSchueler() {
		final var dto = new DTOSchuelerBetrieb(1L, 1L, 1L);
		when(this.conn.queryByKey(DTOSchuelerBetrieb.class, 1L)).thenReturn(dto);
		when(this.conn.queryByKey(DTOSchueler.class, 2L)).thenReturn(new DTOSchueler(2L, "123", true));

		this.data.patch(1L, Map.of("idSchueler", 2L));

		assertThat(dto.idSchueler).isEqualTo(2L);
	}

	@Test
	@DisplayName("patch | idBetrieb | null")
	void patchIdBetriebNull() {
		when(this.conn.queryByKey(DTOSchuelerBetrieb.class, 1L)).thenReturn(mock(DTOSchuelerBetrieb.class));
		final var map = new HashMap<String, Object>();
		map.put("idBetrieb", null);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, map))
				.withMessage("Attribut idBetrieb: Der Wert null ist nicht erlaubt");
	}

	@Test
	@DisplayName("patch | idBetrieb | not found")
	void patchIdBetriebNotFound() {
		when(this.conn.queryByKey(DTOSchuelerBetrieb.class, 1L)).thenReturn(mock(DTOSchuelerBetrieb.class));
		when(this.conn.queryByKey(DTOBetrieb.class, 22L)).thenReturn(null);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("idBetrieb", 22L)))
				.withMessage("Kein Betrieb mit der id 22 gefunden.");
	}

	@Test
	@DisplayName("patch | idBetrieb")
	void patchIdBetrieb() {
		final var dto = new DTOSchuelerBetrieb(1L, 1L, 1L);
		when(this.conn.queryByKey(DTOSchuelerBetrieb.class, 1L)).thenReturn(dto);
		when(this.conn.queryByKey(DTOBetrieb.class, 2L)).thenReturn(new DTOBetrieb(2L));

		this.data.patch(1L, Map.of("idBetrieb", 2L));

		assertThat(dto.idBetrieb).isEqualTo(2L);
	}

	@Test
	@DisplayName("patch | idAnsprechpartner")
	void patchIdAnsprchpartner() {
		final var dto = new DTOSchuelerBetrieb(1L, 1L, 1L);
		dto.idAnsprechpartner = 99L;
		when(this.conn.queryByKey(DTOSchuelerBetrieb.class, 1L)).thenReturn(dto);
		when(this.conn.queryByKey(DTOBetriebeAnsprechpartner.class, 22L)).thenReturn(mock(DTOBetriebeAnsprechpartner.class));

		this.data.patch(1L, Map.of("idAnsprechpartner", 22L));

		assertThat(dto.idAnsprechpartner).isEqualTo(22L);
	}

	@Test
	@DisplayName("patch | idAnsprechpartner | not found")
	void patchIdAnsprechpartnerNotFound() {
		when(this.conn.queryByKey(DTOSchuelerBetrieb.class, 1L)).thenReturn(mock(DTOSchuelerBetrieb.class));
		when(this.conn.queryByKey(DTOBetriebeAnsprechpartner.class, 22L)).thenReturn(null);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("idAnsprechpartner", 22L)))
				.withMessage("Kein Ansprechpartner mit der id 22 gefunden.");
	}

	@Test
	@DisplayName("patch | idBetreuungslehrer")
	void patchIdBetreuungslehrer() {
		final var dto = new DTOSchuelerBetrieb(1L, 1L, 1L);
		dto.idBetreuungslehrer = 99L;
		when(this.conn.queryByKey(DTOSchuelerBetrieb.class, 1L)).thenReturn(dto);
		when(this.conn.queryByKey(DTOLehrer.class, 22L)).thenReturn(mock(DTOLehrer.class));

		this.data.patch(1L, Map.of("idBetreuungslehrer", 22L));

		assertThat(dto.idBetreuungslehrer).isEqualTo(22L);
	}

	@Test
	@DisplayName("patch | idBetreuungslehrer | not found")
	void patchIdBetreuungslehrerNotFound() {
		when(this.conn.queryByKey(DTOSchuelerBetrieb.class, 1L)).thenReturn(mock(DTOSchuelerBetrieb.class));
		when(this.conn.queryByKey(DTOLehrer.class, 22L)).thenReturn(null);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("idBetreuungslehrer", 22L)))
				.withMessage("Kein Lehrer mit der id 22 gefunden.");
	}

	@Test
	@DisplayName("patch | idBeschaeftigungsart")
	void patchIdBeschaeftigungsart() {
		final var dto = new DTOSchuelerBetrieb(1L, 1L, 1L);
		dto.idBeschaeftigungsart = 99L;
		when(this.conn.queryByKey(DTOSchuelerBetrieb.class, 1L)).thenReturn(dto);
		when(this.conn.queryByKey(DTOBeschaeftigungsart.class, 22L)).thenReturn(mock(DTOBeschaeftigungsart.class));

		this.data.patch(1L, Map.of("idBeschaeftigungsart", 22L));

		assertThat(dto.idBeschaeftigungsart).isEqualTo(22L);
	}

	@Test
	@DisplayName("patch | idBeschaeftigungsart | not found")
	void patchIdBeschaeftigungsartNotFound() {
		when(this.conn.queryByKey(DTOSchuelerBetrieb.class, 1L)).thenReturn(mock(DTOSchuelerBetrieb.class));
		when(this.conn.queryByKey(DTOBeschaeftigungsart.class, 22L)).thenReturn(null);

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("idBeschaeftigungsart", 22L)))
				.withMessage("Keine Beschäftigungsart mit der id 22 gefunden.");
	}

	@Test
	@DisplayName("patch | vertragsbeginn")
	void patchVertragsbeginn() {
		final var dto = new DTOSchuelerBetrieb(1L, 1L, 1L);
		dto.vertragsbeginn = "2022-22-05";
		when(this.conn.queryByKey(DTOSchuelerBetrieb.class, 1L)).thenReturn(dto);

		this.data.patch(1L, Map.of("vertragsbeginn", "2010-10-10"));

		assertThat(dto.vertragsbeginn).isEqualTo("2010-10-10");
	}

	@Test
	@DisplayName("patch | vertragsbeginn | invalid")
	void patchVertragsbeginnInvalid() {
		when(this.conn.queryByKey(DTOSchuelerBetrieb.class, 1L)).thenReturn(mock(DTOSchuelerBetrieb.class));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("vertragsbeginn", "123")))
				.withMessage("Das Datumsformat für 123 ist ungültig");
	}

	@Test
	@DisplayName("patch | nameAusbilder")
	void patchNameAusbilder() {
		final var dto = new DTOSchuelerBetrieb(1L, 1L, 1L);
		dto.nameAusbilder = "mueller";
		when(this.conn.queryByKey(DTOSchuelerBetrieb.class, 1L)).thenReturn(dto);

		this.data.patch(1L, Map.of("nameAusbilder", "keks"));

		assertThat(dto.nameAusbilder).isEqualTo("keks");
	}

	@Test
	@DisplayName("patch | vertragsende")
	void patchVertragsende() {
		final var dto = new DTOSchuelerBetrieb(1L, 1L, 1L);
		dto.vertragende = "2022-22-05";
		when(this.conn.queryByKey(DTOSchuelerBetrieb.class, 1L)).thenReturn(dto);

		this.data.patch(1L, Map.of("vertragsende", "2010-10-10"));

		assertThat(dto.vertragende).isEqualTo("2010-10-10");
	}

	@Test
	@DisplayName("patch | vertragsende | invalid")
	void patchVertragsendeInvalid() {
		when(this.conn.queryByKey(DTOSchuelerBetrieb.class, 1L)).thenReturn(mock(DTOSchuelerBetrieb.class));

		assertThatException()
				.isThrownBy(() -> this.data.patch(1L, Map.of("vertragsende", "123")))
				.withMessage("Das Datumsformat für 123 ist ungültig");
	}

	@Test
	@DisplayName("patch | erhaeltAnschreiben")
	void patchErhaeltAnschreiben() {
		final var dto = new DTOSchuelerBetrieb(1L, 1L, 1L);
		dto.erhaeltAnschreiben = false;
		when(this.conn.queryByKey(DTOSchuelerBetrieb.class, 1L)).thenReturn(dto);

		this.data.patch(1L, Map.of("erhaeltAnschreiben", true));

		assertThat(dto.erhaeltAnschreiben).isTrue();
	}

	@Test
	@DisplayName("patch | istPraktikum")
	void patchIstPraktikum() {
		final var dto = new DTOSchuelerBetrieb(1L, 1L, 1L);
		dto.istPraktikum = false;
		when(this.conn.queryByKey(DTOSchuelerBetrieb.class, 1L)).thenReturn(dto);

		this.data.patch(1L, Map.of("istPraktikum", true));

		assertThat(dto.istPraktikum).isTrue();
	}

	@Test
	@DisplayName("patch | sortierung")
	void patchSortierung() {
		final var dto = new DTOSchuelerBetrieb(1L, 1L, 1L);
		dto.sortierung = 123;
		when(this.conn.queryByKey(DTOSchuelerBetrieb.class, 1L)).thenReturn(dto);

		this.data.patch(1L, Map.of("sortierung", 345));

		assertThat(dto.sortierung).isEqualTo(345);
	}
}
