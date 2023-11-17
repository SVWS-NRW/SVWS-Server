package de.svws_nrw.db.dto.current.gost.kursblockung;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.Boolean01Converter;
import de.svws_nrw.db.converter.current.gost.GOStHalbjahrConverter;

import de.svws_nrw.core.types.gost.GostHalbjahr;


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
import de.svws_nrw.csv.converter.current.gost.GOStHalbjahrConverterSerializer;
import de.svws_nrw.csv.converter.current.gost.GOStHalbjahrConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Gost_Blockung.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Gost_Blockung")
@NamedQuery(name = "DTOGostBlockung.all", query = "SELECT e FROM DTOGostBlockung e")
@NamedQuery(name = "DTOGostBlockung.id", query = "SELECT e FROM DTOGostBlockung e WHERE e.ID = :value")
@NamedQuery(name = "DTOGostBlockung.id.multiple", query = "SELECT e FROM DTOGostBlockung e WHERE e.ID IN :value")
@NamedQuery(name = "DTOGostBlockung.name", query = "SELECT e FROM DTOGostBlockung e WHERE e.Name = :value")
@NamedQuery(name = "DTOGostBlockung.name.multiple", query = "SELECT e FROM DTOGostBlockung e WHERE e.Name IN :value")
@NamedQuery(name = "DTOGostBlockung.abi_jahrgang", query = "SELECT e FROM DTOGostBlockung e WHERE e.Abi_Jahrgang = :value")
@NamedQuery(name = "DTOGostBlockung.abi_jahrgang.multiple", query = "SELECT e FROM DTOGostBlockung e WHERE e.Abi_Jahrgang IN :value")
@NamedQuery(name = "DTOGostBlockung.halbjahr", query = "SELECT e FROM DTOGostBlockung e WHERE e.Halbjahr = :value")
@NamedQuery(name = "DTOGostBlockung.halbjahr.multiple", query = "SELECT e FROM DTOGostBlockung e WHERE e.Halbjahr IN :value")
@NamedQuery(name = "DTOGostBlockung.istaktiv", query = "SELECT e FROM DTOGostBlockung e WHERE e.IstAktiv = :value")
@NamedQuery(name = "DTOGostBlockung.istaktiv.multiple", query = "SELECT e FROM DTOGostBlockung e WHERE e.IstAktiv IN :value")
@NamedQuery(name = "DTOGostBlockung.primaryKeyQuery", query = "SELECT e FROM DTOGostBlockung e WHERE e.ID = ?1")
@NamedQuery(name = "DTOGostBlockung.primaryKeyQuery.multiple", query = "SELECT e FROM DTOGostBlockung e WHERE e.ID IN :value")
@NamedQuery(name = "DTOGostBlockung.all.migration", query = "SELECT e FROM DTOGostBlockung e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Name", "Abi_Jahrgang", "Halbjahr", "IstAktiv"})
public final class DTOGostBlockung {

	/** ID der Blockung (generiert) */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Textuelle Bezeichnung der Blockung */
	@Column(name = "Name")
	@JsonProperty
	public String Name;

	/** Der Abiturjahrgang, dem die Blockung zugeordnet ist */
	@Column(name = "Abi_Jahrgang")
	@JsonProperty
	public int Abi_Jahrgang;

	/** Das Halbjahr, welchem die Kursblockung zugeordnet ist (0=EF.1, 1=EF.2, 2=Q1.1, 3=Q1.2, 4=Q2.1, 5=Q2.2) */
	@Column(name = "Halbjahr")
	@JsonProperty
	@Convert(converter = GOStHalbjahrConverter.class)
	@JsonSerialize(using = GOStHalbjahrConverterSerializer.class)
	@JsonDeserialize(using = GOStHalbjahrConverterDeserializer.class)
	public GostHalbjahr Halbjahr;

	/** Gibt an, ob die Blockung aktiviert wurde oder nicht: 1 - true, 0 - false.Bei einer aktivierten Blockung wurde die Vorlage (siehe Vorlage_ID) bereits in die Leistungsdaten übertragen. */
	@Column(name = "IstAktiv")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean IstAktiv;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostBlockung ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOGostBlockung() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostBlockung ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Name   der Wert für das Attribut Name
	 * @param Abi_Jahrgang   der Wert für das Attribut Abi_Jahrgang
	 * @param Halbjahr   der Wert für das Attribut Halbjahr
	 * @param IstAktiv   der Wert für das Attribut IstAktiv
	 */
	public DTOGostBlockung(final long ID, final String Name, final int Abi_Jahrgang, final GostHalbjahr Halbjahr, final Boolean IstAktiv) {
		this.ID = ID;
		if (Name == null) {
			throw new NullPointerException("Name must not be null");
		}
		this.Name = Name;
		this.Abi_Jahrgang = Abi_Jahrgang;
		this.Halbjahr = Halbjahr;
		this.IstAktiv = IstAktiv;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOGostBlockung other = (DTOGostBlockung) obj;
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
		return "DTOGostBlockung(ID=" + this.ID + ", Name=" + this.Name + ", Abi_Jahrgang=" + this.Abi_Jahrgang + ", Halbjahr=" + this.Halbjahr + ", IstAktiv=" + this.IstAktiv + ")";
	}

}
