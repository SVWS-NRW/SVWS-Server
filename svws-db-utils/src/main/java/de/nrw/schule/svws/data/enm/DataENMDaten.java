package de.nrw.schule.svws.data.enm;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.nrw.schule.svws.core.data.enm.ENMDaten;
import de.nrw.schule.svws.core.data.enm.ENMFach;
import de.nrw.schule.svws.core.data.enm.ENMFloskel;
import de.nrw.schule.svws.core.data.enm.ENMFloskelgruppe;
import de.nrw.schule.svws.core.data.enm.ENMJahrgang;
import de.nrw.schule.svws.core.data.enm.ENMKlasse;
import de.nrw.schule.svws.core.data.enm.ENMLerngruppe;
import de.nrw.schule.svws.core.data.enm.ENMSchueler;
import de.nrw.schule.svws.core.types.SchuelerStatus;
import de.nrw.schule.svws.core.types.kurse.ZulaessigeKursart;
import de.nrw.schule.svws.core.utils.enm.ENMDatenManager;
import de.nrw.schule.svws.data.DataManager;
import de.nrw.schule.svws.data.JSONMapper;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.schild.faecher.DTOFach;
import de.nrw.schule.svws.db.dto.current.schild.katalog.DTOFloskelgruppen;
import de.nrw.schule.svws.db.dto.current.schild.katalog.DTOFloskeln;
import de.nrw.schule.svws.db.dto.current.schild.klassen.DTOKlassen;
import de.nrw.schule.svws.db.dto.current.schild.klassen.DTOKlassenLeitung;
import de.nrw.schule.svws.db.dto.current.schild.kurse.DTOKurs;
import de.nrw.schule.svws.db.dto.current.schild.lehrer.DTOLehrer;
import de.nrw.schule.svws.db.dto.current.schild.schueler.DTOSchueler;
import de.nrw.schule.svws.db.dto.current.schild.schule.DTOEigeneSchule;
import de.nrw.schule.svws.db.dto.current.schild.schule.DTOJahrgang;
import de.nrw.schule.svws.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.nrw.schule.svws.db.utils.OperationError;
import de.nrw.schule.svws.db.utils.dto.enm.DTOENMLehrerSchuelerAbschnittsdaten;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link ENMDaten}.
 */
