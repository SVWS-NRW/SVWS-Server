package de.svws_nrw.service.enm;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockedStatic;

import de.svws_nrw.asd.types.Geschlecht;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.enm.v2.ENMv2Ankreuzkompetenz;
import de.svws_nrw.core.data.enm.v2.ENMv2Daten;
import de.svws_nrw.core.data.enm.v2.ENMv2Fach;
import de.svws_nrw.core.data.enm.v2.ENMv2Lerngruppe;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.dto.current.schild.grundschule.DTOAnkreuzkompetenz;
import de.svws_nrw.db.dto.current.schild.grundschule.DTOSchuelerAnkreuzfloskeln;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKurs;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLeistungsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.dto.current.svws.timestamps.DTOTimestampsSchuelerAnkreuzkompetenzen;
import de.svws_nrw.db.dto.current.svws.timestamps.DTOTimestampsSchuelerLeistungsdaten;
import de.svws_nrw.db.dto.current.svws.timestamps.DTOTimestampsSchuelerLernabschnittsdaten;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.enm.NotenmodulCredentialsTimestampsRepository;
import jakarta.ws.rs.core.Response.Status;
import de.svws_nrw.repo.klassen.KlassenRepository;
import de.svws_nrw.repo.klassen.KlassenleitungenRepository;
import de.svws_nrw.repo.kurse.KurseRepository;
import de.svws_nrw.repo.lehrer.LehrerRepository;
import de.svws_nrw.repo.schueler.SchuelerRepository;
import de.svws_nrw.repo.schueler.ankreuzkompetenz.SchuelerAnkreuzkompetenzRepository;
import de.svws_nrw.repo.schueler.ankreuzkompetenz.SchuelerAnkreuzkompetenzTimestampRepository;
import de.svws_nrw.repo.schueler.leistungsdaten.SchuelerLeistungsdatenRepository;
import de.svws_nrw.repo.schueler.leistungsdaten.SchuelerLeistungsdatenTimestampsRepository;
import de.svws_nrw.repo.schueler.lernabschnitt.SchuelerLernabschnittBemerkungRepository;
import de.svws_nrw.repo.schueler.lernabschnitt.SchuelerLernabschnittKursartZuweisungenRepository;
import de.svws_nrw.repo.schueler.lernabschnitt.SchuelerLernabschnittKursartZuweisungenTimestampsRepository;
import de.svws_nrw.repo.schueler.lernabschnitt.SchuelerLernabschnittRepository;
import de.svws_nrw.repo.schueler.lernabschnitt.SchuelerLernabschnittTimestampRepository;
import de.svws_nrw.repo.schueler.teilleistung.SchuelerTeilleistungRepository;
import de.svws_nrw.repo.schueler.teilleistung.SchuelerTeilleistungTimestampRepository;
import de.svws_nrw.repo.schueler.zp10.SchuelerZP10Repository;
import de.svws_nrw.repo.schueler.zp10.SchuelerZP10TimestampsRepository;
import de.svws_nrw.repo.schule.EigeneSchuleRepository;
import de.svws_nrw.repo.schule.SchuljahresabschnitteRepository;
import de.svws_nrw.repo.schule.kataloge.abteilung.AbteilungKlasseRepository;
import de.svws_nrw.repo.schule.kataloge.abteilung.AbteilungenRepository;
import de.svws_nrw.repo.schule.kataloge.ankreuzkompetenz.AnkreuzkompetenzJahrgangRepository;
import de.svws_nrw.repo.schule.kataloge.ankreuzkompetenz.AnkreuzkompetenzKonfigurationRepository;
import de.svws_nrw.repo.schule.kataloge.ankreuzkompetenz.AnkreuzkompetenzRepository;
import de.svws_nrw.repo.schule.kataloge.fach.FachRepository;
import de.svws_nrw.repo.schule.kataloge.floskel.FloskelJahrgangRepository;
import de.svws_nrw.repo.schule.kataloge.floskel.FloskelRepository;
import de.svws_nrw.repo.schule.kataloge.floskelgruppe.FloskelgruppeRepository;
import de.svws_nrw.repo.schule.kataloge.foerderschwerpunkt.FoerderschwerpunktRepository;
import de.svws_nrw.repo.schule.kataloge.jahrgang.JahrgangRepository;
import de.svws_nrw.repo.schule.kataloge.teilleistungsart.TeilleistungsartRepository;
import de.svws_nrw.repo.schule.schulleitung.SchulleitungRepository;

