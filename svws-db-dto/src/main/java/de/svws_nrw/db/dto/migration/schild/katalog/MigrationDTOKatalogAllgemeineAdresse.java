package de.svws_nrw.db.dto.migration.schild.katalog;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverter;
import de.svws_nrw.db.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverter;


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
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverterSerializer;
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverterDeserializer;
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverterSerializer;
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle K_AllgAdresse.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "K_AllgAdresse")
@JsonPropertyOrder({"ID", "AllgAdrAdressArt", "name1", "name2", "strasse", "strassenname", "hausnr", "hausnrzusatz", "ort_id", "ortsteil_id", "plz", "telefon1", "telefon2", "fax", "email", "bemerkungen", "sortierung", "ausbildungsbetrieb", "bietetPraktika", "branche", "zusatz1", "zusatz2", "Sichtbar", "Aenderbar", "SchulnrEigner", "Massnahmentraeger", "BelehrungISG", "GU_ID", "ErwFuehrungszeugnis", "ExtID"})
public final class MigrationDTOKatalogAllgemeineAdresse {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AllgAdrAdressArt */
	public static final String QUERY_BY_ALLGADRADRESSART = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.AllgAdrAdressArt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AllgAdrAdressArt */
	public static final String QUERY_LIST_BY_ALLGADRADRESSART = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.AllgAdrAdressArt IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes name1 */
	public static final String QUERY_BY_NAME1 = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.name1 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes name1 */
	public static final String QUERY_LIST_BY_NAME1 = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.name1 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes name2 */
	public static final String QUERY_BY_NAME2 = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.name2 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes name2 */
	public static final String QUERY_LIST_BY_NAME2 = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.name2 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes strasse */
	public static final String QUERY_BY_STRASSE = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.strasse = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes strasse */
	public static final String QUERY_LIST_BY_STRASSE = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.strasse IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes strassenname */
	public static final String QUERY_BY_STRASSENNAME = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.strassenname = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes strassenname */
	public static final String QUERY_LIST_BY_STRASSENNAME = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.strassenname IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes hausnr */
	public static final String QUERY_BY_HAUSNR = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.hausnr = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes hausnr */
	public static final String QUERY_LIST_BY_HAUSNR = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.hausnr IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes hausnrzusatz */
	public static final String QUERY_BY_HAUSNRZUSATZ = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.hausnrzusatz = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes hausnrzusatz */
	public static final String QUERY_LIST_BY_HAUSNRZUSATZ = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.hausnrzusatz IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ort_id */
	public static final String QUERY_BY_ORT_ID = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.ort_id = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ort_id */
	public static final String QUERY_LIST_BY_ORT_ID = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.ort_id IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ortsteil_id */
	public static final String QUERY_BY_ORTSTEIL_ID = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.ortsteil_id = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ortsteil_id */
	public static final String QUERY_LIST_BY_ORTSTEIL_ID = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.ortsteil_id IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes plz */
	public static final String QUERY_BY_PLZ = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.plz = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes plz */
	public static final String QUERY_LIST_BY_PLZ = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.plz IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes telefon1 */
	public static final String QUERY_BY_TELEFON1 = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.telefon1 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes telefon1 */
	public static final String QUERY_LIST_BY_TELEFON1 = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.telefon1 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes telefon2 */
	public static final String QUERY_BY_TELEFON2 = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.telefon2 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes telefon2 */
	public static final String QUERY_LIST_BY_TELEFON2 = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.telefon2 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes fax */
	public static final String QUERY_BY_FAX = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.fax = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes fax */
	public static final String QUERY_LIST_BY_FAX = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.fax IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes email */
	public static final String QUERY_BY_EMAIL = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.email = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes email */
	public static final String QUERY_LIST_BY_EMAIL = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.email IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes bemerkungen */
	public static final String QUERY_BY_BEMERKUNGEN = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.bemerkungen = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes bemerkungen */
	public static final String QUERY_LIST_BY_BEMERKUNGEN = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.bemerkungen IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes sortierung */
	public static final String QUERY_BY_SORTIERUNG = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.sortierung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes sortierung */
	public static final String QUERY_LIST_BY_SORTIERUNG = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.sortierung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ausbildungsbetrieb */
	public static final String QUERY_BY_AUSBILDUNGSBETRIEB = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.ausbildungsbetrieb = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ausbildungsbetrieb */
	public static final String QUERY_LIST_BY_AUSBILDUNGSBETRIEB = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.ausbildungsbetrieb IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes bietetPraktika */
	public static final String QUERY_BY_BIETETPRAKTIKA = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.bietetPraktika = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes bietetPraktika */
	public static final String QUERY_LIST_BY_BIETETPRAKTIKA = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.bietetPraktika IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes branche */
	public static final String QUERY_BY_BRANCHE = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.branche = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes branche */
	public static final String QUERY_LIST_BY_BRANCHE = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.branche IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes zusatz1 */
	public static final String QUERY_BY_ZUSATZ1 = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.zusatz1 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes zusatz1 */
	public static final String QUERY_LIST_BY_ZUSATZ1 = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.zusatz1 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes zusatz2 */
	public static final String QUERY_BY_ZUSATZ2 = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.zusatz2 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes zusatz2 */
	public static final String QUERY_LIST_BY_ZUSATZ2 = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.zusatz2 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Sichtbar */
	public static final String QUERY_BY_SICHTBAR = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.Sichtbar = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Sichtbar */
	public static final String QUERY_LIST_BY_SICHTBAR = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.Sichtbar IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Aenderbar */
	public static final String QUERY_BY_AENDERBAR = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.Aenderbar = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Aenderbar */
	public static final String QUERY_LIST_BY_AENDERBAR = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.Aenderbar IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchulnrEigner */
	public static final String QUERY_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.SchulnrEigner = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchulnrEigner */
	public static final String QUERY_LIST_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.SchulnrEigner IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Massnahmentraeger */
	public static final String QUERY_BY_MASSNAHMENTRAEGER = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.Massnahmentraeger = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Massnahmentraeger */
	public static final String QUERY_LIST_BY_MASSNAHMENTRAEGER = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.Massnahmentraeger IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes BelehrungISG */
	public static final String QUERY_BY_BELEHRUNGISG = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.BelehrungISG = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes BelehrungISG */
	public static final String QUERY_LIST_BY_BELEHRUNGISG = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.BelehrungISG IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes GU_ID */
	public static final String QUERY_BY_GU_ID = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.GU_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes GU_ID */
	public static final String QUERY_LIST_BY_GU_ID = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.GU_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ErwFuehrungszeugnis */
	public static final String QUERY_BY_ERWFUEHRUNGSZEUGNIS = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.ErwFuehrungszeugnis = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ErwFuehrungszeugnis */
	public static final String QUERY_LIST_BY_ERWFUEHRUNGSZEUGNIS = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.ErwFuehrungszeugnis IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ExtID */
	public static final String QUERY_BY_EXTID = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.ExtID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ExtID */
	public static final String QUERY_LIST_BY_EXTID = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.ExtID IN ?1";

