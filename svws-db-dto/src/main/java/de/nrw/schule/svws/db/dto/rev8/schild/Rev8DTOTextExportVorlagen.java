package de.nrw.schule.svws.db.dto.rev8.schild;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle TextExportVorlagen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "TextExportVorlagen")
@NamedQuery(name="Rev8DTOTextExportVorlagen.all", query="SELECT e FROM Rev8DTOTextExportVorlagen e")
@NamedQuery(name="Rev8DTOTextExportVorlagen.vorlagename", query="SELECT e FROM Rev8DTOTextExportVorlagen e WHERE e.VorlageName = :value")
@NamedQuery(name="Rev8DTOTextExportVorlagen.vorlagename.multiple", query="SELECT e FROM Rev8DTOTextExportVorlagen e WHERE e.VorlageName IN :value")
@NamedQuery(name="Rev8DTOTextExportVorlagen.daten", query="SELECT e FROM Rev8DTOTextExportVorlagen e WHERE e.Daten = :value")
@NamedQuery(name="Rev8DTOTextExportVorlagen.daten.multiple", query="SELECT e FROM Rev8DTOTextExportVorlagen e WHERE e.Daten IN :value")
@NamedQuery(name="Rev8DTOTextExportVorlagen.primaryKeyQuery", query="SELECT e FROM Rev8DTOTextExportVorlagen e WHERE e.VorlageName = ?1")
@NamedQuery(name="Rev8DTOTextExportVorlagen.all.migration", query="SELECT e FROM Rev8DTOTextExportVorlagen e WHERE e.VorlageName IS NOT NULL")
@JsonPropertyOrder({"VorlageName","Daten"})
public class Rev8DTOTextExportVorlagen {

	/** Name der Export-Textvorlage */
	@Id
	@Column(name = "VorlageName")
	@JsonProperty
	public String VorlageName;

	/** Daten die in der Export-Textvorlage enthalten sind */
	@Column(name = "Daten")
	@JsonProperty
	public String Daten;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOTextExportVorlagen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev8DTOTextExportVorlagen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOTextExportVorlagen ohne eine Initialisierung der Attribute.
	 * @param VorlageName   der Wert für das Attribut VorlageName
	 */
	public Rev8DTOTextExportVorlagen(final String VorlageName) {
		if (VorlageName == null) { 
			throw new NullPointerException("VorlageName must not be null");
		}
		this.VorlageName = VorlageName;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev8DTOTextExportVorlagen other = (Rev8DTOTextExportVorlagen) obj;
		if (VorlageName == null) {
			if (other.VorlageName != null)
				return false;
		} else if (!VorlageName.equals(other.VorlageName))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((VorlageName == null) ? 0 : VorlageName.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "Rev8DTOTextExportVorlagen(VorlageName=" + this.VorlageName + ", Daten=" + this.Daten + ")";
	}

}