package de.svws_nrw.module.reporting.html.contexts;

import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.sortierung.SortierungRegistryReportingKlasse;
import de.svws_nrw.module.reporting.sortierung.SortierungRegistryReportingKurs;
import de.svws_nrw.module.reporting.sortierung.SortierungRegistryReportingLehrer;
import de.svws_nrw.module.reporting.sortierung.SortierungRegistryReportingSchueler;
import de.svws_nrw.module.reporting.sortierung.SortierungRegistryReportingStundenplanungFachStundenplan;
import de.svws_nrw.module.reporting.sortierung.SortierungRegistryReportingStundenplanungKlasseStundenplan;
import de.svws_nrw.module.reporting.sortierung.SortierungRegistryReportingStundenplanungLehrerStundenplan;
import de.svws_nrw.module.reporting.sortierung.SortierungRegistryReportingStundenplanungRaumStundenplan;
import de.svws_nrw.module.reporting.sortierung.SortierungRegistryReportingStundenplanungSchuelerStundenplan;
import de.svws_nrw.module.reporting.types.klasse.ReportingKlasse;
import de.svws_nrw.module.reporting.types.kurs.ReportingKurs;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungFachStundenplan;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungKlasseStundenplan;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungLehrerStundenplan;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungRaumStundenplan;
import de.svws_nrw.module.reporting.types.stundenplanung.ReportingStundenplanungSchuelerStundenplan;
import de.svws_nrw.module.reporting.utils.ReportingExceptionUtils;
import org.thymeleaf.context.Context;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Abstrakte Basisklasse für die Thymeleaf-html-Daten-Contexts.
 *
 * @param <T> der Datentyp der vom Context verwalteten Elemente in contextData
 */
public abstract class HtmlContext<T> {

	/** Der zu diesem Context gehörige Thymeleaf-Context. */
	private Context context;

	/** Generische Liste der Context-Daten. */
	private List<T> contextData = new ArrayList<>();

	/** Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	private final ReportingRepository reportingRepository;

	/** Legt fest, ob der Context als Haupt-Daten-Context verwendet werden soll (true) oder als Detail-Daten-Conetxt (false). */
	private boolean istHauptdatenContext = true;

	/** Legt fest, ob die Standardsortierung verwendet des entsprechenden Types werden soll. */
	private boolean verwendeStandardsortierung = true;

	/** Liste von Attributnamen, nach denen der Context sortiert werden soll. */
	private List<String> sortierungAttribute = new ArrayList<>();


	/**
	 * Konstruktor für die Klasse HtmlContext, der das Repository und den Flag zur Hauptdatenquelle initialisiert.
	 *
	 * @param reportingRepository Das Reporting-Repository, welches verwendet wird, um die Daten zu verwalten.
	 * @param istHauptdatenContext Ein Boolean-Wert, der angibt, ob der aktuelle Kontext ein Hauptdatenkontext ist.
	 */
	protected HtmlContext(final ReportingRepository reportingRepository, final Boolean istHauptdatenContext) {
		this.reportingRepository = reportingRepository;
		this.istHauptdatenContext = istHauptdatenContext;
		if (this.istHauptdatenContext) {
			this.verwendeStandardsortierung = reportingRepository.reportingParameter().sortierungHauptdaten.verwendeStandardsortierung;
			setSortierungAttribute(this.reportingRepository.reportingParameter().sortierungHauptdaten.attribute);
		} else {
			this.verwendeStandardsortierung = reportingRepository.reportingParameter().sortierungDetaildaten.verwendeStandardsortierung;
			setSortierungAttribute(this.reportingRepository.reportingParameter().sortierungDetaildaten.attribute);
		}
	}



	// ### Getter und Setter-Methoden ###

	/**
	 * Rückgabe des Thymeleaf-Daten-Context
	 *
	 * @return Gibt einen Thymeleaf-Daten-Context zurück.
	 */
	public Context getContext() {
		return context;
	}

	/**
	 * Setzen des Thymeleaf-Daten-Context
	 *
	 * @param context Thymeleaf-Daten-Context mit den Daten.
	 */
	public void setContext(final Context context) {
		this.context = context;
	}

	/**
	 * Gibt die Context-Daten zurück.
	 *
	 * @return die Liste der in diesem Context verwalteten Daten
	 */
	public List<T> getContextData() {
		return contextData;
	}

