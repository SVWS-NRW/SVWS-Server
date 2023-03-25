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
 * Diese Klasse dient als DTO für die Datenbanktabelle K_Datenschutz.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "K_Datenschutz")
@NamedQuery(name="MigrationDTOKatalogDatenschutz.all", query="SELECT e FROM MigrationDTOKatalogDatenschutz e")
@NamedQuery(name="MigrationDTOKatalogDatenschutz.id", query="SELECT e FROM MigrationDTOKatalogDatenschutz e WHERE e.ID = :value")
@NamedQuery(name="MigrationDTOKatalogDatenschutz.id.multiple", query="SELECT e FROM MigrationDTOKatalogDatenschutz e WHERE e.ID IN :value")
@NamedQuery(name="MigrationDTOKatalogDatenschutz.bezeichnung", query="SELECT e FROM MigrationDTOKatalogDatenschutz e WHERE e.Bezeichnung = :value")
@NamedQuery(name="MigrationDTOKatalogDatenschutz.bezeichnung.multiple", query="SELECT e FROM MigrationDTOKatalogDatenschutz e WHERE e.Bezeichnung IN :value")
@NamedQuery(name="MigrationDTOKatalogDatenschutz.sichtbar", query="SELECT e FROM MigrationDTOKatalogDatenschutz e WHERE e.Sichtbar = :value")
@NamedQuery(name="MigrationDTOKatalogDatenschutz.sichtbar.multiple", query="SELECT e FROM MigrationDTOKatalogDatenschutz e WHERE e.Sichtbar IN :value")
@NamedQuery(name="MigrationDTOKatalogDatenschutz.schluessel", query="SELECT e FROM MigrationDTOKatalogDatenschutz e WHERE e.Schluessel = :value")
@NamedQuery(name="MigrationDTOKatalogDatenschutz.schluessel.multiple", query="SELECT e FROM MigrationDTOKatalogDatenschutz e WHERE e.Schluessel IN :value")
@NamedQuery(name="MigrationDTOKatalogDatenschutz.schulnreigner", query="SELECT e FROM MigrationDTOKatalogDatenschutz e WHERE e.SchulnrEigner = :value")
@NamedQuery(name="MigrationDTOKatalogDatenschutz.schulnreigner.multiple", query="SELECT e FROM MigrationDTOKatalogDatenschutz e WHERE e.SchulnrEigner IN :value")
@NamedQuery(name="MigrationDTOKatalogDatenschutz.sortierung", query="SELECT e FROM MigrationDTOKatalogDatenschutz e WHERE e.Sortierung = :value")
@NamedQuery(name="MigrationDTOKatalogDatenschutz.sortierung.multiple", query="SELECT e FROM MigrationDTOKatalogDatenschutz e WHERE e.Sortierung IN :value")
@NamedQuery(name="MigrationDTOKatalogDatenschutz.beschreibung", query="SELECT e FROM MigrationDTOKatalogDatenschutz e WHERE e.Beschreibung = :value")
@NamedQuery(name="MigrationDTOKatalogDatenschutz.beschreibung.multiple", query="SELECT e FROM MigrationDTOKatalogDatenschutz e WHERE e.Beschreibung IN :value")
@NamedQuery(name="MigrationDTOKatalogDatenschutz.personart", query="SELECT e FROM MigrationDTOKatalogDatenschutz e WHERE e.PersonArt = :value")
@NamedQuery(name="MigrationDTOKatalogDatenschutz.personart.multiple", query="SELECT e FROM MigrationDTOKatalogDatenschutz e WHERE e.PersonArt IN :value")
@NamedQuery(name="MigrationDTOKatalogDatenschutz.primaryKeyQuery", query="SELECT e FROM MigrationDTOKatalogDatenschutz e WHERE e.ID = ?1")
@NamedQuery(name="MigrationDTOKatalogDatenschutz.all.migration", query="SELECT e FROM MigrationDTOKatalogDatenschutz e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Bezeichnung","Sichtbar","Schluessel","SchulnrEigner","Sortierung","Beschreibung","PersonArt"})
public class MigrationDTOKatalogDatenschutz {

	/** Eindeutige ID für den Datensatz */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Eine kurze Bezeichnung des DSGVO-Merkmals */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Regelt die Sichtbarkeit des Merkmals bei der Ansicht der Schülertabelle  */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter=MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** Fest vorgegebene Werte, die es in Schild-NRW später ermöglichen, die DSGVO-Merkmale zu erkennen */
	@Column(name = "Schluessel")
	@JsonProperty
	public String Schluessel;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/** Gibt die Reihenfolge der Merkmale bei der Darstellung an. */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Eine ausführliche Beschreibung des DSGCO-Merkmals */
	@Column(name = "Beschreibung")
	@JsonProperty
	public String Beschreibung;

	/** PersonenArt des Datenschutz-Eintrags (S=Schueler L=Lehrer) */
	@Column(name = "PersonArt")
	@JsonProperty
	public String PersonArt;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOKatalogDatenschutz ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOKatalogDatenschutz() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOKatalogDatenschutz ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 * @param Sichtbar   der Wert für das Attribut Sichtbar
	 * @param SchulnrEigner   der Wert für das Attribut SchulnrEigner
	 * @param Sortierung   der Wert für das Attribut Sortierung
	 */
	public MigrationDTOKatalogDatenschutz(final Long ID, final String Bezeichnung, final Boolean Sichtbar, final Integer SchulnrEigner, final Integer Sortierung) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Bezeichnung == null) { 
			throw new NullPointerException("Bezeichnung must not be null");
		}
		this.Bezeichnung = Bezeichnung;
		if (Sichtbar == null) { 
			throw new NullPointerException("Sichtbar must not be null");
		}
		this.Sichtbar = Sichtbar;
		if (SchulnrEigner == null) { 
			throw new NullPointerException("SchulnrEigner must not be null");
		}
		this.SchulnrEigner = SchulnrEigner;
		if (Sortierung == null) { 
			throw new NullPointerException("Sortierung must not be null");
		}
		this.Sortierung = Sortierung;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOKatalogDatenschutz other = (MigrationDTOKatalogDatenschutz) obj;
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
		return "MigrationDTOKatalogDatenschutz(ID=" + this.ID + ", Bezeichnung=" + this.Bezeichnung + ", Sichtbar=" + this.Sichtbar + ", Schluessel=" + this.Schluessel + ", SchulnrEigner=" + this.SchulnrEigner + ", Sortierung=" + this.Sortierung + ", Beschreibung=" + this.Beschreibung + ", PersonArt=" + this.PersonArt + ")";
	}

}