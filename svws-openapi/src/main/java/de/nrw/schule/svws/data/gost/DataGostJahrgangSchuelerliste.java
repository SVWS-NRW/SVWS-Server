package de.nrw.schule.svws.data.gost;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import de.nrw.schule.svws.core.data.schueler.SchuelerListeEintrag;
import de.nrw.schule.svws.core.types.statkue.Schulform;
import de.nrw.schule.svws.data.DataManager;
import de.nrw.schule.svws.data.schueler.DataSchuelerliste;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.schild.schueler.DTOSchueler;
import de.nrw.schule.svws.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.nrw.schule.svws.db.dto.current.schild.schule.DTOEigeneSchule;
import de.nrw.schule.svws.db.dto.current.views.gost.DTOViewGostSchuelerAbiturjahrgang;
import de.nrw.schule.svws.db.utils.OperationError;
import jakarta.persistence.TypedQuery;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link SchuelerListeEintrag} für die Schüler eines Abiturjahrgangs 
 * der gymnasialen Oberstufe.
 */
public class DataGostJahrgangSchuelerliste extends DataManager<Integer> {

	/** der Abiturjahrgang */
	protected Integer abijahrgang;
	
	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link SchuelerListeEintrag}.
	 * 
	 * @param conn          die Datenbank-Verbindung für den Datenbankzugriff
	 * @param abijahrgang   der Abiturjahrgang 
	 */
	public DataGostJahrgangSchuelerliste(DBEntityManager conn, Integer abijahrgang) {
		super(conn);
		this.abijahrgang = abijahrgang;
	}


	@Override
	public Response getAll() {
		DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
		if (schule == null)
    		return OperationError.NOT_FOUND.getResponse();
    	Schulform schulform = schule.Schulform;
    	if ((schulform == null) || (schulform.daten == null) || (!schulform.daten.hatGymOb))
    		return OperationError.NOT_FOUND.getResponse();

    	// Bestimme alle Schüler-IDs für den Abiturjahrgang der Blockung
		List<DTOViewGostSchuelerAbiturjahrgang> schuelerAbijahrgang = conn.queryNamed("DTOViewGostSchuelerAbiturjahrgang.abiturjahr", abijahrgang, DTOViewGostSchuelerAbiturjahrgang.class);
		if ((schuelerAbijahrgang == null) || (schuelerAbijahrgang.size() == 0))
			return OperationError.NOT_FOUND.getResponse();
		List<Long> schuelerIDs = schuelerAbijahrgang.stream().map(s -> s.ID).collect(Collectors.toList());
		List<DTOSchueler> schuelerListe = conn.queryNamed("DTOSchueler.id.multiple", schuelerIDs, DTOSchueler.class);
		if ((schuelerListe == null) || (schuelerListe.size() == 0))
			return OperationError.NOT_FOUND.getResponse();

    	// Bestimme die aktuellen Lernabschnitte für die Schüler
		TypedQuery<DTOSchuelerLernabschnittsdaten> queryDtoSchuelerLernabschnitte = conn.query(
				"SELECT l FROM DTOSchueler s JOIN DTOSchuelerLernabschnittsdaten l ON "
				+ "s.ID IN :value AND s.ID = l.Schueler_ID AND s.Schuljahresabschnitts_ID = l.Schuljahresabschnitts_ID AND l.WechselNr IS NULL", DTOSchuelerLernabschnittsdaten.class
		);
		Map<Long, DTOSchuelerLernabschnittsdaten> mapAktAbschnitte = queryDtoSchuelerLernabschnitte
				.setParameter("value", schuelerIDs)
				.getResultList().stream().collect(Collectors.toMap(l -> l.Schueler_ID, l -> l));

		// Erstelle die Schülerliste
    	List<SchuelerListeEintrag> daten = schuelerListe.stream()
        		.map(s -> DataSchuelerliste.erstelleSchuelerlistenEintrag(s, mapAktAbschnitte.get(s.ID)))
        		.sorted(DataSchuelerliste.dataComparator)
    	    	.collect(Collectors.toList());
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response getList() {
		return this.getAll();
	}

	@Override
	public Response get(Integer id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response patch(Integer id, InputStream is) {
		throw new UnsupportedOperationException();
	}
	
}
