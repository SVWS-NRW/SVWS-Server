package de.svws_nrw.data.benutzer;

import java.io.InputStream;
import java.text.Collator;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.ArrayList;
import java.util.function.Function;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import de.svws_nrw.core.data.benutzer.BenutzerListeEintrag;
import de.svws_nrw.core.data.benutzer.BenutzergruppeListeEintrag;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.benutzer.DTOBenutzergruppe;
import de.svws_nrw.db.dto.current.schild.benutzer.DTOBenutzergruppenMitglied;
import de.svws_nrw.db.utils.OperationError;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link BenutzergruppeListeEintrag}.
 */
public final class DataBenutzergruppeliste extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link BenutzergruppeListeEintrag}.
	 *
	 * @param conn        die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataBenutzergruppeliste(final DBEntityManager conn) {
		super(conn);
	}


	@Override
	public Response getAll() {
		return this.getList();
	}

	@Override
	public Response getList() {
		final List<DTOBenutzergruppe> benutzergruppe = conn.queryAll(DTOBenutzergruppe.class);
    	if (benutzergruppe == null)
    		throw OperationError.NOT_FOUND.exception();
    	// Erstelle die Benutzerliste und sortiere sie
    	final List<BenutzergruppeListeEintrag> daten = benutzergruppe.stream().map(dtoMapper).sorted(dataComparator).toList();
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	/**
     * @param id ID des Benutzers
     * @return Benutzergruppen, die den Benutzer enthalten.
     */
    public Response getBenutzergruppenMitBenutzerID(final Long id) {
        // Lese die Informationen zu den Gruppen ein
        final List<Long> gruppenIDs = conn
                .queryNamed("DTOBenutzergruppenMitglied.benutzer_id", id, DTOBenutzergruppenMitglied.class).stream()
                .map(g -> g.Gruppe_ID).toList();
        final List<DTOBenutzergruppe> gruppen = (gruppenIDs.isEmpty())
                ? new ArrayList<>()
                : conn.queryNamed("DTOBenutzergruppe.id.multiple", gruppenIDs, DTOBenutzergruppe.class);

        // Erstelle die Benutzerliste und sortiere sie
     // Erstelle die Benutzerliste und sortiere sie
        final List<BenutzergruppeListeEintrag> daten = gruppen.stream().map(dtoMapper).sorted(dataComparator).toList();
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
    }


	@Override
	public Response get(final Long id) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response patch(final Long id, final InputStream is) {
		throw new UnsupportedOperationException();
	}



	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOBenutzergruppe} in einen Core-DTO {@link BenutzergruppeListeEintrag}.
	 */
	private final Function<DTOBenutzergruppe, BenutzergruppeListeEintrag> dtoMapper = (final DTOBenutzergruppe b) -> {
		final BenutzergruppeListeEintrag daten = new BenutzergruppeListeEintrag();
		daten.id = b.ID;
		daten.bezeichnung = b.Bezeichnung;
		daten.istAdmin = (b.IstAdmin != null) && b.IstAdmin;
		return daten;
	};


	/**
	 * Lambda-Ausdruck zum Vergleichen/Sortieren der Core-DTOs {@link BenutzerListeEintrag}.
	 */
	private final Comparator<BenutzergruppeListeEintrag> dataComparator = (a, b) -> {
		final Collator collator = Collator.getInstance(Locale.GERMAN);
		if ((a.bezeichnung == null) && (b.bezeichnung != null))
			return -1;
		else if ((a.bezeichnung != null) && (b.bezeichnung == null))
			return 1;
		else if ((a.bezeichnung == null) && (b.bezeichnung == null))
			return 0;
		return collator.compare(a.bezeichnung, b.bezeichnung);
	};

}