	/** ID der weiteren Adresse (Betriebe) */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** DEPRECATED Die Bezeichnung der Adressart des Betriebs */
	@Column(name = "AllgAdrAdressArt")
	@JsonProperty
	public String AllgAdrAdressArt;

	/** Name1 des Betriebs */
	@Column(name = "AllgAdrName1")
	@JsonProperty
	public String name1;

	/** Name2 des Betriebs */
	@Column(name = "AllgAdrName2")
	@JsonProperty
	public String name2;

	/** Straße des Betriebs */
	@Column(name = "AllgAdrStrasse")
	@JsonProperty
	public String strasse;

	/** Straßenname des Betriebsdatensatz */
	@Column(name = "AllgAdrStrassenname")
	@JsonProperty
	public String strassenname;

	/** Hausnummer wenn getrennt gespeichert */
	@Column(name = "AllgAdrHausNr")
	@JsonProperty
	public String hausnr;

	/** Zusatz zur Hausnummer wenn Hausnummern getrennt gespeichert werden */
	@Column(name = "AllgAdrHausNrZusatz")
	@JsonProperty
	public String hausnrzusatz;

	/** OrtID des Betriebs */
	@Column(name = "AllgAdrOrt_ID")
	@JsonProperty
	public Long ort_id;

	/** OrtsteilID des Betriebs */
	@Column(name = "AllgOrtsteil_ID")
	@JsonProperty
	public Long ortsteil_id;

	/** DEPRECATED: PLZ des Betriebs */
	@Column(name = "AllgAdrPLZ")
	@JsonProperty
	public String plz;

