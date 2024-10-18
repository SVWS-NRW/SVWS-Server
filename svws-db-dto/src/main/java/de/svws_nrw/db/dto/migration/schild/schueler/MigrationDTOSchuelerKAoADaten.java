package de.svws_nrw.db.dto.migration.schild.schueler;

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
@JsonPropertyOrder({"id", "Schueler_ID", "idLernabschnitt", "jahrgang", "idKategorie", "idMerkmal", "idZusatzmerkmal", "idAnschlussoption", "idBerufsfeld", "idEbene4", "bemerkung", "SchulnrEigner", "Jahr", "Abschnitt"})
public final class MigrationDTOSchuelerKAoADaten {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM MigrationDTOSchuelerKAoADaten e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM MigrationDTOSchuelerKAoADaten e WHERE e.id = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM MigrationDTOSchuelerKAoADaten e WHERE e.id IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM MigrationDTOSchuelerKAoADaten e WHERE e.id IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes id */
	public static final String QUERY_BY_ID = "SELECT e FROM MigrationDTOSchuelerKAoADaten e WHERE e.id = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes id */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM MigrationDTOSchuelerKAoADaten e WHERE e.id IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schueler_ID */
	public static final String QUERY_BY_SCHUELER_ID = "SELECT e FROM MigrationDTOSchuelerKAoADaten e WHERE e.Schueler_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schueler_ID */
	public static final String QUERY_LIST_BY_SCHUELER_ID = "SELECT e FROM MigrationDTOSchuelerKAoADaten e WHERE e.Schueler_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes idLernabschnitt */
	public static final String QUERY_BY_IDLERNABSCHNITT = "SELECT e FROM MigrationDTOSchuelerKAoADaten e WHERE e.idLernabschnitt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes idLernabschnitt */
	public static final String QUERY_LIST_BY_IDLERNABSCHNITT = "SELECT e FROM MigrationDTOSchuelerKAoADaten e WHERE e.idLernabschnitt IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes jahrgang */
	public static final String QUERY_BY_JAHRGANG = "SELECT e FROM MigrationDTOSchuelerKAoADaten e WHERE e.jahrgang = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes jahrgang */
	public static final String QUERY_LIST_BY_JAHRGANG = "SELECT e FROM MigrationDTOSchuelerKAoADaten e WHERE e.jahrgang IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes idKategorie */
	public static final String QUERY_BY_IDKATEGORIE = "SELECT e FROM MigrationDTOSchuelerKAoADaten e WHERE e.idKategorie = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes idKategorie */
	public static final String QUERY_LIST_BY_IDKATEGORIE = "SELECT e FROM MigrationDTOSchuelerKAoADaten e WHERE e.idKategorie IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes idMerkmal */
	public static final String QUERY_BY_IDMERKMAL = "SELECT e FROM MigrationDTOSchuelerKAoADaten e WHERE e.idMerkmal = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes idMerkmal */
	public static final String QUERY_LIST_BY_IDMERKMAL = "SELECT e FROM MigrationDTOSchuelerKAoADaten e WHERE e.idMerkmal IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes idZusatzmerkmal */
	public static final String QUERY_BY_IDZUSATZMERKMAL = "SELECT e FROM MigrationDTOSchuelerKAoADaten e WHERE e.idZusatzmerkmal = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes idZusatzmerkmal */
	public static final String QUERY_LIST_BY_IDZUSATZMERKMAL = "SELECT e FROM MigrationDTOSchuelerKAoADaten e WHERE e.idZusatzmerkmal IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes idAnschlussoption */
	public static final String QUERY_BY_IDANSCHLUSSOPTION = "SELECT e FROM MigrationDTOSchuelerKAoADaten e WHERE e.idAnschlussoption = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes idAnschlussoption */
	public static final String QUERY_LIST_BY_IDANSCHLUSSOPTION = "SELECT e FROM MigrationDTOSchuelerKAoADaten e WHERE e.idAnschlussoption IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes idBerufsfeld */
	public static final String QUERY_BY_IDBERUFSFELD = "SELECT e FROM MigrationDTOSchuelerKAoADaten e WHERE e.idBerufsfeld = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes idBerufsfeld */
	public static final String QUERY_LIST_BY_IDBERUFSFELD = "SELECT e FROM MigrationDTOSchuelerKAoADaten e WHERE e.idBerufsfeld IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes idEbene4 */
	public static final String QUERY_BY_IDEBENE4 = "SELECT e FROM MigrationDTOSchuelerKAoADaten e WHERE e.idEbene4 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes idEbene4 */
	public static final String QUERY_LIST_BY_IDEBENE4 = "SELECT e FROM MigrationDTOSchuelerKAoADaten e WHERE e.idEbene4 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes bemerkung */
	public static final String QUERY_BY_BEMERKUNG = "SELECT e FROM MigrationDTOSchuelerKAoADaten e WHERE e.bemerkung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes bemerkung */
	public static final String QUERY_LIST_BY_BEMERKUNG = "SELECT e FROM MigrationDTOSchuelerKAoADaten e WHERE e.bemerkung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchulnrEigner */
	public static final String QUERY_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOSchuelerKAoADaten e WHERE e.SchulnrEigner = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchulnrEigner */
	public static final String QUERY_LIST_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOSchuelerKAoADaten e WHERE e.SchulnrEigner IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Jahr */
	public static final String QUERY_BY_JAHR = "SELECT e FROM MigrationDTOSchuelerKAoADaten e WHERE e.Jahr = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Jahr */
	public static final String QUERY_LIST_BY_JAHR = "SELECT e FROM MigrationDTOSchuelerKAoADaten e WHERE e.Jahr IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Abschnitt */
	public static final String QUERY_BY_ABSCHNITT = "SELECT e FROM MigrationDTOSchuelerKAoADaten e WHERE e.Abschnitt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Abschnitt */
	public static final String QUERY_LIST_BY_ABSCHNITT = "SELECT e FROM MigrationDTOSchuelerKAoADaten e WHERE e.Abschnitt IN ?1";

