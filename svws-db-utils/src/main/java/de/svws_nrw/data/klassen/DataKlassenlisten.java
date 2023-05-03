package de.svws_nrw.data.klassen;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.klassen.KlassenListeEintrag;
import de.svws_nrw.core.data.schueler.Schueler;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.schueler.DataSchuelerliste;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassenLeitung;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link KlassenListeEintrag}.
 */
public final class DataKlassenlisten extends DataManager<Long> {

	private final long abschnitt;

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link KlassenListeEintrag}.
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

	private @NotNull List<@NotNull KlassenListeEintrag> getKlassenListe() {
    	final var klassen = conn.queryNamed("DTOKlassen.schuljahresabschnitts_id", abschnitt, DTOKlassen.class);
    	if ((klassen == null) || (klassen.isEmpty()))
    		return new ArrayList<>();
    	final List<Long> klassenIDs = klassen.stream().map(kl -> kl.ID).toList();
    	final Map<Long, List<DTOKlassenLeitung>> klassenLeitungen = conn.queryNamed("DTOKlassenLeitung.klassen_id.multiple", klassenIDs, DTOKlassenLeitung.class)
    			.stream().collect(Collectors.groupingBy(kll -> kll.Klassen_ID));
    	// Bestimme die Schüler der Klasse
    	final List<DTOSchuelerLernabschnittsdaten> listSchuelerLernabschnitte = conn.query("SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Klassen_ID IN ?1 AND e.WechselNr IS NULL", DTOSchuelerLernabschnittsdaten.class)
    			.setParameter(1, klassenIDs).getResultList();
    	final List<Long> schuelerIDs = listSchuelerLernabschnitte.stream().map(sla -> sla.Schueler_ID).toList();
    	final Map<Long, DTOSchueler> mapSchueler = schuelerIDs == null || schuelerIDs.isEmpty() ? new HashMap<>()
    			: conn.queryNamed("DTOSchueler.id.multiple", schuelerIDs, DTOSchueler.class).stream().collect(Collectors.toMap(s -> s.ID, s -> s));
    	final HashMap<Long, List<Schueler>> mapKlassenSchueler = new HashMap<>();
    	for (final DTOSchuelerLernabschnittsdaten sla : listSchuelerLernabschnitte) {
    		final DTOSchueler dtoSchueler = mapSchueler.get(sla.Schueler_ID);
    		if (dtoSchueler == null)
    			continue;
    		List<Schueler> listSchueler = mapKlassenSchueler.get(sla.Klassen_ID);
    		if (listSchueler == null) {
    			listSchueler = new ArrayList<>();
    			mapKlassenSchueler.put(sla.Klassen_ID, listSchueler);
    		}
    		listSchueler.add(DataSchuelerliste.mapToSchueler.apply(dtoSchueler));
    	}
    	// Erstelle die Einträge für die Liste der Klassen
    	return klassen.stream().map(k -> {
    		final KlassenListeEintrag eintrag = new KlassenListeEintrag();
    		eintrag.id = k.ID;
    		eintrag.kuerzel = k.Klasse;
    		eintrag.idJahrgang = k.Jahrgang_ID;
    		eintrag.parallelitaet = k.ASDKlasse.length() < 3 ? null : k.ASDKlasse.substring(2, 3);
    		eintrag.sortierung = k.Sortierung;
    		eintrag.istSichtbar = k.Sichtbar;
    		final var klListe = klassenLeitungen.get(k.ID);
    		if (klListe != null)
    			for (final DTOKlassenLeitung kl : klListe)
    				eintrag.klassenLehrer.add(kl.Lehrer_ID);
    		final var klSchueler = mapKlassenSchueler.get(k.ID);
    		if (klSchueler != null)
    			eintrag.schueler.addAll(klSchueler);
    		return eintrag;
    	}).sorted((a, b) -> Long.compare(a.sortierung, b.sortierung)).toList();
	}

	@Override
	public Response getList() {
    	final var daten = getKlassenListe();
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
