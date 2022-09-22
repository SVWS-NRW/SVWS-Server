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
 * Diese Klasse dient als DTO für die Datenbank-View Statkue_ZulFaecher.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(DTOStatkueZulaessigesFachPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Statkue_ZulFaecher")
@NamedQuery(name="DTOStatkueZulaessigesFach.all", query="SELECT e FROM DTOStatkueZulaessigesFach e")
@NamedQuery(name="DTOStatkueZulaessigesFach.schulform", query="SELECT e FROM DTOStatkueZulaessigesFach e WHERE e.Schulform = :value")
@NamedQuery(name="DTOStatkueZulaessigesFach.schulform.multiple", query="SELECT e FROM DTOStatkueZulaessigesFach e WHERE e.Schulform IN :value")
@NamedQuery(name="DTOStatkueZulaessigesFach.fsp", query="SELECT e FROM DTOStatkueZulaessigesFach e WHERE e.FSP = :value")
@NamedQuery(name="DTOStatkueZulaessigesFach.fsp.multiple", query="SELECT e FROM DTOStatkueZulaessigesFach e WHERE e.FSP IN :value")
@NamedQuery(name="DTOStatkueZulaessigesFach.bg", query="SELECT e FROM DTOStatkueZulaessigesFach e WHERE e.BG = :value")
@NamedQuery(name="DTOStatkueZulaessigesFach.bg.multiple", query="SELECT e FROM DTOStatkueZulaessigesFach e WHERE e.BG IN :value")
@NamedQuery(name="DTOStatkueZulaessigesFach.fach", query="SELECT e FROM DTOStatkueZulaessigesFach e WHERE e.Fach = :value")
@NamedQuery(name="DTOStatkueZulaessigesFach.fach.multiple", query="SELECT e FROM DTOStatkueZulaessigesFach e WHERE e.Fach IN :value")
@NamedQuery(name="DTOStatkueZulaessigesFach.bezeichnung", query="SELECT e FROM DTOStatkueZulaessigesFach e WHERE e.Bezeichnung = :value")
@NamedQuery(name="DTOStatkueZulaessigesFach.bezeichnung.multiple", query="SELECT e FROM DTOStatkueZulaessigesFach e WHERE e.Bezeichnung IN :value")
@NamedQuery(name="DTOStatkueZulaessigesFach.kz_bereich", query="SELECT e FROM DTOStatkueZulaessigesFach e WHERE e.KZ_Bereich = :value")
@NamedQuery(name="DTOStatkueZulaessigesFach.kz_bereich.multiple", query="SELECT e FROM DTOStatkueZulaessigesFach e WHERE e.KZ_Bereich IN :value")
@NamedQuery(name="DTOStatkueZulaessigesFach.flag", query="SELECT e FROM DTOStatkueZulaessigesFach e WHERE e.Flag = :value")
@NamedQuery(name="DTOStatkueZulaessigesFach.flag.multiple", query="SELECT e FROM DTOStatkueZulaessigesFach e WHERE e.Flag IN :value")
@NamedQuery(name="DTOStatkueZulaessigesFach.sortierung", query="SELECT e FROM DTOStatkueZulaessigesFach e WHERE e.Sortierung = :value")
@NamedQuery(name="DTOStatkueZulaessigesFach.sortierung.multiple", query="SELECT e FROM DTOStatkueZulaessigesFach e WHERE e.Sortierung IN :value")
@NamedQuery(name="DTOStatkueZulaessigesFach.sortierung2", query="SELECT e FROM DTOStatkueZulaessigesFach e WHERE e.Sortierung2 = :value")
@NamedQuery(name="DTOStatkueZulaessigesFach.sortierung2.multiple", query="SELECT e FROM DTOStatkueZulaessigesFach e WHERE e.Sortierung2 IN :value")
@NamedQuery(name="DTOStatkueZulaessigesFach.geaendert", query="SELECT e FROM DTOStatkueZulaessigesFach e WHERE e.geaendert = :value")
@NamedQuery(name="DTOStatkueZulaessigesFach.geaendert.multiple", query="SELECT e FROM DTOStatkueZulaessigesFach e WHERE e.geaendert IN :value")
@NamedQuery(name="DTOStatkueZulaessigesFach.gueltigvon", query="SELECT e FROM DTOStatkueZulaessigesFach e WHERE e.gueltigVon = :value")
@NamedQuery(name="DTOStatkueZulaessigesFach.gueltigvon.multiple", query="SELECT e FROM DTOStatkueZulaessigesFach e WHERE e.gueltigVon IN :value")
@NamedQuery(name="DTOStatkueZulaessigesFach.gueltigbis", query="SELECT e FROM DTOStatkueZulaessigesFach e WHERE e.gueltigBis = :value")
@NamedQuery(name="DTOStatkueZulaessigesFach.gueltigbis.multiple", query="SELECT e FROM DTOStatkueZulaessigesFach e WHERE e.gueltigBis IN :value")
@NamedQuery(name="DTOStatkueZulaessigesFach.primaryKeyQuery", query="SELECT e FROM DTOStatkueZulaessigesFach e WHERE e.Schulform = ?1 AND e.BG = ?2 AND e.Fach = ?3 AND e.Bezeichnung = ?4 AND e.Flag = ?5")
@JsonPropertyOrder({"Schulform","FSP","BG","Fach","Bezeichnung","KZ_Bereich","Flag","Sortierung","Sortierung2","geaendert","gueltigVon","gueltigBis"})
public class DTOStatkueZulaessigesFach {

	/** Die Schulform bei der das Fach zulässig ist. */
	@Id
	@Column(name = "Schulform")
	@JsonProperty
	public String Schulform;

	/** Eine Einschränkung der Zulässigkeit des Faches auf einen Förderschwerpunkt (hier nur Kompatibilität angegeben) */
	@Column(name = "FSP")
	@JsonProperty
	public String FSP;

	/** Eine Einschränkung der Zulässigkeit des Faches auf einen Bildungsgang */
	@Id
	@Column(name = "BG")
	@JsonProperty
	public String BG;

	/** Das Kürzel für die amtliche Schulstatistik */
	@Id
	@Column(name = "Fach")
	@JsonProperty
	public String Fach;

	/** Die Bezeichnung des Faches */
	@Id
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Zur Kompatibilität vorhanden */
	@Column(name = "KZ_Bereich")
	@JsonProperty
	public Integer KZ_Bereich;

	/** Flag - zur Kompatibilität (hier 1) */
	@Id
	@Column(name = "Flag")
	@JsonProperty
	public String Flag;

	/** Zur Kompatibilität vorhanden */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Zur Kompatibilität vorhanden */
	@Column(name = "Sortierung2")
	@JsonProperty
	public Integer Sortierung2;

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
	 * Erstellt ein neues Objekt der Klasse DTOStatkueZulaessigesFach ohne eine Initialisierung der Attribute.
	 */
	private DTOStatkueZulaessigesFach() {
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOStatkueZulaessigesFach other = (DTOStatkueZulaessigesFach) obj;
		if (Schulform == null) {
			if (other.Schulform != null)
				return false;
		} else if (!Schulform.equals(other.Schulform))
			return false;

		if (BG == null) {
			if (other.BG != null)
				return false;
		} else if (!BG.equals(other.BG))
			return false;

		if (Fach == null) {
			if (other.Fach != null)
				return false;
		} else if (!Fach.equals(other.Fach))
			return false;

		if (Bezeichnung == null) {
			if (other.Bezeichnung != null)
				return false;
		} else if (!Bezeichnung.equals(other.Bezeichnung))
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
		result = prime * result + ((Schulform == null) ? 0 : Schulform.hashCode());

		result = prime * result + ((BG == null) ? 0 : BG.hashCode());

		result = prime * result + ((Fach == null) ? 0 : Fach.hashCode());

		result = prime * result + ((Bezeichnung == null) ? 0 : Bezeichnung.hashCode());

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
		return "DTOStatkueZulaessigesFach(Schulform=" + this.Schulform + ", FSP=" + this.FSP + ", BG=" + this.BG + ", Fach=" + this.Fach + ", Bezeichnung=" + this.Bezeichnung + ", KZ_Bereich=" + this.KZ_Bereich + ", Flag=" + this.Flag + ", Sortierung=" + this.Sortierung + ", Sortierung2=" + this.Sortierung2 + ", geaendert=" + this.geaendert + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ")";
	}

}