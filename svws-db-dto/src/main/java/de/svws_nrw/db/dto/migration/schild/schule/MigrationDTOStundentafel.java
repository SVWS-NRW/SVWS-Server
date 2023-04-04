package de.svws_nrw.db.dto.migration.schild.schule;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverter;


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
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverterSerializer;
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Stundentafel.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Stundentafel")
@NamedQuery(name = "MigrationDTOStundentafel.all", query = "SELECT e FROM MigrationDTOStundentafel e")
@NamedQuery(name = "MigrationDTOStundentafel.id", query = "SELECT e FROM MigrationDTOStundentafel e WHERE e.ID = :value")
@NamedQuery(name = "MigrationDTOStundentafel.id.multiple", query = "SELECT e FROM MigrationDTOStundentafel e WHERE e.ID IN :value")
@NamedQuery(name = "MigrationDTOStundentafel.bezeichnung", query = "SELECT e FROM MigrationDTOStundentafel e WHERE e.Bezeichnung = :value")
@NamedQuery(name = "MigrationDTOStundentafel.bezeichnung.multiple", query = "SELECT e FROM MigrationDTOStundentafel e WHERE e.Bezeichnung IN :value")
@NamedQuery(name = "MigrationDTOStundentafel.jahrgang_id", query = "SELECT e FROM MigrationDTOStundentafel e WHERE e.Jahrgang_ID = :value")
@NamedQuery(name = "MigrationDTOStundentafel.jahrgang_id.multiple", query = "SELECT e FROM MigrationDTOStundentafel e WHERE e.Jahrgang_ID IN :value")
@NamedQuery(name = "MigrationDTOStundentafel.asdjahrgang", query = "SELECT e FROM MigrationDTOStundentafel e WHERE e.ASDJahrgang = :value")
@NamedQuery(name = "MigrationDTOStundentafel.asdjahrgang.multiple", query = "SELECT e FROM MigrationDTOStundentafel e WHERE e.ASDJahrgang IN :value")
@NamedQuery(name = "MigrationDTOStundentafel.sgl", query = "SELECT e FROM MigrationDTOStundentafel e WHERE e.SGL = :value")
@NamedQuery(name = "MigrationDTOStundentafel.sgl.multiple", query = "SELECT e FROM MigrationDTOStundentafel e WHERE e.SGL IN :value")
@NamedQuery(name = "MigrationDTOStundentafel.fachklasse_id", query = "SELECT e FROM MigrationDTOStundentafel e WHERE e.Fachklasse_ID = :value")
@NamedQuery(name = "MigrationDTOStundentafel.fachklasse_id.multiple", query = "SELECT e FROM MigrationDTOStundentafel e WHERE e.Fachklasse_ID IN :value")
@NamedQuery(name = "MigrationDTOStundentafel.sichtbar", query = "SELECT e FROM MigrationDTOStundentafel e WHERE e.Sichtbar = :value")
@NamedQuery(name = "MigrationDTOStundentafel.sichtbar.multiple", query = "SELECT e FROM MigrationDTOStundentafel e WHERE e.Sichtbar IN :value")
@NamedQuery(name = "MigrationDTOStundentafel.schulnreigner", query = "SELECT e FROM MigrationDTOStundentafel e WHERE e.SchulnrEigner = :value")
@NamedQuery(name = "MigrationDTOStundentafel.schulnreigner.multiple", query = "SELECT e FROM MigrationDTOStundentafel e WHERE e.SchulnrEigner IN :value")
@NamedQuery(name = "MigrationDTOStundentafel.sortierung", query = "SELECT e FROM MigrationDTOStundentafel e WHERE e.Sortierung = :value")
@NamedQuery(name = "MigrationDTOStundentafel.sortierung.multiple", query = "SELECT e FROM MigrationDTOStundentafel e WHERE e.Sortierung IN :value")
@NamedQuery(name = "MigrationDTOStundentafel.primaryKeyQuery", query = "SELECT e FROM MigrationDTOStundentafel e WHERE e.ID = ?1")
@NamedQuery(name = "MigrationDTOStundentafel.all.migration", query = "SELECT e FROM MigrationDTOStundentafel e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Bezeichnung", "Jahrgang_ID", "ASDJahrgang", "SGL", "Fachklasse_ID", "Sichtbar", "SchulnrEigner", "Sortierung"})
public final class MigrationDTOStundentafel {

	/** ID der Stundentafel */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Bezeichnung der Stundentafel */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** JahrgangsID der Stundentafel */
	@Column(name = "Jahrgang_ID")
	@JsonProperty
	public Long Jahrgang_ID;

	/** ASD-Jahrgang der Stundentafel */
	@Column(name = "ASDJahrgang")
	@JsonProperty
	public String ASDJahrgang;

	/** SGL der Stundentafel */
	@Column(name = "SGL")
	@JsonProperty
	public String SGL;

	/** FachklassenID der Stundentafel */
	@Column(name = "Fachklasse_ID")
	@JsonProperty
	public Long Fachklasse_ID;

	/** Sichtbarkeit der Stundentafel */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/** Sortierungnummer  der Stundentafel */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOStundentafel ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOStundentafel() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOStundentafel ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 */
	public MigrationDTOStundentafel(final Long ID, final String Bezeichnung) {
		if (ID == null) {
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Bezeichnung == null) {
			throw new NullPointerException("Bezeichnung must not be null");
		}
		this.Bezeichnung = Bezeichnung;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOStundentafel other = (MigrationDTOStundentafel) obj;
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
		return "MigrationDTOStundentafel(ID=" + this.ID + ", Bezeichnung=" + this.Bezeichnung + ", Jahrgang_ID=" + this.Jahrgang_ID + ", ASDJahrgang=" + this.ASDJahrgang + ", SGL=" + this.SGL + ", Fachklasse_ID=" + this.Fachklasse_ID + ", Sichtbar=" + this.Sichtbar + ", SchulnrEigner=" + this.SchulnrEigner + ", Sortierung=" + this.Sortierung + ")";
	}

}
