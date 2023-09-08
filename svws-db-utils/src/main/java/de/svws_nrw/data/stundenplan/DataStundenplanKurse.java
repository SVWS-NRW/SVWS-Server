package de.svws_nrw.data.stundenplan;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.stundenplan.StundenplanKurs;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKurs;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKursLehrer;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKursSchueler;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplan;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanSchienen;
import de.svws_nrw.db.utils.OperationError;
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


	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOKurs} in einen Core-DTO {@link StundenplanKurs}.
	 */
	private static final BiFunction<DTOKurs, DTOFach, StundenplanKurs> dtoMapper = (final DTOKurs k, final DTOFach f) -> {
		final StundenplanKurs daten = new StundenplanKurs();
		daten.id = k.ID;
		daten.bezeichnung = k.KurzBez;
		daten.wochenstunden = k.WochenStd;
		daten.sortierung = (k.Sortierung == null) ? ((f.SortierungAllg == null) ? ((f.SortierungSekII == null) ? 32000 : f.SortierungSekII) : f.SortierungAllg) : k.Sortierung;
		return daten;
	};


	@Override
	public Response getAll() {
		return this.getList();
	}

	/**
	 * Gibt die Kurse des Stundenplans zurück.
	 *
	 * @param conn            die Datenbankverbindung
	 * @param idStundenplan   die ID des Stundenplans
	 *
	 * @return die Liste der Kurse
	 */
	public static List<StundenplanKurs> getKurse(final @NotNull DBEntityManager conn, final long idStundenplan) {
		final DTOStundenplan stundenplan = conn.queryByKey(DTOStundenplan.class, idStundenplan);
		if (stundenplan == null)
			throw OperationError.NOT_FOUND.exception("Es wurde kein Stundenplan mit der ID %d gefunden.".formatted(idStundenplan));
		final List<DTOKurs> kurse = conn.queryNamed("DTOKurs.schuljahresabschnitts_id", stundenplan.Schuljahresabschnitts_ID, DTOKurs.class);
		// Schüler bestimmen
		final List<Long> kursIDs = kurse.stream().map(k -> k.ID).toList();
		final Map<Long, List<Long>> mapKursSchuelerIDs = conn.queryList("SELECT e FROM DTOKursSchueler e WHERE e.Kurs_ID IN ?1 AND e.LernabschnittWechselNr = 0", DTOKursSchueler.class, kursIDs)
				.stream().collect(Collectors.groupingBy(ks -> ks.Kurs_ID, Collectors.mapping(ks -> ks.Schueler_ID, Collectors.toList())));
		// Lehrer bestimmen
		final Map<Long, List<Long>> mapKursZusatzkraefte = conn.queryNamed("DTOKursLehrer.kurs_id.multiple", kursIDs, DTOKursLehrer.class)
				.stream().collect(Collectors.groupingBy(ks -> ks.Kurs_ID, Collectors.mapping(ks -> ks.Lehrer_ID, Collectors.toList())));
		// Fächer bestimmen
		final List<Long> faecherIDs = kurse.stream().map(k -> k.Fach_ID).toList();
		final Map<Long, DTOFach> mapFaecher = conn.queryNamed("DTOFach.id.multiple", faecherIDs, DTOFach.class).stream()
				.collect(Collectors.toMap(f -> f.ID, f -> f));
		// Map für Schienen-IDs bestimmen
		final Map<Integer, Map<Long, Long>> mapNummerJahrgangID = conn.queryNamed("DTOStundenplanSchienen.stundenplan_id", idStundenplan, DTOStundenplanSchienen.class)
				.stream().collect(Collectors.groupingBy(s -> s.Nummer, Collectors.toMap(s -> s.Jahrgang_ID, s -> s.ID)));
		// Erstelle die Core-DTOs
		final ArrayList<StundenplanKurs> daten = new ArrayList<>();
		for (final DTOKurs k : kurse) {
			final DTOFach f = mapFaecher.get(k.Fach_ID);
			if (f == null)
				throw OperationError.NOT_FOUND.exception("Es wurde kein Fach mit der ID %d für den Kurs mit der ID %d gefunden.".formatted(k.Fach_ID, k.ID));
			final StundenplanKurs kurs = dtoMapper.apply(k, f);
			if (k.Jahrgang_ID == null) {
				kurs.jahrgaenge.addAll(strLongToList(k.Jahrgaenge));
			} else {
				kurs.jahrgaenge.add(k.Jahrgang_ID);
			}
			if (k.Schienen != null) {
				for (final Long schienenNummer : strLongToList(k.Schienen)) {
					final Map<Long, Long> mapJahrgangID = mapNummerJahrgangID.get(schienenNummer.intValue());
					if ((mapJahrgangID != null) && (!mapJahrgangID.isEmpty())) {
						for (final Long jgID : kurs.jahrgaenge) {
							final Long schienenID = mapJahrgangID.get(jgID);
							if (schienenID != null)
								kurs.schienen.add(schienenID);
						}
					}
				}
			}
			final List<Long> schuelerIDs = mapKursSchuelerIDs.get(k.ID);
			if (schuelerIDs != null)
				kurs.schueler.addAll(schuelerIDs);
			if (k.Lehrer_ID != null)
				kurs.lehrer.add(k.Lehrer_ID);
			final List<Long> zusatzkraefteIDs = mapKursZusatzkraefte.get(k.ID);
			if (zusatzkraefteIDs != null)
				kurs.lehrer.addAll(zusatzkraefteIDs);
			daten.add(kurs);
		}
		return daten;
	}


	@Override
	public Response getList() {
		final List<StundenplanKurs> daten = getKurse(conn, this.stundenplanID);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	private static List<Long> strLongToList(final String str) {
		final List<Long> result = new ArrayList<>();
		if (str == null)
			return result;
		final String[] parts = str.split(",");
		for (final String part : parts) {
			try {
				result.add(Long.parseLong(part.trim()));
			} catch (@SuppressWarnings("unused") final NumberFormatException e) {
				// der Fehler wird hier ignoriert und keine ID übernommen
			}
		}
		return result;
	}


	@Override
	public Response get(final Long id) {
		final DTOStundenplan stundenplan = conn.queryByKey(DTOStundenplan.class, stundenplanID);
		if (stundenplan == null)
			throw OperationError.NOT_FOUND.exception("Es wurde kein Stundenplan mit der ID %d gefunden.".formatted(stundenplanID));
		if (id == null)
			return OperationError.BAD_REQUEST.getResponse("Eine Anfrage zu einem Kurs mit der ID null ist unzulässig.");
		final DTOKurs kurs = conn.queryByKey(DTOKurs.class, id);
		if (kurs == null)
			return OperationError.NOT_FOUND.getResponse("Es wurde kein Kurs mit der ID %d gefunden.".formatted(id));
		if (kurs.Schuljahresabschnitts_ID != stundenplan.Schuljahresabschnitts_ID)
			return OperationError.BAD_REQUEST.getResponse("Der Schuljahresabschnitt %d des Kurses mit der ID %d stimmt nicht mit dem Schuljahresabschitt %d bei dem Stundenplan mit der ID %d überein.".formatted(kurs.Schuljahresabschnitts_ID, kurs.ID, stundenplan.Schuljahresabschnitts_ID, stundenplan.ID));
		// Jahrgänge bestimmen
		final List<Long> jahrgangsIDs = new ArrayList<>();
		if (kurs.Jahrgang_ID == null) {
			jahrgangsIDs.addAll(strLongToList(kurs.Jahrgaenge));
		} else {
			jahrgangsIDs.add(kurs.Jahrgang_ID);
		}
		// Schienen-IDs bestimmen
		final Map<Integer, Map<Long, Long>> mapNummerJahrgangID = conn.queryNamed("DTOStundenplanSchienen.stundenplan_id", stundenplan.ID, DTOStundenplanSchienen.class)
				.stream().collect(Collectors.groupingBy(s -> s.Nummer, Collectors.toMap(s -> s.Jahrgang_ID, s -> s.ID)));
		// Schüler bestimmen
		final List<Long> schuelerIDs = conn.queryList("SELECT e FROM DTOKursSchueler e WHERE e.Kurs_ID = :value AND e.LernabschnittWechselNr IS NULL", DTOKursSchueler.class, kurs.ID)
				.stream().map(ks -> ks.Schueler_ID).toList();
		// Fachdefinition laden
		final DTOFach fach = conn.queryByKey(DTOFach.class, kurs.Fach_ID);
		if (fach == null)
			throw OperationError.NOT_FOUND.exception("Es wurde kein Fach mit der ID %d für den Kurs mit der ID %d gefunden.".formatted(kurs.Fach_ID, kurs.ID));
		// DTO erstellen
		final StundenplanKurs daten = dtoMapper.apply(kurs, fach);
		daten.jahrgaenge.addAll(jahrgangsIDs);
		if (kurs.Schienen != null) {
			for (final Long schienenNummer : strLongToList(kurs.Schienen)) {
				final Map<Long, Long> mapJahrgangID = mapNummerJahrgangID.get(schienenNummer.intValue());
				if ((mapJahrgangID != null) && (!mapJahrgangID.isEmpty())) {
					for (final Long jgID : jahrgangsIDs) {
						final Long schienenID = mapJahrgangID.get(jgID);
						if (schienenID != null)
							daten.schienen.add(schienenID);
					}
				}
			}
		}
		daten.schueler.addAll(schuelerIDs);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	@Override
	public Response patch(final Long id, final InputStream is) {
		throw new UnsupportedOperationException();
	}

}
