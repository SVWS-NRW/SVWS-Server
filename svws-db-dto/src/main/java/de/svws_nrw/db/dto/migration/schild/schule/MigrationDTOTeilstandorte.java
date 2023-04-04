package de.svws_nrw.db.dto.migration.schild.schule;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle EigeneSchule_Teilstandorte.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "EigeneSchule_Teilstandorte")
@NamedQuery(name = "MigrationDTOTeilstandorte.all", query = "SELECT e FROM MigrationDTOTeilstandorte e")
@NamedQuery(name = "MigrationDTOTeilstandorte.schulnreigner", query = "SELECT e FROM MigrationDTOTeilstandorte e WHERE e.SchulnrEigner = :value")
@NamedQuery(name = "MigrationDTOTeilstandorte.schulnreigner.multiple", query = "SELECT e FROM MigrationDTOTeilstandorte e WHERE e.SchulnrEigner IN :value")
@NamedQuery(name = "MigrationDTOTeilstandorte.adrmerkmal", query = "SELECT e FROM MigrationDTOTeilstandorte e WHERE e.AdrMerkmal = :value")
@NamedQuery(name = "MigrationDTOTeilstandorte.adrmerkmal.multiple", query = "SELECT e FROM MigrationDTOTeilstandorte e WHERE e.AdrMerkmal IN :value")
@NamedQuery(name = "MigrationDTOTeilstandorte.plz", query = "SELECT e FROM MigrationDTOTeilstandorte e WHERE e.PLZ = :value")
@NamedQuery(name = "MigrationDTOTeilstandorte.plz.multiple", query = "SELECT e FROM MigrationDTOTeilstandorte e WHERE e.PLZ IN :value")
@NamedQuery(name = "MigrationDTOTeilstandorte.ort", query = "SELECT e FROM MigrationDTOTeilstandorte e WHERE e.Ort = :value")
@NamedQuery(name = "MigrationDTOTeilstandorte.ort.multiple", query = "SELECT e FROM MigrationDTOTeilstandorte e WHERE e.Ort IN :value")
@NamedQuery(name = "MigrationDTOTeilstandorte.strasse", query = "SELECT e FROM MigrationDTOTeilstandorte e WHERE e.Strasse = :value")
@NamedQuery(name = "MigrationDTOTeilstandorte.strasse.multiple", query = "SELECT e FROM MigrationDTOTeilstandorte e WHERE e.Strasse IN :value")
@NamedQuery(name = "MigrationDTOTeilstandorte.strassenname", query = "SELECT e FROM MigrationDTOTeilstandorte e WHERE e.Strassenname = :value")
@NamedQuery(name = "MigrationDTOTeilstandorte.strassenname.multiple", query = "SELECT e FROM MigrationDTOTeilstandorte e WHERE e.Strassenname IN :value")
@NamedQuery(name = "MigrationDTOTeilstandorte.hausnr", query = "SELECT e FROM MigrationDTOTeilstandorte e WHERE e.HausNr = :value")
@NamedQuery(name = "MigrationDTOTeilstandorte.hausnr.multiple", query = "SELECT e FROM MigrationDTOTeilstandorte e WHERE e.HausNr IN :value")
@NamedQuery(name = "MigrationDTOTeilstandorte.hausnrzusatz", query = "SELECT e FROM MigrationDTOTeilstandorte e WHERE e.HausNrZusatz = :value")
@NamedQuery(name = "MigrationDTOTeilstandorte.hausnrzusatz.multiple", query = "SELECT e FROM MigrationDTOTeilstandorte e WHERE e.HausNrZusatz IN :value")
@NamedQuery(name = "MigrationDTOTeilstandorte.bemerkung", query = "SELECT e FROM MigrationDTOTeilstandorte e WHERE e.Bemerkung = :value")
@NamedQuery(name = "MigrationDTOTeilstandorte.bemerkung.multiple", query = "SELECT e FROM MigrationDTOTeilstandorte e WHERE e.Bemerkung IN :value")
@NamedQuery(name = "MigrationDTOTeilstandorte.kuerzel", query = "SELECT e FROM MigrationDTOTeilstandorte e WHERE e.Kuerzel = :value")
@NamedQuery(name = "MigrationDTOTeilstandorte.kuerzel.multiple", query = "SELECT e FROM MigrationDTOTeilstandorte e WHERE e.Kuerzel IN :value")
@NamedQuery(name = "MigrationDTOTeilstandorte.primaryKeyQuery", query = "SELECT e FROM MigrationDTOTeilstandorte e WHERE e.AdrMerkmal = ?1")
@NamedQuery(name = "MigrationDTOTeilstandorte.all.migration", query = "SELECT e FROM MigrationDTOTeilstandorte e WHERE e.AdrMerkmal IS NOT NULL")
@JsonPropertyOrder({"SchulnrEigner", "AdrMerkmal", "PLZ", "Ort", "Strasse", "Strassenname", "HausNr", "HausNrZusatz", "Bemerkung", "Kuerzel"})
public final class MigrationDTOTeilstandorte {

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

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

	/** Straße des Teilstandortes */
	@Column(name = "Strasse")
	@JsonProperty
	public String Strasse;

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
	 * Erstellt ein neues Objekt der Klasse MigrationDTOTeilstandorte ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOTeilstandorte() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOTeilstandorte ohne eine Initialisierung der Attribute.
	 * @param SchulnrEigner   der Wert für das Attribut SchulnrEigner
	 * @param AdrMerkmal   der Wert für das Attribut AdrMerkmal
	 */
	public MigrationDTOTeilstandorte(final Integer SchulnrEigner, final String AdrMerkmal) {
		if (SchulnrEigner == null) {
			throw new NullPointerException("SchulnrEigner must not be null");
		}
		this.SchulnrEigner = SchulnrEigner;
		if (AdrMerkmal == null) {
			throw new NullPointerException("AdrMerkmal must not be null");
		}
		this.AdrMerkmal = AdrMerkmal;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOTeilstandorte other = (MigrationDTOTeilstandorte) obj;
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
		return "MigrationDTOTeilstandorte(SchulnrEigner=" + this.SchulnrEigner + ", AdrMerkmal=" + this.AdrMerkmal + ", PLZ=" + this.PLZ + ", Ort=" + this.Ort + ", Strasse=" + this.Strasse + ", Strassenname=" + this.Strassenname + ", HausNr=" + this.HausNr + ", HausNrZusatz=" + this.HausNrZusatz + ", Bemerkung=" + this.Bemerkung + ", Kuerzel=" + this.Kuerzel + ")";
	}

}
