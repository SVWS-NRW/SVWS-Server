package de.svws_nrw.db.dto.migration.schild.gost;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle NichtMoeglAbiFachKombi.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "NichtMoeglAbiFachKombi")
@NamedQuery(name = "MigrationDTOFaecherNichtMoeglicheKombination.all", query = "SELECT e FROM MigrationDTOFaecherNichtMoeglicheKombination e")
@NamedQuery(name = "MigrationDTOFaecherNichtMoeglicheKombination.fach1_id", query = "SELECT e FROM MigrationDTOFaecherNichtMoeglicheKombination e WHERE e.Fach1_ID = :value")
@NamedQuery(name = "MigrationDTOFaecherNichtMoeglicheKombination.fach1_id.multiple", query = "SELECT e FROM MigrationDTOFaecherNichtMoeglicheKombination e WHERE e.Fach1_ID IN :value")
@NamedQuery(name = "MigrationDTOFaecherNichtMoeglicheKombination.fach2_id", query = "SELECT e FROM MigrationDTOFaecherNichtMoeglicheKombination e WHERE e.Fach2_ID = :value")
@NamedQuery(name = "MigrationDTOFaecherNichtMoeglicheKombination.fach2_id.multiple", query = "SELECT e FROM MigrationDTOFaecherNichtMoeglicheKombination e WHERE e.Fach2_ID IN :value")
@NamedQuery(name = "MigrationDTOFaecherNichtMoeglicheKombination.schulnreigner", query = "SELECT e FROM MigrationDTOFaecherNichtMoeglicheKombination e WHERE e.SchulnrEigner = :value")
@NamedQuery(name = "MigrationDTOFaecherNichtMoeglicheKombination.schulnreigner.multiple", query = "SELECT e FROM MigrationDTOFaecherNichtMoeglicheKombination e WHERE e.SchulnrEigner IN :value")
@NamedQuery(name = "MigrationDTOFaecherNichtMoeglicheKombination.kursart1", query = "SELECT e FROM MigrationDTOFaecherNichtMoeglicheKombination e WHERE e.Kursart1 = :value")
@NamedQuery(name = "MigrationDTOFaecherNichtMoeglicheKombination.kursart1.multiple", query = "SELECT e FROM MigrationDTOFaecherNichtMoeglicheKombination e WHERE e.Kursart1 IN :value")
@NamedQuery(name = "MigrationDTOFaecherNichtMoeglicheKombination.kursart2", query = "SELECT e FROM MigrationDTOFaecherNichtMoeglicheKombination e WHERE e.Kursart2 = :value")
@NamedQuery(name = "MigrationDTOFaecherNichtMoeglicheKombination.kursart2.multiple", query = "SELECT e FROM MigrationDTOFaecherNichtMoeglicheKombination e WHERE e.Kursart2 IN :value")
@NamedQuery(name = "MigrationDTOFaecherNichtMoeglicheKombination.pk", query = "SELECT e FROM MigrationDTOFaecherNichtMoeglicheKombination e WHERE e.PK = :value")
@NamedQuery(name = "MigrationDTOFaecherNichtMoeglicheKombination.pk.multiple", query = "SELECT e FROM MigrationDTOFaecherNichtMoeglicheKombination e WHERE e.PK IN :value")
@NamedQuery(name = "MigrationDTOFaecherNichtMoeglicheKombination.sortierung", query = "SELECT e FROM MigrationDTOFaecherNichtMoeglicheKombination e WHERE e.Sortierung = :value")
@NamedQuery(name = "MigrationDTOFaecherNichtMoeglicheKombination.sortierung.multiple", query = "SELECT e FROM MigrationDTOFaecherNichtMoeglicheKombination e WHERE e.Sortierung IN :value")
@NamedQuery(name = "MigrationDTOFaecherNichtMoeglicheKombination.phase", query = "SELECT e FROM MigrationDTOFaecherNichtMoeglicheKombination e WHERE e.Phase = :value")
@NamedQuery(name = "MigrationDTOFaecherNichtMoeglicheKombination.phase.multiple", query = "SELECT e FROM MigrationDTOFaecherNichtMoeglicheKombination e WHERE e.Phase IN :value")
@NamedQuery(name = "MigrationDTOFaecherNichtMoeglicheKombination.typ", query = "SELECT e FROM MigrationDTOFaecherNichtMoeglicheKombination e WHERE e.Typ = :value")
@NamedQuery(name = "MigrationDTOFaecherNichtMoeglicheKombination.typ.multiple", query = "SELECT e FROM MigrationDTOFaecherNichtMoeglicheKombination e WHERE e.Typ IN :value")
@NamedQuery(name = "MigrationDTOFaecherNichtMoeglicheKombination.primaryKeyQuery", query = "SELECT e FROM MigrationDTOFaecherNichtMoeglicheKombination e WHERE e.PK = ?1")
@NamedQuery(name = "MigrationDTOFaecherNichtMoeglicheKombination.primaryKeyQuery.multiple", query = "SELECT e FROM MigrationDTOFaecherNichtMoeglicheKombination e WHERE e.PK IN ?1")
@NamedQuery(name = "MigrationDTOFaecherNichtMoeglicheKombination.all.migration", query = "SELECT e FROM MigrationDTOFaecherNichtMoeglicheKombination e WHERE e.PK IS NOT NULL")
@JsonPropertyOrder({"Fach1_ID", "Fach2_ID", "SchulnrEigner", "Kursart1", "Kursart2", "PK", "Sortierung", "Phase", "Typ"})
public final class MigrationDTOFaecherNichtMoeglicheKombination {

