package de.svws_nrw.db.dto.current.gost.klausurplanung;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Gost_Klausuren_SchuelerklausurenTermine_Raumstunden.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(DTOGostKlausurenSchuelerklausurenTermineRaumstundenPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Gost_Klausuren_SchuelerklausurenTermine_Raumstunden")
@NamedQuery(name = "DTOGostKlausurenSchuelerklausurenTermineRaumstunden.all", query = "SELECT e FROM DTOGostKlausurenSchuelerklausurenTermineRaumstunden e")
@NamedQuery(name = "DTOGostKlausurenSchuelerklausurenTermineRaumstunden.schuelerklausurtermin_id", query = "SELECT e FROM DTOGostKlausurenSchuelerklausurenTermineRaumstunden e WHERE e.Schuelerklausurtermin_ID = :value")
@NamedQuery(name = "DTOGostKlausurenSchuelerklausurenTermineRaumstunden.schuelerklausurtermin_id.multiple", query = "SELECT e FROM DTOGostKlausurenSchuelerklausurenTermineRaumstunden e WHERE e.Schuelerklausurtermin_ID IN :value")
@NamedQuery(name = "DTOGostKlausurenSchuelerklausurenTermineRaumstunden.raumstunde_id", query = "SELECT e FROM DTOGostKlausurenSchuelerklausurenTermineRaumstunden e WHERE e.Raumstunde_ID = :value")
@NamedQuery(name = "DTOGostKlausurenSchuelerklausurenTermineRaumstunden.raumstunde_id.multiple", query = "SELECT e FROM DTOGostKlausurenSchuelerklausurenTermineRaumstunden e WHERE e.Raumstunde_ID IN :value")
@NamedQuery(name = "DTOGostKlausurenSchuelerklausurenTermineRaumstunden.primaryKeyQuery", query = "SELECT e FROM DTOGostKlausurenSchuelerklausurenTermineRaumstunden e WHERE e.Schuelerklausurtermin_ID = ?1 AND e.Raumstunde_ID = ?2")
@NamedQuery(name = "DTOGostKlausurenSchuelerklausurenTermineRaumstunden.all.migration", query = "SELECT e FROM DTOGostKlausurenSchuelerklausurenTermineRaumstunden e WHERE e.Schuelerklausurtermin_ID IS NOT NULL AND e.Raumstunde_ID IS NOT NULL")
@JsonPropertyOrder({"Schuelerklausurtermin_ID", "Raumstunde_ID"})
public final class DTOGostKlausurenSchuelerklausurenTermineRaumstunden {

	/** ID des Schuelerklausurtermins */
	@Id
	@Column(name = "Schuelerklausurtermin_ID")
	@JsonProperty
	public long Schuelerklausurtermin_ID;

	/** ID der Klausurraumstunde */
	@Id
	@Column(name = "Raumstunde_ID")
	@JsonProperty
	public long Raumstunde_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostKlausurenSchuelerklausurenTermineRaumstunden ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOGostKlausurenSchuelerklausurenTermineRaumstunden() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostKlausurenSchuelerklausurenTermineRaumstunden ohne eine Initialisierung der Attribute.
	 * @param Schuelerklausurtermin_ID   der Wert für das Attribut Schuelerklausurtermin_ID
	 * @param Raumstunde_ID   der Wert für das Attribut Raumstunde_ID
	 */
	public DTOGostKlausurenSchuelerklausurenTermineRaumstunden(final long Schuelerklausurtermin_ID, final long Raumstunde_ID) {
		this.Schuelerklausurtermin_ID = Schuelerklausurtermin_ID;
		this.Raumstunde_ID = Raumstunde_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOGostKlausurenSchuelerklausurenTermineRaumstunden other = (DTOGostKlausurenSchuelerklausurenTermineRaumstunden) obj;
		if (Schuelerklausurtermin_ID != other.Schuelerklausurtermin_ID)
			return false;
		return Raumstunde_ID == other.Raumstunde_ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(Schuelerklausurtermin_ID);

		result = prime * result + Long.hashCode(Raumstunde_ID);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOGostKlausurenSchuelerklausurenTermineRaumstunden(Schuelerklausurtermin_ID=" + this.Schuelerklausurtermin_ID + ", Raumstunde_ID=" + this.Raumstunde_ID + ")";
	}

}
