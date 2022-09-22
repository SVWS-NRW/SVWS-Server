package de.nrw.schule.svws.db.utils.gost;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import de.nrw.schule.svws.core.data.gost.AbiturFachbelegung;
import de.nrw.schule.svws.core.data.gost.AbiturFachbelegungHalbjahr;
import de.nrw.schule.svws.core.data.gost.Abiturdaten;
import de.nrw.schule.svws.core.data.gost.GostFach;
import de.nrw.schule.svws.core.data.gost.GostLeistungen;
import de.nrw.schule.svws.core.data.gost.GostLeistungenFachbelegung;
import de.nrw.schule.svws.core.data.gost.GostLeistungenFachwahl;
import de.nrw.schule.svws.core.types.Note;
import de.nrw.schule.svws.core.types.gost.AbiturBelegungsart;
import de.nrw.schule.svws.core.types.gost.GostAbiturFach;
import de.nrw.schule.svws.core.types.gost.GostHalbjahr;
import de.nrw.schule.svws.core.types.gost.GostKursart;
import de.nrw.schule.svws.core.types.statkue.ZulaessigesFach;
import de.nrw.schule.svws.core.utils.gost.GostFaecherManager;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.schild.schueler.DTOSchueler;
import de.nrw.schule.svws.db.dto.current.schild.schueler.DTOSchuelerSprachenfolge;
import de.nrw.schule.svws.db.dto.current.schild.schueler.DTOSchuelerSprachpruefungen;
import de.nrw.schule.svws.db.dto.current.schild.schueler.abitur.DTOSchuelerAbitur;
import de.nrw.schule.svws.db.dto.current.schild.schueler.abitur.DTOSchuelerAbiturFach;
import de.nrw.schule.svws.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.nrw.schule.svws.db.utils.OperationError;



/**
 * Diese Klasse stellt Hilfsmethoden für den Zugriff auf Informationen 
 * zu dem Abiturbereich von Schülern der gymnasialen Oberstufe zur Verfügung.
 */
public class GostSchuelerAbitur {

