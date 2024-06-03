package de.svws_nrw.db.dto.current.schild.berufskolleg;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerBKAbschluss.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerBKAbschluss")
@JsonPropertyOrder({"Schueler_ID", "Schuljahresabschnitts_ID", "Zulassung", "Bestanden", "ZertifikatBK", "ZulassungErwBK", "BestandenErwBK", "ZulassungBA", "BestandenBA", "PraktPrfNote", "NoteKolloquium", "ThemaAbschlussarbeit", "BAP_Vorhanden", "NoteFachpraxis", "FachPraktAnteilAusr"})
public final class DTOSchuelerBKAbschluss {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOSchuelerBKAbschluss e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOSchuelerBKAbschluss e WHERE e.Schueler_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOSchuelerBKAbschluss e WHERE e.Schueler_ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOSchuelerBKAbschluss e WHERE e.Schueler_ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schueler_ID */
	public static final String QUERY_BY_SCHUELER_ID = "SELECT e FROM DTOSchuelerBKAbschluss e WHERE e.Schueler_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schueler_ID */
	public static final String QUERY_LIST_BY_SCHUELER_ID = "SELECT e FROM DTOSchuelerBKAbschluss e WHERE e.Schueler_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schuljahresabschnitts_ID */
	public static final String QUERY_BY_SCHULJAHRESABSCHNITTS_ID = "SELECT e FROM DTOSchuelerBKAbschluss e WHERE e.Schuljahresabschnitts_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schuljahresabschnitts_ID */
	public static final String QUERY_LIST_BY_SCHULJAHRESABSCHNITTS_ID = "SELECT e FROM DTOSchuelerBKAbschluss e WHERE e.Schuljahresabschnitts_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Zulassung */
	public static final String QUERY_BY_ZULASSUNG = "SELECT e FROM DTOSchuelerBKAbschluss e WHERE e.Zulassung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Zulassung */
	public static final String QUERY_LIST_BY_ZULASSUNG = "SELECT e FROM DTOSchuelerBKAbschluss e WHERE e.Zulassung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Bestanden */
	public static final String QUERY_BY_BESTANDEN = "SELECT e FROM DTOSchuelerBKAbschluss e WHERE e.Bestanden = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Bestanden */
	public static final String QUERY_LIST_BY_BESTANDEN = "SELECT e FROM DTOSchuelerBKAbschluss e WHERE e.Bestanden IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ZertifikatBK */
	public static final String QUERY_BY_ZERTIFIKATBK = "SELECT e FROM DTOSchuelerBKAbschluss e WHERE e.ZertifikatBK = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ZertifikatBK */
	public static final String QUERY_LIST_BY_ZERTIFIKATBK = "SELECT e FROM DTOSchuelerBKAbschluss e WHERE e.ZertifikatBK IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ZulassungErwBK */
	public static final String QUERY_BY_ZULASSUNGERWBK = "SELECT e FROM DTOSchuelerBKAbschluss e WHERE e.ZulassungErwBK = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ZulassungErwBK */
	public static final String QUERY_LIST_BY_ZULASSUNGERWBK = "SELECT e FROM DTOSchuelerBKAbschluss e WHERE e.ZulassungErwBK IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes BestandenErwBK */
	public static final String QUERY_BY_BESTANDENERWBK = "SELECT e FROM DTOSchuelerBKAbschluss e WHERE e.BestandenErwBK = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes BestandenErwBK */
	public static final String QUERY_LIST_BY_BESTANDENERWBK = "SELECT e FROM DTOSchuelerBKAbschluss e WHERE e.BestandenErwBK IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ZulassungBA */
	public static final String QUERY_BY_ZULASSUNGBA = "SELECT e FROM DTOSchuelerBKAbschluss e WHERE e.ZulassungBA = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ZulassungBA */
	public static final String QUERY_LIST_BY_ZULASSUNGBA = "SELECT e FROM DTOSchuelerBKAbschluss e WHERE e.ZulassungBA IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes BestandenBA */
	public static final String QUERY_BY_BESTANDENBA = "SELECT e FROM DTOSchuelerBKAbschluss e WHERE e.BestandenBA = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes BestandenBA */
	public static final String QUERY_LIST_BY_BESTANDENBA = "SELECT e FROM DTOSchuelerBKAbschluss e WHERE e.BestandenBA IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes PraktPrfNote */
	public static final String QUERY_BY_PRAKTPRFNOTE = "SELECT e FROM DTOSchuelerBKAbschluss e WHERE e.PraktPrfNote = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes PraktPrfNote */
	public static final String QUERY_LIST_BY_PRAKTPRFNOTE = "SELECT e FROM DTOSchuelerBKAbschluss e WHERE e.PraktPrfNote IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes NoteKolloquium */
	public static final String QUERY_BY_NOTEKOLLOQUIUM = "SELECT e FROM DTOSchuelerBKAbschluss e WHERE e.NoteKolloquium = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes NoteKolloquium */
	public static final String QUERY_LIST_BY_NOTEKOLLOQUIUM = "SELECT e FROM DTOSchuelerBKAbschluss e WHERE e.NoteKolloquium IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ThemaAbschlussarbeit */
	public static final String QUERY_BY_THEMAABSCHLUSSARBEIT = "SELECT e FROM DTOSchuelerBKAbschluss e WHERE e.ThemaAbschlussarbeit = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ThemaAbschlussarbeit */
	public static final String QUERY_LIST_BY_THEMAABSCHLUSSARBEIT = "SELECT e FROM DTOSchuelerBKAbschluss e WHERE e.ThemaAbschlussarbeit IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes BAP_Vorhanden */
	public static final String QUERY_BY_BAP_VORHANDEN = "SELECT e FROM DTOSchuelerBKAbschluss e WHERE e.BAP_Vorhanden = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes BAP_Vorhanden */
	public static final String QUERY_LIST_BY_BAP_VORHANDEN = "SELECT e FROM DTOSchuelerBKAbschluss e WHERE e.BAP_Vorhanden IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes NoteFachpraxis */
	public static final String QUERY_BY_NOTEFACHPRAXIS = "SELECT e FROM DTOSchuelerBKAbschluss e WHERE e.NoteFachpraxis = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes NoteFachpraxis */
	public static final String QUERY_LIST_BY_NOTEFACHPRAXIS = "SELECT e FROM DTOSchuelerBKAbschluss e WHERE e.NoteFachpraxis IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes FachPraktAnteilAusr */
	public static final String QUERY_BY_FACHPRAKTANTEILAUSR = "SELECT e FROM DTOSchuelerBKAbschluss e WHERE e.FachPraktAnteilAusr = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes FachPraktAnteilAusr */
	public static final String QUERY_LIST_BY_FACHPRAKTANTEILAUSR = "SELECT e FROM DTOSchuelerBKAbschluss e WHERE e.FachPraktAnteilAusr IN ?1";

