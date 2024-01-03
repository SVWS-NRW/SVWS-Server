package de.svws_nrw.data.klassen;

import de.svws_nrw.core.data.klassen.KlassenDaten;
import de.svws_nrw.core.types.klassen.Klassenart;
import de.svws_nrw.core.types.schule.AllgemeinbildendOrganisationsformen;
import de.svws_nrw.core.types.schule.BerufskollegOrganisationsformen;
import de.svws_nrw.core.types.schule.Schulform;
import de.svws_nrw.core.types.schule.Schulgliederung;
import de.svws_nrw.core.types.schule.WeiterbildungskollegOrganisationsformen;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.schueler.DataSchuelerliste;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassenLeitung;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.utils.OperationError;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link KlassenDaten}.
 */
public final class DataKlassendaten extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link KlassenDaten}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataKlassendaten(final DBEntityManager conn) {
		super(conn);
	}


	/**
	 * Mapper zum Erstellen des Core-DTOs aus den Datenbank-DTOs zu den Klassen, Klassenleitungen und Schülern.
	 *
	 * @param schulform          die Schulform
	 * @param klasse			 Klasse
	 * @param klassenLeitungen	 Leitungen der Klasse
	 * @param schueler			 Schüler der Klasse
	 * @param vorgaengerklasse   die Vorgängerklasse, sofern in der DB vorhanden
	 * @param folgeklasse        die Folgeklasse, sofern in der DB bereits angelegt
	 *
	 * @return Core-DTO mit allen Klasseninformationen
	 */
	private static KlassenDaten dtoMapper(final Schulform schulform, final DTOKlassen klasse, final List<DTOKlassenLeitung> klassenLeitungen,
			final List<DTOSchueler> schueler, final DTOKlassen vorgaengerklasse, final DTOKlassen folgeklasse) {
		final KlassenDaten daten = new KlassenDaten();
		daten.id = klasse.ID;
		daten.idSchuljahresabschnitt = klasse.Schuljahresabschnitts_ID;
		daten.kuerzel = klasse.Klasse;
		daten.idJahrgang = klasse.Jahrgang_ID;
		daten.parallelitaet = klasse.ASDKlasse.length() < 3 ? null : klasse.ASDKlasse.substring(2, 3);
		daten.sortierung = klasse.Sortierung;
		daten.istSichtbar = klasse.Sichtbar;
		if (klassenLeitungen != null)
			for (final DTOKlassenLeitung kl : klassenLeitungen)
				daten.klassenLeitungen.add(kl.Lehrer_ID);
		for (final DTOSchueler dto : schueler)
			daten.schueler.add(DataSchuelerliste.mapToSchueler.apply(dto));
		daten.teilstandort = klasse.AdrMerkmal == null ? "" : klasse.AdrMerkmal;
		daten.beschreibung = klasse.Bezeichnung == null ? "" : klasse.Bezeichnung;
		daten.idAllgemeinbildenOrganisationsform = AllgemeinbildendOrganisationsformen.getByKuerzel(klasse.OrgFormKrz) == null
				? null : AllgemeinbildendOrganisationsformen.getByKuerzel(klasse.OrgFormKrz).daten.id;
		daten.idBerufsbildendOrganisationsform = BerufskollegOrganisationsformen.getByKuerzel(klasse.OrgFormKrz) == null
				? null : BerufskollegOrganisationsformen.getByKuerzel(klasse.OrgFormKrz).daten.id;
		daten.idWeiterbildungOrganisationsform = WeiterbildungskollegOrganisationsformen.getByKuerzel(klasse.OrgFormKrz) == null
				? null : WeiterbildungskollegOrganisationsformen.getByKuerzel(klasse.OrgFormKrz).daten.id;
		daten.pruefungsordnung = klasse.PruefOrdnung;
		final Schulgliederung gliederung = Schulgliederung.getBySchulformAndKuerzel(schulform, klasse.ASDSchulformNr);
		if (gliederung == null)
			throw OperationError.INTERNAL_SERVER_ERROR.exception("Kann für die Schulform %s keine gültige Schulgliederung ermitteln.".formatted(schulform.toString()));
		daten.idSchulgliederung = gliederung.daten.id;
		final Klassenart klassenart = Klassenart.getByKuerzel(klasse.Klassenart);
		daten.idKlassenart = ((klassenart != null) && (klassenart.hasSchulform(schulform))) ? klassenart.daten.id : Klassenart.UNDEFINIERT.daten.id;
		daten.noteneingabeGesperrt = klasse.NotenGesperrt != null && klasse.NotenGesperrt;
		daten.verwendungAnkreuzkompetenzen = klasse.Ankreuzzeugnisse != null && klasse.Ankreuzzeugnisse;
		daten.idVorgaengerklasse = (vorgaengerklasse == null) ? null : vorgaengerklasse.ID;
		daten.kuerzelVorgaengerklasse = (vorgaengerklasse == null) ? klasse.VKlasse : vorgaengerklasse.Klasse;
		daten.idFolgeklasse = (folgeklasse == null) ? null : folgeklasse.ID;
		daten.kuerzelFolgeklasse = (folgeklasse == null) ? klasse.FKlasse : folgeklasse.Klasse;
		daten.idFachklasse = klasse.Fachklasse_ID;
		daten.beginnSommersemester = klasse.SommerSem != null && klasse.SommerSem;
		return daten;
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
		final KlassenDaten daten = getFromID(id);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	private KlassenDaten getFromIDInternal(final Long id, final List<DTOSchueler> dtoSchueler) throws WebApplicationException {
		final DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
		if (schule == null)
			throw OperationError.NOT_FOUND.exception("Konnte die Informationen zur Schule nicht einlesen");
		final DTOKlassen klasse = conn.queryByKey(DTOKlassen.class, id);
		if (klasse == null)
			throw OperationError.NOT_FOUND.exception("Keine Klasse zur ID " + id + " gefunden.");
		final List<DTOKlassenLeitung> klassenLeitungen = conn.queryNamed("DTOKlassenLeitung.klassen_id", klasse.ID, DTOKlassenLeitung.class);
		// Bestimme den Schuljahresabschnitt der Klasse
		final DTOSchuljahresabschnitte schuljahresabschnitt = conn.queryByKey(DTOSchuljahresabschnitte.class, klasse.Schuljahresabschnitts_ID);
		if (schuljahresabschnitt == null)
			throw OperationError.NOT_FOUND.exception("Der Schuljahresabschnitt mit der ID %d für die Klasse mit der ID %d wurde nicht gefunden.".formatted(klasse.Schuljahresabschnitts_ID, klasse.ID));
		// Bestimme die Vorgänger-Klasse, falls eine angegeben ist
		DTOKlassen vorgaengerklasse = null;
		if ((klasse.VKlasse != null) && (schuljahresabschnitt.VorigerAbschnitt_ID != null)) {
			final List<DTOKlassen> listKlassen = conn.queryList("SELECT e FROM DTOKlassen e WHERE e.Schuljahresabschnitts_ID = ?1 AND e.Klasse = ?2", DTOKlassen.class, schuljahresabschnitt.VorigerAbschnitt_ID, klasse.VKlasse);
			if (listKlassen.size() == 1)
				vorgaengerklasse = listKlassen.get(0);
		}
		// Bestimme die Folge-Klasse, falls eine angegeben ist
		DTOKlassen folgeklasse = null;
		if ((klasse.FKlasse != null) && (schuljahresabschnitt.FolgeAbschnitt_ID != null)) {
			final List<DTOKlassen> listKlassen = conn.queryList("SELECT e FROM DTOKlassen e WHERE e.Schuljahresabschnitts_ID = ?1 AND e.Klasse = ?2", DTOKlassen.class, schuljahresabschnitt.FolgeAbschnitt_ID, klasse.FKlasse);
			if (listKlassen.size() == 1)
				folgeklasse = listKlassen.get(0);
		}
		// Erstelle das Core-DTO-Objekt für die Klasse
		return dtoMapper(schule.Schulform, klasse, klassenLeitungen, dtoSchueler, vorgaengerklasse, folgeklasse);
	}


	/**
	 * Gibt die Daten einer Klasse zu deren ID zurück.
	 *
	 * @param id	Die ID der Klasse.
	 * @return		Die Daten der KLasse zur ID.
	 *
	 * @throws WebApplicationException im Fehlerfall
	 */
	public KlassenDaten getFromID(final Long id) throws WebApplicationException {
		if (id == null)
			throw OperationError.NOT_FOUND.exception("Keine ID für die Klasse übergeben.");
		// Bestimme die Schüler der Klasse
		final List<Long> schuelerIDs = conn.queryNamed("DTOSchuelerLernabschnittsdaten.klassen_id", id, DTOSchuelerLernabschnittsdaten.class)
			.stream().filter(sla -> sla.WechselNr == 0).map(sla -> sla.Schueler_ID).toList();
		final List<DTOSchueler> dtoSchueler = schuelerIDs == null || schuelerIDs.isEmpty() ? new ArrayList<>()
			: conn.queryNamed("DTOSchueler.id.multiple", schuelerIDs, DTOSchueler.class);
		// Erstelle das Core-DTO-Objekt für die Klasse
		return getFromIDInternal(id, dtoSchueler);
	}


	/**
	 * Gibt die Daten einer Klasse zu deren ID ohne Schülerliste zurück.
	 *
	 * @param id	Die ID der Klasse.
	 * @return		Die Daten der KLasse zur ID ohne Schülerliste.
	 *
	 * @throws WebApplicationException im Fehlerfall
	 */
	public KlassenDaten getFromIDOhneSchueler(final Long id) throws WebApplicationException {
		if (id == null)
			throw OperationError.NOT_FOUND.exception("Keine ID für die Klasse übergeben.");
		// Erstelle das Core-DTO-Objekt für die Klasse
		return getFromIDInternal(id, Collections.emptyList());
	}


	@Override
	public Response patch(final Long id, final InputStream is) {
		throw new UnsupportedOperationException();
	}

}
