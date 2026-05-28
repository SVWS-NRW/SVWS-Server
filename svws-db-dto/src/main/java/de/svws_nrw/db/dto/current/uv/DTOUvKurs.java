package de.svws_nrw.db.dto.current.uv;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.gost.GOStKursartConverter;

import de.svws_nrw.core.types.gost.GostKursart;


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
import de.svws_nrw.csv.converter.current.gost.GOStKursartConverterSerializer;
import de.svws_nrw.csv.converter.current.gost.GOStKursartConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle UV_Kurse.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "UV_Kurse")
@JsonPropertyOrder({"ID", "Planungsabschnitt_ID", "Schuljahresabschnitts_ID", "Fach_ID", "Kursart", "Kursnummer", "Schuelergruppe_ID"})
public final class DTOUvKurs {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOUvKurs e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOUvKurs e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOUvKurs e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOUvKurs e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOUvKurs e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOUvKurs e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Planungsabschnitt_ID */
	public static final String QUERY_BY_PLANUNGSABSCHNITT_ID = "SELECT e FROM DTOUvKurs e WHERE e.Planungsabschnitt_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Planungsabschnitt_ID */
	public static final String QUERY_LIST_BY_PLANUNGSABSCHNITT_ID = "SELECT e FROM DTOUvKurs e WHERE e.Planungsabschnitt_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schuljahresabschnitts_ID */
	public static final String QUERY_BY_SCHULJAHRESABSCHNITTS_ID = "SELECT e FROM DTOUvKurs e WHERE e.Schuljahresabschnitts_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schuljahresabschnitts_ID */
	public static final String QUERY_LIST_BY_SCHULJAHRESABSCHNITTS_ID = "SELECT e FROM DTOUvKurs e WHERE e.Schuljahresabschnitts_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Fach_ID */
	public static final String QUERY_BY_FACH_ID = "SELECT e FROM DTOUvKurs e WHERE e.Fach_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Fach_ID */
	public static final String QUERY_LIST_BY_FACH_ID = "SELECT e FROM DTOUvKurs e WHERE e.Fach_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Kursart */
	public static final String QUERY_BY_KURSART = "SELECT e FROM DTOUvKurs e WHERE e.Kursart = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Kursart */
	public static final String QUERY_LIST_BY_KURSART = "SELECT e FROM DTOUvKurs e WHERE e.Kursart IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Kursnummer */
	public static final String QUERY_BY_KURSNUMMER = "SELECT e FROM DTOUvKurs e WHERE e.Kursnummer = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Kursnummer */
	public static final String QUERY_LIST_BY_KURSNUMMER = "SELECT e FROM DTOUvKurs e WHERE e.Kursnummer IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schuelergruppe_ID */
	public static final String QUERY_BY_SCHUELERGRUPPE_ID = "SELECT e FROM DTOUvKurs e WHERE e.Schuelergruppe_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schuelergruppe_ID */
	public static final String QUERY_LIST_BY_SCHUELERGRUPPE_ID = "SELECT e FROM DTOUvKurs e WHERE e.Schuelergruppe_ID IN ?1";

	/** ID des Kurses (generiert, planungsspezifisch) */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Die ID des Planungsabschnitts als Fremdschlüssel auf die Tabelle UV_Planungsabschnitte */
	@Column(name = "Planungsabschnitt_ID")
	@JsonProperty
	public long Planungsabschnitt_ID;

	/** ID des Schuljahresabschnittes aus der Tabelle Schuljahresabschnitte */
	@Column(name = "Schuljahresabschnitts_ID")
	@JsonProperty
	public long Schuljahresabschnitts_ID;

	/** ID des Faches */
	@Column(name = "Fach_ID")
	@JsonProperty
	public long Fach_ID;

	/** ID der Kursart (siehe ID des Core-Types GostKursart) */
	@Column(name = "Kursart")
	@JsonProperty
	@Convert(converter = GOStKursartConverter.class)
	@JsonSerialize(using = GOStKursartConverterSerializer.class)
	@JsonDeserialize(using = GOStKursartConverterDeserializer.class)
	public GostKursart Kursart;

	/** Die Nummer des Kurses in Bezug auf das Fach (Kurse eines Faches sind in einer Blockung üblicherweise von 1 ab durchnummeriert) */
	@Column(name = "Kursnummer")
	@JsonProperty
	public int Kursnummer;

	/** Fremdschlüssel auf die Schülergruppe (Tabelle UV_Schuelergruppen) */
	@Column(name = "Schuelergruppe_ID")
	@JsonProperty
	public long Schuelergruppe_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOUvKurs ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOUvKurs() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOUvKurs ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Planungsabschnitt_ID   der Wert für das Attribut Planungsabschnitt_ID
	 * @param Schuljahresabschnitts_ID   der Wert für das Attribut Schuljahresabschnitts_ID
	 * @param Fach_ID   der Wert für das Attribut Fach_ID
	 * @param Kursart   der Wert für das Attribut Kursart
	 * @param Kursnummer   der Wert für das Attribut Kursnummer
	 * @param Schuelergruppe_ID   der Wert für das Attribut Schuelergruppe_ID
	 */
	public DTOUvKurs(final long ID, final long Planungsabschnitt_ID, final long Schuljahresabschnitts_ID, final long Fach_ID, final GostKursart Kursart, final int Kursnummer, final long Schuelergruppe_ID) {
		this.ID = ID;
		this.Planungsabschnitt_ID = Planungsabschnitt_ID;
		this.Schuljahresabschnitts_ID = Schuljahresabschnitts_ID;
		this.Fach_ID = Fach_ID;
		if (Kursart == null) {
			throw new NullPointerException("Kursart must not be null");
		}
		this.Kursart = Kursart;
		this.Kursnummer = Kursnummer;
		this.Schuelergruppe_ID = Schuelergruppe_ID;
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
		DTOUvKurs other = (DTOUvKurs) obj;
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
		return "DTOUvKurs(ID=" + this.ID + ", Planungsabschnitt_ID=" + this.Planungsabschnitt_ID + ", Schuljahresabschnitts_ID=" + this.Schuljahresabschnitts_ID + ", Fach_ID=" + this.Fach_ID + ", Kursart=" + this.Kursart + ", Kursnummer=" + this.Kursnummer + ", Schuelergruppe_ID=" + this.Schuelergruppe_ID + ")";
	}

}
