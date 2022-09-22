package de.nrw.schule.svws.db.dto.rev8.schild.katalog;

import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.converter.current.BooleanPlusMinusDefaultMinusConverter;
import de.nrw.schule.svws.db.converter.current.BooleanPlusMinusDefaultPlusConverter;


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
import de.nrw.schule.svws.csv.converter.current.BooleanPlusMinusDefaultMinusConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.BooleanPlusMinusDefaultMinusConverterDeserializer;
import de.nrw.schule.svws.csv.converter.current.BooleanPlusMinusDefaultPlusConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.BooleanPlusMinusDefaultPlusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle K_AllgAdresse.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "K_AllgAdresse")
@NamedQuery(name="Rev8DTOKatalogAllgemeineAdresse.all", query="SELECT e FROM Rev8DTOKatalogAllgemeineAdresse e")
@NamedQuery(name="Rev8DTOKatalogAllgemeineAdresse.id", query="SELECT e FROM Rev8DTOKatalogAllgemeineAdresse e WHERE e.ID = :value")
@NamedQuery(name="Rev8DTOKatalogAllgemeineAdresse.id.multiple", query="SELECT e FROM Rev8DTOKatalogAllgemeineAdresse e WHERE e.ID IN :value")
@NamedQuery(name="Rev8DTOKatalogAllgemeineAdresse.adressart", query="SELECT e FROM Rev8DTOKatalogAllgemeineAdresse e WHERE e.adressArt = :value")
@NamedQuery(name="Rev8DTOKatalogAllgemeineAdresse.adressart.multiple", query="SELECT e FROM Rev8DTOKatalogAllgemeineAdresse e WHERE e.adressArt IN :value")
@NamedQuery(name="Rev8DTOKatalogAllgemeineAdresse.name1", query="SELECT e FROM Rev8DTOKatalogAllgemeineAdresse e WHERE e.name1 = :value")
@NamedQuery(name="Rev8DTOKatalogAllgemeineAdresse.name1.multiple", query="SELECT e FROM Rev8DTOKatalogAllgemeineAdresse e WHERE e.name1 IN :value")
@NamedQuery(name="Rev8DTOKatalogAllgemeineAdresse.name2", query="SELECT e FROM Rev8DTOKatalogAllgemeineAdresse e WHERE e.name2 = :value")
@NamedQuery(name="Rev8DTOKatalogAllgemeineAdresse.name2.multiple", query="SELECT e FROM Rev8DTOKatalogAllgemeineAdresse e WHERE e.name2 IN :value")
@NamedQuery(name="Rev8DTOKatalogAllgemeineAdresse.strassenname", query="SELECT e FROM Rev8DTOKatalogAllgemeineAdresse e WHERE e.strassenname = :value")
@NamedQuery(name="Rev8DTOKatalogAllgemeineAdresse.strassenname.multiple", query="SELECT e FROM Rev8DTOKatalogAllgemeineAdresse e WHERE e.strassenname IN :value")
@NamedQuery(name="Rev8DTOKatalogAllgemeineAdresse.hausnr", query="SELECT e FROM Rev8DTOKatalogAllgemeineAdresse e WHERE e.hausnr = :value")
@NamedQuery(name="Rev8DTOKatalogAllgemeineAdresse.hausnr.multiple", query="SELECT e FROM Rev8DTOKatalogAllgemeineAdresse e WHERE e.hausnr IN :value")
@NamedQuery(name="Rev8DTOKatalogAllgemeineAdresse.hausnrzusatz", query="SELECT e FROM Rev8DTOKatalogAllgemeineAdresse e WHERE e.hausnrzusatz = :value")
@NamedQuery(name="Rev8DTOKatalogAllgemeineAdresse.hausnrzusatz.multiple", query="SELECT e FROM Rev8DTOKatalogAllgemeineAdresse e WHERE e.hausnrzusatz IN :value")
@NamedQuery(name="Rev8DTOKatalogAllgemeineAdresse.ort_id", query="SELECT e FROM Rev8DTOKatalogAllgemeineAdresse e WHERE e.ort_id = :value")
@NamedQuery(name="Rev8DTOKatalogAllgemeineAdresse.ort_id.multiple", query="SELECT e FROM Rev8DTOKatalogAllgemeineAdresse e WHERE e.ort_id IN :value")
@NamedQuery(name="Rev8DTOKatalogAllgemeineAdresse.telefon1", query="SELECT e FROM Rev8DTOKatalogAllgemeineAdresse e WHERE e.telefon1 = :value")
@NamedQuery(name="Rev8DTOKatalogAllgemeineAdresse.telefon1.multiple", query="SELECT e FROM Rev8DTOKatalogAllgemeineAdresse e WHERE e.telefon1 IN :value")
@NamedQuery(name="Rev8DTOKatalogAllgemeineAdresse.telefon2", query="SELECT e FROM Rev8DTOKatalogAllgemeineAdresse e WHERE e.telefon2 = :value")
@NamedQuery(name="Rev8DTOKatalogAllgemeineAdresse.telefon2.multiple", query="SELECT e FROM Rev8DTOKatalogAllgemeineAdresse e WHERE e.telefon2 IN :value")
@NamedQuery(name="Rev8DTOKatalogAllgemeineAdresse.fax", query="SELECT e FROM Rev8DTOKatalogAllgemeineAdresse e WHERE e.fax = :value")
@NamedQuery(name="Rev8DTOKatalogAllgemeineAdresse.fax.multiple", query="SELECT e FROM Rev8DTOKatalogAllgemeineAdresse e WHERE e.fax IN :value")
@NamedQuery(name="Rev8DTOKatalogAllgemeineAdresse.email", query="SELECT e FROM Rev8DTOKatalogAllgemeineAdresse e WHERE e.email = :value")
@NamedQuery(name="Rev8DTOKatalogAllgemeineAdresse.email.multiple", query="SELECT e FROM Rev8DTOKatalogAllgemeineAdresse e WHERE e.email IN :value")
@NamedQuery(name="Rev8DTOKatalogAllgemeineAdresse.bemerkungen", query="SELECT e FROM Rev8DTOKatalogAllgemeineAdresse e WHERE e.bemerkungen = :value")
@NamedQuery(name="Rev8DTOKatalogAllgemeineAdresse.bemerkungen.multiple", query="SELECT e FROM Rev8DTOKatalogAllgemeineAdresse e WHERE e.bemerkungen IN :value")
@NamedQuery(name="Rev8DTOKatalogAllgemeineAdresse.sortierung", query="SELECT e FROM Rev8DTOKatalogAllgemeineAdresse e WHERE e.sortierung = :value")
@NamedQuery(name="Rev8DTOKatalogAllgemeineAdresse.sortierung.multiple", query="SELECT e FROM Rev8DTOKatalogAllgemeineAdresse e WHERE e.sortierung IN :value")
@NamedQuery(name="Rev8DTOKatalogAllgemeineAdresse.ausbildungsbetrieb", query="SELECT e FROM Rev8DTOKatalogAllgemeineAdresse e WHERE e.ausbildungsbetrieb = :value")
@NamedQuery(name="Rev8DTOKatalogAllgemeineAdresse.ausbildungsbetrieb.multiple", query="SELECT e FROM Rev8DTOKatalogAllgemeineAdresse e WHERE e.ausbildungsbetrieb IN :value")
@NamedQuery(name="Rev8DTOKatalogAllgemeineAdresse.bietetpraktika", query="SELECT e FROM Rev8DTOKatalogAllgemeineAdresse e WHERE e.bietetPraktika = :value")
@NamedQuery(name="Rev8DTOKatalogAllgemeineAdresse.bietetpraktika.multiple", query="SELECT e FROM Rev8DTOKatalogAllgemeineAdresse e WHERE e.bietetPraktika IN :value")
@NamedQuery(name="Rev8DTOKatalogAllgemeineAdresse.branche", query="SELECT e FROM Rev8DTOKatalogAllgemeineAdresse e WHERE e.branche = :value")
@NamedQuery(name="Rev8DTOKatalogAllgemeineAdresse.branche.multiple", query="SELECT e FROM Rev8DTOKatalogAllgemeineAdresse e WHERE e.branche IN :value")
@NamedQuery(name="Rev8DTOKatalogAllgemeineAdresse.zusatz1", query="SELECT e FROM Rev8DTOKatalogAllgemeineAdresse e WHERE e.zusatz1 = :value")
@NamedQuery(name="Rev8DTOKatalogAllgemeineAdresse.zusatz1.multiple", query="SELECT e FROM Rev8DTOKatalogAllgemeineAdresse e WHERE e.zusatz1 IN :value")
@NamedQuery(name="Rev8DTOKatalogAllgemeineAdresse.zusatz2", query="SELECT e FROM Rev8DTOKatalogAllgemeineAdresse e WHERE e.zusatz2 = :value")
@NamedQuery(name="Rev8DTOKatalogAllgemeineAdresse.zusatz2.multiple", query="SELECT e FROM Rev8DTOKatalogAllgemeineAdresse e WHERE e.zusatz2 IN :value")
@NamedQuery(name="Rev8DTOKatalogAllgemeineAdresse.sichtbar", query="SELECT e FROM Rev8DTOKatalogAllgemeineAdresse e WHERE e.Sichtbar = :value")
@NamedQuery(name="Rev8DTOKatalogAllgemeineAdresse.sichtbar.multiple", query="SELECT e FROM Rev8DTOKatalogAllgemeineAdresse e WHERE e.Sichtbar IN :value")
@NamedQuery(name="Rev8DTOKatalogAllgemeineAdresse.aenderbar", query="SELECT e FROM Rev8DTOKatalogAllgemeineAdresse e WHERE e.Aenderbar = :value")
@NamedQuery(name="Rev8DTOKatalogAllgemeineAdresse.aenderbar.multiple", query="SELECT e FROM Rev8DTOKatalogAllgemeineAdresse e WHERE e.Aenderbar IN :value")
@NamedQuery(name="Rev8DTOKatalogAllgemeineAdresse.massnahmentraeger", query="SELECT e FROM Rev8DTOKatalogAllgemeineAdresse e WHERE e.Massnahmentraeger = :value")
@NamedQuery(name="Rev8DTOKatalogAllgemeineAdresse.massnahmentraeger.multiple", query="SELECT e FROM Rev8DTOKatalogAllgemeineAdresse e WHERE e.Massnahmentraeger IN :value")
@NamedQuery(name="Rev8DTOKatalogAllgemeineAdresse.belehrungisg", query="SELECT e FROM Rev8DTOKatalogAllgemeineAdresse e WHERE e.BelehrungISG = :value")
@NamedQuery(name="Rev8DTOKatalogAllgemeineAdresse.belehrungisg.multiple", query="SELECT e FROM Rev8DTOKatalogAllgemeineAdresse e WHERE e.BelehrungISG IN :value")
@NamedQuery(name="Rev8DTOKatalogAllgemeineAdresse.gu_id", query="SELECT e FROM Rev8DTOKatalogAllgemeineAdresse e WHERE e.GU_ID = :value")
@NamedQuery(name="Rev8DTOKatalogAllgemeineAdresse.gu_id.multiple", query="SELECT e FROM Rev8DTOKatalogAllgemeineAdresse e WHERE e.GU_ID IN :value")
@NamedQuery(name="Rev8DTOKatalogAllgemeineAdresse.erwfuehrungszeugnis", query="SELECT e FROM Rev8DTOKatalogAllgemeineAdresse e WHERE e.ErwFuehrungszeugnis = :value")
@NamedQuery(name="Rev8DTOKatalogAllgemeineAdresse.erwfuehrungszeugnis.multiple", query="SELECT e FROM Rev8DTOKatalogAllgemeineAdresse e WHERE e.ErwFuehrungszeugnis IN :value")
@NamedQuery(name="Rev8DTOKatalogAllgemeineAdresse.extid", query="SELECT e FROM Rev8DTOKatalogAllgemeineAdresse e WHERE e.ExtID = :value")
@NamedQuery(name="Rev8DTOKatalogAllgemeineAdresse.extid.multiple", query="SELECT e FROM Rev8DTOKatalogAllgemeineAdresse e WHERE e.ExtID IN :value")
@NamedQuery(name="Rev8DTOKatalogAllgemeineAdresse.primaryKeyQuery", query="SELECT e FROM Rev8DTOKatalogAllgemeineAdresse e WHERE e.ID = ?1")
@NamedQuery(name="Rev8DTOKatalogAllgemeineAdresse.all.migration", query="SELECT e FROM Rev8DTOKatalogAllgemeineAdresse e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","adressArt","name1","name2","strassenname","hausnr","hausnrzusatz","ort_id","telefon1","telefon2","fax","email","bemerkungen","sortierung","ausbildungsbetrieb","bietetPraktika","branche","zusatz1","zusatz2","Sichtbar","Aenderbar","Massnahmentraeger","BelehrungISG","GU_ID","ErwFuehrungszeugnis","ExtID"})
public class Rev8DTOKatalogAllgemeineAdresse {

