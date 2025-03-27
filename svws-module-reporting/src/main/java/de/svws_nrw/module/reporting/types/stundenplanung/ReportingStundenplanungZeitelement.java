package de.svws_nrw.module.reporting.types.stundenplanung;


import java.util.Objects;

import de.svws_nrw.core.types.Wochentag;
import de.svws_nrw.module.reporting.types.ReportingBaseType;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse repräsentiert ein zeitbasiertes Element wie {@link ReportingStundenplanungUnterrichtsrasterstunde} im Rahmen der Stundenplanung.
 */
public class ReportingStundenplanungZeitelement extends ReportingBaseType implements Comparable<ReportingStundenplanungZeitelement> {

	/** Die Uhrzeit in Minuten seit 0 Uhr, wann diese dieses Element beginnt. NULL bedeutet "noch nicht definiert". */
	protected Integer beginn;

	/** Die Uhrzeit in Minuten seit 0 Uhr, dieses Element endet. NULL bedeutet "noch nicht definiert". */
	protected Integer ende;

	/** Der {@link Wochentag} an dem dieses Element liegt. */
	protected Wochentag wochentag;


	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 *
	 * @param beginn    Die Uhrzeit in Minuten seit 0 Uhr, wann dieses Element beginnt. NULL bedeutet "noch nicht definiert".
	 * @param ende		Die Uhrzeit in Minuten seit 0 Uhr, wann dieses Element endet. NULL bedeutet "noch nicht definiert".
	 * @param wochentag	Der {@link Wochentag} an dem dieses Element liegt.
	 */
	public ReportingStundenplanungZeitelement(final Integer beginn, final Integer ende, final Wochentag wochentag) {
		this.beginn = beginn;
		this.ende = ende;
		this.wochentag = wochentag;
	}


	// ##### Hash und Equals und Compare-Methoden #####

	/**
	 * Hashcode der Klasse
	 * @return Hashcode der Klasse
	 */
	public int hashCode() {
		return 31 + Objects.hash(beginn, ende, wochentag.id);
	}

