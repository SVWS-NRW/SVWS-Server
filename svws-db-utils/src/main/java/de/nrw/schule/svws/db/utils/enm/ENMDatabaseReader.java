package de.nrw.schule.svws.db.utils.enm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.nrw.schule.svws.core.data.enm.ENMDaten;
import de.nrw.schule.svws.core.data.enm.ENMFloskel;
import de.nrw.schule.svws.core.data.enm.ENMFloskelgruppe;
import de.nrw.schule.svws.core.data.enm.ENMJahrgang;
import de.nrw.schule.svws.core.data.enm.ENMKlasse;
import de.nrw.schule.svws.core.data.enm.ENMLerngruppe;
import de.nrw.schule.svws.core.data.enm.ENMSchueler;
import de.nrw.schule.svws.core.types.kurse.ZulaessigeKursart;
import de.nrw.schule.svws.core.utils.enm.ENMDatenManager;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.schild.faecher.DTOFach;
import de.nrw.schule.svws.db.dto.current.schild.katalog.DTOFloskelgruppen;
import de.nrw.schule.svws.db.dto.current.schild.katalog.DTOFloskeln;
import de.nrw.schule.svws.db.dto.current.schild.klassen.DTOKlassen;
import de.nrw.schule.svws.db.dto.current.schild.klassen.DTOKlassenLeitung;
import de.nrw.schule.svws.db.dto.current.schild.kurse.DTOKurs;
import de.nrw.schule.svws.db.dto.current.schild.lehrer.DTOLehrer;
import de.nrw.schule.svws.db.dto.current.schild.schueler.DTOSchueler;
import de.nrw.schule.svws.db.dto.current.schild.schule.DTOJahrgang;
import de.nrw.schule.svws.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.nrw.schule.svws.db.utils.OperationError;
import de.nrw.schule.svws.db.utils.data.Schule;
import de.nrw.schule.svws.db.utils.dto.enm.DTOENMLehrerSchuelerAbschnittsdaten;

/**
 * Ein Objekt dieser Klasse stellt Daten  für des Externen Notenmoduls (ENM) zusammen, 
 * indem es diese aus den einzelnen Tabellen der Datenbank ausliest und zusammenfasst. 
 * Das ENM ermöglicht das Anzeigen und Eingeben von Leistungsdaten der Lerngruppen 
 * eines Lehrers im aktuellen Lernabschnitt der Schule. 
 */
public class ENMDatabaseReader {

	/** Die Datenbankverbindung mit den Zugriffsrechten auf die SVWS-DB der Schule */
	private final DBEntityManager conn;
	
	/** Die ENM-Daten von diesem Lehrer werden aggregiert. */
	private final DTOLehrer dtoLehrer;
	
	/** Das Hilfsobjekt für den Zugriff auf schulspezifische Daten der SVWS-DB */
	private final Schule schule;
	
	/** Dieses Objekt wird mit den aggregierten ENMDaten befüllt und kann nach dem Ausführen des Konstruktors abgerufen werden. */
	private final ENMDatenManager manager = new ENMDatenManager();
	
	/** Eine Zuordnung aller LehrerDTOs aus der SVWS-DB der Schule zu ihrer ID. */
	private final Map<Long, DTOLehrer> mapLehrer;
	
	/** Eine Zuordnung aller SchülerDTOs aus der SVWS-DB der Schule zu ihrer ID.  */
	private final Map<Long, DTOSchueler> mapSchueler;
	
	/** Eine Zuordnung aller FächerDTOs aus der SVWS-DB der Schule zu ihrer ID. */
	private final Map<Long, DTOFach> mapFaecher; 
	
	/** Eine Zuordnung aller JahrgangDTOs aus der SVWS-DB der Schule zu ihrer ID. */
	private final Map<Long, DTOJahrgang> mapJahrgaenge;
	
	/** Eine Zuordnung aller KlassenDTOs aus der SVWS-DB der Schule zu ihrer ID. */
	private final Map<String, DTOKlassen> mapKlassen;
	
	/** Eine Zuordnung der Klassenlehrer zu den Klassen anhand der entsprechenden IDs. */
	private final Map<Long, List<DTOKlassenLeitung>> mapKlassenLeitung;

