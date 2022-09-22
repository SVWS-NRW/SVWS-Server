package de.nrw.schule.svws.db.dto.current.views.statkue;

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
 * Diese Klasse dient als DTO für die Datenbank-View Statkue_Foerderschwerpunkt.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(DTOStatkueFoerderschwerpunktePK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Statkue_Foerderschwerpunkt")
@NamedQuery(name="DTOStatkueFoerderschwerpunkte.all", query="SELECT e FROM DTOStatkueFoerderschwerpunkte e")
@NamedQuery(name="DTOStatkueFoerderschwerpunkte.sf", query="SELECT e FROM DTOStatkueFoerderschwerpunkte e WHERE e.SF = :value")
@NamedQuery(name="DTOStatkueFoerderschwerpunkte.sf.multiple", query="SELECT e FROM DTOStatkueFoerderschwerpunkte e WHERE e.SF IN :value")
@NamedQuery(name="DTOStatkueFoerderschwerpunkte.fsp", query="SELECT e FROM DTOStatkueFoerderschwerpunkte e WHERE e.FSP = :value")
@NamedQuery(name="DTOStatkueFoerderschwerpunkte.fsp.multiple", query="SELECT e FROM DTOStatkueFoerderschwerpunkte e WHERE e.FSP IN :value")
@NamedQuery(name="DTOStatkueFoerderschwerpunkte.beschreibung", query="SELECT e FROM DTOStatkueFoerderschwerpunkte e WHERE e.Beschreibung = :value")
@NamedQuery(name="DTOStatkueFoerderschwerpunkte.beschreibung.multiple", query="SELECT e FROM DTOStatkueFoerderschwerpunkte e WHERE e.Beschreibung IN :value")
@NamedQuery(name="DTOStatkueFoerderschwerpunkte.flag", query="SELECT e FROM DTOStatkueFoerderschwerpunkte e WHERE e.Flag = :value")
@NamedQuery(name="DTOStatkueFoerderschwerpunkte.flag.multiple", query="SELECT e FROM DTOStatkueFoerderschwerpunkte e WHERE e.Flag IN :value")
@NamedQuery(name="DTOStatkueFoerderschwerpunkte.geaendert", query="SELECT e FROM DTOStatkueFoerderschwerpunkte e WHERE e.geaendert = :value")
@NamedQuery(name="DTOStatkueFoerderschwerpunkte.geaendert.multiple", query="SELECT e FROM DTOStatkueFoerderschwerpunkte e WHERE e.geaendert IN :value")
@NamedQuery(name="DTOStatkueFoerderschwerpunkte.gueltigvon", query="SELECT e FROM DTOStatkueFoerderschwerpunkte e WHERE e.gueltigVon = :value")
@NamedQuery(name="DTOStatkueFoerderschwerpunkte.gueltigvon.multiple", query="SELECT e FROM DTOStatkueFoerderschwerpunkte e WHERE e.gueltigVon IN :value")
@NamedQuery(name="DTOStatkueFoerderschwerpunkte.gueltigbis", query="SELECT e FROM DTOStatkueFoerderschwerpunkte e WHERE e.gueltigBis = :value")
@NamedQuery(name="DTOStatkueFoerderschwerpunkte.gueltigbis.multiple", query="SELECT e FROM DTOStatkueFoerderschwerpunkte e WHERE e.gueltigBis IN :value")
@NamedQuery(name="DTOStatkueFoerderschwerpunkte.primaryKeyQuery", query="SELECT e FROM DTOStatkueFoerderschwerpunkte e WHERE e.SF = ?1 AND e.FSP = ?2 AND e.Flag = ?3")
@JsonPropertyOrder({"SF","FSP","Beschreibung","Flag","geaendert","gueltigVon","gueltigBis"})
public class DTOStatkueFoerderschwerpunkte {

	/** Zulässige Schulform des Förderschwerpunktes */
	@Id
	@Column(name = "SF")
	@JsonProperty
	public String SF;

	/** Das Statistik-Kürzel des Förderschwerpunktes */
	@Id
	@Column(name = "FSP")
	@JsonProperty
	public String FSP;

	/** Textuelle Beschreibung des Förderschwerpunktes */
	@Column(name = "Beschreibung")
	@JsonProperty
	public String Beschreibung;

	/** Ein Flag (hier zur Kompatibilität vorhanden) */
	@Id
	@Column(name = "Flag")
	@JsonProperty
	public String Flag;

	/** Das Datum der letzten Änderung (hier zur Kompatibilität vorhanden) */
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
	 * Erstellt ein neues Objekt der Klasse DTOStatkueFoerderschwerpunkte ohne eine Initialisierung der Attribute.
	 */
	private DTOStatkueFoerderschwerpunkte() {
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOStatkueFoerderschwerpunkte other = (DTOStatkueFoerderschwerpunkte) obj;
		if (SF == null) {
			if (other.SF != null)
				return false;
		} else if (!SF.equals(other.SF))
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
		return "DTOStatkueFoerderschwerpunkte(SF=" + this.SF + ", FSP=" + this.FSP + ", Beschreibung=" + this.Beschreibung + ", Flag=" + this.Flag + ", geaendert=" + this.geaendert + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ")";
	}

}