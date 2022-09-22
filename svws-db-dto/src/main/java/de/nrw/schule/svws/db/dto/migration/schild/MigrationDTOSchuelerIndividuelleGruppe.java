package de.nrw.schule.svws.db.dto.migration.schild;

import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverter;


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
import de.nrw.schule.svws.csv.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverterSerializer;
import de.nrw.schule.svws.csv.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerListe.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerListe")
@NamedQuery(name="MigrationDTOSchuelerIndividuelleGruppe.all", query="SELECT e FROM MigrationDTOSchuelerIndividuelleGruppe e")
@NamedQuery(name="MigrationDTOSchuelerIndividuelleGruppe.id", query="SELECT e FROM MigrationDTOSchuelerIndividuelleGruppe e WHERE e.ID = :value")
@NamedQuery(name="MigrationDTOSchuelerIndividuelleGruppe.id.multiple", query="SELECT e FROM MigrationDTOSchuelerIndividuelleGruppe e WHERE e.ID IN :value")
@NamedQuery(name="MigrationDTOSchuelerIndividuelleGruppe.bezeichnung", query="SELECT e FROM MigrationDTOSchuelerIndividuelleGruppe e WHERE e.Bezeichnung = :value")
@NamedQuery(name="MigrationDTOSchuelerIndividuelleGruppe.bezeichnung.multiple", query="SELECT e FROM MigrationDTOSchuelerIndividuelleGruppe e WHERE e.Bezeichnung IN :value")
@NamedQuery(name="MigrationDTOSchuelerIndividuelleGruppe.erzeuger", query="SELECT e FROM MigrationDTOSchuelerIndividuelleGruppe e WHERE e.Erzeuger = :value")
@NamedQuery(name="MigrationDTOSchuelerIndividuelleGruppe.erzeuger.multiple", query="SELECT e FROM MigrationDTOSchuelerIndividuelleGruppe e WHERE e.Erzeuger IN :value")
@NamedQuery(name="MigrationDTOSchuelerIndividuelleGruppe.privat", query="SELECT e FROM MigrationDTOSchuelerIndividuelleGruppe e WHERE e.Privat = :value")
@NamedQuery(name="MigrationDTOSchuelerIndividuelleGruppe.privat.multiple", query="SELECT e FROM MigrationDTOSchuelerIndividuelleGruppe e WHERE e.Privat IN :value")
@NamedQuery(name="MigrationDTOSchuelerIndividuelleGruppe.schulnreigner", query="SELECT e FROM MigrationDTOSchuelerIndividuelleGruppe e WHERE e.SchulnrEigner = :value")
@NamedQuery(name="MigrationDTOSchuelerIndividuelleGruppe.schulnreigner.multiple", query="SELECT e FROM MigrationDTOSchuelerIndividuelleGruppe e WHERE e.SchulnrEigner IN :value")
@NamedQuery(name="MigrationDTOSchuelerIndividuelleGruppe.primaryKeyQuery", query="SELECT e FROM MigrationDTOSchuelerIndividuelleGruppe e WHERE e.ID = ?1")
@NamedQuery(name="MigrationDTOSchuelerIndividuelleGruppe.all.migration", query="SELECT e FROM MigrationDTOSchuelerIndividuelleGruppe e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Bezeichnung","Erzeuger","Privat","SchulnrEigner"})
public class MigrationDTOSchuelerIndividuelleGruppe {

	/** ID der individuellen Schülerliste */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Bezeichnung der individuellen Schülerliste */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** UserID Erzeuger der individuellen Schülerliste */
	@Column(name = "Erzeuger")
	@JsonProperty
	public String Erzeuger;

	/** Schülerliste Privat oder Öffentlich */
	@Column(name = "Privat")
	@JsonProperty
	@Convert(converter=MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Privat;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuelerIndividuelleGruppe ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOSchuelerIndividuelleGruppe() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuelerIndividuelleGruppe ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 */
	public MigrationDTOSchuelerIndividuelleGruppe(final Long ID, final String Bezeichnung) {
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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOSchuelerIndividuelleGruppe other = (MigrationDTOSchuelerIndividuelleGruppe) obj;
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
		return "MigrationDTOSchuelerIndividuelleGruppe(ID=" + this.ID + ", Bezeichnung=" + this.Bezeichnung + ", Erzeuger=" + this.Erzeuger + ", Privat=" + this.Privat + ", SchulnrEigner=" + this.SchulnrEigner + ")";
	}

}