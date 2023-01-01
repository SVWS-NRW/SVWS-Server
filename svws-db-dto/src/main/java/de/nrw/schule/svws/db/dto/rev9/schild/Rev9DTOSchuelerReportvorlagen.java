package de.nrw.schule.svws.db.dto.rev9.schild;

import de.nrw.schule.svws.db.DBEntityManager;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerReportvorlagen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(Rev9DTOSchuelerReportvorlagenPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerReportvorlagen")
@NamedQuery(name="Rev9DTOSchuelerReportvorlagen.all", query="SELECT e FROM Rev9DTOSchuelerReportvorlagen e")
@NamedQuery(name="Rev9DTOSchuelerReportvorlagen.user_id", query="SELECT e FROM Rev9DTOSchuelerReportvorlagen e WHERE e.User_ID = :value")
@NamedQuery(name="Rev9DTOSchuelerReportvorlagen.user_id.multiple", query="SELECT e FROM Rev9DTOSchuelerReportvorlagen e WHERE e.User_ID IN :value")
@NamedQuery(name="Rev9DTOSchuelerReportvorlagen.reportvorlage", query="SELECT e FROM Rev9DTOSchuelerReportvorlagen e WHERE e.Reportvorlage = :value")
@NamedQuery(name="Rev9DTOSchuelerReportvorlagen.reportvorlage.multiple", query="SELECT e FROM Rev9DTOSchuelerReportvorlagen e WHERE e.Reportvorlage IN :value")
@NamedQuery(name="Rev9DTOSchuelerReportvorlagen.schueler_ids", query="SELECT e FROM Rev9DTOSchuelerReportvorlagen e WHERE e.Schueler_IDs = :value")
@NamedQuery(name="Rev9DTOSchuelerReportvorlagen.schueler_ids.multiple", query="SELECT e FROM Rev9DTOSchuelerReportvorlagen e WHERE e.Schueler_IDs IN :value")
@NamedQuery(name="Rev9DTOSchuelerReportvorlagen.primaryKeyQuery", query="SELECT e FROM Rev9DTOSchuelerReportvorlagen e WHERE e.User_ID = ?1 AND e.Reportvorlage = ?2")
@NamedQuery(name="Rev9DTOSchuelerReportvorlagen.all.migration", query="SELECT e FROM Rev9DTOSchuelerReportvorlagen e WHERE e.User_ID IS NOT NULL AND e.Reportvorlage IS NOT NULL")
@JsonPropertyOrder({"User_ID","Reportvorlage","Schueler_IDs"})
public class Rev9DTOSchuelerReportvorlagen {

	/** UserID des Users der die zugeordneten Reportvorlagen druckt */
	@Id
	@Column(name = "User_ID")
	@JsonProperty
	public Long User_ID;

	/** Pfad zur Reportvorlage */
	@Id
	@Column(name = "Reportvorlage")
	@JsonProperty
	public String Reportvorlage;

	/** SchülerID für die gedruckt werden soll (temporär) */
	@Column(name = "Schueler_IDs")
	@JsonProperty
	public String Schueler_IDs;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev9DTOSchuelerReportvorlagen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev9DTOSchuelerReportvorlagen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev9DTOSchuelerReportvorlagen ohne eine Initialisierung der Attribute.
	 * @param User_ID   der Wert für das Attribut User_ID
	 */
	public Rev9DTOSchuelerReportvorlagen(final Long User_ID) {
		if (User_ID == null) { 
			throw new NullPointerException("User_ID must not be null");
		}
		this.User_ID = User_ID;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev9DTOSchuelerReportvorlagen other = (Rev9DTOSchuelerReportvorlagen) obj;
		if (User_ID == null) {
			if (other.User_ID != null)
				return false;
		} else if (!User_ID.equals(other.User_ID))
			return false;

		if (Reportvorlage == null) {
			if (other.Reportvorlage != null)
				return false;
		} else if (!Reportvorlage.equals(other.Reportvorlage))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((User_ID == null) ? 0 : User_ID.hashCode());

		result = prime * result + ((Reportvorlage == null) ? 0 : Reportvorlage.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "Rev9DTOSchuelerReportvorlagen(User_ID=" + this.User_ID + ", Reportvorlage=" + this.Reportvorlage + ", Schueler_IDs=" + this.Schueler_IDs + ")";
	}

}