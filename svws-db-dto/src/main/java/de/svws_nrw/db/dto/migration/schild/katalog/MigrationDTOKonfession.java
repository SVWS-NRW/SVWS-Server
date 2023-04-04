package de.svws_nrw.db.dto.migration.schild.katalog;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle K_Religion.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "K_Religion")
@NamedQuery(name = "MigrationDTOKonfession.all", query = "SELECT e FROM MigrationDTOKonfession e")
@NamedQuery(name = "MigrationDTOKonfession.id", query = "SELECT e FROM MigrationDTOKonfession e WHERE e.ID = :value")
@NamedQuery(name = "MigrationDTOKonfession.id.multiple", query = "SELECT e FROM MigrationDTOKonfession e WHERE e.ID IN :value")
@NamedQuery(name = "MigrationDTOKonfession.bezeichnung", query = "SELECT e FROM MigrationDTOKonfession e WHERE e.Bezeichnung = :value")
@NamedQuery(name = "MigrationDTOKonfession.bezeichnung.multiple", query = "SELECT e FROM MigrationDTOKonfession e WHERE e.Bezeichnung IN :value")
@NamedQuery(name = "MigrationDTOKonfession.statistikkrz", query = "SELECT e FROM MigrationDTOKonfession e WHERE e.StatistikKrz = :value")
@NamedQuery(name = "MigrationDTOKonfession.statistikkrz.multiple", query = "SELECT e FROM MigrationDTOKonfession e WHERE e.StatistikKrz IN :value")
@NamedQuery(name = "MigrationDTOKonfession.sortierung", query = "SELECT e FROM MigrationDTOKonfession e WHERE e.Sortierung = :value")
@NamedQuery(name = "MigrationDTOKonfession.sortierung.multiple", query = "SELECT e FROM MigrationDTOKonfession e WHERE e.Sortierung IN :value")
@NamedQuery(name = "MigrationDTOKonfession.sichtbar", query = "SELECT e FROM MigrationDTOKonfession e WHERE e.Sichtbar = :value")
@NamedQuery(name = "MigrationDTOKonfession.sichtbar.multiple", query = "SELECT e FROM MigrationDTOKonfession e WHERE e.Sichtbar IN :value")
@NamedQuery(name = "MigrationDTOKonfession.aenderbar", query = "SELECT e FROM MigrationDTOKonfession e WHERE e.Aenderbar = :value")
@NamedQuery(name = "MigrationDTOKonfession.aenderbar.multiple", query = "SELECT e FROM MigrationDTOKonfession e WHERE e.Aenderbar IN :value")
@NamedQuery(name = "MigrationDTOKonfession.exportbez", query = "SELECT e FROM MigrationDTOKonfession e WHERE e.ExportBez = :value")
@NamedQuery(name = "MigrationDTOKonfession.exportbez.multiple", query = "SELECT e FROM MigrationDTOKonfession e WHERE e.ExportBez IN :value")
@NamedQuery(name = "MigrationDTOKonfession.zeugnisbezeichnung", query = "SELECT e FROM MigrationDTOKonfession e WHERE e.ZeugnisBezeichnung = :value")
@NamedQuery(name = "MigrationDTOKonfession.zeugnisbezeichnung.multiple", query = "SELECT e FROM MigrationDTOKonfession e WHERE e.ZeugnisBezeichnung IN :value")
@NamedQuery(name = "MigrationDTOKonfession.schulnreigner", query = "SELECT e FROM MigrationDTOKonfession e WHERE e.SchulnrEigner = :value")
@NamedQuery(name = "MigrationDTOKonfession.schulnreigner.multiple", query = "SELECT e FROM MigrationDTOKonfession e WHERE e.SchulnrEigner IN :value")
@NamedQuery(name = "MigrationDTOKonfession.primaryKeyQuery", query = "SELECT e FROM MigrationDTOKonfession e WHERE e.ID = ?1")
@NamedQuery(name = "MigrationDTOKonfession.all.migration", query = "SELECT e FROM MigrationDTOKonfession e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Bezeichnung", "StatistikKrz", "Sortierung", "Sichtbar", "Aenderbar", "ExportBez", "ZeugnisBezeichnung", "SchulnrEigner"})
public final class MigrationDTOKonfession {

	/** ID der Religion */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Bezeichnung der Religion */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Statistikkürzel der Religion */
	@Column(name = "StatistikKrz")
	@JsonProperty
	public String StatistikKrz;

	/** Sortierung der Religion */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Sichbarkeit der Religion */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** Änderbarkeit der Religion */
	@Column(name = "Aenderbar")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Aenderbar;

	/** Exportbezeichnung der Religion */
	@Column(name = "ExportBez")
	@JsonProperty
	public String ExportBez;

	/** Zeugnisbezeichnung der Religion */
	@Column(name = "ZeugnisBezeichnung")
	@JsonProperty
	public String ZeugnisBezeichnung;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOKonfession ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOKonfession() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOKonfession ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 */
	public MigrationDTOKonfession(final Long ID, final String Bezeichnung) {
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
		MigrationDTOKonfession other = (MigrationDTOKonfession) obj;
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
		return "MigrationDTOKonfession(ID=" + this.ID + ", Bezeichnung=" + this.Bezeichnung + ", StatistikKrz=" + this.StatistikKrz + ", Sortierung=" + this.Sortierung + ", Sichtbar=" + this.Sichtbar + ", Aenderbar=" + this.Aenderbar + ", ExportBez=" + this.ExportBez + ", ZeugnisBezeichnung=" + this.ZeugnisBezeichnung + ", SchulnrEigner=" + this.SchulnrEigner + ")";
	}

}
