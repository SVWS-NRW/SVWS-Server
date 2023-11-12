package de.svws_nrw.db.dto.current.schild.personengruppen;

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
@NamedQuery(name = "DTOPersonengruppenPersonen.all", query = "SELECT e FROM DTOPersonengruppenPersonen e")
@NamedQuery(name = "DTOPersonengruppenPersonen.id", query = "SELECT e FROM DTOPersonengruppenPersonen e WHERE e.ID = :value")
@NamedQuery(name = "DTOPersonengruppenPersonen.id.multiple", query = "SELECT e FROM DTOPersonengruppenPersonen e WHERE e.ID IN :value")
@NamedQuery(name = "DTOPersonengruppenPersonen.gruppe_id", query = "SELECT e FROM DTOPersonengruppenPersonen e WHERE e.Gruppe_ID = :value")
@NamedQuery(name = "DTOPersonengruppenPersonen.gruppe_id.multiple", query = "SELECT e FROM DTOPersonengruppenPersonen e WHERE e.Gruppe_ID IN :value")
@NamedQuery(name = "DTOPersonengruppenPersonen.person_id", query = "SELECT e FROM DTOPersonengruppenPersonen e WHERE e.Person_ID = :value")
@NamedQuery(name = "DTOPersonengruppenPersonen.person_id.multiple", query = "SELECT e FROM DTOPersonengruppenPersonen e WHERE e.Person_ID IN :value")
@NamedQuery(name = "DTOPersonengruppenPersonen.personnr", query = "SELECT e FROM DTOPersonengruppenPersonen e WHERE e.PersonNr = :value")
@NamedQuery(name = "DTOPersonengruppenPersonen.personnr.multiple", query = "SELECT e FROM DTOPersonengruppenPersonen e WHERE e.PersonNr IN :value")
@NamedQuery(name = "DTOPersonengruppenPersonen.personart", query = "SELECT e FROM DTOPersonengruppenPersonen e WHERE e.PersonArt = :value")
@NamedQuery(name = "DTOPersonengruppenPersonen.personart.multiple", query = "SELECT e FROM DTOPersonengruppenPersonen e WHERE e.PersonArt IN :value")
@NamedQuery(name = "DTOPersonengruppenPersonen.personname", query = "SELECT e FROM DTOPersonengruppenPersonen e WHERE e.PersonName = :value")
@NamedQuery(name = "DTOPersonengruppenPersonen.personname.multiple", query = "SELECT e FROM DTOPersonengruppenPersonen e WHERE e.PersonName IN :value")
@NamedQuery(name = "DTOPersonengruppenPersonen.personvorname", query = "SELECT e FROM DTOPersonengruppenPersonen e WHERE e.PersonVorname = :value")
@NamedQuery(name = "DTOPersonengruppenPersonen.personvorname.multiple", query = "SELECT e FROM DTOPersonengruppenPersonen e WHERE e.PersonVorname IN :value")
@NamedQuery(name = "DTOPersonengruppenPersonen.personplz", query = "SELECT e FROM DTOPersonengruppenPersonen e WHERE e.PersonPLZ = :value")
@NamedQuery(name = "DTOPersonengruppenPersonen.personplz.multiple", query = "SELECT e FROM DTOPersonengruppenPersonen e WHERE e.PersonPLZ IN :value")
@NamedQuery(name = "DTOPersonengruppenPersonen.personort", query = "SELECT e FROM DTOPersonengruppenPersonen e WHERE e.PersonOrt = :value")
@NamedQuery(name = "DTOPersonengruppenPersonen.personort.multiple", query = "SELECT e FROM DTOPersonengruppenPersonen e WHERE e.PersonOrt IN :value")
@NamedQuery(name = "DTOPersonengruppenPersonen.personstrassenname", query = "SELECT e FROM DTOPersonengruppenPersonen e WHERE e.PersonStrassenname = :value")
@NamedQuery(name = "DTOPersonengruppenPersonen.personstrassenname.multiple", query = "SELECT e FROM DTOPersonengruppenPersonen e WHERE e.PersonStrassenname IN :value")
@NamedQuery(name = "DTOPersonengruppenPersonen.personhausnr", query = "SELECT e FROM DTOPersonengruppenPersonen e WHERE e.PersonHausNr = :value")
@NamedQuery(name = "DTOPersonengruppenPersonen.personhausnr.multiple", query = "SELECT e FROM DTOPersonengruppenPersonen e WHERE e.PersonHausNr IN :value")
@NamedQuery(name = "DTOPersonengruppenPersonen.personhausnrzusatz", query = "SELECT e FROM DTOPersonengruppenPersonen e WHERE e.PersonHausNrZusatz = :value")
@NamedQuery(name = "DTOPersonengruppenPersonen.personhausnrzusatz.multiple", query = "SELECT e FROM DTOPersonengruppenPersonen e WHERE e.PersonHausNrZusatz IN :value")
@NamedQuery(name = "DTOPersonengruppenPersonen.persontelefon", query = "SELECT e FROM DTOPersonengruppenPersonen e WHERE e.PersonTelefon = :value")
@NamedQuery(name = "DTOPersonengruppenPersonen.persontelefon.multiple", query = "SELECT e FROM DTOPersonengruppenPersonen e WHERE e.PersonTelefon IN :value")
@NamedQuery(name = "DTOPersonengruppenPersonen.personmobil", query = "SELECT e FROM DTOPersonengruppenPersonen e WHERE e.PersonMobil = :value")
@NamedQuery(name = "DTOPersonengruppenPersonen.personmobil.multiple", query = "SELECT e FROM DTOPersonengruppenPersonen e WHERE e.PersonMobil IN :value")
@NamedQuery(name = "DTOPersonengruppenPersonen.personemail", query = "SELECT e FROM DTOPersonengruppenPersonen e WHERE e.PersonEmail = :value")
@NamedQuery(name = "DTOPersonengruppenPersonen.personemail.multiple", query = "SELECT e FROM DTOPersonengruppenPersonen e WHERE e.PersonEmail IN :value")
@NamedQuery(name = "DTOPersonengruppenPersonen.bemerkung", query = "SELECT e FROM DTOPersonengruppenPersonen e WHERE e.Bemerkung = :value")
@NamedQuery(name = "DTOPersonengruppenPersonen.bemerkung.multiple", query = "SELECT e FROM DTOPersonengruppenPersonen e WHERE e.Bemerkung IN :value")
@NamedQuery(name = "DTOPersonengruppenPersonen.zusatzinfo", query = "SELECT e FROM DTOPersonengruppenPersonen e WHERE e.Zusatzinfo = :value")
@NamedQuery(name = "DTOPersonengruppenPersonen.zusatzinfo.multiple", query = "SELECT e FROM DTOPersonengruppenPersonen e WHERE e.Zusatzinfo IN :value")
@NamedQuery(name = "DTOPersonengruppenPersonen.sortierung", query = "SELECT e FROM DTOPersonengruppenPersonen e WHERE e.Sortierung = :value")
@NamedQuery(name = "DTOPersonengruppenPersonen.sortierung.multiple", query = "SELECT e FROM DTOPersonengruppenPersonen e WHERE e.Sortierung IN :value")
@NamedQuery(name = "DTOPersonengruppenPersonen.personanrede", query = "SELECT e FROM DTOPersonengruppenPersonen e WHERE e.PersonAnrede = :value")
@NamedQuery(name = "DTOPersonengruppenPersonen.personanrede.multiple", query = "SELECT e FROM DTOPersonengruppenPersonen e WHERE e.PersonAnrede IN :value")
@NamedQuery(name = "DTOPersonengruppenPersonen.personakadgrad", query = "SELECT e FROM DTOPersonengruppenPersonen e WHERE e.PersonAkadGrad = :value")
@NamedQuery(name = "DTOPersonengruppenPersonen.personakadgrad.multiple", query = "SELECT e FROM DTOPersonengruppenPersonen e WHERE e.PersonAkadGrad IN :value")
@NamedQuery(name = "DTOPersonengruppenPersonen.primaryKeyQuery", query = "SELECT e FROM DTOPersonengruppenPersonen e WHERE e.ID = ?1")
@NamedQuery(name = "DTOPersonengruppenPersonen.primaryKeyQuery.multiple", query = "SELECT e FROM DTOPersonengruppenPersonen e WHERE e.ID IN ?1")
@NamedQuery(name = "DTOPersonengruppenPersonen.all.migration", query = "SELECT e FROM DTOPersonengruppenPersonen e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Gruppe_ID", "Person_ID", "PersonNr", "PersonArt", "PersonName", "PersonVorname", "PersonPLZ", "PersonOrt", "PersonStrassenname", "PersonHausNr", "PersonHausNrZusatz", "PersonTelefon", "PersonMobil", "PersonEmail", "Bemerkung", "Zusatzinfo", "Sortierung", "PersonAnrede", "PersonAkadGrad"})
public final class DTOPersonengruppenPersonen {

