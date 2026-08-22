package de.svws_nrw.module.reporting.repositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.gost.Abiturdaten;
import de.svws_nrw.core.data.gost.GostJahrgangsdaten;
import de.svws_nrw.core.data.gost.GostLaufbahnplanungBeratungsdaten;
import de.svws_nrw.core.data.gost.GostSchuelerGKLWahl;
import de.svws_nrw.core.data.gost.GostStatistikFachwahl;
import de.svws_nrw.core.data.gost.klausuren.GostKlausurvorgabe;
import de.svws_nrw.core.utils.gost.GostFaecherManager;
import de.svws_nrw.data.faecher.DBUtilsFaecherGost;
import de.svws_nrw.data.gost.DataGostAbiturdaten;
import de.svws_nrw.data.gost.DataGostJahrgangFachkombinationen;
import de.svws_nrw.data.gost.DataGostJahrgangsdaten;
import de.svws_nrw.data.gost.DataGostSchuelerLaufbahnplanungBeratungsdaten;
import de.svws_nrw.db.dto.current.gost.DTOGostJahrgangsdaten;
import de.svws_nrw.db.dto.current.gost.DTOGostSchueler;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.diagnose.ReportingLadezustand;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemSchluessel;
import de.svws_nrw.service.gost.GostServiceFactoryBuilder;
import de.svws_nrw.service.gost.klausuren.GostKlausurenServiceFactoryBuilder;
import de.svws_nrw.service.gost.klausuren.GostKlausurenVorgabeService;
import jakarta.ws.rs.core.Response.Status;

/**
 * Domänen-Repository für GOSt-Daten (Abiturjahrgänge, Beratungsdaten, Kursplanung).
 * Wird lazy befüllt, d. h. es erfolgt keine Initialisierungslogik im Konstruktor.
 */
public class ReportingRepositoryGost {

	private final ReportingContext reportingContext;

	private final Map<Integer, GostJahrgangsdaten> mapAbiturjahrgangDaten = new HashMap<>();
	private final Map<Integer, GostFaecherManager> mapAbiturjahrgangFaecher = new HashMap<>();
	private final Map<Long, GostLaufbahnplanungBeratungsdaten> mapBeratungsdaten = new HashMap<>();
	private final Map<Long, Abiturdaten> mapBeratungsdatenAbiturdaten = new HashMap<>();
	private final Map<Long, Abiturdaten> mapSchuelerAbiturdaten = new HashMap<>();
	private final Map<Integer, List<GostStatistikFachwahl>> mapFachwahlen = new HashMap<>();
	private final Map<Long, GostSchuelerGKLWahl> mapGklWahlen = new HashMap<>();
	private final Map<Long, GostKlausurvorgabe> mapKlausurvorgaben = new HashMap<>();

	/**
	 * Die Fehler gescheiterter Ladevorgänge je Cache und Schüler-ID. Sie leben so lange wie die Caches selbst, weil deren Fehler-Marker jeden weiteren
	 * Ladeversuch verhindert; ohne sie wüsste der Zustand nur noch, dass das Laden scheiterte, nicht mehr woran.
	 */
	private final Map<Long, Exception> ladefehlerBeratungsdaten = new HashMap<>();
	private final Map<Long, Exception> ladefehlerBeratungsdatenAbiturdaten = new HashMap<>();
	private final Map<Long, Exception> ladefehlerSchuelerAbiturdaten = new HashMap<>();
	private final Map<Long, Exception> ladefehlerGklWahlen = new HashMap<>();
	private final Map<Long, Exception> ladefehlerKlausurvorgaben = new HashMap<>();

	/** Die Fehler gescheiterter Ladevorgänge der Fachwahlstatistik je Abiturjahr; die Lebensdauer folgt derselben Regel wie bei den Maps oben. */
	private final Map<Integer, Exception> ladefehlerFachwahlen = new HashMap<>();

	/** Zwischenspeicher für die Liste der vorhandenen Abiturjahrgänge. */
	private List<Integer> abiturjahrgaenge = null;

	/**
	 * Erstellt ein neues ReportingRepositoryGost.
	 *
	 * @param reportingContext Der zentrale Reporting-Context mit Zugriff auf die domänenspezifischen Repositories.
	 */
	public ReportingRepositoryGost(final ReportingContext reportingContext) {
		this.reportingContext = reportingContext;
	}


