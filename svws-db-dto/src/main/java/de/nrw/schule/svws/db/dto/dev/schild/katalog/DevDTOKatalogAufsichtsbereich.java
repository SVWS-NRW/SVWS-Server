package de.nrw.schule.svws.db.dto.dev.schild.katalog;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Katalog_Aufsichtsbereich.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Katalog_Aufsichtsbereich")
@NamedQuery(name="DevDTOKatalogAufsichtsbereich.all", query="SELECT e FROM DevDTOKatalogAufsichtsbereich e")
@NamedQuery(name="DevDTOKatalogAufsichtsbereich.id", query="SELECT e FROM DevDTOKatalogAufsichtsbereich e WHERE e.ID = :value")
@NamedQuery(name="DevDTOKatalogAufsichtsbereich.id.multiple", query="SELECT e FROM DevDTOKatalogAufsichtsbereich e WHERE e.ID IN :value")
@NamedQuery(name="DevDTOKatalogAufsichtsbereich.kuerzel", query="SELECT e FROM DevDTOKatalogAufsichtsbereich e WHERE e.Kuerzel = :value")
@NamedQuery(name="DevDTOKatalogAufsichtsbereich.kuerzel.multiple", query="SELECT e FROM DevDTOKatalogAufsichtsbereich e WHERE e.Kuerzel IN :value")
@NamedQuery(name="DevDTOKatalogAufsichtsbereich.beschreibung", query="SELECT e FROM DevDTOKatalogAufsichtsbereich e WHERE e.Beschreibung = :value")
@NamedQuery(name="DevDTOKatalogAufsichtsbereich.beschreibung.multiple", query="SELECT e FROM DevDTOKatalogAufsichtsbereich e WHERE e.Beschreibung IN :value")
@NamedQuery(name="DevDTOKatalogAufsichtsbereich.primaryKeyQuery", query="SELECT e FROM DevDTOKatalogAufsichtsbereich e WHERE e.ID = ?1")
@NamedQuery(name="DevDTOKatalogAufsichtsbereich.all.migration", query="SELECT e FROM DevDTOKatalogAufsichtsbereich e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Kuerzel","Beschreibung"})
public class DevDTOKatalogAufsichtsbereich {

	/** Die ID identifiziert einen Aufsichtsbereich eindeutig */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Die Kurzbezeichnung des Aufsichtsbereichs */
	@Column(name = "Kuerzel")
	@JsonProperty
	public String Kuerzel;

	/** Gegebenenfalls eine ausführlichere Beschreibung des Aufsichtsbereichs */
	@Column(name = "Beschreibung")
	@JsonProperty
	public String Beschreibung;

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOKatalogAufsichtsbereich ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DevDTOKatalogAufsichtsbereich() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOKatalogAufsichtsbereich ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Kuerzel   der Wert für das Attribut Kuerzel
	 * @param Beschreibung   der Wert für das Attribut Beschreibung
	 */
	public DevDTOKatalogAufsichtsbereich(final Long ID, final String Kuerzel, final String Beschreibung) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Kuerzel == null) { 
			throw new NullPointerException("Kuerzel must not be null");
		}
		this.Kuerzel = Kuerzel;
		if (Beschreibung == null) { 
			throw new NullPointerException("Beschreibung must not be null");
		}
		this.Beschreibung = Beschreibung;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DevDTOKatalogAufsichtsbereich other = (DevDTOKatalogAufsichtsbereich) obj;
		if (ID == null) {
			if (other.ID != null)
				return false;
		} else if (!ID.equals(other.ID))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ID == null) ? 0 : ID.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DevDTOKatalogAufsichtsbereich(ID=" + this.ID + ", Kuerzel=" + this.Kuerzel + ", Beschreibung=" + this.Beschreibung + ")";
	}

}