	/** FACH1ID für eine nicht mögliche Kombination */
	@Column(name = "Fach1_ID")
	@JsonProperty
	public Long Fach1_ID;

	/** FACH2ID für eine nicht mögliche Kombination */
	@Column(name = "Fach2_ID")
	@JsonProperty
	public Long Fach2_ID;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/** Kursart Fach1 */
	@Column(name = "Kursart1")
	@JsonProperty
	public String Kursart1;

	/** Kursart Fach2 */
	@Column(name = "Kursart2")
	@JsonProperty
	public String Kursart2;

	/** Primärschlüssel aus FachIDs und Minuszeichen */
	@Id
	@Column(name = "PK")
	@JsonProperty
	public String PK;

	/** Sortierung der nicht möglichen Kombination */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Über welche Jahrgangsstufen geht die Kombination nicht */
	@Column(name = "Phase")
	@JsonProperty
	public String Phase;

	/** Nicht mögliche Fächerkombination (-) oder Fächerprofil (+) */
	@Column(name = "Typ")
	@JsonProperty
	public String Typ;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOFaecherNichtMoeglicheKombination ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOFaecherNichtMoeglicheKombination() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOFaecherNichtMoeglicheKombination ohne eine Initialisierung der Attribute.
	 * @param Fach1_ID   der Wert für das Attribut Fach1_ID
	 * @param Fach2_ID   der Wert für das Attribut Fach2_ID
	 * @param SchulnrEigner   der Wert für das Attribut SchulnrEigner
	 * @param PK   der Wert für das Attribut PK
	 */
	public MigrationDTOFaecherNichtMoeglicheKombination(final Long Fach1_ID, final Long Fach2_ID, final Integer SchulnrEigner, final String PK) {
		if (Fach1_ID == null) {
			throw new NullPointerException("Fach1_ID must not be null");
		}
		this.Fach1_ID = Fach1_ID;
		if (Fach2_ID == null) {
			throw new NullPointerException("Fach2_ID must not be null");
		}
		this.Fach2_ID = Fach2_ID;
		if (SchulnrEigner == null) {
			throw new NullPointerException("SchulnrEigner must not be null");
		}
		this.SchulnrEigner = SchulnrEigner;
		if (PK == null) {
			throw new NullPointerException("PK must not be null");
		}
		this.PK = PK;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOFaecherNichtMoeglicheKombination other = (MigrationDTOFaecherNichtMoeglicheKombination) obj;
		if (PK == null) {
			if (other.PK != null)
				return false;
		} else if (!PK.equals(other.PK))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((PK == null) ? 0 : PK.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "MigrationDTOFaecherNichtMoeglicheKombination(Fach1_ID=" + this.Fach1_ID + ", Fach2_ID=" + this.Fach2_ID + ", SchulnrEigner=" + this.SchulnrEigner + ", Kursart1=" + this.Kursart1 + ", Kursart2=" + this.Kursart2 + ", PK=" + this.PK + ", Sortierung=" + this.Sortierung + ", Phase=" + this.Phase + ", Typ=" + this.Typ + ")";
	}

}
