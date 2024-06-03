package de.svws_nrw.data.gost;

import de.svws_nrw.core.adt.Pair;
import de.svws_nrw.core.data.gost.GostBlockungKurs;
import de.svws_nrw.core.data.gost.GostBlockungRegel;
import de.svws_nrw.core.data.gost.GostBlockungsergebnis;
import de.svws_nrw.core.data.gost.GostBlockungsergebnisKurs;
import de.svws_nrw.core.data.gost.GostBlockungsergebnisKursSchienenZuordnung;
import de.svws_nrw.core.data.gost.GostBlockungsergebnisKursSchienenZuordnungUpdate;
import de.svws_nrw.core.data.gost.GostBlockungsergebnisKursSchuelerZuordnung;
import de.svws_nrw.core.data.gost.GostBlockungsergebnisKursSchuelerZuordnungUpdate;
import de.svws_nrw.core.data.gost.GostBlockungsergebnisSchiene;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.data.schueler.Schueler;
import de.svws_nrw.core.types.KursFortschreibungsart;
import de.svws_nrw.core.types.Note;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.utils.DTOUtils;
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
import de.svws_nrw.db.dto.current.schema.DTOSchemaAutoInkremente;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKurs;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKursLehrer;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLeistungsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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
	 * @param conn   die Datenbankverbindung
	 * @param datenManager   der Blockungsdaten-Manager
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static void getErgebnisListe(final @NotNull DBEntityManager conn, @NotNull final GostBlockungsdatenManager datenManager) throws ApiOperationException {
	    // Bestimme die Liste der Ergebnisse aus der Datenbank
        final List<DTOGostBlockungZwischenergebnis> ergebnisse = conn.queryList(DTOGostBlockungZwischenergebnis.QUERY_BY_BLOCKUNG_ID,
        		DTOGostBlockungZwischenergebnis.class, datenManager.getID());
        if (ergebnisse == null)
            throw new ApiOperationException(Status.NOT_FOUND);
        final List<Long> ergebnisIDs = ergebnisse.stream().map(e -> e.ID).toList();
        if (ergebnisIDs.isEmpty()) // Es muss immer mindestens ein aktuelles Ergebnis vorliegen
            throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR);

        // Bestimme die Kurs-Schienen-Zuordnungen für alle Zwischenergebnisse
        final Map<Long, List<DTOGostBlockungZwischenergebnisKursSchiene>> mapKursSchienen =
            conn.queryList(DTOGostBlockungZwischenergebnisKursSchiene.QUERY_LIST_BY_ZWISCHENERGEBNIS_ID,
                    DTOGostBlockungZwischenergebnisKursSchiene.class, ergebnisIDs)
                .stream().collect(Collectors.groupingBy(e -> e.Zwischenergebnis_ID, Collectors.toList()));

        // Bestimme die Kurs-Schüler-Zuordnungen für alle Zwischenergebnisse
        final Map<Long, List<DTOGostBlockungZwischenergebnisKursSchueler>> mapKursSchueler =
            conn.queryList(DTOGostBlockungZwischenergebnisKursSchueler.QUERY_LIST_BY_ZWISCHENERGEBNIS_ID,
                    DTOGostBlockungZwischenergebnisKursSchueler.class, ergebnisIDs)
                .stream().collect(Collectors.groupingBy(e -> e.Zwischenergebnis_ID, Collectors.toList()));

	    // Durchwandere alle Ergebnisse
        for (final DTOGostBlockungZwischenergebnis erg : ergebnisse) {
            // Erstelle zunächst das Core-DTO für das Ergebnis mit Bewertung
            final var manager = new GostBlockungsergebnisManager(datenManager, erg.ID);
            final var listSchienenKurse = mapKursSchienen.getOrDefault(erg.ID, Collections.emptyList());
            final var listKursSchueler = mapKursSchueler.getOrDefault(erg.ID, Collections.emptyList());

            // Kurs-Schienen-Zuordnungen. Verwende Update-Objekte, da nur EINE Regelvalidierung am Ende erfolgt.
            final @NotNull Set<@NotNull GostBlockungsergebnisKursSchienenZuordnung> kursSchienenZuordnungen = new HashSet<>();
            for (final var ks : listSchienenKurse)
            	kursSchienenZuordnungen.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(ks.Blockung_Kurs_ID, ks.Schienen_ID));
    		final @NotNull GostBlockungsergebnisKursSchienenZuordnungUpdate uKursSchienen = manager.kursSchienenUpdate_01a_FUEGE_KURS_SCHIENEN_PAARE_HINZU(kursSchienenZuordnungen);
    		manager.kursSchienenUpdateExecute(uKursSchienen);

            // Kurs-Schüler-Zuordnungen. Verwende Update-Objekte, da nur EINE Regelvalidierung am Ende erfolgt.
        	final @NotNull Set<@NotNull GostBlockungsergebnisKursSchuelerZuordnung> kursSchuelerZuordnungen = new HashSet<>();
            for (final var ks : listKursSchueler) // Fehlerhafte Zuordnungen führen im Manager nicht mehr zu Exceptions!
            	kursSchuelerZuordnungen.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(ks.Blockung_Kurs_ID, ks.Schueler_ID));
            final @NotNull GostBlockungsergebnisKursSchuelerZuordnungUpdate uKursSchueler = manager.kursSchuelerUpdate_03a_FUEGE_KURS_SCHUELER_PAARE_HINZU(kursSchuelerZuordnungen);
            manager.kursSchuelerUpdateExecute(uKursSchueler);

            // Erzeuge das Ergebnis.
            final GostBlockungsergebnis ergebnis = manager.getErgebnisInklusiveUngueltigerWahlen();
            ergebnis.istAktiv = erg.IstAktiv != null && erg.IstAktiv;
            datenManager.daten().ergebnisse.add(ergebnis);
        }
	}


	/**
	 * Liest die Daten für das Blockungsergebnis aus der Datenbank ein und erstellt den dazugeörigen Ergebnis-Manager
	 *
	 * @param conn   die Datenbankverbindung
	 * @param ergebnis        das Datenbank-DTO des Blockungsergebnisses
	 * @param datenManager    der Blockungsdaten-Manager
	 *
	 * @return der Manager für das Blockungsergebnis
	 *
	 * @throws ApiOperationException   falls das Ergebnis nicht in der Datenbank existiert.
	 */
	public static GostBlockungsergebnisManager getErgebnismanager(final @NotNull DBEntityManager conn, @NotNull final DTOGostBlockungZwischenergebnis ergebnis,
	        @NotNull final GostBlockungsdatenManager datenManager) throws ApiOperationException {
        final GostBlockungsergebnisManager manager = new GostBlockungsergebnisManager(datenManager, ergebnis.ID);

        // Bestimme alle Kurs-Schienen-Zuordnungen. Verwende Update-Objekte, da nur EINE Regelvalidierung am Ende erfolgt.
        final List<DTOGostBlockungZwischenergebnisKursSchiene> listSchienenKurse = conn
                .queryList(DTOGostBlockungZwischenergebnisKursSchiene.QUERY_BY_ZWISCHENERGEBNIS_ID, DTOGostBlockungZwischenergebnisKursSchiene.class, ergebnis.ID);

        final @NotNull Set<@NotNull GostBlockungsergebnisKursSchienenZuordnung> kursSchienenZuordnungen = new HashSet<>();
        for (final DTOGostBlockungZwischenergebnisKursSchiene ks : listSchienenKurse)
        	kursSchienenZuordnungen.add(DTOUtils.newGostBlockungsergebnisKursSchienenZuordnung(ks.Blockung_Kurs_ID, ks.Schienen_ID));
		final @NotNull GostBlockungsergebnisKursSchienenZuordnungUpdate uKursSchienen = manager.kursSchienenUpdate_01a_FUEGE_KURS_SCHIENEN_PAARE_HINZU(kursSchienenZuordnungen);
		manager.kursSchienenUpdateExecute(uKursSchienen);

        // Bestimme alle Kurs-Schüler-Zuordnungen. Verwende Update-Objekte, da nur EINE Regelvalidierung am Ende erfolgt.
    	final List<DTOGostBlockungZwischenergebnisKursSchueler> listKursSchueler = conn
    			.queryList(DTOGostBlockungZwischenergebnisKursSchueler.QUERY_BY_ZWISCHENERGEBNIS_ID, DTOGostBlockungZwischenergebnisKursSchueler.class, ergebnis.ID);

        final @NotNull Set<@NotNull GostBlockungsergebnisKursSchuelerZuordnung> kursSchuelerZuordnungen = new HashSet<>();
        for (final DTOGostBlockungZwischenergebnisKursSchueler ks : listKursSchueler) // Fehlerhafte Zuordnungen stürzen im Manager nicht mehr ab!
           	kursSchuelerZuordnungen.add(DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(ks.Blockung_Kurs_ID, ks.Schueler_ID));
        final @NotNull GostBlockungsergebnisKursSchuelerZuordnungUpdate uKursSchueler = manager.kursSchuelerUpdate_03a_FUEGE_KURS_SCHUELER_PAARE_HINZU(kursSchuelerZuordnungen);
        manager.kursSchuelerUpdateExecute(uKursSchueler);

        // Erzeuge das Ergebnis.
        return manager;
	}


	/**
	 * Liest die Daten für das Blockungsergebnis aus der Datenbank ein und erstellt das zugehörige Core-DTO
	 *
	 * @param conn   die Datenbankverbindung
	 * @param ergebnis        das Datenbank-DTO des Blockungsergebnisses
	 * @param datenManager    der Blockungsdaten-Manager
	 *
	 * @return das Core-DTO für das Blockungsergebnis
	 *
	 * @throws ApiOperationException   falls das Ergebnis nicht in der Datenbank existiert.
	 */
	public static GostBlockungsergebnis getErgebnis(final @NotNull DBEntityManager conn, @NotNull final DTOGostBlockungZwischenergebnis ergebnis,
	        @NotNull final GostBlockungsdatenManager datenManager) throws ApiOperationException {
        // Erzeuge das Ergebnis.
        final GostBlockungsergebnisManager manager = getErgebnismanager(conn, ergebnis, datenManager);
        final GostBlockungsergebnis daten = manager.getErgebnisInklusiveUngueltigerWahlen();
        daten.istAktiv = ergebnis.IstAktiv != null && ergebnis.IstAktiv;
        return daten;
	}


	/**
	 * Liest die Daten für das Blockungsergebnis aus der Datenbank ein und erstellt das
	 * zugehörige Core-DTO
	 *
	 * @param conn   die Datenbankverbindung
	 * @param id	 die ID des Blockungsergebnisses aus der Datenbank
	 *
	 * @return das Core-DTO für das Blockungsergebnis
	 *
	 * @throws ApiOperationException   falls das Ergebnis nicht in der Datenbank existiert.
	 */
	public static GostBlockungsergebnis getErgebnisFromID(final @NotNull DBEntityManager conn, final long id) throws ApiOperationException {
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
		final DTOGostBlockungZwischenergebnis ergebnis = conn.queryByKey(DTOGostBlockungZwischenergebnis.class, id);
		if (ergebnis == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Ungültige Blockungsergebnis-ID übergeben.");
		final GostBlockungsdatenManager datenManager = DataGostBlockungsdaten.getBlockungsdatenManagerFromDB(conn, ergebnis.Blockung_ID);
		return getErgebnis(conn, ergebnis, datenManager);
	}


	/**
	 * Erzeugt den Blockungsergebnis-Manager für die übergeben Ergebnis-ID aus der Datenbank.
	 *
	 * @param conn   die Datenbankverbindung
	 * @param id	 die ID des Blockungsergebnisses aus der Datenbank
	 *
	 * @return der Blockungsergebnis-Manager
	 *
	 * @throws ApiOperationException   falls das Ergebnis nicht in der Datenbank existiert.
	 */
	public static GostBlockungsergebnisManager getErgebnismanagerFromID(final @NotNull DBEntityManager conn, final long id) throws ApiOperationException {
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
		final DTOGostBlockungZwischenergebnis ergebnis = conn.queryByKey(DTOGostBlockungZwischenergebnis.class, id);
		if (ergebnis == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Ungültige Blockungsergebnis-ID übergeben.");
		final GostBlockungsdatenManager datenManager = DataGostBlockungsdaten.getBlockungsdatenManagerFromDB(conn, ergebnis.Blockung_ID);
		return getErgebnismanager(conn, ergebnis, datenManager);
	}


	@Override
	public Response get(final Long id) throws ApiOperationException {
		final GostBlockungsergebnis daten = getErgebnisFromID(conn, id);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}



	@Override
	public Response patch(final Long id, final InputStream is) throws ApiOperationException {
		final Map<String, Object> map = JSONMapper.toMap(is);
		if (map.size() <= 0)
			return Response.status(Status.OK).build();
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
		// Bestimme die Blockung
		DTOGostBlockungZwischenergebnis ergebnis = conn.queryByKey(DTOGostBlockungZwischenergebnis.class, id);
		if (ergebnis == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		for (final Entry<String, Object> entry : map.entrySet()) {
			final String key = entry.getKey();
			final Object value = entry.getValue();
			switch (key) {
				case "id" -> {
					final Long patch_id = JSONMapper.convertToLong(value, true);
					if ((patch_id == null) || (patch_id.longValue() != id.longValue()))
						throw new ApiOperationException(Status.BAD_REQUEST);
				}
				case "istAktiv" -> {
					final boolean result = JSONMapper.convertToBoolean(value, false);
					if (result)
						ergebnis = markiereErgebnisAktiv(conn, ergebnis.ID, true);
					else
						ergebnis.IstAktiv = false;
				}
				default -> throw new ApiOperationException(Status.BAD_REQUEST);
			}
		}
		conn.transactionPersist(ergebnis);
		return Response.status(Status.OK).build();
	}


	/**
	 * Entfernt das Zwischenergebnis mit der angegebenen ID aus der Datenbank.
	 *
	 * @param id   die ID des zu löschenden Blockungsergebnis
	 *
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response delete(final Long id) throws ApiOperationException {
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
		// Bestimme das Zwischenergebnis
		final DTOGostBlockungZwischenergebnis erg = conn.queryByKey(DTOGostBlockungZwischenergebnis.class, id);
		if (erg == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		// Entferne das Ergebnis
		conn.transactionRemove(erg);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(id).build();
	}


	/**
	 * Entfernt die Zwischenergebnis mit den angegebenen IDs aus der Datenbank.
	 *
	 * @param ids   die IDs der zu löschenden Blockungsergebnisse
	 *
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response deleteMultiple(final List<Long> ids) throws ApiOperationException {
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
		// Bestimme das Zwischenergebnis
		final List<DTOGostBlockungZwischenergebnis> ergebnisse = conn.queryByKeyList(DTOGostBlockungZwischenergebnis.class, ids);
		if ((ergebnisse == null) || (ergebnisse.size() != ids.size()))
			throw new ApiOperationException(Status.NOT_FOUND, "Mindestens ein Ergebnis wurde nicht gefunden.");
		final long idBlockung = ergebnisse.get(0).Blockung_ID;
		for (final DTOGostBlockungZwischenergebnis ergebnis : ergebnisse)
			if (idBlockung != ergebnis.Blockung_ID)
				throw new ApiOperationException(Status.BAD_REQUEST, "Die Ergebnisse gehören zu mehreren Blockungen");
		// Entferne die Ergebnisse
		conn.transactionRemoveAll(ergebnisse);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(ids).build();
	}


	/**
	 * Markiert das Blockungsergebnis als aktiv und alle anderen Ergebnisse der Blockung als inaktiv, wenn der
	 * Wert auf true gesetzt ist und ansonsten nur das angegebene Blockungsergebnis auf inaktiv
	 *
	 * @param conn         die Datenbankverbindung
	 * @param idErgebnis   die ID des Blockungsergebnisses
	 * @param aktiv        gibt an,
	 *
	 * @return das DTO zur Blockung, falls damit weitergearbeitet werden soll
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static DTOGostBlockungZwischenergebnis markiereErgebnisAktiv(final DBEntityManager conn, final long idErgebnis, final boolean aktiv) throws ApiOperationException {
		conn.transactionFlush();
		DTOGostBlockungZwischenergebnis ergebnis = conn.queryByKey(DTOGostBlockungZwischenergebnis.class, idErgebnis);
		if (aktiv) {
			conn.transactionNativeUpdate("UPDATE %s SET %s = 0 WHERE %s = %d AND %s <> %d".formatted(
				Schema.tab_Gost_Blockung_Zwischenergebnisse.name(), Schema.tab_Gost_Blockung_Zwischenergebnisse.col_IstAktiv.name(),
				Schema.tab_Gost_Blockung_Zwischenergebnisse.col_Blockung_ID.name(), ergebnis.Blockung_ID,
				Schema.tab_Gost_Blockung_Zwischenergebnisse.col_ID.name(), idErgebnis));
			conn.transactionFlush();
			ergebnis = conn.queryByKey(DTOGostBlockungZwischenergebnis.class, idErgebnis);
		}
		ergebnis.IstAktiv = aktiv;
    	if (!conn.transactionPersist(ergebnis))
        	throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR);
		conn.transactionFlush();
		return ergebnis;
	}


	private void _createKursSchuelerZuordnung(final Long idZwischenergebnis, final Long idSchueler, final Long idKurs) throws ApiOperationException {
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
		if (idSchueler == null)
			throw new ApiOperationException(Status.CONFLICT);
		// Bestimme das Blockungs-Zwischenergebnis
		final DTOGostBlockungZwischenergebnis ergebnis = conn.queryByKey(DTOGostBlockungZwischenergebnis.class, idZwischenergebnis);
		if (ergebnis == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		// Bestimme die zugehörige Blockung
		final DTOGostBlockung blockung = conn.queryByKey(DTOGostBlockung.class, ergebnis.Blockung_ID);
		if (blockung == null)
			throw new ApiOperationException(Status.NOT_FOUND);
    	// Bestimme alle Schüler-IDs für den Abiturjahrgang der Blockung
		final List<DTOSchueler> schuelerAbijahrgang = DBUtilsGostLaufbahn.getSchuelerOfAbiturjahrgang(conn, blockung.Abi_Jahrgang);
		if ((schuelerAbijahrgang == null) || (schuelerAbijahrgang.isEmpty()))
			throw new ApiOperationException(Status.NOT_FOUND);
		final Set<Long> schuelerIDs = schuelerAbijahrgang.stream().map(s -> s.ID).collect(Collectors.toSet());
		if (!schuelerIDs.contains(idSchueler))
			throw new ApiOperationException(Status.CONFLICT);
		// Bestimme die Kurse, welche für die Blockung angelegt wurden
		final DTOGostBlockungKurs kurs = conn.queryByKey(DTOGostBlockungKurs.class, idKurs);
		if (kurs == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		if (!Objects.equals(kurs.Blockung_ID, ergebnis.Blockung_ID))
			throw new ApiOperationException(Status.CONFLICT);

		// Füge die neue Kurszuordnung hinzu
		conn.transactionPersist(new DTOGostBlockungZwischenergebnisKursSchueler(idZwischenergebnis, idKurs, idSchueler));
		conn.transactionFlush();
	}



	private void _deleteKursSchuelerZuordnung(final Long idZwischenergebnis, final Long idSchueler, final Long idKurs) throws ApiOperationException {
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
		if ((idSchueler == null) || (idKurs == null))
			throw new ApiOperationException(Status.CONFLICT);
		// Entferne die Zuordnung
		final DTOGostBlockungZwischenergebnisKursSchueler dto = conn.queryByKey(DTOGostBlockungZwischenergebnisKursSchueler.class, idZwischenergebnis, idKurs, idSchueler);
		if (dto == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		conn.transactionRemove(dto);
		conn.transactionFlush();
	}


	/**
	 * Erstellt eine Kurs-Schüler-Zuordnung in der Datenbank.
	 *
	 * @param idZwischenergebnis   die ID der Zwischenergebnis
	 * @param idSchueler           die ID des Schülers
	 * @param idKurs               die ID des neuen Kurses
	 *
	 * @return die HTTP-Response, welchen den Erfolg der Update-Operation angibt.
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response createKursSchuelerZuordnung(final Long idZwischenergebnis, final Long idSchueler, final Long idKurs) throws ApiOperationException {
		this._createKursSchuelerZuordnung(idZwischenergebnis, idSchueler, idKurs);
        return Response.status(Status.NO_CONTENT).build();
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
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response updateKursSchuelerZuordnung(final Long idZwischenergebnis, final Long idSchueler, final Long idKursAlt, final Long idKursNeu) throws ApiOperationException {
		if (idKursNeu == null)
			return deleteKursSchuelerZuordnung(idZwischenergebnis, idKursAlt, idSchueler);
		// TODO prüfe, ob die Kursarten übereinstimmen...
		this._deleteKursSchuelerZuordnung(idZwischenergebnis, idSchueler, idKursAlt);
		this._createKursSchuelerZuordnung(idZwischenergebnis, idSchueler, idKursNeu);
        return Response.status(Status.NO_CONTENT).build();
	}


	/**
	 * Entfernt alle zum Entfernen angegebenen Kurs-Schüler-Zuordnungen und fügt anschließend alle
	 * zum Hinzufügen angegebenen Kurs-Schüler-Zuordnungen hinzu.
	 *
	 * @param idZwischenergebnis   die ID des Blockungsergebnisses, bei dem die Zuordnungen vorgenommen werden sollen
	 * @param update               die zu entfernenden Kurs-Schüler-Zuordnungen und die hinzuzufügenden Kurs-Schüler-Zuordnungen
	 *
	 * @return die HTTP-Response, welchen den Erfolg der Update-Operation angibt.
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
    public Response updateKursSchuelerZuordnungen(final Long idZwischenergebnis, final @NotNull GostBlockungsergebnisKursSchuelerZuordnungUpdate update) throws ApiOperationException {
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
		if (update.listEntfernen.isEmpty() && update.listHinzuzufuegen.isEmpty())
			return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(new ArrayList<>()).build();
		// Bestimme das Blockungs-Zwischenergebnis
		final DTOGostBlockungZwischenergebnis ergebnis = conn.queryByKey(DTOGostBlockungZwischenergebnis.class, idZwischenergebnis);
		if (ergebnis == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Das Blockungsergebnis mit der ID %d wurde nicht gefunden.".formatted(idZwischenergebnis));
		// Bestimme die zugehörige Blockung
		final DTOGostBlockung blockung = conn.queryByKey(DTOGostBlockung.class, ergebnis.Blockung_ID);
		if (blockung == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Die Blockung mit der ID %d wurde nicht gefunden.".formatted(ergebnis.Blockung_ID));
    	// Bestimme alle Schüler-IDs für den Abiturjahrgang der Blockung
		final List<DTOSchueler> schuelerAbijahrgang = DBUtilsGostLaufbahn.getSchuelerOfAbiturjahrgang(conn, blockung.Abi_Jahrgang);
		if ((schuelerAbijahrgang == null) || (schuelerAbijahrgang.isEmpty()))
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurden keine Schüler in dem Abiturjahrgang gefunden.");
		final Set<Long> schuelerIDs = schuelerAbijahrgang.stream().map(s -> s.ID).collect(Collectors.toSet());
		// Bestimme alle Kurse der Blockung
		final List<DTOGostBlockungKurs> kurse = conn.queryList(DTOGostBlockungKurs.QUERY_BY_BLOCKUNG_ID, DTOGostBlockungKurs.class, blockung.ID);
		if (kurse.isEmpty())
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurden keine Kurse in der Blockung gefunden.");
		final Set<Long> kursIDs = kurse.stream().map(k -> k.ID).collect(Collectors.toSet());

		// Entferne die Kurs-Schüler-Zuordnungen
		if (!update.listEntfernen.isEmpty()) {
			final @NotNull HashSet<@NotNull Pair<@NotNull Long, @NotNull Long>> setEntfernt = new HashSet<>();
			for (final @NotNull GostBlockungsergebnisKursSchuelerZuordnung zuordnung : update.listEntfernen) {
				// Prüfe, ob die zu entfernende Zuordnung doppelt vorkommt
				final @NotNull Pair<@NotNull Long, @NotNull Long> eintrag = new Pair<>(zuordnung.idKurs, zuordnung.idSchueler);
				if (setEntfernt.contains(eintrag))
					throw new ApiOperationException(Status.CONFLICT, "Die Zuordnung des Schüler mit der ID %d zu dem Kurs mit der ID %d sollte doppelt entfernt werden.".formatted(zuordnung.idSchueler, zuordnung.idKurs));
				setEntfernt.add(eintrag);
				if (!schuelerIDs.contains(zuordnung.idSchueler))
					throw new ApiOperationException(Status.NOT_FOUND, "Der Schüler mit der ID %d wurde nicht gefunden.".formatted(zuordnung.idSchueler));
				if (!kursIDs.contains(zuordnung.idKurs))
					throw new ApiOperationException(Status.NOT_FOUND, "Der Kurs mit der ID %d wurde nicht gefunden.".formatted(zuordnung.idKurs));
				// Prüfe, ob die Zuordnung überhaupt existiert und entfernt werden kann
				final DTOGostBlockungZwischenergebnisKursSchueler dto = conn.queryByKey(DTOGostBlockungZwischenergebnisKursSchueler.class, idZwischenergebnis, zuordnung.idKurs, zuordnung.idSchueler);
				if (dto == null)
					throw new ApiOperationException(Status.NOT_FOUND);
				conn.transactionRemove(dto);
			}
			conn.transactionFlush();
		}

		// Füge die Kurs-Schüler-Zuordnungen hinzu
		if (!update.listHinzuzufuegen.isEmpty()) {
			final @NotNull HashSet<@NotNull Pair<@NotNull Long, @NotNull Long>> setHinzugefuegt = new HashSet<>();
			for (final @NotNull GostBlockungsergebnisKursSchuelerZuordnung zuordnung : update.listHinzuzufuegen) {
				// Prüfe, ob die zu entfernende Zuordnung doppelt vorkommt
				final @NotNull Pair<@NotNull Long, @NotNull Long> eintrag = new Pair<>(zuordnung.idKurs, zuordnung.idSchueler);
				if (setHinzugefuegt.contains(eintrag))
					throw new ApiOperationException(Status.CONFLICT, "Die Zuordnung des Schüler mit der ID %d zu dem Kurs mit der ID %d sollte doppelt hinzugefügt werden.".formatted(zuordnung.idSchueler, zuordnung.idKurs));
				setHinzugefuegt.add(eintrag);
				if (!schuelerIDs.contains(zuordnung.idSchueler))
					throw new ApiOperationException(Status.NOT_FOUND, "Der Schüler mit der ID %d wurde nicht gefunden.".formatted(zuordnung.idSchueler));
				if (!kursIDs.contains(zuordnung.idKurs))
					throw new ApiOperationException(Status.NOT_FOUND, "Der Kurs mit der ID %d wurde nicht gefunden.".formatted(zuordnung.idKurs));
				// Füge die neue Kurszuordnung hinzu
				conn.transactionPersist(new DTOGostBlockungZwischenergebnisKursSchueler(idZwischenergebnis, zuordnung.idKurs, zuordnung.idSchueler));
			}
			conn.transactionFlush();
		}
		// Passe ggf die Regeln an
		final List<GostBlockungRegel> daten = (update.regelUpdates.listEntfernen.isEmpty() && update.regelUpdates.listHinzuzufuegen.isEmpty())
				? new ArrayList<>()
				: DataGostBlockungRegel.updateBlockungsregeln(conn, blockung, update.regelUpdates);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
    }



	/**
	 * Entfernt die die Zuordnung des Schüler zu dem Kurs bei einem Zwischenergebnis.
	 *
	 * @param idZwischenergebnis   die ID der Zwischenergebnis
	 * @param idSchueler           die ID des Schülers
	 * @param idKurs               die ID des Kurses
	 *
	 * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response deleteKursSchuelerZuordnung(final Long idZwischenergebnis, final Long idSchueler, final Long idKurs) throws ApiOperationException {
		this._deleteKursSchuelerZuordnung(idZwischenergebnis, idSchueler, idKurs);
        return Response.status(Status.NO_CONTENT).build();
	}


    /**
     * Entfernt die die Zuordnungen von Schülern zu Kursen bei einem Zwischenergebnis.
     *
     * @param idZwischenergebnis   die ID der Zwischenergebnis
     * @param zuordnungen          die Kurs-Schüler-Zuordnungen
     *
     * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
	 *
	 * @throws ApiOperationException   im Fehlerfall
     */
    public Response deleteKursSchuelerZuordnungen(final Long idZwischenergebnis, final @NotNull List<@NotNull GostBlockungsergebnisKursSchuelerZuordnung> zuordnungen) throws ApiOperationException {
    	for (final GostBlockungsergebnisKursSchuelerZuordnung zuordnung : zuordnungen)
    		this._deleteKursSchuelerZuordnung(idZwischenergebnis, zuordnung.idSchueler, zuordnung.idKurs);
        return Response.status(Status.NO_CONTENT).build();
    }


    private void _createKursSchieneZuordnung(final Long idZwischenergebnis, final Long idSchiene, final Long idKurs) throws ApiOperationException {
        DBUtilsGost.pruefeSchuleMitGOSt(conn);
        if (idSchiene == null)
            throw new ApiOperationException(Status.CONFLICT);
        // Bestimme das Blockungs-Zwischenergebnis
        final DTOGostBlockungZwischenergebnis ergebnis = conn.queryByKey(DTOGostBlockungZwischenergebnis.class, idZwischenergebnis);
        if (ergebnis == null)
            throw new ApiOperationException(Status.NOT_FOUND);
        // Bestimme die zugehörige Blockung
        final DTOGostBlockung blockung = conn.queryByKey(DTOGostBlockung.class, ergebnis.Blockung_ID);
        if (blockung == null)
            throw new ApiOperationException(Status.NOT_FOUND);
        // Bestimme Schienen-IDs der Blockung
        final DTOGostBlockungSchiene schiene = conn.queryByKey(DTOGostBlockungSchiene.class, idSchiene);
        if (schiene == null)
            throw new ApiOperationException(Status.NOT_FOUND);
        if (!Objects.equals(schiene.Blockung_ID, ergebnis.Blockung_ID)) // Fehler in der DB
            throw new ApiOperationException(Status.CONFLICT);
        // Bestimme die Kurse, welche für die Blockung angelegt wurden
        final DTOGostBlockungKurs kurs = conn.queryByKey(DTOGostBlockungKurs.class, idKurs);
        if (kurs == null)
            throw new ApiOperationException(Status.NOT_FOUND);
        if (!Objects.equals(kurs.Blockung_ID, ergebnis.Blockung_ID))
            throw new ApiOperationException(Status.CONFLICT); // Fehler in der DB
        // Füge die neue Kurs-Schienen-Zuordnung hinzu
        conn.transactionPersist(new DTOGostBlockungZwischenergebnisKursSchiene(idZwischenergebnis, idKurs, idSchiene));
        conn.transactionFlush();
    }



    private void _deleteKursSchieneZuordnung(final Long idZwischenergebnis, final Long idSchiene, final Long idKurs) throws ApiOperationException {
        DBUtilsGost.pruefeSchuleMitGOSt(conn);
        if ((idSchiene == null) || (idKurs == null))
            throw new ApiOperationException(Status.CONFLICT);
        // Entferne die Zuordnung
        final DTOGostBlockungZwischenergebnisKursSchiene dto = conn.queryByKey(DTOGostBlockungZwischenergebnisKursSchiene.class, idZwischenergebnis, idKurs, idSchiene);
        if (dto == null)
            throw new ApiOperationException(Status.NOT_FOUND);
        conn.transactionRemove(dto);
        conn.transactionFlush();
    }


    /**
     * Erstellt eine Kurs-Schienen-Zuordnung in der Datenbank.
     *
     * @param idZwischenergebnis   die ID der Zwischenergebnis
     * @param idSchiene            die ID der Schiene
     * @param idKurs               die ID des neuen Kurses
     *
     * @return die HTTP-Response, welchen den Erfolg der Update-Operation angibt.
	 *
	 * @throws ApiOperationException   im Fehlerfall
     */
    public Response createKursSchieneZuordnung(final Long idZwischenergebnis, final Long idSchiene, final Long idKurs) throws ApiOperationException {
        this._createKursSchieneZuordnung(idZwischenergebnis, idSchiene, idKurs);
        return Response.status(Status.NO_CONTENT).build();
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
	 *
	 * @throws ApiOperationException   im Fehlerfall
     */
    public Response updateKursSchieneZuordnung(final Long idZwischenergebnis, final Long idKurs, final Long idSchieneAlt, final Long idSchieneNeu) throws ApiOperationException {
        if (idSchieneNeu == null)
            return deleteKursSchieneZuordnung(idZwischenergebnis, idSchieneAlt, idKurs);
        this._deleteKursSchieneZuordnung(idZwischenergebnis, idSchieneAlt, idKurs);
        this._createKursSchieneZuordnung(idZwischenergebnis, idSchieneNeu, idKurs);
        return Response.status(Status.NO_CONTENT).build();
    }


    /**
     * Entfernt die die Zuordnung des Kurses zu der Schiene bei einem Zwischenergebnis.
     *
     * @param idZwischenergebnis   die ID der Zwischenergebnis
     * @param idSchiene            die ID der Schiene
     * @param idKurs               die ID des Kurses
     *
     * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
	 *
	 * @throws ApiOperationException   im Fehlerfall
     */
    public Response deleteKursSchieneZuordnung(final Long idZwischenergebnis, final Long idSchiene, final Long idKurs) throws ApiOperationException {
        this._deleteKursSchieneZuordnung(idZwischenergebnis, idSchiene, idKurs);
        return Response.status(Status.NO_CONTENT).build();
    }


    /**
     * Entfernt die die Zuordnungen von Kursen zu Schienen bei einem Zwischenergebnis.
     *
     * @param idZwischenergebnis   die ID der Zwischenergebnis
     * @param zuordnungen          die Kurs-Schienen-Zuordnungen
     *
     * @return die HTTP-Response, welchen den Erfolg der Lösch-Operation angibt.
	 *
	 * @throws ApiOperationException   im Fehlerfall
     */
    public Response deleteKursSchieneZuordnungen(final Long idZwischenergebnis, final @NotNull List<@NotNull GostBlockungsergebnisKursSchienenZuordnung> zuordnungen) throws ApiOperationException {
    	for (final GostBlockungsergebnisKursSchienenZuordnung zuordnung : zuordnungen)
    		this._deleteKursSchieneZuordnung(idZwischenergebnis, zuordnung.idSchiene, zuordnung.idKurs);
        return Response.status(Status.NO_CONTENT).build();
    }


    private void kursHinzufuegen(final GostBlockungsergebnisManager ergebnisManager, final DTOSchuljahresabschnitte abschnitt, final GostHalbjahr halbjahr,
    		final DTOJahrgang jahrgang, final long id, final GostBlockungKurs kurs, final HashMap<Long, Long> mapKursIDs, final HashMap<Long, DTOKurs> mapKursDTOs) {
    	final GostBlockungsdatenManager datenManager = ergebnisManager.getParent();
		mapKursIDs.put(kurs.id, id);
		// Bestimme die Kurslehrer, sofern bereits festgelegt
		DTOGostBlockungKurslehrer kurslehrer = null;
		final List<DTOGostBlockungKurslehrer> kurslehrerListe = conn.queryList(DTOGostBlockungKurslehrer.QUERY_BY_BLOCKUNG_KURS_ID,
			DTOGostBlockungKurslehrer.class, kurs.id);
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
		dto.Schienen = (schienen == null) ? null : Arrays.stream(schienen).mapToObj(i -> "" + i).collect(Collectors.joining(","));
		dto.Fortschreibungsart = KursFortschreibungsart.KEINE;
		dto.WochenstdKL = (kurslehrer == null) ? null : (double) kurslehrer.Wochenstunden;
		dto.SchulNr = null;
		dto.EpochU = false;
		dto.ZeugnisBez = null;
		dto.Jahrgaenge = null;
		conn.transactionPersist(dto);
    	conn.transactionFlush();
		mapKursDTOs.put(kurs.id, dto);
		for (final DTOKursLehrer kl : kursLehrerZusatzkraefte) {
			conn.transactionPersist(kl);
        	conn.transactionFlush();
		}
    }


    private void kursAnpassen(final DTOKurs dto, final GostBlockungsergebnisManager ergebnisManager,
    		final GostBlockungKurs kurs, final HashMap<Long, Long> mapKursIDs, final HashMap<Long, DTOKurs> mapKursDTOs) {
		mapKursIDs.put(kurs.id, dto.ID);
		// Bestimme die Kurslehrer, sofern bereits festgelegt
		DTOGostBlockungKurslehrer kurslehrer = null;
		final List<DTOGostBlockungKurslehrer> kurslehrerListe = conn.queryList(DTOGostBlockungKurslehrer.QUERY_BY_BLOCKUNG_KURS_ID,
				DTOGostBlockungKurslehrer.class, kurs.id);
		final List<DTOKursLehrer> kursLehrerZusatzkraefte = new ArrayList<>();
		for (final DTOGostBlockungKurslehrer dtoKurslehrer : kurslehrerListe) {
			if (dtoKurslehrer.Reihenfolge == 1) {
				kurslehrer = dtoKurslehrer;
			} else {
				final DTOKursLehrer kl = new DTOKursLehrer(dto.ID, dtoKurslehrer.Lehrer_ID);
				kl.Anteil = (double) dtoKurslehrer.Wochenstunden;
				kursLehrerZusatzkraefte.add(kl);
			}
		}
		dto.WochenStd = kurs.wochenstunden;
		final int[] schienen = ergebnisManager.getOfKursSchienenNummern(kurs.id);
		dto.Schienen = (schienen == null) ? null : Arrays.stream(schienen).mapToObj(i -> "" + i).collect(Collectors.joining(","));
		if (kurslehrer != null) {
			dto.Lehrer_ID = kurslehrer.Lehrer_ID;
			dto.WochenstdKL = (double) kurslehrer.Wochenstunden;
		}
		conn.transactionPersist(dto);
    	conn.transactionFlush();
		mapKursDTOs.put(kurs.id, dto);
		// Aktualisiere die Zusatzkräfte
		if (kurslehrer != null) {
			conn.transactionExecuteDelete("DELETE FROM DTOKursLehrer e WHERE e.Kurs_ID = %d".formatted(dto.ID));
			for (final DTOKursLehrer kl : kursLehrerZusatzkraefte) {
				conn.transactionPersist(kl);
	        	conn.transactionFlush();
			}
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
	 *
	 * @throws ApiOperationException   im Fehlerfall
     */
    private void persist(final GostBlockungsergebnisManager ergebnisManager, final DTOSchuljahresabschnitte abschnitt, final GostHalbjahr halbjahr) throws ApiOperationException {
    	// Bestimme die ID des Jahrgangs
    	final List<DTOJahrgang> jahrgangsliste = conn.queryList("SELECT e FROM DTOJahrgang e WHERE e.ASDJahrgang = ?1 AND e.Sichtbar = ?2", DTOJahrgang.class, halbjahr.jahrgang, true);
    	if (jahrgangsliste.isEmpty())
    		throw new ApiOperationException(Status.NOT_FOUND);
    	if (jahrgangsliste.size() > 1)
    		throw new ApiOperationException(Status.CONFLICT);
    	final DTOJahrgang jahrgang = jahrgangsliste.get(0);
		// Bestimme die ID, mit welcher ein weiterer Kurs eingefügt wird
		final DTOSchemaAutoInkremente dbKurseID = conn.queryByKey(DTOSchemaAutoInkremente.class, Schema.tab_Kurse.name());
		long idKurs = dbKurseID == null ? 1 : dbKurseID.MaxID + 1;
    	// Durchwandere alle Kurse der Blockung und lege diese an, merke dabei auch die Zuordnung der neuen Kurs-IDs zu den Kurs-IDs der Blockung
		final HashMap<Long, Long> mapKursIDs = new HashMap<>();
		final HashMap<Long, DTOKurs> mapKursDTOs = new HashMap<>();
    	final GostBlockungsdatenManager datenManager = ergebnisManager.getParent();
    	for (final GostBlockungKurs kurs : datenManager.daten().kurse)
    		kursHinzufuegen(ergebnisManager, abschnitt, halbjahr, jahrgang, idKurs++, kurs, mapKursIDs, mapKursDTOs);
    	// Durchwandere alle Schüler des Abitur-Jahrgangs und lege ggf. fehlende Lernabschnitte an
		final DTOSchemaAutoInkremente dbID = conn.queryByKey(DTOSchemaAutoInkremente.class, Schema.tab_SchuelerLernabschnittsdaten.name());
		long idSLA = (dbID == null) ? 1 : dbID.MaxID + 1;
		final HashMap<Long, Long> mapLernabschnitte = new HashMap<>();
    	for (final Schueler schueler : datenManager.daten().schueler) {
    		DTOSchuelerLernabschnittsdaten lernabschnitt = DBUtilsSchuelerLernabschnittsdaten.get(conn, schueler.id, abschnitt.ID);
    		if (lernabschnitt == null)
    			lernabschnitt = DBUtilsSchuelerLernabschnittsdaten.createByPrevious(idSLA++, conn, schueler.id, abschnitt);
    		mapLernabschnitte.put(schueler.id, lernabschnitt.ID);
    	}
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
						case "LK" -> (fachwahl.AbiturFach == null) || (fachwahl.AbiturFach == 1) ? "LK1" : "LK2";
						case "ZK" -> "ZK";
						case "AT" -> "GKM";
						default -> null;
					};
    				case Q12 -> switch (fachwahl.Q12_Kursart) {
						case "M" -> "GKM";
						case "S" -> (fachwahl.AbiturFach == null) ? "GKS" : "AB" + fachwahl.AbiturFach;
						case "LK" -> (fachwahl.AbiturFach == null) || (fachwahl.AbiturFach == 1) ? "LK1" : "LK2";
						case "ZK" -> "ZK";
						case "AT" -> "GKM";
						default -> null;
					};
    				case Q21 -> switch (fachwahl.Q21_Kursart) {
						case "M" -> "GKM";
						case "S" -> (fachwahl.AbiturFach == null) ? "GKS" : "AB" + fachwahl.AbiturFach;
						case "LK" -> (fachwahl.AbiturFach == null) || (fachwahl.AbiturFach == 1) ? "LK1" : "LK2";
						case "ZK" -> "ZK";
						case "AT" -> "GKM";
						default -> null;
					};
    				case Q22 -> switch (fachwahl.Q22_Kursart) {
						case "M" -> (fachwahl.AbiturFach == null) ? "GKM" : "AB" + fachwahl.AbiturFach;
						case "S" -> (fachwahl.AbiturFach == null) ? "GKS" : "AB" + fachwahl.AbiturFach;
						case "LK" -> (fachwahl.AbiturFach == null) || (fachwahl.AbiturFach == 1) ? "LK1" : "LK2";
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
    	conn.transactionFlush();
    }


    /**
     * Aktiviert bzw. persistiert das Blockungsergebnis in der Kursliste und in den Leistungsdaten der
     * Schüler des Abiturjahrgangs
     *
     * @param idErgebnis   das zu persistierende Blockungsergebnis
     *
     * @return die HTTP-Response, welchen den Erfolg der Operation angibt
	 *
	 * @throws ApiOperationException   im Fehlerfall
     */
    public Response persist(final Long idErgebnis) throws ApiOperationException {
		DBUtilsGost.pruefeSchuleMitGOSt(conn);

		// Bestimme das Blockungs-Zwischenergebnis
		final DTOGostBlockungZwischenergebnis dtoErgebnis = conn.queryByKey(DTOGostBlockungZwischenergebnis.class, idErgebnis);
		if (dtoErgebnis == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		final GostBlockungsdatenManager datenManager = DataGostBlockungsdaten.getBlockungsdatenManagerFromDB(conn, dtoErgebnis.Blockung_ID);
		// Bestimme die Daten des Ergebnisses
        final GostBlockungsergebnis ergebnis = getErgebnis(conn, dtoErgebnis, datenManager);
        final GostBlockungsergebnisManager ergebnisManager = new GostBlockungsergebnisManager(datenManager, ergebnis);

        // Bestimme das Schuljahr und das Halbjahr, welchem das Ergebnis zugeordnet ist
        final GostHalbjahr halbjahr = GostHalbjahr.fromID(datenManager.daten().gostHalbjahr);
        final int schuljahr = halbjahr.getSchuljahrFromAbiturjahr(datenManager.daten().abijahrgang);
        // Prüfe, ob der Schuljahresabschnitt bereits angelegt wurde
    	final DTOSchuljahresabschnitte abschnitt = SchulUtils.getSchuljahreabschnitt(conn, schuljahr, halbjahr.halbjahr);
    	if (DBUtilsGost.pruefeHatOberstufenKurseInAbschnitt(conn, halbjahr, abschnitt))
    		throw new ApiOperationException(Status.CONFLICT);
    	persist(ergebnisManager, abschnitt, halbjahr);
        // Markiere die Blockung und das Ergebnis als aktiviert
    	DataGostBlockungsdaten.markiereBlockungAktiv(conn, dtoErgebnis.Blockung_ID, true);
		markiereErgebnisAktiv(conn, dtoErgebnis.ID, true);
        return Response.status(Status.NO_CONTENT).build();
    }


    /**
	 * Entfernt die Leistungsdaten für das angegeben Halbjahr der gymnasialen Oberstufe bei den Schülern des Abiturjahrgangs,
	 * welcher durch den Schuljahresabschnitt und das Halbjahr der gymnasialen Oberstufe gegeben ist.
	 * Dies wird nur durchgeführt, wenn Kurse für die gymnasiale Oberstufe angelegt sind und es keine Leistungsdaten
	 * für Oberstufenkursen bei den Schüler gibt, welche bereits Noten beinhalten.
     *
     * @param conn         die Datenbankverbindung
     * @param abiturjahr   die ID des Schuljahresabschnittes
     * @param idHalbjahr   die ID des Halbjahres der gymnasialen Oberstufe
     *
     * @return die HTTP-Response 204
     *
     * @throws ApiOperationException   im Fehlerfall
     */
    public static Response unpersist(final DBEntityManager conn, final int abiturjahr, final int idHalbjahr) throws ApiOperationException {
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
        // Bestimme den Schuljahresabschnitt...
		final GostHalbjahr halbjahr = GostHalbjahr.fromID(idHalbjahr);
		if (halbjahr == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Die ID %d ist nicht gültig als ID für ein Halbjahr der gymnasialen Oberstufe.".formatted(idHalbjahr));
        final int schuljahr = halbjahr.getSchuljahrFromAbiturjahr(abiturjahr);
    	final DTOSchuljahresabschnitte abschnitt = SchulUtils.getSchuljahreabschnitt(conn, schuljahr, halbjahr.halbjahr);
    	// ... und versuche die Leistungsdaten und die Kurse zu entfernen
		DBUtilsGost.deleteOberstufenKurseUndLeistungsdaten(conn, halbjahr, abschnitt);
		return Response.status(Status.NO_CONTENT).build();
    }


    /**
     * Synchronisiert das Blockungsergebnis in dem angegebenen Manager mit den persistierten Daten
     * in dem angegebenen Schuljahrabschnitt.
     *
     * Hierfür muss bereits geprüft sein, ob eine persistierte Blockung in diesem Abschnitt vorliegt!
     *
     * @param ergebnisManager   der Ergebnis-Manager
     * @param abschnitt         der Schuljahresabschnitt, in dem die Blockung synchronisiert wird
     * @param halbjahr          das Halbjahr der gymnasialen Oberstufe
	 *
	 * @throws ApiOperationException   im Fehlerfall
     */
    private void synchronisiere(final GostBlockungsergebnisManager ergebnisManager, final DTOSchuljahresabschnitte abschnitt, final GostHalbjahr halbjahr) throws ApiOperationException {
    	// Bestimme die ID des Jahrgangs
    	final List<DTOJahrgang> jahrgangsliste = conn.queryList("SELECT e FROM DTOJahrgang e WHERE e.ASDJahrgang = ?1 AND e.Sichtbar = ?2", DTOJahrgang.class, halbjahr.jahrgang, true);
    	if (jahrgangsliste.isEmpty())
    		throw new ApiOperationException(Status.NOT_FOUND, "Der Jahrgang '%s' wurde in der Datenbank nicht gefunden.".formatted(halbjahr.jahrgang));
    	if (jahrgangsliste.size() > 1)
    		throw new ApiOperationException(Status.CONFLICT, "Der Jahrgang '%s' wurde mehrfach in der Datenbank gefunden.".formatted(halbjahr.jahrgang));
    	final DTOJahrgang jahrgang = jahrgangsliste.get(0);
		// Bestimme die ID, mit welcher ggf. ein weiterer Kurs eingefügt wird
    	long idKurs = conn.transactionGetNextID(DTOKurs.class);
    	// Durchwandere alle Kurse der Blockung und lege diese an, sofern sie nicht bereits vorhanden sind. Merke dabei auch die Zuordnung der Kurs-IDs zu den Kurs-IDs der Blockung
		final HashMap<Long, Long> mapKursIDs = new HashMap<>();
		final HashMap<Long, DTOKurs> mapKursDTOs = new HashMap<>();
    	final GostBlockungsdatenManager datenManager = ergebnisManager.getParent();
    	for (final GostBlockungKurs kurs : datenManager.daten().kurse) {
    		// Prüfe, ob der Kurs bereits vorhanden ist
    		final List<DTOKurs> kurseVorhanden = conn.queryList("SELECT e FROM DTOKurs e WHERE e.Schuljahresabschnitts_ID = ?1 AND e.KurzBez = ?2 AND e.Fach_ID = ?3"
    				+ " AND e.Jahrgang_ID = ?4 AND e.ASDJahrgang = ?5 AND e.KursartAllg = ?6",
    				DTOKurs.class, abschnitt.ID, datenManager.kursGetName(kurs.id), kurs.fach_id, jahrgang.ID, halbjahr.jahrgang,
    				GostKursart.fromID(kurs.kursart).kuerzel);
    		if (kurseVorhanden.size() > 1)
    			throw new ApiOperationException(Status.CONFLICT, "Es existieren mehrere Kurse für einen Kurs der Blockung. Dies ist nicht zulässig und sollte in der Liste der Kurse zuvor korrigiert werden.");
    		if (kurseVorhanden.isEmpty()) {
    			kursHinzufuegen(ergebnisManager, abschnitt, halbjahr, jahrgang, idKurs++, kurs, mapKursIDs, mapKursDTOs);
    		} else {
    			kursAnpassen(kurseVorhanden.get(0), ergebnisManager, kurs, mapKursIDs, mapKursDTOs);
    		}
    	}
    	// Durchwandere alle Schüler des Abitur-Jahrgangs und lege ggf. fehlende Lernabschnitte an
		final HashMap<Long, Long> mapLernabschnitte = new HashMap<>();
    	for (final Schueler schueler : datenManager.daten().schueler) {
    		final DTOSchuelerLernabschnittsdaten lernabschnitt = DBUtilsSchuelerLernabschnittsdaten.get(conn, schueler.id, abschnitt.ID);
    		if (lernabschnitt == null)
    			continue;    // TODO Hier könnte ggf. ein Lernabschnitt angelegt werden... Dann müsste aber die Beschreibung der Methode bis hin zum Client angepasst werden...
    		mapLernabschnitte.put(schueler.id, lernabschnitt.ID);
    	}
    	// Durchwandere alle Schüler des Abitur-Jahrgangs und passe die Leistungsdaten an
    	for (final Schueler schueler : datenManager.daten().schueler) {
			// Bestimme den Lernabschnitt - TODO dies könnte entfallen, falls Lernabschnitt abgelegt werden (s.o.)
			final Long idLernabschnitt = mapLernabschnitte.get(schueler.id);
			if (idLernabschnitt == null)
				continue;
    		// Bestimme die Kurse, in welche der Schüler gesetzt wurde
    		final Set<GostBlockungsergebnisKurs> kursMenge = ergebnisManager.getOfSchuelerKursmenge(schueler.id);
    		for (final GostBlockungsergebnisKurs kurszuordnung : kursMenge) {
    			final GostBlockungKurs kurs = datenManager.kursGet(kurszuordnung.id);
    			final GostKursart kursart = GostKursart.fromID(kurs.kursart);
    			final GostFach fach = datenManager.faecherManager().get(kurs.fach_id);
    			final DTOKurs kursDTO = mapKursDTOs.get(kurs.id);
    			final DTOGostSchuelerFachbelegungen fachwahl = conn.queryByKey(DTOGostSchuelerFachbelegungen.class, schueler.id, fach.id);
    			final List<DTOSchuelerLeistungsdaten> leistungsDaten = conn.queryList("SELECT e FROM DTOSchuelerLeistungsdaten e WHERE e.Abschnitt_ID = ?1 AND e.Fach_ID = ?2",
    					DTOSchuelerLeistungsdaten.class, idLernabschnitt, kurs.fach_id);
    			if (leistungsDaten.size() != 1)
    				continue;  // TODO Hier könnten ggf. Leistungsdaten ergänzt werden
    			final DTOSchuelerLeistungsdaten leistung = leistungsDaten.get(0);
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
						case "LK" -> (fachwahl.AbiturFach != null) && (fachwahl.AbiturFach == 1) ? "LK1" : "LK2";
						case "ZK" -> "ZK";
						case "AT" -> "GKM";
						default -> null;
					};
    				case Q12 -> switch (fachwahl.Q12_Kursart) {
						case "M" -> "GKM";
						case "S" -> (fachwahl.AbiturFach == null) ? "GKS" : "AB" + fachwahl.AbiturFach;
						case "LK" -> (fachwahl.AbiturFach != null) && (fachwahl.AbiturFach == 1) ? "LK1" : "LK2";
						case "ZK" -> "ZK";
						case "AT" -> "GKM";
						default -> null;
					};
    				case Q21 -> switch (fachwahl.Q21_Kursart) {
						case "M" -> "GKM";
						case "S" -> (fachwahl.AbiturFach == null) ? "GKS" : "AB" + fachwahl.AbiturFach;
						case "LK" -> (fachwahl.AbiturFach != null) && (fachwahl.AbiturFach == 1) ? "LK1" : "LK2";
						case "ZK" -> "ZK";
						case "AT" -> "GKM";
						default -> null;
					};
    				case Q22 -> switch (fachwahl.Q22_Kursart) {
						case "M" -> (fachwahl.AbiturFach == null) ? "GKM" : "AB" + fachwahl.AbiturFach;
						case "S" -> (fachwahl.AbiturFach == null) ? "GKS" : "AB" + fachwahl.AbiturFach;
						case "LK" -> (fachwahl.AbiturFach != null) && (fachwahl.AbiturFach == 1) ? "LK1" : "LK2";
						case "ZK" -> "ZK";
						case "AT" -> "GKM";
						default -> null;
					};
    			};
    			leistung.KursartAllg = kursart.kuerzel;
    			leistung.Kurs_ID = mapKursIDs.get(kurs.id);
    			if ((leistung.NotenKrz == null) || (leistung.NotenKrz == Note.KEINE)) {
	    			leistung.NotenKrz = switch (halbjahr) {
						case EF1 -> switch (fachwahl.EF1_Kursart) {
							case "AT" -> Note.ATTEST;
							default -> leistung.NotenKrz;
						};
						case EF2 -> switch (fachwahl.EF2_Kursart) {
							case "AT" -> Note.ATTEST;
							default -> leistung.NotenKrz;
						};
						case Q11 -> switch (fachwahl.Q11_Kursart) {
							case "AT" -> Note.ATTEST;
							default -> leistung.NotenKrz;
						};
						case Q12 -> switch (fachwahl.Q12_Kursart) {
							case "AT" -> Note.ATTEST;
							default -> leistung.NotenKrz;
						};
						case Q21 -> switch (fachwahl.Q21_Kursart) {
							case "AT" -> Note.ATTEST;
							default -> leistung.NotenKrz;
						};
						case Q22 -> switch (fachwahl.Q22_Kursart) {
							case "AT" -> Note.ATTEST;
							default -> leistung.NotenKrz;
						};
	    			};
    			}
    			leistung.AbiFach = fachwahl.AbiturFach == null ? null : "" + fachwahl.AbiturFach;
    			leistung.Wochenstunden = kurs.wochenstunden;
    			conn.transactionPersist(leistung);
    		}
    	}
    	conn.transactionFlush();
    }


    /**
     * Synchronisiert das Blockungsergebnis mit den Daten in der Kursliste und in den Leistungsdaten der
     * Schüler des Abiturjahrgangs. Dabei werden
     * <ul>
     *   <li> ggf. neue Kurse angelegt </li>
     *   <li> keine (!) leeren Kurse entfernt </li>
     *   <li> die Kurs-Schüler-Zuordnungen bei vorhanden (!) Leistungsdaten zu einem Fach auf
     *     die Zuordnungen der Blockungsdaten angepasst </li>
     * </ul>
     *
     * @param idErgebnis   das zu synchronisierende Blockungsergebnis
     *
     * @return die HTTP-Response, welchen den Erfolg der Operation angibt
	 *
	 * @throws ApiOperationException   im Fehlerfall
     */
    public Response synchronisiere(final Long idErgebnis) throws ApiOperationException {
    	// Bestimm die aktuellen Daten der Schule, insbesondere den aktuellen Schuljahresabschnitt
		final DTOEigeneSchule schule = DBUtilsGost.pruefeSchuleMitGOSt(conn);
		final DTOSchuljahresabschnitte schuleAbschnitt = SchulUtils.getSchuljahreabschnitt(conn, schule.Schuljahresabschnitts_ID);

		// Bestimme das Blockungsergebnis und erzeuge die zugehörigen Manager-Klassen
		final DTOGostBlockungZwischenergebnis dtoErgebnis = conn.queryByKey(DTOGostBlockungZwischenergebnis.class, idErgebnis);
		if (dtoErgebnis == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		final GostBlockungsdatenManager datenManager = DataGostBlockungsdaten.getBlockungsdatenManagerFromDB(conn, dtoErgebnis.Blockung_ID);
        final GostBlockungsergebnis ergebnis = getErgebnis(conn, dtoErgebnis, datenManager);
        final GostBlockungsergebnisManager ergebnisManager = new GostBlockungsergebnisManager(datenManager, ergebnis);

        // Bestimme das Schuljahr und das Halbjahr, welchem das Ergebnis zugeordnet ist
        final GostHalbjahr halbjahr = GostHalbjahr.fromID(datenManager.daten().gostHalbjahr);
        final int schuljahr = halbjahr.getSchuljahrFromAbiturjahr(datenManager.daten().abijahrgang);
        // Prüfe, ob der Schuljahresabschnitt des Ergebnisses in der Vergangenheit liegt
        final DTOSchuljahresabschnitte abschnitt = SchulUtils.getSchuljahreabschnitt(conn, schuljahr, halbjahr.halbjahr);
        if ((schuleAbschnitt.Jahr > abschnitt.Jahr) || ((schuleAbschnitt.Jahr == abschnitt.Jahr) && (schuleAbschnitt.Abschnitt > abschnitt.Abschnitt)))
        	throw new ApiOperationException(Status.BAD_REQUEST, "Der Schuljahresabschnitt des Blockungsergebnisses liegt bereits in der Vergangenheit und darf deswegen nicht mehr synchronisiert werden.");
       	// Prüfe, ob der Schuljahresabschnitt bereits angelegt sind - wenn nicht, dann existiert auch keine persistierte Blockung
    	if (!DBUtilsGost.pruefeHatOberstufenKurseInAbschnitt(conn, halbjahr, abschnitt))
    		throw new ApiOperationException(Status.CONFLICT, "Der Schuljahresabschnitt des Blockungsergebnisses hat noch keine persistierte Blockung. Daher ist eine Synchronisation nicht möglich. Sie können die Blockung stattdessen aktivieren.");
    	// Versuche die Synchronisation durchzuführen
    	synchronisiere(ergebnisManager, abschnitt, halbjahr);
        return Response.status(Status.NO_CONTENT).build();
    }


    /**
     * Fügt die Ergebnisse aus der übergebenen Liste zu der Blockung mit der angegebenen ID hinzu.
     *
     * @param idBlockung   die ID der Blockung
     * @param ergebnisse   die Ergebnisse, die hinzugefügt werden sollen.
     *
     * @return die HTTP-Response mit der Liste der hinzugefügten Ergebnisse, welche dann die Datenbank-IDs
     *         der persistierten Ergebnisse beinhalten.
	 *
	 * @throws ApiOperationException   im Fehlerfall
     */
	public Response addErgebnisse(final long idBlockung, final List<GostBlockungsergebnis> ergebnisse) throws ApiOperationException {
		// Prüfe, ob die Schule eine gymnasiale Oberstufe hat
		DBUtilsGost.pruefeSchuleMitGOSt(conn);
        // Prüfe, Bestimme die Blockung
        final DTOGostBlockung blockung = conn.queryByKey(DTOGostBlockung.class, idBlockung);
        if (blockung == null)
        	throw new ApiOperationException(Status.NOT_FOUND, "Die Blockung mit der ID %d konnte nicht gefunden werden".formatted(idBlockung));
		final DTOSchemaAutoInkremente lastID = conn.queryByKey(DTOSchemaAutoInkremente.class, Schema.tab_Gost_Blockung_Zwischenergebnisse.name());
		long ergebnisID = lastID == null ? 1 : lastID.MaxID + 1;
        for (final GostBlockungsergebnis ergebnis : ergebnisse) {
        	if ((ergebnis.blockungID != idBlockung) || (ergebnis.gostHalbjahr != blockung.Halbjahr.id))
        		throw new ApiOperationException(Status.BAD_REQUEST);
			// Schreibe das Ergebnis in die Datenbank.
			final DTOGostBlockungZwischenergebnis erg = new DTOGostBlockungZwischenergebnis(ergebnisID, idBlockung, false);
			conn.transactionPersist(erg);
			conn.transactionFlush();
			// Schreibe die Kurs-Schienen und die Kurs-Schüler-Zuprdnungen in die Datenbank
			final Set<Long> kursIDs = new HashSet<>();
			for (final GostBlockungsergebnisSchiene schiene : ergebnis.schienen) {
				for (final GostBlockungsergebnisKurs kurs : schiene.kurse) {
					// Ergänze die Kurs-Schienen-Zuordnung
					conn.transactionPersist(new DTOGostBlockungZwischenergebnisKursSchiene(ergebnisID, kurs.id, schiene.id));
					// Ergänze die Schülerzuordnung, falls diese nicht bereits durch den Kurs zuvor ergänzt wurde
					if (kursIDs.contains(kurs.id))
						continue;
					kursIDs.add(kurs.id);
					for (final long idSchueler : kurs.schueler)
						conn.transactionPersist(new DTOGostBlockungZwischenergebnisKursSchueler(ergebnisID, kurs.id, idSchueler));
				}
			}
			conn.transactionFlush();
			// Ergänze die ID bei den Ergebnissen
			ergebnis.id = ergebnisID;
			ergebnisID++;
        }
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(ergebnisse).build();
	}

}
