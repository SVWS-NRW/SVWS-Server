package de.nrw.schule.svws.data.benutzer;

import java.io.InputStream;
import java.util.List;

import de.nrw.schule.svws.core.data.benutzer.BenutzergruppeDaten;
import de.nrw.schule.svws.core.types.benutzer.BenutzerKompetenz;
import de.nrw.schule.svws.core.utils.benutzer.BenutzergruppenManager;
import de.nrw.schule.svws.data.DataManager;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.schild.benutzer.DTOBenutzergruppe;
import de.nrw.schule.svws.db.dto.current.schild.benutzer.DTOBenutzergruppenKompetenz;
import de.nrw.schule.svws.db.utils.OperationError;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link BenutzergruppeDaten}.
 */
public class DataBenutzergruppeDaten extends DataManager<Long> {

    /**
     * Erstellt einen neuen {@link DataManager} für den Core-DTO
     * {@link BenutzergruppeDaten}.
     * 
     * @param conn die Datenbank-Verbindung für den Datenbankzugriff
     */
    public DataBenutzergruppeDaten(DBEntityManager conn) {
        super(conn);
    }

    @Override
    public Response getAll() {
        return this.getList();
    }

    @Override
    public Response getList() {
        throw new UnsupportedOperationException();
    }


    /**
     * Bestimmt das DTO für die Benutzergruppe aus der Datenbank.
     * 
     * @param id   die ID der Benutzergruppe
     * 
     * @return das DTO
     */
    private DTOBenutzergruppe getDTO(Long id) throws WebApplicationException {
        if (id == null)
            throw OperationError.NOT_FOUND.exception("Die ID der zu änderden Benutzergruppe darf nicht null sein.");
        DTOBenutzergruppe bg = conn.queryByKey(DTOBenutzergruppe.class, id);
        if (bg == null)
            throw OperationError.NOT_FOUND.exception("Die Benutzergruppe mit der ID existiert nicht.");
        return bg;
    }
    
    
    /**
     * Erstellt den Benutzergruppen-Manager für die übergebene ID mit den 
     * zugehörigen Daten aus der Datenbank.
     *  
     * @param id   die ID der Benutzergruppe
     */
    private BenutzergruppenManager getManager(Long id) throws WebApplicationException {
        // Bestimme die Benutzergruppe
        DTOBenutzergruppe benutzergruppe = getDTO(id);
        BenutzergruppenManager manager = new BenutzergruppenManager(benutzergruppe.ID, benutzergruppe.Bezeichnung);
        manager.setAdmin(benutzergruppe.IstAdmin == null ? false : benutzergruppe.IstAdmin);
        // Lese die Kompetenzen der Gruppe ein
        List<Long> kompetenzIDs = conn
                .queryNamed("DTOBenutzergruppenKompetenz.gruppe_id", benutzergruppe.ID,
                        DTOBenutzergruppenKompetenz.class)
                .stream().map(g -> g.Kompetenz_ID).sorted().toList();
        for (Long kompetenzID : kompetenzIDs)
            manager.addKompetenz(BenutzerKompetenz.getByID(kompetenzID));
        return manager;
    }


    @Override
    public Response get(Long id) throws WebApplicationException {
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(getManager(id).daten()).build();
    }


    @Override
    public Response patch(Long id, InputStream is) {
        throw new UnsupportedOperationException();
    }


    /**
     * Setzt für die angegebene Benutzergruppen-ID die Bezeichnung.
     *  
     * @param id            die ID der Benutzergruppe
     * @param bezeichnung   die neue Bezeichnung
     * 
     * @return die Response 204 bei Erfolg.
     */
    public Response setBezeichnung(Long id, String bezeichnung) {
       try {
            conn.transactionBegin();
            if ((bezeichnung == null) || "".equals(bezeichnung)) 
                return OperationError.CONFLICT.getResponse("Die Bezeichnung muss gültig sein und darf nicht null oder leer sein");
            DTOBenutzergruppe bg = getDTO(id);
            bg.Bezeichnung = bezeichnung;
            conn.transactionPersist(bg);
            conn.transactionCommit();
        } catch (Exception e) {
            if (e instanceof WebApplicationException webApplicationException)
                return webApplicationException.getResponse();
            return OperationError.INTERNAL_SERVER_ERROR.getResponse();
        } finally {
            conn.transactionRollback();
        }
        return Response.status(Status.OK).build();
    }


