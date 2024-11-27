package de.svws_nrw.module.reporting.types.schueler.erzieher;

import de.svws_nrw.asd.types.Geschlecht;
import de.svws_nrw.core.data.kataloge.OrtKatalogEintrag;
import de.svws_nrw.core.data.kataloge.OrtsteilKatalogEintrag;
import de.svws_nrw.core.types.schule.Nationalitaeten;
import de.svws_nrw.module.reporting.types.person.ReportingPerson;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;

/**
 * Basis-Klasse im Rahmen des Reportings für Daten vom Typ Erzieher.
 */
public class ReportingErzieher extends ReportingPerson {

	/** Die Art des Erziehereintrages. */
	protected ReportingErzieherArt art;

	/** Anmerkungen zum Erziehers. */
	protected String bemerkung;

	/** Gibt an, ob der Erzieher Anschreiben erhält oder nicht. */
	protected Boolean erhaeltAnschreiben;

	/** Die ID des Erziehers. */
	protected long id;

	/** Der Schüler, dem dieser Erzieher zugeordnet ist. */
	protected ReportingSchueler schueler;


	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 *
	 * @param anrede Die Anrede des Erziehers.
	 * @param art Die Art des Erziehereintrages.
	 * @param bemerkung Anmerkungen zum Erziehers.
	 * @param emailPrivat Private E-Mail-Adresse des Erziehers.
	 * @param emailSchule Schulische E-Mail-Adresse des Erziehers.
	 * @param erhaeltAnschreiben Gibt an, ob der Erzieher Anschreiben erhält oder nicht.
	 * @param geburtsdatum Das Geburtsdatum des Erziehers.
	 * @param geburtsland Das Geburtsland des Erziehers.
	 * @param geburtsname Der Geburtsname des Erziehers.
	 * @param geburtsort Der Geburtsort des Erziehers.
	 * @param geschlecht Das Geschlecht des Erziehers.
	 * @param hausnummer Ggf. die Hausnummer zur Straße im Wohnort des Erziehers.
	 * @param hausnummerZusatz Ggf. der Hausnummerzusatz zur Straße im Wohnort des Erziehers.
	 * @param id Die ID des Erziehers.
	 * @param nachname Der Name des Erziehers.
	 * @param schueler Der Schüler, dem dieser Erzieher zugeordnet ist.
	 * @param staatsangehoerigkeit Die Staatsangehörigkeit des Erziehers.
	 * @param staatsangehoerigkeit2 Die ggf. zweite Staatsangehörigkeit des Erziehers.
	 * @param strassenname Ggf. der Straßenname im Wohnort des Erziehers.
	 * @param telefon Die Telefonnummer des Erziehers.
	 * @param telefonMobil Die mobile Telefonnummer des Erziehers.
	 * @param titel Die Titel des Erziehers.
	 * @param vorname Der Vorname des Erziehers.
	 * @param vornamen Alle Vornamen des Erziehers
	 * @param wohnort Der Wohnort des Erziehers.
	 * @param wohnortsteil Ggf. der Ortsteil des Wohnortes des Erziehers.
	 */
	public ReportingErzieher(final String anrede, final ReportingErzieherArt art, final String bemerkung, final String emailPrivat, final String emailSchule,
			final Boolean erhaeltAnschreiben, final String geburtsdatum, final String geburtsland, final String geburtsname, final String geburtsort,
			final Geschlecht geschlecht, final String hausnummer, final String hausnummerZusatz, final long id, final String nachname,
			final ReportingSchueler schueler, final Nationalitaeten staatsangehoerigkeit, final Nationalitaeten staatsangehoerigkeit2,
			final String strassenname, final String telefon, final String telefonMobil, final String titel, final String vorname, final String vornamen,
			final OrtKatalogEintrag wohnort, final OrtsteilKatalogEintrag wohnortsteil) {
		super(anrede, emailPrivat, emailSchule, geburtsdatum, geburtsland, geburtsname, geburtsort, geschlecht, hausnummer, hausnummerZusatz, nachname,
				staatsangehoerigkeit, staatsangehoerigkeit2, strassenname, telefon, telefonMobil, titel, vorname, vornamen, wohnort, wohnortsteil);
		this.art = art;
		this.bemerkung = bemerkung;
		this.erhaeltAnschreiben = erhaeltAnschreiben;
		this.id = id;
		this.schueler = schueler;
	}