	/** SchülerID für den BKAbschlussReiter */
	@Id
	@Column(name = "Schueler_ID")
	@JsonProperty
	public long Schueler_ID;

	/** ID des Schuljahresabschnittes aus der Tabelle Schuljahresabschnitte */
	@Column(name = "Schuljahresabschnitts_ID")
	@JsonProperty
	public Long Schuljahresabschnitts_ID;

	/** Zulassung Ja Nein für den BKAbschlussReiter */
	@Column(name = "Zulassung")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusConverterDeserializer.class)
	public Boolean Zulassung;

	/** Bestanden Ja Nein  für den BKAbschlussReiter */
	@Column(name = "Bestanden")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusConverterDeserializer.class)
	public Boolean Bestanden;

	/** DEPRECATED: Zertifikat für den BKAbschlussReiter */
	@Column(name = "ZertifikatBK")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusConverterDeserializer.class)
	public Boolean ZertifikatBK;

	/** Zulassung erweiterte Beruflliche Kenntnisse */
	@Column(name = "ZulassungErwBK")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusConverterDeserializer.class)
	public Boolean ZulassungErwBK;

	/** Bestanden erweiterte Beruflliche Kenntnisse */
	@Column(name = "BestandenErwBK")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusConverterDeserializer.class)
	public Boolean BestandenErwBK;

	/** Zulassung Berufsabschluss  für den BKAbschlussReiter */
	@Column(name = "ZulassungBA")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusConverterDeserializer.class)
	public Boolean ZulassungBA;

	/** Bestanden Berufsabschluss für den BKAbschlussReiter */
	@Column(name = "BestandenBA")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusConverterDeserializer.class)
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
	@Convert(converter = BooleanPlusMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusConverterDeserializer.class)
	public Boolean FachPraktAnteilAusr;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerBKAbschluss ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSchuelerBKAbschluss() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerBKAbschluss ohne eine Initialisierung der Attribute.
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 */
	public DTOSchuelerBKAbschluss(final long Schueler_ID) {
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
		DTOSchuelerBKAbschluss other = (DTOSchuelerBKAbschluss) obj;
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
		return "DTOSchuelerBKAbschluss(Schueler_ID=" + this.Schueler_ID + ", Schuljahresabschnitts_ID=" + this.Schuljahresabschnitts_ID + ", Zulassung=" + this.Zulassung + ", Bestanden=" + this.Bestanden + ", ZertifikatBK=" + this.ZertifikatBK + ", ZulassungErwBK=" + this.ZulassungErwBK + ", BestandenErwBK=" + this.BestandenErwBK + ", ZulassungBA=" + this.ZulassungBA + ", BestandenBA=" + this.BestandenBA + ", PraktPrfNote=" + this.PraktPrfNote + ", NoteKolloquium=" + this.NoteKolloquium + ", ThemaAbschlussarbeit=" + this.ThemaAbschlussarbeit + ", BAP_Vorhanden=" + this.BAP_Vorhanden + ", NoteFachpraxis=" + this.NoteFachpraxis + ", FachPraktAnteilAusr=" + this.FachPraktAnteilAusr + ")";
	}

}
