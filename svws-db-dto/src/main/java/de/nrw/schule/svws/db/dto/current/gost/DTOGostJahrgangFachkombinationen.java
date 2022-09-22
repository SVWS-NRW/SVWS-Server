package de.nrw.schule.svws.db.dto.current.gost;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Gost_Jahrgang_Fachkombinationen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(DTOGostJahrgangFachkombinationenPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Gost_Jahrgang_Fachkombinationen")
@NamedQuery(name="DTOGostJahrgangFachkombinationen.all", query="SELECT e FROM DTOGostJahrgangFachkombinationen e")
@NamedQuery(name="DTOGostJahrgangFachkombinationen.abi_jahrgang", query="SELECT e FROM DTOGostJahrgangFachkombinationen e WHERE e.Abi_Jahrgang = :value")
@NamedQuery(name="DTOGostJahrgangFachkombinationen.abi_jahrgang.multiple", query="SELECT e FROM DTOGostJahrgangFachkombinationen e WHERE e.Abi_Jahrgang IN :value")
@NamedQuery(name="DTOGostJahrgangFachkombinationen.id", query="SELECT e FROM DTOGostJahrgangFachkombinationen e WHERE e.ID = :value")
@NamedQuery(name="DTOGostJahrgangFachkombinationen.id.multiple", query="SELECT e FROM DTOGostJahrgangFachkombinationen e WHERE e.ID IN :value")
@NamedQuery(name="DTOGostJahrgangFachkombinationen.fach1_id", query="SELECT e FROM DTOGostJahrgangFachkombinationen e WHERE e.Fach1_ID = :value")
@NamedQuery(name="DTOGostJahrgangFachkombinationen.fach1_id.multiple", query="SELECT e FROM DTOGostJahrgangFachkombinationen e WHERE e.Fach1_ID IN :value")
@NamedQuery(name="DTOGostJahrgangFachkombinationen.fach2_id", query="SELECT e FROM DTOGostJahrgangFachkombinationen e WHERE e.Fach2_ID = :value")
@NamedQuery(name="DTOGostJahrgangFachkombinationen.fach2_id.multiple", query="SELECT e FROM DTOGostJahrgangFachkombinationen e WHERE e.Fach2_ID IN :value")
@NamedQuery(name="DTOGostJahrgangFachkombinationen.kursart1", query="SELECT e FROM DTOGostJahrgangFachkombinationen e WHERE e.Kursart1 = :value")
@NamedQuery(name="DTOGostJahrgangFachkombinationen.kursart1.multiple", query="SELECT e FROM DTOGostJahrgangFachkombinationen e WHERE e.Kursart1 IN :value")
@NamedQuery(name="DTOGostJahrgangFachkombinationen.kursart2", query="SELECT e FROM DTOGostJahrgangFachkombinationen e WHERE e.Kursart2 = :value")
@NamedQuery(name="DTOGostJahrgangFachkombinationen.kursart2.multiple", query="SELECT e FROM DTOGostJahrgangFachkombinationen e WHERE e.Kursart2 IN :value")
@NamedQuery(name="DTOGostJahrgangFachkombinationen.phase", query="SELECT e FROM DTOGostJahrgangFachkombinationen e WHERE e.Phase = :value")
@NamedQuery(name="DTOGostJahrgangFachkombinationen.phase.multiple", query="SELECT e FROM DTOGostJahrgangFachkombinationen e WHERE e.Phase IN :value")
@NamedQuery(name="DTOGostJahrgangFachkombinationen.typ", query="SELECT e FROM DTOGostJahrgangFachkombinationen e WHERE e.Typ = :value")
@NamedQuery(name="DTOGostJahrgangFachkombinationen.typ.multiple", query="SELECT e FROM DTOGostJahrgangFachkombinationen e WHERE e.Typ IN :value")
@NamedQuery(name="DTOGostJahrgangFachkombinationen.primaryKeyQuery", query="SELECT e FROM DTOGostJahrgangFachkombinationen e WHERE e.Abi_Jahrgang = ?1 AND e.ID = ?2")
@NamedQuery(name="DTOGostJahrgangFachkombinationen.all.migration", query="SELECT e FROM DTOGostJahrgangFachkombinationen e WHERE e.Abi_Jahrgang IS NOT NULL AND e.ID IS NOT NULL")
@JsonPropertyOrder({"Abi_Jahrgang","ID","Fach1_ID","Fach2_ID","Kursart1","Kursart2","Phase","Typ"})
public class DTOGostJahrgangFachkombinationen {

	/** Gymnasiale Oberstufe - Jahrgangsdaten: Schuljahr, in welchem der Jahrgang das Abitur macht */
	@Id
	@Column(name = "Abi_Jahrgang")
	@JsonProperty
	public Integer Abi_Jahrgang;

	/** Gymnasiale Oberstufe - Jahrgangsdaten: ID des Datensatzes der nicht möglichen Fächerkombination */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public String ID;

	/** Gymnasiale Oberstufe - Jahrgangsdaten: Die ID des ersten Faches der nicht möglichen Fächerkombination / eines Fächerprofils */
	@Column(name = "Fach1_ID")
	@JsonProperty
	public Long Fach1_ID;

	/** Gymnasiale Oberstufe - Jahrgangsdaten: Die ID des zweiten Faches der nicht möglichen Fächerkombination / eines Fächerprofils */
	@Column(name = "Fach2_ID")
	@JsonProperty
	public Long Fach2_ID;

	/** Gymnasiale Oberstufe - Jahrgangsdaten: Die Kursart des ersten Faches der nicht möglichen Fächerkombination / eines Fächerprofils */
	@Column(name = "Kursart1")
	@JsonProperty
	public String Kursart1;

	/** Gymnasiale Oberstufe - Jahrgangsdaten: Die Kursart des zweiten Faches der nicht möglichen Fächerkombination / eines Fächerprofils */
	@Column(name = "Kursart2")
	@JsonProperty
	public String Kursart2;

	/** Gymnasiale Oberstufe - Jahrgangsdaten: Die Gültigkeit der nicht möglichen Fächerkombination EF.1 bis Q2.2 oder Q1.1 bis Q2.2 */
	@Column(name = "Phase")
	@JsonProperty
	public String Phase;

	/** Gymnasiale Oberstufe - Jahrgangsdaten: Gibt an, ob es sich um eine nicht mögliche Fächerkombination (0) oder ein Fächerprofil handelt (1) */
	@Column(name = "Typ")
	@JsonProperty
	public Integer Typ;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostJahrgangFachkombinationen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOGostJahrgangFachkombinationen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostJahrgangFachkombinationen ohne eine Initialisierung der Attribute.
	 * @param Abi_Jahrgang   der Wert für das Attribut Abi_Jahrgang
	 * @param ID   der Wert für das Attribut ID
	 * @param Fach1_ID   der Wert für das Attribut Fach1_ID
	 * @param Fach2_ID   der Wert für das Attribut Fach2_ID
	 * @param Phase   der Wert für das Attribut Phase
	 * @param Typ   der Wert für das Attribut Typ
	 */
	public DTOGostJahrgangFachkombinationen(final Integer Abi_Jahrgang, final String ID, final Long Fach1_ID, final Long Fach2_ID, final String Phase, final Integer Typ) {
		if (Abi_Jahrgang == null) { 
			throw new NullPointerException("Abi_Jahrgang must not be null");
		}
		this.Abi_Jahrgang = Abi_Jahrgang;
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Fach1_ID == null) { 
			throw new NullPointerException("Fach1_ID must not be null");
		}
		this.Fach1_ID = Fach1_ID;
		if (Fach2_ID == null) { 
			throw new NullPointerException("Fach2_ID must not be null");
		}
		this.Fach2_ID = Fach2_ID;
		if (Phase == null) { 
			throw new NullPointerException("Phase must not be null");
		}
		this.Phase = Phase;
		if (Typ == null) { 
			throw new NullPointerException("Typ must not be null");
		}
		this.Typ = Typ;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOGostJahrgangFachkombinationen other = (DTOGostJahrgangFachkombinationen) obj;
		if (Abi_Jahrgang == null) {
			if (other.Abi_Jahrgang != null)
				return false;
		} else if (!Abi_Jahrgang.equals(other.Abi_Jahrgang))
			return false;

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
		result = prime * result + ((Abi_Jahrgang == null) ? 0 : Abi_Jahrgang.hashCode());

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
		return "DTOGostJahrgangFachkombinationen(Abi_Jahrgang=" + this.Abi_Jahrgang + ", ID=" + this.ID + ", Fach1_ID=" + this.Fach1_ID + ", Fach2_ID=" + this.Fach2_ID + ", Kursart1=" + this.Kursart1 + ", Kursart2=" + this.Kursart2 + ", Phase=" + this.Phase + ", Typ=" + this.Typ + ")";
	}

}