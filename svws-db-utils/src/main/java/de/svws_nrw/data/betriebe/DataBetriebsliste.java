package de.svws_nrw.data.betriebe;

import java.io.InputStream;
import java.text.Collator;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Collectors;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import de.svws_nrw.core.data.betrieb.BetriebListeEintrag;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOKatalogAllgemeineAdresse;
import de.svws_nrw.db.utils.OperationError;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link BetriebListeEintrag}.
 */
public final class DataBetriebsliste extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link BetriebListeEintrag}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataBetriebsliste(final DBEntityManager conn) {
		super(conn);
	}

	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOKatalogAllgemeineAdresse} in einen Core-DTO {@link BetriebListeEintrag}.
	 */
	private final Function<DTOKatalogAllgemeineAdresse, BetriebListeEintrag> dtoMapper = (final DTOKatalogAllgemeineAdresse b) -> {
		final BetriebListeEintrag eintrag = new BetriebListeEintrag();
		eintrag.id = b.ID;
		eintrag.adressArt = b.adressArt;
		eintrag.name1 = b.name1;
		eintrag.strassenname = b.strassenname;
		eintrag.hausnr = b.hausnr;
		eintrag.hausnrzusatz = b.hausnrzusatz;
		eintrag.ort_id = b.ort_id;
		return eintrag;
	};

	/**
	 * Lambda-Ausdruck zum Vergleichen/Sortieren der Core-DTOs {@link BetriebListeEintrag}.
	 */
	private final Comparator<BetriebListeEintrag> dataComparator = (a, b) -> {
		final Collator collator = Collator.getInstance(Locale.GERMAN);
		return collator.compare(a.name1, b.name1);
	};

	@Override
	public Response getAll() {
		return this.getList();
	}

	@Override
	public Response getList() {
		final List<DTOKatalogAllgemeineAdresse> betriebe = conn.queryAll(DTOKatalogAllgemeineAdresse.class);
		if (betriebe == null)
			return OperationError.NOT_FOUND.getResponse("Keine Betriebe vorhanden.");
		final List<BetriebListeEintrag> daten = betriebe.stream().map(dtoMapper).sorted(dataComparator).collect(Collectors.toList());
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

}