	/**
	 * Setzt die Context-Daten. Null-Werte werden herausgefiltert. Bei Übergabe von null wird eine leere Liste gesetzt.
	 *
	 * @param data die zu setzende Datenliste
	 */
	public void setContextData(final List<T> data) {
		if (data == null) {
			this.contextData = new ArrayList<>();
			return;
		}
		this.contextData = new ArrayList<>(data.stream().filter(Objects::nonNull).toList());
	}

	/**
	 * Gibt die Liste der Sortierungsattribute zurück, die definiert wurden, um den Kontext entsprechend zu sortieren.
	 *
	 * @return Eine Liste von Attributnamen, die für die Sortierung verwendet werden.
	 */
	public List<String> getSortierungAttribute() {
		return sortierungAttribute;
	}

	/**
	 * Legt die Sortierungsattribute für den Kontext fest. Null-Werte und leere Strings
	 * in der übergebenen Liste der Attribute werden herausgefiltert. Wenn die gefilterte Liste leer ist
	 * oder null übergeben wird, wird eine leere Liste gesetzt.
	 *
	 * @param sortierungAttribute Liste von Attributnamen, die für die Sortierung des Kontexts verwendet werden sollen. Null-Werte und leere Strings werden
	 * ignoriert.
	 */
	public void setSortierungAttribute(final List<String> sortierungAttribute) {
		if ((sortierungAttribute == null) || sortierungAttribute.isEmpty()) {
			this.sortierungAttribute = new ArrayList<>();
			return;
		}

		final List<String> filterSortierungsattribute =
				new ArrayList<>(sortierungAttribute.stream()
						.filter(Objects::nonNull)
						.map(sa -> sa.replace("()", "").trim())
						.filter(sa -> !sa.isBlank())
						.toList());

		if (!filterSortierungsattribute.isEmpty())
			this.sortierungAttribute = filterSortierungsattribute;
		else
			this.sortierungAttribute = new ArrayList<>();
	}



	// ### Methoden zur Sortierung der Context-Daten ###

	/**
	 * Sortiert die im Context gespeicherten Daten (contextData) basierend auf den im Context definierten Sortierungsattributen.
	 * Die Methode filtert null-Werte aus contextData heraus. Für Strings wird die deutsche Sortierreihenfolge angewandt.
	 * Falls die Attribute nicht gefunden werden, werden sie für die Sortierung ignoriert.
	 */
	protected void sortiereContextMitRegistry() {
		if ((this.contextData == null) || this.contextData.isEmpty())
			return;
		final List<T> sorted = sortiereListeMitRegistry(this.contextData);
		this.contextData = (sorted == null) ? new ArrayList<>() : sorted;
	}

