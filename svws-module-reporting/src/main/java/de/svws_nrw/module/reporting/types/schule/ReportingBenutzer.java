package de.svws_nrw.module.reporting.types.schule;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.svws_nrw.asd.types.Geschlecht;
import de.svws_nrw.asd.types.schule.Nationalitaeten;
import de.svws_nrw.base.email.EmailJobManager;
import de.svws_nrw.core.data.kataloge.OrtKatalogEintrag;
import de.svws_nrw.core.data.kataloge.OrtsteilKatalogEintrag;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;
import de.svws_nrw.module.reporting.types.person.ReportingPerson;

/**
 * Basis-Klasse im Rahmen des Reportings für Daten vom Typ Benutzer der aktuellen Verbindung zur Schule.
 */
public class ReportingBenutzer extends ReportingPerson {

	/** Der Anzeigename des Benutzers im Client. */
	protected String anzeigename;

	/** Der Benutzername des Benutzers im Client. */
	protected String benutzername;

	/** Der Anzeigename des Benutzers beim Versand von E-Mails. */
	protected String emailAnzeigename;

	/** Der {@link EmailJobManager} des Benutzers. */
	@JsonIgnore
	protected EmailJobManager emailJobManager;

	/** Die Signatur des Benutzers beim E-Mail-Versand. */
	protected String emailSignatur;

	/** Die E-Mail-Adresse des Benutzers, die für den Versand per SMTP genutzt wird. */
	protected String emailSmtpAdresse;

	/** Der Anmeldename für den SMTP-Server. */
	protected String emailSmtpBenutzername;

	/** Das AES-verschlüsselte SMTP-Kennwort des Benutzers. */
	protected String emailSmtpPasswort;

	/** Die ID des Benutzers. */
	protected long id;

	/** Gibt an, ob dieser Benutzer ein Admin-Benutzer ist. */
	protected boolean istAdmin;

	/** Der Lehrer, wenn es sich bei diesem Benutzer um einen Lehrer handelt. */
	protected ReportingLehrer lehrer;

	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 *
	 * @param anrede Die Anrede des Benutzers.
	 * @param anzeigename Der Anzeigename (displayname) des Benutzers.
	 * @param benutzername Der Benutzername (username) des Benutzers.
	 * @param emailAnzeigename Der Anzeigename des Benutzers beim Versand von E-Mails.
	 * @param emailJobManager Der {@link EmailJobManager} des Benutzers.
	 * @param emailPrivat Private E-Mail-Adresse des Benutzers.
	 * @param emailSchule Schulische E-Mail-Adresse des Benutzers.
	 * @param emailSignatur Die Signatur des Benutzers beim E-Mail-Versand.
	 * @param emailSmtpAdresse Die E-Mail-Adresse des Benutzers, die für den Versand per SMTP genutzt wird.
	 * @param emailSmtpBenutzername Der Anmeldename für den SMTP-Server.
	 * @param emailSmtpPasswort Das AES-verschlüsselte SMTP-Kennwort des Benutzers.
	 * @param faxSchule Die schulische Faxnummer des Benutzers.
	 * @param geburtsdatum Das Geburtsdatum des Benutzers.
	 * @param geburtsland Das Geburtsland des Benutzers.
	 * @param geburtsname Der Geburtsname des Benutzers.
	 * @param geburtsort Der Geburtsort des Benutzers.
	 * @param geschlecht Das Geschlecht des Benutzers.
	 * @param hausnummer Ggf. die Hausnummer zur Straße im Wohnort des Benutzers.
	 * @param hausnummerZusatz Ggf. der Hausnummerzusatz zur Straße im Wohnort des Benutzers.
	 * @param id Die ID des Benutzers.
	 * @param istAdmin Gibt an, ob dieser Benutzer ein Admin-Benutzer ist.
	 * @param lehrer Der Lehrer, wenn es sich bei diesem Benutzer um einen Lehrer handelt.
	 * @param nachname Der Name des Benutzers.
	 * @param staatsangehoerigkeit Die Staatsangehörigkeit des Benutzers.
	 * @param staatsangehoerigkeit2 Die ggf. zweite Staatsangehörigkeit des Benutzers.
	 * @param strassenname Ggf. der Straßenname im Wohnort des Benutzers.
	 * @param telefonPrivat Die private Telefonnummer des Benutzers.
	 * @param telefonPrivatMobil Die private Mobilfunk-Telefonnummer des Benutzers.
	 * @param telefonSchule Die schulische Telefonnummer des Benutzers.
	 * @param telefonSchuleMobil Die schulische Mobilfunk-Telefonnummer des Benutzers.
	 * @param titel Die Titel des Benutzers.
	 * @param vorname Der Vorname des Benutzers.
	 * @param vornamen Alle Vornamen des Benutzers
	 * @param wohnort Der Wohnort des Benutzers.
	 * @param wohnortsteil Ggf. der Ortsteil des Wohnortes des Benutzers.
	 */
	@SuppressWarnings("java:S107") // Konstruktoren mit zu vielen Parametern (gemäß SonarQube) werden aktuell toleriert und nicht refacored (Stand 2026-04).
	public ReportingBenutzer(final String anrede, final String anzeigename, final String benutzername, final String emailAnzeigename,
			final EmailJobManager emailJobManager, final String emailPrivat, final String emailSchule, final String emailSignatur,
			final String emailSmtpAdresse, final String emailSmtpBenutzername, final String emailSmtpPasswort, final String faxSchule,
			final String geburtsdatum, final String geburtsland, final String geburtsname, final String geburtsort, final Geschlecht geschlecht,
			final String hausnummer, final String hausnummerZusatz, final long id, final boolean istAdmin, final ReportingLehrer lehrer,
			final String nachname, final Nationalitaeten staatsangehoerigkeit, final Nationalitaeten staatsangehoerigkeit2, final String strassenname,
			final String telefonPrivat, final String telefonPrivatMobil, final String telefonSchule, final String telefonSchuleMobil, final String titel,
			final String vorname, final String vornamen, final OrtKatalogEintrag wohnort, final OrtsteilKatalogEintrag wohnortsteil) {
		super(anrede, emailPrivat, emailSchule, faxSchule, "", geburtsdatum, geburtsland, geburtsname, geburtsort, geschlecht, hausnummer, hausnummerZusatz,
				nachname, staatsangehoerigkeit, staatsangehoerigkeit2, strassenname, telefonPrivat, telefonPrivatMobil, telefonSchule,
				telefonSchuleMobil, titel, vorname, vornamen, wohnort, wohnortsteil);
		this.anzeigename = ersetzeNullBlankTrim(anzeigename);
		this.benutzername = ersetzeNullBlankTrim(benutzername);
		this.emailAnzeigename = ersetzeNullBlankTrim(emailAnzeigename);
		this.emailJobManager = emailJobManager;
		this.emailSignatur = ersetzeNullBlankTrim(emailSignatur);
		this.emailSmtpAdresse = ersetzeNullBlankTrim(emailSmtpAdresse);
		this.emailSmtpBenutzername = ersetzeNullBlankTrim(emailSmtpBenutzername);
		this.emailSmtpPasswort = ersetzeNullBlankTrim(emailSmtpPasswort);
		this.id = id;
		this.istAdmin = istAdmin;
		this.lehrer = lehrer;
	}



