package de.svws_nrw.db.dto.current.schild.erzieher;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultPlusConverter;
import de.svws_nrw.db.converter.current.NationalitaetenConverter;

import de.svws_nrw.core.types.schule.Nationalitaeten;


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
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultPlusConverterSerializer;
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultPlusConverterDeserializer;
import de.svws_nrw.csv.converter.current.NationalitaetenConverterSerializer;
import de.svws_nrw.csv.converter.current.NationalitaetenConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerErzAdr.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerErzAdr")
@JsonPropertyOrder({"ID", "Schueler_ID", "ErzieherArt_ID", "Anrede1", "Titel1", "Name1", "Vorname1", "Anrede2", "Titel2", "Name2", "Vorname2", "ErzOrt_ID", "ErzStrassenname", "ErzHausNr", "ErzOrtsteil_ID", "ErzHausNrZusatz", "ErzAnschreiben", "Sortierung", "ErzEmail", "ErzAdrZusatz", "Erz1StaatKrz", "Erz2StaatKrz", "ErzEmail2", "Bemerkungen", "CredentialID"})
public final class DTOSchuelerErzieherAdresse {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOSchuelerErzieherAdresse e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOSchuelerErzieherAdresse e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOSchuelerErzieherAdresse e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOSchuelerErzieherAdresse e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOSchuelerErzieherAdresse e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOSchuelerErzieherAdresse e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schueler_ID */
	public static final String QUERY_BY_SCHUELER_ID = "SELECT e FROM DTOSchuelerErzieherAdresse e WHERE e.Schueler_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schueler_ID */
	public static final String QUERY_LIST_BY_SCHUELER_ID = "SELECT e FROM DTOSchuelerErzieherAdresse e WHERE e.Schueler_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ErzieherArt_ID */
	public static final String QUERY_BY_ERZIEHERART_ID = "SELECT e FROM DTOSchuelerErzieherAdresse e WHERE e.ErzieherArt_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ErzieherArt_ID */
	public static final String QUERY_LIST_BY_ERZIEHERART_ID = "SELECT e FROM DTOSchuelerErzieherAdresse e WHERE e.ErzieherArt_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Anrede1 */
	public static final String QUERY_BY_ANREDE1 = "SELECT e FROM DTOSchuelerErzieherAdresse e WHERE e.Anrede1 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Anrede1 */
	public static final String QUERY_LIST_BY_ANREDE1 = "SELECT e FROM DTOSchuelerErzieherAdresse e WHERE e.Anrede1 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Titel1 */
	public static final String QUERY_BY_TITEL1 = "SELECT e FROM DTOSchuelerErzieherAdresse e WHERE e.Titel1 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Titel1 */
	public static final String QUERY_LIST_BY_TITEL1 = "SELECT e FROM DTOSchuelerErzieherAdresse e WHERE e.Titel1 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Name1 */
	public static final String QUERY_BY_NAME1 = "SELECT e FROM DTOSchuelerErzieherAdresse e WHERE e.Name1 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Name1 */
	public static final String QUERY_LIST_BY_NAME1 = "SELECT e FROM DTOSchuelerErzieherAdresse e WHERE e.Name1 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Vorname1 */
	public static final String QUERY_BY_VORNAME1 = "SELECT e FROM DTOSchuelerErzieherAdresse e WHERE e.Vorname1 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Vorname1 */
	public static final String QUERY_LIST_BY_VORNAME1 = "SELECT e FROM DTOSchuelerErzieherAdresse e WHERE e.Vorname1 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Anrede2 */
	public static final String QUERY_BY_ANREDE2 = "SELECT e FROM DTOSchuelerErzieherAdresse e WHERE e.Anrede2 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Anrede2 */
	public static final String QUERY_LIST_BY_ANREDE2 = "SELECT e FROM DTOSchuelerErzieherAdresse e WHERE e.Anrede2 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Titel2 */
	public static final String QUERY_BY_TITEL2 = "SELECT e FROM DTOSchuelerErzieherAdresse e WHERE e.Titel2 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Titel2 */
	public static final String QUERY_LIST_BY_TITEL2 = "SELECT e FROM DTOSchuelerErzieherAdresse e WHERE e.Titel2 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Name2 */
	public static final String QUERY_BY_NAME2 = "SELECT e FROM DTOSchuelerErzieherAdresse e WHERE e.Name2 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Name2 */
	public static final String QUERY_LIST_BY_NAME2 = "SELECT e FROM DTOSchuelerErzieherAdresse e WHERE e.Name2 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Vorname2 */
	public static final String QUERY_BY_VORNAME2 = "SELECT e FROM DTOSchuelerErzieherAdresse e WHERE e.Vorname2 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Vorname2 */
	public static final String QUERY_LIST_BY_VORNAME2 = "SELECT e FROM DTOSchuelerErzieherAdresse e WHERE e.Vorname2 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ErzOrt_ID */
	public static final String QUERY_BY_ERZORT_ID = "SELECT e FROM DTOSchuelerErzieherAdresse e WHERE e.ErzOrt_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ErzOrt_ID */
	public static final String QUERY_LIST_BY_ERZORT_ID = "SELECT e FROM DTOSchuelerErzieherAdresse e WHERE e.ErzOrt_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ErzStrassenname */
	public static final String QUERY_BY_ERZSTRASSENNAME = "SELECT e FROM DTOSchuelerErzieherAdresse e WHERE e.ErzStrassenname = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ErzStrassenname */
	public static final String QUERY_LIST_BY_ERZSTRASSENNAME = "SELECT e FROM DTOSchuelerErzieherAdresse e WHERE e.ErzStrassenname IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ErzHausNr */
	public static final String QUERY_BY_ERZHAUSNR = "SELECT e FROM DTOSchuelerErzieherAdresse e WHERE e.ErzHausNr = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ErzHausNr */
	public static final String QUERY_LIST_BY_ERZHAUSNR = "SELECT e FROM DTOSchuelerErzieherAdresse e WHERE e.ErzHausNr IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ErzOrtsteil_ID */
	public static final String QUERY_BY_ERZORTSTEIL_ID = "SELECT e FROM DTOSchuelerErzieherAdresse e WHERE e.ErzOrtsteil_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ErzOrtsteil_ID */
	public static final String QUERY_LIST_BY_ERZORTSTEIL_ID = "SELECT e FROM DTOSchuelerErzieherAdresse e WHERE e.ErzOrtsteil_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ErzHausNrZusatz */
	public static final String QUERY_BY_ERZHAUSNRZUSATZ = "SELECT e FROM DTOSchuelerErzieherAdresse e WHERE e.ErzHausNrZusatz = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ErzHausNrZusatz */
	public static final String QUERY_LIST_BY_ERZHAUSNRZUSATZ = "SELECT e FROM DTOSchuelerErzieherAdresse e WHERE e.ErzHausNrZusatz IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ErzAnschreiben */
	public static final String QUERY_BY_ERZANSCHREIBEN = "SELECT e FROM DTOSchuelerErzieherAdresse e WHERE e.ErzAnschreiben = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ErzAnschreiben */
	public static final String QUERY_LIST_BY_ERZANSCHREIBEN = "SELECT e FROM DTOSchuelerErzieherAdresse e WHERE e.ErzAnschreiben IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Sortierung */
	public static final String QUERY_BY_SORTIERUNG = "SELECT e FROM DTOSchuelerErzieherAdresse e WHERE e.Sortierung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Sortierung */
	public static final String QUERY_LIST_BY_SORTIERUNG = "SELECT e FROM DTOSchuelerErzieherAdresse e WHERE e.Sortierung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ErzEmail */
	public static final String QUERY_BY_ERZEMAIL = "SELECT e FROM DTOSchuelerErzieherAdresse e WHERE e.ErzEmail = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ErzEmail */
	public static final String QUERY_LIST_BY_ERZEMAIL = "SELECT e FROM DTOSchuelerErzieherAdresse e WHERE e.ErzEmail IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ErzAdrZusatz */
	public static final String QUERY_BY_ERZADRZUSATZ = "SELECT e FROM DTOSchuelerErzieherAdresse e WHERE e.ErzAdrZusatz = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ErzAdrZusatz */
	public static final String QUERY_LIST_BY_ERZADRZUSATZ = "SELECT e FROM DTOSchuelerErzieherAdresse e WHERE e.ErzAdrZusatz IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Erz1StaatKrz */
	public static final String QUERY_BY_ERZ1STAATKRZ = "SELECT e FROM DTOSchuelerErzieherAdresse e WHERE e.Erz1StaatKrz = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Erz1StaatKrz */
	public static final String QUERY_LIST_BY_ERZ1STAATKRZ = "SELECT e FROM DTOSchuelerErzieherAdresse e WHERE e.Erz1StaatKrz IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Erz2StaatKrz */
	public static final String QUERY_BY_ERZ2STAATKRZ = "SELECT e FROM DTOSchuelerErzieherAdresse e WHERE e.Erz2StaatKrz = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Erz2StaatKrz */
	public static final String QUERY_LIST_BY_ERZ2STAATKRZ = "SELECT e FROM DTOSchuelerErzieherAdresse e WHERE e.Erz2StaatKrz IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ErzEmail2 */
	public static final String QUERY_BY_ERZEMAIL2 = "SELECT e FROM DTOSchuelerErzieherAdresse e WHERE e.ErzEmail2 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ErzEmail2 */
	public static final String QUERY_LIST_BY_ERZEMAIL2 = "SELECT e FROM DTOSchuelerErzieherAdresse e WHERE e.ErzEmail2 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Bemerkungen */
	public static final String QUERY_BY_BEMERKUNGEN = "SELECT e FROM DTOSchuelerErzieherAdresse e WHERE e.Bemerkungen = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Bemerkungen */
	public static final String QUERY_LIST_BY_BEMERKUNGEN = "SELECT e FROM DTOSchuelerErzieherAdresse e WHERE e.Bemerkungen IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes CredentialID */
	public static final String QUERY_BY_CREDENTIALID = "SELECT e FROM DTOSchuelerErzieherAdresse e WHERE e.CredentialID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes CredentialID */
	public static final String QUERY_LIST_BY_CREDENTIALID = "SELECT e FROM DTOSchuelerErzieherAdresse e WHERE e.CredentialID IN ?1";

