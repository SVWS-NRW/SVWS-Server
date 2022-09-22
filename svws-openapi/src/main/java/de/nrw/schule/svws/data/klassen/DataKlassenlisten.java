package de.nrw.schule.svws.data.klassen;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import de.nrw.schule.svws.core.data.klassen.KlassenListeEintrag;
import de.nrw.schule.svws.data.DataManager;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.schild.klassen.DTOKlassen;
import de.nrw.schule.svws.db.dto.current.schild.klassen.DTOKlassenLeitung;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link KlassenListeEintrag}.
 */
public class DataKlassenlisten extends DataManager<Long> {

	private final long abschnitt; 
	
	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link KlassenListeEintrag}.
	 * 
	 * @param conn        die Datenbank-Verbindung für den Datenbankzugriff
	 * @param abschnitt   der Lernabschnitt für die Liste der Klassen
	 */
	public DataKlassenlisten(DBEntityManager conn, Long abschnitt) {
		super(conn);
		if (abschnitt == null)
			throw new NullPointerException();
		this.abschnitt = abschnitt;
	}
	
	@Override
	public Response getAll() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response getList() {
    	var klassen = conn.queryNamed("DTOKlassen.schuljahresabschnitts_id", abschnitt, DTOKlassen.class);
    	if ((klassen == null) || (klassen.size() == 0))
    		throw new WebApplicationException(Status.NOT_FOUND.getStatusCode());
    	List<Long> klassenIDs = klassen.stream().map(kl -> kl.ID).collect(Collectors.toList());
    	Map<Long, List<DTOKlassenLeitung>> klassenLeitungen = conn.queryNamed("DTOKlassenLeitung.klassen_id.multiple", klassenIDs, DTOKlassenLeitung.class)
    			.stream().collect(Collectors.groupingBy(kll -> kll.Klassen_ID));
    	var daten = klassen.stream().map(k -> {
    		KlassenListeEintrag eintrag = new KlassenListeEintrag();
    		eintrag.id = k.ID;
    		eintrag.kuerzel = k.Klasse;
    		eintrag.idJahrgang = k.Jahrgang_ID;
    		eintrag.parallelitaet = k.ASDKlasse.length() < 3 ? null : k.ASDKlasse.substring(2, 3);
    		eintrag.sortierung = k.Sortierung;
    		eintrag.istSichtbar = k.Sichtbar;
    		var klListe = klassenLeitungen.get(k.ID);
    		if (klListe != null)
    			for (DTOKlassenLeitung kl : klListe)
    				eintrag.klassenLehrer.add(kl.Lehrer_ID);
    		return eintrag;
    	}).sorted((a,b) -> Long.compare(a.sortierung, b.sortierung)).collect(Collectors.toList());
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response get(Long id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response patch(Long id, InputStream is) {
		throw new UnsupportedOperationException();
	}
	
}
