package de.svws_nrw.db.dto.current.schild.klassen;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.NamedQuery;
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
@NamedQuery(name = "DTOKlassenLeitung.all", query = "SELECT e FROM DTOKlassenLeitung e")
@NamedQuery(name = "DTOKlassenLeitung.klassen_id", query = "SELECT e FROM DTOKlassenLeitung e WHERE e.Klassen_ID = :value")
@NamedQuery(name = "DTOKlassenLeitung.klassen_id.multiple", query = "SELECT e FROM DTOKlassenLeitung e WHERE e.Klassen_ID IN :value")
@NamedQuery(name = "DTOKlassenLeitung.lehrer_id", query = "SELECT e FROM DTOKlassenLeitung e WHERE e.Lehrer_ID = :value")
@NamedQuery(name = "DTOKlassenLeitung.lehrer_id.multiple", query = "SELECT e FROM DTOKlassenLeitung e WHERE e.Lehrer_ID IN :value")
@NamedQuery(name = "DTOKlassenLeitung.reihenfolge", query = "SELECT e FROM DTOKlassenLeitung e WHERE e.Reihenfolge = :value")
@NamedQuery(name = "DTOKlassenLeitung.reihenfolge.multiple", query = "SELECT e FROM DTOKlassenLeitung e WHERE e.Reihenfolge IN :value")
@NamedQuery(name = "DTOKlassenLeitung.primaryKeyQuery", query = "SELECT e FROM DTOKlassenLeitung e WHERE e.Klassen_ID = ?1 AND e.Lehrer_ID = ?2")
@NamedQuery(name = "DTOKlassenLeitung.all.migration", query = "SELECT e FROM DTOKlassenLeitung e WHERE e.Klassen_ID IS NOT NULL AND e.Lehrer_ID IS NOT NULL")
@JsonPropertyOrder({"Klassen_ID", "Lehrer_ID", "Reihenfolge"})
public final class DTOKlassenLeitung {

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

		if (Lehrer_ID != other.Lehrer_ID)
			return false;
		return true;
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
