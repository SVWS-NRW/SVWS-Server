package de.nrw.schule.svws.data.benutzer;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.nrw.schule.svws.core.data.benutzer.BenutzerDaten;
import de.nrw.schule.svws.core.data.benutzer.BenutzergruppeDaten;
import de.nrw.schule.svws.core.types.benutzer.BenutzerKompetenz;
import de.nrw.schule.svws.core.types.benutzer.BenutzerTyp;
import de.nrw.schule.svws.data.DataManager;
import de.nrw.schule.svws.db.Benutzer;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.schild.benutzer.DTOBenutzer;
import de.nrw.schule.svws.db.dto.current.schild.benutzer.DTOBenutzerAllgemein;
import de.nrw.schule.svws.db.dto.current.schild.benutzer.DTOBenutzerKompetenz;
import de.nrw.schule.svws.db.dto.current.schild.benutzer.DTOBenutzergruppe;
import de.nrw.schule.svws.db.dto.current.schild.benutzer.DTOBenutzergruppenKompetenz;
import de.nrw.schule.svws.db.dto.current.schild.benutzer.DTOBenutzergruppenMitglied;
import de.nrw.schule.svws.db.dto.current.svws.auth.DTOCredentials;
import de.nrw.schule.svws.db.dto.current.views.benutzer.DTOViewBenutzerdetails;
import de.nrw.schule.svws.db.utils.OperationError;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link BenutzerDaten}.
 */
public class DataBenutzerDaten extends DataManager<Long> {

    /**
     * Erstellt einen neuen {@link DataManager} für den Core-DTO
     * {@link BenutzerDaten}.
     * 
     * @param conn die Datenbank-Verbindung für den Datenbankzugriff
     */
    public DataBenutzerDaten(DBEntityManager conn) {
        super(conn);
    }

    /**
     * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs
     * {@link DTOViewBenutzerdetails} in einen Core-DTO {@link BenutzerDaten}.
     */
    private Function<DTOViewBenutzerdetails, BenutzerDaten> dtoMapper = (DTOViewBenutzerdetails b) -> {
        BenutzerDaten daten = new BenutzerDaten();
        daten.id = b.ID;
        daten.typ = b.Typ.id;
        daten.typID = b.TypID;
        daten.anzeigename = b.AnzeigeName;
        daten.name = b.Benutzername;
        daten.istAdmin = b.IstAdmin == null ? false : b.IstAdmin;
        daten.idCredentials = b.credentialID;
        return daten;
    };

    /**
     * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOBenutzergruppe}
     * in einen Core-DTO {@link BenutzergruppeDaten}.
     */
    private Function<DTOBenutzergruppe, BenutzergruppeDaten> dtoMapperGruppe = (DTOBenutzergruppe b) -> {
        BenutzergruppeDaten daten = new BenutzergruppeDaten();
        daten.id = b.ID;
        daten.bezeichnung = b.Bezeichnung;
        daten.istAdmin = b.IstAdmin == null ? false : b.IstAdmin;
        return daten;
    };

    @Override
    public Response getAll() {
        return this.getList();
    }

    @Override
    public Response getList() {
        throw new UnsupportedOperationException();
    }

    /**
     * Bestimmt das DTO für den Benutzer aus der Datenbank.
     * 
     * @param id die ID des Benutzers
     * 
     * @return das DTO
     */
    private DTOViewBenutzerdetails getDTO(Long id) throws WebApplicationException {
        if (id == null)
            throw OperationError.NOT_FOUND.exception("Die ID der zu änderden Benutzer darf nicht null sein.");
        DTOViewBenutzerdetails benutzer = conn.queryByKey(DTOViewBenutzerdetails.class, id);
        if (benutzer == null)
            throw OperationError.NOT_FOUND.exception("Der Benutzer mit der ID existiert nicht.");
        return benutzer;
    }

