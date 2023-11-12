package de.svws_nrw.db.dto.migration.schild.grundschule;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverter;


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
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverterSerializer;
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerAnkreuzfloskeln.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerAnkreuzfloskeln")
@NamedQuery(name = "MigrationDTOSchuelerAnkreuzfloskeln.all", query = "SELECT e FROM MigrationDTOSchuelerAnkreuzfloskeln e")
@NamedQuery(name = "MigrationDTOSchuelerAnkreuzfloskeln.id", query = "SELECT e FROM MigrationDTOSchuelerAnkreuzfloskeln e WHERE e.ID = :value")
@NamedQuery(name = "MigrationDTOSchuelerAnkreuzfloskeln.id.multiple", query = "SELECT e FROM MigrationDTOSchuelerAnkreuzfloskeln e WHERE e.ID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAnkreuzfloskeln.schueler_id", query = "SELECT e FROM MigrationDTOSchuelerAnkreuzfloskeln e WHERE e.Schueler_ID = :value")
@NamedQuery(name = "MigrationDTOSchuelerAnkreuzfloskeln.schueler_id.multiple", query = "SELECT e FROM MigrationDTOSchuelerAnkreuzfloskeln e WHERE e.Schueler_ID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAnkreuzfloskeln.abschnitt_id", query = "SELECT e FROM MigrationDTOSchuelerAnkreuzfloskeln e WHERE e.Abschnitt_ID = :value")
@NamedQuery(name = "MigrationDTOSchuelerAnkreuzfloskeln.abschnitt_id.multiple", query = "SELECT e FROM MigrationDTOSchuelerAnkreuzfloskeln e WHERE e.Abschnitt_ID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAnkreuzfloskeln.floskel_id", query = "SELECT e FROM MigrationDTOSchuelerAnkreuzfloskeln e WHERE e.Floskel_ID = :value")
@NamedQuery(name = "MigrationDTOSchuelerAnkreuzfloskeln.floskel_id.multiple", query = "SELECT e FROM MigrationDTOSchuelerAnkreuzfloskeln e WHERE e.Floskel_ID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAnkreuzfloskeln.stufe1", query = "SELECT e FROM MigrationDTOSchuelerAnkreuzfloskeln e WHERE e.Stufe1 = :value")
@NamedQuery(name = "MigrationDTOSchuelerAnkreuzfloskeln.stufe1.multiple", query = "SELECT e FROM MigrationDTOSchuelerAnkreuzfloskeln e WHERE e.Stufe1 IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAnkreuzfloskeln.stufe2", query = "SELECT e FROM MigrationDTOSchuelerAnkreuzfloskeln e WHERE e.Stufe2 = :value")
@NamedQuery(name = "MigrationDTOSchuelerAnkreuzfloskeln.stufe2.multiple", query = "SELECT e FROM MigrationDTOSchuelerAnkreuzfloskeln e WHERE e.Stufe2 IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAnkreuzfloskeln.stufe3", query = "SELECT e FROM MigrationDTOSchuelerAnkreuzfloskeln e WHERE e.Stufe3 = :value")
@NamedQuery(name = "MigrationDTOSchuelerAnkreuzfloskeln.stufe3.multiple", query = "SELECT e FROM MigrationDTOSchuelerAnkreuzfloskeln e WHERE e.Stufe3 IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAnkreuzfloskeln.stufe4", query = "SELECT e FROM MigrationDTOSchuelerAnkreuzfloskeln e WHERE e.Stufe4 = :value")
@NamedQuery(name = "MigrationDTOSchuelerAnkreuzfloskeln.stufe4.multiple", query = "SELECT e FROM MigrationDTOSchuelerAnkreuzfloskeln e WHERE e.Stufe4 IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAnkreuzfloskeln.stufe5", query = "SELECT e FROM MigrationDTOSchuelerAnkreuzfloskeln e WHERE e.Stufe5 = :value")
@NamedQuery(name = "MigrationDTOSchuelerAnkreuzfloskeln.stufe5.multiple", query = "SELECT e FROM MigrationDTOSchuelerAnkreuzfloskeln e WHERE e.Stufe5 IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAnkreuzfloskeln.schulnreigner", query = "SELECT e FROM MigrationDTOSchuelerAnkreuzfloskeln e WHERE e.SchulnrEigner = :value")
@NamedQuery(name = "MigrationDTOSchuelerAnkreuzfloskeln.schulnreigner.multiple", query = "SELECT e FROM MigrationDTOSchuelerAnkreuzfloskeln e WHERE e.SchulnrEigner IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAnkreuzfloskeln.jahr", query = "SELECT e FROM MigrationDTOSchuelerAnkreuzfloskeln e WHERE e.Jahr = :value")
@NamedQuery(name = "MigrationDTOSchuelerAnkreuzfloskeln.jahr.multiple", query = "SELECT e FROM MigrationDTOSchuelerAnkreuzfloskeln e WHERE e.Jahr IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAnkreuzfloskeln.abschnitt", query = "SELECT e FROM MigrationDTOSchuelerAnkreuzfloskeln e WHERE e.Abschnitt = :value")
@NamedQuery(name = "MigrationDTOSchuelerAnkreuzfloskeln.abschnitt.multiple", query = "SELECT e FROM MigrationDTOSchuelerAnkreuzfloskeln e WHERE e.Abschnitt IN :value")
@NamedQuery(name = "MigrationDTOSchuelerAnkreuzfloskeln.primaryKeyQuery", query = "SELECT e FROM MigrationDTOSchuelerAnkreuzfloskeln e WHERE e.ID = ?1")
@NamedQuery(name = "MigrationDTOSchuelerAnkreuzfloskeln.primaryKeyQuery.multiple", query = "SELECT e FROM MigrationDTOSchuelerAnkreuzfloskeln e WHERE e.ID IN ?1")
@NamedQuery(name = "MigrationDTOSchuelerAnkreuzfloskeln.all.migration", query = "SELECT e FROM MigrationDTOSchuelerAnkreuzfloskeln e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Schueler_ID", "Abschnitt_ID", "Floskel_ID", "Stufe1", "Stufe2", "Stufe3", "Stufe4", "Stufe5", "SchulnrEigner", "Jahr", "Abschnitt"})
public final class MigrationDTOSchuelerAnkreuzfloskeln {

