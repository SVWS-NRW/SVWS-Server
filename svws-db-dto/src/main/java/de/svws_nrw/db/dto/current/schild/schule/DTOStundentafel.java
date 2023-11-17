package de.svws_nrw.db.dto.current.schild.schule;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultPlusConverter;


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
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultPlusConverterSerializer;
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultPlusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Stundentafel.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Stundentafel")
@NamedQuery(name = "DTOStundentafel.all", query = "SELECT e FROM DTOStundentafel e")
@NamedQuery(name = "DTOStundentafel.id", query = "SELECT e FROM DTOStundentafel e WHERE e.ID = :value")
@NamedQuery(name = "DTOStundentafel.id.multiple", query = "SELECT e FROM DTOStundentafel e WHERE e.ID IN :value")
@NamedQuery(name = "DTOStundentafel.bezeichnung", query = "SELECT e FROM DTOStundentafel e WHERE e.Bezeichnung = :value")
@NamedQuery(name = "DTOStundentafel.bezeichnung.multiple", query = "SELECT e FROM DTOStundentafel e WHERE e.Bezeichnung IN :value")
@NamedQuery(name = "DTOStundentafel.jahrgang_id", query = "SELECT e FROM DTOStundentafel e WHERE e.Jahrgang_ID = :value")
@NamedQuery(name = "DTOStundentafel.jahrgang_id.multiple", query = "SELECT e FROM DTOStundentafel e WHERE e.Jahrgang_ID IN :value")
@NamedQuery(name = "DTOStundentafel.asdjahrgang", query = "SELECT e FROM DTOStundentafel e WHERE e.ASDJahrgang = :value")
@NamedQuery(name = "DTOStundentafel.asdjahrgang.multiple", query = "SELECT e FROM DTOStundentafel e WHERE e.ASDJahrgang IN :value")
@NamedQuery(name = "DTOStundentafel.sgl", query = "SELECT e FROM DTOStundentafel e WHERE e.SGL = :value")
@NamedQuery(name = "DTOStundentafel.sgl.multiple", query = "SELECT e FROM DTOStundentafel e WHERE e.SGL IN :value")
@NamedQuery(name = "DTOStundentafel.fachklasse_id", query = "SELECT e FROM DTOStundentafel e WHERE e.Fachklasse_ID = :value")
@NamedQuery(name = "DTOStundentafel.fachklasse_id.multiple", query = "SELECT e FROM DTOStundentafel e WHERE e.Fachklasse_ID IN :value")
@NamedQuery(name = "DTOStundentafel.sichtbar", query = "SELECT e FROM DTOStundentafel e WHERE e.Sichtbar = :value")
@NamedQuery(name = "DTOStundentafel.sichtbar.multiple", query = "SELECT e FROM DTOStundentafel e WHERE e.Sichtbar IN :value")
@NamedQuery(name = "DTOStundentafel.sortierung", query = "SELECT e FROM DTOStundentafel e WHERE e.Sortierung = :value")
@NamedQuery(name = "DTOStundentafel.sortierung.multiple", query = "SELECT e FROM DTOStundentafel e WHERE e.Sortierung IN :value")
@NamedQuery(name = "DTOStundentafel.primaryKeyQuery", query = "SELECT e FROM DTOStundentafel e WHERE e.ID = ?1")
@NamedQuery(name = "DTOStundentafel.primaryKeyQuery.multiple", query = "SELECT e FROM DTOStundentafel e WHERE e.ID IN :value")
@NamedQuery(name = "DTOStundentafel.all.migration", query = "SELECT e FROM DTOStundentafel e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Bezeichnung", "Jahrgang_ID", "ASDJahrgang", "SGL", "Fachklasse_ID", "Sichtbar", "Sortierung"})
public final class DTOStundentafel {

	/** ID der Stundentafel */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

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
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** Sortierungnummer  der Stundentafel */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOStundentafel ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOStundentafel() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOStundentafel ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 */
	public DTOStundentafel(final long ID, final String Bezeichnung) {
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
		DTOStundentafel other = (DTOStundentafel) obj;
		return ID == other.ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(ID);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOStundentafel(ID=" + this.ID + ", Bezeichnung=" + this.Bezeichnung + ", Jahrgang_ID=" + this.Jahrgang_ID + ", ASDJahrgang=" + this.ASDJahrgang + ", SGL=" + this.SGL + ", Fachklasse_ID=" + this.Fachklasse_ID + ", Sichtbar=" + this.Sichtbar + ", Sortierung=" + this.Sortierung + ")";
	}

}
