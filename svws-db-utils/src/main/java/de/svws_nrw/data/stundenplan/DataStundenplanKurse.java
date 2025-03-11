package de.svws_nrw.data.stundenplan;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.asd.data.kurse.KursDaten;
import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.core.data.stundenplan.StundenplanKurs;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.kurse.DataKurse;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKursLehrer;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKursSchueler;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplan;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanSchienen;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link StundenplanKurs}.
 */
public final class DataStundenplanKurse extends DataManager<Long> {

	private final Long stundenplanID;

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link StundenplanKurs}.
	 *
	 * @param conn            die Datenbank-Verbindung für den Datenbankzugriff
	 * @param stundenplanID   die ID des Stundenplans, dessen Kurse abgefragt werden
	 */
	public DataStundenplanKurse(final DBEntityManager conn, final Long stundenplanID) {
		super(conn);
		this.stundenplanID = stundenplanID;
	}


	private static StundenplanKurs map(final KursDaten k, final DTOFach f, final @NotNull HashMap2D<Integer, Long, DTOStundenplanSchienen> mapSchienen) {
		final StundenplanKurs daten = new StundenplanKurs();
		daten.id = k.id;
		daten.idFach = k.idFach;
		daten.bezeichnung = k.kuerzel;
		daten.wochenstunden = k.wochenstunden;
		if (k.sortierung != 32000)
			daten.sortierung = k.sortierung;
		else if (f.SortierungAllg != null)
			daten.sortierung = f.SortierungAllg;
		else
			daten.sortierung = (f.SortierungSekII == null) ? 32000 : f.SortierungSekII;
		daten.jahrgaenge.addAll(k.idJahrgaenge);
		// Bestimme aus den Schienennummern der Kurs-Tabelle die IDs der Stundenplan-Kurse
		if (!k.schienen.isEmpty()) {
			for (final int schienenNummer : k.schienen) {
				for (final Long jgID : daten.jahrgaenge) {
					final DTOStundenplanSchienen schienen = mapSchienen.getOrNull(schienenNummer, jgID);
					if (schienen != null)
						daten.schienen.add(schienen.ID);
				}
			}
		}
		return daten;
	}


	@Override
	public Response getAll() throws ApiOperationException {
		return this.getList();
	}

	/**
	 * Gibt die Kurse des Stundenplans zurück.
	 *
	 * @param conn            die Datenbankverbindung
	 * @param idStundenplan   die ID des Stundenplans
	 *
	 * @return die Liste der Kurse
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public static List<StundenplanKurs> getKurse(final @NotNull DBEntityManager conn, final long idStundenplan) throws ApiOperationException {
		final DTOStundenplan stundenplan = conn.queryByKey(DTOStundenplan.class, idStundenplan);
		if (stundenplan == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde kein Stundenplan mit der ID %d gefunden.".formatted(idStundenplan));
		final List<KursDaten> kurse = DataKurse.getKursListenFuerAbschnitt(conn, stundenplan.Schuljahresabschnitts_ID, false);
		if (kurse.isEmpty())
			return new ArrayList<>();
		final List<Long> kursIDs = kurse.stream().map(k -> k.id).toList();
		// Schüler bestimmen
		final Map<Long, List<Long>> mapKursSchuelerIDs = conn.queryList(
				"SELECT e FROM DTOKursSchueler e WHERE e.Kurs_ID IN ?1 AND e.LernabschnittWechselNr = 0", DTOKursSchueler.class, kursIDs)
				.stream().collect(Collectors.groupingBy(ks -> ks.Kurs_ID, Collectors.mapping(ks -> ks.Schueler_ID, Collectors.toList())));
		// Lehrer bestimmen
		final Map<Long, List<Long>> mapKursZusatzkraefte = conn.queryList(DTOKursLehrer.QUERY_LIST_BY_KURS_ID, DTOKursLehrer.class, kursIDs)
				.stream().collect(Collectors.groupingBy(ks -> ks.Kurs_ID, Collectors.mapping(ks -> ks.Lehrer_ID, Collectors.toList())));
		// Fächer bestimmen
		final List<Long> faecherIDs = kurse.stream().map(k -> k.idFach).toList();
		final Map<Long, DTOFach> mapFaecher = conn.queryByKeyList(DTOFach.class, faecherIDs).stream().collect(Collectors.toMap(f -> f.ID, f -> f));
		// Map für Schienen-IDs bestimmen
		DataStundenplanSchienen.updateSchienenFromKursliste(conn, stundenplan.ID, kurse);
		conn.transactionFlush();
		final @NotNull HashMap2D<Integer, Long, DTOStundenplanSchienen> mapSchienen = DataStundenplanSchienen.getMapDTOs(conn, stundenplan.ID);
		// Erstelle die Core-DTOs
		final ArrayList<StundenplanKurs> daten = new ArrayList<>();
		for (final KursDaten k : kurse) {
			final DTOFach f = mapFaecher.get(k.idFach);
			if (f == null)
				throw new ApiOperationException(Status.NOT_FOUND,
						"Es wurde kein Fach mit der ID %d für den Kurs mit der ID %d gefunden.".formatted(k.idFach, k.id));
			final StundenplanKurs kurs = map(k, f, mapSchienen);
			final List<Long> schuelerIDs = mapKursSchuelerIDs.get(k.id);
			if (schuelerIDs != null)
				kurs.schueler.addAll(schuelerIDs);
			if (k.lehrer != null)
				kurs.lehrer.add(k.lehrer);
			final List<Long> zusatzkraefteIDs = mapKursZusatzkraefte.get(k.id);
			if (zusatzkraefteIDs != null)
				kurs.lehrer.addAll(zusatzkraefteIDs);
			daten.add(kurs);
		}
		return daten;
	}


	@Override
	public Response getList() throws ApiOperationException {
		final List<StundenplanKurs> daten = getKurse(conn, this.stundenplanID);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	@Override
	public Response get(final Long id) throws ApiOperationException {
		final DTOStundenplan stundenplan = conn.queryByKey(DTOStundenplan.class, stundenplanID);
		if (stundenplan == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde kein Stundenplan mit der ID %d gefunden.".formatted(stundenplanID));
		if (id == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Eine Anfrage zu einem Kurs mit der ID null ist unzulässig.");
		final List<KursDaten> kurse = DataKurse.getKursListenFuerAbschnitt(conn, stundenplan.Schuljahresabschnitts_ID, false);
		if (kurse.isEmpty())
			throw new ApiOperationException(Status.NOT_FOUND,
					"Es wurden keine Kurse in dem Schuljahresabschnitt mit der ID %d gefunden.".formatted(stundenplan.Schuljahresabschnitts_ID));
		final KursDaten kurs = DataKurse.getKursdaten(conn, id);
		if (kurs == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde kein Kurs mit der ID %d gefunden.".formatted(id));
		if (kurs.idSchuljahresabschnitt != stundenplan.Schuljahresabschnitts_ID)
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Der Schuljahresabschnitt %d des Kurses mit der ID %d stimmt nicht mit dem Schuljahresabschitt %d bei dem Stundenplan mit der ID %d überein."
							.formatted(kurs.idSchuljahresabschnitt, kurs.id, stundenplan.Schuljahresabschnitts_ID, stundenplan.ID));
		// Map für Schienen-IDs bestimmen
		DataStundenplanSchienen.updateSchienenFromKursliste(conn, stundenplan.ID, kurse);
		conn.transactionFlush();
		final @NotNull HashMap2D<Integer, Long, DTOStundenplanSchienen> mapSchienen = DataStundenplanSchienen.getMapDTOs(conn, stundenplan.ID);
		// Schüler bestimmen
		final List<Long> schuelerIDs = conn.queryList(
				"SELECT e FROM DTOKursSchueler e WHERE e.Kurs_ID = :value AND e.LernabschnittWechselNr IS NULL", DTOKursSchueler.class, kurs.id)
				.stream().map(ks -> ks.Schueler_ID).toList();
		// Fachdefinition laden
		final DTOFach fach = conn.queryByKey(DTOFach.class, kurs.idFach);
		if (fach == null)
			throw new ApiOperationException(Status.NOT_FOUND,
					"Es wurde kein Fach mit der ID %d für den Kurs mit der ID %d gefunden.".formatted(kurs.idFach, kurs.id));
		// DTO erstellen
		final StundenplanKurs daten = map(kurs, fach, mapSchienen);
		daten.schueler.addAll(schuelerIDs);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	@Override
	public Response patch(final Long id, final InputStream is) {
		throw new UnsupportedOperationException();
	}

}
