package de.svws_nrw.data.stundenplan;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.stundenplan.SchuelerStundenplan;
import de.svws_nrw.core.data.stundenplan.SchuelerStundenplanUnterricht;
import de.svws_nrw.core.data.stundenplan.StundenplanZeitraster;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLeistungsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplan;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanUnterricht;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanUnterrichtKlasse;
import jakarta.persistence.TypedQuery;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den Core-DTO
 * {@link SchuelerStundenplan}.
 */
public final class DataSchuelerStundenplan extends DataManager<Long> {

	private final Long idStundenplan;

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO
	 * {@link SchuelerStundenplan}.
	 *
	 * @param conn          die Datenbank-Verbindung für den Datenbankzugriff
	 * @param idStundenplan die ID des Stundenplans, dessen Zeitraster abgefragt
	 *                      wird
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
		final DTOSchueler schueler = conn.queryByKey(DTOSchueler.class, idSchueler);
		final DTOStundenplan stundenplan = conn.queryByKey(DTOStundenplan.class, idStundenplan);

		final List<DTOSchuelerLernabschnittsdaten> lernabschnittsdaten = conn.query(
				"SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Schuljahresabschnitts_ID = :sja AND e.Schueler_ID = :sid AND e.WechselNr IS NULL",
				DTOSchuelerLernabschnittsdaten.class).setParameter("sja", stundenplan.Schuljahresabschnitts_ID)
				.setParameter("sid", idSchueler).getResultList();
		if ((lernabschnittsdaten == null) || (lernabschnittsdaten.size() != 1))
			return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(new SchuelerStundenplan())
					.build();
		final DTOSchuelerLernabschnittsdaten lernabschnitt = lernabschnittsdaten.get(0);

		final List<StundenplanZeitraster> zeitraster = DataStundenplanZeitraster.getZeitraster(conn, idStundenplan);

		final ArrayList<SchuelerStundenplanUnterricht> spUnterricht = new ArrayList<>();

		final List<DTOSchuelerLeistungsdaten> leistungsdaten = conn.queryNamed("DTOSchuelerLeistungsdaten.abschnitt_id",
				lernabschnitt.ID, DTOSchuelerLeistungsdaten.class);
		if ((leistungsdaten == null) || (leistungsdaten.isEmpty()))
			return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(new SchuelerStundenplan())
					.build();

		final List<Long> lehrer = leistungsdaten.stream().map(ld -> ld.Fachlehrer_ID).filter(l -> l != null).toList();
		final Map<Long, DTOLehrer> mapLehrer = lehrer.isEmpty() ? new HashMap<>()
				: conn.queryNamed("DTOLehrer.id.multiple", lehrer, DTOLehrer.class).stream().collect(Collectors.toMap(l -> l.ID, l -> l));

		final List<Long> faecher = leistungsdaten.stream().map(ld -> ld.Fach_ID).filter(f -> f != null).toList();
		final Map<Long, DTOFach> mapFaecher = conn.queryNamed("DTOFach.id.multiple", faecher, DTOFach.class).stream()
				.collect(Collectors.toMap(f -> f.ID, f -> f));

		final List<Long> kursIDs = leistungsdaten.stream().map(ld -> ld.Kurs_ID).filter(k -> k != null).toList();

		final String sqlKursIDs = !kursIDs.isEmpty() ? "e.Kurs_ID IN :kids OR " : "";

		final List<DTOStundenplanUnterrichtKlasse> listSuk = conn.queryNamed("DTOStundenplanUnterrichtKlasse.klasse_id",
				lernabschnitt.Klassen_ID, DTOStundenplanUnterrichtKlasse.class);

		final String klIDs = !listSuk.isEmpty() ? "e.ID IN :klid" : "1=2";

		final TypedQuery<DTOStundenplanUnterricht> tq = conn.query(
				"SELECT e FROM DTOStundenplanUnterricht e JOIN DTOStundenplanZeitraster z ON e.Zeitraster_ID = z.ID AND z.Stundenplan_ID = :spid AND ("
						+ sqlKursIDs + klIDs + ")",
				DTOStundenplanUnterricht.class).setParameter("spid", stundenplan.ID);

		if (!listSuk.isEmpty())
			tq.setParameter("klid", listSuk.stream().map(suk -> suk.Unterricht_ID).toList());
		if (!kursIDs.isEmpty())
			tq.setParameter("kids", kursIDs);
		final Map<Long, List<DTOStundenplanUnterricht>> mapUnterricht = tq.getResultList().stream()
				.collect(Collectors.groupingBy(su -> (su.Kurs_ID != null ? su.Kurs_ID : su.Fach_ID)));

		leistungsdaten.stream().forEach(ld -> {
			final List<DTOStundenplanUnterricht> unterricht = mapUnterricht.get(ld.Kurs_ID != null ? ld.Kurs_ID : ld.Fach_ID);
			if ((unterricht == null) || (leistungsdaten.isEmpty()))
				return;
			final DTOLehrer l = mapLehrer.get(ld.Fachlehrer_ID);
			final DTOFach f = mapFaecher.get(ld.Fach_ID);
			unterricht.stream().forEach(u -> {
				final SchuelerStundenplanUnterricht ssu = new SchuelerStundenplanUnterricht();
				ssu.fachBezeichnung = (f == null) ? "" : f.Bezeichnung;
				ssu.fachKuerzel = (f == null) ? "" : f.Kuerzel;
				ssu.fachKuerzelStatistik = (f == null) ? "" : f.StatistikFach.daten.kuerzelASD;
				ssu.idFach = u.Fach_ID;
				ssu.idLehrer = ld.Fachlehrer_ID;
				ssu.idLeistungen = ld.ID;
				ssu.idUnterricht = u.ID;
				ssu.idZeitraster = u.Zeitraster_ID;
				ssu.kursart = ld.Kursart;
				ssu.lehrerKuerzel = (l == null) ? "" : l.Kuerzel;
				ssu.lehrerNachname = (l == null) ? "" : l.Nachname;
				ssu.lehrerVorname = (l == null) ? "" : l.Vorname;
				ssu.wochentyp = u.Wochentyp;
				spUnterricht.add(ssu);
			});
		});

		// Erstelle das Core-DTO-Objekt für die Response
		final SchuelerStundenplan daten = new SchuelerStundenplan();
		daten.idStundenplan = idStundenplan;
		daten.bezeichnungStundenplan = stundenplan.Beschreibung;
		daten.gueltigAb = stundenplan.Beginn;
		daten.gueltigBis = stundenplan.Ende;
		daten.idKlasse = lernabschnitt.Klassen_ID;
		daten.idSchueler = idSchueler;
		daten.idSchuljahresabschnitt = lernabschnitt.Schuljahresabschnitts_ID;
		daten.jahrgang = lernabschnitt.ASDJahrgang;
		daten.nachname = schueler.Nachname;
		daten.vorname = schueler.Vorname;
		daten.unterricht = spUnterricht;
		daten.zeitraster = zeitraster;
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response patch(final Long idSchueler, final InputStream is) {
		throw new UnsupportedOperationException();
	}

}
