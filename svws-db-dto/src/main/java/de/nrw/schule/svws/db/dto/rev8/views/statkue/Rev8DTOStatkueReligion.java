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
 * Diese Klasse dient als DTO für die Datenbank-View Statkue_Religionen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Statkue_Religionen")
@NamedQuery(name="Rev8DTOStatkueReligion.all", query="SELECT e FROM Rev8DTOStatkueReligion e")
@NamedQuery(name="Rev8DTOStatkueReligion.schluessel", query="SELECT e FROM Rev8DTOStatkueReligion e WHERE e.Schluessel = :value")
@NamedQuery(name="Rev8DTOStatkueReligion.schluessel.multiple", query="SELECT e FROM Rev8DTOStatkueReligion e WHERE e.Schluessel IN :value")
@NamedQuery(name="Rev8DTOStatkueReligion.klartext", query="SELECT e FROM Rev8DTOStatkueReligion e WHERE e.Klartext = :value")
@NamedQuery(name="Rev8DTOStatkueReligion.klartext.multiple", query="SELECT e FROM Rev8DTOStatkueReligion e WHERE e.Klartext IN :value")
@NamedQuery(name="Rev8DTOStatkueReligion.geaendert", query="SELECT e FROM Rev8DTOStatkueReligion e WHERE e.geaendert = :value")
@NamedQuery(name="Rev8DTOStatkueReligion.geaendert.multiple", query="SELECT e FROM Rev8DTOStatkueReligion e WHERE e.geaendert IN :value")
@NamedQuery(name="Rev8DTOStatkueReligion.gueltigvon", query="SELECT e FROM Rev8DTOStatkueReligion e WHERE e.gueltigVon = :value")
@NamedQuery(name="Rev8DTOStatkueReligion.gueltigvon.multiple", query="SELECT e FROM Rev8DTOStatkueReligion e WHERE e.gueltigVon IN :value")
@NamedQuery(name="Rev8DTOStatkueReligion.gueltigbis", query="SELECT e FROM Rev8DTOStatkueReligion e WHERE e.gueltigBis = :value")
@NamedQuery(name="Rev8DTOStatkueReligion.gueltigbis.multiple", query="SELECT e FROM Rev8DTOStatkueReligion e WHERE e.gueltigBis IN :value")
@NamedQuery(name="Rev8DTOStatkueReligion.primaryKeyQuery", query="SELECT e FROM Rev8DTOStatkueReligion e WHERE e.Schluessel = ?1")
@JsonPropertyOrder({"Schluessel","Klartext","geaendert","gueltigVon","gueltigBis"})
public class Rev8DTOStatkueReligion {

	/** Das Kürzel der Religion bzw. Konfession */
	@Id
	@Column(name = "Schluessel")
	@JsonProperty
	public String Schluessel;

	/** Die Beschreibung der Religion bzw. Konfession */
	@Column(name = "Klartext")
	@JsonProperty
	public String Klartext;

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
	 * Erstellt ein neues Objekt der Klasse Rev8DTOStatkueReligion ohne eine Initialisierung der Attribute.
	 */
	private Rev8DTOStatkueReligion() {
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev8DTOStatkueReligion other = (Rev8DTOStatkueReligion) obj;
		if (Schluessel == null) {
			if (other.Schluessel != null)
				return false;
		} else if (!Schluessel.equals(other.Schluessel))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Schluessel == null) ? 0 : Schluessel.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "Rev8DTOStatkueReligion(Schluessel=" + this.Schluessel + ", Klartext=" + this.Klartext + ", geaendert=" + this.geaendert + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ")";
	}

}