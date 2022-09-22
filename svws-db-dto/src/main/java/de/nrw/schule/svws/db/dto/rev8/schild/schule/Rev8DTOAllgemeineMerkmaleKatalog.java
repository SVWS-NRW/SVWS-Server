package de.nrw.schule.svws.db.dto.rev8.schild.schule;

import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.converter.current.Boolean01Converter;


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
import de.nrw.schule.svws.csv.converter.current.Boolean01ConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.Boolean01ConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle AllgemeineMerkmaleKatalog.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "AllgemeineMerkmaleKatalog")
@NamedQuery(name="Rev8DTOAllgemeineMerkmaleKatalog.all", query="SELECT e FROM Rev8DTOAllgemeineMerkmaleKatalog e")
@NamedQuery(name="Rev8DTOAllgemeineMerkmaleKatalog.id", query="SELECT e FROM Rev8DTOAllgemeineMerkmaleKatalog e WHERE e.ID = :value")
@NamedQuery(name="Rev8DTOAllgemeineMerkmaleKatalog.id.multiple", query="SELECT e FROM Rev8DTOAllgemeineMerkmaleKatalog e WHERE e.ID IN :value")
@NamedQuery(name="Rev8DTOAllgemeineMerkmaleKatalog.kuerzel", query="SELECT e FROM Rev8DTOAllgemeineMerkmaleKatalog e WHERE e.Kuerzel = :value")
@NamedQuery(name="Rev8DTOAllgemeineMerkmaleKatalog.kuerzel.multiple", query="SELECT e FROM Rev8DTOAllgemeineMerkmaleKatalog e WHERE e.Kuerzel IN :value")
@NamedQuery(name="Rev8DTOAllgemeineMerkmaleKatalog.bezeichnung", query="SELECT e FROM Rev8DTOAllgemeineMerkmaleKatalog e WHERE e.Bezeichnung = :value")
@NamedQuery(name="Rev8DTOAllgemeineMerkmaleKatalog.bezeichnung.multiple", query="SELECT e FROM Rev8DTOAllgemeineMerkmaleKatalog e WHERE e.Bezeichnung IN :value")
@NamedQuery(name="Rev8DTOAllgemeineMerkmaleKatalog.beischule", query="SELECT e FROM Rev8DTOAllgemeineMerkmaleKatalog e WHERE e.beiSchule = :value")
@NamedQuery(name="Rev8DTOAllgemeineMerkmaleKatalog.beischule.multiple", query="SELECT e FROM Rev8DTOAllgemeineMerkmaleKatalog e WHERE e.beiSchule IN :value")
@NamedQuery(name="Rev8DTOAllgemeineMerkmaleKatalog.beischueler", query="SELECT e FROM Rev8DTOAllgemeineMerkmaleKatalog e WHERE e.beiSchueler = :value")
@NamedQuery(name="Rev8DTOAllgemeineMerkmaleKatalog.beischueler.multiple", query="SELECT e FROM Rev8DTOAllgemeineMerkmaleKatalog e WHERE e.beiSchueler IN :value")
@NamedQuery(name="Rev8DTOAllgemeineMerkmaleKatalog.kuerzelasd", query="SELECT e FROM Rev8DTOAllgemeineMerkmaleKatalog e WHERE e.KuerzelASD = :value")
@NamedQuery(name="Rev8DTOAllgemeineMerkmaleKatalog.kuerzelasd.multiple", query="SELECT e FROM Rev8DTOAllgemeineMerkmaleKatalog e WHERE e.KuerzelASD IN :value")
@NamedQuery(name="Rev8DTOAllgemeineMerkmaleKatalog.gueltigvon", query="SELECT e FROM Rev8DTOAllgemeineMerkmaleKatalog e WHERE e.gueltigVon = :value")
@NamedQuery(name="Rev8DTOAllgemeineMerkmaleKatalog.gueltigvon.multiple", query="SELECT e FROM Rev8DTOAllgemeineMerkmaleKatalog e WHERE e.gueltigVon IN :value")
@NamedQuery(name="Rev8DTOAllgemeineMerkmaleKatalog.gueltigbis", query="SELECT e FROM Rev8DTOAllgemeineMerkmaleKatalog e WHERE e.gueltigBis = :value")
@NamedQuery(name="Rev8DTOAllgemeineMerkmaleKatalog.gueltigbis.multiple", query="SELECT e FROM Rev8DTOAllgemeineMerkmaleKatalog e WHERE e.gueltigBis IN :value")
@NamedQuery(name="Rev8DTOAllgemeineMerkmaleKatalog.primaryKeyQuery", query="SELECT e FROM Rev8DTOAllgemeineMerkmaleKatalog e WHERE e.ID = ?1")
@NamedQuery(name="Rev8DTOAllgemeineMerkmaleKatalog.all.migration", query="SELECT e FROM Rev8DTOAllgemeineMerkmaleKatalog e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Kuerzel","Bezeichnung","beiSchule","beiSchueler","KuerzelASD","gueltigVon","gueltigBis"})
public class Rev8DTOAllgemeineMerkmaleKatalog {

