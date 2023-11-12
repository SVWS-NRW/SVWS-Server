package de.svws_nrw.db.dto.migration.schild.schueler;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerSprachenfolge.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerSprachenfolge")
@NamedQuery(name = "MigrationDTOSchuelerSprachenfolge.all", query = "SELECT e FROM MigrationDTOSchuelerSprachenfolge e")
@NamedQuery(name = "MigrationDTOSchuelerSprachenfolge.id", query = "SELECT e FROM MigrationDTOSchuelerSprachenfolge e WHERE e.ID = :value")
@NamedQuery(name = "MigrationDTOSchuelerSprachenfolge.id.multiple", query = "SELECT e FROM MigrationDTOSchuelerSprachenfolge e WHERE e.ID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerSprachenfolge.schueler_id", query = "SELECT e FROM MigrationDTOSchuelerSprachenfolge e WHERE e.Schueler_ID = :value")
@NamedQuery(name = "MigrationDTOSchuelerSprachenfolge.schueler_id.multiple", query = "SELECT e FROM MigrationDTOSchuelerSprachenfolge e WHERE e.Schueler_ID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerSprachenfolge.abschnittvon", query = "SELECT e FROM MigrationDTOSchuelerSprachenfolge e WHERE e.AbschnittVon = :value")
@NamedQuery(name = "MigrationDTOSchuelerSprachenfolge.abschnittvon.multiple", query = "SELECT e FROM MigrationDTOSchuelerSprachenfolge e WHERE e.AbschnittVon IN :value")
@NamedQuery(name = "MigrationDTOSchuelerSprachenfolge.abschnittbis", query = "SELECT e FROM MigrationDTOSchuelerSprachenfolge e WHERE e.AbschnittBis = :value")
@NamedQuery(name = "MigrationDTOSchuelerSprachenfolge.abschnittbis.multiple", query = "SELECT e FROM MigrationDTOSchuelerSprachenfolge e WHERE e.AbschnittBis IN :value")
@NamedQuery(name = "MigrationDTOSchuelerSprachenfolge.referenzniveau", query = "SELECT e FROM MigrationDTOSchuelerSprachenfolge e WHERE e.Referenzniveau = :value")
@NamedQuery(name = "MigrationDTOSchuelerSprachenfolge.referenzniveau.multiple", query = "SELECT e FROM MigrationDTOSchuelerSprachenfolge e WHERE e.Referenzniveau IN :value")
@NamedQuery(name = "MigrationDTOSchuelerSprachenfolge.schulnreigner", query = "SELECT e FROM MigrationDTOSchuelerSprachenfolge e WHERE e.SchulnrEigner = :value")
@NamedQuery(name = "MigrationDTOSchuelerSprachenfolge.schulnreigner.multiple", query = "SELECT e FROM MigrationDTOSchuelerSprachenfolge e WHERE e.SchulnrEigner IN :value")
@NamedQuery(name = "MigrationDTOSchuelerSprachenfolge.fach_id", query = "SELECT e FROM MigrationDTOSchuelerSprachenfolge e WHERE e.Fach_ID = :value")
@NamedQuery(name = "MigrationDTOSchuelerSprachenfolge.fach_id.multiple", query = "SELECT e FROM MigrationDTOSchuelerSprachenfolge e WHERE e.Fach_ID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerSprachenfolge.reihenfolge", query = "SELECT e FROM MigrationDTOSchuelerSprachenfolge e WHERE e.Reihenfolge = :value")
@NamedQuery(name = "MigrationDTOSchuelerSprachenfolge.reihenfolge.multiple", query = "SELECT e FROM MigrationDTOSchuelerSprachenfolge e WHERE e.Reihenfolge IN :value")
@NamedQuery(name = "MigrationDTOSchuelerSprachenfolge.jahrgangvon", query = "SELECT e FROM MigrationDTOSchuelerSprachenfolge e WHERE e.JahrgangVon = :value")
@NamedQuery(name = "MigrationDTOSchuelerSprachenfolge.jahrgangvon.multiple", query = "SELECT e FROM MigrationDTOSchuelerSprachenfolge e WHERE e.JahrgangVon IN :value")
@NamedQuery(name = "MigrationDTOSchuelerSprachenfolge.jahrgangbis", query = "SELECT e FROM MigrationDTOSchuelerSprachenfolge e WHERE e.JahrgangBis = :value")
@NamedQuery(name = "MigrationDTOSchuelerSprachenfolge.jahrgangbis.multiple", query = "SELECT e FROM MigrationDTOSchuelerSprachenfolge e WHERE e.JahrgangBis IN :value")
@NamedQuery(name = "MigrationDTOSchuelerSprachenfolge.primaryKeyQuery", query = "SELECT e FROM MigrationDTOSchuelerSprachenfolge e WHERE e.ID = ?1")
@NamedQuery(name = "MigrationDTOSchuelerSprachenfolge.primaryKeyQuery.multiple", query = "SELECT e FROM MigrationDTOSchuelerSprachenfolge e WHERE e.ID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerSprachenfolge.all.migration", query = "SELECT e FROM MigrationDTOSchuelerSprachenfolge e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Schueler_ID", "AbschnittVon", "AbschnittBis", "Referenzniveau", "SchulnrEigner", "Fach_ID", "Reihenfolge", "JahrgangVon", "JahrgangBis"})
public final class MigrationDTOSchuelerSprachenfolge {

