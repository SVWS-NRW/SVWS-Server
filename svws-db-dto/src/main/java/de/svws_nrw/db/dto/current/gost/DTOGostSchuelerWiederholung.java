package de.svws_nrw.db.dto.current.gost;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.gost.GOStHalbjahrConverter;

import de.svws_nrw.core.types.gost.GostHalbjahr;


import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.svws_nrw.csv.converter.current.gost.GOStHalbjahrConverterSerializer;
import de.svws_nrw.csv.converter.current.gost.GOStHalbjahrConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Gost_Schueler_Wiederholung.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Gost_Schueler_Wiederholung")
@JsonPropertyOrder({"Schueler_ID", "Abi_Jahrgang", "Halbjahr", "GKL_EF_AF1_Klausurvorgabe_ID", "GKL_EF_AF2_Klausurvorgabe_ID", "GKL_EF_AF3_Klausurvorgabe_ID", "GKL_Q_AF1_Klausurvorgabe_ID", "GKL_Q_AF2_Klausurvorgabe_ID", "GKL_Q_AF3_Klausurvorgabe_ID", "ZulassungSimulation"})
public final class DTOGostSchuelerWiederholung {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOGostSchuelerWiederholung e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOGostSchuelerWiederholung e WHERE e.Schueler_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOGostSchuelerWiederholung e WHERE e.Schueler_ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOGostSchuelerWiederholung e WHERE e.Schueler_ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schueler_ID */
	public static final String QUERY_BY_SCHUELER_ID = "SELECT e FROM DTOGostSchuelerWiederholung e WHERE e.Schueler_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schueler_ID */
	public static final String QUERY_LIST_BY_SCHUELER_ID = "SELECT e FROM DTOGostSchuelerWiederholung e WHERE e.Schueler_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Abi_Jahrgang */
	public static final String QUERY_BY_ABI_JAHRGANG = "SELECT e FROM DTOGostSchuelerWiederholung e WHERE e.Abi_Jahrgang = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Abi_Jahrgang */
	public static final String QUERY_LIST_BY_ABI_JAHRGANG = "SELECT e FROM DTOGostSchuelerWiederholung e WHERE e.Abi_Jahrgang IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Halbjahr */
	public static final String QUERY_BY_HALBJAHR = "SELECT e FROM DTOGostSchuelerWiederholung e WHERE e.Halbjahr = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Halbjahr */
	public static final String QUERY_LIST_BY_HALBJAHR = "SELECT e FROM DTOGostSchuelerWiederholung e WHERE e.Halbjahr IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes GKL_EF_AF1_Klausurvorgabe_ID */
	public static final String QUERY_BY_GKL_EF_AF1_KLAUSURVORGABE_ID = "SELECT e FROM DTOGostSchuelerWiederholung e WHERE e.GKL_EF_AF1_Klausurvorgabe_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes GKL_EF_AF1_Klausurvorgabe_ID */
	public static final String QUERY_LIST_BY_GKL_EF_AF1_KLAUSURVORGABE_ID = "SELECT e FROM DTOGostSchuelerWiederholung e WHERE e.GKL_EF_AF1_Klausurvorgabe_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes GKL_EF_AF2_Klausurvorgabe_ID */
	public static final String QUERY_BY_GKL_EF_AF2_KLAUSURVORGABE_ID = "SELECT e FROM DTOGostSchuelerWiederholung e WHERE e.GKL_EF_AF2_Klausurvorgabe_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes GKL_EF_AF2_Klausurvorgabe_ID */
	public static final String QUERY_LIST_BY_GKL_EF_AF2_KLAUSURVORGABE_ID = "SELECT e FROM DTOGostSchuelerWiederholung e WHERE e.GKL_EF_AF2_Klausurvorgabe_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes GKL_EF_AF3_Klausurvorgabe_ID */
	public static final String QUERY_BY_GKL_EF_AF3_KLAUSURVORGABE_ID = "SELECT e FROM DTOGostSchuelerWiederholung e WHERE e.GKL_EF_AF3_Klausurvorgabe_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes GKL_EF_AF3_Klausurvorgabe_ID */
	public static final String QUERY_LIST_BY_GKL_EF_AF3_KLAUSURVORGABE_ID = "SELECT e FROM DTOGostSchuelerWiederholung e WHERE e.GKL_EF_AF3_Klausurvorgabe_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes GKL_Q_AF1_Klausurvorgabe_ID */
	public static final String QUERY_BY_GKL_Q_AF1_KLAUSURVORGABE_ID = "SELECT e FROM DTOGostSchuelerWiederholung e WHERE e.GKL_Q_AF1_Klausurvorgabe_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes GKL_Q_AF1_Klausurvorgabe_ID */
	public static final String QUERY_LIST_BY_GKL_Q_AF1_KLAUSURVORGABE_ID = "SELECT e FROM DTOGostSchuelerWiederholung e WHERE e.GKL_Q_AF1_Klausurvorgabe_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes GKL_Q_AF2_Klausurvorgabe_ID */
	public static final String QUERY_BY_GKL_Q_AF2_KLAUSURVORGABE_ID = "SELECT e FROM DTOGostSchuelerWiederholung e WHERE e.GKL_Q_AF2_Klausurvorgabe_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes GKL_Q_AF2_Klausurvorgabe_ID */
	public static final String QUERY_LIST_BY_GKL_Q_AF2_KLAUSURVORGABE_ID = "SELECT e FROM DTOGostSchuelerWiederholung e WHERE e.GKL_Q_AF2_Klausurvorgabe_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes GKL_Q_AF3_Klausurvorgabe_ID */
	public static final String QUERY_BY_GKL_Q_AF3_KLAUSURVORGABE_ID = "SELECT e FROM DTOGostSchuelerWiederholung e WHERE e.GKL_Q_AF3_Klausurvorgabe_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes GKL_Q_AF3_Klausurvorgabe_ID */
	public static final String QUERY_LIST_BY_GKL_Q_AF3_KLAUSURVORGABE_ID = "SELECT e FROM DTOGostSchuelerWiederholung e WHERE e.GKL_Q_AF3_Klausurvorgabe_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ZulassungSimulation */
	public static final String QUERY_BY_ZULASSUNGSIMULATION = "SELECT e FROM DTOGostSchuelerWiederholung e WHERE e.ZulassungSimulation = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ZulassungSimulation */
	public static final String QUERY_LIST_BY_ZULASSUNGSIMULATION = "SELECT e FROM DTOGostSchuelerWiederholung e WHERE e.ZulassungSimulation IN ?1";

