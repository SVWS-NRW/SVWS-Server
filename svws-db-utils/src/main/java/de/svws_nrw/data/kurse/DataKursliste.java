package de.svws_nrw.data.kurse;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.kurse.KursListeEintrag;
import de.svws_nrw.core.data.schueler.Schueler;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.schueler.DataSchuelerliste;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKurs;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKursSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link KursListeEintrag}.
 */
public final class DataKursliste extends DataManager<Long> {

	private final Long abschnitt;

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link KursListeEintrag}.
	 *
	 * @param conn        die Datenbank-Verbindung für den Datenbankzugriff
	 * @param abschnitt   der Lernabschnitt für die Liste der Kurse
	 */
	public DataKursliste(final DBEntityManager conn, final Long abschnitt) {
		super(conn);
		this.abschnitt = abschnitt;
	}

	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOKurs} in einen Core-DTO {@link KursListeEintrag}.
	 */
	private static Function<DTOKurs, KursListeEintrag> dtoMapper = k -> {
		final KursListeEintrag eintrag = new KursListeEintrag();
		eintrag.id = k.ID;
		eintrag.idSchuljahresabschnitt = k.Schuljahresabschnitts_ID;
		eintrag.kuerzel = k.KurzBez;
		if (k.Jahrgang_ID != null)
			eintrag.idJahrgaenge.add(k.Jahrgang_ID);
		if (k.Jahrgaenge != null)
			for (final String jahrgang : k.Jahrgaenge.split(","))
				if (jahrgang.matches("^\\d+$"))
					eintrag.idJahrgaenge.add(Long.parseLong(jahrgang));
		eintrag.idFach = k.Fach_ID;
		eintrag.lehrer = k.Lehrer_ID;
		eintrag.sortierung = k.Sortierung == null ? 32000 : k.Sortierung;
		eintrag.istSichtbar = k.Sichtbar;
		if ((k.Schienen != null) && (!k.Schienen.isBlank())) {
			for (final String strSchiene : k.Schienen.split(",")) {
				if ("".equals(strSchiene.trim()))
					continue;
				try {
					eintrag.schienen.add(Integer.parseInt(strSchiene.trim()));
				} catch (@SuppressWarnings("unused") final NumberFormatException nfe) {
					// ignore exception
				}
			}
		}
		return eintrag;
	};


	/**
	 * Bestimmt die Liste der Kurse für den angegeben Abschnitt. Ist dieser Abschnitt null, so werden die Kurse
	 * aller Abschnitte zurückgegeben.
	 *
	 * @param conn                     die Datenbankverbindung
	 * @param idSchuljahresabschnitt   die ID des Schuljahresabschnitts
	 * @param mitSchuelerInfo          gibt an, ob die KurslistenEinträge die Information zu Schülern beinhalten soll
	 *
	 * @return die Liste der Kurse
	 */
	public static @NotNull List<@NotNull KursListeEintrag> getKursListenFuerAbschnitt(final DBEntityManager conn,
			final Long idSchuljahresabschnitt, final boolean mitSchuelerInfo) {
    	final @NotNull List<@NotNull DTOKurs> kurse = (idSchuljahresabschnitt == null)
    		? conn.queryAll(DTOKurs.class)
    		: conn.queryNamed("DTOKurs.schuljahresabschnitts_id", idSchuljahresabschnitt, DTOKurs.class);
    	if (kurse.isEmpty())
    		return new ArrayList<>();
    	// Erstelle die Liste der Kurse
    	final List<KursListeEintrag> daten = kurse.stream().map(dtoMapper).sorted((a, b) -> Long.compare(a.sortierung, b.sortierung)).toList();
    	if (!mitSchuelerInfo)
    		return daten;
    	// Ergänze die Liste der Schüler in den Kursen
    	final List<Long> kursIDs = daten.stream().map(k -> k.id).toList();
    	final List<DTOKursSchueler> listKursSchueler =
    			conn.queryList("SELECT e FROM DTOKursSchueler e WHERE e.Kurs_ID IN ?1 AND e.LernabschnittWechselNr = 0", DTOKursSchueler.class, kursIDs);
    	final List<Long> schuelerIDs = listKursSchueler.stream().map(ks -> ks.Schueler_ID).toList();
    	final Map<Long, DTOSchueler> mapSchueler = ((schuelerIDs == null) || (schuelerIDs.isEmpty())) ? new HashMap<>()
    			: conn.queryNamed("DTOSchueler.id.multiple", schuelerIDs, DTOSchueler.class).stream().collect(Collectors.toMap(s -> s.ID, s -> s));
    	final HashMap<Long, List<Schueler>> mapKursSchueler = new HashMap<>();
    	for (final DTOKursSchueler ks : listKursSchueler) {
    		final DTOSchueler dtoSchueler = mapSchueler.get(ks.Schueler_ID);
    		if (dtoSchueler == null)
    			continue;
    		List<Schueler> listSchueler = mapKursSchueler.get(ks.Kurs_ID);
    		if (listSchueler == null) {
    			listSchueler = new ArrayList<>();
    			mapKursSchueler.put(ks.Kurs_ID, listSchueler);
    		}
    		listSchueler.add(DataSchuelerliste.mapToSchueler.apply(dtoSchueler));
    	}
    	for (final KursListeEintrag eintrag : daten) {
    		final List<Schueler> listSchueler = mapKursSchueler.get(eintrag.id);
    		if (listSchueler != null)
    			eintrag.schueler.addAll(listSchueler);
    	}
    	return daten;
	}


	@Override
	public Response getAll() {
		final @NotNull List<@NotNull KursListeEintrag> daten = getKursListenFuerAbschnitt(conn, abschnitt, true);
        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	@Override
	public Response getList() {
		return this.getAll();
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