    @Override
    public Response get(Long id) {
        // Lese die Informationen zu den Gruppen ein
        List<Long> gruppenIDs = conn
                .queryNamed("DTOBenutzergruppenMitglied.benutzer_id", id, DTOBenutzergruppenMitglied.class).stream()
                .map(g -> g.Gruppe_ID).toList();
        List<DTOBenutzergruppe> gruppen = conn.queryNamed("DTOBenutzergruppe.id.multiple", gruppenIDs,
                DTOBenutzergruppe.class);
        // Lese die Informationen zu den Kompetenzen ein
        List<Long> kompetenzIDs = conn.queryNamed("DTOBenutzerKompetenz.benutzer_id", id, DTOBenutzerKompetenz.class)
                .stream().map(b -> b.Kompetenz_ID).sorted().toList();
        List<DTOBenutzergruppenKompetenz> gruppenKompetenzen = conn.queryNamed(
                "DTOBenutzergruppenKompetenz.gruppe_id.multiple", gruppenIDs, DTOBenutzergruppenKompetenz.class);
        Map<Long, List<DTOBenutzergruppenKompetenz>> mapGruppenKompetenzen = gruppenKompetenzen.stream()
                .collect(Collectors.groupingBy(k -> k.Gruppe_ID));
        // Erstelle den Core-DTO für die API und gebe diesen zurück
        BenutzerDaten daten = dtoMapper.apply(getDTO(id));

        for (Long kompetenzID : kompetenzIDs)
            daten.kompetenzen.add(BenutzerKompetenz.getByID(kompetenzID).daten.id);

        for (DTOBenutzergruppe gruppe : gruppen) {
            BenutzergruppeDaten gdaten = dtoMapperGruppe.apply(gruppe);
            List<DTOBenutzergruppenKompetenz> gruppenkompetenzen = mapGruppenKompetenzen.get(gruppe.ID);
            if (gruppenkompetenzen != null)
                for (Long kompetenzID : gruppenkompetenzen.stream().map(k -> k.Kompetenz_ID).distinct().sorted()
                        .toList())
                    gdaten.kompetenzen.add(kompetenzID);
            daten.gruppen.add(gdaten);
        }
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
    }


    @Override
    public Response patch(Long id, InputStream is) {
        throw new UnsupportedOperationException();
    }


