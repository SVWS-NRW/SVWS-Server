
package de.svws_nrw.module.reporting.types.schueler.schulbesuch;

import java.util.List;

import de.svws_nrw.core.data.kataloge.KatalogEntlassgrund;
import de.svws_nrw.module.reporting.types.ReportingBaseType;
import de.svws_nrw.module.reporting.types.schule.ReportingSchulkatalogEintragNRW;

/**
 * Basis-Klasse im Rahmen des Reportings für Daten vom Typ ReportingSchuelerSchulbesuch.
 */
public class ReportingSchuelerSchulbesuch extends ReportingBaseType {

	/** Die vorher besuchte Schule. */
	protected final ReportingSchulkatalogEintragNRW vorherigeSchule;

	/** Die allgemeine Herkunftsart des Schüler in Bezug auf die schulform der zuvor besuchten Schule. */
	protected final String vorigeAllgHerkunft;

	/** Das Entlassdatum an der zuvor besuchten Schule. */
	protected final String vorigeEntlassdatum;

	/** Der Entlassjahrgang an der zuvor besuchten Schule. */
	protected final String vorigeEntlassjahrgang;

	/** Die ID der Art der letzten Versetzung an der zuvor besuchten Schule. */
	protected final String vorigeArtLetzteVersetzung;

	/** Bemerkungen zu der zuvor besuchten Schule. */
	protected final String vorigeBemerkung;

	/** Der Grund für die Entlassung von der zuvor besuchten Schule. */
	protected final KatalogEntlassgrund vorigeEntlassgrund;

	/** Die ID des Abschlusses, welcher an der zuvor besuchten Schule erworben wurde. */
	protected final String vorigeAbschlussartID;

	/** Das Entlassdatum von dieser Schule. */
	protected final String entlassungDatum;

	/** Der Jahrgang bei der Entlassung von dieser Schule. */
	protected final String entlassungJahrgang;

	/** Der Grund für die Entlassung von dieser Schule. */
	protected final KatalogEntlassgrund entlassungGrund;

	/** Die ID des Abschlusses, welcher an dieser Schule erworben wurde. */
	protected final String entlassungAbschlussartID;

	/** Die aufnehmende Schule nach einer Entlassung. */
	protected final ReportingSchulkatalogEintragNRW aufnehmendeSchule;

	/** Das Datum beim Wechsel zu einer aufnehmenden Schule. */
	protected final String aufnehmendWechseldatum;

	/** Gibt an, ob die aufnehmende Schule den Wechsel bestätigt hat. */
	protected final Boolean aufnehmendBestaetigt;

	/** Das Jahr der Einschulung in die Grundschule. */
	protected final Integer grundschuleEinschulungsjahr;

	/** Die ID der Einschulungsart in die Grundschule. */
	protected final Long grundschuleEinschulungsartID;

	/** Die ID der Schuleingangsphase der Grundschule. */
	protected final Long idGrundschuleJahreEingangsphase;

	/** Das Kürzel für die Übergangsempfehlung der Grundschule in die Sekundarstufe I */
	protected final String kuerzelGrundschuleUebergangsempfehlung;

	/** Das Jahr des Wechsels in die Sekundarstufe I. */
	protected final Integer sekIWechsel;

	/** Das Kürzel der ersten Schulform in der Sekundarstufe I */
	protected final String sekIErsteSchulform;

	/** Das Jahr des Wechsels in die Sekundarstufe II. */
	protected final Integer sekIIWechsel;

	/** Die ID der Dauer des Kindergartenbesuchs eines Schülers. */
	protected final Long idDauerKindergartenbesuch;

	/** Die ID des Kindergartens. */
	protected final Long idKindergarten;

	/** Schüler wurde zu einem Sprachförderkurs verpflichtet (Ja/Nein). */
	protected final boolean verpflichtungSprachfoerderkurs;

	/** Teilnahme des Schülers an einem Sprachförderkurs (Ja/Nein). */
	protected final boolean teilnahmeSprachfoerderkurs;

