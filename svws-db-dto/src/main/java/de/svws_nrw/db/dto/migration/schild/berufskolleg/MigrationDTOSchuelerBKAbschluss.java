package de.svws_nrw.db.dto.migration.schild.berufskolleg;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.migration.MigrationBooleanPlusMinusConverter;


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
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusConverterSerializer;
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerBKAbschluss.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerBKAbschluss")
@JsonPropertyOrder({"Schueler_ID", "Schuljahresabschnitts_ID", "Zulassung", "Bestanden", "ZertifikatBK", "ZulassungErwBK", "BestandenErwBK", "ZulassungBA", "BestandenBA", "PraktPrfNote", "NoteKolloquium", "ThemaAbschlussarbeit", "SchulnrEigner", "BAP_Vorhanden", "NoteFachpraxis", "FachPraktAnteilAusr", "Jahr", "Abschnitt"})
public final class MigrationDTOSchuelerBKAbschluss {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM MigrationDTOSchuelerBKAbschluss e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM MigrationDTOSchuelerBKAbschluss e WHERE e.Schueler_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM MigrationDTOSchuelerBKAbschluss e WHERE e.Schueler_ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM MigrationDTOSchuelerBKAbschluss e WHERE e.Schueler_ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schueler_ID */
	public static final String QUERY_BY_SCHUELER_ID = "SELECT e FROM MigrationDTOSchuelerBKAbschluss e WHERE e.Schueler_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schueler_ID */
	public static final String QUERY_LIST_BY_SCHUELER_ID = "SELECT e FROM MigrationDTOSchuelerBKAbschluss e WHERE e.Schueler_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schuljahresabschnitts_ID */
	public static final String QUERY_BY_SCHULJAHRESABSCHNITTS_ID = "SELECT e FROM MigrationDTOSchuelerBKAbschluss e WHERE e.Schuljahresabschnitts_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schuljahresabschnitts_ID */
	public static final String QUERY_LIST_BY_SCHULJAHRESABSCHNITTS_ID = "SELECT e FROM MigrationDTOSchuelerBKAbschluss e WHERE e.Schuljahresabschnitts_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Zulassung */
	public static final String QUERY_BY_ZULASSUNG = "SELECT e FROM MigrationDTOSchuelerBKAbschluss e WHERE e.Zulassung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Zulassung */
	public static final String QUERY_LIST_BY_ZULASSUNG = "SELECT e FROM MigrationDTOSchuelerBKAbschluss e WHERE e.Zulassung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Bestanden */
	public static final String QUERY_BY_BESTANDEN = "SELECT e FROM MigrationDTOSchuelerBKAbschluss e WHERE e.Bestanden = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Bestanden */
	public static final String QUERY_LIST_BY_BESTANDEN = "SELECT e FROM MigrationDTOSchuelerBKAbschluss e WHERE e.Bestanden IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ZertifikatBK */
	public static final String QUERY_BY_ZERTIFIKATBK = "SELECT e FROM MigrationDTOSchuelerBKAbschluss e WHERE e.ZertifikatBK = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ZertifikatBK */
	public static final String QUERY_LIST_BY_ZERTIFIKATBK = "SELECT e FROM MigrationDTOSchuelerBKAbschluss e WHERE e.ZertifikatBK IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ZulassungErwBK */
	public static final String QUERY_BY_ZULASSUNGERWBK = "SELECT e FROM MigrationDTOSchuelerBKAbschluss e WHERE e.ZulassungErwBK = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ZulassungErwBK */
	public static final String QUERY_LIST_BY_ZULASSUNGERWBK = "SELECT e FROM MigrationDTOSchuelerBKAbschluss e WHERE e.ZulassungErwBK IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes BestandenErwBK */
	public static final String QUERY_BY_BESTANDENERWBK = "SELECT e FROM MigrationDTOSchuelerBKAbschluss e WHERE e.BestandenErwBK = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes BestandenErwBK */
	public static final String QUERY_LIST_BY_BESTANDENERWBK = "SELECT e FROM MigrationDTOSchuelerBKAbschluss e WHERE e.BestandenErwBK IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ZulassungBA */
	public static final String QUERY_BY_ZULASSUNGBA = "SELECT e FROM MigrationDTOSchuelerBKAbschluss e WHERE e.ZulassungBA = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ZulassungBA */
	public static final String QUERY_LIST_BY_ZULASSUNGBA = "SELECT e FROM MigrationDTOSchuelerBKAbschluss e WHERE e.ZulassungBA IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes BestandenBA */
	public static final String QUERY_BY_BESTANDENBA = "SELECT e FROM MigrationDTOSchuelerBKAbschluss e WHERE e.BestandenBA = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes BestandenBA */
	public static final String QUERY_LIST_BY_BESTANDENBA = "SELECT e FROM MigrationDTOSchuelerBKAbschluss e WHERE e.BestandenBA IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes PraktPrfNote */
	public static final String QUERY_BY_PRAKTPRFNOTE = "SELECT e FROM MigrationDTOSchuelerBKAbschluss e WHERE e.PraktPrfNote = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes PraktPrfNote */
	public static final String QUERY_LIST_BY_PRAKTPRFNOTE = "SELECT e FROM MigrationDTOSchuelerBKAbschluss e WHERE e.PraktPrfNote IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes NoteKolloquium */
	public static final String QUERY_BY_NOTEKOLLOQUIUM = "SELECT e FROM MigrationDTOSchuelerBKAbschluss e WHERE e.NoteKolloquium = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes NoteKolloquium */
	public static final String QUERY_LIST_BY_NOTEKOLLOQUIUM = "SELECT e FROM MigrationDTOSchuelerBKAbschluss e WHERE e.NoteKolloquium IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ThemaAbschlussarbeit */
	public static final String QUERY_BY_THEMAABSCHLUSSARBEIT = "SELECT e FROM MigrationDTOSchuelerBKAbschluss e WHERE e.ThemaAbschlussarbeit = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ThemaAbschlussarbeit */
	public static final String QUERY_LIST_BY_THEMAABSCHLUSSARBEIT = "SELECT e FROM MigrationDTOSchuelerBKAbschluss e WHERE e.ThemaAbschlussarbeit IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SchulnrEigner */
	public static final String QUERY_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOSchuelerBKAbschluss e WHERE e.SchulnrEigner = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SchulnrEigner */
	public static final String QUERY_LIST_BY_SCHULNREIGNER = "SELECT e FROM MigrationDTOSchuelerBKAbschluss e WHERE e.SchulnrEigner IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes BAP_Vorhanden */
	public static final String QUERY_BY_BAP_VORHANDEN = "SELECT e FROM MigrationDTOSchuelerBKAbschluss e WHERE e.BAP_Vorhanden = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes BAP_Vorhanden */
	public static final String QUERY_LIST_BY_BAP_VORHANDEN = "SELECT e FROM MigrationDTOSchuelerBKAbschluss e WHERE e.BAP_Vorhanden IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes NoteFachpraxis */
	public static final String QUERY_BY_NOTEFACHPRAXIS = "SELECT e FROM MigrationDTOSchuelerBKAbschluss e WHERE e.NoteFachpraxis = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes NoteFachpraxis */
	public static final String QUERY_LIST_BY_NOTEFACHPRAXIS = "SELECT e FROM MigrationDTOSchuelerBKAbschluss e WHERE e.NoteFachpraxis IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes FachPraktAnteilAusr */
	public static final String QUERY_BY_FACHPRAKTANTEILAUSR = "SELECT e FROM MigrationDTOSchuelerBKAbschluss e WHERE e.FachPraktAnteilAusr = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes FachPraktAnteilAusr */
	public static final String QUERY_LIST_BY_FACHPRAKTANTEILAUSR = "SELECT e FROM MigrationDTOSchuelerBKAbschluss e WHERE e.FachPraktAnteilAusr IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Jahr */
	public static final String QUERY_BY_JAHR = "SELECT e FROM MigrationDTOSchuelerBKAbschluss e WHERE e.Jahr = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Jahr */
	public static final String QUERY_LIST_BY_JAHR = "SELECT e FROM MigrationDTOSchuelerBKAbschluss e WHERE e.Jahr IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Abschnitt */
	public static final String QUERY_BY_ABSCHNITT = "SELECT e FROM MigrationDTOSchuelerBKAbschluss e WHERE e.Abschnitt = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Abschnitt */
	public static final String QUERY_LIST_BY_ABSCHNITT = "SELECT e FROM MigrationDTOSchuelerBKAbschluss e WHERE e.Abschnitt IN ?1";