    /**
     * Setzt für die angegebene Benutzer-ID den Anzeigenamen.
     * 
     * @param id   die ID der Benutzergruppe
     * @param name der neue Azeigename
     * 
     * @return die Response 200 bei Erfolg.
     */
    public Response setAnzeigename(Long id, String name) {
        try {
            conn.transactionBegin();
            if ((name == null) || "".equals(name))
                return OperationError.CONFLICT.getResponse("Der Anzeigename muss gültig sein und darf nicht null oder leer sein");
            if (id == null)
                throw OperationError.NOT_FOUND.exception("Die ID der zu änderden Benutzer darf nicht null sein.");
            DTOBenutzerAllgemein benutzerallgemein = conn.queryByKey(DTOBenutzerAllgemein.class, id);
            if (benutzerallgemein == null)
                throw OperationError.NOT_FOUND.exception("Der Benutzer mit der ID existiert nicht.");
            // Der Anzeigename wird nur bei den Benutzern mit dem Benutzertyp Allgemein geändert.
            DTOViewBenutzerdetails benutzerdetails = getDTO(id);
            if (benutzerdetails.Typ != BenutzerTyp.ALLGEMEIN)
                return OperationError.BAD_REQUEST.getResponse("Der Anzeigename kann bei dem Benutzer mit der ID" + id + "aufgrund des Benutzertyps nicht geändert werden");
            // Der alte Anzeigename wurde übergeben.
            if (name.equals(benutzerallgemein.AnzeigeName))
                return Response.status(Status.OK).build();
            // Der Anzeigename wird angepasst...
            benutzerallgemein.AnzeigeName = name;
            conn.transactionPersist(benutzerallgemein);
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
     * Setzt für die angegebene Benutzer-ID den Anmeldenamen des Benutzers.
     * 
     * @param id   die ID des Benutzers
     * @param name der neue Anmeldename
     * 
     * @return die Response 200 bei Erfolg.
     */
    public Response setAnmeldename(Long id, String name) {
        try {
            conn.transactionBegin();
            if ((name == null) || "".equals(name))
                return OperationError.CONFLICT.getResponse("Der Anmeldename muss gültig sein und darf nicht null oder leer sein");
            DTOViewBenutzerdetails benutzer = getDTO(id);
            // Der alte Anmeldename wurde übergeben.
            if (name.equals(benutzer.Benutzername))
                return Response.status(Status.OK).build();
            DTOCredentials cred = conn.queryByKey(DTOCredentials.class, benutzer.credentialID);
            if (cred == null)
                return OperationError.INTERNAL_SERVER_ERROR.getResponse("Dem Benutzer sind keine gültigen Credentials zugeordnet.");
            // Prüfe vorher, ob der Name nicht bereits verwendet wird -> Conflict
            List<DTOCredentials> creds = conn.queryAll(DTOCredentials.class);
            for (DTOCredentials data : creds) {
                if (name.trim().equals(data.Benutzername))
                    return OperationError.CONFLICT.getResponse("Ein Benutzer mit dem Namen existiert bereits");
            }
            cred.Benutzername = name;
            conn.transactionPersist(cred);
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
     * Setzt für die angegebene Benutzer-ID die Benutzerkompetenzen.
     * 
     * @param id   die ID des Benutzers
     * @param kids die IDs der Kompetenzen
     * 
     * @return bei Erfolg eine HTTP-Response 200
     */
    public Response addKompetenzen(Long id, List<Long> kids) throws WebApplicationException {
        if (id == null || kids == null)
            throw OperationError.NOT_FOUND.exception(
                    "Die ID der zu änderden Benutzer bzw IDs der Kompetenzen darf bzw. dürfen nicht null sein.");
        if (kids.size() > 0) {
            // Prüfe, ob die Benutzerkompetenzen mit den Ids existieren.
            for (Long kid : kids) {
                if (BenutzerKompetenz.getByID(kid) == null)
                    throw OperationError.NOT_FOUND
                            .exception("Die Benutzerkompetenz mit der ID " + kid + " existiert nicht!!");
            }
            try {
                conn.transactionBegin();
                for (Long kid : kids) {
                    ArrayList<Long> ids = new ArrayList<Long>(Arrays.asList(id, kid));
                    //TODO ?????? Fehler
                    DTOBenutzerKompetenz bk = conn.queryByKey(DTOBenutzerKompetenz.class, id, kid);
                    if (bk == null) {
                        bk = new DTOBenutzerKompetenz(id, kid);
                        conn.transactionPersist(bk);
                    }
                }

                conn.transactionCommit();
            } catch (Exception e) {
                if (e instanceof WebApplicationException webApplicationException)
                    return webApplicationException.getResponse();
                return OperationError.INTERNAL_SERVER_ERROR.getResponse();
            } finally {
                conn.transactionRollback();
            }
        }
        return Response.status(Status.OK).build();
    }


    /**
     * Entfernt für die angegebene Benutzer-ID die Benutzerkompetenzen.
     * 
     * @param id     die ID des Benutzers
     * @param kids   die IDs der Kompetenzen
     * 
     * @return bei Erfolg eine HTTP-Response 204
     */
    public Response removeKompetenzen(Long id, List<Long> kids) throws WebApplicationException {
        // TODO
        throw new UnsupportedOperationException();
    }
    

    /**
     * Setzt für die angegebene Benutzer-ID, ob der Benutzer administrativ ist oder nicht.
     * 
     * @param id      die ID des Benutzers
     * @param value   der Boolean-Wert
     * 
     * @return bei Erfolg eine HTTP-Response 200
     */
    public Response setAdmin(Long id, Boolean value) {
        try {
            conn.transactionBegin();
            if (value == null)
                return OperationError.CONFLICT.getResponse("Der Status, ob es sich um einen administrativen Benutzer handelt, muss definitiert sein und darf daher nicht null sein.");
            if (id == null)
                throw OperationError.NOT_FOUND.exception("Die ID der zu änderden Benutzer darf nicht null sein.");
            DTOBenutzer benutzer = conn.queryByKey(DTOBenutzer.class, id);
            if (benutzer == null)
                throw OperationError.NOT_FOUND.exception("Der Benutzer mit der ID existiert nicht.");
            benutzer.IstAdmin = value;
            conn.transactionPersist(benutzer);
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
     * Erstellt für den Benutzer mit der übergebenen ID einen neuen Kennwort-Hash
     * basierend auf dem übergebenen Kennwort
     * 
     * @param id         die ID des Benutzers, dessen Kennwort neu gesetzt werden soll
     * @param password   das Kennwort
     * 
     * @return bei Erfolg eine HTTP-Response 204
     */
    public Response setPassword(Long id, String password) {
        try {
            conn.transactionBegin();
            String hash = Benutzer.erstellePasswortHash(password);
            DTOViewBenutzerdetails benutzer = conn.queryByKey(DTOViewBenutzerdetails.class, id);
            if ((benutzer == null) || (benutzer.credentialID == null))
                throw OperationError.NOT_FOUND.exception();
            DTOCredentials credential = conn.queryByKey(DTOCredentials.class, benutzer.credentialID);
            if (credential == null)
                throw OperationError.NOT_FOUND.exception();
            credential.PasswordHash = hash;
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
