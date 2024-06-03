package de.svws_nrw.db.dto.migration.schild.lehrer;

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
@JsonPropertyOrder({"Lehrer_ID", "Foto", "FotoBase64", "SchulnrEigner"})
public final class MigrationDTOLehrerFoto {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM MigrationDTOLehrerFoto e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM MigrationDTOLehrerFoto e WHERE e.Lehrer_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM MigrationDTOLehrerFoto e WHERE e.Lehrer_ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM MigrationDTOLehrerFoto e WHERE e.Lehrer_ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Lehrer_ID */
	public static final String QUERY_BY_LEHRER_ID = "SELECT e FROM MigrationDTOLehrerFoto e WHERE e.Lehrer_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Lehrer_ID */
	public static final String QUERY_LIST_BY_LEHRER_ID = "SELECT e FROM MigrationDTOLehrerFoto e WHERE e.Lehrer_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Foto */
	public static final String QUERY_BY_FOTO = "SELECT e FROM MigrationDTOLehrerFoto e WHERE e.Foto = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Foto */
	public static final String QUERY_LIST_BY_FOTO = "SELECT e FROM MigrationDTOLehrerFoto e WHERE e.Foto IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes FotoBase64 */
	public static final String QUERY_BY_FOTOBASE64 = "SELECT e FROM MigrationDTOLehrerFoto e WHERE e.FotoBase64 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes FotoBase64 */
	public static final String QUERY_LIST_BY_FOTOBASE64 = "SELECT e FROM MigrationDTOLehrerFoto e WHERE e.FotoBase64 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchulnrEigner */
	public static final String QUERY_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOLehrerFoto e WHERE e.SchulnrEigner = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchulnrEigner */
	public static final String QUERY_LIST_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOLehrerFoto e WHERE e.SchulnrEigner IN ?1";

	/** LehrerID zu der das Foto gehört */
	@Id
	@Column(name = "Lehrer_ID")
	@JsonProperty
	public Long Lehrer_ID;

	/** Lehrerfoto im binär-Format */
	@Column(name = "Foto")
	@JsonProperty
	public byte[] Foto;

	/** Lehrerfoto im Base64-Format */
	@Column(name = "FotoBase64")
	@JsonProperty
	public String FotoBase64;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOLehrerFoto ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOLehrerFoto() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOLehrerFoto ohne eine Initialisierung der Attribute.
	 * @param Lehrer_ID   der Wert für das Attribut Lehrer_ID
	 */
	public MigrationDTOLehrerFoto(final Long Lehrer_ID) {
		if (Lehrer_ID == null) {
			throw new NullPointerException("Lehrer_ID must not be null");
		}
		this.Lehrer_ID = Lehrer_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOLehrerFoto other = (MigrationDTOLehrerFoto) obj;
		if (Lehrer_ID == null) {
			if (other.Lehrer_ID != null)
				return false;
		} else if (!Lehrer_ID.equals(other.Lehrer_ID))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Lehrer_ID == null) ? 0 : Lehrer_ID.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "MigrationDTOLehrerFoto(Lehrer_ID=" + this.Lehrer_ID + ", Foto=" + this.Foto + ", FotoBase64=" + this.FotoBase64 + ", SchulnrEigner=" + this.SchulnrEigner + ")";
	}

}
