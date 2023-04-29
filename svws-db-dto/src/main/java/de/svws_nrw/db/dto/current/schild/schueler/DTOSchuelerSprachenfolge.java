package de.svws_nrw.db.dto.current.schild.schueler;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.Boolean01Converter;
import de.svws_nrw.db.converter.current.SprachreferenzniveauConverter;

import de.svws_nrw.core.types.fach.Sprachreferenzniveau;


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
import de.svws_nrw.csv.converter.current.Boolean01ConverterSerializer;
import de.svws_nrw.csv.converter.current.Boolean01ConverterDeserializer;
import de.svws_nrw.csv.converter.current.SprachreferenzniveauConverterSerializer;
import de.svws_nrw.csv.converter.current.SprachreferenzniveauConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerSprachenfolge.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerSprachenfolge")
@NamedQuery(name = "DTOSchuelerSprachenfolge.all", query = "SELECT e FROM DTOSchuelerSprachenfolge e")
@NamedQuery(name = "DTOSchuelerSprachenfolge.id", query = "SELECT e FROM DTOSchuelerSprachenfolge e WHERE e.ID = :value")
@NamedQuery(name = "DTOSchuelerSprachenfolge.id.multiple", query = "SELECT e FROM DTOSchuelerSprachenfolge e WHERE e.ID IN :value")
@NamedQuery(name = "DTOSchuelerSprachenfolge.schueler_id", query = "SELECT e FROM DTOSchuelerSprachenfolge e WHERE e.Schueler_ID = :value")
@NamedQuery(name = "DTOSchuelerSprachenfolge.schueler_id.multiple", query = "SELECT e FROM DTOSchuelerSprachenfolge e WHERE e.Schueler_ID IN :value")
@NamedQuery(name = "DTOSchuelerSprachenfolge.sprache", query = "SELECT e FROM DTOSchuelerSprachenfolge e WHERE e.Sprache = :value")
@NamedQuery(name = "DTOSchuelerSprachenfolge.sprache.multiple", query = "SELECT e FROM DTOSchuelerSprachenfolge e WHERE e.Sprache IN :value")
@NamedQuery(name = "DTOSchuelerSprachenfolge.reihenfolgenr", query = "SELECT e FROM DTOSchuelerSprachenfolge e WHERE e.ReihenfolgeNr = :value")
@NamedQuery(name = "DTOSchuelerSprachenfolge.reihenfolgenr.multiple", query = "SELECT e FROM DTOSchuelerSprachenfolge e WHERE e.ReihenfolgeNr IN :value")
@NamedQuery(name = "DTOSchuelerSprachenfolge.asdjahrgangvon", query = "SELECT e FROM DTOSchuelerSprachenfolge e WHERE e.ASDJahrgangVon = :value")
@NamedQuery(name = "DTOSchuelerSprachenfolge.asdjahrgangvon.multiple", query = "SELECT e FROM DTOSchuelerSprachenfolge e WHERE e.ASDJahrgangVon IN :value")
@NamedQuery(name = "DTOSchuelerSprachenfolge.asdjahrgangbis", query = "SELECT e FROM DTOSchuelerSprachenfolge e WHERE e.ASDJahrgangBis = :value")
@NamedQuery(name = "DTOSchuelerSprachenfolge.asdjahrgangbis.multiple", query = "SELECT e FROM DTOSchuelerSprachenfolge e WHERE e.ASDJahrgangBis IN :value")
@NamedQuery(name = "DTOSchuelerSprachenfolge.abschnittvon", query = "SELECT e FROM DTOSchuelerSprachenfolge e WHERE e.AbschnittVon = :value")
@NamedQuery(name = "DTOSchuelerSprachenfolge.abschnittvon.multiple", query = "SELECT e FROM DTOSchuelerSprachenfolge e WHERE e.AbschnittVon IN :value")
@NamedQuery(name = "DTOSchuelerSprachenfolge.abschnittbis", query = "SELECT e FROM DTOSchuelerSprachenfolge e WHERE e.AbschnittBis = :value")
@NamedQuery(name = "DTOSchuelerSprachenfolge.abschnittbis.multiple", query = "SELECT e FROM DTOSchuelerSprachenfolge e WHERE e.AbschnittBis IN :value")
@NamedQuery(name = "DTOSchuelerSprachenfolge.referenzniveau", query = "SELECT e FROM DTOSchuelerSprachenfolge e WHERE e.Referenzniveau = :value")
@NamedQuery(name = "DTOSchuelerSprachenfolge.referenzniveau.multiple", query = "SELECT e FROM DTOSchuelerSprachenfolge e WHERE e.Referenzniveau IN :value")
@NamedQuery(name = "DTOSchuelerSprachenfolge.kleineslatinumerreicht", query = "SELECT e FROM DTOSchuelerSprachenfolge e WHERE e.KleinesLatinumErreicht = :value")
@NamedQuery(name = "DTOSchuelerSprachenfolge.kleineslatinumerreicht.multiple", query = "SELECT e FROM DTOSchuelerSprachenfolge e WHERE e.KleinesLatinumErreicht IN :value")
@NamedQuery(name = "DTOSchuelerSprachenfolge.latinumerreicht", query = "SELECT e FROM DTOSchuelerSprachenfolge e WHERE e.LatinumErreicht = :value")
@NamedQuery(name = "DTOSchuelerSprachenfolge.latinumerreicht.multiple", query = "SELECT e FROM DTOSchuelerSprachenfolge e WHERE e.LatinumErreicht IN :value")
@NamedQuery(name = "DTOSchuelerSprachenfolge.graecumerreicht", query = "SELECT e FROM DTOSchuelerSprachenfolge e WHERE e.GraecumErreicht = :value")
@NamedQuery(name = "DTOSchuelerSprachenfolge.graecumerreicht.multiple", query = "SELECT e FROM DTOSchuelerSprachenfolge e WHERE e.GraecumErreicht IN :value")
@NamedQuery(name = "DTOSchuelerSprachenfolge.hebraicumerreicht", query = "SELECT e FROM DTOSchuelerSprachenfolge e WHERE e.HebraicumErreicht = :value")
@NamedQuery(name = "DTOSchuelerSprachenfolge.hebraicumerreicht.multiple", query = "SELECT e FROM DTOSchuelerSprachenfolge e WHERE e.HebraicumErreicht IN :value")
@NamedQuery(name = "DTOSchuelerSprachenfolge.primaryKeyQuery", query = "SELECT e FROM DTOSchuelerSprachenfolge e WHERE e.ID = ?1")
@NamedQuery(name = "DTOSchuelerSprachenfolge.all.migration", query = "SELECT e FROM DTOSchuelerSprachenfolge e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Schueler_ID", "Sprache", "ReihenfolgeNr", "ASDJahrgangVon", "ASDJahrgangBis", "AbschnittVon", "AbschnittBis", "Referenzniveau", "KleinesLatinumErreicht", "LatinumErreicht", "GraecumErreicht", "HebraicumErreicht"})
public final class DTOSchuelerSprachenfolge {

