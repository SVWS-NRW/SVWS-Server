package de.nrw.schule.svws.db.dto.dev.schild.lehrer;

import de.nrw.schule.svws.db.DBEntityManager;

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
@NamedQuery(name="DevDTOLehrerFoto.all", query="SELECT e FROM DevDTOLehrerFoto e")
@NamedQuery(name="DevDTOLehrerFoto.lehrer_id", query="SELECT e FROM DevDTOLehrerFoto e WHERE e.Lehrer_ID = :value")
@NamedQuery(name="DevDTOLehrerFoto.lehrer_id.multiple", query="SELECT e FROM DevDTOLehrerFoto e WHERE e.Lehrer_ID IN :value")
@NamedQuery(name="DevDTOLehrerFoto.fotobase64", query="SELECT e FROM DevDTOLehrerFoto e WHERE e.FotoBase64 = :value")
@NamedQuery(name="DevDTOLehrerFoto.fotobase64.multiple", query="SELECT e FROM DevDTOLehrerFoto e WHERE e.FotoBase64 IN :value")
@NamedQuery(name="DevDTOLehrerFoto.primaryKeyQuery", query="SELECT e FROM DevDTOLehrerFoto e WHERE e.Lehrer_ID = ?1")
@NamedQuery(name="DevDTOLehrerFoto.all.migration", query="SELECT e FROM DevDTOLehrerFoto e WHERE e.Lehrer_ID IS NOT NULL")
@JsonPropertyOrder({"Lehrer_ID","FotoBase64"})
public class DevDTOLehrerFoto {

	/** LehrerID zu der das Foto gehört */
	@Id
	@Column(name = "Lehrer_ID")
	@JsonProperty
	public Long Lehrer_ID;

	/** Lehrerfoto im Base64-Format */
	@Column(name = "FotoBase64")
	@JsonProperty
	public String FotoBase64;

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOLehrerFoto ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DevDTOLehrerFoto() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOLehrerFoto ohne eine Initialisierung der Attribute.
	 * @param Lehrer_ID   der Wert für das Attribut Lehrer_ID
	 */
	public DevDTOLehrerFoto(final Long Lehrer_ID) {
		if (Lehrer_ID == null) { 
			throw new NullPointerException("Lehrer_ID must not be null");
		}
		this.Lehrer_ID = Lehrer_ID;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DevDTOLehrerFoto other = (DevDTOLehrerFoto) obj;
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
		return "DevDTOLehrerFoto(Lehrer_ID=" + this.Lehrer_ID + ", FotoBase64=" + this.FotoBase64 + ")";
	}

}