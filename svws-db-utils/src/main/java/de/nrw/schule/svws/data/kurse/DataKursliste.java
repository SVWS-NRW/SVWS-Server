package de.nrw.schule.svws.data.kurse;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.nrw.schule.svws.core.data.kurse.KursListeEintrag;
import de.nrw.schule.svws.core.data.schueler.Schueler;
import de.nrw.schule.svws.data.DataManager;
import de.nrw.schule.svws.data.schueler.DataSchuelerliste;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.schild.kurse.DTOKurs;
import de.nrw.schule.svws.db.dto.current.schild.kurse.DTOKursSchueler;
import de.nrw.schule.svws.db.dto.current.schild.schueler.DTOSchueler;
import de.nrw.schule.svws.db.utils.OperationError;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link KursListeEintrag}.
 */
public class DataKursliste extends DataManager<Long> {

	private final Long abschnitt; 
	
	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link KursListeEintrag}.
	 * 
	 * @param conn        die Datenbank-Verbindung für den Datenbankzugriff
	 * @param abschnitt   der Lernabschnitt für die Liste der Kurse
	 */
	public DataKursliste(DBEntityManager conn, Long abschnitt) {
		super(conn);
		this.abschnitt = abschnitt;
	}
	
	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOKurs} in einen Core-DTO {@link KursListeEintrag}.  
	 */
	private static Function<DTOKurs, KursListeEintrag> dtoMapper = k -> {
		KursListeEintrag eintrag = new KursListeEintrag();
		eintrag.id = k.ID;
		eintrag.idSchuljahresabschnitt = k.Schuljahresabschnitts_ID;
		eintrag.kuerzel = k.KurzBez;
		if (k.Jahrgang_ID != null)
			eintrag.idJahrgaenge.add(k.Jahrgang_ID);
		if (k.Jahrgaenge != null)
			for (String jahrgang : k.Jahrgaenge.split(",")) 
				if (jahrgang.matches("^[0-9]+$")) 
					eintrag.idJahrgaenge.add(Long.parseLong(jahrgang));
		eintrag.idFach = k.Fach_ID;
		eintrag.lehrer = k.Lehrer_ID;
		eintrag.sortierung = k.Sortierung == null ? 32000 : k.Sortierung;
		eintrag.istSichtbar = k.Sichtbar;
		return eintrag;		
	};
	

	@Override
	public Response getAll() {
    	List<DTOKurs> kurse = (abschnitt == null) 
    		? conn.queryAll(DTOKurs.class) 
    		: conn.queryNamed("DTOKurs.schuljahresabschnitts_id", abschnitt, DTOKurs.class);
    	if (kurse == null)
    		return OperationError.NOT_FOUND.getResponse();
    	// Erstelle die Liste der Kurse
    	List<KursListeEintrag> daten = kurse.stream().map(dtoMapper).sorted((a,b) -> Long.compare(a.sortierung, b.sortierung)).collect(Collectors.toList());
    	// Ergänze die Liste der Schüler in den Kursen
    	List<Long> kursIDs = daten.stream().map(k -> k.id).toList();
    	List<DTOKursSchueler> listKursSchueler = conn.queryNamed("DTOKursSchueler.kurs_id.multiple", kursIDs, DTOKursSchueler.class);
    	List<Long> schuelerIDs = listKursSchueler.stream().map(ks -> ks.Schueler_ID).toList();
    	Map<Long, DTOSchueler> mapSchueler = ((schuelerIDs == null) || (schuelerIDs.size() == 0)) ? new HashMap<>() : 
			conn.queryNamed("DTOSchueler.id.multiple", schuelerIDs, DTOSchueler.class).stream().collect(Collectors.toMap(s -> s.ID, s -> s));
    	HashMap<Long, List<Schueler>> mapKursSchueler = new HashMap<>();
    	for (DTOKursSchueler ks : listKursSchueler) {
    		DTOSchueler dtoSchueler = mapSchueler.get(ks.Schueler_ID);
    		if (dtoSchueler == null)
    			continue;
    		List<Schueler> listSchueler = mapKursSchueler.get(ks.Kurs_ID);
    		if (listSchueler == null) {
    			listSchueler = new Vector<>();
    			mapKursSchueler.put(ks.Kurs_ID, listSchueler);
    		}
    		listSchueler.add(DataSchuelerliste.mapToSchueler.apply(dtoSchueler));
    	}
    	for (KursListeEintrag eintrag : daten) {
    		List<Schueler> listSchueler = mapKursSchueler.get(eintrag.id);
    		if (listSchueler != null)
    			eintrag.schueler.addAll(listSchueler);
    	}
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	@Override
	public Response getList() {
		return this.getAll();
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
