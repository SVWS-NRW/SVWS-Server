package de.svws_nrw.db.dto.current.schild.schule;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.Boolean01Converter;


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
import de.svws_nrw.csv.converter.current.Boolean01ConverterSerializer;
import de.svws_nrw.csv.converter.current.Boolean01ConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Schulformen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Schulformen")
@NamedQuery(name = "DTOAlleSchulformen.all", query = "SELECT e FROM DTOAlleSchulformen e")
@NamedQuery(name = "DTOAlleSchulformen.id", query = "SELECT e FROM DTOAlleSchulformen e WHERE e.ID = :value")
@NamedQuery(name = "DTOAlleSchulformen.id.multiple", query = "SELECT e FROM DTOAlleSchulformen e WHERE e.ID IN :value")
@NamedQuery(name = "DTOAlleSchulformen.kuerzel", query = "SELECT e FROM DTOAlleSchulformen e WHERE e.Kuerzel = :value")
@NamedQuery(name = "DTOAlleSchulformen.kuerzel.multiple", query = "SELECT e FROM DTOAlleSchulformen e WHERE e.Kuerzel IN :value")
@NamedQuery(name = "DTOAlleSchulformen.nummer", query = "SELECT e FROM DTOAlleSchulformen e WHERE e.Nummer = :value")
@NamedQuery(name = "DTOAlleSchulformen.nummer.multiple", query = "SELECT e FROM DTOAlleSchulformen e WHERE e.Nummer IN :value")
@NamedQuery(name = "DTOAlleSchulformen.bezeichnung", query = "SELECT e FROM DTOAlleSchulformen e WHERE e.Bezeichnung = :value")
@NamedQuery(name = "DTOAlleSchulformen.bezeichnung.multiple", query = "SELECT e FROM DTOAlleSchulformen e WHERE e.Bezeichnung IN :value")
@NamedQuery(name = "DTOAlleSchulformen.hatgymob", query = "SELECT e FROM DTOAlleSchulformen e WHERE e.HatGymOb = :value")
@NamedQuery(name = "DTOAlleSchulformen.hatgymob.multiple", query = "SELECT e FROM DTOAlleSchulformen e WHERE e.HatGymOb IN :value")
@NamedQuery(name = "DTOAlleSchulformen.gueltigvon", query = "SELECT e FROM DTOAlleSchulformen e WHERE e.gueltigVon = :value")
@NamedQuery(name = "DTOAlleSchulformen.gueltigvon.multiple", query = "SELECT e FROM DTOAlleSchulformen e WHERE e.gueltigVon IN :value")
@NamedQuery(name = "DTOAlleSchulformen.gueltigbis", query = "SELECT e FROM DTOAlleSchulformen e WHERE e.gueltigBis = :value")
@NamedQuery(name = "DTOAlleSchulformen.gueltigbis.multiple", query = "SELECT e FROM DTOAlleSchulformen e WHERE e.gueltigBis IN :value")
@NamedQuery(name = "DTOAlleSchulformen.primaryKeyQuery", query = "SELECT e FROM DTOAlleSchulformen e WHERE e.ID = ?1")
@NamedQuery(name = "DTOAlleSchulformen.all.migration", query = "SELECT e FROM DTOAlleSchulformen e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Kuerzel", "Nummer", "Bezeichnung", "HatGymOb", "gueltigVon", "gueltigBis"})
public final class DTOAlleSchulformen {

	/** ID der Schulform */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Kürzel der Schulform */
	@Column(name = "Kuerzel")
	@JsonProperty
	public String Kuerzel;

	/** Nummer der Schulform für die amtliche Schulstatistik */
	@Column(name = "Nummer")
	@JsonProperty
	public String Nummer;

	/** Bezeichnung der Schulform */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Gibt an, ob eine Schule der Schulform eine gymnasiale Oberstufe haben kann (1) oder nicht (0) */
	@Column(name = "HatGymOb")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean HatGymOb;

	/** Gibt an, ab welchem Schuljahr die Schulform gültig ist */
	@Column(name = "gueltigVon")
	@JsonProperty
	public Integer gueltigVon;

	/** Gibt an, bis zu welchem Schuljahr die Schulform gültig ist */
	@Column(name = "gueltigBis")
	@JsonProperty
	public Integer gueltigBis;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOAlleSchulformen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOAlleSchulformen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOAlleSchulformen ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Kuerzel   der Wert für das Attribut Kuerzel
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 * @param HatGymOb   der Wert für das Attribut HatGymOb
	 */
	public DTOAlleSchulformen(final Long ID, final String Kuerzel, final String Bezeichnung, final Boolean HatGymOb) {
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
		if (HatGymOb == null) {
			throw new NullPointerException("HatGymOb must not be null");
		}
		this.HatGymOb = HatGymOb;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOAlleSchulformen other = (DTOAlleSchulformen) obj;
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
		return "DTOAlleSchulformen(ID=" + this.ID + ", Kuerzel=" + this.Kuerzel + ", Nummer=" + this.Nummer + ", Bezeichnung=" + this.Bezeichnung + ", HatGymOb=" + this.HatGymOb + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ")";
	}

}
