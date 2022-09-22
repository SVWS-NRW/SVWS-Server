package de.nrw.schule.svws.db.dto.current.schule;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Nationalitaeten.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Nationalitaeten")
@NamedQuery(name="DTONationalitaeten.all", query="SELECT e FROM DTONationalitaeten e")
@NamedQuery(name="DTONationalitaeten.id", query="SELECT e FROM DTONationalitaeten e WHERE e.ID = :value")
@NamedQuery(name="DTONationalitaeten.id.multiple", query="SELECT e FROM DTONationalitaeten e WHERE e.ID IN :value")
@NamedQuery(name="DTONationalitaeten.iso3code", query="SELECT e FROM DTONationalitaeten e WHERE e.Iso3Code = :value")
@NamedQuery(name="DTONationalitaeten.iso3code.multiple", query="SELECT e FROM DTONationalitaeten e WHERE e.Iso3Code IN :value")
@NamedQuery(name="DTONationalitaeten.iso2code", query="SELECT e FROM DTONationalitaeten e WHERE e.Iso2Code = :value")
@NamedQuery(name="DTONationalitaeten.iso2code.multiple", query="SELECT e FROM DTONationalitaeten e WHERE e.Iso2Code IN :value")
@NamedQuery(name="DTONationalitaeten.iso3numerisch", query="SELECT e FROM DTONationalitaeten e WHERE e.Iso3Numerisch = :value")
@NamedQuery(name="DTONationalitaeten.iso3numerisch.multiple", query="SELECT e FROM DTONationalitaeten e WHERE e.Iso3Numerisch IN :value")
@NamedQuery(name="DTONationalitaeten.destatiscode", query="SELECT e FROM DTONationalitaeten e WHERE e.DEStatisCode = :value")
@NamedQuery(name="DTONationalitaeten.destatiscode.multiple", query="SELECT e FROM DTONationalitaeten e WHERE e.DEStatisCode IN :value")
@NamedQuery(name="DTONationalitaeten.bezeichnungsuche", query="SELECT e FROM DTONationalitaeten e WHERE e.BezeichnungSuche = :value")
@NamedQuery(name="DTONationalitaeten.bezeichnungsuche.multiple", query="SELECT e FROM DTONationalitaeten e WHERE e.BezeichnungSuche IN :value")
@NamedQuery(name="DTONationalitaeten.bezeichnung", query="SELECT e FROM DTONationalitaeten e WHERE e.Bezeichnung = :value")
@NamedQuery(name="DTONationalitaeten.bezeichnung.multiple", query="SELECT e FROM DTONationalitaeten e WHERE e.Bezeichnung IN :value")
@NamedQuery(name="DTONationalitaeten.bezeichnunglang", query="SELECT e FROM DTONationalitaeten e WHERE e.BezeichnungLang = :value")
@NamedQuery(name="DTONationalitaeten.bezeichnunglang.multiple", query="SELECT e FROM DTONationalitaeten e WHERE e.BezeichnungLang IN :value")
@NamedQuery(name="DTONationalitaeten.staatsangehoerigkeit", query="SELECT e FROM DTONationalitaeten e WHERE e.Staatsangehoerigkeit = :value")
@NamedQuery(name="DTONationalitaeten.staatsangehoerigkeit.multiple", query="SELECT e FROM DTONationalitaeten e WHERE e.Staatsangehoerigkeit IN :value")
@NamedQuery(name="DTONationalitaeten.gueltigvon", query="SELECT e FROM DTONationalitaeten e WHERE e.gueltigVon = :value")
@NamedQuery(name="DTONationalitaeten.gueltigvon.multiple", query="SELECT e FROM DTONationalitaeten e WHERE e.gueltigVon IN :value")
@NamedQuery(name="DTONationalitaeten.gueltigbis", query="SELECT e FROM DTONationalitaeten e WHERE e.gueltigBis = :value")
@NamedQuery(name="DTONationalitaeten.gueltigbis.multiple", query="SELECT e FROM DTONationalitaeten e WHERE e.gueltigBis IN :value")
@NamedQuery(name="DTONationalitaeten.primaryKeyQuery", query="SELECT e FROM DTONationalitaeten e WHERE e.ID = ?1")
@NamedQuery(name="DTONationalitaeten.all.migration", query="SELECT e FROM DTONationalitaeten e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Iso3Code","Iso2Code","Iso3Numerisch","DEStatisCode","BezeichnungSuche","Bezeichnung","BezeichnungLang","Staatsangehoerigkeit","gueltigVon","gueltigBis"})
public class DTONationalitaeten {

