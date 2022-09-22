package de.nrw.schule.svws.data.betriebe;

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

import de.nrw.schule.svws.core.data.betrieb.BetriebListeEintrag;
import de.nrw.schule.svws.data.DataManager;
import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.dto.current.schild.katalog.DTOKatalogAllgemeineAdresse;
import de.nrw.schule.svws.db.utils.OperationError;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link BetriebListeEintrag}.
 */
public class DataBetriebsliste extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link BetriebListeEintrag}.
	 * 
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataBetriebsliste(DBEntityManager conn) {
		super(conn);
	}
	
	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOKatalogAllgemeineAdresse} in einen Core-DTO {@link BetriebListeEintrag}.  
	 */
	private Function<DTOKatalogAllgemeineAdresse, BetriebListeEintrag> dtoMapper = (DTOKatalogAllgemeineAdresse b) -> {
		BetriebListeEintrag eintrag = new BetriebListeEintrag();
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
	private Comparator<BetriebListeEintrag> dataComparator = (a, b) -> {
		Collator collator = Collator.getInstance(Locale.GERMAN);
		return collator.compare(a.name1, b.name1);
	};

	@Override
	public Response getAll() {
		return this.getList();
	}

	@Override
	public Response getList() {
		List<DTOKatalogAllgemeineAdresse> betriebe = conn.queryAll(DTOKatalogAllgemeineAdresse.class);
		if (betriebe == null)
			return OperationError.NOT_FOUND.getResponse("Keine Betriebe vorhanden.");
		List<BetriebListeEintrag> daten = betriebe.stream().map(dtoMapper).sorted(dataComparator).collect(Collectors.toList()); 
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
