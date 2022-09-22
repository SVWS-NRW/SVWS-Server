package de.nrw.schule.svws.db.dto.current.schild.schule;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Schulgliederungen_Schulformen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(DTOAlleSchulgliederungenSchulformenPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Schulgliederungen_Schulformen")
@NamedQuery(name="DTOAlleSchulgliederungenSchulformen.all", query="SELECT e FROM DTOAlleSchulgliederungenSchulformen e")
@NamedQuery(name="DTOAlleSchulgliederungenSchulformen.schulgliederung_id", query="SELECT e FROM DTOAlleSchulgliederungenSchulformen e WHERE e.Schulgliederung_ID = :value")
@NamedQuery(name="DTOAlleSchulgliederungenSchulformen.schulgliederung_id.multiple", query="SELECT e FROM DTOAlleSchulgliederungenSchulformen e WHERE e.Schulgliederung_ID IN :value")
@NamedQuery(name="DTOAlleSchulgliederungenSchulformen.schulform_kuerzel", query="SELECT e FROM DTOAlleSchulgliederungenSchulformen e WHERE e.Schulform_Kuerzel = :value")
@NamedQuery(name="DTOAlleSchulgliederungenSchulformen.schulform_kuerzel.multiple", query="SELECT e FROM DTOAlleSchulgliederungenSchulformen e WHERE e.Schulform_Kuerzel IN :value")
@NamedQuery(name="DTOAlleSchulgliederungenSchulformen.primaryKeyQuery", query="SELECT e FROM DTOAlleSchulgliederungenSchulformen e WHERE e.Schulgliederung_ID = ?1 AND e.Schulform_Kuerzel = ?2")
@NamedQuery(name="DTOAlleSchulgliederungenSchulformen.all.migration", query="SELECT e FROM DTOAlleSchulgliederungenSchulformen e WHERE e.Schulgliederung_ID IS NOT NULL AND e.Schulform_Kuerzel IS NOT NULL")
@JsonPropertyOrder({"Schulgliederung_ID","Schulform_Kuerzel"})
public class DTOAlleSchulgliederungenSchulformen {

	/** die ID der Schulgliederung */
	@Id
	@Column(name = "Schulgliederung_ID")
	@JsonProperty
	public Long Schulgliederung_ID;

	/** das Kürzel der Schulform */
	@Id
	@Column(name = "Schulform_Kuerzel")
	@JsonProperty
	public String Schulform_Kuerzel;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOAlleSchulgliederungenSchulformen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOAlleSchulgliederungenSchulformen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOAlleSchulgliederungenSchulformen ohne eine Initialisierung der Attribute.
	 * @param Schulgliederung_ID   der Wert für das Attribut Schulgliederung_ID
	 * @param Schulform_Kuerzel   der Wert für das Attribut Schulform_Kuerzel
	 */
	public DTOAlleSchulgliederungenSchulformen(final Long Schulgliederung_ID, final String Schulform_Kuerzel) {
		if (Schulgliederung_ID == null) { 
			throw new NullPointerException("Schulgliederung_ID must not be null");
		}
		this.Schulgliederung_ID = Schulgliederung_ID;
		if (Schulform_Kuerzel == null) { 
			throw new NullPointerException("Schulform_Kuerzel must not be null");
		}
		this.Schulform_Kuerzel = Schulform_Kuerzel;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOAlleSchulgliederungenSchulformen other = (DTOAlleSchulgliederungenSchulformen) obj;
		if (Schulgliederung_ID == null) {
			if (other.Schulgliederung_ID != null)
				return false;
		} else if (!Schulgliederung_ID.equals(other.Schulgliederung_ID))
			return false;

		if (Schulform_Kuerzel == null) {
			if (other.Schulform_Kuerzel != null)
				return false;
		} else if (!Schulform_Kuerzel.equals(other.Schulform_Kuerzel))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Schulgliederung_ID == null) ? 0 : Schulgliederung_ID.hashCode());

		result = prime * result + ((Schulform_Kuerzel == null) ? 0 : Schulform_Kuerzel.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOAlleSchulgliederungenSchulformen(Schulgliederung_ID=" + this.Schulgliederung_ID + ", Schulform_Kuerzel=" + this.Schulform_Kuerzel + ")";
	}

}