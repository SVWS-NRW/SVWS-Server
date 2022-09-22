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
 * Diese Klasse dient als DTO für die Datenbank-View Statkue_ZulKuArt.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(DTOStatkueZulaessigeKursartPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Statkue_ZulKuArt")
@NamedQuery(name="DTOStatkueZulaessigeKursart.all", query="SELECT e FROM DTOStatkueZulaessigeKursart e")
@NamedQuery(name="DTOStatkueZulaessigeKursart.sf", query="SELECT e FROM DTOStatkueZulaessigeKursart e WHERE e.SF = :value")
@NamedQuery(name="DTOStatkueZulaessigeKursart.sf.multiple", query="SELECT e FROM DTOStatkueZulaessigeKursart e WHERE e.SF IN :value")
@NamedQuery(name="DTOStatkueZulaessigeKursart.fsp", query="SELECT e FROM DTOStatkueZulaessigeKursart e WHERE e.FSP = :value")
@NamedQuery(name="DTOStatkueZulaessigeKursart.fsp.multiple", query="SELECT e FROM DTOStatkueZulaessigeKursart e WHERE e.FSP IN :value")
@NamedQuery(name="DTOStatkueZulaessigeKursart.bg", query="SELECT e FROM DTOStatkueZulaessigeKursart e WHERE e.BG = :value")
@NamedQuery(name="DTOStatkueZulaessigeKursart.bg.multiple", query="SELECT e FROM DTOStatkueZulaessigeKursart e WHERE e.BG IN :value")
@NamedQuery(name="DTOStatkueZulaessigeKursart.kursart", query="SELECT e FROM DTOStatkueZulaessigeKursart e WHERE e.Kursart = :value")
@NamedQuery(name="DTOStatkueZulaessigeKursart.kursart.multiple", query="SELECT e FROM DTOStatkueZulaessigeKursart e WHERE e.Kursart IN :value")
@NamedQuery(name="DTOStatkueZulaessigeKursart.kursart2", query="SELECT e FROM DTOStatkueZulaessigeKursart e WHERE e.Kursart2 = :value")
@NamedQuery(name="DTOStatkueZulaessigeKursart.kursart2.multiple", query="SELECT e FROM DTOStatkueZulaessigeKursart e WHERE e.Kursart2 IN :value")
@NamedQuery(name="DTOStatkueZulaessigeKursart.bezeichnung", query="SELECT e FROM DTOStatkueZulaessigeKursart e WHERE e.Bezeichnung = :value")
@NamedQuery(name="DTOStatkueZulaessigeKursart.bezeichnung.multiple", query="SELECT e FROM DTOStatkueZulaessigeKursart e WHERE e.Bezeichnung IN :value")
@NamedQuery(name="DTOStatkueZulaessigeKursart.sglbereich", query="SELECT e FROM DTOStatkueZulaessigeKursart e WHERE e.SGLBereich = :value")
@NamedQuery(name="DTOStatkueZulaessigeKursart.sglbereich.multiple", query="SELECT e FROM DTOStatkueZulaessigeKursart e WHERE e.SGLBereich IN :value")
@NamedQuery(name="DTOStatkueZulaessigeKursart.flag", query="SELECT e FROM DTOStatkueZulaessigeKursart e WHERE e.Flag = :value")
@NamedQuery(name="DTOStatkueZulaessigeKursart.flag.multiple", query="SELECT e FROM DTOStatkueZulaessigeKursart e WHERE e.Flag IN :value")
@NamedQuery(name="DTOStatkueZulaessigeKursart.geaendert", query="SELECT e FROM DTOStatkueZulaessigeKursart e WHERE e.geaendert = :value")
@NamedQuery(name="DTOStatkueZulaessigeKursart.geaendert.multiple", query="SELECT e FROM DTOStatkueZulaessigeKursart e WHERE e.geaendert IN :value")
@NamedQuery(name="DTOStatkueZulaessigeKursart.gueltigvon", query="SELECT e FROM DTOStatkueZulaessigeKursart e WHERE e.gueltigVon = :value")
@NamedQuery(name="DTOStatkueZulaessigeKursart.gueltigvon.multiple", query="SELECT e FROM DTOStatkueZulaessigeKursart e WHERE e.gueltigVon IN :value")
@NamedQuery(name="DTOStatkueZulaessigeKursart.gueltigbis", query="SELECT e FROM DTOStatkueZulaessigeKursart e WHERE e.gueltigBis = :value")
@NamedQuery(name="DTOStatkueZulaessigeKursart.gueltigbis.multiple", query="SELECT e FROM DTOStatkueZulaessigeKursart e WHERE e.gueltigBis IN :value")
@NamedQuery(name="DTOStatkueZulaessigeKursart.primaryKeyQuery", query="SELECT e FROM DTOStatkueZulaessigeKursart e WHERE e.SF = ?1 AND e.FSP = ?2 AND e.BG = ?3 AND e.Kursart = ?4 AND e.Kursart2 = ?5 AND e.Bezeichnung = ?6 AND e.SGLBereich = ?7 AND e.Flag = ?8")
@JsonPropertyOrder({"SF","FSP","BG","Kursart","Kursart2","Bezeichnung","SGLBereich","Flag","geaendert","gueltigVon","gueltigBis"})
public class DTOStatkueZulaessigeKursart {

	/** Die Schulform bei der die Kursart zulässig ist. */
	@Id
	@Column(name = "SF")
	@JsonProperty
	public String SF;

	/** Eine Einschränkung der Zulässigkeit der Kusart auf einen Förderschwerpunkt (hier nur Kompatibilität angegeben) */
	@Id
	@Column(name = "FSP")
	@JsonProperty
	public String FSP;

	/** Eine Einschränkung der Zulässigkeit der Kursart auf einen Bildungsgang */
	@Id
	@Column(name = "BG")
	@JsonProperty
	public String BG;

	/** Der numerische Schlüssel für die amtliche Schulstatistik */
	@Id
	@Column(name = "Kursart")
	@JsonProperty
	public String Kursart;

	/** Das Kürzel der Kursart */
	@Id
	@Column(name = "Kursart2")
	@JsonProperty
	public String Kursart2;

	/** Die Bezeichnung der Kursart */
	@Id
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Zur Kompatibilität vorhanden */
	@Id
	@Column(name = "SGLBereich")
	@JsonProperty
	public Integer SGLBereich;

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
	 * Erstellt ein neues Objekt der Klasse DTOStatkueZulaessigeKursart ohne eine Initialisierung der Attribute.
	 */
	private DTOStatkueZulaessigeKursart() {
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOStatkueZulaessigeKursart other = (DTOStatkueZulaessigeKursart) obj;
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

		if (BG == null) {
			if (other.BG != null)
				return false;
		} else if (!BG.equals(other.BG))
			return false;

		if (Kursart == null) {
			if (other.Kursart != null)
				return false;
		} else if (!Kursart.equals(other.Kursart))
			return false;

		if (Kursart2 == null) {
			if (other.Kursart2 != null)
				return false;
		} else if (!Kursart2.equals(other.Kursart2))
			return false;

		if (Bezeichnung == null) {
			if (other.Bezeichnung != null)
				return false;
		} else if (!Bezeichnung.equals(other.Bezeichnung))
			return false;

		if (SGLBereich == null) {
			if (other.SGLBereich != null)
				return false;
		} else if (!SGLBereich.equals(other.SGLBereich))
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

		result = prime * result + ((BG == null) ? 0 : BG.hashCode());

		result = prime * result + ((Kursart == null) ? 0 : Kursart.hashCode());

		result = prime * result + ((Kursart2 == null) ? 0 : Kursart2.hashCode());

		result = prime * result + ((Bezeichnung == null) ? 0 : Bezeichnung.hashCode());

		result = prime * result + ((SGLBereich == null) ? 0 : SGLBereich.hashCode());

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
		return "DTOStatkueZulaessigeKursart(SF=" + this.SF + ", FSP=" + this.FSP + ", BG=" + this.BG + ", Kursart=" + this.Kursart + ", Kursart2=" + this.Kursart2 + ", Bezeichnung=" + this.Bezeichnung + ", SGLBereich=" + this.SGLBereich + ", Flag=" + this.Flag + ", geaendert=" + this.geaendert + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ")";
	}

}