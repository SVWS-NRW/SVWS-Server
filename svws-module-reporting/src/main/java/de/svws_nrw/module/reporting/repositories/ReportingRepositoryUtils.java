package de.svws_nrw.module.reporting.repositories;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.LongFunction;
import java.util.function.Predicate;
import java.util.function.ToLongFunction;

import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.module.reporting.diagnose.ReportingAuswahlergebnis;
import de.svws_nrw.module.reporting.diagnose.ReportingLadezustand;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemSchluessel;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemauswirkung;
import de.svws_nrw.module.reporting.diagnose.ReportingProblemursache;
import de.svws_nrw.module.reporting.utils.ReportingExceptionUtils;

/**
 * Generische Utility-Klasse zum gesammelten Nachladen fehlender Einträge in Repository-Maps
 * sowie zum Erstellen von Listen aus Reporting-Objekten basierend auf IDs.
 */
final class ReportingRepositoryUtils {

	private ReportingRepositoryUtils() {
	}

	/**
	 * Lädt die Stammdaten zu den übergebenen IDs, erzeugt daraus die Reporting-Objekte und liefert die vollständige Auswahl: neben den erzeugten Objekten
	 * auch die IDs, die dabei ausgelassen oder ausgefiltert wurden. Beide Angaben entstehen nur hier - nach außen ist eine ausgelassene ID nicht mehr von
	 * einer ausgefilterten zu unterscheiden, und genau diese Unterscheidung entscheidet über die Meldung eines Ausgabeproblems.
	 * <p>Die Ursache folgt dem Zustand der Stammdaten-Map: Ein Eintrag mit dem Wert {@code null} ist der Fehler-Marker, den
	 * {@link #ladeFehlendeWerteInRepositoryMap} für eine nicht ladbare ID hinterlässt; ein fehlender Eintrag bedeutet, dass es den Datensatz nicht gibt.</p>
	 *
	 * @param <S> Der Typ der Stammdaten (z. B. LehrerStammdaten, SchuelerStammdaten)
	 * @param <R> Der Typ der Reporting-Objekte (z.B. ReportingLehrer, ReportingSchueler)
	 * @param ids Die Liste der IDs, für die Reporting-Objekte erstellt werden sollen
	 * @param mapStammdaten Die Map mit bereits geladenen Stammdaten
	 * @param mapReportingObjekte Eine Map mit bereits erstellten Reporting-Objekten
	 * @param stammdatenLoader Funktion zum Laden fehlender Stammdaten aus der DB (liefert eine Liste)
	 * @param reportingObjektErsteller Funktion zum Erstellen eines Reporting-Objekts aus Stammdaten
	 * @param idExtractor Funktion zum Extrahieren der ID aus einem Stammdaten-Objekt
	 * @param comparator Comparator für die Sortierung (niemals {@code null}; ein Identitäts-Comparator lässt die Reihenfolge unverändert)
	 * @param filter Predicate, das nur akzeptierte Objekte in die Auswahl lässt; ist {@code null}, wird nicht gefiltert.
	 *               Die Stammdaten- und Reporting-Objekt-Maps werden vollständig befüllt — die Filterung wirkt nur auf die Auswahl.
	 * @param datentyp Bezeichnung des Datentyps für Fehlermeldungen (z. B. "Lehrer", "Schüler")
	 * @param logger Der Logger für Fehlermeldungen
	 * @param ladefehler Die Fehler gescheiterter Ladevorgänge je ID. Die Map gehört dem Repository und lebt so lange wie dessen Cache — andernfalls stünde
	 *                   beim zweiten Zugriff auf dieselbe ID zwar noch der Fehler-Marker im Cache, der Fehler dazu aber nicht mehr.
	 *
	 * @return Das Auswahlergebnis mit den erzeugten Objekten, den ausgelassenen und den ausgefilterten IDs.
	 */
	@SuppressWarnings("java:S107")
	public static <S, R> ReportingAuswahlergebnis<R> waehleAus(final List<Long> ids, final Map<Long, S> mapStammdaten,
			final Map<Long, R> mapReportingObjekte, final Function<List<Long>, List<S>> stammdatenLoader, final LongFunction<R> reportingObjektErsteller,
			final ToLongFunction<S> idExtractor, final Comparator<R> comparator, final Predicate<R> filter, final String datentyp, final Logger logger,
			final Map<Long, Exception> ladefehler) {

		final List<Long> idsBereinigt = ((ids == null) ? List.<Long>of() : ids).stream().filter(Objects::nonNull).distinct().toList();
		final List<Long> idsAufloesbar = idsBereinigt.stream().filter(id -> id >= 0).toList();
		if (idsAufloesbar.isEmpty()) {
			// Eine negative ID benennt keinen Datensatz und wird wie eine unbekannte behandelt.
			return ReportingAuswahlergebnis.aus(idsBereinigt, Map.of(), zustaendeNichtVorhanden(idsBereinigt), List.of());
		}

		// Wir adaptieren den Listen-Loader zu einem Map-Loader, damit wir die einheitliche Lademethode nutzen können
		final Function<List<Long>, Map<Long, S>> mapLoader = fehlendeIds -> {
			final Map<Long, S> resultMap = new HashMap<>();
			for (final S data : stammdatenLoader.apply(fehlendeIds)) {
				if (data != null) {
					resultMap.put(idExtractor.applyAsLong(data), data);
				}
			}
			return resultMap;
		};

		// Nachladen aller Daten über die nun einzige Lademethode. Der Fehler eines gescheiterten Einzelzugriffs wird dabei festgehalten und nicht
		// protokolliert: Er gehört in die Meldung des Ausgabeproblems, die daraus entsteht.
		ladeFehlendeWerteInRepositoryMap(idsAufloesbar, mapStammdaten, mapLoader, datentyp, logger, ladefehler);

		final List<R> erzeugt = erzeugeReportingObjekte(idsAufloesbar, mapStammdaten, mapReportingObjekte, reportingObjektErsteller);
		final Map<R, Long> idsJeObjekt = idsJeObjekt(idsAufloesbar, mapReportingObjekte);

		// Erst filtern, dann sortieren: Die Comparatoren greifen auf nachladende Getter zu - etwa den Lernabschnitt eines Schülers oder das Fach eines Kurses.
		// In umgekehrter Reihenfolge löste die Sortierung diese Zugriffe auch für Datensätze aus, die der Anwender gar nicht sehen will, samt der dabei
		// möglichen Logeinträge und Fehler.
		final List<R> akzeptiert = new ArrayList<>();
		final List<Long> idsAusgefiltert = new ArrayList<>();
		for (final R objekt : erzeugt) {
			if ((filter == null) || filter.test(objekt)) {
				akzeptiert.add(objekt);
			} else {
				idsAusgefiltert.add(idsJeObjekt.get(objekt));
			}
		}
		final List<R> sortiert = akzeptiert.stream().sorted(comparator).toList();

		return baueAuswahl(idsBereinigt, mapStammdaten, idsJeObjekt, sortiert, idsAusgefiltert, ladefehler);
	}

