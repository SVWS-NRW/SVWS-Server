package de.nrw.schule.svws.db.dto.current.schild.schule;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Berufskolleg_Fachklassen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Berufskolleg_Fachklassen")
@NamedQuery(name="DTOBerufskollegFachklassen.all", query="SELECT e FROM DTOBerufskollegFachklassen e")
@NamedQuery(name="DTOBerufskollegFachklassen.id", query="SELECT e FROM DTOBerufskollegFachklassen e WHERE e.ID = :value")
@NamedQuery(name="DTOBerufskollegFachklassen.id.multiple", query="SELECT e FROM DTOBerufskollegFachklassen e WHERE e.ID IN :value")
@NamedQuery(name="DTOBerufskollegFachklassen.fachklassenindex", query="SELECT e FROM DTOBerufskollegFachklassen e WHERE e.FachklassenIndex = :value")
@NamedQuery(name="DTOBerufskollegFachklassen.fachklassenindex.multiple", query="SELECT e FROM DTOBerufskollegFachklassen e WHERE e.FachklassenIndex IN :value")
@NamedQuery(name="DTOBerufskollegFachklassen.schluessel", query="SELECT e FROM DTOBerufskollegFachklassen e WHERE e.Schluessel = :value")
@NamedQuery(name="DTOBerufskollegFachklassen.schluessel.multiple", query="SELECT e FROM DTOBerufskollegFachklassen e WHERE e.Schluessel IN :value")
@NamedQuery(name="DTOBerufskollegFachklassen.schluessel2", query="SELECT e FROM DTOBerufskollegFachklassen e WHERE e.Schluessel2 = :value")
@NamedQuery(name="DTOBerufskollegFachklassen.schluessel2.multiple", query="SELECT e FROM DTOBerufskollegFachklassen e WHERE e.Schluessel2 IN :value")
@NamedQuery(name="DTOBerufskollegFachklassen.istausgelaufen", query="SELECT e FROM DTOBerufskollegFachklassen e WHERE e.IstAusgelaufen = :value")
@NamedQuery(name="DTOBerufskollegFachklassen.istausgelaufen.multiple", query="SELECT e FROM DTOBerufskollegFachklassen e WHERE e.IstAusgelaufen IN :value")
@NamedQuery(name="DTOBerufskollegFachklassen.berufsfeldgruppe", query="SELECT e FROM DTOBerufskollegFachklassen e WHERE e.BerufsfeldGruppe = :value")
@NamedQuery(name="DTOBerufskollegFachklassen.berufsfeldgruppe.multiple", query="SELECT e FROM DTOBerufskollegFachklassen e WHERE e.BerufsfeldGruppe IN :value")
@NamedQuery(name="DTOBerufskollegFachklassen.berufsfeld", query="SELECT e FROM DTOBerufskollegFachklassen e WHERE e.Berufsfeld = :value")
@NamedQuery(name="DTOBerufskollegFachklassen.berufsfeld.multiple", query="SELECT e FROM DTOBerufskollegFachklassen e WHERE e.Berufsfeld IN :value")
@NamedQuery(name="DTOBerufskollegFachklassen.berufsebene1", query="SELECT e FROM DTOBerufskollegFachklassen e WHERE e.Berufsebene1 = :value")
@NamedQuery(name="DTOBerufskollegFachklassen.berufsebene1.multiple", query="SELECT e FROM DTOBerufskollegFachklassen e WHERE e.Berufsebene1 IN :value")
@NamedQuery(name="DTOBerufskollegFachklassen.berufsebene2", query="SELECT e FROM DTOBerufskollegFachklassen e WHERE e.Berufsebene2 = :value")
@NamedQuery(name="DTOBerufskollegFachklassen.berufsebene2.multiple", query="SELECT e FROM DTOBerufskollegFachklassen e WHERE e.Berufsebene2 IN :value")
@NamedQuery(name="DTOBerufskollegFachklassen.berufsebene3", query="SELECT e FROM DTOBerufskollegFachklassen e WHERE e.Berufsebene3 = :value")
@NamedQuery(name="DTOBerufskollegFachklassen.berufsebene3.multiple", query="SELECT e FROM DTOBerufskollegFachklassen e WHERE e.Berufsebene3 IN :value")
@NamedQuery(name="DTOBerufskollegFachklassen.bezeichnung", query="SELECT e FROM DTOBerufskollegFachklassen e WHERE e.Bezeichnung = :value")
@NamedQuery(name="DTOBerufskollegFachklassen.bezeichnung.multiple", query="SELECT e FROM DTOBerufskollegFachklassen e WHERE e.Bezeichnung IN :value")
@NamedQuery(name="DTOBerufskollegFachklassen.bezeichnungm", query="SELECT e FROM DTOBerufskollegFachklassen e WHERE e.BezeichnungM = :value")
@NamedQuery(name="DTOBerufskollegFachklassen.bezeichnungm.multiple", query="SELECT e FROM DTOBerufskollegFachklassen e WHERE e.BezeichnungM IN :value")
@NamedQuery(name="DTOBerufskollegFachklassen.bezeichnungw", query="SELECT e FROM DTOBerufskollegFachklassen e WHERE e.BezeichnungW = :value")
@NamedQuery(name="DTOBerufskollegFachklassen.bezeichnungw.multiple", query="SELECT e FROM DTOBerufskollegFachklassen e WHERE e.BezeichnungW IN :value")
@NamedQuery(name="DTOBerufskollegFachklassen.gueltigvon", query="SELECT e FROM DTOBerufskollegFachklassen e WHERE e.gueltigVon = :value")
@NamedQuery(name="DTOBerufskollegFachklassen.gueltigvon.multiple", query="SELECT e FROM DTOBerufskollegFachklassen e WHERE e.gueltigVon IN :value")
@NamedQuery(name="DTOBerufskollegFachklassen.gueltigbis", query="SELECT e FROM DTOBerufskollegFachklassen e WHERE e.gueltigBis = :value")
@NamedQuery(name="DTOBerufskollegFachklassen.gueltigbis.multiple", query="SELECT e FROM DTOBerufskollegFachklassen e WHERE e.gueltigBis IN :value")
@NamedQuery(name="DTOBerufskollegFachklassen.primaryKeyQuery", query="SELECT e FROM DTOBerufskollegFachklassen e WHERE e.ID = ?1")
@NamedQuery(name="DTOBerufskollegFachklassen.all.migration", query="SELECT e FROM DTOBerufskollegFachklassen e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","FachklassenIndex","Schluessel","Schluessel2","IstAusgelaufen","BerufsfeldGruppe","Berufsfeld","Berufsebene1","Berufsebene2","Berufsebene3","Bezeichnung","BezeichnungM","BezeichnungW","gueltigVon","gueltigBis"})
public class DTOBerufskollegFachklassen {

