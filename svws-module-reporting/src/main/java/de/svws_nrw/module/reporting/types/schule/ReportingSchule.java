package de.svws_nrw.module.reporting.types.schule;

import java.util.List;

import de.svws_nrw.asd.data.schule.SchulformKatalogEintrag;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;

/**
 * Basis-Klasse im Rahmen des Reportings für Daten vom Typ Schule.
 */
public class ReportingSchule extends ReportingSchuleBasisdatenNRW {

	/** Der aktuelle Abschnitt des Schuljahres der Schule. */
	protected ReportingSchuljahresabschnitt aktuellerSchuljahresabschnitt;

	/** Die Anzahl der Jahrgangsstufen pro Jahr. */
	protected long anzahlJahrgangsstufenProJahr;

	/** Die Anzahl der Abschnitte pro Jahr */
	protected long anzahlSchuljahresabschnitteProJahr;

	/** Der über die API als ausgewählter Schuljahresabschnitt deklarierte Abschnitt der Schule. */
	protected ReportingSchuljahresabschnitt auswahlSchuljahresabschnitt;

	/** Die allgemeine Bezeichnung der Abschnitte (z. B. Quartal oder Halbjahr) */
	protected String bezeichnungSchuljahresabschnitt;

	/** Eine Liste der einzelnen speziellen Bezeichnungen für die Abschnitte (z. B. 1. Halbjahr, 2. Halbjahr) */
	protected List<String> bezeichnungenSchuljahresabschnitte;

	/** Die Dauer einer Unterrichtseinheit in Minuten. */
	protected long dauerUnterrichtseinheit;

	/** Die Liste der Schuljahresabschnitte, welche an der Schule definiert sind. */
	protected List<ReportingSchuljahresabschnitt> schuljahresabschnitte;

	/** Die Schulform der Schule */
	protected SchulformKatalogEintrag schulform;

	/** Der Lehrer, der die Schulleitungsfunktion besitzt. */
	protected ReportingLehrer schulleitung;

	/** Das Logo der Schule im Base64-Format. */
	protected String schullogo;

	/** Der Lehrer, der die stv. Schulleitungsfunktion besitzt. */
	protected ReportingLehrer stvSchulleitung;

	/** Die Adresse der Homepage der Schule (Domain-Name) */
	protected String webAdresse;


	/**
	 * Konstruktor für die Klasse ReportingSchule.
	 *
	 * @param bezeichnung                Die Bezeichnung der Schule.
	 * @param email                      Die E-Mail-Adresse der Schule.
	 * @param fax                        Die Faxnummer der Schule.
	 * @param hausnummer                 Die Hausnummer der Schule.
	 * @param ort                        Der Ort, in dem sich die Schule befindet.
	 * @param plz                        Die Postleitzahl der Schule.
	 * @param schulnummer				 Die Schulnummer der Schule.
	 * @param strassenname               Der Straßenname der Schule.
	 * @param telefon                    Die Telefonnummer der Schule.
	 * @param hausnummerZusatz           Der Zusatz zur Hausnummer der Schule.
	 * @param aktuellerSchuljahresabschnitt Der aktuelle Schuljahresabschnitt der Schule.
	 * @param anzahlJahrgangsstufenProJahr Die Anzahl der Jahrgangsstufen pro Jahr.
	 * @param anzahlSchuljahresabschnitteProJahr Die Anzahl der Schuljahresabschnitte pro Jahr.
	 * @param auswahlSchuljahresabschnitt Der ausgewählte Schuljahresabschnitt.
	 * @param bezeichnungSchuljahresabschnitt Die Bezeichnung des Schuljahresabschnitts.
	 * @param bezeichnungenSchuljahresabschnitte Die Bezeichnungen der Schuljahresabschnitte.
	 * @param dauerUnterrichtseinheit    Die Dauer einer Unterrichtseinheit in Minuten.
	 * @param schuljahresabschnitte      Die Schuljahresabschnitte der Schule.
	 * @param schulform                  Die Schulform (z. B. Gymnasium, Realschule, etc.).
	 * @param schulleitung               Die Schulleitung der Schule.
	 * @param schullogo                  Das Logo der Schule.
	 * @param stvSchulleitung            Die stellvertretende Schulleitung.
	 * @param webAdresse                 Die Webadresse der Schule.
	 */
	public ReportingSchule(final List<String> bezeichnung, final String email, final String fax, final String hausnummer, final String ort, final String plz,
			final long schulnummer, final String strassenname, final String telefon, final String hausnummerZusatz,
			final ReportingSchuljahresabschnitt aktuellerSchuljahresabschnitt,
			final long anzahlJahrgangsstufenProJahr, final long anzahlSchuljahresabschnitteProJahr,
			final ReportingSchuljahresabschnitt auswahlSchuljahresabschnitt,
			final String bezeichnungSchuljahresabschnitt, final List<String> bezeichnungenSchuljahresabschnitte, final long dauerUnterrichtseinheit,
			final List<ReportingSchuljahresabschnitt> schuljahresabschnitte, final SchulformKatalogEintrag schulform, final ReportingLehrer schulleitung, final String schullogo,
			final ReportingLehrer stvSchulleitung, final String webAdresse) {
		super(bezeichnung, email, fax, hausnummer, ort, plz, schulnummer, strassenname, telefon, hausnummerZusatz);
		this.aktuellerSchuljahresabschnitt = aktuellerSchuljahresabschnitt;
		this.anzahlJahrgangsstufenProJahr = anzahlJahrgangsstufenProJahr;
		this.anzahlSchuljahresabschnitteProJahr = anzahlSchuljahresabschnitteProJahr;
		this.auswahlSchuljahresabschnitt = auswahlSchuljahresabschnitt;
		this.bezeichnungSchuljahresabschnitt = bezeichnungSchuljahresabschnitt;
		this.bezeichnungenSchuljahresabschnitte = bezeichnungenSchuljahresabschnitte;
		this.dauerUnterrichtseinheit = dauerUnterrichtseinheit;
		this.schuljahresabschnitte = schuljahresabschnitte;
		this.schulform = schulform;
		this.schulleitung = schulleitung;
		this.schullogo = schullogo;
		this.stvSchulleitung = stvSchulleitung;
		this.webAdresse = webAdresse;
	}



