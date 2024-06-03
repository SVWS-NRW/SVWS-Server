package de.svws_nrw.db.dto.current.schild.schueler;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.BooleanPlusMinusConverter;


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
import de.svws_nrw.csv.converter.current.BooleanPlusMinusConverterSerializer;
import de.svws_nrw.csv.converter.current.BooleanPlusMinusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerZP10.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerZP10")
@JsonPropertyOrder({"ID", "Schueler_ID", "Schuljahresabschnitts_ID", "Fach_ID", "Vornote", "NoteSchriftlich", "MdlPruefung", "MdlPruefungFW", "NoteMuendlich", "NoteAbschluss", "Fachlehrer_ID"})
public final class DTOSchuelerZP10 {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOSchuelerZP10 e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOSchuelerZP10 e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOSchuelerZP10 e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOSchuelerZP10 e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOSchuelerZP10 e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOSchuelerZP10 e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schueler_ID */
	public static final String QUERY_BY_SCHUELER_ID = "SELECT e FROM DTOSchuelerZP10 e WHERE e.Schueler_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schueler_ID */
	public static final String QUERY_LIST_BY_SCHUELER_ID = "SELECT e FROM DTOSchuelerZP10 e WHERE e.Schueler_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schuljahresabschnitts_ID */
	public static final String QUERY_BY_SCHULJAHRESABSCHNITTS_ID = "SELECT e FROM DTOSchuelerZP10 e WHERE e.Schuljahresabschnitts_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schuljahresabschnitts_ID */
	public static final String QUERY_LIST_BY_SCHULJAHRESABSCHNITTS_ID = "SELECT e FROM DTOSchuelerZP10 e WHERE e.Schuljahresabschnitts_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Fach_ID */
	public static final String QUERY_BY_FACH_ID = "SELECT e FROM DTOSchuelerZP10 e WHERE e.Fach_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Fach_ID */
	public static final String QUERY_LIST_BY_FACH_ID = "SELECT e FROM DTOSchuelerZP10 e WHERE e.Fach_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Vornote */
	public static final String QUERY_BY_VORNOTE = "SELECT e FROM DTOSchuelerZP10 e WHERE e.Vornote = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Vornote */
	public static final String QUERY_LIST_BY_VORNOTE = "SELECT e FROM DTOSchuelerZP10 e WHERE e.Vornote IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes NoteSchriftlich */
	public static final String QUERY_BY_NOTESCHRIFTLICH = "SELECT e FROM DTOSchuelerZP10 e WHERE e.NoteSchriftlich = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes NoteSchriftlich */
	public static final String QUERY_LIST_BY_NOTESCHRIFTLICH = "SELECT e FROM DTOSchuelerZP10 e WHERE e.NoteSchriftlich IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes MdlPruefung */
	public static final String QUERY_BY_MDLPRUEFUNG = "SELECT e FROM DTOSchuelerZP10 e WHERE e.MdlPruefung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes MdlPruefung */
	public static final String QUERY_LIST_BY_MDLPRUEFUNG = "SELECT e FROM DTOSchuelerZP10 e WHERE e.MdlPruefung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes MdlPruefungFW */
	public static final String QUERY_BY_MDLPRUEFUNGFW = "SELECT e FROM DTOSchuelerZP10 e WHERE e.MdlPruefungFW = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes MdlPruefungFW */
	public static final String QUERY_LIST_BY_MDLPRUEFUNGFW = "SELECT e FROM DTOSchuelerZP10 e WHERE e.MdlPruefungFW IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes NoteMuendlich */
	public static final String QUERY_BY_NOTEMUENDLICH = "SELECT e FROM DTOSchuelerZP10 e WHERE e.NoteMuendlich = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes NoteMuendlich */
	public static final String QUERY_LIST_BY_NOTEMUENDLICH = "SELECT e FROM DTOSchuelerZP10 e WHERE e.NoteMuendlich IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes NoteAbschluss */
	public static final String QUERY_BY_NOTEABSCHLUSS = "SELECT e FROM DTOSchuelerZP10 e WHERE e.NoteAbschluss = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes NoteAbschluss */
	public static final String QUERY_LIST_BY_NOTEABSCHLUSS = "SELECT e FROM DTOSchuelerZP10 e WHERE e.NoteAbschluss IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Fachlehrer_ID */
	public static final String QUERY_BY_FACHLEHRER_ID = "SELECT e FROM DTOSchuelerZP10 e WHERE e.Fachlehrer_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Fachlehrer_ID */
	public static final String QUERY_LIST_BY_FACHLEHRER_ID = "SELECT e FROM DTOSchuelerZP10 e WHERE e.Fachlehrer_ID IN ?1";

	/** ID des Facheintrags für den ZP10 Abschluss */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Schüler-ID des Facheintrags für den ZP10 Abschluss */
	@Column(name = "Schueler_ID")
	@JsonProperty
	public long Schueler_ID;

	/** Schuljahresabschnitt-ID für den Facheintrag des ZP10 Abschlusses */
	@Column(name = "Schuljahresabschnitts_ID")
	@JsonProperty
	public Long Schuljahresabschnitts_ID;

	/** Fach-ID des Facheintrags für den ZP10 Abschluss */
	@Column(name = "Fach_ID")
	@JsonProperty
	public long Fach_ID;

	/** Vornote zum ZP10-Facheintrag */
	@Column(name = "Vornote")
	@JsonProperty
	public String Vornote;

	/** Schriftliche Note zum ZP10-Facheintrag */
	@Column(name = "NoteSchriftlich")
	@JsonProperty
	public String NoteSchriftlich;

	/** Gibt an, ob zum ZP10-Facheintrag eine mündliche Prüfung angesetzt ist */
	@Column(name = "MdlPruefung")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusConverterDeserializer.class)
	public Boolean MdlPruefung;

	/** Gibt an, ob zum ZP10-Facheintrag eine freiwilliege mündliche Prüfung angesetzt ist */
	@Column(name = "MdlPruefungFW")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusConverterDeserializer.class)
	public Boolean MdlPruefungFW;

	/** Die Note der mündlichen Prüfung zum ZP10-Facheintrag */
	@Column(name = "NoteMuendlich")
	@JsonProperty
	public String NoteMuendlich;

	/** Die Abschlussnote zum ZP10-Facheintrag */
	@Column(name = "NoteAbschluss")
	@JsonProperty
	public String NoteAbschluss;

	/** Die Lehrer-ID zum ZP10-Facheintrag */
	@Column(name = "Fachlehrer_ID")
	@JsonProperty
	public Long Fachlehrer_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerZP10 ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSchuelerZP10() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerZP10 ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 * @param Fach_ID   der Wert für das Attribut Fach_ID
	 */
	public DTOSchuelerZP10(final long ID, final long Schueler_ID, final long Fach_ID) {
		this.ID = ID;
		this.Schueler_ID = Schueler_ID;
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
		DTOSchuelerZP10 other = (DTOSchuelerZP10) obj;
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
		return "DTOSchuelerZP10(ID=" + this.ID + ", Schueler_ID=" + this.Schueler_ID + ", Schuljahresabschnitts_ID=" + this.Schuljahresabschnitts_ID + ", Fach_ID=" + this.Fach_ID + ", Vornote=" + this.Vornote + ", NoteSchriftlich=" + this.NoteSchriftlich + ", MdlPruefung=" + this.MdlPruefung + ", MdlPruefungFW=" + this.MdlPruefungFW + ", NoteMuendlich=" + this.NoteMuendlich + ", NoteAbschluss=" + this.NoteAbschluss + ", Fachlehrer_ID=" + this.Fachlehrer_ID + ")";
	}

}
