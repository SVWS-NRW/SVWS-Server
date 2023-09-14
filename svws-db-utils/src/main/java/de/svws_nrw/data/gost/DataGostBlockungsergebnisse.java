package de.svws_nrw.data.gost;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.gost.GostBlockungKurs;
import de.svws_nrw.core.data.gost.GostBlockungsergebnis;
import de.svws_nrw.core.data.gost.GostBlockungsergebnisKurs;
import de.svws_nrw.core.data.gost.GostBlockungsergebnisListeneintrag;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.data.schueler.Schueler;
import de.svws_nrw.core.types.KursFortschreibungsart;
import de.svws_nrw.core.types.Note;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.utils.gost.GostBlockungsdatenManager;
import de.svws_nrw.core.utils.gost.GostBlockungsergebnisManager;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.data.schueler.DBUtilsSchuelerLernabschnittsdaten;
import de.svws_nrw.data.schule.SchulUtils;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.gost.DTOGostSchuelerFachbelegungen;
import de.svws_nrw.db.dto.current.gost.kursblockung.DTOGostBlockung;
import de.svws_nrw.db.dto.current.gost.kursblockung.DTOGostBlockungKurs;
import de.svws_nrw.db.dto.current.gost.kursblockung.DTOGostBlockungKurslehrer;
import de.svws_nrw.db.dto.current.gost.kursblockung.DTOGostBlockungSchiene;
import de.svws_nrw.db.dto.current.gost.kursblockung.DTOGostBlockungZwischenergebnis;
import de.svws_nrw.db.dto.current.gost.kursblockung.DTOGostBlockungZwischenergebnisKursSchiene;
import de.svws_nrw.db.dto.current.gost.kursblockung.DTOGostBlockungZwischenergebnisKursSchueler;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKurs;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKursLehrer;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLeistungsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.dto.current.schema.DTOSchemaAutoInkremente;
import de.svws_nrw.db.dto.current.views.gost.DTOViewGostSchuelerAbiturjahrgang;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.OperationError;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link GostBlockungsergebnis}.
 */
