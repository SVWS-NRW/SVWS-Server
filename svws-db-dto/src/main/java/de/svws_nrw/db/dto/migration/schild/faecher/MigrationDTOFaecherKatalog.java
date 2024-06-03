package de.svws_nrw.db.dto.migration.schild.faecher;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.migration.MigrationBoolean01Converter;


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
import de.svws_nrw.csv.converter.migration.MigrationBoolean01ConverterSerializer;
import de.svws_nrw.csv.converter.migration.MigrationBoolean01ConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle FachKatalog.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "FachKatalog")
@JsonPropertyOrder({"ID", "KuerzelASD", "Bezeichnung", "Kuerzel", "Aufgabenfeld", "Fachgruppe", "JahrgangAb", "IstFremdsprache", "IstHKFS", "IstAusRegUFach", "IstErsatzPflichtFS", "IstKonfKoop", "NurSII", "ExportASD", "gueltigVon", "gueltigBis"})
public final class MigrationDTOFaecherKatalog {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM MigrationDTOFaecherKatalog e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM MigrationDTOFaecherKatalog e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM MigrationDTOFaecherKatalog e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM MigrationDTOFaecherKatalog e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM MigrationDTOFaecherKatalog e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM MigrationDTOFaecherKatalog e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes KuerzelASD */
	public static final String QUERY_BY_KUERZELASD = "SELECT e FROM MigrationDTOFaecherKatalog e WHERE e.KuerzelASD = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes KuerzelASD */
	public static final String QUERY_LIST_BY_KUERZELASD = "SELECT e FROM MigrationDTOFaecherKatalog e WHERE e.KuerzelASD IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Bezeichnung */
	public static final String QUERY_BY_BEZEICHNUNG = "SELECT e FROM MigrationDTOFaecherKatalog e WHERE e.Bezeichnung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Bezeichnung */
	public static final String QUERY_LIST_BY_BEZEICHNUNG = "SELECT e FROM MigrationDTOFaecherKatalog e WHERE e.Bezeichnung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Kuerzel */
	public static final String QUERY_BY_KUERZEL = "SELECT e FROM MigrationDTOFaecherKatalog e WHERE e.Kuerzel = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Kuerzel */
	public static final String QUERY_LIST_BY_KUERZEL = "SELECT e FROM MigrationDTOFaecherKatalog e WHERE e.Kuerzel IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Aufgabenfeld */
	public static final String QUERY_BY_AUFGABENFELD = "SELECT e FROM MigrationDTOFaecherKatalog e WHERE e.Aufgabenfeld = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Aufgabenfeld */
	public static final String QUERY_LIST_BY_AUFGABENFELD = "SELECT e FROM MigrationDTOFaecherKatalog e WHERE e.Aufgabenfeld IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Fachgruppe */
	public static final String QUERY_BY_FACHGRUPPE = "SELECT e FROM MigrationDTOFaecherKatalog e WHERE e.Fachgruppe = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Fachgruppe */
	public static final String QUERY_LIST_BY_FACHGRUPPE = "SELECT e FROM MigrationDTOFaecherKatalog e WHERE e.Fachgruppe IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes JahrgangAb */
	public static final String QUERY_BY_JAHRGANGAB = "SELECT e FROM MigrationDTOFaecherKatalog e WHERE e.JahrgangAb = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes JahrgangAb */
	public static final String QUERY_LIST_BY_JAHRGANGAB = "SELECT e FROM MigrationDTOFaecherKatalog e WHERE e.JahrgangAb IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes IstFremdsprache */
	public static final String QUERY_BY_ISTFREMDSPRACHE = "SELECT e FROM MigrationDTOFaecherKatalog e WHERE e.IstFremdsprache = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes IstFremdsprache */
	public static final String QUERY_LIST_BY_ISTFREMDSPRACHE = "SELECT e FROM MigrationDTOFaecherKatalog e WHERE e.IstFremdsprache IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes IstHKFS */
	public static final String QUERY_BY_ISTHKFS = "SELECT e FROM MigrationDTOFaecherKatalog e WHERE e.IstHKFS = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes IstHKFS */
	public static final String QUERY_LIST_BY_ISTHKFS = "SELECT e FROM MigrationDTOFaecherKatalog e WHERE e.IstHKFS IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes IstAusRegUFach */
	public static final String QUERY_BY_ISTAUSREGUFACH = "SELECT e FROM MigrationDTOFaecherKatalog e WHERE e.IstAusRegUFach = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes IstAusRegUFach */
	public static final String QUERY_LIST_BY_ISTAUSREGUFACH = "SELECT e FROM MigrationDTOFaecherKatalog e WHERE e.IstAusRegUFach IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes IstErsatzPflichtFS */
	public static final String QUERY_BY_ISTERSATZPFLICHTFS = "SELECT e FROM MigrationDTOFaecherKatalog e WHERE e.IstErsatzPflichtFS = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes IstErsatzPflichtFS */
	public static final String QUERY_LIST_BY_ISTERSATZPFLICHTFS = "SELECT e FROM MigrationDTOFaecherKatalog e WHERE e.IstErsatzPflichtFS IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes IstKonfKoop */
	public static final String QUERY_BY_ISTKONFKOOP = "SELECT e FROM MigrationDTOFaecherKatalog e WHERE e.IstKonfKoop = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes IstKonfKoop */
	public static final String QUERY_LIST_BY_ISTKONFKOOP = "SELECT e FROM MigrationDTOFaecherKatalog e WHERE e.IstKonfKoop IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes NurSII */
	public static final String QUERY_BY_NURSII = "SELECT e FROM MigrationDTOFaecherKatalog e WHERE e.NurSII = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes NurSII */
	public static final String QUERY_LIST_BY_NURSII = "SELECT e FROM MigrationDTOFaecherKatalog e WHERE e.NurSII IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ExportASD */
	public static final String QUERY_BY_EXPORTASD = "SELECT e FROM MigrationDTOFaecherKatalog e WHERE e.ExportASD = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ExportASD */
	public static final String QUERY_LIST_BY_EXPORTASD = "SELECT e FROM MigrationDTOFaecherKatalog e WHERE e.ExportASD IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes gueltigVon */
	public static final String QUERY_BY_GUELTIGVON = "SELECT e FROM MigrationDTOFaecherKatalog e WHERE e.gueltigVon = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes gueltigVon */
	public static final String QUERY_LIST_BY_GUELTIGVON = "SELECT e FROM MigrationDTOFaecherKatalog e WHERE e.gueltigVon IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes gueltigBis */
	public static final String QUERY_BY_GUELTIGBIS = "SELECT e FROM MigrationDTOFaecherKatalog e WHERE e.gueltigBis = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes gueltigBis */
	public static final String QUERY_LIST_BY_GUELTIGBIS = "SELECT e FROM MigrationDTOFaecherKatalog e WHERE e.gueltigBis IN ?1";

