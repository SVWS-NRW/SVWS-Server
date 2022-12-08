package de.nrw.schule.svws.data.gost;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.stream.Collectors;

import de.nrw.schule.svws.core.data.gost.GostBlockungKurs;
import de.nrw.schule.svws.core.data.gost.GostBlockungsergebnis;
import de.nrw.schule.svws.core.data.gost.GostBlockungsergebnisKurs;
import de.nrw.schule.svws.core.data.gost.GostBlockungsergebnisListeneintrag;
import de.nrw.schule.svws.core.data.gost.GostFach;
import de.nrw.schule.svws.core.data.schueler.Schueler;
import de.nrw.schule.svws.core.types.KursFortschreibungsart;
import de.nrw.schule.svws.core.types.Note;
import de.nrw.schule.svws.core.types.gost.GostHalbjahr;
import de.nrw.schule.svws.core.types.gost.GostKursart;
import de.nrw.schule.svws.core.types.klassen.Klassenart;
import de.nrw.schule.svws.core.types.schule.AllgemeinbildendOrganisationsformen;
import de.nrw.schule.svws.core.types.schule.Pruefungsordnung;
import de.nrw.schule.svws.core.utils.gost.GostBlockungsdatenManager;
import de.nrw.schule.svws.core.utils.gost.GostBlockungsergebnisManager;
import de.nrw.schule.svws.data.DataManager;
import de.nrw.schule.svws.data.schule.SchulUtils;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.gost.DTOGostSchuelerFachbelegungen;
import de.nrw.schule.svws.db.dto.current.gost.kursblockung.DTOGostBlockung;
import de.nrw.schule.svws.db.dto.current.gost.kursblockung.DTOGostBlockungKurs;
import de.nrw.schule.svws.db.dto.current.gost.kursblockung.DTOGostBlockungKurslehrer;
import de.nrw.schule.svws.db.dto.current.gost.kursblockung.DTOGostBlockungSchiene;
import de.nrw.schule.svws.db.dto.current.gost.kursblockung.DTOGostBlockungZwischenergebnis;
import de.nrw.schule.svws.db.dto.current.gost.kursblockung.DTOGostBlockungZwischenergebnisKursSchiene;
import de.nrw.schule.svws.db.dto.current.gost.kursblockung.DTOGostBlockungZwischenergebnisKursSchueler;
import de.nrw.schule.svws.db.dto.current.schild.kurse.DTOKurs;
import de.nrw.schule.svws.db.dto.current.schild.kurse.DTOKursLehrer;
import de.nrw.schule.svws.db.dto.current.schild.schueler.DTOSchuelerLeistungsdaten;
import de.nrw.schule.svws.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.nrw.schule.svws.db.dto.current.schild.schule.DTOEigeneSchule;
import de.nrw.schule.svws.db.dto.current.schild.schule.DTOJahrgang;
import de.nrw.schule.svws.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.nrw.schule.svws.db.dto.current.svws.db.DTODBAutoInkremente;
import de.nrw.schule.svws.db.dto.current.views.gost.DTOViewGostSchuelerAbiturjahrgang;
import de.nrw.schule.svws.db.schema.Schema;
import de.nrw.schule.svws.db.utils.OperationError;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link GostBlockungsergebnis}.
 */