public final class DataGostBlockungsergebnisse extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link GostBlockungsergebnis}.
	 *
	 * @param conn         die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataGostBlockungsergebnisse(final DBEntityManager conn) {
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
	void getErgebnisListe(@NotNull final GostBlockungsdatenManager datenManager) {
	    // Bestimme die Liste der Ergebnisse aus der Datenbank
        final List<DTOGostBlockungZwischenergebnis> ergebnisse = conn.queryNamed(
                "DTOGostBlockungZwischenergebnis.blockung_id", datenManager.getID(), DTOGostBlockungZwischenergebnis.class);
        if (ergebnisse == null)
            throw OperationError.NOT_FOUND.exception();
        final List<Long> ergebnisIDs = ergebnisse.stream().map(e -> e.ID).toList();
        if (ergebnisIDs.isEmpty()) // Es muss immer mindestens ein aktuelles Ergebnis vorliegen
            throw OperationError.INTERNAL_SERVER_ERROR.exception();

        // Bestimme die Kurs-Schienen-Zuordnungen für alle Zwischenergebnisse
        final Map<Long, List<DTOGostBlockungZwischenergebnisKursSchiene>> mapKursSchienen =
            conn.queryNamed("DTOGostBlockungZwischenergebnisKursSchiene.zwischenergebnis_id.multiple", ergebnisIDs,
                    DTOGostBlockungZwischenergebnisKursSchiene.class)
                .stream().collect(Collectors.groupingBy(e -> e.Zwischenergebnis_ID, Collectors.toList()));

        // Bestimme die Kurs-Schüler-Zuordnungen für alle Zwischenergebnisse
        final Map<Long, List<DTOGostBlockungZwischenergebnisKursSchueler>> mapKursSchueler =
            conn.queryNamed("DTOGostBlockungZwischenergebnisKursSchueler.zwischenergebnis_id.multiple", ergebnisIDs,
                    DTOGostBlockungZwischenergebnisKursSchueler.class)
                .stream().collect(Collectors.groupingBy(e -> e.Zwischenergebnis_ID, Collectors.toList()));

	    // Durchwandere alle Ergebnisse
        for (final DTOGostBlockungZwischenergebnis erg : ergebnisse) {
            // Erstelle zunächst das Core-DTO für das Ergebnis mit Bewertung
            final var manager = new GostBlockungsergebnisManager(datenManager, erg.ID);
            final var listSchienenKurse = mapKursSchienen.getOrDefault(erg.ID, Collections.emptyList());
            final var listKursSchueler = mapKursSchueler.getOrDefault(erg.ID, Collections.emptyList());
            for (final var ks : listSchienenKurse)
                manager.setKursSchiene(ks.Blockung_Kurs_ID, ks.Schienen_ID, true);
            for (final var ks : listKursSchueler)
                manager.setSchuelerKurs(ks.Schueler_ID, ks.Blockung_Kurs_ID, true);
            final GostBlockungsergebnis ergebnis = manager.getErgebnis();
            ergebnis.istMarkiert = erg.IstMarkiert != null && erg.IstMarkiert;
            ergebnis.istVorlage = erg.IstVorlage != null && erg.IstVorlage;

            // Hinzufügen des Ergebnis-Listeneintrags zu den Blockungsdaten
            final var eintrag = new GostBlockungsergebnisListeneintrag();
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
	GostBlockungsergebnis getErgebnis(@NotNull final DTOGostBlockungZwischenergebnis ergebnis,
	        @NotNull final GostBlockungsdatenManager datenManager) throws WebApplicationException {
        final GostBlockungsergebnisManager manager = new GostBlockungsergebnisManager(datenManager, ergebnis.ID);
        // Bestimme alle Kurs-Schienen-Zuordnungen
        final List<DTOGostBlockungZwischenergebnisKursSchiene> listSchienenKurse = conn
                .queryNamed("DTOGostBlockungZwischenergebnisKursSchiene.zwischenergebnis_id", ergebnis.ID, DTOGostBlockungZwischenergebnisKursSchiene.class);
        // Bestimme alle Kurs-Schüler-Zuordnungen
        final List<DTOGostBlockungZwischenergebnisKursSchueler> listKursSchueler = conn
                .queryNamed("DTOGostBlockungZwischenergebnisKursSchueler.zwischenergebnis_id", ergebnis.ID, DTOGostBlockungZwischenergebnisKursSchueler.class);
        for (final DTOGostBlockungZwischenergebnisKursSchiene ks : listSchienenKurse)
            manager.setKursSchiene(ks.Blockung_Kurs_ID, ks.Schienen_ID, true);
        for (final DTOGostBlockungZwischenergebnisKursSchueler ks : listKursSchueler)
            manager.setSchuelerKurs(ks.Schueler_ID, ks.Blockung_Kurs_ID, true);
        final GostBlockungsergebnis daten = manager.getErgebnis();
        daten.istMarkiert = ergebnis.IstMarkiert != null && ergebnis.IstMarkiert;
        daten.istVorlage = ergebnis.IstVorlage != null && ergebnis.IstVorlage;
        return daten;
	}


	@Override
	public Response get(final Long id) {
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
		// Bestimme das Blockungs-Zwischenergebnis
		final DTOGostBlockungZwischenergebnis ergebnis = conn.queryByKey(DTOGostBlockungZwischenergebnis.class, id);
		if (ergebnis == null)
			return OperationError.NOT_FOUND.getResponse();
		final GostBlockungsdatenManager datenManager = (new DataGostBlockungsdaten(conn)).getBlockungsdatenManagerFromDB(ergebnis.Blockung_ID);
		// Bestimme die Daten des Ergebnisses
        final GostBlockungsergebnis daten = getErgebnis(ergebnis, datenManager);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}



	@Override
	public Response patch(final Long id, final InputStream is) {
		final Map<String, Object> map = JSONMapper.toMap(is);
		if (map.size() <= 0)
			return Response.status(Status.OK).build();
		try {
			conn.transactionBegin();
			DBUtilsGost.pruefeSchuleMitGOSt(conn);
			// Bestimme die Blockung
			final DTOGostBlockungZwischenergebnis ergebnis = conn.queryByKey(DTOGostBlockungZwischenergebnis.class, id);
			if (ergebnis == null)
				return OperationError.NOT_FOUND.getResponse();
			for (final Entry<String, Object> entry : map.entrySet()) {
				final String key = entry.getKey();
				final Object value = entry.getValue();
				switch (key) {
					case "id" -> {
						final Long patch_id = JSONMapper.convertToLong(value, true);
						if ((patch_id == null) || (patch_id.longValue() != id.longValue()))
							throw OperationError.BAD_REQUEST.exception();
					}
					case "istMarkiert" -> ergebnis.IstMarkiert = JSONMapper.convertToBoolean(value, false);
					case "istVorlage" -> ergebnis.IstVorlage = JSONMapper.convertToBoolean(value, false);
					default -> throw OperationError.BAD_REQUEST.exception();
				}
			}
			conn.transactionPersist(ergebnis);
			conn.transactionCommit();
		} catch (final Exception e) {
			if (e instanceof final WebApplicationException webAppException)
				return webAppException.getResponse();
			return OperationError.INTERNAL_SERVER_ERROR.getResponse();
		} finally {
			// Perform a rollback if necessary
			conn.transactionRollback();
		}
		return Response.status(Status.OK).build();
	}


	/**
	 * Entfernt das Zwischenergebnis mit der angegebenen ID aus der Datenbank.
	 *
	 * @param id   die ID des zu löschenden Blockungsergebnis
	 *
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
	 */
	public Response delete(final Long id) {
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
		// Bestimme das Zwischenergebnis
		final DTOGostBlockungZwischenergebnis erg = conn.queryByKey(DTOGostBlockungZwischenergebnis.class, id);
		if (erg == null)
			return OperationError.NOT_FOUND.getResponse();
		// Entferne das Ergebnis
		conn.remove(erg);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(id).build();
	}



	private void _createKursSchuelerZuordnung(final Long idZwischenergebnis, final Long idSchueler, final Long idKurs) {
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
		if (idSchueler == null)
			throw OperationError.CONFLICT.exception();
		// Bestimme das Blockungs-Zwischenergebnis
		final DTOGostBlockungZwischenergebnis ergebnis = conn.queryByKey(DTOGostBlockungZwischenergebnis.class, idZwischenergebnis);
		if (ergebnis == null)
			throw OperationError.NOT_FOUND.exception();
		// Bestimme die zugehörige Blockung
		final DTOGostBlockung blockung = conn.queryByKey(DTOGostBlockung.class, ergebnis.Blockung_ID);
		if (blockung == null)
			throw OperationError.NOT_FOUND.exception();
    	// Bestimme alle Schüler-IDs für den Abiturjahrgang der Blockung
		final List<DTOViewGostSchuelerAbiturjahrgang> schuelerAbijahrgang = conn.queryNamed("DTOViewGostSchuelerAbiturjahrgang.abiturjahr", blockung.Abi_Jahrgang, DTOViewGostSchuelerAbiturjahrgang.class);
		if ((schuelerAbijahrgang == null) || (schuelerAbijahrgang.isEmpty()))
			throw OperationError.NOT_FOUND.exception();
		final Set<Long> schuelerIDs = schuelerAbijahrgang.stream().map(s -> s.ID).collect(Collectors.toSet());
		if (!schuelerIDs.contains(idSchueler))
			throw OperationError.CONFLICT.exception();
		// Bestimme die Kurse, welche für die Blockung angelegt wurden
		final DTOGostBlockungKurs kurs = conn.queryByKey(DTOGostBlockungKurs.class, idKurs);
		if (kurs == null)
			throw OperationError.NOT_FOUND.exception();
		if (!Objects.equals(kurs.Blockung_ID, ergebnis.Blockung_ID))
			throw OperationError.CONFLICT.exception();

		// Füge die neue Kurszuordnung hinzu
		conn.transactionPersist(new DTOGostBlockungZwischenergebnisKursSchueler(idZwischenergebnis, idKurs, idSchueler));
	}



	private void _deleteKursSchuelerZuordnung(final Long idZwischenergebnis, final Long idSchueler, final Long idKurs) {
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
		if ((idSchueler == null) || (idKurs == null))
			throw OperationError.CONFLICT.exception();
		// Entferne die Zuordnung
		final DTOGostBlockungZwischenergebnisKursSchueler dto = conn.queryByKey(DTOGostBlockungZwischenergebnisKursSchueler.class, idZwischenergebnis, idKurs, idSchueler);
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
	public Response createKursSchuelerZuordnung(final Long idZwischenergebnis, final Long idSchueler, final Long idKurs) {
		try {
			conn.transactionBegin();
			this._createKursSchuelerZuordnung(idZwischenergebnis, idSchueler, idKurs);
			conn.transactionCommit();
	        return Response.status(Status.NO_CONTENT).build();
		} catch (final Exception exception) {
			conn.transactionRollback();
			if (exception instanceof final WebApplicationException webex)
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
	public Response updateKursSchuelerZuordnung(final Long idZwischenergebnis, final Long idSchueler, final Long idKursAlt, final Long idKursNeu) {
		if (idKursNeu == null)
			return deleteKursSchuelerZuordnung(idZwischenergebnis, idKursAlt, idSchueler);
		try {
			conn.transactionBegin();
			// TODO prüfe, ob die Kursarten übereinstimmen...
			this._deleteKursSchuelerZuordnung(idZwischenergebnis, idSchueler, idKursAlt);
			this._createKursSchuelerZuordnung(idZwischenergebnis, idSchueler, idKursNeu);
			conn.transactionCommit();
	        return Response.status(Status.NO_CONTENT).build();
		} catch (final Exception exception) {
			conn.transactionRollback();
			if (exception instanceof final WebApplicationException webex)
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
	public Response deleteKursSchuelerZuordnung(final Long idZwischenergebnis, final Long idSchueler, final Long idKurs) {
		try {
			conn.transactionBegin();
			this._deleteKursSchuelerZuordnung(idZwischenergebnis, idSchueler, idKurs);
			conn.transactionCommit();
	        return Response.status(Status.NO_CONTENT).build();
		} catch (final Exception exception) {
			conn.transactionRollback();
			if (exception instanceof final WebApplicationException webex)
				return webex.getResponse();
			throw exception;
		}
	}



    private void _createKursSchieneZuordnung(final Long idZwischenergebnis, final Long idSchiene, final Long idKurs) {
        DBUtilsGost.pruefeSchuleMitGOSt(conn);
        if (idSchiene == null)
            throw OperationError.CONFLICT.exception();
        // Bestimme das Blockungs-Zwischenergebnis
        final DTOGostBlockungZwischenergebnis ergebnis = conn.queryByKey(DTOGostBlockungZwischenergebnis.class, idZwischenergebnis);
        if (ergebnis == null)
            throw OperationError.NOT_FOUND.exception();
        // Bestimme die zugehörige Blockung
        final DTOGostBlockung blockung = conn.queryByKey(DTOGostBlockung.class, ergebnis.Blockung_ID);
        if (blockung == null)
            throw OperationError.NOT_FOUND.exception();
        // Bestimme Schienen-IDs der Blockung
        final DTOGostBlockungSchiene schiene = conn.queryByKey(DTOGostBlockungSchiene.class, idSchiene);
        if (schiene == null)
            throw OperationError.NOT_FOUND.exception();
        if (!Objects.equals(schiene.Blockung_ID, ergebnis.Blockung_ID)) // Fehler in der DB
            throw OperationError.CONFLICT.exception();
        // Bestimme die Kurse, welche für die Blockung angelegt wurden
        final DTOGostBlockungKurs kurs = conn.queryByKey(DTOGostBlockungKurs.class, idKurs);
        if (kurs == null)
            throw OperationError.NOT_FOUND.exception();
        if (!Objects.equals(kurs.Blockung_ID, ergebnis.Blockung_ID))
            throw OperationError.CONFLICT.exception(); // Fehler in der DB
        // Füge die neue Kurs-Schienen-Zuordnung hinzu
        conn.transactionPersist(new DTOGostBlockungZwischenergebnisKursSchiene(idZwischenergebnis, idKurs, idSchiene));
    }



    private void _deleteKursSchieneZuordnung(final Long idZwischenergebnis, final Long idSchiene, final Long idKurs) {
        DBUtilsGost.pruefeSchuleMitGOSt(conn);
        if ((idSchiene == null) || (idKurs == null))
            throw OperationError.CONFLICT.exception();
        // Entferne die Zuordnung
        final DTOGostBlockungZwischenergebnisKursSchiene dto = conn.queryByKey(DTOGostBlockungZwischenergebnisKursSchiene.class, idZwischenergebnis, idKurs, idSchiene);
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
    public Response createKursSchieneZuordnung(final Long idZwischenergebnis, final Long idSchiene, final Long idKurs) {
        try {
            conn.transactionBegin();
            this._createKursSchieneZuordnung(idZwischenergebnis, idSchiene, idKurs);
            conn.transactionCommit();
            return Response.status(Status.NO_CONTENT).build();
        } catch (final Exception exception) {
            conn.transactionRollback();
            if (exception instanceof final WebApplicationException webex)
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
    public Response updateKursSchieneZuordnung(final Long idZwischenergebnis, final Long idKurs, final Long idSchieneAlt, final Long idSchieneNeu) {
        if (idSchieneNeu == null)
            return deleteKursSchieneZuordnung(idZwischenergebnis, idSchieneAlt, idKurs);
        try {
            conn.transactionBegin();
            this._deleteKursSchieneZuordnung(idZwischenergebnis, idSchieneAlt, idKurs);
            this._createKursSchieneZuordnung(idZwischenergebnis, idSchieneNeu, idKurs);
            conn.transactionCommit();
            return Response.status(Status.NO_CONTENT).build();
        } catch (final Exception exception) {
            conn.transactionRollback();
            if (exception instanceof final WebApplicationException webex)
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
    public Response deleteKursSchieneZuordnung(final Long idZwischenergebnis, final Long idSchiene, final Long idKurs) {
        try {
            conn.transactionBegin();
            this._deleteKursSchieneZuordnung(idZwischenergebnis, idSchiene, idKurs);
            conn.transactionCommit();
            return Response.status(Status.NO_CONTENT).build();
        } catch (final Exception exception) {
            conn.transactionRollback();
            if (exception instanceof final WebApplicationException webex)
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
    private void aktiviere(final GostBlockungsergebnisManager ergebnisManager, final DTOSchuljahresabschnitte abschnitt, final GostHalbjahr halbjahr) {
        try {
            conn.transactionBegin();
	    	// Bestimme die ID des Jahrgangs
	    	final List<DTOJahrgang> jahrgangsliste = conn.queryList("SELECT e FROM DTOJahrgang e WHERE e.ASDJahrgang = ?1 AND e.Sichtbar = ?2", DTOJahrgang.class, halbjahr.jahrgang, true);
	    	if (jahrgangsliste.isEmpty())
	    		throw OperationError.NOT_FOUND.exception();
	    	if (jahrgangsliste.size() > 1)
	    		throw OperationError.CONFLICT.exception();
	    	final DTOJahrgang jahrgang = jahrgangsliste.get(0);
			// Bestimme die ID, mit welche der nächste Kurs eingefügt wird
			final DTOSchemaAutoInkremente dbKurseID = conn.queryByKey(DTOSchemaAutoInkremente.class, Schema.tab_Kurse.name());
			long idKurs = dbKurseID == null ? 1 : dbKurseID.MaxID + 1;
	    	// Durchwandere alle Kurse der Blockung und lege diese an, merke dabei auch die Zuordnung der neuen Kurs-IDs zu den Kurs-IDs der Blockung
			final HashMap<Long, Long> mapKursIDs = new HashMap<>();
			final HashMap<Long, DTOKurs> mapKursDTOs = new HashMap<>();
	    	final GostBlockungsdatenManager datenManager = ergebnisManager.getParent();
	    	for (final GostBlockungKurs kurs : datenManager.daten().kurse) {
	    		final long id = idKurs++;
	    		mapKursIDs.put(kurs.id, id);
	    		// Bestimme die Kurslehrer, sofern bereits festgelegt
	    		DTOGostBlockungKurslehrer kurslehrer = null;
	    		final List<DTOGostBlockungKurslehrer> kurslehrerListe = conn.queryNamed("DTOGostBlockungKurslehrer.blockung_kurs_id", kurs.id, DTOGostBlockungKurslehrer.class);
	    		final List<DTOKursLehrer> kursLehrerZusatzkraefte = new ArrayList<>();
	    		for (final DTOGostBlockungKurslehrer dtoKurslehrer : kurslehrerListe) {
	    			if (dtoKurslehrer.Reihenfolge == 1) {
	    				kurslehrer = dtoKurslehrer;
	    			} else {
	    				final DTOKursLehrer kl = new DTOKursLehrer(id, dtoKurslehrer.Lehrer_ID);
	    				kl.Anteil = (double) dtoKurslehrer.Wochenstunden;
	    				kursLehrerZusatzkraefte.add(kl);
	    			}
	    		}
	    		final DTOKurs dto = new DTOKurs(id, abschnitt.ID, datenManager.kursGetName(kurs.id), kurs.fach_id);
	    		dto.Jahrgang_ID = jahrgang.ID;
	    		dto.ASDJahrgang = halbjahr.jahrgang;
	    		dto.KursartAllg = GostKursart.fromID(kurs.kursart).kuerzel;
	    		dto.WochenStd = kurs.wochenstunden;
	    		dto.Lehrer_ID = kurslehrer == null ? null : kurslehrer.Lehrer_ID;
	    		final GostFach fach = datenManager.faecherManager().get(kurs.fach_id);
	    		dto.Sortierung = fach.sortierung;
	    		dto.Sichtbar = true;
	    		final int[] schienen = ergebnisManager.getOfKursSchienenNummern(kurs.id);
	    		dto.Schienen = (schienen == null) ? null : Arrays.stream(schienen).mapToObj(i -> "" + i)
	    				.collect(Collectors.joining(","));
	    		dto.Fortschreibungsart = KursFortschreibungsart.KEINE;
	    		dto.WochenstdKL = (kurslehrer == null) ? null : (double) kurslehrer.Wochenstunden;
	    		dto.SchulNr = null;
	    		dto.EpochU = false;
	    		dto.ZeugnisBez = null;
	    		dto.Jahrgaenge = null;
	    		conn.transactionPersist(dto);
	    		mapKursDTOs.put(kurs.id, dto);
	    		for (final DTOKursLehrer kl : kursLehrerZusatzkraefte)
	    			conn.transactionPersist(kl);
	    	}
	        if (!conn.transactionCommit())
	        	throw OperationError.INTERNAL_SERVER_ERROR.exception();
	    	// Durchwandere alle Schüler des Abitur-Jahrgangs und lege ggf. fehlende Lernabschnitte an
			final HashMap<Long, Long> mapLernabschnitte = new HashMap<>();
	    	for (final Schueler schueler : datenManager.daten().schueler) {
	    		final DTOSchuelerLernabschnittsdaten lernabschnitt = DBUtilsSchuelerLernabschnittsdaten
	    				.transactionGetOrCreateByPrevious(conn, schueler.id, abschnitt);
	    		mapLernabschnitte.put(schueler.id, lernabschnitt.ID);
	    	}
            conn.transactionBegin();
			// Bestimme die ID, mit welcher die nächsten Schülerleistungsdaten eingefügt werden
			final DTOSchemaAutoInkremente dbLeistungenID = conn.queryByKey(DTOSchemaAutoInkremente.class, Schema.tab_SchuelerLeistungsdaten.name());
			long idLeistungen = dbLeistungenID == null ? 1 : dbLeistungenID.MaxID + 1;
	    	// Durchwandere alle Schüler des Abitur-Jahrgangs und lege die Leistungsdaten an
	    	for (final Schueler schueler : datenManager.daten().schueler) {
	    		// Bestimme die Kurse, in welche der Schüler gesetzt wurde
	    		final Set<GostBlockungsergebnisKurs> kursMenge = ergebnisManager.getOfSchuelerKursmenge(schueler.id);
	    		for (final GostBlockungsergebnisKurs kurszuordnung : kursMenge) {
	    			final GostBlockungKurs kurs = datenManager.kursGet(kurszuordnung.id);
	    			final GostFach fach = datenManager.faecherManager().get(kurs.fach_id);
	    			final DTOKurs kursDTO = mapKursDTOs.get(kurs.id);
	    			final DTOGostSchuelerFachbelegungen fachwahl = conn.queryByKey(DTOGostSchuelerFachbelegungen.class, schueler.id, fach.id);
	    			final DTOSchuelerLeistungsdaten leistung = new DTOSchuelerLeistungsdaten(idLeistungen++, mapLernabschnitte.get(schueler.id), kurs.fach_id);
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
	    			leistung.Gewichtung = 1;
	    			leistung.NoteAbschlussBA = null;
	    			leistung.Umfang = null;
	    			conn.transactionPersist(leistung);
	    		}
	    	}
	        if (!conn.transactionCommit())
	        	throw OperationError.INTERNAL_SERVER_ERROR.exception();
	    } catch (final Exception exception) {
	        conn.transactionRollback();
	        if (exception instanceof final WebApplicationException webex)
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
    public Response aktiviere(final Long idErgebnis) {
    	try {
			final DTOEigeneSchule schule = DBUtilsGost.pruefeSchuleMitGOSt(conn);

			// Bestimme das Blockungs-Zwischenergebnis
			final DTOGostBlockungZwischenergebnis dtoErgebnis = conn.queryByKey(DTOGostBlockungZwischenergebnis.class, idErgebnis);
			if (dtoErgebnis == null)
				return OperationError.NOT_FOUND.getResponse();
			final GostBlockungsdatenManager datenManager = (new DataGostBlockungsdaten(conn)).getBlockungsdatenManagerFromDB(dtoErgebnis.Blockung_ID);
			// Bestimme die Daten des Ergebnisses
	        final GostBlockungsergebnis ergebnis = getErgebnis(dtoErgebnis, datenManager);
	        final GostBlockungsergebnisManager ergebnisManager = new GostBlockungsergebnisManager(datenManager, ergebnis);

	        // Bestimme das Schuljahr und das Halbjahr, welchem das Ergebnis zugeordnet ist
	        final GostHalbjahr halbjahr = GostHalbjahr.fromID(datenManager.daten().gostHalbjahr);
	        final int schuljahr = halbjahr.getSchuljahrFromAbiturjahr(datenManager.daten().abijahrgang);
	        // Prüfe, ob der bzw. die Schuljahresabschnitte (im Quartalsmodus) bereits angelegt wurden
	        if (schule.AnzahlAbschnitte == 4) {
	        	final DTOSchuljahresabschnitte abschnitt1 = SchulUtils.getSchuljahreabschnitt(conn, schuljahr, halbjahr.halbjahr == 1 ? 1 : 3);
	        	final DTOSchuljahresabschnitte abschnitt2 = SchulUtils.getSchuljahreabschnitt(conn, schuljahr, halbjahr.halbjahr == 1 ? 2 : 4);
	        	if (DBUtilsGost.pruefeHatOberstufenKurseInAbschnitt(conn, halbjahr, abschnitt1)
	        			|| DBUtilsGost.pruefeHatOberstufenKurseInAbschnitt(conn, halbjahr, abschnitt2))
	        		return OperationError.CONFLICT.getResponse();
	        	aktiviere(ergebnisManager, abschnitt1, halbjahr);
	        	aktiviere(ergebnisManager, abschnitt2, halbjahr);
	        } else {
	        	final DTOSchuljahresabschnitte abschnitt = SchulUtils.getSchuljahreabschnitt(conn, schuljahr, halbjahr.halbjahr);
	        	if (DBUtilsGost.pruefeHatOberstufenKurseInAbschnitt(conn, halbjahr, abschnitt))
	        		return OperationError.CONFLICT.getResponse();
	        	aktiviere(ergebnisManager, abschnitt, halbjahr);
	        }
	        // Markiere die Blockung und das Ergebnis als aktiviert
	        try {
	            conn.transactionBegin();
	        	final DTOGostBlockung bl = conn.queryByKey(DTOGostBlockung.class, dtoErgebnis.Blockung_ID);
	        	bl.IstAktiv = true;
	        	if (!conn.transactionPersist(bl))
		        	throw OperationError.INTERNAL_SERVER_ERROR.exception();
				final DTOGostBlockungZwischenergebnis erg = conn.queryByKey(DTOGostBlockungZwischenergebnis.class, dtoErgebnis.ID);
	        	erg.IstVorlage = true;
	        	if (!conn.transactionPersist(erg))
		        	throw OperationError.INTERNAL_SERVER_ERROR.exception();
		        if (!conn.transactionCommit())
		        	throw OperationError.INTERNAL_SERVER_ERROR.exception();
		    } catch (final Exception exception) {
		        conn.transactionRollback();
		        if (exception instanceof final WebApplicationException webex)
		            throw webex;
		        throw exception;
		    }
	        return Response.status(Status.NO_CONTENT).build();
	    } catch (final Exception exception) {
	        if (exception instanceof final WebApplicationException webex)
	            return webex.getResponse();
	        throw exception;
	    }
    }

}
