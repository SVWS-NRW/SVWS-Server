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
 * Diese Klasse dient als DTO für die Datenbanktabelle Gost_Klausuren_Schuelerklausuren_Raeume_Stunden.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(DTOGostKlausurenSchuelerklausurenRaeumeStundenPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Gost_Klausuren_Schuelerklausuren_Raeume_Stunden")
@NamedQuery(name = "DTOGostKlausurenSchuelerklausurenRaeumeStunden.all", query = "SELECT e FROM DTOGostKlausurenSchuelerklausurenRaeumeStunden e")
@NamedQuery(name = "DTOGostKlausurenSchuelerklausurenRaeumeStunden.schuelerklausur_id", query = "SELECT e FROM DTOGostKlausurenSchuelerklausurenRaeumeStunden e WHERE e.Schuelerklausur_ID = :value")
@NamedQuery(name = "DTOGostKlausurenSchuelerklausurenRaeumeStunden.schuelerklausur_id.multiple", query = "SELECT e FROM DTOGostKlausurenSchuelerklausurenRaeumeStunden e WHERE e.Schuelerklausur_ID IN :value")
@NamedQuery(name = "DTOGostKlausurenSchuelerklausurenRaeumeStunden.klausurraumstunde_id", query = "SELECT e FROM DTOGostKlausurenSchuelerklausurenRaeumeStunden e WHERE e.KlausurRaumStunde_ID = :value")
@NamedQuery(name = "DTOGostKlausurenSchuelerklausurenRaeumeStunden.klausurraumstunde_id.multiple", query = "SELECT e FROM DTOGostKlausurenSchuelerklausurenRaeumeStunden e WHERE e.KlausurRaumStunde_ID IN :value")
@NamedQuery(name = "DTOGostKlausurenSchuelerklausurenRaeumeStunden.primaryKeyQuery", query = "SELECT e FROM DTOGostKlausurenSchuelerklausurenRaeumeStunden e WHERE e.Schuelerklausur_ID = ?1 AND e.KlausurRaumStunde_ID = ?2")
@NamedQuery(name = "DTOGostKlausurenSchuelerklausurenRaeumeStunden.all.migration", query = "SELECT e FROM DTOGostKlausurenSchuelerklausurenRaeumeStunden e WHERE e.Schuelerklausur_ID IS NOT NULL AND e.KlausurRaumStunde_ID IS NOT NULL")
@JsonPropertyOrder({"Schuelerklausur_ID", "KlausurRaumStunde_ID"})
public final class DTOGostKlausurenSchuelerklausurenRaeumeStunden {

	/** ID der Schuelerklausur */
	@Id
	@Column(name = "Schuelerklausur_ID")
	@JsonProperty
	public long Schuelerklausur_ID;

	/** ID der Klausurraumstunde */
	@Id
	@Column(name = "KlausurRaumStunde_ID")
	@JsonProperty
	public long KlausurRaumStunde_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostKlausurenSchuelerklausurenRaeumeStunden ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOGostKlausurenSchuelerklausurenRaeumeStunden() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostKlausurenSchuelerklausurenRaeumeStunden ohne eine Initialisierung der Attribute.
	 * @param Schuelerklausur_ID   der Wert für das Attribut Schuelerklausur_ID
	 * @param KlausurRaumStunde_ID   der Wert für das Attribut KlausurRaumStunde_ID
	 */
	public DTOGostKlausurenSchuelerklausurenRaeumeStunden(final long Schuelerklausur_ID, final long KlausurRaumStunde_ID) {
		this.Schuelerklausur_ID = Schuelerklausur_ID;
		this.KlausurRaumStunde_ID = KlausurRaumStunde_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOGostKlausurenSchuelerklausurenRaeumeStunden other = (DTOGostKlausurenSchuelerklausurenRaeumeStunden) obj;
		if (Schuelerklausur_ID != other.Schuelerklausur_ID)
			return false;
		return KlausurRaumStunde_ID == other.KlausurRaumStunde_ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(Schuelerklausur_ID);

		result = prime * result + Long.hashCode(KlausurRaumStunde_ID);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOGostKlausurenSchuelerklausurenRaeumeStunden(Schuelerklausur_ID=" + this.Schuelerklausur_ID + ", KlausurRaumStunde_ID=" + this.KlausurRaumStunde_ID + ")";
	}

}
