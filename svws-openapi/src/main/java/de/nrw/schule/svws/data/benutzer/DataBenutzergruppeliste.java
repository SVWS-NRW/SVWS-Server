package de.nrw.schule.svws.data.benutzer;

import java.io.InputStream;
import java.text.Collator;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import de.nrw.schule.svws.core.data.benutzer.BenutzerListeEintrag;
import de.nrw.schule.svws.core.data.benutzer.BenutzergruppeListeEintrag;
import de.nrw.schule.svws.data.DataManager;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.schild.benutzer.DTOBenutzergruppe;
import de.nrw.schule.svws.db.utils.OperationError;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link BenutzergruppeListeEintrag}.
 */
public class DataBenutzergruppeliste extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link BenutzergruppeListeEintrag}.
	 * 
	 * @param conn        die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataBenutzergruppeliste(DBEntityManager conn) {
		super(conn);
	}
	
	
	@Override
	public Response getAll() {
		return this.getList();
	}

	@Override
	public Response getList() {
		List<DTOBenutzergruppe> benutzer = conn.queryAll(DTOBenutzergruppe.class); 
    	if (benutzer == null)
    		throw OperationError.NOT_FOUND.exception();
    	// Erstelle die Benutzerliste und sortiere sie
    	List<BenutzergruppeListeEintrag> daten = benutzer.stream().map(dtoMapper).sorted(dataComparator).collect(Collectors.toList());
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

	
	
	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOBenutzergruppe} in einen Core-DTO {@link BenutzergruppeListeEintrag}.  
	 */
	private Function<DTOBenutzergruppe, BenutzergruppeListeEintrag> dtoMapper = (DTOBenutzergruppe b) -> {
		BenutzergruppeListeEintrag daten = new BenutzergruppeListeEintrag();
		daten.id = b.ID;
		daten.bezeichnung = b.Bezeichnung;
		daten.istAdmin = b.IstAdmin == null ? false : b.IstAdmin;
		return daten;
	};	
	
	
	/**
	 * Lambda-Ausdruck zum Vergleichen/Sortieren der Core-DTOs {@link BenutzerListeEintrag}.  
	 */
	private Comparator<BenutzergruppeListeEintrag> dataComparator = (a,b) -> {
		Collator collator = Collator.getInstance(Locale.GERMAN);
		if ((a.bezeichnung == null) && (b.bezeichnung != null))
			return -1;
		else if ((a.bezeichnung != null) && (b.bezeichnung == null))
			return 1;
		else if ((a.bezeichnung == null) && (b.bezeichnung == null))
			return 0;
		return collator.compare(a.bezeichnung, b.bezeichnung);
	};
	
}
