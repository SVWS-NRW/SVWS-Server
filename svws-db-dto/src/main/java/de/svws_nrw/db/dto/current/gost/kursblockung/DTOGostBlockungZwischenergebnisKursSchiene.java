package de.svws_nrw.db.dto.current.gost.kursblockung;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Gost_Blockung_Zwischenergebnisse_Kurs_Schienen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(DTOGostBlockungZwischenergebnisKursSchienePK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Gost_Blockung_Zwischenergebnisse_Kurs_Schienen")
@JsonPropertyOrder({"Zwischenergebnis_ID", "Blockung_Kurs_ID", "Schienen_ID"})
public final class DTOGostBlockungZwischenergebnisKursSchiene {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOGostBlockungZwischenergebnisKursSchiene e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOGostBlockungZwischenergebnisKursSchiene e WHERE e.Zwischenergebnis_ID = ?1 AND e.Blockung_Kurs_ID = ?2 AND e.Schienen_ID = ?3";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOGostBlockungZwischenergebnisKursSchiene e WHERE e.Zwischenergebnis_ID IS NOT NULL AND e.Blockung_Kurs_ID IS NOT NULL AND e.Schienen_ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Zwischenergebnis_ID */
	public static final String QUERY_BY_ZWISCHENERGEBNIS_ID = "SELECT e FROM DTOGostBlockungZwischenergebnisKursSchiene e WHERE e.Zwischenergebnis_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Zwischenergebnis_ID */
	public static final String QUERY_LIST_BY_ZWISCHENERGEBNIS_ID = "SELECT e FROM DTOGostBlockungZwischenergebnisKursSchiene e WHERE e.Zwischenergebnis_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Blockung_Kurs_ID */
	public static final String QUERY_BY_BLOCKUNG_KURS_ID = "SELECT e FROM DTOGostBlockungZwischenergebnisKursSchiene e WHERE e.Blockung_Kurs_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Blockung_Kurs_ID */
	public static final String QUERY_LIST_BY_BLOCKUNG_KURS_ID = "SELECT e FROM DTOGostBlockungZwischenergebnisKursSchiene e WHERE e.Blockung_Kurs_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schienen_ID */
	public static final String QUERY_BY_SCHIENEN_ID = "SELECT e FROM DTOGostBlockungZwischenergebnisKursSchiene e WHERE e.Schienen_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schienen_ID */
	public static final String QUERY_LIST_BY_SCHIENEN_ID = "SELECT e FROM DTOGostBlockungZwischenergebnisKursSchiene e WHERE e.Schienen_ID IN ?1";

	/** Kurs-Schienen-Zuordnung eines Zwischenergebnisses: ID des Zwischenergebnisses */
	@Id
	@Column(name = "Zwischenergebnis_ID")
	@JsonProperty
	public long Zwischenergebnis_ID;

	/** Kurs-Schienen-Zuordnung eines Zwischenergebnisses: ID des Kurses */
	@Id
	@Column(name = "Blockung_Kurs_ID")
	@JsonProperty
	public long Blockung_Kurs_ID;

	/** Kurs-Schienen-Zuordnung eines Zwischenergebnisses: ID der Schiene aus der Blockung */
	@Id
	@Column(name = "Schienen_ID")
	@JsonProperty
	public long Schienen_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostBlockungZwischenergebnisKursSchiene ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOGostBlockungZwischenergebnisKursSchiene() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostBlockungZwischenergebnisKursSchiene ohne eine Initialisierung der Attribute.
	 * @param Zwischenergebnis_ID   der Wert für das Attribut Zwischenergebnis_ID
	 * @param Blockung_Kurs_ID   der Wert für das Attribut Blockung_Kurs_ID
	 * @param Schienen_ID   der Wert für das Attribut Schienen_ID
	 */
	public DTOGostBlockungZwischenergebnisKursSchiene(final long Zwischenergebnis_ID, final long Blockung_Kurs_ID, final long Schienen_ID) {
		this.Zwischenergebnis_ID = Zwischenergebnis_ID;
		this.Blockung_Kurs_ID = Blockung_Kurs_ID;
		this.Schienen_ID = Schienen_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOGostBlockungZwischenergebnisKursSchiene other = (DTOGostBlockungZwischenergebnisKursSchiene) obj;
		if (Zwischenergebnis_ID != other.Zwischenergebnis_ID)
			return false;
		if (Blockung_Kurs_ID != other.Blockung_Kurs_ID)
			return false;
		return Schienen_ID == other.Schienen_ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(Zwischenergebnis_ID);

		result = prime * result + Long.hashCode(Blockung_Kurs_ID);

		result = prime * result + Long.hashCode(Schienen_ID);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOGostBlockungZwischenergebnisKursSchiene(Zwischenergebnis_ID=" + this.Zwischenergebnis_ID + ", Blockung_Kurs_ID=" + this.Blockung_Kurs_ID + ", Schienen_ID=" + this.Schienen_ID + ")";
	}

}
