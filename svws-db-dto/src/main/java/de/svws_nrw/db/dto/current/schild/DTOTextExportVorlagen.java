package de.svws_nrw.db.dto.current.schild;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle TextExportVorlagen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "TextExportVorlagen")
@NamedQuery(name = "DTOTextExportVorlagen.all", query = "SELECT e FROM DTOTextExportVorlagen e")
@NamedQuery(name = "DTOTextExportVorlagen.vorlagename", query = "SELECT e FROM DTOTextExportVorlagen e WHERE e.VorlageName = :value")
@NamedQuery(name = "DTOTextExportVorlagen.vorlagename.multiple", query = "SELECT e FROM DTOTextExportVorlagen e WHERE e.VorlageName IN :value")
@NamedQuery(name = "DTOTextExportVorlagen.daten", query = "SELECT e FROM DTOTextExportVorlagen e WHERE e.Daten = :value")
@NamedQuery(name = "DTOTextExportVorlagen.daten.multiple", query = "SELECT e FROM DTOTextExportVorlagen e WHERE e.Daten IN :value")
@NamedQuery(name = "DTOTextExportVorlagen.primaryKeyQuery", query = "SELECT e FROM DTOTextExportVorlagen e WHERE e.VorlageName = ?1")
@NamedQuery(name = "DTOTextExportVorlagen.all.migration", query = "SELECT e FROM DTOTextExportVorlagen e WHERE e.VorlageName IS NOT NULL")
@JsonPropertyOrder({"VorlageName", "Daten"})
public final class DTOTextExportVorlagen {

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
	 * Erstellt ein neues Objekt der Klasse DTOTextExportVorlagen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOTextExportVorlagen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOTextExportVorlagen ohne eine Initialisierung der Attribute.
	 * @param VorlageName   der Wert für das Attribut VorlageName
	 */
	public DTOTextExportVorlagen(final String VorlageName) {
		if (VorlageName == null) {
			throw new NullPointerException("VorlageName must not be null");
		}
		this.VorlageName = VorlageName;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOTextExportVorlagen other = (DTOTextExportVorlagen) obj;
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
		return "DTOTextExportVorlagen(VorlageName=" + this.VorlageName + ", Daten=" + this.Daten + ")";
	}

}