	/** Gymnasiale Oberstufe - Wiederholung: Die ID des Schülers in der Schülertabelle */
	@Id
	@Column(name = "Schueler_ID")
	@JsonProperty
	public long Schueler_ID;

	/** Gymnasiale Oberstufe - Wiederholung: Das Abiturjahr des Jahrgangs, in dem der Schüler wiederholt */
	@Column(name = "Abi_Jahrgang")
	@JsonProperty
	public int Abi_Jahrgang;

	/** Gymnasiale Oberstufe - Wiederholung: Das Halbjahr im Abiturjahrgang der Wiederholung, mit dem der Schüler dort einsteigt (0=EF.1, 1=EF.2, 2=Q1.1, 3=Q1.2, 4=Q2.1, 5=Q2.2) */
	@Column(name = "Halbjahr")
	@JsonProperty
	@Convert(converter = GOStHalbjahrConverter.class)
	@JsonSerialize(using = GOStHalbjahrConverterSerializer.class)
	@JsonDeserialize(using = GOStHalbjahrConverterDeserializer.class)
	public GostHalbjahr Halbjahr;

	/** Gymnasiale Oberstufe - Wiederholung: Gleichwertiger Komplexer Leistungsnachweis: ID der Klausurvorgabe der EF in Aufgabenfeld 1 */
	@Column(name = "GKL_EF_AF1_Klausurvorgabe_ID")
	@JsonProperty
	public Long GKL_EF_AF1_Klausurvorgabe_ID;

