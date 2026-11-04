package de.svws_nrw.service.enm;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Supplier;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockedStatic;

import jakarta.ws.rs.core.Response.Status;

import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.enm.v2.ENMv2Daten;
import de.svws_nrw.core.data.enm.v2.ENMv2Leistung;
import de.svws_nrw.core.data.enm.v2.ENMv2Schueler;
import de.svws_nrw.core.data.enm.v2.ENMv2SchuelerAnkreuzkompetenz;
import de.svws_nrw.data.TransactionSupport;
import de.svws_nrw.db.dto.current.schild.grundschule.DTOSchuelerAnkreuzfloskeln;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.db.dto.current.svws.timestamps.DTOTimestampsSchuelerAnkreuzkompetenzen;
import de.svws_nrw.db.dto.current.svws.timestamps.DTOTimestampsSchuelerLernabschnittsdaten;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.enm.NotenmodulCredentialsRepository;
import de.svws_nrw.repo.enm.NotenmodulCredentialsTimestampsRepository;
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
import de.svws_nrw.repo.schule.EigeneSchuleRepository;

/**
 * Tests für den Import von ENM-Daten (Version 2) im {@link EnmV2ImportService}. Die Repositories werden gemockt.
 */
@DisplayName("EnmV2ImportService")
class EnmV2ImportServiceTest {

	private static final long ID_SCHUELER = 1L;
	private static final long ID_LERNABSCHNITT = 100L;
	private static final long ID_ANKREUZKOMPETENZ = 400L;

	/** Der (ältere) Zeitstempel in der Datenbank in UTC */
	private static final String TS_DB_UTC = "2025-01-01 10:00:00.000";

	/** Ein (neuerer) Zeitstempel in den importierten ENM-Daten in lokaler Zeit */
	private static final String TS_ENM_NEU = "2025-06-01 12:00:00.000";

	private MockedStatic<TransactionSupport> transactions;

	private EigeneSchuleRepository eigeneSchuleRepository;
	private SchuelerRepository schuelerRepository;
	private SchuelerLernabschnittRepository schuelerLernabschnittRepository;
	private SchuelerLernabschnittTimestampRepository schuelerLernabschnittTimestampRepository;
	private SchuelerAnkreuzkompetenzRepository schuelerAnkreuzkompetenzRepository;
	private SchuelerAnkreuzkompetenzTimestampRepository schuelerAnkreuzkompetenzTimestampRepository;

	private DTOSchuelerAnkreuzfloskeln dtoAnkreuzkompetenz;

	private EnmV2ImportService service;


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
		transactions.when(() -> TransactionSupport.transactional(ArgumentMatchers.<Runnable>any()))
				.thenAnswer(inv -> {
					inv.getArgument(0, Runnable.class).run();
					return null;
				});

		eigeneSchuleRepository = mock(EigeneSchuleRepository.class);
		schuelerRepository = mock(SchuelerRepository.class);
		schuelerLernabschnittRepository = mock(SchuelerLernabschnittRepository.class);
		schuelerLernabschnittTimestampRepository = mock(SchuelerLernabschnittTimestampRepository.class);
		schuelerAnkreuzkompetenzRepository = mock(SchuelerAnkreuzkompetenzRepository.class);
		schuelerAnkreuzkompetenzTimestampRepository = mock(SchuelerAnkreuzkompetenzTimestampRepository.class);

		service = new EnmV2ImportService(
				eigeneSchuleRepository,
				mock(LehrerRepository.class),
				schuelerRepository,
				schuelerLernabschnittRepository,
				schuelerLernabschnittTimestampRepository,
				mock(SchuelerLernabschnittBemerkungRepository.class),
				mock(SchuelerLernabschnittKursartZuweisungenRepository.class),
				mock(SchuelerLernabschnittKursartZuweisungenTimestampsRepository.class),
				mock(SchuelerLeistungsdatenRepository.class),
				mock(SchuelerLeistungsdatenTimestampsRepository.class),
				mock(SchuelerTeilleistungRepository.class),
				mock(SchuelerTeilleistungTimestampRepository.class),
				schuelerAnkreuzkompetenzRepository,
				schuelerAnkreuzkompetenzTimestampRepository,
				mock(NotenmodulCredentialsRepository.class),
				mock(NotenmodulCredentialsTimestampsRepository.class));