	/** ID des Personeneintrags zur Personengruppe */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** GruppenID des Personeneintrags zur Personengruppe */
	@Column(name = "Gruppe_ID")
	@JsonProperty
	public long Gruppe_ID;

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
	 * Erstellt ein neues Objekt der Klasse DTOPersonengruppenPersonen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOPersonengruppenPersonen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOPersonengruppenPersonen ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Gruppe_ID   der Wert für das Attribut Gruppe_ID
	 * @param PersonName   der Wert für das Attribut PersonName
	 */
	public DTOPersonengruppenPersonen(final long ID, final long Gruppe_ID, final String PersonName) {
		this.ID = ID;
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
		DTOPersonengruppenPersonen other = (DTOPersonengruppenPersonen) obj;
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
		return "DTOPersonengruppenPersonen(ID=" + this.ID + ", Gruppe_ID=" + this.Gruppe_ID + ", Person_ID=" + this.Person_ID + ", PersonNr=" + this.PersonNr + ", PersonArt=" + this.PersonArt + ", PersonName=" + this.PersonName + ", PersonVorname=" + this.PersonVorname + ", PersonPLZ=" + this.PersonPLZ + ", PersonOrt=" + this.PersonOrt + ", PersonStrassenname=" + this.PersonStrassenname + ", PersonHausNr=" + this.PersonHausNr + ", PersonHausNrZusatz=" + this.PersonHausNrZusatz + ", PersonTelefon=" + this.PersonTelefon + ", PersonMobil=" + this.PersonMobil + ", PersonEmail=" + this.PersonEmail + ", Bemerkung=" + this.Bemerkung + ", Zusatzinfo=" + this.Zusatzinfo + ", Sortierung=" + this.Sortierung + ", PersonAnrede=" + this.PersonAnrede + ", PersonAkadGrad=" + this.PersonAkadGrad + ")";
	}

}
