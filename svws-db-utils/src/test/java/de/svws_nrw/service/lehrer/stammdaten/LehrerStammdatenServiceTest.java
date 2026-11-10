package de.svws_nrw.service.lehrer.stammdaten;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import de.svws_nrw.asd.types.Geschlecht;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.types.PersonalTyp;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.katalog.DTOOrtsteil;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOSchulleitung;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.mapper.lehrer.stammdaten.LehrerStammdatenMapper;
import de.svws_nrw.mapper.schule.schulleitung.SchulleitungMapper;
import de.svws_nrw.repo.lehrer.LehrerRepository;
import de.svws_nrw.repo.schule.kataloge.ort.OrtRepository;
import de.svws_nrw.repo.schule.kataloge.ortsteil.OrtsteilRepository;
import de.svws_nrw.repo.schule.schulleitung.SchulleitungRepository;
import de.svws_nrw.service.lehrer.einwilligung.LehrerEinwilligungService;
import de.svws_nrw.service.lehrer.foto.LehrerFoto;
import de.svws_nrw.service.lehrer.foto.LehrerFotoService;
import de.svws_nrw.service.lehrer.lernplattform.LehrerLernplattformService;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.jackson.nullable.JsonNullable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * Tests für den {@link LehrerStammdatenService}.
 */
@ExtendWith(MockitoExtension.class)
class LehrerStammdatenServiceTest {

	private static final long ID = 1L;
	private static final long ID_NEU = 5L;
	private static final long ID_ORT = 100L;
	private static final long ID_ORTSTEIL = 200L;
	private static final long ID_ANDERER_ORT = 999L;
	private static final int GESCHLECHT_M = 3;
	private static final int GESCHLECHT_UNGUELTIG = 999;
	private static final long NATIONALITAET_DZA = 68090065L;
	private static final long NATIONALITAET_UNGUELTIG = 999999999L;

	@Mock
	private LehrerRepository lehrerRepository;

	@Mock
	private SchulleitungRepository schulleitungRepository;

	@Mock
	private OrtRepository ortRepository;

	@Mock
	private OrtsteilRepository ortsteilRepository;

	@Mock
	private LehrerFotoService fotoService;

	@Mock
	private LehrerLernplattformService lernplattformService;

	@Mock
	private LehrerEinwilligungService einwilligungService;

	private LehrerStammdatenService service;

	private MockedStatic<TransactionSupport> transactionSupport;

	private DTOLehrer entity;

	@BeforeAll
	static void setUpAll() {
		ASDCoreTypeUtils.initAll();
	}