	// ##### GOSt-Schul-Vorbedingung #####

	/**
	 * Gibt die Abi-Jahrgänge zurück, die in der Datenbank zu DTOGostJahrgangsdaten hinterlegt sind.
	 * Die Liste wird beim ersten Aufruf aus der Datenbank geladen und für die Lebensdauer des Reporting-Contexts zwischengespeichert.
	 *
	 * @return Liste der vorhandenen Abi-Jahrgänge.
	 *
	 * @throws ApiOperationException Mit Status 500, falls die Abiturjahrgänge nicht geladen werden konnten - das ist ein Serverproblem und kein Befund an den
	 *                               Parametern, die gegen diese Liste geprüft werden.
	 */
	public List<Integer> abiturjahrgaenge() throws ApiOperationException {
		if (abiturjahrgaenge == null) {
			try {
				abiturjahrgaenge = this.reportingContext.conn().queryAll(DTOGostJahrgangsdaten.class).stream().map(aj -> aj.Abi_Jahrgang).toList();
			} catch (final Exception e) {
				throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e,
						"FEHLER: Die vorhandenen Abiturjahrgänge konnten nicht ermittelt werden.");
			}
		}
		return abiturjahrgaenge;
	}


	// ##### Jahrgangsdaten und Fächer #####

	/**
	 * Lädt die GOSt-Jahrgangsdaten zum übergebenen Abiturjahr aus der Datenbank. Die Daten werden bei erstem Zugriff aus der
	 * Datenbank geladen und im Cache gehalten.
	 *
	 * @param abiturjahr Das Abiturjahr des Jahrgangs.
	 *
	 * @return Die GOSt-Jahrgangsdaten.
	 *
	 * @throws ApiOperationException Im Fehlerfall.
	 */
	public GostJahrgangsdaten jahrgangsdaten(final int abiturjahr) throws ApiOperationException {
		return mapAbiturjahrgangDaten.computeIfAbsent(abiturjahr,
				jahr -> DataGostJahrgangsdaten.getJahrgangsdaten(this.reportingContext.conn(), jahr));
	}

	/**
	 * Lädt den GOSt-FächerManager zum übergebenen Abiturjahr aus der Datenbank und ergänzt die Fachkombinationen. Die Daten
	 * werden bei erstem Zugriff aus der Datenbank geladen und im Cache gehalten.
	 *
	 * @param abiturjahr Das Abiturjahr des Jahrgangs.
	 *
	 * @return Der mit Fachkombinationen ergänzte FächerManager des Abiturjahrgangs.
	 *
	 * @throws ApiOperationException Im Fehlerfall.
	 */
	public GostFaecherManager faecherManager(final int abiturjahr) throws ApiOperationException {
		return mapAbiturjahrgangFaecher.computeIfAbsent(abiturjahr, jahr -> {
			final int auswahlSchuljahr = this.reportingContext.repositorySchule().auswahlSchuljahresabschnitt().schuljahr();
			final GostFaecherManager manager =
					DBUtilsFaecherGost.getFaecherManager(auswahlSchuljahr, this.reportingContext.conn(), jahr);
			manager.addFachkombinationenAll(
					DataGostJahrgangFachkombinationen.getFachkombinationen(this.reportingContext.conn(), jahr));
			return manager;
		});
	}


	// ##### Beratungsdaten und Beratungs-Abiturdaten #####

	/**
	 * Liefert die GOSt-Laufbahnberatungsdaten zum übergebenen Schüler. Beim ersten Zugriff werden die Daten für alle bekannten
	 * Schüler gesammelt nachgeladen und im Cache abgelegt.
	 *
	 * @param idSchueler Die ID des Schülers.
	 *
	 * @return Die Beratungsdaten oder {@code null}, falls keine vorhanden sind oder das Laden fehlgeschlagen ist.
	 */
	public GostLaufbahnplanungBeratungsdaten beratungsdaten(final long idSchueler) {
		final List<Long> ids = new ArrayList<>(this.reportingContext.repositorySchueler().idsGeladenerSchueler());
		ids.add(idSchueler);
		beratungsdaten(ids);
		return mapBeratungsdaten.get(idSchueler);
	}

	/**
	 * Lädt die GOSt-Laufbahnberatungsdaten zu den übergebenen Schüler-IDs aus der Datenbank und übernimmt sie in den Cache.
	 *
	 * @param idsSchueler Die IDs der Schüler, deren Beratungsdaten geladen werden sollen.
	 *
	 * @return Map mit Schüler-ID als Schlüssel und den zugehörigen Beratungsdaten als Wert.
	 */
	public Map<Long, GostLaufbahnplanungBeratungsdaten> beratungsdaten(final List<Long> idsSchueler) {
		ladeBeratungsdaten(idsSchueler);
		return filtereMap(idsSchueler, mapBeratungsdaten);
	}

	/**
	 * Liefert den Ladezustand der GOSt-Laufbahnberatungsdaten je übergebener Schüler-ID.
	 * <p>Anders als {@link #beratungsdaten(List)} unterscheidet der Zustand, warum keine Daten vorliegen: Ein Schüler ohne Beratungsdaten gehört nicht zur
	 * gymnasialen Oberstufe, ein gescheiterter Zugriff ist eine Störung. Die gefilterte Map verwirft beide Fälle gleichermaßen.</p>
	 *
	 * @param idsSchueler Die IDs der Schüler, deren Beratungsdaten geprüft werden sollen.
	 *
	 * @return Der Ladezustand je Schüler-ID.
	 */
	public Map<Long, ReportingLadezustand<GostLaufbahnplanungBeratungsdaten>> zustaendeBeratungsdaten(final List<Long> idsSchueler) {
		ladeBeratungsdaten(idsSchueler);
		return ReportingRepositoryUtils.zustaendeAus(idsSchueler, mapBeratungsdaten, ladefehlerBeratungsdaten);
	}

	/**
	 * Lädt die fehlenden GOSt-Laufbahnberatungsdaten in den Cache.
	 *
	 * @param idsSchueler Die IDs der Schüler.
	 */
	private void ladeBeratungsdaten(final List<Long> idsSchueler) {
		ReportingRepositoryUtils.ladeFehlendeWerteInRepositoryMap(
				idsSchueler,
				mapBeratungsdaten,
				ids -> new DataGostSchuelerLaufbahnplanungBeratungsdaten(this.reportingContext.conn()).getMapFromIDs(ids),
				"GOSt-Beratungsdaten",
				this.reportingContext.logger(),
				ladefehlerBeratungsdaten);
	}

	/**
	 * Liefert die im Rahmen der Laufbahnberatung ermittelten Abiturdaten zum übergebenen Schüler. Beim ersten Zugriff werden
	 * die Daten für alle bekannten Schüler gesammelt nachgeladen und im Cache abgelegt.
	 *
	 * @param idSchueler Die ID des Schülers.
	 *
	 * @return Die Beratungs-Abiturdaten oder {@code null}, falls keine vorhanden sind oder das Laden fehlgeschlagen ist.
	 */
	public Abiturdaten beratungsdatenAbiturdaten(final long idSchueler) {
		final List<Long> ids = new ArrayList<>(this.reportingContext.repositorySchueler().idsGeladenerSchueler());
		ids.add(idSchueler);
		beratungsdatenAbiturdaten(ids);
		return mapBeratungsdatenAbiturdaten.get(idSchueler);
	}

	/**
	 * Lädt die im Rahmen der Laufbahnberatung ermittelten Abiturdaten zu den übergebenen Schüler-IDs aus der Datenbank und
	 * übernimmt sie in den Cache.
	 *
	 * @param idsSchueler Die IDs der Schüler, deren Beratungs-Abiturdaten geladen werden sollen.
	 *
	 * @return Map mit Schüler-ID als Schlüssel und den zugehörigen Abiturdaten als Wert.
	 */
	public Map<Long, Abiturdaten> beratungsdatenAbiturdaten(final List<Long> idsSchueler) {
		ladeBeratungsdatenAbiturdaten(idsSchueler);
		return filtereMap(idsSchueler, mapBeratungsdatenAbiturdaten);
	}

	/**
	 * Liefert den Ladezustand der Beratungs-Abiturdaten je übergebener Schüler-ID.
	 * <p>Anders als {@link #beratungsdatenAbiturdaten(List)} unterscheidet der Zustand, warum keine Daten vorliegen: eine fachlich nicht angelegte Laufbahn
	 * oder ein gescheiterter Zugriff.</p>
	 *
	 * @param idsSchueler Die IDs der Schüler, deren Beratungs-Abiturdaten geprüft werden sollen.
	 *
	 * @return Der Ladezustand je Schüler-ID.
	 */
	public Map<Long, ReportingLadezustand<Abiturdaten>> zustaendeBeratungsdatenAbiturdaten(final List<Long> idsSchueler) {
		ladeBeratungsdatenAbiturdaten(idsSchueler);
		return ReportingRepositoryUtils.zustaendeAus(idsSchueler, mapBeratungsdatenAbiturdaten, ladefehlerBeratungsdatenAbiturdaten);
	}

	/**
	 * Lädt die fehlenden Beratungs-Abiturdaten in den Cache.
	 *
	 * @param idsSchueler Die IDs der Schüler.
	 */
	private void ladeBeratungsdatenAbiturdaten(final List<Long> idsSchueler) {
		ReportingRepositoryUtils.ladeFehlendeWerteInRepositoryMap(
				idsSchueler,
				mapBeratungsdatenAbiturdaten,
				ids -> GostServiceFactoryBuilder.getGostServiceFactory().getGostAbiturdatenService().getList(ids)
						.stream().collect(Collectors.toMap(a -> a.schuelerID, a -> a)),
				"GOSt-Beratungsdaten-Abiturdaten",
				this.reportingContext.logger(),
				ladefehlerBeratungsdatenAbiturdaten);
	}

	// ##### Abiturdaten der Schüler #####

	/**
	 * Liefert die GOSt-Abiturdaten zum übergebenen Schüler. Beim ersten Zugriff werden die Daten für alle bekannten Schüler
	 * gesammelt nachgeladen und im Cache abgelegt.
	 *
	 * @param idSchueler Die ID des Schülers.
	 *
	 * @return Die Abiturdaten oder {@code null}, falls keine vorhanden sind oder das Laden fehlgeschlagen ist.
	 */
	public Abiturdaten schuelerAbiturdaten(final long idSchueler) {
		final List<Long> ids = new ArrayList<>(this.reportingContext.repositorySchueler().idsGeladenerSchueler());
		ids.add(idSchueler);
		schuelerAbiturdaten(ids);
		return mapSchuelerAbiturdaten.get(idSchueler);
	}

	/**
	 * Lädt die GOSt-Abiturdaten zu den übergebenen Schüler-IDs aus der Datenbank und übernimmt sie in den Cache.
	 * Das Abiturjahr wird beim Konstruktor von {@link DataGostAbiturdaten} mit {@code null} übergeben, da
	 * {@link DataGostAbiturdaten#getMapAbiturdatenFromIDs(List)} nicht auf das Abiturjahr angewiesen ist.
	 *
	 * @param idsSchueler Die IDs der Schüler, deren Abiturdaten geladen werden sollen.
	 *
	 * @return Map mit Schüler-ID als Schlüssel und den zugehörigen Abiturdaten als Wert.
	 */
	public Map<Long, Abiturdaten> schuelerAbiturdaten(final List<Long> idsSchueler) {
		ladeSchuelerAbiturdaten(idsSchueler);
		return filtereMap(idsSchueler, mapSchuelerAbiturdaten);
	}

	/**
	 * Liefert den Ladezustand der GOSt-Abiturdaten je übergebener Schüler-ID.
	 * <p>Anders als {@link #schuelerAbiturdaten(List)} unterscheidet der Zustand, warum keine Daten vorliegen: eine fachlich nicht vorhandene Abiturakte oder
	 * ein gescheiterter Zugriff.</p>
	 *
	 * @param idsSchueler Die IDs der Schüler, deren Abiturdaten geprüft werden sollen.
	 *
	 * @return Der Ladezustand je Schüler-ID.
	 */
	public Map<Long, ReportingLadezustand<Abiturdaten>> zustaendeSchuelerAbiturdaten(final List<Long> idsSchueler) {
		ladeSchuelerAbiturdaten(idsSchueler);
		return ReportingRepositoryUtils.zustaendeAus(idsSchueler, mapSchuelerAbiturdaten, ladefehlerSchuelerAbiturdaten);
	}

	/**
	 * Lädt die fehlenden GOSt-Abiturdaten in den Cache.
	 *
	 * @param idsSchueler Die IDs der Schüler.
	 */
	private void ladeSchuelerAbiturdaten(final List<Long> idsSchueler) {
		ReportingRepositoryUtils.ladeFehlendeWerteInRepositoryMap(
				idsSchueler,
				mapSchuelerAbiturdaten,
				ids -> new DataGostAbiturdaten(this.reportingContext.conn(), null).getMapAbiturdatenFromIDs(ids),
				"GOSt-Abiturdaten",
				this.reportingContext.logger(),
				ladefehlerSchuelerAbiturdaten);
	}


	// ##### Fachwahlen #####

	/**
	 * Gibt die GOSt-Fachwahlstatistik für den übergebenen Abiturjahrgang zurück: die Wahlen je Fach und Halbjahr aus der Laufbahnplanung, unabhängig von
	 * einer Blockung. Die Daten werden bei erstem Zugriff aus der Datenbank geladen und im Cache gehalten.
	 * <p><b>Strikter Zugriff:</b> Die Statistik ist hier der Hauptinhalt des Reports - der Fachwahlstatistik-Report eines Abiturjahrgangs. Ein gescheiterter
	 * Ladevorgang bricht deshalb mit einem Serverfehler ab, statt einen leeren Report mit Erfolgsstatus zu erzeugen. Für die Fachwahl-Anzahlen als
	 * Anreicherung einer anderen Ausgabe gilt {@link #fachwahlenOptional(int)}.</p>
	 *
	 * @param abiturjahr Das Abiturjahr des Jahrgangs.
	 *
	 * @return Liste der Fachwahl-Statistikeinträge des Abiturjahrgangs; leer, wenn der Jahrgang keine Fachwahlen hat.
	 *
	 * @throws ApiOperationException Mit Status 500 und der ursprünglichen Ursache, falls die Statistik nicht geladen werden konnte.
	 */
	public List<GostStatistikFachwahl> fachwahlen(final int abiturjahr) throws ApiOperationException {
		ladeFachwahlen(abiturjahr);
		final List<GostStatistikFachwahl> fachwahlen = mapFachwahlen.get(abiturjahr);
		if (fachwahlen == null) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, ladefehlerFachwahlen.get(abiturjahr),
					"FEHLER: Die GOSt-Fachwahlstatistik des Abiturjahrgangs %d konnte nicht ermittelt werden.".formatted(abiturjahr));
		}
		return fachwahlen;
	}

	/**
	 * Gibt die GOSt-Fachwahlstatistik für den übergebenen Abiturjahrgang zurück, ohne bei einem Ladefehler abzubrechen.
	 * <p><b>Optionaler Zugriff:</b> Die Fachwahl-Anzahlen aus der Laufbahnplanung reichern hier eine andere Ausgabe an - in der Kursstatistik eines
	 * Blockungsergebnisses die Spalte der Wahlen je Fach und Kursart; die übrigen Statistikwerte dieser Ausgabe stammen aus dem Blockungsergebnis selbst. Ein
	 * gescheiterter Ladevorgang wird als fehlende Teildaten gemeldet und die Ausgabe ohne diese Werte fortgesetzt; allein eine Infrastrukturstörung bricht
	 * über die Meldefassade ab. Wer die Statistik als Hauptinhalt benötigt, verwendet {@link #fachwahlen(int)}.</p>
	 *
	 * @param abiturjahr Das Abiturjahr des Jahrgangs.
	 *
	 * @return Liste der Fachwahl-Statistikeinträge des Abiturjahrgangs; leer, wenn der Jahrgang keine Fachwahlen hat oder das Laden gescheitert ist.
	 */
	public List<GostStatistikFachwahl> fachwahlenOptional(final int abiturjahr) {
		ladeFachwahlen(abiturjahr);
		final List<GostStatistikFachwahl> fachwahlen = mapFachwahlen.get(abiturjahr);
		if (fachwahlen == null) {
			ReportingRepositoryUtils.meldeTeildatenLadefehler(this.reportingContext,
					ReportingProblemSchluessel.fuer(GostStatistikFachwahl.class, abiturjahr),
					"Die GOSt-Fachwahlstatistik des Abiturjahrgangs %d".formatted(abiturjahr), ladefehlerFachwahlen.get(abiturjahr));
			return List.of();
		}
		return fachwahlen;
	}

	/**
	 * Lädt die Fachwahlstatistik des Abiturjahrgangs in den Cache, falls sie dort noch fehlt. Ein gescheiterter Ladevorgang hinterlässt den Fehler-Marker
	 * {@code null} samt festgehaltenem Fehler; die Bewertung des Fehlschlags liegt bei den beiden Zugriffen, weil nur sie die Rolle der Statistik im
	 * jeweiligen Report kennen.
	 *
	 * @param abiturjahr Das Abiturjahr des Jahrgangs.
	 */
	private void ladeFachwahlen(final int abiturjahr) {
		if (mapFachwahlen.containsKey(abiturjahr)) {
			return;
		}
		try {
			final List<GostStatistikFachwahl> fachwahlen =
					GostServiceFactoryBuilder.getGostServiceFactory().getGostJahrgangFachwahlService().getFachwahlStatistik(abiturjahr);
			mapFachwahlen.put(abiturjahr, (fachwahlen != null) ? fachwahlen : List.of());
		} catch (final Exception e) {
			mapFachwahlen.put(abiturjahr, null);
			ladefehlerFachwahlen.put(abiturjahr, e);
		}
	}


	// ##### Gleichwertige Komplexe Lernleistungen (GKL, Abitur ab 2030) #####

	/**
	 * Liefert die Wahlen zu den Gleichwertigen Komplexen Lernleistungen (GKL) des übergebenen Schülers. Beim ersten Zugriff
	 * werden die Daten für alle bekannten Schüler gesammelt nachgeladen und im Cache abgelegt.
	 *
	 * @param idSchueler Die ID des Schülers.
	 *
	 * @return Die GKL-Wahlen des Schülers. Enthält ausschließlich {@code null}-Werte, falls für den Schüler keine Wahlen
	 *         hinterlegt sind oder das Laden fehlgeschlagen ist; der Fehlschlag wird zusätzlich als Ausgabeproblem gemeldet.
	 */
	public GostSchuelerGKLWahl gklWahl(final long idSchueler) {
		final List<Long> ids = new ArrayList<>(this.reportingContext.repositorySchueler().idsGeladenerSchueler());
		ids.add(idSchueler);
		ReportingRepositoryUtils.ladeFehlendeWerteInRepositoryMap(
				ids,
				mapGklWahlen,
				this::ladeGklWahlen,
				"GOSt-GKL-Wahlen",
				this.reportingContext.logger(),
				ladefehlerGklWahlen);
		if (ladefehlerGklWahlen.containsKey(idSchueler)) {
			ReportingRepositoryUtils.meldeTeildatenLadefehler(this.reportingContext,
					ReportingProblemSchluessel.fuer(GostSchuelerGKLWahl.class, idSchueler),
					"Die GKL-Wahlen des Schülers %d".formatted(idSchueler), ladefehlerGklWahlen.get(idSchueler));
		}
		final GostSchuelerGKLWahl wahl = mapGklWahlen.get(idSchueler);
		return (wahl != null) ? wahl : new GostSchuelerGKLWahl();
	}

	/**
	 * Lädt die GKL-Wahlen zu den übergebenen Schüler-IDs mit einer gesammelten Datenbankabfrage. Für Schüler ohne
	 * GOSt-Eintrag wird ein leeres Wahl-Objekt hinterlegt, damit die IDs nicht erneut abgefragt werden.
	 *
	 * @param idsSchueler Die IDs der Schüler, deren GKL-Wahlen geladen werden sollen.
	 *
	 * @return Map mit Schüler-ID als Schlüssel und den zugehörigen GKL-Wahlen als Wert.
	 */
	private Map<Long, GostSchuelerGKLWahl> ladeGklWahlen(final List<Long> idsSchueler) {
		final Map<Long, DTOGostSchueler> dtos = this.reportingContext.conn()
				.queryList(DTOGostSchueler.QUERY_LIST_BY_SCHUELER_ID, DTOGostSchueler.class, idsSchueler)
				.stream().collect(Collectors.toMap(dto -> dto.Schueler_ID, dto -> dto));
		final Map<Long, GostSchuelerGKLWahl> result = new HashMap<>();
		for (final Long id : idsSchueler) {
			final GostSchuelerGKLWahl wahl = new GostSchuelerGKLWahl();
			wahl.idSchueler = id;
			final DTOGostSchueler dto = dtos.get(id);
			if (dto != null) {
				wahl.idKlausurvorgabeEF_Sprachen = dto.GKL_EF_AF1_Klausurvorgabe_ID;
				wahl.idKlausurvorgabeEF_GW = dto.GKL_EF_AF2_Klausurvorgabe_ID;
				wahl.idKlausurvorgabeEF_NW = dto.GKL_EF_AF3_Klausurvorgabe_ID;
				wahl.idKlausurvorgabeQ_Sprachen = dto.GKL_Q_AF1_Klausurvorgabe_ID;
				wahl.idKlausurvorgabeQ_GW = dto.GKL_Q_AF2_Klausurvorgabe_ID;
				wahl.idKlausurvorgabeQ_NW = dto.GKL_Q_AF3_Klausurvorgabe_ID;
			}
			result.put(id, wahl);
		}
		return result;
	}

	/**
	 * Liefert zu den übergebenen IDs die zugehörigen GOSt-Klausurvorgaben. Die Daten werden bei erstem Zugriff aus der
	 * Datenbank geladen und im Cache gehalten.
	 *
	 * @param idsKlausurvorgaben Die IDs der Klausurvorgaben.
	 *
	 * @return Map mit der ID der Klausurvorgabe als Schlüssel und der zugehörigen Klausurvorgabe als Wert. IDs, zu denen
	 *         keine Klausurvorgabe gefunden werden konnte, fehlen in der Map; ein gescheiterter Zugriff wird zusätzlich
	 *         als Ausgabeproblem gemeldet.
	 */
	public Map<Long, GostKlausurvorgabe> klausurvorgaben(final List<Long> idsKlausurvorgaben) {
		ReportingRepositoryUtils.ladeFehlendeWerteInRepositoryMap(
				idsKlausurvorgaben,
				mapKlausurvorgaben,
				ids -> gostKlausurenVorgabeService().getListByIds(ids).stream()
						.collect(Collectors.toMap(vorgabe -> vorgabe.id, vorgabe -> vorgabe)),
				"GOSt-Klausurvorgaben",
				this.reportingContext.logger(),
				ladefehlerKlausurvorgaben);
		if (idsKlausurvorgaben != null) {
			for (final Long id : idsKlausurvorgaben) {
				if ((id != null) && ladefehlerKlausurvorgaben.containsKey(id)) {
					ReportingRepositoryUtils.meldeTeildatenLadefehler(this.reportingContext,
							ReportingProblemSchluessel.fuer(GostKlausurvorgabe.class, id),
							"Die GOSt-Klausurvorgabe %d".formatted(id), ladefehlerKlausurvorgaben.get(id));
				}
			}
		}
		return filtereMap(idsKlausurvorgaben, mapKlausurvorgaben);
	}

	/**
	 * Erstellt einen neuen Service für den Zugriff auf die GOSt-Klausurvorgaben. Anders als die übrigen GOSt-Services in
	 * dieser Klasse wird dieser Service nicht über {@link GostServiceFactoryBuilder} bezogen, da dessen
	 * {@code GostServiceFactory} ihn nicht anbietet — stattdessen wird dieselbe {@link GostKlausurenServiceFactory}
	 * genutzt, über die auch der Klausurplanungs-Controller den Service erzeugt.
	 *
	 * @return der Service
	 */
	private static GostKlausurenVorgabeService gostKlausurenVorgabeService() {
		return GostKlausurenServiceFactoryBuilder.getGostKlausurenServiceFactory().getGostKlausurenVorgabeService();
	}


	// ##### Hilfsmethoden #####

	/**
	 * Liefert für die übergebenen IDs eine neue Map mit den zugehörigen Werten aus dem Cache. {@code null}-IDs sowie IDs
	 * ohne Cache-Eintrag (Wert {@code null}) werden übersprungen. Wird {@code null} als ID-Liste übergeben, ist das
	 * Ergebnis eine leere Map.
	 *
	 * @param <V>   Der Wertetyp der Cache-Map.
	 * @param ids   Die IDs, deren Cache-Einträge in das Ergebnis übernommen werden sollen.
	 * @param cache Die Cache-Map, aus der die Werte gelesen werden.
	 *
	 * @return Eine neue Map mit den ID/Wert-Paaren der vorhandenen Cache-Einträge.
	 */
	private static <V> Map<Long, V> filtereMap(final List<Long> ids, final Map<Long, V> cache) {
		final Map<Long, V> result = new HashMap<>();
		if (ids == null) {
			return result;
		}
		for (final Long id : ids) {
			if (id == null) {
				continue;
			}
			final V wert = cache.get(id);
			if (wert != null) {
				result.put(id, wert);
			}
		}
		return result;
	}
}
