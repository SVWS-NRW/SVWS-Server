
package de.svws_nrw.module.reporting.types.schule;

import java.util.List;

import de.svws_nrw.module.reporting.types.ReportingBaseType;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Basis-Klasse im Rahmen des Reportings für Daten vom Typ ReportingNRWSchulkatalogEintrag.
 */
@XmlRootElement
public class ReportingSchuleBasisdatenNRW extends ReportingBaseType {

	/** Die unter Umständen max. dreizeilige Bezeichnung der Schule. */
	protected List<String> bezeichnung;

	/** Die Mailadresse der Schule. */
	protected String email;

	/** Die Faxnummer der Schule. */
	protected String fax;

	/** Die Hausnummer zur Straße, in der die Schule liegt. */
	protected String hausnummer;

	/** Der Ort, in dem die Schule liegt. */
	protected String ort;

	/** Die Postleitzahl des Gebietes an, in dem die Schule liegt. */
	protected String plz;

	/** Die eindeutige Schulnummer der Schule */
	protected long schulnummer;

	/** Der Straßenname der Straße an, in der die Schule liegt. */
	protected String strassenname;

	/** Die Telefonnummer der Schule. */
	protected String telefon;

	/** Ggf. der Hausnummernzusatz der Straße, in der die Schule liegt. */
	protected String hausnummerZusatz;

	/**
	 * Konstruktor zur Initialisierung eines Eintrags im Reporting NRW Schulkatalog.
	 *
	 * @param bezeichnung Die unter Umständen max. dreizeilige Bezeichnung der Schule.
	 * @param email Die Mailadresse der Schule.
	 * @param fax Die Faxnummer der Schule.
	 * @param hausnummer Die Hausnummer zur Straße, in der die Schule liegt.
	 * @param ort Der Ort, in dem die Schule liegt.
	 * @param plz Die Postleitzahl des Gebietes, in dem die Schule liegt.
	 * @param schulnummer Die eindeutige Schulnummer der Schule
	 * @param strassenname Der Straßenname der Straße, in der die Schule liegt.
	 * @param telefon Die Telefonnummer der Schule.
	 * @param hausnummerZusatz Ggf. der Hausnummernzusatz der Straße, in der die Schule liegt.
	 */
	public ReportingSchuleBasisdatenNRW(final List<String> bezeichnung, final String email, final String fax,
			final String hausnummer, final String ort, final String plz, final long schulnummer, final String strassenname, final String telefon,
			final String hausnummerZusatz) {
		this.bezeichnung = bezeichnung;
		this.email = email;
		this.fax = fax;
		this.hausnummer = hausnummer;
		this.ort = ort;
		this.plz = plz;
		this.schulnummer = schulnummer;
		this.strassenname = strassenname;
		this.telefon = telefon;
		this.hausnummerZusatz = hausnummerZusatz;
	}

	// ##### Berechnete Felder #####

	/**
	 * Stellt die Anschrift der Schule als einzeiligen Text zur Verfügung.
	 *
	 * @return Anschrift der Schule einzeilig
	 */
	public String anschriftEinzeilig() {
		return String.format("%s%s%s".formatted(
				((!bezeichnung.isEmpty()) && (bezeichnung.getFirst() != null) && !bezeichnung.getFirst().isEmpty()) ? bezeichnung.getFirst().trim() : "",
				", " + this.strassennameHausnummer(),
				", " + this.plzOrt()));
	}

	/**
	 * Stellt die Anschrift der Schule als ein mehrzeiliges Feld zur Verfügung.
	 *
	 * @return Anschrift der Schule
	 */
	public String anschrift() {
		return String.format("%s%s%s".formatted(
				((!bezeichnung.isEmpty()) && (bezeichnung.getFirst() != null) && !bezeichnung.getFirst().isEmpty()) ? bezeichnung.getFirst().trim() : "",
				"%n" + this.strassennameHausnummer(),
				"%n" + this.plzOrt()));
	}

	/**
	 * Stellt die Anschrift der Schule als ein mehrzeiliges Feld im html-Format zur Verfügung.
	 *
	 * @return Anschrift der Schule im html-Format
	 */
	public String anschriftHtml() {
		return String.format("%s%s%s".formatted(
				((!bezeichnung.isEmpty()) && (bezeichnung.getFirst() != null) && !bezeichnung.getFirst().isEmpty()) ? bezeichnung.getFirst().trim() : "",
				"<br/>" + this.strassennameHausnummer(),
				"<br/>" + this.plzOrt()));
	}

	/**
	 * Stellt die Anschrift mit vollständiger Bezeichnung der Schule als ein mehrzeiliges Feld zur Verfügung.
	 *
	 * @return Anschrift der Schule
	 */
	public String anschriftVollstaendig() {
		return String.format("%s%s%s".formatted(
				this.bezeichnungSchuleMehrzeilig(),
				"%n" + this.strassennameHausnummer(),
				"%n" + this.plzOrt()));
	}

	/**
	 * Stellt die Anschrift mit vollständiger Bezeichnung der Schule als ein mehrzeiliges Feld im html-Format zur Verfügung.
	 *
	 * @return Anschrift der Schule im html-Format
	 */
	public String anschriftVollstaendigHtml() {
		return String.format("%s%s%s".formatted(
				this.bezeichnungSchuleMehrzeiligHtml(),
				"<br/>" + this.strassennameHausnummer(),
				"<br/>" + this.plzOrt()));
	}

