package de.nrw.schule.svws.db.dto.rev8.schild.erzieher;

import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.converter.current.BooleanPlusMinusDefaultPlusConverter;
import de.nrw.schule.svws.db.converter.current.NationalitaetenConverter;

import de.nrw.schule.svws.core.types.schule.Nationalitaeten;


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
import de.nrw.schule.svws.csv.converter.current.BooleanPlusMinusDefaultPlusConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.BooleanPlusMinusDefaultPlusConverterDeserializer;
import de.nrw.schule.svws.csv.converter.current.NationalitaetenConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.NationalitaetenConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerErzAdr.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerErzAdr")
@NamedQuery(name="Rev8DTOSchuelerErzieherAdresse.all", query="SELECT e FROM Rev8DTOSchuelerErzieherAdresse e")
@NamedQuery(name="Rev8DTOSchuelerErzieherAdresse.id", query="SELECT e FROM Rev8DTOSchuelerErzieherAdresse e WHERE e.ID = :value")
@NamedQuery(name="Rev8DTOSchuelerErzieherAdresse.id.multiple", query="SELECT e FROM Rev8DTOSchuelerErzieherAdresse e WHERE e.ID IN :value")
@NamedQuery(name="Rev8DTOSchuelerErzieherAdresse.schueler_id", query="SELECT e FROM Rev8DTOSchuelerErzieherAdresse e WHERE e.Schueler_ID = :value")
@NamedQuery(name="Rev8DTOSchuelerErzieherAdresse.schueler_id.multiple", query="SELECT e FROM Rev8DTOSchuelerErzieherAdresse e WHERE e.Schueler_ID IN :value")
@NamedQuery(name="Rev8DTOSchuelerErzieherAdresse.erzieherart_id", query="SELECT e FROM Rev8DTOSchuelerErzieherAdresse e WHERE e.ErzieherArt_ID = :value")
@NamedQuery(name="Rev8DTOSchuelerErzieherAdresse.erzieherart_id.multiple", query="SELECT e FROM Rev8DTOSchuelerErzieherAdresse e WHERE e.ErzieherArt_ID IN :value")
@NamedQuery(name="Rev8DTOSchuelerErzieherAdresse.anrede1", query="SELECT e FROM Rev8DTOSchuelerErzieherAdresse e WHERE e.Anrede1 = :value")
@NamedQuery(name="Rev8DTOSchuelerErzieherAdresse.anrede1.multiple", query="SELECT e FROM Rev8DTOSchuelerErzieherAdresse e WHERE e.Anrede1 IN :value")
@NamedQuery(name="Rev8DTOSchuelerErzieherAdresse.titel1", query="SELECT e FROM Rev8DTOSchuelerErzieherAdresse e WHERE e.Titel1 = :value")
@NamedQuery(name="Rev8DTOSchuelerErzieherAdresse.titel1.multiple", query="SELECT e FROM Rev8DTOSchuelerErzieherAdresse e WHERE e.Titel1 IN :value")
@NamedQuery(name="Rev8DTOSchuelerErzieherAdresse.name1", query="SELECT e FROM Rev8DTOSchuelerErzieherAdresse e WHERE e.Name1 = :value")
@NamedQuery(name="Rev8DTOSchuelerErzieherAdresse.name1.multiple", query="SELECT e FROM Rev8DTOSchuelerErzieherAdresse e WHERE e.Name1 IN :value")
@NamedQuery(name="Rev8DTOSchuelerErzieherAdresse.vorname1", query="SELECT e FROM Rev8DTOSchuelerErzieherAdresse e WHERE e.Vorname1 = :value")
@NamedQuery(name="Rev8DTOSchuelerErzieherAdresse.vorname1.multiple", query="SELECT e FROM Rev8DTOSchuelerErzieherAdresse e WHERE e.Vorname1 IN :value")
@NamedQuery(name="Rev8DTOSchuelerErzieherAdresse.anrede2", query="SELECT e FROM Rev8DTOSchuelerErzieherAdresse e WHERE e.Anrede2 = :value")
@NamedQuery(name="Rev8DTOSchuelerErzieherAdresse.anrede2.multiple", query="SELECT e FROM Rev8DTOSchuelerErzieherAdresse e WHERE e.Anrede2 IN :value")
@NamedQuery(name="Rev8DTOSchuelerErzieherAdresse.titel2", query="SELECT e FROM Rev8DTOSchuelerErzieherAdresse e WHERE e.Titel2 = :value")
@NamedQuery(name="Rev8DTOSchuelerErzieherAdresse.titel2.multiple", query="SELECT e FROM Rev8DTOSchuelerErzieherAdresse e WHERE e.Titel2 IN :value")
@NamedQuery(name="Rev8DTOSchuelerErzieherAdresse.name2", query="SELECT e FROM Rev8DTOSchuelerErzieherAdresse e WHERE e.Name2 = :value")
@NamedQuery(name="Rev8DTOSchuelerErzieherAdresse.name2.multiple", query="SELECT e FROM Rev8DTOSchuelerErzieherAdresse e WHERE e.Name2 IN :value")
@NamedQuery(name="Rev8DTOSchuelerErzieherAdresse.vorname2", query="SELECT e FROM Rev8DTOSchuelerErzieherAdresse e WHERE e.Vorname2 = :value")
@NamedQuery(name="Rev8DTOSchuelerErzieherAdresse.vorname2.multiple", query="SELECT e FROM Rev8DTOSchuelerErzieherAdresse e WHERE e.Vorname2 IN :value")
@NamedQuery(name="Rev8DTOSchuelerErzieherAdresse.erzort_id", query="SELECT e FROM Rev8DTOSchuelerErzieherAdresse e WHERE e.ErzOrt_ID = :value")
@NamedQuery(name="Rev8DTOSchuelerErzieherAdresse.erzort_id.multiple", query="SELECT e FROM Rev8DTOSchuelerErzieherAdresse e WHERE e.ErzOrt_ID IN :value")
@NamedQuery(name="Rev8DTOSchuelerErzieherAdresse.erzstrassenname", query="SELECT e FROM Rev8DTOSchuelerErzieherAdresse e WHERE e.ErzStrassenname = :value")
@NamedQuery(name="Rev8DTOSchuelerErzieherAdresse.erzstrassenname.multiple", query="SELECT e FROM Rev8DTOSchuelerErzieherAdresse e WHERE e.ErzStrassenname IN :value")
@NamedQuery(name="Rev8DTOSchuelerErzieherAdresse.erzhausnr", query="SELECT e FROM Rev8DTOSchuelerErzieherAdresse e WHERE e.ErzHausNr = :value")
@NamedQuery(name="Rev8DTOSchuelerErzieherAdresse.erzhausnr.multiple", query="SELECT e FROM Rev8DTOSchuelerErzieherAdresse e WHERE e.ErzHausNr IN :value")
@NamedQuery(name="Rev8DTOSchuelerErzieherAdresse.erzortsteil_id", query="SELECT e FROM Rev8DTOSchuelerErzieherAdresse e WHERE e.ErzOrtsteil_ID = :value")
@NamedQuery(name="Rev8DTOSchuelerErzieherAdresse.erzortsteil_id.multiple", query="SELECT e FROM Rev8DTOSchuelerErzieherAdresse e WHERE e.ErzOrtsteil_ID IN :value")
@NamedQuery(name="Rev8DTOSchuelerErzieherAdresse.erzhausnrzusatz", query="SELECT e FROM Rev8DTOSchuelerErzieherAdresse e WHERE e.ErzHausNrZusatz = :value")
@NamedQuery(name="Rev8DTOSchuelerErzieherAdresse.erzhausnrzusatz.multiple", query="SELECT e FROM Rev8DTOSchuelerErzieherAdresse e WHERE e.ErzHausNrZusatz IN :value")
@NamedQuery(name="Rev8DTOSchuelerErzieherAdresse.erzanschreiben", query="SELECT e FROM Rev8DTOSchuelerErzieherAdresse e WHERE e.ErzAnschreiben = :value")
@NamedQuery(name="Rev8DTOSchuelerErzieherAdresse.erzanschreiben.multiple", query="SELECT e FROM Rev8DTOSchuelerErzieherAdresse e WHERE e.ErzAnschreiben IN :value")
@NamedQuery(name="Rev8DTOSchuelerErzieherAdresse.sortierung", query="SELECT e FROM Rev8DTOSchuelerErzieherAdresse e WHERE e.Sortierung = :value")
@NamedQuery(name="Rev8DTOSchuelerErzieherAdresse.sortierung.multiple", query="SELECT e FROM Rev8DTOSchuelerErzieherAdresse e WHERE e.Sortierung IN :value")
@NamedQuery(name="Rev8DTOSchuelerErzieherAdresse.erzemail", query="SELECT e FROM Rev8DTOSchuelerErzieherAdresse e WHERE e.ErzEmail = :value")
@NamedQuery(name="Rev8DTOSchuelerErzieherAdresse.erzemail.multiple", query="SELECT e FROM Rev8DTOSchuelerErzieherAdresse e WHERE e.ErzEmail IN :value")
@NamedQuery(name="Rev8DTOSchuelerErzieherAdresse.erzadrzusatz", query="SELECT e FROM Rev8DTOSchuelerErzieherAdresse e WHERE e.ErzAdrZusatz = :value")
@NamedQuery(name="Rev8DTOSchuelerErzieherAdresse.erzadrzusatz.multiple", query="SELECT e FROM Rev8DTOSchuelerErzieherAdresse e WHERE e.ErzAdrZusatz IN :value")
@NamedQuery(name="Rev8DTOSchuelerErzieherAdresse.erz1staatkrz", query="SELECT e FROM Rev8DTOSchuelerErzieherAdresse e WHERE e.Erz1StaatKrz = :value")
@NamedQuery(name="Rev8DTOSchuelerErzieherAdresse.erz1staatkrz.multiple", query="SELECT e FROM Rev8DTOSchuelerErzieherAdresse e WHERE e.Erz1StaatKrz IN :value")
@NamedQuery(name="Rev8DTOSchuelerErzieherAdresse.erz2staatkrz", query="SELECT e FROM Rev8DTOSchuelerErzieherAdresse e WHERE e.Erz2StaatKrz = :value")
@NamedQuery(name="Rev8DTOSchuelerErzieherAdresse.erz2staatkrz.multiple", query="SELECT e FROM Rev8DTOSchuelerErzieherAdresse e WHERE e.Erz2StaatKrz IN :value")
@NamedQuery(name="Rev8DTOSchuelerErzieherAdresse.erzemail2", query="SELECT e FROM Rev8DTOSchuelerErzieherAdresse e WHERE e.ErzEmail2 = :value")
@NamedQuery(name="Rev8DTOSchuelerErzieherAdresse.erzemail2.multiple", query="SELECT e FROM Rev8DTOSchuelerErzieherAdresse e WHERE e.ErzEmail2 IN :value")
@NamedQuery(name="Rev8DTOSchuelerErzieherAdresse.erz1zusatznachname", query="SELECT e FROM Rev8DTOSchuelerErzieherAdresse e WHERE e.Erz1ZusatzNachname = :value")
@NamedQuery(name="Rev8DTOSchuelerErzieherAdresse.erz1zusatznachname.multiple", query="SELECT e FROM Rev8DTOSchuelerErzieherAdresse e WHERE e.Erz1ZusatzNachname IN :value")
@NamedQuery(name="Rev8DTOSchuelerErzieherAdresse.erz2zusatznachname", query="SELECT e FROM Rev8DTOSchuelerErzieherAdresse e WHERE e.Erz2ZusatzNachname = :value")
@NamedQuery(name="Rev8DTOSchuelerErzieherAdresse.erz2zusatznachname.multiple", query="SELECT e FROM Rev8DTOSchuelerErzieherAdresse e WHERE e.Erz2ZusatzNachname IN :value")
@NamedQuery(name="Rev8DTOSchuelerErzieherAdresse.bemerkungen", query="SELECT e FROM Rev8DTOSchuelerErzieherAdresse e WHERE e.Bemerkungen = :value")
@NamedQuery(name="Rev8DTOSchuelerErzieherAdresse.bemerkungen.multiple", query="SELECT e FROM Rev8DTOSchuelerErzieherAdresse e WHERE e.Bemerkungen IN :value")
@NamedQuery(name="Rev8DTOSchuelerErzieherAdresse.credentialid", query="SELECT e FROM Rev8DTOSchuelerErzieherAdresse e WHERE e.CredentialID = :value")
@NamedQuery(name="Rev8DTOSchuelerErzieherAdresse.credentialid.multiple", query="SELECT e FROM Rev8DTOSchuelerErzieherAdresse e WHERE e.CredentialID IN :value")
@NamedQuery(name="Rev8DTOSchuelerErzieherAdresse.primaryKeyQuery", query="SELECT e FROM Rev8DTOSchuelerErzieherAdresse e WHERE e.ID = ?1")
@NamedQuery(name="Rev8DTOSchuelerErzieherAdresse.all.migration", query="SELECT e FROM Rev8DTOSchuelerErzieherAdresse e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Schueler_ID","ErzieherArt_ID","Anrede1","Titel1","Name1","Vorname1","Anrede2","Titel2","Name2","Vorname2","ErzOrt_ID","ErzStrassenname","ErzHausNr","ErzOrtsteil_ID","ErzHausNrZusatz","ErzAnschreiben","Sortierung","ErzEmail","ErzAdrZusatz","Erz1StaatKrz","Erz2StaatKrz","ErzEmail2","Erz1ZusatzNachname","Erz2ZusatzNachname","Bemerkungen","CredentialID"})
public class Rev8DTOSchuelerErzieherAdresse {