	/** Die Informationen zu allen bisher besuchten Schulen. */
	protected final List<ReportingSchuelerSchulbesuchSchule> alleSchulen;

	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis der übergebenen Parameter.
	 *
	 * @param vorherigeSchule Die vorher besuchte Schule.
	 * @param vorigeAllgHerkunft Die allgemeine Herkunftsart des Schüler in Bezug auf die schulform der zuvor besuchten Schule.
	 * @param vorigeEntlassdatum Das Entlassdatum an der zuvor besuchten Schule.
	 * @param vorigeEntlassjahrgang Der Entlassjahrgang an der zuvor besuchten Schule.
	 * @param vorigeArtLetzteVersetzung Die ID der Art der letzten Versetzung an der zuvor besuchten Schule.
	 * @param vorigeBemerkung Bemerkungen zu der zuvor besuchten Schule.
	 * @param vorigeEntlassgrund Die ID des Grundes für die Entlassung von der zuvor besuchten Schule.
	 * @param vorigeAbschlussartID Die ID des Abschlusses, welcher an der zuvor besuchten Schule erworben wurde.
	 * @param entlassungDatum Das Entlassdatum von dieser Schule.
	 * @param entlassungJahrgang Der Jahrgang bei der Entlassung von dieser Schule.
	 * @param entlassungGrund Die ID des Grundes für die Entlassung von dieser Schule.
	 * @param entlassungAbschlussartID Die ID des Abschlusses, welcher an dieser Schule erworben wurde.
	 * @param aufnehmendeSchule Die aufnehmende Schule nach einer Entlassung.
	 * @param aufnehmendWechseldatum Das Datum beim Wechsel zu einer aufnehmenden Schule.
	 * @param aufnehmendBestaetigt Gibt an, ob die aufnehmende Schule den Wechsel bestätigt hat.
	 * @param grundschuleEinschulungsjahr Das Jahr der Einschulung in die Grundschule.
	 * @param grundschuleEinschulungsartID Die ID der Einschulungsart in die Grundschule.
	 * @param idGrundschuleJahreEingangsphase Die ID der Schuleingangsphase der Grundschule.
	 * @param kuerzelGrundschuleUebergangsempfehlung Das Kürzel für die Übergangsempfehlung der Grundschule in die Sekundarstufe I.
	 * @param sekIWechsel Das Jahr des Wechsels in die Sekundarstufe I.
	 * @param sekIErsteSchulform Das Kürzel der ersten Schulform in der Sekundarstufe I.
	 * @param sekIIWechsel Das Jahr des Wechsels in die Sekundarstufe II.
	 * @param idDauerKindergartenbesuch Die ID der Dauer des Kindergartenbesuchs eines Schülers.
	 * @param idKindergarten Die ID des Kindergartens.
	 * @param verpflichtungSprachfoerderkurs Schüler wurde zu einem Sprachförderkurs verpflichtet (Ja/Nein).
	 * @param teilnahmeSprachfoerderkurs Teilnahme des Schülers an einem Sprachförderkurs (Ja/Nein).
	 * @param alleSchulen Die Informationen zu allen bisher besuchten Schulen.
	 */
	public ReportingSchuelerSchulbesuch(final ReportingSchulkatalogEintragNRW vorherigeSchule, final String vorigeAllgHerkunft,
			final String vorigeEntlassdatum, final String vorigeEntlassjahrgang, final String vorigeArtLetzteVersetzung,
			final String vorigeBemerkung, final KatalogEntlassgrund vorigeEntlassgrund, final String vorigeAbschlussartID,
			final String entlassungDatum, final String entlassungJahrgang, final KatalogEntlassgrund entlassungGrund,
			final String entlassungAbschlussartID, final ReportingSchulkatalogEintragNRW aufnehmendeSchule, final String aufnehmendWechseldatum,
			final Boolean aufnehmendBestaetigt, final Integer grundschuleEinschulungsjahr, final Long grundschuleEinschulungsartID,
			final Long idGrundschuleJahreEingangsphase, final String kuerzelGrundschuleUebergangsempfehlung,
			final Integer sekIWechsel, final String sekIErsteSchulform, final Integer sekIIWechsel,
			final Long idDauerKindergartenbesuch, final Long idKindergarten, final boolean verpflichtungSprachfoerderkurs,
			final boolean teilnahmeSprachfoerderkurs, final List<ReportingSchuelerSchulbesuchSchule> alleSchulen) {
		this.vorherigeSchule = vorherigeSchule;
		this.vorigeAllgHerkunft = vorigeAllgHerkunft;
		this.vorigeEntlassdatum = vorigeEntlassdatum;
		this.vorigeEntlassjahrgang = vorigeEntlassjahrgang;
		this.vorigeArtLetzteVersetzung = vorigeArtLetzteVersetzung;
		this.vorigeBemerkung = vorigeBemerkung;
		this.vorigeEntlassgrund = vorigeEntlassgrund;
		this.vorigeAbschlussartID = vorigeAbschlussartID;
		this.entlassungDatum = entlassungDatum;
		this.entlassungJahrgang = entlassungJahrgang;
		this.entlassungGrund = entlassungGrund;
		this.entlassungAbschlussartID = entlassungAbschlussartID;
		this.aufnehmendeSchule = aufnehmendeSchule;
		this.aufnehmendWechseldatum = aufnehmendWechseldatum;
		this.aufnehmendBestaetigt = aufnehmendBestaetigt;
		this.grundschuleEinschulungsjahr = grundschuleEinschulungsjahr;
		this.grundschuleEinschulungsartID = grundschuleEinschulungsartID;
		this.idGrundschuleJahreEingangsphase = idGrundschuleJahreEingangsphase;
		this.kuerzelGrundschuleUebergangsempfehlung = kuerzelGrundschuleUebergangsempfehlung;
		this.sekIWechsel = sekIWechsel;
		this.sekIErsteSchulform = sekIErsteSchulform;
		this.sekIIWechsel = sekIIWechsel;
		this.idDauerKindergartenbesuch = idDauerKindergartenbesuch;
		this.idKindergarten = idKindergarten;
		this.verpflichtungSprachfoerderkurs = verpflichtungSprachfoerderkurs;
		this.teilnahmeSprachfoerderkurs = teilnahmeSprachfoerderkurs;
		this.alleSchulen = alleSchulen;
	}

