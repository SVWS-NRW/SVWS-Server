package de.nrw.schule.svws.db.dto.rev9.schild.katalog;

import de.nrw.schule.svws.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle AllgAdrAnsprechpartner.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "AllgAdrAnsprechpartner")
@NamedQuery(name="Rev9DTOAnsprechpartnerAllgemeineAdresse.all", query="SELECT e FROM Rev9DTOAnsprechpartnerAllgemeineAdresse e")
@NamedQuery(name="Rev9DTOAnsprechpartnerAllgemeineAdresse.id", query="SELECT e FROM Rev9DTOAnsprechpartnerAllgemeineAdresse e WHERE e.ID = :value")
@NamedQuery(name="Rev9DTOAnsprechpartnerAllgemeineAdresse.id.multiple", query="SELECT e FROM Rev9DTOAnsprechpartnerAllgemeineAdresse e WHERE e.ID IN :value")
@NamedQuery(name="Rev9DTOAnsprechpartnerAllgemeineAdresse.adresse_id", query="SELECT e FROM Rev9DTOAnsprechpartnerAllgemeineAdresse e WHERE e.Adresse_ID = :value")
@NamedQuery(name="Rev9DTOAnsprechpartnerAllgemeineAdresse.adresse_id.multiple", query="SELECT e FROM Rev9DTOAnsprechpartnerAllgemeineAdresse e WHERE e.Adresse_ID IN :value")
@NamedQuery(name="Rev9DTOAnsprechpartnerAllgemeineAdresse.name", query="SELECT e FROM Rev9DTOAnsprechpartnerAllgemeineAdresse e WHERE e.Name = :value")
@NamedQuery(name="Rev9DTOAnsprechpartnerAllgemeineAdresse.name.multiple", query="SELECT e FROM Rev9DTOAnsprechpartnerAllgemeineAdresse e WHERE e.Name IN :value")
@NamedQuery(name="Rev9DTOAnsprechpartnerAllgemeineAdresse.vorname", query="SELECT e FROM Rev9DTOAnsprechpartnerAllgemeineAdresse e WHERE e.Vorname = :value")
@NamedQuery(name="Rev9DTOAnsprechpartnerAllgemeineAdresse.vorname.multiple", query="SELECT e FROM Rev9DTOAnsprechpartnerAllgemeineAdresse e WHERE e.Vorname IN :value")
@NamedQuery(name="Rev9DTOAnsprechpartnerAllgemeineAdresse.anrede", query="SELECT e FROM Rev9DTOAnsprechpartnerAllgemeineAdresse e WHERE e.Anrede = :value")
@NamedQuery(name="Rev9DTOAnsprechpartnerAllgemeineAdresse.anrede.multiple", query="SELECT e FROM Rev9DTOAnsprechpartnerAllgemeineAdresse e WHERE e.Anrede IN :value")
@NamedQuery(name="Rev9DTOAnsprechpartnerAllgemeineAdresse.telefon", query="SELECT e FROM Rev9DTOAnsprechpartnerAllgemeineAdresse e WHERE e.Telefon = :value")
@NamedQuery(name="Rev9DTOAnsprechpartnerAllgemeineAdresse.telefon.multiple", query="SELECT e FROM Rev9DTOAnsprechpartnerAllgemeineAdresse e WHERE e.Telefon IN :value")
@NamedQuery(name="Rev9DTOAnsprechpartnerAllgemeineAdresse.email", query="SELECT e FROM Rev9DTOAnsprechpartnerAllgemeineAdresse e WHERE e.Email = :value")
@NamedQuery(name="Rev9DTOAnsprechpartnerAllgemeineAdresse.email.multiple", query="SELECT e FROM Rev9DTOAnsprechpartnerAllgemeineAdresse e WHERE e.Email IN :value")
@NamedQuery(name="Rev9DTOAnsprechpartnerAllgemeineAdresse.abteilung", query="SELECT e FROM Rev9DTOAnsprechpartnerAllgemeineAdresse e WHERE e.Abteilung = :value")
@NamedQuery(name="Rev9DTOAnsprechpartnerAllgemeineAdresse.abteilung.multiple", query="SELECT e FROM Rev9DTOAnsprechpartnerAllgemeineAdresse e WHERE e.Abteilung IN :value")
@NamedQuery(name="Rev9DTOAnsprechpartnerAllgemeineAdresse.titel", query="SELECT e FROM Rev9DTOAnsprechpartnerAllgemeineAdresse e WHERE e.Titel = :value")
@NamedQuery(name="Rev9DTOAnsprechpartnerAllgemeineAdresse.titel.multiple", query="SELECT e FROM Rev9DTOAnsprechpartnerAllgemeineAdresse e WHERE e.Titel IN :value")
@NamedQuery(name="Rev9DTOAnsprechpartnerAllgemeineAdresse.gu_id", query="SELECT e FROM Rev9DTOAnsprechpartnerAllgemeineAdresse e WHERE e.GU_ID = :value")
@NamedQuery(name="Rev9DTOAnsprechpartnerAllgemeineAdresse.gu_id.multiple", query="SELECT e FROM Rev9DTOAnsprechpartnerAllgemeineAdresse e WHERE e.GU_ID IN :value")
@NamedQuery(name="Rev9DTOAnsprechpartnerAllgemeineAdresse.primaryKeyQuery", query="SELECT e FROM Rev9DTOAnsprechpartnerAllgemeineAdresse e WHERE e.ID = ?1")
@NamedQuery(name="Rev9DTOAnsprechpartnerAllgemeineAdresse.all.migration", query="SELECT e FROM Rev9DTOAnsprechpartnerAllgemeineAdresse e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Adresse_ID","Name","Vorname","Anrede","Telefon","Email","Abteilung","Titel","GU_ID"})
public class Rev9DTOAnsprechpartnerAllgemeineAdresse {

