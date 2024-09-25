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
@JsonPropertyOrder({"id", "idLernabschnitt", "jahrgang", "idKategorie", "idMerkmal", "idZusatzmerkmal", "idAnschlussoption", "idBerufsfeld", "idEbene4", "bemerkung"})
public final class DTOSchuelerKAoADaten {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOSchuelerKAoADaten e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOSchuelerKAoADaten e WHERE e.id = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOSchuelerKAoADaten e WHERE e.id IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOSchuelerKAoADaten e WHERE e.id IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes id */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOSchuelerKAoADaten e WHERE e.id = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes id */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOSchuelerKAoADaten e WHERE e.id IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes idLernabschnitt */
	public static final String QUERY_BY_IDLERNABSCHNITT = "SELECT e FROM DTOSchuelerKAoADaten e WHERE e.idLernabschnitt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes idLernabschnitt */
	public static final String QUERY_LIST_BY_IDLERNABSCHNITT = "SELECT e FROM DTOSchuelerKAoADaten e WHERE e.idLernabschnitt IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes jahrgang */
	public static final String QUERY_BY_JAHRGANG = "SELECT e FROM DTOSchuelerKAoADaten e WHERE e.jahrgang = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes jahrgang */
	public static final String QUERY_LIST_BY_JAHRGANG = "SELECT e FROM DTOSchuelerKAoADaten e WHERE e.jahrgang IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes idKategorie */
	public static final String QUERY_BY_IDKATEGORIE = "SELECT e FROM DTOSchuelerKAoADaten e WHERE e.idKategorie = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes idKategorie */
	public static final String QUERY_LIST_BY_IDKATEGORIE = "SELECT e FROM DTOSchuelerKAoADaten e WHERE e.idKategorie IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes idMerkmal */
	public static final String QUERY_BY_IDMERKMAL = "SELECT e FROM DTOSchuelerKAoADaten e WHERE e.idMerkmal = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes idMerkmal */
	public static final String QUERY_LIST_BY_IDMERKMAL = "SELECT e FROM DTOSchuelerKAoADaten e WHERE e.idMerkmal IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes idZusatzmerkmal */
	public static final String QUERY_BY_IDZUSATZMERKMAL = "SELECT e FROM DTOSchuelerKAoADaten e WHERE e.idZusatzmerkmal = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes idZusatzmerkmal */
	public static final String QUERY_LIST_BY_IDZUSATZMERKMAL = "SELECT e FROM DTOSchuelerKAoADaten e WHERE e.idZusatzmerkmal IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes idAnschlussoption */
	public static final String QUERY_BY_IDANSCHLUSSOPTION = "SELECT e FROM DTOSchuelerKAoADaten e WHERE e.idAnschlussoption = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes idAnschlussoption */
	public static final String QUERY_LIST_BY_IDANSCHLUSSOPTION = "SELECT e FROM DTOSchuelerKAoADaten e WHERE e.idAnschlussoption IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes idBerufsfeld */
	public static final String QUERY_BY_IDBERUFSFELD = "SELECT e FROM DTOSchuelerKAoADaten e WHERE e.idBerufsfeld = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes idBerufsfeld */
	public static final String QUERY_LIST_BY_IDBERUFSFELD = "SELECT e FROM DTOSchuelerKAoADaten e WHERE e.idBerufsfeld IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes idEbene4 */
	public static final String QUERY_BY_IDEBENE4 = "SELECT e FROM DTOSchuelerKAoADaten e WHERE e.idEbene4 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes idEbene4 */
	public static final String QUERY_LIST_BY_IDEBENE4 = "SELECT e FROM DTOSchuelerKAoADaten e WHERE e.idEbene4 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes bemerkung */
	public static final String QUERY_BY_BEMERKUNG = "SELECT e FROM DTOSchuelerKAoADaten e WHERE e.bemerkung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes bemerkung */
	public static final String QUERY_LIST_BY_BEMERKUNG = "SELECT e FROM DTOSchuelerKAoADaten e WHERE e.bemerkung IN ?1";

	/** ID des KAOA-Eintrags beim Schüler */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long id;

	/** ID der zugehörigen Schülerlernabschnittsdaten */
	@Column(name = "Abschnitt_ID")
	@JsonProperty
	public long idLernabschnitt;

	/** Jahrgang des KAOA-Eintrags beim Schüler */
	@Column(name = "Jahrgang")
	@JsonProperty
	public String jahrgang;

	/** ID der Kategorie des KAOA-Eintrags beim Schüler FK */
	@Column(name = "KategorieID")
	@JsonProperty
	public long idKategorie;

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

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerKAoADaten ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSchuelerKAoADaten() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerKAoADaten ohne eine Initialisierung der Attribute.
	 * @param id   der Wert für das Attribut id
	 * @param idLernabschnitt   der Wert für das Attribut idLernabschnitt
	 * @param idKategorie   der Wert für das Attribut idKategorie
	 */
	public DTOSchuelerKAoADaten(final long id, final long idLernabschnitt, final long idKategorie) {
		this.id = id;
		this.idLernabschnitt = idLernabschnitt;
		this.idKategorie = idKategorie;
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
		return id == other.id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(id);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOSchuelerKAoADaten(id=" + this.id + ", idLernabschnitt=" + this.idLernabschnitt + ", jahrgang=" + this.jahrgang + ", idKategorie=" + this.idKategorie + ", idMerkmal=" + this.idMerkmal + ", idZusatzmerkmal=" + this.idZusatzmerkmal + ", idAnschlussoption=" + this.idAnschlussoption + ", idBerufsfeld=" + this.idBerufsfeld + ", idEbene4=" + this.idEbene4 + ", bemerkung=" + this.bemerkung + ")";
	}

}