	/** ID des Faches */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Das eindeutige Kürzel des Faches entsprechend der Vorgaben der amtlichen Schulstatistik */
	@Column(name = "KuerzelASD")
	@JsonProperty
	public String KuerzelASD;

	/** Die texttuelle Beschreibung des Faches */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Das atomare Kürzel des Faches (z.B. bei Fremdsprachen - für das Sprachenkürzel) - Teil des Kürzels für die amtliche Schulstatistik */
	@Column(name = "Kuerzel")
	@JsonProperty
	public String Kuerzel;

	/** Das Aufgabenfeld, welchem das Fach ggf. zugeordnet ist (1, 2 oder 3) */
	@Column(name = "Aufgabenfeld")
	@JsonProperty
	public Integer Aufgabenfeld;

	/** Das Kürzel der zugeordneten Fachgruppe */
	@Column(name = "Fachgruppe")
	@JsonProperty
	public String Fachgruppe;

	/** Der ASD-Jahrgang, ab dem das Fach zulässig ist (z.B. bei Fremdsprachen) */
	@Column(name = "JahrgangAb")
	@JsonProperty
	public String JahrgangAb;

	/** Gibt an, ob es sich um ein Fach der Herkuntftsprache handelt (Unterrichts in der Herkunftssprache oder Herkunftssprache anstelle einer Pflichtfremdsprache) */
	@Column(name = "IstFremdsprache")
	@JsonProperty
	@Convert(converter = MigrationBoolean01Converter.class)
	@JsonSerialize(using = MigrationBoolean01ConverterSerializer.class)
	@JsonDeserialize(using = MigrationBoolean01ConverterDeserializer.class)
	public Boolean IstFremdsprache;

