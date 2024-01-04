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
 * Diese Klasse dient als DTO für die Datenbanktabelle Gost_Klausuren_Schuelerklausuren_Termine.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Gost_Klausuren_Schuelerklausuren_Termine")
@NamedQuery(name = "DTOGostKlausurenSchuelerklausurenTermine.all", query = "SELECT e FROM DTOGostKlausurenSchuelerklausurenTermine e")
@NamedQuery(name = "DTOGostKlausurenSchuelerklausurenTermine.id", query = "SELECT e FROM DTOGostKlausurenSchuelerklausurenTermine e WHERE e.ID = :value")
@NamedQuery(name = "DTOGostKlausurenSchuelerklausurenTermine.id.multiple", query = "SELECT e FROM DTOGostKlausurenSchuelerklausurenTermine e WHERE e.ID IN :value")
@NamedQuery(name = "DTOGostKlausurenSchuelerklausurenTermine.schuelerklausur_id", query = "SELECT e FROM DTOGostKlausurenSchuelerklausurenTermine e WHERE e.Schuelerklausur_ID = :value")
@NamedQuery(name = "DTOGostKlausurenSchuelerklausurenTermine.schuelerklausur_id.multiple", query = "SELECT e FROM DTOGostKlausurenSchuelerklausurenTermine e WHERE e.Schuelerklausur_ID IN :value")
@NamedQuery(name = "DTOGostKlausurenSchuelerklausurenTermine.folge_nr", query = "SELECT e FROM DTOGostKlausurenSchuelerklausurenTermine e WHERE e.Folge_Nr = :value")
@NamedQuery(name = "DTOGostKlausurenSchuelerklausurenTermine.folge_nr.multiple", query = "SELECT e FROM DTOGostKlausurenSchuelerklausurenTermine e WHERE e.Folge_Nr IN :value")
@NamedQuery(name = "DTOGostKlausurenSchuelerklausurenTermine.termin_id", query = "SELECT e FROM DTOGostKlausurenSchuelerklausurenTermine e WHERE e.Termin_ID = :value")
@NamedQuery(name = "DTOGostKlausurenSchuelerklausurenTermine.termin_id.multiple", query = "SELECT e FROM DTOGostKlausurenSchuelerklausurenTermine e WHERE e.Termin_ID IN :value")
@NamedQuery(name = "DTOGostKlausurenSchuelerklausurenTermine.startzeit", query = "SELECT e FROM DTOGostKlausurenSchuelerklausurenTermine e WHERE e.Startzeit = :value")
@NamedQuery(name = "DTOGostKlausurenSchuelerklausurenTermine.startzeit.multiple", query = "SELECT e FROM DTOGostKlausurenSchuelerklausurenTermine e WHERE e.Startzeit IN :value")
@NamedQuery(name = "DTOGostKlausurenSchuelerklausurenTermine.bemerkungen", query = "SELECT e FROM DTOGostKlausurenSchuelerklausurenTermine e WHERE e.Bemerkungen = :value")
@NamedQuery(name = "DTOGostKlausurenSchuelerklausurenTermine.bemerkungen.multiple", query = "SELECT e FROM DTOGostKlausurenSchuelerklausurenTermine e WHERE e.Bemerkungen IN :value")
@NamedQuery(name = "DTOGostKlausurenSchuelerklausurenTermine.primaryKeyQuery", query = "SELECT e FROM DTOGostKlausurenSchuelerklausurenTermine e WHERE e.ID = ?1")
@NamedQuery(name = "DTOGostKlausurenSchuelerklausurenTermine.primaryKeyQuery.multiple", query = "SELECT e FROM DTOGostKlausurenSchuelerklausurenTermine e WHERE e.ID IN :value")
@NamedQuery(name = "DTOGostKlausurenSchuelerklausurenTermine.all.migration", query = "SELECT e FROM DTOGostKlausurenSchuelerklausurenTermine e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Schuelerklausur_ID", "Folge_Nr", "Termin_ID", "Startzeit", "Bemerkungen"})
public final class DTOGostKlausurenSchuelerklausurenTermine {

	/** ID des Schülerklausur-Termins (generiert) */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** ID der Schülerklausur */
	@Column(name = "Schuelerklausur_ID")
	@JsonProperty
	public long Schuelerklausur_ID;

	/** Folgenummer des Schülerklausur-Termins */
	@Column(name = "Folge_Nr")
	@JsonProperty
	public int Folge_Nr;

	/** ID des Klausurtermins, null falls Termin der Kursklausur */
	@Column(name = "Termin_ID")
	@JsonProperty
	public Long Termin_ID;

	/** Startzeit der Klausur, wenn abweichend von Startzeit des Klausurtermins */
	@Column(name = "Startzeit")
	@JsonProperty
	@Convert(converter = UhrzeitConverter.class)
	@JsonSerialize(using = UhrzeitConverterSerializer.class)
	@JsonDeserialize(using = UhrzeitConverterDeserializer.class)
	public Integer Startzeit;

	/** Text für Bemerkungen des Schuelerklausurtermins */
	@Column(name = "Bemerkungen")
	@JsonProperty
	public String Bemerkungen;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostKlausurenSchuelerklausurenTermine ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOGostKlausurenSchuelerklausurenTermine() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostKlausurenSchuelerklausurenTermine ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Schuelerklausur_ID   der Wert für das Attribut Schuelerklausur_ID
	 * @param Folge_Nr   der Wert für das Attribut Folge_Nr
	 */
	public DTOGostKlausurenSchuelerklausurenTermine(final long ID, final long Schuelerklausur_ID, final int Folge_Nr) {
		this.ID = ID;
		this.Schuelerklausur_ID = Schuelerklausur_ID;
		this.Folge_Nr = Folge_Nr;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOGostKlausurenSchuelerklausurenTermine other = (DTOGostKlausurenSchuelerklausurenTermine) obj;
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
		return "DTOGostKlausurenSchuelerklausurenTermine(ID=" + this.ID + ", Schuelerklausur_ID=" + this.Schuelerklausur_ID + ", Folge_Nr=" + this.Folge_Nr + ", Termin_ID=" + this.Termin_ID + ", Startzeit=" + this.Startzeit + ", Bemerkungen=" + this.Bemerkungen + ")";
	}

}