	/**
	 * Ordnet jedem erzeugten Reporting-Objekt seine ID zu. Verglichen wird über die Objektidentität und nicht über {@code equals}: Zwei fachlich gleiche
	 * Reporting-Objekte blieben sonst ununterscheidbar, und die Auswahl schriebe beide unter derselben ID fort.
	 *
	 * @param <R>                 Der Typ der Reporting-Objekte.
	 * @param idsAufloesbar       Die auflösbaren IDs.
	 * @param mapReportingObjekte Die Map der erstellten Reporting-Objekte.
	 *
	 * @return Die Zuordnung von Objekt auf ID.
	 */
	private static <R> Map<R, Long> idsJeObjekt(final List<Long> idsAufloesbar, final Map<Long, R> mapReportingObjekte) {
		final Map<R, Long> idsJeObjekt = new IdentityHashMap<>();
		for (final Long id : idsAufloesbar) {
			final R objekt = mapReportingObjekte.get(id);
			if (objekt != null) {
				idsJeObjekt.put(objekt, id);
			}
		}
		return idsJeObjekt;
	}

	/**
	 * Stellt das Auswahlergebnis aus den ausgewählten Objekten und dem Zustand der Stammdaten-Map zusammen.
	 *
	 * @param <S> Der Typ der Stammdaten.
	 * @param <R> Der Typ der Reporting-Objekte.
	 * @param idsBereinigt Die angeforderten IDs ohne Duplikate und leere Einträge.
	 * @param mapStammdaten Die Map mit den geladenen Stammdaten.
	 * @param idsJeObjekt Die Zuordnung von Reporting-Objekt auf seine ID.
	 * @param sortiert Die ausgewählten Objekte in Ausgabereihenfolge, bereits gefiltert.
	 * @param idsAusgefiltert Die vom Benutzerfilter ausgeschlossenen IDs.
	 * @param fehlerJeId Die Fehler gescheiterter Einzelzugriffe je ID.
	 *
	 * @return Das Auswahlergebnis.
	 */
	private static <S, R> ReportingAuswahlergebnis<R> baueAuswahl(final List<Long> idsBereinigt, final Map<Long, S> mapStammdaten,
			final Map<R, Long> idsJeObjekt, final List<R> sortiert, final List<Long> idsAusgefiltert, final Map<Long, Exception> fehlerJeId) {

		final LinkedHashMap<Long, R> ausgewaehlt = new LinkedHashMap<>();
		for (final R objekt : sortiert) {
			ausgewaehlt.put(idsJeObjekt.get(objekt), objekt);
		}

		// Als Menge, damit die Prüfung je angeforderter ID nicht linear über die Liste läuft: Ein Report über alle Schüler einer Schule fordert tausende
		// IDs an, und mit einem Benutzerfilter stünde ein Großteil davon in dieser Liste.
		final Set<Long> idsAusgefiltertMenge = Set.copyOf(idsAusgefiltert);

		final LinkedHashMap<Long, ReportingLadezustand<R>> ausgelassen = new LinkedHashMap<>();
		for (final Long id : idsBereinigt) {
			if (ausgewaehlt.containsKey(id) || idsAusgefiltertMenge.contains(id)) {
				continue;
			}
			// Der Zustand entsteht aus der Stammdaten-Map: Ein Eintrag ohne Wert ist der Fehler-Marker eines gescheiterten Ladevorgangs, ein fehlender Eintrag
			// bedeutet, dass es den Datensatz nicht gibt. Der Wert des Zustands bleibt ungenutzt - hier zählt allein, warum das Objekt fehlt.
			final ReportingLadezustand<S> zustandStammdaten = zustaendeAus(List.of(id), mapStammdaten, fehlerJeId).get(id);
			ausgelassen.put(id, zustandStammdaten.istGeladen()
					? ReportingLadezustand.nichtVorhanden()
					: ReportingLadezustand.uebernimmUrsache(zustandStammdaten));
		}

		return ReportingAuswahlergebnis.aus(idsBereinigt, ausgewaehlt, ausgelassen, idsAusgefiltert);
	}