	/** ID des allgemeinen Merkmals bei Schulen und/oder Schülern */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Das Kürzel des allgemeinen Merkmals bei Schulen und/oder Schülern */
	@Column(name = "Kuerzel")
	@JsonProperty
	public String Kuerzel;

	/** Die texttuelle Beschreibung des allgemeinen Merkmals bei Schulen und/oder Schülern */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Gibt an, das das Merkmal bei der Schule gesetzt werden kann */
	@Column(name = "beiSchule")
	@JsonProperty
	@Convert(converter=Boolean01Converter.class)
	@JsonSerialize(using=Boolean01ConverterSerializer.class)
	@JsonDeserialize(using=Boolean01ConverterDeserializer.class)
	public Boolean beiSchule;

	/** Gibt an, das das Merkmal bei einem Schüler gesetzt werden kann */
	@Column(name = "beiSchueler")
	@JsonProperty
	@Convert(converter=Boolean01Converter.class)
	@JsonSerialize(using=Boolean01ConverterSerializer.class)
	@JsonDeserialize(using=Boolean01ConverterDeserializer.class)
	public Boolean beiSchueler;

	/** ggf. ein Kürzel, welches im Rahmen der amtlichen Schulstatistik verwendet wird */
	@Column(name = "KuerzelASD")
	@JsonProperty
	public String KuerzelASD;

	/** Gibt die Gültigkeit ab welchem Schuljahr an */
	@Column(name = "gueltigVon")
	@JsonProperty
	public Integer gueltigVon;

	/** Gibt die Gültigkeit bis zu welchem Schuljahr an */
	@Column(name = "gueltigBis")
	@JsonProperty
	public Integer gueltigBis;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOAllgemeineMerkmaleKatalog ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev8DTOAllgemeineMerkmaleKatalog() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOAllgemeineMerkmaleKatalog ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Kuerzel   der Wert für das Attribut Kuerzel
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 * @param beiSchule   der Wert für das Attribut beiSchule
	 * @param beiSchueler   der Wert für das Attribut beiSchueler
	 */
	public Rev8DTOAllgemeineMerkmaleKatalog(final Long ID, final String Kuerzel, final String Bezeichnung, final Boolean beiSchule, final Boolean beiSchueler) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Kuerzel == null) { 
			throw new NullPointerException("Kuerzel must not be null");
		}
		this.Kuerzel = Kuerzel;
		if (Bezeichnung == null) { 
			throw new NullPointerException("Bezeichnung must not be null");
		}
		this.Bezeichnung = Bezeichnung;
		if (beiSchule == null) { 
			throw new NullPointerException("beiSchule must not be null");
		}
		this.beiSchule = beiSchule;
		if (beiSchueler == null) { 
			throw new NullPointerException("beiSchueler must not be null");
		}
		this.beiSchueler = beiSchueler;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev8DTOAllgemeineMerkmaleKatalog other = (Rev8DTOAllgemeineMerkmaleKatalog) obj;
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
		return "Rev8DTOAllgemeineMerkmaleKatalog(ID=" + this.ID + ", Kuerzel=" + this.Kuerzel + ", Bezeichnung=" + this.Bezeichnung + ", beiSchule=" + this.beiSchule + ", beiSchueler=" + this.beiSchueler + ", KuerzelASD=" + this.KuerzelASD + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ")";
	}

}