	/** SchülerID für den BKAbschlussReiter */
	@Id
	@Column(name = "Schueler_ID")
	@JsonProperty
	public Long Schueler_ID;

	/** ID des Schuljahresabschnittes aus der Tabelle Schuljahresabschnitte */
	@Column(name = "Schuljahresabschnitts_ID")
	@JsonProperty
	public Long Schuljahresabschnitts_ID;

	/** Zulassung Ja Nein für den BKAbschlussReiter */
	@Column(name = "Zulassung")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusConverterDeserializer.class)
	public Boolean Zulassung;

	/** Bestanden Ja Nein  für den BKAbschlussReiter */
	@Column(name = "Bestanden")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusConverterDeserializer.class)
	public Boolean Bestanden;

	/** DEPRECATED: Zertifikat für den BKAbschlussReiter */
	@Column(name = "ZertifikatBK")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusConverterDeserializer.class)
	public Boolean ZertifikatBK;

	/** Zulassung erweiterte Beruflliche Kenntnisse */
	@Column(name = "ZulassungErwBK")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusConverterDeserializer.class)
	public Boolean ZulassungErwBK;

	/** Bestanden erweiterte Beruflliche Kenntnisse */
	@Column(name = "BestandenErwBK")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusConverterDeserializer.class)
	public Boolean BestandenErwBK;

	/** Zulassung Berufsabschluss  für den BKAbschlussReiter */
	@Column(name = "ZulassungBA")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusConverterDeserializer.class)
	public Boolean ZulassungBA;

	/** Bestanden Berufsabschluss für den BKAbschlussReiter */
	@Column(name = "BestandenBA")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusConverterDeserializer.class)
	public Boolean BestandenBA;

	/** Note Praktische Prüfung für den BKAbschlussReiter */
	@Column(name = "PraktPrfNote")
	@JsonProperty
	public String PraktPrfNote;

	/** Note Kolloqium für den BKAbschlussReiter */
	@Column(name = "NoteKolloquium")
	@JsonProperty
	public String NoteKolloquium;

	/** Thema Abschlussarbeit für den BKAbschlussReiter */
	@Column(name = "ThemaAbschlussarbeit")
	@JsonProperty
	public String ThemaAbschlussarbeit;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/** Berufsabschlussprüfung vorhanden für den BKAbschlussReiter */
	@Column(name = "BAP_Vorhanden")
	@JsonProperty
	public String BAP_Vorhanden;

	/** Note der Fachpraxis für den BKAbschlussReiter */
	@Column(name = "NoteFachpraxis")
	@JsonProperty
	public String NoteFachpraxis;

	/** Fachpraktische Anteile mindestens ausreichend für den BKAbschlussReiter */
	@Column(name = "FachPraktAnteilAusr")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusConverterDeserializer.class)
	public Boolean FachPraktAnteilAusr;

	/** Schuljahr für den BKAbschlussReiter */
	@Column(name = "Jahr")
	@JsonProperty
	public Integer Jahr;

	/** Abschnitt  für den BKAbschlussReiter */
	@Column(name = "Abschnitt")
	@JsonProperty
	public Integer Abschnitt;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuelerBKAbschluss ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOSchuelerBKAbschluss() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuelerBKAbschluss ohne eine Initialisierung der Attribute.
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 */
	public MigrationDTOSchuelerBKAbschluss(final Long Schueler_ID) {
		if (Schueler_ID == null) {
			throw new NullPointerException("Schueler_ID must not be null");
		}
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
		MigrationDTOSchuelerBKAbschluss other = (MigrationDTOSchuelerBKAbschluss) obj;
		if (Schueler_ID == null) {
			if (other.Schueler_ID != null)
				return false;
		} else if (!Schueler_ID.equals(other.Schueler_ID))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Schueler_ID == null) ? 0 : Schueler_ID.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "MigrationDTOSchuelerBKAbschluss(Schueler_ID=" + this.Schueler_ID + ", Schuljahresabschnitts_ID=" + this.Schuljahresabschnitts_ID + ", Zulassung=" + this.Zulassung + ", Bestanden=" + this.Bestanden + ", ZertifikatBK=" + this.ZertifikatBK + ", ZulassungErwBK=" + this.ZulassungErwBK + ", BestandenErwBK=" + this.BestandenErwBK + ", ZulassungBA=" + this.ZulassungBA + ", BestandenBA=" + this.BestandenBA + ", PraktPrfNote=" + this.PraktPrfNote + ", NoteKolloquium=" + this.NoteKolloquium + ", ThemaAbschlussarbeit=" + this.ThemaAbschlussarbeit + ", SchulnrEigner=" + this.SchulnrEigner + ", BAP_Vorhanden=" + this.BAP_Vorhanden + ", NoteFachpraxis=" + this.NoteFachpraxis + ", FachPraktAnteilAusr=" + this.FachPraktAnteilAusr + ", Jahr=" + this.Jahr + ", Abschnitt=" + this.Abschnitt + ")";
	}

}
