package de.svws_nrw.data.gost;

import de.svws_nrw.core.data.gost.AbiturFachbelegung;
import de.svws_nrw.core.data.gost.AbiturFachbelegungHalbjahr;
import de.svws_nrw.core.data.gost.Abiturdaten;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.data.gost.GostFachwahl;
import de.svws_nrw.core.data.gost.GostJahrgangFachwahlen;
import de.svws_nrw.core.data.gost.GostJahrgangFachwahlenHalbjahr;
import de.svws_nrw.core.data.gost.GostLeistungen;
import de.svws_nrw.core.data.gost.GostLeistungenFachbelegung;
import de.svws_nrw.core.data.gost.GostLeistungenFachwahl;
import de.svws_nrw.core.data.schueler.Sprachbelegung;
import de.svws_nrw.core.data.schueler.Sprachendaten;
import de.svws_nrw.core.types.Note;
import de.svws_nrw.core.types.SchuelerStatus;
import de.svws_nrw.core.types.fach.ZulaessigesFach;
import de.svws_nrw.core.types.gost.GostAbiturFach;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.types.jahrgang.Jahrgaenge;
import de.svws_nrw.core.utils.gost.GostFaecherManager;
import de.svws_nrw.core.utils.jahrgang.JahrgangsUtils;
import de.svws_nrw.data.faecher.DBUtilsFaecherGost;
import de.svws_nrw.data.schueler.DBUtilsSchueler;
import de.svws_nrw.data.schule.SchulUtils;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangFachbelegungen;
import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangsdaten;
import de.svws_nrw.db.dto.current.gost.DTOGostSchueler;
import de.svws_nrw.db.dto.current.gost.DTOGostSchuelerFachbelegungen;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLeistungsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.utils.OperationError;
import jakarta.persistence.TypedQuery;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * Diese Klasse stellt Hilfsmethoden für den Zugriff auf Informationen
 * zu der Laufbahnplanung von Schülern der gymnasialen Oberstufe zur Verfügung.
 */
public final class DBUtilsGostLaufbahn {

	private DBUtilsGostLaufbahn() {
		throw new IllegalStateException("Instantiation of " + DBUtilsGostLaufbahn.class.getName() + " not allowed");
	}

