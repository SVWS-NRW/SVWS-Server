package de.svws_nrw.module.reporting.types.schueler.telefon;

import de.svws_nrw.module.reporting.types.ReportingBaseType;

/**
 * Basis-Klasse im Rahmen des Reportings für Daten vom Typ SchuelerTelefonkontakt.
 */
public class ReportingSchuelerTelefonkontakt extends ReportingBaseType {

	/** Eine ergänzende Bemerkung zum Telefonkontakt. */
	protected String bemerkung;

	/** Die Bezeichnung des Telefonkontaktes */
	protected String bezeichnung;

	/** Gibt an, ob die Nummer des Kontakts gesperrt wurde. */
	protected boolean istGesperrt;

	/** Die Sortierung des Kontakts innerhalb aller Kontakte */
	protected int sortierung;

	/** Die Telefonnummer des Kontakts. */
	protected String telefonnummer;


	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 *
	 * @param bemerkung			Eine ergänzende Bemerkung zum Telefonkontakt.
	 * @param bezeichnung		Die Bezeichnung des Telefonkontaktes.
	 * @param istGesperrt		Gibt an, ob die Nummer des Kontakts gesperrt wurde.
	 * @param sortierung		Die Sortierung des Kontakts innerhalb aller Kontakte.
	 * @param telefonnummer		Die Telefonnummer des Kontakts.
	 */
	public ReportingSchuelerTelefonkontakt(final String bemerkung, final String bezeichnung, final boolean istGesperrt,
			final int sortierung, final String telefonnummer) {
		this.bemerkung = bemerkung;
		this.bezeichnung = bezeichnung;
		this.istGesperrt = istGesperrt;
		this.sortierung = sortierung;
		this.telefonnummer = telefonnummer;
	}



	// ##### Getter #####

	/**
	 * Eine ergänzende Bemerkung zum Telefonkontakt.
	 *
	 * @return Inhalt des Feldes bemerkung
	 */
	public String bemerkung() {
		return bemerkung;
	}

	/**
	 * Die Bezeichnung des Telefonkontaktes.
	 *
	 * @return Inhalt des Feldes bezeichnung
	 */
	public String bezeichnung() {
		return bezeichnung;
	}

	/**
	 * Gibt an, ob die Nummer des Kontakts gesperrt wurde.
	 *
	 * @return Inhalt des Feldes istGesperrt
	 */
	public boolean istGesperrt() {
		return istGesperrt;
	}

	/**
	 * Die Sortierung des Kontakts innerhalb aller Kontakte.
	 *
	 * @return Inhalt des Feldes sortierung
	 */
	public int sortierung() {
		return sortierung;
	}

	/**
	 * Die Telefonnummer des Kontakts.
	 *
	 * @return Inhalt des Feldes telefonnummer
	 */
	public String telefonnummer() {
		return telefonnummer;
	}

}