	/**
	 * Ermittelt die für das Abitur relevanten Daten für den Schüler mit der angegebenen 
	 * ID aus den Leistungsdaten in der Datenbank.  
	 * 
	 * @param conn   die Datenbankverbindung
	 * @param id     die ID des Schülers
	 * 
	 * @return die für das Abitur relevanten Daten für den Schüler mit der angegebenen ID
	 */
    public static Abiturdaten getAbiturdatenAusLeistungsdaten(DBEntityManager conn, long id) {
    	GostLeistungen leistungen = GostSchueler.getLeistungsdaten(conn, id);
    	if (leistungen == null)
    		throw new WebApplicationException(Status.NOT_FOUND.getStatusCode());
    	
    	// TODO bestimme ggf. einen Teil der Daten aus den LuPO-Wahlen des Schülers
    	
		if (!"Q2".equals(leistungen.aktuellerJahrgang))
			return null;
		Abiturdaten abidaten = new Abiturdaten();
		abidaten.schuelerID = leistungen.id;
		abidaten.schuljahrAbitur = leistungen.aktuellesSchuljahr;
		abidaten.abiturjahr = abidaten.schuljahrAbitur + 1;
		abidaten.sprachendaten = leistungen.sprachendaten;
		abidaten.bilingualeSprache = leistungen.bilingualeSprache;
		abidaten.projektKursThema = leistungen.projektkursThema;
		abidaten.projektkursLeitfach1Kuerzel = leistungen.projektkursLeitfach1Kuerzel;
		abidaten.projektkursLeitfach2Kuerzel = leistungen.projektkursLeitfach2Kuerzel;
		for (GostHalbjahr hj : GostHalbjahr.values())
			abidaten.bewertetesHalbjahr[hj.id] = leistungen.bewertetesHalbjahr[hj.id];
		for (GostLeistungenFachwahl leistungenFach : leistungen.faecher) {
			GostHalbjahr letzteBelegungHalbjahr = null;   // das Halbjahr der letzten Belegung 
			AbiturFachbelegung fach = new AbiturFachbelegung();
			fach.fachID = leistungenFach.fach.id;
			fach.istFSNeu = leistungenFach.istFSNeu;
			fach.abiturFach = GostAbiturFach.fromID(leistungenFach.abiturfach) == null ? null : leistungenFach.abiturfach;
			for (GostLeistungenFachbelegung leistungenBelegung : leistungenFach.belegungen) {
				if (!leistungenBelegung.abschnittGewertet)
					continue;

				// Nehme jeweils die Kursart, welche beim letzten gewerteten Abschnitt eingetragen ist
				if ((letzteBelegungHalbjahr == null || GostHalbjahr.fromKuerzel(leistungenBelegung.halbjahrKuerzel).compareTo(letzteBelegungHalbjahr) > 0) 
						&& (GostHalbjahr.fromKuerzel(leistungenBelegung.halbjahrKuerzel) != null) && leistungenBelegung.abschnittGewertet) {
					letzteBelegungHalbjahr = GostHalbjahr.fromKuerzel(leistungenBelegung.halbjahrKuerzel);
					fach.letzteKursart = (GostKursart.fromKuerzel(leistungenBelegung.kursartKuerzel) == null) ? null : GostKursart.fromKuerzel(leistungenBelegung.kursartKuerzel).kuerzel;
				}

				// Erzeuge die zugehörige Belegung
				AbiturFachbelegungHalbjahr belegung = new AbiturFachbelegungHalbjahr();
				belegung.halbjahrKuerzel = (GostHalbjahr.fromKuerzel(leistungenBelegung.halbjahrKuerzel) == null) ? null : GostHalbjahr.fromKuerzel(leistungenBelegung.halbjahrKuerzel).kuerzel;
				belegung.kursartKuerzel = (GostKursart.fromKuerzel(leistungenBelegung.kursartKuerzel) == null) ? null : GostKursart.fromKuerzel(leistungenBelegung.kursartKuerzel).kuerzel;
				belegung.schriftlich = leistungenBelegung.istSchriftlich;
				belegung.biliSprache = leistungenBelegung.bilingualeSprache;
				belegung.lehrer = leistungenBelegung.lehrer;
				belegung.wochenstunden = leistungenBelegung.wochenstunden;
				belegung.fehlstundenGesamt = leistungenBelegung.fehlstundenGesamt;
				belegung.fehlstundenUnentschuldigt = leistungenBelegung.fehlstundenUnentschuldigt;
				belegung.notenkuerzel = Note.fromKuerzel(leistungenBelegung.notenKuerzel).kuerzel;				
				fach.belegungen[GostHalbjahr.fromKuerzel(belegung.halbjahrKuerzel).id] = belegung;
			}
			abidaten.fachbelegungen.add(fach);
		}

		// Bestimmt die Fehlstunden-Summe für den Block I (Qualifikationsphase) anhand der Fehlstunden bei den einzelnen Kurs-Belegungen.
		int block1FehlstundenGesamt = 0;
		int block1FehlstundenUnentschuldigt = 0;
		for (AbiturFachbelegung fach : abidaten.fachbelegungen) {
			for (AbiturFachbelegungHalbjahr belegung : fach.belegungen) {
				if ((belegung == null) || !GostHalbjahr.fromKuerzel(belegung.halbjahrKuerzel).istQualifikationsphase())
					continue;
				block1FehlstundenGesamt += belegung.fehlstundenGesamt;
				block1FehlstundenUnentschuldigt += belegung.fehlstundenUnentschuldigt;
			}
		}
		abidaten.block1FehlstundenGesamt = block1FehlstundenGesamt;
		abidaten.block1FehlstundenUnentschuldigt = block1FehlstundenUnentschuldigt;		
		
		// und gib die Abiturdaten zurück...
		return abidaten;    	
    }

    
    
