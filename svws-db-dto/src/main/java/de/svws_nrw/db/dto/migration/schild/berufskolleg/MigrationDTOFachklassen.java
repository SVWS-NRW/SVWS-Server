package de.svws_nrw.db.dto.migration.schild.berufskolleg;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverter;


import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverterSerializer;
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle EigeneSchule_Fachklassen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "EigeneSchule_Fachklassen")
@JsonPropertyOrder({"id", "BKIndex", "FKS", "AP", "bezeichnung", "sortierung", "istSichtbar", "Aenderbar", "Kennung", "FKS_AP_SIM", "BKIndexTyp", "bezeichnungWeiblich", "Status", "SchulnrEigner", "Lernfelder", "idDqrNiveau", "berufsebene1", "berufsebene2", "berufsebene3"})
public final class MigrationDTOFachklassen {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM MigrationDTOFachklassen e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM MigrationDTOFachklassen e WHERE e.id = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM MigrationDTOFachklassen e WHERE e.id IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM MigrationDTOFachklassen e WHERE e.id IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes id */
	public static final String QUERY_BY_ID = "SELECT e FROM MigrationDTOFachklassen e WHERE e.id = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes id */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM MigrationDTOFachklassen e WHERE e.id IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes BKIndex */
	public static final String QUERY_BY_BKINDEX = "SELECT e FROM MigrationDTOFachklassen e WHERE e.BKIndex = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes BKIndex */
	public static final String QUERY_LIST_BY_BKINDEX = "SELECT e FROM MigrationDTOFachklassen e WHERE e.BKIndex IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes FKS */
	public static final String QUERY_BY_FKS = "SELECT e FROM MigrationDTOFachklassen e WHERE e.FKS = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes FKS */
	public static final String QUERY_LIST_BY_FKS = "SELECT e FROM MigrationDTOFachklassen e WHERE e.FKS IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AP */
	public static final String QUERY_BY_AP = "SELECT e FROM MigrationDTOFachklassen e WHERE e.AP = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AP */
	public static final String QUERY_LIST_BY_AP = "SELECT e FROM MigrationDTOFachklassen e WHERE e.AP IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes bezeichnung */
	public static final String QUERY_BY_BEZEICHNUNG = "SELECT e FROM MigrationDTOFachklassen e WHERE e.bezeichnung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes bezeichnung */
	public static final String QUERY_LIST_BY_BEZEICHNUNG = "SELECT e FROM MigrationDTOFachklassen e WHERE e.bezeichnung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes sortierung */
	public static final String QUERY_BY_SORTIERUNG = "SELECT e FROM MigrationDTOFachklassen e WHERE e.sortierung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes sortierung */
	public static final String QUERY_LIST_BY_SORTIERUNG = "SELECT e FROM MigrationDTOFachklassen e WHERE e.sortierung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes istSichtbar */
	public static final String QUERY_BY_ISTSICHTBAR = "SELECT e FROM MigrationDTOFachklassen e WHERE e.istSichtbar = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes istSichtbar */
	public static final String QUERY_LIST_BY_ISTSICHTBAR = "SELECT e FROM MigrationDTOFachklassen e WHERE e.istSichtbar IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Aenderbar */
	public static final String QUERY_BY_AENDERBAR = "SELECT e FROM MigrationDTOFachklassen e WHERE e.Aenderbar = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Aenderbar */
	public static final String QUERY_LIST_BY_AENDERBAR = "SELECT e FROM MigrationDTOFachklassen e WHERE e.Aenderbar IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Kennung */
	public static final String QUERY_BY_KENNUNG = "SELECT e FROM MigrationDTOFachklassen e WHERE e.Kennung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Kennung */
	public static final String QUERY_LIST_BY_KENNUNG = "SELECT e FROM MigrationDTOFachklassen e WHERE e.Kennung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes FKS_AP_SIM */
	public static final String QUERY_BY_FKS_AP_SIM = "SELECT e FROM MigrationDTOFachklassen e WHERE e.FKS_AP_SIM = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes FKS_AP_SIM */
	public static final String QUERY_LIST_BY_FKS_AP_SIM = "SELECT e FROM MigrationDTOFachklassen e WHERE e.FKS_AP_SIM IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes BKIndexTyp */
	public static final String QUERY_BY_BKINDEXTYP = "SELECT e FROM MigrationDTOFachklassen e WHERE e.BKIndexTyp = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes BKIndexTyp */
	public static final String QUERY_LIST_BY_BKINDEXTYP = "SELECT e FROM MigrationDTOFachklassen e WHERE e.BKIndexTyp IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes bezeichnungWeiblich */
	public static final String QUERY_BY_BEZEICHNUNGWEIBLICH = "SELECT e FROM MigrationDTOFachklassen e WHERE e.bezeichnungWeiblich = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes bezeichnungWeiblich */
	public static final String QUERY_LIST_BY_BEZEICHNUNGWEIBLICH = "SELECT e FROM MigrationDTOFachklassen e WHERE e.bezeichnungWeiblich IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Status */
	public static final String QUERY_BY_STATUS = "SELECT e FROM MigrationDTOFachklassen e WHERE e.Status = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Status */
	public static final String QUERY_LIST_BY_STATUS = "SELECT e FROM MigrationDTOFachklassen e WHERE e.Status IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchulnrEigner */
	public static final String QUERY_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOFachklassen e WHERE e.SchulnrEigner = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchulnrEigner */
	public static final String QUERY_LIST_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOFachklassen e WHERE e.SchulnrEigner IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Lernfelder */
	public static final String QUERY_BY_LERNFELDER = "SELECT e FROM MigrationDTOFachklassen e WHERE e.Lernfelder = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Lernfelder */
	public static final String QUERY_LIST_BY_LERNFELDER = "SELECT e FROM MigrationDTOFachklassen e WHERE e.Lernfelder IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes idDqrNiveau */
	public static final String QUERY_BY_IDDQRNIVEAU = "SELECT e FROM MigrationDTOFachklassen e WHERE e.idDqrNiveau = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes idDqrNiveau */
	public static final String QUERY_LIST_BY_IDDQRNIVEAU = "SELECT e FROM MigrationDTOFachklassen e WHERE e.idDqrNiveau IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes berufsebene1 */
	public static final String QUERY_BY_BERUFSEBENE1 = "SELECT e FROM MigrationDTOFachklassen e WHERE e.berufsebene1 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes berufsebene1 */
	public static final String QUERY_LIST_BY_BERUFSEBENE1 = "SELECT e FROM MigrationDTOFachklassen e WHERE e.berufsebene1 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes berufsebene2 */
	public static final String QUERY_BY_BERUFSEBENE2 = "SELECT e FROM MigrationDTOFachklassen e WHERE e.berufsebene2 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes berufsebene2 */
	public static final String QUERY_LIST_BY_BERUFSEBENE2 = "SELECT e FROM MigrationDTOFachklassen e WHERE e.berufsebene2 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes berufsebene3 */
	public static final String QUERY_BY_BERUFSEBENE3 = "SELECT e FROM MigrationDTOFachklassen e WHERE e.berufsebene3 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes berufsebene3 */
	public static final String QUERY_LIST_BY_BERUFSEBENE3 = "SELECT e FROM MigrationDTOFachklassen e WHERE e.berufsebene3 IN ?1";