	/** ID des KAOA-Eintrags beim Schüler */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long id;

	/** DEPRECATED: Schüler-ID des KAOA-Eintrags beim Schüler, in Abschnitt_ID enthalten */
	@Column(name = "Schueler_ID")
	@JsonProperty
	public Long Schueler_ID;

	/** ID der zugehörigen Schülerlernabschnittsdaten */
	@Column(name = "Abschnitt_ID")
	@JsonProperty
	public Long idLernabschnitt;

	/** Jahrgang des KAOA-Eintrags beim Schüler */
	@Column(name = "Jahrgang")
	@JsonProperty
	public String jahrgang;

	/** ID der Kategorie des KAOA-Eintrags beim Schüler FK */
	@Column(name = "KategorieID")
	@JsonProperty
	public Long idKategorie;

	/** ID des Merkmal des KAOA-Eintrags beim Schüler FK */
	@Column(name = "MerkmalID")
	@JsonProperty
	public Long idMerkmal;

	/** ID des Zusatzmerkmal des KAOA-Eintrags beim Schüler FK */
	@Column(name = "ZusatzmerkmalID")
	@JsonProperty
	public Long idZusatzmerkmal;

	/** ID der Anschlussoption des KAOA-Eintrags beim Schüler FK */
	@Column(name = "AnschlussoptionID")
	@JsonProperty
	public Long idAnschlussoption;

	/** ID des Berufsfeld des KAOA-Eintrags beim Schüler FK */
	@Column(name = "BerufsfeldID")
	@JsonProperty
	public Long idBerufsfeld;

	/** ID der Ebene4 des KAOA-Eintrags beim Schüler FK */
	@Column(name = "SBO_Ebene4ID")
	@JsonProperty
	public Long idEbene4;

	/** Bemerkung des KAOA-Eintrags beim Schüler */
	@Column(name = "Bemerkung")
	@JsonProperty
	public String bemerkung;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/** Schuljahr des KAOA-Eintrags beim Schüler */
	@Column(name = "Jahr")
	@JsonProperty
	public Integer Jahr;

	/** Abschnitt des KAOA-Eintrags beim Schüler */
	@Column(name = "Abschnitt")
	@JsonProperty
	public Integer Abschnitt;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuelerKAoADaten ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOSchuelerKAoADaten() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuelerKAoADaten ohne eine Initialisierung der Attribute.
	 * @param id   der Wert für das Attribut id
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 * @param idLernabschnitt   der Wert für das Attribut idLernabschnitt
	 * @param idKategorie   der Wert für das Attribut idKategorie
	 * @param SchulnrEigner   der Wert für das Attribut SchulnrEigner
	 * @param Jahr   der Wert für das Attribut Jahr
	 * @param Abschnitt   der Wert für das Attribut Abschnitt
	 */
	public MigrationDTOSchuelerKAoADaten(final Long id, final Long Schueler_ID, final Long idLernabschnitt, final Long idKategorie, final Integer SchulnrEigner, final Integer Jahr, final Integer Abschnitt) {
		if (id == null) {
			throw new NullPointerException("id must not be null");
		}
		this.id = id;
		if (Schueler_ID == null) {
			throw new NullPointerException("Schueler_ID must not be null");
		}
		this.Schueler_ID = Schueler_ID;
		if (idLernabschnitt == null) {
			throw new NullPointerException("idLernabschnitt must not be null");
		}
		this.idLernabschnitt = idLernabschnitt;
		if (idKategorie == null) {
			throw new NullPointerException("idKategorie must not be null");
		}
		this.idKategorie = idKategorie;
		if (SchulnrEigner == null) {
			throw new NullPointerException("SchulnrEigner must not be null");
		}
		this.SchulnrEigner = SchulnrEigner;
		if (Jahr == null) {
			throw new NullPointerException("Jahr must not be null");
		}
		this.Jahr = Jahr;
		if (Abschnitt == null) {
			throw new NullPointerException("Abschnitt must not be null");
		}
		this.Abschnitt = Abschnitt;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOSchuelerKAoADaten other = (MigrationDTOSchuelerKAoADaten) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
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
		return "MigrationDTOSchuelerKAoADaten(id=" + this.id + ", Schueler_ID=" + this.Schueler_ID + ", idLernabschnitt=" + this.idLernabschnitt + ", jahrgang=" + this.jahrgang + ", idKategorie=" + this.idKategorie + ", idMerkmal=" + this.idMerkmal + ", idZusatzmerkmal=" + this.idZusatzmerkmal + ", idAnschlussoption=" + this.idAnschlussoption + ", idBerufsfeld=" + this.idBerufsfeld + ", idEbene4=" + this.idEbene4 + ", bemerkung=" + this.bemerkung + ", SchulnrEigner=" + this.SchulnrEigner + ", Jahr=" + this.Jahr + ", Abschnitt=" + this.Abschnitt + ")";
	}

}
