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
 * Diese Klasse dient als DTO für die Datenbanktabelle Gost_Klausuren_Aufsichten.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Gost_Klausuren_Aufsichten")
@NamedQuery(name="Rev9DTOGostKlausurenAufsichten.all", query="SELECT e FROM Rev9DTOGostKlausurenAufsichten e")
@NamedQuery(name="Rev9DTOGostKlausurenAufsichten.id", query="SELECT e FROM Rev9DTOGostKlausurenAufsichten e WHERE e.ID = :value")
@NamedQuery(name="Rev9DTOGostKlausurenAufsichten.id.multiple", query="SELECT e FROM Rev9DTOGostKlausurenAufsichten e WHERE e.ID IN :value")
@NamedQuery(name="Rev9DTOGostKlausurenAufsichten.raum_id", query="SELECT e FROM Rev9DTOGostKlausurenAufsichten e WHERE e.Raum_ID = :value")
@NamedQuery(name="Rev9DTOGostKlausurenAufsichten.raum_id.multiple", query="SELECT e FROM Rev9DTOGostKlausurenAufsichten e WHERE e.Raum_ID IN :value")
@NamedQuery(name="Rev9DTOGostKlausurenAufsichten.lehrer_id", query="SELECT e FROM Rev9DTOGostKlausurenAufsichten e WHERE e.Lehrer_ID = :value")
@NamedQuery(name="Rev9DTOGostKlausurenAufsichten.lehrer_id.multiple", query="SELECT e FROM Rev9DTOGostKlausurenAufsichten e WHERE e.Lehrer_ID IN :value")
@NamedQuery(name="Rev9DTOGostKlausurenAufsichten.startzeit", query="SELECT e FROM Rev9DTOGostKlausurenAufsichten e WHERE e.Startzeit = :value")
@NamedQuery(name="Rev9DTOGostKlausurenAufsichten.startzeit.multiple", query="SELECT e FROM Rev9DTOGostKlausurenAufsichten e WHERE e.Startzeit IN :value")
@NamedQuery(name="Rev9DTOGostKlausurenAufsichten.endzeit", query="SELECT e FROM Rev9DTOGostKlausurenAufsichten e WHERE e.Endzeit = :value")
@NamedQuery(name="Rev9DTOGostKlausurenAufsichten.endzeit.multiple", query="SELECT e FROM Rev9DTOGostKlausurenAufsichten e WHERE e.Endzeit IN :value")
@NamedQuery(name="Rev9DTOGostKlausurenAufsichten.bemerkungen", query="SELECT e FROM Rev9DTOGostKlausurenAufsichten e WHERE e.Bemerkungen = :value")
@NamedQuery(name="Rev9DTOGostKlausurenAufsichten.bemerkungen.multiple", query="SELECT e FROM Rev9DTOGostKlausurenAufsichten e WHERE e.Bemerkungen IN :value")
@NamedQuery(name="Rev9DTOGostKlausurenAufsichten.primaryKeyQuery", query="SELECT e FROM Rev9DTOGostKlausurenAufsichten e WHERE e.ID = ?1")
@NamedQuery(name="Rev9DTOGostKlausurenAufsichten.all.migration", query="SELECT e FROM Rev9DTOGostKlausurenAufsichten e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Raum_ID","Lehrer_ID","Startzeit","Endzeit","Bemerkungen"})
public class Rev9DTOGostKlausurenAufsichten {

	/** ID der Klausuraufsicht (generiert) */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** ID des Klausurraums */
	@Column(name = "Raum_ID")
	@JsonProperty
	public Long Raum_ID;

	/** ID des Lehrers */
	@Column(name = "Lehrer_ID")
	@JsonProperty
	public Long Lehrer_ID;

	/** Die Startzeit der Aufsicht */
	@Column(name = "Startzeit")
	@JsonProperty
	public String Startzeit;

	/** Die Endzeit der Aufsicht */
	@Column(name = "Endzeit")
	@JsonProperty
	public String Endzeit;

	/** Text für Bemerkungen zur Klausurvorlage */
	@Column(name = "Bemerkungen")
	@JsonProperty
	public String Bemerkungen;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev9DTOGostKlausurenAufsichten ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev9DTOGostKlausurenAufsichten() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev9DTOGostKlausurenAufsichten ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Raum_ID   der Wert für das Attribut Raum_ID
	 */
	public Rev9DTOGostKlausurenAufsichten(final Long ID, final Long Raum_ID) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Raum_ID == null) { 
			throw new NullPointerException("Raum_ID must not be null");
		}
		this.Raum_ID = Raum_ID;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev9DTOGostKlausurenAufsichten other = (Rev9DTOGostKlausurenAufsichten) obj;
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
		return "Rev9DTOGostKlausurenAufsichten(ID=" + this.ID + ", Raum_ID=" + this.Raum_ID + ", Lehrer_ID=" + this.Lehrer_ID + ", Startzeit=" + this.Startzeit + ", Endzeit=" + this.Endzeit + ", Bemerkungen=" + this.Bemerkungen + ")";
	}

}