package de.svws_nrw.data.faecher;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import de.svws_nrw.core.adt.map.ArrayMap;
import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.asd.types.fach.Fach;
import de.svws_nrw.core.data.fach.FaecherListeEintrag;
import de.svws_nrw.core.types.gost.GostFachbereich;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.data.DataManagerRevised;
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
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für den
 * Core-DTO {@link FaecherListeEintrag}.
 */
public final class DataFaecherliste extends DataManagerRevised<Long, DTOFach, FaecherListeEintrag> {

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für den Core-DTO {@link FaecherListeEintrag}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataFaecherliste(final DBEntityManager conn) {
		super(conn);
	}

	@Override
	protected FaecherListeEintrag map(final DTOFach dtoFach) {
		final FaecherListeEintrag daten = new FaecherListeEintrag();
		daten.id = dtoFach.ID;
		daten.kuerzel = (dtoFach.Kuerzel == null) ? "" : dtoFach.Kuerzel;
		daten.kuerzelStatistik = dtoFach.StatistikKuerzel;
		daten.bezeichnung = (dtoFach.Bezeichnung == null) ? "" : dtoFach.Bezeichnung;
		daten.istOberstufenFach = dtoFach.IstOberstufenFach;
		daten.istPruefungsordnungsRelevant = dtoFach.IstPruefungsordnungsRelevant;
		daten.sortierung = dtoFach.SortierungAllg;
		daten.istSichtbar = dtoFach.Sichtbar;
		daten.aufgabenfeld = dtoFach.Aufgabenfeld;
		daten.bilingualeSprache = dtoFach.Unterrichtssprache;
		daten.istNachpruefungErlaubt = (dtoFach.IstNachpruefungErlaubt != null) && dtoFach.IstNachpruefungErlaubt;
		daten.aufZeugnis = (dtoFach.AufZeugnis != null) && dtoFach.AufZeugnis;
		daten.bezeichnungZeugnis = (dtoFach.BezeichnungZeugnis == null) ? "" : dtoFach.BezeichnungZeugnis;
		daten.bezeichnungUeberweisungszeugnis = (dtoFach.BezeichnungUeberweisungsZeugnis == null) ? "" : dtoFach.BezeichnungUeberweisungsZeugnis;
		daten.maxZeichenInFachbemerkungen = (dtoFach.MaxBemZeichen == null) ? Integer.MAX_VALUE : dtoFach.MaxBemZeichen;
		daten.istSchriftlichZK = (dtoFach.IstSchriftlichZK != null) && dtoFach.IstSchriftlichZK;
		daten.istSchriftlichBA = (dtoFach.IstSchriftlichBA != null) && dtoFach.IstSchriftlichBA;
		daten.istFHRFach = false; // TODO Wert bestimmen
		daten.holeAusAltenLernabschnitten = (dtoFach.AbgeschlFaecherHolen != null) && dtoFach.AbgeschlFaecherHolen;
		return daten;
	}

	/**
	 * Lambda-Ausdruck zum Vergleichen/Sortieren der Core-DTOs {@link FaecherListeEintrag}.
	 */
	private static final Comparator<FaecherListeEintrag> dataComparator = (a, b) -> {
		final Collator collator = Collator.getInstance(Locale.GERMAN);
		if ((a.kuerzel == null) && (b.kuerzel != null))
			return -1;
		else if ((a.kuerzel != null) && (b.kuerzel == null))
			return 1;
		else if (a.kuerzel == null)
			return 0;
		int result = collator.compare(a.kuerzel, b.kuerzel);
		if (result == 0) {
			if ((a.bezeichnung == null) && (b.bezeichnung != null))
				return -1;
			else if ((a.bezeichnung != null) && (b.bezeichnung == null))
				return 1;
			else if (a.bezeichnung == null)
				return 0;
			result = collator.compare(a.bezeichnung, b.bezeichnung);
		}
		if (result == 0) {
			if ((a.kuerzel == null) && (b.kuerzel != null))
				return -1;
			else if ((a.kuerzel != null) && (b.kuerzel == null))
				return 1;
			else if (a.kuerzel == null)
				return 0;
			result = collator.compare(a.kuerzel, b.kuerzel);
		}
		return result;
	};

