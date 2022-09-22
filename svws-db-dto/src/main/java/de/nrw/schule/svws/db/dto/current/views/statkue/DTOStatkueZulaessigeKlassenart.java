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
 * Diese Klasse dient als DTO für die Datenbank-View Statkue_ZulKlArt.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(DTOStatkueZulaessigeKlassenartPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Statkue_ZulKlArt")
@NamedQuery(name="DTOStatkueZulaessigeKlassenart.all", query="SELECT e FROM DTOStatkueZulaessigeKlassenart e")
@NamedQuery(name="DTOStatkueZulaessigeKlassenart.klart", query="SELECT e FROM DTOStatkueZulaessigeKlassenart e WHERE e.KlArt = :value")
@NamedQuery(name="DTOStatkueZulaessigeKlassenart.klart.multiple", query="SELECT e FROM DTOStatkueZulaessigeKlassenart e WHERE e.KlArt IN :value")
@NamedQuery(name="DTOStatkueZulaessigeKlassenart.schulform", query="SELECT e FROM DTOStatkueZulaessigeKlassenart e WHERE e.Schulform = :value")
@NamedQuery(name="DTOStatkueZulaessigeKlassenart.schulform.multiple", query="SELECT e FROM DTOStatkueZulaessigeKlassenart e WHERE e.Schulform IN :value")
@NamedQuery(name="DTOStatkueZulaessigeKlassenart.fsp", query="SELECT e FROM DTOStatkueZulaessigeKlassenart e WHERE e.FSP = :value")
@NamedQuery(name="DTOStatkueZulaessigeKlassenart.fsp.multiple", query="SELECT e FROM DTOStatkueZulaessigeKlassenart e WHERE e.FSP IN :value")
@NamedQuery(name="DTOStatkueZulaessigeKlassenart.bezeichnung", query="SELECT e FROM DTOStatkueZulaessigeKlassenart e WHERE e.Bezeichnung = :value")
@NamedQuery(name="DTOStatkueZulaessigeKlassenart.bezeichnung.multiple", query="SELECT e FROM DTOStatkueZulaessigeKlassenart e WHERE e.Bezeichnung IN :value")
@NamedQuery(name="DTOStatkueZulaessigeKlassenart.geaendert", query="SELECT e FROM DTOStatkueZulaessigeKlassenart e WHERE e.geaendert = :value")
@NamedQuery(name="DTOStatkueZulaessigeKlassenart.geaendert.multiple", query="SELECT e FROM DTOStatkueZulaessigeKlassenart e WHERE e.geaendert IN :value")
@NamedQuery(name="DTOStatkueZulaessigeKlassenart.gueltigvon", query="SELECT e FROM DTOStatkueZulaessigeKlassenart e WHERE e.gueltigVon = :value")
@NamedQuery(name="DTOStatkueZulaessigeKlassenart.gueltigvon.multiple", query="SELECT e FROM DTOStatkueZulaessigeKlassenart e WHERE e.gueltigVon IN :value")
@NamedQuery(name="DTOStatkueZulaessigeKlassenart.gueltigbis", query="SELECT e FROM DTOStatkueZulaessigeKlassenart e WHERE e.gueltigBis = :value")
@NamedQuery(name="DTOStatkueZulaessigeKlassenart.gueltigbis.multiple", query="SELECT e FROM DTOStatkueZulaessigeKlassenart e WHERE e.gueltigBis IN :value")
@NamedQuery(name="DTOStatkueZulaessigeKlassenart.primaryKeyQuery", query="SELECT e FROM DTOStatkueZulaessigeKlassenart e WHERE e.KlArt = ?1 AND e.Schulform = ?2 AND e.FSP = ?3 AND e.Bezeichnung = ?4")
@JsonPropertyOrder({"KlArt","Schulform","FSP","Bezeichnung","geaendert","gueltigVon","gueltigBis"})
public class DTOStatkueZulaessigeKlassenart {

	/** Das Kürzel der Klassenart */
	@Id
	@Column(name = "KlArt")
	@JsonProperty
	public String KlArt;

	/** Die Schulform bei der die Kursart zulässig ist. */
	@Id
	@Column(name = "Schulform")
	@JsonProperty
	public String Schulform;

	/** Eine Einschränkung der Zulässigkeit der Kusart auf einen Förderschwerpunkt (hier nur Kompatibilität angegeben) */
	@Id
	@Column(name = "FSP")
	@JsonProperty
	public String FSP;

	/** Die Bezeichnung der Kursart */
	@Id
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

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
	 * Erstellt ein neues Objekt der Klasse DTOStatkueZulaessigeKlassenart ohne eine Initialisierung der Attribute.
	 */
	private DTOStatkueZulaessigeKlassenart() {
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOStatkueZulaessigeKlassenart other = (DTOStatkueZulaessigeKlassenart) obj;
		if (KlArt == null) {
			if (other.KlArt != null)
				return false;
		} else if (!KlArt.equals(other.KlArt))
			return false;

		if (Schulform == null) {
			if (other.Schulform != null)
				return false;
		} else if (!Schulform.equals(other.Schulform))
			return false;

		if (FSP == null) {
			if (other.FSP != null)
				return false;
		} else if (!FSP.equals(other.FSP))
			return false;

		if (Bezeichnung == null) {
			if (other.Bezeichnung != null)
				return false;
		} else if (!Bezeichnung.equals(other.Bezeichnung))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((KlArt == null) ? 0 : KlArt.hashCode());

		result = prime * result + ((Schulform == null) ? 0 : Schulform.hashCode());

		result = prime * result + ((FSP == null) ? 0 : FSP.hashCode());

		result = prime * result + ((Bezeichnung == null) ? 0 : Bezeichnung.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOStatkueZulaessigeKlassenart(KlArt=" + this.KlArt + ", Schulform=" + this.Schulform + ", FSP=" + this.FSP + ", Bezeichnung=" + this.Bezeichnung + ", geaendert=" + this.geaendert + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ")";
	}

}