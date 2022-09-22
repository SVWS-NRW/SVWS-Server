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
 * Diese Klasse dient als DTO für die Datenbank-View Statkue_Schulformen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(Rev8DTOStatkueSchulformenPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Statkue_Schulformen")
@NamedQuery(name="Rev8DTOStatkueSchulformen.all", query="SELECT e FROM Rev8DTOStatkueSchulformen e")
@NamedQuery(name="Rev8DTOStatkueSchulformen.schulform", query="SELECT e FROM Rev8DTOStatkueSchulformen e WHERE e.Schulform = :value")
@NamedQuery(name="Rev8DTOStatkueSchulformen.schulform.multiple", query="SELECT e FROM Rev8DTOStatkueSchulformen e WHERE e.Schulform IN :value")
@NamedQuery(name="Rev8DTOStatkueSchulformen.sf", query="SELECT e FROM Rev8DTOStatkueSchulformen e WHERE e.SF = :value")
@NamedQuery(name="Rev8DTOStatkueSchulformen.sf.multiple", query="SELECT e FROM Rev8DTOStatkueSchulformen e WHERE e.SF IN :value")
@NamedQuery(name="Rev8DTOStatkueSchulformen.bezeichnung", query="SELECT e FROM Rev8DTOStatkueSchulformen e WHERE e.Bezeichnung = :value")
@NamedQuery(name="Rev8DTOStatkueSchulformen.bezeichnung.multiple", query="SELECT e FROM Rev8DTOStatkueSchulformen e WHERE e.Bezeichnung IN :value")
@NamedQuery(name="Rev8DTOStatkueSchulformen.flag", query="SELECT e FROM Rev8DTOStatkueSchulformen e WHERE e.Flag = :value")
@NamedQuery(name="Rev8DTOStatkueSchulformen.flag.multiple", query="SELECT e FROM Rev8DTOStatkueSchulformen e WHERE e.Flag IN :value")
@NamedQuery(name="Rev8DTOStatkueSchulformen.geaendert", query="SELECT e FROM Rev8DTOStatkueSchulformen e WHERE e.geaendert = :value")
@NamedQuery(name="Rev8DTOStatkueSchulformen.geaendert.multiple", query="SELECT e FROM Rev8DTOStatkueSchulformen e WHERE e.geaendert IN :value")
@NamedQuery(name="Rev8DTOStatkueSchulformen.sortierung", query="SELECT e FROM Rev8DTOStatkueSchulformen e WHERE e.Sortierung = :value")
@NamedQuery(name="Rev8DTOStatkueSchulformen.sortierung.multiple", query="SELECT e FROM Rev8DTOStatkueSchulformen e WHERE e.Sortierung IN :value")
@NamedQuery(name="Rev8DTOStatkueSchulformen.gueltigvon", query="SELECT e FROM Rev8DTOStatkueSchulformen e WHERE e.gueltigVon = :value")
@NamedQuery(name="Rev8DTOStatkueSchulformen.gueltigvon.multiple", query="SELECT e FROM Rev8DTOStatkueSchulformen e WHERE e.gueltigVon IN :value")
@NamedQuery(name="Rev8DTOStatkueSchulformen.gueltigbis", query="SELECT e FROM Rev8DTOStatkueSchulformen e WHERE e.gueltigBis = :value")
@NamedQuery(name="Rev8DTOStatkueSchulformen.gueltigbis.multiple", query="SELECT e FROM Rev8DTOStatkueSchulformen e WHERE e.gueltigBis IN :value")
@NamedQuery(name="Rev8DTOStatkueSchulformen.primaryKeyQuery", query="SELECT e FROM Rev8DTOStatkueSchulformen e WHERE e.SF = ?1 AND e.Bezeichnung = ?2")
@JsonPropertyOrder({"Schulform","SF","Bezeichnung","Flag","geaendert","Sortierung","gueltigVon","gueltigBis"})
public class Rev8DTOStatkueSchulformen {

	/** Eindeutige Nummer der Schulform */
	@Column(name = "Schulform")
	@JsonProperty
	public String Schulform;

	/** Das Kürzel der Schulform */
	@Id
	@Column(name = "SF")
	@JsonProperty
	public String SF;

	/** Die Bezeichnung der Schulform */
	@Id
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Flag zur Kompatibilität zur Schulver */
	@Column(name = "Flag")
	@JsonProperty
	public String Flag;

	/** Das Datumd er letzten Änderung */
	@Column(name = "geaendert")
	@JsonProperty
	public String geaendert;

	/** Gibt die Sortierreihenfolge an */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Gibt das Schuljahr an, ab dem die Schulform verwendet werden kann oder null, falls es keine Einschränkung gibt */
	@Column(name = "gueltigVon")
	@JsonProperty
	public Integer gueltigVon;

	/** Gibt das Schuljahr an, bis zu welchem die Schulform verwendet werden kann oder null, falls es keine Einschränkung gibt */
	@Column(name = "gueltigBis")
	@JsonProperty
	public Integer gueltigBis;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOStatkueSchulformen ohne eine Initialisierung der Attribute.
	 */
	private Rev8DTOStatkueSchulformen() {
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev8DTOStatkueSchulformen other = (Rev8DTOStatkueSchulformen) obj;
		if (SF == null) {
			if (other.SF != null)
				return false;
		} else if (!SF.equals(other.SF))
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
		result = prime * result + ((SF == null) ? 0 : SF.hashCode());

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
		return "Rev8DTOStatkueSchulformen(Schulform=" + this.Schulform + ", SF=" + this.SF + ", Bezeichnung=" + this.Bezeichnung + ", Flag=" + this.Flag + ", geaendert=" + this.geaendert + ", Sortierung=" + this.Sortierung + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ")";
	}

}