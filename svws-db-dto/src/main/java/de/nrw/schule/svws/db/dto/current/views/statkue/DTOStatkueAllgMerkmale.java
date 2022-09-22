package de.nrw.schule.svws.db.dto.current.views.statkue;

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
 * Diese Klasse dient als DTO für die Datenbank-View Statkue_AllgMerkmale.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Statkue_AllgMerkmale")
@NamedQuery(name="DTOStatkueAllgMerkmale.all", query="SELECT e FROM DTOStatkueAllgMerkmale e")
@NamedQuery(name="DTOStatkueAllgMerkmale.id", query="SELECT e FROM DTOStatkueAllgMerkmale e WHERE e.ID = :value")
@NamedQuery(name="DTOStatkueAllgMerkmale.id.multiple", query="SELECT e FROM DTOStatkueAllgMerkmale e WHERE e.ID IN :value")
@NamedQuery(name="DTOStatkueAllgMerkmale.sf", query="SELECT e FROM DTOStatkueAllgMerkmale e WHERE e.SF = :value")
@NamedQuery(name="DTOStatkueAllgMerkmale.sf.multiple", query="SELECT e FROM DTOStatkueAllgMerkmale e WHERE e.SF IN :value")
@NamedQuery(name="DTOStatkueAllgMerkmale.kurztext", query="SELECT e FROM DTOStatkueAllgMerkmale e WHERE e.Kurztext = :value")
@NamedQuery(name="DTOStatkueAllgMerkmale.kurztext.multiple", query="SELECT e FROM DTOStatkueAllgMerkmale e WHERE e.Kurztext IN :value")
@NamedQuery(name="DTOStatkueAllgMerkmale.statistikkuerzel", query="SELECT e FROM DTOStatkueAllgMerkmale e WHERE e.StatistikKuerzel = :value")
@NamedQuery(name="DTOStatkueAllgMerkmale.statistikkuerzel.multiple", query="SELECT e FROM DTOStatkueAllgMerkmale e WHERE e.StatistikKuerzel IN :value")
@NamedQuery(name="DTOStatkueAllgMerkmale.langtext", query="SELECT e FROM DTOStatkueAllgMerkmale e WHERE e.Langtext = :value")
@NamedQuery(name="DTOStatkueAllgMerkmale.langtext.multiple", query="SELECT e FROM DTOStatkueAllgMerkmale e WHERE e.Langtext IN :value")
@NamedQuery(name="DTOStatkueAllgMerkmale.schule", query="SELECT e FROM DTOStatkueAllgMerkmale e WHERE e.Schule = :value")
@NamedQuery(name="DTOStatkueAllgMerkmale.schule.multiple", query="SELECT e FROM DTOStatkueAllgMerkmale e WHERE e.Schule IN :value")
@NamedQuery(name="DTOStatkueAllgMerkmale.schueler", query="SELECT e FROM DTOStatkueAllgMerkmale e WHERE e.Schueler = :value")
@NamedQuery(name="DTOStatkueAllgMerkmale.schueler.multiple", query="SELECT e FROM DTOStatkueAllgMerkmale e WHERE e.Schueler IN :value")
@NamedQuery(name="DTOStatkueAllgMerkmale.beginn", query="SELECT e FROM DTOStatkueAllgMerkmale e WHERE e.Beginn = :value")
@NamedQuery(name="DTOStatkueAllgMerkmale.beginn.multiple", query="SELECT e FROM DTOStatkueAllgMerkmale e WHERE e.Beginn IN :value")
@NamedQuery(name="DTOStatkueAllgMerkmale.ende", query="SELECT e FROM DTOStatkueAllgMerkmale e WHERE e.Ende = :value")
@NamedQuery(name="DTOStatkueAllgMerkmale.ende.multiple", query="SELECT e FROM DTOStatkueAllgMerkmale e WHERE e.Ende IN :value")
@NamedQuery(name="DTOStatkueAllgMerkmale.sort", query="SELECT e FROM DTOStatkueAllgMerkmale e WHERE e.Sort = :value")
@NamedQuery(name="DTOStatkueAllgMerkmale.sort.multiple", query="SELECT e FROM DTOStatkueAllgMerkmale e WHERE e.Sort IN :value")
@NamedQuery(name="DTOStatkueAllgMerkmale.gueltigvon", query="SELECT e FROM DTOStatkueAllgMerkmale e WHERE e.gueltigVon = :value")
@NamedQuery(name="DTOStatkueAllgMerkmale.gueltigvon.multiple", query="SELECT e FROM DTOStatkueAllgMerkmale e WHERE e.gueltigVon IN :value")
@NamedQuery(name="DTOStatkueAllgMerkmale.gueltigbis", query="SELECT e FROM DTOStatkueAllgMerkmale e WHERE e.gueltigBis = :value")
@NamedQuery(name="DTOStatkueAllgMerkmale.gueltigbis.multiple", query="SELECT e FROM DTOStatkueAllgMerkmale e WHERE e.gueltigBis IN :value")
@NamedQuery(name="DTOStatkueAllgMerkmale.primaryKeyQuery", query="SELECT e FROM DTOStatkueAllgMerkmale e WHERE e.ID = ?1")
@JsonPropertyOrder({"ID","SF","Kurztext","StatistikKuerzel","Langtext","Schule","Schueler","Beginn","Ende","Sort","gueltigVon","gueltigBis"})
public class DTOStatkueAllgMerkmale {