	// ##### Hash und Equals Methoden #####

	/**
	 * Hashcode der Klasse
	 * @return Hashcode der Klasse
	 */
	public int hashCode() {
		return 31 + Long.hashCode(id);
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
		if (!(obj instanceof final ReportingErzieher other))
			return false;
		return (id == other.id);
	}


	// ##### Berechnete Felder #####
	/**
	 * Erzeugt den geschlechtsspezifischen Ausdruck "Ihr Sohn/Ihre Tochter" im Nominativ.
	 *
	 * @return Geschlechtsspezifischer Ausdruck für "Ihr Sohn/Ihre Tochter" im Nominativ
	 */
	public String sohnTochterNominativ() {
		switch (schueler.geschlecht()) {
			case Geschlecht.W -> {
				return "Ihre Tochter";
			}
			case Geschlecht.M -> {
				return "Ihr Sohn";
			}
			case null, default -> {
				return "Ihr Kind";
			}
		}
	}

	/**
	 * Erzeugt den geschlechtsspezifischen Ausdruck "Ihr Sohn/Ihre Tochter" im Genitiv.
	 *
	 * @return Geschlechtsspezifischer Ausdruck für "Ihr Sohn/Ihre Tochter" im Genitiv
	 */
	public String sohnTochterGenitiv() {
		switch (schueler.geschlecht()) {
			case Geschlecht.W -> {
				return "Ihrer Tochter";
			}
			case Geschlecht.M -> {
				return "Ihres Sohn";
			}
			case null, default -> {
				return "Ihres Kindes";
			}
		}
	}

	/**
	 * Erzeugt den geschlechtsspezifischen Ausdruck "Ihr Sohn/Ihre Tochter" im Dativ.
	 *
	 * @return Geschlechtsspezifischer Ausdruck für "Ihr Sohn/Ihre Tochter" im Dativ
	 */
	public String sohnTochterDativ() {
		switch (schueler.geschlecht()) {
			case Geschlecht.W -> {
				return "Ihrer Tochter";
			}
			case Geschlecht.M -> {
				return "Ihrem Sohn";
			}
			case null, default -> {
				return "Ihrem Kind";
			}
		}
	}

	/**
	 * Erzeugt den geschlechtsspezifischen Ausdruck "Ihr Sohn/Ihre Tochter" im Akkusativ.
	 *
	 * @return Geschlechtsspezifischer Ausdruck für "Ihr Sohn/Ihre Tochter" im Akkusativ
	 */
	public String sohnTochterAkkusativ() {
		switch (schueler.geschlecht()) {
			case Geschlecht.W -> {
				return "Ihrer Tochter";
			}
			case Geschlecht.M -> {
				return "Ihren Sohn";
			}
			case null, default -> {
				return "Ihr Kind";
			}
		}
	}



	// ##### Getter #####

	/**
	 * Die Art des Erziehereintrages.
	 *
	 * @return Inhalt des Feldes art
	 */
	public ReportingErzieherArt art() {
		return art;
	}

	/**
	 * Anmerkungen zum Erziehers.
	 *
	 * @return Inhalt des Feldes bemerkung
	 */
	public String bemerkung() {
		return bemerkung;
	}

	/**
	 * Gibt an, ob der Erzieher Anschreiben erhält oder nicht.
	 *
	 * @return Inhalt des Feldes erhaeltAnschreiben
	 */
	public Boolean erhaeltAnschreiben() {
		return erhaeltAnschreiben;
	}

	/**
	 * Die ID des Erziehers.
	 *
	 * @return Inhalt des Feldes id
	 */
	public long id() {
		return id;
	}

	/**
	 * Der Schüler, dem dieser Erzieher zugeordnet ist.
	 *
	 * @return Inhalt des Feldes schueler
	 */
	public ReportingSchueler schueler() {
		return schueler;
	}

}
