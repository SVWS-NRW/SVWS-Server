package de.nrw.schule.svws.db.dto.rev9.gost.klausurplanung;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Gost_Klausuren_Klausuren.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Gost_Klausuren_Klausuren")
@NamedQuery(name="Rev9DTOGostKlausurenKlausuren.all", query="SELECT e FROM Rev9DTOGostKlausurenKlausuren e")
@NamedQuery(name="Rev9DTOGostKlausurenKlausuren.id", query="SELECT e FROM Rev9DTOGostKlausurenKlausuren e WHERE e.ID = :value")
@NamedQuery(name="Rev9DTOGostKlausurenKlausuren.id.multiple", query="SELECT e FROM Rev9DTOGostKlausurenKlausuren e WHERE e.ID IN :value")
@NamedQuery(name="Rev9DTOGostKlausurenKlausuren.vorgabe_id", query="SELECT e FROM Rev9DTOGostKlausurenKlausuren e WHERE e.Vorgabe_ID = :value")
@NamedQuery(name="Rev9DTOGostKlausurenKlausuren.vorgabe_id.multiple", query="SELECT e FROM Rev9DTOGostKlausurenKlausuren e WHERE e.Vorgabe_ID IN :value")
@NamedQuery(name="Rev9DTOGostKlausurenKlausuren.kurs_id", query="SELECT e FROM Rev9DTOGostKlausurenKlausuren e WHERE e.Kurs_ID = :value")
@NamedQuery(name="Rev9DTOGostKlausurenKlausuren.kurs_id.multiple", query="SELECT e FROM Rev9DTOGostKlausurenKlausuren e WHERE e.Kurs_ID IN :value")
@NamedQuery(name="Rev9DTOGostKlausurenKlausuren.termin_id", query="SELECT e FROM Rev9DTOGostKlausurenKlausuren e WHERE e.Termin_ID = :value")
@NamedQuery(name="Rev9DTOGostKlausurenKlausuren.termin_id.multiple", query="SELECT e FROM Rev9DTOGostKlausurenKlausuren e WHERE e.Termin_ID IN :value")
@NamedQuery(name="Rev9DTOGostKlausurenKlausuren.raum_id", query="SELECT e FROM Rev9DTOGostKlausurenKlausuren e WHERE e.Raum_ID = :value")
@NamedQuery(name="Rev9DTOGostKlausurenKlausuren.raum_id.multiple", query="SELECT e FROM Rev9DTOGostKlausurenKlausuren e WHERE e.Raum_ID IN :value")
@NamedQuery(name="Rev9DTOGostKlausurenKlausuren.startzeit", query="SELECT e FROM Rev9DTOGostKlausurenKlausuren e WHERE e.Startzeit = :value")
@NamedQuery(name="Rev9DTOGostKlausurenKlausuren.startzeit.multiple", query="SELECT e FROM Rev9DTOGostKlausurenKlausuren e WHERE e.Startzeit IN :value")
@NamedQuery(name="Rev9DTOGostKlausurenKlausuren.primaryKeyQuery", query="SELECT e FROM Rev9DTOGostKlausurenKlausuren e WHERE e.ID = ?1")
@NamedQuery(name="Rev9DTOGostKlausurenKlausuren.all.migration", query="SELECT e FROM Rev9DTOGostKlausurenKlausuren e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Vorgabe_ID","Kurs_ID","Termin_ID","Raum_ID","Startzeit"})
public class Rev9DTOGostKlausurenKlausuren {

	/** ID der Klausurvorgaben (generiert) */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** ID der Klausurvorgaben */
	@Column(name = "Vorgabe_ID")
	@JsonProperty
	public Long Vorgabe_ID;

	/** Kurs_ID der Klausur */
	@Column(name = "Kurs_ID")
	@JsonProperty
	public Long Kurs_ID;

	/** ID des Klausurtermins */
	@Column(name = "Termin_ID")
	@JsonProperty
	public Long Termin_ID;

	/** ID des Klausurraums */
	@Column(name = "Raum_ID")
	@JsonProperty
	public Long Raum_ID;

	/** Startzeit der Klausur, wenn abweichend von Startzeit der Klausur-Schiene */
	@Column(name = "Startzeit")
	@JsonProperty
	public String Startzeit;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev9DTOGostKlausurenKlausuren ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev9DTOGostKlausurenKlausuren() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev9DTOGostKlausurenKlausuren ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Vorgabe_ID   der Wert für das Attribut Vorgabe_ID
	 * @param Kurs_ID   der Wert für das Attribut Kurs_ID
	 */
	public Rev9DTOGostKlausurenKlausuren(final Long ID, final Long Vorgabe_ID, final Long Kurs_ID) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Vorgabe_ID == null) { 
			throw new NullPointerException("Vorgabe_ID must not be null");
		}
		this.Vorgabe_ID = Vorgabe_ID;
		if (Kurs_ID == null) { 
			throw new NullPointerException("Kurs_ID must not be null");
		}
		this.Kurs_ID = Kurs_ID;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev9DTOGostKlausurenKlausuren other = (Rev9DTOGostKlausurenKlausuren) obj;
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
		return "Rev9DTOGostKlausurenKlausuren(ID=" + this.ID + ", Vorgabe_ID=" + this.Vorgabe_ID + ", Kurs_ID=" + this.Kurs_ID + ", Termin_ID=" + this.Termin_ID + ", Raum_ID=" + this.Raum_ID + ", Startzeit=" + this.Startzeit + ")";
	}

}