	/** Die ID der Nationalität */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Der dreistellige Länder-Code nach ISO 3166 */
	@Column(name = "Iso3Code")
	@JsonProperty
	public String Iso3Code;

	/** Der zweistellige Länder-Code nach ISO 3166 */
	@Column(name = "Iso2Code")
	@JsonProperty
	public String Iso2Code;

	/** Der dreistellige numerische Länder-Code nach ISO 3166 */
	@Column(name = "Iso3Numerisch")
	@JsonProperty
	public String Iso3Numerisch;

	/** Der dreistellige Länder-Code des statistischen Bundesamtes (DESTATIS) */
	@Column(name = "DEStatisCode")
	@JsonProperty
	public String DEStatisCode;

	/** Eine für die Suche geeignete Bezeichnung */
	@Column(name = "BezeichnungSuche")
	@JsonProperty
	public String BezeichnungSuche;

	/** Eine kurze Bezeichnung */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Eine lange Bezeichnung */
	@Column(name = "BezeichnungLang")
	@JsonProperty
	public String BezeichnungLang;

	/** Die Bezeichnung der Staatsangehörigkeit */
	@Column(name = "Staatsangehoerigkeit")
	@JsonProperty
	public String Staatsangehoerigkeit;

	/** Statkue Tabelle IT.NRW: Gibt die Gültigkeit ab welchem Schuljahr an */
	@Column(name = "gueltigVon")
	@JsonProperty
	public Integer gueltigVon;

	/** Statkue Tabelle IT.NRW: Gibt die Gültigkeit bis zu welchem Schuljahr an */
	@Column(name = "gueltigBis")
	@JsonProperty
	public Integer gueltigBis;

	/**
	 * Erstellt ein neues Objekt der Klasse DTONationalitaeten ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTONationalitaeten() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTONationalitaeten ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Iso3Code   der Wert für das Attribut Iso3Code
	 * @param Iso2Code   der Wert für das Attribut Iso2Code
	 * @param DEStatisCode   der Wert für das Attribut DEStatisCode
	 * @param BezeichnungSuche   der Wert für das Attribut BezeichnungSuche
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 * @param BezeichnungLang   der Wert für das Attribut BezeichnungLang
	 * @param Staatsangehoerigkeit   der Wert für das Attribut Staatsangehoerigkeit
	 */
	public DTONationalitaeten(final Long ID, final String Iso3Code, final String Iso2Code, final String DEStatisCode, final String BezeichnungSuche, final String Bezeichnung, final String BezeichnungLang, final String Staatsangehoerigkeit) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Iso3Code == null) { 
			throw new NullPointerException("Iso3Code must not be null");
		}
		this.Iso3Code = Iso3Code;
		if (Iso2Code == null) { 
			throw new NullPointerException("Iso2Code must not be null");
		}
		this.Iso2Code = Iso2Code;
		if (DEStatisCode == null) { 
			throw new NullPointerException("DEStatisCode must not be null");
		}
		this.DEStatisCode = DEStatisCode;
		if (BezeichnungSuche == null) { 
			throw new NullPointerException("BezeichnungSuche must not be null");
		}
		this.BezeichnungSuche = BezeichnungSuche;
		if (Bezeichnung == null) { 
			throw new NullPointerException("Bezeichnung must not be null");
		}
		this.Bezeichnung = Bezeichnung;
		if (BezeichnungLang == null) { 
			throw new NullPointerException("BezeichnungLang must not be null");
		}
		this.BezeichnungLang = BezeichnungLang;
		if (Staatsangehoerigkeit == null) { 
			throw new NullPointerException("Staatsangehoerigkeit must not be null");
		}
		this.Staatsangehoerigkeit = Staatsangehoerigkeit;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTONationalitaeten other = (DTONationalitaeten) obj;
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
		return "DTONationalitaeten(ID=" + this.ID + ", Iso3Code=" + this.Iso3Code + ", Iso2Code=" + this.Iso2Code + ", Iso3Numerisch=" + this.Iso3Numerisch + ", DEStatisCode=" + this.DEStatisCode + ", BezeichnungSuche=" + this.BezeichnungSuche + ", Bezeichnung=" + this.Bezeichnung + ", BezeichnungLang=" + this.BezeichnungLang + ", Staatsangehoerigkeit=" + this.Staatsangehoerigkeit + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ")";
	}

}