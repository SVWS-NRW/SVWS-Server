package de.nrw.schule.svws.db.dto.rev8.schild.stundenplan;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Stundenplan_Unterricht.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Stundenplan_Unterricht")
@NamedQuery(name="Rev8DTOStundenplanUnterricht.all", query="SELECT e FROM Rev8DTOStundenplanUnterricht e")
@NamedQuery(name="Rev8DTOStundenplanUnterricht.id", query="SELECT e FROM Rev8DTOStundenplanUnterricht e WHERE e.ID = :value")
@NamedQuery(name="Rev8DTOStundenplanUnterricht.id.multiple", query="SELECT e FROM Rev8DTOStundenplanUnterricht e WHERE e.ID IN :value")
@NamedQuery(name="Rev8DTOStundenplanUnterricht.zeitraster_id", query="SELECT e FROM Rev8DTOStundenplanUnterricht e WHERE e.Zeitraster_ID = :value")
@NamedQuery(name="Rev8DTOStundenplanUnterricht.zeitraster_id.multiple", query="SELECT e FROM Rev8DTOStundenplanUnterricht e WHERE e.Zeitraster_ID IN :value")
@NamedQuery(name="Rev8DTOStundenplanUnterricht.wochentyp", query="SELECT e FROM Rev8DTOStundenplanUnterricht e WHERE e.Wochentyp = :value")
@NamedQuery(name="Rev8DTOStundenplanUnterricht.wochentyp.multiple", query="SELECT e FROM Rev8DTOStundenplanUnterricht e WHERE e.Wochentyp IN :value")
@NamedQuery(name="Rev8DTOStundenplanUnterricht.klasse_id", query="SELECT e FROM Rev8DTOStundenplanUnterricht e WHERE e.Klasse_ID = :value")
@NamedQuery(name="Rev8DTOStundenplanUnterricht.klasse_id.multiple", query="SELECT e FROM Rev8DTOStundenplanUnterricht e WHERE e.Klasse_ID IN :value")
@NamedQuery(name="Rev8DTOStundenplanUnterricht.kurs_id", query="SELECT e FROM Rev8DTOStundenplanUnterricht e WHERE e.Kurs_ID = :value")
@NamedQuery(name="Rev8DTOStundenplanUnterricht.kurs_id.multiple", query="SELECT e FROM Rev8DTOStundenplanUnterricht e WHERE e.Kurs_ID IN :value")
@NamedQuery(name="Rev8DTOStundenplanUnterricht.fach_id", query="SELECT e FROM Rev8DTOStundenplanUnterricht e WHERE e.Fach_ID = :value")
@NamedQuery(name="Rev8DTOStundenplanUnterricht.fach_id.multiple", query="SELECT e FROM Rev8DTOStundenplanUnterricht e WHERE e.Fach_ID IN :value")
@NamedQuery(name="Rev8DTOStundenplanUnterricht.primaryKeyQuery", query="SELECT e FROM Rev8DTOStundenplanUnterricht e WHERE e.ID = ?1")
@NamedQuery(name="Rev8DTOStundenplanUnterricht.all.migration", query="SELECT e FROM Rev8DTOStundenplanUnterricht e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Zeitraster_ID","Wochentyp","Klasse_ID","Kurs_ID","Fach_ID"})
public class Rev8DTOStundenplanUnterricht {

	/** Die eindeutige ID für diese Zuordnung des Untericht-Eintrages zu einem Stundenplan */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Die ID des belegten Zeitraster-Eintrags */
	@Column(name = "Zeitraster_ID")
	@JsonProperty
	public Long Zeitraster_ID;

	/** Gibt an, ob es sich um einen Eintrag für jede Woche handelt (0) oder ob es sich um einen unterschiedlichen (!) Eintrag für eine A- bzw. B-Wochen (1 bzw. 2) handelt */
	@Column(name = "Wochentyp")
	@JsonProperty
	public Integer Wochentyp;

	/** Die Klasse, in welcher der Unterricht stattfindet. Bei Kursen entfällt dieser Eintrag und es ist eine ID des Kurses gesetzt */
	@Column(name = "Klasse_ID")
	@JsonProperty
	public Long Klasse_ID;

	/** Die ID des Kurses, falls der Unterricht nicht im Klassenverband stattfindet */
	@Column(name = "Kurs_ID")
	@JsonProperty
	public Long Kurs_ID;

	/** Die ID des Faches, in dem der Unterricht stattfindet */
	@Column(name = "Fach_ID")
	@JsonProperty
	public Long Fach_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOStundenplanUnterricht ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev8DTOStundenplanUnterricht() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOStundenplanUnterricht ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Zeitraster_ID   der Wert für das Attribut Zeitraster_ID
	 * @param Wochentyp   der Wert für das Attribut Wochentyp
	 * @param Fach_ID   der Wert für das Attribut Fach_ID
	 */
	public Rev8DTOStundenplanUnterricht(final Long ID, final Long Zeitraster_ID, final Integer Wochentyp, final Long Fach_ID) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Zeitraster_ID == null) { 
			throw new NullPointerException("Zeitraster_ID must not be null");
		}
		this.Zeitraster_ID = Zeitraster_ID;
		if (Wochentyp == null) { 
			throw new NullPointerException("Wochentyp must not be null");
		}
		this.Wochentyp = Wochentyp;
		if (Fach_ID == null) { 
			throw new NullPointerException("Fach_ID must not be null");
		}
		this.Fach_ID = Fach_ID;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev8DTOStundenplanUnterricht other = (Rev8DTOStundenplanUnterricht) obj;
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
		return "Rev8DTOStundenplanUnterricht(ID=" + this.ID + ", Zeitraster_ID=" + this.Zeitraster_ID + ", Wochentyp=" + this.Wochentyp + ", Klasse_ID=" + this.Klasse_ID + ", Kurs_ID=" + this.Kurs_ID + ", Fach_ID=" + this.Fach_ID + ")";
	}

}