	/** Die ID des Merkmals. */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public String ID;

	/** Die Schulform für welche das Merkmal zur Verfügung steht */
	@Column(name = "SF")
	@JsonProperty
	public String SF;

	/** Das Kürzel des Merkmals */
	@Column(name = "Kurztext")
	@JsonProperty
	public String Kurztext;

	/** Ggf. das Statistikkürzel des Merkmals */
	@Column(name = "StatistikKuerzel")
	@JsonProperty
	public String StatistikKuerzel;

	/** Die Beschreibung des Merkmals */
	@Column(name = "Langtext")
	@JsonProperty
	public String Langtext;

	/** Gibt an, ob das Merkmal bei einer Schule gesetzt werden kann */
	@Column(name = "Schule")
	@JsonProperty
	public Integer Schule;

	/** Gibt an, ob das Merkmal bei einem Schüler gesetzt werden kann */
	@Column(name = "Schueler")
	@JsonProperty
	public Integer Schueler;

	/** Zur Kompatibilität vorhanden */
	@Column(name = "Beginn")
	@JsonProperty
	public String Beginn;

	/** Zur Kompatibilität vorhanden */
	@Column(name = "Ende")
	@JsonProperty
	public String Ende;

	/** Zur Kompatibilität vorhanden */
	@Column(name = "Sort")
	@JsonProperty
	public Integer Sort;

	/** Gibt die Gültigkeit ab welchem Schuljahr an */
	@Column(name = "gueltigVon")
	@JsonProperty
	public String gueltigVon;

	/** Gibt die Gültigkeit bis zu welchem Schuljahr an */
	@Column(name = "gueltigBis")
	@JsonProperty
	public String gueltigBis;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOStatkueAllgMerkmale ohne eine Initialisierung der Attribute.
	 */
	private DTOStatkueAllgMerkmale() {
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOStatkueAllgMerkmale other = (DTOStatkueAllgMerkmale) obj;
		if (ID == null) {
			if (other.ID != null)
				return false;
		} else if (!ID.equals(other.ID))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ID == null) ? 0 : ID.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOStatkueAllgMerkmale(ID=" + this.ID + ", SF=" + this.SF + ", Kurztext=" + this.Kurztext + ", StatistikKuerzel=" + this.StatistikKuerzel + ", Langtext=" + this.Langtext + ", Schule=" + this.Schule + ", Schueler=" + this.Schueler + ", Beginn=" + this.Beginn + ", Ende=" + this.Ende + ", Sort=" + this.Sort + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ")";
	}

}