	/** ID der Fachklasse im schulinternen Katalog der Fachklassen nur BK und SBK */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long id;

	/** BKIndex aus der Statkue bildet mit FKS und AP eine eindeutige Kombination IT.NRW */
	@Column(name = "BKIndex")
	@JsonProperty
	public Integer BKIndex;

	/** Fachklassenschlüssel bildet mit BKIndex und AP eine eindeutige Kombination IT.NRW */
	@Column(name = "FKS")
	@JsonProperty
	public String FKS;

	/** Laufende Nummer zum FKS bildet mit FKS und BKIndex eine eindeutige Kombination IT.NRW */
	@Column(name = "AP")
	@JsonProperty
	public String AP;

	/** Bezeichnung der Fachklasse Text */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String bezeichnung;

	/** Sortiernummer der Fachklasse */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer sortierung;

	/** steuert die Sichtbarkeit der Fachklasse */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean istSichtbar;

	/** Gibt an ob die Fachklasse änderbar ist */
	@Column(name = "Aenderbar")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Aenderbar;

	/** Kennung der Fachklasse */
	@Column(name = "Kennung")
	@JsonProperty
	public String Kennung;

	/** Kombination aus FKS und AP mit Minuszeichen */
	@Column(name = "FKS_AP_SIM")
	@JsonProperty
	public String FKS_AP_SIM;

	/** Typ des BKIndex IT.NW */
	@Column(name = "BKIndexTyp")
	@JsonProperty
	public String BKIndexTyp;

	/** Weiblicher Beschreibungstext für die Fachklassenbezeichnung */
	@Column(name = "Beschreibung_W")
	@JsonProperty
	public String bezeichnungWeiblich;

	/** Status der Fachklasse kann auslaufend sein */
	@Column(name = "Status")
	@JsonProperty
	public String Status;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/** Text für die Lernfelder die zur Fachklasse auf dem Zeugnis ausgewiesen werden */
	@Column(name = "Lernfelder")
	@JsonProperty
	public String Lernfelder;

	/** DQR-Niveau der Fachklasse Deutscher Qualitätsrahmen */
	@Column(name = "DQR_Niveau")
	@JsonProperty
	public Integer idDqrNiveau;

	/** Berufsebene 1 */
	@Column(name = "Ebene1Klartext")
	@JsonProperty
	public String berufsebene1;

	/** Berufsebene 2 */
	@Column(name = "Ebene2Klartext")
	@JsonProperty
	public String berufsebene2;

	/** Berufsebene 3 */
	@Column(name = "Ebene3Klartext")
	@JsonProperty
	public String berufsebene3;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOFachklassen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOFachklassen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOFachklassen ohne eine Initialisierung der Attribute.
	 * @param id   der Wert für das Attribut id
	 */
	public MigrationDTOFachklassen(final Long id) {
		if (id == null) {
			throw new NullPointerException("id must not be null");
		}
		this.id = id;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		MigrationDTOFachklassen other = (MigrationDTOFachklassen) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "MigrationDTOFachklassen(id=" + this.id + ", BKIndex=" + this.BKIndex + ", FKS=" + this.FKS + ", AP=" + this.AP + ", bezeichnung=" + this.bezeichnung + ", sortierung=" + this.sortierung + ", istSichtbar=" + this.istSichtbar + ", Aenderbar=" + this.Aenderbar + ", Kennung=" + this.Kennung + ", FKS_AP_SIM=" + this.FKS_AP_SIM + ", BKIndexTyp=" + this.BKIndexTyp + ", bezeichnungWeiblich=" + this.bezeichnungWeiblich + ", Status=" + this.Status + ", SchulnrEigner=" + this.SchulnrEigner + ", Lernfelder=" + this.Lernfelder + ", idDqrNiveau=" + this.idDqrNiveau + ", berufsebene1=" + this.berufsebene1 + ", berufsebene2=" + this.berufsebene2 + ", berufsebene3=" + this.berufsebene3 + ")";
	}

}