	/** Eine Zuordnung aller KursDTOs aus der SVWS-DB der Schule zu ihrer ID. */
	private final Map<Long, DTOKurs> mapKurse;

	
	/**
	 * Sammelt die JSon-Daten des Externen Notenmoduls (ENM) zu dem Lehrer, dessen ID übergeben wird, 
	 * bezogen auf den aktuellen Lernabschnitt. 
	 * 
	 * @param conn  die Datenbankverbindung zum Zugriff auf das SVWS-DB-Schema und mit der 
	 * 				Berechtigung für diese Abfrage.
	 * @param id    die ID des Lehrers dessen ENM-Daten abgefragt werden.
	 */
	public ENMDatabaseReader(final DBEntityManager conn, final long id) {
		this.conn = conn;
		dtoLehrer = conn.queryByKey(DTOLehrer.class, id);
    	if (dtoLehrer == null)
    		throw OperationError.NOT_FOUND.exception();    	
    	schule = Schule.query(conn);

    	// TODO Optimierung: Lese nur die benötigen Schueler und Lehrer und Kurse aus der Datenbank aus.
    	List<DTOLehrer> lehrer = conn.queryAll(DTOLehrer.class);
    	if (lehrer.size() == 0) 
    		throw OperationError.NOT_FOUND.exception();
    	mapLehrer = lehrer.stream().collect(Collectors.toMap(e -> e.ID, e -> e));
    	
    	mapSchueler = conn.queryAll(DTOSchueler.class).stream().collect(Collectors.toMap(s -> s.ID, s -> s)); 
    	if (mapSchueler.size() == 0) 
    		throw OperationError.NOT_FOUND.exception();
    	
    	mapFaecher = conn.queryAll(DTOFach.class).stream().collect(Collectors.toMap(f -> f.ID, f -> f));
    	if (mapFaecher.size() == 0) 
    		throw OperationError.NOT_FOUND.exception();
    	
    	mapJahrgaenge = conn.queryAll(DTOJahrgang.class).stream().collect(Collectors.toMap(j -> j.ID, j -> j));
    	if (mapJahrgaenge.size() == 0) 
    		throw OperationError.NOT_FOUND.exception();
    	
    	List<DTOKlassen> klassen = conn.queryNamed("DTOKlassen.schuljahresabschnitts_id", schule.dto.Schuljahresabschnitts_ID, DTOKlassen.class);
    	if (klassen.size() == 0) 
    		throw OperationError.NOT_FOUND.exception();
    	mapKlassen = klassen.stream().collect(Collectors.toMap(e -> e.Klasse, e -> e));
    	List<Long> klassenIDs = klassen.stream().map(kl -> kl.ID).collect(Collectors.toList());
    	mapKlassenLeitung = conn.queryNamed("DTOKlassenLeitung.klassen_id.multiple", klassenIDs, DTOKlassenLeitung.class).stream().collect(Collectors.groupingBy(kl -> kl.Klassen_ID));
    	
    	mapKurse = conn.queryAll(DTOKurs.class).stream().collect(Collectors.toMap(k -> k.ID, k -> k));
    	if (mapKurse == null) 
    		throw OperationError.NOT_FOUND.exception();
    	
       	getSchuldaten(schule);
    	initDaten();
    	getAbschnittsdaten();
	}
	
	
	/**
	 * Liefert das ENMDatenobjekt zurück, dass die ENMDaten zu dem entsprechenden Lehrer enthält.
	 * 
	 * @return die Daten des ENMModuls
	 */
	public ENMDaten getENMDaten() {
 		return manager.daten;
	}
	
	
	/**
	 * Aggregiert aus den Schülerabschnittsdaten und -leistungsdaten die einzelnen Informationen für das ENM. 
	 * Dabei wird unter anderem auch ermittelt, welche Kataloginformationen (Klassen, Lehrer, Jahrgänge) 
	 * bei den Daten für das ENM einzutragen sind.
	 */
	private void getAbschnittsdaten() {
    	List<DTOENMLehrerSchuelerAbschnittsdaten> schuelerabschnitte = 
    			DTOENMLehrerSchuelerAbschnittsdaten.query(conn, schule.dto.Schuljahresabschnitts_ID, dtoLehrer.Kuerzel);
    	for (DTOENMLehrerSchuelerAbschnittsdaten schuelerabschnitt : schuelerabschnitte) {
    		DTOKlassen dtoKlasse = mapKlassen.get(schuelerabschnitt.klasse);
			if (dtoKlasse == null) {
				// TODO DB-Error -> should not happen but must be handled
				throw new NullPointerException();
			}
    		ENMKlasse enmKlasse = manager.getKlasse(dtoKlasse.ID);
			if (enmKlasse == null) {
				manager.addKlasse(dtoKlasse.ID, dtoKlasse.ASDKlasse, dtoKlasse.Klasse,
						dtoKlasse.Sortierung);
				enmKlasse = manager.getKlasse(dtoKlasse.ID);
				for (DTOKlassenLeitung kl : mapKlassenLeitung.get(dtoKlasse.ID)) {
					if (manager.getLehrer(kl.Lehrer_ID) == null) {
						DTOLehrer dtoKlassenlehrer = mapLehrer.get(kl.Lehrer_ID);
						if (dtoKlassenlehrer != null) {
							manager.addLehrer(dtoKlassenlehrer.ID, dtoKlassenlehrer.Kuerzel, 
									          dtoKlassenlehrer.Nachname, dtoKlassenlehrer.Vorname, dtoKlassenlehrer.Geschlecht, dtoKlassenlehrer.eMailDienstlich);
						}
					}					
					enmKlasse.klassenlehrer.add(kl.Lehrer_ID);
				}
			}
    		
    		ZulaessigeKursart kursart = (schuelerabschnitt.kursID == null) ? null : ZulaessigeKursart.getByASDKursart(schuelerabschnitt.kursart);

    		ENMSchueler enmSchueler = manager.getSchueler(schuelerabschnitt.schuelerID);
    		if (enmSchueler == null) {
    			var dtoSchueler = mapSchueler.get(schuelerabschnitt.schuelerID);
    			ENMJahrgang enmJahrgang = manager.getJahrgang(schuelerabschnitt.jahrgangID);
    			if (enmJahrgang == null) {
    				DTOJahrgang dtoJahrgang = mapJahrgaenge.get(schuelerabschnitt.jahrgangID);
    				if (dtoJahrgang == null) {
    					// TODO DB-Error -> should not happen but must be handled
    					throw new NullPointerException();
    				}
    				manager.addJahrgang(dtoJahrgang.ID, dtoJahrgang.ASDJahrgang, dtoJahrgang.InternKrz,
    						dtoJahrgang.ASDBezeichnung, dtoJahrgang.Sekundarstufe, dtoJahrgang.Sortierung);
    				enmJahrgang = manager.getJahrgang(schuelerabschnitt.jahrgangID);
    			}
    			
    			manager.addSchueler(dtoSchueler.ID, schuelerabschnitt.jahrgangID, dtoKlasse.ID, dtoSchueler.Nachname, dtoSchueler.Vorname, 
    					            dtoSchueler.Geschlecht, schuelerabschnitt.BilingualerZweig, schuelerabschnitt.ZieldifferentesLernen,
    								dtoSchueler.Lernstandsbericht);  // Deutsch als Fremdsprache liegt vor, wenn für den Schüler Lernstandsberichte geschrieben werden...
    			enmSchueler = manager.getSchueler(schuelerabschnitt.schuelerID);
    			enmSchueler.lernabschnitt.id = schuelerabschnitt.abschnittID;
    			enmSchueler.lernabschnitt.pruefungsordnung = schuelerabschnitt.pruefungsordnung;
    			enmSchueler.lernabschnitt.lernbereich1note = schuelerabschnitt.lernbereich1note == null ? null : schuelerabschnitt.lernbereich1note.kuerzel;
    			enmSchueler.lernabschnitt.lernbereich2note = schuelerabschnitt.lernbereich2note == null ? null : schuelerabschnitt.lernbereich2note.kuerzel;
    			enmSchueler.lernabschnitt.foerderschwerpunkt1 = schuelerabschnitt.foerderschwerpunkt1Kuerzel;
    			enmSchueler.lernabschnitt.foerderschwerpunkt2 = schuelerabschnitt.foerderschwerpunkt2Kuerzel;    			
    		}

    		// Hier wird die temporäre LerngruppenID erstellt, mit der in der Klasse ENMDaten gearbeitet wird. 
    		// Es wird eine eindeutige ID benötigt, die Kurs- und Klassenübergreifend diese Lerngruppe identifiziert.
    		String strLerngruppenID = (schuelerabschnitt.kursID == null) 
    			? "Klasse:" + schuelerabschnitt.klasse + "/" + schuelerabschnitt.fachID 
    			: "Kurs:" + schuelerabschnitt.kursID;
    		ENMLerngruppe lerngruppe = manager.getLerngruppe(strLerngruppenID);
    		if (lerngruppe == null) {
				DTOFach fach = mapFaecher.get(schuelerabschnitt.fachID);
				String kursartAllg = (kursart == null) ? null : (kursart.daten.kuerzelAllg == null) || "".equals(kursart.daten.kuerzelAllg)
						? kursart.daten.kuerzel : kursart.daten.kuerzelAllg;
				// Fach zu ENMDaten hinzufügen ?
				if (manager.getFach(fach.ID) == null)
					manager.addFach(fach.ID, fach.StatistikFach.daten.kuerzelASD, fach.Kuerzel, fach.SortierungAllg, fach.IstFremdsprache);
				// Unterscheidung zwischen den beiden Lerngruppen-Typen...
    			if (schuelerabschnitt.kursID == null) {  // es ist eine Klasse
    				manager.addLerngruppe(strLerngruppenID, dtoKlasse.ID, schuelerabschnitt.fachID, null, 
        					            fach.Kuerzel, kursartAllg, fach.Unterichtssprache, 
        					            schuelerabschnitt.wochenstunden == null ? 0 : schuelerabschnitt.wochenstunden);
    			} else {  // es ist ein Kurs
    				DTOKurs kurs = mapKurse.get(schuelerabschnitt.kursID);
    				manager.addLerngruppe(strLerngruppenID, schuelerabschnitt.kursID, schuelerabschnitt.fachID,
        					kursart == null ? -1 : Integer.parseInt(kursart.daten.nummer), kurs.KurzBez, kursartAllg, 
        					fach.Unterichtssprache, kurs.WochenStd);
    			}
    			lerngruppe = manager.getLerngruppe(strLerngruppenID);
    			lerngruppe.lehrerID.add(dtoLehrer.ID);
				// TODO ggf. im Team-Teaching unterrichtende Lehrer hinzufügen (Zusatzkraft in Leistungsdaten bzw. weitere Lehrkraft bei Kursen)
    		}
    		
    		boolean istSchriftlich = (schuelerabschnitt.kursID != null)
    			&& ((kursart == ZulaessigeKursart.LK1) || (kursart == ZulaessigeKursart.LK2) 
    				|| (kursart == ZulaessigeKursart.GKS) || (kursart == ZulaessigeKursart.AB3) 
    				|| ((kursart == ZulaessigeKursart.AB4) && (!("Q2".equals(dtoKlasse.ASDKlasse) && (schule.getHalbjahr() == 2))))); 
    		
    		String tmpAbiFach = schuelerabschnitt.AbiturFach;   // TODO check whether to remove this workaround (Eclipse-Compiler-Bug) - move into switch(schuelerabschnitt.AbiturFach)
    		Integer abiFach = (tmpAbiFach == null) ? null : switch(tmpAbiFach) {
    			case "1" -> 1; 
    			case "2" -> 2; 
    			case "3" -> 3; 
    			case "4" -> 4;
    			default -> null;
    		};
    		boolean istGemahnt = (schuelerabschnitt.istGemahnt == null) ? false : schuelerabschnitt.istGemahnt;
    		String mahndatum = schuelerabschnitt.mahndatum;
    		manager.addSchuelerLeistungsdaten(enmSchueler, schuelerabschnitt.leistungID, lerngruppe.id, schuelerabschnitt.note.kuerzel,
    				istSchriftlich, abiFach, schuelerabschnitt.fehlstundenGesamt, 
    				schuelerabschnitt.fehlstundenUnentschuldigt, schuelerabschnitt.fachbezogeneBemerkungen, null,
    				istGemahnt, mahndatum);
    		// TODO get and add Teilleistungen
    		// TODO check and add ZP10 - Data
    		// TODO check and add BKAbschluss - Data
    	}
	}