	/** ID des Erzieherdatensatzes */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** SchülerID zum Erzieherdatensatz */
	@Column(name = "Schueler_ID")
	@JsonProperty
	public Long Schueler_ID;

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

	/** Vorname1 zum Erzieherdatensatz PAuswG vom 21.6.2019 §5 Abs. 2 */
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

	/** Vorname2 zum Erzieherdatensatz PAuswG vom 21.6.2019 §5 Abs. 2 */
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
	@Convert(converter=BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultPlusConverterDeserializer.class)
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
	@Convert(converter=NationalitaetenConverter.class)
	@JsonSerialize(using=NationalitaetenConverterSerializer.class)
	@JsonDeserialize(using=NationalitaetenConverterDeserializer.class)
	public Nationalitaeten Erz1StaatKrz;

	/** Staatangehörigkeit2 zum Erzieherdatensatz */
	@Column(name = "Erz2StaatKrz")
	@JsonProperty
	@Convert(converter=NationalitaetenConverter.class)
	@JsonSerialize(using=NationalitaetenConverterSerializer.class)
	@JsonDeserialize(using=NationalitaetenConverterDeserializer.class)
	public Nationalitaeten Erz2StaatKrz;

	/** Email2 zum Erzieherdatensatz */
	@Column(name = "ErzEmail2")
	@JsonProperty
	public String ErzEmail2;

