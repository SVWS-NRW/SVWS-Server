package de.nrw.schule.svws.db.dto.dev.schild.stundenplan;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Stundenplan_Aufsichtsbereiche.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Stundenplan_Aufsichtsbereiche")
@NamedQuery(name="DevDTOStundenplanAufsichtsbereich.all", query="SELECT e FROM DevDTOStundenplanAufsichtsbereich e")
@NamedQuery(name="DevDTOStundenplanAufsichtsbereich.id", query="SELECT e FROM DevDTOStundenplanAufsichtsbereich e WHERE e.ID = :value")
@NamedQuery(name="DevDTOStundenplanAufsichtsbereich.id.multiple", query="SELECT e FROM DevDTOStundenplanAufsichtsbereich e WHERE e.ID IN :value")
@NamedQuery(name="DevDTOStundenplanAufsichtsbereich.stundenplan_id", query="SELECT e FROM DevDTOStundenplanAufsichtsbereich e WHERE e.Stundenplan_ID = :value")
@NamedQuery(name="DevDTOStundenplanAufsichtsbereich.stundenplan_id.multiple", query="SELECT e FROM DevDTOStundenplanAufsichtsbereich e WHERE e.Stundenplan_ID IN :value")
@NamedQuery(name="DevDTOStundenplanAufsichtsbereich.kuerzel", query="SELECT e FROM DevDTOStundenplanAufsichtsbereich e WHERE e.Kuerzel = :value")
@NamedQuery(name="DevDTOStundenplanAufsichtsbereich.kuerzel.multiple", query="SELECT e FROM DevDTOStundenplanAufsichtsbereich e WHERE e.Kuerzel IN :value")
@NamedQuery(name="DevDTOStundenplanAufsichtsbereich.beschreibung", query="SELECT e FROM DevDTOStundenplanAufsichtsbereich e WHERE e.Beschreibung = :value")
@NamedQuery(name="DevDTOStundenplanAufsichtsbereich.beschreibung.multiple", query="SELECT e FROM DevDTOStundenplanAufsichtsbereich e WHERE e.Beschreibung IN :value")
@NamedQuery(name="DevDTOStundenplanAufsichtsbereich.primaryKeyQuery", query="SELECT e FROM DevDTOStundenplanAufsichtsbereich e WHERE e.ID = ?1")
@NamedQuery(name="DevDTOStundenplanAufsichtsbereich.all.migration", query="SELECT e FROM DevDTOStundenplanAufsichtsbereich e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Stundenplan_ID","Kuerzel","Beschreibung"})
public class DevDTOStundenplanAufsichtsbereich {

	/** Die ID identifiziert einen Aufsichtsbereicheintrag für einen Stundenplan eindeutig - hat keinen Bezug zur ID der Katalog-Tabelle */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Die ID des Stundenplans, dem dieser Aufsichtsbereicheintrag zugeordnet wird */
	@Column(name = "Stundenplan_ID")
	@JsonProperty
	public Long Stundenplan_ID;

	/** Die Kurzbezeichnung des Aufsichtsbereichs */
	@Column(name = "Kuerzel")
	@JsonProperty
	public String Kuerzel;

	/** Gegebenenfalls eine ausführlichere Beschreibung des Aufsichtsbereichs */
	@Column(name = "Beschreibung")
	@JsonProperty
	public String Beschreibung;

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOStundenplanAufsichtsbereich ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DevDTOStundenplanAufsichtsbereich() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOStundenplanAufsichtsbereich ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Stundenplan_ID   der Wert für das Attribut Stundenplan_ID
	 * @param Kuerzel   der Wert für das Attribut Kuerzel
	 * @param Beschreibung   der Wert für das Attribut Beschreibung
	 */
	public DevDTOStundenplanAufsichtsbereich(final Long ID, final Long Stundenplan_ID, final String Kuerzel, final String Beschreibung) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Stundenplan_ID == null) { 
			throw new NullPointerException("Stundenplan_ID must not be null");
		}
		this.Stundenplan_ID = Stundenplan_ID;
		if (Kuerzel == null) { 
			throw new NullPointerException("Kuerzel must not be null");
		}
		this.Kuerzel = Kuerzel;
		if (Beschreibung == null) { 
			throw new NullPointerException("Beschreibung must not be null");
		}
		this.Beschreibung = Beschreibung;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DevDTOStundenplanAufsichtsbereich other = (DevDTOStundenplanAufsichtsbereich) obj;
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
		return "DevDTOStundenplanAufsichtsbereich(ID=" + this.ID + ", Stundenplan_ID=" + this.Stundenplan_ID + ", Kuerzel=" + this.Kuerzel + ", Beschreibung=" + this.Beschreibung + ")";
	}

}