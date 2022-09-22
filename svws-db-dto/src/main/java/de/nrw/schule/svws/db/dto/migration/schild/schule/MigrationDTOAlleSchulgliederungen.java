package de.nrw.schule.svws.db.dto.migration.schild.schule;

import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.converter.migration.MigrationBoolean01Converter;


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
import de.nrw.schule.svws.csv.converter.migration.MigrationBoolean01ConverterSerializer;
import de.nrw.schule.svws.csv.converter.migration.MigrationBoolean01ConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Schulgliederungen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Schulgliederungen")
@NamedQuery(name="MigrationDTOAlleSchulgliederungen.all", query="SELECT e FROM MigrationDTOAlleSchulgliederungen e")
@NamedQuery(name="MigrationDTOAlleSchulgliederungen.id", query="SELECT e FROM MigrationDTOAlleSchulgliederungen e WHERE e.ID = :value")
@NamedQuery(name="MigrationDTOAlleSchulgliederungen.id.multiple", query="SELECT e FROM MigrationDTOAlleSchulgliederungen e WHERE e.ID IN :value")
@NamedQuery(name="MigrationDTOAlleSchulgliederungen.kuerzel", query="SELECT e FROM MigrationDTOAlleSchulgliederungen e WHERE e.Kuerzel = :value")
@NamedQuery(name="MigrationDTOAlleSchulgliederungen.kuerzel.multiple", query="SELECT e FROM MigrationDTOAlleSchulgliederungen e WHERE e.Kuerzel IN :value")
@NamedQuery(name="MigrationDTOAlleSchulgliederungen.istbkbildungsgang", query="SELECT e FROM MigrationDTOAlleSchulgliederungen e WHERE e.IstBKBildungsgang = :value")
@NamedQuery(name="MigrationDTOAlleSchulgliederungen.istbkbildungsgang.multiple", query="SELECT e FROM MigrationDTOAlleSchulgliederungen e WHERE e.IstBKBildungsgang IN :value")
@NamedQuery(name="MigrationDTOAlleSchulgliederungen.istauslaufend", query="SELECT e FROM MigrationDTOAlleSchulgliederungen e WHERE e.IstAuslaufend = :value")
@NamedQuery(name="MigrationDTOAlleSchulgliederungen.istauslaufend.multiple", query="SELECT e FROM MigrationDTOAlleSchulgliederungen e WHERE e.IstAuslaufend IN :value")
@NamedQuery(name="MigrationDTOAlleSchulgliederungen.istausgelaufen", query="SELECT e FROM MigrationDTOAlleSchulgliederungen e WHERE e.IstAusgelaufen = :value")
@NamedQuery(name="MigrationDTOAlleSchulgliederungen.istausgelaufen.multiple", query="SELECT e FROM MigrationDTOAlleSchulgliederungen e WHERE e.IstAusgelaufen IN :value")
@NamedQuery(name="MigrationDTOAlleSchulgliederungen.bezeichnung", query="SELECT e FROM MigrationDTOAlleSchulgliederungen e WHERE e.Bezeichnung = :value")
@NamedQuery(name="MigrationDTOAlleSchulgliederungen.bezeichnung.multiple", query="SELECT e FROM MigrationDTOAlleSchulgliederungen e WHERE e.Bezeichnung IN :value")
@NamedQuery(name="MigrationDTOAlleSchulgliederungen.bkanlage", query="SELECT e FROM MigrationDTOAlleSchulgliederungen e WHERE e.BKAnlage = :value")
@NamedQuery(name="MigrationDTOAlleSchulgliederungen.bkanlage.multiple", query="SELECT e FROM MigrationDTOAlleSchulgliederungen e WHERE e.BKAnlage IN :value")
@NamedQuery(name="MigrationDTOAlleSchulgliederungen.bktyp", query="SELECT e FROM MigrationDTOAlleSchulgliederungen e WHERE e.BKTyp = :value")
@NamedQuery(name="MigrationDTOAlleSchulgliederungen.bktyp.multiple", query="SELECT e FROM MigrationDTOAlleSchulgliederungen e WHERE e.BKTyp IN :value")
@NamedQuery(name="MigrationDTOAlleSchulgliederungen.bkindex", query="SELECT e FROM MigrationDTOAlleSchulgliederungen e WHERE e.BKIndex = :value")
@NamedQuery(name="MigrationDTOAlleSchulgliederungen.bkindex.multiple", query="SELECT e FROM MigrationDTOAlleSchulgliederungen e WHERE e.BKIndex IN :value")
@NamedQuery(name="MigrationDTOAlleSchulgliederungen.istvz", query="SELECT e FROM MigrationDTOAlleSchulgliederungen e WHERE e.IstVZ = :value")
@NamedQuery(name="MigrationDTOAlleSchulgliederungen.istvz.multiple", query="SELECT e FROM MigrationDTOAlleSchulgliederungen e WHERE e.IstVZ IN :value")
@NamedQuery(name="MigrationDTOAlleSchulgliederungen.gueltigvon", query="SELECT e FROM MigrationDTOAlleSchulgliederungen e WHERE e.gueltigVon = :value")
@NamedQuery(name="MigrationDTOAlleSchulgliederungen.gueltigvon.multiple", query="SELECT e FROM MigrationDTOAlleSchulgliederungen e WHERE e.gueltigVon IN :value")
@NamedQuery(name="MigrationDTOAlleSchulgliederungen.gueltigbis", query="SELECT e FROM MigrationDTOAlleSchulgliederungen e WHERE e.gueltigBis = :value")
@NamedQuery(name="MigrationDTOAlleSchulgliederungen.gueltigbis.multiple", query="SELECT e FROM MigrationDTOAlleSchulgliederungen e WHERE e.gueltigBis IN :value")
@NamedQuery(name="MigrationDTOAlleSchulgliederungen.primaryKeyQuery", query="SELECT e FROM MigrationDTOAlleSchulgliederungen e WHERE e.ID = ?1")
@NamedQuery(name="MigrationDTOAlleSchulgliederungen.all.migration", query="SELECT e FROM MigrationDTOAlleSchulgliederungen e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Kuerzel","IstBKBildungsgang","IstAuslaufend","IstAusgelaufen","Bezeichnung","BKAnlage","BKTyp","BKIndex","IstVZ","gueltigVon","gueltigBis"})
public class MigrationDTOAlleSchulgliederungen {

