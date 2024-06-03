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
 * Diese Klasse dient als DTO für die Datenbanktabelle Gost_Blockung_Kurslehrer.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(DTOGostBlockungKurslehrerPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Gost_Blockung_Kurslehrer")
@JsonPropertyOrder({"Blockung_Kurs_ID", "Lehrer_ID", "Reihenfolge", "Wochenstunden"})
public final class DTOGostBlockungKurslehrer {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOGostBlockungKurslehrer e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOGostBlockungKurslehrer e WHERE e.Blockung_Kurs_ID = ?1 AND e.Lehrer_ID = ?2";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOGostBlockungKurslehrer e WHERE e.Blockung_Kurs_ID IS NOT NULL AND e.Lehrer_ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Blockung_Kurs_ID */
	public static final String QUERY_BY_BLOCKUNG_KURS_ID = "SELECT e FROM DTOGostBlockungKurslehrer e WHERE e.Blockung_Kurs_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Blockung_Kurs_ID */
	public static final String QUERY_LIST_BY_BLOCKUNG_KURS_ID = "SELECT e FROM DTOGostBlockungKurslehrer e WHERE e.Blockung_Kurs_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Lehrer_ID */
	public static final String QUERY_BY_LEHRER_ID = "SELECT e FROM DTOGostBlockungKurslehrer e WHERE e.Lehrer_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Lehrer_ID */
	public static final String QUERY_LIST_BY_LEHRER_ID = "SELECT e FROM DTOGostBlockungKurslehrer e WHERE e.Lehrer_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Reihenfolge */
	public static final String QUERY_BY_REIHENFOLGE = "SELECT e FROM DTOGostBlockungKurslehrer e WHERE e.Reihenfolge = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Reihenfolge */
	public static final String QUERY_LIST_BY_REIHENFOLGE = "SELECT e FROM DTOGostBlockungKurslehrer e WHERE e.Reihenfolge IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Wochenstunden */
	public static final String QUERY_BY_WOCHENSTUNDEN = "SELECT e FROM DTOGostBlockungKurslehrer e WHERE e.Wochenstunden = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Wochenstunden */
	public static final String QUERY_LIST_BY_WOCHENSTUNDEN = "SELECT e FROM DTOGostBlockungKurslehrer e WHERE e.Wochenstunden IN ?1";

	/** ID des Kurses */
	@Id
	@Column(name = "Blockung_Kurs_ID")
	@JsonProperty
	public long Blockung_Kurs_ID;

	/** ID des Lehrers, welcher dem Kurs zugeordnet ist */
	@Id
	@Column(name = "Lehrer_ID")
	@JsonProperty
	public long Lehrer_ID;

	/** Eine Reihenfolge für die Lehrer, z.B. zur Unterscheidung des eigentlichen Kurslehrer (z.B. 1) und einer Zusatzkraft (z.B. 2) */
	@Column(name = "Reihenfolge")
	@JsonProperty
	public int Reihenfolge;

	/** Die Anzahl der Wochenstunden für die der Lehrer in dem Kurs eingesetzt wird */
	@Column(name = "Wochenstunden")
	@JsonProperty
	public int Wochenstunden;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostBlockungKurslehrer ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOGostBlockungKurslehrer() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostBlockungKurslehrer ohne eine Initialisierung der Attribute.
	 * @param Blockung_Kurs_ID   der Wert für das Attribut Blockung_Kurs_ID
	 * @param Lehrer_ID   der Wert für das Attribut Lehrer_ID
	 * @param Reihenfolge   der Wert für das Attribut Reihenfolge
	 * @param Wochenstunden   der Wert für das Attribut Wochenstunden
	 */
	public DTOGostBlockungKurslehrer(final long Blockung_Kurs_ID, final long Lehrer_ID, final int Reihenfolge, final int Wochenstunden) {
		this.Blockung_Kurs_ID = Blockung_Kurs_ID;
		this.Lehrer_ID = Lehrer_ID;
		this.Reihenfolge = Reihenfolge;
		this.Wochenstunden = Wochenstunden;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOGostBlockungKurslehrer other = (DTOGostBlockungKurslehrer) obj;
		if (Blockung_Kurs_ID != other.Blockung_Kurs_ID)
			return false;
		return Lehrer_ID == other.Lehrer_ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(Blockung_Kurs_ID);

		result = prime * result + Long.hashCode(Lehrer_ID);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOGostBlockungKurslehrer(Blockung_Kurs_ID=" + this.Blockung_Kurs_ID + ", Lehrer_ID=" + this.Lehrer_ID + ", Reihenfolge=" + this.Reihenfolge + ", Wochenstunden=" + this.Wochenstunden + ")";
	}

}
