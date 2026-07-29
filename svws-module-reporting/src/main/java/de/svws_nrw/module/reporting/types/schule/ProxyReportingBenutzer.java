package de.svws_nrw.module.reporting.types.schule;

import java.util.Collection;
import java.util.HashSet;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.svws_nrw.base.email.EmailJobManager;
import de.svws_nrw.core.data.benutzer.BenutzerEMailDaten;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.dto.current.views.benutzer.DTOViewBenutzerdetails;
import de.svws_nrw.module.reporting.repositories.ReportingContext;

/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ Benutzer und erweitert die Klasse {@link ReportingBenutzer}.
 */
public class ProxyReportingBenutzer extends ReportingBenutzer {

	/** Context mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingContext reportingContext;

	/** Das Datenbank-Objekt des Benutzers, um dessen Kompetenzen zu prüfen. */
	@JsonIgnore
	private final Benutzer benutzer;


	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingBenutzer}.
	 *
	 * @param reportingContext Repository für das Reporting.
	 * @param benutzer         Benutzer-Daten aus der Datenbank.
	 * @param benutzerDetails  Details zum Benutzer aus der Datenbank.
	 * @param benutzerEMailDaten E-Mail-Daten des Benutzers.
	 * @param benutzerEMailJobManager E-Mail-Job-Manager des Benutzers.
	 */
	public ProxyReportingBenutzer(final ReportingContext reportingContext, final Benutzer benutzer, final DTOViewBenutzerdetails benutzerDetails,
			final BenutzerEMailDaten benutzerEMailDaten, final EmailJobManager benutzerEMailJobManager) {
		super("",
				"",
				"",
				"",
				benutzerEMailJobManager,
				"",
				"",
				"",
				"",
				"",
				"",
				"",
				"",
				"",
				"",
				"",
				null,
				"",
				"",
				-1,
				false,
				null,
				"",
				null,
				null,
				"",
				"",
				"",
				"",
				"",
				"",
				"",
				"",
				null,
				null);

		this.reportingContext = reportingContext;

		this.benutzer = benutzer;
		super.id = this.benutzer.getId();
		super.istAdmin = this.benutzer.istAdmin();
		super.benutzername = ersetzeNullBlankTrim(this.benutzer.getUsername());

		if (benutzerDetails != null) {
			super.anzeigename = ersetzeNullBlankTrim(benutzerDetails.AnzeigeName);
			super.emailAnzeigename = ersetzeNullBlankTrim(benutzerDetails.AnzeigeName);
		}

		if (benutzerEMailDaten != null) {
			super.emailSignatur = ersetzeNullBlankTrim(benutzerEMailDaten.signatur);
			super.emailSmtpAdresse = ersetzeNullBlankTrim(benutzerEMailDaten.address);
			super.emailSmtpBenutzername = ersetzeNullBlankTrim(benutzerEMailDaten.usernameSMTP);
			super.emailSmtpPasswort = ersetzeNullBlankTrim(benutzerEMailDaten.passwordSMTP);
		}

		// Prüfe, ob der angemeldete Benutzer Lehrer ist. Übernehme dann dessen Informationen. Andernfalls weitere Informationen aus der Datenbank laden.
		// Hinweis: lehrer(id) kann trotz Cache-Treffer null liefern, wenn die Lehrkraft per User-Filter ausgeschlossen ist.
		super.lehrer = ((benutzer.getIdLehrer() != null) && this.reportingContext.repositoryLehrer().existiertLehrer(benutzer.getIdLehrer()))
				? this.reportingContext.repositoryLehrer().lehrer(benutzer.getIdLehrer())
				: null;
		if (super.lehrer != null) {
			super.anzeigename = super.lehrer.vornameNachname();
			super.anrede = super.lehrer.anrede();
			super.emailPrivat = super.lehrer.emailPrivat();
			super.emailSchule = super.lehrer.emailSchule();
			super.faxSchule = super.lehrer.faxSchule();
			super.geburtsdatum = super.lehrer.geburtsdatum();
			super.geburtsland = super.lehrer.geburtsland();
			super.geburtsname = super.lehrer.geburtsname();
			super.geburtsort = super.lehrer.geburtsort();
			super.geschlecht = super.lehrer.geschlecht();
			super.hausnummer = super.lehrer.hausnummer();
			super.hausnummerZusatz = super.lehrer.hausnummerZusatz();
			super.nachname = super.lehrer.nachname();
			super.staatsangehoerigkeit = super.lehrer.staatsangehoerigkeit();
			super.staatsangehoerigkeit2 = super.lehrer.staatsangehoerigkeit2();
			super.strassenname = super.lehrer.strassenname();
			super.telefonPrivat = super.lehrer.telefonPrivat();
			super.telefonPrivatMobil = super.lehrer.telefonPrivatMobil();
			super.telefonSchule = super.lehrer.telefonSchule();
			super.telefonSchuleMobil = super.lehrer.telefonSchuleMobil();
			super.titel = super.lehrer.titel();
			super.vorname = super.lehrer.vorname();
			super.vornamen = super.lehrer.vornamen();
			super.wohnort = super.lehrer.wohnort();
			super.wohnortsteil = super.lehrer.wohnortsteil();
		}

		// Sicherstellen, dass die Anzeige- und E-Mail-Namen nicht leer sind.
		if (super.anzeigename.isBlank()) {
			super.anzeigename = super.benutzername;
		}
		if (super.emailAnzeigename.isBlank()) {
			super.emailAnzeigename = super.anzeigename;
		}
	}


	// ##### Hash und Equals Methoden #####

	/**
	 * Hashcode der Klasse
	 * @return Hashcode der Klasse
	 */
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	/**
	 * Equals der Klasse
	 * @param obj Das Vergleichsobjekt
	 * @return    true, falls es das gleiche Objekt ist, andernfalls false.
	 */
	@Override
	public boolean equals(final Object obj) {
		return super.equals(obj);
	}


	/**
	 * Gibt das Repository mit den Daten der Schule und den zwischengespeicherten Daten zurück.
	 *
	 * @return Repository für das Reporting
	 */
	@JsonIgnore
	public ReportingContext reportingContext() {
		return reportingContext;
	}

	/**
	 * Prüft, ob der aktuell angemeldete Benutzer eine der übergebenen Kompetenzen besitzt
	 * (oder Administrator ist).
	 *
	 * @param kompetenzen Die zu prüfenden Kompetenzen.
	 *
	 * @return true, falls der Benutzer eine der Kompetenzen besitzt, sonst false.
	 */
	@JsonIgnore
	public boolean pruefeKompetenz(final Collection<BenutzerKompetenz> kompetenzen) {
		return this.benutzer.pruefeKompetenz(new HashSet<>(kompetenzen));
	}
}