/**
 * Tests für die Aggregation der ENM-Daten (Version 2) im {@link EnmV2GetService}. Die Repositories werden gemockt;
 * nicht explizit konfigurierte Repository-Methoden liefern leere Collections.
 */
@DisplayName("EnmV2GetService")
class EnmV2GetServiceTest {

	/** Ein gültiger UTC-Zeitstempel für die Timestamp-DTOs (alle Spalten sind NOT NULL) */
	private static final String TS = "2025-01-01 10:00:00.000";

	private static final long ID_ABSCHNITT = 1L;
	private static final long ID_JAHRGANG = 5L;
	private static final long ID_KLASSE = 10L;
	private static final long ID_SCHUELER = 1L;
	private static final long ID_LERNABSCHNITT = 100L;
	private static final long ID_KURS = 20L;
	private static final long ID_FACH = 30L;
	private static final long ID_LEHRER = 40L;
	private static final long ID_LEISTUNG = 200L;

	private MockedStatic<TransactionSupport> transactions;

	private EigeneSchuleRepository eigeneSchuleRepository;
	private SchuljahresabschnitteRepository schuljahresabschnitteRepository;
	private LehrerRepository lehrerRepository;
	private FachRepository fachRepository;
	private SchuelerRepository schuelerRepository;
	private SchuelerLernabschnittRepository schuelerLernabschnittRepository;
	private SchuelerLernabschnittTimestampRepository schuelerLernabschnittTimestampRepository;
	private SchuelerLeistungsdatenRepository schuelerLeistungsdatenRepository;
	private SchuelerLeistungsdatenTimestampsRepository schuelerLeistungsdatenTimestampsRepository;
	private KurseRepository kurseRepository;
	private KlassenRepository klassenRepository;
	private JahrgangRepository jahrgangRepository;
	private DTOEigeneSchule dtoSchule;
	private DTOJahrgang dtoJahrgang;
	private DTOKlassen dtoKlasse;
	private DTOFach dtoFach;
	private SchuelerAnkreuzkompetenzRepository schuelerAnkreuzkompetenzRepository;
	private SchuelerAnkreuzkompetenzTimestampRepository schuelerAnkreuzkompetenzTimestampRepository;
	private AnkreuzkompetenzRepository ankreuzkompetenzRepository;

	private EnmV2GetService service;


	@BeforeAll
	static void initCoreTypes() {
		ASDCoreTypeUtils.initAll();
	}

