package de.nrw.schule.svws.db.dto.rev8.schild.katalog;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Katalog_Raeume.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Katalog_Raeume")
@NamedQuery(name="Rev8DTOKatalogRaum.all", query="SELECT e FROM Rev8DTOKatalogRaum e")
@NamedQuery(name="Rev8DTOKatalogRaum.id", query="SELECT e FROM Rev8DTOKatalogRaum e WHERE e.ID = :value")
@NamedQuery(name="Rev8DTOKatalogRaum.id.multiple", query="SELECT e FROM Rev8DTOKatalogRaum e WHERE e.ID IN :value")
@NamedQuery(name="Rev8DTOKatalogRaum.kuerzel", query="SELECT e FROM Rev8DTOKatalogRaum e WHERE e.Kuerzel = :value")
@NamedQuery(name="Rev8DTOKatalogRaum.kuerzel.multiple", query="SELECT e FROM Rev8DTOKatalogRaum e WHERE e.Kuerzel IN :value")
@NamedQuery(name="Rev8DTOKatalogRaum.beschreibung", query="SELECT e FROM Rev8DTOKatalogRaum e WHERE e.Beschreibung = :value")
@NamedQuery(name="Rev8DTOKatalogRaum.beschreibung.multiple", query="SELECT e FROM Rev8DTOKatalogRaum e WHERE e.Beschreibung IN :value")
@NamedQuery(name="Rev8DTOKatalogRaum.groesse", query="SELECT e FROM Rev8DTOKatalogRaum e WHERE e.Groesse = :value")
@NamedQuery(name="Rev8DTOKatalogRaum.groesse.multiple", query="SELECT e FROM Rev8DTOKatalogRaum e WHERE e.Groesse IN :value")
@NamedQuery(name="Rev8DTOKatalogRaum.primaryKeyQuery", query="SELECT e FROM Rev8DTOKatalogRaum e WHERE e.ID = ?1")
@NamedQuery(name="Rev8DTOKatalogRaum.all.migration", query="SELECT e FROM Rev8DTOKatalogRaum e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Kuerzel","Beschreibung","Groesse"})
public class Rev8DTOKatalogRaum {

	/** Die ID identifiziert einen Raumeintrag eindeutig */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Das Kürzel des Raums - auch eindeutig */
	@Column(name = "Kuerzel")
	@JsonProperty
	public String Kuerzel;

	/** Gegebenenfalls eine ausführlichere Beschreibung des Raumes */
	@Column(name = "Beschreibung")
	@JsonProperty
	public String Beschreibung;

	/** Die Größe des Raumes, d.h. wie viele Schüler hier max. Platz haben */
	@Column(name = "Groesse")
	@JsonProperty
	public Integer Groesse;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOKatalogRaum ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev8DTOKatalogRaum() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOKatalogRaum ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Kuerzel   der Wert für das Attribut Kuerzel
	 * @param Beschreibung   der Wert für das Attribut Beschreibung
	 * @param Groesse   der Wert für das Attribut Groesse
	 */
	public Rev8DTOKatalogRaum(final Long ID, final String Kuerzel, final String Beschreibung, final Integer Groesse) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Kuerzel == null) { 
			throw new NullPointerException("Kuerzel must not be null");
		}
		this.Kuerzel = Kuerzel;
		if (Beschreibung == null) { 
			throw new NullPointerException("Beschreibung must not be null");
		}
		this.Beschreibung = Beschreibung;
		if (Groesse == null) { 
			throw new NullPointerException("Groesse must not be null");
		}
		this.Groesse = Groesse;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev8DTOKatalogRaum other = (Rev8DTOKatalogRaum) obj;
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
		return "Rev8DTOKatalogRaum(ID=" + this.ID + ", Kuerzel=" + this.Kuerzel + ", Beschreibung=" + this.Beschreibung + ", Groesse=" + this.Groesse + ")";
	}

}