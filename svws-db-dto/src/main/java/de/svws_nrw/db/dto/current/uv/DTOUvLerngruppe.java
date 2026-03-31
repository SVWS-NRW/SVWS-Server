package de.svws_nrw.db.dto.current.uv;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle UV_Lerngruppen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "UV_Lerngruppen")
@JsonPropertyOrder({"ID", "Klasse_ID", "Fach_ID", "Kurs_ID", "Planungsabschnitt_ID", "Wochenstunden", "WochenstundenUnterrichtet", "KoopSchulNr", "KoopAnzahlExterne"})
public final class DTOUvLerngruppe {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOUvLerngruppe e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOUvLerngruppe e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOUvLerngruppe e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOUvLerngruppe e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOUvLerngruppe e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOUvLerngruppe e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Klasse_ID */
	public static final String QUERY_BY_KLASSE_ID = "SELECT e FROM DTOUvLerngruppe e WHERE e.Klasse_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Klasse_ID */
	public static final String QUERY_LIST_BY_KLASSE_ID = "SELECT e FROM DTOUvLerngruppe e WHERE e.Klasse_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Fach_ID */
	public static final String QUERY_BY_FACH_ID = "SELECT e FROM DTOUvLerngruppe e WHERE e.Fach_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Fach_ID */
	public static final String QUERY_LIST_BY_FACH_ID = "SELECT e FROM DTOUvLerngruppe e WHERE e.Fach_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Kurs_ID */
	public static final String QUERY_BY_KURS_ID = "SELECT e FROM DTOUvLerngruppe e WHERE e.Kurs_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Kurs_ID */
	public static final String QUERY_LIST_BY_KURS_ID = "SELECT e FROM DTOUvLerngruppe e WHERE e.Kurs_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Planungsabschnitt_ID */
	public static final String QUERY_BY_PLANUNGSABSCHNITT_ID = "SELECT e FROM DTOUvLerngruppe e WHERE e.Planungsabschnitt_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Planungsabschnitt_ID */
	public static final String QUERY_LIST_BY_PLANUNGSABSCHNITT_ID = "SELECT e FROM DTOUvLerngruppe e WHERE e.Planungsabschnitt_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Wochenstunden */
	public static final String QUERY_BY_WOCHENSTUNDEN = "SELECT e FROM DTOUvLerngruppe e WHERE e.Wochenstunden = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Wochenstunden */
	public static final String QUERY_LIST_BY_WOCHENSTUNDEN = "SELECT e FROM DTOUvLerngruppe e WHERE e.Wochenstunden IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes WochenstundenUnterrichtet */
	public static final String QUERY_BY_WOCHENSTUNDENUNTERRICHTET = "SELECT e FROM DTOUvLerngruppe e WHERE e.WochenstundenUnterrichtet = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes WochenstundenUnterrichtet */
	public static final String QUERY_LIST_BY_WOCHENSTUNDENUNTERRICHTET = "SELECT e FROM DTOUvLerngruppe e WHERE e.WochenstundenUnterrichtet IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes KoopSchulNr */
	public static final String QUERY_BY_KOOPSCHULNR = "SELECT e FROM DTOUvLerngruppe e WHERE e.KoopSchulNr = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes KoopSchulNr */
	public static final String QUERY_LIST_BY_KOOPSCHULNR = "SELECT e FROM DTOUvLerngruppe e WHERE e.KoopSchulNr IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes KoopAnzahlExterne */
	public static final String QUERY_BY_KOOPANZAHLEXTERNE = "SELECT e FROM DTOUvLerngruppe e WHERE e.KoopAnzahlExterne = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes KoopAnzahlExterne */
	public static final String QUERY_LIST_BY_KOOPANZAHLEXTERNE = "SELECT e FROM DTOUvLerngruppe e WHERE e.KoopAnzahlExterne IN ?1";

	/** Eindeutige ID der Lerngruppe im Planungsabschnitt (automatisch generiert) */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Fremdschlüssel auf die Klasse (Tabelle UV_Klassen) */
	@Column(name = "Klasse_ID")
	@JsonProperty
	public Long Klasse_ID;

	/** Fremdschlüssel auf das Fach (Tabelle UV_Faecher) */
	@Column(name = "Fach_ID")
	@JsonProperty
	public Long Fach_ID;

	/** Fremdschlüssel auf den Kurs (Tabelle UV_Kurse) */
	@Column(name = "Kurs_ID")
	@JsonProperty
	public Long Kurs_ID;

	/** Die ID des Planungsabschnitts als Fremdschlüssel auf die Tabelle UV_Planungsabschnitte */
	@Column(name = "Planungsabschnitt_ID")
	@JsonProperty
	public long Planungsabschnitt_ID;

	/** Die Anzahl der vorgesehenen Wochenstunden für die Lerngruppe */
	@Column(name = "Wochenstunden")
	@JsonProperty
	public double Wochenstunden;

	/** Die Anzahl der Wochenstunden, die die Lerngruppe tatsächlich unterrichtet wurde */
	@Column(name = "WochenstundenUnterrichtet")
	@JsonProperty
	public double WochenstundenUnterrichtet;

	/** Schulnummer von Koopschule, null falls kein Koop */
	@Column(name = "KoopSchulNr")
	@JsonProperty
	public String KoopSchulNr;

	/** Die Anzahl der externen Schüler von Koop-Schulen */
	@Column(name = "KoopAnzahlExterne")
	@JsonProperty
	public int KoopAnzahlExterne;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOUvLerngruppe ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOUvLerngruppe() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOUvLerngruppe ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Planungsabschnitt_ID   der Wert für das Attribut Planungsabschnitt_ID
	 * @param Wochenstunden   der Wert für das Attribut Wochenstunden
	 * @param WochenstundenUnterrichtet   der Wert für das Attribut WochenstundenUnterrichtet
	 * @param KoopAnzahlExterne   der Wert für das Attribut KoopAnzahlExterne
	 */
	public DTOUvLerngruppe(final long ID, final long Planungsabschnitt_ID, final double Wochenstunden, final double WochenstundenUnterrichtet, final int KoopAnzahlExterne) {
		this.ID = ID;
		this.Planungsabschnitt_ID = Planungsabschnitt_ID;
		this.Wochenstunden = Wochenstunden;
		this.WochenstundenUnterrichtet = WochenstundenUnterrichtet;
		this.KoopAnzahlExterne = KoopAnzahlExterne;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		DTOUvLerngruppe other = (DTOUvLerngruppe) obj;
		return ID == other.ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(ID);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOUvLerngruppe(ID=" + this.ID + ", Klasse_ID=" + this.Klasse_ID + ", Fach_ID=" + this.Fach_ID + ", Kurs_ID=" + this.Kurs_ID + ", Planungsabschnitt_ID=" + this.Planungsabschnitt_ID + ", Wochenstunden=" + this.Wochenstunden + ", WochenstundenUnterrichtet=" + this.WochenstundenUnterrichtet + ", KoopSchulNr=" + this.KoopSchulNr + ", KoopAnzahlExterne=" + this.KoopAnzahlExterne + ")";
	}

}