	/**
	 * Bildet alle übergebenen IDs auf den Zustand "nicht vorhanden" ab.
	 *
	 * @param <R> Der Typ der Reporting-Objekte.
	 * @param ids Die IDs.
	 *
	 * @return Die Zuordnung von ID auf Zustand.
	 */
	private static <R> Map<Long, ReportingLadezustand<R>> zustaendeNichtVorhanden(final List<Long> ids) {
		final LinkedHashMap<Long, ReportingLadezustand<R>> zustaende = new LinkedHashMap<>();
		for (final Long id : ids) {
			zustaende.put(id, ReportingLadezustand.nichtVorhanden());
		}
		return zustaende;
	}

	/**
	 * Stellt sicher, dass für die übergebenen IDs Einträge in der Repository-Map existieren.
	 * Schlägt das gesammelte Laden fehl, wird ein Fallback ausgelöst, bei dem alle noch fehlenden IDs
	 * einzeln geladen werden, um fehlerhafte Datensätze zu isolieren.
	 *
	 * @param <T>               Der Typ der Werte in der Repository-Map.
	 * @param ids               Die Menge an IDs, für die Daten beschafft werden sollen.
	 * @param repositoryMap     Die Repository-Map, in die geladene Werte eingetragen werden.
	 * @param bulkLoader        Eine Funktion, die eine Liste von IDs entgegennimmt und eine Map mit Ergebnissen liefert.
	 * @param datenbezeichnung  Eine sprechende Bezeichnung der Daten für Log-Meldungen.
	 * @param logger            Logger für Fehler- und Info-Meldungen.
	 */
	public static <T> void ladeFehlendeWerteInRepositoryMap(final Collection<Long> ids, final Map<Long, T> repositoryMap,
			final Function<List<Long>, Map<Long, T>> bulkLoader, final String datenbezeichnung, final Logger logger) {
		ladeFehlendeWerteInRepositoryMap(ids, repositoryMap, bulkLoader, datenbezeichnung, logger, null);
	}

