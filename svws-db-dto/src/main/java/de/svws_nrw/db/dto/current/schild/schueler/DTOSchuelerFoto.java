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
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerFotos.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerFotos")
@JsonPropertyOrder({"Schueler_ID", "FotoBase64"})
public final class DTOSchuelerFoto {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOSchuelerFoto e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOSchuelerFoto e WHERE e.Schueler_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOSchuelerFoto e WHERE e.Schueler_ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOSchuelerFoto e WHERE e.Schueler_ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schueler_ID */
	public static final String QUERY_BY_SCHUELER_ID = "SELECT e FROM DTOSchuelerFoto e WHERE e.Schueler_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schueler_ID */
	public static final String QUERY_LIST_BY_SCHUELER_ID = "SELECT e FROM DTOSchuelerFoto e WHERE e.Schueler_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes FotoBase64 */
	public static final String QUERY_BY_FOTOBASE64 = "SELECT e FROM DTOSchuelerFoto e WHERE e.FotoBase64 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes FotoBase64 */
	public static final String QUERY_LIST_BY_FOTOBASE64 = "SELECT e FROM DTOSchuelerFoto e WHERE e.FotoBase64 IN ?1";

	/** SchülerID zum Foto */
	@Id
	@Column(name = "Schueler_ID")
	@JsonProperty
	public long Schueler_ID;

	/** Schülerfoto im Base64-Format */
	@Column(name = "FotoBase64")
	@JsonProperty
	public String FotoBase64;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerFoto ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSchuelerFoto() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerFoto ohne eine Initialisierung der Attribute.
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 */
	public DTOSchuelerFoto(final long Schueler_ID) {
		this.Schueler_ID = Schueler_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOSchuelerFoto other = (DTOSchuelerFoto) obj;
		return Schueler_ID == other.Schueler_ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(Schueler_ID);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOSchuelerFoto(Schueler_ID=" + this.Schueler_ID + ", FotoBase64=" + this.FotoBase64 + ")";
	}

}