	/** ID des Erzieherdatensatzes */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** SchülerID zum Erzieherdatensatz */
	@Column(name = "Schueler_ID")
	@JsonProperty
	public long Schueler_ID;

	/** ErziherARTID zum Erzieherdatensatz */
	@Column(name = "ErzieherArt_ID")
	@JsonProperty
	public Long ErzieherArt_ID;

	/** Anrede1 zum Erzieherdatensatz */
	@Column(name = "Anrede1")
	@JsonProperty
	public String Anrede1;

	/** Titel1 zum Erzieherdatensatz */
	@Column(name = "Titel1")
	@JsonProperty
	public String Titel1;

	/** Nachname1 zum Erzieherdatensatz PAuswG vom 21.6.2019 §5 Abs. 2 */
	@Column(name = "Name1")
	@JsonProperty
	public String Name1;

	/** Vorname1 zum Erzieherdatensatz PAuswG vom 21.6.2019 §5 Abs. 2. Wird im Client mit Rufname angezeigt. */
	@Column(name = "Vorname1")
	@JsonProperty
	public String Vorname1;

	/** Anrede2 zum Erzieherdatensatz */
	@Column(name = "Anrede2")
	@JsonProperty
	public String Anrede2;

	/** Titel2 zum Erzieherdatensatz */
	@Column(name = "Titel2")
	@JsonProperty
	public String Titel2;

