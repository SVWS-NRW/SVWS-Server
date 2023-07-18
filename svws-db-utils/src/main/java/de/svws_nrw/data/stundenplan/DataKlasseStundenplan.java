package de.svws_nrw.data.stundenplan;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.stundenplan.KlasseStundenplan;
import de.svws_nrw.core.data.stundenplan.KlasseStundenplanUnterricht;
import de.svws_nrw.core.data.stundenplan.StundenplanKlasse;
import de.svws_nrw.core.data.stundenplan.StundenplanLehrer;
import de.svws_nrw.core.data.stundenplan.StundenplanRaum;
import de.svws_nrw.core.data.stundenplan.StundenplanSchiene;
import de.svws_nrw.core.data.stundenplan.StundenplanZeitraster;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKurs;
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
 * {@link KlasseStundenplan}.
 */
public final class DataKlasseStundenplan extends DataManager<Long> {

	private final Long idStundenplan;

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO
	 * {@link KlasseStundenplan}.
	 *
	 * @param conn          die Datenbank-Verbindung für den Datenbankzugriff
	 * @param idStundenplan die ID des Stundenplans, dessen Zeitraster abgefragt
	 *                      wird
	 */
	public DataKlasseStundenplan(final DBEntityManager conn, final Long idStundenplan) {
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
		final KlasseStundenplan result = new KlasseStundenplan();
		final DTOStundenplan stundenplan = conn.queryByKey(DTOStundenplan.class, idStundenplan);
		if (stundenplan == null)
			throw new NotFoundException("Kein Stundenplan mit angegebener ID gefunden");
		final DTOKlassen klasse = conn.queryByKey(DTOKlassen.class, id);
		if (klasse == null)
			throw new NotFoundException("Kein Klasse mit angegebener ID gefunden");
		result.idStundenplan = stundenplan.ID;
		result.idSchuljahresabschnitt = stundenplan.Schuljahresabschnitts_ID;
		result.bezeichnungStundenplan = stundenplan.Beschreibung;
		result.gueltigAb = stundenplan.Beginn;
		result.gueltigBis = stundenplan.Ende;
		result.idKlasse = klasse.ID;
		result.klasseName = klasse.Klasse;
		result.zeitraster = DataStundenplanZeitraster.getZeitraster(conn, idStundenplan);
		result.unterricht = getUnterricht(klasse, result.zeitraster);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(result).build();
	}

	private @NotNull List<@NotNull KlasseStundenplanUnterricht> getUnterricht(final DTOKlassen klasse, final List<StundenplanZeitraster> zeitraster) {
		final @NotNull List<@NotNull KlasseStundenplanUnterricht> result = new ArrayList<>();
		if (zeitraster == null || zeitraster.isEmpty())
			return result;
		final List<Long> zeitrasterIds = zeitraster.stream().map(z -> z.id).toList();
		final List<DTOStundenplanUnterricht> unterrichte = conn.query(
				"SELECT DISTINCT u FROM DTOStundenplanUnterricht u LEFT JOIN DTOStundenplanUnterrichtKlasse uk ON uk.Unterricht_ID = u.ID "
						+ "LEFT JOIN DTOKurs kurs ON u.Kurs_ID = kurs.ID "
						+ "LEFT JOIN DTOKlassen klasse ON klasse.Jahrgang_ID = kurs.Jahrgang_ID "
						+ "WHERE (uk.Klasse_ID = :klasseId OR klasse.ID = :klasseId2) "
						+ "AND u.Zeitraster_ID IN :zrids", DTOStundenplanUnterricht.class)
				.setParameter("klasseId", klasse.ID).setParameter("klasseId2", klasse.ID)
				.setParameter("zrids", zeitrasterIds).getResultList();
		final List<Long> unterrichtIds = unterrichte.stream().map(u -> u.ID).toList();
		final Map<Long, DTOFach> fachById = conn.queryNamed("DTOFach.all", DTOFach.class).getResultList().stream()
				.collect(Collectors.toMap(f -> f.ID, Function.identity()));

		final List<Long> kursIds = unterrichte.stream().filter(u -> u.Kurs_ID != null).map(u -> u.Kurs_ID).toList();
		final Map<Long, DTOKurs> kursById;
		if (kursIds != null && !kursIds.isEmpty()) {
			kursById = conn.queryNamed("DTOKurs.id.multiple", kursIds, DTOKurs.class).stream()
					.collect(Collectors.toMap(k -> k.ID, Function.identity()));
		} else {
			kursById = new HashMap<>();
		}

		final Map<Long, List<StundenplanKlasse>> klassenByUnterrichtIds = DataStundenplanKlassen
				.getKlassenByUnterrichtIds(conn, idStundenplan, unterrichtIds);
		final Map<Long, StundenplanLehrer> lehrerById = DataStundenplanLehrer.getLehrer(conn, idStundenplan).stream()
				.collect(Collectors.toMap(l -> l.id, Function.identity()));
		final List<DTOStundenplanUnterrichtLehrer> unterrichtLehrerList = unterrichtIds.isEmpty() ? new ArrayList<>()
				: conn.queryNamed("DTOStundenplanUnterrichtLehrer.unterricht_id.multiple", unterrichtIds, DTOStundenplanUnterrichtLehrer.class);
		final Map<Long, List<StundenplanLehrer>> lehrerByUnterrichtId = new HashMap<>();
		unterrichtLehrerList.stream().forEach(ul -> {
			final List<StundenplanLehrer> listLehrer = lehrerByUnterrichtId.computeIfAbsent(ul.Unterricht_ID, id -> new ArrayList<>());
			if (lehrerById.containsKey(ul.Lehrer_ID))
				listLehrer.add(lehrerById.get(ul.Lehrer_ID));
		});

		final Map<Long, List<StundenplanSchiene>> schienenByUnterrichtId = DataStundenplanSchienen
				.getSchienenByUnterrichtId(conn, idStundenplan, unterrichtIds);
		final Map<Long, List<StundenplanRaum>> raeumeByUnterrichtId = DataStundenplanRaeume
				.getRaeumeByUnterrichtId(conn, idStundenplan, unterrichtIds);

		for (final var unterricht : unterrichte) {
			final KlasseStundenplanUnterricht u = new KlasseStundenplanUnterricht();
			final DTOFach f = fachById.get(unterricht.Fach_ID);
			if (f != null) {
				u.fachBezeichnung = f.Bezeichnung;
				u.fachKuerzel = f.Kuerzel;
				u.fachKuerzelStatistik = f.StatistikFach.daten.kuerzel;
				u.idFach = unterricht.Fach_ID;
			}
			u.idUnterricht = unterricht.ID;
			u.idZeitraster = unterricht.Zeitraster_ID;
			u.wochentyp = unterricht.Wochentyp;

			if (kursById.containsKey(unterricht.Kurs_ID))
				u.kursart = kursById.get(unterricht.Kurs_ID).KursartAllg;
			u.klassen = klassenByUnterrichtIds.computeIfAbsent(unterricht.ID, id -> new ArrayList<>());
			u.schiene = schienenByUnterrichtId.computeIfAbsent(unterricht.ID, id -> new ArrayList<>());
			u.unterrichtsraeume = raeumeByUnterrichtId.computeIfAbsent(unterricht.ID, id -> new ArrayList<>());
			u.lehrer = lehrerByUnterrichtId.computeIfAbsent(unterricht.ID, id -> new ArrayList<>());
			result.add(u);
		}
		return result;
	}

	@Override
	public Response patch(final Long idSchueler, final InputStream is) {
		throw new UnsupportedOperationException();
	}
}
