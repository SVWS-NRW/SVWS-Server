package de.svws_nrw.data.faecher;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import de.svws_nrw.core.adt.map.ArrayMap;
import de.svws_nrw.core.data.fach.FachDaten;
import de.svws_nrw.asd.types.fach.Fach;
import de.svws_nrw.core.types.gost.GostFachbereich;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link FachDaten}.
 */
public final class DataFaecherliste extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link FachDaten}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataFaecherliste(final DBEntityManager conn) {
		super(conn);
	}

	/**
	 * Lambda-Ausdruck zum Umwandeln eines Datenbank-DTOs {@link DTOFach} in einen Core-DTO {@link FachDaten}.
	 */
	private static final Function<DTOFach, FachDaten> dtoMapperFach = (final DTOFach f) -> {
		final FachDaten daten = new FachDaten();
		daten.id = f.ID;
		daten.kuerzel = (f.Kuerzel == null) ? "" : f.Kuerzel;
		daten.kuerzelStatistik = f.StatistikKuerzel;
		daten.bezeichnung = (f.Bezeichnung == null) ? "" : f.Bezeichnung;
		daten.istOberstufenFach = f.IstOberstufenFach;
		daten.istPruefungsordnungsRelevant = f.IstPruefungsordnungsRelevant;
		daten.sortierung = f.SortierungAllg;
		daten.istSichtbar = f.Sichtbar;
		daten.aufgabenfeld = f.Aufgabenfeld;
		daten.bilingualeSprache = f.Unterrichtssprache;
		daten.istNachpruefungErlaubt = (f.IstNachpruefungErlaubt != null) && f.IstNachpruefungErlaubt;
		daten.aufZeugnis = (f.AufZeugnis != null) && f.AufZeugnis;
		daten.bezeichnungZeugnis = (f.BezeichnungZeugnis == null) ? "" : f.BezeichnungZeugnis;
		daten.bezeichnungUeberweisungszeugnis = (f.BezeichnungUeberweisungsZeugnis == null) ? "" : f.BezeichnungUeberweisungsZeugnis;
		daten.maxZeichenInFachbemerkungen = (f.MaxBemZeichen == null) ? Integer.MAX_VALUE : f.MaxBemZeichen;
		daten.istSchriftlichZK = (f.IstSchriftlichZK != null) && f.IstSchriftlichZK;
		daten.istSchriftlichBA = (f.IstSchriftlichBA != null) && f.IstSchriftlichBA;
		daten.istFHRFach = false; // TODO Wert bestimmen
		daten.holeAusAltenLernabschnitten = (f.AbgeschlFaecherHolen != null) && f.AbgeschlFaecherHolen;
		return daten;
	};


	/**
	 * Bestimmt die Liste aller Fächer.
	 *
	 * @param conn   die Datenbankverbindung
	 *
	 * @return die Liste der Fächer
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static List<FachDaten> getFaecherListe(final DBEntityManager conn) throws ApiOperationException {
		final List<DTOFach> faecher = conn.queryAll(DTOFach.class);
		if (faecher == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurden keine Fächer in der Datenbank gefunden.");
		return faecher.stream().map(dtoMapperFach::apply).sorted((a, b) -> Long.compare(a.sortierung, b.sortierung)).toList();
	}


	@Override
	public Response getAll() throws ApiOperationException {
		final List<FachDaten> daten = getFaecherListe(conn);
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
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static Response setDefaultSortierungSekII(final DBEntityManager conn) throws ApiOperationException {
		// Bestimme zunächst die Schulform
		final DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
		if (schule == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		final Schulform schulform = Schulform.data().getWertByKuerzel(schule.SchulformKuerzel);
		if (schulform == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		final DTOSchuljahresabschnitte schuljahresabschnitt = conn.queryByKey(DTOSchuljahresabschnitte.class, schule.Schuljahresabschnitts_ID);
		if (schuljahresabschnitt == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Keine gültiger Schuljahresabschnitt vorhanden.");
		// Bestimme die Fächer
		final @NotNull List<@NotNull DTOFach> faecher = conn.queryAll(DTOFach.class);
		if ((faecher == null) || (faecher.isEmpty()))
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurden keine Fächer gefunden.");
		if (!schulform.daten(schuljahresabschnitt.Jahr).hatGymOb)
			throw new ApiOperationException(Status.BAD_REQUEST, "Eine Default-Sortierung für die Sekundarstufe II erfordert eine entsprechende Schulform.");
		// Lege Datenstrukturen für die Zuordnung zu den einzelnen Statistik-Fächern an und befülle diese
		final @NotNull Set<@NotNull Fach> setGostFaecher = GostFachbereich.getAlleFaecher().keySet();
		final @NotNull ArrayMap<@NotNull Fach, @NotNull List<@NotNull DTOFach>> map = new ArrayMap<>(Fach.values());
		final @NotNull List<@NotNull DTOFach> nichtZugeordnet = new ArrayList<>();
		for (final @NotNull DTOFach fach : faecher) {
			final Fach tmpFach = Fach.data().getWertBySchluessel(fach.StatistikKuerzel);
			if (setGostFaecher.contains(tmpFach))
				map.computeIfAbsent(tmpFach, k -> new ArrayList<>()).add(fach);
			else
				nichtZugeordnet.add(fach);
		}
		// Bestimme die Fächer der Oberstufe in Standard-Sortierung
		final @NotNull List<@NotNull Fach> gostFaecher = GostFachbereich.getAlleFaecherSortiert();
		final @NotNull List<@NotNull DTOFach> faecherSortiert = new ArrayList<>();
		for (final @NotNull Fach gostFach : gostFaecher) {
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
