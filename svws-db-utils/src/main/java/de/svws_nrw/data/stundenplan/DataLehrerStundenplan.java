package de.svws_nrw.data.stundenplan;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.stundenplan.LehrerStundenplan;
import de.svws_nrw.core.data.stundenplan.LehrerStundenplanUnterricht;
import de.svws_nrw.core.data.stundenplan.StundenplanKlasse;
import de.svws_nrw.core.data.stundenplan.StundenplanPausenaufsicht;
import de.svws_nrw.core.data.stundenplan.StundenplanRaum;
import de.svws_nrw.core.data.stundenplan.StundenplanSchiene;
import de.svws_nrw.core.data.stundenplan.StundenplanZeitraster;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKurs;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
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
 * {@link LehrerStundenplan}.
 */
public final class DataLehrerStundenplan extends DataManager<Long> {

	private final Long idStundenplan;
	private final DataStundenplanPausenaufsichten dataPausenaufsichten;

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO
	 * {@link LehrerStundenplan}.
	 *
	 * @param conn          die Datenbank-Verbindung für den Datenbankzugriff
	 * @param idStundenplan die ID des Stundenplans, dessen Zeitraster abgefragt
	 *                      wird
	 */
	public DataLehrerStundenplan(final DBEntityManager conn, final Long idStundenplan) {
		super(conn);
		this.idStundenplan = idStundenplan;
		this.dataPausenaufsichten = new DataStundenplanPausenaufsichten(conn, idStundenplan);
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
		final LehrerStundenplan result = new LehrerStundenplan();
		final DTOStundenplan stundenplan = conn.queryByKey(DTOStundenplan.class, idStundenplan);
		if (stundenplan == null)
			throw new NotFoundException("Kein Stundenplan mit angegebener ID gefunden");
		final DTOLehrer lehrer = conn.queryByKey(DTOLehrer.class, id);
		if (lehrer == null)
			throw new NotFoundException("Kein Lehrer mit angegebener ID gefunden");
		result.idStundenplan = stundenplan.ID;
		result.idSchuljahresabschnitt = stundenplan.Schuljahresabschnitts_ID;
		result.bezeichnungStundenplan = stundenplan.Beschreibung;
		result.gueltigAb = stundenplan.Beginn;
		result.gueltigBis = ifNullEmptyString(stundenplan.Ende);
		result.idLehrer = lehrer.ID;
		result.nachname = lehrer.Nachname;
		result.vorname = ifNullEmptyString(lehrer.Vorname);
		result.zeitraster = DataStundenplanZeitraster.getZeitraster(conn, idStundenplan);
		result.pausenzeiten = DataStundenplanPausenzeiten.getPausenzeiten(conn, idStundenplan);
		result.unterricht = getUnterricht(lehrer.ID, result.zeitraster);
		result.pausenaufsichten = getPausenaufsichten(lehrer.ID);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(result).build();
	}

	private @NotNull List<@NotNull StundenplanPausenaufsicht> getPausenaufsichten(final Long lehrerId) {
		return this.dataPausenaufsichten.getAufsichten(idStundenplan).stream()
				.filter(pa -> (pa != null) && (pa.idLehrer == lehrerId)).toList();
	}

	private @NotNull List<@NotNull LehrerStundenplanUnterricht> getUnterricht(final long lehrerId, final List<StundenplanZeitraster> zeitraster) {
		final @NotNull List<@NotNull LehrerStundenplanUnterricht> result = new ArrayList<>();
		if (zeitraster == null || zeitraster.isEmpty())
			return result;
		final List<DTOStundenplanUnterricht> unterrichte = conn.query(
				"SELECT u FROM DTOStundenplanUnterricht u JOIN DTOStundenplanUnterrichtLehrer ul ON ul.Unterricht_ID = u.ID "
						+ "WHERE ul.Lehrer_ID = :lehrerId AND u.Zeitraster_ID IN :zrids", DTOStundenplanUnterricht.class)
				.setParameter("lehrerId", lehrerId).setParameter("zrids", zeitraster.stream().map(z -> z.id).toList()).getResultList();
		if (unterrichte == null || unterrichte.isEmpty())
			return result;
		final List<Long> unterrichtIds = unterrichte.stream().map(u -> u.ID).toList();
		final Map<Long, DTOFach> fachById = conn.queryNamed("DTOFach.all", DTOFach.class).getResultList().stream()
				.collect(Collectors.toMap(f -> f.ID, Function.identity()));

		final List<Long> kursIds = unterrichte.stream().map(u -> u.Kurs_ID).toList();
		final List<DTOKurs> kurse = conn.queryNamed("DTOKurs.id.multiple", kursIds, DTOKurs.class);
		if (kurse == null)
			return result;
		final Map<Long, DTOKurs> kursById = kurse.stream().collect(Collectors.toMap(k -> k.ID, Function.identity()));

		final Map<Long, List<StundenplanKlasse>> klassenByUnterrichtIds = DataStundenplanKlassen
				.getKlassenByUnterrichtIds(conn, idStundenplan, unterrichtIds);
		final List<DTOStundenplanUnterrichtLehrer> unterrichtLehrerList = conn
				.queryNamed("DTOStundenplanUnterrichtLehrer.lehrer_id", lehrerId, DTOStundenplanUnterrichtLehrer.class);
		if (unterrichtLehrerList == null || unterrichtLehrerList.isEmpty())
			return result;

		final Map<Long, List<StundenplanSchiene>> schienenByUnterrichtId = DataStundenplanSchienen
				.getSchienenByUnterrichtId(conn, idStundenplan, unterrichtIds);

		final Map<Long, List<StundenplanRaum>> raeumeByUnterrichtId = DataStundenplanRaeume.getRaeumeByUnterrichtId(conn,
				idStundenplan, unterrichtIds);

		for (final var unterricht : unterrichte) {
			final LehrerStundenplanUnterricht u = new LehrerStundenplanUnterricht();
			final DTOFach f = fachById.get(unterricht.Fach_ID);
			if (f != null) {
				u.fachBezeichnung = f.Bezeichnung;
				u.fachKuerzel = f.Kuerzel;
				u.fachKuerzelStatistik = f.StatistikFach.daten.kuerzel;
				u.idFach = unterricht.Fach_ID;
			}
			u.idUnterricht = unterricht.ID;
			u.idZeitraster = unterricht.Zeitraster_ID;
			u.klassen = klassenByUnterrichtIds.computeIfAbsent(unterricht.ID, id -> new ArrayList<>());
			if (kursById.containsKey(unterricht.Kurs_ID))
				u.kursart = kursById.get(unterricht.Kurs_ID).KursartAllg;
			u.schienen = schienenByUnterrichtId.computeIfAbsent(unterricht.ID, id -> new ArrayList<>());
			u.unterrichtsraeume = raeumeByUnterrichtId.computeIfAbsent(unterricht.ID, id -> new ArrayList<>());
			u.wochentyp = unterricht.Wochentyp;
			result.add(u);
		}
		return result;
	}

	@Override
	public Response patch(final Long idSchueler, final InputStream is) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Util um @NonNull-Werten leere Strings anstatt eines null zuzuweisen. Gibt
	 * param zurück, wenn param != null ist, sonst einen leeren String
	 *
	 * @param param der String, der auf Null geprüft werden soll
	 * @return param == null ? "" : param
	 */
	public static String ifNullEmptyString(final String param) {
		return param == null ? "" : param;
	}

}