	/**
	 * Ermittelt die für das Abitur relevanten Daten für den Schüler mit der angegebenen 
	 * ID aus den in der Datenbank gespeicherten Abiturtabellen.
	 * 
	 * @param conn   die Datenbank-Verbindung
	 * @param id     die ID des Schülers
	 * 
	 * @return die für das Abitur relevanten Daten für den Schüler mit der angegebenen ID
	 */
    public static Abiturdaten getAbiturdaten(DBEntityManager conn, long id) {
		// Ermittle für einen Vergleich die Abiturdaten für Block I aus den Leistungsdaten, nutze dafür den entsprechenden Service
    	Abiturdaten abidatenVergleich = getAbiturdatenAusLeistungsdaten(conn, id);

    	// Ermittle nun zunächst die Abiturdaten aus den entsprechenden Tabellen
    	DTOSchueler dtoSchueler = conn.queryByKey(DTOSchueler.class, id);
    	if (dtoSchueler == null)
			throw OperationError.NOT_FOUND.exception();

		final Map<Long, DTOSchuljahresabschnitte> schuljahresabschnitte = conn.queryAll(DTOSchuljahresabschnitte.class).stream().collect(Collectors.toMap(a -> a.ID, a -> a));
		DTOSchuljahresabschnitte dtoAbschnitt = schuljahresabschnitte.get(dtoSchueler.Schuljahresabschnitts_ID);
		if (dtoAbschnitt == null)
			throw OperationError.NOT_FOUND.exception();
		    	
		// Lese die Abiturdaten anhand der ID aus der Datenbank 
		List<DTOSchuelerAbitur> dtosSchuelerAbitur = conn.queryNamed("DTOSchuelerAbitur.schueler_id", id, DTOSchuelerAbitur.class);
		if ((dtosSchuelerAbitur == null) || (dtosSchuelerAbitur.size() == 0))
			throw new WebApplicationException(Status.NOT_FOUND.getStatusCode());
		// TODO if (dtosSchuelerAbitur.size() > 1) - Es existieren mehrere Abiturdatensätze für den Schüler mit der ID - TODO neueren Jahrgang auswählen
		DTOSchuelerAbitur dtoSchuelerAbitur = dtosSchuelerAbitur.get(0);
		List<DTOSchuelerAbiturFach> faecher = conn.queryNamed("DTOSchuelerAbiturFach.schueler_id", id, DTOSchuelerAbiturFach.class);
		if (faecher == null) 
			throw new WebApplicationException(Status.NOT_FOUND.getStatusCode());

        // Lese beide Tabellen mit den Informationen zu den belegten oder geprüften Sprachen aus.
		List<DTOSchuelerSprachenfolge> sprachenfolge = conn.queryNamed("DTOSchuelerSprachenfolge.schueler_id", id, DTOSchuelerSprachenfolge.class);
        List<DTOSchuelerSprachpruefungen> sprachpruefungen = conn.queryNamed("DTOSchuelerSprachpruefungen.schueler_id", id, DTOSchuelerSprachpruefungen.class);
		
		DTOSchuljahresabschnitte dtoAbschnittPruefung = schuljahresabschnitte.get(dtoSchuelerAbitur.Schuljahresabschnitts_ID);
		
		// Bestimme zunächst das Abiturjahr
		Integer abiturjahr = null;
		if (dtoSchuelerAbitur.Schuljahresabschnitts_ID != null) {
			DTOSchuljahresabschnitte abschnitt = conn.queryByKey(DTOSchuljahresabschnitte.class, dtoSchuelerAbitur.Schuljahresabschnitts_ID); 
			if (abschnitt != null)
				abiturjahr = abschnitt.Jahr + 1; 
		}
		if (abiturjahr == null)
			abiturjahr = abidatenVergleich.abiturjahr;
		
		// Lese die Oberstufenfächer aus der DB ein, um schnell Daten zu einzelnen Fächern nachschlagen zu können
    	GostFaecherManager gostFaecher = FaecherGost.getFaecherListeGost(conn, abiturjahr);
    			
		// Kopiere die DTOs in die Abiturdaten-Klasse 
		Abiturdaten abidaten = new Abiturdaten();
		abidaten.schuelerID = dtoSchuelerAbitur.Schueler_ID;
		abidaten.schuljahrAbitur = dtoAbschnittPruefung == null ? null : dtoAbschnittPruefung.Jahr;
		abidaten.abiturjahr = abiturjahr;
        // TODO IMPLEMENTATION SPRACHPRÜFUNGEN: Alte LuPO Einträge zur manuellen Prüfung von Fremdsprachen können entfernt werden.
		abidaten.sek1Fremdsprache2ManuellGeprueft = dtoSchuelerAbitur.FremdspracheSekIManuellGeprueft;
		abidaten.projektKursThema = dtoSchuelerAbitur.ProjektkursThema;
		abidaten.block1FehlstundenGesamt = dtoSchuelerAbitur.FehlstundenSumme == null ? -1 : dtoSchuelerAbitur.FehlstundenSumme;
		abidaten.block1FehlstundenUnentschuldigt = dtoSchuelerAbitur.FehlstundenSummeUnentschuldigt == null ? -1 : dtoSchuelerAbitur.FehlstundenSummeUnentschuldigt;
		abidaten.latinum = false;
		for (DTOSchuelerSprachenfolge folge : sprachenfolge)
			if (ZulaessigesFach.L.daten.kuerzel.equals(folge.Sprache) && (folge.LatinumErreicht != null) && (folge.LatinumErreicht == true))
				abidaten.latinum = true;
		abidaten.kleinesLatinum = false;
		for (DTOSchuelerSprachenfolge folge : sprachenfolge)
			if (ZulaessigesFach.L.daten.kuerzel.equals(folge.Sprache) && (folge.KleinesLatinumErreicht != null) && (folge.KleinesLatinumErreicht == true))
				abidaten.kleinesLatinum = true;
		abidaten.graecum = false;
		for (DTOSchuelerSprachenfolge folge : sprachenfolge)
			if (ZulaessigesFach.G.daten.kuerzel.equals(folge.Sprache) && (folge.GraecumErreicht != null) && (folge.GraecumErreicht == true))
				abidaten.graecum = true;
		abidaten.hebraicum = false;
		for (DTOSchuelerSprachenfolge folge : sprachenfolge)
			if (ZulaessigesFach.H.daten.kuerzel.equals(folge.Sprache) && (folge.HebraicumErreicht != null) && (folge.HebraicumErreicht == true))
				abidaten.hebraicum = true;
		abidaten.besondereLernleistung = dtoSchuelerAbitur.BesondereLernleistungArt.kuerzel;
		abidaten.besondereLernleistungNotenKuerzel = dtoSchuelerAbitur.BesondereLernleistungNotenpunkte.kuerzel;
		abidaten.besondereLernleistungThema = dtoSchuelerAbitur.BesondereLernleistungThema;
		
		abidaten.block1AnzahlKurse = dtoSchuelerAbitur.BlockI_AnzahlKurseEingebracht;
		abidaten.block1DefiziteGesamt = dtoSchuelerAbitur.BlockI_AnzahlDefiziteEingebracht;
		abidaten.block1DefiziteLK = dtoSchuelerAbitur.BlockI_AnzahlDefiziteLK;
		abidaten.block1PunktSummeGK = dtoSchuelerAbitur.BlockI_SummeNotenpunkteGK;
		abidaten.block1PunktSummeLK = dtoSchuelerAbitur.BlockI_SummeNotenpunkteLK;
		abidaten.block1PunktSummeNormiert = dtoSchuelerAbitur.BlockI_PunktsummeNormiert;
		abidaten.block1NotenpunkteDurchschnitt = dtoSchuelerAbitur.BlockI_NotenpunktdurchschnittEingebrachterKurse;
		abidaten.block1Zulassung = !"-".equals(dtoSchuelerAbitur.BlockI_HatZulassung);
		abidaten.freiwilligerRuecktritt = "R".equals(dtoSchuelerAbitur.BlockI_HatZulassung);
		
		abidaten.block2DefiziteGesamt = dtoSchuelerAbitur.Pruefung_AnzahlDefizite;
		abidaten.block2DefiziteLK = dtoSchuelerAbitur.Pruefung_AnzahlDefiziteLK;
		abidaten.block2PunktSumme = dtoSchuelerAbitur.Pruefung_Punktsumme;
		
		abidaten.gesamtPunkte = dtoSchuelerAbitur.AbiturGesamtPunktzahl;
		abidaten.gesamtPunkteVerbesserung = dtoSchuelerAbitur.VerbesserungAbPunktzahl;
		abidaten.pruefungBestanden = dtoSchuelerAbitur.Pruefung_hatBestanden;
		abidaten.note = dtoSchuelerAbitur.AbiturNote;
		
		// Füge die Fächerdaten hinzu...
		for (DTOSchuelerAbiturFach dto : faecher) {
			AbiturFachbelegung fach = new AbiturFachbelegung();
			GostFach gostFach = gostFaecher.get(dto.Fach_ID);
			fach.fachID = gostFach.id;
			fach.letzteKursart = dto.KursartAllgemein == null ? null : dto.KursartAllgemein.kuerzel;
			fach.abiturFach = dto.AbiturFach == null ? null : dto.AbiturFach.id;
			if (dto.KursartAllgemein == GostKursart.PJK) {
				abidaten.projektkursLeitfach1Kuerzel = gostFach.projektKursLeitfach1Kuerzel;
				abidaten.projektkursLeitfach2Kuerzel = gostFach.projektKursLeitfach2Kuerzel;
			}
			fach.block1PunktSumme = dto.ZulassungPunktsumme;
			fach.block1NotenpunkteDurchschnitt = dto.ZulassungNotenpunktdurchschnitt;

			fach.block2NotenKuerzelPruefung = dto.PruefungNotenpunkte.kuerzel;
			fach.block2PunkteZwischenstand = dto.PruefungPunktsummeZwischenstand;
			fach.block2MuendlichePruefungAbweichung = dto.PruefungMuendlichAbweichung;
			fach.block2MuendlichePruefungBestehen = dto.PruefungMuendlichBestehen;
			fach.block2MuendlichePruefungFreiwillig = dto.PruefungMuendlichFreiwillig;
			fach.block2MuendlichePruefungReihenfolge = dto.PruefungMuendlichReihenfolge;
			fach.block2MuendlichePruefungNotenKuerzel = dto.PruefungNotenpunkte.kuerzel;
			fach.block2Punkte = dto.PruefungPunktsummeGesamt;
			fach.block2Pruefer = dto.Fachlehrer_ID;
			if (dto.EF_HJ1_BelegungArt != AbiturBelegungsart.NICHT_BELEGT) {
				AbiturFachbelegungHalbjahr ef1 = new AbiturFachbelegungHalbjahr();
				ef1.halbjahrKuerzel = GostHalbjahr.EF1.kuerzel;
				ef1.notenkuerzel = dto.EF_HJ1_Notenpunkte.kuerzel;
				ef1.schriftlich = (dto.EF_HJ1_BelegungArt == AbiturBelegungsart.SCHRIFTLICH);
				fach.belegungen[GostHalbjahr.EF1.id] = ef1;
			}
			if (dto.EF_HJ2_BelegungArt != AbiturBelegungsart.NICHT_BELEGT) {
				AbiturFachbelegungHalbjahr ef2 = new AbiturFachbelegungHalbjahr();
				ef2.halbjahrKuerzel = GostHalbjahr.EF2.kuerzel;
				ef2.notenkuerzel = dto.EF_HJ2_Notenpunkte.kuerzel;
				ef2.schriftlich = (dto.EF_HJ2_BelegungArt == AbiturBelegungsart.SCHRIFTLICH);
				fach.belegungen[GostHalbjahr.EF2.id] = ef2;
			}
			if (dto.Q1_HJ1_BelegungArt != AbiturBelegungsart.NICHT_BELEGT) {
				AbiturFachbelegungHalbjahr q11 = new AbiturFachbelegungHalbjahr();
				q11.halbjahrKuerzel = GostHalbjahr.Q11.kuerzel;
				q11.notenkuerzel = dto.Q1_HJ1_Notenpunkte.kuerzel;
				q11.schriftlich = (dto.Q1_HJ1_BelegungArt == AbiturBelegungsart.SCHRIFTLICH);
				q11.wochenstunden = dto.Q1_HJ1_Wochenstunden;
				q11.block1gewertet = dto.Q1_HJ1_MarkiertFuerAbiturBerechnung.fuerBerechnung;
				q11.block1kursAufZeugnis = dto.Q1_HJ1_MarkiertFuerAbiturBerechnung.aufAbiturZeugnis;
				fach.belegungen[GostHalbjahr.Q11.id] = q11;
			}
			if (dto.Q1_HJ2_BelegungArt != AbiturBelegungsart.NICHT_BELEGT) {
				AbiturFachbelegungHalbjahr q12 = new AbiturFachbelegungHalbjahr();
				q12.halbjahrKuerzel = GostHalbjahr.Q12.kuerzel;
				q12.notenkuerzel = dto.Q1_HJ2_Notenpunkte.kuerzel;
				q12.schriftlich = (dto.Q1_HJ2_BelegungArt == AbiturBelegungsart.SCHRIFTLICH);
				q12.wochenstunden = dto.Q1_HJ2_Wochenstunden;
				q12.block1gewertet = dto.Q1_HJ2_MarkiertFuerAbiturBerechnung.fuerBerechnung;
				q12.block1kursAufZeugnis = dto.Q1_HJ2_MarkiertFuerAbiturBerechnung.aufAbiturZeugnis;
				fach.belegungen[GostHalbjahr.Q12.id] = q12;
			}
			if (dto.Q2_HJ1_BelegungArt != AbiturBelegungsart.NICHT_BELEGT) {
				AbiturFachbelegungHalbjahr q21 = new AbiturFachbelegungHalbjahr();
				q21.halbjahrKuerzel = GostHalbjahr.Q21.kuerzel;
				q21.notenkuerzel = dto.Q2_HJ1_Notenpunkte.kuerzel;
				q21.schriftlich = (dto.Q2_HJ1_BelegungArt == AbiturBelegungsart.SCHRIFTLICH);
				q21.wochenstunden = dto.Q2_HJ1_Wochenstunden;
				q21.block1gewertet = dto.Q2_HJ1_MarkiertFuerAbiturBerechnung.fuerBerechnung;
				q21.block1kursAufZeugnis = dto.Q2_HJ1_MarkiertFuerAbiturBerechnung.aufAbiturZeugnis;
				fach.belegungen[GostHalbjahr.Q21.id] = q21;
			}
			if (dto.Q2_HJ2_BelegungArt != AbiturBelegungsart.NICHT_BELEGT) {
				AbiturFachbelegungHalbjahr q22 = new AbiturFachbelegungHalbjahr();
				q22.halbjahrKuerzel = GostHalbjahr.Q22.kuerzel;
				q22.notenkuerzel = dto.Q2_HJ2_Notenpunkte.kuerzel;
				q22.schriftlich = (dto.Q2_HJ2_BelegungArt == AbiturBelegungsart.SCHRIFTLICH);
				q22.wochenstunden = dto.Q2_HJ2_Wochenstunden;
				q22.block1gewertet = dto.Q2_HJ2_MarkiertFuerAbiturBerechnung.fuerBerechnung;
				q22.block1kursAufZeugnis = dto.Q2_HJ2_MarkiertFuerAbiturBerechnung.aufAbiturZeugnis;
				fach.belegungen[GostHalbjahr.Q22.id] = q22;
			}		
			abidaten.fachbelegungen.add(fach);
		}
		
		// Markiere alles Gost-Halbjahre als gewertet
		for (GostHalbjahr hj : GostHalbjahr.values())
			abidaten.bewertetesHalbjahr[hj.id] = true;
		
		// Kopiere Abiturdaten, welche nicht in den Abitur-DB-Tabellen direkt vorhanden sind
		abidaten.sprachendaten = abidatenVergleich.sprachendaten;
		abidaten.bilingualeSprache = abidatenVergleich.bilingualeSprache;
		for (AbiturFachbelegung fach : abidaten.fachbelegungen) {
			AbiturFachbelegung fachVergleich = null;
			for (AbiturFachbelegung f : abidatenVergleich.fachbelegungen) {
				if (f.fachID == fach.fachID) {
					fachVergleich = f;
					break;
				}
			}
			if (fachVergleich == null)
				continue;
			fach.istFSNeu = fachVergleich.istFSNeu;
			for (AbiturFachbelegungHalbjahr belegung : fach.belegungen) {
				if (belegung == null)
					continue;
				AbiturFachbelegungHalbjahr belegungVergleich = fachVergleich.belegungen[GostHalbjahr.fromKuerzel(belegung.halbjahrKuerzel).id];
				if (belegungVergleich == null)
					continue;
				if (GostHalbjahr.fromKuerzel(belegung.halbjahrKuerzel).istEinfuehrungsphase()) {
					belegung.wochenstunden = belegungVergleich.wochenstunden;
				}
				GostKursart tmpKursart = GostKursart.fromKuerzel(belegungVergleich.kursartKuerzel); 
				belegung.kursartKuerzel = (tmpKursart == null) ? null : tmpKursart.kuerzel;
				fach.letzteKursart = belegung.kursartKuerzel;
				belegung.biliSprache = belegungVergleich.biliSprache;
				belegung.lehrer = belegungVergleich.lehrer;
				belegung.fehlstundenGesamt = belegungVergleich.fehlstundenGesamt;
				belegung.fehlstundenUnentschuldigt = belegungVergleich.fehlstundenUnentschuldigt;
			}
		}
		
		// gib die Abiturdaten zurück.
		return abidaten;    	
    }
	
}
