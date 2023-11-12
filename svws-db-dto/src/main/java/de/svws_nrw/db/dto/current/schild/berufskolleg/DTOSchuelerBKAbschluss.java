package de.svws_nrw.db.dto.current.schild.berufskolleg;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.BooleanPlusMinusConverter;


import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
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
@NamedQuery(name = "DTOSchuelerBKAbschluss.all", query = "SELECT e FROM DTOSchuelerBKAbschluss e")
@NamedQuery(name = "DTOSchuelerBKAbschluss.schueler_id", query = "SELECT e FROM DTOSchuelerBKAbschluss e WHERE e.Schueler_ID = :value")
@NamedQuery(name = "DTOSchuelerBKAbschluss.schueler_id.multiple", query = "SELECT e FROM DTOSchuelerBKAbschluss e WHERE e.Schueler_ID IN :value")
@NamedQuery(name = "DTOSchuelerBKAbschluss.schuljahresabschnitts_id", query = "SELECT e FROM DTOSchuelerBKAbschluss e WHERE e.Schuljahresabschnitts_ID = :value")
@NamedQuery(name = "DTOSchuelerBKAbschluss.schuljahresabschnitts_id.multiple", query = "SELECT e FROM DTOSchuelerBKAbschluss e WHERE e.Schuljahresabschnitts_ID IN :value")
@NamedQuery(name = "DTOSchuelerBKAbschluss.zulassung", query = "SELECT e FROM DTOSchuelerBKAbschluss e WHERE e.Zulassung = :value")
@NamedQuery(name = "DTOSchuelerBKAbschluss.zulassung.multiple", query = "SELECT e FROM DTOSchuelerBKAbschluss e WHERE e.Zulassung IN :value")
@NamedQuery(name = "DTOSchuelerBKAbschluss.bestanden", query = "SELECT e FROM DTOSchuelerBKAbschluss e WHERE e.Bestanden = :value")
@NamedQuery(name = "DTOSchuelerBKAbschluss.bestanden.multiple", query = "SELECT e FROM DTOSchuelerBKAbschluss e WHERE e.Bestanden IN :value")
@NamedQuery(name = "DTOSchuelerBKAbschluss.zertifikatbk", query = "SELECT e FROM DTOSchuelerBKAbschluss e WHERE e.ZertifikatBK = :value")
@NamedQuery(name = "DTOSchuelerBKAbschluss.zertifikatbk.multiple", query = "SELECT e FROM DTOSchuelerBKAbschluss e WHERE e.ZertifikatBK IN :value")
@NamedQuery(name = "DTOSchuelerBKAbschluss.zulassungerwbk", query = "SELECT e FROM DTOSchuelerBKAbschluss e WHERE e.ZulassungErwBK = :value")
@NamedQuery(name = "DTOSchuelerBKAbschluss.zulassungerwbk.multiple", query = "SELECT e FROM DTOSchuelerBKAbschluss e WHERE e.ZulassungErwBK IN :value")
@NamedQuery(name = "DTOSchuelerBKAbschluss.bestandenerwbk", query = "SELECT e FROM DTOSchuelerBKAbschluss e WHERE e.BestandenErwBK = :value")
@NamedQuery(name = "DTOSchuelerBKAbschluss.bestandenerwbk.multiple", query = "SELECT e FROM DTOSchuelerBKAbschluss e WHERE e.BestandenErwBK IN :value")
@NamedQuery(name = "DTOSchuelerBKAbschluss.zulassungba", query = "SELECT e FROM DTOSchuelerBKAbschluss e WHERE e.ZulassungBA = :value")
@NamedQuery(name = "DTOSchuelerBKAbschluss.zulassungba.multiple", query = "SELECT e FROM DTOSchuelerBKAbschluss e WHERE e.ZulassungBA IN :value")
@NamedQuery(name = "DTOSchuelerBKAbschluss.bestandenba", query = "SELECT e FROM DTOSchuelerBKAbschluss e WHERE e.BestandenBA = :value")
@NamedQuery(name = "DTOSchuelerBKAbschluss.bestandenba.multiple", query = "SELECT e FROM DTOSchuelerBKAbschluss e WHERE e.BestandenBA IN :value")
@NamedQuery(name = "DTOSchuelerBKAbschluss.praktprfnote", query = "SELECT e FROM DTOSchuelerBKAbschluss e WHERE e.PraktPrfNote = :value")
@NamedQuery(name = "DTOSchuelerBKAbschluss.praktprfnote.multiple", query = "SELECT e FROM DTOSchuelerBKAbschluss e WHERE e.PraktPrfNote IN :value")
@NamedQuery(name = "DTOSchuelerBKAbschluss.notekolloquium", query = "SELECT e FROM DTOSchuelerBKAbschluss e WHERE e.NoteKolloquium = :value")
@NamedQuery(name = "DTOSchuelerBKAbschluss.notekolloquium.multiple", query = "SELECT e FROM DTOSchuelerBKAbschluss e WHERE e.NoteKolloquium IN :value")
@NamedQuery(name = "DTOSchuelerBKAbschluss.themaabschlussarbeit", query = "SELECT e FROM DTOSchuelerBKAbschluss e WHERE e.ThemaAbschlussarbeit = :value")
@NamedQuery(name = "DTOSchuelerBKAbschluss.themaabschlussarbeit.multiple", query = "SELECT e FROM DTOSchuelerBKAbschluss e WHERE e.ThemaAbschlussarbeit IN :value")
@NamedQuery(name = "DTOSchuelerBKAbschluss.bap_vorhanden", query = "SELECT e FROM DTOSchuelerBKAbschluss e WHERE e.BAP_Vorhanden = :value")
@NamedQuery(name = "DTOSchuelerBKAbschluss.bap_vorhanden.multiple", query = "SELECT e FROM DTOSchuelerBKAbschluss e WHERE e.BAP_Vorhanden IN :value")
@NamedQuery(name = "DTOSchuelerBKAbschluss.notefachpraxis", query = "SELECT e FROM DTOSchuelerBKAbschluss e WHERE e.NoteFachpraxis = :value")
@NamedQuery(name = "DTOSchuelerBKAbschluss.notefachpraxis.multiple", query = "SELECT e FROM DTOSchuelerBKAbschluss e WHERE e.NoteFachpraxis IN :value")
@NamedQuery(name = "DTOSchuelerBKAbschluss.fachpraktanteilausr", query = "SELECT e FROM DTOSchuelerBKAbschluss e WHERE e.FachPraktAnteilAusr = :value")
@NamedQuery(name = "DTOSchuelerBKAbschluss.fachpraktanteilausr.multiple", query = "SELECT e FROM DTOSchuelerBKAbschluss e WHERE e.FachPraktAnteilAusr IN :value")
@NamedQuery(name = "DTOSchuelerBKAbschluss.primaryKeyQuery", query = "SELECT e FROM DTOSchuelerBKAbschluss e WHERE e.Schueler_ID = ?1")
@NamedQuery(name = "DTOSchuelerBKAbschluss.primaryKeyQuery.multiple", query = "SELECT e FROM DTOSchuelerBKAbschluss e WHERE e.Schueler_ID IN ?1")
@NamedQuery(name = "DTOSchuelerBKAbschluss.all.migration", query = "SELECT e FROM DTOSchuelerBKAbschluss e WHERE e.Schueler_ID IS NOT NULL")
@JsonPropertyOrder({"Schueler_ID", "Schuljahresabschnitts_ID", "Zulassung", "Bestanden", "ZertifikatBK", "ZulassungErwBK", "BestandenErwBK", "ZulassungBA", "BestandenBA", "PraktPrfNote", "NoteKolloquium", "ThemaAbschlussarbeit", "BAP_Vorhanden", "NoteFachpraxis", "FachPraktAnteilAusr"})
public final class DTOSchuelerBKAbschluss {

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
