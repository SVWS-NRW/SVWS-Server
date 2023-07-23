package de.svws_nrw.data.stundenplan;

import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.stundenplan.StundenplanKomplett;
import de.svws_nrw.core.data.stundenplan.StundenplanUnterricht;
import de.svws_nrw.core.data.stundenplan.StundenplanZeitraster;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLeistungsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplan;
import de.svws_nrw.db.utils.OperationError;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den Core-DTO
 * {@link StundenplanKomplett}.
 */
public final class DataSchuelerStundenplan extends DataManager<Long> {

	private final Long idStundenplan;

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO
	 * {@link StundenplanKomplett}.
	 *
	 * @param conn            die Datenbank-Verbindung für den Datenbankzugriff
	 * @param idStundenplan   die ID des Stundenplans, dessen Zeitraster abgefragt wird
	 */
	public DataSchuelerStundenplan(final DBEntityManager conn, final Long idStundenplan) {
		super(conn);
		this.idStundenplan = idStundenplan;
	}

	@Override
	public Response getAll() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response getList() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response get(final Long idSchueler) {
		// Bestimme den Schüler und die Daten des Stundenplans
		final DTOSchueler dtoSchueler = conn.queryByKey(DTOSchueler.class, idSchueler);
		if (dtoSchueler == null)
			return OperationError.NOT_FOUND.getResponse("Kein Schüler mit der ID %d gefunden.".formatted(idSchueler));

		// Bestimme den Schüler und die Daten des Stundenplans
		final DTOStundenplan dtoStundenplan = conn.queryByKey(DTOStundenplan.class, idStundenplan);
		if (dtoStundenplan == null)
			return OperationError.NOT_FOUND.getResponse("Kein Stundenplan mit der ID %d gefunden.".formatted(idStundenplan));

		// Erzeuge damit das Grundgerüst für den Stundenplan
		final @NotNull StundenplanKomplett stundenplan = new StundenplanKomplett();
		stundenplan.daten.id = idStundenplan;
		stundenplan.unterrichtsverteilung.id = dtoStundenplan.ID;
		stundenplan.daten.idSchuljahresabschnitt = dtoStundenplan.Schuljahresabschnitts_ID;
		stundenplan.daten.gueltigAb = dtoStundenplan.Beginn;
		stundenplan.daten.gueltigBis = dtoStundenplan.Ende;
		stundenplan.daten.bezeichnungStundenplan = dtoStundenplan.Beschreibung;
		stundenplan.daten.wochenTypModell = dtoStundenplan.WochentypModell;

		// Zeitraster ...
		final List<StundenplanZeitraster> zeitraster = DataStundenplanZeitraster.getZeitraster(conn, idStundenplan);
		stundenplan.daten.zeitraster.addAll(zeitraster);
		stundenplan.daten.kalenderwochenZuordnung.addAll(DataStundenplanKalenderwochenzuordnung.getKalenderwochenzuordnungen(conn, idStundenplan));

		// Bestime den Lernabschnitt des Schülers, um anschließend mithilfe de aktuellen Leistungsdaten den Stundenplan zu befüllen
		final List<DTOSchuelerLernabschnittsdaten> lernabschnittsdaten = conn.query(
				"SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Schuljahresabschnitts_ID = :sja AND e.Schueler_ID = :sid AND e.WechselNr IS NULL",
				DTOSchuelerLernabschnittsdaten.class).setParameter("sja", dtoStundenplan.Schuljahresabschnitts_ID)
				.setParameter("sid", idSchueler).getResultList();
		if ((lernabschnittsdaten == null) || (lernabschnittsdaten.size() != 1))
			return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(stundenplan).build();
		final DTOSchuelerLernabschnittsdaten lernabschnitt = lernabschnittsdaten.get(0);

		// Bestimme nun die Leistungsdaten zu dem Lernabschnitt
		final List<DTOSchuelerLeistungsdaten> leistungsdaten = conn.queryNamed("DTOSchuelerLeistungsdaten.abschnitt_id",
				lernabschnitt.ID, DTOSchuelerLeistungsdaten.class);
		if ((leistungsdaten == null) || (leistungsdaten.isEmpty()))
			return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(stundenplan).build();

		// Bestimme die Unterrichte, die zu den Leistungsdaten gehören ...
		final List<StundenplanUnterricht> alleUnterrichte = DataStundenplanUnterricht.getUnterrichte(conn, idStundenplan);
		final Map<Long, List<StundenplanUnterricht>> mapUnterricht = alleUnterrichte.stream()
				.filter(u -> (u.idKurs != null) || u.klassen.contains(lernabschnitt.Klassen_ID))
				.collect(Collectors.groupingBy(u -> (u.idKurs != null ? u.idKurs : u.idFach)));

		// Gehe die Leistungsdaten und trage die Unterrichte ein
		final Set<Long> lehrerIDs = new HashSet<>();
		final Set<Long> klassenIDs = new HashSet<>();
		final Set<Long> kursIDs = new HashSet<>();
		final Set<Long> raumIDs = new HashSet<>();
		final Set<Long> schienenIDs = new HashSet<>();
		final Set<Long> fachIDs = new HashSet<>();
		for (final DTOSchuelerLeistungsdaten ld : leistungsdaten) {
			final List<StundenplanUnterricht> unterrichte = mapUnterricht.get(ld.Kurs_ID != null ? ld.Kurs_ID : ld.Fach_ID);
			if (unterrichte == null)
				continue;
			if (ld.Kurs_ID != null)
				kursIDs.add(ld.Kurs_ID);
			fachIDs.add(ld.Fach_ID);
			stundenplan.unterrichte.addAll(unterrichte);
			for (final StundenplanUnterricht u : unterrichte) {
				lehrerIDs.addAll(u.lehrer);
				klassenIDs.addAll(u.klassen);
				raumIDs.addAll(u.raeume);
				schienenIDs.addAll(u.schienen);
			}
		}

		// Ergänze die Informationen zu den Lehrern, Klassen, Fächern, Räumen und Schienen
		stundenplan.unterrichtsverteilung.lehrer.addAll(DataStundenplanLehrer.getLehrer(conn, idStundenplan).stream()
				.filter(l -> lehrerIDs.contains(l.id)).toList());
		stundenplan.unterrichtsverteilung.klassen.addAll(DataStundenplanKlassen.getKlassen(conn, idStundenplan).stream()
				.filter(k -> klassenIDs.contains(k.id) || (lernabschnitt.Klassen_ID == k.id)).toList());
		stundenplan.unterrichtsverteilung.kurse.addAll(DataStundenplanKurse.getKurse(conn, idStundenplan).stream()
				.filter(k -> kursIDs.contains(k.id)).toList());
		stundenplan.unterrichtsverteilung.faecher.addAll(DataStundenplanFaecher.getFaecher(conn, idStundenplan).stream()
				.filter(f -> fachIDs.contains(f.id)).toList());
		stundenplan.daten.raeume.addAll(DataStundenplanRaeume.getRaeume(conn, idStundenplan).stream()
				.filter(r -> raumIDs.contains(r.id)).toList());
		stundenplan.daten.schienen.addAll(DataStundenplanSchienen.getSchienen(conn, idStundenplan).stream()
				.filter(s -> schienenIDs.contains(s.id)).toList());
		// Füge die Kurs-Schüler hinzu und ergänze ggf. noch Klasseneinträge, die bei diesen Schülern vorkommen
		final Set<Long> schuelerIDs = new HashSet<>();
		schuelerIDs.add(idSchueler); // Ergänze die Information zum Schüler, dessen Stundenplan angezeigt wird
		schuelerIDs.addAll(stundenplan.unterrichtsverteilung.kurse.stream().flatMap(k -> k.schueler.stream()).toList());
		stundenplan.unterrichtsverteilung.schueler.addAll(DataStundenplanSchueler.getSchueler(conn, idStundenplan).stream()
				.filter(s -> schuelerIDs.contains(s.id)).toList());
		final Set<Long> weitereKlassenIDs = new HashSet<>();
		weitereKlassenIDs.add(lernabschnitt.Klassen_ID); // Ergänze die Information zum Schüler, dessen Stundenplan angezeigt wird
		weitereKlassenIDs.addAll(stundenplan.unterrichtsverteilung.schueler.stream().map(s -> s.idKlasse).toList());
		weitereKlassenIDs.removeAll(stundenplan.unterrichtsverteilung.klassen.stream().map(k -> k.id).toList());
		stundenplan.unterrichtsverteilung.klassen.addAll(DataStundenplanKlassen.getKlassen(conn, idStundenplan).stream()
				.filter(k -> weitereKlassenIDs.contains(k.id)).toList());
		// Füge die Jahrgänge hinzu
		final Set<Long> jahrgangIDs = new HashSet<>();
		jahrgangIDs.addAll(stundenplan.unterrichtsverteilung.klassen.stream().flatMap(j -> j.jahrgaenge.stream()).toList());
		jahrgangIDs.addAll(stundenplan.unterrichtsverteilung.kurse.stream().flatMap(j -> j.jahrgaenge.stream()).toList());
		stundenplan.daten.jahrgaenge.addAll(DataStundenplanJahrgaenge.getJahrgaenge(conn, idStundenplan).stream()
				.filter(j -> jahrgangIDs.contains(j.id)).toList());
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(stundenplan).build();
	}

	@Override
	public Response patch(final Long idSchueler, final InputStream is) {
		throw new UnsupportedOperationException();
	}

}