	/**
	 * Sortiert eine Liste von Objekten basierend auf den im Kontext definierten Sortierungsattributen,
	 * die durch eine Sortierung-Registry bereitgestellt werden.
	 * Sofern eine Sortierung möglich ist, werden Null-Werte aus der übergebenen Liste in der sortierten Ergebnisliste entfernt,
	 * aber falls die Sortierungsattribute leer sind oder nicht gültig sind und so eine Sortierung nicht möglich ist,
	 * wird die ursprüngliche Liste ohne Änderungen zurückgegeben.
	 *
	 * @param list Die Liste von Objekten, die sortiert werden sollen. Null-Werte in der Liste
	 *             werden herausgefiltert. Wenn die Liste null ist, wird ebenfalls null zurückgegeben.
	 * @return Die sortierte Liste, ggf. bereinigt um null-Werte. Falls die Eingabeliste oder die Sortierungsattribute null
	 *         oder leer sind oder keine gültigen Attribute für die Sortierung gefunden werden konnten,
	 *         wird die unveränderte Eingabeliste zurückgegeben.
	 */
	protected List<T> sortiereListeMitRegistry(final List<T> list) {
		if ((list == null) || (this.sortierungAttribute.isEmpty() && !this.verwendeStandardsortierung))
			return list;

		final List<T> listNonNull = new ArrayList<>(list.stream().filter(Objects::nonNull).toList());

		if (listNonNull.isEmpty())
			return list;

		// Erstelle eine Liste für evtl. fehlerhaft übergebene Sortierkriterien.
		final List<String> validierungsfehler = new ArrayList<>();

		switch (listNonNull.getFirst()) {
			case final ReportingSchueler ignore -> {
				return sortiereMitComparatorAusRegistry(list, listNonNull, this.verwendeStandardsortierung,
						() -> SortierungRegistryReportingSchueler.buildComparatorStandard(validierungsfehler),
						() -> SortierungRegistryReportingSchueler.buildComparator(this.sortierungAttribute, validierungsfehler), "ReportingSchueler",
						validierungsfehler);
			}
			case final ReportingLehrer ignore -> {
				return sortiereMitComparatorAusRegistry(list, listNonNull, this.verwendeStandardsortierung,
						() -> SortierungRegistryReportingLehrer.buildComparatorStandard(validierungsfehler),
						() -> SortierungRegistryReportingLehrer.buildComparator(this.sortierungAttribute, validierungsfehler), "ReportingLehrer",
						validierungsfehler);
			}
			case final ReportingKlasse ignore -> {
				return sortiereMitComparatorAusRegistry(list, listNonNull, this.verwendeStandardsortierung,
						() -> SortierungRegistryReportingKlasse.buildComparatorStandard(validierungsfehler),
						() -> SortierungRegistryReportingKlasse.buildComparator(this.sortierungAttribute, validierungsfehler), "ReportingKlasse",
						validierungsfehler);
			}
			case final ReportingKurs ignore -> {
				return sortiereMitComparatorAusRegistry(list, listNonNull, this.verwendeStandardsortierung,
						() -> SortierungRegistryReportingKurs.buildComparatorStandard(validierungsfehler),
						() -> SortierungRegistryReportingKurs.buildComparator(this.sortierungAttribute, validierungsfehler), "ReportingKurs",
						validierungsfehler);
			}
			case final ReportingStundenplanungFachStundenplan ignore -> {
				return sortiereMitComparatorAusRegistry(list, listNonNull, this.verwendeStandardsortierung,
						() -> SortierungRegistryReportingStundenplanungFachStundenplan.buildComparatorStandard(validierungsfehler),
						() -> SortierungRegistryReportingStundenplanungFachStundenplan.buildComparator(this.sortierungAttribute, validierungsfehler),
						"ReportingStundenplanungFachStundenplan", validierungsfehler);
			}
			case final ReportingStundenplanungKlasseStundenplan ignore -> {
				return sortiereMitComparatorAusRegistry(list, listNonNull, this.verwendeStandardsortierung,
						() -> SortierungRegistryReportingStundenplanungKlasseStundenplan.buildComparatorStandard(validierungsfehler),
						() -> SortierungRegistryReportingStundenplanungKlasseStundenplan.buildComparator(this.sortierungAttribute, validierungsfehler),
						"ReportingStundenplanungKlasseStundenplan", validierungsfehler);
			}
			case final ReportingStundenplanungLehrerStundenplan ignore -> {
				return sortiereMitComparatorAusRegistry(list, listNonNull, this.verwendeStandardsortierung,
						() -> SortierungRegistryReportingStundenplanungLehrerStundenplan.buildComparatorStandard(validierungsfehler),
						() -> SortierungRegistryReportingStundenplanungLehrerStundenplan.buildComparator(this.sortierungAttribute, validierungsfehler),
						"ReportingStundenplanungLehrerStundenplan", validierungsfehler);
			}
			case final ReportingStundenplanungRaumStundenplan ignore -> {
				return sortiereMitComparatorAusRegistry(list, listNonNull, this.verwendeStandardsortierung,
						() -> SortierungRegistryReportingStundenplanungRaumStundenplan.buildComparatorStandard(validierungsfehler),
						() -> SortierungRegistryReportingStundenplanungRaumStundenplan.buildComparator(this.sortierungAttribute, validierungsfehler),
						"ReportingStundenplanungRaumStundenplan", validierungsfehler);
			}
			case final ReportingStundenplanungSchuelerStundenplan ignore -> {
				return sortiereMitComparatorAusRegistry(list, listNonNull, this.verwendeStandardsortierung,
						() -> SortierungRegistryReportingStundenplanungSchuelerStundenplan.buildComparatorStandard(validierungsfehler),
						() -> SortierungRegistryReportingStundenplanungSchuelerStundenplan.buildComparator(this.sortierungAttribute, validierungsfehler),
						"ReportingStundenplanungSchuelerStundenplan", validierungsfehler);
			}
			case null, default -> {
				return list;
			}
		}
	}

