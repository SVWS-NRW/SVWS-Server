package de.svws_nrw.module.reporting.types.gost.klausurplanung;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;


/**
 * <p>Basis-Klasse im Rahmen des Reportings für Daten vom Typ GostKlausurplanungKlausurplan.</p>
 * <p>Sie enthält die Daten zu einem Klausurplan, d. h. beispielsweise zu den Terminen.</p>
 * <p>Diese Klasse ist als reiner Datentyp konzipiert, d. h. sie hat keine Anbindung an die Datenbank. Sie dient als Super-Klasse
 * einer Proxy-Klasse, welche die Getter in Teilen überschreibt und dort die Daten aus der Datenbank nachlädt.</p>
 */
public class ReportingGostKlausurplanungKlausurplan {

	/** Eine Liste vom Typ GostKlausurplanungTermin, die alle Termine des Klausurplanes beinhaltet. */
	protected List<ReportingGostKlausurplanungKlausurtermin> klausurtermine;



	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 * @param klausurtermine	Eine Liste vom Typ GostKlausurplanungTermin, die alle Termine des Klausurplanes beinhaltet.
	 */
	public ReportingGostKlausurplanungKlausurplan(final List<ReportingGostKlausurplanungKlausurtermin> klausurtermine) {
		this.klausurtermine = klausurtermine;
	}


	// ##### Berechnete Methoden #####

	/**
	 * Eine Liste vom Typ String, die alle vorhandenen Datumsangaben der Termine des Klausurplanes beinhaltet (distinct).
	 * @return Liste der Datumsangaben der Klausurtermine
	 */
	public List<String> datumsangabenKlausurtermine() {
		return this.klausurtermine.stream().filter(t -> Objects.nonNull(t.datum())).map(t -> t.datum).sorted().distinct().toList();
	}

	/**
	 * Eine Liste vom Typ GostKlausurplanungKlausurtermin, die alle Termine des Klausurplanes beinhaltet, denen bereits ein Datum zugewiesen wurde.
	 * @return Liste der Klausurtermine mit Datumsangabe
	 */
	public List<ReportingGostKlausurplanungKlausurtermin> klausurtermineMitDatum() {
		return this.klausurtermine.stream().filter(t -> Objects.nonNull(t.datum())).toList();
	}

	/**
	 * Eine Liste vom Typ GostKlausurplanungKlausurtermin, die alle Termine des Klausurplanes beinhaltet, denen noch kein Datum zugewiesen wurde.
	 * @return Liste der Klausurtermine ohne Datumsangabe
	 */
	public List<ReportingGostKlausurplanungKlausurtermin> klausurtermineOhneDatum() {
		return this.klausurtermine.stream().filter(t -> Objects.isNull(t.datum())).toList();
	}

	/**
	 * Eine Liste vom Typ GostKlausurplanungKlausurtermin, die alle Termine des Klausurplanes zum angegebenen Datum beinhaltet.
	 * @param  datum 	Datum, zu dem die Liste der Klausurtermine zurückgegeben werden soll.
	 * @return 			Liste der Klausurtermine mit dem gewünschten Datum
	 */
	public List<ReportingGostKlausurplanungKlausurtermin> klausurtermineZumDatum(final String datum) {
		if ((datum == null) || datum.isEmpty())
			return new ArrayList<>();
		return this.klausurtermine.stream().filter(t -> datum.equals(t.datum()))
				.sorted(Comparator
						.comparing(ReportingGostKlausurplanungKlausurtermin::gostHalbjahr)
						.thenComparing(ReportingGostKlausurplanungKlausurtermin::startuhrzeit))
				.toList();
	}


	// ##### Getter #####

	/**
	 * Eine Liste vom Typ GostKlausurplanungTermin, die alle Termine des Klausurplanes beinhaltet.
	 * @return Liste der Klausurtermine
	 */
	public List<ReportingGostKlausurplanungKlausurtermin> klausurtermine() {
		return this.klausurtermine;
	}

}
