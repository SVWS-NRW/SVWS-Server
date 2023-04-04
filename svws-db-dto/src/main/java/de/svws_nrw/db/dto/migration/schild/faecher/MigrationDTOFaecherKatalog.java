package de.svws_nrw.db.dto.migration.schild.faecher;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.migration.MigrationBoolean01Converter;


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
@NamedQuery(name = "MigrationDTOFaecherKatalog.all", query = "SELECT e FROM MigrationDTOFaecherKatalog e")
@NamedQuery(name = "MigrationDTOFaecherKatalog.id", query = "SELECT e FROM MigrationDTOFaecherKatalog e WHERE e.ID = :value")
@NamedQuery(name = "MigrationDTOFaecherKatalog.id.multiple", query = "SELECT e FROM MigrationDTOFaecherKatalog e WHERE e.ID IN :value")
@NamedQuery(name = "MigrationDTOFaecherKatalog.kuerzelasd", query = "SELECT e FROM MigrationDTOFaecherKatalog e WHERE e.KuerzelASD = :value")
@NamedQuery(name = "MigrationDTOFaecherKatalog.kuerzelasd.multiple", query = "SELECT e FROM MigrationDTOFaecherKatalog e WHERE e.KuerzelASD IN :value")
@NamedQuery(name = "MigrationDTOFaecherKatalog.bezeichnung", query = "SELECT e FROM MigrationDTOFaecherKatalog e WHERE e.Bezeichnung = :value")
@NamedQuery(name = "MigrationDTOFaecherKatalog.bezeichnung.multiple", query = "SELECT e FROM MigrationDTOFaecherKatalog e WHERE e.Bezeichnung IN :value")
@NamedQuery(name = "MigrationDTOFaecherKatalog.kuerzel", query = "SELECT e FROM MigrationDTOFaecherKatalog e WHERE e.Kuerzel = :value")
@NamedQuery(name = "MigrationDTOFaecherKatalog.kuerzel.multiple", query = "SELECT e FROM MigrationDTOFaecherKatalog e WHERE e.Kuerzel IN :value")
@NamedQuery(name = "MigrationDTOFaecherKatalog.aufgabenfeld", query = "SELECT e FROM MigrationDTOFaecherKatalog e WHERE e.Aufgabenfeld = :value")
@NamedQuery(name = "MigrationDTOFaecherKatalog.aufgabenfeld.multiple", query = "SELECT e FROM MigrationDTOFaecherKatalog e WHERE e.Aufgabenfeld IN :value")
@NamedQuery(name = "MigrationDTOFaecherKatalog.fachgruppe", query = "SELECT e FROM MigrationDTOFaecherKatalog e WHERE e.Fachgruppe = :value")
@NamedQuery(name = "MigrationDTOFaecherKatalog.fachgruppe.multiple", query = "SELECT e FROM MigrationDTOFaecherKatalog e WHERE e.Fachgruppe IN :value")
@NamedQuery(name = "MigrationDTOFaecherKatalog.jahrgangab", query = "SELECT e FROM MigrationDTOFaecherKatalog e WHERE e.JahrgangAb = :value")
@NamedQuery(name = "MigrationDTOFaecherKatalog.jahrgangab.multiple", query = "SELECT e FROM MigrationDTOFaecherKatalog e WHERE e.JahrgangAb IN :value")
@NamedQuery(name = "MigrationDTOFaecherKatalog.istfremdsprache", query = "SELECT e FROM MigrationDTOFaecherKatalog e WHERE e.IstFremdsprache = :value")
@NamedQuery(name = "MigrationDTOFaecherKatalog.istfremdsprache.multiple", query = "SELECT e FROM MigrationDTOFaecherKatalog e WHERE e.IstFremdsprache IN :value")
@NamedQuery(name = "MigrationDTOFaecherKatalog.isthkfs", query = "SELECT e FROM MigrationDTOFaecherKatalog e WHERE e.IstHKFS = :value")
@NamedQuery(name = "MigrationDTOFaecherKatalog.isthkfs.multiple", query = "SELECT e FROM MigrationDTOFaecherKatalog e WHERE e.IstHKFS IN :value")
@NamedQuery(name = "MigrationDTOFaecherKatalog.istausregufach", query = "SELECT e FROM MigrationDTOFaecherKatalog e WHERE e.IstAusRegUFach = :value")
@NamedQuery(name = "MigrationDTOFaecherKatalog.istausregufach.multiple", query = "SELECT e FROM MigrationDTOFaecherKatalog e WHERE e.IstAusRegUFach IN :value")
@NamedQuery(name = "MigrationDTOFaecherKatalog.istersatzpflichtfs", query = "SELECT e FROM MigrationDTOFaecherKatalog e WHERE e.IstErsatzPflichtFS = :value")
@NamedQuery(name = "MigrationDTOFaecherKatalog.istersatzpflichtfs.multiple", query = "SELECT e FROM MigrationDTOFaecherKatalog e WHERE e.IstErsatzPflichtFS IN :value")
@NamedQuery(name = "MigrationDTOFaecherKatalog.istkonfkoop", query = "SELECT e FROM MigrationDTOFaecherKatalog e WHERE e.IstKonfKoop = :value")
@NamedQuery(name = "MigrationDTOFaecherKatalog.istkonfkoop.multiple", query = "SELECT e FROM MigrationDTOFaecherKatalog e WHERE e.IstKonfKoop IN :value")
@NamedQuery(name = "MigrationDTOFaecherKatalog.nursii", query = "SELECT e FROM MigrationDTOFaecherKatalog e WHERE e.NurSII = :value")
@NamedQuery(name = "MigrationDTOFaecherKatalog.nursii.multiple", query = "SELECT e FROM MigrationDTOFaecherKatalog e WHERE e.NurSII IN :value")
@NamedQuery(name = "MigrationDTOFaecherKatalog.exportasd", query = "SELECT e FROM MigrationDTOFaecherKatalog e WHERE e.ExportASD = :value")
@NamedQuery(name = "MigrationDTOFaecherKatalog.exportasd.multiple", query = "SELECT e FROM MigrationDTOFaecherKatalog e WHERE e.ExportASD IN :value")
@NamedQuery(name = "MigrationDTOFaecherKatalog.gueltigvon", query = "SELECT e FROM MigrationDTOFaecherKatalog e WHERE e.gueltigVon = :value")
@NamedQuery(name = "MigrationDTOFaecherKatalog.gueltigvon.multiple", query = "SELECT e FROM MigrationDTOFaecherKatalog e WHERE e.gueltigVon IN :value")
@NamedQuery(name = "MigrationDTOFaecherKatalog.gueltigbis", query = "SELECT e FROM MigrationDTOFaecherKatalog e WHERE e.gueltigBis = :value")
@NamedQuery(name = "MigrationDTOFaecherKatalog.gueltigbis.multiple", query = "SELECT e FROM MigrationDTOFaecherKatalog e WHERE e.gueltigBis IN :value")
@NamedQuery(name = "MigrationDTOFaecherKatalog.primaryKeyQuery", query = "SELECT e FROM MigrationDTOFaecherKatalog e WHERE e.ID = ?1")
@NamedQuery(name = "MigrationDTOFaecherKatalog.all.migration", query = "SELECT e FROM MigrationDTOFaecherKatalog e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "KuerzelASD", "Bezeichnung", "Kuerzel", "Aufgabenfeld", "Fachgruppe", "JahrgangAb", "IstFremdsprache", "IstHKFS", "IstAusRegUFach", "IstErsatzPflichtFS", "IstKonfKoop", "NurSII", "ExportASD", "gueltigVon", "gueltigBis"})
public final class MigrationDTOFaecherKatalog {

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
