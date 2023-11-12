package de.svws_nrw.db.dto.current.schild.benutzer;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.BenutzerTypConverter;
import de.svws_nrw.db.converter.current.Boolean01Converter;

import de.svws_nrw.core.types.benutzer.BenutzerTyp;


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
import de.svws_nrw.csv.converter.current.BenutzerTypConverterSerializer;
import de.svws_nrw.csv.converter.current.BenutzerTypConverterDeserializer;
import de.svws_nrw.csv.converter.current.Boolean01ConverterSerializer;
import de.svws_nrw.csv.converter.current.Boolean01ConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Benutzer.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Benutzer")
@NamedQuery(name = "DTOBenutzer.all", query = "SELECT e FROM DTOBenutzer e")
@NamedQuery(name = "DTOBenutzer.id", query = "SELECT e FROM DTOBenutzer e WHERE e.ID = :value")
@NamedQuery(name = "DTOBenutzer.id.multiple", query = "SELECT e FROM DTOBenutzer e WHERE e.ID IN :value")
@NamedQuery(name = "DTOBenutzer.typ", query = "SELECT e FROM DTOBenutzer e WHERE e.Typ = :value")
@NamedQuery(name = "DTOBenutzer.typ.multiple", query = "SELECT e FROM DTOBenutzer e WHERE e.Typ IN :value")
@NamedQuery(name = "DTOBenutzer.allgemein_id", query = "SELECT e FROM DTOBenutzer e WHERE e.Allgemein_ID = :value")
@NamedQuery(name = "DTOBenutzer.allgemein_id.multiple", query = "SELECT e FROM DTOBenutzer e WHERE e.Allgemein_ID IN :value")
@NamedQuery(name = "DTOBenutzer.lehrer_id", query = "SELECT e FROM DTOBenutzer e WHERE e.Lehrer_ID = :value")
@NamedQuery(name = "DTOBenutzer.lehrer_id.multiple", query = "SELECT e FROM DTOBenutzer e WHERE e.Lehrer_ID IN :value")
@NamedQuery(name = "DTOBenutzer.schueler_id", query = "SELECT e FROM DTOBenutzer e WHERE e.Schueler_ID = :value")
@NamedQuery(name = "DTOBenutzer.schueler_id.multiple", query = "SELECT e FROM DTOBenutzer e WHERE e.Schueler_ID IN :value")
@NamedQuery(name = "DTOBenutzer.erzieher_id", query = "SELECT e FROM DTOBenutzer e WHERE e.Erzieher_ID = :value")
@NamedQuery(name = "DTOBenutzer.erzieher_id.multiple", query = "SELECT e FROM DTOBenutzer e WHERE e.Erzieher_ID IN :value")
@NamedQuery(name = "DTOBenutzer.istadmin", query = "SELECT e FROM DTOBenutzer e WHERE e.IstAdmin = :value")
@NamedQuery(name = "DTOBenutzer.istadmin.multiple", query = "SELECT e FROM DTOBenutzer e WHERE e.IstAdmin IN :value")
@NamedQuery(name = "DTOBenutzer.primaryKeyQuery", query = "SELECT e FROM DTOBenutzer e WHERE e.ID = ?1")
@NamedQuery(name = "DTOBenutzer.primaryKeyQuery.multiple", query = "SELECT e FROM DTOBenutzer e WHERE e.ID IN ?1")
@NamedQuery(name = "DTOBenutzer.all.migration", query = "SELECT e FROM DTOBenutzer e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Typ", "Allgemein_ID", "Lehrer_ID", "Schueler_ID", "Erzieher_ID", "IstAdmin"})
public final class DTOBenutzer {

	/** Die ID des Benutzers */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Der Typ des Benutzers (0 = Allgemeiner Benutzer, 1 = Lehrer bzw. Personal aus K_Lehrer, 2 = Schueler, 3 = Erzieher) */
	@Column(name = "Typ")
	@JsonProperty
	@Convert(converter = BenutzerTypConverter.class)
	@JsonSerialize(using = BenutzerTypConverterSerializer.class)
	@JsonDeserialize(using = BenutzerTypConverterDeserializer.class)
	public BenutzerTyp Typ;

	/** Die ID des allgemeinen Benutzers, falls der Benutzer es sich um einen allgemeinen Benutzer handelt */
	@Column(name = "Allgemein_ID")
	@JsonProperty
	public Long Allgemein_ID;

	/** Die Lehrer-ID des Benutzers, falls der Benutzer es sich um einen Lehrer handelt */
	@Column(name = "Lehrer_ID")
	@JsonProperty
	public Long Lehrer_ID;

	/** Die Schüler-ID des Benutzers, falls der Benutzer es sich um einen Schüler handelt */
	@Column(name = "Schueler_ID")
	@JsonProperty
	public Long Schueler_ID;

	/** Die erzieher-ID des Benutzers, falls der Benutzer es sich um einen Erzieher handelt */
	@Column(name = "Erzieher_ID")
	@JsonProperty
	public Long Erzieher_ID;

	/** Gibt an, ob der Benutzer Administrator-Rechte hat (1) oder nicht (0) */
	@Column(name = "IstAdmin")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean IstAdmin;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOBenutzer ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOBenutzer() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOBenutzer ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Typ   der Wert für das Attribut Typ
	 * @param IstAdmin   der Wert für das Attribut IstAdmin
	 */
	public DTOBenutzer(final long ID, final BenutzerTyp Typ, final Boolean IstAdmin) {
		this.ID = ID;
		this.Typ = Typ;
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
		DTOBenutzer other = (DTOBenutzer) obj;
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
		return "DTOBenutzer(ID=" + this.ID + ", Typ=" + this.Typ + ", Allgemein_ID=" + this.Allgemein_ID + ", Lehrer_ID=" + this.Lehrer_ID + ", Schueler_ID=" + this.Schueler_ID + ", Erzieher_ID=" + this.Erzieher_ID + ", IstAdmin=" + this.IstAdmin + ")";
	}

}
