package de.nrw.schule.svws.data.gost;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import de.nrw.schule.svws.core.data.gost.GostBlockungsergebnis;
import de.nrw.schule.svws.core.data.gost.GostBlockungsergebnisListeneintrag;
import de.nrw.schule.svws.core.utils.gost.GostBlockungsdatenManager;
import de.nrw.schule.svws.core.utils.gost.GostBlockungsergebnisManager;
import de.nrw.schule.svws.data.DataManager;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.gost.kursblockung.DTOGostBlockung;
import de.nrw.schule.svws.db.dto.current.gost.kursblockung.DTOGostBlockungKurs;
import de.nrw.schule.svws.db.dto.current.gost.kursblockung.DTOGostBlockungSchiene;
import de.nrw.schule.svws.db.dto.current.gost.kursblockung.DTOGostBlockungZwischenergebnis;
import de.nrw.schule.svws.db.dto.current.gost.kursblockung.DTOGostBlockungZwischenergebnisKursSchiene;
import de.nrw.schule.svws.db.dto.current.gost.kursblockung.DTOGostBlockungZwischenergebnisKursSchueler;
import de.nrw.schule.svws.db.dto.current.schild.schueler.DTOSchueler;
import de.nrw.schule.svws.db.dto.current.views.gost.DTOViewGostSchuelerAbiturjahrgang;
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
	 * @param schuelerListe   die Liste der Schüler in dem Abiturjahrgang
	 * 
	 * @return das Core-DTO für das Blockungsergebnis
	 * 
	 * @throws WebApplicationException   falls das Ergebnis nicht in der Datenbank existiert.
	 */
	GostBlockungsergebnis getErgebnis(@NotNull DTOGostBlockungZwischenergebnis ergebnis, 
	        @NotNull GostBlockungsdatenManager datenManager, @NotNull List<@NotNull DTOSchueler> schuelerListe) throws WebApplicationException {
		// Schülerliste ist nun im GostBlockungsdatenManager  
        // List<Schueler> schueler = schuelerListe.stream().map(dtoMapperSchueler).sorted(schuelerComparator).collect(Collectors.toList());
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
    	// Bestimme alle Schüler-IDs für den Abiturjahrgang der Blockung
		List<DTOSchueler> schuelerListe = (new DataGostJahrgangSchuelerliste(conn, datenManager.daten().abijahrgang)).getSchuelerDTOs();
		// Bestimme die Daten des Ergebnisses
        GostBlockungsergebnis daten = getErgebnis(ergebnis, datenManager, schuelerListe);
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
	
}
