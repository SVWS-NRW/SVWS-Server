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
 * Diese Klasse dient als DTO für die Datenbanktabelle Gost_Klausuren_Termine_Jahrgaenge.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(DTOGostKlausurenTermineJahrgaengePK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Gost_Klausuren_Termine_Jahrgaenge")
@NamedQuery(name = "DTOGostKlausurenTermineJahrgaenge.all", query = "SELECT e FROM DTOGostKlausurenTermineJahrgaenge e")
@NamedQuery(name = "DTOGostKlausurenTermineJahrgaenge.termin_id", query = "SELECT e FROM DTOGostKlausurenTermineJahrgaenge e WHERE e.Termin_ID = :value")
@NamedQuery(name = "DTOGostKlausurenTermineJahrgaenge.termin_id.multiple", query = "SELECT e FROM DTOGostKlausurenTermineJahrgaenge e WHERE e.Termin_ID IN :value")
@NamedQuery(name = "DTOGostKlausurenTermineJahrgaenge.abi_jahrgang", query = "SELECT e FROM DTOGostKlausurenTermineJahrgaenge e WHERE e.Abi_Jahrgang = :value")
@NamedQuery(name = "DTOGostKlausurenTermineJahrgaenge.abi_jahrgang.multiple", query = "SELECT e FROM DTOGostKlausurenTermineJahrgaenge e WHERE e.Abi_Jahrgang IN :value")
@NamedQuery(name = "DTOGostKlausurenTermineJahrgaenge.primaryKeyQuery", query = "SELECT e FROM DTOGostKlausurenTermineJahrgaenge e WHERE e.Termin_ID = ?1 AND e.Abi_Jahrgang = ?2")
@NamedQuery(name = "DTOGostKlausurenTermineJahrgaenge.all.migration", query = "SELECT e FROM DTOGostKlausurenTermineJahrgaenge e WHERE e.Termin_ID IS NOT NULL AND e.Abi_Jahrgang IS NOT NULL")
@JsonPropertyOrder({"Termin_ID", "Abi_Jahrgang"})
public final class DTOGostKlausurenTermineJahrgaenge {

	/** Termin_ID des Klausurtermins */
	@Id
	@Column(name = "Termin_ID")
	@JsonProperty
	public long Termin_ID;

	/** Der Abiturjahrgang, der zum Klausurtermin zugelassen werden soll. */
	@Id
	@Column(name = "Abi_Jahrgang")
	@JsonProperty
	public int Abi_Jahrgang;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostKlausurenTermineJahrgaenge ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOGostKlausurenTermineJahrgaenge() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostKlausurenTermineJahrgaenge ohne eine Initialisierung der Attribute.
	 * @param Termin_ID   der Wert für das Attribut Termin_ID
	 * @param Abi_Jahrgang   der Wert für das Attribut Abi_Jahrgang
	 */
	public DTOGostKlausurenTermineJahrgaenge(final long Termin_ID, final int Abi_Jahrgang) {
		this.Termin_ID = Termin_ID;
		this.Abi_Jahrgang = Abi_Jahrgang;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOGostKlausurenTermineJahrgaenge other = (DTOGostKlausurenTermineJahrgaenge) obj;
		if (Termin_ID != other.Termin_ID)
			return false;
		return Abi_Jahrgang == other.Abi_Jahrgang;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(Termin_ID);

		result = prime * result + Integer.hashCode(Abi_Jahrgang);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOGostKlausurenTermineJahrgaenge(Termin_ID=" + this.Termin_ID + ", Abi_Jahrgang=" + this.Abi_Jahrgang + ")";
	}

}
