package de.nrw.schule.svws.db.dto.rev8.schild.kurse;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle KursartenKatalog_Keys.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "KursartenKatalog_Keys")
@NamedQuery(name="Rev8DTOKursartenKatalogKeys.all", query="SELECT e FROM Rev8DTOKursartenKatalogKeys e")
@NamedQuery(name="Rev8DTOKursartenKatalogKeys.kuerzel", query="SELECT e FROM Rev8DTOKursartenKatalogKeys e WHERE e.Kuerzel = :value")
@NamedQuery(name="Rev8DTOKursartenKatalogKeys.kuerzel.multiple", query="SELECT e FROM Rev8DTOKursartenKatalogKeys e WHERE e.Kuerzel IN :value")
@NamedQuery(name="Rev8DTOKursartenKatalogKeys.primaryKeyQuery", query="SELECT e FROM Rev8DTOKursartenKatalogKeys e WHERE e.Kuerzel = ?1")
@NamedQuery(name="Rev8DTOKursartenKatalogKeys.all.migration", query="SELECT e FROM Rev8DTOKursartenKatalogKeys e WHERE e.Kuerzel IS NOT NULL")
@JsonPropertyOrder({"Kuerzel"})
public class Rev8DTOKursartenKatalogKeys {

	/** Das Kürzel der Kursart */
	@Id
	@Column(name = "Kuerzel")
	@JsonProperty
	public String Kuerzel;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOKursartenKatalogKeys ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev8DTOKursartenKatalogKeys() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOKursartenKatalogKeys ohne eine Initialisierung der Attribute.
	 * @param Kuerzel   der Wert für das Attribut Kuerzel
	 */
	public Rev8DTOKursartenKatalogKeys(final String Kuerzel) {
		if (Kuerzel == null) { 
			throw new NullPointerException("Kuerzel must not be null");
		}
		this.Kuerzel = Kuerzel;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev8DTOKursartenKatalogKeys other = (Rev8DTOKursartenKatalogKeys) obj;
		if (Kuerzel == null) {
			if (other.Kuerzel != null)
				return false;
		} else if (!Kuerzel.equals(other.Kuerzel))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Kuerzel == null) ? 0 : Kuerzel.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "Rev8DTOKursartenKatalogKeys(Kuerzel=" + this.Kuerzel + ")";
	}

}