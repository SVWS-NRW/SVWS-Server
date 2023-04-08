package de.svws_nrw.data.benutzer;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.benutzer.BenutzerDaten;
import de.svws_nrw.core.data.benutzer.BenutzergruppeDaten;
import de.svws_nrw.core.data.benutzer.Credentials;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.core.types.benutzer.BenutzerTyp;
import de.svws_nrw.core.types.schule.Schulform;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.benutzer.DTOBenutzer;
import de.svws_nrw.db.dto.current.schild.benutzer.DTOBenutzerAllgemein;
import de.svws_nrw.db.dto.current.schild.benutzer.DTOBenutzerKompetenz;
import de.svws_nrw.db.dto.current.schild.benutzer.DTOBenutzergruppe;
import de.svws_nrw.db.dto.current.schild.benutzer.DTOBenutzergruppenKompetenz;
import de.svws_nrw.db.dto.current.schild.benutzer.DTOBenutzergruppenMitglied;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.db.dto.current.svws.auth.DTOCredentials;
import de.svws_nrw.db.dto.current.svws.db.DTODBAutoInkremente;
import de.svws_nrw.db.dto.current.views.benutzer.DTOViewBenutzerdetails;
import de.svws_nrw.db.utils.OperationError;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link BenutzerDaten}.
 */
public final class DataBenutzerDaten extends DataManager<Long> {

    /**
     * Erstellt einen neuen {@link DataManager} für den Core-DTO
     * {@link BenutzerDaten}.
     *
     * @param conn die Datenbank-Verbindung für den Datenbankzugriff
     */
    public DataBenutzerDaten(final DBEntityManager conn) {
        super(conn);
    }

    /**
     * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs
     * {@link DTOViewBenutzerdetails} in einen Core-DTO {@link BenutzerDaten}.
     */
    private final Function<DTOViewBenutzerdetails, BenutzerDaten> dtoMapper = (final DTOViewBenutzerdetails b) -> {
        final BenutzerDaten daten = new BenutzerDaten();
        daten.id = b.ID;
        daten.typ = b.Typ.id;
        daten.typID = b.TypID;
        daten.anzeigename = b.AnzeigeName;
        daten.name = b.Benutzername;
        daten.istAdmin = (b.IstAdmin != null) && b.IstAdmin;
        daten.idCredentials = b.credentialID;
        return daten;
    };

    /**
     * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOBenutzergruppe}
     * in einen Core-DTO {@link BenutzergruppeDaten}.
     */
    private final Function<DTOBenutzergruppe, BenutzergruppeDaten> dtoMapperGruppe = (final DTOBenutzergruppe b) -> {
        final BenutzergruppeDaten daten = new BenutzergruppeDaten();
        daten.id = b.ID;
        daten.bezeichnung = b.Bezeichnung;
        daten.istAdmin = (b.IstAdmin != null) && b.IstAdmin;
        return daten;
    };

    /**
     * Bestimmt das DTO für den Benutzer aus der Datenbank.
     *
     * @param id die ID des Benutzers
     *
     * @return das DTO
     */
    private DTOViewBenutzerdetails getDTO(final Long id) throws WebApplicationException {
        if (id == null)
            throw OperationError.NOT_FOUND.exception("Die ID des zu änderden Benutzers darf nicht null sein.");
        final DTOViewBenutzerdetails benutzer = conn.queryByKey(DTOViewBenutzerdetails.class, id);
        if (benutzer == null)
            throw OperationError.NOT_FOUND.exception("Der Benutzer mit der ID existiert nicht.");
        return benutzer;
    }