public class DataGostBlockungsergebnisse extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link GostBlockungsergebnis}.
	 * 
	 * @param conn         die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataGostBlockungsergebnisse(DBEntityManager conn) {
		super(conn);
	}


	@Override
	public Response getAll() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response getList() {
		throw new UnsupportedOperationException();
	}


	/**
	 * Bestimmt die Liste der Blockungsergebnisse und das aktuelle Blockungsergebnis
	 * für den angegebenen Blockungsdaten-Manager
	 * 
	 * @param datenManager   der Blockungsdaten-Manager
	 */
	void getErgebnisListe(@NotNull GostBlockungsdatenManager datenManager) {
	    // Bestimme die Liste der Ergebnisse aus der Datenbank
        List<DTOGostBlockungZwischenergebnis> ergebnisse = conn.queryNamed(
                "DTOGostBlockungZwischenergebnis.blockung_id", datenManager.getID(), DTOGostBlockungZwischenergebnis.class);
        if (ergebnisse == null)
            throw OperationError.NOT_FOUND.exception();
        List<Long> ergebnisIDs = ergebnisse.stream().map(e -> e.ID).collect(Collectors.toList());
        if (ergebnisIDs.size() == 0) // Es muss immer mindestens ein aktuelles Ergebnis vorliegen
            throw OperationError.INTERNAL_SERVER_ERROR.exception();
        
        // Bestimme die Kurs-Schienen-Zuordnungen für alle Zwischenergebnisse
        Map<Long, List<DTOGostBlockungZwischenergebnisKursSchiene>> mapKursSchienen = 
            conn.queryNamed("DTOGostBlockungZwischenergebnisKursSchiene.zwischenergebnis_id.multiple", ergebnisIDs,
                    DTOGostBlockungZwischenergebnisKursSchiene.class)
                .stream().collect(Collectors.groupingBy(e -> e.Zwischenergebnis_ID, Collectors.toList()));
        
        // Bestimme die Kurs-Schüler-Zuordnungen für alle Zwischenergebnisse
        Map<Long, List<DTOGostBlockungZwischenergebnisKursSchueler>> mapKursSchueler =  
            conn.queryNamed("DTOGostBlockungZwischenergebnisKursSchueler.zwischenergebnis_id.multiple", ergebnisIDs,
                    DTOGostBlockungZwischenergebnisKursSchueler.class)
                .stream().collect(Collectors.groupingBy(e -> e.Zwischenergebnis_ID, Collectors.toList()));
        
	    // Durchwandere alle Ergebnisse
        for (DTOGostBlockungZwischenergebnis erg : ergebnisse) {
            // Erstelle zunächst das Core-DTO für das Ergebnis mit Bewertung
            var manager = new GostBlockungsergebnisManager(datenManager, erg.ID);
            var listSchienenKurse = mapKursSchienen.getOrDefault(erg.ID, Collections.emptyList());
            var listKursSchueler = mapKursSchueler.getOrDefault(erg.ID, Collections.emptyList());
            for (var ks : listSchienenKurse)
                manager.setKursSchiene(ks.Blockung_Kurs_ID, ks.Schienen_ID, true);
            for (var ks : listKursSchueler)
                manager.setSchuelerKurs(ks.Schueler_ID, ks.Blockung_Kurs_ID, true);
            GostBlockungsergebnis ergebnis = manager.getErgebnis();
            ergebnis.istMarkiert = erg.IstMarkiert == null ? false : erg.IstMarkiert;
            ergebnis.istVorlage = erg.IstVorlage == null ? false : erg.IstVorlage;

            // Hinzufügen des Ergebnis-Listeneintrags zu den Blockungsdaten
            var eintrag = new GostBlockungsergebnisListeneintrag();
            eintrag.id = ergebnis.id;
            eintrag.blockungID = ergebnis.blockungID;
            eintrag.name = ergebnis.name;
            eintrag.gostHalbjahr = ergebnis.gostHalbjahr;
            eintrag.istMarkiert = ergebnis.istMarkiert;
            eintrag.istVorlage = ergebnis.istVorlage;
            eintrag.bewertung = ergebnis.bewertung;
            datenManager.daten().ergebnisse.add(eintrag);
        }
	}


	/**
	 * Liest die Daten für das Blockungsergebnis aus der Datenbank ein und erstellt das
	 * zugehörige Core-DTO  
	 * 
	 * @param ergebnis        das Datenbank-DTO des Blockungsergebnisses
	 * @param datenManager    der Blockungsdaten-Manager
	 * 
	 * @return das Core-DTO für das Blockungsergebnis
	 * 
	 * @throws WebApplicationException   falls das Ergebnis nicht in der Datenbank existiert.
	 */
	GostBlockungsergebnis getErgebnis(@NotNull DTOGostBlockungZwischenergebnis ergebnis, 
	        @NotNull GostBlockungsdatenManager datenManager) throws WebApplicationException {
        GostBlockungsergebnisManager manager = new GostBlockungsergebnisManager(datenManager, ergebnis.ID);
        // Bestimme alle Kurs-Schienen-Zuordnungen
        List<DTOGostBlockungZwischenergebnisKursSchiene> listSchienenKurse = conn
                .queryNamed("DTOGostBlockungZwischenergebnisKursSchiene.zwischenergebnis_id", ergebnis.ID, DTOGostBlockungZwischenergebnisKursSchiene.class);
        // Bestimme alle Kurs-Schüler-Zuordnungen
        List<DTOGostBlockungZwischenergebnisKursSchueler> listKursSchueler = conn
                .queryNamed("DTOGostBlockungZwischenergebnisKursSchueler.zwischenergebnis_id", ergebnis.ID, DTOGostBlockungZwischenergebnisKursSchueler.class);
        for (DTOGostBlockungZwischenergebnisKursSchiene ks : listSchienenKurse)
            manager.setKursSchiene(ks.Blockung_Kurs_ID, ks.Schienen_ID, true);
        for (DTOGostBlockungZwischenergebnisKursSchueler ks : listKursSchueler)
            manager.setSchuelerKurs(ks.Schueler_ID, ks.Blockung_Kurs_ID, true);
        GostBlockungsergebnis daten = manager.getErgebnis();
        daten.istMarkiert = ergebnis.IstMarkiert == null ? false : ergebnis.IstMarkiert;
        daten.istVorlage = ergebnis.IstVorlage == null ? false : ergebnis.IstVorlage;
        return daten;
	}


	@Override
	public Response get(Long id) {
		GostUtils.pruefeSchuleMitGOSt(conn);
		// Bestimme das Blockungs-Zwischenergebnis
		DTOGostBlockungZwischenergebnis ergebnis = conn.queryByKey(DTOGostBlockungZwischenergebnis.class, id);
		if (ergebnis == null)
			return OperationError.NOT_FOUND.getResponse();
		GostBlockungsdatenManager datenManager = (new DataGostBlockungsdaten(conn)).getBlockungsdatenManagerFromDB(ergebnis.Blockung_ID);
		// Bestimme die Daten des Ergebnisses
        GostBlockungsergebnis daten = getErgebnis(ergebnis, datenManager);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}



	@Override
	public Response patch(Long id, InputStream is) {
		throw new UnsupportedOperationException();
	}


	/**
	 * Entfernt das Zwischenergebnis mit der angegebenen ID aus der Datenbank. 
	 *
	 * @param id   die ID des zu löschenden Blockungsergebnis 
	 * 
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
	 */
	public Response delete(Long id) {
		GostUtils.pruefeSchuleMitGOSt(conn);
		// Bestimme das Zwischenergebnis
		DTOGostBlockungZwischenergebnis erg = conn.queryByKey(DTOGostBlockungZwischenergebnis.class, id);
		if (erg == null)
			return OperationError.NOT_FOUND.getResponse();
		// Entferne das Ergebnis
		conn.remove(erg);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(id).build();
	}



	private void _createKursSchuelerZuordnung(Long idZwischenergebnis, Long idSchueler, Long idKurs) {
		GostUtils.pruefeSchuleMitGOSt(conn);
		if (idSchueler == null)
			throw OperationError.CONFLICT.exception();
		// Bestimme das Blockungs-Zwischenergebnis
		DTOGostBlockungZwischenergebnis ergebnis = conn.queryByKey(DTOGostBlockungZwischenergebnis.class, idZwischenergebnis);
		if (ergebnis == null)
			throw OperationError.NOT_FOUND.exception();
		// Bestimme die zugehörige Blockung
		DTOGostBlockung blockung = conn.queryByKey(DTOGostBlockung.class, ergebnis.Blockung_ID);
		if (blockung == null)
			throw OperationError.NOT_FOUND.exception();
    	// Bestimme alle Schüler-IDs für den Abiturjahrgang der Blockung
		List<DTOViewGostSchuelerAbiturjahrgang> schuelerAbijahrgang = conn.queryNamed("DTOViewGostSchuelerAbiturjahrgang.abiturjahr", blockung.Abi_Jahrgang, DTOViewGostSchuelerAbiturjahrgang.class);
		if ((schuelerAbijahrgang == null) || (schuelerAbijahrgang.size() == 0))
			throw OperationError.NOT_FOUND.exception();
		Set<Long> schuelerIDs = schuelerAbijahrgang.stream().map(s -> s.ID).collect(Collectors.toSet());
		if (!schuelerIDs.contains(idSchueler))
			throw OperationError.CONFLICT.exception();
		// Bestimme die Kurse, welche für die Blockung angelegt wurden
		DTOGostBlockungKurs kurs = conn.queryByKey(DTOGostBlockungKurs.class, idKurs);
		if (kurs == null)
			throw OperationError.NOT_FOUND.exception();
		if (kurs.Blockung_ID != ergebnis.Blockung_ID)
			throw OperationError.CONFLICT.exception();
		
		// Füge die neue Kurszuordnung hinzu
		conn.transactionPersist(new DTOGostBlockungZwischenergebnisKursSchueler(idZwischenergebnis, idKurs, idSchueler));
	}



	private void _deleteKursSchuelerZuordnung(Long idZwischenergebnis, Long idSchueler, Long idKurs) {
		GostUtils.pruefeSchuleMitGOSt(conn);
		if ((idSchueler == null) || (idKurs == null))
			throw OperationError.CONFLICT.exception();
		// Entferne die Zuordnung
		DTOGostBlockungZwischenergebnisKursSchueler dto = conn.queryByKey(DTOGostBlockungZwischenergebnisKursSchueler.class, idZwischenergebnis, idKurs, idSchueler);
		if (dto == null)
			throw OperationError.NOT_FOUND.exception();
		conn.transactionRemove(dto);
	}


	/**
	 * Erstellt eine Kurs-Schüler-Zuordnung in der Datenbank.
	 * 
	 * @param idZwischenergebnis   die ID der Zwischenergebnis
	 * @param idSchueler           die ID des Schülers 
	 * @param idKurs               die ID des neuen Kurses
	 * 
	 * @return die HTTP-Response, welchen den Erfolg der Update-Operation angibt.
	 */
	public Response createKursSchuelerZuordnung(Long idZwischenergebnis, Long idSchueler, Long idKurs) {
		try {
			conn.transactionBegin();
			this._createKursSchuelerZuordnung(idZwischenergebnis, idSchueler, idKurs);
			conn.transactionCommit();
	        return Response.status(Status.NO_CONTENT).build();
		} catch (Exception exception) {
			conn.transactionRollback();
			if (exception instanceof WebApplicationException webex)
				return webex.getResponse();
			throw exception;			
		}
	}
	
	
	/**
	 * Aktualisiert eine Kurs-Schüler-Zuordnung in der Datenbank.
	 * 
	 * @param idZwischenergebnis   die ID der Zwischenergebnis
	 * @param idSchueler           die ID des Schülers 
	 * @param idKursAlt            die ID des alten Kurses
	 * @param idKursNeu            die ID des neuen Kurses
	 * 
	 * @return die HTTP-Response, welchen den Erfolg der Update-Operation angibt.
	 */
	public Response updateKursSchuelerZuordnung(Long idZwischenergebnis, Long idSchueler, Long idKursAlt, Long idKursNeu) {
		if (idKursNeu == null)
			return deleteKursSchuelerZuordnung(idZwischenergebnis, idKursAlt, idSchueler);
		try {
			conn.transactionBegin();
			// TODO prüfe, ob die Kursarten übereinstimmen...
			this._deleteKursSchuelerZuordnung(idZwischenergebnis, idSchueler, idKursAlt);
			this._createKursSchuelerZuordnung(idZwischenergebnis, idSchueler, idKursNeu);			
			conn.transactionCommit();
	        return Response.status(Status.NO_CONTENT).build();
		} catch (Exception exception) {
			conn.transactionRollback();
			if (exception instanceof WebApplicationException webex)
				return webex.getResponse();
			throw exception;			
		}
	}
	
	
	/**
	 * Entfernt die die Zuordnung des Schüler zu dem Kurs bei einem Zwischenergebnis.
	 *  
	 * @param idZwischenergebnis   die ID der Zwischenergebnis
	 * @param idSchueler           die ID des Schülers 
	 * @param idKurs               die ID des Kurses
	 * 
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
	 */
	public Response deleteKursSchuelerZuordnung(Long idZwischenergebnis, Long idSchueler, Long idKurs) {
		try {
			conn.transactionBegin();
			this._deleteKursSchuelerZuordnung(idZwischenergebnis, idSchueler, idKurs);
			conn.transactionCommit();
	        return Response.status(Status.NO_CONTENT).build();
		} catch (Exception exception) {
			conn.transactionRollback();
			if (exception instanceof WebApplicationException webex)
				return webex.getResponse();
			throw exception;
		}
	}

	
	
    private void _createKursSchieneZuordnung(Long idZwischenergebnis, Long idSchiene, Long idKurs) {
        GostUtils.pruefeSchuleMitGOSt(conn);
        if (idSchiene == null)
            throw OperationError.CONFLICT.exception();
        // Bestimme das Blockungs-Zwischenergebnis
        DTOGostBlockungZwischenergebnis ergebnis = conn.queryByKey(DTOGostBlockungZwischenergebnis.class, idZwischenergebnis);
        if (ergebnis == null)
            throw OperationError.NOT_FOUND.exception();
        // Bestimme die zugehörige Blockung
        DTOGostBlockung blockung = conn.queryByKey(DTOGostBlockung.class, ergebnis.Blockung_ID);
        if (blockung == null)
            throw OperationError.NOT_FOUND.exception();
        // Bestimme Schienen-IDs der Blockung
        DTOGostBlockungSchiene schiene = conn.queryByKey(DTOGostBlockungSchiene.class, idSchiene);
        if (schiene == null)
            throw OperationError.NOT_FOUND.exception();
        if (schiene.Blockung_ID != ergebnis.Blockung_ID) // Fehler in der DB
            throw OperationError.CONFLICT.exception();
        // Bestimme die Kurse, welche für die Blockung angelegt wurden
        DTOGostBlockungKurs kurs = conn.queryByKey(DTOGostBlockungKurs.class, idKurs);
        if (kurs == null)
            throw OperationError.NOT_FOUND.exception();
        if (kurs.Blockung_ID != ergebnis.Blockung_ID)
            throw OperationError.CONFLICT.exception(); // Fehler in der DB
        // Füge die neue Kurs-Schienen-Zuordnung hinzu
        conn.transactionPersist(new DTOGostBlockungZwischenergebnisKursSchiene(idZwischenergebnis, idKurs, idSchiene));
    }



    private void _deleteKursSchieneZuordnung(Long idZwischenergebnis, Long idSchiene, Long idKurs) {
        GostUtils.pruefeSchuleMitGOSt(conn);
        if ((idSchiene == null) || (idKurs == null))
            throw OperationError.CONFLICT.exception();
        // Entferne die Zuordnung
        DTOGostBlockungZwischenergebnisKursSchiene dto = conn.queryByKey(DTOGostBlockungZwischenergebnisKursSchiene.class, idZwischenergebnis, idKurs, idSchiene);
        if (dto == null)
            throw OperationError.NOT_FOUND.exception();
        conn.transactionRemove(dto);
    }


    /**
     * Erstellt eine Kurs-Schienen-Zuordnung in der Datenbank.
     * 
     * @param idZwischenergebnis   die ID der Zwischenergebnis
     * @param idSchiene            die ID der Schiene 
     * @param idKurs               die ID des neuen Kurses
     * 
     * @return die HTTP-Response, welchen den Erfolg der Update-Operation angibt.
     */
    public Response createKursSchieneZuordnung(Long idZwischenergebnis, Long idSchiene, Long idKurs) {
        try {
            conn.transactionBegin();
            this._createKursSchieneZuordnung(idZwischenergebnis, idSchiene, idKurs);
            conn.transactionCommit();
            return Response.status(Status.NO_CONTENT).build();
        } catch (Exception exception) {
            conn.transactionRollback();
            if (exception instanceof WebApplicationException webex)
                return webex.getResponse();
            throw exception;            
        }
    }
    
    
    /**
     * Aktualisiert eine Kurs-Schiene-Zuordnung in der Datenbank.
     * 
     * @param idZwischenergebnis   die ID der Zwischenergebnis
     * @param idKurs               die ID des Kurses 
     * @param idSchieneAlt         die ID der alten Schiene
     * @param idSchieneNeu         die ID der neuen Schiene
     * 
     * @return die HTTP-Response, welchen den Erfolg der Update-Operation angibt.
     */
    public Response updateKursSchieneZuordnung(Long idZwischenergebnis, Long idKurs, Long idSchieneAlt, Long idSchieneNeu) {
        if (idSchieneNeu == null)
            return deleteKursSchieneZuordnung(idZwischenergebnis, idSchieneAlt, idKurs);
        try {
            conn.transactionBegin();
            this._deleteKursSchieneZuordnung(idZwischenergebnis, idSchieneAlt, idKurs);
            this._createKursSchieneZuordnung(idZwischenergebnis, idSchieneNeu, idKurs);           
            conn.transactionCommit();
            return Response.status(Status.NO_CONTENT).build();
        } catch (Exception exception) {
            conn.transactionRollback();
            if (exception instanceof WebApplicationException webex)
                return webex.getResponse();
            throw exception;            
        }
    }
    
    
    /**
     * Entfernt die die Zuordnung des Kurses zu der Schiene bei einem Zwischenergebnis.
     *  
     * @param idZwischenergebnis   die ID der Zwischenergebnis
     * @param idSchiene            die ID der Schiene 
     * @param idKurs               die ID des Kurses
     * 
     * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
     */
    public Response deleteKursSchieneZuordnung(Long idZwischenergebnis, Long idSchiene, Long idKurs) {
        try {
            conn.transactionBegin();
            this._deleteKursSchieneZuordnung(idZwischenergebnis, idSchiene, idKurs);
            conn.transactionCommit();
            return Response.status(Status.NO_CONTENT).build();
        } catch (Exception exception) {
            conn.transactionRollback();
            if (exception instanceof WebApplicationException webex)
                return webex.getResponse();
            throw exception;
        }
    }


    /**
     * Aktiviert das Blockungsergebnis in dem angegebenen Manager in dem angegebenen Schuljahrabschnitt.
     * 
     * Hierfür muss bereits geprüft sein, ob nicht bereits eine Blockung in diesem Abschnitt aktiv ist!
     *  
     * @param ergebnisManager   der Ergebnis-Manager
     * @param abschnitt         der Schuljahresabschnitt, in dem die Blockung aktiviert wird
     * @param halbjahr          das Halbjahr der gymnasialen Oberstufe
     */
    private void aktiviere(GostBlockungsergebnisManager ergebnisManager, DTOSchuljahresabschnitte abschnitt, GostHalbjahr halbjahr) {
        try {
            conn.transactionBegin();
	    	// Bestimme die ID des Jahrgangs
	    	List<DTOJahrgang> jahrgangsliste = conn.queryList("SELECT e FROM DTOJahrgang e WHERE e.ASDJahrgang = ?1 AND e.Sichtbar = ?2", DTOJahrgang.class, halbjahr.jahrgang, true);
	    	if (jahrgangsliste.size() == 0)
	    		throw OperationError.NOT_FOUND.exception();
	    	if (jahrgangsliste.size() > 1)
	    		throw OperationError.CONFLICT.exception();
	    	DTOJahrgang jahrgang = jahrgangsliste.get(0);
			// Bestimme die ID, mit welche der nächste Kurs eingefügt wird
			DTODBAutoInkremente dbKurseID = conn.queryByKey(DTODBAutoInkremente.class, Schema.tab_Kurse.name());
			long idKurs = dbKurseID == null ? 1 : dbKurseID.MaxID + 1;    	
	    	// Durchwandere alle Kurse der Blockung und lege diese an, merke dabei auch die Zuordnung der neuen Kurs-IDs zu den Kurs-IDs der Blockung
			HashMap<Long, Long> mapKursIDs = new HashMap<>(); 
			HashMap<Long, DTOKurs> mapKursDTOs = new HashMap<>(); 
	    	GostBlockungsdatenManager datenManager = ergebnisManager.getParent();
	    	for (GostBlockungKurs kurs : datenManager.daten().kurse) {
	    		long id = idKurs++;
	    		mapKursIDs.put(kurs.id, id);
	    		// Bestimme die Kurslehrer, sofern bereits festgelegt
	    		DTOGostBlockungKurslehrer kurslehrer = null;
	    		List<DTOGostBlockungKurslehrer> kurslehrerListe = conn.queryNamed("DTOGostBlockungKurslehrer.blockung_kurs_id", kurs.id, DTOGostBlockungKurslehrer.class);
	    		List<DTOKursLehrer> kursLehrerZusatzkraefte = new Vector<>();
	    		for (DTOGostBlockungKurslehrer dtoKurslehrer : kurslehrerListe) {
	    			if ((dtoKurslehrer.Reihenfolge != null) && (dtoKurslehrer.Reihenfolge == 1)) {
	    				kurslehrer = dtoKurslehrer;
	    			} else {
	    				DTOKursLehrer kl = new DTOKursLehrer(id, dtoKurslehrer.Lehrer_ID);
	    				kl.Anteil = dtoKurslehrer.Wochenstunden == null ? null : dtoKurslehrer.Wochenstunden.doubleValue();
	    				kursLehrerZusatzkraefte.add(kl);
	    			}
	    		}
	    		DTOKurs dto = new DTOKurs(id, abschnitt.ID, datenManager.getNameOfKurs(kurs.id), kurs.fach_id);
	    		dto.Jahrgang_ID = jahrgang.ID;
	    		dto.ASDJahrgang = halbjahr.jahrgang;
	    		dto.KursartAllg = GostKursart.fromID(kurs.kursart).kuerzel;
	    		dto.WochenStd = kurs.wochenstunden;
	    		dto.Lehrer_ID = kurslehrer == null ? null : kurslehrer.Lehrer_ID;
	    		GostFach fach = datenManager.faecherManager().get(kurs.fach_id);
	    		dto.Sortierung = fach.sortierung;
	    		dto.Sichtbar = true;
	    		int[] schienen = ergebnisManager.getOfKursSchienenNummern(kurs.id);
	    		dto.Schienen = (schienen == null) ? null : Arrays.stream(schienen).mapToObj(i -> "" + i)
	    				.collect(Collectors.joining(","));
	    		dto.Fortschreibungsart = KursFortschreibungsart.KEINE;
	    		dto.WochenstdKL = ((kurslehrer == null) || (kurslehrer.Wochenstunden == null)) 
	    				? null : kurslehrer.Wochenstunden.doubleValue();
	    		dto.SchulNr = null;
	    		dto.EpochU = false;
	    		dto.ZeugnisBez = null;
	    		dto.Jahrgaenge = null;
	    		conn.transactionPersist(dto);
	    		mapKursDTOs.put(kurs.id, dto);
	    		for (DTOKursLehrer kl : kursLehrerZusatzkraefte)
	    			conn.transactionPersist(kl);
	    	}
	        if (!conn.transactionCommit())
	        	throw OperationError.INTERNAL_SERVER_ERROR.exception();
            conn.transactionBegin();
			// Bestimme die ID, mit welche der nächste Schülerlernabschnitt eingefügt wird
			DTODBAutoInkremente dbLernabschnitteID = conn.queryByKey(DTODBAutoInkremente.class, Schema.tab_SchuelerLernabschnittsdaten.name());
			long idLernabschnitt = dbLernabschnitteID == null ? 1 : dbLernabschnitteID.MaxID + 1;
	    	// Durchwandere alle Schüler des Abitur-Jahrgangs und lege ggf. fehlende Lernabschnitte an
			HashMap<Long, Long> mapLernabschnitte = new HashMap<>();
	    	for (Schueler schueler : datenManager.daten().schueler) {
	    		// Prüfe, ob bereits ein Lernabschnitt vorhanden ist
	    		List<DTOSchuelerLernabschnittsdaten> lernabschnitte = conn.queryList("SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Schueler_ID = ?1 AND e.Schuljahresabschnitts_ID = ?2 AND e.WechselNr IS NULL",
	    				DTOSchuelerLernabschnittsdaten.class, schueler.id, abschnitt.ID);
	    		DTOSchuelerLernabschnittsdaten lernabschnitt = null;
	    		if (lernabschnitte.size() > 0) {
	    			lernabschnitt = lernabschnitte.get(0);
	    		} else {
	    			lernabschnitt = new DTOSchuelerLernabschnittsdaten(idLernabschnitt++, schueler.id, abschnitt.ID, false);
	    			lernabschnitt.WechselNr = null;
	    			lernabschnitt.Schulbesuchsjahre = null; // TODO Berechnen aus altem Lernabschnitt
	    			lernabschnitt.Hochrechnung = null;
	    			lernabschnitt.SemesterWertung = true;
	    			lernabschnitt.PruefOrdnung = Pruefungsordnung.APO_GOST.daten.kuerzelSchild;  // TODO Schulgliederung.DEFAULT, Schulgliederung.GY8 -> /G8 oder Schulgliederung.GY9 -> /G9
	    			lernabschnitt.Klassen_ID = null;  // TODO aus altem Lernabschnitt ermitteln
	    			lernabschnitt.Verspaetet = null;
	    			lernabschnitt.NPV_Fach_ID = null;
	    			lernabschnitt.NPV_NoteKrz = null;
	    			lernabschnitt.NPV_Datum = null;
	    			lernabschnitt.NPAA_Fach_ID = null;
	    			lernabschnitt.NPAA_NoteKrz = null;
	    			lernabschnitt.NPAA_Datum = null;
	    			lernabschnitt.NPBQ_Fach_ID = null;
	    			lernabschnitt.NPBQ_NoteKrz = null;
	    			lernabschnitt.NPBQ_Datum = null;
	    			lernabschnitt.VersetzungKrz = null;
	    			lernabschnitt.AbschlussArt = null;
	    			lernabschnitt.AbschlIstPrognose = null;
	    			lernabschnitt.Konferenzdatum = null;
	    			lernabschnitt.ZeugnisDatum = null;
	    			lernabschnitt.Schulgliederung = null; // TODO aus der Klassentabelle oder der Jahrgangstabelle
	    			lernabschnitt.ASDJahrgang = halbjahr.jahrgang;
	    			lernabschnitt.Jahrgang_ID = jahrgang.ID;
	    			lernabschnitt.Fachklasse_ID = null;
	    			lernabschnitt.Schwerpunkt_ID = null;
	    			lernabschnitt.ZeugnisBem = null;
	    			lernabschnitt.Schwerbehinderung = null; // TODO aus altem Lernabschnitt
	    			lernabschnitt.Foerderschwerpunkt_ID = null; // TODO aus altem Lernabschnitt
	    			lernabschnitt.OrgFormKrz = AllgemeinbildendOrganisationsformen.HALBTAG.daten.kuerzel;
	    			lernabschnitt.RefPaed = null;
	    			lernabschnitt.Klassenart = Klassenart.RK.daten.kuerzel; // TODO aus altem Lernabschnitt übernehmen
	    			lernabschnitt.SumFehlStd = 0;
	    			lernabschnitt.SumFehlStdU = 0;
	    			lernabschnitt.Wiederholung = false;   // TODO prüfe, ob ein alter Lernabschnitt mit dem Gost-Halbjahr existiert
	    			lernabschnitt.Gesamtnote_GS = null;
	    			lernabschnitt.Gesamtnote_NW = null;
	    			lernabschnitt.Folgeklasse_ID = null;  // TODO aus Klassentabelle
	    			lernabschnitt.Foerderschwerpunkt2_ID = null; // TODO aus altem Lernabschnitt
	    			lernabschnitt.Abschluss = null;
	    			lernabschnitt.Abschluss_B = null;
	    			lernabschnitt.DSNote = null;
	    			lernabschnitt.AV_Leist = null;
	    			lernabschnitt.AV_Zuv = null;
	    			lernabschnitt.AV_Selbst = null;
	    			lernabschnitt.SV_Verant = null;
	    			lernabschnitt.SV_Konfl = null;
	    			lernabschnitt.SV_Koop = null;
	    			lernabschnitt.MoeglNPFaecher = null;
	    			lernabschnitt.Zertifikate = null;
	    			lernabschnitt.DatumFHR = null;
	    			lernabschnitt.PruefAlgoErgebnis = null;
	    			lernabschnitt.Zeugnisart = null;
	    			lernabschnitt.DatumVon = null;  // TODO
	    			lernabschnitt.DatumBis = null;  // TODO
	    			lernabschnitt.FehlstundenGrenzwert = null;
	    			lernabschnitt.Sonderpaedagoge_ID = null;
	    			lernabschnitt.FachPraktAnteilAusr = null;
	    			lernabschnitt.BilingualerZweig = null;   // TODO aus alten Lernabschnitt übernehmen
	    			lernabschnitt.AOSF = null;   // TODO aus alten Lernabschnitt übernehmen
	    			lernabschnitt.Autist = null;   // TODO aus alten Lernabschnitt übernehmen
	    			lernabschnitt.ZieldifferentesLernen = null;
	    			conn.transactionPersist(lernabschnitt);
	    		}
	    		mapLernabschnitte.put(schueler.id, lernabschnitt.ID);
	    	}
	        if (!conn.transactionCommit())
	        	throw OperationError.INTERNAL_SERVER_ERROR.exception();
            conn.transactionBegin();
			// Bestimme die ID, mit welcher die nächsten Schülerleistungsdaten eingefügt werden
			DTODBAutoInkremente dbLeistungenID = conn.queryByKey(DTODBAutoInkremente.class, Schema.tab_SchuelerLeistungsdaten.name());
			long idLeistungen = dbLeistungenID == null ? 1 : dbLeistungenID.MaxID + 1;
	    	// Durchwandere alle Schüler des Abitur-Jahrgangs und lege die Leistungsdaten an
	    	for (Schueler schueler : datenManager.daten().schueler) {
	    		// Bestimme die Kurse, in welche der Schüler gesetzt wurde  
	    		HashSet<GostBlockungsergebnisKurs> kursMenge = ergebnisManager.getOfSchuelerKursmenge(schueler.id);
	    		for (GostBlockungsergebnisKurs kurszuordnung : kursMenge) {
	    			GostBlockungKurs kurs = datenManager.getKurs(kurszuordnung.id);
	    			GostFach fach = datenManager.faecherManager().get(kurs.fach_id);
	    			DTOKurs kursDTO = mapKursDTOs.get(kurs.id);
	    			DTOGostSchuelerFachbelegungen fachwahl = conn.queryByKey(DTOGostSchuelerFachbelegungen.class, schueler.id, fach.id);
	    			DTOSchuelerLeistungsdaten leistung = new DTOSchuelerLeistungsdaten(idLeistungen++, mapLernabschnitte.get(schueler.id), kurs.fach_id);
	    			leistung.Hochrechnung = null;
	    			leistung.Fachlehrer_ID = kursDTO.Lehrer_ID;
	    			leistung.Kursart = switch (halbjahr) {
	    				case EF1 -> switch (fachwahl.EF1_Kursart) {
	    					case "M" -> "GKM";
	    					case "S" -> "GKS";
	    					case "AT" -> "GKM";
	    					default -> null;
	    				};
	    				case EF2 -> switch (fachwahl.EF2_Kursart) {
							case "M" -> "GKM";
							case "S" -> "GKS";
							case "AT" -> "GKM";
							default -> null;
						};
	    				case Q11 -> switch (fachwahl.Q11_Kursart) {
							case "M" -> "GKM";
							case "S" -> (fachwahl.AbiturFach == null) ? "GKS" : "AB" + fachwahl.AbiturFach;
							case "LK" -> (fachwahl.AbiturFach == 1) ? "LK1" : "LK2";
							case "ZK" -> "ZK";
							case "AT" -> "GKM";
							default -> null;
						};
	    				case Q12 -> switch (fachwahl.Q12_Kursart) {
							case "M" -> "GKM";
							case "S" -> (fachwahl.AbiturFach == null) ? "GKS" : "AB" + fachwahl.AbiturFach;
							case "LK" -> (fachwahl.AbiturFach == 1) ? "LK1" : "LK2";
							case "ZK" -> "ZK";
							case "AT" -> "GKM";
							default -> null;
						};
	    				case Q21 -> switch (fachwahl.Q21_Kursart) {
							case "M" -> "GKM";
							case "S" -> (fachwahl.AbiturFach == null) ? "GKS" : "AB" + fachwahl.AbiturFach;
							case "LK" -> (fachwahl.AbiturFach == 1) ? "LK1" : "LK2";
							case "ZK" -> "ZK";
							case "AT" -> "GKM";
							default -> null;
						};
	    				case Q22 -> switch (fachwahl.Q22_Kursart) {
							case "M" -> (fachwahl.AbiturFach == null) ? "GKM" : "AB" + fachwahl.AbiturFach;
							case "S" -> (fachwahl.AbiturFach == null) ? "GKS" : "AB" + fachwahl.AbiturFach;
							case "LK" -> (fachwahl.AbiturFach == 1) ? "LK1" : "LK2";
							case "ZK" -> "ZK";
							case "AT" -> "GKM";
							default -> null;
						};
	    			};
	    			leistung.KursartAllg = GostKursart.fromID(kurs.kursart).kuerzel;
	    			leistung.Kurs_ID = mapKursIDs.get(kurs.id);
	    			leistung.NotenKrz = switch (halbjahr) {
						case EF1 -> switch (fachwahl.EF1_Kursart) {
							case "AT" -> Note.ATTEST;
							default -> null;
						};
						case EF2 -> switch (fachwahl.EF2_Kursart) {
							case "AT" -> Note.ATTEST;
							default -> null;
						};
						case Q11 -> switch (fachwahl.Q11_Kursart) {
							case "AT" -> Note.ATTEST;
							default -> null;
						};
						case Q12 -> switch (fachwahl.Q12_Kursart) {
							case "AT" -> Note.ATTEST;
							default -> null;
						};
						case Q21 -> switch (fachwahl.Q21_Kursart) {
							case "AT" -> Note.ATTEST;
							default -> null;
						};
						case Q22 -> switch (fachwahl.Q22_Kursart) {
							case "AT" -> Note.ATTEST;
							default -> null;
						};
	    			};
	    			leistung.Warnung = null;
	    			leistung.Warndatum = null;
	    			leistung.AbiFach = fachwahl.AbiturFach == null ? null : "" + fachwahl.AbiturFach;
	    			leistung.Wochenstunden = kurs.wochenstunden; 
	    			leistung.AbiZeugnis = null;
	    			leistung.Prognose = null;
	    			leistung.FehlStd = 0;
	    			leistung.uFehlStd = 0;
	    			leistung.Sortierung = fach.sortierung;
	    			leistung.Lernentw = null;
	    			leistung.Gekoppelt = null;
	    			leistung.VorherAbgeschl = null;
	    			leistung.AbschlussJahrgang = null;
	    			leistung.HochrechnungStatus = null;
	    			leistung.SchulNr = null;
	    			leistung.Zusatzkraft_ID = null;  // TODO
	    			leistung.WochenstdZusatzkraft = null;  // TODO
	    			leistung.Prf10Fach = null;
	    			leistung.AufZeugnis = true;
	    			leistung.Gewichtung = null;
	    			leistung.NoteAbschlussBA = null;
	    			leistung.Umfang = null;
	    			conn.transactionPersist(leistung);
	    		}
	    	}
	        if (!conn.transactionCommit())
	        	throw OperationError.INTERNAL_SERVER_ERROR.exception();
	    } catch (Exception exception) {
	        conn.transactionRollback();
	        if (exception instanceof WebApplicationException webex)
	            throw webex;
	        throw exception;
	    }
    }


    /**
     * Aktiviert bzw. persistiert das Blockungsergebnis in der Kursliste und in den Leistungsdaten der
     * Schüler des Abiturjahrgangs
     * 
     * @param idErgebnis   das zu persistierende Blockungsergebnis
     * 
     * @return die HTTP-Response, welchen den Erfolg der Operation angibt
     */
    public Response aktiviere(Long idErgebnis) {
    	try {
			DTOEigeneSchule schule = GostUtils.pruefeSchuleMitGOSt(conn);
			
			// Bestimme das Blockungs-Zwischenergebnis
			DTOGostBlockungZwischenergebnis dtoErgebnis = conn.queryByKey(DTOGostBlockungZwischenergebnis.class, idErgebnis);
			if (dtoErgebnis == null)
				return OperationError.NOT_FOUND.getResponse();
			GostBlockungsdatenManager datenManager = (new DataGostBlockungsdaten(conn)).getBlockungsdatenManagerFromDB(dtoErgebnis.Blockung_ID);
			// Bestimme die Daten des Ergebnisses
	        GostBlockungsergebnis ergebnis = getErgebnis(dtoErgebnis, datenManager);
	        GostBlockungsergebnisManager ergebnisManager = new GostBlockungsergebnisManager(datenManager, ergebnis);
			
	        // Bestimme das Schuljahr und das Halbjahr, welchem das Ergebnis zugeordnet ist
	        GostHalbjahr halbjahr = GostHalbjahr.fromID(datenManager.daten().gostHalbjahr);
	        int schuljahr = halbjahr.getSchuljahrFromAbiturjahr(datenManager.daten().abijahrgang);
	        // Prüfe, ob der bzw. die Schuljahresabschnitte (im Quartalsmodus) bereits angelegt wurden
	        if (schule.AnzahlAbschnitte == 4) {
	        	DTOSchuljahresabschnitte abschnitt1 = SchulUtils.getSchuljahreabschnitt(conn, schuljahr, halbjahr.halbjahr == 1 ? 1 : 3);
	        	DTOSchuljahresabschnitte abschnitt2 = SchulUtils.getSchuljahreabschnitt(conn, schuljahr, halbjahr.halbjahr == 1 ? 2 : 4);
	        	if (GostUtils.pruefeHatOberstufenKurseInAbschnitt(conn, halbjahr, abschnitt1) || 
	        			GostUtils.pruefeHatOberstufenKurseInAbschnitt(conn, halbjahr, abschnitt2))
	        		return OperationError.CONFLICT.getResponse();
	        	aktiviere(ergebnisManager, abschnitt1, halbjahr);
	        	aktiviere(ergebnisManager, abschnitt2, halbjahr);
	        } else {
	        	DTOSchuljahresabschnitte abschnitt = SchulUtils.getSchuljahreabschnitt(conn, schuljahr, halbjahr.halbjahr);
	        	if (GostUtils.pruefeHatOberstufenKurseInAbschnitt(conn, halbjahr, abschnitt))
	        		return OperationError.CONFLICT.getResponse();
	        	aktiviere(ergebnisManager, abschnitt, halbjahr);
	        }
	        // Markiere die Blockung und das Ergebnis als aktiviert
	        try {
	            conn.transactionBegin();
	        	DTOGostBlockung bl = conn.queryByKey(DTOGostBlockung.class, dtoErgebnis.Blockung_ID);
	        	bl.IstAktiv = true;
	        	if (!conn.transactionPersist(bl))
		        	throw OperationError.INTERNAL_SERVER_ERROR.exception();
				DTOGostBlockungZwischenergebnis erg = conn.queryByKey(DTOGostBlockungZwischenergebnis.class, dtoErgebnis.ID);
	        	erg.IstVorlage = true;
	        	if (!conn.transactionPersist(erg))
		        	throw OperationError.INTERNAL_SERVER_ERROR.exception();
		        if (!conn.transactionCommit())
		        	throw OperationError.INTERNAL_SERVER_ERROR.exception();
		    } catch (Exception exception) {
		        conn.transactionRollback();
		        if (exception instanceof WebApplicationException webex)
		            throw webex;
		        throw exception;
		    }
	        return Response.status(Status.NO_CONTENT).build();
	    } catch (Exception exception) {
	        if (exception instanceof WebApplicationException webex)
	            return webex.getResponse();
	        throw exception;
	    }
    }

}
