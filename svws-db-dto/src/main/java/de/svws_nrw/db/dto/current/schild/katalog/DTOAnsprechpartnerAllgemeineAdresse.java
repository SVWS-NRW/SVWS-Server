package de.svws_nrw.db.dto.current.schild.katalog;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle AllgAdrAnsprechpartner.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "AllgAdrAnsprechpartner")
@JsonPropertyOrder({"ID", "Adresse_ID", "Name", "Vorname", "Anrede", "Telefon", "Email", "Abteilung", "Titel", "GU_ID"})
public final class DTOAnsprechpartnerAllgemeineAdresse {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOAnsprechpartnerAllgemeineAdresse e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOAnsprechpartnerAllgemeineAdresse e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOAnsprechpartnerAllgemeineAdresse e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOAnsprechpartnerAllgemeineAdresse e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOAnsprechpartnerAllgemeineAdresse e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOAnsprechpartnerAllgemeineAdresse e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Adresse_ID */
	public static final String QUERY_BY_ADRESSE_ID = "SELECT e FROM DTOAnsprechpartnerAllgemeineAdresse e WHERE e.Adresse_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Adresse_ID */
	public static final String QUERY_LIST_BY_ADRESSE_ID = "SELECT e FROM DTOAnsprechpartnerAllgemeineAdresse e WHERE e.Adresse_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Name */
	public static final String QUERY_BY_NAME = "SELECT e FROM DTOAnsprechpartnerAllgemeineAdresse e WHERE e.Name = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Name */
	public static final String QUERY_LIST_BY_NAME = "SELECT e FROM DTOAnsprechpartnerAllgemeineAdresse e WHERE e.Name IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Vorname */
	public static final String QUERY_BY_VORNAME = "SELECT e FROM DTOAnsprechpartnerAllgemeineAdresse e WHERE e.Vorname = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Vorname */
	public static final String QUERY_LIST_BY_VORNAME = "SELECT e FROM DTOAnsprechpartnerAllgemeineAdresse e WHERE e.Vorname IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Anrede */
	public static final String QUERY_BY_ANREDE = "SELECT e FROM DTOAnsprechpartnerAllgemeineAdresse e WHERE e.Anrede = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Anrede */
	public static final String QUERY_LIST_BY_ANREDE = "SELECT e FROM DTOAnsprechpartnerAllgemeineAdresse e WHERE e.Anrede IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Telefon */
	public static final String QUERY_BY_TELEFON = "SELECT e FROM DTOAnsprechpartnerAllgemeineAdresse e WHERE e.Telefon = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Telefon */
	public static final String QUERY_LIST_BY_TELEFON = "SELECT e FROM DTOAnsprechpartnerAllgemeineAdresse e WHERE e.Telefon IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Email */
	public static final String QUERY_BY_EMAIL = "SELECT e FROM DTOAnsprechpartnerAllgemeineAdresse e WHERE e.Email = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Email */
	public static final String QUERY_LIST_BY_EMAIL = "SELECT e FROM DTOAnsprechpartnerAllgemeineAdresse e WHERE e.Email IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Abteilung */
	public static final String QUERY_BY_ABTEILUNG = "SELECT e FROM DTOAnsprechpartnerAllgemeineAdresse e WHERE e.Abteilung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Abteilung */
	public static final String QUERY_LIST_BY_ABTEILUNG = "SELECT e FROM DTOAnsprechpartnerAllgemeineAdresse e WHERE e.Abteilung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Titel */
	public static final String QUERY_BY_TITEL = "SELECT e FROM DTOAnsprechpartnerAllgemeineAdresse e WHERE e.Titel = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Titel */
	public static final String QUERY_LIST_BY_TITEL = "SELECT e FROM DTOAnsprechpartnerAllgemeineAdresse e WHERE e.Titel IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes GU_ID */
	public static final String QUERY_BY_GU_ID = "SELECT e FROM DTOAnsprechpartnerAllgemeineAdresse e WHERE e.GU_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes GU_ID */
	public static final String QUERY_LIST_BY_GU_ID = "SELECT e FROM DTOAnsprechpartnerAllgemeineAdresse e WHERE e.GU_ID IN ?1";

	/** ID des Ansprechpartners der Tabelle AllgAdresse (Betriebe) */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** ID des Betriebs (der Adresse) aus der Tabelle AllgAdresse */
	@Column(name = "Adresse_ID")
	@JsonProperty
	public long Adresse_ID;

	/** Name des Ansprechpartners im Betrieb PAuswG vom 21.6.2019 §5 Abs. 2 */
	@Column(name = "Name")
	@JsonProperty
	public String Name;

	/** Vorname des Ansprechpartners im Betrieb PAuswG vom 21.6.2019 §5 Abs. 2. Wird im Client mit Rufname angezeigt. */
	@Column(name = "Vorname")
	@JsonProperty
	public String Vorname;

	/** Anrede des Ansprechpartners im Betrieb */
	@Column(name = "Anrede")
	@JsonProperty
	public String Anrede;

	/** Telefonnummer des Ansprechpartners im Betrieb */
	@Column(name = "Telefon")
	@JsonProperty
	public String Telefon;

	/** Email-Adresse des Ansprechpartners im Betrieb */
	@Column(name = "Email")
	@JsonProperty
	public String Email;

	/** ggf Abteilung des Ansprechpartners im Betrieb */
	@Column(name = "Abteilung")
	@JsonProperty
	public String Abteilung;

	/** Titel des Ansprechpartners im Betrieb */
	@Column(name = "Titel")
	@JsonProperty
	public String Titel;

	/** GU_ID des Ansprechpartners im Betrieb */
	@Column(name = "GU_ID")
	@JsonProperty
	public String GU_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOAnsprechpartnerAllgemeineAdresse ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOAnsprechpartnerAllgemeineAdresse() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOAnsprechpartnerAllgemeineAdresse ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Adresse_ID   der Wert für das Attribut Adresse_ID
	 */
	public DTOAnsprechpartnerAllgemeineAdresse(final long ID, final long Adresse_ID) {
		this.ID = ID;
		this.Adresse_ID = Adresse_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOAnsprechpartnerAllgemeineAdresse other = (DTOAnsprechpartnerAllgemeineAdresse) obj;
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
		return "DTOAnsprechpartnerAllgemeineAdresse(ID=" + this.ID + ", Adresse_ID=" + this.Adresse_ID + ", Name=" + this.Name + ", Vorname=" + this.Vorname + ", Anrede=" + this.Anrede + ", Telefon=" + this.Telefon + ", Email=" + this.Email + ", Abteilung=" + this.Abteilung + ", Titel=" + this.Titel + ", GU_ID=" + this.GU_ID + ")";
	}

}
