package de.svws_nrw.db.dto.current.gost;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Gost_Jahrgang_Fachwahlen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(DTOGostJahrgangFachbelegungenPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Gost_Jahrgang_Fachwahlen")
@NamedQuery(name = "DTOGostJahrgangFachbelegungen.all", query = "SELECT e FROM DTOGostJahrgangFachbelegungen e")
@NamedQuery(name = "DTOGostJahrgangFachbelegungen.abi_jahrgang", query = "SELECT e FROM DTOGostJahrgangFachbelegungen e WHERE e.Abi_Jahrgang = :value")
@NamedQuery(name = "DTOGostJahrgangFachbelegungen.abi_jahrgang.multiple", query = "SELECT e FROM DTOGostJahrgangFachbelegungen e WHERE e.Abi_Jahrgang IN :value")
@NamedQuery(name = "DTOGostJahrgangFachbelegungen.fach_id", query = "SELECT e FROM DTOGostJahrgangFachbelegungen e WHERE e.Fach_ID = :value")
@NamedQuery(name = "DTOGostJahrgangFachbelegungen.fach_id.multiple", query = "SELECT e FROM DTOGostJahrgangFachbelegungen e WHERE e.Fach_ID IN :value")
@NamedQuery(name = "DTOGostJahrgangFachbelegungen.ef1_kursart", query = "SELECT e FROM DTOGostJahrgangFachbelegungen e WHERE e.EF1_Kursart = :value")
@NamedQuery(name = "DTOGostJahrgangFachbelegungen.ef1_kursart.multiple", query = "SELECT e FROM DTOGostJahrgangFachbelegungen e WHERE e.EF1_Kursart IN :value")
@NamedQuery(name = "DTOGostJahrgangFachbelegungen.ef2_kursart", query = "SELECT e FROM DTOGostJahrgangFachbelegungen e WHERE e.EF2_Kursart = :value")
@NamedQuery(name = "DTOGostJahrgangFachbelegungen.ef2_kursart.multiple", query = "SELECT e FROM DTOGostJahrgangFachbelegungen e WHERE e.EF2_Kursart IN :value")
@NamedQuery(name = "DTOGostJahrgangFachbelegungen.q11_kursart", query = "SELECT e FROM DTOGostJahrgangFachbelegungen e WHERE e.Q11_Kursart = :value")
@NamedQuery(name = "DTOGostJahrgangFachbelegungen.q11_kursart.multiple", query = "SELECT e FROM DTOGostJahrgangFachbelegungen e WHERE e.Q11_Kursart IN :value")
@NamedQuery(name = "DTOGostJahrgangFachbelegungen.q12_kursart", query = "SELECT e FROM DTOGostJahrgangFachbelegungen e WHERE e.Q12_Kursart = :value")
@NamedQuery(name = "DTOGostJahrgangFachbelegungen.q12_kursart.multiple", query = "SELECT e FROM DTOGostJahrgangFachbelegungen e WHERE e.Q12_Kursart IN :value")
@NamedQuery(name = "DTOGostJahrgangFachbelegungen.q21_kursart", query = "SELECT e FROM DTOGostJahrgangFachbelegungen e WHERE e.Q21_Kursart = :value")
@NamedQuery(name = "DTOGostJahrgangFachbelegungen.q21_kursart.multiple", query = "SELECT e FROM DTOGostJahrgangFachbelegungen e WHERE e.Q21_Kursart IN :value")
@NamedQuery(name = "DTOGostJahrgangFachbelegungen.q22_kursart", query = "SELECT e FROM DTOGostJahrgangFachbelegungen e WHERE e.Q22_Kursart = :value")
@NamedQuery(name = "DTOGostJahrgangFachbelegungen.q22_kursart.multiple", query = "SELECT e FROM DTOGostJahrgangFachbelegungen e WHERE e.Q22_Kursart IN :value")
@NamedQuery(name = "DTOGostJahrgangFachbelegungen.abiturfach", query = "SELECT e FROM DTOGostJahrgangFachbelegungen e WHERE e.AbiturFach = :value")
@NamedQuery(name = "DTOGostJahrgangFachbelegungen.abiturfach.multiple", query = "SELECT e FROM DTOGostJahrgangFachbelegungen e WHERE e.AbiturFach IN :value")
@NamedQuery(name = "DTOGostJahrgangFachbelegungen.bemerkungen", query = "SELECT e FROM DTOGostJahrgangFachbelegungen e WHERE e.Bemerkungen = :value")
@NamedQuery(name = "DTOGostJahrgangFachbelegungen.bemerkungen.multiple", query = "SELECT e FROM DTOGostJahrgangFachbelegungen e WHERE e.Bemerkungen IN :value")
@NamedQuery(name = "DTOGostJahrgangFachbelegungen.primaryKeyQuery", query = "SELECT e FROM DTOGostJahrgangFachbelegungen e WHERE e.Abi_Jahrgang = ?1 AND e.Fach_ID = ?2")
@NamedQuery(name = "DTOGostJahrgangFachbelegungen.all.migration", query = "SELECT e FROM DTOGostJahrgangFachbelegungen e WHERE e.Abi_Jahrgang IS NOT NULL AND e.Fach_ID IS NOT NULL")
@JsonPropertyOrder({"Abi_Jahrgang", "Fach_ID", "EF1_Kursart", "EF2_Kursart", "Q11_Kursart", "Q12_Kursart", "Q21_Kursart", "Q22_Kursart", "AbiturFach", "Bemerkungen"})
public final class DTOGostJahrgangFachbelegungen {

	/** Schuljahr, in welchem der Jahrgang das Abitur macht */
	@Id
	@Column(name = "Abi_Jahrgang")
	@JsonProperty
	public int Abi_Jahrgang;

	/** Gymnasiale Oberstufe - Jahrgangsdaten - Fachwahlen: ID des Faches in der Fächertabelle */
	@Id
	@Column(name = "Fach_ID")
	@JsonProperty
	public long Fach_ID;

	/** Gymnasiale Oberstufe - Jahrgangsdaten - Fachwahlen: Kursart des belegten Faches in EF.1 */
	@Column(name = "EF1_Kursart")
	@JsonProperty
	public String EF1_Kursart;

	/** Gymnasiale Oberstufe - Jahrgangsdaten - Fachwahlen: Kursart des belegten Faches in EF.2 */
	@Column(name = "EF2_Kursart")
	@JsonProperty
	public String EF2_Kursart;

	/** Gymnasiale Oberstufe - Jahrgangsdaten - Fachwahlen: Kursart des belegten Faches in Q1.1 */
	@Column(name = "Q11_Kursart")
	@JsonProperty
	public String Q11_Kursart;

	/** Gymnasiale Oberstufe - Jahrgangsdaten - Fachwahlen: Kursart des belegten Faches in Q1.2 */
	@Column(name = "Q12_Kursart")
	@JsonProperty
	public String Q12_Kursart;

	/** Gymnasiale Oberstufe - Jahrgangsdaten - Fachwahlen: Kursart des belegten Faches in Q2.1 */
	@Column(name = "Q21_Kursart")
	@JsonProperty
	public String Q21_Kursart;

	/** Gymnasiale Oberstufe - Jahrgangsdaten - Fachwahlen: Kursart des belegten Faches in Q2.2 */
	@Column(name = "Q22_Kursart")
	@JsonProperty
	public String Q22_Kursart;

	/** Gymnasiale Oberstufe - Jahrgangsdaten - Fachwahlen: Abiturfach 1 bis 4 oder null */
	@Column(name = "AbiturFach")
	@JsonProperty
	public Integer AbiturFach;

	/** Gymnasiale Oberstufe - Jahrgangsdaten - Fachwahlen: Bemerkungen zum belegten Fach */
	@Column(name = "Bemerkungen")
	@JsonProperty
	public String Bemerkungen;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostJahrgangFachbelegungen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOGostJahrgangFachbelegungen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostJahrgangFachbelegungen ohne eine Initialisierung der Attribute.
	 * @param Abi_Jahrgang   der Wert für das Attribut Abi_Jahrgang
	 * @param Fach_ID   der Wert für das Attribut Fach_ID
	 */
	public DTOGostJahrgangFachbelegungen(final int Abi_Jahrgang, final long Fach_ID) {
		this.Abi_Jahrgang = Abi_Jahrgang;
		this.Fach_ID = Fach_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOGostJahrgangFachbelegungen other = (DTOGostJahrgangFachbelegungen) obj;
		if (Abi_Jahrgang != other.Abi_Jahrgang)
			return false;
		return Fach_ID == other.Fach_ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Integer.hashCode(Abi_Jahrgang);

		result = prime * result + Long.hashCode(Fach_ID);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOGostJahrgangFachbelegungen(Abi_Jahrgang=" + this.Abi_Jahrgang + ", Fach_ID=" + this.Fach_ID + ", EF1_Kursart=" + this.EF1_Kursart + ", EF2_Kursart=" + this.EF2_Kursart + ", Q11_Kursart=" + this.Q11_Kursart + ", Q12_Kursart=" + this.Q12_Kursart + ", Q21_Kursart=" + this.Q21_Kursart + ", Q22_Kursart=" + this.Q22_Kursart + ", AbiturFach=" + this.AbiturFach + ", Bemerkungen=" + this.Bemerkungen + ")";
	}

}
