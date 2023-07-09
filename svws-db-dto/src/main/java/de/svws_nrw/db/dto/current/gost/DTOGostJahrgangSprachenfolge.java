package de.svws_nrw.db.dto.current.gost;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Gost_Jahrgang_Sprachenfolge.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Gost_Jahrgang_Sprachenfolge")
@NamedQuery(name = "DTOGostJahrgangSprachenfolge.all", query = "SELECT e FROM DTOGostJahrgangSprachenfolge e")
@NamedQuery(name = "DTOGostJahrgangSprachenfolge.abi_jahrgang", query = "SELECT e FROM DTOGostJahrgangSprachenfolge e WHERE e.Abi_Jahrgang = :value")
@NamedQuery(name = "DTOGostJahrgangSprachenfolge.abi_jahrgang.multiple", query = "SELECT e FROM DTOGostJahrgangSprachenfolge e WHERE e.Abi_Jahrgang IN :value")
@NamedQuery(name = "DTOGostJahrgangSprachenfolge.sprache", query = "SELECT e FROM DTOGostJahrgangSprachenfolge e WHERE e.Sprache = :value")
@NamedQuery(name = "DTOGostJahrgangSprachenfolge.sprache.multiple", query = "SELECT e FROM DTOGostJahrgangSprachenfolge e WHERE e.Sprache IN :value")
@NamedQuery(name = "DTOGostJahrgangSprachenfolge.reihenfolgenr", query = "SELECT e FROM DTOGostJahrgangSprachenfolge e WHERE e.ReihenfolgeNr = :value")
@NamedQuery(name = "DTOGostJahrgangSprachenfolge.reihenfolgenr.multiple", query = "SELECT e FROM DTOGostJahrgangSprachenfolge e WHERE e.ReihenfolgeNr IN :value")
@NamedQuery(name = "DTOGostJahrgangSprachenfolge.asdjahrgangvon", query = "SELECT e FROM DTOGostJahrgangSprachenfolge e WHERE e.ASDJahrgangVon = :value")
@NamedQuery(name = "DTOGostJahrgangSprachenfolge.asdjahrgangvon.multiple", query = "SELECT e FROM DTOGostJahrgangSprachenfolge e WHERE e.ASDJahrgangVon IN :value")
@NamedQuery(name = "DTOGostJahrgangSprachenfolge.primaryKeyQuery", query = "SELECT e FROM DTOGostJahrgangSprachenfolge e WHERE e.Abi_Jahrgang = ?1")
@NamedQuery(name = "DTOGostJahrgangSprachenfolge.all.migration", query = "SELECT e FROM DTOGostJahrgangSprachenfolge e WHERE e.Abi_Jahrgang IS NOT NULL")
@JsonPropertyOrder({"Abi_Jahrgang", "Sprache", "ReihenfolgeNr", "ASDJahrgangVon"})
public final class DTOGostJahrgangSprachenfolge {

	/** Gymnasiale Oberstufe - Jahrgangsdaten - Sprachenfolge: Schuljahr, in welchem der Jahrgang das Abitur macht */
	@Id
	@Column(name = "Abi_Jahrgang")
	@JsonProperty
	public int Abi_Jahrgang;

	/** Gymnasiale Oberstufe - Jahrgangsdaten - Sprachenfolge: Atomares Sprachkürzel des Sprach-Faches */
	@Column(name = "Sprache")
	@JsonProperty
	public String Sprache;

	/** Gymnasiale Oberstufe - Jahrgangsdaten - Sprachenfolge: Reihenfolge Nummer des Sprachenfolgeeintrags */
	@Column(name = "ReihenfolgeNr")
	@JsonProperty
	public Integer ReihenfolgeNr;

	/** Gymnasiale Oberstufe - Jahrgangsdaten - Sprachenfolge: ASD-Jahrgang des Beginns des Sprachenfolgeeintrags */
	@Column(name = "ASDJahrgangVon")
	@JsonProperty
	public String ASDJahrgangVon;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostJahrgangSprachenfolge ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOGostJahrgangSprachenfolge() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostJahrgangSprachenfolge ohne eine Initialisierung der Attribute.
	 * @param Abi_Jahrgang   der Wert für das Attribut Abi_Jahrgang
	 * @param Sprache   der Wert für das Attribut Sprache
	 */
	public DTOGostJahrgangSprachenfolge(final int Abi_Jahrgang, final String Sprache) {
		this.Abi_Jahrgang = Abi_Jahrgang;
		if (Sprache == null) {
			throw new NullPointerException("Sprache must not be null");
		}
		this.Sprache = Sprache;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOGostJahrgangSprachenfolge other = (DTOGostJahrgangSprachenfolge) obj;
		return Abi_Jahrgang == other.Abi_Jahrgang;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		return "DTOGostJahrgangSprachenfolge(Abi_Jahrgang=" + this.Abi_Jahrgang + ", Sprache=" + this.Sprache + ", ReihenfolgeNr=" + this.ReihenfolgeNr + ", ASDJahrgangVon=" + this.ASDJahrgangVon + ")";
	}

}
