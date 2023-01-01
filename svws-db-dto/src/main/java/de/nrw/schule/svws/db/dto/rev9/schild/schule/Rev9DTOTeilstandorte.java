package de.nrw.schule.svws.db.dto.rev9.schild.schule;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle EigeneSchule_Teilstandorte.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "EigeneSchule_Teilstandorte")
@NamedQuery(name="Rev9DTOTeilstandorte.all", query="SELECT e FROM Rev9DTOTeilstandorte e")
@NamedQuery(name="Rev9DTOTeilstandorte.adrmerkmal", query="SELECT e FROM Rev9DTOTeilstandorte e WHERE e.AdrMerkmal = :value")
@NamedQuery(name="Rev9DTOTeilstandorte.adrmerkmal.multiple", query="SELECT e FROM Rev9DTOTeilstandorte e WHERE e.AdrMerkmal IN :value")
@NamedQuery(name="Rev9DTOTeilstandorte.plz", query="SELECT e FROM Rev9DTOTeilstandorte e WHERE e.PLZ = :value")
@NamedQuery(name="Rev9DTOTeilstandorte.plz.multiple", query="SELECT e FROM Rev9DTOTeilstandorte e WHERE e.PLZ IN :value")
@NamedQuery(name="Rev9DTOTeilstandorte.ort", query="SELECT e FROM Rev9DTOTeilstandorte e WHERE e.Ort = :value")
@NamedQuery(name="Rev9DTOTeilstandorte.ort.multiple", query="SELECT e FROM Rev9DTOTeilstandorte e WHERE e.Ort IN :value")
@NamedQuery(name="Rev9DTOTeilstandorte.strassenname", query="SELECT e FROM Rev9DTOTeilstandorte e WHERE e.Strassenname = :value")
@NamedQuery(name="Rev9DTOTeilstandorte.strassenname.multiple", query="SELECT e FROM Rev9DTOTeilstandorte e WHERE e.Strassenname IN :value")
@NamedQuery(name="Rev9DTOTeilstandorte.hausnr", query="SELECT e FROM Rev9DTOTeilstandorte e WHERE e.HausNr = :value")
@NamedQuery(name="Rev9DTOTeilstandorte.hausnr.multiple", query="SELECT e FROM Rev9DTOTeilstandorte e WHERE e.HausNr IN :value")
@NamedQuery(name="Rev9DTOTeilstandorte.hausnrzusatz", query="SELECT e FROM Rev9DTOTeilstandorte e WHERE e.HausNrZusatz = :value")
@NamedQuery(name="Rev9DTOTeilstandorte.hausnrzusatz.multiple", query="SELECT e FROM Rev9DTOTeilstandorte e WHERE e.HausNrZusatz IN :value")
@NamedQuery(name="Rev9DTOTeilstandorte.bemerkung", query="SELECT e FROM Rev9DTOTeilstandorte e WHERE e.Bemerkung = :value")
@NamedQuery(name="Rev9DTOTeilstandorte.bemerkung.multiple", query="SELECT e FROM Rev9DTOTeilstandorte e WHERE e.Bemerkung IN :value")
@NamedQuery(name="Rev9DTOTeilstandorte.kuerzel", query="SELECT e FROM Rev9DTOTeilstandorte e WHERE e.Kuerzel = :value")
@NamedQuery(name="Rev9DTOTeilstandorte.kuerzel.multiple", query="SELECT e FROM Rev9DTOTeilstandorte e WHERE e.Kuerzel IN :value")
@NamedQuery(name="Rev9DTOTeilstandorte.primaryKeyQuery", query="SELECT e FROM Rev9DTOTeilstandorte e WHERE e.AdrMerkmal = ?1")
@NamedQuery(name="Rev9DTOTeilstandorte.all.migration", query="SELECT e FROM Rev9DTOTeilstandorte e WHERE e.AdrMerkmal IS NOT NULL")
@JsonPropertyOrder({"AdrMerkmal","PLZ","Ort","Strassenname","HausNr","HausNrZusatz","Bemerkung","Kuerzel"})
public class Rev9DTOTeilstandorte {

	/** Adressmerkmal des Teilstandortes (A...Z) */
	@Id
	@Column(name = "AdrMerkmal")
	@JsonProperty
	public String AdrMerkmal;

	/** Postleitzahl des Teilstandortes */
	@Column(name = "PLZ")
	@JsonProperty
	public String PLZ;

	/** Ort des Teilstandortes */
	@Column(name = "Ort")
	@JsonProperty
	public String Ort;

	/** Straßenname des Teilstandortes */
	@Column(name = "Strassenname")
	@JsonProperty
	public String Strassenname;

	/** Hausnummer des Teilstandortes */
	@Column(name = "HausNr")
	@JsonProperty
	public String HausNr;

	/** Hausnummerzusatz des Teilstandortes */
	@Column(name = "HausNrZusatz")
	@JsonProperty
	public String HausNrZusatz;

	/** Bemerkung zum Teilstandort (Text) */
	@Column(name = "Bemerkung")
	@JsonProperty
	public String Bemerkung;

	/** Kürzel des Teilstandortes */
	@Column(name = "Kuerzel")
	@JsonProperty
	public String Kuerzel;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev9DTOTeilstandorte ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev9DTOTeilstandorte() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev9DTOTeilstandorte ohne eine Initialisierung der Attribute.
	 * @param AdrMerkmal   der Wert für das Attribut AdrMerkmal
	 */
	public Rev9DTOTeilstandorte(final String AdrMerkmal) {
		if (AdrMerkmal == null) { 
			throw new NullPointerException("AdrMerkmal must not be null");
		}
		this.AdrMerkmal = AdrMerkmal;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev9DTOTeilstandorte other = (Rev9DTOTeilstandorte) obj;
		if (AdrMerkmal == null) {
			if (other.AdrMerkmal != null)
				return false;
		} else if (!AdrMerkmal.equals(other.AdrMerkmal))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((AdrMerkmal == null) ? 0 : AdrMerkmal.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "Rev9DTOTeilstandorte(AdrMerkmal=" + this.AdrMerkmal + ", PLZ=" + this.PLZ + ", Ort=" + this.Ort + ", Strassenname=" + this.Strassenname + ", HausNr=" + this.HausNr + ", HausNrZusatz=" + this.HausNrZusatz + ", Bemerkung=" + this.Bemerkung + ", Kuerzel=" + this.Kuerzel + ")";
	}

}