	/** ID der Schulgliederung */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Kürzel der Schulgliederung */
	@Column(name = "Kuerzel")
	@JsonProperty
	public String Kuerzel;

	/** Gibt an, ob die Schulgliederung einen Bildungsgang am Berufskollegs darstellt (1) oder nicht (0) */
	@Column(name = "IstBKBildungsgang")
	@JsonProperty
	@Convert(converter=MigrationBoolean01Converter.class)
	@JsonSerialize(using=MigrationBoolean01ConverterSerializer.class)
	@JsonDeserialize(using=MigrationBoolean01ConverterDeserializer.class)
	public Boolean IstBKBildungsgang;

	/** Gibt an, ob die Schulgliederung auslaufend ist (1) oder nicht (0) */
	@Column(name = "IstAuslaufend")
	@JsonProperty
	@Convert(converter=MigrationBoolean01Converter.class)
	@JsonSerialize(using=MigrationBoolean01ConverterSerializer.class)
	@JsonDeserialize(using=MigrationBoolean01ConverterDeserializer.class)
	public Boolean IstAuslaufend;

	/** Gibt an, ob die Schulgliederung ausgelaufen ist (1) oder nicht (0) */
	@Column(name = "IstAusgelaufen")
	@JsonProperty
	@Convert(converter=MigrationBoolean01Converter.class)
	@JsonSerialize(using=MigrationBoolean01ConverterSerializer.class)
	@JsonDeserialize(using=MigrationBoolean01ConverterDeserializer.class)
	public Boolean IstAusgelaufen;