	// ##### Getter #####

	/**
	 * Der aktuelle Abschnitt des Schuljahres der Schule.
	 *
	 * @return Inhalt des Feldes aktuellerSchuljahresabschnitt
	 */
	public ReportingSchuljahresabschnitt aktuellerSchuljahresabschnitt() {
		return aktuellerSchuljahresabschnitt;
	}

	/**
	 * Die Anzahl der Jahrgangsstufen pro Jahr.
	 *
	 * @return Inhalt des Feldes anzahlJahrgangsstufenProJahr
	 */
	public long anzahlJahrgangsstufenProJahr() {
		return anzahlJahrgangsstufenProJahr;
	}

	/**
	 * Die Anzahl der Abschnitte pro Jahr
	 *
	 * @return Inhalt des Feldes anzahlSchuljahresabschnitteProJahr
	 */
	public long anzahlSchuljahresabschnitteProJahr() {
		return anzahlSchuljahresabschnitteProJahr;
	}

	/**
	 * Der über die API als ausgewählter Schuljahresabschnitt deklarierter Abschnitt der Schule.
	 *
	 * @return Inhalt des Feldes auswahlSchuljahresabschnitt
	 */
	public ReportingSchuljahresabschnitt auswahlSchuljahresabschnitt() {
		return auswahlSchuljahresabschnitt;
	}

	/**
	 * Die allgemeine Bezeichnung der Abschnitte (z.B. Quartal oder Halbjahr)
	 *
	 * @return Inhalt des Feldes bezeichnungSchuljahresabschnitt
	 */
	public String bezeichnungSchuljahresabschnitt() {
		return bezeichnungSchuljahresabschnitt;
	}

	/**
	 * Eine Liste der einzelnen speziellen Bezeichnungen für die Abschnitte (z.B. 1. Halbjahr, 2. Halbjahr)
	 *
	 * @return Inhalt des Feldes bezeichnungenSchuljahresabschnitte
	 */
	public List<String> bezeichnungenSchuljahresabschnitte() {
		return bezeichnungenSchuljahresabschnitte;
	}

	/**
	 * Die Dauer einer Unterrichtseinheit in Minuten.
	 *
	 * @return Inhalt des Feldes dauerUnterrichtseinheit
	 */
	public long dauerUnterrichtseinheit() {
		return dauerUnterrichtseinheit;
	}


	/**
	 * Die Liste der Schuljahresabschnitte, welche an der Schule definiert sind.
	 *
	 * @return Inhalt des Feldes schuljahresabschnitte
	 */
	public List<ReportingSchuljahresabschnitt> schuljahresabschnitte() {
		return schuljahresabschnitte;
	}

	/**
	 * Die Schulform der Schule
	 *
	 * @return Inhalt des Feldes schulform
	 */
	public SchulformKatalogEintrag schulform() {
		return schulform;
	}

	/**
	 * Der Lehrer, der die Schulleitungsfunktion besitzt.
	 *
	 * @return Inhalt des Feldes schulleitung
	 */
	public ReportingLehrer schulleitung() {
		return schulleitung;
	}

	/**
	 * Das Schullogo der Schule im Base64-Format
	 *
	 * @return Inhalt des Feldes schullogo
	 */
	public String schullogo() {
		return schullogo;
	}

	/**
	 * Der Lehrer, der die stv. Schulleitungsfunktion besitzt.
	 *
	 * @return Inhalt des Feldes stvSchulleitung
	 */
	public ReportingLehrer stvSchulleitung() {
		return stvSchulleitung;
	}

	/**
	 * Die Adresse der Homepage der Schule (Domain-Name)
	 *
	 * @return Inhalt des Feldes webAdresse
	 */
	public String webAdresse() {
		return webAdresse;
	}

}
