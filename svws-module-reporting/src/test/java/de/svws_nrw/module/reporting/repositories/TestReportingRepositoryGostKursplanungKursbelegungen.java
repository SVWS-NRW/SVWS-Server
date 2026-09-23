package de.svws_nrw.module.reporting.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import de.svws_nrw.asd.data.schueler.Schueler;
import de.svws_nrw.asd.types.Geschlecht;
import de.svws_nrw.asd.types.schueler.SchuelerStatus;
import de.svws_nrw.asd.utils.ASDCoreTypeUtils;
import de.svws_nrw.core.data.gost.GostBlockungKurs;
import de.svws_nrw.core.data.gost.GostBlockungKursLehrer;
import de.svws_nrw.core.data.gost.GostBlockungSchiene;
import de.svws_nrw.core.data.gost.GostBlockungsdaten;
import de.svws_nrw.core.data.gost.GostBlockungsergebnis;
import de.svws_nrw.core.data.gost.GostBlockungsergebnisKurs;
import de.svws_nrw.core.data.gost.GostBlockungsergebnisSchiene;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.data.gost.GostFachwahl;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.utils.DTOUtils;
import de.svws_nrw.core.utils.gost.GostBlockungsdatenManager;
import de.svws_nrw.core.utils.gost.GostFaecherManager;
import de.svws_nrw.data.gost.DataGostBlockungsdaten;
import de.svws_nrw.data.gost.DataGostBlockungsergebnisse;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.filterung.ReportingFilterService;
import de.svws_nrw.module.reporting.parameter.ReportingParameterTypisiert;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import de.svws_nrw.module.reporting.types.schueler.gost.kursplanung.ReportingSchuelerGostKursplanungKursbelegung;

/**
 * Prüft, dass ein gescheiterter Aufbau des Kursplanungs-Repositorys die Kursbelegungen der Schüler nicht vervielfacht. Die Kursbelegungen hängen an den
 * ReportingSchueler-Objekten des zentralen Schüler-Repositorys, und der Aufbau hängt sie an, statt sie zu ersetzen: Ein zweiter Aufbau nach einem Fehler
 * trüge sie ein weiteres Mal ein, und die Ausgabe zeigte jeden Kurs doppelt.
 */
@DisplayName("Die Kursbelegungen nach einem gescheiterten Aufbau der Kursplanung")
class TestReportingRepositoryGostKursplanungKursbelegungen {

	/** Die ID des Blockungsergebnisses, das die Reportparameter benennen. */
	private static final long ID_BLOCKUNGSERGEBNIS = 3L;

	/** Die ID der Blockung, die das Blockungsergebnis nennt. */
	private static final long ID_BLOCKUNG = 7L;

	/** Die ID der einzigen Schiene der Blockung. */
	private static final long ID_SCHIENE = 1L;

	/** Die ID des Faches, zu dem beide Kurse gehören. */
	private static final long ID_FACH = 1L;

	/** Die ID des Kurses, der die Kursbelegung des Schülers trägt. */
	private static final long ID_KURS_MIT_BELEGUNG = 11L;

	/** Die ID des Kurses, dessen Verarbeitung den Aufbau scheitern lässt. */
	private static final long ID_KURS_MIT_FEHLER = 12L;

	/** Die ID der Lehrkraft, deren Auflösung den Aufbau scheitern lässt. */
	private static final long ID_LEHRKRAFT = 99L;

	/** Die ID des Schülers, der den ersten Kurs belegt. */
	private static final long ID_SCHUELER = 100L;

	/** Das Abiturjahr der Blockung. */
	private static final int ABITURJAHR = 2025;

	/** Der gemockte Context, über den das Repository seine Umgebung erreicht. */
	private ReportingContext reportingContext;

	/** Die Belegungsliste des ReportingSchueler, an die das Repository seine Kursbelegungen hängt. */
	private List<ReportingSchuelerGostKursplanungKursbelegung> kursbelegungen;


	/** Geschlecht und Schülerstatus der Blockungsdaten stammen aus den Core-Types und verlangen deren Initialisierung. */
	@BeforeAll
	static void setUpCoreTypes() {
		ASDCoreTypeUtils.initAll();
	}