    /**
     * Bestimmt die Anzahl der administrativen Gruppen, welche dem aktuellen
     * Benutzer administrative Rechte verleihen.
     *
     * @return die Anzahl der Gruppen über die der Benutzer administrative
     *         Berechtigungen hat
     */
    private int getAnzahlAdminGruppen() {
        // Ermittle die administrativen Gruppen des aktuellen Benutzers
        final List<DTOBenutzergruppe> bgs = conn.queryList(
                "SELECT bg FROM DTOBenutzergruppenMitglied bgm JOIN DTOBenutzergruppe bg "
                        + "ON bgm.Gruppe_ID = bg.ID and bgm.Benutzer_ID= ?1 and bg.IstAdmin= ?2 ",
                DTOBenutzergruppe.class,
                conn.getUser().getId(), true);
        return bgs.size();
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


    /**
     * Setzt für die angegebene Benutzer-ID den Benutzer administrativ.
     *
     * @param id die ID des Benutzers
     *
     * @return bei Erfolg eine HTTP-Response 200
     */
    public Response addAdmin(final Long id) {
        try {
            conn.transactionBegin();
            if (id == null)
                throw OperationError.NOT_FOUND.exception("Die ID der zu änderden Benutzer darf nicht null sein.");
            final DTOBenutzer benutzer = conn.queryByKey(DTOBenutzer.class, id);
            if (benutzer == null)
                throw OperationError.NOT_FOUND.exception("Der Benutzer mit der ID existiert nicht.");
            if (benutzer.IstAdmin)
                throw OperationError.BAD_REQUEST.exception("Der Benutzer hat bereits administrative Rechte.");
            benutzer.IstAdmin = true;
            conn.transactionPersist(benutzer);
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
     * Setzt für die angegebene Benutzer-ID die Benutzerkompetenzen.
     *
     * @param id   die ID des Benutzers
     * @param kids die IDs der Kompetenzen
     *
     * @return bei Erfolg eine HTTP-Response 200
     *
     * @throws WebApplicationException eine Exception mit dem entsprechenden
     *                                 HTTP-Fehlercode im Fehlerfall
     */
    public Response addKompetenzen(final Long id, final List<Long> kids) throws WebApplicationException {

        //Prüft, die Zulässigkeit der Kompetenzen für die Schulform
        this.istKompetenzZulaessig(kids);

    	if ((id == null) || (kids == null))
            return OperationError.NOT_FOUND.getResponse(
                    "Die ID der zu änderden Benutzer bzw IDs der Kompetenzen darf bzw. dürfen nicht null sein.");
        // Prüfe, ob der Benutzer mit der ID existiert.
        getDTO(id);
        // Prüfe, ob die Benutzerkompetenzen mit den Ids existieren.
        for (final Long kid : kids) {
            if (BenutzerKompetenz.getByID(kid) == null)
                throw OperationError.NOT_FOUND
                        .exception("Die Benutzerkompetenz mit der ID " + kid + " existiert nicht!!");
        }
        // Füge die Kompetenzen hinzu
        try {
            conn.transactionBegin();
            for (final Long kid : kids) {
                DTOBenutzerKompetenz bk = conn.queryByKey(DTOBenutzerKompetenz.class, id, kid);
                if (bk == null) {
                    bk = new DTOBenutzerKompetenz(id, kid);
                    conn.transactionPersist(bk);
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
        return Response.status(Status.OK).build();
    }

    /**
     * Erstellt einen neuen Benutzer *
     *
     * @param cred       Das JSON-Objekt mit den Daten für Credentials-Obejkt
     * @param anzeigename Anzeigename des neuen Benutzers
     *
     * @return Eine Response mit dem neuen Benutzer
     */
    public Response createBenutzerAllgemein(final Credentials cred, final String anzeigename) {
        DTOBenutzerAllgemein benutzer_allg = null;
        DTOBenutzer benutzer = null;
        DTOCredentials credential = null;
        Long c_ID = (long) 0;

        if (cred.benutzername == null || cred.password == null)
        	throw OperationError.BAD_REQUEST.exception("Benuzername oder Passwort leer!");

        try {
        	conn.transactionBegin();

            // Bestimme die ID des Benutzers / Credentials / BenutzerAllgemeins
            final DTODBAutoInkremente ba_lastID = conn.queryByKey(DTODBAutoInkremente.class, "BenutzerAllgemein");
            final Long ba_ID = ba_lastID == null ? 1 : ba_lastID.MaxID + 1;
            final DTODBAutoInkremente c_lastID = conn.queryByKey(DTODBAutoInkremente.class, "Credentials");
            c_ID = c_lastID == null ? 1 : c_lastID.MaxID + 1;
            final DTODBAutoInkremente b_lastID = conn.queryByKey(DTODBAutoInkremente.class, "Benutzer");
            final Long b_ID = b_lastID == null ? 1 : b_lastID.MaxID + 1;

            // Lege die Objekte an
            benutzer_allg = new DTOBenutzerAllgemein(ba_ID);
            benutzer_allg.AnzeigeName = anzeigename;

            benutzer = new DTOBenutzer(b_ID, BenutzerTyp.ALLGEMEIN, false);
            credential = new DTOCredentials(c_ID, cred.benutzername);
            credential.PasswordHash = Benutzer.erstellePasswortHash(cred.password);


            // Objekten miteinander verbinden
            benutzer.Allgemein_ID = benutzer_allg.ID;
            benutzer_allg.CredentialID = credential.ID;

            //Credential-Objekt persistieren
            conn.transactionPersist(credential);
            if (!conn.transactionCommit())
                return OperationError.CONFLICT.getResponse("Datenbankfehler beim Persistieren des Account-Credentials");
        } catch (final Exception e) {
            if (e instanceof final WebApplicationException webApplicationException)
                return webApplicationException.getResponse();
            return OperationError.INTERNAL_SERVER_ERROR.getResponse();
        } finally {
            conn.transactionRollback();
        }

        // BenutzerAllg.-Objekt persistieren
        try {
            conn.transactionBegin();
            conn.transactionPersist(benutzer_allg);
            if (!conn.transactionCommit())
                return OperationError.CONFLICT.getResponse("Datenbankfehler beim Persistieren des Betriebansprechpartners");
        } catch (final Exception e) {
           if (e instanceof final WebApplicationException webApplicationException)
               return webApplicationException.getResponse();
           return OperationError.INTERNAL_SERVER_ERROR.getResponse();
        } finally {
            conn.transactionRollback();
        }

        // Benutzer-Objekt persistieren
        try {
            conn.transactionBegin();
            conn.transactionPersist(benutzer);
            if (!conn.transactionCommit())
                return OperationError.CONFLICT.getResponse("Datenbankfehler beim Persistieren des Betriebansprechpartners");
        } catch (final Exception e) {
           if (e instanceof final WebApplicationException webApplicationException)
               return webApplicationException.getResponse();
           return OperationError.INTERNAL_SERVER_ERROR.getResponse();
        } finally {
            conn.transactionRollback();
        }
        final BenutzerDaten daten = dtoMapper.apply(getDTO(c_ID));
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
    }

    /**
     * Erstellt einen neuen Benutzer mit dem Benutzertyp Erzieher
     *
     * @return Eine Response mit dem neuen Benutzer
     */
    public Response createBenutzerErzieher() {
        throw new UnsupportedOperationException();
    }

    /**
     * Erstellt einen neuen Benutzer mit dem Benutzertyp Lehrer
     *
     * @return Eine Response mit dem neuen Benutzer
     */
    public Response createBenutzerLehrer() {
        throw new UnsupportedOperationException();
    }

    /**
     * Erstellt einen neuen Benutzer mit dem Benutzertyp Lehrer
     *
     * @return Eine Response mit dem neuen Benutzer
     */
    public Response createBenutzerSchueler() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Response getAll() {
        return this.getList();
    }

    @Override
    public Response getList() {
        throw new UnsupportedOperationException();
    }



    @Override
    public Response get(final Long id) {
        // Lese die Informationen zu den Gruppen ein
        final List<Long> gruppenIDs = conn
                .queryNamed("DTOBenutzergruppenMitglied.benutzer_id", id, DTOBenutzergruppenMitglied.class).stream()
                .map(g -> g.Gruppe_ID).toList();
        final List<DTOBenutzergruppe> gruppen = (gruppenIDs.size() == 0)
                ? new ArrayList<>()
                : conn.queryNamed("DTOBenutzergruppe.id.multiple", gruppenIDs, DTOBenutzergruppe.class);
        // Lese die Informationen zu den Kompetenzen ein
        final List<Long> kompetenzIDs = conn.queryNamed("DTOBenutzerKompetenz.benutzer_id", id, DTOBenutzerKompetenz.class)
                .stream().map(b -> b.Kompetenz_ID).sorted().toList();
        final List<DTOBenutzergruppenKompetenz> gruppenKompetenzen = (gruppenIDs.size() == 0)
                ? new ArrayList<>()
                : conn.queryNamed("DTOBenutzergruppenKompetenz.gruppe_id.multiple", gruppenIDs,
                        DTOBenutzergruppenKompetenz.class);
        final Map<Long, List<DTOBenutzergruppenKompetenz>> mapGruppenKompetenzen = gruppenKompetenzen.stream()
                .collect(Collectors.groupingBy(k -> k.Gruppe_ID));
        // Erstelle den Core-DTO für die API und gebe diesen zurück
        final BenutzerDaten daten = dtoMapper.apply(getDTO(id));

        for (final Long kompetenzID : kompetenzIDs)
            daten.kompetenzen.add(BenutzerKompetenz.getByID(kompetenzID).daten.id);

        for (final DTOBenutzergruppe gruppe : gruppen) {
            final BenutzergruppeDaten gdaten = dtoMapperGruppe.apply(gruppe);
            final List<DTOBenutzergruppenKompetenz> gruppenkompetenzen = mapGruppenKompetenzen.get(gruppe.ID);
            if (gruppenkompetenzen != null)
                for (final Long kompetenzID : gruppenkompetenzen.stream().map(k -> k.Kompetenz_ID).distinct().sorted()
                        .toList())
                    gdaten.kompetenzen.add(kompetenzID);
            daten.gruppen.add(gdaten);
        }
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
    }

    /**
     * Entfernt die Admin-Berechtigung des Benutzers.
     *
     * @param id die ID des Benutzers
     *
     * @return bei Erfolg eine HTTP-Response 200
     */
    public Response removeAdmin(final Long id) {
        try {
            conn.transactionBegin();
            if (id == null)
                throw OperationError.NOT_FOUND.exception("Die ID der zu änderden Benutzer darf nicht null sein.");
            final DTOBenutzer benutzer = conn.queryByKey(DTOBenutzer.class, id);
            if (benutzer == null)
                throw OperationError.NOT_FOUND.exception("Der Benutzer mit der ID existiert nicht.");
            if (!benutzer.IstAdmin)
                throw OperationError.BAD_REQUEST.exception("Der Benutzer mit der ID " + id
                        + " besitzt selbst direkt keine administrative Berechtigung, die entfernt werden könnte.");
            if ((id == conn.getUser().getId()) && (getAnzahlAdminGruppen() == 0))
                throw OperationError.BAD_REQUEST.exception(
                        "Der aktuelle Benutzer darf seine Admin-Berechtigung nicht entfernen, wenn er diese nicht zusätzlich über administrative Gruppen besitzt.");
            benutzer.IstAdmin = false;
            conn.transactionPersist(benutzer);
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
     * Löscht die Benutzer mit den IDs
     *
     * @param bids die IDs der Benutzer
     *
     * @return bei Erfolg eine HTTP-Response 200
     */
    public Response removeBenutzerAllgemein(final List<Long> bids) {
        // TODO Auto-generated method stub
        try {
            conn.transactionBegin();
            for (final Long id : bids) {

                final DTOViewBenutzerdetails v_benutzer = getDTO(id);

                //Ist der angemeldete Benutzer dabei?
                if (conn.getUser().getId() == id)
                    return OperationError.CONFLICT.getResponse("Der aktuelle User kann sich selber nicht löschen.");

                //Lese credential-ID
                final Long c_ID = v_benutzer.credentialID;
                if (c_ID == null)
                    throw OperationError.NOT_FOUND.exception("Der zu löschende Datensatz in DTOViewBentuerdetails mit der credential-ID" + id + " existiert nicht");
                //Credential löschen
                final DTOCredentials credential = conn.queryByKey(DTOCredentials.class, c_ID);
                if (credential == null)
                    throw OperationError.NOT_FOUND.exception("Der zu löschende Datensatz in DTOCredentials mit der ID" + id + " existiert nicht");
                conn.transactionRemove(credential);

                //Lese benutzer-ID
                final Long b_ID = v_benutzer.ID;
                // Benutzer löschen.
                // Damit werden die dazugehörige Datensätze in den Tabenllen DaBenutzerAllgemein, BenutzerKompetenzen und BenutzergruppenMitglieder gelöscht.
                final DTOBenutzer benutzer = conn.queryByKey(DTOBenutzer.class, b_ID);
                final DTOBenutzerAllgemein allg_benutzer =  conn.queryByKey(DTOBenutzerAllgemein.class, benutzer.Allgemein_ID);

                if (allg_benutzer == null)
                    throw OperationError.NOT_FOUND.exception("Der zu löschende Datensatz in DTOBenutzerAllgemein mit der ID" + id + " existiert nicht");
                conn.transactionRemove(allg_benutzer);
                //conn.transactionRemove(benutzer);
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
     * Entfernt für die angegebene Benutzer-ID die Benutzerkompetenzen.
     *
     * @param id   die ID des Benutzers
     * @param kids die IDs der Kompetenzen
     *
     * @return bei Erfolg eine HTTP-Response 204
     */
    public Response removeKompetenzen(final Long id, final List<Long> kids) {

      //Prüft, die Zulässigkeit der Kompetenzen für die Schulform
        this.istKompetenzZulaessig(kids);

        if (id == null || kids == null)
            return OperationError.NOT_FOUND.getResponse(
                    "Die ID der zu änderden Benutzer bzw IDs der Kompetenzen darf bzw. dürfen nicht null sein.");
        // Prüfe, ob der Benutzer mit der ID existiert.
        getDTO(id);
        // Prüfe, ob die Benutzerkompetenzen mit den Ids existieren.
        for (final Long kid : kids) {
            if (BenutzerKompetenz.getByID(kid) == null)
                return OperationError.NOT_FOUND
                        .getResponse("Die Benutzerkompetenz mit der ID " + kid + " existiert nicht!!");
        }
        try {
            conn.transactionBegin();
            for (final Long kid : kids) {
                // Bestimme den Datensatz aus DTOBenutzerKompetenz
                final DTOBenutzerKompetenz bk = conn.queryByKey(DTOBenutzerKompetenz.class, id, kid);
                if (bk == null)
                    throw OperationError.NOT_FOUND
                            .exception("Der zu löschende Datensatz in DTOBenutzerkompetenz mit Benutzer_ID" + id
                                    + "und Kompetenz_ID" + kid + " existiert nicht");
                // Entferne die Kompetenz
                conn.transactionRemove(bk);
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

    @Override
    public Response patch(final Long id, final InputStream is) {
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
    public Response setAnzeigename(final Long id, final String name) {
        try {
            conn.transactionBegin();
            if ((name == null) || "".equals(name))
                return OperationError.CONFLICT
                        .getResponse("Der Anzeigename muss gültig sein und darf nicht null oder leer sein");
            if (id == null)
                throw OperationError.NOT_FOUND.exception("Die ID der zu änderden Benutzer darf nicht null sein.");
            final DTOBenutzerAllgemein benutzerallgemein = conn.queryByKey(DTOBenutzerAllgemein.class, id);
            if (benutzerallgemein == null)
                throw OperationError.NOT_FOUND.exception("Der Benutzer mit der ID existiert nicht.");
            // Der Anzeigename wird nur bei den Benutzern mit dem Benutzertyp Allgemein
            // geändert.
            final DTOViewBenutzerdetails benutzerdetails = getDTO(id);
            if (benutzerdetails.Typ != BenutzerTyp.ALLGEMEIN)
                return OperationError.BAD_REQUEST.getResponse("Der Anzeigename kann bei dem Benutzer mit der ID" + id
                        + "aufgrund des Benutzertyps nicht geändert werden");
            // Der alte Anzeigename wurde übergeben.
            if (name.equals(benutzerallgemein.AnzeigeName))
                return Response.status(Status.OK).build();
            // Der Anzeigename wird angepasst...
            benutzerallgemein.AnzeigeName = name;
            conn.transactionPersist(benutzerallgemein);
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
     * Setzt für die angegebene Benutzer-ID den Anmeldenamen des Benutzers.
     *
     * @param id   die ID des Benutzers
     * @param name der neue Anmeldename
     *
     * @return die Response 200 bei Erfolg.
     */
    public Response setAnmeldename(final Long id, final String name) {
        try {
            conn.transactionBegin();
            if ((name == null) || "".equals(name))
                return OperationError.CONFLICT
                        .getResponse("Der Anmeldename muss gültig sein und darf nicht null oder leer sein");
            // TODO Ist es richtig?
            if (conn.getUser().getId() == id)
                return OperationError.CONFLICT.getResponse("Der aktuelle User kann seinen Benutzernamen nicht ändern.");
            final DTOViewBenutzerdetails benutzer = getDTO(id);
            // Der alte Anmeldename wurde übergeben.
            if (name.equals(benutzer.Benutzername))
                return Response.status(Status.OK).build();
            final DTOCredentials cred = conn.queryByKey(DTOCredentials.class, benutzer.credentialID);
            if (cred == null)
                return OperationError.INTERNAL_SERVER_ERROR
                        .getResponse("Dem Benutzer sind keine gültigen Credentials zugeordnet.");
            // Prüfe vorher, ob der Name nicht bereits verwendet wird -> Conflict
            final List<DTOCredentials> creds = conn.queryAll(DTOCredentials.class);
            for (final DTOCredentials data : creds) {
                if (name.trim().equals(data.Benutzername))
                    return OperationError.CONFLICT.getResponse("Ein Benutzer mit dem Namen existiert bereits");
            }
            cred.Benutzername = name;
            conn.transactionPersist(cred);
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
     * Erstellt für den Benutzer mit der übergebenen ID einen neuen Kennwort-Hash
     * basierend auf dem übergebenen Kennwort
     *
     * @param id       die ID des Benutzers, dessen Kennwort neu gesetzt werden soll
     * @param password das Kennwort
     *
     * @return bei Erfolg eine HTTP-Response 204
     */
    public Response setPassword(final Long id, final String password) {
        try {
            conn.transactionBegin();
            final String hash = Benutzer.erstellePasswortHash(password);
            final DTOViewBenutzerdetails benutzer = conn.queryByKey(DTOViewBenutzerdetails.class, id);
            if ((benutzer == null) || (benutzer.credentialID == null))
                throw OperationError.NOT_FOUND.exception();
            final DTOCredentials credential = conn.queryByKey(DTOCredentials.class, benutzer.credentialID);
            if (credential == null)
                throw OperationError.NOT_FOUND.exception();
            credential.PasswordHash = hash;
            conn.transactionCommit();
            return Response.status(Status.NO_CONTENT).build();
        } catch (final Exception exception) {
            conn.transactionRollback();
            if (exception instanceof final WebApplicationException webex)
                return webex.getResponse();
            throw exception;
        }
    }
}
