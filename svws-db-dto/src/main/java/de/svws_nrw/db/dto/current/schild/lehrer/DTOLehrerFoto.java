package de.svws_nrw.db.dto.current.schild.lehrer;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle LehrerFotos.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "LehrerFotos")
@JsonPropertyOrder({"idLehrer", "fotoBase64"})
public final class DTOLehrerFoto {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOLehrerFoto e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOLehrerFoto e WHERE e.idLehrer = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOLehrerFoto e WHERE e.idLehrer IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOLehrerFoto e WHERE e.idLehrer IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes idLehrer */
	public static final String QUERY_BY_IDLEHRER = "SELECT e FROM DTOLehrerFoto e WHERE e.idLehrer = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes idLehrer */
	public static final String QUERY_LIST_BY_IDLEHRER = "SELECT e FROM DTOLehrerFoto e WHERE e.idLehrer IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes fotoBase64 */
	public static final String QUERY_BY_FOTOBASE64 = "SELECT e FROM DTOLehrerFoto e WHERE e.fotoBase64 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes fotoBase64 */
	public static final String QUERY_LIST_BY_FOTOBASE64 = "SELECT e FROM DTOLehrerFoto e WHERE e.fotoBase64 IN ?1";

	/** LehrerID zu der das Foto gehört */
	@Id
	@Column(name = "Lehrer_ID")
	@JsonProperty
	public long idLehrer;

	/** Lehrerfoto im Base64-Format */
	@Column(name = "FotoBase64")
	@JsonProperty
	public String fotoBase64;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOLehrerFoto ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOLehrerFoto() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOLehrerFoto ohne eine Initialisierung der Attribute.
	 * @param idLehrer   der Wert für das Attribut idLehrer
	 */
	public DTOLehrerFoto(final long idLehrer) {
		this.idLehrer = idLehrer;
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
		DTOLehrerFoto other = (DTOLehrerFoto) obj;
		return idLehrer == other.idLehrer;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(idLehrer);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOLehrerFoto(idLehrer=" + this.idLehrer + ", fotoBase64=" + this.fotoBase64 + ")";
	}

}
