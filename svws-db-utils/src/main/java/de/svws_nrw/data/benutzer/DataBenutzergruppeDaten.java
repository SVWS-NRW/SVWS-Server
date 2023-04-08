package de.svws_nrw.data.benutzer;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.benutzer.BenutzergruppeDaten;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.core.types.schule.Schulform;
import de.svws_nrw.core.utils.benutzer.BenutzergruppenManager;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.benutzer.DTOBenutzer;
import de.svws_nrw.db.dto.current.schild.benutzer.DTOBenutzergruppe;
import de.svws_nrw.db.dto.current.schild.benutzer.DTOBenutzergruppenKompetenz;
import de.svws_nrw.db.dto.current.schild.benutzer.DTOBenutzergruppenMitglied;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.db.dto.current.svws.db.DTODBAutoInkremente;
import de.svws_nrw.db.utils.OperationError;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link BenutzergruppeDaten}.
 */
public final class DataBenutzergruppeDaten extends DataManager<Long> {

    /**
     * Erstellt einen neuen {@link DataManager} für den Core-DTO
     * {@link BenutzergruppeDaten}.
     *
     * @param conn die Datenbank-Verbindung für den Datenbankzugriff
     */
    public DataBenutzergruppeDaten(final DBEntityManager conn) {
        super(conn);
    }

    /**
     * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOBenutzergruppe}
     */