	@BeforeEach
	void setUp() {
		reportingContext = mock(ReportingContext.class);
		when(reportingContext.logger()).thenReturn(new Logger());
		final ReportingParameterTypisiert reportingParameter = mock(ReportingParameterTypisiert.class);
		when(reportingParameter.idHauptdatenObjekt()).thenReturn(ID_BLOCKUNGSERGEBNIS);
		when(reportingContext.reportingParameter()).thenReturn(reportingParameter);
		when(reportingContext.filterService()).thenReturn(mock(ReportingFilterService.class));
		when(reportingContext.repositorySchule()).thenReturn(mock(ReportingRepositorySchule.class, RETURNS_DEEP_STUBS));

		kursbelegungen = new ArrayList<>();
		final ReportingSchueler schueler = mock(ReportingSchueler.class);
		when(schueler.id()).thenReturn(ID_SCHUELER);
		when(schueler.gostKursplanungKursbelegungen()).thenReturn(kursbelegungen);
		final ReportingRepositorySchueler repositorySchueler = mock(ReportingRepositorySchueler.class);
		when(repositorySchueler.schueler(any())).thenReturn(new ArrayList<>(List.of(schueler)));
		when(reportingContext.repositorySchueler()).thenReturn(repositorySchueler);

		// Die Lehrkraft des zweiten Kurses lässt den Aufbau scheitern, nachdem der erste Kurs seine Kursbelegung angehängt hat.
		final ReportingRepositoryLehrer repositoryLehrer = mock(ReportingRepositoryLehrer.class);
		when(repositoryLehrer.lehrer(ID_LEHRKRAFT)).thenThrow(new DeveloperNotificationException("Fehlerinjektion"));
		when(reportingContext.repositoryLehrer()).thenReturn(repositoryLehrer);
	}


	/**
	 * Erzeugt ein Fach der gymnasialen Oberstufe.
	 *
	 * @param id      Die ID des Faches.
	 * @param kuerzel Das Kürzel des Faches.
	 *
	 * @return Das Fach.
	 */
	private static GostFach fach(final long id, final String kuerzel) {
		final GostFach fach = new GostFach();
		fach.id = id;
		fach.kuerzel = kuerzel;
		fach.kuerzelAnzeige = kuerzel;
		return fach;
	}

	/**
	 * Erzeugt einen Grundkurs der Blockung im Fach mit der ID {@link #ID_FACH}.
	 *
	 * @param id     Die ID des Kurses.
	 * @param nummer Die Nummer des Kurses; sie bestimmt die Reihenfolge, in der das Repository die Kurse verarbeitet.
	 *
	 * @return Der Kurs.
	 */
	private static GostBlockungKurs kurs(final long id, final int nummer) {
		final GostBlockungKurs kurs = new GostBlockungKurs();
		kurs.id = id;
		kurs.fach_id = ID_FACH;
		kurs.kursart = GostKursart.GK.id;
		kurs.nummer = nummer;
		return kurs;
	}

	/**
	 * Baut die Blockungsdaten mit einer Schiene, zwei Kursen, einem Schüler und dessen Fachwahl auf. Nur der zweite Kurs trägt die Lehrkraft, an der der
	 * Aufbau scheitert.
	 *
	 * @return Der Blockungsdaten-Manager zu diesen Daten.
	 */
	private static GostBlockungsdatenManager blockungsdatenManager() {
		final GostFaecherManager faecherManager = new GostFaecherManager(GostHalbjahr.Q11.getSchuljahrFromAbiturjahr(ABITURJAHR));
		faecherManager.add(fach(ID_FACH, "D"));

		final GostBlockungsdaten blockungsdaten = new GostBlockungsdaten();
		blockungsdaten.id = ID_BLOCKUNG;
		blockungsdaten.name = "Blockung mit zwei Kursen";
		blockungsdaten.abijahrgang = ABITURJAHR;
		blockungsdaten.gostHalbjahr = GostHalbjahr.Q11.id;

		final GostBlockungSchiene schiene = new GostBlockungSchiene();
		schiene.id = ID_SCHIENE;
		schiene.nummer = 1;
		schiene.bezeichnung = "Schiene 1";
		blockungsdaten.schienen.add(schiene);

		final Schueler schueler = new Schueler();
		schueler.id = ID_SCHUELER;
		schueler.nachname = "Muster";
		schueler.vorname = "Max";
		schueler.geschlecht = Geschlecht.M.id;
		schueler.status = SchuelerStatus.AKTIV.ordinal();
		blockungsdaten.schueler.add(schueler);

		final GostFachwahl fachwahl = new GostFachwahl();
		fachwahl.schuelerID = ID_SCHUELER;
		fachwahl.fachID = ID_FACH;
		fachwahl.kursartID = GostKursart.GK.id;
		fachwahl.istSchriftlich = true;
		blockungsdaten.fachwahlen.add(fachwahl);

		blockungsdaten.kurse.add(kurs(ID_KURS_MIT_BELEGUNG, 1));
		final GostBlockungKurs kursMitFehler = kurs(ID_KURS_MIT_FEHLER, 2);
		final GostBlockungKursLehrer lehrkraft = new GostBlockungKursLehrer();
		lehrkraft.id = ID_LEHRKRAFT;
		lehrkraft.reihenfolge = 1;
		lehrkraft.kuerzel = "MUS";
		lehrkraft.nachname = "Muster";
		lehrkraft.vorname = "Erika";
		kursMitFehler.lehrer.add(lehrkraft);
		blockungsdaten.kurse.add(kursMitFehler);

		return new GostBlockungsdatenManager(blockungsdaten, faecherManager);
	}