	/**
	 * Bestimmt die Liste aller Fächer.
	 *
	 * @param includeReferenzInfo   wenn True erhalten die Daten die Information, ob das Fach in anderen Datenbanktabellen referenziert ist oder nicht.
	 * 								Dies erfordert eine zusätzliche Datenbankabfrage und ist aus daher Performancegründen nur empfohlen, wenn diese
	 * 								Information benötigt wird.
	 *
	 * @return die Liste der Fächer oder leere Liste
	 */
	public List<FaecherListeEintrag> getFaecherListe(final boolean includeReferenzInfo) {
		final List<DTOFach> faecher = conn.queryAll(DTOFach.class);
		if (faecher.isEmpty())
			return Collections.emptyList();

		final Set<Long> idsOfReferencedFaecher =
				includeReferenzInfo ? getIdsOfReferencedFaecher(faecher.stream().map(f -> f.ID).collect(Collectors.toSet())) : Collections.emptySet();

		return faecher.stream().map(f -> {
			final FaecherListeEintrag fachDaten = map(f);
			if (includeReferenzInfo)
				fachDaten.referenziertInAnderenTabellen = idsOfReferencedFaecher.contains(fachDaten.id);
			return fachDaten;
		}).sorted(dataComparator).toList();
	}

	@Override
	public List<FaecherListeEintrag> getAll() {
		return getFaecherListe(true);
	}

	@Override
	protected long getLongId(final DTOFach fach) {
		return fach.ID;
	}

	@Override
	protected void checkBeforeDeletionWithSimpleOperationResponse(final List<DTOFach> faecher, final Map<Long, SimpleOperationResponse> mapResponses) {
		final Set<Long> result = getIdsOfReferencedFaecher(faecher.stream().map(f -> f.ID).collect(Collectors.toSet()));
		faecher.stream().filter(f -> result.contains(f.ID)).forEach(f -> {
			final SimpleOperationResponse response = mapResponses.get(f.ID);
			response.success = false;
			response.log.add("Das Fach mit dem Kuerzel %s und der id %d ist in der Datenbank referenziert und kann daher nicht gelöscht werden"
					.formatted(f.Kuerzel, f.ID));
		});
	}

	private Set<Long> getIdsOfReferencedFaecher(final Set<Long> idsFaecher) {
		final String queryGostJahrgangFachwahlen = "SELECT DISTINCT a.Fach_ID FROM DTOGostJahrgangFachbelegungen a WHERE a.Fach_ID IN :idsFaecher";
		final String queryGostSchuelerFachwahlen = "SELECT DISTINCT b.Fach_ID FROM DTOGostSchuelerFachbelegungen b WHERE b.Fach_ID IN :idsFaecher";
		final String queryKurse = "SELECT DISTINCT c.Fach_ID FROM DTOKurs c WHERE c.Fach_ID IN :idsFaecher";
		final String querySchuelerAbiFaecher = "SELECT DISTINCT d.Fach_ID FROM DTOSchuelerAbiturFach d WHERE d.Fach_ID IN :idsFaecher";
		final String querySchuelerBKFaecher = "SELECT DISTINCT e.Fach_ID FROM DTOSchuelerBKFach e WHERE e.Fach_ID IN :idsFaecher";
		final String querySchuelerFehlstunden = "SELECT DISTINCT f.Fach_ID FROM DTOSchuelerFehlstunden f WHERE f.Fach_ID IN :idsFaecher";
		final String querySchuelerFHRFaecher = "SELECT DISTINCT g.Fach_ID FROM DTOSchuelerFHRFach g WHERE g.Fach_ID IN :idsFaecher";
		final String querySchuelerLeistungsdaten = "SELECT DISTINCT h.Fach_ID FROM DTOSchuelerLeistungsdaten h WHERE h.Fach_ID IN :idsFaecher";
		final String querySchuelerLernabschnittsdaten = "SELECT i.Fachklasse_ID FROM DTOSchuelerLernabschnittsdaten i WHERE i.Fachklasse_ID IN :idsFaecher";
		final String querySchuelerZP10 = "SELECT DISTINCT j.Fach_ID FROM DTOSchuelerZP10 j WHERE j.Fach_ID IN :idsFaecher";
		final String querySchuelerZuweisungen = "SELECT DISTINCT k.Fach_ID FROM DTOSchuelerZuweisung k WHERE k.Fach_ID IN :idsFaecher";

		final String query = String.join("\nUNION ALL\n", queryGostJahrgangFachwahlen, queryGostSchuelerFachwahlen, queryKurse, querySchuelerAbiFaecher,
				querySchuelerBKFaecher, querySchuelerFehlstunden, querySchuelerFHRFaecher, querySchuelerLeistungsdaten, querySchuelerLernabschnittsdaten,
				querySchuelerZP10, querySchuelerZuweisungen);
		final List<Long> results = conn.query(query, Long.class).setParameter("idsFaecher", idsFaecher).getResultList();
		return new HashSet<>(results);
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
