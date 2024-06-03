package de.svws_nrw.db.dto.current.gost.kursblockung;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.kursblockung.GostKursblockungRegelTypConverter;

import de.svws_nrw.core.types.kursblockung.GostKursblockungRegelTyp;


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
import de.svws_nrw.csv.converter.current.kursblockung.GostKursblockungRegelTypConverterSerializer;
import de.svws_nrw.csv.converter.current.kursblockung.GostKursblockungRegelTypConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Gost_Blockung_Regeln.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Gost_Blockung_Regeln")
@JsonPropertyOrder({"ID", "Blockung_ID", "Typ"})
public final class DTOGostBlockungRegel {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOGostBlockungRegel e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOGostBlockungRegel e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOGostBlockungRegel e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOGostBlockungRegel e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOGostBlockungRegel e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOGostBlockungRegel e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Blockung_ID */
	public static final String QUERY_BY_BLOCKUNG_ID = "SELECT e FROM DTOGostBlockungRegel e WHERE e.Blockung_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Blockung_ID */
	public static final String QUERY_LIST_BY_BLOCKUNG_ID = "SELECT e FROM DTOGostBlockungRegel e WHERE e.Blockung_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Typ */
	public static final String QUERY_BY_TYP = "SELECT e FROM DTOGostBlockungRegel e WHERE e.Typ = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Typ */
	public static final String QUERY_LIST_BY_TYP = "SELECT e FROM DTOGostBlockungRegel e WHERE e.Typ IN ?1";

	/** ID der Regel (generiert) */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** ID der Blockung */
	@Column(name = "Blockung_ID")
	@JsonProperty
	public long Blockung_ID;

	/** Die ID des Typs der Regeldefinition (siehe Core-Type GostKursblockungRegeltyp) */
	@Column(name = "Typ")
	@JsonProperty
	@Convert(converter = GostKursblockungRegelTypConverter.class)
	@JsonSerialize(using = GostKursblockungRegelTypConverterSerializer.class)
	@JsonDeserialize(using = GostKursblockungRegelTypConverterDeserializer.class)
	public GostKursblockungRegelTyp Typ;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostBlockungRegel ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOGostBlockungRegel() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostBlockungRegel ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Blockung_ID   der Wert für das Attribut Blockung_ID
	 * @param Typ   der Wert für das Attribut Typ
	 */
	public DTOGostBlockungRegel(final long ID, final long Blockung_ID, final GostKursblockungRegelTyp Typ) {
		this.ID = ID;
		this.Blockung_ID = Blockung_ID;
		this.Typ = Typ;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOGostBlockungRegel other = (DTOGostBlockungRegel) obj;
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
		return "DTOGostBlockungRegel(ID=" + this.ID + ", Blockung_ID=" + this.Blockung_ID + ", Typ=" + this.Typ + ")";
	}

}