	/** Die ID der Fachklasse */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Der Index, welcher der Fachklasse für die Zuordnung zum Bildungsgang zugeordnet ist */
	@Column(name = "FachklassenIndex")
	@JsonProperty
	public Integer FachklassenIndex;

	/** Der erste Teil des Fachklassenschlüssels (FKS, dreistellig)  */
	@Column(name = "Schluessel")
	@JsonProperty
	public String Schluessel;

	/** Der zweite Teil des Fachklassenschlüssels (AP, zweistellig) */
	@Column(name = "Schluessel2")
	@JsonProperty
	public String Schluessel2;

	/** Gibt an, ob die Fachklasse ausgelaufen ist oder nicht */
	@Column(name = "IstAusgelaufen")
	@JsonProperty
	@Convert(converter=Boolean01Converter.class)
	@JsonSerialize(using=Boolean01ConverterSerializer.class)
	@JsonDeserialize(using=Boolean01ConverterDeserializer.class)
	public Boolean IstAusgelaufen;

	/** Die Gruppe der Berufsfelder, welcher das Berufsfeld der Fachklasse */
	@Column(name = "BerufsfeldGruppe")
	@JsonProperty
	public String BerufsfeldGruppe;

	/** Das Berufsfeld, welchem die Fachklasse zugeordnet ist */
	@Column(name = "Berufsfeld")
	@JsonProperty
	public String Berufsfeld;

	/** Die Berufsebene 1 */
	@Column(name = "Berufsebene1")
	@JsonProperty
	public String Berufsebene1;

	/** Die Berufsebene 2 */
	@Column(name = "Berufsebene2")
	@JsonProperty
	public String Berufsebene2;

	/** Die Berufsebene 3 */
	@Column(name = "Berufsebene3")
	@JsonProperty
	public String Berufsebene3;

	/** Die Bezeichnung der Fachklasse */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Die Bezeichnung der Fachklasse (Text in männlicher Form) */
	@Column(name = "BezeichnungM")
	@JsonProperty
	public String BezeichnungM;

	/** Die Bezeichnung der Fachklasse (Text in weiblicher Form) */
	@Column(name = "BezeichnungW")
	@JsonProperty
	public String BezeichnungW;

