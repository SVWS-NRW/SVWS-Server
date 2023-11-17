package de.svws_nrw.db.dto.migration.schild.katalog;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverter;
import de.svws_nrw.db.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverter;


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
@NamedQuery(name = "MigrationDTOKatalogAllgemeineAdresse.all", query = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e")
@NamedQuery(name = "MigrationDTOKatalogAllgemeineAdresse.id", query = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.ID = :value")
@NamedQuery(name = "MigrationDTOKatalogAllgemeineAdresse.id.multiple", query = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.ID IN :value")
@NamedQuery(name = "MigrationDTOKatalogAllgemeineAdresse.allgadradressart", query = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.AllgAdrAdressArt = :value")
@NamedQuery(name = "MigrationDTOKatalogAllgemeineAdresse.allgadradressart.multiple", query = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.AllgAdrAdressArt IN :value")
@NamedQuery(name = "MigrationDTOKatalogAllgemeineAdresse.name1", query = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.name1 = :value")
@NamedQuery(name = "MigrationDTOKatalogAllgemeineAdresse.name1.multiple", query = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.name1 IN :value")
@NamedQuery(name = "MigrationDTOKatalogAllgemeineAdresse.name2", query = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.name2 = :value")
@NamedQuery(name = "MigrationDTOKatalogAllgemeineAdresse.name2.multiple", query = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.name2 IN :value")
@NamedQuery(name = "MigrationDTOKatalogAllgemeineAdresse.strasse", query = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.strasse = :value")
@NamedQuery(name = "MigrationDTOKatalogAllgemeineAdresse.strasse.multiple", query = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.strasse IN :value")
@NamedQuery(name = "MigrationDTOKatalogAllgemeineAdresse.strassenname", query = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.strassenname = :value")
@NamedQuery(name = "MigrationDTOKatalogAllgemeineAdresse.strassenname.multiple", query = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.strassenname IN :value")
@NamedQuery(name = "MigrationDTOKatalogAllgemeineAdresse.hausnr", query = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.hausnr = :value")
@NamedQuery(name = "MigrationDTOKatalogAllgemeineAdresse.hausnr.multiple", query = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.hausnr IN :value")
@NamedQuery(name = "MigrationDTOKatalogAllgemeineAdresse.hausnrzusatz", query = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.hausnrzusatz = :value")
@NamedQuery(name = "MigrationDTOKatalogAllgemeineAdresse.hausnrzusatz.multiple", query = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.hausnrzusatz IN :value")
@NamedQuery(name = "MigrationDTOKatalogAllgemeineAdresse.ort_id", query = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.ort_id = :value")
@NamedQuery(name = "MigrationDTOKatalogAllgemeineAdresse.ort_id.multiple", query = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.ort_id IN :value")
@NamedQuery(name = "MigrationDTOKatalogAllgemeineAdresse.ortsteil_id", query = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.ortsteil_id = :value")
@NamedQuery(name = "MigrationDTOKatalogAllgemeineAdresse.ortsteil_id.multiple", query = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.ortsteil_id IN :value")
@NamedQuery(name = "MigrationDTOKatalogAllgemeineAdresse.plz", query = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.plz = :value")
@NamedQuery(name = "MigrationDTOKatalogAllgemeineAdresse.plz.multiple", query = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.plz IN :value")
@NamedQuery(name = "MigrationDTOKatalogAllgemeineAdresse.telefon1", query = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.telefon1 = :value")
@NamedQuery(name = "MigrationDTOKatalogAllgemeineAdresse.telefon1.multiple", query = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.telefon1 IN :value")
@NamedQuery(name = "MigrationDTOKatalogAllgemeineAdresse.telefon2", query = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.telefon2 = :value")
@NamedQuery(name = "MigrationDTOKatalogAllgemeineAdresse.telefon2.multiple", query = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.telefon2 IN :value")
@NamedQuery(name = "MigrationDTOKatalogAllgemeineAdresse.fax", query = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.fax = :value")
@NamedQuery(name = "MigrationDTOKatalogAllgemeineAdresse.fax.multiple", query = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.fax IN :value")
@NamedQuery(name = "MigrationDTOKatalogAllgemeineAdresse.email", query = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.email = :value")
@NamedQuery(name = "MigrationDTOKatalogAllgemeineAdresse.email.multiple", query = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.email IN :value")
@NamedQuery(name = "MigrationDTOKatalogAllgemeineAdresse.bemerkungen", query = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.bemerkungen = :value")
@NamedQuery(name = "MigrationDTOKatalogAllgemeineAdresse.bemerkungen.multiple", query = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.bemerkungen IN :value")
@NamedQuery(name = "MigrationDTOKatalogAllgemeineAdresse.sortierung", query = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.sortierung = :value")
@NamedQuery(name = "MigrationDTOKatalogAllgemeineAdresse.sortierung.multiple", query = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.sortierung IN :value")
@NamedQuery(name = "MigrationDTOKatalogAllgemeineAdresse.ausbildungsbetrieb", query = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.ausbildungsbetrieb = :value")
@NamedQuery(name = "MigrationDTOKatalogAllgemeineAdresse.ausbildungsbetrieb.multiple", query = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.ausbildungsbetrieb IN :value")
@NamedQuery(name = "MigrationDTOKatalogAllgemeineAdresse.bietetpraktika", query = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.bietetPraktika = :value")
@NamedQuery(name = "MigrationDTOKatalogAllgemeineAdresse.bietetpraktika.multiple", query = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.bietetPraktika IN :value")
@NamedQuery(name = "MigrationDTOKatalogAllgemeineAdresse.branche", query = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.branche = :value")
@NamedQuery(name = "MigrationDTOKatalogAllgemeineAdresse.branche.multiple", query = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.branche IN :value")
@NamedQuery(name = "MigrationDTOKatalogAllgemeineAdresse.zusatz1", query = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.zusatz1 = :value")
@NamedQuery(name = "MigrationDTOKatalogAllgemeineAdresse.zusatz1.multiple", query = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.zusatz1 IN :value")
@NamedQuery(name = "MigrationDTOKatalogAllgemeineAdresse.zusatz2", query = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.zusatz2 = :value")
@NamedQuery(name = "MigrationDTOKatalogAllgemeineAdresse.zusatz2.multiple", query = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.zusatz2 IN :value")
@NamedQuery(name = "MigrationDTOKatalogAllgemeineAdresse.sichtbar", query = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.Sichtbar = :value")
@NamedQuery(name = "MigrationDTOKatalogAllgemeineAdresse.sichtbar.multiple", query = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.Sichtbar IN :value")
@NamedQuery(name = "MigrationDTOKatalogAllgemeineAdresse.aenderbar", query = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.Aenderbar = :value")
@NamedQuery(name = "MigrationDTOKatalogAllgemeineAdresse.aenderbar.multiple", query = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.Aenderbar IN :value")
@NamedQuery(name = "MigrationDTOKatalogAllgemeineAdresse.schulnreigner", query = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.SchulnrEigner = :value")
@NamedQuery(name = "MigrationDTOKatalogAllgemeineAdresse.schulnreigner.multiple", query = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.SchulnrEigner IN :value")
@NamedQuery(name = "MigrationDTOKatalogAllgemeineAdresse.massnahmentraeger", query = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.Massnahmentraeger = :value")
@NamedQuery(name = "MigrationDTOKatalogAllgemeineAdresse.massnahmentraeger.multiple", query = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.Massnahmentraeger IN :value")
@NamedQuery(name = "MigrationDTOKatalogAllgemeineAdresse.belehrungisg", query = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.BelehrungISG = :value")
@NamedQuery(name = "MigrationDTOKatalogAllgemeineAdresse.belehrungisg.multiple", query = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.BelehrungISG IN :value")
@NamedQuery(name = "MigrationDTOKatalogAllgemeineAdresse.gu_id", query = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.GU_ID = :value")
@NamedQuery(name = "MigrationDTOKatalogAllgemeineAdresse.gu_id.multiple", query = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.GU_ID IN :value")
@NamedQuery(name = "MigrationDTOKatalogAllgemeineAdresse.erwfuehrungszeugnis", query = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.ErwFuehrungszeugnis = :value")
@NamedQuery(name = "MigrationDTOKatalogAllgemeineAdresse.erwfuehrungszeugnis.multiple", query = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.ErwFuehrungszeugnis IN :value")
@NamedQuery(name = "MigrationDTOKatalogAllgemeineAdresse.extid", query = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.ExtID = :value")
@NamedQuery(name = "MigrationDTOKatalogAllgemeineAdresse.extid.multiple", query = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.ExtID IN :value")
@NamedQuery(name = "MigrationDTOKatalogAllgemeineAdresse.primaryKeyQuery", query = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.ID = ?1")
@NamedQuery(name = "MigrationDTOKatalogAllgemeineAdresse.primaryKeyQuery.multiple", query = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.ID IN :value")
@NamedQuery(name = "MigrationDTOKatalogAllgemeineAdresse.all.migration", query = "SELECT e FROM MigrationDTOKatalogAllgemeineAdresse e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "AllgAdrAdressArt", "name1", "name2", "strasse", "strassenname", "hausnr", "hausnrzusatz", "ort_id", "ortsteil_id", "plz", "telefon1", "telefon2", "fax", "email", "bemerkungen", "sortierung", "ausbildungsbetrieb", "bietetPraktika", "branche", "zusatz1", "zusatz2", "Sichtbar", "Aenderbar", "SchulnrEigner", "Massnahmentraeger", "BelehrungISG", "GU_ID", "ErwFuehrungszeugnis", "ExtID"})
public final class MigrationDTOKatalogAllgemeineAdresse {

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