	/**
	 * Ermittelt die Daten für den Schüler der gymnasialen Oberstufe. Ist kein Schüler angelegt, so wird dieser mit den
	 * Default-Daten des Jahrgangs angelegt. Es wird intern geprüft, ob eine neue Transaktion gestartet werden muss
	 * oder ob die Handhabung von außerhalb erfolgt.
	 *
	 * @param conn         die zu nutzende Datenbank-Verbindung
	 * @param idSchueler   die ID des Schülers
	 * @param abijahr      der Abiturjahrgang
	 *
	 * @return die Daten des Schülers
	 */
	public static DTOGostSchueler getSchuelerOrInit(final DBEntityManager conn, final long idSchueler, final int abijahr) {
		final boolean needTransaction = !conn.hasActiveTransaction();
		try {
			if (needTransaction)
				conn.transactionBegin();
    		// Prüfe, ob der Abiturjahrgang für den Schüler existiert
			final DTOGostJahrgangsdaten jahrgang = conn.queryByKey(DTOGostJahrgangsdaten.class, abijahr);
			if (jahrgang == null)
				throw OperationError.NOT_FOUND.exception();
			// Lese den Schüler aus
	    	DTOGostSchueler dtoGostSchueler = conn.queryByKey(DTOGostSchueler.class, idSchueler);
	    	if (dtoGostSchueler == null) {
	    		dtoGostSchueler = new DTOGostSchueler(idSchueler, false);
	    		if (!conn.transactionPersist(dtoGostSchueler))
	    			throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR.getStatusCode());
	    		// Initialisiere die Laufbahnplanung mit Default-Einträgen
	    		DataGostJahrgangLaufbahnplanung.transactionResetSchueler(conn, jahrgang, idSchueler);
	    	}
	    	if (needTransaction)
	    		conn.transactionCommit();
			return dtoGostSchueler;
		} catch (final Exception e) {
			if (e instanceof final WebApplicationException webAppException)
				throw webAppException;
			throw OperationError.INTERNAL_SERVER_ERROR.exception(e);
		} finally {
			// Perform a rollback if necessary
			if (needTransaction)
				conn.transactionRollback();
		}
	}

	/**
	 * Ermittelt die für die Laufbahnplanung der gymnasialen Oberstufe relevanten Daten für
	 * den Schüler mit der angegebenen ID aus den in der Datenbank gespeicherten
	 * Laufbahnplanungstabellen und ggf. den Abiturtabellen.
	 *
	 * @param conn   die Datenbank-Verbindung
	 * @param id     die ID des Schülers
	 *
	 * @return die für das Abitur relevanten Daten für den Schüler mit der angegebenen ID
	 */
    public static Abiturdaten get(final DBEntityManager conn, final long id) {
    	final @NotNull DTOEigeneSchule schule = SchulUtils.getDTOSchule(conn);
    	final DTOSchueler dtoSchueler = conn.queryByKey(DTOSchueler.class, id);
    	if (dtoSchueler == null)
    		throw new WebApplicationException(Status.NOT_FOUND.getStatusCode());
    	final DTOSchuljahresabschnitte dtoAbschnitt = conn.queryByKey(DTOSchuljahresabschnitte.class, dtoSchueler.Schuljahresabschnitts_ID);
    	if (dtoAbschnitt == null)
    		throw new WebApplicationException(Status.NOT_FOUND.getStatusCode());

    	final TypedQuery<DTOSchuelerLernabschnittsdaten> queryAktAbschnitt = conn.query("SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Schueler_ID = :schueler_id AND e.Schuljahresabschnitts_ID = :abschnitt_id", DTOSchuelerLernabschnittsdaten.class);
    	final DTOSchuelerLernabschnittsdaten aktAbschnitt = queryAktAbschnitt
    			.setParameter("schueler_id", id)
    			.setParameter("abschnitt_id", dtoSchueler.Schuljahresabschnitts_ID)
    			.getResultList().stream().findFirst().orElse(null);
    	if (aktAbschnitt == null)
    		throw new WebApplicationException(Status.NOT_FOUND.getStatusCode());
    	final Integer abiturjahr = DBUtilsGost.getAbiturjahr(schule.Schulform, aktAbschnitt, dtoAbschnitt.Jahr);
    	final GostFaecherManager gostFaecher = DBUtilsFaecherGost.getFaecherListeGost(conn, abiturjahr);
    	getSchuelerOrInit(conn, id, abiturjahr);   // Initialisiere die Daten des Schülers, falls er nicht bereits angelegt wurde
    	final Map<Long, DTOGostSchuelerFachbelegungen> dtoFachwahlen =
    			conn.queryNamed("DTOGostSchuelerFachbelegungen.schueler_id", id, DTOGostSchuelerFachbelegungen.class)
    			.stream().collect(Collectors.toMap(fb -> fb.Fach_ID, fb -> fb));

    	// Bestimme die bereits vorhandenen Leistungsdaten für die weitere Laufbahnplanung
    	final GostLeistungen leistungen = DBUtilsGost.getLeistungsdaten(conn, id);
    	if (leistungen == null)
    		throw new WebApplicationException(Status.NOT_FOUND.getStatusCode());

    	final Abiturdaten abidaten = new Abiturdaten();
    	abidaten.schuelerID = id;
    	abidaten.abiturjahr = abiturjahr;
    	abidaten.schuljahrAbitur = abidaten.abiturjahr - 1;
    	abidaten.sprachendaten = leistungen.sprachendaten;
		abidaten.bilingualeSprache = leistungen.bilingualeSprache;
		abidaten.projektKursThema = leistungen.projektkursThema;
		abidaten.projektkursLeitfach1Kuerzel = leistungen.projektkursLeitfach1Kuerzel;
		abidaten.projektkursLeitfach2Kuerzel = leistungen.projektkursLeitfach2Kuerzel;

		for (final GostHalbjahr hj : GostHalbjahr.values())
			abidaten.bewertetesHalbjahr[hj.id] = leistungen.bewertetesHalbjahr[hj.id];

		for (final GostLeistungenFachwahl leistungenFach : leistungen.faecher) {
			GostHalbjahr letzteBelegungHalbjahr = null;   // das Halbjahr der letzten Belegung
			final AbiturFachbelegung fach = new AbiturFachbelegung();
			fach.fachID = leistungenFach.fach.id;
			fach.istFSNeu = leistungenFach.istFSNeu;
			fach.abiturFach = GostAbiturFach.fromID(leistungenFach.abiturfach) == null ? null : leistungenFach.abiturfach;
			for (final GostLeistungenFachbelegung leistungenBelegung : leistungenFach.belegungen) {
				if (!leistungenBelegung.abschnittGewertet)
					continue;
				// Nehme jeweils die Kursart, welche beim letzten gewerteten Abschnitt eingetragen ist
				if ((letzteBelegungHalbjahr == null || GostHalbjahr.fromKuerzel(leistungenBelegung.halbjahrKuerzel).compareTo(letzteBelegungHalbjahr) > 0) && (GostHalbjahr.fromKuerzel(leistungenBelegung.halbjahrKuerzel) != null)) {
					letzteBelegungHalbjahr = GostHalbjahr.fromKuerzel(leistungenBelegung.halbjahrKuerzel);
					fach.letzteKursart = GostKursart.fromKuerzel(leistungenBelegung.kursartKuerzel) == null ? null : GostKursart.fromKuerzel(leistungenBelegung.kursartKuerzel).kuerzel;
				}

				// Erzeuge die zugehörige Belegung
				final AbiturFachbelegungHalbjahr belegung = new AbiturFachbelegungHalbjahr();
				belegung.halbjahrKuerzel = (GostHalbjahr.fromKuerzel(leistungenBelegung.halbjahrKuerzel) == null) ? null : GostHalbjahr.fromKuerzel(leistungenBelegung.halbjahrKuerzel).kuerzel;
				belegung.kursartKuerzel = (GostKursart.fromKuerzel(leistungenBelegung.kursartKuerzel) == null) ? null : GostKursart.fromKuerzel(leistungenBelegung.kursartKuerzel).kuerzel;
				if ("AT".equals(leistungenBelegung.notenKuerzel)) {
					final GostFach gostFach = gostFaecher.get(fach.fachID);
					if (ZulaessigesFach.SP == ZulaessigesFach.getByKuerzelASD(gostFach.kuerzel))
						belegung.kursartKuerzel = "AT";
				}
				belegung.schriftlich = leistungenBelegung.istSchriftlich;
				belegung.biliSprache = leistungenBelegung.bilingualeSprache;
				belegung.lehrer = leistungenBelegung.lehrer;
				belegung.wochenstunden = leistungenBelegung.wochenstunden;
				belegung.fehlstundenGesamt = leistungenBelegung.fehlstundenGesamt;
				belegung.fehlstundenUnentschuldigt = leistungenBelegung.fehlstundenUnentschuldigt;
				belegung.notenkuerzel = Note.fromKuerzel(leistungenBelegung.notenKuerzel).kuerzel;
				fach.belegungen[GostHalbjahr.fromKuerzel(leistungenBelegung.halbjahrKuerzel).id] = belegung;
			}
			// Prüfe, ob das Fach in einem gewerteten Abschnitt belegt wurde. Wenn ja, dann füge es zu es den Fachbelegungen hinzu
			if (letzteBelegungHalbjahr != null)
				abidaten.fachbelegungen.add(fach);
		}

		// Bestimmt die Fehlstunden-Summe für den Block I (Qualifikationsphase) anhand der Fehlstunden bei den einzelnen Kurs-Belegungen.
		int block1FehlstundenGesamt = 0;
		int block1FehlstundenUnentschuldigt = 0;
		for (final AbiturFachbelegung fach : abidaten.fachbelegungen) {
			for (final AbiturFachbelegungHalbjahr belegung : fach.belegungen) {
				if ((belegung == null) || !GostHalbjahr.fromKuerzel(belegung.halbjahrKuerzel).istQualifikationsphase())
					continue;
				block1FehlstundenGesamt += belegung.fehlstundenGesamt;
				block1FehlstundenUnentschuldigt += belegung.fehlstundenUnentschuldigt;
			}
		}
		abidaten.block1FehlstundenGesamt = block1FehlstundenGesamt;
		abidaten.block1FehlstundenUnentschuldigt = block1FehlstundenUnentschuldigt;

    	// Belegte Fächer aus den Leistungsdaten überprüfen und Abiturfach setzen
		for (final AbiturFachbelegung fach : abidaten.fachbelegungen) {
			final DTOGostSchuelerFachbelegungen belegungPlanung = dtoFachwahlen.get(fach.fachID);
			if (belegungPlanung == null) {
			    fach.abiturFach = null;
		    } else {
			    final GostAbiturFach tmpAbiturFach = GostAbiturFach.fromID(belegungPlanung.AbiturFach);
			    fach.abiturFach = tmpAbiturFach == null ? null : tmpAbiturFach.id;
			}
		}

		// Füge gewählte Fächer ohne Leistungsdaten hinzu
		for (final DTOGostSchuelerFachbelegungen belegungPlanung : dtoFachwahlen.values()) {
			// filtere leere Belegungen aus der Planung
			if ((belegungPlanung.EF1_Kursart == null) && (belegungPlanung.EF2_Kursart == null)
					&& (belegungPlanung.Q11_Kursart == null) && (belegungPlanung.Q12_Kursart == null)
					&& (belegungPlanung.Q21_Kursart == null) && (belegungPlanung.Q22_Kursart == null))
				continue;

			// Prüfe, ob die Fachbelegung aufgrund von Leistungsdaten schon vorhanden ist
			AbiturFachbelegung fach = null;
			for (final AbiturFachbelegung fb : abidaten.fachbelegungen) {
				if (fb.fachID == belegungPlanung.Fach_ID) {
					fach = fb;
					break;
				}
			}
			// Es wurde keine Fachbelegung gefunden -> also muss eine neue ergänzt werden.
			if (fach == null) {
				fach = new AbiturFachbelegung();
				fach.fachID = belegungPlanung.Fach_ID;
				abidaten.fachbelegungen.add(fach);
			}

			final GostFach gostFach = gostFaecher.get(fach.fachID);
			if (gostFach == null)
			    continue;
			final ZulaessigesFach zulFach = ZulaessigesFach.getByKuerzelASD(gostFach.kuerzel);
			if (zulFach != null)
				fach.istFSNeu = zulFach.daten.istFremdsprache && zulFach.daten.nurSII;
			final GostAbiturFach tmpAbiturFach = GostAbiturFach.fromID(belegungPlanung.AbiturFach);
			fach.abiturFach = tmpAbiturFach == null ? null : tmpAbiturFach.id;
			GostKursart fachKursart = GostKursart.GK;
			if ("PX".equals(gostFach.kuerzel))
				fachKursart = GostKursart.PJK;
			else if ("VX".equals(gostFach.kuerzel))
				fachKursart = GostKursart.VTF;
			if ((fach.belegungen[GostHalbjahr.EF1.id] == null) && (belegungPlanung.EF1_Kursart != null))
				setFachbelegung(fach, GostHalbjahr.EF1, belegungPlanung.EF1_Kursart, fachKursart, gostFach.wochenstundenQualifikationsphase, false);
			if ((fach.belegungen[GostHalbjahr.EF2.id] == null) && (belegungPlanung.EF2_Kursart != null))
				setFachbelegung(fach, GostHalbjahr.EF2, belegungPlanung.EF2_Kursart, fachKursart, gostFach.wochenstundenQualifikationsphase, false);
			if ((fach.belegungen[GostHalbjahr.Q11.id] == null) && (belegungPlanung.Q11_Kursart != null))
				setFachbelegung(fach, GostHalbjahr.Q11, belegungPlanung.Q11_Kursart, fachKursart, gostFach.wochenstundenQualifikationsphase, belegungPlanung.Markiert_Q1);
			if ((fach.belegungen[GostHalbjahr.Q12.id] == null) && (belegungPlanung.Q12_Kursart != null))
				setFachbelegung(fach, GostHalbjahr.Q12, belegungPlanung.Q12_Kursart, fachKursart, gostFach.wochenstundenQualifikationsphase, belegungPlanung.Markiert_Q2);
			if ((fach.belegungen[GostHalbjahr.Q21.id] == null) && (belegungPlanung.Q21_Kursart != null))
				setFachbelegung(fach, GostHalbjahr.Q21, belegungPlanung.Q21_Kursart, fachKursart, gostFach.wochenstundenQualifikationsphase, belegungPlanung.Markiert_Q3);
			if ((fach.belegungen[GostHalbjahr.Q22.id] == null) && (belegungPlanung.Q22_Kursart != null))
				setFachbelegung(fach, GostHalbjahr.Q22, belegungPlanung.Q22_Kursart, fachKursart, gostFach.wochenstundenQualifikationsphase, belegungPlanung.Markiert_Q4);
		}

		// und gib die Abiturdaten zurück...
		return abidaten;
    }


	/**
	 * Ermittelt die für die Laufbahnplanung der gymnasialen Oberstufe relevanten Fachwahldaten
	 * für die Vorlagen von Laufbahnplanungen bei den Abiturjahrgängen
	 * den Schüler mit der angegebenen ID aus den in der Datenbank gespeicherten
	 * Laufbahnplanungstabellen.
	 *
	 * @param conn       die Datenbank-Verbindung
	 * @param abijahr    das Abiturjahr
	 *
	 * @return die Fachwahlinformationen für die Laufbahnplanungs-Vorlage des angegebenen Abiturjahrgangs
	 */
    public static Abiturdaten getVorlage(final DBEntityManager conn, final int abijahr) {
		final DTOGostJahrgangsdaten jahrgang = conn.queryByKey(DTOGostJahrgangsdaten.class, abijahr);
		if (jahrgang == null)
			throw OperationError.NOT_FOUND.exception();
    	final GostFaecherManager gostFaecher = DBUtilsFaecherGost.getFaecherListeGost(conn, abijahr);
    	final Map<Long, DTOGostJahrgangFachbelegungen> dtoFachwahlen =
    			conn.queryNamed("DTOGostJahrgangFachbelegungen.abi_jahrgang", abijahr, DTOGostJahrgangFachbelegungen.class)
    			.stream().collect(Collectors.toMap(fb -> fb.Fach_ID, fb -> fb));

    	final Abiturdaten abidaten = new Abiturdaten();
    	abidaten.schuelerID = -1;
    	abidaten.abiturjahr = abijahr;
    	abidaten.schuljahrAbitur = abijahr - 1;
		// Erstelle Fake-Einträge für die Sprachenfolge, da die konkrete Belegung der Schüler in der Sprachenfolge unklar ist
		abidaten.bilingualeSprache = null;               // TODO ggf. auch ein alternatives Defaulting für den bilingualen Zweig erlauben
    	abidaten.sprachendaten.schuelerID = -1;
    	for (final String sprachkuerzel : gostFaecher.getFremdsprachenkuerzel()) {
			final Sprachbelegung belegung = new Sprachbelegung();
			belegung.sprache = sprachkuerzel;
			belegung.reihenfolge = 1;
			belegung.belegungVonJahrgang = Jahrgaenge.JG_05.daten.kuerzel;
			abidaten.sprachendaten.belegungen.add(belegung);
        }
		for (final GostHalbjahr hj : GostHalbjahr.values())
			abidaten.bewertetesHalbjahr[hj.id] = false;  // Da es sich um eine Vorlage handelt, sind die Halbjahre nicht bewertet
		// Füge gewählte Fächer hinzu
		for (final DTOGostJahrgangFachbelegungen belegungPlanung : dtoFachwahlen.values()) {
			// filtere leere Belegungen aus der Planung
			if ((belegungPlanung.EF1_Kursart == null) && (belegungPlanung.EF2_Kursart == null)
					&& (belegungPlanung.Q11_Kursart == null) && (belegungPlanung.Q12_Kursart == null)
					&& (belegungPlanung.Q21_Kursart == null) && (belegungPlanung.Q22_Kursart == null))
				continue;

			final AbiturFachbelegung fach = new AbiturFachbelegung();
			fach.fachID = belegungPlanung.Fach_ID;
			abidaten.fachbelegungen.add(fach);

			final GostFach gostFach = gostFaecher.get(fach.fachID);
			if (gostFach == null)
			    continue;
			final ZulaessigesFach zulFach = ZulaessigesFach.getByKuerzelASD(gostFach.kuerzel);
			if (zulFach != null)
				fach.istFSNeu = zulFach.daten.istFremdsprache && zulFach.daten.nurSII;
			final GostAbiturFach tmpAbiturFach = GostAbiturFach.fromID(belegungPlanung.AbiturFach);
			fach.abiturFach = tmpAbiturFach == null ? null : tmpAbiturFach.id;
			GostKursart fachKursart = GostKursart.GK;
			if ("PX".equals(gostFach.kuerzel))
				fachKursart = GostKursart.PJK;
			else if ("VX".equals(gostFach.kuerzel))
				fachKursart = GostKursart.VTF;
			if (belegungPlanung.EF1_Kursart != null)
				setFachbelegung(fach, GostHalbjahr.EF1, belegungPlanung.EF1_Kursart, fachKursart, gostFach.wochenstundenQualifikationsphase, false);
			if (belegungPlanung.EF2_Kursart != null)
				setFachbelegung(fach, GostHalbjahr.EF2, belegungPlanung.EF2_Kursart, fachKursart, gostFach.wochenstundenQualifikationsphase, false);
			if (belegungPlanung.Q11_Kursart != null)
				setFachbelegung(fach, GostHalbjahr.Q11, belegungPlanung.Q11_Kursart, fachKursart, gostFach.wochenstundenQualifikationsphase, false);
			if (belegungPlanung.Q12_Kursart != null)
				setFachbelegung(fach, GostHalbjahr.Q12, belegungPlanung.Q12_Kursart, fachKursart, gostFach.wochenstundenQualifikationsphase, false);
			if (belegungPlanung.Q21_Kursart != null)
				setFachbelegung(fach, GostHalbjahr.Q21, belegungPlanung.Q21_Kursart, fachKursart, gostFach.wochenstundenQualifikationsphase, false);
			if (belegungPlanung.Q22_Kursart != null)
				setFachbelegung(fach, GostHalbjahr.Q22, belegungPlanung.Q22_Kursart, fachKursart, gostFach.wochenstundenQualifikationsphase, false);
		}
		return abidaten;
    }


    private static void setFachbelegung(final AbiturFachbelegung fach, final GostHalbjahr halbjahr,
    		final String belegungPlanungKursart, final GostKursart fachKursart, final int wochenstunden, final boolean istInAbiwertung) {
    	final AbiturFachbelegungHalbjahr belegung = new AbiturFachbelegungHalbjahr();
    	belegung.halbjahrKuerzel = halbjahr.kuerzel;
		belegung.kursartKuerzel = belegungPlanungKursart == null ? null : switch (belegungPlanungKursart) {
			case "AT" -> "AT";
			case "LK" -> "LK";
			case "ZK" -> "ZK";
			default -> fachKursart.toString();
		};
		belegung.schriftlich = belegungPlanungKursart == null ? null
				: "LK".equals(belegungPlanungKursart) || "S".equals(belegungPlanungKursart);
		belegung.wochenstunden = "LK".equals(belegungPlanungKursart) ? 5 : wochenstunden;
		belegung.block1gewertet = istInAbiwertung;
		fach.belegungen[halbjahr.id] = belegung;
		fach.letzteKursart = belegung.kursartKuerzel;
    }



	/**
	 * Ermittelt die Daten für die Schüler der gymnasialen Oberstufe. Sind diese nicht angelegt, so legen
	 * diese mit den Default-Daten des Jahrgangs an.
	 *
	 * @param conn          die zu nutzende Datenbank-Verbindung
	 * @param schuelerIDs   die IDS deR Schüler
	 * @param abijahr       der Abiturjahrgang
	 *
	 * @return die Daten der Schüler
	 */
	public static @NotNull List<@NotNull DTOGostSchueler> getSchuelerOrInit(final DBEntityManager conn, final @NotNull List<@NotNull Long> schuelerIDs, final int abijahr) {
		// Prüfe, ob der Abiturjahrgang für den Schüler existiert
		final DTOGostJahrgangsdaten jahrgang = conn.queryByKey(DTOGostJahrgangsdaten.class, abijahr);
		if (jahrgang == null)
			throw OperationError.NOT_FOUND.exception();
		// Bestimme die Schülereinträge aus der Datenbank
		final @NotNull List<@NotNull DTOGostSchueler> result = conn.queryByKeyList(DTOGostSchueler.class, schuelerIDs);
		if (result.size() == schuelerIDs.size())
			return result;
		final @NotNull Set<@NotNull Long> idsExisting = result.stream().map(s -> s.Schueler_ID).collect(Collectors.toUnmodifiableSet());
		for (final long idSchueler : schuelerIDs) {
			if (idsExisting.contains(idSchueler))
				continue;
			final DTOGostSchueler dtoGostSchueler = new DTOGostSchueler(idSchueler, false);
			result.add(dtoGostSchueler);
    		if (!conn.transactionPersist(dtoGostSchueler))
    			throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR.getStatusCode());
    		conn.transactionFlush();
    		// Initialisiere die Laufbahnplanung mit Default-Einträgen
        	final List<DTOGostJahrgangFachbelegungen> dtoFachwahlen = conn.queryNamed("DTOGostJahrgangFachbelegungen.abi_jahrgang", abijahr, DTOGostJahrgangFachbelegungen.class);
    		conn.transactionExecuteDelete("DELETE FROM DTOGostSchuelerFachbelegungen e WHERE e.Schueler_ID = %d".formatted(idSchueler));
    		conn.transactionFlush();
    		for (final DTOGostJahrgangFachbelegungen dto : dtoFachwahlen) {
    			final DTOGostSchuelerFachbelegungen fw = new DTOGostSchuelerFachbelegungen(idSchueler, dto.Fach_ID);
    			fw.EF1_Kursart = dto.EF1_Kursart;
    			fw.EF2_Kursart = dto.EF2_Kursart;
    			fw.Q11_Kursart = dto.Q11_Kursart;
    			fw.Q12_Kursart = dto.Q11_Kursart;
    			fw.Q21_Kursart = dto.Q21_Kursart;
    			fw.Q22_Kursart = dto.Q22_Kursart;
    			fw.AbiturFach = dto.AbiturFach;
    			fw.Bemerkungen = dto.Bemerkungen;
    			conn.transactionPersist(fw);
    		}
    		conn.transactionFlush();
		}
		return result;
	}


	/**
	 * Ermittelt die für die Laufbahnplanung der gymnasialen Oberstufe relevanten Daten für
	 * die Schüler mit der angegebenen IDs aus den in der Datenbank gespeicherten
	 * Laufbahnplanungstabellen und ggf. den Abiturtabellen.
	 *
	 * @param conn          die Datenbank-Verbindung
	 * @param schule        das DTO der Schule
	 * @param abijahrgang   der Abiturjahrgang
	 *
	 * @return die für das Abitur relevanten Daten für die Schüler mit den angegebenen IDs
	 */
    public static @NotNull Map<@NotNull Long, @NotNull Abiturdaten> getAbiturdaten(final DBEntityManager conn, final DTOEigeneSchule schule, final int abijahrgang) {
    	final @NotNull Map<@NotNull Long, @NotNull Abiturdaten> result = new HashMap<>();
    	// Bestimme die Fächer des Abiturjahrgangs und die Schuljahresabschnitte
    	final GostFaecherManager gostFaecher = DBUtilsFaecherGost.getFaecherListeGost(conn, abijahrgang);
    	final Map<Long, DTOSchuljahresabschnitte> mapSchuljahresabschnitte = conn.queryAll(DTOSchuljahresabschnitte.class).stream().collect(Collectors.toMap(a -> a.ID, a -> a));
    	// Bestimme alle Schüler des angegebenen Abiturjahrgangs
    	final List<DTOSchueler> listSchueler = getSchuelerOfAbiturjahrgang(conn, abijahrgang);
    	if (listSchueler.isEmpty())
    		return new HashMap<>();
		final List<Long> schuelerIDs = listSchueler.stream().map(s -> s.ID).toList();
    	final Map<Long, DTOSchueler> mapSchueler = listSchueler.stream().collect(Collectors.toMap(s -> s.ID, s -> s));
    	// Bestimme die aktuellen Lernabschnitte der Schüler
    	final TypedQuery<DTOSchuelerLernabschnittsdaten> queryAktAbschnitte = conn.query("SELECT e FROM DTOSchuelerLernabschnittsdaten e JOIN DTOSchueler s ON s.ID IN :ids AND e.Schueler_ID = s.ID AND e.Schuljahresabschnitts_ID = s.Schuljahresabschnitts_ID AND e.WechselNr = 0", DTOSchuelerLernabschnittsdaten.class);
    	final List<DTOSchuelerLernabschnittsdaten> listAktAbschnitte = queryAktAbschnitte.setParameter("ids", schuelerIDs).getResultList();
    	final Map<Long, DTOSchuelerLernabschnittsdaten> mapAktAbschnitteBySchuelerID = listAktAbschnitte.stream().collect(Collectors.toMap(a -> a.Schueler_ID, a -> a));
    	// Bestimme die Leistungsdaten mithilfe der Sprachendaten der Schüler
    	final TypedQuery<DTOSchuelerLernabschnittsdaten> queryAlleGostAbschnitte = conn.query("SELECT e FROM DTOSchuelerLernabschnittsdaten e JOIN DTOSchueler s ON s.ID IN :ids AND e.Schueler_ID = s.ID AND e.Schuljahresabschnitts_ID = s.Schuljahresabschnitts_ID AND e.WechselNr = 0 AND e.ASDJahrgang IN ('EF', 'Q1', 'Q2')", DTOSchuelerLernabschnittsdaten.class);
    	final List<DTOSchuelerLernabschnittsdaten> listAlleGostAbschnitte = queryAlleGostAbschnitte.setParameter("ids", schuelerIDs).getResultList();
    	final List<Long> listAlleGostLernabschnittsIDs = listAlleGostAbschnitte.stream().map(l -> l.ID).toList();
    	final Map<Long, List<DTOSchuelerLernabschnittsdaten>> mapAlleGostAbschnitteBySchuelerID = listAlleGostAbschnitte.stream().collect(Collectors.groupingBy(a -> a.Schueler_ID, Collectors.toList()));
    	schuelerIDs.stream().forEach(id -> mapAlleGostAbschnitteBySchuelerID.computeIfAbsent(id, k -> new ArrayList<>()));
    	final Map<Long, Sprachendaten> mapSprachendaten = DBUtilsSchueler.getSchuelerSprachendaten(conn, schuelerIDs).stream().collect(Collectors.toMap(sd -> sd.schuelerID, sd -> sd));
    	final Map<Long, List<DTOSchuelerLeistungsdaten>> mapLeistungenByAbschnittID = listAlleGostLernabschnittsIDs.isEmpty()
    			? new HashMap<>()
    			: conn.queryNamed("DTOSchuelerLeistungsdaten.abschnitt_id.multiple", listAlleGostLernabschnittsIDs, DTOSchuelerLeistungsdaten.class)
    			.stream().collect(Collectors.groupingBy(l -> l.Abschnitt_ID));
    	listAlleGostLernabschnittsIDs.stream().forEach(id -> mapLeistungenByAbschnittID.computeIfAbsent(id, k -> new ArrayList<>()));
    	final Map<Long, GostLeistungen> mapGostLeistungen = DBUtilsGost.getLeistungsdatenFromDTOs(schuelerIDs, gostFaecher, mapSchuljahresabschnitte, mapSchueler, mapAlleGostAbschnitteBySchuelerID, mapLeistungenByAbschnittID, mapSprachendaten);
    	// Bestimme die allgemeinen Daten des Schülers und die Fachbelegungen für die Gymnasiale Obertufe und lege dabei ggf. Default-Werte an
    	getSchuelerOrInit(conn, schuelerIDs, abijahrgang);
    	final List<DTOGostSchuelerFachbelegungen> listAlleFachwahlen = conn.queryNamed("DTOGostSchuelerFachbelegungen.schueler_id.multiple", schuelerIDs, DTOGostSchuelerFachbelegungen.class);
    	final Map<Long, Map<Long, DTOGostSchuelerFachbelegungen>> mapAlleFachwahlen = listAlleFachwahlen.stream().collect(Collectors.groupingBy(fw -> fw.Schueler_ID, Collectors.toMap(f -> f.Fach_ID, f -> f)));

    	// Erstelle die Abiturdaten aus den DTOs
    	for (final long idSchueler : schuelerIDs) {
        	final DTOSchueler dtoSchueler = mapSchueler.get(idSchueler);
        	final DTOSchuljahresabschnitte dtoAbschnitt = mapSchuljahresabschnitte.get(dtoSchueler.Schuljahresabschnitts_ID);
        	final DTOSchuelerLernabschnittsdaten aktAbschnitt = mapAktAbschnitteBySchuelerID.get(idSchueler);
        	if (aktAbschnitt == null)
        		throw new WebApplicationException(Status.NOT_FOUND.getStatusCode());
        	final Integer abiturjahr = DBUtilsGost.getAbiturjahr(schule.Schulform, aktAbschnitt, dtoAbschnitt.Jahr);
        	final Map<Long, DTOGostSchuelerFachbelegungen> dtoFachwahlen = mapAlleFachwahlen.computeIfAbsent(idSchueler, k -> new HashMap<>());
        	// Bestimme die bereits vorhandenen Leistungsdaten
        	final GostLeistungen leistungen = mapGostLeistungen.get(idSchueler);
        	if (leistungen == null)
        		throw new WebApplicationException(Status.NOT_FOUND.getStatusCode());

	    	final Abiturdaten abidaten = new Abiturdaten();
	    	abidaten.schuelerID = idSchueler;
	    	abidaten.abiturjahr = abiturjahr;
	    	abidaten.schuljahrAbitur = abidaten.abiturjahr - 1;
	    	abidaten.sprachendaten = leistungen.sprachendaten;
			abidaten.bilingualeSprache = leistungen.bilingualeSprache;
			abidaten.projektKursThema = leistungen.projektkursThema;
			abidaten.projektkursLeitfach1Kuerzel = leistungen.projektkursLeitfach1Kuerzel;
			abidaten.projektkursLeitfach2Kuerzel = leistungen.projektkursLeitfach2Kuerzel;

			for (final GostHalbjahr hj : GostHalbjahr.values())
				abidaten.bewertetesHalbjahr[hj.id] = leistungen.bewertetesHalbjahr[hj.id];

			for (final GostLeistungenFachwahl leistungenFach : leistungen.faecher) {
				GostHalbjahr letzteBelegungHalbjahr = null;   // das Halbjahr der letzten Belegung
				final AbiturFachbelegung fach = new AbiturFachbelegung();
				fach.fachID = leistungenFach.fach.id;
				fach.istFSNeu = leistungenFach.istFSNeu;
				fach.abiturFach = GostAbiturFach.fromID(leistungenFach.abiturfach) == null ? null : leistungenFach.abiturfach;
				for (final GostLeistungenFachbelegung leistungenBelegung : leistungenFach.belegungen) {
					if (!leistungenBelegung.abschnittGewertet)
						continue;
					// Nehme jeweils die Kursart, welche beim letzten gewerteten Abschnitt eingetragen ist
					if ((letzteBelegungHalbjahr == null || GostHalbjahr.fromKuerzel(leistungenBelegung.halbjahrKuerzel).compareTo(letzteBelegungHalbjahr) > 0) && (GostHalbjahr.fromKuerzel(leistungenBelegung.halbjahrKuerzel) != null)) {
						letzteBelegungHalbjahr = GostHalbjahr.fromKuerzel(leistungenBelegung.halbjahrKuerzel);
						fach.letzteKursart = GostKursart.fromKuerzel(leistungenBelegung.kursartKuerzel) == null ? null : GostKursart.fromKuerzel(leistungenBelegung.kursartKuerzel).kuerzel;
					}

					// Erzeuge die zugehörige Belegung
					final AbiturFachbelegungHalbjahr belegung = new AbiturFachbelegungHalbjahr();
					belegung.halbjahrKuerzel = (GostHalbjahr.fromKuerzel(leistungenBelegung.halbjahrKuerzel) == null) ? null : GostHalbjahr.fromKuerzel(leistungenBelegung.halbjahrKuerzel).kuerzel;
					belegung.kursartKuerzel = (GostKursart.fromKuerzel(leistungenBelegung.kursartKuerzel) == null) ? null : GostKursart.fromKuerzel(leistungenBelegung.kursartKuerzel).kuerzel;
					if ("AT".equals(leistungenBelegung.notenKuerzel)) {
						final GostFach gostFach = gostFaecher.get(fach.fachID);
						if (ZulaessigesFach.SP == ZulaessigesFach.getByKuerzelASD(gostFach.kuerzel))
							belegung.kursartKuerzel = "AT";
					}
					belegung.schriftlich = leistungenBelegung.istSchriftlich;
					belegung.biliSprache = leistungenBelegung.bilingualeSprache;
					belegung.lehrer = leistungenBelegung.lehrer;
					belegung.wochenstunden = leistungenBelegung.wochenstunden;
					belegung.fehlstundenGesamt = leistungenBelegung.fehlstundenGesamt;
					belegung.fehlstundenUnentschuldigt = leistungenBelegung.fehlstundenUnentschuldigt;
					belegung.notenkuerzel = Note.fromKuerzel(leistungenBelegung.notenKuerzel).kuerzel;
					fach.belegungen[GostHalbjahr.fromKuerzel(leistungenBelegung.halbjahrKuerzel).id] = belegung;
				}
				// Prüfe, ob das Fach in einem gewerteten Abschnitt belegt wurde. Wenn ja, dann füge es zu es den Fachbelegungen hinzu
				if (letzteBelegungHalbjahr != null)
					abidaten.fachbelegungen.add(fach);
			}

			// Bestimmt die Fehlstunden-Summe für den Block I (Qualifikationsphase) anhand der Fehlstunden bei den einzelnen Kurs-Belegungen.
			int block1FehlstundenGesamt = 0;
			int block1FehlstundenUnentschuldigt = 0;
			for (final AbiturFachbelegung fach : abidaten.fachbelegungen) {
				for (final AbiturFachbelegungHalbjahr belegung : fach.belegungen) {
					if ((belegung == null) || !GostHalbjahr.fromKuerzel(belegung.halbjahrKuerzel).istQualifikationsphase())
						continue;
					block1FehlstundenGesamt += belegung.fehlstundenGesamt;
					block1FehlstundenUnentschuldigt += belegung.fehlstundenUnentschuldigt;
				}
			}
			abidaten.block1FehlstundenGesamt = block1FehlstundenGesamt;
			abidaten.block1FehlstundenUnentschuldigt = block1FehlstundenUnentschuldigt;

	    	// Belegte Fächer aus den Leistungsdaten überprüfen und Abiturfach setzen
			for (final AbiturFachbelegung fach : abidaten.fachbelegungen) {
				final DTOGostSchuelerFachbelegungen belegungPlanung = dtoFachwahlen.get(fach.fachID);
				if (belegungPlanung == null) {
				    fach.abiturFach = null;
			    } else {
				    final GostAbiturFach tmpAbiturFach = GostAbiturFach.fromID(belegungPlanung.AbiturFach);
				    fach.abiturFach = tmpAbiturFach == null ? null : tmpAbiturFach.id;
				}
			}

			// Füge gewählte Fächer ohne Leistungsdaten hinzu
			for (final DTOGostSchuelerFachbelegungen belegungPlanung : dtoFachwahlen.values()) {
				// filtere leere Belegungen aus der Planung
				if ((belegungPlanung.EF1_Kursart == null) && (belegungPlanung.EF2_Kursart == null)
						&& (belegungPlanung.Q11_Kursart == null) && (belegungPlanung.Q12_Kursart == null)
						&& (belegungPlanung.Q21_Kursart == null) && (belegungPlanung.Q22_Kursart == null))
					continue;

				// Korrigiere ggf. falsche Werte bei Markiert_Q1, Markiert_Q2, Markiert_Q3 und Markiert_Q4
				if (belegungPlanung.Markiert_Q1 == null)
					belegungPlanung.Markiert_Q1 = false;
				if (belegungPlanung.Markiert_Q2 == null)
					belegungPlanung.Markiert_Q2 = false;
				if (belegungPlanung.Markiert_Q3 == null)
					belegungPlanung.Markiert_Q3 = false;
				if (belegungPlanung.Markiert_Q4 == null)
					belegungPlanung.Markiert_Q4 = false;

				// Prüfe, ob die Fachbelegung aufgrund von Leistungsdaten schon vorhanden ist
				AbiturFachbelegung fach = null;
				for (final AbiturFachbelegung fb : abidaten.fachbelegungen) {
					if (fb.fachID == belegungPlanung.Fach_ID) {
						fach = fb;
						break;
					}
				}
				// Es wurde keine Fachbelegung gefunden -> also muss eine neue ergänzt werden.
				if (fach == null) {
					fach = new AbiturFachbelegung();
					fach.fachID = belegungPlanung.Fach_ID;
					abidaten.fachbelegungen.add(fach);
				}

				final GostFach gostFach = gostFaecher.get(fach.fachID);
				if (gostFach == null)
				    continue;
				final ZulaessigesFach zulFach = ZulaessigesFach.getByKuerzelASD(gostFach.kuerzel);
				if (zulFach != null)
					fach.istFSNeu = zulFach.daten.istFremdsprache && zulFach.daten.nurSII;
				final GostAbiturFach tmpAbiturFach = GostAbiturFach.fromID(belegungPlanung.AbiturFach);
				fach.abiturFach = tmpAbiturFach == null ? null : tmpAbiturFach.id;
				GostKursart fachKursart = GostKursart.GK;
				if ("PX".equals(gostFach.kuerzel))
					fachKursart = GostKursart.PJK;
				else if ("VX".equals(gostFach.kuerzel))
					fachKursart = GostKursart.VTF;
				if ((fach.belegungen[GostHalbjahr.EF1.id] == null) && (belegungPlanung.EF1_Kursart != null))
					setFachbelegung(fach, GostHalbjahr.EF1, belegungPlanung.EF1_Kursart, fachKursart, gostFach.wochenstundenQualifikationsphase, false);
				if ((fach.belegungen[GostHalbjahr.EF2.id] == null) && (belegungPlanung.EF2_Kursart != null))
					setFachbelegung(fach, GostHalbjahr.EF2, belegungPlanung.EF2_Kursart, fachKursart, gostFach.wochenstundenQualifikationsphase, false);
				if ((fach.belegungen[GostHalbjahr.Q11.id] == null) && (belegungPlanung.Q11_Kursart != null))
					setFachbelegung(fach, GostHalbjahr.Q11, belegungPlanung.Q11_Kursart, fachKursart, gostFach.wochenstundenQualifikationsphase, belegungPlanung.Markiert_Q1);
				if ((fach.belegungen[GostHalbjahr.Q12.id] == null) && (belegungPlanung.Q12_Kursart != null))
					setFachbelegung(fach, GostHalbjahr.Q12, belegungPlanung.Q12_Kursart, fachKursart, gostFach.wochenstundenQualifikationsphase, belegungPlanung.Markiert_Q2);
				if ((fach.belegungen[GostHalbjahr.Q21.id] == null) && (belegungPlanung.Q21_Kursart != null))
					setFachbelegung(fach, GostHalbjahr.Q21, belegungPlanung.Q21_Kursart, fachKursart, gostFach.wochenstundenQualifikationsphase, belegungPlanung.Markiert_Q3);
				if ((fach.belegungen[GostHalbjahr.Q22.id] == null) && (belegungPlanung.Q22_Kursart != null))
					setFachbelegung(fach, GostHalbjahr.Q22, belegungPlanung.Q22_Kursart, fachKursart, gostFach.wochenstundenQualifikationsphase, belegungPlanung.Markiert_Q4);
			}

			result.put(idSchueler, abidaten);
    	}
		// und gib die Abiturdaten zurück...
		return result;
    }


    /**
     * Bestimmt alle Schüler des angebebenen Abiturjahrgangs als Set Schüler-IDs.
     *
     * @param conn          die Datenbankverbindung
     * @param abijahrgang   der Abiturjahrgang
     *
     * @return die Menge der Schüler-IDs
     */
    public static List<DTOSchueler> getSchuelerOfAbiturjahrgang(final DBEntityManager conn, final int abijahrgang) {
    	// Bestimme die Schule, die Schuljahresabschnitte und alle Jahrgänge der Schule
    	final DTOEigeneSchule schule = DBUtilsGost.pruefeSchuleMitGOSt(conn);
    	final Map<Long, DTOSchuljahresabschnitte> mapAbschnitte = conn.queryAll(DTOSchuljahresabschnitte.class).stream().collect(Collectors.toMap(a -> a.ID, a -> a));
    	final Map<Long, DTOJahrgang> mapJahrgaenge = conn.queryAll(DTOJahrgang.class).stream().collect(Collectors.toMap(j -> j.ID, j -> j));
    	// Bestimme alle Jahrgänge der Schule, welche als ASD-Jahrgang 'EF', 'Q1', 'Q2' haben
    	final List<DTOJahrgang> listJahrgaengeGost = conn.queryNamed("DTOJahrgang.asdjahrgang.multiple", List.of("EF", "Q1", "Q2"), DTOJahrgang.class);
    	final List<Long> listJahrgaengeGostIDs = listJahrgaengeGost.stream().map(j -> j.ID).toList();
    	// Bestimme alle Schüler mit Geloescht <> '+' und (Schueler.Status <> 8 oder Schueler.Entlassjahrgang_ID.ASDJahrgang in ('EF', 'Q1', 'Q2')
    	final List<DTOSchueler> alleSchueler = conn.queryList("SELECT e FROM DTOSchueler e WHERE e.Geloescht <> true AND (e.Status <> ?1 OR e.Entlassjahrgang_ID IN ?2)", DTOSchueler.class, SchuelerStatus.ABSCHLUSS, listJahrgaengeGostIDs);
    	final List<Long> alleSchuelerIDs = alleSchueler.stream().map(s -> s.ID).toList();
    	// Bestimme die aktuellen SchuelerLernabschnitte der Schüler
    	final List<DTOSchuelerLernabschnittsdaten> schuelerLernabschnittsdaten = conn.queryList(
			"SELECT sla FROM DTOSchuelerLernabschnittsdaten sla JOIN DTOSchueler s WHERE s.ID IN ?1 AND sla.Schueler_ID = s.ID AND sla.Schuljahresabschnitts_ID = s.Schuljahresabschnitts_ID AND sla.WechselNr = 0",
			DTOSchuelerLernabschnittsdaten.class, alleSchuelerIDs);
    	final Map<Long, DTOSchuelerLernabschnittsdaten> mapLernabschnitte = schuelerLernabschnittsdaten.stream().collect(Collectors.toMap(l -> l.Schueler_ID, l -> l));
    	// Filtere die Schüler auf die Schüler des Abiturjahrgangs
    	final List<DTOSchueler> result = new ArrayList<>();
    	for (final DTOSchueler schueler : alleSchueler) {
    		final DTOSchuljahresabschnitte schuljahresabschnitt = mapAbschnitte.get(schueler.Schuljahresabschnitts_ID);
    		if (schuljahresabschnitt == null)
    			continue;
    		final DTOSchuelerLernabschnittsdaten lernabschnitt = mapLernabschnitte.get(schueler.ID);
    		if (lernabschnitt == null)
    			continue;
    		final DTOJahrgang jahrgang = mapJahrgaenge.get(lernabschnitt.Jahrgang_ID);
    		if (jahrgang == null)
    			continue;
    		// Bestimme die Restjahre in Bezug auf den Abiturjahrgang und den Schuljahresabschnitt
    		final int restjahreNachAbiturjahr = abijahrgang - schuljahresabschnitt.Jahr;
    		final Integer restjahreNachJahrgang = JahrgangsUtils.getRestlicheJahre(schule.Schulform, jahrgang.Gliederung, jahrgang.ASDJahrgang);
			if ((restjahreNachJahrgang != null) && (restjahreNachAbiturjahr == restjahreNachJahrgang))
				result.add(schueler);
    	}
    	return result;
    }


    /**
     * Bestimmt alle Fachwahlen des angebebenen Abiturjahrgangs als Map von der ID des Schülers auf die jeweiligen Fachwahlen.
     *
     * @param conn          die Datenbankverbindung
     * @param abijahrgang   der Abiturjahrgang
     *
     * @return die Fachwahlen des Abiturjahrgangs als Map
     */
    public static Map<Long, GostJahrgangFachwahlen> getFachwahlenByAbiJahrgang(final DBEntityManager conn, final int abijahrgang) {
		final DTOEigeneSchule schule = DBUtilsGost.pruefeSchuleMitGOSt(conn);
		final Map<Long, GostJahrgangFachwahlen> result = new HashMap<>();
		// Lese die Fachliste aus der DB
		final Map<Long, DTOFach> faecher = conn.queryAll(DTOFach.class).stream().collect(Collectors.toMap(f -> f.ID, f -> f));
		if ((faecher == null) || (faecher.size() == 0))
	        throw OperationError.NOT_FOUND.exception("Es konnten keine Fächer in der Datenbank gefunden werden.");
		final Map<Long, Abiturdaten> mapAbiturdaten = getAbiturdaten(conn, schule, abijahrgang);
		// Erstelle die Fachwahl-Objekte
		for (final Abiturdaten abidaten : mapAbiturdaten.values()) {
			final GostJahrgangFachwahlen fachwahlen = new GostJahrgangFachwahlen();
			result.put(abidaten.schuelerID, fachwahlen);
			for (final AbiturFachbelegung belegung : abidaten.fachbelegungen) {
				final DTOFach fach = faecher.get(belegung.fachID);
				if (fach == null)
					continue;
				for (final GostHalbjahr halbjahr : GostHalbjahr.values()) {
					if (belegung.belegungen[halbjahr.id] != null) {
						final AbiturFachbelegungHalbjahr belegungHj = belegung.belegungen[halbjahr.id];
						final GostKursart kursart = GostKursart.fromKuerzel(belegungHj.kursartKuerzel);
						if (kursart == null)
							continue;
						final GostFachwahl fw = new GostFachwahl();
						fw.fachID = belegung.fachID;
						fw.schuelerID = abidaten.schuelerID;
						fw.kursartID = kursart.id;
						fw.istSchriftlich = belegungHj.schriftlich;
						fw.abiturfach = belegung.abiturFach;
						if (fachwahlen.halbjahr[halbjahr.id] == null)
							fachwahlen.halbjahr[halbjahr.id] = new GostJahrgangFachwahlenHalbjahr();
						fachwahlen.halbjahr[halbjahr.id].fachwahlen.add(fw);
					}
				}
				if ((belegung.abiturFach != null) && (belegung.belegungen[GostHalbjahr.Q22.id] != null)) {
					final AbiturFachbelegungHalbjahr belegungHj = belegung.belegungen[GostHalbjahr.Q22.id];
					final GostFachwahl fwAbi = new GostFachwahl();
					fwAbi.fachID = belegung.fachID;
					fwAbi.schuelerID = abidaten.schuelerID;
					fwAbi.kursartID = GostKursart.fromKuerzel(belegungHj.kursartKuerzel).id;
					fwAbi.istSchriftlich = belegungHj.schriftlich;
					fwAbi.abiturfach = belegung.abiturFach;
					fachwahlen.abitur.fachwahlen.add(fwAbi);
				}
			}
		}
		return result;
    }

}
