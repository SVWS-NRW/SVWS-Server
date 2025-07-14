
package de.svws_nrw.module.reporting.types.schule;

import java.util.List;

import de.svws_nrw.asd.data.schule.SchulformKatalogEintrag;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Basis-Klasse im Rahmen des Reportings für Daten vom Typ ReportingSchulkatalogEintragNRW.
 */
@XmlRootElement
public class ReportingSchulkatalogEintragNRW extends ReportingSchuleBasisdatenNRW {

	/** Die ID des Katalog-Eintrags. */
	protected long id;

	/** Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht. */
	protected boolean istSichtbar;

	/** Ein Kürzel, welches der Schule zugeordnet ist. */
	protected String kuerzel;

	/** Eine Kurzbezeichnung für die Schule. */
	protected String kurzbezeichnung;

	/** Die Schulform der Schule. */
	protected SchulformKatalogEintrag schulform;

	/** Der Name des/der Schuleiters/Schulleiterin. */
	protected String schulleiter;

	/** Gibt die Position in der Sortierreihenfolge für die Katalog-Einträge an. */
	protected int sortierung;

	/**
	 * Konstruktor zur Erstellung eines Reporting-Eintrags für den Schulkatalog in NRW.
	 *
	 * @param bezeichnung Die unter Umständen max. dreizeilige Bezeichnung der Schule.
	 * @param email Die Mailadresse der Schule.
	 * @param fax Die Faxnummer der Schule.
	 * @param hausnummer Die Hausnummer der Schule.
	 * @param ort Der Ort, in dem die Schule liegt.
	 * @param plz Die Postleitzahl des Ortes, in dem die Schule liegt.
	 * @param schulnummer Die Schulnummer der Schule.
	 * @param strassenname Der Straßenname der Schule.
	 * @param telefon Die Telefonnummer der Schule.
	 * @param hausnummerZusatz Ein optionaler Zusatz zur Hausnummer der Schule.
	 * @param id Die eindeutige ID des Katalog-Eintrags.
	 * @param istSichtbar Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll.
	 * @param kuerzel Das Kürzel der Schule.
	 * @param kurzbezeichnung Die Kurzbezeichnung der Schule.
	 * @param schulform Die Schulform der Schule.
	 * @param schulleiter Der Name des/der Schulleiters/Schulleiterin.
	 * @param sortierung Die Position dieses Eintrags in der Sortierreihenfolge.
	 */
	public ReportingSchulkatalogEintragNRW(final List<String> bezeichnung, final String email, final String fax, final String hausnummer,
			final String ort, final String plz, final long schulnummer, final String strassenname, final String telefon, final String hausnummerZusatz,
			final long id, final boolean istSichtbar, final String kuerzel, final String kurzbezeichnung, final SchulformKatalogEintrag schulform,
			final String schulleiter, final int sortierung) {
		super(bezeichnung, email, fax, hausnummer, ort, plz, schulnummer, strassenname, telefon, hausnummerZusatz);
		this.id = id;
		this.istSichtbar = istSichtbar;
		this.kuerzel = kuerzel;
		this.kurzbezeichnung = kurzbezeichnung;
		this.schulform = schulform;
		this.schulleiter = schulleiter;
		this.sortierung = sortierung;
	}


	// ##### Getter #####

	/**
	 * Gibt die ID des Katalog-Eintrags zurück.
	 *
	 * @return die ID des Katalog-Eintrags
	 */
	public long id() {
		return this.id;
	}

	/**
	 * Gibt zurück, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht.
	 *
	 * @return true, falls der Eintrag sichtbar sein soll
	 */
	public boolean istSichtbar() {
		return this.istSichtbar;
	}

	/**
	 * Gibt das Kürzel der Schule zurück.
	 *
	 * @return das Kürzel der Schule
	 */
	public String kuerzel() {
		return this.kuerzel;
	}

	/**
	 * Gibt die Kurzbezeichnung der Schule zurück.
	 *
	 * @return die Kurzbezeichnung der Schule
	 */
	public String kurzbezeichnung() {
		return this.kurzbezeichnung;
	}

	/**
	 * Gibt die Schulform der Schule zurück.
	 *
	 * @return die Schulform
	 */
	public SchulformKatalogEintrag schulform() {
		return this.schulform;
	}

	/**
	 * Gibt den Namen des/der Schulleiters/Schulleiterin zurück.
	 *
	 * @return den Namen des/der Schulleiters/Schulleiterin
	 */
	public String schulleiter() {
		return this.schulleiter;
	}

	/**
	 * Gibt die Position in der Sortierreihenfolge für die Katalog-Einträge zurück.
	 *
	 * @return die Position in der Sortierreihenfolge
	 */
	public int sortierung() {
		return this.sortierung;
	}
}
