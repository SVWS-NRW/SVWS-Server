package de.svws_nrw.db.dto.current.schild;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultPlusConverter;


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
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultPlusConverterSerializer;
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultPlusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerListe.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerListe")
@JsonPropertyOrder({"ID", "Bezeichnung", "Erzeuger", "Privat"})
public final class DTOSchuelerIndividuelleGruppe {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOSchuelerIndividuelleGruppe e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOSchuelerIndividuelleGruppe e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOSchuelerIndividuelleGruppe e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOSchuelerIndividuelleGruppe e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOSchuelerIndividuelleGruppe e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOSchuelerIndividuelleGruppe e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Bezeichnung */
	public static final String QUERY_BY_BEZEICHNUNG = "SELECT e FROM DTOSchuelerIndividuelleGruppe e WHERE e.Bezeichnung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Bezeichnung */
	public static final String QUERY_LIST_BY_BEZEICHNUNG = "SELECT e FROM DTOSchuelerIndividuelleGruppe e WHERE e.Bezeichnung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Erzeuger */
	public static final String QUERY_BY_ERZEUGER = "SELECT e FROM DTOSchuelerIndividuelleGruppe e WHERE e.Erzeuger = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Erzeuger */
	public static final String QUERY_LIST_BY_ERZEUGER = "SELECT e FROM DTOSchuelerIndividuelleGruppe e WHERE e.Erzeuger IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Privat */
	public static final String QUERY_BY_PRIVAT = "SELECT e FROM DTOSchuelerIndividuelleGruppe e WHERE e.Privat = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Privat */
	public static final String QUERY_LIST_BY_PRIVAT = "SELECT e FROM DTOSchuelerIndividuelleGruppe e WHERE e.Privat IN ?1";

	/** ID der individuellen Schülerliste */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Bezeichnung der individuellen Schülerliste */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** UserID Erzeuger der individuellen Schülerliste */
	@Column(name = "Erzeuger")
	@JsonProperty
	public String Erzeuger;

	/** Schülerliste Privat oder Öffentlich */
	@Column(name = "Privat")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Privat;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerIndividuelleGruppe ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSchuelerIndividuelleGruppe() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerIndividuelleGruppe ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 */
	public DTOSchuelerIndividuelleGruppe(final long ID, final String Bezeichnung) {
		this.ID = ID;
		if (Bezeichnung == null) {
			throw new NullPointerException("Bezeichnung must not be null");
		}
		this.Bezeichnung = Bezeichnung;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOSchuelerIndividuelleGruppe other = (DTOSchuelerIndividuelleGruppe) obj;
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
		return "DTOSchuelerIndividuelleGruppe(ID=" + this.ID + ", Bezeichnung=" + this.Bezeichnung + ", Erzeuger=" + this.Erzeuger + ", Privat=" + this.Privat + ")";
	}

}
