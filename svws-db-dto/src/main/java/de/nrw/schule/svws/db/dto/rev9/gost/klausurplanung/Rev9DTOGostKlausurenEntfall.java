package de.nrw.schule.svws.db.dto.rev9.gost.klausurplanung;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Gost_Klausuren_Entfall.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(Rev9DTOGostKlausurenEntfallPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Gost_Klausuren_Entfall")
@NamedQuery(name="Rev9DTOGostKlausurenEntfall.all", query="SELECT e FROM Rev9DTOGostKlausurenEntfall e")
@NamedQuery(name="Rev9DTOGostKlausurenEntfall.termin_id", query="SELECT e FROM Rev9DTOGostKlausurenEntfall e WHERE e.Termin_ID = :value")
@NamedQuery(name="Rev9DTOGostKlausurenEntfall.termin_id.multiple", query="SELECT e FROM Rev9DTOGostKlausurenEntfall e WHERE e.Termin_ID IN :value")
@NamedQuery(name="Rev9DTOGostKlausurenEntfall.kurs_id", query="SELECT e FROM Rev9DTOGostKlausurenEntfall e WHERE e.Kurs_ID = :value")
@NamedQuery(name="Rev9DTOGostKlausurenEntfall.kurs_id.multiple", query="SELECT e FROM Rev9DTOGostKlausurenEntfall e WHERE e.Kurs_ID IN :value")
@NamedQuery(name="Rev9DTOGostKlausurenEntfall.zeitraster_id", query="SELECT e FROM Rev9DTOGostKlausurenEntfall e WHERE e.Zeitraster_ID = :value")
@NamedQuery(name="Rev9DTOGostKlausurenEntfall.zeitraster_id.multiple", query="SELECT e FROM Rev9DTOGostKlausurenEntfall e WHERE e.Zeitraster_ID IN :value")
@NamedQuery(name="Rev9DTOGostKlausurenEntfall.primaryKeyQuery", query="SELECT e FROM Rev9DTOGostKlausurenEntfall e WHERE e.Termin_ID = ?1 AND e.Kurs_ID = ?2 AND e.Zeitraster_ID = ?3")
@NamedQuery(name="Rev9DTOGostKlausurenEntfall.all.migration", query="SELECT e FROM Rev9DTOGostKlausurenEntfall e WHERE e.Termin_ID IS NOT NULL AND e.Kurs_ID IS NOT NULL AND e.Zeitraster_ID IS NOT NULL")
@JsonPropertyOrder({"Termin_ID","Kurs_ID","Zeitraster_ID"})
public class Rev9DTOGostKlausurenEntfall {

	/** ID des Klausurtermins */
	@Id
	@Column(name = "Termin_ID")
	@JsonProperty
	public Long Termin_ID;

	/** ID des Kurses */
	@Id
	@Column(name = "Kurs_ID")
	@JsonProperty
	public Long Kurs_ID;

	/** ID des Zeitrasters */
	@Id
	@Column(name = "Zeitraster_ID")
	@JsonProperty
	public Long Zeitraster_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev9DTOGostKlausurenEntfall ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev9DTOGostKlausurenEntfall() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev9DTOGostKlausurenEntfall ohne eine Initialisierung der Attribute.
	 * @param Termin_ID   der Wert für das Attribut Termin_ID
	 * @param Kurs_ID   der Wert für das Attribut Kurs_ID
	 * @param Zeitraster_ID   der Wert für das Attribut Zeitraster_ID
	 */
	public Rev9DTOGostKlausurenEntfall(final Long Termin_ID, final Long Kurs_ID, final Long Zeitraster_ID) {
		if (Termin_ID == null) { 
			throw new NullPointerException("Termin_ID must not be null");
		}
		this.Termin_ID = Termin_ID;
		if (Kurs_ID == null) { 
			throw new NullPointerException("Kurs_ID must not be null");
		}
		this.Kurs_ID = Kurs_ID;
		if (Zeitraster_ID == null) { 
			throw new NullPointerException("Zeitraster_ID must not be null");
		}
		this.Zeitraster_ID = Zeitraster_ID;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev9DTOGostKlausurenEntfall other = (Rev9DTOGostKlausurenEntfall) obj;
		if (Termin_ID == null) {
			if (other.Termin_ID != null)
				return false;
		} else if (!Termin_ID.equals(other.Termin_ID))
			return false;

		if (Kurs_ID == null) {
			if (other.Kurs_ID != null)
				return false;
		} else if (!Kurs_ID.equals(other.Kurs_ID))
			return false;

		if (Zeitraster_ID == null) {
			if (other.Zeitraster_ID != null)
				return false;
		} else if (!Zeitraster_ID.equals(other.Zeitraster_ID))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Termin_ID == null) ? 0 : Termin_ID.hashCode());

		result = prime * result + ((Kurs_ID == null) ? 0 : Kurs_ID.hashCode());

		result = prime * result + ((Zeitraster_ID == null) ? 0 : Zeitraster_ID.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "Rev9DTOGostKlausurenEntfall(Termin_ID=" + this.Termin_ID + ", Kurs_ID=" + this.Kurs_ID + ", Zeitraster_ID=" + this.Zeitraster_ID + ")";
	}

}