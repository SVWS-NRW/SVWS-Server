package de.svws_nrw.data.faecher;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import de.svws_nrw.core.adt.map.ArrayMap;
import de.svws_nrw.core.data.fach.FaecherListeEintrag;
import de.svws_nrw.core.types.fach.ZulaessigesFach;
import de.svws_nrw.core.types.gost.GostFachbereich;
import de.svws_nrw.core.types.schule.Schulform;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.db.utils.OperationError;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link FaecherListeEintrag}.
 */
public final class DataFaecherliste extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link FaecherListeEintrag}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataFaecherliste(final DBEntityManager conn) {
		super(conn);
	}

	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOFach} in einen Core-DTO {@link FaecherListeEintrag}.
	 */
	private static final Function<DTOFach, FaecherListeEintrag> dtoMapperFach = (final DTOFach f) -> {
		final FaecherListeEintrag daten = new FaecherListeEintrag();
		daten.id = f.ID;
		daten.kuerzel = (f.Kuerzel == null) ? "" : f.Kuerzel;
		daten.kuerzelStatistik = f.StatistikFach.daten.kuerzelASD;
		daten.bezeichnung = (f.Bezeichnung == null) ? "" : f.Bezeichnung;
		daten.istOberstufenFach = f.IstOberstufenFach;
		daten.sortierung = f.SortierungAllg;
		daten.istSichtbar = f.Sichtbar;
		return daten;
	};


	/**
	 * Bestimmt die Liste aller Fächer.
	 *
	 * @param conn   die Datenbankverbindung
	 *
	 * @return die Liste der Fächer
	 */
	public static List<FaecherListeEintrag> getFaecherListe(final DBEntityManager conn) {
    	final List<DTOFach> faecher = conn.queryAll(DTOFach.class);
    	if (faecher == null)
    		throw OperationError.NOT_FOUND.exception("Es wurden keine Fächer in der Datenbank gefunden.");
    	return faecher.stream().map(dtoMapperFach::apply).sorted((a, b) -> Long.compare(a.sortierung, b.sortierung)).toList();
	}


	@Override
	public Response getAll() {
    	final List<FaecherListeEintrag> daten = getFaecherListe(conn);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	@Override
	public Response getList() {
		throw new UnsupportedOperationException();
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
	 * Setzt für die Fächer der Fächerliste Default-Werte in das Feld Sortierung.
	 * Diese orientieren sich an der Sortierreihenfolge der Fächer der Oberstufe.
	 * Fächer, die nicht der Oberstufe zugeordnet werden können werden mit
	 * der ursprünglichen Sortierung angehangen.
	 *
	 * @param conn                   die Datenbankverbindung
	 *
	 * @return die HTTP-Response
	 */
	public static Response setDefaultSortierungSekII(final DBEntityManager conn) {
		// Bestimme zunächst die Schulform
		final DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
		if (schule == null)
			throw OperationError.NOT_FOUND.exception();
		final Schulform schulform = schule.Schulform;
		if ((schulform == null) || (schulform.daten == null))
			throw OperationError.NOT_FOUND.exception();
		// Bestimme die Fächer
		final @NotNull List<@NotNull DTOFach> faecher = conn.queryAll(DTOFach.class);
    	if ((faecher == null) || (faecher.isEmpty()))
    		throw OperationError.NOT_FOUND.exception("Es wurden keine Fächer gefunden.");
    	if (!schulform.daten.hatGymOb)
    		throw OperationError.BAD_REQUEST.exception("Eine Default-Sortierung für die Sekundarstufe II erfordert eine entsprechende Schulform.");
    	// Lege Datenstrukturen für die Zuordnung zu den einzelnen Statistik-Fächern an und befülle diese
    	final @NotNull Set<@NotNull ZulaessigesFach> setGostFaecher = GostFachbereich.getAlleFaecher();
    	final @NotNull ArrayMap<@NotNull ZulaessigesFach, @NotNull List<@NotNull DTOFach>> map = new ArrayMap<>(ZulaessigesFach.values());
    	final @NotNull List<@NotNull DTOFach> nichtZugeordnet = new ArrayList<>();
    	for (final @NotNull DTOFach fach : faecher) {
    		if (setGostFaecher.contains(fach.StatistikFach))
    			map.computeIfAbsent(fach.StatistikFach, k -> new ArrayList<>()).add(fach);
    		else
    			nichtZugeordnet.add(fach);
    	}
    	// Bestimme die Fächer der Oberstufe in Standard-Sortierung
    	final @NotNull List<@NotNull ZulaessigesFach> gostFaecher = GostFachbereich.getAlleFaecherSortiert();
    	final @NotNull List<@NotNull DTOFach> faecherSortiert = new ArrayList<>();
    	for (final @NotNull ZulaessigesFach gostFach : gostFaecher) {
    		final List<@NotNull DTOFach> tmpFach = map.get(gostFach);
    		if (tmpFach == null)
    			continue;
    		tmpFach.sort((final @NotNull DTOFach a, final @NotNull DTOFach b) -> a.Kuerzel.compareToIgnoreCase(b.Kuerzel));
    		faecherSortiert.addAll(tmpFach);
    	}
    	faecherSortiert.addAll(nichtZugeordnet);
    	int i = 1;
    	for (final DTOFach fach : faecherSortiert) {
    		fach.SortierungAllg = i++;
    		fach.SortierungSekII = fach.SortierungAllg;
    		conn.transactionPersist(fach);
    	}
    	return Response.status(Status.NO_CONTENT).type(MediaType.APPLICATION_JSON).build();
	}

}