public class DataENMDaten extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link ENMDaten}.
	 * 
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataENMDaten(DBEntityManager conn) {
		super(conn);
	}
	
	@Override
	public Response getAll() {
		ENMDaten daten = getDaten(null);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}
	
	/**
	 * Gibt die ENM-Daten als mit GZIP komprimiertes JSON-Objekt zurück.
	 * 
	 * @return das GZIP-komprimierte JSON-Objekt.
	 */
	public Response getAllGZip() {
		ENMDaten daten = getDaten(null);
		return JSONMapper.gzipFromObject(daten, "enm.json.gz");
	}

	@Override
	public Response getList() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response get(Long id) {
		if (id == null)
			return OperationError.NOT_FOUND.getResponse();
		ENMDaten daten = getDaten(id);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	@Override
	public Response patch(Long id, InputStream is) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Ermittelt die ENM-Daten zu dem Lehrer mit der angegebenen ID.
	 * Ist die ID null so werden die ENM-Daten für alle Lehrer des aktuellen
	 * Schuljahresabschnitts generiert.
	 * 
	 * @param id   die ID des Lehrers oder null
	 * 
	 * @return die ENMDaten
	 */
	private ENMDaten getDaten(Long id) {
		// Lese die Daten aus der Datenbank ein
		DTOEigeneSchule schule = getSchule();
    	DTOSchuljahresabschnitte abschnitt = getSchuljahresabschnitt(schule); 
    	Map<Long, DTOLehrer> mapLehrer = getLehrerListe();
		DTOLehrer dtoLehrer = (id == null) ? null : mapLehrer.get(id);   // Ermittle den Lehrer nur, falls ENM-Daten für einen speziellen Lehrer bestimt werden.
    	if ((id != null) && (dtoLehrer == null))
    		throw OperationError.NOT_FOUND.exception();
    	Map<Long, DTOSchueler> mapSchueler = getSchuelerListe(schule);
    	Map<Long, DTOFach> mapFaecher = getFaecherListe();
    	Map<Long, DTOJahrgang> mapJahrgaenge = getJahrgangsListe();
    	Map<String, DTOKlassen> mapKlassen = getKlassenListe(schule);
    	Map<Long, List<DTOKlassenLeitung>> mapKlassenLeitung = getKlassenleitungen(mapKlassen);
    	Map<Long, DTOKurs> mapKurse = getKurse(schule);
    	
    	// Erstelle einen ENM-Daten-Manager und füge ggf. den Lehrer hinzu für welchen die ENM-Daten erzeugt werden
    	ENMDatenManager manager = new ENMDatenManager(id);
    	if (dtoLehrer != null)
    		manager.addLehrer(manager.daten.lehrerID, dtoLehrer.Kuerzel, dtoLehrer.Nachname, dtoLehrer.Vorname, dtoLehrer.Geschlecht, dtoLehrer.eMailDienstlich);
		initManager(manager, schule, abschnitt);
		
   	    // Aggregiert aus den Schülerabschnittsdaten und -leistungsdaten die einzelnen Informationen für das ENM. 
    	// Dabei wird unter anderem auch ermittelt, welche Kataloginformationen (Klassen, Lehrer, Jahrgänge) 
   	    // bei den Daten für das ENM einzutragen sind.
    	List<DTOENMLehrerSchuelerAbschnittsdaten> schuelerabschnitte = dtoLehrer == null
    			? DTOENMLehrerSchuelerAbschnittsdaten.queryAll(conn, schule.Schuljahresabschnitts_ID)
    			: DTOENMLehrerSchuelerAbschnittsdaten.query(conn, schule.Schuljahresabschnitts_ID, dtoLehrer.Kuerzel);
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
				List<DTOKlassenLeitung> klassenleitungen = mapKlassenLeitung.get(dtoKlasse.ID);
				if (klassenleitungen != null) {
					for (DTOKlassenLeitung kl : klassenleitungen) {
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
    			enmSchueler.lernabschnitt.fehlstundenGesamt = schuelerabschnitt.fehlstundenSummeGesamt;
    			enmSchueler.lernabschnitt.tsFehlstundenGesamt = schuelerabschnitt.tsFehlstundenSummeGesamt;
    			enmSchueler.lernabschnitt.fehlstundenGesamtUnentschuldigt = schuelerabschnitt.fehlstundenSummeUnentschuldigt;
    			enmSchueler.lernabschnitt.tsFehlstundenGesamtUnentschuldigt = schuelerabschnitt.tsFehlstundenSummeUnentschuldigt;
    			enmSchueler.lernabschnitt.pruefungsordnung = schuelerabschnitt.pruefungsordnung;
    			enmSchueler.lernabschnitt.lernbereich1note = schuelerabschnitt.lernbereich1note == null ? null : schuelerabschnitt.lernbereich1note.kuerzel;
    			enmSchueler.lernabschnitt.lernbereich2note = schuelerabschnitt.lernbereich2note == null ? null : schuelerabschnitt.lernbereich2note.kuerzel;
    			enmSchueler.lernabschnitt.foerderschwerpunkt1 = schuelerabschnitt.foerderschwerpunkt1Kuerzel;
    			enmSchueler.lernabschnitt.foerderschwerpunkt2 = schuelerabschnitt.foerderschwerpunkt2Kuerzel;
    			enmSchueler.bemerkungen.ASV = schuelerabschnitt.ASV;
    			enmSchueler.bemerkungen.tsASV = schuelerabschnitt.tsASV;
    			enmSchueler.bemerkungen.AUE = schuelerabschnitt.AUE;
    			enmSchueler.bemerkungen.tsAUE = schuelerabschnitt.tsAUE;
    			enmSchueler.bemerkungen.ZB = schuelerabschnitt.zeugnisBemerkungen;
    			enmSchueler.bemerkungen.tsZB = schuelerabschnitt.tsZeugnisBemerkungen;
    			enmSchueler.bemerkungen.LELS = schuelerabschnitt.LELS;
    			// TODO Schulform-Empfehlung enmSchueler.bemerkungen.schulformEmpf = ...
    			enmSchueler.bemerkungen.individuelleVersetzungsbemerkungen = schuelerabschnitt.bemerkungVersetzung;
    			enmSchueler.bemerkungen.tsIndividuelleVersetzungsbemerkungen = schuelerabschnitt.tsBemerkungVersetzung;
    			enmSchueler.bemerkungen.foerderbemerkungen = schuelerabschnitt.bemerkungFSP;
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
    			lerngruppe.lehrerID.add(schuelerabschnitt.lehrerID);
				if (manager.getLehrer(schuelerabschnitt.lehrerID) == null) {
					DTOLehrer dtoFachlehrer = mapLehrer.get(schuelerabschnitt.lehrerID);
					if (dtoFachlehrer != null) {
						manager.addLehrer(dtoFachlehrer.ID, dtoFachlehrer.Kuerzel, 
								dtoFachlehrer.Nachname, dtoFachlehrer.Vorname, dtoFachlehrer.Geschlecht, dtoFachlehrer.eMailDienstlich);
					}
				}
				// TODO ggf. im Team-Teaching unterrichtende Lehrer hinzufügen (Zusatzkraft in Leistungsdaten bzw. weitere Lehrkraft bei Kursen)
    		}
    		
    		int halbjahr = (schule.AnzahlAbschnitte == 4) ? abschnitt.Abschnitt / 2 : abschnitt.Abschnitt;
    		boolean istSchriftlich = (schuelerabschnitt.kursID != null)
    			&& ((kursart == ZulaessigeKursart.LK1) || (kursart == ZulaessigeKursart.LK2) 
    				|| (kursart == ZulaessigeKursart.GKS) || (kursart == ZulaessigeKursart.AB3) 
    				|| ((kursart == ZulaessigeKursart.AB4) && (!("Q2".equals(dtoKlasse.ASDKlasse) && (halbjahr == 2))))); 
    		
    		String tmpAbiFach = schuelerabschnitt.AbiturFach;
    		Integer abiFach = (tmpAbiFach == null) ? null : switch(tmpAbiFach) {
    			case "1" -> 1; 
    			case "2" -> 2; 
    			case "3" -> 3; 
    			case "4" -> 4;
    			default -> null;
    		};
    		boolean istGemahnt = (schuelerabschnitt.istGemahnt == null) ? false : schuelerabschnitt.istGemahnt;
    		String mahndatum = schuelerabschnitt.mahndatum;
    		manager.addSchuelerLeistungsdaten(enmSchueler, schuelerabschnitt.leistungID, lerngruppe.id, 
    				schuelerabschnitt.note.kuerzel, schuelerabschnitt.tsNote, istSchriftlich, abiFach, 
    				schuelerabschnitt.fehlstundenGesamt, schuelerabschnitt.tsFehlstundenGesamt,
    				schuelerabschnitt.fehlstundenUnentschuldigt, schuelerabschnitt.tsFehlstundenUnentschuldigt,
    				schuelerabschnitt.fachbezogeneBemerkungen, schuelerabschnitt.tsFachbezogeneBemerkungen, null,
    				istGemahnt, schuelerabschnitt.tsIstGemahnt, mahndatum);
    		// TODO get and add Teilleistungen
    		// TODO check and add ZP10 - Data
    		// TODO check and add BKAbschluss - Data
    	}
    	
    	// Kopiere den Floskel-Katalog in die ENM-Daten
    	getFloskeln(manager, mapJahrgaenge);
    	
    	return manager.daten;
	}


	private static void initManager(ENMDatenManager manager, DTOEigeneSchule schule, DTOSchuljahresabschnitte abschnitt) {
		// Setze die grundlegenden Schuldaten
    	manager.setSchuldaten(schule.SchulNr, abschnitt.Jahr, schule.AnzahlAbschnitte, abschnitt.Abschnitt,
    			/** TODO */ null, /** TODO */ true, /** TODO */ false, /** TODO */ true,
    			schule.Schulform.daten.kuerzel, /** TODO */ null);
    	// Kopiere den Noten-Katalog aus dem Core-type in die ENM-Daten
		manager.addNoten();
    	// Kopiere den Förderschwerpunkt-Katalog aus dem Core-type in die ENM-Daten
		manager.addFoerderschwerpunkte(schule.Schulform);
	}

	private DTOEigeneSchule getSchule() {
		DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
		if (schule == null)
			throw OperationError.NOT_FOUND.exception();
		return schule;
	}
	
	private DTOSchuljahresabschnitte getSchuljahresabschnitt(DTOEigeneSchule schule) {
		DTOSchuljahresabschnitte abschnitt = conn.queryByKey(DTOSchuljahresabschnitte.class, schule.Schuljahresabschnitts_ID);
		if (abschnitt == null)
			throw OperationError.NOT_FOUND.exception();
		return abschnitt;
	}
	
	// TODO Optimierung: Lese nur die aktiven Lehrer aus der Datenbank aus.
	private Map<Long, DTOLehrer> getLehrerListe() {
		List<DTOLehrer> lehrer = conn.queryAll(DTOLehrer.class);
		if (lehrer.size() == 0) 
			throw OperationError.NOT_FOUND.exception();
		return lehrer.stream().collect(Collectors.toMap(e -> e.ID, e -> e));
	}
	
	private Map<Long, DTOSchueler> getSchuelerListe(DTOEigeneSchule schule) {
		List<DTOSchueler> schueler = conn.queryNamed("DTOSchueler.schuljahresabschnitts_id", schule.Schuljahresabschnitts_ID, DTOSchueler.class);
		if (schueler.size() == 0)
			throw OperationError.NOT_FOUND.exception();
		return schueler.stream()
				.filter(s -> s.Status == SchuelerStatus.AKTIV || s.Status == SchuelerStatus.EXTERN)
				.collect(Collectors.toMap(s -> s.ID, s -> s)); 
	}

	private Map<Long, DTOFach> getFaecherListe() {
		List<DTOFach> faecher = conn.queryAll(DTOFach.class);
		if (faecher.size() == 0) 
			throw OperationError.NOT_FOUND.exception();
		return faecher.stream().collect(Collectors.toMap(f -> f.ID, f -> f));
	}

	private Map<Long, DTOJahrgang> getJahrgangsListe() {
		List<DTOJahrgang> jahrgaenge = conn.queryAll(DTOJahrgang.class);
		if (jahrgaenge.size() == 0) 
			throw OperationError.NOT_FOUND.exception();
		return jahrgaenge.stream().collect(Collectors.toMap(j -> j.ID, j -> j));
	}
	
	private Map<String, DTOKlassen> getKlassenListe(DTOEigeneSchule schule) {
		List<DTOKlassen> klassen = conn.queryNamed("DTOKlassen.schuljahresabschnitts_id", schule.Schuljahresabschnitts_ID, DTOKlassen.class);
		if (klassen.size() == 0) 
			throw OperationError.NOT_FOUND.exception();
		return klassen.stream().collect(Collectors.toMap(e -> e.Klasse, e -> e));
	}

	private Map<Long, List<DTOKlassenLeitung>> getKlassenleitungen(Map<String, DTOKlassen> mapKlassen) {
		List<Long> klassenIDs = mapKlassen.values().stream().map(kl -> kl.ID).collect(Collectors.toList());
		return conn.queryNamed("DTOKlassenLeitung.klassen_id.multiple", klassenIDs, DTOKlassenLeitung.class).stream()
				.collect(Collectors.groupingBy(kl -> kl.Klassen_ID));
	}
	
	private Map<Long, DTOKurs> getKurse(DTOEigeneSchule schule) {
		List<DTOKurs> kurse = conn.queryNamed("DTOKurs.schuljahresabschnitts_id", schule.Schuljahresabschnitts_ID, DTOKurs.class);
		if (kurse == null) 
			throw OperationError.NOT_FOUND.exception();
		return conn.queryAll(DTOKurs.class).stream().collect(Collectors.toMap(k -> k.ID, k -> k));
	}
	

	/**
	 * Füllt die Datenstruktur für die Floskelgruppen des ENM mit den in der SVWS-DB hinterlegten 
	 * Floskelgruppen und den zugehörigen Floskeln.
	 * 
	 * @param manager         der ENM-Daten-Manager
	 * @param mapJahrgaenge   eine Map mit den jeweiligen Jahrgängen 
	 */
	private void getFloskeln(ENMDatenManager manager, Map<Long, DTOJahrgang> mapJahrgaenge) {
		Map<String, DTOJahrgang> mapJG = mapJahrgaenge.values().stream().collect(Collectors.toMap(j -> j.ASDJahrgang, j -> j));
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
		    ENMFach fach = manager.getFachByKuerzel(dto.FloskelFach);
			ENMFloskel enmFl = new ENMFloskel();
			enmFl.kuerzel = dto.Kuerzel;
			enmFl.text = dto.FloskelText;
			enmFl.fachID = (fach == null) ? null : fach.id;
			try {
				enmFl.niveau = Long.parseLong(dto.FloskelNiveau);
			} catch (@SuppressWarnings("unused") NumberFormatException e) {
				enmFl.niveau = null;
			}
			DTOJahrgang jg = mapJG.get(dto.FloskelJahrgang);
			enmFl.jahrgangID = (jg == null) ? null : jg.ID;
			ENMFloskelgruppe enmFG = map.get(dto.FloskelGruppe);
			if (enmFG != null) {
				enmFG.floskeln.add(enmFl);
			} else {
				// TODO Fehlerbehandlung: Wie ordnet man die Floskel zu? -> allgemein
			}
		}
	}

}
