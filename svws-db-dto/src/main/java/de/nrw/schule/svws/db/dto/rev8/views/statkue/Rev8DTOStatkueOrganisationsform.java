package de.nrw.schule.svws.db.dto.rev8.views.statkue;

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
 * Diese Klasse dient als DTO für die Datenbank-View Statkue_Organisationsform.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(Rev8DTOStatkueOrganisationsformPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Statkue_Organisationsform")
@NamedQuery(name="Rev8DTOStatkueOrganisationsform.all", query="SELECT e FROM Rev8DTOStatkueOrganisationsform e")
@NamedQuery(name="Rev8DTOStatkueOrganisationsform.sf", query="SELECT e FROM Rev8DTOStatkueOrganisationsform e WHERE e.SF = :value")
@NamedQuery(name="Rev8DTOStatkueOrganisationsform.sf.multiple", query="SELECT e FROM Rev8DTOStatkueOrganisationsform e WHERE e.SF IN :value")
@NamedQuery(name="Rev8DTOStatkueOrganisationsform.orgform", query="SELECT e FROM Rev8DTOStatkueOrganisationsform e WHERE e.OrgForm = :value")
@NamedQuery(name="Rev8DTOStatkueOrganisationsform.orgform.multiple", query="SELECT e FROM Rev8DTOStatkueOrganisationsform e WHERE e.OrgForm IN :value")
@NamedQuery(name="Rev8DTOStatkueOrganisationsform.fsp", query="SELECT e FROM Rev8DTOStatkueOrganisationsform e WHERE e.FSP = :value")
@NamedQuery(name="Rev8DTOStatkueOrganisationsform.fsp.multiple", query="SELECT e FROM Rev8DTOStatkueOrganisationsform e WHERE e.FSP IN :value")
@NamedQuery(name="Rev8DTOStatkueOrganisationsform.beschreibung", query="SELECT e FROM Rev8DTOStatkueOrganisationsform e WHERE e.Beschreibung = :value")
@NamedQuery(name="Rev8DTOStatkueOrganisationsform.beschreibung.multiple", query="SELECT e FROM Rev8DTOStatkueOrganisationsform e WHERE e.Beschreibung IN :value")
@NamedQuery(name="Rev8DTOStatkueOrganisationsform.flag", query="SELECT e FROM Rev8DTOStatkueOrganisationsform e WHERE e.Flag = :value")
@NamedQuery(name="Rev8DTOStatkueOrganisationsform.flag.multiple", query="SELECT e FROM Rev8DTOStatkueOrganisationsform e WHERE e.Flag IN :value")
@NamedQuery(name="Rev8DTOStatkueOrganisationsform.geaendert", query="SELECT e FROM Rev8DTOStatkueOrganisationsform e WHERE e.geaendert = :value")
@NamedQuery(name="Rev8DTOStatkueOrganisationsform.geaendert.multiple", query="SELECT e FROM Rev8DTOStatkueOrganisationsform e WHERE e.geaendert IN :value")
@NamedQuery(name="Rev8DTOStatkueOrganisationsform.gueltigvon", query="SELECT e FROM Rev8DTOStatkueOrganisationsform e WHERE e.gueltigVon = :value")
@NamedQuery(name="Rev8DTOStatkueOrganisationsform.gueltigvon.multiple", query="SELECT e FROM Rev8DTOStatkueOrganisationsform e WHERE e.gueltigVon IN :value")
@NamedQuery(name="Rev8DTOStatkueOrganisationsform.gueltigbis", query="SELECT e FROM Rev8DTOStatkueOrganisationsform e WHERE e.gueltigBis = :value")
@NamedQuery(name="Rev8DTOStatkueOrganisationsform.gueltigbis.multiple", query="SELECT e FROM Rev8DTOStatkueOrganisationsform e WHERE e.gueltigBis IN :value")
@NamedQuery(name="Rev8DTOStatkueOrganisationsform.primaryKeyQuery", query="SELECT e FROM Rev8DTOStatkueOrganisationsform e WHERE e.SF = ?1 AND e.OrgForm = ?2 AND e.FSP = ?3 AND e.Flag = ?4")
@JsonPropertyOrder({"SF","OrgForm","FSP","Beschreibung","Flag","geaendert","gueltigVon","gueltigBis"})
public class Rev8DTOStatkueOrganisationsform {

	/** Die Schulform für welche die Organisationsform zur Verfügung steht */
	@Id
	@Column(name = "SF")
	@JsonProperty
	public String SF;

	/** Das Kürzel der Organisationsform */
	@Id
	@Column(name = "OrgForm")
	@JsonProperty
	public String OrgForm;

	/** Eine Einschränkung auf einen Förderschwerpunkt (hier nur Kompatibilität angegeben) */
	@Id
	@Column(name = "FSP")
	@JsonProperty
	public String FSP;

	/** Die Beschreibung der Organisationsform */
	@Column(name = "Beschreibung")
	@JsonProperty
	public String Beschreibung;

	/** Flag - zur Kompatibilität (hier 1) */
	@Id
	@Column(name = "Flag")
	@JsonProperty
	public String Flag;

	/** Datum der letzten Änderung (hier zur Kompatibilität vorhanden) */
	@Column(name = "geaendert")
	@JsonProperty
	public String geaendert;

	/** Gibt die Gültigkeit ab welchem Schuljahr an */
	@Column(name = "gueltigVon")
	@JsonProperty
	public String gueltigVon;

	/** Gibt die Gültigkeit bis zu welchem Schuljahr an */
	@Column(name = "gueltigBis")
	@JsonProperty
	public String gueltigBis;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOStatkueOrganisationsform ohne eine Initialisierung der Attribute.
	 */
	private Rev8DTOStatkueOrganisationsform() {
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev8DTOStatkueOrganisationsform other = (Rev8DTOStatkueOrganisationsform) obj;
		if (SF == null) {
			if (other.SF != null)
				return false;
		} else if (!SF.equals(other.SF))
			return false;

		if (OrgForm == null) {
			if (other.OrgForm != null)
				return false;
		} else if (!OrgForm.equals(other.OrgForm))
			return false;

		if (FSP == null) {
			if (other.FSP != null)
				return false;
		} else if (!FSP.equals(other.FSP))
			return false;

		if (Flag == null) {
			if (other.Flag != null)
				return false;
		} else if (!Flag.equals(other.Flag))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((SF == null) ? 0 : SF.hashCode());

		result = prime * result + ((OrgForm == null) ? 0 : OrgForm.hashCode());

		result = prime * result + ((FSP == null) ? 0 : FSP.hashCode());

		result = prime * result + ((Flag == null) ? 0 : Flag.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "Rev8DTOStatkueOrganisationsform(SF=" + this.SF + ", OrgForm=" + this.OrgForm + ", FSP=" + this.FSP + ", Beschreibung=" + this.Beschreibung + ", Flag=" + this.Flag + ", geaendert=" + this.geaendert + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ")";
	}

}