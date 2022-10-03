package de.nrw.schule.svws.db.dto.migration.statkue;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Statkue_Bilingual.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(MigrationDTOStatkueBilingualPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Statkue_Bilingual")
@NamedQuery(name="MigrationDTOStatkueBilingual.all", query="SELECT e FROM MigrationDTOStatkueBilingual e")
@NamedQuery(name="MigrationDTOStatkueBilingual.sf", query="SELECT e FROM MigrationDTOStatkueBilingual e WHERE e.SF = :value")
@NamedQuery(name="MigrationDTOStatkueBilingual.sf.multiple", query="SELECT e FROM MigrationDTOStatkueBilingual e WHERE e.SF IN :value")
@NamedQuery(name="MigrationDTOStatkueBilingual.fach", query="SELECT e FROM MigrationDTOStatkueBilingual e WHERE e.Fach = :value")
@NamedQuery(name="MigrationDTOStatkueBilingual.fach.multiple", query="SELECT e FROM MigrationDTOStatkueBilingual e WHERE e.Fach IN :value")
@NamedQuery(name="MigrationDTOStatkueBilingual.beschreibung", query="SELECT e FROM MigrationDTOStatkueBilingual e WHERE e.Beschreibung = :value")
@NamedQuery(name="MigrationDTOStatkueBilingual.beschreibung.multiple", query="SELECT e FROM MigrationDTOStatkueBilingual e WHERE e.Beschreibung IN :value")
@NamedQuery(name="MigrationDTOStatkueBilingual.geaendert", query="SELECT e FROM MigrationDTOStatkueBilingual e WHERE e.geaendert = :value")
@NamedQuery(name="MigrationDTOStatkueBilingual.geaendert.multiple", query="SELECT e FROM MigrationDTOStatkueBilingual e WHERE e.geaendert IN :value")
@NamedQuery(name="MigrationDTOStatkueBilingual.gueltigvon", query="SELECT e FROM MigrationDTOStatkueBilingual e WHERE e.gueltigVon = :value")
@NamedQuery(name="MigrationDTOStatkueBilingual.gueltigvon.multiple", query="SELECT e FROM MigrationDTOStatkueBilingual e WHERE e.gueltigVon IN :value")
@NamedQuery(name="MigrationDTOStatkueBilingual.gueltigbis", query="SELECT e FROM MigrationDTOStatkueBilingual e WHERE e.gueltigBis = :value")
@NamedQuery(name="MigrationDTOStatkueBilingual.gueltigbis.multiple", query="SELECT e FROM MigrationDTOStatkueBilingual e WHERE e.gueltigBis IN :value")
@NamedQuery(name="MigrationDTOStatkueBilingual.primaryKeyQuery", query="SELECT e FROM MigrationDTOStatkueBilingual e WHERE e.SF = ?1 AND e.Fach = ?2")
@NamedQuery(name="MigrationDTOStatkueBilingual.all.migration", query="SELECT e FROM MigrationDTOStatkueBilingual e WHERE e.SF IS NOT NULL AND e.Fach IS NOT NULL")
@JsonPropertyOrder({"SF","Fach","Beschreibung","geaendert","gueltigVon","gueltigBis"})
public class MigrationDTOStatkueBilingual {

	/** Statkue Tabelle IT.NRW: zulässige Schulform Bilinguale Fächer */
	@Id
	@Column(name = "SF")
	@JsonProperty
	public String SF;

	/** Statkue Tabelle IT.NRW: Fachkürzel Bilinguale Fächer */
	@Id
	@Column(name = "Fach")
	@JsonProperty
	public String Fach;

	/** Statkue Tabelle IT.NRW: Beschreiung Bilinguale Fächer */
	@Column(name = "Beschreibung")
	@JsonProperty
	public String Beschreibung;

	/** Statkue Tabelle IT.NRW: Datum der letzten Änderung Bilinguale Fächer */
	@Column(name = "geaendert")
	@JsonProperty
	public String geaendert;

	/** Statkue Tabelle IT.NRW: Gibt die Gültigkeit ab welchem Schuljahr an */
	@Column(name = "gueltigVon")
	@JsonProperty
	public Integer gueltigVon;

	/** Statkue Tabelle IT.NRW: Gibt die Gültigkeit bis zu welchem Schuljahr an */
	@Column(name = "gueltigBis")
	@JsonProperty
	public Integer gueltigBis;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOStatkueBilingual ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOStatkueBilingual() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOStatkueBilingual ohne eine Initialisierung der Attribute.
	 * @param SF   der Wert für das Attribut SF
	 * @param Fach   der Wert für das Attribut Fach
	 */
	public MigrationDTOStatkueBilingual(final String SF, final String Fach) {
		if (SF == null) { 
			throw new NullPointerException("SF must not be null");
		}
		this.SF = SF;
		if (Fach == null) { 
			throw new NullPointerException("Fach must not be null");
		}
		this.Fach = Fach;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOStatkueBilingual other = (MigrationDTOStatkueBilingual) obj;
		if (SF == null) {
			if (other.SF != null)
				return false;
		} else if (!SF.equals(other.SF))
			return false;

		if (Fach == null) {
			if (other.Fach != null)
				return false;
		} else if (!Fach.equals(other.Fach))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((SF == null) ? 0 : SF.hashCode());

		result = prime * result + ((Fach == null) ? 0 : Fach.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "MigrationDTOStatkueBilingual(SF=" + this.SF + ", Fach=" + this.Fach + ", Beschreibung=" + this.Beschreibung + ", geaendert=" + this.geaendert + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ")";
	}

}