	/** Gibt die Gültigkeit ab welchem Schuljahr an */
	@Column(name = "gueltigVon")
	@JsonProperty
	public Integer gueltigVon;

	/** Gibt die Gültigkeit bis zu welchem Schuljahr an */
	@Column(name = "gueltigBis")
	@JsonProperty
	public Integer gueltigBis;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOBerufskollegFachklassen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOBerufskollegFachklassen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOBerufskollegFachklassen ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param FachklassenIndex   der Wert für das Attribut FachklassenIndex
	 * @param Schluessel   der Wert für das Attribut Schluessel
	 * @param Schluessel2   der Wert für das Attribut Schluessel2
	 * @param IstAusgelaufen   der Wert für das Attribut IstAusgelaufen
	 * @param BerufsfeldGruppe   der Wert für das Attribut BerufsfeldGruppe
	 * @param Berufsfeld   der Wert für das Attribut Berufsfeld
	 * @param Berufsebene1   der Wert für das Attribut Berufsebene1
	 * @param Berufsebene2   der Wert für das Attribut Berufsebene2
	 * @param Berufsebene3   der Wert für das Attribut Berufsebene3
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 * @param BezeichnungM   der Wert für das Attribut BezeichnungM
	 * @param BezeichnungW   der Wert für das Attribut BezeichnungW
	 */
	public DTOBerufskollegFachklassen(final Long ID, final Integer FachklassenIndex, final String Schluessel, final String Schluessel2, final Boolean IstAusgelaufen, final String BerufsfeldGruppe, final String Berufsfeld, final String Berufsebene1, final String Berufsebene2, final String Berufsebene3, final String Bezeichnung, final String BezeichnungM, final String BezeichnungW) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (FachklassenIndex == null) { 
			throw new NullPointerException("FachklassenIndex must not be null");
		}
		this.FachklassenIndex = FachklassenIndex;
		if (Schluessel == null) { 
			throw new NullPointerException("Schluessel must not be null");
		}
		this.Schluessel = Schluessel;
		if (Schluessel2 == null) { 
			throw new NullPointerException("Schluessel2 must not be null");
		}
		this.Schluessel2 = Schluessel2;
		if (IstAusgelaufen == null) { 
			throw new NullPointerException("IstAusgelaufen must not be null");
		}
		this.IstAusgelaufen = IstAusgelaufen;
		if (BerufsfeldGruppe == null) { 
			throw new NullPointerException("BerufsfeldGruppe must not be null");
		}
		this.BerufsfeldGruppe = BerufsfeldGruppe;
		if (Berufsfeld == null) { 
			throw new NullPointerException("Berufsfeld must not be null");
		}
		this.Berufsfeld = Berufsfeld;
		if (Berufsebene1 == null) { 
			throw new NullPointerException("Berufsebene1 must not be null");
		}
		this.Berufsebene1 = Berufsebene1;
		if (Berufsebene2 == null) { 
			throw new NullPointerException("Berufsebene2 must not be null");
		}
		this.Berufsebene2 = Berufsebene2;
		if (Berufsebene3 == null) { 
			throw new NullPointerException("Berufsebene3 must not be null");
		}
		this.Berufsebene3 = Berufsebene3;
		if (Bezeichnung == null) { 
			throw new NullPointerException("Bezeichnung must not be null");
		}
		this.Bezeichnung = Bezeichnung;
		if (BezeichnungM == null) { 
			throw new NullPointerException("BezeichnungM must not be null");
		}
		this.BezeichnungM = BezeichnungM;
		if (BezeichnungW == null) { 
			throw new NullPointerException("BezeichnungW must not be null");
		}
		this.BezeichnungW = BezeichnungW;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOBerufskollegFachklassen other = (DTOBerufskollegFachklassen) obj;
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
		return "DTOBerufskollegFachklassen(ID=" + this.ID + ", FachklassenIndex=" + this.FachklassenIndex + ", Schluessel=" + this.Schluessel + ", Schluessel2=" + this.Schluessel2 + ", IstAusgelaufen=" + this.IstAusgelaufen + ", BerufsfeldGruppe=" + this.BerufsfeldGruppe + ", Berufsfeld=" + this.Berufsfeld + ", Berufsebene1=" + this.Berufsebene1 + ", Berufsebene2=" + this.Berufsebene2 + ", Berufsebene3=" + this.Berufsebene3 + ", Bezeichnung=" + this.Bezeichnung + ", BezeichnungM=" + this.BezeichnungM + ", BezeichnungW=" + this.BezeichnungW + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ")";
	}

}