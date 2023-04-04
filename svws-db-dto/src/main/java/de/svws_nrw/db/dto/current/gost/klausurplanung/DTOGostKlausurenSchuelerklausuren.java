package de.svws_nrw.db.dto.current.gost.klausurplanung;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.UhrzeitConverter;


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
import de.svws_nrw.csv.converter.current.UhrzeitConverterSerializer;
import de.svws_nrw.csv.converter.current.UhrzeitConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Gost_Klausuren_Schuelerklausuren.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Gost_Klausuren_Schuelerklausuren")
@NamedQuery(name = "DTOGostKlausurenSchuelerklausuren.all", query = "SELECT e FROM DTOGostKlausurenSchuelerklausuren e")
@NamedQuery(name = "DTOGostKlausurenSchuelerklausuren.id", query = "SELECT e FROM DTOGostKlausurenSchuelerklausuren e WHERE e.ID = :value")
@NamedQuery(name = "DTOGostKlausurenSchuelerklausuren.id.multiple", query = "SELECT e FROM DTOGostKlausurenSchuelerklausuren e WHERE e.ID IN :value")
@NamedQuery(name = "DTOGostKlausurenSchuelerklausuren.kursklausur_id", query = "SELECT e FROM DTOGostKlausurenSchuelerklausuren e WHERE e.Kursklausur_ID = :value")
@NamedQuery(name = "DTOGostKlausurenSchuelerklausuren.kursklausur_id.multiple", query = "SELECT e FROM DTOGostKlausurenSchuelerklausuren e WHERE e.Kursklausur_ID IN :value")
@NamedQuery(name = "DTOGostKlausurenSchuelerklausuren.termin_id", query = "SELECT e FROM DTOGostKlausurenSchuelerklausuren e WHERE e.Termin_ID = :value")
@NamedQuery(name = "DTOGostKlausurenSchuelerklausuren.termin_id.multiple", query = "SELECT e FROM DTOGostKlausurenSchuelerklausuren e WHERE e.Termin_ID IN :value")
@NamedQuery(name = "DTOGostKlausurenSchuelerklausuren.schueler_id", query = "SELECT e FROM DTOGostKlausurenSchuelerklausuren e WHERE e.Schueler_ID = :value")
@NamedQuery(name = "DTOGostKlausurenSchuelerklausuren.schueler_id.multiple", query = "SELECT e FROM DTOGostKlausurenSchuelerklausuren e WHERE e.Schueler_ID IN :value")
@NamedQuery(name = "DTOGostKlausurenSchuelerklausuren.startzeit", query = "SELECT e FROM DTOGostKlausurenSchuelerklausuren e WHERE e.Startzeit = :value")
@NamedQuery(name = "DTOGostKlausurenSchuelerklausuren.startzeit.multiple", query = "SELECT e FROM DTOGostKlausurenSchuelerklausuren e WHERE e.Startzeit IN :value")
@NamedQuery(name = "DTOGostKlausurenSchuelerklausuren.primaryKeyQuery", query = "SELECT e FROM DTOGostKlausurenSchuelerklausuren e WHERE e.ID = ?1")
@NamedQuery(name = "DTOGostKlausurenSchuelerklausuren.all.migration", query = "SELECT e FROM DTOGostKlausurenSchuelerklausuren e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Kursklausur_ID", "Termin_ID", "Schueler_ID", "Startzeit"})
public final class DTOGostKlausurenSchuelerklausuren {

	/** ID der Klausurvorgaben (generiert) */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** ID der Kursklausur */
	@Column(name = "Kursklausur_ID")
	@JsonProperty
	public Long Kursklausur_ID;

	/** ID des Klausurtermins */
	@Column(name = "Termin_ID")
	@JsonProperty
	public Long Termin_ID;

	/** ID des Schülers */
	@Column(name = "Schueler_ID")
	@JsonProperty
	public Long Schueler_ID;

	/** Startzeit der Klausur, wenn abweichend von Startzeit der Klausur-Schiene */
	@Column(name = "Startzeit")
	@JsonProperty
	@Convert(converter = UhrzeitConverter.class)
	@JsonSerialize(using = UhrzeitConverterSerializer.class)
	@JsonDeserialize(using = UhrzeitConverterDeserializer.class)
	public String Startzeit;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostKlausurenSchuelerklausuren ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOGostKlausurenSchuelerklausuren() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostKlausurenSchuelerklausuren ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Kursklausur_ID   der Wert für das Attribut Kursklausur_ID
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 */
	public DTOGostKlausurenSchuelerklausuren(final Long ID, final Long Kursklausur_ID, final Long Schueler_ID) {
		if (ID == null) {
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Kursklausur_ID == null) {
			throw new NullPointerException("Kursklausur_ID must not be null");
		}
		this.Kursklausur_ID = Kursklausur_ID;
		if (Schueler_ID == null) {
			throw new NullPointerException("Schueler_ID must not be null");
		}
		this.Schueler_ID = Schueler_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOGostKlausurenSchuelerklausuren other = (DTOGostKlausurenSchuelerklausuren) obj;
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
		return "DTOGostKlausurenSchuelerklausuren(ID=" + this.ID + ", Kursklausur_ID=" + this.Kursklausur_ID + ", Termin_ID=" + this.Termin_ID + ", Schueler_ID=" + this.Schueler_ID + ", Startzeit=" + this.Startzeit + ")";
	}

}