	/**
	 * Wie {@link #ladeFehlendeWerteInRepositoryMap(Collection, Map, Function, String, Logger)}, hält den Fehler eines endgültig gescheiterten Einzelzugriffs
	 * jedoch je ID fest, statt ihn allein zu protokollieren. Der Fehler-Marker {@code put(id, null)} in der Repository-Map sagt nur, dass das Laden
	 * scheiterte; wer daraus ein Ausgabeproblem meldet, braucht auch Fehlertyp und Stacktrace.
	 * <p>Mit Rückkanal unterbleibt der Logeintrag des Einzelfehlers: Er entsteht dedupliziert bei der Meldung des Ausgabeproblems. Der Fehler des
	 * gesammelten Zugriffs wird weiterhin protokolliert, denn er ist kein Ausgabeproblem, solange die Einzelzugriffe gelingen.</p>
	 * <p>Der Rückkanal muss so lange leben wie die Repository-Map: Ihr Fehler-Marker verhindert jeden weiteren Ladeversuch, sodass eine aufrufbezogene Map
	 * beim zweiten Zugriff auf dieselbe ID nichts mehr vom Fehler wüsste.</p>
	 *
	 * @param <T>               Der Typ der Werte in der Repository-Map.
	 * @param ids               Die Menge an IDs, für die Daten beschafft werden sollen.
	 * @param repositoryMap     Die Repository-Map, in die geladene Werte eingetragen werden.
	 * @param bulkLoader        Eine Funktion, die eine Liste von IDs entgegennimmt und eine Map mit Ergebnissen liefert.
	 * @param datenbezeichnung  Eine sprechende Bezeichnung der Daten für Log-Meldungen.
	 * @param logger            Logger für Fehler- und Info-Meldungen.
	 * @param fehlerJeId        Nimmt den Fehler eines endgültig gescheiterten Einzelzugriffs je ID auf, oder {@code null}.
	 */
	public static <T> void ladeFehlendeWerteInRepositoryMap(final Collection<Long> ids, final Map<Long, T> repositoryMap,
			final Function<List<Long>, Map<Long, T>> bulkLoader, final String datenbezeichnung, final Logger logger,
			final Map<Long, Exception> fehlerJeId) {

		if ((ids == null) || ids.isEmpty()) {
			return;
		}
		final List<Long> fehlendeIds = ids.stream().filter(Objects::nonNull).distinct().filter(id -> !repositoryMap.containsKey(id)).toList();
		if (fehlendeIds.isEmpty()) {
			return;
		}

		try {
			// Versuch: Bulk-Load
			repositoryMap.putAll(bulkLoader.apply(fehlendeIds));
		} catch (final Exception e) {
			ReportingExceptionUtils.logException(
					"INFO: Fehler beim gesammelten Laden von " + datenbezeichnung + ". Versuche einzelnes Laden für fehlende IDs.", e, logger,
					LogLevel.INFO, 0);

			// Fallback: Jede fehlende ID einzeln probieren
			for (final Long id : fehlendeIds) {
				try {
					repositoryMap.putAll(bulkLoader.apply(List.of(id)));
				} catch (final Exception exSingle) {
					if (fehlerJeId == null) {
						ReportingExceptionUtils.logException(
								"INFO: Fehler beim einzelnen Laden von " + datenbezeichnung + " für ID " + id + ".", exSingle, logger, LogLevel.INFO, 0);
					} else {
						fehlerJeId.put(id, exSingle);
					}
					// Markieren, dass für diese ID das Laden fehlschlug, um Endlosschleifen bei erneuter Anfrage zu vermeiden
					repositoryMap.put(id, null);
				}
			}
		}
	}

