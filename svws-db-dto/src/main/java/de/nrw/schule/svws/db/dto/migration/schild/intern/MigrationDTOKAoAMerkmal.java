package de.nrw.schule.svws.db.dto.migration.schild.intern;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Schildintern_KAoA_Merkmal.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Schildintern_KAoA_Merkmal")
@NamedQuery(name="MigrationDTOKAoAMerkmal.all", query="SELECT e FROM MigrationDTOKAoAMerkmal e")
@NamedQuery(name="MigrationDTOKAoAMerkmal.id", query="SELECT e FROM MigrationDTOKAoAMerkmal e WHERE e.ID = :value")
@NamedQuery(name="MigrationDTOKAoAMerkmal.id.multiple", query="SELECT e FROM MigrationDTOKAoAMerkmal e WHERE e.ID IN :value")
@NamedQuery(name="MigrationDTOKAoAMerkmal.gueltigvon", query="SELECT e FROM MigrationDTOKAoAMerkmal e WHERE e.gueltigVon = :value")
@NamedQuery(name="MigrationDTOKAoAMerkmal.gueltigvon.multiple", query="SELECT e FROM MigrationDTOKAoAMerkmal e WHERE e.gueltigVon IN :value")
@NamedQuery(name="MigrationDTOKAoAMerkmal.gueltigbis", query="SELECT e FROM MigrationDTOKAoAMerkmal e WHERE e.gueltigBis = :value")
@NamedQuery(name="MigrationDTOKAoAMerkmal.gueltigbis.multiple", query="SELECT e FROM MigrationDTOKAoAMerkmal e WHERE e.gueltigBis IN :value")
@NamedQuery(name="MigrationDTOKAoAMerkmal.m_kuerzel", query="SELECT e FROM MigrationDTOKAoAMerkmal e WHERE e.M_Kuerzel = :value")
@NamedQuery(name="MigrationDTOKAoAMerkmal.m_kuerzel.multiple", query="SELECT e FROM MigrationDTOKAoAMerkmal e WHERE e.M_Kuerzel IN :value")
@NamedQuery(name="MigrationDTOKAoAMerkmal.kategorie_id", query="SELECT e FROM MigrationDTOKAoAMerkmal e WHERE e.Kategorie_ID = :value")
@NamedQuery(name="MigrationDTOKAoAMerkmal.kategorie_id.multiple", query="SELECT e FROM MigrationDTOKAoAMerkmal e WHERE e.Kategorie_ID IN :value")
@NamedQuery(name="MigrationDTOKAoAMerkmal.m_beschreibung", query="SELECT e FROM MigrationDTOKAoAMerkmal e WHERE e.M_Beschreibung = :value")
@NamedQuery(name="MigrationDTOKAoAMerkmal.m_beschreibung.multiple", query="SELECT e FROM MigrationDTOKAoAMerkmal e WHERE e.M_Beschreibung IN :value")
@NamedQuery(name="MigrationDTOKAoAMerkmal.m_option", query="SELECT e FROM MigrationDTOKAoAMerkmal e WHERE e.M_Option = :value")
@NamedQuery(name="MigrationDTOKAoAMerkmal.m_option.multiple", query="SELECT e FROM MigrationDTOKAoAMerkmal e WHERE e.M_Option IN :value")
@NamedQuery(name="MigrationDTOKAoAMerkmal.m_kategorie", query="SELECT e FROM MigrationDTOKAoAMerkmal e WHERE e.M_Kategorie = :value")
@NamedQuery(name="MigrationDTOKAoAMerkmal.m_kategorie.multiple", query="SELECT e FROM MigrationDTOKAoAMerkmal e WHERE e.M_Kategorie IN :value")
@NamedQuery(name="MigrationDTOKAoAMerkmal.bk_anlagen", query="SELECT e FROM MigrationDTOKAoAMerkmal e WHERE e.BK_Anlagen = :value")
@NamedQuery(name="MigrationDTOKAoAMerkmal.bk_anlagen.multiple", query="SELECT e FROM MigrationDTOKAoAMerkmal e WHERE e.BK_Anlagen IN :value")
@NamedQuery(name="MigrationDTOKAoAMerkmal.primaryKeyQuery", query="SELECT e FROM MigrationDTOKAoAMerkmal e WHERE e.ID = ?1")
@NamedQuery(name="MigrationDTOKAoAMerkmal.all.migration", query="SELECT e FROM MigrationDTOKAoAMerkmal e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","gueltigVon","gueltigBis","M_Kuerzel","Kategorie_ID","M_Beschreibung","M_Option","M_Kategorie","BK_Anlagen"})
public class MigrationDTOKAoAMerkmal {

	/** Schildintern Tabelle: Eindeutige ID für den Datensatz */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Schildintern Tabelle: Der Datensatz ist gültig ab dem Datum */
	@Column(name = "gueltigVon")
	@JsonProperty
	public Integer gueltigVon;

	/** Schildintern Tabelle: Der Datensatz ist gültig bis zu dem Datum */
	@Column(name = "gueltigBis")
	@JsonProperty
	public Integer gueltigBis;

	/** Schildintern Tabelle: Kürzel des Merkmals */
	@Column(name = "M_Kuerzel")
	@JsonProperty
	public String M_Kuerzel;

	/** Schildintern Tabelle: ID der übergeordneten Kategorie zum Merkmal */
	@Column(name = "Kategorie_ID")
	@JsonProperty
	public Long Kategorie_ID;

	/** Schildintern Tabelle: Beschreibung (Klartext) des Merkmals */
	@Column(name = "M_Beschreibung")
	@JsonProperty
	public String M_Beschreibung;

	/** Schildintern Tabelle: Zusatz Option die zum Merkmal eintragbar ist */
	@Column(name = "M_Option")
	@JsonProperty
	public String M_Option;

	/** Schildintern Tabelle: DEPRECATED: Übergeordnete Kategorie zum Merkmal */
	@Column(name = "M_Kategorie")
	@JsonProperty
	public String M_Kategorie;

	/** Schildintern Tabelle: Feld für die Anzeige in der Schulform BK */
	@Column(name = "BK_Anlagen")
	@JsonProperty
	public String BK_Anlagen;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOKAoAMerkmal ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOKAoAMerkmal() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOKAoAMerkmal ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param M_Kuerzel   der Wert für das Attribut M_Kuerzel
	 * @param Kategorie_ID   der Wert für das Attribut Kategorie_ID
	 */
	public MigrationDTOKAoAMerkmal(final Long ID, final String M_Kuerzel, final Long Kategorie_ID) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (M_Kuerzel == null) { 
			throw new NullPointerException("M_Kuerzel must not be null");
		}
		this.M_Kuerzel = M_Kuerzel;
		if (Kategorie_ID == null) { 
			throw new NullPointerException("Kategorie_ID must not be null");
		}
		this.Kategorie_ID = Kategorie_ID;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOKAoAMerkmal other = (MigrationDTOKAoAMerkmal) obj;
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
		return "MigrationDTOKAoAMerkmal(ID=" + this.ID + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ", M_Kuerzel=" + this.M_Kuerzel + ", Kategorie_ID=" + this.Kategorie_ID + ", M_Beschreibung=" + this.M_Beschreibung + ", M_Option=" + this.M_Option + ", M_Kategorie=" + this.M_Kategorie + ", BK_Anlagen=" + this.BK_Anlagen + ")";
	}

}