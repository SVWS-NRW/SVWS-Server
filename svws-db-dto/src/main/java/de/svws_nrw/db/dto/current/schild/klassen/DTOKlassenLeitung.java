package de.svws_nrw.db.dto.current.schild.klassen;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle KlassenLehrer.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(DTOKlassenLeitungPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "KlassenLehrer")
@JsonPropertyOrder({"Klassen_ID", "Lehrer_ID", "Reihenfolge"})
public final class DTOKlassenLeitung {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOKlassenLeitung e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOKlassenLeitung e WHERE e.Klassen_ID = ?1 AND e.Lehrer_ID = ?2";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOKlassenLeitung e WHERE e.Klassen_ID IS NOT NULL AND e.Lehrer_ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Klassen_ID */
	public static final String QUERY_BY_KLASSEN_ID = "SELECT e FROM DTOKlassenLeitung e WHERE e.Klassen_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Klassen_ID */
	public static final String QUERY_LIST_BY_KLASSEN_ID = "SELECT e FROM DTOKlassenLeitung e WHERE e.Klassen_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Lehrer_ID */
	public static final String QUERY_BY_LEHRER_ID = "SELECT e FROM DTOKlassenLeitung e WHERE e.Lehrer_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Lehrer_ID */
	public static final String QUERY_LIST_BY_LEHRER_ID = "SELECT e FROM DTOKlassenLeitung e WHERE e.Lehrer_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Reihenfolge */
	public static final String QUERY_BY_REIHENFOLGE = "SELECT e FROM DTOKlassenLeitung e WHERE e.Reihenfolge = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Reihenfolge */
	public static final String QUERY_LIST_BY_REIHENFOLGE = "SELECT e FROM DTOKlassenLeitung e WHERE e.Reihenfolge IN ?1";

	/** ID der Klasse */
	@Id
	@Column(name = "Klassen_ID")
	@JsonProperty
	public long Klassen_ID;

	/** ID des Lehrers */
	@Id
	@Column(name = "Lehrer_ID")
	@JsonProperty
	public long Lehrer_ID;

	/** Die Reihenfolge, in welcher die Klassenlehrer in der Klassen angegeben werden. Kann zur Unterscheidung zwischen Klassenlehrern (1) und deren Stellvertretern (2, ...) genutzt werden, wenn keine alphabetische Reihenfolge gewünscht ist.  */
	@Column(name = "Reihenfolge")
	@JsonProperty
	public int Reihenfolge;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOKlassenLeitung ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOKlassenLeitung() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOKlassenLeitung ohne eine Initialisierung der Attribute.
	 * @param Klassen_ID   der Wert für das Attribut Klassen_ID
	 * @param Lehrer_ID   der Wert für das Attribut Lehrer_ID
	 * @param Reihenfolge   der Wert für das Attribut Reihenfolge
	 */
	public DTOKlassenLeitung(final long Klassen_ID, final long Lehrer_ID, final int Reihenfolge) {
		this.Klassen_ID = Klassen_ID;
		this.Lehrer_ID = Lehrer_ID;
		this.Reihenfolge = Reihenfolge;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOKlassenLeitung other = (DTOKlassenLeitung) obj;
		if (Klassen_ID != other.Klassen_ID)
			return false;
		return Lehrer_ID == other.Lehrer_ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(Klassen_ID);

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
		return "DTOKlassenLeitung(Klassen_ID=" + this.Klassen_ID + ", Lehrer_ID=" + this.Lehrer_ID + ", Reihenfolge=" + this.Reihenfolge + ")";
	}

}
