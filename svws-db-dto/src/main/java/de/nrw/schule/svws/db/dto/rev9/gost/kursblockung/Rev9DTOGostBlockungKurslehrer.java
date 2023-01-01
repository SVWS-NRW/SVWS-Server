package de.nrw.schule.svws.db.dto.rev9.gost.kursblockung;

import de.nrw.schule.svws.db.DBEntityManager;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Gost_Blockung_Kurslehrer.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(Rev9DTOGostBlockungKurslehrerPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Gost_Blockung_Kurslehrer")
@NamedQuery(name="Rev9DTOGostBlockungKurslehrer.all", query="SELECT e FROM Rev9DTOGostBlockungKurslehrer e")
@NamedQuery(name="Rev9DTOGostBlockungKurslehrer.blockung_kurs_id", query="SELECT e FROM Rev9DTOGostBlockungKurslehrer e WHERE e.Blockung_Kurs_ID = :value")
@NamedQuery(name="Rev9DTOGostBlockungKurslehrer.blockung_kurs_id.multiple", query="SELECT e FROM Rev9DTOGostBlockungKurslehrer e WHERE e.Blockung_Kurs_ID IN :value")
@NamedQuery(name="Rev9DTOGostBlockungKurslehrer.lehrer_id", query="SELECT e FROM Rev9DTOGostBlockungKurslehrer e WHERE e.Lehrer_ID = :value")
@NamedQuery(name="Rev9DTOGostBlockungKurslehrer.lehrer_id.multiple", query="SELECT e FROM Rev9DTOGostBlockungKurslehrer e WHERE e.Lehrer_ID IN :value")
@NamedQuery(name="Rev9DTOGostBlockungKurslehrer.reihenfolge", query="SELECT e FROM Rev9DTOGostBlockungKurslehrer e WHERE e.Reihenfolge = :value")
@NamedQuery(name="Rev9DTOGostBlockungKurslehrer.reihenfolge.multiple", query="SELECT e FROM Rev9DTOGostBlockungKurslehrer e WHERE e.Reihenfolge IN :value")
@NamedQuery(name="Rev9DTOGostBlockungKurslehrer.wochenstunden", query="SELECT e FROM Rev9DTOGostBlockungKurslehrer e WHERE e.Wochenstunden = :value")
@NamedQuery(name="Rev9DTOGostBlockungKurslehrer.wochenstunden.multiple", query="SELECT e FROM Rev9DTOGostBlockungKurslehrer e WHERE e.Wochenstunden IN :value")
@NamedQuery(name="Rev9DTOGostBlockungKurslehrer.primaryKeyQuery", query="SELECT e FROM Rev9DTOGostBlockungKurslehrer e WHERE e.Blockung_Kurs_ID = ?1 AND e.Lehrer_ID = ?2")
@NamedQuery(name="Rev9DTOGostBlockungKurslehrer.all.migration", query="SELECT e FROM Rev9DTOGostBlockungKurslehrer e WHERE e.Blockung_Kurs_ID IS NOT NULL AND e.Lehrer_ID IS NOT NULL")
@JsonPropertyOrder({"Blockung_Kurs_ID","Lehrer_ID","Reihenfolge","Wochenstunden"})
public class Rev9DTOGostBlockungKurslehrer {

	/** ID des Kurses */
	@Id
	@Column(name = "Blockung_Kurs_ID")
	@JsonProperty
	public Long Blockung_Kurs_ID;

	/** ID des Lehrers, welcher dem Kurs zugeordnet ist */
	@Id
	@Column(name = "Lehrer_ID")
	@JsonProperty
	public Long Lehrer_ID;

	/** Eine Reihenfolge für die Lehrer, z.B. zur Unterscheidung des eigentlichen Kurslehrer (z.B. 1) und einer Zusatzkraft (z.B. 2) */
	@Column(name = "Reihenfolge")
	@JsonProperty
	public Integer Reihenfolge;

	/** Die Anzahl der Wochenstunden für die der Lehrer in dem Kurs eingesetzt wird */
	@Column(name = "Wochenstunden")
	@JsonProperty
	public Integer Wochenstunden;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev9DTOGostBlockungKurslehrer ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev9DTOGostBlockungKurslehrer() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev9DTOGostBlockungKurslehrer ohne eine Initialisierung der Attribute.
	 * @param Blockung_Kurs_ID   der Wert für das Attribut Blockung_Kurs_ID
	 * @param Lehrer_ID   der Wert für das Attribut Lehrer_ID
	 * @param Reihenfolge   der Wert für das Attribut Reihenfolge
	 * @param Wochenstunden   der Wert für das Attribut Wochenstunden
	 */
	public Rev9DTOGostBlockungKurslehrer(final Long Blockung_Kurs_ID, final Long Lehrer_ID, final Integer Reihenfolge, final Integer Wochenstunden) {
		if (Blockung_Kurs_ID == null) { 
			throw new NullPointerException("Blockung_Kurs_ID must not be null");
		}
		this.Blockung_Kurs_ID = Blockung_Kurs_ID;
		if (Lehrer_ID == null) { 
			throw new NullPointerException("Lehrer_ID must not be null");
		}
		this.Lehrer_ID = Lehrer_ID;
		if (Reihenfolge == null) { 
			throw new NullPointerException("Reihenfolge must not be null");
		}
		this.Reihenfolge = Reihenfolge;
		if (Wochenstunden == null) { 
			throw new NullPointerException("Wochenstunden must not be null");
		}
		this.Wochenstunden = Wochenstunden;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev9DTOGostBlockungKurslehrer other = (Rev9DTOGostBlockungKurslehrer) obj;
		if (Blockung_Kurs_ID == null) {
			if (other.Blockung_Kurs_ID != null)
				return false;
		} else if (!Blockung_Kurs_ID.equals(other.Blockung_Kurs_ID))
			return false;

		if (Lehrer_ID == null) {
			if (other.Lehrer_ID != null)
				return false;
		} else if (!Lehrer_ID.equals(other.Lehrer_ID))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Blockung_Kurs_ID == null) ? 0 : Blockung_Kurs_ID.hashCode());

		result = prime * result + ((Lehrer_ID == null) ? 0 : Lehrer_ID.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "Rev9DTOGostBlockungKurslehrer(Blockung_Kurs_ID=" + this.Blockung_Kurs_ID + ", Lehrer_ID=" + this.Lehrer_ID + ", Reihenfolge=" + this.Reihenfolge + ", Wochenstunden=" + this.Wochenstunden + ")";
	}

}