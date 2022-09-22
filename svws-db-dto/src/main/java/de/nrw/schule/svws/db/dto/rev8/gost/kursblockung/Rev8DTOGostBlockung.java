package de.nrw.schule.svws.db.dto.rev8.gost.kursblockung;

import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.converter.current.gost.GOStHalbjahrConverter;

import de.nrw.schule.svws.core.types.gost.GostHalbjahr;


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
import de.nrw.schule.svws.csv.converter.current.gost.GOStHalbjahrConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.gost.GOStHalbjahrConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Gost_Blockung.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Gost_Blockung")
@NamedQuery(name="Rev8DTOGostBlockung.all", query="SELECT e FROM Rev8DTOGostBlockung e")
@NamedQuery(name="Rev8DTOGostBlockung.id", query="SELECT e FROM Rev8DTOGostBlockung e WHERE e.ID = :value")
@NamedQuery(name="Rev8DTOGostBlockung.id.multiple", query="SELECT e FROM Rev8DTOGostBlockung e WHERE e.ID IN :value")
@NamedQuery(name="Rev8DTOGostBlockung.name", query="SELECT e FROM Rev8DTOGostBlockung e WHERE e.Name = :value")
@NamedQuery(name="Rev8DTOGostBlockung.name.multiple", query="SELECT e FROM Rev8DTOGostBlockung e WHERE e.Name IN :value")
@NamedQuery(name="Rev8DTOGostBlockung.abi_jahrgang", query="SELECT e FROM Rev8DTOGostBlockung e WHERE e.Abi_Jahrgang = :value")
@NamedQuery(name="Rev8DTOGostBlockung.abi_jahrgang.multiple", query="SELECT e FROM Rev8DTOGostBlockung e WHERE e.Abi_Jahrgang IN :value")
@NamedQuery(name="Rev8DTOGostBlockung.halbjahr", query="SELECT e FROM Rev8DTOGostBlockung e WHERE e.Halbjahr = :value")
@NamedQuery(name="Rev8DTOGostBlockung.halbjahr.multiple", query="SELECT e FROM Rev8DTOGostBlockung e WHERE e.Halbjahr IN :value")
@NamedQuery(name="Rev8DTOGostBlockung.ergebnis_id", query="SELECT e FROM Rev8DTOGostBlockung e WHERE e.Ergebnis_ID = :value")
@NamedQuery(name="Rev8DTOGostBlockung.ergebnis_id.multiple", query="SELECT e FROM Rev8DTOGostBlockung e WHERE e.Ergebnis_ID IN :value")
@NamedQuery(name="Rev8DTOGostBlockung.primaryKeyQuery", query="SELECT e FROM Rev8DTOGostBlockung e WHERE e.ID = ?1")
@NamedQuery(name="Rev8DTOGostBlockung.all.migration", query="SELECT e FROM Rev8DTOGostBlockung e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Name","Abi_Jahrgang","Halbjahr","Ergebnis_ID"})
public class Rev8DTOGostBlockung {

	/** Kursblockung der Gymnasialen Oberstufe: ID der Blockung (generiert) */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Kursblockung der Gymnasialen Oberstufe: Textuelle Bezeichnung der Blockung */
	@Column(name = "Name")
	@JsonProperty
	public String Name;

	/** Kursblockung der Gymnasialen Oberstufe: Der Abiturjahrgang, dem die Blockung zugeordnet ist */
	@Column(name = "Abi_Jahrgang")
	@JsonProperty
	public Integer Abi_Jahrgang;

	/** Kursblockung der Gymnasialen Oberstufe: Das Halbjahr, welchem die Kursblockung zugeordnet ist (0=EF.1, 1=EF.2, 2=Q1.1, 3=Q1.2, 4=Q2.1, 5=Q2.2) */
	@Column(name = "Halbjahr")
	@JsonProperty
	@Convert(converter=GOStHalbjahrConverter.class)
	@JsonSerialize(using=GOStHalbjahrConverterSerializer.class)
	@JsonDeserialize(using=GOStHalbjahrConverterDeserializer.class)
	public GostHalbjahr Halbjahr;

	/** Kursblockung der Gymnasialen Oberstufe: Die ID des als aktiv ausgewählten Zwischenergebnisses, sofern eines definiert ist - ansonsten null */
	@Column(name = "Ergebnis_ID")
	@JsonProperty
	public Long Ergebnis_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOGostBlockung ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev8DTOGostBlockung() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOGostBlockung ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Name   der Wert für das Attribut Name
	 * @param Abi_Jahrgang   der Wert für das Attribut Abi_Jahrgang
	 * @param Halbjahr   der Wert für das Attribut Halbjahr
	 */
	public Rev8DTOGostBlockung(final Long ID, final String Name, final Integer Abi_Jahrgang, final GostHalbjahr Halbjahr) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Name == null) { 
			throw new NullPointerException("Name must not be null");
		}
		this.Name = Name;
		if (Abi_Jahrgang == null) { 
			throw new NullPointerException("Abi_Jahrgang must not be null");
		}
		this.Abi_Jahrgang = Abi_Jahrgang;
		if (Halbjahr == null) { 
			throw new NullPointerException("Halbjahr must not be null");
		}
		this.Halbjahr = Halbjahr;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev8DTOGostBlockung other = (Rev8DTOGostBlockung) obj;
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
		return "Rev8DTOGostBlockung(ID=" + this.ID + ", Name=" + this.Name + ", Abi_Jahrgang=" + this.Abi_Jahrgang + ", Halbjahr=" + this.Halbjahr + ", Ergebnis_ID=" + this.Ergebnis_ID + ")";
	}

}