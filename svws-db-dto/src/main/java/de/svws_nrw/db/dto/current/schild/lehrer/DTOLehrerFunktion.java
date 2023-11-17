package de.svws_nrw.db.dto.current.schild.lehrer;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle LehrerFunktionen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "LehrerFunktionen")
@NamedQuery(name = "DTOLehrerFunktion.all", query = "SELECT e FROM DTOLehrerFunktion e")
@NamedQuery(name = "DTOLehrerFunktion.id", query = "SELECT e FROM DTOLehrerFunktion e WHERE e.ID = :value")
@NamedQuery(name = "DTOLehrerFunktion.id.multiple", query = "SELECT e FROM DTOLehrerFunktion e WHERE e.ID IN :value")
@NamedQuery(name = "DTOLehrerFunktion.abschnitt_id", query = "SELECT e FROM DTOLehrerFunktion e WHERE e.Abschnitt_ID = :value")
@NamedQuery(name = "DTOLehrerFunktion.abschnitt_id.multiple", query = "SELECT e FROM DTOLehrerFunktion e WHERE e.Abschnitt_ID IN :value")
@NamedQuery(name = "DTOLehrerFunktion.funktion_id", query = "SELECT e FROM DTOLehrerFunktion e WHERE e.Funktion_ID = :value")
@NamedQuery(name = "DTOLehrerFunktion.funktion_id.multiple", query = "SELECT e FROM DTOLehrerFunktion e WHERE e.Funktion_ID IN :value")
@NamedQuery(name = "DTOLehrerFunktion.primaryKeyQuery", query = "SELECT e FROM DTOLehrerFunktion e WHERE e.ID = ?1")
@NamedQuery(name = "DTOLehrerFunktion.primaryKeyQuery.multiple", query = "SELECT e FROM DTOLehrerFunktion e WHERE e.ID IN :value")
@NamedQuery(name = "DTOLehrerFunktion.all.migration", query = "SELECT e FROM DTOLehrerFunktion e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Abschnitt_ID", "Funktion_ID"})
public final class DTOLehrerFunktion {

	/** ID für den Eintrag für die schulinterne Funktion eines Lehrers */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** ID der Lehrerabschnittsdaten */
	@Column(name = "Abschnitt_ID")
	@JsonProperty
	public long Abschnitt_ID;

	/** ID der schulinternen Funktion */
	@Column(name = "Funktion_ID")
	@JsonProperty
	public long Funktion_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOLehrerFunktion ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOLehrerFunktion() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOLehrerFunktion ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Abschnitt_ID   der Wert für das Attribut Abschnitt_ID
	 * @param Funktion_ID   der Wert für das Attribut Funktion_ID
	 */
	public DTOLehrerFunktion(final long ID, final long Abschnitt_ID, final long Funktion_ID) {
		this.ID = ID;
		this.Abschnitt_ID = Abschnitt_ID;
		this.Funktion_ID = Funktion_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOLehrerFunktion other = (DTOLehrerFunktion) obj;
		return ID == other.ID;
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
		return "DTOLehrerFunktion(ID=" + this.ID + ", Abschnitt_ID=" + this.Abschnitt_ID + ", Funktion_ID=" + this.Funktion_ID + ")";
	}

}
