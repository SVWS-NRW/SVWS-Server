package de.nrw.schule.svws.db.dto.rev9.gost.klausurplanung;

import de.nrw.schule.svws.db.DBEntityManager;

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
@IdClass(Rev9DTOGostKlausurenSchuelerklausurenRaeumeStundenPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Gost_Klausuren_Schuelerklausuren_Raeume_Stunden")
@NamedQuery(name="Rev9DTOGostKlausurenSchuelerklausurenRaeumeStunden.all", query="SELECT e FROM Rev9DTOGostKlausurenSchuelerklausurenRaeumeStunden e")
@NamedQuery(name="Rev9DTOGostKlausurenSchuelerklausurenRaeumeStunden.schuelerklausur_id", query="SELECT e FROM Rev9DTOGostKlausurenSchuelerklausurenRaeumeStunden e WHERE e.Schuelerklausur_ID = :value")
@NamedQuery(name="Rev9DTOGostKlausurenSchuelerklausurenRaeumeStunden.schuelerklausur_id.multiple", query="SELECT e FROM Rev9DTOGostKlausurenSchuelerklausurenRaeumeStunden e WHERE e.Schuelerklausur_ID IN :value")
@NamedQuery(name="Rev9DTOGostKlausurenSchuelerklausurenRaeumeStunden.klausurraumstunde_id", query="SELECT e FROM Rev9DTOGostKlausurenSchuelerklausurenRaeumeStunden e WHERE e.KlausurRaumStunde_ID = :value")
@NamedQuery(name="Rev9DTOGostKlausurenSchuelerklausurenRaeumeStunden.klausurraumstunde_id.multiple", query="SELECT e FROM Rev9DTOGostKlausurenSchuelerklausurenRaeumeStunden e WHERE e.KlausurRaumStunde_ID IN :value")
@NamedQuery(name="Rev9DTOGostKlausurenSchuelerklausurenRaeumeStunden.primaryKeyQuery", query="SELECT e FROM Rev9DTOGostKlausurenSchuelerklausurenRaeumeStunden e WHERE e.Schuelerklausur_ID = ?1 AND e.KlausurRaumStunde_ID = ?2")
@NamedQuery(name="Rev9DTOGostKlausurenSchuelerklausurenRaeumeStunden.all.migration", query="SELECT e FROM Rev9DTOGostKlausurenSchuelerklausurenRaeumeStunden e WHERE e.Schuelerklausur_ID IS NOT NULL AND e.KlausurRaumStunde_ID IS NOT NULL")
@JsonPropertyOrder({"Schuelerklausur_ID","KlausurRaumStunde_ID"})
public class Rev9DTOGostKlausurenSchuelerklausurenRaeumeStunden {

	/** ID der Schuelerklausur */
	@Id
	@Column(name = "Schuelerklausur_ID")
	@JsonProperty
	public Long Schuelerklausur_ID;

	/** ID der Klausurraumstunde */
	@Id
	@Column(name = "KlausurRaumStunde_ID")
	@JsonProperty
	public Long KlausurRaumStunde_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev9DTOGostKlausurenSchuelerklausurenRaeumeStunden ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev9DTOGostKlausurenSchuelerklausurenRaeumeStunden() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev9DTOGostKlausurenSchuelerklausurenRaeumeStunden ohne eine Initialisierung der Attribute.
	 * @param Schuelerklausur_ID   der Wert für das Attribut Schuelerklausur_ID
	 * @param KlausurRaumStunde_ID   der Wert für das Attribut KlausurRaumStunde_ID
	 */
	public Rev9DTOGostKlausurenSchuelerklausurenRaeumeStunden(final Long Schuelerklausur_ID, final Long KlausurRaumStunde_ID) {
		if (Schuelerklausur_ID == null) { 
			throw new NullPointerException("Schuelerklausur_ID must not be null");
		}
		this.Schuelerklausur_ID = Schuelerklausur_ID;
		if (KlausurRaumStunde_ID == null) { 
			throw new NullPointerException("KlausurRaumStunde_ID must not be null");
		}
		this.KlausurRaumStunde_ID = KlausurRaumStunde_ID;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev9DTOGostKlausurenSchuelerklausurenRaeumeStunden other = (Rev9DTOGostKlausurenSchuelerklausurenRaeumeStunden) obj;
		if (Schuelerklausur_ID == null) {
			if (other.Schuelerklausur_ID != null)
				return false;
		} else if (!Schuelerklausur_ID.equals(other.Schuelerklausur_ID))
			return false;

		if (KlausurRaumStunde_ID == null) {
			if (other.KlausurRaumStunde_ID != null)
				return false;
		} else if (!KlausurRaumStunde_ID.equals(other.KlausurRaumStunde_ID))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Schuelerklausur_ID == null) ? 0 : Schuelerklausur_ID.hashCode());

		result = prime * result + ((KlausurRaumStunde_ID == null) ? 0 : KlausurRaumStunde_ID.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "Rev9DTOGostKlausurenSchuelerklausurenRaeumeStunden(Schuelerklausur_ID=" + this.Schuelerklausur_ID + ", KlausurRaumStunde_ID=" + this.KlausurRaumStunde_ID + ")";
	}

}