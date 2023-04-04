package de.svws_nrw.db.dto.migration.schild.schueler;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
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
@NamedQuery(name = "MigrationDTOSchuelerFoto.all", query = "SELECT e FROM MigrationDTOSchuelerFoto e")
@NamedQuery(name = "MigrationDTOSchuelerFoto.schueler_id", query = "SELECT e FROM MigrationDTOSchuelerFoto e WHERE e.Schueler_ID = :value")
@NamedQuery(name = "MigrationDTOSchuelerFoto.schueler_id.multiple", query = "SELECT e FROM MigrationDTOSchuelerFoto e WHERE e.Schueler_ID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerFoto.foto", query = "SELECT e FROM MigrationDTOSchuelerFoto e WHERE e.Foto = :value")
@NamedQuery(name = "MigrationDTOSchuelerFoto.foto.multiple", query = "SELECT e FROM MigrationDTOSchuelerFoto e WHERE e.Foto IN :value")
@NamedQuery(name = "MigrationDTOSchuelerFoto.fotobase64", query = "SELECT e FROM MigrationDTOSchuelerFoto e WHERE e.FotoBase64 = :value")
@NamedQuery(name = "MigrationDTOSchuelerFoto.fotobase64.multiple", query = "SELECT e FROM MigrationDTOSchuelerFoto e WHERE e.FotoBase64 IN :value")
@NamedQuery(name = "MigrationDTOSchuelerFoto.schulnreigner", query = "SELECT e FROM MigrationDTOSchuelerFoto e WHERE e.SchulnrEigner = :value")
@NamedQuery(name = "MigrationDTOSchuelerFoto.schulnreigner.multiple", query = "SELECT e FROM MigrationDTOSchuelerFoto e WHERE e.SchulnrEigner IN :value")
@NamedQuery(name = "MigrationDTOSchuelerFoto.primaryKeyQuery", query = "SELECT e FROM MigrationDTOSchuelerFoto e WHERE e.Schueler_ID = ?1")
@NamedQuery(name = "MigrationDTOSchuelerFoto.all.migration", query = "SELECT e FROM MigrationDTOSchuelerFoto e WHERE e.Schueler_ID IS NOT NULL")
@JsonPropertyOrder({"Schueler_ID", "Foto", "FotoBase64", "SchulnrEigner"})
public final class MigrationDTOSchuelerFoto {

	/** SchülerID zum Foto */
	@Id
	@Column(name = "Schueler_ID")
	@JsonProperty
	public Long Schueler_ID;

	/** Schülerfoto im binär-Format */
	@Column(name = "Foto")
	@JsonProperty
	public byte[] Foto;

	/** Schülerfoto im Base64-Format */
	@Column(name = "FotoBase64")
	@JsonProperty
	public String FotoBase64;

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
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 */
	public MigrationDTOSchuelerFoto(final Long Schueler_ID) {
		if (Schueler_ID == null) {
			throw new NullPointerException("Schueler_ID must not be null");
		}
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
		MigrationDTOSchuelerFoto other = (MigrationDTOSchuelerFoto) obj;
		if (Schueler_ID == null) {
			if (other.Schueler_ID != null)
				return false;
		} else if (!Schueler_ID.equals(other.Schueler_ID))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Schueler_ID == null) ? 0 : Schueler_ID.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "MigrationDTOSchuelerFoto(Schueler_ID=" + this.Schueler_ID + ", Foto=" + this.Foto + ", FotoBase64=" + this.FotoBase64 + ", SchulnrEigner=" + this.SchulnrEigner + ")";
	}

}