	/**
	 * Leitet aus einer Repository-Map den Ladezustand je angeforderter ID ab. Die Map allein trennt die beiden Fälle des Fehlens nicht: Ein Eintrag mit dem
	 * Wert {@code null} ist der Fehler-Marker eines gescheiterten Zugriffs, ein fehlender Eintrag bedeutet, dass es den Datensatz nicht gibt. Wer beides
	 * gleich behandelt, macht aus einer Störung eine fachlich fehlende Akte.
	 *
	 * <p>Die Ursache eines Fehlschlags stammt aus {@link ReportingProblemursache#fuerLadefehler}: Ein eindeutiger Verbindungsfehler wird zur
	 * Infrastrukturstörung, alles Übrige bleibt datensatzbezogen. Ohne festgehaltenen Fehler bleibt es beim datensatzbezogenen Ladefehler.</p>
	 *
	 * @param <T>          Der Typ der Werte in der Repository-Map.
	 * @param ids          Die angeforderten IDs.
	 * @param repositoryMap Die Repository-Map nach dem Laden.
	 * @param fehlerJeId   Die Fehler gescheiterter Ladevorgänge je ID, oder {@code null}. Sie muss so lange leben wie die Repository-Map.
	 *
	 * @return Die Zuordnung von ID auf Ladezustand, in der Reihenfolge der angeforderten IDs.
	 */
	public static <T> Map<Long, ReportingLadezustand<T>> zustaendeAus(final Collection<Long> ids, final Map<Long, T> repositoryMap,
			final Map<Long, Exception> fehlerJeId) {
		final LinkedHashMap<Long, ReportingLadezustand<T>> zustaende = new LinkedHashMap<>();
		if (ids == null) {
			return zustaende;
		}
		for (final Long id : ids) {
			if (id == null) {
				continue;
			}
			final T wert = repositoryMap.get(id);
			if (wert != null) {
				zustaende.put(id, ReportingLadezustand.geladen(wert));
			} else if (repositoryMap.containsKey(id)) {
				final Exception fehler = (fehlerJeId == null) ? null : fehlerJeId.get(id);
				zustaende.put(id, ReportingLadezustand.fehlgeschlagen(ReportingProblemursache.fuerLadefehler(fehler), fehler));
			} else {
				zustaende.put(id, ReportingLadezustand.nichtVorhanden());
			}
		}
		return zustaende;
	}

	/**
	 * Stellt sicher, dass für die übergebenen IDs Einträge für Listen-Daten in der Map existieren.
	 * Anders als bei normalen Werten, wird bei fehlenden Ergebnissen immer eine leere Liste hinterlegt,
	 * sodass Konsumenten nie {@code null} sehen.
	 *
	 * @param <T>               Der Element-Typ der Listen in der Repository-Map.
	 * @param ids               Die Menge an IDs, für die Listen-Daten beschafft werden sollen.
	 * @param repositoryMap     Die Repository-Map, in die geladene Listen eingetragen werden.
	 * @param bulkLoader        Eine Funktion, die eine Liste von IDs entgegennimmt und eine Map mit Ergebnislisten liefert.
	 * @param datenbezeichnung  Eine sprechende Bezeichnung der Daten für Log-Meldungen.
	 * @param logger            Logger für Fehler- und Info-Meldungen.
	 */
	public static <T> void ladeFehlendeListenInRepositoryMap(final Collection<Long> ids, final Map<Long, List<T>> repositoryMap,
			final Function<List<Long>, Map<Long, List<T>>> bulkLoader, final String datenbezeichnung, final Logger logger) {
		ladeFehlendeListenInRepositoryMap(ids, repositoryMap, bulkLoader, datenbezeichnung, logger, null);
	}

