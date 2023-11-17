package de.svws_nrw.db.dto.migration.schild.benutzer;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.migration.MigrationBoolean01Converter;


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
import de.svws_nrw.csv.converter.migration.MigrationBoolean01ConverterSerializer;
import de.svws_nrw.csv.converter.migration.MigrationBoolean01ConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Benutzergruppen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Benutzergruppen")
@NamedQuery(name = "MigrationDTOBenutzergruppe.all", query = "SELECT e FROM MigrationDTOBenutzergruppe e")
@NamedQuery(name = "MigrationDTOBenutzergruppe.id", query = "SELECT e FROM MigrationDTOBenutzergruppe e WHERE e.ID = :value")
@NamedQuery(name = "MigrationDTOBenutzergruppe.id.multiple", query = "SELECT e FROM MigrationDTOBenutzergruppe e WHERE e.ID IN :value")
@NamedQuery(name = "MigrationDTOBenutzergruppe.bezeichnung", query = "SELECT e FROM MigrationDTOBenutzergruppe e WHERE e.Bezeichnung = :value")
@NamedQuery(name = "MigrationDTOBenutzergruppe.bezeichnung.multiple", query = "SELECT e FROM MigrationDTOBenutzergruppe e WHERE e.Bezeichnung IN :value")
@NamedQuery(name = "MigrationDTOBenutzergruppe.istadmin", query = "SELECT e FROM MigrationDTOBenutzergruppe e WHERE e.IstAdmin = :value")
@NamedQuery(name = "MigrationDTOBenutzergruppe.istadmin.multiple", query = "SELECT e FROM MigrationDTOBenutzergruppe e WHERE e.IstAdmin IN :value")
@NamedQuery(name = "MigrationDTOBenutzergruppe.primaryKeyQuery", query = "SELECT e FROM MigrationDTOBenutzergruppe e WHERE e.ID = ?1")
@NamedQuery(name = "MigrationDTOBenutzergruppe.primaryKeyQuery.multiple", query = "SELECT e FROM MigrationDTOBenutzergruppe e WHERE e.ID IN :value")
@NamedQuery(name = "MigrationDTOBenutzergruppe.all.migration", query = "SELECT e FROM MigrationDTOBenutzergruppe e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Bezeichnung", "IstAdmin"})
public final class MigrationDTOBenutzergruppe {

	/** Die ID der Benutzergruppe */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Die Bezeichnung der Benutzergruppe */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Gibt an, ob die Benutzergruppe Administrator-Rechte hat (1) oder nicht (0) */
	@Column(name = "IstAdmin")
	@JsonProperty
	@Convert(converter = MigrationBoolean01Converter.class)
	@JsonSerialize(using = MigrationBoolean01ConverterSerializer.class)
	@JsonDeserialize(using = MigrationBoolean01ConverterDeserializer.class)
	public Boolean IstAdmin;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOBenutzergruppe ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOBenutzergruppe() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOBenutzergruppe ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 * @param IstAdmin   der Wert für das Attribut IstAdmin
	 */
	public MigrationDTOBenutzergruppe(final Long ID, final String Bezeichnung, final Boolean IstAdmin) {
		if (ID == null) {
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Bezeichnung == null) {
			throw new NullPointerException("Bezeichnung must not be null");
		}
		this.Bezeichnung = Bezeichnung;
		if (IstAdmin == null) {
			throw new NullPointerException("IstAdmin must not be null");
		}
		this.IstAdmin = IstAdmin;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOBenutzergruppe other = (MigrationDTOBenutzergruppe) obj;
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
		return "MigrationDTOBenutzergruppe(ID=" + this.ID + ", Bezeichnung=" + this.Bezeichnung + ", IstAdmin=" + this.IstAdmin + ")";
	}

}
