package de.nrw.schule.svws.db.dto.current.statkue;

import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.converter.current.Boolean01Converter;


import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.nrw.schule.svws.csv.converter.current.Boolean01ConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.Boolean01ConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Statkue_AndereGrundschulen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Statkue_AndereGrundschulen")
@NamedQuery(name="DTOStatkueAndereGrundschulen.all", query="SELECT e FROM DTOStatkueAndereGrundschulen e")
@NamedQuery(name="DTOStatkueAndereGrundschulen.snr", query="SELECT e FROM DTOStatkueAndereGrundschulen e WHERE e.SNR = :value")
@NamedQuery(name="DTOStatkueAndereGrundschulen.snr.multiple", query="SELECT e FROM DTOStatkueAndereGrundschulen e WHERE e.SNR IN :value")
@NamedQuery(name="DTOStatkueAndereGrundschulen.sf", query="SELECT e FROM DTOStatkueAndereGrundschulen e WHERE e.SF = :value")
@NamedQuery(name="DTOStatkueAndereGrundschulen.sf.multiple", query="SELECT e FROM DTOStatkueAndereGrundschulen e WHERE e.SF IN :value")
@NamedQuery(name="DTOStatkueAndereGrundschulen.abez1", query="SELECT e FROM DTOStatkueAndereGrundschulen e WHERE e.ABez1 = :value")
@NamedQuery(name="DTOStatkueAndereGrundschulen.abez1.multiple", query="SELECT e FROM DTOStatkueAndereGrundschulen e WHERE e.ABez1 IN :value")
@NamedQuery(name="DTOStatkueAndereGrundschulen.strasse", query="SELECT e FROM DTOStatkueAndereGrundschulen e WHERE e.Strasse = :value")
@NamedQuery(name="DTOStatkueAndereGrundschulen.strasse.multiple", query="SELECT e FROM DTOStatkueAndereGrundschulen e WHERE e.Strasse IN :value")
@NamedQuery(name="DTOStatkueAndereGrundschulen.ort", query="SELECT e FROM DTOStatkueAndereGrundschulen e WHERE e.Ort = :value")
@NamedQuery(name="DTOStatkueAndereGrundschulen.ort.multiple", query="SELECT e FROM DTOStatkueAndereGrundschulen e WHERE e.Ort IN :value")
@NamedQuery(name="DTOStatkueAndereGrundschulen.auswahl", query="SELECT e FROM DTOStatkueAndereGrundschulen e WHERE e.Auswahl = :value")
@NamedQuery(name="DTOStatkueAndereGrundschulen.auswahl.multiple", query="SELECT e FROM DTOStatkueAndereGrundschulen e WHERE e.Auswahl IN :value")
@NamedQuery(name="DTOStatkueAndereGrundschulen.regschl", query="SELECT e FROM DTOStatkueAndereGrundschulen e WHERE e.RegSchl = :value")
@NamedQuery(name="DTOStatkueAndereGrundschulen.regschl.multiple", query="SELECT e FROM DTOStatkueAndereGrundschulen e WHERE e.RegSchl IN :value")
@NamedQuery(name="DTOStatkueAndereGrundschulen.geaendert", query="SELECT e FROM DTOStatkueAndereGrundschulen e WHERE e.geaendert = :value")
@NamedQuery(name="DTOStatkueAndereGrundschulen.geaendert.multiple", query="SELECT e FROM DTOStatkueAndereGrundschulen e WHERE e.geaendert IN :value")
@NamedQuery(name="DTOStatkueAndereGrundschulen.gueltigvon", query="SELECT e FROM DTOStatkueAndereGrundschulen e WHERE e.gueltigVon = :value")
@NamedQuery(name="DTOStatkueAndereGrundschulen.gueltigvon.multiple", query="SELECT e FROM DTOStatkueAndereGrundschulen e WHERE e.gueltigVon IN :value")
@NamedQuery(name="DTOStatkueAndereGrundschulen.gueltigbis", query="SELECT e FROM DTOStatkueAndereGrundschulen e WHERE e.gueltigBis = :value")
@NamedQuery(name="DTOStatkueAndereGrundschulen.gueltigbis.multiple", query="SELECT e FROM DTOStatkueAndereGrundschulen e WHERE e.gueltigBis IN :value")
@NamedQuery(name="DTOStatkueAndereGrundschulen.primaryKeyQuery", query="SELECT e FROM DTOStatkueAndereGrundschulen e WHERE e.SNR = ?1")
@NamedQuery(name="DTOStatkueAndereGrundschulen.all.migration", query="SELECT e FROM DTOStatkueAndereGrundschulen e WHERE e.SNR IS NOT NULL")
@JsonPropertyOrder({"SNR","SF","ABez1","Strasse","Ort","Auswahl","RegSchl","geaendert","gueltigVon","gueltigBis"})
public class DTOStatkueAndereGrundschulen {

	/** Statkue Tabelle IT.NRW: Schulnummer anderer Schulen oder Herkunftsformen */
	@Id
	@Column(name = "SNR")
	@JsonProperty
	public String SNR;

	/** Statkue Tabelle IT.NRW: Schulform anderer Schulen oder Herkunftsformen */
	@Column(name = "SF")
	@JsonProperty
	public String SF;

	/** Statkue Tabelle IT.NRW: Bezeichnung anderer Schulen oder Herkunftsformen */
	@Column(name = "ABez1")
	@JsonProperty
	public String ABez1;

	/** Statkue Tabelle IT.NRW: leer anderer Schulen oder Herkunftsformen */
	@Column(name = "Strasse")
	@JsonProperty
	public String Strasse;

	/** Statkue Tabelle IT.NRW: leer anderer Schulen oder Herkunftsformen */
	@Column(name = "Ort")
	@JsonProperty
	public String Ort;

	/** Statkue Tabelle IT.NRW: imme r0  anderer Schulen oder Herkunftsformen */
	@Column(name = "Auswahl")
	@JsonProperty
	@Convert(converter=Boolean01Converter.class)
	@JsonSerialize(using=Boolean01ConverterSerializer.class)
	@JsonDeserialize(using=Boolean01ConverterDeserializer.class)
	public Boolean Auswahl;

	/** Statkue Tabelle IT.NRW: RegSchl ist identisch mit SF  anderer Schulen oder Herkunftsformen */
	@Column(name = "RegSchl")
	@JsonProperty
	public String RegSchl;

	/** Statkue Tabelle IT.NRW: Datum der letzten Änderung anderer Schulen oder Herkunftsformen */
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
	 * Erstellt ein neues Objekt der Klasse DTOStatkueAndereGrundschulen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOStatkueAndereGrundschulen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOStatkueAndereGrundschulen ohne eine Initialisierung der Attribute.
	 * @param SNR   der Wert für das Attribut SNR
	 */
	public DTOStatkueAndereGrundschulen(final String SNR) {
		if (SNR == null) { 
			throw new NullPointerException("SNR must not be null");
		}
		this.SNR = SNR;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOStatkueAndereGrundschulen other = (DTOStatkueAndereGrundschulen) obj;
		if (SNR == null) {
			if (other.SNR != null)
				return false;
		} else if (!SNR.equals(other.SNR))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((SNR == null) ? 0 : SNR.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOStatkueAndereGrundschulen(SNR=" + this.SNR + ", SF=" + this.SF + ", ABez1=" + this.ABez1 + ", Strasse=" + this.Strasse + ", Ort=" + this.Ort + ", Auswahl=" + this.Auswahl + ", RegSchl=" + this.RegSchl + ", geaendert=" + this.geaendert + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ")";
	}

}