	/** Gymnasiale Oberstufe - Wiederholung: Gleichwertiger Komplexer Leistungsnachweis: ID der Klausurvorgabe der EF in Aufgabenfeld 2 */
	@Column(name = "GKL_EF_AF2_Klausurvorgabe_ID")
	@JsonProperty
	public Long GKL_EF_AF2_Klausurvorgabe_ID;

	/** Gymnasiale Oberstufe - Wiederholung: Gleichwertiger Komplexer Leistungsnachweis: ID der Klausurvorgabe der EF in Aufgabenfeld 3 */
	@Column(name = "GKL_EF_AF3_Klausurvorgabe_ID")
	@JsonProperty
	public Long GKL_EF_AF3_Klausurvorgabe_ID;

	/** Gymnasiale Oberstufe - Wiederholung: Gleichwertiger Komplexer Leistungsnachweis: ID der Klausurvorgabe der Q-Phase in Aufgabenfeld 1 */
	@Column(name = "GKL_Q_AF1_Klausurvorgabe_ID")
	@JsonProperty
	public Long GKL_Q_AF1_Klausurvorgabe_ID;

	/** Gymnasiale Oberstufe - Wiederholung: Gleichwertiger Komplexer Leistungsnachweis: ID der Klausurvorgabe der Q-Phase in Aufgabenfeld 2 */
	@Column(name = "GKL_Q_AF2_Klausurvorgabe_ID")
	@JsonProperty
	public Long GKL_Q_AF2_Klausurvorgabe_ID;

	/** Gymnasiale Oberstufe - Wiederholung: Gleichwertiger Komplexer Leistungsnachweis: ID der Klausurvorgabe der Q-Phase in Aufgabenfeld 3 */
	@Column(name = "GKL_Q_AF3_Klausurvorgabe_ID")
	@JsonProperty
	public Long GKL_Q_AF3_Klausurvorgabe_ID;

	/** Gymnasiale Oberstufe - Wiederholung: Die Eingaben der Simulation der Abiturzulassung als JSON-Text */
	@Column(name = "ZulassungSimulation")
	@JsonProperty
	public String ZulassungSimulation;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostSchuelerWiederholung ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOGostSchuelerWiederholung() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostSchuelerWiederholung ohne eine Initialisierung der Attribute.
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 * @param Abi_Jahrgang   der Wert für das Attribut Abi_Jahrgang
	 * @param Halbjahr   der Wert für das Attribut Halbjahr
	 */
	public DTOGostSchuelerWiederholung(final long Schueler_ID, final int Abi_Jahrgang, final GostHalbjahr Halbjahr) {
		this.Schueler_ID = Schueler_ID;
		this.Abi_Jahrgang = Abi_Jahrgang;
		this.Halbjahr = Halbjahr;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		DTOGostSchuelerWiederholung other = (DTOGostSchuelerWiederholung) obj;
		return Schueler_ID == other.Schueler_ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(Schueler_ID);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOGostSchuelerWiederholung(Schueler_ID=" + this.Schueler_ID + ", Abi_Jahrgang=" + this.Abi_Jahrgang + ", Halbjahr=" + this.Halbjahr + ", GKL_EF_AF1_Klausurvorgabe_ID=" + this.GKL_EF_AF1_Klausurvorgabe_ID + ", GKL_EF_AF2_Klausurvorgabe_ID=" + this.GKL_EF_AF2_Klausurvorgabe_ID + ", GKL_EF_AF3_Klausurvorgabe_ID=" + this.GKL_EF_AF3_Klausurvorgabe_ID + ", GKL_Q_AF1_Klausurvorgabe_ID=" + this.GKL_Q_AF1_Klausurvorgabe_ID + ", GKL_Q_AF2_Klausurvorgabe_ID=" + this.GKL_Q_AF2_Klausurvorgabe_ID + ", GKL_Q_AF3_Klausurvorgabe_ID=" + this.GKL_Q_AF3_Klausurvorgabe_ID + ", ZulassungSimulation=" + this.ZulassungSimulation + ")";
	}

}