	/** Gibt an, ob es sich um ein Fach der Herkuntftsprache handelt (Unterrichts in der Herkunftssprache oder Herkunftssprache anstelle einer Pflichtfremdsprache) */
	@Column(name = "IstHKFS")
	@JsonProperty
	@Convert(converter = MigrationBoolean01Converter.class)
	@JsonSerialize(using = MigrationBoolean01ConverterSerializer.class)
	@JsonDeserialize(using = MigrationBoolean01ConverterDeserializer.class)
	public Boolean IstHKFS;

	/** Gibt an, ob das Fach außerhalb des regulären Fachunterichts unterrichtet wird */
	@Column(name = "IstAusRegUFach")
	@JsonProperty
	@Convert(converter = MigrationBoolean01Converter.class)
	@JsonSerialize(using = MigrationBoolean01ConverterSerializer.class)
	@JsonDeserialize(using = MigrationBoolean01ConverterDeserializer.class)
	public Boolean IstAusRegUFach;

	/** Gibt an, ob es sich bei dem Fach um einen Ersatz für eine Pflichtfremdsprache handelt (siehe auch istHKFS) */
	@Column(name = "IstErsatzPflichtFS")
	@JsonProperty
	@Convert(converter = MigrationBoolean01Converter.class)
	@JsonSerialize(using = MigrationBoolean01ConverterSerializer.class)
	@JsonDeserialize(using = MigrationBoolean01ConverterDeserializer.class)
	public Boolean IstErsatzPflichtFS;

	/** Gibt an, ob das Religionsfach konfessionell kooperativ unterrichtet wird oder nicht - Teil des Kürzels für die amtliche Schulstatistik */
	@Column(name = "IstKonfKoop")
	@JsonProperty
	@Convert(converter = MigrationBoolean01Converter.class)
	@JsonSerialize(using = MigrationBoolean01ConverterSerializer.class)
	@JsonDeserialize(using = MigrationBoolean01ConverterDeserializer.class)
	public Boolean IstKonfKoop;

	/** Gibt an, ob das Fach nur in der Sekundarstufe II unterrichtet wird */
	@Column(name = "NurSII")
	@JsonProperty
	@Convert(converter = MigrationBoolean01Converter.class)
	@JsonSerialize(using = MigrationBoolean01ConverterSerializer.class)
	@JsonDeserialize(using = MigrationBoolean01ConverterDeserializer.class)
	public Boolean NurSII;

	/** Gibt an, ob das Fach bei Export der amtlichen Schulstatistik berücksichtigt werden soll oder nicht */
	@Column(name = "ExportASD")
	@JsonProperty
	@Convert(converter = MigrationBoolean01Converter.class)
	@JsonSerialize(using = MigrationBoolean01ConverterSerializer.class)
	@JsonDeserialize(using = MigrationBoolean01ConverterDeserializer.class)
	public Boolean ExportASD;

	/** Gibt die Gültigkeit ab welchem Schuljahr an */
	@Column(name = "gueltigVon")
	@JsonProperty
	public Integer gueltigVon;