	/**
	 * Hashcode der Klasse
	 * @return Hashcode der Klasse
	 */
	@Override
	public int hashCode() {
		return 31 + Long.hashCode(id);
	}

	/**
	 * Equals der Klasse
	 * @param obj Das Vergleichsobjekt
	 * @return	true, falls es das gleiche Objekt ist, andernfalls false.
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof final ReportingBenutzer other)) {
			return false;
		}
		return (id == other.id);
	}


	// ##### Berechnete Felder #####
	/**
	 * Gibt an, ob für den Benutzer ein Eintrag aus der Tabelle der Lehrkräfte angegeben wurde.
	 *
	 * @return true, wenn der Benutzer einen Lehrkrafteintrag besitzt, sonst false.
	 */
	public boolean hatLehrkraftdaten() {
		return this.lehrer != null;
	}



	// ##### Getter #####

	/**
	 * Der Anzeigename (displayname) des Benutzers.
	 *
	 * @return Inhalt des Feldes anzeigename; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String anzeigename() {
		return anzeigename;
	}

	/**
	 * Der Benutzername (username) des Benutzers.
	 *
	 * @return Inhalt des Feldes benutzername; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String benutzername() {
		return benutzername;
	}

	/**
	 * Der Anzeigename des Benutzers beim Versand von E-Mails.
	 *
	 * @return Inhalt des Feldes emailAnzeigename; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String emailAnzeigename() {
		return emailAnzeigename;
	}

	/**
	 * Der {@link EmailJobManager} des Benutzers.
	 *
	 * @return Inhalt des Feldes emailJobManager
	 */
	@JsonIgnore
	public EmailJobManager emailJobManager() {
		return emailJobManager;
	}

	/**
	 * Die Signatur des Benutzers beim E-Mail-Versand.
	 *
	 * @return Inhalt des Feldes emailSignatur; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String emailSignatur() {
		return emailSignatur;
	}

	/**
	 * Die E-Mail-Adresse des Benutzers, die für den Versand per SMTP genutzt wird.
	 *
	 * @return Inhalt des Feldes emailSmtpAdresse; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String emailSmtpAdresse() {
		return emailSmtpAdresse;
	}

	/**
	 * Der Anmeldename für den SMTP-Server.
	 *
	 * @return Inhalt des Feldes emailSmtpBenutzername; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String emailSmtpBenutzername() {
		return emailSmtpBenutzername;
	}

	/**
	 * Das AES-verschlüsselte SMTP-Kennwort des Benutzers.
	 *
	 * @return Inhalt des Feldes emailSmtpPasswort; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String emailSmtpPasswort() {
		return emailSmtpPasswort;
	}

	/**
	 * Die ID des Benutzers.
	 *
	 * @return Inhalt des Feldes id
	 */
	public long id() {
		return id;
	}

	/**
	 * Gibt an, ob dieser Benutzer ein Admin-Benutzer ist.
	 *
	 * @return Inhalt des Feldes istAdmin
	 */
	public boolean istAdmin() {
		return istAdmin;
	}

	/**
	 * Der Lehrer, wenn es sich bei diesem Benutzer um einen Lehrer handelt.
	 *
	 * @return Inhalt des Feldes lehrer
	 */
	public ReportingLehrer lehrer() {
		return lehrer;
	}
}