	/**
	 * Wie {@link #ladeFehlendeListenInRepositoryMap(Collection, Map, Function, String, Logger)}, hält den Fehler eines endgültig gescheiterten Einzelzugriffs
	 * jedoch je ID fest, statt ihn allein zu protokollieren. Bei Listen ist dieser Rückkanal die einzige Spur des Fehlers: Eine nicht ladbare ID erhält die
	 * leere Liste, damit Konsumenten nie {@code null} sehen, und ist damit in der Repository-Map nicht mehr von einer ID ohne fachliche Daten zu unterscheiden.
	 * <p>Mit Rückkanal unterbleibt der Logeintrag des Einzelfehlers: Er entsteht dedupliziert bei der Meldung des Ausgabeproblems. Der Fehler des gesammelten
	 * Zugriffs wird weiterhin protokolliert, und der Rückkanal muss so lange leben wie die Repository-Map, weil diese nach dem ersten Versuch jedes weitere
	 * Laden verhindert.</p>
	 *
	 * @param <T>               Der Element-Typ der Listen in der Repository-Map.
	 * @param ids               Die Menge an IDs, für die Listen-Daten beschafft werden sollen.
	 * @param repositoryMap     Die Repository-Map, in die geladene Listen eingetragen werden.
	 * @param bulkLoader        Eine Funktion, die eine Liste von IDs entgegennimmt und eine Map mit Ergebnislisten liefert.
	 * @param datenbezeichnung  Eine sprechende Bezeichnung der Daten für Log-Meldungen.
	 * @param logger            Logger für Fehler- und Info-Meldungen.
	 * @param fehlerJeId        Nimmt den Fehler eines endgültig gescheiterten Einzelzugriffs je ID auf, oder {@code null}.
	 */
	public static <T> void ladeFehlendeListenInRepositoryMap(final Collection<Long> ids, final Map<Long, List<T>> repositoryMap,
			final Function<List<Long>, Map<Long, List<T>>> bulkLoader, final String datenbezeichnung, final Logger logger,
			final Map<Long, Exception> fehlerJeId) {

		if ((ids == null) || ids.isEmpty()) {
			return;
		}
		final List<Long> fehlendeIds = ids.stream().filter(Objects::nonNull).distinct().filter(id -> !repositoryMap.containsKey(id)).toList();
		if (fehlendeIds.isEmpty()) {
			return;
		}

		try {
			repositoryMap.putAll(bulkLoader.apply(fehlendeIds));
			// Falls die DB für einige IDs keine Liste geliefert hat, setzen wir eine leere Liste als Fallback
			for (final Long id : fehlendeIds) {
				repositoryMap.putIfAbsent(id, new ArrayList<>());
			}
		} catch (final Exception e) {
			ReportingExceptionUtils.logException(
					"INFO: Fehler beim gesammelten Laden von " + datenbezeichnung + ". Versuche einzelnes Laden für fehlende IDs.", e, logger,
					LogLevel.INFO, 0);
			ladeFehlendeListenEinzeln(fehlendeIds, repositoryMap, bulkLoader, datenbezeichnung, logger, fehlerJeId);
		}
	}

	/**
	 * Lädt nach einem gescheiterten gesammelten Zugriff jede fehlende ID einzeln, um fehlerhafte Datensätze zu isolieren. Eine ID, deren Einzelzugriff
	 * ebenfalls scheitert, erhält die leere Liste; ihr Fehler wird über den Rückkanal festgehalten oder, falls keiner übergeben wurde, protokolliert.
	 *
	 * @param <T>               Der Element-Typ der Listen in der Repository-Map.
	 * @param fehlendeIds       Die IDs, die der gesammelte Zugriff nicht laden konnte.
	 * @param repositoryMap     Die Repository-Map, in die geladene Listen eingetragen werden.
	 * @param bulkLoader        Eine Funktion, die eine Liste von IDs entgegennimmt und eine Map mit Ergebnislisten liefert.
	 * @param datenbezeichnung  Eine sprechende Bezeichnung der Daten für Log-Meldungen.
	 * @param logger            Logger für Fehler- und Info-Meldungen.
	 * @param fehlerJeId        Nimmt den Fehler eines endgültig gescheiterten Einzelzugriffs je ID auf, oder {@code null}.
	 */
	private static <T> void ladeFehlendeListenEinzeln(final List<Long> fehlendeIds, final Map<Long, List<T>> repositoryMap,
			final Function<List<Long>, Map<Long, List<T>>> bulkLoader, final String datenbezeichnung, final Logger logger,
			final Map<Long, Exception> fehlerJeId) {
		for (final Long id : fehlendeIds) {
			try {
				final List<T> daten = bulkLoader.apply(List.of(id)).get(id);
				repositoryMap.put(id, (daten != null) ? daten : new ArrayList<>());
			} catch (final Exception exSingle) {
				if (fehlerJeId == null) {
					ReportingExceptionUtils.logException(
							"INFO: Fehler beim einzelnen Laden von " + datenbezeichnung + " für ID " + id + ". Setze leere Liste.", exSingle, logger,
							LogLevel.INFO, 0);
				} else {
					fehlerJeId.put(id, exSingle);
				}
				repositoryMap.put(id, new ArrayList<>());
			}
		}
	}