		stubGrunddaten();
	}

	@AfterEach
	void tearDown() {
		transactions.close();
	}


	// -------------------------------------------------------------------------
	// Fixture: ein Schüler mit Lernabschnitt und einer Ankreuzkompetenz (alle Stufen in der DB: nur Stufe 1)
	// -------------------------------------------------------------------------

	private void stubGrunddaten() {
		final DTOEigeneSchule schule = new DTOEigeneSchule(1L, 0L, 0L, false, 0L, false, false, false, 0L, 0L);
		schule.SchulformKuerzel = "GY";
		when(eigeneSchuleRepository.getFirst()).thenReturn(schule);

		when(schuelerRepository.findMapByIds(ArgumentMatchers.anyCollection()))
				.thenReturn(Map.of(ID_SCHUELER, new DTOSchueler(ID_SCHUELER, "guid", false)));
		when(schuelerLernabschnittRepository.findMapByIds(ArgumentMatchers.anyCollection()))
				.thenReturn(Map.of(ID_LERNABSCHNITT, new DTOSchuelerLernabschnittsdaten(ID_LERNABSCHNITT, ID_SCHUELER, 1L, false, false, 0L)));
		when(schuelerLernabschnittTimestampRepository.findMapByIds(ArgumentMatchers.anyCollection()))
				.thenReturn(Map.of(ID_LERNABSCHNITT, new DTOTimestampsSchuelerLernabschnittsdaten(ID_LERNABSCHNITT,
						TS_DB_UTC, TS_DB_UTC, TS_DB_UTC, TS_DB_UTC, TS_DB_UTC, TS_DB_UTC, TS_DB_UTC, TS_DB_UTC, TS_DB_UTC)));

		dtoAnkreuzkompetenz = new DTOSchuelerAnkreuzfloskeln(ID_ANKREUZKOMPETENZ, ID_LERNABSCHNITT, 300L);
		dtoAnkreuzkompetenz.Stufe1 = true;
		dtoAnkreuzkompetenz.Stufe2 = false;
		dtoAnkreuzkompetenz.Stufe3 = false;
		dtoAnkreuzkompetenz.Stufe4 = false;
		dtoAnkreuzkompetenz.Stufe5 = false;
		when(schuelerAnkreuzkompetenzRepository.findMapByIds(ArgumentMatchers.anyCollection()))
				.thenReturn(Map.of(ID_ANKREUZKOMPETENZ, dtoAnkreuzkompetenz));
		when(schuelerAnkreuzkompetenzTimestampRepository.findMapByIds(ArgumentMatchers.anyCollection()))
				.thenReturn(Map.of(ID_ANKREUZKOMPETENZ, new DTOTimestampsSchuelerAnkreuzkompetenzen(ID_ANKREUZKOMPETENZ, TS_DB_UTC)));
	}

	/**
	 * Erstellt ENM-Daten mit einem Schüler, der eine Ankreuzkompetenz mit den angegebenen Stufen und einem neueren Zeitstempel besitzt.
	 *
	 * @param stufen   die Stufen der Ankreuzkompetenz (kann null sein)
	 *
	 * @return die ENM-Daten
	 */
	private static ENMv2Daten enmDaten(final boolean[] stufen) {
		final ENMv2SchuelerAnkreuzkompetenz kompetenz = new ENMv2SchuelerAnkreuzkompetenz();
		kompetenz.id = ID_ANKREUZKOMPETENZ;
		kompetenz.kompetenzID = 300L;
		kompetenz.stufen = stufen;
		kompetenz.tsStufe = TS_ENM_NEU;
		final ENMv2Schueler schueler = new ENMv2Schueler();
		schueler.id = ID_SCHUELER;
		schueler.lernabschnitt.id = ID_LERNABSCHNITT;
		schueler.ankreuzkompetenzen.add(kompetenz);
		final ENMv2Daten daten = new ENMv2Daten();
		daten.schueler.add(schueler);
		return daten;
	}


	// -------------------------------------------------------------------------
	// Eingabevalidierung: Stufen der Ankreuzkompetenz
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("Ein Eintrag mit genau 5 Stufen und neuerem Zeitstempel wird übernommen")
	void stufenKorrekt() {
		service.applyLatest(enmDaten(new boolean[] { false, true, false, false, true }));

		assertThat(dtoAnkreuzkompetenz.Stufe1).isFalse();
		assertThat(dtoAnkreuzkompetenz.Stufe2).isTrue();
		assertThat(dtoAnkreuzkompetenz.Stufe5).isTrue();
	}

	@Test
	@DisplayName("Ein Eintrag mit zu wenigen Stufen wird übersprungen, ohne dass der Import abbricht")
	void stufenZuWenige() {
		service.applyLatest(enmDaten(new boolean[] { false, true, false }));

		assertThat(dtoAnkreuzkompetenz.Stufe1).isTrue();
		assertThat(dtoAnkreuzkompetenz.Stufe2).isFalse();
	}

	@Test
	@DisplayName("Ein Eintrag mit zu vielen Stufen wird übersprungen, ohne dass der Import abbricht")
	void stufenZuViele() {
		service.applyLatest(enmDaten(new boolean[] { false, true, false, false, true, true }));

		assertThat(dtoAnkreuzkompetenz.Stufe1).isTrue();
		assertThat(dtoAnkreuzkompetenz.Stufe2).isFalse();
	}

	@Test
	@DisplayName("Ein Eintrag ohne Stufen (null) wird übersprungen, ohne dass der Import abbricht")
	void stufenNull() {
		service.applyLatest(enmDaten(null));

		assertThat(dtoAnkreuzkompetenz.Stufe1).isTrue();
		assertThat(dtoAnkreuzkompetenz.Stufe2).isFalse();
	}


	// -------------------------------------------------------------------------
	// Eingabevalidierung: Struktur der importierten ENM-Daten (Pflichtfelder dürfen nicht null sein)
	// -------------------------------------------------------------------------

	/**
	 * Prüft, dass die ENM-Daten mit BAD_REQUEST abgelehnt werden und dass dabei nichts aus der Datenbank geladen wurde.
	 *
	 * @param daten   die zu importierenden ENM-Daten
	 * @param teil    ein Teil der erwarteten Fehlermeldung
	 */
	private void pruefeAbgelehnt(final ENMv2Daten daten, final String teil) {
		assertThatThrownBy(() -> service.applyLatest(daten))
				.isInstanceOf(ApiOperationException.class)
				.hasFieldOrPropertyWithValue("status", Status.BAD_REQUEST)
				.hasMessageContaining(teil);
		verify(schuelerRepository, never()).findMapByIds(anyCollection());
	}

	@Test
	@DisplayName("Ohne Liste der Lehrer (null) werden die ENM-Daten abgelehnt")
	void strukturLehrerNull() {
		final ENMv2Daten daten = enmDaten(new boolean[5]);
		daten.lehrer = null;

		pruefeAbgelehnt(daten, "Lehrer");
	}

	@Test
	@DisplayName("Eine Liste der Lehrer mit einem leeren Eintrag (null) wird abgelehnt")
	void strukturLehrerEintragNull() {
		final ENMv2Daten daten = enmDaten(new boolean[5]);
		daten.lehrer.add(null);

		pruefeAbgelehnt(daten, "Lehrer");
	}

	@Test
	@DisplayName("Ohne ENM-Daten (null) wird der Import abgelehnt")
	void strukturDatenNull() {
		pruefeAbgelehnt(null, "ENM-Daten");
	}

	@Test
	@DisplayName("Ohne Liste der Schüler (null) werden die ENM-Daten abgelehnt")
	void strukturSchuelerNull() {
		final ENMv2Daten daten = enmDaten(new boolean[5]);
		daten.schueler = null;

		pruefeAbgelehnt(daten, "Schüler");
	}

	@Test
	@DisplayName("Ein leerer Eintrag (null) in der Liste der Schüler wird abgelehnt")
	void strukturSchuelerEintragNull() {
		final ENMv2Daten daten = enmDaten(new boolean[5]);
		daten.schueler = new ArrayList<>();
		daten.schueler.add(null);

		pruefeAbgelehnt(daten, "Schüler");
	}

	@Test
	@DisplayName("Ein Schüler ohne Lernabschnitt (null) wird mit Angabe der Schüler-ID abgelehnt")
	void strukturLernabschnittNull() {
		final ENMv2Daten daten = enmDaten(new boolean[5]);
		daten.schueler.getFirst().lernabschnitt = null;

		pruefeAbgelehnt(daten, "ID " + ID_SCHUELER);
	}

	@Test
	@DisplayName("Ein Schüler ohne Bemerkungen (null) wird mit Angabe der Schüler-ID abgelehnt")
	void strukturBemerkungenNull() {
		final ENMv2Daten daten = enmDaten(new boolean[5]);
		daten.schueler.getFirst().bemerkungen = null;

		pruefeAbgelehnt(daten, "ID " + ID_SCHUELER);
	}

	@Test
	@DisplayName("Ein Schüler ohne Liste der Ankreuzkompetenzen (null) wird mit Angabe der Schüler-ID abgelehnt")
	void strukturAnkreuzkompetenzenNull() {
		final ENMv2Daten daten = enmDaten(new boolean[5]);
		daten.schueler.getFirst().ankreuzkompetenzen = null;

		pruefeAbgelehnt(daten, "ID " + ID_SCHUELER);
	}

	@Test
	@DisplayName("Ein Schüler ohne Liste der Leistungsdaten (null) wird mit Angabe der Schüler-ID abgelehnt")
	void strukturLeistungsdatenNull() {
		final ENMv2Daten daten = enmDaten(new boolean[5]);
		daten.schueler.getFirst().leistungsdaten = null;

		pruefeAbgelehnt(daten, "ID " + ID_SCHUELER);
	}

	@Test
	@DisplayName("Eine Leistung ohne Liste der Teilleistungen (null) wird mit Angabe der Leistungs-ID abgelehnt")
	void strukturTeilleistungenNull() {
		final ENMv2Daten daten = enmDaten(new boolean[5]);
		final ENMv2Leistung leistung = new ENMv2Leistung();
		leistung.id = 200L;
		leistung.teilleistungen = null;
		daten.schueler.getFirst().leistungsdaten.add(leistung);

		pruefeAbgelehnt(daten, "ID 200");
	}

}
