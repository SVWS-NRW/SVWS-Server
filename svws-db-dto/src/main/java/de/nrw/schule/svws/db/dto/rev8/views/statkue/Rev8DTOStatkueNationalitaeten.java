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
 * Diese Klasse dient als DTO für die Datenbank-View Statkue_Nationalitaeten.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Statkue_Nationalitaeten")
@NamedQuery(name="Rev8DTOStatkueNationalitaeten.all", query="SELECT e FROM Rev8DTOStatkueNationalitaeten e")
@NamedQuery(name="Rev8DTOStatkueNationalitaeten.schluessel", query="SELECT e FROM Rev8DTOStatkueNationalitaeten e WHERE e.Schluessel = :value")
@NamedQuery(name="Rev8DTOStatkueNationalitaeten.schluessel.multiple", query="SELECT e FROM Rev8DTOStatkueNationalitaeten e WHERE e.Schluessel IN :value")
@NamedQuery(name="Rev8DTOStatkueNationalitaeten.land", query="SELECT e FROM Rev8DTOStatkueNationalitaeten e WHERE e.Land = :value")
@NamedQuery(name="Rev8DTOStatkueNationalitaeten.land.multiple", query="SELECT e FROM Rev8DTOStatkueNationalitaeten e WHERE e.Land IN :value")
@NamedQuery(name="Rev8DTOStatkueNationalitaeten.nationalitaet", query="SELECT e FROM Rev8DTOStatkueNationalitaeten e WHERE e.Nationalitaet = :value")
@NamedQuery(name="Rev8DTOStatkueNationalitaeten.nationalitaet.multiple", query="SELECT e FROM Rev8DTOStatkueNationalitaeten e WHERE e.Nationalitaet IN :value")
@NamedQuery(name="Rev8DTOStatkueNationalitaeten.flag", query="SELECT e FROM Rev8DTOStatkueNationalitaeten e WHERE e.Flag = :value")
@NamedQuery(name="Rev8DTOStatkueNationalitaeten.flag.multiple", query="SELECT e FROM Rev8DTOStatkueNationalitaeten e WHERE e.Flag IN :value")
@NamedQuery(name="Rev8DTOStatkueNationalitaeten.geaendert", query="SELECT e FROM Rev8DTOStatkueNationalitaeten e WHERE e.geaendert = :value")
@NamedQuery(name="Rev8DTOStatkueNationalitaeten.geaendert.multiple", query="SELECT e FROM Rev8DTOStatkueNationalitaeten e WHERE e.geaendert IN :value")
@NamedQuery(name="Rev8DTOStatkueNationalitaeten.beginn", query="SELECT e FROM Rev8DTOStatkueNationalitaeten e WHERE e.Beginn = :value")
@NamedQuery(name="Rev8DTOStatkueNationalitaeten.beginn.multiple", query="SELECT e FROM Rev8DTOStatkueNationalitaeten e WHERE e.Beginn IN :value")
@NamedQuery(name="Rev8DTOStatkueNationalitaeten.ende", query="SELECT e FROM Rev8DTOStatkueNationalitaeten e WHERE e.Ende = :value")
@NamedQuery(name="Rev8DTOStatkueNationalitaeten.ende.multiple", query="SELECT e FROM Rev8DTOStatkueNationalitaeten e WHERE e.Ende IN :value")
@NamedQuery(name="Rev8DTOStatkueNationalitaeten.gueltigvon", query="SELECT e FROM Rev8DTOStatkueNationalitaeten e WHERE e.gueltigVon = :value")
@NamedQuery(name="Rev8DTOStatkueNationalitaeten.gueltigvon.multiple", query="SELECT e FROM Rev8DTOStatkueNationalitaeten e WHERE e.gueltigVon IN :value")
@NamedQuery(name="Rev8DTOStatkueNationalitaeten.gueltigbis", query="SELECT e FROM Rev8DTOStatkueNationalitaeten e WHERE e.gueltigBis = :value")
@NamedQuery(name="Rev8DTOStatkueNationalitaeten.gueltigbis.multiple", query="SELECT e FROM Rev8DTOStatkueNationalitaeten e WHERE e.gueltigBis IN :value")
@NamedQuery(name="Rev8DTOStatkueNationalitaeten.primaryKeyQuery", query="SELECT e FROM Rev8DTOStatkueNationalitaeten e WHERE e.Schluessel = ?1")
@JsonPropertyOrder({"Schluessel","Land","Nationalitaet","Flag","geaendert","Beginn","Ende","gueltigVon","gueltigBis"})
public class Rev8DTOStatkueNationalitaeten {

	/** Der Statisik-Schlüssel (DESTATIS) */
	@Id
	@Column(name = "Schluessel")
	@JsonProperty
	public String Schluessel;

	/** Die Bezeichnung des Landes */
	@Column(name = "Land")
	@JsonProperty
	public String Land;

	/** Die Bezeichnung der Staatsangehörigkeit */
	@Column(name = "Nationalitaet")
	@JsonProperty
	public String Nationalitaet;

	/** Simulation des Flags aus der Statkue */
	@Column(name = "Flag")
	@JsonProperty
	public String Flag;

	/** Datum der letzten Änderung (hier zur Kompatibilität vorhanden) */
	@Column(name = "geaendert")
	@JsonProperty
	public String geaendert;

	/** Beginn der Gültigkeit (hier zur Kompatibilität vorhanden) */
	@Column(name = "Beginn")
	@JsonProperty
	public String Beginn;

	/** Ende der Gültigkeit (hier zur Kompatibilität vorhanden) */
	@Column(name = "Ende")
	@JsonProperty
	public String Ende;

	/** Gibt die Gültigkeit ab welchem Schuljahr an */
	@Column(name = "gueltigVon")
	@JsonProperty
	public String gueltigVon;

	/** Gibt die Gültigkeit bis zu welchem Schuljahr an */
	@Column(name = "gueltigBis")
	@JsonProperty
	public String gueltigBis;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOStatkueNationalitaeten ohne eine Initialisierung der Attribute.
	 */
	private Rev8DTOStatkueNationalitaeten() {
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev8DTOStatkueNationalitaeten other = (Rev8DTOStatkueNationalitaeten) obj;
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
		return "Rev8DTOStatkueNationalitaeten(Schluessel=" + this.Schluessel + ", Land=" + this.Land + ", Nationalitaet=" + this.Nationalitaet + ", Flag=" + this.Flag + ", geaendert=" + this.geaendert + ", Beginn=" + this.Beginn + ", Ende=" + this.Ende + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ")";
	}

}