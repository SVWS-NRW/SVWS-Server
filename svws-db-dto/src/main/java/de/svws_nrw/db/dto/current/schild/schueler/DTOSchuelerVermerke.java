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
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerVermerke.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerVermerke")
@NamedQuery(name = "DTOSchuelerVermerke.all", query = "SELECT e FROM DTOSchuelerVermerke e")
@NamedQuery(name = "DTOSchuelerVermerke.id", query = "SELECT e FROM DTOSchuelerVermerke e WHERE e.ID = :value")
@NamedQuery(name = "DTOSchuelerVermerke.id.multiple", query = "SELECT e FROM DTOSchuelerVermerke e WHERE e.ID IN :value")
@NamedQuery(name = "DTOSchuelerVermerke.schueler_id", query = "SELECT e FROM DTOSchuelerVermerke e WHERE e.Schueler_ID = :value")
@NamedQuery(name = "DTOSchuelerVermerke.schueler_id.multiple", query = "SELECT e FROM DTOSchuelerVermerke e WHERE e.Schueler_ID IN :value")
@NamedQuery(name = "DTOSchuelerVermerke.vermerkart_id", query = "SELECT e FROM DTOSchuelerVermerke e WHERE e.VermerkArt_ID = :value")
@NamedQuery(name = "DTOSchuelerVermerke.vermerkart_id.multiple", query = "SELECT e FROM DTOSchuelerVermerke e WHERE e.VermerkArt_ID IN :value")
@NamedQuery(name = "DTOSchuelerVermerke.datum", query = "SELECT e FROM DTOSchuelerVermerke e WHERE e.Datum = :value")
@NamedQuery(name = "DTOSchuelerVermerke.datum.multiple", query = "SELECT e FROM DTOSchuelerVermerke e WHERE e.Datum IN :value")
@NamedQuery(name = "DTOSchuelerVermerke.bemerkung", query = "SELECT e FROM DTOSchuelerVermerke e WHERE e.Bemerkung = :value")
@NamedQuery(name = "DTOSchuelerVermerke.bemerkung.multiple", query = "SELECT e FROM DTOSchuelerVermerke e WHERE e.Bemerkung IN :value")
@NamedQuery(name = "DTOSchuelerVermerke.angelegtvon", query = "SELECT e FROM DTOSchuelerVermerke e WHERE e.AngelegtVon = :value")
@NamedQuery(name = "DTOSchuelerVermerke.angelegtvon.multiple", query = "SELECT e FROM DTOSchuelerVermerke e WHERE e.AngelegtVon IN :value")
@NamedQuery(name = "DTOSchuelerVermerke.geaendertvon", query = "SELECT e FROM DTOSchuelerVermerke e WHERE e.GeaendertVon = :value")
@NamedQuery(name = "DTOSchuelerVermerke.geaendertvon.multiple", query = "SELECT e FROM DTOSchuelerVermerke e WHERE e.GeaendertVon IN :value")
@NamedQuery(name = "DTOSchuelerVermerke.primaryKeyQuery", query = "SELECT e FROM DTOSchuelerVermerke e WHERE e.ID = ?1")
@NamedQuery(name = "DTOSchuelerVermerke.primaryKeyQuery.multiple", query = "SELECT e FROM DTOSchuelerVermerke e WHERE e.ID IN ?1")
@NamedQuery(name = "DTOSchuelerVermerke.all.migration", query = "SELECT e FROM DTOSchuelerVermerke e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Schueler_ID", "VermerkArt_ID", "Datum", "Bemerkung", "AngelegtVon", "GeaendertVon"})
public final class DTOSchuelerVermerke {

	/** ID des Vermerkeintrages beim Schüler */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** SchülerID des Vermerkeintrages beim Schüler */
	@Column(name = "Schueler_ID")
	@JsonProperty
	public long Schueler_ID;

	/** Art des Vermerkeintrages beim Schüler */
	@Column(name = "VermerkArt_ID")
	@JsonProperty
	public Long VermerkArt_ID;

	/** Datum des Vermerkeintrages beim Schüler */
	@Column(name = "Datum")
	@JsonProperty
	@Convert(converter = DatumConverter.class)
	@JsonSerialize(using = DatumConverterSerializer.class)
	@JsonDeserialize(using = DatumConverterDeserializer.class)
	public String Datum;

	/** Bemerkung des Vermerkeintrages beim Schüler */
	@Column(name = "Bemerkung")
	@JsonProperty
	public String Bemerkung;

	/** Angelegt von User des Vermerkeintrages beim Schüler */
	@Column(name = "AngelegtVon")
	@JsonProperty
	public String AngelegtVon;

	/** Geändert von User des Vermerkeintrages beim Schüler */
	@Column(name = "GeaendertVon")
	@JsonProperty
	public String GeaendertVon;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerVermerke ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSchuelerVermerke() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerVermerke ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 */
	public DTOSchuelerVermerke(final long ID, final long Schueler_ID) {
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
		DTOSchuelerVermerke other = (DTOSchuelerVermerke) obj;
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
		return "DTOSchuelerVermerke(ID=" + this.ID + ", Schueler_ID=" + this.Schueler_ID + ", VermerkArt_ID=" + this.VermerkArt_ID + ", Datum=" + this.Datum + ", Bemerkung=" + this.Bemerkung + ", AngelegtVon=" + this.AngelegtVon + ", GeaendertVon=" + this.GeaendertVon + ")";
	}

}
