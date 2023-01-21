package de.nrw.schule.svws.data.benutzer;

import java.io.InputStream;
import java.text.Collator;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import java.util.function.Function;
import java.util.stream.Collectors;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import de.nrw.schule.svws.core.data.benutzer.BenutzerListeEintrag;
import de.nrw.schule.svws.core.data.benutzer.BenutzergruppeListeEintrag;
import de.nrw.schule.svws.data.DataManager;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.schild.benutzer.DTOBenutzergruppe;
import de.nrw.schule.svws.db.dto.current.schild.benutzer.DTOBenutzergruppenMitglied;
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
		List<DTOBenutzergruppe> benutzergruppe = conn.queryAll(DTOBenutzergruppe.class); 
    	if (benutzergruppe == null)
    		throw OperationError.NOT_FOUND.exception();
    	// Erstelle die Benutzerliste und sortiere sie
    	List<BenutzergruppeListeEintrag> daten = benutzergruppe.stream().map(dtoMapper).sorted(dataComparator).collect(Collectors.toList());
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	
	/**
     * @param id ID des Benutzers
     * @return Benutzergruppen, die den Benutzer enthalten.
     */
    public Response getBenutzergruppenMitBenutzerID(Long id) {
        // Lese die Informationen zu den Gruppen ein
        List<Long> gruppenIDs = conn
                .queryNamed("DTOBenutzergruppenMitglied.benutzer_id", id, DTOBenutzergruppenMitglied.class).stream()
                .map(g -> g.Gruppe_ID).toList();
        List<DTOBenutzergruppe> gruppen = (gruppenIDs.size() == 0)
                ? new Vector<>()
                : conn.queryNamed("DTOBenutzergruppe.id.multiple", gruppenIDs, DTOBenutzergruppe.class);
        
        // Erstelle die Benutzerliste und sortiere sie
     // Erstelle die Benutzerliste und sortiere sie
        List<BenutzergruppeListeEintrag> daten = gruppen.stream().map(dtoMapper).sorted(dataComparator).collect(Collectors.toList());
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