	/**
	 * Equals der Klasse
	 * @param obj Das Vergleichsobjekt
	 * @return	true, falls es das gleiche Objekt ist, andernfalls false.
	 */
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof final ReportingStundenplanungZeitelement other))
			return false;
		return (Objects.equals(beginn, other.beginn)) && (Objects.equals(ende, other.ende))
				&& (Objects.equals(wochentag.id, other.wochentag.id));
	}

	/**
	 * Vergleicht dieses ReportingStundenplanungZeitelement mit einem anderen,
	 * um eine sortierte Ordnung zu bestimmen. Der Vergleich erfolgt basierend
	 * auf dem Wochentag sowie dem Beginn- und Endzeitpunkt.
	 *
	 * @param zeitelement Das zu vergleichende ReportingStundenplanungZeitelement.
	 * @return Einen negativen Wert, wenn dieses Element vor dem angegebenen Element liegt,
	 *         einen positiven Wert, wenn es danach liegt,
	 *         oder 0, wenn beide Elemente in ihrer Anordnung gleich sind.
	 */
	@Override
	public int compareTo(final @NotNull ReportingStundenplanungZeitelement zeitelement) {
		// Eine null beim Wochentag wird als "vor Montag" gewertet.
		final int thisWochentag = (this.wochentag == null) ? -1 : this.wochentag.id;
		final int zeitelementWochentag = (zeitelement.wochentag == null) ? -1 : zeitelement.wochentag.id;

		// Vergleiche die Wochentage.
		if (thisWochentag < zeitelementWochentag)
			return -1;
		if (thisWochentag > zeitelementWochentag)
			return 1;

		// Falls vorhanden, dann werden null-Werte als "vor Beginn" oder "nach Ende" des 1440 Minuten Zeitraumes gewertet.
		final int thisStart = (this.beginn == null) ? -1 : this.beginn;
		final int zeitelementStart = (zeitelement.beginn == null) ? -1 : zeitelement.beginn;

		if (thisStart < zeitelementStart)
			return -1;
		if (thisStart > zeitelementStart)
			return 1;

		// Falls vorhanden, dann werden null-Werte als "vor Beginn" oder "nach Ende" des 1440 Minuten Zeitraumes gewertet.
		final int thisEnde = (this.ende == null) ? 1441 : this.ende;
		final int zeitelementEnde = (zeitelement.ende == null) ? 1441 : zeitelement.ende;

		// Ob hier ist Beginn gleichzeitig mit Vergleichselement vorausgesetzt. Vergleich richtet sich nach Endzeitpunkt.
		return Integer.compare(thisEnde, zeitelementEnde);
	}

	/**
	 * Vergleicht dieses ReportingStundenplanungZeitElement mit einem anderen, um eine sortierte Ordnung zu bestimmen.
	 * Der Vergleich erfolgt basierend auf dem Wochentag sowie dem Beginn- und Endzeitpunkt.
	 * Der Rückgabewerte kann wie folgt interpretiert werden.
	 * -8 = Wochentag dieses Objekts liegt vor dem übergebenen Zeitelement.
	 * -4 = Gleicher Wochentag, dieses Objekt beginnt und endet vor dem Beginn des übergebenen Zeitelements.
	 * -3 = Gleicher Wochentag, dieses Objekt beginnt vor dem Beginn des übergebenen Zeitelements und endet in ihm.
	 * -2 = Gleicher Wochentag, dieses Objekt beginnt vor dem Beginn des übergebenen Zeitelements und endet nach ihm.
	 * -1 = Gleicher Wochentag, dieses Objekt beginnt gleichzeitig mit dem übergebenen Zeitelement und endet vor ihm.
	 *  0 = Beide Element stimmen in Wochentag, Beginn und Ende überein.
	 * +1 = Gleicher Wochentag, dieses Objekt beginnt gleichzeitig mit dem übergebenen Zeitelement und endet nach ihm.
	 * +2 = Gleicher Wochentag, dieses Objekt beginnt nach dem Beginn des übergebenen Zeitelements und endet in ihm.
	 * +3 = Gleicher Wochentag, dieses Objekt beginnt nach dem Beginn des übergebenen Zeitelements und endet nach ihm.
	 * +4 = Gleicher Wochentag, dieses Objekt beginnt und endet nach dem Beginn des übergebenen Zeitelements.
	 * +8 = Wochentag dieses Objekts liegt nach dem übergebenen Zeitelement.
	 *
	 * @param zeitelement Das zu vergleichende ReportingStundenplanungZeitElement.
	 * @return Einen negativen Wert, wenn dieses Element vor dem angegebenen Element liegt,
	 *         einen positiven Wert, wenn es danach liegt,
	 *         oder 0, wenn beide Elemente in ihrer Anordnung gleich sind.
	 */
	private int compareToDetail(final @NotNull ReportingStundenplanungZeitelement zeitelement) {
		// Eine null beim Wochentag als "vor Montag" gewertet.
		final int thisWochentag = (this.wochentag == null) ? -1 : this.wochentag.id;
		final int zeitelementWochentag = (zeitelement.wochentag == null) ? -1 : zeitelement.wochentag.id;

		// Vergleiche die Wochentage.
		if (thisWochentag < zeitelementWochentag)
			return -8;
		if (thisWochentag > zeitelementWochentag)
			return 8;

		// Falls vorhanden, dann werden null-Werte als "vor Beginn" oder "nach Ende" des 1440 Minuten Zeitraumes gewertet.
		final int thisStart = (this.beginn == null) ? -1 : this.beginn;
		final int thisEnde = (this.ende == null) ? 1441 : this.ende;
		final int zeitelementStart = (zeitelement.beginn == null) ? -1 : zeitelement.beginn;
		final int zeitelementEnde = (zeitelement.ende == null) ? 1441 : zeitelement.ende;

		// Im Folgenden stimmen die Objekte im Wochentag schon überein.

		// Vergleiche die Lage der Elemente zueinander anhand von Start und Ende.
		if ((thisStart == zeitelementStart) && (thisEnde == zeitelementEnde))
			return 0;

		if (thisStart < zeitelementStart) {
			// Beginn vor dem Vergleichselement.
			if (thisEnde <= zeitelementStart)
				// Endet vor Beginn des Vergleichselements.
				return -4;
			if (thisEnde <= zeitelementEnde)
				// Endet im Vergleichselements.
				return -3;
			else
				// Endet nach dem Ende des Vergleichselements.
				return -2;
		} else if (thisStart >= zeitelementEnde) {
			// Beginn nach dem Vergleichselement.
			return 4;
		} else if (thisStart > zeitelementStart) {
			// Beginn im Vergleichselement.
			if (thisEnde <= zeitelementEnde)
				return 2;
			else
				return 3;
		} else {
			// Beginn gleichzeitig mit Vergleichselement.
			if (thisEnde < zeitelementEnde)
				// Endet vor dem Vergleichselement
				return -1;
			else
				// Endet nach dem Vergleichselement
				return 1;
		}
	}


	// ##### Berechnete Methoden

	/**
	 * Prüft, ob dieses Zeitelement teilweise in einem anderen Zeitelement enthalten ist.
	 *
	 * @param zeitelement Das zu prüfende ReportingStundenplanungZeitelement, in dem
	 *                    dieses Zeitelement enthalten sein könnte.
	 * @return true, wenn dieses Zeitelement teilweise im angegebenen Zeitelement
	 *         enthalten ist, sonst false.
	 */
	public boolean istTeilweiseEnthaltenInZeitelement(final ReportingStundenplanungZeitelement zeitelement) {
		final int compare = compareToDetail(zeitelement);
		return ((compare >= -3) && (compare <= 3));
	}

	/**
	 * Prüft, ob dieses Zeitelement vollständig in einem anderen Zeitelement enthalten ist.
	 *
	 * @param zeitelement Das zu prüfende ReportingStundenplanungZeitelement, in dem
	 *                    dieses Zeitelement enthalten sein könnte.
	 * @return true, wenn dieses Zeitelement vollständig im angegebenen Zeitelement
	 *         enthalten ist, sonst false.
	 */
	public boolean istVollstaendigEnthaltenInZeitelement(final ReportingStundenplanungZeitelement zeitelement) {
		final int compare = compareToDetail(zeitelement);
		return ((compare == -1) || (compare == 0) || (compare == 2));
	}


	// ##### Getter #####

	/**
	 * Berechnet die Dauer des Zeitelements in Minuten basierend auf den definierten
	 * Start- und Endzeiten. Wenn sowohl der Beginn als auch das Ende nicht definiert sind,
	 * wird die maximale Tagesdauer von 1440 Minuten zurückgegeben.
	 * Wenn entweder der Beginn oder das Ende nicht definiert ist, wird die verbleibende
	 * oder vergangene Tageszeit in Minuten berechnet, basierend auf der gültigen definierten Zeit.
	 *
	 * @return Die Dauer in Minuten. 0, wenn keine Zeiten definiert sind.
	 *         Wenn nur die Endzeit definiert ist, wird diese zurückgegeben.
	 *         Wenn nur die Startzeit definiert ist, wird der verbleibende Tageszeitraum (1440 - Startzeit) zurückgegeben.
	 *         Andernfalls die Differenz zwischen Endzeit und Startzeit.
	 */
	public int dauerInMinuten() {
		if ((beginn == null) && (ende == null))
			return 0;
		if (beginn == null)
			return ende;
		if (ende == null)
			return 1440 - beginn;
		return ende - beginn;
	}

	/**
	 * Liefert die Zeit in Minuten seit 0 Uhr, wann dieses Element beginnt.
	 *
	 * @return Die Minuten seit 0 Uhr oder NULL, wenn nicht definiert.
	 */
	public Integer beginn() {
		return beginn;
	}

	/**
	 * Liefert die Zeit in Minuten seit 0 Uhr, wann dieses Element endet.
	 *
	 * @return Die Minuten seit 0 Uhr oder NULL, wenn nicht definiert.
	 */
	public Integer ende() {
		return ende;
	}

	/**
	 * Liefert den Wochentag, an dem dieses Element liegt.
	 *
	 * @return Der Wochentag dieses Elements.
	 */
	public Wochentag wochentag() {
		return wochentag;
	}
}