	/** ID der weiteren Adresse (Betriebe) */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Adressart des Betriebs, Fremdschlüssel auf die ID in K_Adressart */
	@Column(name = "AdressArt_ID")
	@JsonProperty
	public Long adressArt;

	/** Name1 des Betriebs */
	@Column(name = "AllgAdrName1")
	@JsonProperty
	public String name1;

	/** Name2 des Betriebs */
	@Column(name = "AllgAdrName2")
	@JsonProperty
	public String name2;

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
	@Convert(converter=BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean ausbildungsbetrieb;

	/** Gibt an ob der Betrieb Praktikumsplätze bietet Ja Nein */
	@Column(name = "AllgAdrBietetPraktika")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultMinusConverterDeserializer.class)
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
	@Convert(converter=BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** Datensatz ist änderbar Ja Nein */
	@Column(name = "Aenderbar")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Aenderbar;

	/** Bezeichnung des Maßnahmenträgers */
	@Column(name = "Massnahmentraeger")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Massnahmentraeger;

	/** Belehrung nach Infektionsschutzgesetz notwendig Ja Nein */
	@Column(name = "BelehrungISG")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean BelehrungISG;

	/** GU_ID des Betriebsdatensatzes (für Import zur Erkennung) */
	@Column(name = "GU_ID")
	@JsonProperty
	public String GU_ID;

	/** Wird für diesen Betrieb ein Erweitertes Führungszeugnis benötigt? */
	@Column(name = "ErwFuehrungszeugnis")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean ErwFuehrungszeugnis;

	/** Externe ID des Betriebsdatensatzes */
	@Column(name = "ExtID")
	@JsonProperty
	public String ExtID;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOKatalogAllgemeineAdresse ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev8DTOKatalogAllgemeineAdresse() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOKatalogAllgemeineAdresse ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 */
	public Rev8DTOKatalogAllgemeineAdresse(final Long ID) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev8DTOKatalogAllgemeineAdresse other = (Rev8DTOKatalogAllgemeineAdresse) obj;
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
		return "Rev8DTOKatalogAllgemeineAdresse(ID=" + this.ID + ", adressArt=" + this.adressArt + ", name1=" + this.name1 + ", name2=" + this.name2 + ", strassenname=" + this.strassenname + ", hausnr=" + this.hausnr + ", hausnrzusatz=" + this.hausnrzusatz + ", ort_id=" + this.ort_id + ", telefon1=" + this.telefon1 + ", telefon2=" + this.telefon2 + ", fax=" + this.fax + ", email=" + this.email + ", bemerkungen=" + this.bemerkungen + ", sortierung=" + this.sortierung + ", ausbildungsbetrieb=" + this.ausbildungsbetrieb + ", bietetPraktika=" + this.bietetPraktika + ", branche=" + this.branche + ", zusatz1=" + this.zusatz1 + ", zusatz2=" + this.zusatz2 + ", Sichtbar=" + this.Sichtbar + ", Aenderbar=" + this.Aenderbar + ", Massnahmentraeger=" + this.Massnahmentraeger + ", BelehrungISG=" + this.BelehrungISG + ", GU_ID=" + this.GU_ID + ", ErwFuehrungszeugnis=" + this.ErwFuehrungszeugnis + ", ExtID=" + this.ExtID + ")";
	}

}