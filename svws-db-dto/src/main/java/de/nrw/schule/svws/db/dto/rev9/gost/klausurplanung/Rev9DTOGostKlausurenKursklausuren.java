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
 * Diese Klasse dient als DTO für die Datenbanktabelle Gost_Klausuren_Kursklausuren.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Gost_Klausuren_Kursklausuren")
@NamedQuery(name="Rev9DTOGostKlausurenKursklausuren.all", query="SELECT e FROM Rev9DTOGostKlausurenKursklausuren e")
@NamedQuery(name="Rev9DTOGostKlausurenKursklausuren.id", query="SELECT e FROM Rev9DTOGostKlausurenKursklausuren e WHERE e.ID = :value")
@NamedQuery(name="Rev9DTOGostKlausurenKursklausuren.id.multiple", query="SELECT e FROM Rev9DTOGostKlausurenKursklausuren e WHERE e.ID IN :value")
@NamedQuery(name="Rev9DTOGostKlausurenKursklausuren.vorgabe_id", query="SELECT e FROM Rev9DTOGostKlausurenKursklausuren e WHERE e.Vorgabe_ID = :value")
@NamedQuery(name="Rev9DTOGostKlausurenKursklausuren.vorgabe_id.multiple", query="SELECT e FROM Rev9DTOGostKlausurenKursklausuren e WHERE e.Vorgabe_ID IN :value")
@NamedQuery(name="Rev9DTOGostKlausurenKursklausuren.kurs_id", query="SELECT e FROM Rev9DTOGostKlausurenKursklausuren e WHERE e.Kurs_ID = :value")
@NamedQuery(name="Rev9DTOGostKlausurenKursklausuren.kurs_id.multiple", query="SELECT e FROM Rev9DTOGostKlausurenKursklausuren e WHERE e.Kurs_ID IN :value")
@NamedQuery(name="Rev9DTOGostKlausurenKursklausuren.termin_id", query="SELECT e FROM Rev9DTOGostKlausurenKursklausuren e WHERE e.Termin_ID = :value")
@NamedQuery(name="Rev9DTOGostKlausurenKursklausuren.termin_id.multiple", query="SELECT e FROM Rev9DTOGostKlausurenKursklausuren e WHERE e.Termin_ID IN :value")
@NamedQuery(name="Rev9DTOGostKlausurenKursklausuren.startzeit", query="SELECT e FROM Rev9DTOGostKlausurenKursklausuren e WHERE e.Startzeit = :value")
@NamedQuery(name="Rev9DTOGostKlausurenKursklausuren.startzeit.multiple", query="SELECT e FROM Rev9DTOGostKlausurenKursklausuren e WHERE e.Startzeit IN :value")
@NamedQuery(name="Rev9DTOGostKlausurenKursklausuren.primaryKeyQuery", query="SELECT e FROM Rev9DTOGostKlausurenKursklausuren e WHERE e.ID = ?1")
@NamedQuery(name="Rev9DTOGostKlausurenKursklausuren.all.migration", query="SELECT e FROM Rev9DTOGostKlausurenKursklausuren e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Vorgabe_ID","Kurs_ID","Termin_ID","Startzeit"})
public class Rev9DTOGostKlausurenKursklausuren {

	/** ID der Kursklausur (generiert) */
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

	/** Startzeit der Klausur, wenn abweichend von Startzeit der Klausur-Schiene */
	@Column(name = "Startzeit")
	@JsonProperty
	public String Startzeit;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev9DTOGostKlausurenKursklausuren ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev9DTOGostKlausurenKursklausuren() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev9DTOGostKlausurenKursklausuren ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Vorgabe_ID   der Wert für das Attribut Vorgabe_ID
	 * @param Kurs_ID   der Wert für das Attribut Kurs_ID
	 */
	public Rev9DTOGostKlausurenKursklausuren(final Long ID, final Long Vorgabe_ID, final Long Kurs_ID) {
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
		Rev9DTOGostKlausurenKursklausuren other = (Rev9DTOGostKlausurenKursklausuren) obj;
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
		return "Rev9DTOGostKlausurenKursklausuren(ID=" + this.ID + ", Vorgabe_ID=" + this.Vorgabe_ID + ", Kurs_ID=" + this.Kurs_ID + ", Termin_ID=" + this.Termin_ID + ", Startzeit=" + this.Startzeit + ")";
	}

}