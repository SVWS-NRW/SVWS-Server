package de.svws_nrw.db.dto.current.schild.stundenplan;

import de.svws_nrw.db.DBEntityManager;

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
@NamedQuery(name = "DTOStundenplanAufsichtsbereich.all", query = "SELECT e FROM DTOStundenplanAufsichtsbereich e")
@NamedQuery(name = "DTOStundenplanAufsichtsbereich.id", query = "SELECT e FROM DTOStundenplanAufsichtsbereich e WHERE e.ID = :value")
@NamedQuery(name = "DTOStundenplanAufsichtsbereich.id.multiple", query = "SELECT e FROM DTOStundenplanAufsichtsbereich e WHERE e.ID IN :value")
@NamedQuery(name = "DTOStundenplanAufsichtsbereich.stundenplan_id", query = "SELECT e FROM DTOStundenplanAufsichtsbereich e WHERE e.Stundenplan_ID = :value")
@NamedQuery(name = "DTOStundenplanAufsichtsbereich.stundenplan_id.multiple", query = "SELECT e FROM DTOStundenplanAufsichtsbereich e WHERE e.Stundenplan_ID IN :value")
@NamedQuery(name = "DTOStundenplanAufsichtsbereich.kuerzel", query = "SELECT e FROM DTOStundenplanAufsichtsbereich e WHERE e.Kuerzel = :value")
@NamedQuery(name = "DTOStundenplanAufsichtsbereich.kuerzel.multiple", query = "SELECT e FROM DTOStundenplanAufsichtsbereich e WHERE e.Kuerzel IN :value")
@NamedQuery(name = "DTOStundenplanAufsichtsbereich.beschreibung", query = "SELECT e FROM DTOStundenplanAufsichtsbereich e WHERE e.Beschreibung = :value")
@NamedQuery(name = "DTOStundenplanAufsichtsbereich.beschreibung.multiple", query = "SELECT e FROM DTOStundenplanAufsichtsbereich e WHERE e.Beschreibung IN :value")
@NamedQuery(name = "DTOStundenplanAufsichtsbereich.primaryKeyQuery", query = "SELECT e FROM DTOStundenplanAufsichtsbereich e WHERE e.ID = ?1")
@NamedQuery(name = "DTOStundenplanAufsichtsbereich.all.migration", query = "SELECT e FROM DTOStundenplanAufsichtsbereich e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Stundenplan_ID", "Kuerzel", "Beschreibung"})
public final class DTOStundenplanAufsichtsbereich {

	/** Die ID identifiziert einen Aufsichtsbereicheintrag für einen Stundenplan eindeutig - hat keinen Bezug zur ID der Katalog-Tabelle */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Die ID des Stundenplans, dem dieser Aufsichtsbereicheintrag zugeordnet wird */
	@Column(name = "Stundenplan_ID")
	@JsonProperty
	public long Stundenplan_ID;

	/** Die Kurzbezeichnung des Aufsichtsbereichs */
	@Column(name = "Kuerzel")
	@JsonProperty
	public String Kuerzel;

	/** Gegebenenfalls eine ausführlichere Beschreibung des Aufsichtsbereichs */
	@Column(name = "Beschreibung")
	@JsonProperty
	public String Beschreibung;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOStundenplanAufsichtsbereich ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOStundenplanAufsichtsbereich() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOStundenplanAufsichtsbereich ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Stundenplan_ID   der Wert für das Attribut Stundenplan_ID
	 * @param Kuerzel   der Wert für das Attribut Kuerzel
	 * @param Beschreibung   der Wert für das Attribut Beschreibung
	 */
	public DTOStundenplanAufsichtsbereich(final long ID, final long Stundenplan_ID, final String Kuerzel, final String Beschreibung) {
		this.ID = ID;
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
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOStundenplanAufsichtsbereich other = (DTOStundenplanAufsichtsbereich) obj;
		if (ID != other.ID)
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(ID);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOStundenplanAufsichtsbereich(ID=" + this.ID + ", Stundenplan_ID=" + this.Stundenplan_ID + ", Kuerzel=" + this.Kuerzel + ", Beschreibung=" + this.Beschreibung + ")";
	}

}
