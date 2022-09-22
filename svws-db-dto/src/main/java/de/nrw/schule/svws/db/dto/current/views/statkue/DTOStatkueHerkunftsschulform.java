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
 * Diese Klasse dient als DTO für die Datenbank-View Statkue_Herkunftsschulform.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(DTOStatkueHerkunftsschulformPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Statkue_Herkunftsschulform")
@NamedQuery(name="DTOStatkueHerkunftsschulform.all", query="SELECT e FROM DTOStatkueHerkunftsschulform e")
@NamedQuery(name="DTOStatkueHerkunftsschulform.sf", query="SELECT e FROM DTOStatkueHerkunftsschulform e WHERE e.SF = :value")
@NamedQuery(name="DTOStatkueHerkunftsschulform.sf.multiple", query="SELECT e FROM DTOStatkueHerkunftsschulform e WHERE e.SF IN :value")
@NamedQuery(name="DTOStatkueHerkunftsschulform.hsf", query="SELECT e FROM DTOStatkueHerkunftsschulform e WHERE e.HSF = :value")
@NamedQuery(name="DTOStatkueHerkunftsschulform.hsf.multiple", query="SELECT e FROM DTOStatkueHerkunftsschulform e WHERE e.HSF IN :value")
@NamedQuery(name="DTOStatkueHerkunftsschulform.beschreibung", query="SELECT e FROM DTOStatkueHerkunftsschulform e WHERE e.Beschreibung = :value")
@NamedQuery(name="DTOStatkueHerkunftsschulform.beschreibung.multiple", query="SELECT e FROM DTOStatkueHerkunftsschulform e WHERE e.Beschreibung IN :value")
@NamedQuery(name="DTOStatkueHerkunftsschulform.flag", query="SELECT e FROM DTOStatkueHerkunftsschulform e WHERE e.Flag = :value")
@NamedQuery(name="DTOStatkueHerkunftsschulform.flag.multiple", query="SELECT e FROM DTOStatkueHerkunftsschulform e WHERE e.Flag IN :value")
@NamedQuery(name="DTOStatkueHerkunftsschulform.geaendert", query="SELECT e FROM DTOStatkueHerkunftsschulform e WHERE e.geaendert = :value")
@NamedQuery(name="DTOStatkueHerkunftsschulform.geaendert.multiple", query="SELECT e FROM DTOStatkueHerkunftsschulform e WHERE e.geaendert IN :value")
@NamedQuery(name="DTOStatkueHerkunftsschulform.gueltigvon", query="SELECT e FROM DTOStatkueHerkunftsschulform e WHERE e.gueltigVon = :value")
@NamedQuery(name="DTOStatkueHerkunftsschulform.gueltigvon.multiple", query="SELECT e FROM DTOStatkueHerkunftsschulform e WHERE e.gueltigVon IN :value")
@NamedQuery(name="DTOStatkueHerkunftsschulform.gueltigbis", query="SELECT e FROM DTOStatkueHerkunftsschulform e WHERE e.gueltigBis = :value")
@NamedQuery(name="DTOStatkueHerkunftsschulform.gueltigbis.multiple", query="SELECT e FROM DTOStatkueHerkunftsschulform e WHERE e.gueltigBis IN :value")
@NamedQuery(name="DTOStatkueHerkunftsschulform.primaryKeyQuery", query="SELECT e FROM DTOStatkueHerkunftsschulform e WHERE e.SF = ?1 AND e.HSF = ?2")
@JsonPropertyOrder({"SF","HSF","Beschreibung","Flag","geaendert","gueltigVon","gueltigBis"})
public class DTOStatkueHerkunftsschulform {

	/** Zulässige Schulform für die Herkunft */
	@Id
	@Column(name = "SF")
	@JsonProperty
	public String SF;

	/** Das Kürzel für die Herkunft/Herkunftsschulform */
	@Id
	@Column(name = "HSF")
	@JsonProperty
	public String HSF;

	/** Textuelle Beschreibung der Herkunft/Herkunftsschulform */
	@Column(name = "Beschreibung")
	@JsonProperty
	public String Beschreibung;

	/** Ein Flag (hier zur Kompatibilität vorhanden) */
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
	 * Erstellt ein neues Objekt der Klasse DTOStatkueHerkunftsschulform ohne eine Initialisierung der Attribute.
	 */
	private DTOStatkueHerkunftsschulform() {
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOStatkueHerkunftsschulform other = (DTOStatkueHerkunftsschulform) obj;
		if (SF == null) {
			if (other.SF != null)
				return false;
		} else if (!SF.equals(other.SF))
			return false;

		if (HSF == null) {
			if (other.HSF != null)
				return false;
		} else if (!HSF.equals(other.HSF))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((SF == null) ? 0 : SF.hashCode());

		result = prime * result + ((HSF == null) ? 0 : HSF.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOStatkueHerkunftsschulform(SF=" + this.SF + ", HSF=" + this.HSF + ", Beschreibung=" + this.Beschreibung + ", Flag=" + this.Flag + ", geaendert=" + this.geaendert + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ")";
	}

}