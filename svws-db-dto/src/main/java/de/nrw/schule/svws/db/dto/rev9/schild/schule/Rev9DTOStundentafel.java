package de.nrw.schule.svws.db.dto.rev9.schild.schule;

import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.converter.current.BooleanPlusMinusDefaultPlusConverter;


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
import de.nrw.schule.svws.csv.converter.current.BooleanPlusMinusDefaultPlusConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.BooleanPlusMinusDefaultPlusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Stundentafel.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Stundentafel")
@NamedQuery(name="Rev9DTOStundentafel.all", query="SELECT e FROM Rev9DTOStundentafel e")
@NamedQuery(name="Rev9DTOStundentafel.id", query="SELECT e FROM Rev9DTOStundentafel e WHERE e.ID = :value")
@NamedQuery(name="Rev9DTOStundentafel.id.multiple", query="SELECT e FROM Rev9DTOStundentafel e WHERE e.ID IN :value")
@NamedQuery(name="Rev9DTOStundentafel.bezeichnung", query="SELECT e FROM Rev9DTOStundentafel e WHERE e.Bezeichnung = :value")
@NamedQuery(name="Rev9DTOStundentafel.bezeichnung.multiple", query="SELECT e FROM Rev9DTOStundentafel e WHERE e.Bezeichnung IN :value")
@NamedQuery(name="Rev9DTOStundentafel.jahrgang_id", query="SELECT e FROM Rev9DTOStundentafel e WHERE e.Jahrgang_ID = :value")
@NamedQuery(name="Rev9DTOStundentafel.jahrgang_id.multiple", query="SELECT e FROM Rev9DTOStundentafel e WHERE e.Jahrgang_ID IN :value")
@NamedQuery(name="Rev9DTOStundentafel.asdjahrgang", query="SELECT e FROM Rev9DTOStundentafel e WHERE e.ASDJahrgang = :value")
@NamedQuery(name="Rev9DTOStundentafel.asdjahrgang.multiple", query="SELECT e FROM Rev9DTOStundentafel e WHERE e.ASDJahrgang IN :value")
@NamedQuery(name="Rev9DTOStundentafel.sgl", query="SELECT e FROM Rev9DTOStundentafel e WHERE e.SGL = :value")
@NamedQuery(name="Rev9DTOStundentafel.sgl.multiple", query="SELECT e FROM Rev9DTOStundentafel e WHERE e.SGL IN :value")
@NamedQuery(name="Rev9DTOStundentafel.fachklasse_id", query="SELECT e FROM Rev9DTOStundentafel e WHERE e.Fachklasse_ID = :value")
@NamedQuery(name="Rev9DTOStundentafel.fachklasse_id.multiple", query="SELECT e FROM Rev9DTOStundentafel e WHERE e.Fachklasse_ID IN :value")
@NamedQuery(name="Rev9DTOStundentafel.sichtbar", query="SELECT e FROM Rev9DTOStundentafel e WHERE e.Sichtbar = :value")
@NamedQuery(name="Rev9DTOStundentafel.sichtbar.multiple", query="SELECT e FROM Rev9DTOStundentafel e WHERE e.Sichtbar IN :value")
@NamedQuery(name="Rev9DTOStundentafel.sortierung", query="SELECT e FROM Rev9DTOStundentafel e WHERE e.Sortierung = :value")
@NamedQuery(name="Rev9DTOStundentafel.sortierung.multiple", query="SELECT e FROM Rev9DTOStundentafel e WHERE e.Sortierung IN :value")
@NamedQuery(name="Rev9DTOStundentafel.primaryKeyQuery", query="SELECT e FROM Rev9DTOStundentafel e WHERE e.ID = ?1")
@NamedQuery(name="Rev9DTOStundentafel.all.migration", query="SELECT e FROM Rev9DTOStundentafel e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Bezeichnung","Jahrgang_ID","ASDJahrgang","SGL","Fachklasse_ID","Sichtbar","Sortierung"})
public class Rev9DTOStundentafel {

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
	@Convert(converter=BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** Sortierungnummer  der Stundentafel */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev9DTOStundentafel ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev9DTOStundentafel() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev9DTOStundentafel ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 */
	public Rev9DTOStundentafel(final Long ID, final String Bezeichnung) {
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
		Rev9DTOStundentafel other = (Rev9DTOStundentafel) obj;
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
		return "Rev9DTOStundentafel(ID=" + this.ID + ", Bezeichnung=" + this.Bezeichnung + ", Jahrgang_ID=" + this.Jahrgang_ID + ", ASDJahrgang=" + this.ASDJahrgang + ", SGL=" + this.SGL + ", Fachklasse_ID=" + this.Fachklasse_ID + ", Sichtbar=" + this.Sichtbar + ", Sortierung=" + this.Sortierung + ")";
	}

}