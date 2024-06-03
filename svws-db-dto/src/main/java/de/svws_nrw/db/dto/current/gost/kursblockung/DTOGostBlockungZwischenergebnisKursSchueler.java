package de.svws_nrw.db.dto.current.gost.kursblockung;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Gost_Blockung_Zwischenergebnisse_Kurs_Schueler.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(DTOGostBlockungZwischenergebnisKursSchuelerPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Gost_Blockung_Zwischenergebnisse_Kurs_Schueler")
@JsonPropertyOrder({"Zwischenergebnis_ID", "Blockung_Kurs_ID", "Schueler_ID"})
public final class DTOGostBlockungZwischenergebnisKursSchueler {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOGostBlockungZwischenergebnisKursSchueler e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOGostBlockungZwischenergebnisKursSchueler e WHERE e.Zwischenergebnis_ID = ?1 AND e.Blockung_Kurs_ID = ?2 AND e.Schueler_ID = ?3";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOGostBlockungZwischenergebnisKursSchueler e WHERE e.Zwischenergebnis_ID IS NOT NULL AND e.Blockung_Kurs_ID IS NOT NULL AND e.Schueler_ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Zwischenergebnis_ID */
	public static final String QUERY_BY_ZWISCHENERGEBNIS_ID = "SELECT e FROM DTOGostBlockungZwischenergebnisKursSchueler e WHERE e.Zwischenergebnis_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Zwischenergebnis_ID */
	public static final String QUERY_LIST_BY_ZWISCHENERGEBNIS_ID = "SELECT e FROM DTOGostBlockungZwischenergebnisKursSchueler e WHERE e.Zwischenergebnis_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Blockung_Kurs_ID */
	public static final String QUERY_BY_BLOCKUNG_KURS_ID = "SELECT e FROM DTOGostBlockungZwischenergebnisKursSchueler e WHERE e.Blockung_Kurs_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Blockung_Kurs_ID */
	public static final String QUERY_LIST_BY_BLOCKUNG_KURS_ID = "SELECT e FROM DTOGostBlockungZwischenergebnisKursSchueler e WHERE e.Blockung_Kurs_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schueler_ID */
	public static final String QUERY_BY_SCHUELER_ID = "SELECT e FROM DTOGostBlockungZwischenergebnisKursSchueler e WHERE e.Schueler_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schueler_ID */
	public static final String QUERY_LIST_BY_SCHUELER_ID = "SELECT e FROM DTOGostBlockungZwischenergebnisKursSchueler e WHERE e.Schueler_ID IN ?1";

	/** Kurs-Schüler-Zuordnung eines Zwischenergebnisses: ID des Zwischenergebnisses */
	@Id
	@Column(name = "Zwischenergebnis_ID")
	@JsonProperty
	public long Zwischenergebnis_ID;

	/** Kurs-Schüler-Zuordnung eines Zwischenergebnisses: ID des Kurses */
	@Id
	@Column(name = "Blockung_Kurs_ID")
	@JsonProperty
	public long Blockung_Kurs_ID;

	/** Kurs-Schüler-Zuordnung eines Zwischenergebnisses: ID des Schülers */
	@Id
	@Column(name = "Schueler_ID")
	@JsonProperty
	public long Schueler_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostBlockungZwischenergebnisKursSchueler ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOGostBlockungZwischenergebnisKursSchueler() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostBlockungZwischenergebnisKursSchueler ohne eine Initialisierung der Attribute.
	 * @param Zwischenergebnis_ID   der Wert für das Attribut Zwischenergebnis_ID
	 * @param Blockung_Kurs_ID   der Wert für das Attribut Blockung_Kurs_ID
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 */
	public DTOGostBlockungZwischenergebnisKursSchueler(final long Zwischenergebnis_ID, final long Blockung_Kurs_ID, final long Schueler_ID) {
		this.Zwischenergebnis_ID = Zwischenergebnis_ID;
		this.Blockung_Kurs_ID = Blockung_Kurs_ID;
		this.Schueler_ID = Schueler_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOGostBlockungZwischenergebnisKursSchueler other = (DTOGostBlockungZwischenergebnisKursSchueler) obj;
		if (Zwischenergebnis_ID != other.Zwischenergebnis_ID)
			return false;
		if (Blockung_Kurs_ID != other.Blockung_Kurs_ID)
			return false;
		return Schueler_ID == other.Schueler_ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(Zwischenergebnis_ID);

		result = prime * result + Long.hashCode(Blockung_Kurs_ID);

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
		return "DTOGostBlockungZwischenergebnisKursSchueler(Zwischenergebnis_ID=" + this.Zwischenergebnis_ID + ", Blockung_Kurs_ID=" + this.Blockung_Kurs_ID + ", Schueler_ID=" + this.Schueler_ID + ")";
	}

}
