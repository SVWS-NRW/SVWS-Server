package de.svws_nrw.db.dto.current.schild.lehrer;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultPlusConverter;
import de.svws_nrw.db.converter.current.DatumConverter;
import de.svws_nrw.db.converter.current.GeschlechtConverterFromString;
import de.svws_nrw.db.converter.current.NationalitaetenConverter;
import de.svws_nrw.db.converter.current.PersonalTypConverter;

import de.svws_nrw.core.types.Geschlecht;
import de.svws_nrw.core.types.PersonalTyp;
import de.svws_nrw.core.types.schule.Nationalitaeten;


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
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultPlusConverterSerializer;
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultPlusConverterDeserializer;
import de.svws_nrw.csv.converter.current.DatumConverterSerializer;
import de.svws_nrw.csv.converter.current.DatumConverterDeserializer;
import de.svws_nrw.csv.converter.current.GeschlechtConverterFromStringSerializer;
import de.svws_nrw.csv.converter.current.GeschlechtConverterFromStringDeserializer;
import de.svws_nrw.csv.converter.current.NationalitaetenConverterSerializer;
import de.svws_nrw.csv.converter.current.NationalitaetenConverterDeserializer;
import de.svws_nrw.csv.converter.current.PersonalTypConverterSerializer;
import de.svws_nrw.csv.converter.current.PersonalTypConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle K_Lehrer.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "K_Lehrer")
@NamedQuery(name = "DTOLehrer.all", query = "SELECT e FROM DTOLehrer e")
@NamedQuery(name = "DTOLehrer.id", query = "SELECT e FROM DTOLehrer e WHERE e.ID = :value")
@NamedQuery(name = "DTOLehrer.id.multiple", query = "SELECT e FROM DTOLehrer e WHERE e.ID IN :value")
@NamedQuery(name = "DTOLehrer.gu_id", query = "SELECT e FROM DTOLehrer e WHERE e.GU_ID = :value")
@NamedQuery(name = "DTOLehrer.gu_id.multiple", query = "SELECT e FROM DTOLehrer e WHERE e.GU_ID IN :value")
@NamedQuery(name = "DTOLehrer.kuerzel", query = "SELECT e FROM DTOLehrer e WHERE e.Kuerzel = :value")
@NamedQuery(name = "DTOLehrer.kuerzel.multiple", query = "SELECT e FROM DTOLehrer e WHERE e.Kuerzel IN :value")
@NamedQuery(name = "DTOLehrer.kuerzellid", query = "SELECT e FROM DTOLehrer e WHERE e.kuerzelLID = :value")
@NamedQuery(name = "DTOLehrer.kuerzellid.multiple", query = "SELECT e FROM DTOLehrer e WHERE e.kuerzelLID IN :value")
@NamedQuery(name = "DTOLehrer.nachname", query = "SELECT e FROM DTOLehrer e WHERE e.Nachname = :value")
@NamedQuery(name = "DTOLehrer.nachname.multiple", query = "SELECT e FROM DTOLehrer e WHERE e.Nachname IN :value")
@NamedQuery(name = "DTOLehrer.vorname", query = "SELECT e FROM DTOLehrer e WHERE e.Vorname = :value")
@NamedQuery(name = "DTOLehrer.vorname.multiple", query = "SELECT e FROM DTOLehrer e WHERE e.Vorname IN :value")
@NamedQuery(name = "DTOLehrer.persontyp", query = "SELECT e FROM DTOLehrer e WHERE e.PersonTyp = :value")
@NamedQuery(name = "DTOLehrer.persontyp.multiple", query = "SELECT e FROM DTOLehrer e WHERE e.PersonTyp IN :value")
@NamedQuery(name = "DTOLehrer.sortierung", query = "SELECT e FROM DTOLehrer e WHERE e.Sortierung = :value")
@NamedQuery(name = "DTOLehrer.sortierung.multiple", query = "SELECT e FROM DTOLehrer e WHERE e.Sortierung IN :value")
@NamedQuery(name = "DTOLehrer.sichtbar", query = "SELECT e FROM DTOLehrer e WHERE e.Sichtbar = :value")
@NamedQuery(name = "DTOLehrer.sichtbar.multiple", query = "SELECT e FROM DTOLehrer e WHERE e.Sichtbar IN :value")
@NamedQuery(name = "DTOLehrer.aenderbar", query = "SELECT e FROM DTOLehrer e WHERE e.Aenderbar = :value")
@NamedQuery(name = "DTOLehrer.aenderbar.multiple", query = "SELECT e FROM DTOLehrer e WHERE e.Aenderbar IN :value")
@NamedQuery(name = "DTOLehrer.fuerexport", query = "SELECT e FROM DTOLehrer e WHERE e.FuerExport = :value")
@NamedQuery(name = "DTOLehrer.fuerexport.multiple", query = "SELECT e FROM DTOLehrer e WHERE e.FuerExport IN :value")
@NamedQuery(name = "DTOLehrer.statistikrelevant", query = "SELECT e FROM DTOLehrer e WHERE e.statistikRelevant = :value")
@NamedQuery(name = "DTOLehrer.statistikrelevant.multiple", query = "SELECT e FROM DTOLehrer e WHERE e.statistikRelevant IN :value")
@NamedQuery(name = "DTOLehrer.strassenname", query = "SELECT e FROM DTOLehrer e WHERE e.Strassenname = :value")
@NamedQuery(name = "DTOLehrer.strassenname.multiple", query = "SELECT e FROM DTOLehrer e WHERE e.Strassenname IN :value")
@NamedQuery(name = "DTOLehrer.hausnr", query = "SELECT e FROM DTOLehrer e WHERE e.HausNr = :value")
@NamedQuery(name = "DTOLehrer.hausnr.multiple", query = "SELECT e FROM DTOLehrer e WHERE e.HausNr IN :value")
@NamedQuery(name = "DTOLehrer.hausnrzusatz", query = "SELECT e FROM DTOLehrer e WHERE e.HausNrZusatz = :value")
@NamedQuery(name = "DTOLehrer.hausnrzusatz.multiple", query = "SELECT e FROM DTOLehrer e WHERE e.HausNrZusatz IN :value")
@NamedQuery(name = "DTOLehrer.ort_id", query = "SELECT e FROM DTOLehrer e WHERE e.Ort_ID = :value")
@NamedQuery(name = "DTOLehrer.ort_id.multiple", query = "SELECT e FROM DTOLehrer e WHERE e.Ort_ID IN :value")
@NamedQuery(name = "DTOLehrer.ortsteil_id", query = "SELECT e FROM DTOLehrer e WHERE e.Ortsteil_ID = :value")
@NamedQuery(name = "DTOLehrer.ortsteil_id.multiple", query = "SELECT e FROM DTOLehrer e WHERE e.Ortsteil_ID IN :value")
@NamedQuery(name = "DTOLehrer.telefon", query = "SELECT e FROM DTOLehrer e WHERE e.telefon = :value")
@NamedQuery(name = "DTOLehrer.telefon.multiple", query = "SELECT e FROM DTOLehrer e WHERE e.telefon IN :value")
@NamedQuery(name = "DTOLehrer.telefonmobil", query = "SELECT e FROM DTOLehrer e WHERE e.telefonMobil = :value")
@NamedQuery(name = "DTOLehrer.telefonmobil.multiple", query = "SELECT e FROM DTOLehrer e WHERE e.telefonMobil IN :value")
@NamedQuery(name = "DTOLehrer.emailprivat", query = "SELECT e FROM DTOLehrer e WHERE e.eMailPrivat = :value")
@NamedQuery(name = "DTOLehrer.emailprivat.multiple", query = "SELECT e FROM DTOLehrer e WHERE e.eMailPrivat IN :value")
@NamedQuery(name = "DTOLehrer.emaildienstlich", query = "SELECT e FROM DTOLehrer e WHERE e.eMailDienstlich = :value")
@NamedQuery(name = "DTOLehrer.emaildienstlich.multiple", query = "SELECT e FROM DTOLehrer e WHERE e.eMailDienstlich IN :value")
@NamedQuery(name = "DTOLehrer.staatsangehoerigkeit", query = "SELECT e FROM DTOLehrer e WHERE e.staatsangehoerigkeit = :value")
@NamedQuery(name = "DTOLehrer.staatsangehoerigkeit.multiple", query = "SELECT e FROM DTOLehrer e WHERE e.staatsangehoerigkeit IN :value")
@NamedQuery(name = "DTOLehrer.geburtsdatum", query = "SELECT e FROM DTOLehrer e WHERE e.Geburtsdatum = :value")
@NamedQuery(name = "DTOLehrer.geburtsdatum.multiple", query = "SELECT e FROM DTOLehrer e WHERE e.Geburtsdatum IN :value")
@NamedQuery(name = "DTOLehrer.geschlecht", query = "SELECT e FROM DTOLehrer e WHERE e.Geschlecht = :value")
@NamedQuery(name = "DTOLehrer.geschlecht.multiple", query = "SELECT e FROM DTOLehrer e WHERE e.Geschlecht IN :value")
@NamedQuery(name = "DTOLehrer.anrede", query = "SELECT e FROM DTOLehrer e WHERE e.Anrede = :value")
@NamedQuery(name = "DTOLehrer.anrede.multiple", query = "SELECT e FROM DTOLehrer e WHERE e.Anrede IN :value")
@NamedQuery(name = "DTOLehrer.amtsbezeichnung", query = "SELECT e FROM DTOLehrer e WHERE e.Amtsbezeichnung = :value")
@NamedQuery(name = "DTOLehrer.amtsbezeichnung.multiple", query = "SELECT e FROM DTOLehrer e WHERE e.Amtsbezeichnung IN :value")
@NamedQuery(name = "DTOLehrer.titel", query = "SELECT e FROM DTOLehrer e WHERE e.Titel = :value")
@NamedQuery(name = "DTOLehrer.titel.multiple", query = "SELECT e FROM DTOLehrer e WHERE e.Titel IN :value")
@NamedQuery(name = "DTOLehrer.faecher", query = "SELECT e FROM DTOLehrer e WHERE e.Faecher = :value")
@NamedQuery(name = "DTOLehrer.faecher.multiple", query = "SELECT e FROM DTOLehrer e WHERE e.Faecher IN :value")
@NamedQuery(name = "DTOLehrer.identnrteil1", query = "SELECT e FROM DTOLehrer e WHERE e.identNrTeil1 = :value")
@NamedQuery(name = "DTOLehrer.identnrteil1.multiple", query = "SELECT e FROM DTOLehrer e WHERE e.identNrTeil1 IN :value")
@NamedQuery(name = "DTOLehrer.identnrteil2sernr", query = "SELECT e FROM DTOLehrer e WHERE e.identNrTeil2SerNr = :value")
@NamedQuery(name = "DTOLehrer.identnrteil2sernr.multiple", query = "SELECT e FROM DTOLehrer e WHERE e.identNrTeil2SerNr IN :value")
@NamedQuery(name = "DTOLehrer.panr", query = "SELECT e FROM DTOLehrer e WHERE e.PANr = :value")
@NamedQuery(name = "DTOLehrer.panr.multiple", query = "SELECT e FROM DTOLehrer e WHERE e.PANr IN :value")
@NamedQuery(name = "DTOLehrer.personalnrlbv", query = "SELECT e FROM DTOLehrer e WHERE e.personalNrLBV = :value")
@NamedQuery(name = "DTOLehrer.personalnrlbv.multiple", query = "SELECT e FROM DTOLehrer e WHERE e.personalNrLBV IN :value")
@NamedQuery(name = "DTOLehrer.verguetungsschluessel", query = "SELECT e FROM DTOLehrer e WHERE e.verguetungsSchluessel = :value")
@NamedQuery(name = "DTOLehrer.verguetungsschluessel.multiple", query = "SELECT e FROM DTOLehrer e WHERE e.verguetungsSchluessel IN :value")
@NamedQuery(name = "DTOLehrer.datumzugang", query = "SELECT e FROM DTOLehrer e WHERE e.DatumZugang = :value")
@NamedQuery(name = "DTOLehrer.datumzugang.multiple", query = "SELECT e FROM DTOLehrer e WHERE e.DatumZugang IN :value")
@NamedQuery(name = "DTOLehrer.grundzugang", query = "SELECT e FROM DTOLehrer e WHERE e.GrundZugang = :value")
@NamedQuery(name = "DTOLehrer.grundzugang.multiple", query = "SELECT e FROM DTOLehrer e WHERE e.GrundZugang IN :value")
@NamedQuery(name = "DTOLehrer.datumabgang", query = "SELECT e FROM DTOLehrer e WHERE e.DatumAbgang = :value")
@NamedQuery(name = "DTOLehrer.datumabgang.multiple", query = "SELECT e FROM DTOLehrer e WHERE e.DatumAbgang IN :value")
@NamedQuery(name = "DTOLehrer.grundabgang", query = "SELECT e FROM DTOLehrer e WHERE e.GrundAbgang = :value")
@NamedQuery(name = "DTOLehrer.grundabgang.multiple", query = "SELECT e FROM DTOLehrer e WHERE e.GrundAbgang IN :value")
@NamedQuery(name = "DTOLehrer.kennworttools", query = "SELECT e FROM DTOLehrer e WHERE e.KennwortTools = :value")
@NamedQuery(name = "DTOLehrer.kennworttools.multiple", query = "SELECT e FROM DTOLehrer e WHERE e.KennwortTools IN :value")
@NamedQuery(name = "DTOLehrer.kennworttoolsaktuell", query = "SELECT e FROM DTOLehrer e WHERE e.KennwortToolsAktuell = :value")
@NamedQuery(name = "DTOLehrer.kennworttoolsaktuell.multiple", query = "SELECT e FROM DTOLehrer e WHERE e.KennwortToolsAktuell IN :value")
@NamedQuery(name = "DTOLehrer.credentialid", query = "SELECT e FROM DTOLehrer e WHERE e.CredentialID = :value")
@NamedQuery(name = "DTOLehrer.credentialid.multiple", query = "SELECT e FROM DTOLehrer e WHERE e.CredentialID IN :value")
@NamedQuery(name = "DTOLehrer.primaryKeyQuery", query = "SELECT e FROM DTOLehrer e WHERE e.ID = ?1")
@NamedQuery(name = "DTOLehrer.all.migration", query = "SELECT e FROM DTOLehrer e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "GU_ID", "Kuerzel", "kuerzelLID", "Nachname", "Vorname", "PersonTyp", "Sortierung", "Sichtbar", "Aenderbar", "FuerExport", "statistikRelevant", "Strassenname", "HausNr", "HausNrZusatz", "Ort_ID", "Ortsteil_ID", "telefon", "telefonMobil", "eMailPrivat", "eMailDienstlich", "staatsangehoerigkeit", "Geburtsdatum", "Geschlecht", "Anrede", "Amtsbezeichnung", "Titel", "Faecher", "identNrTeil1", "identNrTeil2SerNr", "PANr", "personalNrLBV", "verguetungsSchluessel", "DatumZugang", "GrundZugang", "DatumAbgang", "GrundAbgang", "KennwortTools", "KennwortToolsAktuell", "CredentialID"})
public final class DTOLehrer {

	/** Eindeutige ID zur Kennzeichnung des Lehrer-Datensatzes */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Eindeutige ID Datenbank übergreifend. Wurde früher mal für Logineo genutzt, kann später mal zur Identifizierung genutzt werden. */
	@Column(name = "GU_ID")
	@JsonProperty
	public String GU_ID;

	/** Lehrer-Kürzel für eine lesbare eindeutige Identifikation des Lehrers */
	@Column(name = "Kuerzel")
	@JsonProperty
	public String Kuerzel;

	/** Lehrer-Kürzel für eine eindeutige Identifikation des Lehrers – Verwendung für die Statistik - TODO lassen sich kuerzel und LIDKrz nicht sinnvoll zusammenfassen? */
	@Column(name = "LIDKrz")
	@JsonProperty
	public String kuerzelLID;

	/** Der Nachname des Lehrers PAuswG vom 21.6.2019 §5 Abs. 2 */
	@Column(name = "Nachname")
	@JsonProperty
	public String Nachname;

	/** Der Vorname des Lehrers PAuswG vom 21.6.2019 §5 Abs. 2. Wird im Client mit Rufname angezeigt. */
	@Column(name = "Vorname")
	@JsonProperty
	public String Vorname;

	/** Die Art der Person – wurde nachträglich hinzugefügt, damit auch Nicht-Lehrer in die Liste aufgenommen und unterschieden werden können */
	@Column(name = "PersonTyp")
	@JsonProperty
	@Convert(converter = PersonalTypConverter.class)
	@JsonSerialize(using = PersonalTypConverterSerializer.class)
	@JsonDeserialize(using = PersonalTypConverterDeserializer.class)
	public PersonalTyp PersonTyp;

	/** Eine Nummer, die zur Sortierung der Lehrer-Datensätze verwendet werden kann. */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Gibt an, ob der Lehrer-Datensatz in der Oberfläche sichtbar sein soll und bei einer Auswahl zur Verfügung steht.  */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** Gibt an, ob Änderungen am Lehrer-Datensatz erlaubt sind. */
	@Column(name = "Aenderbar")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Aenderbar;

	/** Gibt an, ob der Lehrer-Datensatz für den Export in andere Software verwendet werden soll - TODO fuer welche(n) Zweck(e) wird dies gespeichert - gehört dies an diese Stelle?  */
	@Column(name = "FuerExport")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean FuerExport;

	/** Gibt an, ob der Lehrer-Datensatz bei der Statistik berücksichtigt werden soll. */
	@Column(name = "Statistik")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean statistikRelevant;

	/** Straßenname der Lehrkraft */
	@Column(name = "Strassenname")
	@JsonProperty
	public String Strassenname;

	/** Hausnummer wenn getrennt gespeichert */
	@Column(name = "HausNr")
	@JsonProperty
	public String HausNr;

	/** Zusatz zur Hausnummer wenn Hausnummern getrennt gespeichert werden */
	@Column(name = "HausNrZusatz")
	@JsonProperty
	public String HausNrZusatz;

	/** Adressdaten des Lehrers: Fremdschlüssel auf die Katalog-Tabelle mit den Orten */
	@Column(name = "Ort_ID")
	@JsonProperty
	public Long Ort_ID;

	/** Adressdaten des Lehrers: Fremdschlüssel auf die Katalog-Tabelle mit den Ortsteilen */
	@Column(name = "Ortsteil_ID")
	@JsonProperty
	public Long Ortsteil_ID;

	/** Adressdaten des Lehrers: Telefonnummer */
	@Column(name = "Tel")
	@JsonProperty
	public String telefon;

	/** Adressdaten des Lehrers: Mobilnummer oder Faxnummer */
	@Column(name = "Handy")
	@JsonProperty
	public String telefonMobil;

	/** Adressdaten des Lehrers: Private Email-Adresse */
	@Column(name = "Email")
	@JsonProperty
	public String eMailPrivat;

	/** Adressdaten des Lehrers: Dienstliche Email-Adresse */
	@Column(name = "EmailDienstlich")
	@JsonProperty
	public String eMailDienstlich;

	/** Die erste Staatsangehörigkeit des Lehrers */
	@Column(name = "StaatKrz")
	@JsonProperty
	@Convert(converter = NationalitaetenConverter.class)
	@JsonSerialize(using = NationalitaetenConverterSerializer.class)
	@JsonDeserialize(using = NationalitaetenConverterDeserializer.class)
	public Nationalitaeten staatsangehoerigkeit;

	/** Das Geburtsdatum des Lehrers */
	@Column(name = "Geburtsdatum")
	@JsonProperty
	@Convert(converter = DatumConverter.class)
	@JsonSerialize(using = DatumConverterSerializer.class)
	@JsonDeserialize(using = DatumConverterDeserializer.class)
	public String Geburtsdatum;

	/** Das Geschlecht des Lehrers - TODO ist in der Datenbank als String und nicht als Integer (3/4) hinterlegt, dies sollte in allen Tabellen einheitlich sein */
	@Column(name = "Geschlecht")
	@JsonProperty
	@Convert(converter = GeschlechtConverterFromString.class)
	@JsonSerialize(using = GeschlechtConverterFromStringSerializer.class)
	@JsonDeserialize(using = GeschlechtConverterFromStringDeserializer.class)
	public Geschlecht Geschlecht;

	/** Die Anrede für den Lehrer */
	@Column(name = "Anrede")
	@JsonProperty
	public String Anrede;

	/** Die Amtsbezeichnung des Lehrers */
	@Column(name = "Amtsbezeichnung")
	@JsonProperty
	public String Amtsbezeichnung;

	/** Ggf. der Titel des Lehrers */
	@Column(name = "Titel")
	@JsonProperty
	public String Titel;

	/** Die Fächer, die der Lehrer unterrichtet - TODO hat dieses Feld noch einen Zweck? Fächer sind dem Lehrer eigentlich anders zugeordnet...   */
	@Column(name = "Faecher")
	@JsonProperty
	public String Faecher;

	/** Der vordere Teil der NRW-weit eindeutigen Ident-Nummer - setzt sich normalerweise aus Geburtsdatum und Geschlecht (3/4) zusammen, kann in Einzelfällen aber von diesem Schema abweichen */
	@Column(name = "IdentNr1")
	@JsonProperty
	public String identNrTeil1;

	/** Der hintere Teil der Ident-Nummer – wird üblicherweise NRW-weit fortlaufend vergeben */
	@Column(name = "SerNr")
	@JsonProperty
	public String identNrTeil2SerNr;

	/** Personalakten-Nummer (wird von den Bezirksregierungen ggf genutzt) */
	@Column(name = "PANr")
	@JsonProperty
	public String PANr;

	/** Die Personalnummer beim Landesamt für Besoldung und Versorgung (LBV) */
	@Column(name = "LBVNr")
	@JsonProperty
	public String personalNrLBV;

	/** Der Vergütungsschlüssel */
	@Column(name = "VSchluessel")
	@JsonProperty
	public String verguetungsSchluessel;

	/** Das Datum, wann der Lehrer an die Schule gekommen ist. */
	@Column(name = "DatumZugang")
	@JsonProperty
	@Convert(converter = DatumConverter.class)
	@JsonSerialize(using = DatumConverterSerializer.class)
	@JsonDeserialize(using = DatumConverterDeserializer.class)
	public String DatumZugang;

	/** Der Grund für den Zugang des Lehrers */
	@Column(name = "GrundZugang")
	@JsonProperty
	public String GrundZugang;

	/** Das Datum, wann der Lehrer die Schule verlassen hat. */
	@Column(name = "DatumAbgang")
	@JsonProperty
	@Convert(converter = DatumConverter.class)
	@JsonSerialize(using = DatumConverterSerializer.class)
	@JsonDeserialize(using = DatumConverterDeserializer.class)
	public String DatumAbgang;

	/** Der Grund für das Verlassen der Schule durch den Lehrer */
	@Column(name = "GrundAbgang")
	@JsonProperty
	public String GrundAbgang;

	/** Wird für das GS-Modul genutzt (gehashed). */
	@Column(name = "KennwortTools")
	@JsonProperty
	public String KennwortTools;

	/** Gibt an ob das LPassword geändert wurde oder ob es noch das Initialkennwort ist */
	@Column(name = "KennwortToolsAktuell")
	@JsonProperty
	public String KennwortToolsAktuell;

	/** Die ID des Credential-Eintrags */
	@Column(name = "CredentialID")
	@JsonProperty
	public Long CredentialID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOLehrer ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOLehrer() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOLehrer ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Kuerzel   der Wert für das Attribut Kuerzel
	 * @param Nachname   der Wert für das Attribut Nachname
	 */
	public DTOLehrer(final long ID, final String Kuerzel, final String Nachname) {
		this.ID = ID;
		if (Kuerzel == null) {
			throw new NullPointerException("Kuerzel must not be null");
		}
		this.Kuerzel = Kuerzel;
		if (Nachname == null) {
			throw new NullPointerException("Nachname must not be null");
		}
		this.Nachname = Nachname;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOLehrer other = (DTOLehrer) obj;
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
		return "DTOLehrer(ID=" + this.ID + ", GU_ID=" + this.GU_ID + ", Kuerzel=" + this.Kuerzel + ", kuerzelLID=" + this.kuerzelLID + ", Nachname=" + this.Nachname + ", Vorname=" + this.Vorname + ", PersonTyp=" + this.PersonTyp + ", Sortierung=" + this.Sortierung + ", Sichtbar=" + this.Sichtbar + ", Aenderbar=" + this.Aenderbar + ", FuerExport=" + this.FuerExport + ", statistikRelevant=" + this.statistikRelevant + ", Strassenname=" + this.Strassenname + ", HausNr=" + this.HausNr + ", HausNrZusatz=" + this.HausNrZusatz + ", Ort_ID=" + this.Ort_ID + ", Ortsteil_ID=" + this.Ortsteil_ID + ", telefon=" + this.telefon + ", telefonMobil=" + this.telefonMobil + ", eMailPrivat=" + this.eMailPrivat + ", eMailDienstlich=" + this.eMailDienstlich + ", staatsangehoerigkeit=" + this.staatsangehoerigkeit + ", Geburtsdatum=" + this.Geburtsdatum + ", Geschlecht=" + this.Geschlecht + ", Anrede=" + this.Anrede + ", Amtsbezeichnung=" + this.Amtsbezeichnung + ", Titel=" + this.Titel + ", Faecher=" + this.Faecher + ", identNrTeil1=" + this.identNrTeil1 + ", identNrTeil2SerNr=" + this.identNrTeil2SerNr + ", PANr=" + this.PANr + ", personalNrLBV=" + this.personalNrLBV + ", verguetungsSchluessel=" + this.verguetungsSchluessel + ", DatumZugang=" + this.DatumZugang + ", GrundZugang=" + this.GrundZugang + ", DatumAbgang=" + this.DatumAbgang + ", GrundAbgang=" + this.GrundAbgang + ", KennwortTools=" + this.KennwortTools + ", KennwortToolsAktuell=" + this.KennwortToolsAktuell + ", CredentialID=" + this.CredentialID + ")";
	}

}
