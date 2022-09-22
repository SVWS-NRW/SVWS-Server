package de.nrw.schule.svws.db.dto.rev8.gost.kursblockung;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Gost_Blockung_Zwischenergebnisse_Kurs_Schienen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(Rev8DTOGostBlockungZwischenergebnisKursSchienePK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Gost_Blockung_Zwischenergebnisse_Kurs_Schienen")
@NamedQuery(name="Rev8DTOGostBlockungZwischenergebnisKursSchiene.all", query="SELECT e FROM Rev8DTOGostBlockungZwischenergebnisKursSchiene e")
@NamedQuery(name="Rev8DTOGostBlockungZwischenergebnisKursSchiene.zwischenergebnis_id", query="SELECT e FROM Rev8DTOGostBlockungZwischenergebnisKursSchiene e WHERE e.Zwischenergebnis_ID = :value")
@NamedQuery(name="Rev8DTOGostBlockungZwischenergebnisKursSchiene.zwischenergebnis_id.multiple", query="SELECT e FROM Rev8DTOGostBlockungZwischenergebnisKursSchiene e WHERE e.Zwischenergebnis_ID IN :value")
@NamedQuery(name="Rev8DTOGostBlockungZwischenergebnisKursSchiene.blockung_kurs_id", query="SELECT e FROM Rev8DTOGostBlockungZwischenergebnisKursSchiene e WHERE e.Blockung_Kurs_ID = :value")
@NamedQuery(name="Rev8DTOGostBlockungZwischenergebnisKursSchiene.blockung_kurs_id.multiple", query="SELECT e FROM Rev8DTOGostBlockungZwischenergebnisKursSchiene e WHERE e.Blockung_Kurs_ID IN :value")
@NamedQuery(name="Rev8DTOGostBlockungZwischenergebnisKursSchiene.schienen_id", query="SELECT e FROM Rev8DTOGostBlockungZwischenergebnisKursSchiene e WHERE e.Schienen_ID = :value")
@NamedQuery(name="Rev8DTOGostBlockungZwischenergebnisKursSchiene.schienen_id.multiple", query="SELECT e FROM Rev8DTOGostBlockungZwischenergebnisKursSchiene e WHERE e.Schienen_ID IN :value")
@NamedQuery(name="Rev8DTOGostBlockungZwischenergebnisKursSchiene.primaryKeyQuery", query="SELECT e FROM Rev8DTOGostBlockungZwischenergebnisKursSchiene e WHERE e.Zwischenergebnis_ID = ?1 AND e.Blockung_Kurs_ID = ?2 AND e.Schienen_ID = ?3")
@NamedQuery(name="Rev8DTOGostBlockungZwischenergebnisKursSchiene.all.migration", query="SELECT e FROM Rev8DTOGostBlockungZwischenergebnisKursSchiene e WHERE e.Zwischenergebnis_ID IS NOT NULL AND e.Blockung_Kurs_ID IS NOT NULL AND e.Schienen_ID IS NOT NULL")
@JsonPropertyOrder({"Zwischenergebnis_ID","Blockung_Kurs_ID","Schienen_ID"})
public class Rev8DTOGostBlockungZwischenergebnisKursSchiene {

	/** Kursblockung der Gymnasialen Oberstufe - Kurs-Schienen-Zuordnung eines Zwischenergebnisses einer Blockung: ID des Zwischenergebnisses */
	@Id
	@Column(name = "Zwischenergebnis_ID")
	@JsonProperty
	public Long Zwischenergebnis_ID;

	/** Kursblockung der Gymnasialen Oberstufe - Kurs-Schienen-Zuordnung eines Zwischenergebnisses einer Blockung: ID des Kurses */
	@Id
	@Column(name = "Blockung_Kurs_ID")
	@JsonProperty
	public Long Blockung_Kurs_ID;

	/** Kursblockung der Gymnasialen Oberstufe - Kurs-Schienen-Zuordnung eines Zwischenergebnisses einer Blockung: ID der Schiene aus der Blockung */
	@Id
	@Column(name = "Schienen_ID")
	@JsonProperty
	public Long Schienen_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOGostBlockungZwischenergebnisKursSchiene ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev8DTOGostBlockungZwischenergebnisKursSchiene() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOGostBlockungZwischenergebnisKursSchiene ohne eine Initialisierung der Attribute.
	 * @param Zwischenergebnis_ID   der Wert für das Attribut Zwischenergebnis_ID
	 * @param Blockung_Kurs_ID   der Wert für das Attribut Blockung_Kurs_ID
	 * @param Schienen_ID   der Wert für das Attribut Schienen_ID
	 */
	public Rev8DTOGostBlockungZwischenergebnisKursSchiene(final Long Zwischenergebnis_ID, final Long Blockung_Kurs_ID, final Long Schienen_ID) {
		if (Zwischenergebnis_ID == null) { 
			throw new NullPointerException("Zwischenergebnis_ID must not be null");
		}
		this.Zwischenergebnis_ID = Zwischenergebnis_ID;
		if (Blockung_Kurs_ID == null) { 
			throw new NullPointerException("Blockung_Kurs_ID must not be null");
		}
		this.Blockung_Kurs_ID = Blockung_Kurs_ID;
		if (Schienen_ID == null) { 
			throw new NullPointerException("Schienen_ID must not be null");
		}
		this.Schienen_ID = Schienen_ID;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev8DTOGostBlockungZwischenergebnisKursSchiene other = (Rev8DTOGostBlockungZwischenergebnisKursSchiene) obj;
		if (Zwischenergebnis_ID == null) {
			if (other.Zwischenergebnis_ID != null)
				return false;
		} else if (!Zwischenergebnis_ID.equals(other.Zwischenergebnis_ID))
			return false;

		if (Blockung_Kurs_ID == null) {
			if (other.Blockung_Kurs_ID != null)
				return false;
		} else if (!Blockung_Kurs_ID.equals(other.Blockung_Kurs_ID))
			return false;

		if (Schienen_ID == null) {
			if (other.Schienen_ID != null)
				return false;
		} else if (!Schienen_ID.equals(other.Schienen_ID))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Zwischenergebnis_ID == null) ? 0 : Zwischenergebnis_ID.hashCode());

		result = prime * result + ((Blockung_Kurs_ID == null) ? 0 : Blockung_Kurs_ID.hashCode());

		result = prime * result + ((Schienen_ID == null) ? 0 : Schienen_ID.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "Rev8DTOGostBlockungZwischenergebnisKursSchiene(Zwischenergebnis_ID=" + this.Zwischenergebnis_ID + ", Blockung_Kurs_ID=" + this.Blockung_Kurs_ID + ", Schienen_ID=" + this.Schienen_ID + ")";
	}

}