	/** Zusatznachname1 zum Erzieherdatensatz */
	@Column(name = "Erz1ZusatzNachname")
	@JsonProperty
	public String Erz1ZusatzNachname;

	/** Zusatznachname2 zum Erzieherdatensatz */
	@Column(name = "Erz2ZusatzNachname")
	@JsonProperty
	public String Erz2ZusatzNachname;

	/** Memofeld Bemerkungen zum Erzieherdatensatz */
	@Column(name = "Bemerkungen")
	@JsonProperty
	public String Bemerkungen;

	/** Die ID des Credential-Eintrags */
	@Column(name = "CredentialID")
	@JsonProperty
	public Long CredentialID;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOSchuelerErzieherAdresse ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev8DTOSchuelerErzieherAdresse() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOSchuelerErzieherAdresse ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 */
	public Rev8DTOSchuelerErzieherAdresse(final Long ID, final Long Schueler_ID) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Schueler_ID == null) { 
			throw new NullPointerException("Schueler_ID must not be null");
		}
		this.Schueler_ID = Schueler_ID;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev8DTOSchuelerErzieherAdresse other = (Rev8DTOSchuelerErzieherAdresse) obj;
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
		return "Rev8DTOSchuelerErzieherAdresse(ID=" + this.ID + ", Schueler_ID=" + this.Schueler_ID + ", ErzieherArt_ID=" + this.ErzieherArt_ID + ", Anrede1=" + this.Anrede1 + ", Titel1=" + this.Titel1 + ", Name1=" + this.Name1 + ", Vorname1=" + this.Vorname1 + ", Anrede2=" + this.Anrede2 + ", Titel2=" + this.Titel2 + ", Name2=" + this.Name2 + ", Vorname2=" + this.Vorname2 + ", ErzOrt_ID=" + this.ErzOrt_ID + ", ErzStrassenname=" + this.ErzStrassenname + ", ErzHausNr=" + this.ErzHausNr + ", ErzOrtsteil_ID=" + this.ErzOrtsteil_ID + ", ErzHausNrZusatz=" + this.ErzHausNrZusatz + ", ErzAnschreiben=" + this.ErzAnschreiben + ", Sortierung=" + this.Sortierung + ", ErzEmail=" + this.ErzEmail + ", ErzAdrZusatz=" + this.ErzAdrZusatz + ", Erz1StaatKrz=" + this.Erz1StaatKrz + ", Erz2StaatKrz=" + this.Erz2StaatKrz + ", ErzEmail2=" + this.ErzEmail2 + ", Erz1ZusatzNachname=" + this.Erz1ZusatzNachname + ", Erz2ZusatzNachname=" + this.Erz2ZusatzNachname + ", Bemerkungen=" + this.Bemerkungen + ", CredentialID=" + this.CredentialID + ")";
	}

}