	/** Telefonnummer1 des Betriebs */
	@Column(name = "AllgAdrTelefon1")
	@JsonProperty
	public String telefon1;

	/** Telefonnummer2 des Betriebs */
	@Column(name = "AllgAdrTelefon2")
	@JsonProperty
	public String telefon2;

	/** Faxnummer des Betriebs */
	@Column(name = "AllgAdrFax")
	@JsonProperty
	public String fax;

	/** E-MailAdresse des Betriebes */
	@Column(name = "AllgAdrEmail")
	@JsonProperty
	public String email;

	/** Bemerkung zum Betrieb */
	@Column(name = "AllgAdrBemerkungen")
	@JsonProperty
	public String bemerkungen;

	/** Sortierung des Betriebsdatensatz */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer sortierung;

	/** Gibt an ob der Betrieb ausbildet Ja Nein */
	@Column(name = "AllgAdrAusbildungsBetrieb")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean ausbildungsbetrieb;

	/** Gibt an ob der Betrieb Praktikumsplätze bietet Ja Nein */
	@Column(name = "AllgAdrBietetPraktika")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean bietetPraktika;

	/** Brache des Betriebs */
	@Column(name = "AllgAdrBranche")
	@JsonProperty
	public String branche;

	/** Adresszusatz zum Betrieb */
	@Column(name = "AllgAdrZusatz1")
	@JsonProperty
	public String zusatz1;

	/** Adresszusatz2 zum Betrieb */
	@Column(name = "AllgAdrZusatz2")
	@JsonProperty
	public String zusatz2;

	/** Sichtbarkeit des Datensatzes */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** Datensatz ist änderbar Ja Nein */
	@Column(name = "Aenderbar")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Aenderbar;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/** Bezeichnung des Maßnahmenträgers */
	@Column(name = "Massnahmentraeger")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Massnahmentraeger;

	/** Belehrung nach Infektionsschutzgesetz notwendig Ja Nein */
	@Column(name = "BelehrungISG")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean BelehrungISG;

	/** GU_ID des Betriebsdatensatzes (für Import zur Erkennung) */
	@Column(name = "GU_ID")
	@JsonProperty
	public String GU_ID;

	/** Wird für diesen Betrieb ein Erweitertes Führungszeugnis benötigt? */
	@Column(name = "ErwFuehrungszeugnis")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean ErwFuehrungszeugnis;

	/** Externe ID des Betriebsdatensatzes */
	@Column(name = "ExtID")
	@JsonProperty
	public String ExtID;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOKatalogAllgemeineAdresse ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOKatalogAllgemeineAdresse() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOKatalogAllgemeineAdresse ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 */
	public MigrationDTOKatalogAllgemeineAdresse(final Long ID) {
		if (ID == null) {
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOKatalogAllgemeineAdresse other = (MigrationDTOKatalogAllgemeineAdresse) obj;
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
		return "MigrationDTOKatalogAllgemeineAdresse(ID=" + this.ID + ", AllgAdrAdressArt=" + this.AllgAdrAdressArt + ", name1=" + this.name1 + ", name2=" + this.name2 + ", strasse=" + this.strasse + ", strassenname=" + this.strassenname + ", hausnr=" + this.hausnr + ", hausnrzusatz=" + this.hausnrzusatz + ", ort_id=" + this.ort_id + ", ortsteil_id=" + this.ortsteil_id + ", plz=" + this.plz + ", telefon1=" + this.telefon1 + ", telefon2=" + this.telefon2 + ", fax=" + this.fax + ", email=" + this.email + ", bemerkungen=" + this.bemerkungen + ", sortierung=" + this.sortierung + ", ausbildungsbetrieb=" + this.ausbildungsbetrieb + ", bietetPraktika=" + this.bietetPraktika + ", branche=" + this.branche + ", zusatz1=" + this.zusatz1 + ", zusatz2=" + this.zusatz2 + ", Sichtbar=" + this.Sichtbar + ", Aenderbar=" + this.Aenderbar + ", SchulnrEigner=" + this.SchulnrEigner + ", Massnahmentraeger=" + this.Massnahmentraeger + ", BelehrungISG=" + this.BelehrungISG + ", GU_ID=" + this.GU_ID + ", ErwFuehrungszeugnis=" + this.ErwFuehrungszeugnis + ", ExtID=" + this.ExtID + ")";
	}

}