	/**
	 * Füllt die grundlegenden Informationen der Schule in die ENMDaten.
	 * 
	 * @param schule  die Schule, deren Informationen übertragen werden
	 * @param daten   das ENMDaten-Objekt, welches befüllt wird.
	 */
	private void getSchuldaten(Schule schule) {
		DTOSchuljahresabschnitte abschnitt = conn.queryByKey(DTOSchuljahresabschnitte.class, schule.dto.Schuljahresabschnitts_ID);
		// Grundlegene Daten der Schule eintragen.
		manager.daten.schulnummer = schule.dto.SchulNr;
		manager.daten.schuljahr = abschnitt.Jahr;
		manager.daten.anzahlAbschnitte = schule.dto.AnzahlAbschnitte;
		manager.daten.aktuellerAbschnitt = abschnitt.Abschnitt;
		manager.daten.publicKey = null; // TODO
		manager.daten.fehlstundenEingabe = true; // TODO
		manager.daten.fehlstundenSIFachbezogen = false;  // TODO
		manager.daten.fehlstundenSIIFachbezogen = true;  // TODO
		manager.daten.schulform = schule.dto.Schulform.daten.kuerzel;
		manager.daten.mailadresse = null;  // TODO
	}

	
	/**
	 * Initialisiert die grundlegenden Daten des ENMDatenobjekts. Dazu gehören: <br> 
	 * <ul>
	 * <li> das LehrerDTO zu dem Lehrer für den die ENM Datei erstellt wird </li> 
	 * <li> der Notenkatalog </li>
	 * <li> die Förderschwerpunkte </li>
	 * <li> die Floskeln </li> 
	 * </ul>
	 */
	private void initDaten() {
    	// Lehrer hinzufügen, für den die ENM Datei erstellt wird. 
		manager.daten.lehrerID = dtoLehrer.ID;
		manager.addLehrer(manager.daten.lehrerID, dtoLehrer.Kuerzel, dtoLehrer.Nachname, dtoLehrer.Vorname, dtoLehrer.Geschlecht, dtoLehrer.eMailDienstlich);

    	// Kopiere den Noten-Katalog aus dem Core-type in die ENM-Daten
		manager.addNoten();
    	
    	// Kopiere den Förderschwerpunkt-Katalog aus dem Core-type in die ENM-Daten
		manager.addFoerderschwerpunkte(schule.dto.Schulform);
    	
    	// Kopiere den Floskel-Katalog in die ENM-Daten
    	getFloskeln();
	}
	
	
	/**
	 * Füllt die Datenstruktur für die Floskelgruppen des ENM mit den in der SVWS-DB hinterlegten 
	 * Floskelgruppen und den zugehörigen Floskeln.
	 */
	private void getFloskeln() {
		List<DTOFloskelgruppen> dtoFloskelgruppen = conn.queryAll(DTOFloskelgruppen.class);
		HashMap<String, ENMFloskelgruppe> map = new HashMap<>();
		for (DTOFloskelgruppen dto : dtoFloskelgruppen) {
			ENMFloskelgruppe enmFG = new ENMFloskelgruppe();
			enmFG.kuerzel = dto.Kuerzel;
			enmFG.bezeichnung = dto.Bezeichnung;
			enmFG.hauptgruppe = dto.Hauptgruppe;
			map.put(enmFG.kuerzel, enmFG);
			manager.daten.floskelgruppen.add(enmFG);
		}
		List<DTOFloskeln> dtoFloskeln = conn.queryAll(DTOFloskeln.class);
		for (DTOFloskeln dto : dtoFloskeln) {
			ENMFloskel enmFl = new ENMFloskel();
			enmFl.kuerzel = dto.Kuerzel;
			enmFl.text = dto.FloskelText;
			// TODO	enmFl.fachID = dto.FloskelFach();
			try {
				enmFl.niveau = Long.parseLong(dto.FloskelNiveau);
			} catch (NumberFormatException e) {
				enmFl.niveau = null;
			}
			// TODO	enmFl.jahrgangID = dto.FloskelJahrgang();
			ENMFloskelgruppe enmFG = map.get(dto.FloskelGruppe);
			if (enmFG != null) {
				enmFG.floskeln.add(enmFl);
			} else {
				// TODO Fehlerbehandlung: Wie ordnet man die Floskel zu? -> allgemein
			}
		}
	}
	
}
