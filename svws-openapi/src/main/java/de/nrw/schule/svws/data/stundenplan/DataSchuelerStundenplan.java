package de.nrw.schule.svws.data.stundenplan;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.stream.Collectors;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import de.nrw.schule.svws.core.data.stundenplan.SchuelerStundenplan;
import de.nrw.schule.svws.core.data.stundenplan.SchuelerStundenplanUnterricht;
import de.nrw.schule.svws.core.data.stundenplan.StundenplanZeitraster;
import de.nrw.schule.svws.data.DataManager;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.schild.faecher.DTOFach;
import de.nrw.schule.svws.db.dto.current.schild.lehrer.DTOLehrer;
import de.nrw.schule.svws.db.dto.current.schild.schueler.DTOSchueler;
import de.nrw.schule.svws.db.dto.current.schild.schueler.DTOSchuelerLeistungsdaten;
import de.nrw.schule.svws.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.nrw.schule.svws.db.dto.current.schild.stundenplan.DTOStundenplan;
import de.nrw.schule.svws.db.dto.current.schild.stundenplan.DTOStundenplanUnterricht;
import de.nrw.schule.svws.db.utils.OperationError;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link StundenplanZeitraster}.
 */
public class DataSchuelerStundenplan extends DataManager<Long> {

	private final Long idStundenplan;
	
	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link StundenplanZeitraster}.
	 * 
	 * @param conn            die Datenbank-Verbindung für den Datenbankzugriff
	 * @param idStundenplan   die ID des Stundenplans, dessen Zeitraster abgefragt wird
	 */
	public DataSchuelerStundenplan(DBEntityManager conn, Long idStundenplan) {
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
	public Response get(Long idSchueler) {
		DTOSchueler schueler = conn.queryByKey(DTOSchueler.class, idSchueler);
		DTOStundenplan stundenplan = conn.queryByKey(DTOStundenplan.class, idStundenplan);
		
		List<DTOSchuelerLernabschnittsdaten> lernabschnittsdaten = conn
			.query("SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Schuljahresabschnitts_ID = :sja AND e.Schueler_ID = :sid AND e.WechselNr IS NULL", DTOSchuelerLernabschnittsdaten.class)
			.setParameter("sja", stundenplan.Schuljahresabschnitts_ID)
			.setParameter("sid", idSchueler)
			.getResultList();
		if ((lernabschnittsdaten == null) || (lernabschnittsdaten.size() != 1))
			throw OperationError.NOT_FOUND.exception();
		DTOSchuelerLernabschnittsdaten lernabschnitt = lernabschnittsdaten.get(0);
		
		Vector<StundenplanZeitraster> zeitraster = (new DataStundenplanZeitraster(conn, idStundenplan)).getZeitraster();
		Map<Long, StundenplanZeitraster> mapZeitraster = zeitraster.stream().collect(Collectors.toMap(zr -> zr.id , zr -> zr));

		Vector<SchuelerStundenplanUnterricht> spUnterricht = new Vector<>();
		
		List<DTOSchuelerLeistungsdaten> leistungsdaten = conn.queryNamed("DTOSchuelerLeistungsdaten.abschnitt_id", lernabschnitt.ID, DTOSchuelerLeistungsdaten.class);
		if ((leistungsdaten == null) || (leistungsdaten.isEmpty()))
			throw OperationError.NOT_FOUND.exception();
		
		List<Long> lehrer = leistungsdaten.stream().map(ld -> ld.Fachlehrer_ID).filter(l -> l != null).toList();
		Map<Long, DTOLehrer> mapLehrer = conn.queryNamed("DTOLehrer.id.multiple", lehrer, DTOLehrer.class).stream().collect(Collectors.toMap(l -> l.ID, l -> l));

		List<Long> faecher = leistungsdaten.stream().map(ld -> ld.Fach_ID).filter(f -> f != null).toList();
		Map<Long, DTOFach> mapFaecher = conn.queryNamed("DTOFach.id.multiple", faecher, DTOFach.class).stream().collect(Collectors.toMap(f -> f.ID, f -> f));

		List<Long> kursIDs = leistungsdaten.stream().map(ld -> ld.Kurs_ID).filter(k -> k != null).toList();
		Map<Long, List<DTOStundenplanUnterricht>> mapUnterricht = conn
			.query("SELECT e FROM DTOStundenplanUnterricht e JOIN DTOStundenplanZeitraster z ON e.Zeitraster_ID = z.ID AND z.Stundenplan_ID = :spid AND (e.Kurs_ID IN :kids OR e.Klasse_ID = :klid)", DTOStundenplanUnterricht.class)
			.setParameter("spid", stundenplan.ID)
			.setParameter("kids", kursIDs)
			.setParameter("klid", lernabschnitt.Klassen_ID)
			.getResultList().stream()
			.collect(Collectors.groupingBy(su -> (su.Kurs_ID != null ? su.Kurs_ID : su.Fach_ID)));
		
		leistungsdaten.stream().forEach(ld -> {
			List<DTOStundenplanUnterricht> unterricht = mapUnterricht.get(ld.Kurs_ID != null ? ld.Kurs_ID : ld.Fach_ID);
			if ((unterricht == null) || (leistungsdaten.isEmpty()))
				// TODO Wirklich eine Exception?
				throw OperationError.NOT_FOUND.exception();
			DTOLehrer l = mapLehrer.get(ld.Fachlehrer_ID);
			DTOFach f = mapFaecher.get(ld.Fach_ID);
			unterricht.stream().forEach(u -> {
				SchuelerStundenplanUnterricht ssu = new SchuelerStundenplanUnterricht();
				ssu.fachBezeichnung = (f == null) ? "" : f.Bezeichnung;
				ssu.fachKuerzel = (f == null) ? "" : f.Kuerzel;
				ssu.fachKuerzelStatistik = (f == null) ? "" : f.StatistikFach.daten.kuerzelASD;
				ssu.idFach = u.Fach_ID;
				ssu.idLehrer = ld.Fachlehrer_ID;
				ssu.idLeistungen = ld.ID;
				ssu.idUnterricht = u.ID;
				ssu.idZeitraster = u.Zeitraster_ID;
				ssu.zeitraster = mapZeitraster.get(u.Zeitraster_ID);
				ssu.kursart = ld.Kursart;
				ssu.lehrerKuerzel = (l == null) ? "" : l.Kuerzel;
				ssu.lehrerNachname = (l == null) ? "" : l.Nachname;
				ssu.lehrerVorname = (l == null) ? "" : l.Vorname;
				ssu.wochentyp = u.Wochentyp;
				spUnterricht.add(ssu);
			});
		});

		// Erstelle das Core-DTO-Objekt für die Response
		SchuelerStundenplan daten = new SchuelerStundenplan();
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
	public Response patch(Long idSchueler, InputStream is) {
		throw new UnsupportedOperationException();
	}

		
}
