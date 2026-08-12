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
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerFotos.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerFotos")
@JsonPropertyOrder({"idSchueler", "Foto", "fotoBase64", "SchulnrEigner"})
public final class MigrationDTOSchuelerFoto {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM MigrationDTOSchuelerFoto e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM MigrationDTOSchuelerFoto e WHERE e.idSchueler = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM MigrationDTOSchuelerFoto e WHERE e.idSchueler IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM MigrationDTOSchuelerFoto e WHERE e.idSchueler IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes idSchueler */
	public static final String QUERY_BY_IDSCHUELER = "SELECT e FROM MigrationDTOSchuelerFoto e WHERE e.idSchueler = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes idSchueler */
	public static final String QUERY_LIST_BY_IDSCHUELER = "SELECT e FROM MigrationDTOSchuelerFoto e WHERE e.idSchueler IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Foto */
	public static final String QUERY_BY_FOTO = "SELECT e FROM MigrationDTOSchuelerFoto e WHERE e.Foto = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Foto */
	public static final String QUERY_LIST_BY_FOTO = "SELECT e FROM MigrationDTOSchuelerFoto e WHERE e.Foto IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes fotoBase64 */
	public static final String QUERY_BY_FOTOBASE64 = "SELECT e FROM MigrationDTOSchuelerFoto e WHERE e.fotoBase64 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes fotoBase64 */
	public static final String QUERY_LIST_BY_FOTOBASE64 = "SELECT e FROM MigrationDTOSchuelerFoto e WHERE e.fotoBase64 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchulnrEigner */
	public static final String QUERY_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOSchuelerFoto e WHERE e.SchulnrEigner = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchulnrEigner */
	public static final String QUERY_LIST_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOSchuelerFoto e WHERE e.SchulnrEigner IN ?1";

	/** SchülerID zum Foto */
	@Id
	@Column(name = "Schueler_ID")
	@JsonProperty
	public Long idSchueler;

	/** Schülerfoto im binär-Format */
	@Column(name = "Foto")
	@JsonProperty
	public byte[] Foto;

	/** Schülerfoto im Base64-Format */
	@Column(name = "FotoBase64")
	@JsonProperty
	public String fotoBase64;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuelerFoto ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOSchuelerFoto() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuelerFoto ohne eine Initialisierung der Attribute.
	 * @param idSchueler   der Wert für das Attribut idSchueler
	 */
	public MigrationDTOSchuelerFoto(final Long idSchueler) {
		if (idSchueler == null) {
			throw new NullPointerException("idSchueler must not be null");
		}
		this.idSchueler = idSchueler;
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
		MigrationDTOSchuelerFoto other = (MigrationDTOSchuelerFoto) obj;
		if (idSchueler == null) {
			if (other.idSchueler != null) {
				return false;
			}
		} else if (!idSchueler.equals(other.idSchueler)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idSchueler == null) ? 0 : idSchueler.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "MigrationDTOSchuelerFoto(idSchueler=" + this.idSchueler + ", Foto=" + this.Foto + ", fotoBase64=" + this.fotoBase64 + ", SchulnrEigner=" + this.SchulnrEigner + ")";
	}

}
