package de.svws_nrw.db.dto.current.schild.schueler;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.Boolean01Converter;


import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.svws_nrw.csv.converter.current.Boolean01ConverterSerializer;
import de.svws_nrw.csv.converter.current.Boolean01ConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerSprachenfolge.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerSprachenfolge")
@JsonPropertyOrder({"ID", "Schueler_ID", "Sprache", "ReihenfolgeNr", "ASDJahrgangVon", "ASDJahrgangBis", "AbschnittVon", "AbschnittBis", "Referenzniveau", "KleinesLatinumErreicht", "LatinumErreicht", "GraecumErreicht", "HebraicumErreicht"})
public final class DTOSchuelerSprachenfolge {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOSchuelerSprachenfolge e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOSchuelerSprachenfolge e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOSchuelerSprachenfolge e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOSchuelerSprachenfolge e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOSchuelerSprachenfolge e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOSchuelerSprachenfolge e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schueler_ID */
	public static final String QUERY_BY_SCHUELER_ID = "SELECT e FROM DTOSchuelerSprachenfolge e WHERE e.Schueler_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schueler_ID */
	public static final String QUERY_LIST_BY_SCHUELER_ID = "SELECT e FROM DTOSchuelerSprachenfolge e WHERE e.Schueler_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Sprache */
	public static final String QUERY_BY_SPRACHE = "SELECT e FROM DTOSchuelerSprachenfolge e WHERE e.Sprache = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Sprache */
	public static final String QUERY_LIST_BY_SPRACHE = "SELECT e FROM DTOSchuelerSprachenfolge e WHERE e.Sprache IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ReihenfolgeNr */
	public static final String QUERY_BY_REIHENFOLGENR = "SELECT e FROM DTOSchuelerSprachenfolge e WHERE e.ReihenfolgeNr = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ReihenfolgeNr */
	public static final String QUERY_LIST_BY_REIHENFOLGENR = "SELECT e FROM DTOSchuelerSprachenfolge e WHERE e.ReihenfolgeNr IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ASDJahrgangVon */
	public static final String QUERY_BY_ASDJAHRGANGVON = "SELECT e FROM DTOSchuelerSprachenfolge e WHERE e.ASDJahrgangVon = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ASDJahrgangVon */
	public static final String QUERY_LIST_BY_ASDJAHRGANGVON = "SELECT e FROM DTOSchuelerSprachenfolge e WHERE e.ASDJahrgangVon IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ASDJahrgangBis */
	public static final String QUERY_BY_ASDJAHRGANGBIS = "SELECT e FROM DTOSchuelerSprachenfolge e WHERE e.ASDJahrgangBis = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ASDJahrgangBis */
	public static final String QUERY_LIST_BY_ASDJAHRGANGBIS = "SELECT e FROM DTOSchuelerSprachenfolge e WHERE e.ASDJahrgangBis IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AbschnittVon */
	public static final String QUERY_BY_ABSCHNITTVON = "SELECT e FROM DTOSchuelerSprachenfolge e WHERE e.AbschnittVon = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AbschnittVon */
	public static final String QUERY_LIST_BY_ABSCHNITTVON = "SELECT e FROM DTOSchuelerSprachenfolge e WHERE e.AbschnittVon IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AbschnittBis */
	public static final String QUERY_BY_ABSCHNITTBIS = "SELECT e FROM DTOSchuelerSprachenfolge e WHERE e.AbschnittBis = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AbschnittBis */
	public static final String QUERY_LIST_BY_ABSCHNITTBIS = "SELECT e FROM DTOSchuelerSprachenfolge e WHERE e.AbschnittBis IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Referenzniveau */
	public static final String QUERY_BY_REFERENZNIVEAU = "SELECT e FROM DTOSchuelerSprachenfolge e WHERE e.Referenzniveau = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Referenzniveau */
	public static final String QUERY_LIST_BY_REFERENZNIVEAU = "SELECT e FROM DTOSchuelerSprachenfolge e WHERE e.Referenzniveau IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes KleinesLatinumErreicht */
	public static final String QUERY_BY_KLEINESLATINUMERREICHT = "SELECT e FROM DTOSchuelerSprachenfolge e WHERE e.KleinesLatinumErreicht = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes KleinesLatinumErreicht */
	public static final String QUERY_LIST_BY_KLEINESLATINUMERREICHT = "SELECT e FROM DTOSchuelerSprachenfolge e WHERE e.KleinesLatinumErreicht IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LatinumErreicht */
	public static final String QUERY_BY_LATINUMERREICHT = "SELECT e FROM DTOSchuelerSprachenfolge e WHERE e.LatinumErreicht = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LatinumErreicht */
	public static final String QUERY_LIST_BY_LATINUMERREICHT = "SELECT e FROM DTOSchuelerSprachenfolge e WHERE e.LatinumErreicht IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes GraecumErreicht */
	public static final String QUERY_BY_GRAECUMERREICHT = "SELECT e FROM DTOSchuelerSprachenfolge e WHERE e.GraecumErreicht = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes GraecumErreicht */
	public static final String QUERY_LIST_BY_GRAECUMERREICHT = "SELECT e FROM DTOSchuelerSprachenfolge e WHERE e.GraecumErreicht IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes HebraicumErreicht */
	public static final String QUERY_BY_HEBRAICUMERREICHT = "SELECT e FROM DTOSchuelerSprachenfolge e WHERE e.HebraicumErreicht = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes HebraicumErreicht */
	public static final String QUERY_LIST_BY_HEBRAICUMERREICHT = "SELECT e FROM DTOSchuelerSprachenfolge e WHERE e.HebraicumErreicht IN ?1";

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
	public String Referenzniveau;

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
