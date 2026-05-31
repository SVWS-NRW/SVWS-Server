package de.svws_nrw.module.reporting.html.contexts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.sortierung.ReportingSortierung;
import org.thymeleaf.context.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

	/** Context mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	protected final ReportingContext reportingContext;


	/**
	 * Konstruktor für die Klasse HtmlContext.
	 *
	 * @param reportingContext Der Reporting-Context, der verwendet wird, um die Daten zu verwalten.
	 */
	protected HtmlContext(final ReportingContext reportingContext) {
		this.reportingContext = reportingContext;
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
	 * Die IDs der Daten (z. B. Schüler-/Lehrer-/Klassen-/Kurs-IDs), die dieser Context für nachgelagerte Schritte (z. B. E-Mail-Versand) bereitstellt.
	 * Der Standard ist eine leere Liste. Abgeleitete Kontexte überschreiben diese Methode, um die konkreten IDs zu ermitteln.
	 *
	 * @return Die Liste der IDs. Standard ist eine leere Liste.
	 */
	public List<Long> getIds() {
		return new ArrayList<>();
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
	 * Sortiert die übergebene Liste über {@link HtmlContextSortierung#sortiere} und übernimmt das Ergebnis als Context-Daten.
	 * Null-Werte werden herausgefiltert. Bei Übergabe von null wird eine leere Liste gesetzt.
	 *
	 * @param data       die zu sortierende und zu setzende Datenliste
	 * @param sortierung die Sortierkonfiguration des Reporting-Typs
	 * @param typ        die Klasse des Reporting-Typs (für das Auflösen der ReportParameter-Sortierung)
	 */
	protected void setContextDataSortiert(final List<T> data, final ReportingSortierung<T> sortierung, final Class<T> typ) {
		this.contextData = HtmlContextSortierung.sortiere(reportingContext, data, sortierung, typ);
	}

}
