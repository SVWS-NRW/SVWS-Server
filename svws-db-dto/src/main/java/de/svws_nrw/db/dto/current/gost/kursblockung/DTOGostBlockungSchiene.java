package de.svws_nrw.db.dto.current.gost.kursblockung;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Gost_Blockung_Schienen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Gost_Blockung_Schienen")
@NamedQuery(name = "DTOGostBlockungSchiene.all", query = "SELECT e FROM DTOGostBlockungSchiene e")
@NamedQuery(name = "DTOGostBlockungSchiene.id", query = "SELECT e FROM DTOGostBlockungSchiene e WHERE e.ID = :value")
@NamedQuery(name = "DTOGostBlockungSchiene.id.multiple", query = "SELECT e FROM DTOGostBlockungSchiene e WHERE e.ID IN :value")
@NamedQuery(name = "DTOGostBlockungSchiene.blockung_id", query = "SELECT e FROM DTOGostBlockungSchiene e WHERE e.Blockung_ID = :value")
@NamedQuery(name = "DTOGostBlockungSchiene.blockung_id.multiple", query = "SELECT e FROM DTOGostBlockungSchiene e WHERE e.Blockung_ID IN :value")
@NamedQuery(name = "DTOGostBlockungSchiene.nummer", query = "SELECT e FROM DTOGostBlockungSchiene e WHERE e.Nummer = :value")
@NamedQuery(name = "DTOGostBlockungSchiene.nummer.multiple", query = "SELECT e FROM DTOGostBlockungSchiene e WHERE e.Nummer IN :value")
@NamedQuery(name = "DTOGostBlockungSchiene.bezeichnung", query = "SELECT e FROM DTOGostBlockungSchiene e WHERE e.Bezeichnung = :value")
@NamedQuery(name = "DTOGostBlockungSchiene.bezeichnung.multiple", query = "SELECT e FROM DTOGostBlockungSchiene e WHERE e.Bezeichnung IN :value")
@NamedQuery(name = "DTOGostBlockungSchiene.wochenstunden", query = "SELECT e FROM DTOGostBlockungSchiene e WHERE e.Wochenstunden = :value")
@NamedQuery(name = "DTOGostBlockungSchiene.wochenstunden.multiple", query = "SELECT e FROM DTOGostBlockungSchiene e WHERE e.Wochenstunden IN :value")
@NamedQuery(name = "DTOGostBlockungSchiene.primaryKeyQuery", query = "SELECT e FROM DTOGostBlockungSchiene e WHERE e.ID = ?1")
@NamedQuery(name = "DTOGostBlockungSchiene.primaryKeyQuery.multiple", query = "SELECT e FROM DTOGostBlockungSchiene e WHERE e.ID IN :value")
@NamedQuery(name = "DTOGostBlockungSchiene.all.migration", query = "SELECT e FROM DTOGostBlockungSchiene e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Blockung_ID", "Nummer", "Bezeichnung", "Wochenstunden"})
public final class DTOGostBlockungSchiene {

	/** ID der Schiene in der Blockung (generiert) */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** ID der Blockung */
	@Column(name = "Blockung_ID")
	@JsonProperty
	public long Blockung_ID;

	/** Die Nummer der Schiene, beginnend bei 1 */
	@Column(name = "Nummer")
	@JsonProperty
	public int Nummer;

	/** Bezeichnung der Schiene (z.B. LK Schiene 1) */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Die Anzahl der Wochenstunden, welche der Schiene zugeordnet sind */
	@Column(name = "Wochenstunden")
	@JsonProperty
	public int Wochenstunden;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostBlockungSchiene ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOGostBlockungSchiene() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostBlockungSchiene ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Blockung_ID   der Wert für das Attribut Blockung_ID
	 * @param Nummer   der Wert für das Attribut Nummer
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 * @param Wochenstunden   der Wert für das Attribut Wochenstunden
	 */
	public DTOGostBlockungSchiene(final long ID, final long Blockung_ID, final int Nummer, final String Bezeichnung, final int Wochenstunden) {
		this.ID = ID;
		this.Blockung_ID = Blockung_ID;
		this.Nummer = Nummer;
		if (Bezeichnung == null) {
			throw new NullPointerException("Bezeichnung must not be null");
		}
		this.Bezeichnung = Bezeichnung;
		this.Wochenstunden = Wochenstunden;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOGostBlockungSchiene other = (DTOGostBlockungSchiene) obj;
		return ID == other.ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(ID);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOGostBlockungSchiene(ID=" + this.ID + ", Blockung_ID=" + this.Blockung_ID + ", Nummer=" + this.Nummer + ", Bezeichnung=" + this.Bezeichnung + ", Wochenstunden=" + this.Wochenstunden + ")";
	}

}
