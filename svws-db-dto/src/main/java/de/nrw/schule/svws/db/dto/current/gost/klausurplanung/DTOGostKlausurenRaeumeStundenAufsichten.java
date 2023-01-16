package de.nrw.schule.svws.db.dto.current.gost.klausurplanung;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Gost_Klausuren_Raeume_Stunden_Aufsichten.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Gost_Klausuren_Raeume_Stunden_Aufsichten")
@NamedQuery(name="DTOGostKlausurenRaeumeStundenAufsichten.all", query="SELECT e FROM DTOGostKlausurenRaeumeStundenAufsichten e")
@NamedQuery(name="DTOGostKlausurenRaeumeStundenAufsichten.id", query="SELECT e FROM DTOGostKlausurenRaeumeStundenAufsichten e WHERE e.ID = :value")
@NamedQuery(name="DTOGostKlausurenRaeumeStundenAufsichten.id.multiple", query="SELECT e FROM DTOGostKlausurenRaeumeStundenAufsichten e WHERE e.ID IN :value")
@NamedQuery(name="DTOGostKlausurenRaeumeStundenAufsichten.klausurraumstunde_id", query="SELECT e FROM DTOGostKlausurenRaeumeStundenAufsichten e WHERE e.KlausurRaumStunde_ID = :value")
@NamedQuery(name="DTOGostKlausurenRaeumeStundenAufsichten.klausurraumstunde_id.multiple", query="SELECT e FROM DTOGostKlausurenRaeumeStundenAufsichten e WHERE e.KlausurRaumStunde_ID IN :value")
@NamedQuery(name="DTOGostKlausurenRaeumeStundenAufsichten.lehrer_id", query="SELECT e FROM DTOGostKlausurenRaeumeStundenAufsichten e WHERE e.Lehrer_ID = :value")
@NamedQuery(name="DTOGostKlausurenRaeumeStundenAufsichten.lehrer_id.multiple", query="SELECT e FROM DTOGostKlausurenRaeumeStundenAufsichten e WHERE e.Lehrer_ID IN :value")
@NamedQuery(name="DTOGostKlausurenRaeumeStundenAufsichten.startzeit", query="SELECT e FROM DTOGostKlausurenRaeumeStundenAufsichten e WHERE e.Startzeit = :value")
@NamedQuery(name="DTOGostKlausurenRaeumeStundenAufsichten.startzeit.multiple", query="SELECT e FROM DTOGostKlausurenRaeumeStundenAufsichten e WHERE e.Startzeit IN :value")
@NamedQuery(name="DTOGostKlausurenRaeumeStundenAufsichten.endzeit", query="SELECT e FROM DTOGostKlausurenRaeumeStundenAufsichten e WHERE e.Endzeit = :value")
@NamedQuery(name="DTOGostKlausurenRaeumeStundenAufsichten.endzeit.multiple", query="SELECT e FROM DTOGostKlausurenRaeumeStundenAufsichten e WHERE e.Endzeit IN :value")
@NamedQuery(name="DTOGostKlausurenRaeumeStundenAufsichten.bemerkungen", query="SELECT e FROM DTOGostKlausurenRaeumeStundenAufsichten e WHERE e.Bemerkungen = :value")
@NamedQuery(name="DTOGostKlausurenRaeumeStundenAufsichten.bemerkungen.multiple", query="SELECT e FROM DTOGostKlausurenRaeumeStundenAufsichten e WHERE e.Bemerkungen IN :value")
@NamedQuery(name="DTOGostKlausurenRaeumeStundenAufsichten.primaryKeyQuery", query="SELECT e FROM DTOGostKlausurenRaeumeStundenAufsichten e WHERE e.ID = ?1")
@NamedQuery(name="DTOGostKlausurenRaeumeStundenAufsichten.all.migration", query="SELECT e FROM DTOGostKlausurenRaeumeStundenAufsichten e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","KlausurRaumStunde_ID","Lehrer_ID","Startzeit","Endzeit","Bemerkungen"})
public class DTOGostKlausurenRaeumeStundenAufsichten {

	/** ID der Klausuraufsicht (generiert) */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** ID der Klausur-Raumstunde */
	@Column(name = "KlausurRaumStunde_ID")
	@JsonProperty
	public Long KlausurRaumStunde_ID;

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

	/** Text für Bemerkungen zur Aufsicht */
	@Column(name = "Bemerkungen")
	@JsonProperty
	public String Bemerkungen;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostKlausurenRaeumeStundenAufsichten ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOGostKlausurenRaeumeStundenAufsichten() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostKlausurenRaeumeStundenAufsichten ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param KlausurRaumStunde_ID   der Wert für das Attribut KlausurRaumStunde_ID
	 */
	public DTOGostKlausurenRaeumeStundenAufsichten(final Long ID, final Long KlausurRaumStunde_ID) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (KlausurRaumStunde_ID == null) { 
			throw new NullPointerException("KlausurRaumStunde_ID must not be null");
		}
		this.KlausurRaumStunde_ID = KlausurRaumStunde_ID;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOGostKlausurenRaeumeStundenAufsichten other = (DTOGostKlausurenRaeumeStundenAufsichten) obj;
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
		return "DTOGostKlausurenRaeumeStundenAufsichten(ID=" + this.ID + ", KlausurRaumStunde_ID=" + this.KlausurRaumStunde_ID + ", Lehrer_ID=" + this.Lehrer_ID + ", Startzeit=" + this.Startzeit + ", Endzeit=" + this.Endzeit + ", Bemerkungen=" + this.Bemerkungen + ")";
	}

}