	/** ID des Ankreuzfloskeleintrags */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** DEPRECATED: Schüler-ID zum Ankreuzfloskeleintrag, in Abschnitt_ID enthalten */
	@Column(name = "Schueler_ID")
	@JsonProperty
	public Long Schueler_ID;

	/** ID der zugehörigen Schülerlernabschnittsdaten */
	@Column(name = "Abschnitt_ID")
	@JsonProperty
	public Long Abschnitt_ID;

	/** ID der Ankreuzfloskel aus dem Katalog */
	@Column(name = "Floskel_ID")
	@JsonProperty
	public Long Floskel_ID;

	/** Gibt an ob diese Stufe angekreuzt ist */
	@Column(name = "Stufe1")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Stufe1;

	/** Gibt an ob diese Stufe angekreuzt ist */
	@Column(name = "Stufe2")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Stufe2;

	/** Gibt an ob diese Stufe angekreuzt ist */
	@Column(name = "Stufe3")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Stufe3;

	/** Gibt an ob diese Stufe angekreuzt ist */
	@Column(name = "Stufe4")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Stufe4;

	/** Gibt an ob diese Stufe angekreuzt ist */
	@Column(name = "Stufe5")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Stufe5;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/** Schuljahr zum Ankreusfloskeleintrag */
	@Column(name = "Jahr")
	@JsonProperty
	public Integer Jahr;

	/** Abschnitt zum Ankreusfloskeleintrag */
	@Column(name = "Abschnitt")
	@JsonProperty
	public Integer Abschnitt;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuelerAnkreuzfloskeln ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOSchuelerAnkreuzfloskeln() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuelerAnkreuzfloskeln ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 * @param Abschnitt_ID   der Wert für das Attribut Abschnitt_ID
	 * @param Floskel_ID   der Wert für das Attribut Floskel_ID
	 * @param SchulnrEigner   der Wert für das Attribut SchulnrEigner
	 * @param Jahr   der Wert für das Attribut Jahr
	 * @param Abschnitt   der Wert für das Attribut Abschnitt
	 */
	public MigrationDTOSchuelerAnkreuzfloskeln(final Long ID, final Long Schueler_ID, final Long Abschnitt_ID, final Long Floskel_ID, final Integer SchulnrEigner, final Integer Jahr, final Integer Abschnitt) {
		if (ID == null) {
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Schueler_ID == null) {
			throw new NullPointerException("Schueler_ID must not be null");
		}
		this.Schueler_ID = Schueler_ID;
		if (Abschnitt_ID == null) {
			throw new NullPointerException("Abschnitt_ID must not be null");
		}
		this.Abschnitt_ID = Abschnitt_ID;
		if (Floskel_ID == null) {
			throw new NullPointerException("Floskel_ID must not be null");
		}
		this.Floskel_ID = Floskel_ID;
		if (SchulnrEigner == null) {
			throw new NullPointerException("SchulnrEigner must not be null");
		}
		this.SchulnrEigner = SchulnrEigner;
		if (Jahr == null) {
			throw new NullPointerException("Jahr must not be null");
		}
		this.Jahr = Jahr;
		if (Abschnitt == null) {
			throw new NullPointerException("Abschnitt must not be null");
		}
		this.Abschnitt = Abschnitt;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOSchuelerAnkreuzfloskeln other = (MigrationDTOSchuelerAnkreuzfloskeln) obj;
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
		return "MigrationDTOSchuelerAnkreuzfloskeln(ID=" + this.ID + ", Schueler_ID=" + this.Schueler_ID + ", Abschnitt_ID=" + this.Abschnitt_ID + ", Floskel_ID=" + this.Floskel_ID + ", Stufe1=" + this.Stufe1 + ", Stufe2=" + this.Stufe2 + ", Stufe3=" + this.Stufe3 + ", Stufe4=" + this.Stufe4 + ", Stufe5=" + this.Stufe5 + ", SchulnrEigner=" + this.SchulnrEigner + ", Jahr=" + this.Jahr + ", Abschnitt=" + this.Abschnitt + ")";
	}

}
