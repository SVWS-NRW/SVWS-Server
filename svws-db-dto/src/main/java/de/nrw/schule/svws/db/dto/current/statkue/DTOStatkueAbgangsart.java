package de.nrw.schule.svws.db.dto.current.statkue;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Statkue_Abgangsart.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(DTOStatkueAbgangsartPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Statkue_Abgangsart")
@NamedQuery(name="DTOStatkueAbgangsart.all", query="SELECT e FROM DTOStatkueAbgangsart e")
@NamedQuery(name="DTOStatkueAbgangsart.sf", query="SELECT e FROM DTOStatkueAbgangsart e WHERE e.SF = :value")
@NamedQuery(name="DTOStatkueAbgangsart.sf.multiple", query="SELECT e FROM DTOStatkueAbgangsart e WHERE e.SF IN :value")
@NamedQuery(name="DTOStatkueAbgangsart.art", query="SELECT e FROM DTOStatkueAbgangsart e WHERE e.Art = :value")
@NamedQuery(name="DTOStatkueAbgangsart.art.multiple", query="SELECT e FROM DTOStatkueAbgangsart e WHERE e.Art IN :value")
@NamedQuery(name="DTOStatkueAbgangsart.beschreibung", query="SELECT e FROM DTOStatkueAbgangsart e WHERE e.Beschreibung = :value")
@NamedQuery(name="DTOStatkueAbgangsart.beschreibung.multiple", query="SELECT e FROM DTOStatkueAbgangsart e WHERE e.Beschreibung IN :value")
@NamedQuery(name="DTOStatkueAbgangsart.kz_bereich", query="SELECT e FROM DTOStatkueAbgangsart e WHERE e.KZ_Bereich = :value")
@NamedQuery(name="DTOStatkueAbgangsart.kz_bereich.multiple", query="SELECT e FROM DTOStatkueAbgangsart e WHERE e.KZ_Bereich IN :value")
@NamedQuery(name="DTOStatkueAbgangsart.kz_bereich_jg", query="SELECT e FROM DTOStatkueAbgangsart e WHERE e.KZ_Bereich_JG = :value")
@NamedQuery(name="DTOStatkueAbgangsart.kz_bereich_jg.multiple", query="SELECT e FROM DTOStatkueAbgangsart e WHERE e.KZ_Bereich_JG IN :value")
@NamedQuery(name="DTOStatkueAbgangsart.abgangsjg", query="SELECT e FROM DTOStatkueAbgangsart e WHERE e.AbgangsJG = :value")
@NamedQuery(name="DTOStatkueAbgangsart.abgangsjg.multiple", query="SELECT e FROM DTOStatkueAbgangsart e WHERE e.AbgangsJG IN :value")
@NamedQuery(name="DTOStatkueAbgangsart.flag", query="SELECT e FROM DTOStatkueAbgangsart e WHERE e.Flag = :value")
@NamedQuery(name="DTOStatkueAbgangsart.flag.multiple", query="SELECT e FROM DTOStatkueAbgangsart e WHERE e.Flag IN :value")
@NamedQuery(name="DTOStatkueAbgangsart.geaendert", query="SELECT e FROM DTOStatkueAbgangsart e WHERE e.geaendert = :value")
@NamedQuery(name="DTOStatkueAbgangsart.geaendert.multiple", query="SELECT e FROM DTOStatkueAbgangsart e WHERE e.geaendert IN :value")
@NamedQuery(name="DTOStatkueAbgangsart.sortierung", query="SELECT e FROM DTOStatkueAbgangsart e WHERE e.Sortierung = :value")
@NamedQuery(name="DTOStatkueAbgangsart.sortierung.multiple", query="SELECT e FROM DTOStatkueAbgangsart e WHERE e.Sortierung IN :value")
@NamedQuery(name="DTOStatkueAbgangsart.gueltigvon", query="SELECT e FROM DTOStatkueAbgangsart e WHERE e.gueltigVon = :value")
@NamedQuery(name="DTOStatkueAbgangsart.gueltigvon.multiple", query="SELECT e FROM DTOStatkueAbgangsart e WHERE e.gueltigVon IN :value")
@NamedQuery(name="DTOStatkueAbgangsart.gueltigbis", query="SELECT e FROM DTOStatkueAbgangsart e WHERE e.gueltigBis = :value")
@NamedQuery(name="DTOStatkueAbgangsart.gueltigbis.multiple", query="SELECT e FROM DTOStatkueAbgangsart e WHERE e.gueltigBis IN :value")
@NamedQuery(name="DTOStatkueAbgangsart.primaryKeyQuery", query="SELECT e FROM DTOStatkueAbgangsart e WHERE e.AbgangsJG = ?1 AND e.Art = ?2 AND e.KZ_Bereich = ?3 AND e.KZ_Bereich_JG = ?4 AND e.SF = ?5")
@NamedQuery(name="DTOStatkueAbgangsart.all.migration", query="SELECT e FROM DTOStatkueAbgangsart e WHERE e.AbgangsJG IS NOT NULL AND e.Art IS NOT NULL AND e.KZ_Bereich IS NOT NULL AND e.KZ_Bereich_JG IS NOT NULL AND e.SF IS NOT NULL")
@JsonPropertyOrder({"SF","Art","Beschreibung","KZ_Bereich","KZ_Bereich_JG","AbgangsJG","Flag","geaendert","Sortierung","gueltigVon","gueltigBis"})
public class DTOStatkueAbgangsart {

	/** Statkue Tabelle IT.NRW: zulässige Schulform der Abgangsart */
	@Id
	@Column(name = "SF")
	@JsonProperty
	public String SF;

	/** Statkue Tabelle IT.NRW: ASD-Kürzel der Abgangsart */
	@Id
	@Column(name = "Art")
	@JsonProperty
	public String Art;

	/** Statkue Tabelle IT.NRW: Beschreibung der Abgangsart */
	@Column(name = "Beschreibung")
	@JsonProperty
	public String Beschreibung;

	/** Statkue Tabelle IT.NRW: ??? */
	@Id
	@Column(name = "KZ_Bereich")
	@JsonProperty
	public Integer KZ_Bereich;

	/** Statkue Tabelle IT.NRW: ??? */
	@Id
	@Column(name = "KZ_Bereich_JG")
	@JsonProperty
	public Integer KZ_Bereich_JG;

	/** Statkue Tabelle IT.NRW: zulässige Jahrgäng der Abgangsart */
	@Id
	@Column(name = "AbgangsJG")
	@JsonProperty
	public String AbgangsJG;

	/** Statkue Tabelle IT.NRW: ??? */
	@Column(name = "Flag")
	@JsonProperty
	public String Flag;

	/** Statkue Tabelle IT.NRW: Datum der letzten Änderung der Abgangsart */
	@Column(name = "geaendert")
	@JsonProperty
	public String geaendert;

	/** Statkue Tabelle IT.NRW: Sortierung der Abgangsart */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Statkue Tabelle IT.NRW: Gibt die Gültigkeit ab welchem Schuljahr an */
	@Column(name = "gueltigVon")
	@JsonProperty
	public Integer gueltigVon;

	/** Statkue Tabelle IT.NRW: Gibt die Gültigkeit bis zu welchem Schuljahr an */
	@Column(name = "gueltigBis")
	@JsonProperty
	public Integer gueltigBis;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOStatkueAbgangsart ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOStatkueAbgangsart() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOStatkueAbgangsart ohne eine Initialisierung der Attribute.
	 * @param SF   der Wert für das Attribut SF
	 * @param Art   der Wert für das Attribut Art
	 */
	public DTOStatkueAbgangsart(final String SF, final String Art) {
		if (SF == null) { 
			throw new NullPointerException("SF must not be null");
		}
		this.SF = SF;
		if (Art == null) { 
			throw new NullPointerException("Art must not be null");
		}
		this.Art = Art;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOStatkueAbgangsart other = (DTOStatkueAbgangsart) obj;
		if (AbgangsJG == null) {
			if (other.AbgangsJG != null)
				return false;
		} else if (!AbgangsJG.equals(other.AbgangsJG))
			return false;

		if (Art == null) {
			if (other.Art != null)
				return false;
		} else if (!Art.equals(other.Art))
			return false;

		if (KZ_Bereich == null) {
			if (other.KZ_Bereich != null)
				return false;
		} else if (!KZ_Bereich.equals(other.KZ_Bereich))
			return false;

		if (KZ_Bereich_JG == null) {
			if (other.KZ_Bereich_JG != null)
				return false;
		} else if (!KZ_Bereich_JG.equals(other.KZ_Bereich_JG))
			return false;

		if (SF == null) {
			if (other.SF != null)
				return false;
		} else if (!SF.equals(other.SF))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((AbgangsJG == null) ? 0 : AbgangsJG.hashCode());

		result = prime * result + ((Art == null) ? 0 : Art.hashCode());

		result = prime * result + ((KZ_Bereich == null) ? 0 : KZ_Bereich.hashCode());

		result = prime * result + ((KZ_Bereich_JG == null) ? 0 : KZ_Bereich_JG.hashCode());

		result = prime * result + ((SF == null) ? 0 : SF.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOStatkueAbgangsart(SF=" + this.SF + ", Art=" + this.Art + ", Beschreibung=" + this.Beschreibung + ", KZ_Bereich=" + this.KZ_Bereich + ", KZ_Bereich_JG=" + this.KZ_Bereich_JG + ", AbgangsJG=" + this.AbgangsJG + ", Flag=" + this.Flag + ", geaendert=" + this.geaendert + ", Sortierung=" + this.Sortierung + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ")";
	}

}