	@BeforeEach
	void setUp() {
		// Die Transaktion wird durchgereicht, da keine Datenbank-Verbindung existiert
		transactions = mockStatic(TransactionSupport.class);
		transactions.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Supplier<Object>>any()))
				.thenAnswer(inv -> inv.getArgument(0, Supplier.class).get());

		eigeneSchuleRepository = mock(EigeneSchuleRepository.class);
		schuljahresabschnitteRepository = mock(SchuljahresabschnitteRepository.class);
		lehrerRepository = mock(LehrerRepository.class);
		fachRepository = mock(FachRepository.class);
		schuelerRepository = mock(SchuelerRepository.class);
		schuelerLernabschnittRepository = mock(SchuelerLernabschnittRepository.class);
		schuelerLernabschnittTimestampRepository = mock(SchuelerLernabschnittTimestampRepository.class);
		schuelerLeistungsdatenRepository = mock(SchuelerLeistungsdatenRepository.class);
		schuelerLeistungsdatenTimestampsRepository = mock(SchuelerLeistungsdatenTimestampsRepository.class);
		kurseRepository = mock(KurseRepository.class);
		klassenRepository = mock(KlassenRepository.class);
		jahrgangRepository = mock(JahrgangRepository.class);
		schuelerAnkreuzkompetenzRepository = mock(SchuelerAnkreuzkompetenzRepository.class);
		schuelerAnkreuzkompetenzTimestampRepository = mock(SchuelerAnkreuzkompetenzTimestampRepository.class);
		ankreuzkompetenzRepository = mock(AnkreuzkompetenzRepository.class);

		final EnmV2GetServiceKontext kontext = EnmV2GetServiceKontext.of(
				eigeneSchuleRepository,
				mock(SchulleitungRepository.class),
				schuljahresabschnitteRepository,
				mock(AbteilungenRepository.class),
				mock(AbteilungKlasseRepository.class),
				lehrerRepository,
				fachRepository,
				schuelerRepository,
				schuelerLernabschnittRepository,
				schuelerLernabschnittTimestampRepository,
				mock(SchuelerLernabschnittBemerkungRepository.class),
				mock(SchuelerLernabschnittKursartZuweisungenRepository.class),
				mock(SchuelerLernabschnittKursartZuweisungenTimestampsRepository.class),
				schuelerLeistungsdatenRepository,
				schuelerLeistungsdatenTimestampsRepository,
				mock(SchuelerTeilleistungRepository.class),
				mock(SchuelerTeilleistungTimestampRepository.class),
				schuelerAnkreuzkompetenzRepository,
				schuelerAnkreuzkompetenzTimestampRepository,
				mock(SchuelerZP10Repository.class),
				mock(SchuelerZP10TimestampsRepository.class),
				kurseRepository,
				klassenRepository,
				mock(KlassenleitungenRepository.class),
				jahrgangRepository,
				mock(FoerderschwerpunktRepository.class),
				mock(AnkreuzkompetenzKonfigurationRepository.class),
				ankreuzkompetenzRepository,
				mock(AnkreuzkompetenzJahrgangRepository.class),
				mock(FloskelRepository.class),
				mock(FloskelgruppeRepository.class),
				mock(FloskelJahrgangRepository.class),
				mock(TeilleistungsartRepository.class),
				mock(NotenmodulCredentialGeneratorService.class),
				mock(NotenmodulCredentialsTimestampsRepository.class));
		service = new EnmV2GetService(kontext);

		stubGrunddaten();
	}

	@AfterEach
	void tearDown() {
		transactions.close();
	}


	// -------------------------------------------------------------------------
	// Fixture: ein Schüler in einer Klasse mit einer Kurs-Leistung (GY, Schuljahr 2025/1)
	// -------------------------------------------------------------------------

	private void stubGrunddaten() {
		final DTOEigeneSchule schule = new DTOEigeneSchule(1L, 0L, 0L, false, 0L, false, false, false, 0L, 0L);
		dtoSchule = schule;
		schule.SchulNr = 123456;
		schule.SchulformKuerzel = "GY";
		schule.Schuljahresabschnitts_ID = ID_ABSCHNITT;
		schule.AnzahlAbschnitte = 2;
		when(eigeneSchuleRepository.getFirst()).thenReturn(schule);
		when(schuljahresabschnitteRepository.getById(any())).thenReturn(new DTOSchuljahresabschnitte(ID_ABSCHNITT, 2025, 1));

		final DTOJahrgang jahrgang = new DTOJahrgang(ID_JAHRGANG);
		dtoJahrgang = jahrgang;
		jahrgang.InternKrz = "05";
		jahrgang.ASDJahrgang = "05";
		jahrgang.ASDBezeichnung = "Jahrgang 5";
		jahrgang.Sekundarstufe = "SI";
		jahrgang.Sortierung = 1;
		when(jahrgangRepository.getMap()).thenReturn(Map.of(ID_JAHRGANG, jahrgang));

		final DTOKlassen klasse = new DTOKlassen(ID_KLASSE, ID_ABSCHNITT, "5a", false, false);
		dtoKlasse = klasse;
		klasse.ASDKlasse = "05a";
		klasse.Jahrgang_ID = ID_JAHRGANG;
		klasse.Sortierung = 1;
		when(klassenRepository.getMapBySchuljahresabschnitt(anyLong())).thenReturn(Map.of(ID_KLASSE, klasse));

		final DTOLehrer lehrer = new DTOLehrer(ID_LEHRER, "MUS", "Muster");
		lehrer.Geschlecht = Geschlecht.W;
		when(lehrerRepository.getMap()).thenReturn(Map.of(ID_LEHRER, lehrer));

		final DTOFach fach = new DTOFach(ID_FACH, false);
		dtoFach = fach;
		fach.Kuerzel = "M";
		fach.StatistikKuerzel = "M";
		fach.Bezeichnung = "Mathematik";
		fach.SortierungAllg = 1;
		fach.IstFremdsprache = false;
		when(fachRepository.getMap()).thenReturn(Map.of(ID_FACH, fach));

		final DTOSchueler schueler = new DTOSchueler(ID_SCHUELER, "guid", false);
		schueler.Nachname = "Schüler";
		schueler.Vorname = "Susi";
		schueler.Geschlecht = Geschlecht.W;
		schueler.Lernstandsbericht = false;
		when(schuelerRepository.getMapByStatusAndSchuljahresabschnitt(anyLong(), any())).thenReturn(Map.of(ID_SCHUELER, schueler));

		final DTOSchuelerLernabschnittsdaten lernabschnitt =
				new DTOSchuelerLernabschnittsdaten(ID_LERNABSCHNITT, ID_SCHUELER, ID_ABSCHNITT, false, false, 0L);
		lernabschnitt.Klassen_ID = ID_KLASSE;
		lernabschnitt.Jahrgang_ID = ID_JAHRGANG;
		lernabschnitt.ZieldifferentesLernen = false;
		when(schuelerLernabschnittRepository.getMapByLernabschnittIDAndSchuljahreabschnitt(any(), anyLong()))
				.thenReturn(Map.of(ID_LERNABSCHNITT, lernabschnitt));
		when(schuelerLernabschnittTimestampRepository.findMapByIds(any()))
				.thenReturn(Map.of(ID_LERNABSCHNITT,
						new DTOTimestampsSchuelerLernabschnittsdaten(ID_LERNABSCHNITT, TS, TS, TS, TS, TS, TS, TS, TS, TS)));
	}

	/**
	 * Fügt der Fixture einen Kurs mit der angegebenen Wochenstundenzahl und eine Leistung des Schülers in diesem Kurs hinzu.
	 *
	 * @param wochenstunden   die Wochenstunden des Kurses (kann null sein)
	 */
	private void stubKursMitLeistung(final Integer wochenstunden) {
		stubKursMitLeistung(wochenstunden, "GK");
	}

	/**
	 * Fügt der Fixture einen Kurs mit der angegebenen Wochenstundenzahl und eine Leistung des Schülers in diesem Kurs hinzu.
	 *
	 * @param wochenstunden   die Wochenstunden des Kurses (kann null sein)
	 * @param kursartAllg     die allgemeine Kursart in den Leistungsdaten (kann null sein)
	 */
	private void stubKursMitLeistung(final Integer wochenstunden, final String kursartAllg) {
		final DTOKurs kurs = new DTOKurs(ID_KURS, ID_ABSCHNITT, "M-GK1", ID_FACH);
		kurs.WochenStd = wochenstunden;
		when(kurseRepository.getMapBySchuljahresabschnitt(anyLong())).thenReturn(Map.of(ID_KURS, kurs));

		final DTOSchuelerLeistungsdaten leistung = new DTOSchuelerLeistungsdaten(ID_LEISTUNG, ID_LERNABSCHNITT, ID_FACH);
		leistung.Fachlehrer_ID = ID_LEHRER;
		leistung.Kurs_ID = ID_KURS;
		leistung.Kursart = "GK1";
		leistung.KursartAllg = kursartAllg;
		when(schuelerLeistungsdatenRepository.findListByLernabschnitt(any())).thenReturn(List.of(leistung));
		when(schuelerLeistungsdatenTimestampsRepository.findMapByIds(any()))
				.thenReturn(Map.of(ID_LEISTUNG, new DTOTimestampsSchuelerLeistungsdaten(ID_LEISTUNG, TS, TS, TS, TS, TS, TS)));
	}

	/**
	 * Fügt der Fixture eine Leistung des Schülers im Klassenunterricht (ohne Kurs) hinzu.
	 *
	 * @param kursartAllg   die allgemeine Kursart in den Leistungsdaten (kann null sein)
	 */
	private void stubKlassenunterrichtMitLeistung(final String kursartAllg) {
		final DTOSchuelerLeistungsdaten leistung = new DTOSchuelerLeistungsdaten(ID_LEISTUNG, ID_LERNABSCHNITT, ID_FACH);
		leistung.Fachlehrer_ID = ID_LEHRER;
		leistung.KursartAllg = kursartAllg;
		when(schuelerLeistungsdatenRepository.findListByLernabschnitt(any())).thenReturn(List.of(leistung));
		when(schuelerLeistungsdatenTimestampsRepository.findMapByIds(any()))
				.thenReturn(Map.of(ID_LEISTUNG, new DTOTimestampsSchuelerLeistungsdaten(ID_LEISTUNG, TS, TS, TS, TS, TS, TS)));
	}

	/**
	 * Fügt der Fixture eine Ankreuzkompetenz aus dem Katalog hinzu, die beim Schüler angekreuzt ist.
	 *
	 * @param sortierung   die Sortierung im Katalog (kann null sein)
	 */
	private void stubAnkreuzkompetenz(final Integer sortierung) {
		final long idKompetenz = 300L;
		final long idSchuelerKompetenz = 400L;
		final DTOAnkreuzkompetenz kompetenz = new DTOAnkreuzkompetenz(idKompetenz, 0, "kann rechnen");
		kompetenz.sortierung = sortierung;
		when(ankreuzkompetenzRepository.getMap()).thenReturn(Map.of(idKompetenz, kompetenz));

		final DTOSchuelerAnkreuzfloskeln schuelerKompetenz = new DTOSchuelerAnkreuzfloskeln(idSchuelerKompetenz, ID_LERNABSCHNITT, idKompetenz);
		schuelerKompetenz.Stufe1 = true;
		schuelerKompetenz.Stufe2 = false;
		schuelerKompetenz.Stufe3 = false;
		schuelerKompetenz.Stufe4 = false;
		schuelerKompetenz.Stufe5 = false;
		when(schuelerAnkreuzkompetenzRepository.findListByLernabschnitt(any())).thenReturn(List.of(schuelerKompetenz));
		when(schuelerAnkreuzkompetenzTimestampRepository.findMapByIds(any()))
				.thenReturn(Map.of(idSchuelerKompetenz, new DTOTimestampsSchuelerAnkreuzkompetenzen(idSchuelerKompetenz, TS)));
	}


	// -------------------------------------------------------------------------
	// Befund 1: Kurs ohne Wochenstunden
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("Ein Kurs ohne Wochenstunden (NULL in der DB) führt zu einer Lerngruppe mit 0 Wochenstunden")
	void kursOhneWochenstunden() {
		stubKursMitLeistung(null);

		final ENMv2Daten daten = service.get(null, null);

		assertThat(daten.lerngruppen).hasSize(1);
		final ENMv2Lerngruppe lerngruppe = daten.lerngruppen.getFirst();
		assertThat(lerngruppe.kID).isEqualTo(ID_KURS);
		assertThat(lerngruppe.wochenstunden).isZero();
	}

	@Test
	@DisplayName("Ein Kurs mit Wochenstunden übernimmt diese in die Lerngruppe")
	void kursMitWochenstunden() {
		stubKursMitLeistung(3);

		final ENMv2Daten daten = service.get(null, null);

		assertThat(daten.lerngruppen).hasSize(1);
		assertThat(daten.lerngruppen.getFirst().wochenstunden).isEqualTo(3);
	}


	// -------------------------------------------------------------------------
	// Befund 2: Ankreuzkompetenz ohne Sortierung im Katalog
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("Eine Ankreuzkompetenz ohne Sortierung (NULL in der DB) erhält die Standard-Sortierung 32000")
	void ankreuzkompetenzOhneSortierung() {
		stubAnkreuzkompetenz(null);

		final ENMv2Daten daten = service.get(null, null);

		assertThat(daten.ankreuzkompetenzen.kompetenzen).hasSize(1);
		final ENMv2Ankreuzkompetenz kompetenz = daten.ankreuzkompetenzen.kompetenzen.getFirst();
		assertThat(kompetenz.sortierung).isEqualTo(32000);
	}

	@Test
	@DisplayName("Eine Ankreuzkompetenz mit Sortierung übernimmt diese in die ENM-Daten")
	void ankreuzkompetenzMitSortierung() {
		stubAnkreuzkompetenz(7);

		final ENMv2Daten daten = service.get(null, null);

		assertThat(daten.ankreuzkompetenzen.kompetenzen).hasSize(1);
		assertThat(daten.ankreuzkompetenzen.kompetenzen.getFirst().sortierung).isEqualTo(7);
	}


	// -------------------------------------------------------------------------
	// Befund 3: Jahrgang, Klasse und Fach ohne Sortierung
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("Ein Jahrgang ohne Sortierung (NULL in der DB) erhält die Standard-Sortierung 32000")
	void jahrgangOhneSortierung() {
		dtoJahrgang.Sortierung = null;

		final ENMv2Daten daten = service.get(null, null);

		assertThat(daten.jahrgaenge).hasSize(1);
		assertThat(daten.jahrgaenge.getFirst().sortierung).isEqualTo(32000);
	}

	@Test
	@DisplayName("Eine Klasse ohne Sortierung (NULL in der DB) erhält die Standard-Sortierung 32000")
	void klasseOhneSortierung() {
		dtoKlasse.Sortierung = null;

		final ENMv2Daten daten = service.get(null, null);

		assertThat(daten.klassen).hasSize(1);
		assertThat(daten.klassen.getFirst().sortierung).isEqualTo(32000);
	}

	@Test
	@DisplayName("Ein Fach ohne Sortierung (NULL in der DB) erhält die Standard-Sortierung 32000")
	void fachOhneSortierung() {
		dtoFach.SortierungAllg = null;
		stubKursMitLeistung(3);

		final ENMv2Daten daten = service.get(null, null);

		assertThat(daten.faecher).hasSize(1);
		final ENMv2Fach fach = daten.faecher.getFirst();
		assertThat(fach.sortierung).isEqualTo(32000);
	}


	// -------------------------------------------------------------------------
	// Befund 4: unvollständige Schul-Konfiguration
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("Ohne aktuellen Schuljahresabschnitt der Schule wird ein Fehler mit Klartext geworfen")
	void schuleOhneSchuljahresabschnitt() {
		dtoSchule.Schuljahresabschnitts_ID = null;

		assertThatThrownBy(() -> service.get(null, null))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Status.INTERNAL_SERVER_ERROR)
				.hasMessageContaining("Schuljahresabschnitt");
	}

	@Test
	@DisplayName("Ohne Schulnummer der Schule wird ein Fehler mit Klartext geworfen")
	void schuleOhneSchulnummer() {
		dtoSchule.SchulNr = null;

		assertThatThrownBy(() -> service.get(null, null))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Status.INTERNAL_SERVER_ERROR)
				.hasMessageContaining("Schulnummer");
	}

	@Test
	@DisplayName("Ohne Schulform der Schule wird ein Fehler mit Klartext geworfen")
	void schuleOhneSchulform() {
		dtoSchule.SchulformKuerzel = null;

		assertThatThrownBy(() -> service.get(null, null))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Status.INTERNAL_SERVER_ERROR)
				.hasMessageContaining("Schulform");
	}

	@Test
	@DisplayName("Mit einem unbekannten Schulform-Kürzel wird ein Fehler mit Klartext geworfen")
	void schuleMitUnbekannterSchulform() {
		dtoSchule.SchulformKuerzel = "??";

		assertThatThrownBy(() -> service.get(null, null))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Status.INTERNAL_SERVER_ERROR)
				.hasMessageContaining("Schulform");
	}

	@Test
	@DisplayName("Ohne Anzahl der Abschnitte wird der Datenbank-Default 2 verwendet")
	void schuleOhneAnzahlAbschnitte() {
		dtoSchule.AnzahlAbschnitte = null;

		final ENMv2Daten daten = service.get(null, null);

		assertThat(daten.anzahlAbschnitte).isEqualTo(2);
	}

	@Test
	@DisplayName("Die Schule wird nur einmal aus der Datenbank gelesen")
	void schuleWirdEinmalGelesen() {
		service.get(null, null);

		verify(eigeneSchuleRepository, times(1)).getFirst();
	}


	// -------------------------------------------------------------------------
	// Befund 5: Klasse oder Kurs gehört nicht zum aktuellen Schuljahresabschnitt
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("Ein Lernabschnitt mit einer Klasse außerhalb des Schuljahresabschnitts wird übersprungen")
	void lernabschnittMitKlasseAusAnderemAbschnitt() {
		when(klassenRepository.getMapBySchuljahresabschnitt(anyLong())).thenReturn(Map.of());

		final ENMv2Daten daten = service.get(null, null);

		assertThat(daten.schueler).isEmpty();
		assertThat(daten.klassen).isEmpty();
	}

	@Test
	@DisplayName("Eine Leistung mit einem Kurs außerhalb des Schuljahresabschnitts wird übersprungen, der Rest des Schülers bleibt erhalten")
	void leistungMitKursAusAnderemAbschnitt() {
		stubKursMitLeistung(3);
		when(kurseRepository.getMapBySchuljahresabschnitt(anyLong())).thenReturn(Map.of());

		final ENMv2Daten daten = service.get(null, null);

		assertThat(daten.schueler).hasSize(1);
		assertThat(daten.schueler.getFirst().leistungsdaten).isEmpty();
		assertThat(daten.lerngruppen).isEmpty();
		assertThat(daten.faecher).isEmpty();
	}


	// -------------------------------------------------------------------------
	// Befund 7 (Logikfehler): Fallback für die allgemeine Kursart
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("Eine bekannte allgemeine Kursart wird unverändert übernommen")
	void kursartAllgBekannt() {
		stubKursMitLeistung(3, "GK");

		assertThat(service.get(null, null).lerngruppen.getFirst().kursartKuerzel).isEqualTo("GK");
	}

	@Test
	@DisplayName("Ein Kurs ohne allgemeine Kursart (NULL in der DB) erhält die Kursart PUT")
	void kursOhneKursartAllg() {
		stubKursMitLeistung(3, null);

		assertThat(service.get(null, null).lerngruppen.getFirst().kursartKuerzel).isEqualTo("PUT");
	}

	@Test
	@DisplayName("Ein Kurs mit unbekannter allgemeiner Kursart erhält die Kursart PUT")
	void kursMitUnbekannterKursartAllg() {
		stubKursMitLeistung(3, "XYZ");

		assertThat(service.get(null, null).lerngruppen.getFirst().kursartKuerzel).isEqualTo("PUT");
	}

	@Test
	@DisplayName("Klassenunterricht ohne allgemeine Kursart (NULL in der DB) erhält die Kursart PUK")
	void klassenunterrichtOhneKursartAllg() {
		stubKlassenunterrichtMitLeistung(null);

		assertThat(service.get(null, null).lerngruppen.getFirst().kursartKuerzel).isEqualTo("PUK");
	}

	@Test
	@DisplayName("Klassenunterricht mit unbekannter allgemeiner Kursart erhält die Kursart PUK")
	void klassenunterrichtMitUnbekannterKursartAllg() {
		stubKlassenunterrichtMitLeistung("XYZ");

		assertThat(service.get(null, null).lerngruppen.getFirst().kursartKuerzel).isEqualTo("PUK");
	}

}
