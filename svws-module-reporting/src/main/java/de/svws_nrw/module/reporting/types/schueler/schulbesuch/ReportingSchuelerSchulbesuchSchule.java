package de.svws_nrw.module.reporting.types.schueler.schulbesuch;

import de.svws_nrw.module.reporting.types.ReportingBaseType;
import de.svws_nrw.module.reporting.types.schule.ReportingSchulkatalogEintragNRW;

/**
 * Basis-Klasse im Rahmen des Reportings für Daten vom Typ ReportingSchuelerSchulbesuchSchule.
 */
public class ReportingSchuelerSchulbesuchSchule extends ReportingBaseType {

	/** Das Datum, ab wann die Schule besucht wurde. */
	protected final String datumVon;

	/** Das Datum, bis wann die Schule besucht wurde. */
	protected final String datumBis;

	/** Die ID des Abschlusses, welcher an der Schule erworben wurde. */
	protected final String idAbschlussart;

	/** Die ID des Grundes für die Entlassung von der Schule. */
	protected final Long idEntlassgrund;

	/** Die ID der Organisationsform der Schule (z. B. für Halbtagsunterricht). */
	protected final String idOrganisationsform;

	/** Die Daten der Schule. */
	protected final ReportingSchulkatalogEintragNRW schule;

	/** Der Jahrgang, ab wann die Schule besucht wurde. */
	protected final String jahrgangVon;

	/** Der Jahrgang, bis wann die Schule besucht wurde. */
	protected final String jahrgangBis;

	/** Der Schlüssel des Bildungsganges/Schulgliederung an der Schule. */
	protected final String schulgliederung;

	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis der übergebenen Parameter.
	 *
	 * @param datumVon Das Datum, ab wann die Schule besucht wurde.
	 * @param datumBis Das Datum, bis wann die Schule besucht wurde.
	 * @param idAbschlussart Die ID des Abschlusses, welcher an der Schule erworben wurde.
	 * @param idEntlassgrund Die ID des Grundes für die Entlassung von der Schule.
	 * @param idOrganisationsform Die ID der Organisationsform der Schule (z. B. für Halbtagsunterricht).
	 * @param jahrgangVon Der Jahrgang, ab wann die Schule besucht wurde.
	 * @param jahrgangBis Der Jahrgang, bis wann die Schule besucht wurde.
	 * @param schule Die Daten der Schule.
	 * @param schulgliederung Der Schlüssel des Bildungsganges/Schulgliederung an der Schule.
	 */
	@SuppressWarnings("java:S107") // Konstruktoren mit zu vielen Parametern (gemäß SonarQube) werden aktuell toleriert und nicht refacored (Stand 2026-04).
	public ReportingSchuelerSchulbesuchSchule(final String datumVon, final String datumBis, final String idAbschlussart, final Long idEntlassgrund,
			final String idOrganisationsform, final String jahrgangVon, final String jahrgangBis, final ReportingSchulkatalogEintragNRW schule,
			final String schulgliederung) {
		this.datumVon = ersetzeNullBlankTrim(datumVon);
		this.datumBis = ersetzeNullBlankTrim(datumBis);
		this.idAbschlussart = ersetzeNullBlankTrim(idAbschlussart);
		this.idEntlassgrund = idEntlassgrund;
		this.idOrganisationsform = ersetzeNullBlankTrim(idOrganisationsform);
		this.jahrgangVon = ersetzeNullBlankTrim(jahrgangVon);
		this.jahrgangBis = ersetzeNullBlankTrim(jahrgangBis);
		this.schule = schule;
		this.schulgliederung = ersetzeNullBlankTrim(schulgliederung);
	}

	// ##### Getter #####

	/**
	 * Gibt die ID des Abschlusses zurück.
	 *
	 * @return Die ID des Abschlusses; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String abschlussartID() {
		return idAbschlussart;
	}

	/**
	 * Gibt das Datum zurück, ab dem die Schule besucht wurde.
	 *
	 * @return Das Datum, ab dem die Schule besucht wurde; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String datumVon() {
		return datumVon;
	}

	/**
	 * Gibt das Datum zurück, bis wann die Schule besucht wurde.
	 *
	 * @return Das Datum, bis wann die Schule besucht wurde; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String datumBis() {
		return datumBis;
	}

	/**
	 * Gibt die ID des Grundes für die Entlassung zurück.
	 *
	 * @return Die ID des Grundes für die Entlassung.
	 */
	public Long entlassgrundID() {
		return idEntlassgrund;
	}

	/**
	 * Gibt die ID der Organisationsform der Schule zurück.
	 *
	 * @return Die ID der Organisationsform; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String organisationsFormID() {
		return idOrganisationsform;
	}

	/**
	 * Gibt den Jahrgang zurück, ab dem die Schule besucht wurde.
	 *
	 * @return Der Jahrgang, ab dem die Schule besucht wurde; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String jahrgangVon() {
		return jahrgangVon;
	}

	/**
	 * Gibt den Jahrgang zurück, bis wann die Schule besucht wurde.
	 *
	 * @return Der Jahrgang, bis wann die Schule besucht wurde; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String jahrgangBis() {
		return jahrgangBis;
	}

	/**
	 * Gibt die Daten der Schule zurück.
	 *
	 * @return Die Daten der Schule.
	 */
	public ReportingSchulkatalogEintragNRW schule() {
		return schule;
	}

	/**
	 * Gibt den Schlüssel des Bildungsganges/Schulgliederung zurück.
	 *
	 * @return Der Schlüssel des Bildungsganges/Schulgliederung; nie {@code null}, bei fehlendem Wert ein leerer String.
	 */
	public String schulgliederung() {
		return schulgliederung;
	}

}
