package de.nrw.schule.svws.db.dto.current.schild.lehrer;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Schulleitung.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Schulleitung")
@NamedQuery(name="DTOSchulleitung.all", query="SELECT e FROM DTOSchulleitung e")
@NamedQuery(name="DTOSchulleitung.id", query="SELECT e FROM DTOSchulleitung e WHERE e.ID = :value")
@NamedQuery(name="DTOSchulleitung.id.multiple", query="SELECT e FROM DTOSchulleitung e WHERE e.ID IN :value")
@NamedQuery(name="DTOSchulleitung.leitungsfunktionid", query="SELECT e FROM DTOSchulleitung e WHERE e.LeitungsfunktionID = :value")
@NamedQuery(name="DTOSchulleitung.leitungsfunktionid.multiple", query="SELECT e FROM DTOSchulleitung e WHERE e.LeitungsfunktionID IN :value")
@NamedQuery(name="DTOSchulleitung.funktionstext", query="SELECT e FROM DTOSchulleitung e WHERE e.Funktionstext = :value")
@NamedQuery(name="DTOSchulleitung.funktionstext.multiple", query="SELECT e FROM DTOSchulleitung e WHERE e.Funktionstext IN :value")
@NamedQuery(name="DTOSchulleitung.lehrerid", query="SELECT e FROM DTOSchulleitung e WHERE e.LehrerID = :value")
@NamedQuery(name="DTOSchulleitung.lehrerid.multiple", query="SELECT e FROM DTOSchulleitung e WHERE e.LehrerID IN :value")
@NamedQuery(name="DTOSchulleitung.von", query="SELECT e FROM DTOSchulleitung e WHERE e.Von = :value")
@NamedQuery(name="DTOSchulleitung.von.multiple", query="SELECT e FROM DTOSchulleitung e WHERE e.Von IN :value")
@NamedQuery(name="DTOSchulleitung.bis", query="SELECT e FROM DTOSchulleitung e WHERE e.Bis = :value")
@NamedQuery(name="DTOSchulleitung.bis.multiple", query="SELECT e FROM DTOSchulleitung e WHERE e.Bis IN :value")
@NamedQuery(name="DTOSchulleitung.primaryKeyQuery", query="SELECT e FROM DTOSchulleitung e WHERE e.ID = ?1")
@NamedQuery(name="DTOSchulleitung.all.migration", query="SELECT e FROM DTOSchulleitung e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","LeitungsfunktionID","Funktionstext","LehrerID","Von","Bis"})
public class DTOSchulleitung {

	/** ID der Schulleitungsfunktionseintrags */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** ID der Schulleitungsfunktion (Fremdschlüssel aus der Tabelle SchulleitungFunktion) */
	@Column(name = "LeitungsfunktionID")
	@JsonProperty
	public Long LeitungsfunktionID;

	/** Beschreibung der Funktion (Default Text aus SchulleitungFunktion aber änderbar) */
	@Column(name = "Funktionstext")
	@JsonProperty
	public String Funktionstext;

	/** ID des Lehrerdatensatzes */
	@Column(name = "LehrerID")
	@JsonProperty
	public Long LehrerID;

	/** Beginndatum */
	@Column(name = "Von")
	@JsonProperty
	public String Von;

	/** Endedatum */
	@Column(name = "Bis")
	@JsonProperty
	public String Bis;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchulleitung ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSchulleitung() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchulleitung ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param LeitungsfunktionID   der Wert für das Attribut LeitungsfunktionID
	 * @param Funktionstext   der Wert für das Attribut Funktionstext
	 * @param LehrerID   der Wert für das Attribut LehrerID
	 */
	public DTOSchulleitung(final Long ID, final Long LeitungsfunktionID, final String Funktionstext, final Long LehrerID) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (LeitungsfunktionID == null) { 
			throw new NullPointerException("LeitungsfunktionID must not be null");
		}
		this.LeitungsfunktionID = LeitungsfunktionID;
		if (Funktionstext == null) { 
			throw new NullPointerException("Funktionstext must not be null");
		}
		this.Funktionstext = Funktionstext;
		if (LehrerID == null) { 
			throw new NullPointerException("LehrerID must not be null");
		}
		this.LehrerID = LehrerID;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOSchulleitung other = (DTOSchulleitung) obj;
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
		return "DTOSchulleitung(ID=" + this.ID + ", LeitungsfunktionID=" + this.LeitungsfunktionID + ", Funktionstext=" + this.Funktionstext + ", LehrerID=" + this.LehrerID + ", Von=" + this.Von + ", Bis=" + this.Bis + ")";
	}

}