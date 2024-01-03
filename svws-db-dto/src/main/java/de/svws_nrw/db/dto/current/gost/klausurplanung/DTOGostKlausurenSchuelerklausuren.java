package de.svws_nrw.db.dto.current.gost.klausurplanung;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Gost_Klausuren_Schuelerklausuren.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Gost_Klausuren_Schuelerklausuren")
@NamedQuery(name = "DTOGostKlausurenSchuelerklausuren.all", query = "SELECT e FROM DTOGostKlausurenSchuelerklausuren e")
@NamedQuery(name = "DTOGostKlausurenSchuelerklausuren.id", query = "SELECT e FROM DTOGostKlausurenSchuelerklausuren e WHERE e.ID = :value")
@NamedQuery(name = "DTOGostKlausurenSchuelerklausuren.id.multiple", query = "SELECT e FROM DTOGostKlausurenSchuelerklausuren e WHERE e.ID IN :value")
@NamedQuery(name = "DTOGostKlausurenSchuelerklausuren.kursklausur_id", query = "SELECT e FROM DTOGostKlausurenSchuelerklausuren e WHERE e.Kursklausur_ID = :value")
@NamedQuery(name = "DTOGostKlausurenSchuelerklausuren.kursklausur_id.multiple", query = "SELECT e FROM DTOGostKlausurenSchuelerklausuren e WHERE e.Kursklausur_ID IN :value")
@NamedQuery(name = "DTOGostKlausurenSchuelerklausuren.schueler_id", query = "SELECT e FROM DTOGostKlausurenSchuelerklausuren e WHERE e.Schueler_ID = :value")
@NamedQuery(name = "DTOGostKlausurenSchuelerklausuren.schueler_id.multiple", query = "SELECT e FROM DTOGostKlausurenSchuelerklausuren e WHERE e.Schueler_ID IN :value")
@NamedQuery(name = "DTOGostKlausurenSchuelerklausuren.bemerkungen", query = "SELECT e FROM DTOGostKlausurenSchuelerklausuren e WHERE e.Bemerkungen = :value")
@NamedQuery(name = "DTOGostKlausurenSchuelerklausuren.bemerkungen.multiple", query = "SELECT e FROM DTOGostKlausurenSchuelerklausuren e WHERE e.Bemerkungen IN :value")
@NamedQuery(name = "DTOGostKlausurenSchuelerklausuren.primaryKeyQuery", query = "SELECT e FROM DTOGostKlausurenSchuelerklausuren e WHERE e.ID = ?1")
@NamedQuery(name = "DTOGostKlausurenSchuelerklausuren.primaryKeyQuery.multiple", query = "SELECT e FROM DTOGostKlausurenSchuelerklausuren e WHERE e.ID IN :value")
@NamedQuery(name = "DTOGostKlausurenSchuelerklausuren.all.migration", query = "SELECT e FROM DTOGostKlausurenSchuelerklausuren e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Kursklausur_ID", "Schueler_ID", "Bemerkungen"})
public final class DTOGostKlausurenSchuelerklausuren {

	/** ID der Klausurvorgaben (generiert) */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** ID der Kursklausur */
	@Column(name = "Kursklausur_ID")
	@JsonProperty
	public long Kursklausur_ID;

	/** ID des Schülers */
	@Column(name = "Schueler_ID")
	@JsonProperty
	public long Schueler_ID;

	/** Text für Bemerkungen zur Schuelerklausur */
	@Column(name = "Bemerkungen")
	@JsonProperty
	public String Bemerkungen;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostKlausurenSchuelerklausuren ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOGostKlausurenSchuelerklausuren() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostKlausurenSchuelerklausuren ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Kursklausur_ID   der Wert für das Attribut Kursklausur_ID
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 */
	public DTOGostKlausurenSchuelerklausuren(final long ID, final long Kursklausur_ID, final long Schueler_ID) {
		this.ID = ID;
		this.Kursklausur_ID = Kursklausur_ID;
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
		DTOGostKlausurenSchuelerklausuren other = (DTOGostKlausurenSchuelerklausuren) obj;
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
		return "DTOGostKlausurenSchuelerklausuren(ID=" + this.ID + ", Kursklausur_ID=" + this.Kursklausur_ID + ", Schueler_ID=" + this.Schueler_ID + ", Bemerkungen=" + this.Bemerkungen + ")";
	}

}