	@BeforeEach
	void setUp() {
		entity = new DTOLehrer(ID, "MUS", "Mustermann");
		entity.Vorname = "Max";
		entity.PersonTyp = PersonalTyp.LEHRKRAFT;
		entity.Geschlecht = Geschlecht.M;

		transactionSupport = mockStatic(TransactionSupport.class);
		transactionSupport.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<Object>>any()))
				.thenAnswer(invocation -> invocation.getArgument(0, Supplier.class).get());

		final var repositories = new LehrerStammdatenRepositories(
				lehrerRepository,
				schulleitungRepository,
				ortRepository,
				ortsteilRepository
		);
		service = new LehrerStammdatenService(
				repositories,
				fotoService,
				lernplattformService,
				einwilligungService,
				LehrerStammdatenMapper.INSTANCE,
				SchulleitungMapper.INSTANCE
		);
	}

	@AfterEach
	void tearDown() {
		transactionSupport.close();
	}

	private static LehrerStammdatenCreateRequest createRequest() {
		final var request = new LehrerStammdatenCreateRequest();
		request.kuerzel = "NEU";
		request.personalTyp = "LEHRKRAFT";
		request.nachname = "Neumann";
		request.vorname = "Nora";
		request.geschlecht = GESCHLECHT_M;

		return request;
	}

	private static DTOOrtsteil ortsteil(final long idOrtsteil, final long idOrt) {
		final var ortsteil = new DTOOrtsteil(idOrtsteil, "Ortsteil");
		ortsteil.idOrt = idOrt;

		return ortsteil;
	}

	private void stubCreate() {
		lenient().when(lehrerRepository.create(any(DTOLehrer.class))).thenAnswer(invocation -> {
			final var lehrer = invocation.getArgument(0, DTOLehrer.class);
			lehrer.ID = ID_NEU;

			return lehrer;
		});
	}

	// -------------------------------------------------------------------------
	// Lesen
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("getById | liefert die Stammdaten mit Foto und Leitungsfunktionen")
	void getById() {
		entity.kuerzelLID = "ALBE";
		entity.Sichtbar = false;
		when(lehrerRepository.findById(ID)).thenReturn(Optional.of(entity));
		when(fotoService.getByLehrerIds(List.of(ID))).thenReturn(List.of(new LehrerFoto(ID, "base64")));
		when(schulleitungRepository.getAllByIdLehrer(ID)).thenReturn(List.of(new DTOSchulleitung(1L, 2L, "Schulleitung", ID)));

		final var result = service.getById(ID);

		assertThat(result)
				.satisfies(r -> assertThat(r.leitungsfunktionen).hasSize(1).allSatisfy(l -> assertThat(l.bezeichnung).isEqualTo("Schulleitung")))
				.extracting("id", "kuerzel", "kuerzelStatistik", "personalTyp", "geschlecht", "foto", "istSichtbar", "istRelevantFuerStatistik")
				.containsExactly(ID, "MUS", "ALBE", "LEHRKRAFT", GESCHLECHT_M, "base64", false, true);
	}

	@Test
	@DisplayName("getById | Standardwerte für nicht gesetzte Felder und fehlendes Foto")
	void getById_standardwerte() {
		final var leer = new DTOLehrer(ID, "MUS", "Mustermann");
		when(lehrerRepository.findById(ID)).thenReturn(Optional.of(leer));
		when(fotoService.getByLehrerIds(List.of(ID))).thenReturn(List.of());

		final var result = service.getById(ID);

		assertThat(result)
				.extracting("personalTyp", "anrede", "titel", "amtsbezeichnung", "vorname", "geschlecht", "foto", "idStaatsangehoerigkeit",
						"leitungsfunktionen")
				.containsExactly("", "", "", "", "", LehrerStammdatenMapper.ID_GESCHLECHT_NICHT_GESETZT, null, null, List.of());
	}

	@Test
	@DisplayName("getById | Lehrkraft nicht gefunden -> NOT_FOUND")
	void getById_notFound() {
		when(lehrerRepository.findById(ID)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.getById(ID))
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Keine Lehrkraft mit der ID 1 gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("getList | leere oder fehlende ID-Liste liefert eine leere Liste ohne Datenbankzugriff")
	void getList_leer() {
		assertThat(service.getList(null)).isEmpty();
		assertThat(service.getList(List.of())).isEmpty();
		verifyNoInteractions(lehrerRepository, fotoService);
	}

	@Test
	@DisplayName("getList | liefert die Stammdaten mit Fotos")
	void getList() {
		when(lehrerRepository.findListByIds(List.of(ID))).thenReturn(List.of(entity));
		when(fotoService.getByLehrerIds(List.of(ID))).thenReturn(List.of(new LehrerFoto(ID, "base64")));

		assertThat(service.getList(List.of(ID))).hasSize(1).allSatisfy(d -> assertThat(d.foto).isEqualTo("base64"));
	}

	@Test
	@DisplayName("getListOhneFotos | leere oder fehlende ID-Liste liefert eine leere Liste")
	void getListOhneFotos_leer() {
		assertThat(service.getListOhneFotos(null)).isEmpty();
		assertThat(service.getListOhneFotos(List.of())).isEmpty();
		verifyNoInteractions(lehrerRepository, fotoService);
	}

	@Test
	@DisplayName("getListOhneFotos | liefert die Stammdaten ohne Fotos")
	void getListOhneFotos() {
		when(lehrerRepository.findListByIds(List.of(ID))).thenReturn(List.of(entity));

		assertThat(service.getListOhneFotos(List.of(ID))).hasSize(1).allSatisfy(d -> assertThat(d.foto).isNull());
		verifyNoInteractions(fotoService);
	}

	@Test
	@DisplayName("getAll | liefert alle Stammdaten mit Fotos, bei leerem Bestand eine leere Liste")
	void getAll() {
		when(lehrerRepository.getAll()).thenReturn(List.of(entity)).thenReturn(List.of());
		when(fotoService.getByLehrerIds(List.of(ID))).thenReturn(List.of(new LehrerFoto(ID, "base64")));

		assertThat(service.getAll()).hasSize(1).allSatisfy(d -> assertThat(d.foto).isEqualTo("base64"));
		assertThat(service.getAll()).isEmpty();
	}

	@Test
	@DisplayName("getAllOhneFotos | liefert alle Stammdaten ohne Fotos")
	void getAllOhneFotos() {
		when(lehrerRepository.getAll()).thenReturn(List.of(entity));

		assertThat(service.getAllOhneFotos()).hasSize(1).allSatisfy(d -> assertThat(d.foto).isNull());
		verifyNoInteractions(fotoService);
	}

	// -------------------------------------------------------------------------
	// Anlegen
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("create | legt die Lehrkraft an und initialisiert Lernplattformen und Einwilligungen über die Services")
	void create() {
		stubCreate();
		final var request = createRequest();
		request.anrede = "Frau";
		request.idStaatsangehoerigkeit = NATIONALITAET_DZA;
		request.istSichtbar = false;

		final var result = service.create(request);

		assertThat(result)
				.extracting("id", "kuerzel", "nachname", "vorname", "personalTyp", "geschlecht", "anrede", "istSichtbar", "idStaatsangehoerigkeit")
				.containsExactly(ID_NEU, "NEU", "Neumann", "Nora", "LEHRKRAFT", GESCHLECHT_M, "Frau", false, NATIONALITAET_DZA);
		verify(lernplattformService).addForLehrer(ID_NEU);
		verify(einwilligungService).addForLehrer(ID_NEU);
	}

	@Test
	@DisplayName("create | Wohnort und passender Ortsteil werden übernommen")
	void create_wohnortUndOrtsteil() {
		stubCreate();
		when(ortRepository.existsById(ID_ORT)).thenReturn(true);
		when(ortsteilRepository.findById(ID_ORTSTEIL)).thenReturn(Optional.of(ortsteil(ID_ORTSTEIL, ID_ORT)));
		final var request = createRequest();
		request.wohnortID = ID_ORT;
		request.ortsteilID = ID_ORTSTEIL;

		final var result = service.create(request);

		assertThat(result).extracting("wohnortID", "ortsteilID").containsExactly(ID_ORT, ID_ORTSTEIL);
	}

	@Test
	@DisplayName("create | ohne Wohnort und Ortsteil wird kein Ort geprüft")
	void create_ohneWohnort() {
		stubCreate();

		final var result = service.create(createRequest());

		assertThat(result).extracting("wohnortID", "ortsteilID").containsExactly(null, null);
		verifyNoInteractions(ortRepository, ortsteilRepository);
	}

	@Test
	@DisplayName("create | Kürzel bereits vorhanden -> BAD_REQUEST")
	void create_kuerzelVorhanden() {
		when(lehrerRepository.existsByKuerzel("NEU")).thenReturn(true);
		final var request = createRequest();

		assertThatThrownBy(() -> service.create(request))
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Das Kuerzel NEU ist bereits vorhanden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
		verify(lehrerRepository, never()).create(any(DTOLehrer.class));
		verifyNoInteractions(lernplattformService, einwilligungService);
	}

	@Test
	@DisplayName("create | ungültiger Personal-Typ -> CONFLICT")
	void create_personalTypUngueltig() {
		final var request = createRequest();
		request.personalTyp = "GIBTSNICHT";

		assertThatThrownBy(() -> service.create(request))
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Ein PersonalTyp mit dem Kuerzel GIBTSNICHT wurde nicht gefunden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.CONFLICT);
	}

	@Test
	@DisplayName("create | ungültiges Geschlecht -> CONFLICT")
	void create_geschlechtUngueltig() {
		final var request = createRequest();
		request.geschlecht = GESCHLECHT_UNGUELTIG;

		assertThatThrownBy(() -> service.create(request))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.CONFLICT);
	}

	@Test
	@DisplayName("create | ungültige Staatsangehörigkeit -> NOT_FOUND")
	void create_staatsangehoerigkeitUngueltig() {
		final var request = createRequest();
		request.idStaatsangehoerigkeit = NATIONALITAET_UNGUELTIG;

		assertThatThrownBy(() -> service.create(request))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("create | nicht vorhandener Wohnort -> CONFLICT")
	void create_wohnortUngueltig() {
		when(ortRepository.existsById(ID_ORT)).thenReturn(false);
		final var request = createRequest();
		request.wohnortID = ID_ORT;

		assertThatThrownBy(() -> service.create(request))
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("WohnortID 100 ungültig.")
				.hasFieldOrPropertyWithValue("status", Response.Status.CONFLICT);
	}

	// -------------------------------------------------------------------------
	// Patch
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("patch | Lehrkraft nicht gefunden -> NOT_FOUND")
	void patch_notFound() {
		when(lehrerRepository.findById(ID)).thenReturn(Optional.empty());
		final var request = new LehrerStammdatenPatchRequest();

		assertThatThrownBy(() -> service.patch(ID, request))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
	}

	@Test
	@DisplayName("patch | setzt nur die gesetzten Felder, auch explizites null")
	void patch_felder() {
		entity.Anrede = "Herr";
		entity.Titel = "Dr.";
		when(lehrerRepository.findById(ID)).thenReturn(Optional.of(entity));
		final var request = new LehrerStammdatenPatchRequest();
		request.nachname = JsonNullable.of("Musterfrau");
		request.titel = JsonNullable.of(null);
		request.personalTyp = JsonNullable.of("SEKRETARIAT");
		request.geschlecht = JsonNullable.of(4);
		request.idStaatsangehoerigkeit = JsonNullable.of(NATIONALITAET_DZA);
		request.istSichtbar = JsonNullable.of(false);

		final var result = service.patch(ID, request);

		assertThat(result)
				.extracting("nachname", "titel", "anrede", "personalTyp", "geschlecht", "istSichtbar", "kuerzel")
				.containsExactly("Musterfrau", "", "Herr", "SEKRETARIAT", 4, false, "MUS");
		assertThat(entity)
				.returns(null, e -> e.Titel)
				.satisfies(e -> assertThat(e.staatsangehoerigkeit).isNotNull());
	}

	@Test
	@DisplayName("patch | Kürzel wird geprüft und gesetzt")
	void patch_kuerzel() {
		when(lehrerRepository.findById(ID)).thenReturn(Optional.of(entity));
		final var request = new LehrerStammdatenPatchRequest();
		request.kuerzel = JsonNullable.of("NEU");

		assertThat(service.patch(ID, request).kuerzel).isEqualTo("NEU");
		verify(lehrerRepository).existsByKuerzelExcludingId("NEU", ID);
	}

	@Test
	@DisplayName("patch | Kürzel bereits von anderer Lehrkraft verwendet -> BAD_REQUEST")
	void patch_kuerzelVorhanden() {
		when(lehrerRepository.findById(ID)).thenReturn(Optional.of(entity));
		when(lehrerRepository.existsByKuerzelExcludingId("NEU", ID)).thenReturn(true);
		final var request = new LehrerStammdatenPatchRequest();
		request.kuerzel = JsonNullable.of("NEU");

		assertThatThrownBy(() -> service.patch(ID, request))
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("Das Kuerzel NEU ist bereits vorhanden.")
				.hasFieldOrPropertyWithValue("status", Response.Status.BAD_REQUEST);
	}

	@Test
	@DisplayName("patch | ungültiger Personal-Typ -> CONFLICT")
	void patch_personalTypUngueltig() {
		when(lehrerRepository.findById(ID)).thenReturn(Optional.of(entity));
		final var request = new LehrerStammdatenPatchRequest();
		request.personalTyp = JsonNullable.of("GIBTSNICHT");

		assertThatThrownBy(() -> service.patch(ID, request))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.CONFLICT);
	}

	@Test
	@DisplayName("patch | ungültiges Geschlecht -> CONFLICT")
	void patch_geschlechtUngueltig() {
		when(lehrerRepository.findById(ID)).thenReturn(Optional.of(entity));
		final var request = new LehrerStammdatenPatchRequest();
		request.geschlecht = JsonNullable.of(GESCHLECHT_UNGUELTIG);

		assertThatThrownBy(() -> service.patch(ID, request))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.CONFLICT);
	}

	@Test
	@DisplayName("patch | ungültige Staatsangehörigkeit -> NOT_FOUND, null entfernt sie")
	void patch_staatsangehoerigkeit() {
		when(lehrerRepository.findById(ID)).thenReturn(Optional.of(entity));
		final var ungueltig = new LehrerStammdatenPatchRequest();
		ungueltig.idStaatsangehoerigkeit = JsonNullable.of(NATIONALITAET_UNGUELTIG);
		final var entfernen = new LehrerStammdatenPatchRequest();
		entfernen.idStaatsangehoerigkeit = JsonNullable.of(null);

		assertThatThrownBy(() -> service.patch(ID, ungueltig))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Response.Status.NOT_FOUND);
		assertThat(service.patch(ID, entfernen).idStaatsangehoerigkeit).isNull();
	}

	@Test
	@DisplayName("patch | Foto wird an den LehrerFotoService delegiert")
	void patch_foto() {
		when(lehrerRepository.findById(ID)).thenReturn(Optional.of(entity));
		final var request = new LehrerStammdatenPatchRequest();
		request.foto = JsonNullable.of("neuesFoto");

		service.patch(ID, request);

		verify(fotoService).upsertOrDelete(ID, "neuesFoto");
	}

	@Test
	@DisplayName("patch | ohne Foto im Request wird der LehrerFotoService nicht zum Schreiben genutzt")
	void patch_ohneFoto() {
		when(lehrerRepository.findById(ID)).thenReturn(Optional.of(entity));

		service.patch(ID, new LehrerStammdatenPatchRequest());

		verify(fotoService, never()).upsertOrDelete(anyLong(), any());
	}

	// -------------------------------------------------------------------------
	// Patch: Wohnort und Ortsteil
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("patch | ohne Wohnort und Ortsteil bleiben beide unverändert und es wird kein Ort geprüft")
	void patch_wohnortUnveraendert() {
		entity.Ort_ID = ID_ORT;
		entity.Ortsteil_ID = ID_ORTSTEIL;
		when(lehrerRepository.findById(ID)).thenReturn(Optional.of(entity));

		final var result = service.patch(ID, new LehrerStammdatenPatchRequest());

		assertThat(result).extracting("wohnortID", "ortsteilID").containsExactly(ID_ORT, ID_ORTSTEIL);
		verifyNoInteractions(ortRepository, ortsteilRepository);
	}

	@Test
	@DisplayName("patch | Wohnort und passender Ortsteil werden gesetzt")
	void patch_wohnortUndOrtsteil() {
		when(lehrerRepository.findById(ID)).thenReturn(Optional.of(entity));
		when(ortRepository.existsById(ID_ORT)).thenReturn(true);
		when(ortsteilRepository.findById(ID_ORTSTEIL)).thenReturn(Optional.of(ortsteil(ID_ORTSTEIL, ID_ORT)));
		final var request = new LehrerStammdatenPatchRequest();
		request.wohnortID = JsonNullable.of(ID_ORT);
		request.ortsteilID = JsonNullable.of(ID_ORTSTEIL);

		assertThat(service.patch(ID, request)).extracting("wohnortID", "ortsteilID").containsExactly(ID_ORT, ID_ORTSTEIL);
	}

	@Test
	@DisplayName("patch | nur Ortsteil: der Wohnort der Lehrkraft wird verwendet")
	void patch_nurOrtsteil() {
		entity.Ort_ID = ID_ORT;
		when(lehrerRepository.findById(ID)).thenReturn(Optional.of(entity));
		when(ortRepository.existsById(ID_ORT)).thenReturn(true);
		when(ortsteilRepository.findById(ID_ORTSTEIL)).thenReturn(Optional.of(ortsteil(ID_ORTSTEIL, ID_ORT)));
		final var request = new LehrerStammdatenPatchRequest();
		request.ortsteilID = JsonNullable.of(ID_ORTSTEIL);

		assertThat(service.patch(ID, request)).extracting("wohnortID", "ortsteilID").containsExactly(ID_ORT, ID_ORTSTEIL);
	}

	@Test
	@DisplayName("patch | nur Wohnort: ein nicht passender Ortsteil der Lehrkraft wird stillschweigend entfernt")
	void patch_nurWohnortOrtsteilPasstNicht() {
		entity.Ort_ID = ID_ANDERER_ORT;
		entity.Ortsteil_ID = ID_ORTSTEIL;
		when(lehrerRepository.findById(ID)).thenReturn(Optional.of(entity));
		when(ortRepository.existsById(ID_ORT)).thenReturn(true);
		when(ortsteilRepository.findById(ID_ORTSTEIL)).thenReturn(Optional.of(ortsteil(ID_ORTSTEIL, ID_ANDERER_ORT)));
		final var request = new LehrerStammdatenPatchRequest();
		request.wohnortID = JsonNullable.of(ID_ORT);

		assertThat(service.patch(ID, request)).extracting("wohnortID", "ortsteilID").containsExactly(ID_ORT, null);
	}

	@Test
	@DisplayName("patch | nur Wohnort: ein passender Ortsteil der Lehrkraft bleibt erhalten")
	void patch_nurWohnortOrtsteilPasst() {
		entity.Ort_ID = ID_ORT;
		entity.Ortsteil_ID = ID_ORTSTEIL;
		when(lehrerRepository.findById(ID)).thenReturn(Optional.of(entity));
		when(ortRepository.existsById(ID_ORT)).thenReturn(true);
		when(ortsteilRepository.findById(ID_ORTSTEIL)).thenReturn(Optional.of(ortsteil(ID_ORTSTEIL, ID_ORT)));
		final var request = new LehrerStammdatenPatchRequest();
		request.wohnortID = JsonNullable.of(ID_ORT);

		assertThat(service.patch(ID, request)).extracting("wohnortID", "ortsteilID").containsExactly(ID_ORT, ID_ORTSTEIL);
	}

	@Test
	@DisplayName("patch | nicht vorhandener Ortsteil wird stillschweigend entfernt")
	void patch_ortsteilNichtVorhanden() {
		entity.Ort_ID = ID_ORT;
		when(lehrerRepository.findById(ID)).thenReturn(Optional.of(entity));
		when(ortRepository.existsById(ID_ORT)).thenReturn(true);
		when(ortsteilRepository.findById(ID_ORTSTEIL)).thenReturn(Optional.empty());
		final var request = new LehrerStammdatenPatchRequest();
		request.ortsteilID = JsonNullable.of(ID_ORTSTEIL);

		assertThat(service.patch(ID, request)).extracting("wohnortID", "ortsteilID").containsExactly(ID_ORT, null);
	}

	@Test
	@DisplayName("patch | Wohnort null entfernt Wohnort und Ortsteil")
	void patch_wohnortNull() {
		entity.Ort_ID = ID_ORT;
		entity.Ortsteil_ID = ID_ORTSTEIL;
		when(lehrerRepository.findById(ID)).thenReturn(Optional.of(entity));
		final var request = new LehrerStammdatenPatchRequest();
		request.wohnortID = JsonNullable.of(null);

		assertThat(service.patch(ID, request)).extracting("wohnortID", "ortsteilID").containsExactly(null, null);
	}

	@Test
	@DisplayName("patch | nicht vorhandener Wohnort -> CONFLICT")
	void patch_wohnortNichtVorhanden() {
		when(lehrerRepository.findById(ID)).thenReturn(Optional.of(entity));
		when(ortRepository.existsById(ID_ORT)).thenReturn(false);
		final var request = new LehrerStammdatenPatchRequest();
		request.wohnortID = JsonNullable.of(ID_ORT);

		assertThatThrownBy(() -> service.patch(ID, request))
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("WohnortID 100 ungültig.")
				.hasFieldOrPropertyWithValue("status", Response.Status.CONFLICT);
	}

	@Test
	@DisplayName("patch | negativer Wohnort -> CONFLICT ohne Datenbankabfrage")
	void patch_wohnortNegativ() {
		when(lehrerRepository.findById(ID)).thenReturn(Optional.of(entity));
		final var request = new LehrerStammdatenPatchRequest();
		request.wohnortID = JsonNullable.of(-1L);

		assertThatThrownBy(() -> service.patch(ID, request))
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("WohnortID -1 ungültig.")
				.hasFieldOrPropertyWithValue("status", Response.Status.CONFLICT);
		verifyNoInteractions(ortRepository);
	}

	@Test
	@DisplayName("patch | negativer Ortsteil -> CONFLICT")
	void patch_ortsteilNegativ() {
		when(lehrerRepository.findById(ID)).thenReturn(Optional.of(entity));
		final var request = new LehrerStammdatenPatchRequest();
		request.ortsteilID = JsonNullable.of(-1L);

		assertThatThrownBy(() -> service.patch(ID, request))
				.isInstanceOf(ApiOperationException.class)
				.hasMessage("OrtsteilID -1 ungültig.")
				.hasFieldOrPropertyWithValue("status", Response.Status.CONFLICT);
	}

}
