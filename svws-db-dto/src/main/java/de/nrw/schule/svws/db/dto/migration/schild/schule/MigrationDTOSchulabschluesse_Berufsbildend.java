package de.nrw.schule.svws.db.dto.migration.schild.schule;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Schulabschluesse_Berufsbildend.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Schulabschluesse_Berufsbildend")
@NamedQuery(name="MigrationDTOSchulabschluesse_Berufsbildend.all", query="SELECT e FROM MigrationDTOSchulabschluesse_Berufsbildend e")
@NamedQuery(name="MigrationDTOSchulabschluesse_Berufsbildend.id", query="SELECT e FROM MigrationDTOSchulabschluesse_Berufsbildend e WHERE e.ID = :value")
@NamedQuery(name="MigrationDTOSchulabschluesse_Berufsbildend.id.multiple", query="SELECT e FROM MigrationDTOSchulabschluesse_Berufsbildend e WHERE e.ID IN :value")
@NamedQuery(name="MigrationDTOSchulabschluesse_Berufsbildend.kuerzel", query="SELECT e FROM MigrationDTOSchulabschluesse_Berufsbildend e WHERE e.Kuerzel = :value")
@NamedQuery(name="MigrationDTOSchulabschluesse_Berufsbildend.kuerzel.multiple", query="SELECT e FROM MigrationDTOSchulabschluesse_Berufsbildend e WHERE e.Kuerzel IN :value")
@NamedQuery(name="MigrationDTOSchulabschluesse_Berufsbildend.bezeichnung", query="SELECT e FROM MigrationDTOSchulabschluesse_Berufsbildend e WHERE e.Bezeichnung = :value")
@NamedQuery(name="MigrationDTOSchulabschluesse_Berufsbildend.bezeichnung.multiple", query="SELECT e FROM MigrationDTOSchulabschluesse_Berufsbildend e WHERE e.Bezeichnung IN :value")
@NamedQuery(name="MigrationDTOSchulabschluesse_Berufsbildend.kuerzel_statistik", query="SELECT e FROM MigrationDTOSchulabschluesse_Berufsbildend e WHERE e.Kuerzel_Statistik = :value")
@NamedQuery(name="MigrationDTOSchulabschluesse_Berufsbildend.kuerzel_statistik.multiple", query="SELECT e FROM MigrationDTOSchulabschluesse_Berufsbildend e WHERE e.Kuerzel_Statistik IN :value")
@NamedQuery(name="MigrationDTOSchulabschluesse_Berufsbildend.gueltigvon", query="SELECT e FROM MigrationDTOSchulabschluesse_Berufsbildend e WHERE e.gueltigVon = :value")
@NamedQuery(name="MigrationDTOSchulabschluesse_Berufsbildend.gueltigvon.multiple", query="SELECT e FROM MigrationDTOSchulabschluesse_Berufsbildend e WHERE e.gueltigVon IN :value")
@NamedQuery(name="MigrationDTOSchulabschluesse_Berufsbildend.gueltigbis", query="SELECT e FROM MigrationDTOSchulabschluesse_Berufsbildend e WHERE e.gueltigBis = :value")
@NamedQuery(name="MigrationDTOSchulabschluesse_Berufsbildend.gueltigbis.multiple", query="SELECT e FROM MigrationDTOSchulabschluesse_Berufsbildend e WHERE e.gueltigBis IN :value")
@NamedQuery(name="MigrationDTOSchulabschluesse_Berufsbildend.primaryKeyQuery", query="SELECT e FROM MigrationDTOSchulabschluesse_Berufsbildend e WHERE e.ID = ?1")
@NamedQuery(name="MigrationDTOSchulabschluesse_Berufsbildend.all.migration", query="SELECT e FROM MigrationDTOSchulabschluesse_Berufsbildend e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Kuerzel","Bezeichnung","Kuerzel_Statistik","gueltigVon","gueltigBis"})
public class MigrationDTOSchulabschluesse_Berufsbildend {

	/** ID der Art des berufsbildendenbildenden Schulabschlusses */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Kürzel der Art des berufsbildendenbildenden Schulabschlusses */
	@Column(name = "Kuerzel")
	@JsonProperty
	public String Kuerzel;

	/** Bezeichnung der Art des berufsbildendenbildenden Schulabschlusses */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Kürzel der Art des berufsbildendenbildenden Schulabschlusses für die amtliche Schulstatistik */
	@Column(name = "Kuerzel_Statistik")
	@JsonProperty
	public String Kuerzel_Statistik;

	/** Gibt an, ab welchem Schuljahr die Abschlussart gültig ist */
	@Column(name = "gueltigVon")
	@JsonProperty
	public Integer gueltigVon;

	/** Gibt an, bis zu welchem Schuljahr die Abschlussart gültig ist */
	@Column(name = "gueltigBis")
	@JsonProperty
	public Integer gueltigBis;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchulabschluesse_Berufsbildend ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOSchulabschluesse_Berufsbildend() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchulabschluesse_Berufsbildend ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Kuerzel   der Wert für das Attribut Kuerzel
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 * @param Kuerzel_Statistik   der Wert für das Attribut Kuerzel_Statistik
	 */
	public MigrationDTOSchulabschluesse_Berufsbildend(final Long ID, final String Kuerzel, final String Bezeichnung, final String Kuerzel_Statistik) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Kuerzel == null) { 
			throw new NullPointerException("Kuerzel must not be null");
		}
		this.Kuerzel = Kuerzel;
		if (Bezeichnung == null) { 
			throw new NullPointerException("Bezeichnung must not be null");
		}
		this.Bezeichnung = Bezeichnung;
		if (Kuerzel_Statistik == null) { 
			throw new NullPointerException("Kuerzel_Statistik must not be null");
		}
		this.Kuerzel_Statistik = Kuerzel_Statistik;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOSchulabschluesse_Berufsbildend other = (MigrationDTOSchulabschluesse_Berufsbildend) obj;
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
		return "MigrationDTOSchulabschluesse_Berufsbildend(ID=" + this.ID + ", Kuerzel=" + this.Kuerzel + ", Bezeichnung=" + this.Bezeichnung + ", Kuerzel_Statistik=" + this.Kuerzel_Statistik + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ")";
	}

}