    private final Function<DTOBenutzergruppe, BenutzergruppeDaten> dtoMapper = (final DTOBenutzergruppe k) -> {
        final BenutzergruppeDaten daten = new BenutzergruppeDaten();
        daten.id = k.ID;
        daten.bezeichnung = k.Bezeichnung;
        daten.istAdmin = k.IstAdmin;
     // Lese die Kompetenzen der Gruppe ein
        final List<Long> kompetenzIDs = conn
                .queryNamed("DTOBenutzergruppenKompetenz.gruppe_id", k.ID,
                        DTOBenutzergruppenKompetenz.class)
                .stream().map(g -> g.Kompetenz_ID).sorted().toList();
        for (final Long kompetenzID : kompetenzIDs)
            daten.kompetenzen.add(kompetenzID);
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
     * Bestimmt das DTO für die Benutzergruppe aus der Datenbank.
     *
     * @param id die ID der Benutzergruppe
     *
     * @return das DTO
     */
    private DTOBenutzergruppe getDTO(final Long id) throws WebApplicationException {
        if (id == null)
            throw OperationError.NOT_FOUND.exception("Die ID der zu ändernden Benutzergruppe darf nicht null sein.");
        final DTOBenutzergruppe bg = conn.queryByKey(DTOBenutzergruppe.class, id);
        if (bg == null)
            throw OperationError.NOT_FOUND.exception("Die Benutzergruppe mit der ID existiert nicht.");
        return bg;
    }

    /**
     * Erstellt den Benutzergruppen-Manager für die übergebene ID mit den
     * zugehörigen Daten aus der Datenbank.
     *
     * @param id die ID der Benutzergruppe
     *
     * @return der Benutzergruppen-Manager
     * @throws WebApplicationException im Fehlerfall
     */
    private BenutzergruppenManager getManager(final Long id) throws WebApplicationException {
        // Bestimme die Benutzergruppe
        final DTOBenutzergruppe benutzergruppe = getDTO(id);
        final BenutzergruppenManager manager = new BenutzergruppenManager(benutzergruppe.ID, benutzergruppe.Bezeichnung);
        manager.setAdmin((benutzergruppe.IstAdmin != null) && benutzergruppe.IstAdmin);
        // Lese die Kompetenzen der Gruppe ein
        final List<Long> kompetenzIDs = conn
                .queryNamed("DTOBenutzergruppenKompetenz.gruppe_id", benutzergruppe.ID,
                        DTOBenutzergruppenKompetenz.class)
                .stream().map(g -> g.Kompetenz_ID).sorted().toList();
        for (final Long kompetenzID : kompetenzIDs)
            manager.addKompetenz(BenutzerKompetenz.getByID(kompetenzID));
        return manager;
    }

    /**
     * Überprüft für die Schulform die zulässigkeit der Kompetenzen, die einem Objekt hinzugefügt bzw. entzogen werden.
     *
     * @param kids die IDs der Kompetenzen
     *
     * @return true, wenn alle Kompetenzen zulässig sind, sonst false
     *
     */
    private boolean istKompetenzZulaessig(final List<Long> kids) throws WebApplicationException {
      //Überprüfe die Zulässigkeit der Kompetenzen für die Schulform
        //Nehme als Schulform GY als Beispiel

        final DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
        if (schule == null)
            throw OperationError.NOT_FOUND.exception("Keine Schule angelegt.");
        final Schulform schulform = Schulform.getByNummer(schule.SchulformNr);

        final List<BenutzerKompetenz> bks = new ArrayList<>();
        for (final Long kid:kids) {
            bks.add(BenutzerKompetenz.getByID(kid));
        }

        for (final BenutzerKompetenz bk : bks) {
            if (!bk.hatSchulform(schulform))
                throw OperationError.FORBIDDEN.exception("Die Kompetenz" + bk.daten.bezeichnung + "ist für die Schulform"
                                                        + schulform.daten.bezeichnung + "nicht zulässig");
        }
        return true;
    }



    @Override
    public Response get(final Long id) throws WebApplicationException {
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(getManager(id).daten()).build();
    }

    @Override
    public Response patch(final Long id, final InputStream is) {
        throw new UnsupportedOperationException();
    }

    /**
     * Setzt für die angegebene Benutzergruppen-ID die Bezeichnung.
     *
     * @param id          die ID der Benutzergruppe
     * @param bezeichnung die neue Bezeichnung
     *
     * @return die Response 204 bei Erfolg.
     */
    public Response setBezeichnung(final Long id, final String bezeichnung) {
        try {
            conn.transactionBegin();
            if ((bezeichnung == null) || "".equals(bezeichnung))
                return OperationError.CONFLICT
                        .getResponse("Die Bezeichnung muss gültig sein und darf nicht null oder leer sein");
            final DTOBenutzergruppe bg = getDTO(id);
            bg.Bezeichnung = bezeichnung;
            conn.transactionPersist(bg);
            conn.transactionCommit();
        } catch (final Exception e) {
            if (e instanceof final WebApplicationException webApplicationException)
                return webApplicationException.getResponse();
            return OperationError.INTERNAL_SERVER_ERROR.getResponse();
        } finally {
            conn.transactionRollback();
        }
        return Response.status(Status.NO_CONTENT).build();
    }

    /**
     * Setzt für die angegebene Benutzergruppe-ID die Benutzergruppe administrativ.
     *
     * @param id die ID der Benutzergruppe
     *
     * @return die Response 204 bei Erfolg.
     */
    public Response addAdmin(final Long id) {
        try {
            conn.transactionBegin();
            final DTOBenutzergruppe bg = getDTO(id);
            if (!bg.IstAdmin) {
                bg.IstAdmin = true;
                conn.transactionPersist(bg);
            }
            conn.transactionCommit();
        } catch (final Exception e) {
            if (e instanceof final WebApplicationException webApplicationException)
                return webApplicationException.getResponse();
            return OperationError.INTERNAL_SERVER_ERROR.getResponse();
        } finally {
            conn.transactionRollback();
        }
        return Response.status(Status.OK).build();
    }

    /**
     * Entfernt die Admin-Berechtigung der Benutzergruppe.
     *
     * @param id die ID der Benutzergruppe
     *
     * @return bei Erfolg eine HTTP-Response 200
     */
    public Response removeAdmin(final Long id) {
        try {
            conn.transactionBegin();
            final DTOBenutzergruppe bg = getDTO(id);
            if (!bg.IstAdmin)
                throw OperationError.BAD_REQUEST.exception("Die Gruppe mit der ID " + id + " ist nicht administrativ, "
                        + "weshalb keine Admin-Berechtigung entfernt werden kann.");
            pruefeAdminUeberGruppe(id);
            bg.IstAdmin = false;
            conn.transactionPersist(bg);
            conn.transactionCommit();
        } catch (final Exception e) {
            if (e instanceof final WebApplicationException webApplicationException)
                return webApplicationException.getResponse();
            return OperationError.INTERNAL_SERVER_ERROR.getResponse();
        } finally {
            conn.transactionRollback();
        }
        return Response.status(Status.OK).build();
    }


    /**
     * Fügt die übergebenen Kompetenzen bei der Benutzergruppe hinzu.
     *
     * @param id   die ID der Benutzergruppe
     * @param kids die ID der Kompetenz
     *
     * @return die Response 204 bei Erfolg.
     */
    public Response addKompetenzen(final Long id, final List<Long> kids) {

    	//Überprüfe die Zulässigkeit der Kompetenzen für die Schulform
    	this.istKompetenzZulaessig(kids);

    	if (id == null || kids == null)
            throw OperationError.NOT_FOUND.exception(
                    "Die ID der zu ändernden Benutzergruppe bzw IDs der Kompetenzen darf bzw. dürfen nicht null sein.");
        getDTO(id); // Prüfe, ob die Gruppe überhaupt in der DB definiert ist
        if (kids.size() > 0) {
            // Prüfe, ob die Benutzerkompetenzen mit den Ids existieren.
            for (final Long kid : kids) {
                if (BenutzerKompetenz.getByID(kid) == null)
                    throw OperationError.NOT_FOUND
                            .exception("Die Benutzerkompetenz mit der ID " + kid + " existiert nicht!!");
            }
            try {
                conn.transactionBegin();
                for (final Long k_id : kids) {
                    // Hat die Gruppe die Kompetenz?
                    DTOBenutzergruppenKompetenz bgkomp = conn.queryByKey(DTOBenutzergruppenKompetenz.class, id, k_id);
                    // Nein, also hinzufügen.
                    if (bgkomp == null) {
                        bgkomp = new DTOBenutzergruppenKompetenz(id, k_id);
                        conn.transactionPersist(bgkomp);
                    }
                }
                conn.transactionCommit();
            } catch (final Exception e) {
                if (e instanceof final WebApplicationException webApplicationException)
                    return webApplicationException.getResponse();
                return OperationError.INTERNAL_SERVER_ERROR.getResponse();
            } finally {
                conn.transactionRollback();
            }
        }
        return Response.status(Status.OK).build();
    }


    /**
     * Entfernt die übergebenen Kompetenzen bei der Benutzergruppe.
     *
     * @param id   die ID der Benutzergruppe
     * @param kids die ID der Kompetenz
     *
     * @return die Response 204 bei Erfolg.
     */
    public Response removeKompetenzen(final Long id, final List<Long> kids) {
      //Überprüfe die Zulässigkeit der Kompetenzen für die Schulform
        this.istKompetenzZulaessig(kids);
        if (id == null || kids == null)
            throw OperationError.NOT_FOUND.exception(
                    "Die ID der zu ändernden Benutzergruppe bzw IDs der Kompetenzen darf bzw. dürfen nicht null sein.");
        getDTO(id); // Prüfe, ob die Gruppe überhaupt in der DB definiert ist
        if (kids.size() > 0) {
            // Prüfe, ob die Benutzerkompetenzen mit den Ids existieren.
            for (final Long kid : kids) {
                if (BenutzerKompetenz.getByID(kid) == null)
                    throw OperationError.NOT_FOUND
                            .exception("Die Benutzerkompetenz mit der ID " + kid + " existiert nicht!!");
            }
            try {
                conn.transactionBegin();
                for (final Long k_id : kids) {
                    // Hat die Gruppe die Kompetenz?
                    final DTOBenutzergruppenKompetenz bgkomp = conn.queryByKey(DTOBenutzergruppenKompetenz.class, id, k_id);
                    // Ja, also entfernen.
                    if (bgkomp != null)
                        conn.transactionRemove(bgkomp);
                }
                conn.transactionCommit();
            } catch (final Exception e) {
                if (e instanceof final WebApplicationException webApplicationException)
                    return webApplicationException.getResponse();
                return OperationError.INTERNAL_SERVER_ERROR.getResponse();
            } finally {
                conn.transactionRollback();
            }
        }
        return Response.status(Status.OK).build();
    }


    /**
     * Fügt die übergebenen Benutzer bei der Benutzergruppe hinzu.
     *
     * @param id   die ID der Benutzergruppe
     * @param bids die ID der Benutzer
     *
     * @return die Response 200 bei Erfolg.
     */
    public Response addBenutzer(final Long id, final List<Long> bids) {
        if ((id == null) || (bids == null))
            return OperationError.NOT_FOUND.getResponse("Die ID der zu ändernden Benutzergruppe bzw IDs der Benutzer darf "
                    + "bzw. dürfen nicht null sein.");
        getDTO(id); // Prüfe, ob die Gruppe überhaupt in der DB definiert ist
        if (bids.size() > 0) {
            // Prüfe, ob die Benutzer mit den Ids existieren.
            for (final Long bid : bids) {
                final DTOBenutzer benutzer = conn.queryByKey(DTOBenutzer.class, bid);
                if (benutzer == null)
                    return OperationError.NOT_FOUND.getResponse("Der Benutzermit der ID " + bid + " existiert nicht!");
            }
            try {
                conn.transactionBegin();
                for (final Long bid : bids) {
                    // Hat die Gruppe den Benutzer?
                    DTOBenutzergruppenMitglied bgm = conn.queryByKey(DTOBenutzergruppenMitglied.class, id, bid);
                    // Nein, also hinzufügen.
                    if (bgm == null) {
                        bgm = new DTOBenutzergruppenMitglied(id, bid);
                        conn.transactionPersist(bgm);
                    }
                }
                conn.transactionCommit();
            } catch (final Exception e) {
                if (e instanceof final WebApplicationException webApplicationException)
                    return webApplicationException.getResponse();
                return OperationError.INTERNAL_SERVER_ERROR.getResponse();
            } finally {
                conn.transactionRollback();
            }
        }
        final BenutzergruppeDaten daten = dtoMapper.apply(getDTO(id));
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
    }

    /**
     * Entfernt die übergebenen Benutzer der Benutzergruppe.
     *
     * @param id    die ID der Benutzergruppe
     * @param bids  die ID der Benutzer
     *
     * @return die Response 200 bei Erfolg.
     */
    public Response removeBenutzer(final Long id, final List<Long> bids) {
        if (id == null || bids == null)
            return OperationError.NOT_FOUND.getResponse("Die ID der zu ändernden Benutzergruppe bzw IDs der Benutzer darf "
                    + "bzw. dürfen nicht null sein.");
        getDTO(id); // Prüfe, ob die Gruppe überhaupt in der DB definiert ist
        if (bids.size() > 0) {
        	// Prüfe, ob die Benutzer mit den Ids existieren.
        	final List<DTOBenutzer> benutzer = conn.queryNamed("DTOBenutzer.id.multiple", bids, DTOBenutzer.class);
        	if (benutzer.size() != bids.size())
        		return OperationError.NOT_FOUND.getResponse("Ein übergebener Benutzer existiert nicht!");
            try {
                conn.transactionBegin();
                for (final Long bid : bids) {
                    // Prüfe zunächst, ob der Benutzer überhaupt Mitglied der Gruppe ist...
                    final DTOBenutzergruppenMitglied bgm = conn.queryByKey(DTOBenutzergruppenMitglied.class, id, bid);
                    if (bgm == null)
                        throw OperationError.NOT_FOUND.exception("Der Benutzer mit der ID " + bid + " kann nicht aus der Gruppe "
                                + "mit der ID " + id + " entfernt werden, da dieser nicht Mitglied in der Gruppe ist.");
                    // Prüfe, ob der zu entfernende Benutzer der aktuelle Benutzer und die betroffene Gruppe administrativ ist...
                    if ((conn.getUser().getId() == bid) && (getDTO(id).IstAdmin))
                        pruefeAdminUeberGruppe(id);
                    conn.transactionRemove(bgm);
                }
                conn.transactionCommit();
            } catch (final Exception e) {
            	if (e instanceof final WebApplicationException webApplicationException)
            		return webApplicationException.getResponse();
            	return OperationError.INTERNAL_SERVER_ERROR.getResponse();
            } finally {
            	conn.transactionRollback();
            }
        }
        final BenutzergruppeDaten daten = dtoMapper.apply(getDTO(id));
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
    }


    /**
     * Prüft, ob der aktuelle Benutzer mit der übergebenen ID, welcher admin-Rechte
     * besitzt, diese Rechte in Abhängigkeit von der Benutzergruppe mit der übergebenen ID
     * besitzt.
     *
     * @param idGruppe     die ID der Gruppe
     *
     * @throws WebApplicationException   falls die Admin-Berechitigung des aktuellen Benutzers
     *                                   von der Admin-Berechtigung der angegebenen Gruppe abhängt.
     */
    private void pruefeAdminUeberGruppe(final long idGruppe) throws WebApplicationException {
        // Ermittle den aktuellen Benutzer und prüfe, ob dieser direkt als Benutzer Admin-Rechte besitzt.
        final DTOBenutzer benutzer = conn.queryByKey(DTOBenutzer.class, conn.getUser().getId());
        if (benutzer.IstAdmin)
            return;

        // Ermittle die administrativen Gruppen des aktuellen Benutzers
        final List<DTOBenutzergruppe> bgs = conn.queryList("SELECT bg FROM DTOBenutzergruppenMitglied bgm JOIN DTOBenutzergruppe bg "
                + "ON bgm.Gruppe_ID = bg.ID and bgm.Benutzer_ID= ?1 and bg.IstAdmin= ?2 ", DTOBenutzergruppe.class,
                conn.getUser().getId(), true);

        // Prüfe, ob der aktuelle Benutzer überhaupt eine Admin-Berechtigung über eine administrative Gruppe hat
        if (bgs.size() == 0) {
            // Dieser Fall sollte nicht auftreten, da der aktuelle Benutzer dann weder als Benutzer
            // noch über eine Gruppe administrative Berechtigungen erhalte hätte und diese Operation
            // unzulässig wäre...
            throw OperationError.INTERNAL_SERVER_ERROR.exception("Der aktuelle Benutzer verfügt über keine "
                    + "administrative Berechtigung und darf daher diese API-Methode nicht aufrufen.");
        }
        if ((bgs.size() == 1) && (bgs.get(0).ID.longValue() == idGruppe)) {
            // Der aktuelle Benutzer ist nur in genau der administrativen Gruppe,
            // aus der er entfernt werden soll. Dies ist nicht zulässig.
            throw OperationError.FORBIDDEN.exception("Der aktuelle Benutzer bezieht seine administrative Berechtigung über "
                    + "die Benutzergruppe und würde die administrative Berechtigung durch diese Operation verlieren.");
        }
    }

    /**
     * Erstellt eine neue Benutzergruppe
     *
     * @param is       Das JSON-Objekt mit den Daten
     *
     * @return Eine Response mit dem neuen Benutzer
     */
    public Response create(final InputStream is) {
        DTOBenutzergruppe bg = null;


        final Map<String, Object> map = JSONMapper.toMap(is);
        if (map.size() > 0) {
            try {
                conn.transactionBegin();

                // Bestimme die ID der neuen Benutzergruppe
                final DTODBAutoInkremente lastID = conn.queryByKey(DTODBAutoInkremente.class, "Benutzergruppen");
                final Long id = lastID == null ? 1 : lastID.MaxID + 1;

                // TODO Konstruktor-Parameter überprüfen
                bg = new DTOBenutzergruppe(id, "temp", false);
                for (final Entry<String, Object> entry : map.entrySet()) {
                    final String key = entry.getKey();
                    final Object value = entry.getValue();
                    switch (key) {
                        case "id" -> {
                            final Long create_id = JSONMapper.convertToLong(value, true);
                            if (create_id != null && create_id != -1)
                                throw OperationError.BAD_REQUEST.exception("ID muss leer sein.");
                        }
                        case "bezeichnung" -> bg.Bezeichnung = JSONMapper.convertToString(value, true, true);
                        case "istAdmin" -> bg.IstAdmin = JSONMapper.convertToBoolean(value, true);
                        case "kompetenzen" -> System.out.println("//TODO Kompetenzen bei Benutzergruppe-Inputstream Create");
                        default -> throw OperationError.BAD_REQUEST.exception();
                    }
                }
                conn.transactionPersist(bg);
                if (!conn.transactionCommit())
                    return OperationError.CONFLICT
                            .getResponse("Datenbankfehler beim Persistieren des Betriebansprechpartners");
            } catch (final Exception e) {
                if (e instanceof final WebApplicationException webApplicationException)
                    return webApplicationException.getResponse();
                return OperationError.INTERNAL_SERVER_ERROR.getResponse();
            } finally {
                conn.transactionRollback();
            }
        }
        if (bg == null)
            throw OperationError.NOT_FOUND.exception();
        final BenutzergruppeDaten daten = dtoMapper.apply(getDTO(bg.ID));
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();

    }

    /**
     * Entfernt  die Benutzergruppen mit den IDs
     *
     * @param bgids    die IDs der Bentuzergruppen
     *
     * @return bei Erfolg eine HTTP-Response 200
     */
    public Response remove(final List<Long> bgids) {
        try {
            conn.transactionBegin();
           final DTOBenutzer user = conn.queryByKey(DTOBenutzer.class, conn.getUser().getId());
            if (user == null)
                throw OperationError.NOT_FOUND.exception("Der User-Benutzer existiert nicht !!!");

            //In diesem Fall wird der mögliche Verlust der Adminberechtigung des Benutzers über Gruppen überprüft und ggf. verhindert
            if (!user.IstAdmin) {

              //Lese die Benutzergruppen_IDs des Users ein.
                final List<Long> user_gruppen_ids = conn
                        .queryNamed("DTOBenutzergruppenMitglied.benutzer_id", conn.getUser().getId(),
                                DTOBenutzergruppenMitglied.class)
                        .stream().map(g -> g.Gruppe_ID).sorted().toList();

                //Lese die IDs der administrativen Benutzergruppen aus user_gruppen_ids ein
                final List<Long>  user_admingruppen_ids = new ArrayList<>();
                for (final Long id : user_gruppen_ids)
                    if (getDTO(id).IstAdmin)
                        user_admingruppen_ids.add(id);

                //Lese aus den bgids die ids der administrativen Benutzergruppen vom User.
                final List<Long> user_admingruppen_ids_request = bgids.stream().filter(item -> getDTO(item).IstAdmin && user_admingruppen_ids.contains(item)).collect(Collectors.toList());

                if (user_admingruppen_ids_request.size() == user_admingruppen_ids.size())
                    throw OperationError.BAD_REQUEST.exception("Der Löschvorgang ist nicht erlaubt, weil dadurch die Adminberechtigung des Benutzers entfernt wird.");
            }

            for (final Long id : bgids)
                 conn.transactionRemove(getDTO(id));
            conn.transactionCommit();
        } catch (final Exception e) {
            if (e instanceof final WebApplicationException webApplicationException)
                return webApplicationException.getResponse();
            return OperationError.INTERNAL_SERVER_ERROR.getResponse();
        } finally {
            conn.transactionRollback();
        }
        return Response.status(Status.OK).build();
    }
}

