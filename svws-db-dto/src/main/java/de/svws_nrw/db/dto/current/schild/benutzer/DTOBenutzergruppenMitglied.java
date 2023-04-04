package de.svws_nrw.db.dto.current.schild.benutzer;

import de.svws_nrw.db.DBEntityManager;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle BenutzergruppenMitglieder.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(DTOBenutzergruppenMitgliedPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "BenutzergruppenMitglieder")
@NamedQuery(name = "DTOBenutzergruppenMitglied.all", query = "SELECT e FROM DTOBenutzergruppenMitglied e")
@NamedQuery(name = "DTOBenutzergruppenMitglied.gruppe_id", query = "SELECT e FROM DTOBenutzergruppenMitglied e WHERE e.Gruppe_ID = :value")
@NamedQuery(name = "DTOBenutzergruppenMitglied.gruppe_id.multiple", query = "SELECT e FROM DTOBenutzergruppenMitglied e WHERE e.Gruppe_ID IN :value")
@NamedQuery(name = "DTOBenutzergruppenMitglied.benutzer_id", query = "SELECT e FROM DTOBenutzergruppenMitglied e WHERE e.Benutzer_ID = :value")
@NamedQuery(name = "DTOBenutzergruppenMitglied.benutzer_id.multiple", query = "SELECT e FROM DTOBenutzergruppenMitglied e WHERE e.Benutzer_ID IN :value")
@NamedQuery(name = "DTOBenutzergruppenMitglied.primaryKeyQuery", query = "SELECT e FROM DTOBenutzergruppenMitglied e WHERE e.Gruppe_ID = ?1 AND e.Benutzer_ID = ?2")
@NamedQuery(name = "DTOBenutzergruppenMitglied.all.migration", query = "SELECT e FROM DTOBenutzergruppenMitglied e WHERE e.Gruppe_ID IS NOT NULL AND e.Benutzer_ID IS NOT NULL")
@JsonPropertyOrder({"Gruppe_ID", "Benutzer_ID"})
public final class DTOBenutzergruppenMitglied {

	/** Die ID der Benutzergruppe */
	@Id
	@Column(name = "Gruppe_ID")
	@JsonProperty
	public Long Gruppe_ID;

	/** Die ID des Benutzers */
	@Id
	@Column(name = "Benutzer_ID")
	@JsonProperty
	public Long Benutzer_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOBenutzergruppenMitglied ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOBenutzergruppenMitglied() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOBenutzergruppenMitglied ohne eine Initialisierung der Attribute.
	 * @param Gruppe_ID   der Wert für das Attribut Gruppe_ID
	 * @param Benutzer_ID   der Wert für das Attribut Benutzer_ID
	 */
	public DTOBenutzergruppenMitglied(final Long Gruppe_ID, final Long Benutzer_ID) {
		if (Gruppe_ID == null) {
			throw new NullPointerException("Gruppe_ID must not be null");
		}
		this.Gruppe_ID = Gruppe_ID;
		if (Benutzer_ID == null) {
			throw new NullPointerException("Benutzer_ID must not be null");
		}
		this.Benutzer_ID = Benutzer_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOBenutzergruppenMitglied other = (DTOBenutzergruppenMitglied) obj;
		if (Gruppe_ID == null) {
			if (other.Gruppe_ID != null)
				return false;
		} else if (!Gruppe_ID.equals(other.Gruppe_ID))
			return false;

		if (Benutzer_ID == null) {
			if (other.Benutzer_ID != null)
				return false;
		} else if (!Benutzer_ID.equals(other.Benutzer_ID))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Gruppe_ID == null) ? 0 : Gruppe_ID.hashCode());

		result = prime * result + ((Benutzer_ID == null) ? 0 : Benutzer_ID.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOBenutzergruppenMitglied(Gruppe_ID=" + this.Gruppe_ID + ", Benutzer_ID=" + this.Benutzer_ID + ")";
	}

}
