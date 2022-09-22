package de.nrw.schule.svws.db.dto.rev8.schulver;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Schulver_WeitereSF.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(Rev8DTOSchulverWeitereSFPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Schulver_WeitereSF")
@NamedQuery(name="Rev8DTOSchulverWeitereSF.all", query="SELECT e FROM Rev8DTOSchulverWeitereSF e")
@NamedQuery(name="Rev8DTOSchulverWeitereSF.snr", query="SELECT e FROM Rev8DTOSchulverWeitereSF e WHERE e.SNR = :value")
@NamedQuery(name="Rev8DTOSchulverWeitereSF.snr.multiple", query="SELECT e FROM Rev8DTOSchulverWeitereSF e WHERE e.SNR IN :value")
@NamedQuery(name="Rev8DTOSchulverWeitereSF.sgl", query="SELECT e FROM Rev8DTOSchulverWeitereSF e WHERE e.SGL = :value")
@NamedQuery(name="Rev8DTOSchulverWeitereSF.sgl.multiple", query="SELECT e FROM Rev8DTOSchulverWeitereSF e WHERE e.SGL IN :value")
@NamedQuery(name="Rev8DTOSchulverWeitereSF.fsp", query="SELECT e FROM Rev8DTOSchulverWeitereSF e WHERE e.FSP = :value")
@NamedQuery(name="Rev8DTOSchulverWeitereSF.fsp.multiple", query="SELECT e FROM Rev8DTOSchulverWeitereSF e WHERE e.FSP IN :value")
@NamedQuery(name="Rev8DTOSchulverWeitereSF.gueltigvon", query="SELECT e FROM Rev8DTOSchulverWeitereSF e WHERE e.gueltigVon = :value")
@NamedQuery(name="Rev8DTOSchulverWeitereSF.gueltigvon.multiple", query="SELECT e FROM Rev8DTOSchulverWeitereSF e WHERE e.gueltigVon IN :value")
@NamedQuery(name="Rev8DTOSchulverWeitereSF.gueltigbis", query="SELECT e FROM Rev8DTOSchulverWeitereSF e WHERE e.gueltigBis = :value")
@NamedQuery(name="Rev8DTOSchulverWeitereSF.gueltigbis.multiple", query="SELECT e FROM Rev8DTOSchulverWeitereSF e WHERE e.gueltigBis IN :value")
@NamedQuery(name="Rev8DTOSchulverWeitereSF.primaryKeyQuery", query="SELECT e FROM Rev8DTOSchulverWeitereSF e WHERE e.SNR = ?1 AND e.SGL = ?2 AND e.FSP = ?3")
@NamedQuery(name="Rev8DTOSchulverWeitereSF.all.migration", query="SELECT e FROM Rev8DTOSchulverWeitereSF e WHERE e.SNR IS NOT NULL AND e.SGL IS NOT NULL AND e.FSP IS NOT NULL")
@JsonPropertyOrder({"SNR","SGL","FSP","gueltigVon","gueltigBis"})
public class Rev8DTOSchulverWeitereSF {

	/** Schulver Tabelle IT.NRW: Schulnummer der Schule */
	@Id
	@Column(name = "SNR")
	@JsonProperty
	public String SNR;

	/** Schulver Tabelle IT.NRW: Gliederung Statistikkürzel */
	@Id
	@Column(name = "SGL")
	@JsonProperty
	public String SGL;

	/** Schulver Tabelle IT.NRW: Förderschwerpunkt ASD-Kürzel */
	@Id
	@Column(name = "FSP")
	@JsonProperty
	public String FSP;

	/** Schulver Tabelle IT.NRW: Gibt die Gültigkeit ab welchem Schuljahr an */
	@Column(name = "gueltigVon")
	@JsonProperty
	public Integer gueltigVon;

	/** Schulver Tabelle IT.NRW: Gibt die Gültigkeit bis zu welchem Schuljahr an */
	@Column(name = "gueltigBis")
	@JsonProperty
	public Integer gueltigBis;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOSchulverWeitereSF ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev8DTOSchulverWeitereSF() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOSchulverWeitereSF ohne eine Initialisierung der Attribute.
	 * @param SNR   der Wert für das Attribut SNR
	 * @param SGL   der Wert für das Attribut SGL
	 * @param FSP   der Wert für das Attribut FSP
	 */
	public Rev8DTOSchulverWeitereSF(final String SNR, final String SGL, final String FSP) {
		if (SNR == null) { 
			throw new NullPointerException("SNR must not be null");
		}
		this.SNR = SNR;
		if (SGL == null) { 
			throw new NullPointerException("SGL must not be null");
		}
		this.SGL = SGL;
		if (FSP == null) { 
			throw new NullPointerException("FSP must not be null");
		}
		this.FSP = FSP;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev8DTOSchulverWeitereSF other = (Rev8DTOSchulverWeitereSF) obj;
		if (SNR == null) {
			if (other.SNR != null)
				return false;
		} else if (!SNR.equals(other.SNR))
			return false;

		if (SGL == null) {
			if (other.SGL != null)
				return false;
		} else if (!SGL.equals(other.SGL))
			return false;

		if (FSP == null) {
			if (other.FSP != null)
				return false;
		} else if (!FSP.equals(other.FSP))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((SNR == null) ? 0 : SNR.hashCode());

		result = prime * result + ((SGL == null) ? 0 : SGL.hashCode());

		result = prime * result + ((FSP == null) ? 0 : FSP.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "Rev8DTOSchulverWeitereSF(SNR=" + this.SNR + ", SGL=" + this.SGL + ", FSP=" + this.FSP + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ")";
	}

}