	/** ID des Sprachenfolgeeintrags */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** SchülerID des Sprachenfolgeeintrags */
	@Column(name = "Schueler_ID")
	@JsonProperty
	public long Schueler_ID;

	/** Atomares Sprachkürzel aus StatKue_SVWS_ZulaessigeFaecher */
	@Column(name = "Sprache")
	@JsonProperty
	public String Sprache;

	/** Reihenfolge Nummer des Sprachenfolgeeintrags */
	@Column(name = "ReihenfolgeNr")
	@JsonProperty
	public Integer ReihenfolgeNr;

	/** ASDJahrgang Beginn des Sprachenfolgeeintrags */
	@Column(name = "ASDJahrgangVon")
	@JsonProperty
	public String ASDJahrgangVon;

	/** ASDJahrgang Ende des Sprachenfolgeeintrags */
	@Column(name = "ASDJahrgangBis")
	@JsonProperty
	public String ASDJahrgangBis;

	/** Abschnitt Beginn des Sprachenfolgeeintrags */
	@Column(name = "AbschnittVon")
	@JsonProperty
	public Integer AbschnittVon;

	/** Abschnitt Ende des Sprachenfolgeeintrags */
	@Column(name = "AbschnittBis")
	@JsonProperty
	public Integer AbschnittBis;

	/** Referenzniveau GeR des Sprachenfolgeeintrags */
	@Column(name = "Referenzniveau")
	@JsonProperty
	@Convert(converter = SprachreferenzniveauConverter.class)
	@JsonSerialize(using = SprachreferenzniveauConverterSerializer.class)
	@JsonDeserialize(using = SprachreferenzniveauConverterDeserializer.class)
	public Sprachreferenzniveau Referenzniveau;

	/** Gibt an, ob der Schüler das kleine Latinum erreicht hat */
	@Column(name = "KleinesLatinumErreicht")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean KleinesLatinumErreicht;

	/** Gibt an, ob der Schüler das Latinum erreicht hat */
	@Column(name = "LatinumErreicht")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean LatinumErreicht;

	/** Gibt an, ob der Schüler das Graecum erreicht hat */
	@Column(name = "GraecumErreicht")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean GraecumErreicht;

	/** Gibt an, ob der Schüler das Hebraicum erreicht hat */
	@Column(name = "HebraicumErreicht")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean HebraicumErreicht;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerSprachenfolge ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSchuelerSprachenfolge() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerSprachenfolge ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 * @param Sprache   der Wert für das Attribut Sprache
	 */
	public DTOSchuelerSprachenfolge(final long ID, final long Schueler_ID, final String Sprache) {
		this.ID = ID;
		this.Schueler_ID = Schueler_ID;
		if (Sprache == null) {
			throw new NullPointerException("Sprache must not be null");
		}
		this.Sprache = Sprache;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOSchuelerSprachenfolge other = (DTOSchuelerSprachenfolge) obj;
		return ID == other.ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(ID);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOSchuelerSprachenfolge(ID=" + this.ID + ", Schueler_ID=" + this.Schueler_ID + ", Sprache=" + this.Sprache + ", ReihenfolgeNr=" + this.ReihenfolgeNr + ", ASDJahrgangVon=" + this.ASDJahrgangVon + ", ASDJahrgangBis=" + this.ASDJahrgangBis + ", AbschnittVon=" + this.AbschnittVon + ", AbschnittBis=" + this.AbschnittBis + ", Referenzniveau=" + this.Referenzniveau + ", KleinesLatinumErreicht=" + this.KleinesLatinumErreicht + ", LatinumErreicht=" + this.LatinumErreicht + ", GraecumErreicht=" + this.GraecumErreicht + ", HebraicumErreicht=" + this.HebraicumErreicht + ")";
	}

}