	/** ID des Ansprechpartners der Tabelle AllgAdresse (Betriebe) */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** ID des Betriebs (der Adresse) aus der Tabelle AllgAdresse */
	@Column(name = "Adresse_ID")
	@JsonProperty
	public Long Adresse_ID;

	/** Name des Ansprechpartners im Betrieb PAuswG vom 21.6.2019 §5 Abs. 2 */
	@Column(name = "Name")
	@JsonProperty
	public String Name;

	/** Vorname des Ansprechpartners im Betrieb PAuswG vom 21.6.2019 §5 Abs. 2 */
	@Column(name = "Vorname")
	@JsonProperty
	public String Vorname;

	/** Anrede des Ansprechpartners im Betrieb */
	@Column(name = "Anrede")
	@JsonProperty
	public String Anrede;

	/** Telefonnummer des Ansprechpartners im Betrieb */
	@Column(name = "Telefon")
	@JsonProperty
	public String Telefon;

	/** Email-Adresse des Ansprechpartners im Betrieb */
	@Column(name = "Email")
	@JsonProperty
	public String Email;

	/** ggf Abteilung des Ansprechpartners im Betrieb */
	@Column(name = "Abteilung")
	@JsonProperty
	public String Abteilung;

	/** Titel des Ansprechpartners im Betrieb */
	@Column(name = "Titel")
	@JsonProperty
	public String Titel;

	/** GU_ID des Ansprechpartners im Betrieb */
	@Column(name = "GU_ID")
	@JsonProperty
	public String GU_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev9DTOAnsprechpartnerAllgemeineAdresse ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev9DTOAnsprechpartnerAllgemeineAdresse() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev9DTOAnsprechpartnerAllgemeineAdresse ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Adresse_ID   der Wert für das Attribut Adresse_ID
	 */
	public Rev9DTOAnsprechpartnerAllgemeineAdresse(final Long ID, final Long Adresse_ID) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Adresse_ID == null) { 
			throw new NullPointerException("Adresse_ID must not be null");
		}
		this.Adresse_ID = Adresse_ID;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev9DTOAnsprechpartnerAllgemeineAdresse other = (Rev9DTOAnsprechpartnerAllgemeineAdresse) obj;
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
		return "Rev9DTOAnsprechpartnerAllgemeineAdresse(ID=" + this.ID + ", Adresse_ID=" + this.Adresse_ID + ", Name=" + this.Name + ", Vorname=" + this.Vorname + ", Anrede=" + this.Anrede + ", Telefon=" + this.Telefon + ", Email=" + this.Email + ", Abteilung=" + this.Abteilung + ", Titel=" + this.Titel + ", GU_ID=" + this.GU_ID + ")";
	}

}