	/** Bezeichnung der Schulgliederung */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Die Anlage, falls es sich um einen Bildungsgang am Berufskolleg handelt */
	@Column(name = "BKAnlage")
	@JsonProperty
	public String BKAnlage;

	/** Der Typ der Anlage, falls es sich um einen Bildungsgang am Berufskolleg handelt */
	@Column(name = "BKTyp")
	@JsonProperty
	public String BKTyp;

	/** Der Index für den Zugriff auf die Fachklassen, falls es sich um einen Bildungsgang am Berufskolleg handelt */
	@Column(name = "BKIndex")
	@JsonProperty
	public Integer BKIndex;

	/** Gibt an, ob es sich um einen Bildungsgang in Vollzeit handelt (1) oder nicht (0) */
	@Column(name = "IstVZ")
	@JsonProperty
	@Convert(converter=MigrationBoolean01Converter.class)
	@JsonSerialize(using=MigrationBoolean01ConverterSerializer.class)
	@JsonDeserialize(using=MigrationBoolean01ConverterDeserializer.class)
	public Boolean IstVZ;

	/** Gibt an, ab welchem Schuljahr die Schulgliederung gültig ist */
	@Column(name = "gueltigVon")
	@JsonProperty
	public Integer gueltigVon;

	/** Gibt an, bis zu welchem Schuljahr die Schulgliederungen gültig ist */
	@Column(name = "gueltigBis")
	@JsonProperty
	public Integer gueltigBis;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOAlleSchulgliederungen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOAlleSchulgliederungen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOAlleSchulgliederungen ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Kuerzel   der Wert für das Attribut Kuerzel
	 * @param IstBKBildungsgang   der Wert für das Attribut IstBKBildungsgang
	 * @param IstAuslaufend   der Wert für das Attribut IstAuslaufend
	 * @param IstAusgelaufen   der Wert für das Attribut IstAusgelaufen
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 * @param IstVZ   der Wert für das Attribut IstVZ
	 */
	public MigrationDTOAlleSchulgliederungen(final Long ID, final String Kuerzel, final Boolean IstBKBildungsgang, final Boolean IstAuslaufend, final Boolean IstAusgelaufen, final String Bezeichnung, final Boolean IstVZ) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Kuerzel == null) { 
			throw new NullPointerException("Kuerzel must not be null");
		}
		this.Kuerzel = Kuerzel;
		if (IstBKBildungsgang == null) { 
			throw new NullPointerException("IstBKBildungsgang must not be null");
		}
		this.IstBKBildungsgang = IstBKBildungsgang;
		if (IstAuslaufend == null) { 
			throw new NullPointerException("IstAuslaufend must not be null");
		}
		this.IstAuslaufend = IstAuslaufend;
		if (IstAusgelaufen == null) { 
			throw new NullPointerException("IstAusgelaufen must not be null");
		}
		this.IstAusgelaufen = IstAusgelaufen;
		if (Bezeichnung == null) { 
			throw new NullPointerException("Bezeichnung must not be null");
		}
		this.Bezeichnung = Bezeichnung;
		if (IstVZ == null) { 
			throw new NullPointerException("IstVZ must not be null");
		}
		this.IstVZ = IstVZ;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOAlleSchulgliederungen other = (MigrationDTOAlleSchulgliederungen) obj;
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
		return "MigrationDTOAlleSchulgliederungen(ID=" + this.ID + ", Kuerzel=" + this.Kuerzel + ", IstBKBildungsgang=" + this.IstBKBildungsgang + ", IstAuslaufend=" + this.IstAuslaufend + ", IstAusgelaufen=" + this.IstAusgelaufen + ", Bezeichnung=" + this.Bezeichnung + ", BKAnlage=" + this.BKAnlage + ", BKTyp=" + this.BKTyp + ", BKIndex=" + this.BKIndex + ", IstVZ=" + this.IstVZ + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ")";
	}

}