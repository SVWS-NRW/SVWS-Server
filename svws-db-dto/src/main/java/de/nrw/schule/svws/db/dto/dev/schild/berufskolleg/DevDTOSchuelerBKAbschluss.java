package de.nrw.schule.svws.db.dto.dev.schild.berufskolleg;

import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.converter.current.BooleanPlusMinusConverter;


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
import de.nrw.schule.svws.csv.converter.current.BooleanPlusMinusConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.BooleanPlusMinusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerBKAbschluss.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerBKAbschluss")
@NamedQuery(name="DevDTOSchuelerBKAbschluss.all", query="SELECT e FROM DevDTOSchuelerBKAbschluss e")
@NamedQuery(name="DevDTOSchuelerBKAbschluss.schueler_id", query="SELECT e FROM DevDTOSchuelerBKAbschluss e WHERE e.Schueler_ID = :value")
@NamedQuery(name="DevDTOSchuelerBKAbschluss.schueler_id.multiple", query="SELECT e FROM DevDTOSchuelerBKAbschluss e WHERE e.Schueler_ID IN :value")
@NamedQuery(name="DevDTOSchuelerBKAbschluss.schuljahresabschnitts_id", query="SELECT e FROM DevDTOSchuelerBKAbschluss e WHERE e.Schuljahresabschnitts_ID = :value")
@NamedQuery(name="DevDTOSchuelerBKAbschluss.schuljahresabschnitts_id.multiple", query="SELECT e FROM DevDTOSchuelerBKAbschluss e WHERE e.Schuljahresabschnitts_ID IN :value")
@NamedQuery(name="DevDTOSchuelerBKAbschluss.zulassung", query="SELECT e FROM DevDTOSchuelerBKAbschluss e WHERE e.Zulassung = :value")
@NamedQuery(name="DevDTOSchuelerBKAbschluss.zulassung.multiple", query="SELECT e FROM DevDTOSchuelerBKAbschluss e WHERE e.Zulassung IN :value")
@NamedQuery(name="DevDTOSchuelerBKAbschluss.bestanden", query="SELECT e FROM DevDTOSchuelerBKAbschluss e WHERE e.Bestanden = :value")
@NamedQuery(name="DevDTOSchuelerBKAbschluss.bestanden.multiple", query="SELECT e FROM DevDTOSchuelerBKAbschluss e WHERE e.Bestanden IN :value")
@NamedQuery(name="DevDTOSchuelerBKAbschluss.zertifikatbk", query="SELECT e FROM DevDTOSchuelerBKAbschluss e WHERE e.ZertifikatBK = :value")
@NamedQuery(name="DevDTOSchuelerBKAbschluss.zertifikatbk.multiple", query="SELECT e FROM DevDTOSchuelerBKAbschluss e WHERE e.ZertifikatBK IN :value")
@NamedQuery(name="DevDTOSchuelerBKAbschluss.zulassungerwbk", query="SELECT e FROM DevDTOSchuelerBKAbschluss e WHERE e.ZulassungErwBK = :value")
@NamedQuery(name="DevDTOSchuelerBKAbschluss.zulassungerwbk.multiple", query="SELECT e FROM DevDTOSchuelerBKAbschluss e WHERE e.ZulassungErwBK IN :value")
@NamedQuery(name="DevDTOSchuelerBKAbschluss.bestandenerwbk", query="SELECT e FROM DevDTOSchuelerBKAbschluss e WHERE e.BestandenErwBK = :value")
@NamedQuery(name="DevDTOSchuelerBKAbschluss.bestandenerwbk.multiple", query="SELECT e FROM DevDTOSchuelerBKAbschluss e WHERE e.BestandenErwBK IN :value")
@NamedQuery(name="DevDTOSchuelerBKAbschluss.zulassungba", query="SELECT e FROM DevDTOSchuelerBKAbschluss e WHERE e.ZulassungBA = :value")
@NamedQuery(name="DevDTOSchuelerBKAbschluss.zulassungba.multiple", query="SELECT e FROM DevDTOSchuelerBKAbschluss e WHERE e.ZulassungBA IN :value")
@NamedQuery(name="DevDTOSchuelerBKAbschluss.bestandenba", query="SELECT e FROM DevDTOSchuelerBKAbschluss e WHERE e.BestandenBA = :value")
@NamedQuery(name="DevDTOSchuelerBKAbschluss.bestandenba.multiple", query="SELECT e FROM DevDTOSchuelerBKAbschluss e WHERE e.BestandenBA IN :value")
@NamedQuery(name="DevDTOSchuelerBKAbschluss.praktprfnote", query="SELECT e FROM DevDTOSchuelerBKAbschluss e WHERE e.PraktPrfNote = :value")
@NamedQuery(name="DevDTOSchuelerBKAbschluss.praktprfnote.multiple", query="SELECT e FROM DevDTOSchuelerBKAbschluss e WHERE e.PraktPrfNote IN :value")
@NamedQuery(name="DevDTOSchuelerBKAbschluss.notekolloquium", query="SELECT e FROM DevDTOSchuelerBKAbschluss e WHERE e.NoteKolloquium = :value")
@NamedQuery(name="DevDTOSchuelerBKAbschluss.notekolloquium.multiple", query="SELECT e FROM DevDTOSchuelerBKAbschluss e WHERE e.NoteKolloquium IN :value")
@NamedQuery(name="DevDTOSchuelerBKAbschluss.themaabschlussarbeit", query="SELECT e FROM DevDTOSchuelerBKAbschluss e WHERE e.ThemaAbschlussarbeit = :value")
@NamedQuery(name="DevDTOSchuelerBKAbschluss.themaabschlussarbeit.multiple", query="SELECT e FROM DevDTOSchuelerBKAbschluss e WHERE e.ThemaAbschlussarbeit IN :value")
@NamedQuery(name="DevDTOSchuelerBKAbschluss.bap_vorhanden", query="SELECT e FROM DevDTOSchuelerBKAbschluss e WHERE e.BAP_Vorhanden = :value")
@NamedQuery(name="DevDTOSchuelerBKAbschluss.bap_vorhanden.multiple", query="SELECT e FROM DevDTOSchuelerBKAbschluss e WHERE e.BAP_Vorhanden IN :value")
@NamedQuery(name="DevDTOSchuelerBKAbschluss.notefachpraxis", query="SELECT e FROM DevDTOSchuelerBKAbschluss e WHERE e.NoteFachpraxis = :value")
@NamedQuery(name="DevDTOSchuelerBKAbschluss.notefachpraxis.multiple", query="SELECT e FROM DevDTOSchuelerBKAbschluss e WHERE e.NoteFachpraxis IN :value")
@NamedQuery(name="DevDTOSchuelerBKAbschluss.fachpraktanteilausr", query="SELECT e FROM DevDTOSchuelerBKAbschluss e WHERE e.FachPraktAnteilAusr = :value")
@NamedQuery(name="DevDTOSchuelerBKAbschluss.fachpraktanteilausr.multiple", query="SELECT e FROM DevDTOSchuelerBKAbschluss e WHERE e.FachPraktAnteilAusr IN :value")
@NamedQuery(name="DevDTOSchuelerBKAbschluss.primaryKeyQuery", query="SELECT e FROM DevDTOSchuelerBKAbschluss e WHERE e.Schueler_ID = ?1")
@NamedQuery(name="DevDTOSchuelerBKAbschluss.all.migration", query="SELECT e FROM DevDTOSchuelerBKAbschluss e WHERE e.Schueler_ID IS NOT NULL")
@JsonPropertyOrder({"Schueler_ID","Schuljahresabschnitts_ID","Zulassung","Bestanden","ZertifikatBK","ZulassungErwBK","BestandenErwBK","ZulassungBA","BestandenBA","PraktPrfNote","NoteKolloquium","ThemaAbschlussarbeit","BAP_Vorhanden","NoteFachpraxis","FachPraktAnteilAusr"})
public class DevDTOSchuelerBKAbschluss {

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
	@Convert(converter=BooleanPlusMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusConverterDeserializer.class)
	public Boolean Zulassung;

