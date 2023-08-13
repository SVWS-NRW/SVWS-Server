package de.svws_nrw.data.stundenplan;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.stundenplan.StundenplanKlasse;
import de.svws_nrw.core.data.stundenplan.StundenplanKomplett;
import de.svws_nrw.core.data.stundenplan.StundenplanLehrer;
import de.svws_nrw.core.data.stundenplan.StundenplanRaum;
import de.svws_nrw.core.data.stundenplan.StundenplanSchiene;
import de.svws_nrw.core.data.stundenplan.StundenplanUnterricht;
import de.svws_nrw.core.data.stundenplan.StundenplanZeitraster;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplan;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanUnterricht;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanUnterrichtLehrer;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den Core-DTO
 * {@link StundenplanKomplett}.
 */
public final class DataLehrerStundenplan extends DataManager<Long> {

	private final Long idStundenplan;


	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO
	 * {@link StundenplanKomplett}.
	 *
	 * @param conn          die Datenbank-Verbindung für den Datenbankzugriff
	 * @param idStundenplan die ID des Stundenplans, dessen Zeitraster abgefragt
	 *                      wird
	 */
	public DataLehrerStundenplan(final DBEntityManager conn, final Long idStundenplan) {
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
	public Response get(final Long id) {
		final StundenplanKomplett stundenplan = new StundenplanKomplett();
		final DTOStundenplan dtoStundenplan = conn.queryByKey(DTOStundenplan.class, idStundenplan);
		if (dtoStundenplan == null)
			throw new NotFoundException("Kein Stundenplan mit angegebener ID gefunden");
		stundenplan.daten.id = dtoStundenplan.ID;
		stundenplan.unterrichtsverteilung.id = dtoStundenplan.ID;
		stundenplan.daten.idSchuljahresabschnitt = dtoStundenplan.Schuljahresabschnitts_ID;
		stundenplan.daten.bezeichnungStundenplan = dtoStundenplan.Beschreibung;
		stundenplan.daten.gueltigAb = dtoStundenplan.Beginn;
		stundenplan.daten.gueltigBis = dtoStundenplan.Ende == null ? "" : dtoStundenplan.Ende;
		stundenplan.daten.wochenTypModell = dtoStundenplan.WochentypModell;
		final StundenplanLehrer lehrer = DataStundenplanLehrer.getById(conn, idStundenplan, id);
		stundenplan.daten.zeitraster = DataStundenplanZeitraster.getZeitraster(conn, idStundenplan);
		stundenplan.daten.pausenzeiten = DataStundenplanPausenzeiten.getPausenzeiten(conn, idStundenplan);
		stundenplan.daten.aufsichtsbereiche = DataStundenplanAufsichtsbereiche.getAufsichtsbereiche(conn, idStundenplan);
		stundenplan.daten.jahrgaenge.addAll(DataStundenplanJahrgaenge.getJahrgaenge(conn, idStundenplan));
		stundenplan.daten.kalenderwochenZuordnung.addAll(DataStundenplanKalenderwochenzuordnung.getKalenderwochenzuordnungen(conn, idStundenplan));
		if (!stundenplan.daten.zeitraster.isEmpty())
			getUnterricht(stundenplan, lehrer.id, stundenplan.daten.zeitraster);
		// Füge ggf. noch die Schüler der Klassen zu der Schülerliste hinzu
		final Set<Long> schuelerIDs = new HashSet<>();
		schuelerIDs.addAll(stundenplan.unterrichtsverteilung.klassen.stream().flatMap(k -> k.schueler.stream()).toList());
		schuelerIDs.removeAll(stundenplan.unterrichtsverteilung.schueler.stream().map(s -> s.id).toList());
		if (!schuelerIDs.isEmpty())
			stundenplan.unterrichtsverteilung.schueler.addAll(DataStundenplanSchueler.getSchueler(conn, idStundenplan).stream()
				.filter(s -> schuelerIDs.contains(s.id)).toList());
		// Füge die Pausenaufsichten des Lehrers hinzu und ergänze ggf. noch den Lehrer selbst mit seinen Fächern...
		stundenplan.pausenaufsichten = DataStundenplanPausenaufsichten.getAufsichtenVonLehrer(conn, idStundenplan, lehrer.id);
		if (stundenplan.unterrichtsverteilung.lehrer.isEmpty()) {
			stundenplan.unterrichtsverteilung.lehrer.add(lehrer);
			stundenplan.unterrichtsverteilung.faecher.addAll(DataStundenplanFaecher.getFaecher(conn, idStundenplan).stream()
					.filter(f -> lehrer.faecher.contains(f.id)).toList());
		}
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(stundenplan).build();
	}


	private void getUnterricht(final StundenplanKomplett stundenplan, final long idLehrer, final List<StundenplanZeitraster> zeitraster) {
		final @NotNull List<@NotNull StundenplanUnterricht> result = new ArrayList<>();
		final List<DTOStundenplanUnterricht> unterrichte = conn.query(
				"SELECT u FROM DTOStundenplanUnterricht u JOIN DTOStundenplanUnterrichtLehrer ul ON ul.Unterricht_ID = u.ID "
						+ "WHERE ul.Lehrer_ID = :lehrerId AND u.Zeitraster_ID IN :zrids", DTOStundenplanUnterricht.class)
				.setParameter("lehrerId", idLehrer).setParameter("zrids", zeitraster.stream().map(z -> z.id).toList()).getResultList();
		if (unterrichte.isEmpty())
			return;
		final List<Long> unterrichtIds = unterrichte.stream().map(u -> u.ID).toList();

		final Map<Long, List<StundenplanKlasse>> klassenByUnterrichtIds = DataStundenplanKlassen.getKlassenByUnterrichtIds(conn, idStundenplan, unterrichtIds);
		final Map<Long, StundenplanLehrer> lehrerById = DataStundenplanLehrer.getLehrer(conn, idStundenplan).stream().collect(Collectors.toMap(l -> l.id, Function.identity()));
		final List<DTOStundenplanUnterrichtLehrer> unterrichtLehrerList = unterrichtIds.isEmpty() ? new ArrayList<>()
				: conn.queryNamed("DTOStundenplanUnterrichtLehrer.unterricht_id.multiple", unterrichtIds, DTOStundenplanUnterrichtLehrer.class);
		final Map<Long, List<StundenplanLehrer>> lehrerByUnterrichtId = new HashMap<>();
		unterrichtLehrerList.stream().forEach(ul -> {
			final List<StundenplanLehrer> listLehrer = lehrerByUnterrichtId.computeIfAbsent(ul.Unterricht_ID, id -> new ArrayList<>());
			if (lehrerById.containsKey(ul.Lehrer_ID))
				listLehrer.add(lehrerById.get(ul.Lehrer_ID));
		});

		final Map<Long, List<StundenplanSchiene>> schienenByUnterrichtId = DataStundenplanSchienen.getSchienenByUnterrichtId(conn, idStundenplan, unterrichtIds);
		final Map<Long, List<StundenplanRaum>> raeumeByUnterrichtId = DataStundenplanRaeume.getRaeumeByUnterrichtId(conn, idStundenplan, unterrichtIds);

		final Set<Long> fachIDs = new HashSet<>();
		final Set<Long> kursIDs = new HashSet<>();
		final Set<StundenplanKlasse> setKlassen = new HashSet<>();
		final Set<StundenplanSchiene> setSchienen = new HashSet<>();
		final Set<StundenplanRaum> setRaeume = new HashSet<>();
		final Set<StundenplanLehrer> setLehrer = new HashSet<>();
		for (final DTOStundenplanUnterricht unterricht : unterrichte) {
			final StundenplanUnterricht u = new StundenplanUnterricht();
			u.id = unterricht.ID;
			u.idZeitraster = unterricht.Zeitraster_ID;
			u.wochentyp = unterricht.Wochentyp;
			u.idKurs = unterricht.Kurs_ID;
			if (u.idKurs != null)
				kursIDs.add(u.idKurs);
			u.idFach = unterricht.Fach_ID;
			fachIDs.add(u.idFach);

			final List<StundenplanKlasse> klassen = klassenByUnterrichtIds.computeIfAbsent(unterricht.ID, id -> new ArrayList<>());
			setKlassen.addAll(klassen);
			u.klassen.addAll(klassen.stream().map(k -> k.id).toList());
			final List<StundenplanSchiene> schienen = schienenByUnterrichtId.computeIfAbsent(unterricht.ID, id -> new ArrayList<>());
			setSchienen.addAll(schienen);
			u.schienen.addAll(schienen.stream().map(s -> s.id).toList());
			final List<StundenplanRaum> raeume = raeumeByUnterrichtId.computeIfAbsent(unterricht.ID, id -> new ArrayList<>());
			setRaeume.addAll(raeume);
			u.raeume.addAll(raeume.stream().map(r -> r.id).toList());
			final List<StundenplanLehrer> lehrer = lehrerByUnterrichtId.computeIfAbsent(unterricht.ID, id -> new ArrayList<>());
			setLehrer.addAll(lehrer);
			u.lehrer.addAll(lehrer.stream().map(l -> l.id).toList());
			result.add(u);
		}
		stundenplan.unterrichte.addAll(result);
		for (final StundenplanSchiene s : setSchienen)
			stundenplan.daten.schienen.add(s);
		for (final StundenplanRaum r : setRaeume)
			stundenplan.daten.raeume.add(r);
		for (final StundenplanKlasse k : setKlassen)
			stundenplan.unterrichtsverteilung.klassen.add(k);
		for (final StundenplanLehrer l : setLehrer)
			stundenplan.unterrichtsverteilung.lehrer.add(l);
		stundenplan.unterrichtsverteilung.kurse.addAll(DataStundenplanKurse.getKurse(conn, idStundenplan).stream()
				.filter(k -> kursIDs.contains(k.id)).toList());
		final Set<Long> klassenIDs = stundenplan.unterrichtsverteilung.klassen.stream().map(k -> k.id).collect(Collectors.toSet());
		stundenplan.unterrichtsverteilung.klassenunterricht.addAll(DataStundenplanKlassenunterricht.getKlassenunterrichte(conn, idStundenplan).stream()
				.filter(k -> klassenIDs.contains(k.idKlasse)).toList());
		fachIDs.addAll(stundenplan.unterrichtsverteilung.klassenunterricht.stream().map(ku -> ku.idFach).distinct().toList());
		stundenplan.unterrichtsverteilung.faecher.addAll(DataStundenplanFaecher.getFaecher(conn, idStundenplan).stream()
				.filter(f -> fachIDs.contains(f.id)).toList());
		// Füge die Kurs-Schüler hinzu und ergänze ggf. noch Klasseneinträge, die bei diesen Schülern vorkommen
		final Set<Long> schuelerIDs = new HashSet<>();
		schuelerIDs.addAll(stundenplan.unterrichtsverteilung.kurse.stream().flatMap(k -> k.schueler.stream()).toList());
		stundenplan.unterrichtsverteilung.schueler.addAll(DataStundenplanSchueler.getSchueler(conn, idStundenplan).stream()
				.filter(s -> schuelerIDs.contains(s.id)).toList());
		final Set<Long> weitereKlassenIDs = new HashSet<>();
		weitereKlassenIDs.addAll(stundenplan.unterrichtsverteilung.schueler.stream().map(s -> s.idKlasse).toList());
		weitereKlassenIDs.removeAll(stundenplan.unterrichtsverteilung.klassen.stream().map(k -> k.id).toList());
		stundenplan.unterrichtsverteilung.klassen.addAll(DataStundenplanKlassen.getKlassen(conn, idStundenplan).stream()
				.filter(k -> weitereKlassenIDs.contains(k.id)).toList());
	}


	@Override
	public Response patch(final Long idSchueler, final InputStream is) {
		throw new UnsupportedOperationException();
	}

}
