package de.svws_nrw.db.dto.current.schild.schule;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.Boolean01Converter;


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
import de.svws_nrw.csv.converter.current.Boolean01ConverterSerializer;
import de.svws_nrw.csv.converter.current.Boolean01ConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Schulformen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Schulformen")
@JsonPropertyOrder({"ID", "Kuerzel", "Nummer", "Bezeichnung", "HatGymOb", "gueltigVon", "gueltigBis"})
public final class DTOAlleSchulformen {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOAlleSchulformen e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOAlleSchulformen e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOAlleSchulformen e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOAlleSchulformen e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOAlleSchulformen e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOAlleSchulformen e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Kuerzel */
	public static final String QUERY_BY_KUERZEL = "SELECT e FROM DTOAlleSchulformen e WHERE e.Kuerzel = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Kuerzel */
	public static final String QUERY_LIST_BY_KUERZEL = "SELECT e FROM DTOAlleSchulformen e WHERE e.Kuerzel IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Nummer */
	public static final String QUERY_BY_NUMMER = "SELECT e FROM DTOAlleSchulformen e WHERE e.Nummer = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Nummer */
	public static final String QUERY_LIST_BY_NUMMER = "SELECT e FROM DTOAlleSchulformen e WHERE e.Nummer IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Bezeichnung */
	public static final String QUERY_BY_BEZEICHNUNG = "SELECT e FROM DTOAlleSchulformen e WHERE e.Bezeichnung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Bezeichnung */
	public static final String QUERY_LIST_BY_BEZEICHNUNG = "SELECT e FROM DTOAlleSchulformen e WHERE e.Bezeichnung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes HatGymOb */
	public static final String QUERY_BY_HATGYMOB = "SELECT e FROM DTOAlleSchulformen e WHERE e.HatGymOb = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes HatGymOb */
	public static final String QUERY_LIST_BY_HATGYMOB = "SELECT e FROM DTOAlleSchulformen e WHERE e.HatGymOb IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes gueltigVon */
	public static final String QUERY_BY_GUELTIGVON = "SELECT e FROM DTOAlleSchulformen e WHERE e.gueltigVon = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes gueltigVon */
	public static final String QUERY_LIST_BY_GUELTIGVON = "SELECT e FROM DTOAlleSchulformen e WHERE e.gueltigVon IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes gueltigBis */
	public static final String QUERY_BY_GUELTIGBIS = "SELECT e FROM DTOAlleSchulformen e WHERE e.gueltigBis = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes gueltigBis */
	public static final String QUERY_LIST_BY_GUELTIGBIS = "SELECT e FROM DTOAlleSchulformen e WHERE e.gueltigBis IN ?1";

	/** ID der Schulform */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Kürzel der Schulform */
	@Column(name = "Kuerzel")
	@JsonProperty
	public String Kuerzel;

	/** Nummer der Schulform für die amtliche Schulstatistik */
	@Column(name = "Nummer")
	@JsonProperty
	public String Nummer;

	/** Bezeichnung der Schulform */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Gibt an, ob eine Schule der Schulform eine gymnasiale Oberstufe haben kann (1) oder nicht (0) */
	@Column(name = "HatGymOb")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean HatGymOb;

	/** Gibt an, ab welchem Schuljahr die Schulform gültig ist */
	@Column(name = "gueltigVon")
	@JsonProperty
	public Integer gueltigVon;

	/** Gibt an, bis zu welchem Schuljahr die Schulform gültig ist */
	@Column(name = "gueltigBis")
	@JsonProperty
	public Integer gueltigBis;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOAlleSchulformen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOAlleSchulformen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOAlleSchulformen ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Kuerzel   der Wert für das Attribut Kuerzel
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 * @param HatGymOb   der Wert für das Attribut HatGymOb
	 */
	public DTOAlleSchulformen(final long ID, final String Kuerzel, final String Bezeichnung, final Boolean HatGymOb) {
		this.ID = ID;
		if (Kuerzel == null) {
			throw new NullPointerException("Kuerzel must not be null");
		}
		this.Kuerzel = Kuerzel;
		if (Bezeichnung == null) {
			throw new NullPointerException("Bezeichnung must not be null");
		}
		this.Bezeichnung = Bezeichnung;
		this.HatGymOb = HatGymOb;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOAlleSchulformen other = (DTOAlleSchulformen) obj;
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
		return "DTOAlleSchulformen(ID=" + this.ID + ", Kuerzel=" + this.Kuerzel + ", Nummer=" + this.Nummer + ", Bezeichnung=" + this.Bezeichnung + ", HatGymOb=" + this.HatGymOb + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ")";
	}

}
