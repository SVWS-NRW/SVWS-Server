package de.nrw.schule.svws.db.dto.rev9.schild.berufskolleg;

import de.nrw.schule.svws.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle EigeneSchule_Zertifikate.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "EigeneSchule_Zertifikate")
@NamedQuery(name="Rev9DTOZertifikate.all", query="SELECT e FROM Rev9DTOZertifikate e")
@NamedQuery(name="Rev9DTOZertifikate.id", query="SELECT e FROM Rev9DTOZertifikate e WHERE e.ID = :value")
@NamedQuery(name="Rev9DTOZertifikate.id.multiple", query="SELECT e FROM Rev9DTOZertifikate e WHERE e.ID IN :value")
@NamedQuery(name="Rev9DTOZertifikate.kuerzel", query="SELECT e FROM Rev9DTOZertifikate e WHERE e.Kuerzel = :value")
@NamedQuery(name="Rev9DTOZertifikate.kuerzel.multiple", query="SELECT e FROM Rev9DTOZertifikate e WHERE e.Kuerzel IN :value")
@NamedQuery(name="Rev9DTOZertifikate.bezeichnung", query="SELECT e FROM Rev9DTOZertifikate e WHERE e.Bezeichnung = :value")
@NamedQuery(name="Rev9DTOZertifikate.bezeichnung.multiple", query="SELECT e FROM Rev9DTOZertifikate e WHERE e.Bezeichnung IN :value")
@NamedQuery(name="Rev9DTOZertifikate.fach", query="SELECT e FROM Rev9DTOZertifikate e WHERE e.Fach = :value")
@NamedQuery(name="Rev9DTOZertifikate.fach.multiple", query="SELECT e FROM Rev9DTOZertifikate e WHERE e.Fach IN :value")
@NamedQuery(name="Rev9DTOZertifikate.formatvorlage", query="SELECT e FROM Rev9DTOZertifikate e WHERE e.Formatvorlage = :value")
@NamedQuery(name="Rev9DTOZertifikate.formatvorlage.multiple", query="SELECT e FROM Rev9DTOZertifikate e WHERE e.Formatvorlage IN :value")
@NamedQuery(name="Rev9DTOZertifikate.primaryKeyQuery", query="SELECT e FROM Rev9DTOZertifikate e WHERE e.ID = ?1")
@NamedQuery(name="Rev9DTOZertifikate.all.migration", query="SELECT e FROM Rev9DTOZertifikate e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Kuerzel","Bezeichnung","Fach","Formatvorlage"})
public class Rev9DTOZertifikate {

	/** ID des Zertifikats */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Kürzel des Zertifikats */
	@Column(name = "Kuerzel")
	@JsonProperty
	public String Kuerzel;

	/** Bezeichnung des Zertifikats */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Fachbezeichnung für das Zertifikat */
	@Column(name = "Fach")
	@JsonProperty
	public String Fach;

	/** Formatforlage für das Zertifikat */
	@Column(name = "Formatvorlage")
	@JsonProperty
	public String Formatvorlage;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev9DTOZertifikate ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev9DTOZertifikate() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev9DTOZertifikate ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Kuerzel   der Wert für das Attribut Kuerzel
	 */
	public Rev9DTOZertifikate(final Long ID, final String Kuerzel) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Kuerzel == null) { 
			throw new NullPointerException("Kuerzel must not be null");
		}
		this.Kuerzel = Kuerzel;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev9DTOZertifikate other = (Rev9DTOZertifikate) obj;
		if (ID == null) {
			if (other.ID != null)
				return false;
		} else if (!ID.equals(other.ID))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ID == null) ? 0 : ID.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "Rev9DTOZertifikate(ID=" + this.ID + ", Kuerzel=" + this.Kuerzel + ", Bezeichnung=" + this.Bezeichnung + ", Fach=" + this.Fach + ", Formatvorlage=" + this.Formatvorlage + ")";
	}

}