package de.svws_nrw.db.dto.current.schild.kurse;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Kurs_Schueler.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(DTOKursSchuelerPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Kurs_Schueler")
@JsonPropertyOrder({"Kurs_ID", "Schueler_ID", "LernabschnittWechselNr", "Leistung_ID"})
public final class DTOKursSchueler {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOKursSchueler e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOKursSchueler e WHERE e.Kurs_ID = ?1 AND e.Schueler_ID = ?2 AND e.LernabschnittWechselNr = ?3 AND e.Leistung_ID = ?4";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOKursSchueler e WHERE e.Kurs_ID IS NOT NULL AND e.Schueler_ID IS NOT NULL AND e.LernabschnittWechselNr IS NOT NULL AND e.Leistung_ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Kurs_ID */
	public static final String QUERY_BY_KURS_ID = "SELECT e FROM DTOKursSchueler e WHERE e.Kurs_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Kurs_ID */
	public static final String QUERY_LIST_BY_KURS_ID = "SELECT e FROM DTOKursSchueler e WHERE e.Kurs_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schueler_ID */
	public static final String QUERY_BY_SCHUELER_ID = "SELECT e FROM DTOKursSchueler e WHERE e.Schueler_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schueler_ID */
	public static final String QUERY_LIST_BY_SCHUELER_ID = "SELECT e FROM DTOKursSchueler e WHERE e.Schueler_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes LernabschnittWechselNr */
	public static final String QUERY_BY_LERNABSCHNITTWECHSELNR = "SELECT e FROM DTOKursSchueler e WHERE e.LernabschnittWechselNr = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes LernabschnittWechselNr */
	public static final String QUERY_LIST_BY_LERNABSCHNITTWECHSELNR = "SELECT e FROM DTOKursSchueler e WHERE e.LernabschnittWechselNr IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Leistung_ID */
	public static final String QUERY_BY_LEISTUNG_ID = "SELECT e FROM DTOKursSchueler e WHERE e.Leistung_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Leistung_ID */
	public static final String QUERY_LIST_BY_LEISTUNG_ID = "SELECT e FROM DTOKursSchueler e WHERE e.Leistung_ID IN ?1";

	/** Die eindeutige ID des Kurses – verweist auf den Kurs */
	@Id
	@Column(name = "Kurs_ID")
	@JsonProperty
	public long Kurs_ID;

	/** Die eindeutige ID des Schülers – verweist auf den Schüler */
	@Id
	@Column(name = "Schueler_ID")
	@JsonProperty
	public long Schueler_ID;

	/** Wird für Wiederholungen im Laufenden Schuljahresabschnitt genutzt 0=aktueller/neuester Lernabschnitt 1=vor dem ersten Wechsel 2=vor dem zweiten Wechsel usw */
	@Id
	@Column(name = "LernabschnittWechselNr")
	@JsonProperty
	public Integer LernabschnittWechselNr;

	/** Die eindeutige ID der Leistungsdaten, in denen die Zuordnung stattgefunden hat */
	@Id
	@Column(name = "Leistung_ID")
	@JsonProperty
	public long Leistung_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOKursSchueler ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOKursSchueler() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOKursSchueler ohne eine Initialisierung der Attribute.
	 * @param Kurs_ID   der Wert für das Attribut Kurs_ID
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 * @param Leistung_ID   der Wert für das Attribut Leistung_ID
	 */
	public DTOKursSchueler(final long Kurs_ID, final long Schueler_ID, final long Leistung_ID) {
		this.Kurs_ID = Kurs_ID;
		this.Schueler_ID = Schueler_ID;
		this.Leistung_ID = Leistung_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOKursSchueler other = (DTOKursSchueler) obj;
		if (Kurs_ID != other.Kurs_ID)
			return false;
		if (Schueler_ID != other.Schueler_ID)
			return false;
		if (LernabschnittWechselNr == null) {
			if (other.LernabschnittWechselNr != null)
				return false;
		} else if (!LernabschnittWechselNr.equals(other.LernabschnittWechselNr))
			return false;
		return Leistung_ID == other.Leistung_ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(Kurs_ID);

		result = prime * result + Long.hashCode(Schueler_ID);

		result = prime * result + ((LernabschnittWechselNr == null) ? 0 : LernabschnittWechselNr.hashCode());

		result = prime * result + Long.hashCode(Leistung_ID);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOKursSchueler(Kurs_ID=" + this.Kurs_ID + ", Schueler_ID=" + this.Schueler_ID + ", LernabschnittWechselNr=" + this.LernabschnittWechselNr + ", Leistung_ID=" + this.Leistung_ID + ")";
	}

}