	/** Nachname2 zum Erzieherdatensatz PAuswG vom 21.6.2019 §5 Abs. 2 */
	@Column(name = "Name2")
	@JsonProperty
	public String Name2;

	/** Vorname2 zum Erzieherdatensatz PAuswG vom 21.6.2019 §5 Abs. 2. Wird im Client mit Rufname angezeigt. */
	@Column(name = "Vorname2")
	@JsonProperty
	public String Vorname2;

	/** OrtID zum Erzieherdatensatz */
	@Column(name = "ErzOrt_ID")
	@JsonProperty
	public Long ErzOrt_ID;

	/** Straßenname des Erzieherdatensatzes */
	@Column(name = "ErzStrassenname")
	@JsonProperty
	public String ErzStrassenname;

	/** Hausnummer wenn getrennt gespeichert */
	@Column(name = "ErzHausNr")
	@JsonProperty
	public String ErzHausNr;

	/** OrtsteilID zum Erzieherdatensatz */
	@Column(name = "ErzOrtsteil_ID")
	@JsonProperty
	public Long ErzOrtsteil_ID;

	/** Zusatz zur Hausnummer wenn Hausnummern getrennt gespeichert werden */
	@Column(name = "ErzHausNrZusatz")
	@JsonProperty
	public String ErzHausNrZusatz;

