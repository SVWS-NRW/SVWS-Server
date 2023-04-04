package de.svws_nrw.db.dto.migration.schild.schueler;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.migration.MigrationDatumConverter;


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
import de.svws_nrw.csv.converter.migration.MigrationDatumConverterSerializer;
import de.svws_nrw.csv.converter.migration.MigrationDatumConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerVermerke.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerVermerke")
@NamedQuery(name = "MigrationDTOSchuelerVermerke.all", query = "SELECT e FROM MigrationDTOSchuelerVermerke e")
@NamedQuery(name = "MigrationDTOSchuelerVermerke.id", query = "SELECT e FROM MigrationDTOSchuelerVermerke e WHERE e.ID = :value")
@NamedQuery(name = "MigrationDTOSchuelerVermerke.id.multiple", query = "SELECT e FROM MigrationDTOSchuelerVermerke e WHERE e.ID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerVermerke.schueler_id", query = "SELECT e FROM MigrationDTOSchuelerVermerke e WHERE e.Schueler_ID = :value")
@NamedQuery(name = "MigrationDTOSchuelerVermerke.schueler_id.multiple", query = "SELECT e FROM MigrationDTOSchuelerVermerke e WHERE e.Schueler_ID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerVermerke.vermerkart_id", query = "SELECT e FROM MigrationDTOSchuelerVermerke e WHERE e.VermerkArt_ID = :value")
@NamedQuery(name = "MigrationDTOSchuelerVermerke.vermerkart_id.multiple", query = "SELECT e FROM MigrationDTOSchuelerVermerke e WHERE e.VermerkArt_ID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerVermerke.datum", query = "SELECT e FROM MigrationDTOSchuelerVermerke e WHERE e.Datum = :value")
@NamedQuery(name = "MigrationDTOSchuelerVermerke.datum.multiple", query = "SELECT e FROM MigrationDTOSchuelerVermerke e WHERE e.Datum IN :value")
@NamedQuery(name = "MigrationDTOSchuelerVermerke.bemerkung", query = "SELECT e FROM MigrationDTOSchuelerVermerke e WHERE e.Bemerkung = :value")
@NamedQuery(name = "MigrationDTOSchuelerVermerke.bemerkung.multiple", query = "SELECT e FROM MigrationDTOSchuelerVermerke e WHERE e.Bemerkung IN :value")
@NamedQuery(name = "MigrationDTOSchuelerVermerke.schulnreigner", query = "SELECT e FROM MigrationDTOSchuelerVermerke e WHERE e.SchulnrEigner = :value")
@NamedQuery(name = "MigrationDTOSchuelerVermerke.schulnreigner.multiple", query = "SELECT e FROM MigrationDTOSchuelerVermerke e WHERE e.SchulnrEigner IN :value")
@NamedQuery(name = "MigrationDTOSchuelerVermerke.angelegtvon", query = "SELECT e FROM MigrationDTOSchuelerVermerke e WHERE e.AngelegtVon = :value")
@NamedQuery(name = "MigrationDTOSchuelerVermerke.angelegtvon.multiple", query = "SELECT e FROM MigrationDTOSchuelerVermerke e WHERE e.AngelegtVon IN :value")
@NamedQuery(name = "MigrationDTOSchuelerVermerke.geaendertvon", query = "SELECT e FROM MigrationDTOSchuelerVermerke e WHERE e.GeaendertVon = :value")
@NamedQuery(name = "MigrationDTOSchuelerVermerke.geaendertvon.multiple", query = "SELECT e FROM MigrationDTOSchuelerVermerke e WHERE e.GeaendertVon IN :value")
@NamedQuery(name = "MigrationDTOSchuelerVermerke.primaryKeyQuery", query = "SELECT e FROM MigrationDTOSchuelerVermerke e WHERE e.ID = ?1")
@NamedQuery(name = "MigrationDTOSchuelerVermerke.all.migration", query = "SELECT e FROM MigrationDTOSchuelerVermerke e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Schueler_ID", "VermerkArt_ID", "Datum", "Bemerkung", "SchulnrEigner", "AngelegtVon", "GeaendertVon"})
public final class MigrationDTOSchuelerVermerke {

	/** ID des Vermerkeintrages beim Schüler */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** SchülerID des Vermerkeintrages beim Schüler */
	@Column(name = "Schueler_ID")
	@JsonProperty
	public Long Schueler_ID;

	/** Art des Vermerkeintrages beim Schüler */
	@Column(name = "VermerkArt_ID")
	@JsonProperty
	public Long VermerkArt_ID;

	/** Datum des Vermerkeintrages beim Schüler */
	@Column(name = "Datum")
	@JsonProperty
	@Convert(converter = MigrationDatumConverter.class)
	@JsonSerialize(using = MigrationDatumConverterSerializer.class)
	@JsonDeserialize(using = MigrationDatumConverterDeserializer.class)
	public String Datum;

	/** Bemerkung des Vermerkeintrages beim Schüler */
	@Column(name = "Bemerkung")
	@JsonProperty
	public String Bemerkung;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/** Angelegt von User des Vermerkeintrages beim Schüler */
	@Column(name = "AngelegtVon")
	@JsonProperty
	public String AngelegtVon;

	/** Geändert von User des Vermerkeintrages beim Schüler */
	@Column(name = "GeaendertVon")
	@JsonProperty
	public String GeaendertVon;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuelerVermerke ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOSchuelerVermerke() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuelerVermerke ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 */
	public MigrationDTOSchuelerVermerke(final Long ID, final Long Schueler_ID) {
		if (ID == null) {
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
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
		MigrationDTOSchuelerVermerke other = (MigrationDTOSchuelerVermerke) obj;
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
		return "MigrationDTOSchuelerVermerke(ID=" + this.ID + ", Schueler_ID=" + this.Schueler_ID + ", VermerkArt_ID=" + this.VermerkArt_ID + ", Datum=" + this.Datum + ", Bemerkung=" + this.Bemerkung + ", SchulnrEigner=" + this.SchulnrEigner + ", AngelegtVon=" + this.AngelegtVon + ", GeaendertVon=" + this.GeaendertVon + ")";
	}

}
