package de.svws_nrw.db.dto.current.schild.schueler;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.DatumConverter;


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
import de.svws_nrw.csv.converter.current.DatumConverterSerializer;
import de.svws_nrw.csv.converter.current.DatumConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerMerkmale.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerMerkmale")
@NamedQuery(name = "DTOSchuelerMerkmale.all", query = "SELECT e FROM DTOSchuelerMerkmale e")
@NamedQuery(name = "DTOSchuelerMerkmale.id", query = "SELECT e FROM DTOSchuelerMerkmale e WHERE e.ID = :value")
@NamedQuery(name = "DTOSchuelerMerkmale.id.multiple", query = "SELECT e FROM DTOSchuelerMerkmale e WHERE e.ID IN :value")
@NamedQuery(name = "DTOSchuelerMerkmale.schueler_id", query = "SELECT e FROM DTOSchuelerMerkmale e WHERE e.Schueler_ID = :value")
@NamedQuery(name = "DTOSchuelerMerkmale.schueler_id.multiple", query = "SELECT e FROM DTOSchuelerMerkmale e WHERE e.Schueler_ID IN :value")
@NamedQuery(name = "DTOSchuelerMerkmale.kurztext", query = "SELECT e FROM DTOSchuelerMerkmale e WHERE e.Kurztext = :value")
@NamedQuery(name = "DTOSchuelerMerkmale.kurztext.multiple", query = "SELECT e FROM DTOSchuelerMerkmale e WHERE e.Kurztext IN :value")
@NamedQuery(name = "DTOSchuelerMerkmale.datumvon", query = "SELECT e FROM DTOSchuelerMerkmale e WHERE e.DatumVon = :value")
@NamedQuery(name = "DTOSchuelerMerkmale.datumvon.multiple", query = "SELECT e FROM DTOSchuelerMerkmale e WHERE e.DatumVon IN :value")
@NamedQuery(name = "DTOSchuelerMerkmale.datumbis", query = "SELECT e FROM DTOSchuelerMerkmale e WHERE e.DatumBis = :value")
@NamedQuery(name = "DTOSchuelerMerkmale.datumbis.multiple", query = "SELECT e FROM DTOSchuelerMerkmale e WHERE e.DatumBis IN :value")
@NamedQuery(name = "DTOSchuelerMerkmale.primaryKeyQuery", query = "SELECT e FROM DTOSchuelerMerkmale e WHERE e.ID = ?1")
@NamedQuery(name = "DTOSchuelerMerkmale.all.migration", query = "SELECT e FROM DTOSchuelerMerkmale e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Schueler_ID", "Kurztext", "DatumVon", "DatumBis"})
public final class DTOSchuelerMerkmale {

	/** ID des Eintrag bei besondere Merkmale zum Schüler */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Schüler-ID des Eintrag bei besondere Merkmale zum Schüler */
	@Column(name = "Schueler_ID")
	@JsonProperty
	public long Schueler_ID;

	/** Kurztext des Merkmals des Eintrag bei besondere Merkmale zum Schüler */
	@Column(name = "Kurztext")
	@JsonProperty
	public String Kurztext;

	/** Datum Beginn des Eintrag bei besondere Merkmale zum Schüler */
	@Column(name = "DatumVon")
	@JsonProperty
	@Convert(converter = DatumConverter.class)
	@JsonSerialize(using = DatumConverterSerializer.class)
	@JsonDeserialize(using = DatumConverterDeserializer.class)
	public String DatumVon;

	/** Datum Ende des Eintrag bei besondere Merkmale zum Schüler */
	@Column(name = "DatumBis")
	@JsonProperty
	@Convert(converter = DatumConverter.class)
	@JsonSerialize(using = DatumConverterSerializer.class)
	@JsonDeserialize(using = DatumConverterDeserializer.class)
	public String DatumBis;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerMerkmale ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSchuelerMerkmale() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerMerkmale ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 */
	public DTOSchuelerMerkmale(final long ID, final long Schueler_ID) {
		this.ID = ID;
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
		DTOSchuelerMerkmale other = (DTOSchuelerMerkmale) obj;
		if (ID != other.ID)
			return false;
		return true;
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
		return "DTOSchuelerMerkmale(ID=" + this.ID + ", Schueler_ID=" + this.Schueler_ID + ", Kurztext=" + this.Kurztext + ", DatumVon=" + this.DatumVon + ", DatumBis=" + this.DatumBis + ")";
	}

}
