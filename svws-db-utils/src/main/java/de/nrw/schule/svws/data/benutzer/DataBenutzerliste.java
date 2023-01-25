package de.nrw.schule.svws.data.benutzer;

import java.io.InputStream;
import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Collectors;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import de.nrw.schule.svws.core.data.benutzer.BenutzerListeEintrag;
import de.nrw.schule.svws.data.DataManager;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.schild.benutzer.DTOBenutzergruppe;
import de.nrw.schule.svws.db.dto.current.schild.benutzer.DTOBenutzergruppenMitglied;
import de.nrw.schule.svws.db.dto.current.views.benutzer.DTOViewBenutzerdetails;
import de.nrw.schule.svws.db.utils.OperationError;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link BenutzerListeEintrag}.
 */
public class DataBenutzerliste extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO
	 * {@link BenutzerListeEintrag}.
	 * 
	 * @param conn die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataBenutzerliste(DBEntityManager conn) {
		super(conn);
	}

	@Override
	public Response getAll() {
		return this.getList();
	}

	@Override
	public Response getList() {
		List<DTOViewBenutzerdetails> benutzer = conn.queryAll(DTOViewBenutzerdetails.class);
		if (benutzer == null)
			throw OperationError.NOT_FOUND.exception();
		// Erstelle die Benutzerliste und sortiere sie
		List<BenutzerListeEintrag> daten = benutzer.stream().map(dtoMapper).sorted(dataComparator)
				.collect(Collectors.toList());
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	/**
	 * @param id ID der Benutzergruppe
	 * @return Benutzer, die in der Benutzergruppe sind.
	 */
	public Response getListMitGruppenID(Long id) {
		// Bestimme die IDs der Benutzer in der Benutzergruppe mit id
		DTOBenutzergruppe benutzergruppe = conn.queryByKey(DTOBenutzergruppe.class, id);
		if (benutzergruppe == null)
			throw OperationError.NOT_FOUND.exception();
		List<Long> benutzerIDs = conn
				.queryNamed("DTOBenutzergruppenMitglied.gruppe_id", benutzergruppe.ID, DTOBenutzergruppenMitglied.class)
				.stream().map(g -> g.Benutzer_ID).sorted().toList();
		List<DTOViewBenutzerdetails> benutzer = (benutzerIDs.size() == 0) 
		        ? Collections.emptyList()
		        : conn.queryNamed("DTOViewBenutzerdetails.id.multiple", benutzerIDs, DTOViewBenutzerdetails.class);
		if (benutzer == null)
			throw OperationError.NOT_FOUND.exception();
		// Erstelle die Benutzerliste und sortiere sie
		List<BenutzerListeEintrag> daten = benutzer.stream().map(dtoMapper).sorted(dataComparator)
				.collect(Collectors.toList());
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
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs
	 * {@link DTOViewBenutzerdetails} in einen Core-DTO
	 * {@link BenutzerListeEintrag}.
	 */
	private Function<DTOViewBenutzerdetails, BenutzerListeEintrag> dtoMapper = (DTOViewBenutzerdetails b) -> {
		BenutzerListeEintrag daten = new BenutzerListeEintrag();
		daten.id = b.ID;
		daten.typ = b.Typ.id;
		daten.typID = b.TypID;
		daten.anzeigename = b.AnzeigeName;
		daten.name = b.Benutzername;
		daten.istAdmin = b.IstAdmin == null ? false : b.IstAdmin;
		daten.idCredentials = b.credentialID;
		return daten;
	};

	/**
	 * Lambda-Ausdruck zum Vergleichen/Sortieren der Core-DTOs
	 * {@link BenutzerListeEintrag}.
	 */
	private Comparator<BenutzerListeEintrag> dataComparator = (a, b) -> {
		Collator collator = Collator.getInstance(Locale.GERMAN);
		if ((a.anzeigename == null) && (b.anzeigename != null))
			return -1;
		else if ((a.anzeigename != null) && (b.anzeigename == null))
			return 1;
		else if ((a.anzeigename == null) && (b.anzeigename == null))
			return 0;
		return collator.compare(a.anzeigename, b.anzeigename);
	};

}
