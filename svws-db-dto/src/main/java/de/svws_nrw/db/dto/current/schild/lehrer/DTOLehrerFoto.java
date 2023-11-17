package de.svws_nrw.db.dto.current.schild.lehrer;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle LehrerFotos.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "LehrerFotos")
@NamedQuery(name = "DTOLehrerFoto.all", query = "SELECT e FROM DTOLehrerFoto e")
@NamedQuery(name = "DTOLehrerFoto.lehrer_id", query = "SELECT e FROM DTOLehrerFoto e WHERE e.Lehrer_ID = :value")
@NamedQuery(name = "DTOLehrerFoto.lehrer_id.multiple", query = "SELECT e FROM DTOLehrerFoto e WHERE e.Lehrer_ID IN :value")
@NamedQuery(name = "DTOLehrerFoto.fotobase64", query = "SELECT e FROM DTOLehrerFoto e WHERE e.FotoBase64 = :value")
@NamedQuery(name = "DTOLehrerFoto.fotobase64.multiple", query = "SELECT e FROM DTOLehrerFoto e WHERE e.FotoBase64 IN :value")
@NamedQuery(name = "DTOLehrerFoto.primaryKeyQuery", query = "SELECT e FROM DTOLehrerFoto e WHERE e.Lehrer_ID = ?1")
@NamedQuery(name = "DTOLehrerFoto.primaryKeyQuery.multiple", query = "SELECT e FROM DTOLehrerFoto e WHERE e.Lehrer_ID IN :value")
@NamedQuery(name = "DTOLehrerFoto.all.migration", query = "SELECT e FROM DTOLehrerFoto e WHERE e.Lehrer_ID IS NOT NULL")
@JsonPropertyOrder({"Lehrer_ID", "FotoBase64"})
public final class DTOLehrerFoto {

	/** LehrerID zu der das Foto gehört */
	@Id
	@Column(name = "Lehrer_ID")
	@JsonProperty
	public long Lehrer_ID;

	/** Lehrerfoto im Base64-Format */
	@Column(name = "FotoBase64")
	@JsonProperty
	public String FotoBase64;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOLehrerFoto ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOLehrerFoto() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOLehrerFoto ohne eine Initialisierung der Attribute.
	 * @param Lehrer_ID   der Wert für das Attribut Lehrer_ID
	 */
	public DTOLehrerFoto(final long Lehrer_ID) {
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
		DTOLehrerFoto other = (DTOLehrerFoto) obj;
		return Lehrer_ID == other.Lehrer_ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(Lehrer_ID);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOLehrerFoto(Lehrer_ID=" + this.Lehrer_ID + ", FotoBase64=" + this.FotoBase64 + ")";
	}

}
