package de.nrw.schule.svws.db.dto.rev8.statkue;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Statkue_Reformpaedagogik.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(Rev8DTOStatkueReformpaedagogikPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Statkue_Reformpaedagogik")
@NamedQuery(name="Rev8DTOStatkueReformpaedagogik.all", query="SELECT e FROM Rev8DTOStatkueReformpaedagogik e")
@NamedQuery(name="Rev8DTOStatkueReformpaedagogik.sf", query="SELECT e FROM Rev8DTOStatkueReformpaedagogik e WHERE e.SF = :value")
@NamedQuery(name="Rev8DTOStatkueReformpaedagogik.sf.multiple", query="SELECT e FROM Rev8DTOStatkueReformpaedagogik e WHERE e.SF IN :value")
@NamedQuery(name="Rev8DTOStatkueReformpaedagogik.rpg", query="SELECT e FROM Rev8DTOStatkueReformpaedagogik e WHERE e.RPG = :value")
@NamedQuery(name="Rev8DTOStatkueReformpaedagogik.rpg.multiple", query="SELECT e FROM Rev8DTOStatkueReformpaedagogik e WHERE e.RPG IN :value")
@NamedQuery(name="Rev8DTOStatkueReformpaedagogik.beschreibung", query="SELECT e FROM Rev8DTOStatkueReformpaedagogik e WHERE e.Beschreibung = :value")
@NamedQuery(name="Rev8DTOStatkueReformpaedagogik.beschreibung.multiple", query="SELECT e FROM Rev8DTOStatkueReformpaedagogik e WHERE e.Beschreibung IN :value")
@NamedQuery(name="Rev8DTOStatkueReformpaedagogik.geaendert", query="SELECT e FROM Rev8DTOStatkueReformpaedagogik e WHERE e.geaendert = :value")
@NamedQuery(name="Rev8DTOStatkueReformpaedagogik.geaendert.multiple", query="SELECT e FROM Rev8DTOStatkueReformpaedagogik e WHERE e.geaendert IN :value")
@NamedQuery(name="Rev8DTOStatkueReformpaedagogik.gueltigvon", query="SELECT e FROM Rev8DTOStatkueReformpaedagogik e WHERE e.gueltigVon = :value")
@NamedQuery(name="Rev8DTOStatkueReformpaedagogik.gueltigvon.multiple", query="SELECT e FROM Rev8DTOStatkueReformpaedagogik e WHERE e.gueltigVon IN :value")
@NamedQuery(name="Rev8DTOStatkueReformpaedagogik.gueltigbis", query="SELECT e FROM Rev8DTOStatkueReformpaedagogik e WHERE e.gueltigBis = :value")
@NamedQuery(name="Rev8DTOStatkueReformpaedagogik.gueltigbis.multiple", query="SELECT e FROM Rev8DTOStatkueReformpaedagogik e WHERE e.gueltigBis IN :value")
@NamedQuery(name="Rev8DTOStatkueReformpaedagogik.primaryKeyQuery", query="SELECT e FROM Rev8DTOStatkueReformpaedagogik e WHERE e.SF = ?1 AND e.RPG = ?2")
@NamedQuery(name="Rev8DTOStatkueReformpaedagogik.all.migration", query="SELECT e FROM Rev8DTOStatkueReformpaedagogik e WHERE e.SF IS NOT NULL AND e.RPG IS NOT NULL")
@JsonPropertyOrder({"SF","RPG","Beschreibung","geaendert","gueltigVon","gueltigBis"})
public class Rev8DTOStatkueReformpaedagogik {

	/** Statkue Tabelle IT.NRW: zulässige Schulform für Reformpädagogik */
	@Id
	@Column(name = "SF")
	@JsonProperty
	public String SF;

	/** Statkue Tabelle IT.NRW: Statstikkürzel Reformpädagogik */
	@Id
	@Column(name = "RPG")
	@JsonProperty
	public String RPG;

	/** Statkue Tabelle IT.NRW: Beschreibung Reformpädagogik */
	@Column(name = "Beschreibung")
	@JsonProperty
	public String Beschreibung;

	/** Statkue Tabelle IT.NRW: Datum der letzten Änderung */
	@Column(name = "geaendert")
	@JsonProperty
	public String geaendert;

	/** Statkue Tabelle IT.NRW: Gibt die Gültigkeit ab welchem Schuljahr an */
	@Column(name = "gueltigVon")
	@JsonProperty
	public Integer gueltigVon;

	/** Statkue Tabelle IT.NRW: Gibt die Gültigkeit bis zu welchem Schuljahr an */
	@Column(name = "gueltigBis")
	@JsonProperty
	public Integer gueltigBis;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOStatkueReformpaedagogik ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev8DTOStatkueReformpaedagogik() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOStatkueReformpaedagogik ohne eine Initialisierung der Attribute.
	 * @param SF   der Wert für das Attribut SF
	 * @param RPG   der Wert für das Attribut RPG
	 * @param Beschreibung   der Wert für das Attribut Beschreibung
	 */
	public Rev8DTOStatkueReformpaedagogik(final String SF, final String RPG, final String Beschreibung) {
		if (SF == null) { 
			throw new NullPointerException("SF must not be null");
		}
		this.SF = SF;
		if (RPG == null) { 
			throw new NullPointerException("RPG must not be null");
		}
		this.RPG = RPG;
		if (Beschreibung == null) { 
			throw new NullPointerException("Beschreibung must not be null");
		}
		this.Beschreibung = Beschreibung;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev8DTOStatkueReformpaedagogik other = (Rev8DTOStatkueReformpaedagogik) obj;
		if (SF == null) {
			if (other.SF != null)
				return false;
		} else if (!SF.equals(other.SF))
			return false;

		if (RPG == null) {
			if (other.RPG != null)
				return false;
		} else if (!RPG.equals(other.RPG))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((SF == null) ? 0 : SF.hashCode());

		result = prime * result + ((RPG == null) ? 0 : RPG.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "Rev8DTOStatkueReformpaedagogik(SF=" + this.SF + ", RPG=" + this.RPG + ", Beschreibung=" + this.Beschreibung + ", geaendert=" + this.geaendert + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ")";
	}

}