	/**
	 * Erstellt für alle IDs mit vorhandenen Stammdaten ein Reporting-Objekt und cached es in der übergebenen Map.
	 * Nutzt `putIfAbsent` um Fehler bei zirkulären Abhängigkeiten (z.B. Klasse -> Schüler -> Klasse) zu vermeiden.
	 * <p>Maßgeblich ist der tatsächlich vorhandene Wert, nicht der Schlüssel: {@link #ladeFehlendeWerteInRepositoryMap} hinterlegt für eine technisch nicht
	 * ladbare ID den Fehler-Marker {@code put(id, null)}. Dieser erfüllt zwar {@code containsKey}, steht aber für "keine Stammdaten". Würde er als Datensatz
	 * gewertet, erhielte der Ersteller {@code null}-Stammdaten und liefe in eine {@link NullPointerException}. Der betroffene Datensatz wird stattdessen
	 * ausgelassen.</p>
	 *
	 * @param <S> Der Typ der Stammdaten (z. B. LehrerStammdaten, SchuelerStammdaten)
	 * @param <R> Der Typ des Reporting-Objektes (z. B. LehrerReporting, SchuelerReporting)
	 * @param idsNonNull Die Liste der IDs für die Reporting-Objekte erstellt werden sollen
	 * @param mapStammdaten Die Map mit den Stammdaten
	 * @param mapReportingObjekte Die Map für das Caching der Reporting-Objekte
	 * @param reportingObjektErsteller Die Funktion zum Erstellen des Reporting-Objektes
	 *
	 * @return Die Liste der erstellten Reporting-Objekte
	 */
	private static <S, R> List<R> erzeugeReportingObjekte(final List<Long> idsNonNull, final Map<Long, S> mapStammdaten,
			final Map<Long, R> mapReportingObjekte, final LongFunction<R> reportingObjektErsteller) {

		final List<R> result = new ArrayList<>();
		idsNonNull.stream().filter(id -> mapStammdaten.get(id) != null).forEach(id -> {
			if (!mapReportingObjekte.containsKey(id)) {
				mapReportingObjekte.putIfAbsent(id, reportingObjektErsteller.apply(id));
			}
			result.add(mapReportingObjekte.get(id));
		});
		return result;
	}

	/**
	 * Meldet für jede ausgelassene ID der Auswahl, deren Laden gescheitert ist, ein Ausgabeproblem. Die Listen-Zugriffe der Repositories geben nur die
	 * Objekte heraus; ohne diese Meldung bliebe ein endgültig gescheiterter Einzelzugriff völlig still, weil der Rückkanal des Ladefehlers dessen Logeintrag
	 * unterdrückt. Eine ID, die es nicht gibt, und eine vom Benutzerfilter ausgeschlossene werden nicht gemeldet: Dort hat der Zugriff einwandfrei gearbeitet.
	 *
	 * @param <T>              Der Reporting-Typ der ausgewählten Objekte.
	 * @param reportingContext Context mit Parametern, Logger und Daten-Cache zur Report-Generierung.
	 * @param auswahl          Das Auswahlergebnis des Listen-Zugriffs.
	 * @param objektart        Die Objektart für den Schlüssel des Ausgabeproblems.
	 * @param bezeichnung      Die Benennung eines Datensatzes im Genitiv für den Logeintrag, etwa "der Lehrkraft"; die ID wird angehängt.
	 */
	static <T> void meldeFehlgeschlageneAuslassungen(final ReportingContext reportingContext, final ReportingAuswahlergebnis<T> auswahl,
			final Class<?> objektart, final String bezeichnung) {
		for (final Map.Entry<Long, ReportingLadezustand<T>> ausgelassen : auswahl.ausgelassen().entrySet()) {
			if (ausgelassen.getValue().ursache().istFehlschlagUrsache()) {
				meldeTeildatenLadefehler(reportingContext, ReportingProblemSchluessel.fuer(objektart, ausgelassen.getKey()),
						"Die Daten %s %d".formatted(bezeichnung, ausgelassen.getKey()), ausgelassen.getValue().fehler());
			}
		}
	}

