package de.svws_nrw.service.schueler.schulbesuch;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import de.svws_nrw.asd.data.schueler.SchuelerSchulbesuchMerkmal;
import de.svws_nrw.asd.data.schueler.SchuelerSchulbesuchSchule;
import de.svws_nrw.asd.data.schueler.SchuelerSchulbesuchsdaten;
import de.svws_nrw.asd.types.jahrgang.PrimarstufeSchuleingangsphaseBesuchsjahre;
import de.svws_nrw.asd.types.schueler.Einschulungsart;
import de.svws_nrw.asd.types.schueler.HerkunftSchulform;
import de.svws_nrw.asd.types.schueler.Uebergangsempfehlung;
import de.svws_nrw.asd.types.schule.Kindergartenbesuch;
import de.svws_nrw.asd.types.schule.SchulabschlussAllgemeinbildend;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.kataloge.SchulEintrag;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.data.kataloge.DataKatalogEntlassgruende;
import de.svws_nrw.data.schule.DataSchulen;
import de.svws_nrw.db.dto.current.schild.katalog.DTOSchuleNRW;
import de.svws_nrw.db.dto.current.schild.schueler.DTOEntlassarten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.schueler.schulbesuch.SchulbesuchMapper;
import de.svws_nrw.mapper.schueler.schulbesuch.SchulbesuchMappingContext;
import de.svws_nrw.repo.schueler.SchuelerRepository;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.jackson.nullable.JsonNullable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SchuelerSchulbesuchServiceTest {

	@Mock
	private SchuelerRepository schuelerRepository;

	@Mock
	private SchuelerMerkmalService schuelerMerkmalService;

	@Mock
	private SchuelerBisherigeSchuleService schuelerBisherigeSchuleService;

	@Mock
	private DataKatalogEntlassgruende dataKatalogEntlassgruende;

	@Mock
	private DataSchulen dataSchulen;

	@Mock
	private SchulbesuchMapper schulbesuchMapper;

	@Mock
	private MockedStatic<TransactionSupport> transactionSupport;

	@InjectMocks
	private SchuelerSchulbesuchService schuelerSchulbesuchService;

	@Captor
	private ArgumentCaptor<DTOSchueler> schuelerCaptor;

	private long idSchueler;
	private DTOSchueler schueler;
	private DTOSchuleNRW schule;
	private DTOEntlassarten entlassart;
	private SchuelerSchulbesuchsdaten apiModel;

	@BeforeEach
	void setUpEach() {
		idSchueler = 1L;
		schueler = new DTOSchueler(idSchueler, "123", true);
		schule = new DTOSchuleNRW(1L, "123");
		entlassart = new DTOEntlassarten(1L, "entlassart");
		apiModel = mock(SchuelerSchulbesuchsdaten.class);
		transactionSupport.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<Object>>any()))
				.thenAnswer(invocation -> invocation.getArgument(0, Supplier.class).get());
	}

	@BeforeAll
	static void setUpAll() {
		ASDCoreTypeUtils.initAll();
	}

	@AfterEach
	void tearDown() {
		transactionSupport.close();
	}

	// -------------------------------------------------------------------------
	// getById
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("getById - schueler not found")
	void getByIdNotFound() {
		when(this.schuelerRepository.findById(idSchueler)).thenReturn(Optional.empty());

		assertThatException()
				.isThrownBy(() -> schuelerSchulbesuchService.getById(idSchueler))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Kein Schüler zur ID 1 gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("getById")
	void getById() {
		final var merkmale = List.of(mock(SchuelerSchulbesuchMerkmal.class));
		final var bisherigeSchulen = List.of(mock(SchuelerSchulbesuchSchule.class));

		when(schuelerRepository.findById(idSchueler)).thenReturn(Optional.of(schueler));
		when(schuelerMerkmalService.getAllByIdSchueler(idSchueler)).thenReturn(merkmale);
		when(schuelerBisherigeSchuleService.getAllByIdSchueler(idSchueler)).thenReturn(bisherigeSchulen);
		when(dataSchulen.getAllEntities()).thenReturn(List.of(schule));
		when(dataKatalogEntlassgruende.getAllEntities()).thenReturn(List.of(entlassart));
		when(schulbesuchMapper.toApi(eq(schueler), any(SchulbesuchMappingContext.class))).thenReturn(apiModel);

		final var result = schuelerSchulbesuchService.getById(idSchueler);

		assertThat(result).isNotNull().isEqualTo(apiModel);
		verify(schuelerRepository, times(1)).findById(idSchueler);
		verify(schuelerMerkmalService, times(1)).getAllByIdSchueler(idSchueler);
		verify(schuelerBisherigeSchuleService, times(1)).getAllByIdSchueler(idSchueler);
		verify(schulbesuchMapper, times(1)).toApi(eq(schueler), any(SchulbesuchMappingContext.class));
	}

	// -------------------------------------------------------------------------
	// getByIds
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("getByIds - ids missing")
	void getByIds_idsMissing() {
		assertThat(schuelerSchulbesuchService.getByIds(Collections.emptyList())).isEmpty();
	}

	@Test
	@DisplayName("getByIds - not found")
	void getByIds_notFound() {
		when(this.schuelerRepository.findListByIds(List.of(idSchueler))).thenReturn(Collections.emptyList());
		assertThat(schuelerSchulbesuchService.getByIds(List.of(idSchueler))).isEmpty();

		verifyNoInteractions(dataSchulen);
		verifyNoInteractions(dataKatalogEntlassgruende);
	}

	@Test
	@DisplayName("getByIds")
	void getByIds() {
		final var ids = List.of(3L, 1L, 2L);
		final var schueler1 = new DTOSchueler(1L, "a", true);
		final var schueler2 = new DTOSchueler(2L, "b", true);
		final var schueler3 = new DTOSchueler(3L, "c", true);

		final var apiModel1 = new SchuelerSchulbesuchsdaten();
		apiModel1.id = 1L;
		final var apiModel2 = new SchuelerSchulbesuchsdaten();
		apiModel2.id = 2L;
		final var apiModel3 = new SchuelerSchulbesuchsdaten();
		apiModel3.id = 3L;

		when(schuelerRepository.findListByIds(ids)).thenReturn(List.of(schueler3, schueler1, schueler2));
		when(schuelerMerkmalService.getAllByIdSchueler(anyLong())).thenReturn(List.of());
		when(schuelerBisherigeSchuleService.getAllByIdSchueler(anyLong())).thenReturn(List.of());
		when(dataSchulen.getAllEntities()).thenReturn(List.of(schule));
		when(dataKatalogEntlassgruende.getAllEntities()).thenReturn(List.of(entlassart));
		when(schulbesuchMapper.toApi(eq(schueler1), any())).thenReturn(apiModel1);
		when(schulbesuchMapper.toApi(eq(schueler2), any())).thenReturn(apiModel2);
		when(schulbesuchMapper.toApi(eq(schueler3), any())).thenReturn(apiModel3);

		final var result = schuelerSchulbesuchService.getByIds(ids);

		assertThat(result)
				.extracting("id", Long.class)
				.containsExactly(1L, 2L, 3L);
	}

	// -------------------------------------------------------------------------
	// patch - Allgemein
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("patch - schueler not found")
	void patch_schuelerNotFound() {
		when(schuelerRepository.findById(idSchueler)).thenReturn(Optional.empty());

		assertThatException()
				.isThrownBy(() -> schuelerSchulbesuchService.patch(idSchueler, mock(SchuelerSchulbesuchPatchRequest.class)))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Kein Schüler zur ID %d gefunden.".formatted(idSchueler));
	}

	@Test
	@DisplayName("patch - validate - values not present")
	void patch_validate_valuesNotPresent() {
		final var patchRequest = new SchuelerSchulbesuchPatchRequest();
		patchRequest.idVorherigeSchule = JsonNullable.undefined();
		patchRequest.idEntlassgrundVorherigeSchule = JsonNullable.undefined();
		patchRequest.idEntlassgrundDieseSchule = JsonNullable.undefined();
		patchRequest.idAufnehmendeSchule = JsonNullable.undefined();
		patchRequest.idEinschulungsartGrundschule = JsonNullable.undefined();
		patchRequest.idEingangsphaseGrundschule = JsonNullable.undefined();
		patchRequest.idUebergangsempfehlungGrundschule = JsonNullable.undefined();
		patchRequest.idDauerKindergartenbesuch = JsonNullable.undefined();
		final var someValue = "someValue";
		schueler.LSSchulNr = someValue;
		schueler.LSEntlassgrund = someValue;
		schueler.Entlassgrund = someValue;
		schueler.SchulwechselNr = someValue;
		schueler.EinschulungsartASD = someValue;
		schueler.EPJahre = 41;
		schueler.Uebergangsempfehlung_JG5 = someValue;
		schueler.DauerKindergartenbesuch = someValue;
		when(schuelerRepository.findById(idSchueler)).thenReturn(Optional.of(schueler));

		schuelerSchulbesuchService.patch(idSchueler, patchRequest);

		verify(schulbesuchMapper).patch(any(), schuelerCaptor.capture());
		assertThat(schuelerCaptor.getValue())
				.satisfies(s -> {
					assertThat(s.LSSchulNr).isEqualTo(someValue);
					assertThat(s.LSEntlassgrund).isEqualTo(someValue);
					assertThat(s.Entlassgrund).isEqualTo(someValue);
					assertThat(s.SchulwechselNr).isEqualTo(someValue);
					assertThat(s.EinschulungsartASD).isEqualTo(someValue);
					assertThat(s.EPJahre).isEqualTo(41);
					assertThat(s.Uebergangsempfehlung_JG5).isEqualTo(someValue);
					assertThat(s.DauerKindergartenbesuch).isEqualTo(someValue);
				});
	}

	// -------------------------------------------------------------------------
	// patch - Entlassart (schluesselHoechsterSchulabschluss)
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("patch - Entlassart")
	void patchEntlassart() {
		final var patchRequest = new SchuelerSchulbesuchPatchRequest();
		final var schluessel = SchulabschlussAllgemeinbildend.ABITUR.historie().getLast().schluessel;
		patchRequest.schluesselHoechsterSchulabschluss = JsonNullable.of(schluessel);
		schueler.Entlassart = "--";
		when(schuelerRepository.findById(idSchueler)).thenReturn(Optional.of(schueler));

		schuelerSchulbesuchService.patch(idSchueler, patchRequest);

		verify(schulbesuchMapper).patch(any(), schuelerCaptor.capture());
		assertThat(schuelerCaptor.getValue().Entlassart).isEqualTo(schluessel);
	}

	@Test
	@DisplayName("patch - Entlassart - null")
	void patchEntlassart_null() {
		final var patchRequest = new SchuelerSchulbesuchPatchRequest();
		patchRequest.schluesselHoechsterSchulabschluss = JsonNullable.of(null);
		schueler.Entlassart = "--";
		when(schuelerRepository.findById(idSchueler)).thenReturn(Optional.of(schueler));

		schuelerSchulbesuchService.patch(idSchueler, patchRequest);

		verify(schulbesuchMapper).patch(any(), schuelerCaptor.capture());
		assertThat(schuelerCaptor.getValue().LSSchulNr).isNull();
	}

	@Test
	@DisplayName("patch - Entlassart - wrong schluessel")
	void patchEntlassart_wrongSchluessel() {
		final var patchRequest = new SchuelerSchulbesuchPatchRequest();
		patchRequest.schluesselHoechsterSchulabschluss = JsonNullable.of("--");
		schueler.Entlassart = "before patch";
		when(schuelerRepository.findById(idSchueler)).thenReturn(Optional.of(schueler));

		assertThatException()
				.isThrownBy(() -> schuelerSchulbesuchService.patch(idSchueler, patchRequest))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Status.BAD_REQUEST);
	}

	// -------------------------------------------------------------------------
	// patch - Fachklasse
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("patch - Fachklasse - wrong schluessel")
	void patchFachklasse_wrongSchluessel() {
		final var patchRequest = new SchuelerSchulbesuchPatchRequest();
		patchRequest.schluesselCoreTypeFachklasseVorherigeSchule = JsonNullable.of("99-99999");
		when(schuelerRepository.findById(idSchueler)).thenReturn(Optional.of(schueler));

		assertThatException()
				.isThrownBy(() -> schuelerSchulbesuchService.patch(idSchueler, patchRequest))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Keine Fachklasse mit dem Schlüssel 99-99999 gefunden.")
				.hasFieldOrPropertyWithValue("status", Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch - Fachklasse - wrong format")
	void patchFachklasse_wrongFormat() {
		final var patchRequest = new SchuelerSchulbesuchPatchRequest();
		patchRequest.schluesselCoreTypeFachklasseVorherigeSchule = JsonNullable.of("--");
		when(schuelerRepository.findById(idSchueler)).thenReturn(Optional.of(schueler));

		assertThatException()
				.isThrownBy(() -> schuelerSchulbesuchService.patch(idSchueler, patchRequest))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Der Schlüssel '--' entspricht nicht dem erwarteten Format (z.B. 'XX-XXXXX' oder 'XXX-XXXXX').")
				.hasFieldOrPropertyWithValue("status", Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch - Fachklasse")
	void patchFachklasse() {
		final var patchRequest = new SchuelerSchulbesuchPatchRequest();
		patchRequest.schluesselCoreTypeFachklasseVorherigeSchule = JsonNullable.of("170-10100");
		schueler.LSFachklSIM = "beforePatch";
		schueler.LSFachklKennung = "beforePatch";
		when(schuelerRepository.findById(idSchueler)).thenReturn(Optional.of(schueler));

		schuelerSchulbesuchService.patch(idSchueler, patchRequest);

		verify(schulbesuchMapper).patch(any(), schuelerCaptor.capture());
		assertThat(schuelerCaptor.getValue().LSFachklSIM).isEqualTo("10100");
		assertThat(schuelerCaptor.getValue().LSFachklKennung).isEqualTo("170-101-00");
	}

	@Test
	@DisplayName("patch - Fachklasse - null")
	void patchFachklasse_null() {
		final var patchRequest = new SchuelerSchulbesuchPatchRequest();
		patchRequest.schluesselCoreTypeFachklasseVorherigeSchule = JsonNullable.of(null);
		schueler.LSFachklSIM = "beforePatch";
		schueler.LSFachklKennung = "beforePatch";
		when(schuelerRepository.findById(idSchueler)).thenReturn(Optional.of(schueler));

		schuelerSchulbesuchService.patch(idSchueler, patchRequest);

		verify(schulbesuchMapper).patch(any(), schuelerCaptor.capture());
		assertThat(schuelerCaptor.getValue().LSFachklSIM).isNull();
		assertThat(schuelerCaptor.getValue().LSFachklKennung).isNull();
	}

	// -------------------------------------------------------------------------
	// patch - HerkunftSonstige
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("patch - HerkunftSonstige - wrong schluessel")
	void patchHerkunftSonstige_wrongSchluessel() {
		final var patchRequest = new SchuelerSchulbesuchPatchRequest();
		patchRequest.idHerkunftSonstigeVorherigeSchule = JsonNullable.of(99999L);
		when(schuelerRepository.findById(idSchueler)).thenReturn(Optional.of(schueler));

		assertThatException()
				.isThrownBy(() -> schuelerSchulbesuchService.patch(idSchueler, patchRequest))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Keine HerkunftSonstige mit der ID 99999 gefunden.")
				.hasFieldOrPropertyWithValue("status", Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch - HerkunftSonstige")
	void patchHerkunftSonstige() {
		final var patchRequest = new SchuelerSchulbesuchPatchRequest();
		final var eintrag = de.svws_nrw.asd.types.schueler.HerkunftSonstige.data()
				.getWerte().getFirst().historie().getFirst();
		patchRequest.idHerkunftSonstigeVorherigeSchule = JsonNullable.of(eintrag.id);
		schueler.LSSchulform = "beforePatch";
		schueler.LSSchulformSIM = "beforePatch";
		when(schuelerRepository.findById(idSchueler)).thenReturn(Optional.of(schueler));

		schuelerSchulbesuchService.patch(idSchueler, patchRequest);

		verify(schulbesuchMapper).patch(any(), schuelerCaptor.capture());
		assertThat(schuelerCaptor.getValue().LSSchulform).isNull();
		assertThat(schuelerCaptor.getValue().LSSchulformSIM).isEqualTo(eintrag.kuerzel);
	}

	@Test
	@DisplayName("patch - HerkunftSonstige - null")
	void patchHerkunftSonstige_null() {
		final var patchRequest = new SchuelerSchulbesuchPatchRequest();
		patchRequest.idHerkunftSonstigeVorherigeSchule = JsonNullable.of(null);
		when(schuelerRepository.findById(idSchueler)).thenReturn(Optional.of(schueler));

		schuelerSchulbesuchService.patch(idSchueler, patchRequest);

		verify(schulbesuchMapper).patch(any(), schuelerCaptor.capture());
		assertThat(schuelerCaptor.getValue().LSSchulform).isNull();
		assertThat(schuelerCaptor.getValue().LSSchulformSIM).isNull();
	}

	// -------------------------------------------------------------------------
	// patch - HerkunftSchulform (idHerkunftSchulformVorherigeSchule)
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("patch - HerkunftSchulform - unbekannte id - BAD_REQUEST")
	void patchHerkunftSchulform_unbekannteId_wirftBadRequest() {
		final var patchRequest = new SchuelerSchulbesuchPatchRequest();
		patchRequest.idHerkunftSchulformVorherigeSchule = JsonNullable.of(99999L);
		when(schuelerRepository.findById(idSchueler)).thenReturn(Optional.of(schueler));

		assertThatException()
				.isThrownBy(() -> schuelerSchulbesuchService.patch(idSchueler, patchRequest))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Keine HerkunftSchulform mit der ID 99999 gefunden.")
				.hasFieldOrPropertyWithValue("status", Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch - HerkunftSchulform - gueltige id - setzt LSSchulform und LSSchulformSIM")
	void patchHerkunftSchulform_gueltigeId_setztFelder() {
		final var patchRequest = new SchuelerSchulbesuchPatchRequest();
		// GY: kein BK/WB/SB/SF -> LSSchulform=kuerzel, LSSchulformSIM=kuerzel
		final var eintrag = HerkunftSchulform.data().getWertByKuerzel("GY").historie().getFirst();
		patchRequest.idHerkunftSchulformVorherigeSchule = JsonNullable.of(eintrag.id);
		schueler.LSSchulform = "beforePatch";
		schueler.LSSchulformSIM = "beforePatch";
		when(schuelerRepository.findById(idSchueler)).thenReturn(Optional.of(schueler));

		schuelerSchulbesuchService.patch(idSchueler, patchRequest);

		verify(schulbesuchMapper).patch(any(), schuelerCaptor.capture());
		assertThat(schuelerCaptor.getValue().LSSchulform).isEqualTo("GY");
		assertThat(schuelerCaptor.getValue().LSSchulformSIM).isEqualTo("GY");
	}

	@Test
	@DisplayName("patch - HerkunftSchulform - BK - setzt LSSchulformSIM=null")
	void patchHerkunftSchulform_BK_setztSimNull() {
		final var patchRequest = new SchuelerSchulbesuchPatchRequest();
		final var eintrag = HerkunftSchulform.data().getWertByKuerzel("BK").historie().getFirst();
		patchRequest.idHerkunftSchulformVorherigeSchule = JsonNullable.of(eintrag.id);
		schueler.LSSchulform = "beforePatch";
		schueler.LSSchulformSIM = "beforePatch";
		when(schuelerRepository.findById(idSchueler)).thenReturn(Optional.of(schueler));

		schuelerSchulbesuchService.patch(idSchueler, patchRequest);

		verify(schulbesuchMapper).patch(any(), schuelerCaptor.capture());
		assertThat(schuelerCaptor.getValue().LSSchulform).isEqualTo("BK");
		assertThat(schuelerCaptor.getValue().LSSchulformSIM).isNull();
	}

	@Test
	@DisplayName("patch - HerkunftSchulform - null - setzt beide Felder auf null")
	void patchHerkunftSchulform_null_setztBeideNull() {
		final var patchRequest = new SchuelerSchulbesuchPatchRequest();
		patchRequest.idHerkunftSchulformVorherigeSchule = JsonNullable.of(null);
		schueler.LSSchulform = "beforePatch";
		schueler.LSSchulformSIM = "beforePatch";
		when(schuelerRepository.findById(idSchueler)).thenReturn(Optional.of(schueler));

		schuelerSchulbesuchService.patch(idSchueler, patchRequest);

		verify(schulbesuchMapper).patch(any(), schuelerCaptor.capture());
		assertThat(schuelerCaptor.getValue().LSSchulform).isNull();
		assertThat(schuelerCaptor.getValue().LSSchulformSIM).isNull();
	}

	// -------------------------------------------------------------------------
	// patch - LSSchulNr (idVorherigeSchule)
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("patch - patchLSSchulNr")
	void patchVorherigeSchuleAndSchulform() {
		final var patchRequest = new SchuelerSchulbesuchPatchRequest();
		patchRequest.idVorherigeSchule = JsonNullable.of(1L);
		schueler.LSSchulNr = "beforePatch";

		final var schulEintrag = new SchulEintrag();
		schulEintrag.id = 1L;
		schulEintrag.schulnummerIntern = "100001";
		schulEintrag.schulnummerStatistik = "100001";
		// GY: Schulform.json id=6000 – kein BK/WB/SB, daher LSSchulform=LSSchulformSIM="GY"
		schulEintrag.idSchulform = 6000L;

		when(schuelerRepository.findById(idSchueler)).thenReturn(Optional.of(schueler));
		when(dataSchulen.getById(1L)).thenReturn(schulEintrag);

		schuelerSchulbesuchService.patch(idSchueler, patchRequest);

		verify(schulbesuchMapper).patch(any(), schuelerCaptor.capture());
		assertThat(schuelerCaptor.getValue().LSSchulNr).isEqualTo("100001");
		assertThat(schuelerCaptor.getValue().LSSchulform).isEqualTo("GY");
		assertThat(schuelerCaptor.getValue().LSSchulformSIM).isEqualTo("GY");
	}

	@Test
	@DisplayName("patch - patchLSSchulNr - null")
	void patchVorherigeSchule_AndSchulform_null() {
		final var patchRequest = new SchuelerSchulbesuchPatchRequest();
		patchRequest.idVorherigeSchule = JsonNullable.of(null);
		schueler.LSSchulNr = "beforePatch";
		when(schuelerRepository.findById(idSchueler)).thenReturn(Optional.of(schueler));

		schuelerSchulbesuchService.patch(idSchueler, patchRequest);

		verify(schulbesuchMapper).patch(any(), schuelerCaptor.capture());
		assertThat(schuelerCaptor.getValue().LSSchulNr).isNull();
	}

	// -------------------------------------------------------------------------
	// patch - LSEntlassgrund (vorigeEntlassgrundID)
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("patch - patchLSEntlassgrund")
	void patchLSEntlassgrund() {
		final var patchRequest = new SchuelerSchulbesuchPatchRequest();
		patchRequest.idEntlassgrundVorherigeSchule = JsonNullable.of(1L);
		schueler.LSEntlassgrund = "beforePatch";
		when(schuelerRepository.findById(idSchueler)).thenReturn(Optional.of(schueler));
		when(dataKatalogEntlassgruende.getEntityById(1L)).thenReturn(entlassart);

		schuelerSchulbesuchService.patch(idSchueler, patchRequest);

		verify(schulbesuchMapper).patch(any(), schuelerCaptor.capture());
		assertThat(schuelerCaptor.getValue().LSEntlassgrund).isEqualTo("entlassart");
	}

	@Test
	@DisplayName("patch - patchLSEntlassgrund - null")
	void patchLSEntlassgrund_null() {
		final var patchRequest = new SchuelerSchulbesuchPatchRequest();
		patchRequest.idEntlassgrundVorherigeSchule = JsonNullable.of(null);
		schueler.LSEntlassgrund = "beforePatch";
		when(schuelerRepository.findById(idSchueler)).thenReturn(Optional.of(schueler));

		schuelerSchulbesuchService.patch(idSchueler, patchRequest);

		verify(schulbesuchMapper).patch(any(), schuelerCaptor.capture());
		assertThat(schuelerCaptor.getValue().LSEntlassgrund).isNull();
	}

	// -------------------------------------------------------------------------
	// patch - Entlassgrund (entlassungGrundID)
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("patch - patchEntlassgrund")
	void patchEntlassgrund() {
		final var patchRequest = new SchuelerSchulbesuchPatchRequest();
		patchRequest.idEntlassgrundDieseSchule = JsonNullable.of(1L);
		schueler.Entlassgrund = "beforePatch";
		when(schuelerRepository.findById(idSchueler)).thenReturn(Optional.of(schueler));
		when(dataKatalogEntlassgruende.getEntityById(1L)).thenReturn(entlassart);

		schuelerSchulbesuchService.patch(idSchueler, patchRequest);

		verify(schulbesuchMapper).patch(any(), schuelerCaptor.capture());
		assertThat(schuelerCaptor.getValue().Entlassgrund).isEqualTo("entlassart");
	}

	@Test
	@DisplayName("patch - patchEntlassgrund - null")
	void patchEntlassgrund_null() {
		final var patchRequest = new SchuelerSchulbesuchPatchRequest();
		patchRequest.idEntlassgrundDieseSchule = JsonNullable.of(null);
		schueler.Entlassgrund = "beforePatch";
		when(schuelerRepository.findById(idSchueler)).thenReturn(Optional.of(schueler));

		schuelerSchulbesuchService.patch(idSchueler, patchRequest);

		verify(schulbesuchMapper).patch(any(), schuelerCaptor.capture());
		assertThat(schuelerCaptor.getValue().Entlassgrund).isNull();
	}

	// -------------------------------------------------------------------------
	// patch - SchulwechselNr (idAufnehmendeSchule)
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("patch - patchSchulwechselNr")
	void patchSchulwechselNr() {
		final var patchRequest = new SchuelerSchulbesuchPatchRequest();
		patchRequest.idAufnehmendeSchule = JsonNullable.of(1L);
		schueler.SchulwechselNr = "beforePatch";
		when(schuelerRepository.findById(idSchueler)).thenReturn(Optional.of(schueler));
		when(dataSchulen.getEntityById(1L)).thenReturn(schule);

		schuelerSchulbesuchService.patch(idSchueler, patchRequest);

		verify(schulbesuchMapper).patch(any(), schuelerCaptor.capture());
		assertThat(schuelerCaptor.getValue().SchulwechselNr).isEqualTo("123");
	}

	@Test
	@DisplayName("patch - patchSchulwechselNr - null")
	void patchSchulwechselNr_null() {
		final var patchRequest = new SchuelerSchulbesuchPatchRequest();
		patchRequest.idAufnehmendeSchule = JsonNullable.of(null);
		schueler.SchulwechselNr = "beforePatch";
		when(schuelerRepository.findById(idSchueler)).thenReturn(Optional.of(schueler));

		schuelerSchulbesuchService.patch(idSchueler, patchRequest);

		verify(schulbesuchMapper).patch(any(), schuelerCaptor.capture());
		assertThat(schuelerCaptor.getValue().SchulwechselNr).isNull();
	}

	// -------------------------------------------------------------------------
	// patch - Einschulungsart (grundschuleEinschulungsartID)
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("patch - patchEinschulungsart")
	void patchEinschulungsart() {
		final var patchRequest = new SchuelerSchulbesuchPatchRequest();
		final var eintrag = Einschulungsart.data().getWerte().getFirst().historie().getFirst();
		patchRequest.idEinschulungsartGrundschule = JsonNullable.of(eintrag.id);
		schueler.EinschulungsartASD = "beforePatch";
		when(schuelerRepository.findById(idSchueler)).thenReturn(Optional.of(schueler));

		schuelerSchulbesuchService.patch(idSchueler, patchRequest);

		verify(schulbesuchMapper).patch(any(), schuelerCaptor.capture());
		assertThat(schuelerCaptor.getValue().EinschulungsartASD).isEqualTo(eintrag.schluessel);
	}

	@Test
	@DisplayName("patch - patchEinschulungsart - null")
	void patchEinschulungsart_null() {
		final var patchRequest = new SchuelerSchulbesuchPatchRequest();
		patchRequest.idEinschulungsartGrundschule = JsonNullable.of(null);
		schueler.EinschulungsartASD = "beforePatch";
		when(schuelerRepository.findById(idSchueler)).thenReturn(Optional.of(schueler));

		schuelerSchulbesuchService.patch(idSchueler, patchRequest);

		verify(schulbesuchMapper).patch(any(), schuelerCaptor.capture());
		assertThat(schuelerCaptor.getValue().EinschulungsartASD).isNull();
	}

	@Test
	@DisplayName("patch - patchEinschulungsart - invalid id")
	void patchEinschulungsart_invalidId() {
		final var patchRequest = new SchuelerSchulbesuchPatchRequest();
		patchRequest.idEinschulungsartGrundschule = JsonNullable.of(-1L);
		when(schuelerRepository.findById(idSchueler)).thenReturn(Optional.of(schueler));

		assertThatException()
				.isThrownBy(() -> schuelerSchulbesuchService.patch(idSchueler, patchRequest))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Keine Einschulungsart mit der ID -1 gefunden.")
				.hasFieldOrPropertyWithValue("status", Status.BAD_REQUEST);
	}

	// -------------------------------------------------------------------------
	// patch - EPJahre (idGrundschuleJahreEingangsphase)
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("patch - patchEPJahre")
	void patchEPJahre() {
		final var patchRequest = new SchuelerSchulbesuchPatchRequest();
		final var eintrag = PrimarstufeSchuleingangsphaseBesuchsjahre.data().getWerte().getFirst().historie().getFirst();
		patchRequest.idEingangsphaseGrundschule = JsonNullable.of(eintrag.id);
		schueler.EPJahre = 99;
		when(schuelerRepository.findById(idSchueler)).thenReturn(Optional.of(schueler));

		schuelerSchulbesuchService.patch(idSchueler, patchRequest);

		verify(schulbesuchMapper).patch(any(), schuelerCaptor.capture());
		assertThat(schuelerCaptor.getValue().EPJahre).isEqualTo(Math.toIntExact(eintrag.id));
	}

	@Test
	@DisplayName("patch - patchEPJahre - null")
	void patchEPJahre_null() {
		final var patchRequest = new SchuelerSchulbesuchPatchRequest();
		patchRequest.idEingangsphaseGrundschule = JsonNullable.of(null);
		schueler.EPJahre = 99;
		when(schuelerRepository.findById(idSchueler)).thenReturn(Optional.of(schueler));

		schuelerSchulbesuchService.patch(idSchueler, patchRequest);

		verify(schulbesuchMapper).patch(any(), schuelerCaptor.capture());
		assertThat(schuelerCaptor.getValue().EPJahre).isNull();
	}

	@Test
	@DisplayName("patch - patchEPJahre - invalid id")
	void patchEPJahre_invalidId() {
		final var patchRequest = new SchuelerSchulbesuchPatchRequest();
		patchRequest.idEingangsphaseGrundschule = JsonNullable.of(-1L);
		when(schuelerRepository.findById(idSchueler)).thenReturn(Optional.of(schueler));

		assertThatException()
				.isThrownBy(() -> schuelerSchulbesuchService.patch(idSchueler, patchRequest))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Keine Eingangsphase mit der ID -1 vorhanden.")
				.hasFieldOrPropertyWithValue("status", Status.BAD_REQUEST);
	}

	// -------------------------------------------------------------------------
	// patch - Uebergangsempfehlung (idGrundschuleUebergangsempfehlung)
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("patch - patchUebergangsempfehlung")
	void patchUebergangsempfehlung() {
		final var patchRequest = new SchuelerSchulbesuchPatchRequest();
		final var eintrag = Uebergangsempfehlung.data().getWerte().getFirst().historie().getFirst();
		patchRequest.idUebergangsempfehlungGrundschule = JsonNullable.of(eintrag.id);
		schueler.Uebergangsempfehlung_JG5 = "beforePatch";
		when(schuelerRepository.findById(idSchueler)).thenReturn(Optional.of(schueler));

		schuelerSchulbesuchService.patch(idSchueler, patchRequest);

		verify(schulbesuchMapper).patch(any(), schuelerCaptor.capture());
		assertThat(schuelerCaptor.getValue().Uebergangsempfehlung_JG5).isEqualTo(eintrag.schluessel);
	}

	@Test
	@DisplayName("patch - patchUebergangsempfehlung - null")
	void patchUebergangsempfehlung_null() {
		final var patchRequest = new SchuelerSchulbesuchPatchRequest();
		patchRequest.idUebergangsempfehlungGrundschule = JsonNullable.of(null);
		schueler.Uebergangsempfehlung_JG5 = "beforePatch";
		when(schuelerRepository.findById(idSchueler)).thenReturn(Optional.of(schueler));

		schuelerSchulbesuchService.patch(idSchueler, patchRequest);

		verify(schulbesuchMapper).patch(any(), schuelerCaptor.capture());
		assertThat(schuelerCaptor.getValue().Uebergangsempfehlung_JG5).isNull();
	}

	@Test
	@DisplayName("patch - patchUebergangsempfehlung - invalid id")
	void patchUebergangsempfehlung_invalidId() {
		final var patchRequest = new SchuelerSchulbesuchPatchRequest();
		patchRequest.idUebergangsempfehlungGrundschule = JsonNullable.of(-1L);
		when(schuelerRepository.findById(idSchueler)).thenReturn(Optional.of(schueler));

		assertThatException()
				.isThrownBy(() -> schuelerSchulbesuchService.patch(idSchueler, patchRequest))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Keine Übergangsempfehlung für die ID -1 gefunden.")
				.hasFieldOrPropertyWithValue("status", Status.BAD_REQUEST);
	}

	// -------------------------------------------------------------------------
	// patch - DauerKindergartenbesuch (idDauerKindergartenbesuch)
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("patch - patchKindergartenbesuch")
	void patchKindergartenbesuch() {
		final var patchRequest = new SchuelerSchulbesuchPatchRequest();
		final var eintrag = Kindergartenbesuch.data().getWerte().getFirst().historie().getFirst();
		patchRequest.idDauerKindergartenbesuch = JsonNullable.of(eintrag.id);
		schueler.DauerKindergartenbesuch = "beforePatch";
		when(schuelerRepository.findById(idSchueler)).thenReturn(Optional.of(schueler));

		schuelerSchulbesuchService.patch(idSchueler, patchRequest);

		verify(schulbesuchMapper).patch(any(), schuelerCaptor.capture());
		assertThat(schuelerCaptor.getValue().DauerKindergartenbesuch).isEqualTo(eintrag.schluessel);
	}

	@Test
	@DisplayName("patch - patchKindergartenbesuch - null")
	void patchKindergartenbesuch_null() {
		final var patchRequest = new SchuelerSchulbesuchPatchRequest();
		patchRequest.idDauerKindergartenbesuch = JsonNullable.of(null);
		schueler.DauerKindergartenbesuch = "beforePatch";
		when(schuelerRepository.findById(idSchueler)).thenReturn(Optional.of(schueler));

		schuelerSchulbesuchService.patch(idSchueler, patchRequest);

		verify(schulbesuchMapper).patch(any(), schuelerCaptor.capture());
		assertThat(schuelerCaptor.getValue().DauerKindergartenbesuch).isNull();
	}

	@Test
	@DisplayName("patch - patchKindergartenbesuch - invalid id")
	void patchKindergartenbesuch_invalidId() {
		final var patchRequest = new SchuelerSchulbesuchPatchRequest();
		patchRequest.idDauerKindergartenbesuch = JsonNullable.of(-1L);
		when(schuelerRepository.findById(idSchueler)).thenReturn(Optional.of(schueler));

		assertThatException()
				.isThrownBy(() -> schuelerSchulbesuchService.patch(idSchueler, patchRequest))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Kein Kindergartenbesuch mit der ID -1 gefunden.")
				.hasFieldOrPropertyWithValue("status", Status.BAD_REQUEST);
	}

	// -------------------------------------------------------------------------
	// patch - AbschlussartVorherigeSchule (schluesselAbschlussart*)
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("patch - patchAbschlussartVorherigeSchule - beide undefined - kein Patch")
	void patchAbschlussartVorherigeSchule_beideUndefined() {
		final var patchRequest = new SchuelerSchulbesuchPatchRequest();
		schueler.LSEntlassArt = "2A";
		when(schuelerRepository.findById(idSchueler)).thenReturn(Optional.of(schueler));

		schuelerSchulbesuchService.patch(idSchueler, patchRequest);

		verify(schulbesuchMapper).patch(any(), schuelerCaptor.capture());
		assertThat(schuelerCaptor.getValue().LSEntlassArt).isEqualTo("2A");
	}

	@Test
	@DisplayName("patch - patchAbschlussartVorherigeSchule - nur allgemeinbildend - zweite Stelle ersetzen")
	void patchAbschlussartVorherigeSchule_nurAllgemeinbildend() {
		final var patchRequest = new SchuelerSchulbesuchPatchRequest();
		patchRequest.schluesselAbschlussartAllgemeinbildendVorherigeSchule = JsonNullable.of("A");
		schueler.LSEntlassArt = "2G";
		when(schuelerRepository.findById(idSchueler)).thenReturn(Optional.of(schueler));

		schuelerSchulbesuchService.patch(idSchueler, patchRequest);

		verify(schulbesuchMapper).patch(any(), schuelerCaptor.capture());
		assertThat(schuelerCaptor.getValue().LSEntlassArt).isEqualTo("2A");
	}

	@Test
	@DisplayName("patch - patchAbschlussartVorherigeSchule - nur allgemeinbildend - DB hat keinen berufsbildend")
	void patchAbschlussartVorherigeSchule_nurAllgemeinbildend_ohneBerufsbildendInDb() {
		final var patchRequest = new SchuelerSchulbesuchPatchRequest();
		patchRequest.schluesselAbschlussartAllgemeinbildendVorherigeSchule = JsonNullable.of("A");
		schueler.LSEntlassArt = "G";
		when(schuelerRepository.findById(idSchueler)).thenReturn(Optional.of(schueler));

		schuelerSchulbesuchService.patch(idSchueler, patchRequest);

		verify(schulbesuchMapper).patch(any(), schuelerCaptor.capture());
		assertThat(schuelerCaptor.getValue().LSEntlassArt).isEqualTo("A");
	}

	@Test
	@DisplayName("patch - patchAbschlussartVorherigeSchule - nur berufsbildend - erste Stelle ersetzen")
	void patchAbschlussartVorherigeSchule_nurBerufsbildend() {
		final var patchRequest = new SchuelerSchulbesuchPatchRequest();
		patchRequest.schluesselAbschlussartBerufsbildendVorherigeSchule = JsonNullable.of("3");
		schueler.LSEntlassArt = "2G";
		when(schuelerRepository.findById(idSchueler)).thenReturn(Optional.of(schueler));

		schuelerSchulbesuchService.patch(idSchueler, patchRequest);

		verify(schulbesuchMapper).patch(any(), schuelerCaptor.capture());
		assertThat(schuelerCaptor.getValue().LSEntlassArt).isEqualTo("3G");
	}

	@Test
	@DisplayName("patch - patchAbschlussartVorherigeSchule - nur berufsbildend - DB hat keinen allgemeinbildend - bleibt null")
	void patchAbschlussartVorherigeSchule_nurBerufsbildend_ohneAllgemeinbildendInDb() {
		final var patchRequest = new SchuelerSchulbesuchPatchRequest();
		patchRequest.schluesselAbschlussartBerufsbildendVorherigeSchule = JsonNullable.of("3");
		schueler.LSEntlassArt = null;
		when(schuelerRepository.findById(idSchueler)).thenReturn(Optional.of(schueler));

		schuelerSchulbesuchService.patch(idSchueler, patchRequest);

		verify(schulbesuchMapper).patch(any(), schuelerCaptor.capture());
		assertThat(schuelerCaptor.getValue().LSEntlassArt).isNull();
	}

	@Test
	@DisplayName("patch - patchAbschlussartVorherigeSchule - beide gesetzt - kombiniert")
	void patchAbschlussartVorherigeSchule_beideGesetzt() {
		final var patchRequest = new SchuelerSchulbesuchPatchRequest();
		patchRequest.schluesselAbschlussartAllgemeinbildendVorherigeSchule = JsonNullable.of("A");
		patchRequest.schluesselAbschlussartBerufsbildendVorherigeSchule = JsonNullable.of("2");
		schueler.LSEntlassArt = "3G";
		when(schuelerRepository.findById(idSchueler)).thenReturn(Optional.of(schueler));

		schuelerSchulbesuchService.patch(idSchueler, patchRequest);

		verify(schulbesuchMapper).patch(any(), schuelerCaptor.capture());
		assertThat(schuelerCaptor.getValue().LSEntlassArt).isEqualTo("2A");
	}

	@Test
	@DisplayName("patch - patchAbschlussartVorherigeSchule - allgemeinbildend null - setzt LSEntlassArt auf null")
	void patchAbschlussartVorherigeSchule_allgemeinbildendNull() {
		final var patchRequest = new SchuelerSchulbesuchPatchRequest();
		patchRequest.schluesselAbschlussartAllgemeinbildendVorherigeSchule = JsonNullable.of(null);
		schueler.LSEntlassArt = "2G";
		when(schuelerRepository.findById(idSchueler)).thenReturn(Optional.of(schueler));

		schuelerSchulbesuchService.patch(idSchueler, patchRequest);

		verify(schulbesuchMapper).patch(any(), schuelerCaptor.capture());
		assertThat(schuelerCaptor.getValue().LSEntlassArt).isNull();
	}

	@Test
	@DisplayName("patch - patchAbschlussartVorherigeSchule - ungültiger allgemeinbildend Schlüssel - BAD_REQUEST")
	void patchAbschlussartVorherigeSchule_ungueltigerAllgemeinbildendSchluessel() {
		final var patchRequest = new SchuelerSchulbesuchPatchRequest();
		patchRequest.schluesselAbschlussartAllgemeinbildendVorherigeSchule = JsonNullable.of("UNGUELTIG");
		schueler.LSEntlassArt = "2G";
		when(schuelerRepository.findById(idSchueler)).thenReturn(Optional.of(schueler));

		assertThatException()
				.isThrownBy(() -> schuelerSchulbesuchService.patch(idSchueler, patchRequest))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch - patchAbschlussartVorherigeSchule - ungültiger berufsbildend Schlüssel - BAD_REQUEST")
	void patchAbschlussartVorherigeSchule_ungueltigerBerufsbildendSchluessel() {
		final var patchRequest = new SchuelerSchulbesuchPatchRequest();
		patchRequest.schluesselAbschlussartBerufsbildendVorherigeSchule = JsonNullable.of("UNGUELTIG");
		schueler.LSEntlassArt = "2G";
		when(schuelerRepository.findById(idSchueler)).thenReturn(Optional.of(schueler));

		assertThatException()
				.isThrownBy(() -> schuelerSchulbesuchService.patch(idSchueler, patchRequest))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Status.BAD_REQUEST);
	}

	// -------------------------------------------------------------------------
	// validate - Hochschulabschluss
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("patch - patchHochschulabschluss - null")
	void patchHochschulabschluss_null() {
		final var patchRequest = new SchuelerSchulbesuchPatchRequest();
		patchRequest.idHochschulabschluss = JsonNullable.of(null);
		schueler.idHochschulabschluss = 99L;
		when(schuelerRepository.findById(idSchueler)).thenReturn(Optional.of(schueler));

		assertThatNoException().isThrownBy(() -> schuelerSchulbesuchService.patch(idSchueler, patchRequest));
	}

	@Test
	@DisplayName("patch - patchHochschulabschluss - invalid id")
	void patchHochschulabschluss_invalidId() {
		final var patchRequest = new SchuelerSchulbesuchPatchRequest();
		patchRequest.idHochschulabschluss = JsonNullable.of(-1L);
		when(schuelerRepository.findById(idSchueler)).thenReturn(Optional.of(schueler));

		assertThatException()
				.isThrownBy(() -> schuelerSchulbesuchService.patch(idSchueler, patchRequest))
				.isInstanceOf(ApiOperationException.class)
				.withMessage("Kein Hochschulabschluss mit der ID -1 gefunden.")
				.hasFieldOrPropertyWithValue("status", Status.BAD_REQUEST);
	}

}
