package de.svws_nrw.data.gost;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.schueler.SchuelerListeEintrag;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.schueler.DataSchuelerliste;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.persistence.TypedQuery;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link SchuelerListeEintrag} für die Schüler eines Abiturjahrgangs
 * der gymnasialen Oberstufe.
 */
public final class DataGostJahrgangSchuelerliste extends DataManager<Integer> {

	/** der Abiturjahrgang */
	protected Integer abijahrgang;

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link SchuelerListeEintrag}.
	 *
	 * @param conn          die Datenbank-Verbindung für den Datenbankzugriff
	 * @param abijahrgang   der Abiturjahrgang
	 */
	public DataGostJahrgangSchuelerliste(final DBEntityManager conn, final Integer abijahrgang) {
		super(conn);
		this.abijahrgang = abijahrgang;
	}


	/**
	 * Ermittelt die Schüler des Abiturjahrgangs
	 *
	 * @return die Liste der Schüler
	 *
	 * @throws ApiOperationException   für den Fall, dass keine Schüler für den Abiturjahrgang bestimmt werden konnten
	 */
	List<DTOSchueler> getSchuelerDTOs() throws ApiOperationException {
		if (abijahrgang == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		return DBUtilsGostLaufbahn.getSchuelerOfAbiturjahrgang(conn, abijahrgang);
	}

	/**
	 * Ermittelt die Schüler des Abiturjahrgangs
	 *
	 * @return die Liste der Schüler
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public List<SchuelerListeEintrag> getAllSchueler() throws ApiOperationException {
		final DTOEigeneSchule schule = DBUtilsGost.pruefeSchuleMitGOSt(conn);

		// Bestimme alle Schüler-IDs für den Abiturjahrgang der Blockung
		final List<DTOSchueler> schuelerListe = getSchuelerDTOs();
		if (schuelerListe.isEmpty())
			return new ArrayList<>();
		final List<Long> schuelerIDs = schuelerListe.stream().map(s -> s.ID).toList();

		// Bestimme die aktuellen Lernabschnitte für die Schüler
		final TypedQuery<DTOSchuelerLernabschnittsdaten> queryDtoSchuelerLernabschnitte = conn.query(
				"SELECT l FROM DTOSchueler s JOIN DTOSchuelerLernabschnittsdaten l ON "
						+ "s.ID IN :value AND s.ID = l.Schueler_ID AND s.Schuljahresabschnitts_ID = l.Schuljahresabschnitts_ID AND l.WechselNr = 0",
				DTOSchuelerLernabschnittsdaten.class);
		final Map<Long, DTOSchuelerLernabschnittsdaten> mapAktAbschnitte = queryDtoSchuelerLernabschnitte
				.setParameter("value", schuelerIDs)
				.getResultList().stream().collect(Collectors.toMap(l -> l.Schueler_ID, l -> l));

		// Bestimme die Jahrgänge der Schule
		final Map<Long, DTOJahrgang> mapJahrgaenge = conn.queryAll(DTOJahrgang.class).stream().collect(Collectors.toMap(j -> j.ID, j -> j));

		// Erstelle die Schülerliste
		return schuelerListe.stream()
				.map(s -> DataSchuelerliste.erstelleSchuelerlistenEintrag(s, mapAktAbschnitte.get(s.ID), mapJahrgaenge, schule.Schulform))
				.sorted(DataSchuelerliste.dataComparator)
				.toList();
	}


	@Override
	public Response getAll() throws ApiOperationException {
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(getAllSchueler()).build();
	}

	@Override
	public Response getList() throws ApiOperationException {
		return this.getAll();
	}

	@Override
	public Response get(final Integer id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response patch(final Integer id, final InputStream is) {
		throw new UnsupportedOperationException();
	}

}
