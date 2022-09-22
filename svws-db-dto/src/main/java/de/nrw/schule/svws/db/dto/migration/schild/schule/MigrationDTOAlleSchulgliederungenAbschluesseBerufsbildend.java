package de.nrw.schule.svws.db.dto.migration.schild.schule;

import de.nrw.schule.svws.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Schulgliederungen_Abschluesse_Berufsbildend.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(MigrationDTOAlleSchulgliederungenAbschluesseBerufsbildendPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Schulgliederungen_Abschluesse_Berufsbildend")
@NamedQuery(name="MigrationDTOAlleSchulgliederungenAbschluesseBerufsbildend.all", query="SELECT e FROM MigrationDTOAlleSchulgliederungenAbschluesseBerufsbildend e")
@NamedQuery(name="MigrationDTOAlleSchulgliederungenAbschluesseBerufsbildend.schulgliederung_id", query="SELECT e FROM MigrationDTOAlleSchulgliederungenAbschluesseBerufsbildend e WHERE e.Schulgliederung_ID = :value")
@NamedQuery(name="MigrationDTOAlleSchulgliederungenAbschluesseBerufsbildend.schulgliederung_id.multiple", query="SELECT e FROM MigrationDTOAlleSchulgliederungenAbschluesseBerufsbildend e WHERE e.Schulgliederung_ID IN :value")
@NamedQuery(name="MigrationDTOAlleSchulgliederungenAbschluesseBerufsbildend.abschluss_kuerzel", query="SELECT e FROM MigrationDTOAlleSchulgliederungenAbschluesseBerufsbildend e WHERE e.Abschluss_Kuerzel = :value")
@NamedQuery(name="MigrationDTOAlleSchulgliederungenAbschluesseBerufsbildend.abschluss_kuerzel.multiple", query="SELECT e FROM MigrationDTOAlleSchulgliederungenAbschluesseBerufsbildend e WHERE e.Abschluss_Kuerzel IN :value")
@NamedQuery(name="MigrationDTOAlleSchulgliederungenAbschluesseBerufsbildend.primaryKeyQuery", query="SELECT e FROM MigrationDTOAlleSchulgliederungenAbschluesseBerufsbildend e WHERE e.Schulgliederung_ID = ?1 AND e.Abschluss_Kuerzel = ?2")
@NamedQuery(name="MigrationDTOAlleSchulgliederungenAbschluesseBerufsbildend.all.migration", query="SELECT e FROM MigrationDTOAlleSchulgliederungenAbschluesseBerufsbildend e WHERE e.Schulgliederung_ID IS NOT NULL AND e.Abschluss_Kuerzel IS NOT NULL")
@JsonPropertyOrder({"Schulgliederung_ID","Abschluss_Kuerzel"})
public class MigrationDTOAlleSchulgliederungenAbschluesseBerufsbildend {

	/** die ID der Schulgliederung */
	@Id
	@Column(name = "Schulgliederung_ID")
	@JsonProperty
	public Long Schulgliederung_ID;

	/** die ID der Art des berufsbildenden Abschlusses */
	@Id
	@Column(name = "Abschluss_Kuerzel")
	@JsonProperty
	public String Abschluss_Kuerzel;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOAlleSchulgliederungenAbschluesseBerufsbildend ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOAlleSchulgliederungenAbschluesseBerufsbildend() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOAlleSchulgliederungenAbschluesseBerufsbildend ohne eine Initialisierung der Attribute.
	 * @param Schulgliederung_ID   der Wert für das Attribut Schulgliederung_ID
	 * @param Abschluss_Kuerzel   der Wert für das Attribut Abschluss_Kuerzel
	 */
	public MigrationDTOAlleSchulgliederungenAbschluesseBerufsbildend(final Long Schulgliederung_ID, final String Abschluss_Kuerzel) {
		if (Schulgliederung_ID == null) { 
			throw new NullPointerException("Schulgliederung_ID must not be null");
		}
		this.Schulgliederung_ID = Schulgliederung_ID;
		if (Abschluss_Kuerzel == null) { 
			throw new NullPointerException("Abschluss_Kuerzel must not be null");
		}
		this.Abschluss_Kuerzel = Abschluss_Kuerzel;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOAlleSchulgliederungenAbschluesseBerufsbildend other = (MigrationDTOAlleSchulgliederungenAbschluesseBerufsbildend) obj;
		if (Schulgliederung_ID == null) {
			if (other.Schulgliederung_ID != null)
				return false;
		} else if (!Schulgliederung_ID.equals(other.Schulgliederung_ID))
			return false;

		if (Abschluss_Kuerzel == null) {
			if (other.Abschluss_Kuerzel != null)
				return false;
		} else if (!Abschluss_Kuerzel.equals(other.Abschluss_Kuerzel))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Schulgliederung_ID == null) ? 0 : Schulgliederung_ID.hashCode());

		result = prime * result + ((Abschluss_Kuerzel == null) ? 0 : Abschluss_Kuerzel.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "MigrationDTOAlleSchulgliederungenAbschluesseBerufsbildend(Schulgliederung_ID=" + this.Schulgliederung_ID + ", Abschluss_Kuerzel=" + this.Abschluss_Kuerzel + ")";
	}

}