package de.svws_nrw.data.benutzer;

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
import de.svws_nrw.core.data.benutzer.BenutzerListeEintrag;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.benutzer.DTOBenutzergruppe;
import de.svws_nrw.db.dto.current.schild.benutzer.DTOBenutzergruppenMitglied;
import de.svws_nrw.db.dto.current.views.benutzer.DTOViewBenutzerdetails;
import de.svws_nrw.db.utils.OperationError;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link BenutzerListeEintrag}.
 */
public final class DataBenutzerliste extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO
	 * {@link BenutzerListeEintrag}.
	 *
	 * @param conn die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataBenutzerliste(final DBEntityManager conn) {
		super(conn);
	}

	@Override
	public Response getAll() {
		return this.getList();
	}

	@Override
	public Response getList() {
		final List<DTOViewBenutzerdetails> benutzer = conn.queryAll(DTOViewBenutzerdetails.class);
		if (benutzer == null)
			throw OperationError.NOT_FOUND.exception();
		// Erstelle die Benutzerliste und sortiere sie
		final List<BenutzerListeEintrag> daten = benutzer.stream().map(dtoMapper).sorted(dataComparator)
				.collect(Collectors.toList());
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	/**
	 * @param id ID der Benutzergruppe
	 * @return Benutzer, die in der Benutzergruppe sind.
	 */
	public Response getListMitGruppenID(final Long id) {
		// Bestimme die IDs der Benutzer in der Benutzergruppe mit id
		final DTOBenutzergruppe benutzergruppe = conn.queryByKey(DTOBenutzergruppe.class, id);
		if (benutzergruppe == null)
			throw OperationError.NOT_FOUND.exception();
		final List<Long> benutzerIDs = conn
				.queryNamed("DTOBenutzergruppenMitglied.gruppe_id", benutzergruppe.ID, DTOBenutzergruppenMitglied.class)
				.stream().map(g -> g.Benutzer_ID).sorted().toList();
		final List<DTOViewBenutzerdetails> benutzer = (benutzerIDs.size() == 0)
		        ? Collections.emptyList()
		        : conn.queryNamed("DTOViewBenutzerdetails.id.multiple", benutzerIDs, DTOViewBenutzerdetails.class);
		if (benutzer == null)
			throw OperationError.NOT_FOUND.exception();
		// Erstelle die Benutzerliste und sortiere sie
		final List<BenutzerListeEintrag> daten = benutzer.stream().map(dtoMapper).sorted(dataComparator)
				.collect(Collectors.toList());
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
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs
	 * {@link DTOViewBenutzerdetails} in einen Core-DTO
	 * {@link BenutzerListeEintrag}.
	 */
	private final Function<DTOViewBenutzerdetails, BenutzerListeEintrag> dtoMapper = (final DTOViewBenutzerdetails b) -> {
		final BenutzerListeEintrag daten = new BenutzerListeEintrag();
		daten.id = b.ID;
		daten.typ = b.Typ.id;
		daten.typID = b.TypID;
		daten.anzeigename = b.AnzeigeName;
		daten.name = b.Benutzername;
		daten.istAdmin = (b.IstAdmin != null) && b.IstAdmin;
		daten.idCredentials = b.credentialID;
		return daten;
	};

	/**
	 * Lambda-Ausdruck zum Vergleichen/Sortieren der Core-DTOs
	 * {@link BenutzerListeEintrag}.
	 */
	private final Comparator<BenutzerListeEintrag> dataComparator = (a, b) -> {
		final Collator collator = Collator.getInstance(Locale.GERMAN);
		if ((a.anzeigename == null) && (b.anzeigename != null))
			return -1;
		else if ((a.anzeigename != null) && (b.anzeigename == null))
			return 1;
		else if ((a.anzeigename == null) && (b.anzeigename == null))
			return 0;
		return collator.compare(a.anzeigename, b.anzeigename);
	};

}
