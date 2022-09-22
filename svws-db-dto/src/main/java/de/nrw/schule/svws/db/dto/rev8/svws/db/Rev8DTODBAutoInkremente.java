package de.nrw.schule.svws.db.dto.rev8.svws.db;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle SVWS_DB_AutoInkremente.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SVWS_DB_AutoInkremente")
@NamedQuery(name="Rev8DTODBAutoInkremente.all", query="SELECT e FROM Rev8DTODBAutoInkremente e")
@NamedQuery(name="Rev8DTODBAutoInkremente.nametabelle", query="SELECT e FROM Rev8DTODBAutoInkremente e WHERE e.NameTabelle = :value")
@NamedQuery(name="Rev8DTODBAutoInkremente.nametabelle.multiple", query="SELECT e FROM Rev8DTODBAutoInkremente e WHERE e.NameTabelle IN :value")
@NamedQuery(name="Rev8DTODBAutoInkremente.maxid", query="SELECT e FROM Rev8DTODBAutoInkremente e WHERE e.MaxID = :value")
@NamedQuery(name="Rev8DTODBAutoInkremente.maxid.multiple", query="SELECT e FROM Rev8DTODBAutoInkremente e WHERE e.MaxID IN :value")
@NamedQuery(name="Rev8DTODBAutoInkremente.primaryKeyQuery", query="SELECT e FROM Rev8DTODBAutoInkremente e WHERE e.NameTabelle = ?1")
@NamedQuery(name="Rev8DTODBAutoInkremente.all.migration", query="SELECT e FROM Rev8DTODBAutoInkremente e WHERE e.NameTabelle IS NOT NULL")
@JsonPropertyOrder({"NameTabelle","MaxID"})
public class Rev8DTODBAutoInkremente {

	/** Gibt den Tabellennamen an, für dessen Auto-Inkrement der ID-Wert verwendet werden soll. */
	@Id
	@Column(name = "NameTabelle")
	@JsonProperty
	public String NameTabelle;

	/** Die ID des höchsten jemals in die DB geschriebenen ID-Wertes bei der zugehörigen Tabelle */
	@Column(name = "MaxID")
	@JsonProperty
	public Long MaxID;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTODBAutoInkremente ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev8DTODBAutoInkremente() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTODBAutoInkremente ohne eine Initialisierung der Attribute.
	 * @param NameTabelle   der Wert für das Attribut NameTabelle
	 * @param MaxID   der Wert für das Attribut MaxID
	 */
	public Rev8DTODBAutoInkremente(final String NameTabelle, final Long MaxID) {
		if (NameTabelle == null) { 
			throw new NullPointerException("NameTabelle must not be null");
		}
		this.NameTabelle = NameTabelle;
		if (MaxID == null) { 
			throw new NullPointerException("MaxID must not be null");
		}
		this.MaxID = MaxID;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev8DTODBAutoInkremente other = (Rev8DTODBAutoInkremente) obj;
		if (NameTabelle == null) {
			if (other.NameTabelle != null)
				return false;
		} else if (!NameTabelle.equals(other.NameTabelle))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((NameTabelle == null) ? 0 : NameTabelle.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "Rev8DTODBAutoInkremente(NameTabelle=" + this.NameTabelle + ", MaxID=" + this.MaxID + ")";
	}

}