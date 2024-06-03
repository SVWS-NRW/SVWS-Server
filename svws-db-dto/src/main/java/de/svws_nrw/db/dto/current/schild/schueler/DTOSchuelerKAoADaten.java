package de.svws_nrw.db.dto.current.schild.schueler;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerKAoADaten.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerKAoADaten")
@JsonPropertyOrder({"ID", "Abschnitt_ID", "Jahrgang", "KategorieID", "MerkmalID", "ZusatzmerkmalID", "AnschlussoptionID", "BerufsfeldID", "SBO_Ebene4ID", "Bemerkung"})
public final class DTOSchuelerKAoADaten {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOSchuelerKAoADaten e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOSchuelerKAoADaten e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOSchuelerKAoADaten e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOSchuelerKAoADaten e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOSchuelerKAoADaten e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOSchuelerKAoADaten e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Abschnitt_ID */
	public static final String QUERY_BY_ABSCHNITT_ID = "SELECT e FROM DTOSchuelerKAoADaten e WHERE e.Abschnitt_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Abschnitt_ID */
	public static final String QUERY_LIST_BY_ABSCHNITT_ID = "SELECT e FROM DTOSchuelerKAoADaten e WHERE e.Abschnitt_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Jahrgang */
	public static final String QUERY_BY_JAHRGANG = "SELECT e FROM DTOSchuelerKAoADaten e WHERE e.Jahrgang = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Jahrgang */
	public static final String QUERY_LIST_BY_JAHRGANG = "SELECT e FROM DTOSchuelerKAoADaten e WHERE e.Jahrgang IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes KategorieID */
	public static final String QUERY_BY_KATEGORIEID = "SELECT e FROM DTOSchuelerKAoADaten e WHERE e.KategorieID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes KategorieID */
	public static final String QUERY_LIST_BY_KATEGORIEID = "SELECT e FROM DTOSchuelerKAoADaten e WHERE e.KategorieID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes MerkmalID */
	public static final String QUERY_BY_MERKMALID = "SELECT e FROM DTOSchuelerKAoADaten e WHERE e.MerkmalID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes MerkmalID */
	public static final String QUERY_LIST_BY_MERKMALID = "SELECT e FROM DTOSchuelerKAoADaten e WHERE e.MerkmalID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ZusatzmerkmalID */
	public static final String QUERY_BY_ZUSATZMERKMALID = "SELECT e FROM DTOSchuelerKAoADaten e WHERE e.ZusatzmerkmalID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ZusatzmerkmalID */
	public static final String QUERY_LIST_BY_ZUSATZMERKMALID = "SELECT e FROM DTOSchuelerKAoADaten e WHERE e.ZusatzmerkmalID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AnschlussoptionID */
	public static final String QUERY_BY_ANSCHLUSSOPTIONID = "SELECT e FROM DTOSchuelerKAoADaten e WHERE e.AnschlussoptionID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AnschlussoptionID */
	public static final String QUERY_LIST_BY_ANSCHLUSSOPTIONID = "SELECT e FROM DTOSchuelerKAoADaten e WHERE e.AnschlussoptionID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes BerufsfeldID */
	public static final String QUERY_BY_BERUFSFELDID = "SELECT e FROM DTOSchuelerKAoADaten e WHERE e.BerufsfeldID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes BerufsfeldID */
	public static final String QUERY_LIST_BY_BERUFSFELDID = "SELECT e FROM DTOSchuelerKAoADaten e WHERE e.BerufsfeldID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SBO_Ebene4ID */
	public static final String QUERY_BY_SBO_EBENE4ID = "SELECT e FROM DTOSchuelerKAoADaten e WHERE e.SBO_Ebene4ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SBO_Ebene4ID */
	public static final String QUERY_LIST_BY_SBO_EBENE4ID = "SELECT e FROM DTOSchuelerKAoADaten e WHERE e.SBO_Ebene4ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Bemerkung */
	public static final String QUERY_BY_BEMERKUNG = "SELECT e FROM DTOSchuelerKAoADaten e WHERE e.Bemerkung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Bemerkung */
	public static final String QUERY_LIST_BY_BEMERKUNG = "SELECT e FROM DTOSchuelerKAoADaten e WHERE e.Bemerkung IN ?1";

	/** ID des KAOA-Eintrags beim Schüler */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** ID der zugehörigen Schülerlernabschnittsdaten */
	@Column(name = "Abschnitt_ID")
	@JsonProperty
	public long Abschnitt_ID;

	/** Jahrgang des KAOA-Eintrags beim Schüler */
	@Column(name = "Jahrgang")
	@JsonProperty
	public String Jahrgang;

	/** ID der Kategorie des KAOA-Eintrags beim Schüler FK */
	@Column(name = "KategorieID")
	@JsonProperty
	public long KategorieID;

	/** ID des Merkmal des KAOA-Eintrags beim Schüler FK */
	@Column(name = "MerkmalID")
	@JsonProperty
	public Long MerkmalID;

	/** ID des Zusatzmerkmal des KAOA-Eintrags beim Schüler FK */
	@Column(name = "ZusatzmerkmalID")
	@JsonProperty
	public Long ZusatzmerkmalID;

	/** ID der Anschlussoption des KAOA-Eintrags beim Schüler FK */
	@Column(name = "AnschlussoptionID")
	@JsonProperty
	public Long AnschlussoptionID;

	/** ID des Berufsfeld des KAOA-Eintrags beim Schüler FK */
	@Column(name = "BerufsfeldID")
	@JsonProperty
	public Long BerufsfeldID;

	/** ID der Ebene4 des KAOA-Eintrags beim Schüler FK */
	@Column(name = "SBO_Ebene4ID")
	@JsonProperty
	public Long SBO_Ebene4ID;

	/** Bemerkung des KAOA-Eintrags beim Schüler */
	@Column(name = "Bemerkung")
	@JsonProperty
	public String Bemerkung;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerKAoADaten ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSchuelerKAoADaten() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerKAoADaten ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Abschnitt_ID   der Wert für das Attribut Abschnitt_ID
	 * @param KategorieID   der Wert für das Attribut KategorieID
	 */
	public DTOSchuelerKAoADaten(final long ID, final long Abschnitt_ID, final long KategorieID) {
		this.ID = ID;
		this.Abschnitt_ID = Abschnitt_ID;
		this.KategorieID = KategorieID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOSchuelerKAoADaten other = (DTOSchuelerKAoADaten) obj;
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
		return "DTOSchuelerKAoADaten(ID=" + this.ID + ", Abschnitt_ID=" + this.Abschnitt_ID + ", Jahrgang=" + this.Jahrgang + ", KategorieID=" + this.KategorieID + ", MerkmalID=" + this.MerkmalID + ", ZusatzmerkmalID=" + this.ZusatzmerkmalID + ", AnschlussoptionID=" + this.AnschlussoptionID + ", BerufsfeldID=" + this.BerufsfeldID + ", SBO_Ebene4ID=" + this.SBO_Ebene4ID + ", Bemerkung=" + this.Bemerkung + ")";
	}

}
