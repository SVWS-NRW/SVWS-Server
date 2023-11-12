package de.svws_nrw.db.dto.migration.schild.personengruppen;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Personengruppen_Personen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Personengruppen_Personen")
@NamedQuery(name = "MigrationDTOPersonengruppenPersonen.all", query = "SELECT e FROM MigrationDTOPersonengruppenPersonen e")
@NamedQuery(name = "MigrationDTOPersonengruppenPersonen.id", query = "SELECT e FROM MigrationDTOPersonengruppenPersonen e WHERE e.ID = :value")
@NamedQuery(name = "MigrationDTOPersonengruppenPersonen.id.multiple", query = "SELECT e FROM MigrationDTOPersonengruppenPersonen e WHERE e.ID IN :value")
@NamedQuery(name = "MigrationDTOPersonengruppenPersonen.schulnreigner", query = "SELECT e FROM MigrationDTOPersonengruppenPersonen e WHERE e.SchulnrEigner = :value")
@NamedQuery(name = "MigrationDTOPersonengruppenPersonen.schulnreigner.multiple", query = "SELECT e FROM MigrationDTOPersonengruppenPersonen e WHERE e.SchulnrEigner IN :value")
@NamedQuery(name = "MigrationDTOPersonengruppenPersonen.gruppe_id", query = "SELECT e FROM MigrationDTOPersonengruppenPersonen e WHERE e.Gruppe_ID = :value")
@NamedQuery(name = "MigrationDTOPersonengruppenPersonen.gruppe_id.multiple", query = "SELECT e FROM MigrationDTOPersonengruppenPersonen e WHERE e.Gruppe_ID IN :value")
@NamedQuery(name = "MigrationDTOPersonengruppenPersonen.person_id", query = "SELECT e FROM MigrationDTOPersonengruppenPersonen e WHERE e.Person_ID = :value")
@NamedQuery(name = "MigrationDTOPersonengruppenPersonen.person_id.multiple", query = "SELECT e FROM MigrationDTOPersonengruppenPersonen e WHERE e.Person_ID IN :value")
@NamedQuery(name = "MigrationDTOPersonengruppenPersonen.personnr", query = "SELECT e FROM MigrationDTOPersonengruppenPersonen e WHERE e.PersonNr = :value")
@NamedQuery(name = "MigrationDTOPersonengruppenPersonen.personnr.multiple", query = "SELECT e FROM MigrationDTOPersonengruppenPersonen e WHERE e.PersonNr IN :value")
@NamedQuery(name = "MigrationDTOPersonengruppenPersonen.personart", query = "SELECT e FROM MigrationDTOPersonengruppenPersonen e WHERE e.PersonArt = :value")
@NamedQuery(name = "MigrationDTOPersonengruppenPersonen.personart.multiple", query = "SELECT e FROM MigrationDTOPersonengruppenPersonen e WHERE e.PersonArt IN :value")
@NamedQuery(name = "MigrationDTOPersonengruppenPersonen.personname", query = "SELECT e FROM MigrationDTOPersonengruppenPersonen e WHERE e.PersonName = :value")
@NamedQuery(name = "MigrationDTOPersonengruppenPersonen.personname.multiple", query = "SELECT e FROM MigrationDTOPersonengruppenPersonen e WHERE e.PersonName IN :value")
@NamedQuery(name = "MigrationDTOPersonengruppenPersonen.personvorname", query = "SELECT e FROM MigrationDTOPersonengruppenPersonen e WHERE e.PersonVorname = :value")
@NamedQuery(name = "MigrationDTOPersonengruppenPersonen.personvorname.multiple", query = "SELECT e FROM MigrationDTOPersonengruppenPersonen e WHERE e.PersonVorname IN :value")
@NamedQuery(name = "MigrationDTOPersonengruppenPersonen.personplz", query = "SELECT e FROM MigrationDTOPersonengruppenPersonen e WHERE e.PersonPLZ = :value")
@NamedQuery(name = "MigrationDTOPersonengruppenPersonen.personplz.multiple", query = "SELECT e FROM MigrationDTOPersonengruppenPersonen e WHERE e.PersonPLZ IN :value")
@NamedQuery(name = "MigrationDTOPersonengruppenPersonen.personort", query = "SELECT e FROM MigrationDTOPersonengruppenPersonen e WHERE e.PersonOrt = :value")
@NamedQuery(name = "MigrationDTOPersonengruppenPersonen.personort.multiple", query = "SELECT e FROM MigrationDTOPersonengruppenPersonen e WHERE e.PersonOrt IN :value")
@NamedQuery(name = "MigrationDTOPersonengruppenPersonen.personstrasse", query = "SELECT e FROM MigrationDTOPersonengruppenPersonen e WHERE e.PersonStrasse = :value")
@NamedQuery(name = "MigrationDTOPersonengruppenPersonen.personstrasse.multiple", query = "SELECT e FROM MigrationDTOPersonengruppenPersonen e WHERE e.PersonStrasse IN :value")
@NamedQuery(name = "MigrationDTOPersonengruppenPersonen.personstrassenname", query = "SELECT e FROM MigrationDTOPersonengruppenPersonen e WHERE e.PersonStrassenname = :value")
@NamedQuery(name = "MigrationDTOPersonengruppenPersonen.personstrassenname.multiple", query = "SELECT e FROM MigrationDTOPersonengruppenPersonen e WHERE e.PersonStrassenname IN :value")
@NamedQuery(name = "MigrationDTOPersonengruppenPersonen.personhausnr", query = "SELECT e FROM MigrationDTOPersonengruppenPersonen e WHERE e.PersonHausNr = :value")
@NamedQuery(name = "MigrationDTOPersonengruppenPersonen.personhausnr.multiple", query = "SELECT e FROM MigrationDTOPersonengruppenPersonen e WHERE e.PersonHausNr IN :value")
@NamedQuery(name = "MigrationDTOPersonengruppenPersonen.personhausnrzusatz", query = "SELECT e FROM MigrationDTOPersonengruppenPersonen e WHERE e.PersonHausNrZusatz = :value")
@NamedQuery(name = "MigrationDTOPersonengruppenPersonen.personhausnrzusatz.multiple", query = "SELECT e FROM MigrationDTOPersonengruppenPersonen e WHERE e.PersonHausNrZusatz IN :value")
@NamedQuery(name = "MigrationDTOPersonengruppenPersonen.persontelefon", query = "SELECT e FROM MigrationDTOPersonengruppenPersonen e WHERE e.PersonTelefon = :value")
@NamedQuery(name = "MigrationDTOPersonengruppenPersonen.persontelefon.multiple", query = "SELECT e FROM MigrationDTOPersonengruppenPersonen e WHERE e.PersonTelefon IN :value")
@NamedQuery(name = "MigrationDTOPersonengruppenPersonen.personmobil", query = "SELECT e FROM MigrationDTOPersonengruppenPersonen e WHERE e.PersonMobil = :value")
@NamedQuery(name = "MigrationDTOPersonengruppenPersonen.personmobil.multiple", query = "SELECT e FROM MigrationDTOPersonengruppenPersonen e WHERE e.PersonMobil IN :value")
@NamedQuery(name = "MigrationDTOPersonengruppenPersonen.personemail", query = "SELECT e FROM MigrationDTOPersonengruppenPersonen e WHERE e.PersonEmail = :value")
@NamedQuery(name = "MigrationDTOPersonengruppenPersonen.personemail.multiple", query = "SELECT e FROM MigrationDTOPersonengruppenPersonen e WHERE e.PersonEmail IN :value")
@NamedQuery(name = "MigrationDTOPersonengruppenPersonen.bemerkung", query = "SELECT e FROM MigrationDTOPersonengruppenPersonen e WHERE e.Bemerkung = :value")
@NamedQuery(name = "MigrationDTOPersonengruppenPersonen.bemerkung.multiple", query = "SELECT e FROM MigrationDTOPersonengruppenPersonen e WHERE e.Bemerkung IN :value")
@NamedQuery(name = "MigrationDTOPersonengruppenPersonen.zusatzinfo", query = "SELECT e FROM MigrationDTOPersonengruppenPersonen e WHERE e.Zusatzinfo = :value")
@NamedQuery(name = "MigrationDTOPersonengruppenPersonen.zusatzinfo.multiple", query = "SELECT e FROM MigrationDTOPersonengruppenPersonen e WHERE e.Zusatzinfo IN :value")
@NamedQuery(name = "MigrationDTOPersonengruppenPersonen.sortierung", query = "SELECT e FROM MigrationDTOPersonengruppenPersonen e WHERE e.Sortierung = :value")
@NamedQuery(name = "MigrationDTOPersonengruppenPersonen.sortierung.multiple", query = "SELECT e FROM MigrationDTOPersonengruppenPersonen e WHERE e.Sortierung IN :value")
@NamedQuery(name = "MigrationDTOPersonengruppenPersonen.personanrede", query = "SELECT e FROM MigrationDTOPersonengruppenPersonen e WHERE e.PersonAnrede = :value")
@NamedQuery(name = "MigrationDTOPersonengruppenPersonen.personanrede.multiple", query = "SELECT e FROM MigrationDTOPersonengruppenPersonen e WHERE e.PersonAnrede IN :value")
@NamedQuery(name = "MigrationDTOPersonengruppenPersonen.personakadgrad", query = "SELECT e FROM MigrationDTOPersonengruppenPersonen e WHERE e.PersonAkadGrad = :value")
@NamedQuery(name = "MigrationDTOPersonengruppenPersonen.personakadgrad.multiple", query = "SELECT e FROM MigrationDTOPersonengruppenPersonen e WHERE e.PersonAkadGrad IN :value")
@NamedQuery(name = "MigrationDTOPersonengruppenPersonen.primaryKeyQuery", query = "SELECT e FROM MigrationDTOPersonengruppenPersonen e WHERE e.ID = ?1")
@NamedQuery(name = "MigrationDTOPersonengruppenPersonen.primaryKeyQuery.multiple", query = "SELECT e FROM MigrationDTOPersonengruppenPersonen e WHERE e.ID IN :value")
@NamedQuery(name = "MigrationDTOPersonengruppenPersonen.all.migration", query = "SELECT e FROM MigrationDTOPersonengruppenPersonen e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "SchulnrEigner", "Gruppe_ID", "Person_ID", "PersonNr", "PersonArt", "PersonName", "PersonVorname", "PersonPLZ", "PersonOrt", "PersonStrasse", "PersonStrassenname", "PersonHausNr", "PersonHausNrZusatz", "PersonTelefon", "PersonMobil", "PersonEmail", "Bemerkung", "Zusatzinfo", "Sortierung", "PersonAnrede", "PersonAkadGrad"})
public final class MigrationDTOPersonengruppenPersonen {

	/** ID des Personeneintrags zur Personengruppe */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/** GruppenID des Personeneintrags zur Personengruppe */
	@Column(name = "Gruppe_ID")
	@JsonProperty
	public Long Gruppe_ID;

	/** PersonenID des Personeneintrags zur Personengruppe wenn in DB vorhandne */
	@Column(name = "Person_ID")
	@JsonProperty
	public Long Person_ID;

	/** Personennummer des Personeneintrags zur Personengruppe */
	@Column(name = "PersonNr")
	@JsonProperty
	public Integer PersonNr;

	/** PersonenArt des Personeneintrags zur Personengruppe */
	@Column(name = "PersonArt")
	@JsonProperty
	public String PersonArt;

	/** Name des Personeneintrags zur Personengruppe */
	@Column(name = "PersonName")
	@JsonProperty
	public String PersonName;

	/** Vorname des Personeneintrags zur Personengruppe */
	@Column(name = "PersonVorname")
	@JsonProperty
	public String PersonVorname;

	/** PLZ des Personeneintrags zur Personengruppe */
	@Column(name = "PersonPLZ")
	@JsonProperty
	public String PersonPLZ;

	/** Ort des Personeneintrags zur Personengruppe */
	@Column(name = "PersonOrt")
	@JsonProperty
	public String PersonOrt;

	/** Straße des Personeneintrags zur Personengruppe */
	@Column(name = "PersonStrasse")
	@JsonProperty
	public String PersonStrasse;

	/** Straßenname zur Person der Personengruppe */
	@Column(name = "PersonStrassenname")
	@JsonProperty
	public String PersonStrassenname;

	/** Hausnummer wenn getrennt gespeichert */
	@Column(name = "PersonHausNr")
	@JsonProperty
	public String PersonHausNr;

	/** Zusatz zur Hausnummer wenn Hausnummern getrennt gespeichert werden */
	@Column(name = "PersonHausNrZusatz")
	@JsonProperty
	public String PersonHausNrZusatz;

	/** Telfonnummer des Personeneintrags zur Personengruppe */
	@Column(name = "PersonTelefon")
	@JsonProperty
	public String PersonTelefon;

	/** Mobilnummer des Personeneintrags zur Personengruppe */
	@Column(name = "PersonMobil")
	@JsonProperty
	public String PersonMobil;

	/** Email  des Personeneintrags zur Personengruppe */
	@Column(name = "PersonEmail")
	@JsonProperty
	public String PersonEmail;

	/** Bemerkung des Personeneintrags zur Personengruppe */
	@Column(name = "Bemerkung")
	@JsonProperty
	public String Bemerkung;

	/** Zusatzinfo des Personeneintrags zur Personengruppe */
	@Column(name = "Zusatzinfo")
	@JsonProperty
	public String Zusatzinfo;

	/** Sortierung des Personeneintrags zur Personengruppe */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Anrede des Personeneintrags zur Personengruppe */
	@Column(name = "PersonAnrede")
	@JsonProperty
	public String PersonAnrede;

	/** DEPRECATED: Titel des Personeneintrags zur Personengruppe */
	@Column(name = "PersonAkadGrad")
	@JsonProperty
	public String PersonAkadGrad;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOPersonengruppenPersonen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOPersonengruppenPersonen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOPersonengruppenPersonen ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param SchulnrEigner   der Wert für das Attribut SchulnrEigner
	 * @param Gruppe_ID   der Wert für das Attribut Gruppe_ID
	 * @param PersonName   der Wert für das Attribut PersonName
	 */
	public MigrationDTOPersonengruppenPersonen(final Long ID, final Integer SchulnrEigner, final Long Gruppe_ID, final String PersonName) {
		if (ID == null) {
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (SchulnrEigner == null) {
			throw new NullPointerException("SchulnrEigner must not be null");
		}
		this.SchulnrEigner = SchulnrEigner;
		if (Gruppe_ID == null) {
			throw new NullPointerException("Gruppe_ID must not be null");
		}
		this.Gruppe_ID = Gruppe_ID;
		if (PersonName == null) {
			throw new NullPointerException("PersonName must not be null");
		}
		this.PersonName = PersonName;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOPersonengruppenPersonen other = (MigrationDTOPersonengruppenPersonen) obj;
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
		return "MigrationDTOPersonengruppenPersonen(ID=" + this.ID + ", SchulnrEigner=" + this.SchulnrEigner + ", Gruppe_ID=" + this.Gruppe_ID + ", Person_ID=" + this.Person_ID + ", PersonNr=" + this.PersonNr + ", PersonArt=" + this.PersonArt + ", PersonName=" + this.PersonName + ", PersonVorname=" + this.PersonVorname + ", PersonPLZ=" + this.PersonPLZ + ", PersonOrt=" + this.PersonOrt + ", PersonStrasse=" + this.PersonStrasse + ", PersonStrassenname=" + this.PersonStrassenname + ", PersonHausNr=" + this.PersonHausNr + ", PersonHausNrZusatz=" + this.PersonHausNrZusatz + ", PersonTelefon=" + this.PersonTelefon + ", PersonMobil=" + this.PersonMobil + ", PersonEmail=" + this.PersonEmail + ", Bemerkung=" + this.Bemerkung + ", Zusatzinfo=" + this.Zusatzinfo + ", Sortierung=" + this.Sortierung + ", PersonAnrede=" + this.PersonAnrede + ", PersonAkadGrad=" + this.PersonAkadGrad + ")";
	}

}
