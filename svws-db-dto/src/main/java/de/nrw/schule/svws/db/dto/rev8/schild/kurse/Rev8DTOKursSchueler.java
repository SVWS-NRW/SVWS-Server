package de.nrw.schule.svws.db.dto.rev8.schild.kurse;

import de.nrw.schule.svws.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Kurs_Schueler.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(Rev8DTOKursSchuelerPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Kurs_Schueler")
@NamedQuery(name="Rev8DTOKursSchueler.all", query="SELECT e FROM Rev8DTOKursSchueler e")
@NamedQuery(name="Rev8DTOKursSchueler.kurs_id", query="SELECT e FROM Rev8DTOKursSchueler e WHERE e.Kurs_ID = :value")
@NamedQuery(name="Rev8DTOKursSchueler.kurs_id.multiple", query="SELECT e FROM Rev8DTOKursSchueler e WHERE e.Kurs_ID IN :value")
@NamedQuery(name="Rev8DTOKursSchueler.schueler_id", query="SELECT e FROM Rev8DTOKursSchueler e WHERE e.Schueler_ID = :value")
@NamedQuery(name="Rev8DTOKursSchueler.schueler_id.multiple", query="SELECT e FROM Rev8DTOKursSchueler e WHERE e.Schueler_ID IN :value")
@NamedQuery(name="Rev8DTOKursSchueler.primaryKeyQuery", query="SELECT e FROM Rev8DTOKursSchueler e WHERE e.Kurs_ID = ?1 AND e.Schueler_ID = ?2")
@NamedQuery(name="Rev8DTOKursSchueler.all.migration", query="SELECT e FROM Rev8DTOKursSchueler e WHERE e.Kurs_ID IS NOT NULL AND e.Schueler_ID IS NOT NULL")
@JsonPropertyOrder({"Kurs_ID","Schueler_ID"})
public class Rev8DTOKursSchueler {

	/** Die eindeutige ID des Kurses – verweist auf den Kurs */
	@Id
	@Column(name = "Kurs_ID")
	@JsonProperty
	public Long Kurs_ID;

	/** Die eindeutige ID des Schülers – verweist auf den Schüler */
	@Id
	@Column(name = "Schueler_ID")
	@JsonProperty
	public Long Schueler_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOKursSchueler ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev8DTOKursSchueler() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOKursSchueler ohne eine Initialisierung der Attribute.
	 * @param Kurs_ID   der Wert für das Attribut Kurs_ID
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 */
	public Rev8DTOKursSchueler(final Long Kurs_ID, final Long Schueler_ID) {
		if (Kurs_ID == null) { 
			throw new NullPointerException("Kurs_ID must not be null");
		}
		this.Kurs_ID = Kurs_ID;
		if (Schueler_ID == null) { 
			throw new NullPointerException("Schueler_ID must not be null");
		}
		this.Schueler_ID = Schueler_ID;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev8DTOKursSchueler other = (Rev8DTOKursSchueler) obj;
		if (Kurs_ID == null) {
			if (other.Kurs_ID != null)
				return false;
		} else if (!Kurs_ID.equals(other.Kurs_ID))
			return false;

		if (Schueler_ID == null) {
			if (other.Schueler_ID != null)
				return false;
		} else if (!Schueler_ID.equals(other.Schueler_ID))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Kurs_ID == null) ? 0 : Kurs_ID.hashCode());

		result = prime * result + ((Schueler_ID == null) ? 0 : Schueler_ID.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "Rev8DTOKursSchueler(Kurs_ID=" + this.Kurs_ID + ", Schueler_ID=" + this.Schueler_ID + ")";
	}

}