    /**
     * Setzt für die angegebene Benutzergruppen-ID, ob die Benutzergruppe
     * administrativ ist oder nicht.
     *  
     * @param id      die ID der Benutzergruppe
     * @param value   der Boolean-Wert
     * 
     * @return die Response 204 bei Erfolg.
     */
    public Response setAdmin(Long id, Boolean value) {
       try {
            conn.transactionBegin();
            if (value == null) 
                return OperationError.CONFLICT.getResponse("Der Status, ob es sich um eine administrative Gruppe handelt, muss definitiert sein und darf daher nicht null sein.");
            DTOBenutzergruppe bg = getDTO(id);
            bg.IstAdmin = value;
            conn.transactionPersist(bg);
            conn.transactionCommit();
        } catch (Exception e) {
            if (e instanceof WebApplicationException webApplicationException)
                return webApplicationException.getResponse();
            return OperationError.INTERNAL_SERVER_ERROR.getResponse();
        } finally {
            conn.transactionRollback();
        }
        return Response.status(Status.OK).build();
    }


    /**
     * Setzt für die angegebene Benutzergruppen-ID, ob die angegebene Kompetenz vorhanden
     * ist ist oder nicht.
     *  
     * @param id      die ID der Benutzergruppe
     * @param komp    die ID der Kompetenz
     * @param value   gibt an, ob die Kompetenz hinzugeüfgt wird oder entfernt wird
     * 
     * @return die Response 204 bei Erfolg.
     */
    public Response setKompetenz(Long id, Long komp, Boolean value) {
       try {
            conn.transactionBegin();
            if (value == null) 
                throw OperationError.CONFLICT.exception("Der Status, ob es sich um eine administrative Gruppe handelt, muss definitiert sein und darf daher nicht null sein.");
            getDTO(id);   // Prüfe, ob die Gruppe überhaupt in der DB definiert ist
            DTOBenutzergruppenKompetenz bgkomp = conn.queryByKey(DTOBenutzergruppenKompetenz.class, id, komp);
            if (value && bgkomp == null) {
                bgkomp = new DTOBenutzergruppenKompetenz(id, komp);
                conn.transactionPersist(bgkomp);
            } else if (!value && bgkomp != null) {
                conn.transactionRemove(bgkomp);
            }
            conn.transactionCommit();
        } catch (Exception e) {
            if (e instanceof WebApplicationException webApplicationException)
                return webApplicationException.getResponse();
            return OperationError.INTERNAL_SERVER_ERROR.getResponse();
        } finally {
            conn.transactionRollback();
        }
        return Response.status(Status.OK).build();
    }


    /**
     * Setzt für die angegebene Benutzergruppen-ID, ob die angegebene Kompetenz vorhanden
     * ist ist oder nicht.
     *  
     * @param id      die ID der Benutzergruppe
     * @param komp    die ID der Kompetenz
     * @param value   der InputStream mit dem Boolean-Wert
     * 
     * @return die Response 204 bei Erfolg.
     */
    public Response setKompetenzen(Long id, Long[] komp, Boolean value) {
        try {
            conn.transactionBegin();
            if (value == null) 
                throw OperationError.CONFLICT.exception("Der Status, ob es sich um eine administrative Gruppe handelt, muss definitiert sein und darf daher nicht null sein.");
            getDTO(id);   // Prüfe, ob die Gruppe überhaupt in der DB definiert ist
            for (Long k_id : komp) {
                DTOBenutzergruppenKompetenz bgkomp = conn.queryByKey(DTOBenutzergruppenKompetenz.class, id, k_id);
                if (value && bgkomp == null) {
                    bgkomp = new DTOBenutzergruppenKompetenz(id, k_id);
                    conn.transactionPersist(bgkomp);
                } else if (!value && bgkomp != null) {
                    conn.transactionRemove(bgkomp);
                }
                conn.transactionCommit();
            }
        } catch (Exception e) {
            if (e instanceof WebApplicationException webApplicationException)
                return webApplicationException.getResponse();
            return OperationError.INTERNAL_SERVER_ERROR.getResponse();
        } finally {
            conn.transactionRollback();
        }
        return Response.status(Status.OK).build();
    }
    
}

