package de.svws_nrw.db.dto.current.gost;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
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
@JsonPropertyOrder({"Abi_Jahrgang", "Fach_ID", "EF1_Kursart", "EF2_Kursart", "Q11_Kursart", "Q12_Kursart", "Q21_Kursart", "Q22_Kursart", "AbiturFach", "Bemerkungen"})
public final class DTOGostJahrgangFachbelegungen {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOGostJahrgangFachbelegungen e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOGostJahrgangFachbelegungen e WHERE e.Abi_Jahrgang = ?1 AND e.Fach_ID = ?2";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOGostJahrgangFachbelegungen e WHERE e.Abi_Jahrgang IS NOT NULL AND e.Fach_ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Abi_Jahrgang */
	public static final String QUERY_BY_ABI_JAHRGANG = "SELECT e FROM DTOGostJahrgangFachbelegungen e WHERE e.Abi_Jahrgang = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Abi_Jahrgang */
	public static final String QUERY_LIST_BY_ABI_JAHRGANG = "SELECT e FROM DTOGostJahrgangFachbelegungen e WHERE e.Abi_Jahrgang IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Fach_ID */
	public static final String QUERY_BY_FACH_ID = "SELECT e FROM DTOGostJahrgangFachbelegungen e WHERE e.Fach_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Fach_ID */
	public static final String QUERY_LIST_BY_FACH_ID = "SELECT e FROM DTOGostJahrgangFachbelegungen e WHERE e.Fach_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes EF1_Kursart */
	public static final String QUERY_BY_EF1_KURSART = "SELECT e FROM DTOGostJahrgangFachbelegungen e WHERE e.EF1_Kursart = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes EF1_Kursart */
	public static final String QUERY_LIST_BY_EF1_KURSART = "SELECT e FROM DTOGostJahrgangFachbelegungen e WHERE e.EF1_Kursart IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes EF2_Kursart */
	public static final String QUERY_BY_EF2_KURSART = "SELECT e FROM DTOGostJahrgangFachbelegungen e WHERE e.EF2_Kursart = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes EF2_Kursart */
	public static final String QUERY_LIST_BY_EF2_KURSART = "SELECT e FROM DTOGostJahrgangFachbelegungen e WHERE e.EF2_Kursart IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Q11_Kursart */
	public static final String QUERY_BY_Q11_KURSART = "SELECT e FROM DTOGostJahrgangFachbelegungen e WHERE e.Q11_Kursart = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Q11_Kursart */
	public static final String QUERY_LIST_BY_Q11_KURSART = "SELECT e FROM DTOGostJahrgangFachbelegungen e WHERE e.Q11_Kursart IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Q12_Kursart */
	public static final String QUERY_BY_Q12_KURSART = "SELECT e FROM DTOGostJahrgangFachbelegungen e WHERE e.Q12_Kursart = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Q12_Kursart */
	public static final String QUERY_LIST_BY_Q12_KURSART = "SELECT e FROM DTOGostJahrgangFachbelegungen e WHERE e.Q12_Kursart IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Q21_Kursart */
	public static final String QUERY_BY_Q21_KURSART = "SELECT e FROM DTOGostJahrgangFachbelegungen e WHERE e.Q21_Kursart = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Q21_Kursart */
	public static final String QUERY_LIST_BY_Q21_KURSART = "SELECT e FROM DTOGostJahrgangFachbelegungen e WHERE e.Q21_Kursart IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Q22_Kursart */
	public static final String QUERY_BY_Q22_KURSART = "SELECT e FROM DTOGostJahrgangFachbelegungen e WHERE e.Q22_Kursart = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Q22_Kursart */
	public static final String QUERY_LIST_BY_Q22_KURSART = "SELECT e FROM DTOGostJahrgangFachbelegungen e WHERE e.Q22_Kursart IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AbiturFach */
	public static final String QUERY_BY_ABITURFACH = "SELECT e FROM DTOGostJahrgangFachbelegungen e WHERE e.AbiturFach = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AbiturFach */
	public static final String QUERY_LIST_BY_ABITURFACH = "SELECT e FROM DTOGostJahrgangFachbelegungen e WHERE e.AbiturFach IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Bemerkungen */
	public static final String QUERY_BY_BEMERKUNGEN = "SELECT e FROM DTOGostJahrgangFachbelegungen e WHERE e.Bemerkungen = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Bemerkungen */
	public static final String QUERY_LIST_BY_BEMERKUNGEN = "SELECT e FROM DTOGostJahrgangFachbelegungen e WHERE e.Bemerkungen IN ?1";

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