	// ##### Getter #####

	/**
	 * Gibt die vorher besuchte Schule zurück.
	 *
	 * @return Die vorher besuchte Schule.
	 */
	public ReportingSchulkatalogEintragNRW vorherigeSchule() {
		return vorherigeSchule;
	}

	/**
	 * Gibt die allgemeine Herkunftsart des Schülers zurück.
	 *
	 * @return Die allgemeine Herkunftsart des Schülers.
	 */
	public String vorigeAllgHerkunft() {
		return vorigeAllgHerkunft;
	}

	/**
	 * Gibt das Entlassdatum an der zuvor besuchten Schule zurück.
	 *
	 * @return Das Entlassdatum an der zuvor besuchten Schule.
	 */
	public String vorigeEntlassdatum() {
		return vorigeEntlassdatum;
	}

	/**
	 * Gibt den Entlassjahrgang an der zuvor besuchten Schule zurück.
	 *
	 * @return Der Entlassjahrgang an der zuvor besuchten Schule.
	 */
	public String vorigeEntlassjahrgang() {
		return vorigeEntlassjahrgang;
	}

	/**
	 * Gibt die ID der Art der letzten Versetzung an der zuvor besuchten Schule zurück.
	 *
	 * @return Die ID der Art der letzten Versetzung.
	 */
	public String vorigeArtLetzteVersetzung() {
		return vorigeArtLetzteVersetzung;
	}

	/**
	 * Gibt die Bemerkungen zu der zuvor besuchten Schule zurück.
	 *
	 * @return Die Bemerkungen zu der zuvor besuchten Schule.
	 */
	public String vorigeBemerkung() {
		return vorigeBemerkung;
	}

	/**
	 * Gibt den Grund für die Entlassung von der zuvor besuchten Schule zurück.
	 *
	 * @return Der Grund für die Entlassung.
	 */
	public KatalogEntlassgrund vorigeEntlassgrund() {
		return vorigeEntlassgrund;
	}

	/**
	 * Gibt die ID des Abschlusses der zuvor besuchten Schule zurück.
	 *
	 * @return Die ID des Abschlusses.
	 */
	public String vorigeAbschlussartID() {
		return vorigeAbschlussartID;
	}

	/**
	 * Gibt das Entlassdatum von dieser Schule zurück.
	 *
	 * @return Das Entlassdatum von dieser Schule.
	 */
	public String entlassungDatum() {
		return entlassungDatum;
	}

	/**
	 * Gibt den Jahrgang bei der Entlassung von dieser Schule zurück.
	 *
	 * @return Der Jahrgang bei der Entlassung.
	 */
	public String entlassungJahrgang() {
		return entlassungJahrgang;
	}

	/**
	 * Gibt den Grund für die Entlassung von dieser Schule zurück.
	 *
	 * @return Der Grund für die Entlassung.
	 */
	public KatalogEntlassgrund entlassungGrund() {
		return entlassungGrund;
	}

