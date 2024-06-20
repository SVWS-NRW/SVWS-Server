package de.svws_nrw.data.ge;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import de.svws_nrw.core.abschluss.AbschlussManager;
import de.svws_nrw.core.data.abschluss.GEAbschlussFaecher;
import de.svws_nrw.core.types.Note;
import de.svws_nrw.core.types.ge.GELeistungsdifferenzierteKursart;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLeistungsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.utils.ApiOperationException;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link GEAbschlussFaecher}.
 */
public final class DataGEAbschlussFaecher extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link GEAbschlussFaecher}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataGEAbschlussFaecher(final DBEntityManager conn) {
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

	@Override
	public Response get(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		final DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
		if (schule == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		return getPrognoseLeistungsdaten(id, schule.Schuljahresabschnitts_ID);
	}

	/**
	 * Fragt die Leistungsdaten aus dem angegebenen Abschnitt zu der ID eines Schüler für
	 * die Abschlussberechnung an der Gesamtschule ab..
	 *
	 * @param id            die ID des Schüler, für den die Leistungsdaten ausgelesen werden sollen
	 * @param abschnittID   der Schuljahresabschnitt, für den die Leistungsdaten ausgelesen werden sollen
	 *
	 * @return die Liste der Fächer
	 * @throws ApiOperationException
	 */
	public Response getByAbschnitt(final Long id, final Long abschnittID) throws ApiOperationException {
		if ((id == null) || (abschnittID == null))
			throw new ApiOperationException(Status.NOT_FOUND);
		return getPrognoseLeistungsdaten(id, abschnittID);
	}

	@Override
	public Response patch(final Long id, final InputStream is) {
		throw new UnsupportedOperationException();
	}



	/**
	 * Ermittelt die Leistungsdaten in Bezug auf die Prognose- bzw. Abschlussberechnung in der Sek I der
	 * Gesamtschule für den Schüler mit der angegebenen ID aus der Datenbank für das angegebene Schuljahr und
	 * den angegebenen Abschnitt (Quartal oder Halbjahr)
	 *
	 * @param id           die ID des Schülers
	 * @param idAbschnitt  die ID des Schuljahresabschnittes, von dem die Leistungsdaten abgefragt werden
	 *
	 * @return die HTTP-Antwort mit den Leistungsdaten des angegebenen Lernabschnittes für den Schüler
	 *         mit der angegebenen ID oder eine HTTP-Fehlermeldung
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	private Response getPrognoseLeistungsdaten(final long id, final long idAbschnitt) throws ApiOperationException {
		// Prüft, ob der Abschnitt in der Schule definiert ist.
		final DTOSchuljahresabschnitte abschnitt = conn.queryByKey(DTOSchuljahresabschnitte.class, idAbschnitt);
		if (abschnitt == null)
			throw new ApiOperationException(Status.BAD_REQUEST);

		// Lies alle Fächer ein, damit auf die Informationen bezüglich Kürzel und Statistikkürzel bei der Auswahl der Leistungsdaten zurückgegriffen werden kann.
		final Map<Long, DTOFach> faecher = conn.queryAll(DTOFach.class).stream().collect(Collectors.toMap(f -> f.ID, f -> f));
		if (faecher == null)
			throw new ApiOperationException(Status.NOT_FOUND);

		// Lese die Schülerdaten aus, um zu prüfen, ob ein Schüler mit der angegebenen ID ind er Datenbank überhaupt existiert.
		final DTOSchueler schueler = conn.queryByKey(DTOSchueler.class, id);
		if (schueler == null)
			throw new ApiOperationException(Status.NOT_FOUND);

		// Bestimme den Lernabschnitt anhand des angegebenen Schuljahres und des Abschnittes im Schuljahr
		final DTOSchuelerLernabschnittsdaten lernabschnitt =
				conn.queryList(DTOSchuelerLernabschnittsdaten.QUERY_BY_SCHUELER_ID, DTOSchuelerLernabschnittsdaten.class, id)
						.stream().filter(l -> (l.Schuljahresabschnitts_ID == idAbschnitt)).findFirst().orElse(null);
		if (lernabschnitt == null)
			throw new ApiOperationException(Status.NOT_FOUND);

		// Bestimme die Leistungsdaten aus dem Lernabschnitt
		final List<DTOSchuelerLeistungsdaten> leistungen =
				conn.queryList(DTOSchuelerLeistungsdaten.QUERY_BY_ABSCHNITT_ID, DTOSchuelerLeistungsdaten.class, lernabschnitt.ID);
		if ((leistungen == null) || (leistungen.isEmpty()))
			throw new ApiOperationException(Status.NOT_FOUND);

		final GEAbschlussFaecher daten = new GEAbschlussFaecher();
		daten.schuljahr = abschnitt.Jahr;
		daten.abschnitt = abschnitt.Abschnitt;
		daten.jahrgang = lernabschnitt.ASDJahrgang;
		daten.faecher = leistungen.stream()
				.filter(l -> (l.AufZeugnis != null) && (l.AufZeugnis))
				.map(l -> {
					final DTOFach fach = faecher.get(l.Fach_ID);
					if (fach == null)
						return null;
					final GELeistungsdifferenzierteKursart kursart = "E".equals(l.Kursart) ? GELeistungsdifferenzierteKursart.E
							: ("G".equals(l.Kursart) ? GELeistungsdifferenzierteKursart.G : GELeistungsdifferenzierteKursart.Sonstige);
					final boolean istFremdsprache = (fach.IstFremdsprache != null) && fach.IstFremdsprache;
					final Note note = l.NotenKrz;
					if (note == null)
						return null;
					final Integer noteSekI = note.getNoteSekI();
					if (noteSekI == null)
						return null;
					// Erkenne das WP-Fach anhand der Kursart bei den Leistungsdaten
					if ("WPI".equals(l.KursartAllg)) {
						return AbschlussManager.erstelleAbschlussFach("WP", fach.Kuerzel, noteSekI, kursart, istFremdsprache);
					}
					String kuerzel = fach.StatistikFach.daten.kuerzelASD;
					if ("E5".equals(kuerzel))
						kuerzel = "E";
					if ((("M".equals(kuerzel)) || ("D".equals(kuerzel)) || ("E".equals(kuerzel))) && (kursart == GELeistungsdifferenzierteKursart.Sonstige))
						return null;
					return AbschlussManager.erstelleAbschlussFach(kuerzel, fach.Kuerzel, noteSekI, kursart, istFremdsprache);
				})
				.filter(l -> l != null)
				.collect(Collectors.toList());

		// Füge die Lernabschnittsnoten hinzu - sofern sie definiert wurden
		final Integer noteNW = (lernabschnitt.Gesamtnote_NW == null) ? null : lernabschnitt.Gesamtnote_NW.getNoteSekI();
		if (noteNW != null)
			daten.faecher.add(AbschlussManager.erstelleAbschlussFach("LBNW", "LBNW", noteNW, GELeistungsdifferenzierteKursart.Sonstige, false));
		final Integer noteAL = (lernabschnitt.Gesamtnote_GS == null) ? null : lernabschnitt.Gesamtnote_GS.getNoteSekI();
		if (noteAL != null)
			daten.faecher.add(AbschlussManager.erstelleAbschlussFach("LBAL", "LBAL", noteAL, GELeistungsdifferenzierteKursart.Sonstige, false));
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

}