	/** ID des Sprachenfolgeeintrags */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** SchülerID des Sprachenfolgeeintrags */
	@Column(name = "Schueler_ID")
	@JsonProperty
	public Long Schueler_ID;

	/** Abschnitt Beginn des Sprachenfolgeeintrags */
	@Column(name = "AbschnittVon")
	@JsonProperty
	public Integer AbschnittVon;

	/** Abschnitt Ende des Sprachenfolgeeintrags */
	@Column(name = "AbschnittBis")
	@JsonProperty
	public Integer AbschnittBis;

	/** Referenzniveau GeR des Sprachenfolgeeintrags */
	@Column(name = "Referenzniveau")
	@JsonProperty
	public String Referenzniveau;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/** DEPRECATED: FachID des Sprachenfolgeeintrags, Ersetzt durch das Sprachenkürzel */
	@Column(name = "Fach_ID")
	@JsonProperty
	public Long Fach_ID;

	/** DEPRECATED: Reihenfolge Nummer des Sprachenfolgeeintrags bzw. N oder P, ersetzt durch ReihenfolgeNr und Sprachprüfung-Tabelle */
	@Column(name = "Reihenfolge")
	@JsonProperty
	public String Reihenfolge;

	/** DEPRECATED: Jahrgang Beginn des Sprachenfolgeeintrags, durch ASDJahrgang ersetzt */
	@Column(name = "JahrgangVon")
	@JsonProperty
	public Integer JahrgangVon;

	/** DEPRECATED: Jahrgang Ende des Sprachenfolgeeintrags, durch ASDJahrgang ersetzt */
	@Column(name = "JahrgangBis")
	@JsonProperty
	public Integer JahrgangBis;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuelerSprachenfolge ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOSchuelerSprachenfolge() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuelerSprachenfolge ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 */
	public MigrationDTOSchuelerSprachenfolge(final Long ID, final Long Schueler_ID) {
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
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOSchuelerSprachenfolge other = (MigrationDTOSchuelerSprachenfolge) obj;
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
		return "MigrationDTOSchuelerSprachenfolge(ID=" + this.ID + ", Schueler_ID=" + this.Schueler_ID + ", AbschnittVon=" + this.AbschnittVon + ", AbschnittBis=" + this.AbschnittBis + ", Referenzniveau=" + this.Referenzniveau + ", SchulnrEigner=" + this.SchulnrEigner + ", Fach_ID=" + this.Fach_ID + ", Reihenfolge=" + this.Reihenfolge + ", JahrgangVon=" + this.JahrgangVon + ", JahrgangBis=" + this.JahrgangBis + ")";
	}

}
