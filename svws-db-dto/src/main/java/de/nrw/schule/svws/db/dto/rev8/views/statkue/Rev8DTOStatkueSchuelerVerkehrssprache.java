package de.nrw.schule.svws.db.dto.rev8.views.statkue;

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
 * Diese Klasse dient als DTO für die Datenbank-View Statkue_SchuelerVerkehrssprache.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Statkue_SchuelerVerkehrssprache")
@NamedQuery(name="Rev8DTOStatkueSchuelerVerkehrssprache.all", query="SELECT e FROM Rev8DTOStatkueSchuelerVerkehrssprache e")
@NamedQuery(name="Rev8DTOStatkueSchuelerVerkehrssprache.id", query="SELECT e FROM Rev8DTOStatkueSchuelerVerkehrssprache e WHERE e.ID = :value")
@NamedQuery(name="Rev8DTOStatkueSchuelerVerkehrssprache.id.multiple", query="SELECT e FROM Rev8DTOStatkueSchuelerVerkehrssprache e WHERE e.ID IN :value")
@NamedQuery(name="Rev8DTOStatkueSchuelerVerkehrssprache.kurztext", query="SELECT e FROM Rev8DTOStatkueSchuelerVerkehrssprache e WHERE e.Kurztext = :value")
@NamedQuery(name="Rev8DTOStatkueSchuelerVerkehrssprache.kurztext.multiple", query="SELECT e FROM Rev8DTOStatkueSchuelerVerkehrssprache e WHERE e.Kurztext IN :value")
@NamedQuery(name="Rev8DTOStatkueSchuelerVerkehrssprache.langtext", query="SELECT e FROM Rev8DTOStatkueSchuelerVerkehrssprache e WHERE e.Langtext = :value")
@NamedQuery(name="Rev8DTOStatkueSchuelerVerkehrssprache.langtext.multiple", query="SELECT e FROM Rev8DTOStatkueSchuelerVerkehrssprache e WHERE e.Langtext IN :value")
@NamedQuery(name="Rev8DTOStatkueSchuelerVerkehrssprache.gesprochen_in", query="SELECT e FROM Rev8DTOStatkueSchuelerVerkehrssprache e WHERE e.Gesprochen_in = :value")
@NamedQuery(name="Rev8DTOStatkueSchuelerVerkehrssprache.gesprochen_in.multiple", query="SELECT e FROM Rev8DTOStatkueSchuelerVerkehrssprache e WHERE e.Gesprochen_in IN :value")
@NamedQuery(name="Rev8DTOStatkueSchuelerVerkehrssprache.beginn", query="SELECT e FROM Rev8DTOStatkueSchuelerVerkehrssprache e WHERE e.Beginn = :value")
@NamedQuery(name="Rev8DTOStatkueSchuelerVerkehrssprache.beginn.multiple", query="SELECT e FROM Rev8DTOStatkueSchuelerVerkehrssprache e WHERE e.Beginn IN :value")
@NamedQuery(name="Rev8DTOStatkueSchuelerVerkehrssprache.ende", query="SELECT e FROM Rev8DTOStatkueSchuelerVerkehrssprache e WHERE e.Ende = :value")
@NamedQuery(name="Rev8DTOStatkueSchuelerVerkehrssprache.ende.multiple", query="SELECT e FROM Rev8DTOStatkueSchuelerVerkehrssprache e WHERE e.Ende IN :value")
@NamedQuery(name="Rev8DTOStatkueSchuelerVerkehrssprache.gueltigvon", query="SELECT e FROM Rev8DTOStatkueSchuelerVerkehrssprache e WHERE e.gueltigVon = :value")
@NamedQuery(name="Rev8DTOStatkueSchuelerVerkehrssprache.gueltigvon.multiple", query="SELECT e FROM Rev8DTOStatkueSchuelerVerkehrssprache e WHERE e.gueltigVon IN :value")
@NamedQuery(name="Rev8DTOStatkueSchuelerVerkehrssprache.gueltigbis", query="SELECT e FROM Rev8DTOStatkueSchuelerVerkehrssprache e WHERE e.gueltigBis = :value")
@NamedQuery(name="Rev8DTOStatkueSchuelerVerkehrssprache.gueltigbis.multiple", query="SELECT e FROM Rev8DTOStatkueSchuelerVerkehrssprache e WHERE e.gueltigBis IN :value")
@NamedQuery(name="Rev8DTOStatkueSchuelerVerkehrssprache.primaryKeyQuery", query="SELECT e FROM Rev8DTOStatkueSchuelerVerkehrssprache e WHERE e.ID = ?1")
@JsonPropertyOrder({"ID","Kurztext","Langtext","Gesprochen_in","Beginn","Ende","gueltigVon","gueltigBis"})
public class Rev8DTOStatkueSchuelerVerkehrssprache {

	/** Die ID der Verkehrssprache */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Integer ID;

	/** Das Kürzel der Verkehrssprache */
	@Column(name = "Kurztext")
	@JsonProperty
	public String Kurztext;

	/** Die Bezeichnung der Verkehrssprache */
	@Column(name = "Langtext")
	@JsonProperty
	public String Langtext;

	/** Die Länder, in denen die Sprache gesprochen wird */
	@Column(name = "Gesprochen_in")
	@JsonProperty
	public String Gesprochen_in;

	/** Beginn der Gültigkeit (hier zur Kompatibilität vorhanden) */
	@Column(name = "Beginn")
	@JsonProperty
	public String Beginn;

	/** Ende der Gültigkeit (hier zur Kompatibilität vorhanden) */
	@Column(name = "Ende")
	@JsonProperty
	public String Ende;

	/** Gibt die Gültigkeit ab welchem Schuljahr an (hier zur Kompatibilität vorhanden) */
	@Column(name = "gueltigVon")
	@JsonProperty
	public String gueltigVon;

	/** Gibt die Gültigkeit bis zu welchem Schuljahr an (hier zur Kompatibilität vorhanden) */
	@Column(name = "gueltigBis")
	@JsonProperty
	public String gueltigBis;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOStatkueSchuelerVerkehrssprache ohne eine Initialisierung der Attribute.
	 */
	private Rev8DTOStatkueSchuelerVerkehrssprache() {
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev8DTOStatkueSchuelerVerkehrssprache other = (Rev8DTOStatkueSchuelerVerkehrssprache) obj;
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
		return "Rev8DTOStatkueSchuelerVerkehrssprache(ID=" + this.ID + ", Kurztext=" + this.Kurztext + ", Langtext=" + this.Langtext + ", Gesprochen_in=" + this.Gesprochen_in + ", Beginn=" + this.Beginn + ", Ende=" + this.Ende + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ")";
	}

}