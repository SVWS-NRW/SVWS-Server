package de.nrw.schule.svws.db.dto.current.views.statkue;

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
 * Diese Klasse dient als DTO für die Datenbank-View Statkue_SVWS_Schulgliederungen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Statkue_SVWS_Schulgliederungen")
@NamedQuery(name="DTOSVWSSchulgliederungen.all", query="SELECT e FROM DTOSVWSSchulgliederungen e")
@NamedQuery(name="DTOSVWSSchulgliederungen.sgl", query="SELECT e FROM DTOSVWSSchulgliederungen e WHERE e.SGL = :value")
@NamedQuery(name="DTOSVWSSchulgliederungen.sgl.multiple", query="SELECT e FROM DTOSVWSSchulgliederungen e WHERE e.SGL IN :value")
@NamedQuery(name="DTOSVWSSchulgliederungen.istbk", query="SELECT e FROM DTOSVWSSchulgliederungen e WHERE e.istBK = :value")
@NamedQuery(name="DTOSVWSSchulgliederungen.istbk.multiple", query="SELECT e FROM DTOSVWSSchulgliederungen e WHERE e.istBK IN :value")
@NamedQuery(name="DTOSVWSSchulgliederungen.schulformen", query="SELECT e FROM DTOSVWSSchulgliederungen e WHERE e.Schulformen = :value")
@NamedQuery(name="DTOSVWSSchulgliederungen.schulformen.multiple", query="SELECT e FROM DTOSVWSSchulgliederungen e WHERE e.Schulformen IN :value")
@NamedQuery(name="DTOSVWSSchulgliederungen.istauslaufend", query="SELECT e FROM DTOSVWSSchulgliederungen e WHERE e.istAuslaufend = :value")
@NamedQuery(name="DTOSVWSSchulgliederungen.istauslaufend.multiple", query="SELECT e FROM DTOSVWSSchulgliederungen e WHERE e.istAuslaufend IN :value")
@NamedQuery(name="DTOSVWSSchulgliederungen.istausgelaufen", query="SELECT e FROM DTOSVWSSchulgliederungen e WHERE e.istAusgelaufen = :value")
@NamedQuery(name="DTOSVWSSchulgliederungen.istausgelaufen.multiple", query="SELECT e FROM DTOSVWSSchulgliederungen e WHERE e.istAusgelaufen IN :value")
@NamedQuery(name="DTOSVWSSchulgliederungen.beschreibung", query="SELECT e FROM DTOSVWSSchulgliederungen e WHERE e.Beschreibung = :value")
@NamedQuery(name="DTOSVWSSchulgliederungen.beschreibung.multiple", query="SELECT e FROM DTOSVWSSchulgliederungen e WHERE e.Beschreibung IN :value")
@NamedQuery(name="DTOSVWSSchulgliederungen.bkanlage", query="SELECT e FROM DTOSVWSSchulgliederungen e WHERE e.BKAnlage = :value")
@NamedQuery(name="DTOSVWSSchulgliederungen.bkanlage.multiple", query="SELECT e FROM DTOSVWSSchulgliederungen e WHERE e.BKAnlage IN :value")
@NamedQuery(name="DTOSVWSSchulgliederungen.bktyp", query="SELECT e FROM DTOSVWSSchulgliederungen e WHERE e.BKTyp = :value")
@NamedQuery(name="DTOSVWSSchulgliederungen.bktyp.multiple", query="SELECT e FROM DTOSVWSSchulgliederungen e WHERE e.BKTyp IN :value")
@NamedQuery(name="DTOSVWSSchulgliederungen.bkindex", query="SELECT e FROM DTOSVWSSchulgliederungen e WHERE e.BKIndex = :value")
@NamedQuery(name="DTOSVWSSchulgliederungen.bkindex.multiple", query="SELECT e FROM DTOSVWSSchulgliederungen e WHERE e.BKIndex IN :value")
@NamedQuery(name="DTOSVWSSchulgliederungen.istvz", query="SELECT e FROM DTOSVWSSchulgliederungen e WHERE e.istVZ = :value")
@NamedQuery(name="DTOSVWSSchulgliederungen.istvz.multiple", query="SELECT e FROM DTOSVWSSchulgliederungen e WHERE e.istVZ IN :value")
@NamedQuery(name="DTOSVWSSchulgliederungen.bkabschlussberuf", query="SELECT e FROM DTOSVWSSchulgliederungen e WHERE e.BKAbschlussBeruf = :value")
@NamedQuery(name="DTOSVWSSchulgliederungen.bkabschlussberuf.multiple", query="SELECT e FROM DTOSVWSSchulgliederungen e WHERE e.BKAbschlussBeruf IN :value")
@NamedQuery(name="DTOSVWSSchulgliederungen.bkabschlussallg", query="SELECT e FROM DTOSVWSSchulgliederungen e WHERE e.BKAbschlussAllg = :value")
@NamedQuery(name="DTOSVWSSchulgliederungen.bkabschlussallg.multiple", query="SELECT e FROM DTOSVWSSchulgliederungen e WHERE e.BKAbschlussAllg IN :value")
@NamedQuery(name="DTOSVWSSchulgliederungen.primaryKeyQuery", query="SELECT e FROM DTOSVWSSchulgliederungen e WHERE e.SGL = ?1")
@JsonPropertyOrder({"SGL","istBK","Schulformen","istAuslaufend","istAusgelaufen","Beschreibung","BKAnlage","BKTyp","BKIndex","istVZ","BKAbschlussBeruf","BKAbschlussAllg"})
public class DTOSVWSSchulgliederungen {