	/** Bestanden Ja Nein  für den BKAbschlussReiter */
	@Column(name = "Bestanden")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusConverterDeserializer.class)
	public Boolean Bestanden;

	/** DEPRECATED: Zertifikat für den BKAbschlussReiter */
	@Column(name = "ZertifikatBK")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusConverterDeserializer.class)
	public Boolean ZertifikatBK;

	/** Zulassung erweiterte Beruflliche Kenntnisse */
	@Column(name = "ZulassungErwBK")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusConverterDeserializer.class)
	public Boolean ZulassungErwBK;

	/** Bestanden erweiterte Beruflliche Kenntnisse */
	@Column(name = "BestandenErwBK")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusConverterDeserializer.class)
	public Boolean BestandenErwBK;

	/** Zulassung Berufsabschluss  für den BKAbschlussReiter */
	@Column(name = "ZulassungBA")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusConverterDeserializer.class)
	public Boolean ZulassungBA;

	/** Bestanden Berufsabschluss für den BKAbschlussReiter */
	@Column(name = "BestandenBA")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusConverterDeserializer.class)
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
	@Convert(converter=BooleanPlusMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusConverterDeserializer.class)
	public Boolean FachPraktAnteilAusr;

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOSchuelerBKAbschluss ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DevDTOSchuelerBKAbschluss() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOSchuelerBKAbschluss ohne eine Initialisierung der Attribute.
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 */
	public DevDTOSchuelerBKAbschluss(final Long Schueler_ID) {
		if (Schueler_ID == null) { 
			throw new NullPointerException("Schueler_ID must not be null");
		}
		this.Schueler_ID = Schueler_ID;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DevDTOSchuelerBKAbschluss other = (DevDTOSchuelerBKAbschluss) obj;
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
		return "DevDTOSchuelerBKAbschluss(Schueler_ID=" + this.Schueler_ID + ", Schuljahresabschnitts_ID=" + this.Schuljahresabschnitts_ID + ", Zulassung=" + this.Zulassung + ", Bestanden=" + this.Bestanden + ", ZertifikatBK=" + this.ZertifikatBK + ", ZulassungErwBK=" + this.ZulassungErwBK + ", BestandenErwBK=" + this.BestandenErwBK + ", ZulassungBA=" + this.ZulassungBA + ", BestandenBA=" + this.BestandenBA + ", PraktPrfNote=" + this.PraktPrfNote + ", NoteKolloquium=" + this.NoteKolloquium + ", ThemaAbschlussarbeit=" + this.ThemaAbschlussarbeit + ", BAP_Vorhanden=" + this.BAP_Vorhanden + ", NoteFachpraxis=" + this.NoteFachpraxis + ", FachPraktAnteilAusr=" + this.FachPraktAnteilAusr + ")";
	}

}