package de.nrw.schule.svws.db.dto.current.schild.schueler;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerZP10.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerZP10")
@NamedQuery(name="DTOSchuelerZP10.all", query="SELECT e FROM DTOSchuelerZP10 e")
@NamedQuery(name="DTOSchuelerZP10.id", query="SELECT e FROM DTOSchuelerZP10 e WHERE e.ID = :value")
@NamedQuery(name="DTOSchuelerZP10.id.multiple", query="SELECT e FROM DTOSchuelerZP10 e WHERE e.ID IN :value")
@NamedQuery(name="DTOSchuelerZP10.schueler_id", query="SELECT e FROM DTOSchuelerZP10 e WHERE e.Schueler_ID = :value")
@NamedQuery(name="DTOSchuelerZP10.schueler_id.multiple", query="SELECT e FROM DTOSchuelerZP10 e WHERE e.Schueler_ID IN :value")
@NamedQuery(name="DTOSchuelerZP10.schuljahresabschnitts_id", query="SELECT e FROM DTOSchuelerZP10 e WHERE e.Schuljahresabschnitts_ID = :value")
@NamedQuery(name="DTOSchuelerZP10.schuljahresabschnitts_id.multiple", query="SELECT e FROM DTOSchuelerZP10 e WHERE e.Schuljahresabschnitts_ID IN :value")
@NamedQuery(name="DTOSchuelerZP10.fach_id", query="SELECT e FROM DTOSchuelerZP10 e WHERE e.Fach_ID = :value")
@NamedQuery(name="DTOSchuelerZP10.fach_id.multiple", query="SELECT e FROM DTOSchuelerZP10 e WHERE e.Fach_ID IN :value")
@NamedQuery(name="DTOSchuelerZP10.vornote", query="SELECT e FROM DTOSchuelerZP10 e WHERE e.Vornote = :value")
@NamedQuery(name="DTOSchuelerZP10.vornote.multiple", query="SELECT e FROM DTOSchuelerZP10 e WHERE e.Vornote IN :value")
@NamedQuery(name="DTOSchuelerZP10.noteschriftlich", query="SELECT e FROM DTOSchuelerZP10 e WHERE e.NoteSchriftlich = :value")
@NamedQuery(name="DTOSchuelerZP10.noteschriftlich.multiple", query="SELECT e FROM DTOSchuelerZP10 e WHERE e.NoteSchriftlich IN :value")
@NamedQuery(name="DTOSchuelerZP10.mdlpruefung", query="SELECT e FROM DTOSchuelerZP10 e WHERE e.MdlPruefung = :value")
@NamedQuery(name="DTOSchuelerZP10.mdlpruefung.multiple", query="SELECT e FROM DTOSchuelerZP10 e WHERE e.MdlPruefung IN :value")
@NamedQuery(name="DTOSchuelerZP10.mdlpruefungfw", query="SELECT e FROM DTOSchuelerZP10 e WHERE e.MdlPruefungFW = :value")
@NamedQuery(name="DTOSchuelerZP10.mdlpruefungfw.multiple", query="SELECT e FROM DTOSchuelerZP10 e WHERE e.MdlPruefungFW IN :value")
@NamedQuery(name="DTOSchuelerZP10.notemuendlich", query="SELECT e FROM DTOSchuelerZP10 e WHERE e.NoteMuendlich = :value")
@NamedQuery(name="DTOSchuelerZP10.notemuendlich.multiple", query="SELECT e FROM DTOSchuelerZP10 e WHERE e.NoteMuendlich IN :value")
@NamedQuery(name="DTOSchuelerZP10.noteabschluss", query="SELECT e FROM DTOSchuelerZP10 e WHERE e.NoteAbschluss = :value")
@NamedQuery(name="DTOSchuelerZP10.noteabschluss.multiple", query="SELECT e FROM DTOSchuelerZP10 e WHERE e.NoteAbschluss IN :value")
@NamedQuery(name="DTOSchuelerZP10.fachlehrer_id", query="SELECT e FROM DTOSchuelerZP10 e WHERE e.Fachlehrer_ID = :value")
@NamedQuery(name="DTOSchuelerZP10.fachlehrer_id.multiple", query="SELECT e FROM DTOSchuelerZP10 e WHERE e.Fachlehrer_ID IN :value")
@NamedQuery(name="DTOSchuelerZP10.primaryKeyQuery", query="SELECT e FROM DTOSchuelerZP10 e WHERE e.ID = ?1")
@NamedQuery(name="DTOSchuelerZP10.all.migration", query="SELECT e FROM DTOSchuelerZP10 e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Schueler_ID","Schuljahresabschnitts_ID","Fach_ID","Vornote","NoteSchriftlich","MdlPruefung","MdlPruefungFW","NoteMuendlich","NoteAbschluss","Fachlehrer_ID"})
public class DTOSchuelerZP10 {

	/** ID des Facheintrags für den ZP10 Abschluss */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Schüler-ID des Facheintrags für den ZP10 Abschluss */
	@Column(name = "Schueler_ID")
	@JsonProperty
	public Long Schueler_ID;

	/** Schuljahresabschnitt-ID für den Facheintrag des ZP10 Abschlusses */
	@Column(name = "Schuljahresabschnitts_ID")
	@JsonProperty
	public Long Schuljahresabschnitts_ID;

	/** Fach-ID des Facheintrags für den ZP10 Abschluss */
	@Column(name = "Fach_ID")
	@JsonProperty
	public Long Fach_ID;

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
	@Convert(converter=BooleanPlusMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusConverterDeserializer.class)
	public Boolean MdlPruefung;

	/** Gibt an, ob zum ZP10-Facheintrag eine freiwilliege mündliche Prüfung angesetzt ist */
	@Column(name = "MdlPruefungFW")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusConverterDeserializer.class)
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
	public DTOSchuelerZP10(final Long ID, final Long Schueler_ID, final Long Fach_ID) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Schueler_ID == null) { 
			throw new NullPointerException("Schueler_ID must not be null");
		}
		this.Schueler_ID = Schueler_ID;
		if (Fach_ID == null) { 
			throw new NullPointerException("Fach_ID must not be null");
		}
		this.Fach_ID = Fach_ID;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOSchuelerZP10 other = (DTOSchuelerZP10) obj;
		if (ID == null) {
			if (other.ID != null)
				return false;
		} else if (!ID.equals(other.ID))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ID == null) ? 0 : ID.hashCode());
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