	/** Das dreistellige Kürzel der Schulgliederung bzw. des Bildungsganges */
	@Id
	@Column(name = "SGL")
	@JsonProperty
	public String SGL;

	/** Boolscher Wert ob es sich um eine Schulgliederung einer BK-Anlage handelt */
	@Column(name = "istBK")
	@JsonProperty
	@Convert(converter=Boolean01Converter.class)
	@JsonSerialize(using=Boolean01ConverterSerializer.class)
	@JsonDeserialize(using=Boolean01ConverterDeserializer.class)
	public Boolean istBK;

	/** Eine Komma-separierte Liste der Schulform-Kürzel, bei denen der Bildungsgang vorhanden ist */
	@Column(name = "Schulformen")
	@JsonProperty
	public String Schulformen;

	/** Boolscher Wert, ob es sich um eine auslaufende Schulgliederung handelt */
	@Column(name = "istAuslaufend")
	@JsonProperty
	@Convert(converter=Boolean01Converter.class)
	@JsonSerialize(using=Boolean01ConverterSerializer.class)
	@JsonDeserialize(using=Boolean01ConverterDeserializer.class)
	public Boolean istAuslaufend;

	/** Boolscher Wert, ob die Schulgliederung bereits ausgelaufen ist */
	@Column(name = "istAusgelaufen")
	@JsonProperty
	@Convert(converter=Boolean01Converter.class)
	@JsonSerialize(using=Boolean01ConverterSerializer.class)
	@JsonDeserialize(using=Boolean01ConverterDeserializer.class)
	public Boolean istAusgelaufen;

	/** Textuelle Beschreibung der Schulgliederung */
	@Column(name = "Beschreibung")
	@JsonProperty
	public String Beschreibung;

	/** Die Anlage bei einem Bildungsgang des Berufskollegs */
	@Column(name = "BKAnlage")
	@JsonProperty
	public String BKAnlage;

	/** Der Typ der Anlage bei einem Bildungsgang des Berufskollegs */
	@Column(name = "BKTyp")
	@JsonProperty
	public String BKTyp;

	/** Der Index in die Fachklassen-Tabelle einem Bildungsgang des Berufskollegs */
	@Column(name = "BKIndex")
	@JsonProperty
	public String BKIndex;

	/** Boolscher Wert, ob ein Bildungsgang des Berufskollegs Vollzeit erfordert */
	@Column(name = "istVZ")
	@JsonProperty
	@Convert(converter=Boolean01Converter.class)
	@JsonSerialize(using=Boolean01ConverterSerializer.class)
	@JsonDeserialize(using=Boolean01ConverterDeserializer.class)
	public Boolean istVZ;

	/** Eine Komma-separierte Liste der Kürzel der möglichen berufsbildenden Abschlüsse bei einem Bildungsgang des Berufskollegs */
	@Column(name = "BKAbschlussBeruf")
	@JsonProperty
	public String BKAbschlussBeruf;

	/** Eine Komma-separierte Liste der Kürzel der möglichen allgemeinbildenden Abschlüsse bei einem Bildungsgang des Berufskollegs */
	@Column(name = "BKAbschlussAllg")
	@JsonProperty
	public String BKAbschlussAllg;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSVWSSchulgliederungen ohne eine Initialisierung der Attribute.
	 */
	private DTOSVWSSchulgliederungen() {
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOSVWSSchulgliederungen other = (DTOSVWSSchulgliederungen) obj;
		if (SGL == null) {
			if (other.SGL != null)
				return false;
		} else if (!SGL.equals(other.SGL))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((SGL == null) ? 0 : SGL.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOSVWSSchulgliederungen(SGL=" + this.SGL + ", istBK=" + this.istBK + ", Schulformen=" + this.Schulformen + ", istAuslaufend=" + this.istAuslaufend + ", istAusgelaufen=" + this.istAusgelaufen + ", Beschreibung=" + this.Beschreibung + ", BKAnlage=" + this.BKAnlage + ", BKTyp=" + this.BKTyp + ", BKIndex=" + this.BKIndex + ", istVZ=" + this.istVZ + ", BKAbschlussBeruf=" + this.BKAbschlussBeruf + ", BKAbschlussAllg=" + this.BKAbschlussAllg + ")";
	}

}