	/**
	 * Meldet ein Ausgabeproblem für Daten, deren Laden gescheitert ist und die deshalb in der Ausgabe fehlen. Die Ursache stammt aus
	 * {@link ReportingProblemursache#fuerLadefehler}: In aller Regel ist sie datensatzbezogen und wird hingenommen - der betroffene Datensatz erscheint
	 * weiterhin, ihm fehlen allein diese Daten. Zeigt der Fehler dagegen eine abgerissene Verbindung, bricht die Fassade die Ausgabe ab.
	 * <p>Alle Repositories melden über diese Stelle, damit die Wortwahl im Log einheitlich bleibt. Ein eigener Logaufruf daneben führte denselben Fehler
	 * zweimal im Log, und mit {@code ERROR} gäbe er einem hingenommenen Befund die Dringlichkeit eines Abbruchs.</p>
	 *
	 * @param reportingContext Context mit Parametern, Logger und Daten-Cache zur Report-Generierung.
	 * @param schluessel       Welche Daten welches Datensatzes betroffen sind.
	 * @param bezeichnung      Die Benennung der ausgelassenen Daten für den Logeintrag, etwa "Die Erzieherdaten des Schülers 42".
	 * @param fehler           Der Fehler, an dem das Laden gescheitert ist.
	 */
	static void meldeTeildatenLadefehler(final ReportingContext reportingContext, final ReportingProblemSchluessel schluessel, final String bezeichnung,
			final Exception fehler) {
		reportingContext.meldeAusgabeproblem(ReportingProblemursache.fuerLadefehler(fehler), ReportingProblemauswirkung.TEILDATEN_FEHLEN,
				schluessel, "%s konnten nicht geladen werden und fehlen in der Ausgabe.".formatted(bezeichnung), fehler);
	}

	/**
	 * Meldet ein Ausgabeproblem, wenn zu der übergebenen ID ein Ladefehler festgehalten ist. Liegt keiner vor, geschieht nichts.
	 * <p>Diese Prüfung gehört zu jedem Zugriff, der {@link #ladeFehlendeWerteInRepositoryMap} eine Fehlermap mitgibt: Die Utility protokolliert einen
	 * endgültig gescheiterten Einzelzugriff dann nicht mehr selbst, und ohne die Meldung verschwände der Fehler spurlos.</p>
	 * <p>Gemeldet wird bei jedem Zugriff, denn welcher der erste ist, hängt an der Reportvorlage - die Deduplizierung macht daraus einen Befund und einen
	 * Logeintrag.</p>
	 *
	 * @param reportingContext Context mit Parametern, Logger und Daten-Cache zur Report-Generierung.
	 * @param ladefehler       Die Fehler gescheiterter Ladevorgänge dieser Teildaten je ID.
	 * @param id               Die ID des Datensatzes, dessen Teildaten fehlen.
	 * @param objektart        Die Objektart der ausgelassenen Teildaten für den Schlüssel des Ausgabeproblems.
	 * @param bezeichnung      Die Benennung der ausgelassenen Daten für den Logeintrag, etwa "Die Erzieherdaten des Schülers 42".
	 */
	static void meldeTeildatenLadefehler(final ReportingContext reportingContext, final Map<Long, Exception> ladefehler, final long id,
			final Class<?> objektart, final String bezeichnung) {
		if (!ladefehler.containsKey(id)) {
			return;
		}
		meldeTeildatenLadefehler(reportingContext, ReportingProblemSchluessel.fuer(objektart, id), bezeichnung, ladefehler.get(id));
	}
}