	/** Erhältanschreiben Ja Nein zum Erzieherdatensatz */
	@Column(name = "ErzAnschreiben")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean ErzAnschreiben;

	/** Sortierung zum Erzieherdatensatz */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Email1 zum Erzieherdatensatz */
	@Column(name = "ErzEmail")
	@JsonProperty
	public String ErzEmail;

	/** Aresszusatz zum Erzieherdatensatz */
	@Column(name = "ErzAdrZusatz")
	@JsonProperty
	public String ErzAdrZusatz;

	/** Staatangehörigkeit1 zum Erzieherdatensatz */
	@Column(name = "Erz1StaatKrz")
	@JsonProperty
	@Convert(converter = NationalitaetenConverter.class)
	@JsonSerialize(using = NationalitaetenConverterSerializer.class)
	@JsonDeserialize(using = NationalitaetenConverterDeserializer.class)
	public Nationalitaeten Erz1StaatKrz;

	/** Staatangehörigkeit2 zum Erzieherdatensatz */
	@Column(name = "Erz2StaatKrz")
	@JsonProperty
	@Convert(converter = NationalitaetenConverter.class)
	@JsonSerialize(using = NationalitaetenConverterSerializer.class)
	@JsonDeserialize(using = NationalitaetenConverterDeserializer.class)
	public Nationalitaeten Erz2StaatKrz;

	/** Email2 zum Erzieherdatensatz */
	@Column(name = "ErzEmail2")
	@JsonProperty
	public String ErzEmail2;

	/** Memofeld Bemerkungen zum Erzieherdatensatz */
	@Column(name = "Bemerkungen")
	@JsonProperty
	public String Bemerkungen;

	/** Die ID des Credential-Eintrags */
	@Column(name = "CredentialID")
	@JsonProperty
	public Long CredentialID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerErzieherAdresse ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSchuelerErzieherAdresse() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerErzieherAdresse ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 */
	public DTOSchuelerErzieherAdresse(final long ID, final long Schueler_ID) {
		this.ID = ID;
		this.Schueler_ID = Schueler_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOSchuelerErzieherAdresse other = (DTOSchuelerErzieherAdresse) obj;
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
		return "DTOSchuelerErzieherAdresse(ID=" + this.ID + ", Schueler_ID=" + this.Schueler_ID + ", ErzieherArt_ID=" + this.ErzieherArt_ID + ", Anrede1=" + this.Anrede1 + ", Titel1=" + this.Titel1 + ", Name1=" + this.Name1 + ", Vorname1=" + this.Vorname1 + ", Anrede2=" + this.Anrede2 + ", Titel2=" + this.Titel2 + ", Name2=" + this.Name2 + ", Vorname2=" + this.Vorname2 + ", ErzOrt_ID=" + this.ErzOrt_ID + ", ErzStrassenname=" + this.ErzStrassenname + ", ErzHausNr=" + this.ErzHausNr + ", ErzOrtsteil_ID=" + this.ErzOrtsteil_ID + ", ErzHausNrZusatz=" + this.ErzHausNrZusatz + ", ErzAnschreiben=" + this.ErzAnschreiben + ", Sortierung=" + this.Sortierung + ", ErzEmail=" + this.ErzEmail + ", ErzAdrZusatz=" + this.ErzAdrZusatz + ", Erz1StaatKrz=" + this.Erz1StaatKrz + ", Erz2StaatKrz=" + this.Erz2StaatKrz + ", ErzEmail2=" + this.ErzEmail2 + ", Bemerkungen=" + this.Bemerkungen + ", CredentialID=" + this.CredentialID + ")";
	}

}