	/**
	 * Gibt die ID des Abschlusses dieser Schule zurück.
	 *
	 * @return Die ID des Abschlusses.
	 */
	public String entlassungAbschlussartID() {
		return entlassungAbschlussartID;
	}

	/**
	 * Gibt die aufnehmende Schule nach einer Entlassung zurück.
	 *
	 * @return Die aufnehmende Schule.
	 */
	public ReportingSchulkatalogEintragNRW aufnehmendeSchule() {
		return aufnehmendeSchule;
	}

	/**
	 * Gibt das Datum beim Wechsel zu einer aufnehmenden Schule zurück.
	 *
	 * @return Das Datum beim Wechsel.
	 */
	public String aufnehmendWechseldatum() {
		return aufnehmendWechseldatum;
	}

	/**
	 * Gibt zurück, ob die aufnehmende Schule den Wechsel bestätigt hat.
	 *
	 * @return true, wenn bestätigt, sonst false.
	 */
	public Boolean aufnehmendBestaetigt() {
		return aufnehmendBestaetigt;
	}

	/**
	 * Gibt das Jahr der Einschulung in die Grundschule zurück.
	 *
	 * @return Das Jahr der Einschulung.
	 */
	public Integer grundschuleEinschulungsjahr() {
		return grundschuleEinschulungsjahr;
	}

	/**
	 * Gibt die ID der Einschulungsart in die Grundschule zurück.
	 *
	 * @return Die ID der Einschulungsart.
	 */
	public Long grundschuleEinschulungsartID() {
		return grundschuleEinschulungsartID;
	}

	/**
	 * Gibt die ID der Schuleingangsphase der Grundschule zurück.
	 *
	 * @return Die ID der Schuleingangsphase.
	 */
	public Long idGrundschuleJahreEingangsphase() {
		return idGrundschuleJahreEingangsphase;
	}

	/**
	 * Gibt das Kürzel für die Übergangsempfehlung der Grundschule zurück.
	 *
	 * @return Das Kürzel für die Übergangsempfehlung.
	 */
	public String kuerzelGrundschuleUebergangsempfehlung() {
		return kuerzelGrundschuleUebergangsempfehlung;
	}

	/**
	 * Gibt das Jahr des Wechsels in die Sekundarstufe I zurück.
	 *
	 * @return Das Jahr des Wechsels.
	 */
	public Integer sekIWechsel() {
		return sekIWechsel;
	}

	/**
	 * Gibt das Kürzel der ersten Schulform in der Sekundarstufe I zurück.
	 *
	 * @return Das Kürzel der ersten Schulform.
	 */
	public String sekIErsteSchulform() {
		return sekIErsteSchulform;
	}

	/**
	 * Gibt das Jahr des Wechsels in die Sekundarstufe II zurück.
	 *
	 * @return Das Jahr des Wechsels.
	 */
	public Integer sekIIWechsel() {
		return sekIIWechsel;
	}

	/**
	 * Gibt die ID der Dauer des Kindergartenbesuchs zurück.
	 *
	 * @return Die ID der Dauer des Kindergartenbesuchs.
	 */
	public Long idDauerKindergartenbesuch() {
		return idDauerKindergartenbesuch;
	}

	/**
	 * Gibt die ID des Kindergartens zurück.
	 *
	 * @return Die ID des Kindergartens.
	 */
	public Long idKindergarten() {
		return idKindergarten;
	}

	/**
	 * Gibt zurück, ob der Schüler zu einem Sprachförderkurs verpflichtet wurde.
	 *
	 * @return true, wenn verpflichtet, sonst false.
	 */
	public boolean verpflichtungSprachfoerderkurs() {
		return verpflichtungSprachfoerderkurs;
	}

	/**
	 * Gibt zurück, ob der Schüler an einem Sprachförderkurs teilgenommen hat.
	 *
	 * @return true, wenn teilgenommen, sonst false.
	 */
	public boolean teilnahmeSprachfoerderkurs() {
		return teilnahmeSprachfoerderkurs;
	}

	/**
	 * Gibt die Liste aller bisher besuchten Schulen zurück.
	 *
	 * @return Die Liste aller bisher besuchten Schulen.
	 */
	public List<ReportingSchuelerSchulbesuchSchule> alleSchulen() {
		return alleSchulen;
	}

}