	/**
	 * Sortiert eine Liste basierend auf einem Comparator, der entweder aus einer Standard- oder einer benutzerdefinierten
	 * Registry bezogen wird. Falls kein Comparator erstellt werden kann, wird die ursprüngliche Liste zurückgegeben
	 * und entsprechende Informationen ins Log geschrieben. Ungültige Attribute zur Sortierung werden ebenfalls ins Log
	 * geschrieben.
	 *
	 * @param <X> Der Elementtyp, für den der übergebene Comparator definiert ist (und der damit den tatsächlichen Typ der Listeneinträge beschreibt).
	 * @param listOriginal Die ursprüngliche Liste, die zurückgegeben wird, falls kein Comparator definiert werden konnte.
	 * @param listNonNull Die Liste ohne null-Werte, die sortiert werden soll.
	 * @param verwendeStandardsortierung Gibt an, ob die Standardsortierung verwendet werden soll.
	 * @param supplierComparatorStandard Supplier für den standardmäßigen Comparator.
	 * @param supplierComparatorBenutzerdefiniert Supplier für den benutzerdefinierten Comparator.
	 * @param typeName Der Typname, für den der Comparator gesucht wird (wird im Log verwendet).
	 * @param validierungsfehler Liste der Attributnamen, die nicht validiert werden konnten und ins Log geschrieben werden.
	 * @return Die sortierte Liste, sofern ein Comparator verfügbar war. Andernfalls die ursprüngliche Liste.
	 */
	private <X> List<T> sortiereMitComparatorAusRegistry(final List<T> listOriginal, final List<T> listNonNull, final boolean verwendeStandardsortierung,
			final Supplier<Comparator<X>> supplierComparatorStandard, final Supplier<Comparator<X>> supplierComparatorBenutzerdefiniert,
			final String typeName, final List<String> validierungsfehler) {

		final Comparator<X> comparator = verwendeStandardsortierung ? supplierComparatorStandard.get() : supplierComparatorBenutzerdefiniert.get();
		if (comparator == null) {
			if (validierungsfehler.isEmpty())
				validierungsfehler.add("- keine -");
			ReportingExceptionUtils.putInfoInLog(
					("INFO: Es konnte kein Comparator für %s erstellt werden. Zudem wurden folgende Attribute zur Sortierung "
							+ "übergeben, die nicht in der Registry definiert wurden: %s.")
							.formatted(typeName, String.join(", ", validierungsfehler)),
					reportingRepository.logger(), LogLevel.INFO, 0
			);
			return listOriginal;
		}
		typisierteSortierung(listNonNull, comparator);

		// Falls Attribute ungültig waren, werden diese ins Log geschrieben.
		if (!validierungsfehler.isEmpty()) {
			ReportingExceptionUtils.putInfoInLog(
					"INFO: Es wurden folgende Attribute zur Sortierung übergeben, die nicht in der Registry definiert wurden: "
							+ String.join(", ", validierungsfehler),
					reportingRepository.logger(), LogLevel.INFO, 0);
		}

		return listNonNull;
	}

	/**
	 * Sortiert eine Liste von Objekten basierend auf einem angegebenen Comparator.
	 * Die Methode führt einen kontrollierten Cast durch, um die Liste zur Typisierung zu bringen,
	 * und ruft anschließend die Sortierfunktion der Liste mit dem bereitgestellten Comparator auf.
	 *
	 * @param <X> Der Typ der Objekte, die innerhalb der Liste erwartet werden, auf die der Comparator angewandt wird.
	 * @param liste Die Liste von Objekten, die sortiert werden soll. Die Liste darf nicht null sein.
	 * @param comparator Der Comparator, der zur Bestimmung der Sortierfolge für die Liste verwendet wird.
	 *                   Der Comparator darf nicht null sein.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static <X> void typisierteSortierung(final List<?> liste, final Comparator<X> comparator) {
		final List typisierteListe = (List) liste;
		typisierteListe.sort((Comparator) comparator);
	}
}
