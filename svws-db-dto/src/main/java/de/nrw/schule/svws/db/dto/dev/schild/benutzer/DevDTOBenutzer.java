package de.nrw.schule.svws.db.dto.dev.schild.benutzer;

import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.converter.current.BenutzerTypConverter;
import de.nrw.schule.svws.db.converter.current.Boolean01Converter;

import de.nrw.schule.svws.core.types.benutzer.BenutzerTyp;


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
import de.nrw.schule.svws.csv.converter.current.BenutzerTypConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.BenutzerTypConverterDeserializer;
import de.nrw.schule.svws.csv.converter.current.Boolean01ConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.Boolean01ConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Benutzer.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Benutzer")
@NamedQuery(name="DevDTOBenutzer.all", query="SELECT e FROM DevDTOBenutzer e")
@NamedQuery(name="DevDTOBenutzer.id", query="SELECT e FROM DevDTOBenutzer e WHERE e.ID = :value")
@NamedQuery(name="DevDTOBenutzer.id.multiple", query="SELECT e FROM DevDTOBenutzer e WHERE e.ID IN :value")
@NamedQuery(name="DevDTOBenutzer.typ", query="SELECT e FROM DevDTOBenutzer e WHERE e.Typ = :value")
@NamedQuery(name="DevDTOBenutzer.typ.multiple", query="SELECT e FROM DevDTOBenutzer e WHERE e.Typ IN :value")
@NamedQuery(name="DevDTOBenutzer.allgemein_id", query="SELECT e FROM DevDTOBenutzer e WHERE e.Allgemein_ID = :value")
@NamedQuery(name="DevDTOBenutzer.allgemein_id.multiple", query="SELECT e FROM DevDTOBenutzer e WHERE e.Allgemein_ID IN :value")
@NamedQuery(name="DevDTOBenutzer.lehrer_id", query="SELECT e FROM DevDTOBenutzer e WHERE e.Lehrer_ID = :value")
@NamedQuery(name="DevDTOBenutzer.lehrer_id.multiple", query="SELECT e FROM DevDTOBenutzer e WHERE e.Lehrer_ID IN :value")
@NamedQuery(name="DevDTOBenutzer.schueler_id", query="SELECT e FROM DevDTOBenutzer e WHERE e.Schueler_ID = :value")
@NamedQuery(name="DevDTOBenutzer.schueler_id.multiple", query="SELECT e FROM DevDTOBenutzer e WHERE e.Schueler_ID IN :value")
@NamedQuery(name="DevDTOBenutzer.erzieher_id", query="SELECT e FROM DevDTOBenutzer e WHERE e.Erzieher_ID = :value")
@NamedQuery(name="DevDTOBenutzer.erzieher_id.multiple", query="SELECT e FROM DevDTOBenutzer e WHERE e.Erzieher_ID IN :value")
@NamedQuery(name="DevDTOBenutzer.istadmin", query="SELECT e FROM DevDTOBenutzer e WHERE e.IstAdmin = :value")
@NamedQuery(name="DevDTOBenutzer.istadmin.multiple", query="SELECT e FROM DevDTOBenutzer e WHERE e.IstAdmin IN :value")
@NamedQuery(name="DevDTOBenutzer.primaryKeyQuery", query="SELECT e FROM DevDTOBenutzer e WHERE e.ID = ?1")
@NamedQuery(name="DevDTOBenutzer.all.migration", query="SELECT e FROM DevDTOBenutzer e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Typ","Allgemein_ID","Lehrer_ID","Schueler_ID","Erzieher_ID","IstAdmin"})
public class DevDTOBenutzer {

	/** Die ID des Benutzers */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Der Typ des Benutzers (0 = Allgemeiner Benutzer, 1 = Lehrer bzw. Personal aus K_Lehrer, 2 = Schueler, 3 = Erzieher) */
	@Column(name = "Typ")
	@JsonProperty
	@Convert(converter=BenutzerTypConverter.class)
	@JsonSerialize(using=BenutzerTypConverterSerializer.class)
	@JsonDeserialize(using=BenutzerTypConverterDeserializer.class)
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
	@Convert(converter=Boolean01Converter.class)
	@JsonSerialize(using=Boolean01ConverterSerializer.class)
	@JsonDeserialize(using=Boolean01ConverterDeserializer.class)
	public Boolean IstAdmin;

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOBenutzer ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DevDTOBenutzer() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOBenutzer ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Typ   der Wert für das Attribut Typ
	 * @param IstAdmin   der Wert für das Attribut IstAdmin
	 */
	public DevDTOBenutzer(final Long ID, final BenutzerTyp Typ, final Boolean IstAdmin) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Typ == null) { 
			throw new NullPointerException("Typ must not be null");
		}
		this.Typ = Typ;
		if (IstAdmin == null) { 
			throw new NullPointerException("IstAdmin must not be null");
		}
		this.IstAdmin = IstAdmin;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DevDTOBenutzer other = (DevDTOBenutzer) obj;
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
		return "DevDTOBenutzer(ID=" + this.ID + ", Typ=" + this.Typ + ", Allgemein_ID=" + this.Allgemein_ID + ", Lehrer_ID=" + this.Lehrer_ID + ", Schueler_ID=" + this.Schueler_ID + ", Erzieher_ID=" + this.Erzieher_ID + ", IstAdmin=" + this.IstAdmin + ")";
	}

}