	/**
	 * Stellt die drei einzelnen Bezeichnungsfelder der Schule als ein mehrzeiliges Feld zur Verfügung.
	 *
	 * @return Mehrzeiliges Bezeichnungsfeld
	 */
	public String bezeichnungSchuleMehrzeilig() {
		final StringBuilder result = new StringBuilder(
				((!bezeichnung.isEmpty()) && (bezeichnung.getFirst() != null) && !bezeichnung.getFirst().isEmpty()) ? bezeichnung.getFirst().trim() : "");
		if (result.isEmpty())
			return "";
		for (int i = 1; i < bezeichnung.size(); i++) {
			if ((bezeichnung.get(i) != null) && (!bezeichnung.get(i).trim().isEmpty()))
				result.append("%n").append(bezeichnung.get(i).trim());
		}
		return result.toString();
	}

	/**
	 * Stellt die drei einzelnen Bezeichnungsfelder der Schule als ein mehrzeiliges Feld im html-Format zur Verfügung.
	 *
	 * @return Mehrzeiliges Bezeichnungsfeld im html-Format
	 */
	public String bezeichnungSchuleMehrzeiligHtml() {
		final StringBuilder result = new StringBuilder(
				((!bezeichnung.isEmpty()) && (bezeichnung.getFirst() != null) && !bezeichnung.getFirst().isEmpty()) ? bezeichnung.getFirst().trim() : "");
		if (result.isEmpty())
			return "";
		for (int i = 1; i < bezeichnung.size(); i++) {
			if ((bezeichnung.get(i) != null) && (!bezeichnung.get(i).trim().isEmpty()))
				result.append("<br/>").append(bezeichnung.get(i).trim());
		}
		return result.toString();
	}

	/**
	 * Erzeugt die Angabe von Postleitzahl und Wohnort.
	 *
	 * @return Postleitzahl und Wohnort
	 */
	public String plzOrt() {
		if ((this.plz == null))
			return "";

		String result = this.plz;
		result += " " + this.ort;

		return result.trim();
	}

	/**
	 * Stellt die Rücksendeinformation im Fensterumschlag der Schule zur Verfügung.
	 *
	 * @return Rücksendeinformation
	 */
	public String ruecksendeinformation() {
		return String.format("%s%s%s%s%s".formatted(
				((!bezeichnung.isEmpty()) && (bezeichnung.getFirst() != null) && !bezeichnung.getFirst().isEmpty()) ? bezeichnung.getFirst().trim() : "",
				(((!bezeichnung.isEmpty()) && (bezeichnung.getFirst() != null) && !bezeichnung.getFirst().isEmpty())
						&& (!this.strassennameHausnummer().isEmpty() || !this.plzOrt().isEmpty())) ? " - " : "",
				this.strassennameHausnummer(),
				(!this.strassennameHausnummer().isEmpty() && !this.plzOrt().isEmpty()) ? " - " : "",
				this.plzOrt()));
	}

	/**
	 * Erzeugt die Angabe von Straße und Hausnummer.
	 *
	 * @return Straße und Hausnummer
	 */
	public String strassennameHausnummer() {
		if (this.strassenname.isEmpty())
			return "";

		String result = this.strassenname;
		result += !this.hausnummer.isEmpty() ? (" " + this.hausnummer) : "";
		result += (!this.hausnummer.isEmpty() && !this.hausnummerZusatz.isEmpty()) ? (" " + this.hausnummerZusatz) : "";

		return result.trim();
	}



	// ##### Getter #####

	/**
	 * Die unter Umständen max. dreizeilige Bezeichnung der Schule.
	 *
	 * @return Die dreizeilige Bezeichnung der Schule.
	 */
	public List<String> bezeichnung() {
		return bezeichnung;
	}

	/**
	 * Gibt die Mailadresse der Schule zurück.
	 *
	 * @return die Mailadresse der Schule
	 */
	public String email() {
		return this.email;
	}

	/**
	 * Gibt die Faxnummer der Schule zurück.
	 *
	 * @return die Faxnummer der Schule
	 */
	public String fax() {
		return this.fax;
	}

	/**
	 * Gibt die Hausnummer zur Straße, in der die Schule liegt, zurück.
	 *
	 * @return die Hausnummer zur Straße
	 */
	public String hausnummer() {
		return this.hausnummer;
	}

	/**
	 * Gibt den Ort an, in dem die Schule liegt, zurück.
	 *
	 * @return Ort der Schule
	 */
	public String ort() {
		return this.ort;
	}

	/**
	 * Gibt die Postleitzahl des Gebietes an, in dem die Schule liegt, zurück.
	 *
	 * @return die Postleitzahl der Schule
	 */
	public String plz() {
		return this.plz;
	}

	/**
	 * Die eindeutige Schulnummer der Schule
	 *
	 * @return die Schulnummer der Schule
	 */
	public long schulnummer() {
		return schulnummer;
	}

	/**
	 * Gibt den Straßennamen der Straße an, in der die Schule liegt, zurück.
	 *
	 * @return den Straßennamen der Schule
	 */
	public String strassenname() {
		return this.strassenname;
	}

	/**
	 * Gibt die Telefonnummer der Schule zurück.
	 *
	 * @return die Telefonnummer der Schule
	 */
	public String telefon() {
		return this.telefon;
	}

	/**
	 * Gibt den Hausnummernzusatz der Straße an, in der die Schule liegt, zurück.
	 *
	 * @return den Hausnummernzusatz der Straße
	 */
	public String hausnummerZusatz() {
		return this.hausnummerZusatz;
	}


}
