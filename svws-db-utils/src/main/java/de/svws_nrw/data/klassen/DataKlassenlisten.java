package de.svws_nrw.data.klassen;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.klassen.KlassenDaten;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.schule.DataSchuljahresabschnitte;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassenLeitung;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.utils.OperationError;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link KlassenDaten}.
 */
public final class DataKlassenlisten extends DataManager<Long> {

	private final long abschnitt;

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link KlassenDaten}.
	 *
	 * @param conn        die Datenbank-Verbindung für den Datenbankzugriff
	 * @param abschnitt   der Lernabschnitt für die Liste der Klassen
	 */
	public DataKlassenlisten(final DBEntityManager conn, final Long abschnitt) {
		super(conn);
		if (abschnitt == null)
			throw new NullPointerException();
		this.abschnitt = abschnitt;
	}

	@Override
	public Response getAll() {
		throw new UnsupportedOperationException();
	}

	private static @NotNull List<@NotNull KlassenDaten> getKlassenListe(final DBEntityManager conn, final long idSchuljahresabschnitt) {
		// Bestimme die Informationen zur Schule und zu den Schuljahresabschnitten
		final DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
		if (schule == null)
			throw OperationError.NOT_FOUND.exception("Konnte die Informationen zur Schule nicht einlesen");
		final @NotNull Map<@NotNull Long, @NotNull DTOSchuljahresabschnitte> mapSchuljahresabschnitte = DataSchuljahresabschnitte.getDTOMap(conn);
		final DTOSchuljahresabschnitte schuljahresabschnitt = mapSchuljahresabschnitte.get(idSchuljahresabschnitt);
		if (schuljahresabschnitt == null)
			throw OperationError.NOT_FOUND.exception("Konnte den Schuljahresabschnitt für die ID %d nicht finden.".formatted(idSchuljahresabschnitt));
		// Bestimme alle Klassen des aktuellen Schuljahresabschnitts und deren Klassenleitungen
    	final List<DTOKlassen> klassen = conn.queryNamed("DTOKlassen.schuljahresabschnitts_id", schuljahresabschnitt.ID, DTOKlassen.class);
    	if ((klassen == null) || (klassen.isEmpty()))
    		return new ArrayList<>();
    	final List<Long> klassenIDs = klassen.stream().map(kl -> kl.ID).toList();
    	final Map<Long, List<DTOKlassenLeitung>> klassenLeitungen = conn.queryNamed("DTOKlassenLeitung.klassen_id.multiple", klassenIDs, DTOKlassenLeitung.class)
    			.stream().collect(Collectors.groupingBy(kll -> kll.Klassen_ID));
    	// Bestimme alle Klassen-DTOs der klassen aus dem vorigen und nachfolgenden Schuljahresabschnitt
    	final Map<String, DTOKlassen> klassenVorher = (schuljahresabschnitt.VorigerAbschnitt_ID == null)
    			? new HashMap<>()
    			: conn.queryNamed("DTOKlassen.schuljahresabschnitts_id", schuljahresabschnitt.VorigerAbschnitt_ID, DTOKlassen.class).stream().collect(Collectors.toMap(k -> k.Klasse, k -> k));
    	// Bestimme alle Klassen-DTOs der klassen aus dem vorigen und nachfolgenden Schuljahresabschnitt
    	final Map<String, DTOKlassen> klassenNachher = (schuljahresabschnitt.FolgeAbschnitt_ID == null)
    			? new HashMap<>()
    			: conn.queryNamed("DTOKlassen.schuljahresabschnitts_id", schuljahresabschnitt.FolgeAbschnitt_ID, DTOKlassen.class).stream().collect(Collectors.toMap(k -> k.Klasse, k -> k));
    	// Bestimme die Schüler der Klasse
    	final List<DTOSchuelerLernabschnittsdaten> listSchuelerLernabschnitte = conn.query("SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Klassen_ID IN ?1 AND e.WechselNr = 0", DTOSchuelerLernabschnittsdaten.class)
    			.setParameter(1, klassenIDs).getResultList();
    	final List<Long> schuelerIDs = listSchuelerLernabschnitte.stream().map(sla -> sla.Schueler_ID).toList();
    	final Map<Long, DTOSchueler> mapSchueler = schuelerIDs == null || schuelerIDs.isEmpty() ? new HashMap<>()
    			: conn.queryNamed("DTOSchueler.id.multiple", schuelerIDs, DTOSchueler.class).stream().collect(Collectors.toMap(s -> s.ID, s -> s));
    	final Map<Long, List<DTOSchueler>> mapKlassenSchueler = new HashMap<>();
    	for (final DTOSchuelerLernabschnittsdaten sla : listSchuelerLernabschnitte) {
    		final DTOSchueler dtoSchueler = mapSchueler.get(sla.Schueler_ID);
    		if (dtoSchueler == null)
    			continue;
    		mapKlassenSchueler.computeIfAbsent(sla.Klassen_ID, l -> new ArrayList<>()).add(dtoSchueler);
    	}
    	// Erstelle die Einträge für die Liste der Klassen
    	return klassen.stream().map(k ->
    		DataKlassendaten.dtoMapper(schule.Schulform, mapSchuljahresabschnitte, k, klassenLeitungen.computeIfAbsent(k.ID, l -> new ArrayList<>()),
    				mapKlassenSchueler.computeIfAbsent(k.ID, l -> new ArrayList<>()), klassenVorher, klassenNachher)
    	).sorted((a, b) -> Long.compare(a.sortierung, b.sortierung)).toList();
	}

	@Override
	public Response getList() {
    	final var daten = getKlassenListe(conn, abschnitt);
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
	 * Setzt für die Klassen der Klassenliste des angegeben Schuljahresabschnittes Default-Werte in das
	 * Feld Sortierung.
	 *
	 * @param conn                   die Datenbankverbindung
	 * @param schuljahresabschnitt   die ID des Schuljahresabschnitts
	 *
	 * @return die HTTP-Response
	 */
	public static Response setDefaultSortierung(final DBEntityManager conn, final long schuljahresabschnitt) {
		final List<DTOJahrgang> jahrgaenge = conn.queryAll(DTOJahrgang.class);
    	if ((jahrgaenge == null) || (jahrgaenge.isEmpty()))
    		throw OperationError.NOT_FOUND.exception("Es wurden keine Jahrgänge gefunden.");
    	final Map<Long, DTOJahrgang> mapJahrgaenge = jahrgaenge.stream().collect(Collectors.toMap(j -> j.ID, j -> j));
    	final List<DTOKlassen> klassen = conn.queryNamed("DTOKlassen.schuljahresabschnitts_id", schuljahresabschnitt, DTOKlassen.class);
    	if ((klassen == null) || (klassen.isEmpty()))
    		throw OperationError.NOT_FOUND.exception("Es wurden für den Abschnitt %d keine Klassen gefunden.".formatted(schuljahresabschnitt));
    	conn.transactionFlush();
    	klassen.sort((final DTOKlassen a, final DTOKlassen b) -> {
    		final DTOJahrgang jgA = mapJahrgaenge.get(a.Jahrgang_ID);
    		final DTOJahrgang jgB = mapJahrgaenge.get(b.Jahrgang_ID);
    		if (((jgA == null) || (jgA.Sortierung == null)) && ((jgB == null) || (jgB.Sortierung == null)))
    			return 0;
    		if ((jgA == null) || (jgA.Sortierung == null))
    			return 1;
    		if ((jgB == null) || (jgB.Sortierung == null))
    			return -1;
    		if (!Objects.equals(jgA.Sortierung, jgB.Sortierung))
    			return jgA.Sortierung - jgB.Sortierung;
    		final String parA = ((a.ASDKlasse == null) || (a.ASDKlasse.length() < 3)) ? "" : a.ASDKlasse.substring(2, a.ASDKlasse.length());
    		final String parB = ((b.ASDKlasse == null) || (b.ASDKlasse.length() < 3)) ? "" : b.ASDKlasse.substring(2, b.ASDKlasse.length());
    		if (parA.length() != parB.length())
    			return parA.length() - parB.length();
    		return parA.compareToIgnoreCase(parB);
    	});
    	int i = 1;
    	for (final DTOKlassen klasse : klassen) {
    		klasse.Sortierung = i++;
    		conn.transactionPersist(klasse);
    	}
    	conn.transactionFlush();
    	return Response.status(Status.NO_CONTENT).type(MediaType.APPLICATION_JSON).build();
	}

}