	/** Gibt die Gültigkeit bis zu welchem Schuljahr an */
	@Column(name = "gueltigBis")
	@JsonProperty
	public Integer gueltigBis;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOFaecherKatalog ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOFaecherKatalog() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOFaecherKatalog ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param KuerzelASD   der Wert für das Attribut KuerzelASD
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 * @param Kuerzel   der Wert für das Attribut Kuerzel
	 * @param IstFremdsprache   der Wert für das Attribut IstFremdsprache
	 * @param IstHKFS   der Wert für das Attribut IstHKFS
	 * @param IstAusRegUFach   der Wert für das Attribut IstAusRegUFach
	 * @param IstErsatzPflichtFS   der Wert für das Attribut IstErsatzPflichtFS
	 * @param IstKonfKoop   der Wert für das Attribut IstKonfKoop
	 * @param NurSII   der Wert für das Attribut NurSII
	 * @param ExportASD   der Wert für das Attribut ExportASD
	 */
	public MigrationDTOFaecherKatalog(final Long ID, final String KuerzelASD, final String Bezeichnung, final String Kuerzel, final Boolean IstFremdsprache, final Boolean IstHKFS, final Boolean IstAusRegUFach, final Boolean IstErsatzPflichtFS, final Boolean IstKonfKoop, final Boolean NurSII, final Boolean ExportASD) {
		if (ID == null) {
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (KuerzelASD == null) {
			throw new NullPointerException("KuerzelASD must not be null");
		}
		this.KuerzelASD = KuerzelASD;
		if (Bezeichnung == null) {
			throw new NullPointerException("Bezeichnung must not be null");
		}
		this.Bezeichnung = Bezeichnung;
		if (Kuerzel == null) {
			throw new NullPointerException("Kuerzel must not be null");
		}
		this.Kuerzel = Kuerzel;
		if (IstFremdsprache == null) {
			throw new NullPointerException("IstFremdsprache must not be null");
		}
		this.IstFremdsprache = IstFremdsprache;
		if (IstHKFS == null) {
			throw new NullPointerException("IstHKFS must not be null");
		}
		this.IstHKFS = IstHKFS;
		if (IstAusRegUFach == null) {
			throw new NullPointerException("IstAusRegUFach must not be null");
		}
		this.IstAusRegUFach = IstAusRegUFach;
		if (IstErsatzPflichtFS == null) {
			throw new NullPointerException("IstErsatzPflichtFS must not be null");
		}
		this.IstErsatzPflichtFS = IstErsatzPflichtFS;
		if (IstKonfKoop == null) {
			throw new NullPointerException("IstKonfKoop must not be null");
		}
		this.IstKonfKoop = IstKonfKoop;
		if (NurSII == null) {
			throw new NullPointerException("NurSII must not be null");
		}
		this.NurSII = NurSII;
		if (ExportASD == null) {
			throw new NullPointerException("ExportASD must not be null");
		}
		this.ExportASD = ExportASD;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOFaecherKatalog other = (MigrationDTOFaecherKatalog) obj;
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
		return "MigrationDTOFaecherKatalog(ID=" + this.ID + ", KuerzelASD=" + this.KuerzelASD + ", Bezeichnung=" + this.Bezeichnung + ", Kuerzel=" + this.Kuerzel + ", Aufgabenfeld=" + this.Aufgabenfeld + ", Fachgruppe=" + this.Fachgruppe + ", JahrgangAb=" + this.JahrgangAb + ", IstFremdsprache=" + this.IstFremdsprache + ", IstHKFS=" + this.IstHKFS + ", IstAusRegUFach=" + this.IstAusRegUFach + ", IstErsatzPflichtFS=" + this.IstErsatzPflichtFS + ", IstKonfKoop=" + this.IstKonfKoop + ", NurSII=" + this.NurSII + ", ExportASD=" + this.ExportASD + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ")";
	}

}