	/**
	 * Baut das Blockungsergebnis auf: Beide Kurse liegen in der Schiene, der Schüler belegt den ersten Kurs.
	 *
	 * @return Das Blockungsergebnis.
	 */
	private static GostBlockungsergebnis blockungsergebnis() {
		final GostBlockungsergebnisKurs kursMitBelegung =
				DTOUtils.newGostBlockungsergebnisKurs(ID_KURS_MIT_BELEGUNG, ID_FACH, GostKursart.GK.id, 1);
		kursMitBelegung.schueler.add(ID_SCHUELER);
		final GostBlockungsergebnisKurs kursMitFehler = DTOUtils.newGostBlockungsergebnisKurs(ID_KURS_MIT_FEHLER, ID_FACH, GostKursart.GK.id, 1);

		final GostBlockungsergebnisSchiene schiene = DTOUtils.newGostBlockungsergebnisSchiene(ID_SCHIENE);
		schiene.kurse.add(kursMitBelegung);
		schiene.kurse.add(kursMitFehler);

		final GostBlockungsergebnis ergebnis = new GostBlockungsergebnis();
		ergebnis.id = ID_BLOCKUNGSERGEBNIS;
		ergebnis.blockungID = ID_BLOCKUNG;
		ergebnis.gostHalbjahr = GostHalbjahr.Q11.id;
		ergebnis.schienen.add(schiene);
		return ergebnis;
	}


	@Test
	void testEinZweiterZugriffNachEinemAufbaufehlerVerdoppeltDieKursbelegungenNicht() {
		final ReportingRepositoryGostKursplanung repository = new ReportingRepositoryGostKursplanung(reportingContext);
		final GostBlockungsdatenManager datenManager = blockungsdatenManager();
		final GostBlockungsergebnis ergebnis = blockungsergebnis();

		try (MockedStatic<DataGostBlockungsergebnisse> dataErgebnisse = mockStatic(DataGostBlockungsergebnisse.class);
				MockedStatic<DataGostBlockungsdaten> dataBlockungsdaten = mockStatic(DataGostBlockungsdaten.class)) {
			dataErgebnisse.when(() -> DataGostBlockungsergebnisse.getErgebnisFromID(any(), anyLong())).thenReturn(ergebnis);
			dataBlockungsdaten.when(() -> DataGostBlockungsdaten.getBlockungsdatenManagerFromDB(any(), eq(ID_BLOCKUNG))).thenReturn(datenManager);

			assertThrows(ApiOperationException.class, repository::kurse);
			assertEquals(1, kursbelegungen.size(), "Der erste Kurs hängt seine Kursbelegung an, bevor der Aufbau scheitert.");

			assertThrows(ApiOperationException.class, repository::kurse);
			assertEquals(1, kursbelegungen.size(), "Der zweite Zugriff hängt keine weitere Kursbelegung an das geteilte Schüler-Objekt.");
			dataErgebnisse.verify(() -> DataGostBlockungsergebnisse.getErgebnisFromID(any(), anyLong()), times(1));
		}
	}

}
