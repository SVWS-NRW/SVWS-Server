package de.svws_nrw.data.gost;

import java.util.Map;
import java.util.stream.Collectors;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;
import de.svws_nrw.core.data.gost.AbiturFachbelegung;
import de.svws_nrw.core.data.gost.AbiturFachbelegungHalbjahr;
import de.svws_nrw.core.data.gost.Abiturdaten;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.data.gost.GostLeistungen;
import de.svws_nrw.core.data.gost.GostLeistungenFachbelegung;
import de.svws_nrw.core.data.gost.GostLeistungenFachwahl;
import de.svws_nrw.core.data.schueler.Sprachendaten;
import de.svws_nrw.core.types.Note;
import de.svws_nrw.core.types.fach.ZulaessigesFach;
import de.svws_nrw.core.types.gost.GostAbiturFach;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.utils.gost.GostFaecherManager;
import de.svws_nrw.data.faecher.DBUtilsFaecherGost;
import de.svws_nrw.data.schule.SchulUtils;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangFachbelegungen;
import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangsdaten;
import de.svws_nrw.db.dto.current.gost.DTOGostSchueler;
import de.svws_nrw.db.dto.current.gost.DTOGostSchuelerFachbelegungen;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.utils.OperationError;
import jakarta.persistence.TypedQuery;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse stellt Hilfsmethoden für den Zugriff auf Informationen
 * zu der Laufbahnplanung von Schülern der gymnasialen Oberstufe zur Verfügung.
 */
public final class DBUtilsGostLaufbahn {

	private DBUtilsGostLaufbahn() {
		throw new IllegalStateException("Instantiation of " + DBUtilsGostLaufbahn.class.getName() + " not allowed");
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
    	DTOGostSchueler dtoGostSchueler = conn.queryByKey(DTOGostSchueler.class, id);
    	if (dtoGostSchueler == null) {
    		dtoGostSchueler = new DTOGostSchueler(id, false);
    		if (!conn.persist(dtoGostSchueler))
    			throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR.getStatusCode());
    	}
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
    	abidaten.sprachendaten = new Sprachendaten();    // TODO auch Default für den Jahrgang in der DB zur Verfügung stellen?
    	abidaten.sprachendaten.schuelerID = -1;
		abidaten.bilingualeSprache = null;               // TODO ggf. auch ein alternatives Defaulting für den bilingualen Zweig erlauben
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

}
