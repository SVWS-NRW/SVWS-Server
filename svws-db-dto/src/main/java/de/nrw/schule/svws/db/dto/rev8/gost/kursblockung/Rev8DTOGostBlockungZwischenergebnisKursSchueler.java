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
 * Diese Klasse dient als DTO für die Datenbanktabelle Gost_Blockung_Zwischenergebnisse_Kurs_Schueler.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(Rev8DTOGostBlockungZwischenergebnisKursSchuelerPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Gost_Blockung_Zwischenergebnisse_Kurs_Schueler")
@NamedQuery(name="Rev8DTOGostBlockungZwischenergebnisKursSchueler.all", query="SELECT e FROM Rev8DTOGostBlockungZwischenergebnisKursSchueler e")
@NamedQuery(name="Rev8DTOGostBlockungZwischenergebnisKursSchueler.zwischenergebnis_id", query="SELECT e FROM Rev8DTOGostBlockungZwischenergebnisKursSchueler e WHERE e.Zwischenergebnis_ID = :value")
@NamedQuery(name="Rev8DTOGostBlockungZwischenergebnisKursSchueler.zwischenergebnis_id.multiple", query="SELECT e FROM Rev8DTOGostBlockungZwischenergebnisKursSchueler e WHERE e.Zwischenergebnis_ID IN :value")
@NamedQuery(name="Rev8DTOGostBlockungZwischenergebnisKursSchueler.blockung_kurs_id", query="SELECT e FROM Rev8DTOGostBlockungZwischenergebnisKursSchueler e WHERE e.Blockung_Kurs_ID = :value")
@NamedQuery(name="Rev8DTOGostBlockungZwischenergebnisKursSchueler.blockung_kurs_id.multiple", query="SELECT e FROM Rev8DTOGostBlockungZwischenergebnisKursSchueler e WHERE e.Blockung_Kurs_ID IN :value")
@NamedQuery(name="Rev8DTOGostBlockungZwischenergebnisKursSchueler.schueler_id", query="SELECT e FROM Rev8DTOGostBlockungZwischenergebnisKursSchueler e WHERE e.Schueler_ID = :value")
@NamedQuery(name="Rev8DTOGostBlockungZwischenergebnisKursSchueler.schueler_id.multiple", query="SELECT e FROM Rev8DTOGostBlockungZwischenergebnisKursSchueler e WHERE e.Schueler_ID IN :value")
@NamedQuery(name="Rev8DTOGostBlockungZwischenergebnisKursSchueler.primaryKeyQuery", query="SELECT e FROM Rev8DTOGostBlockungZwischenergebnisKursSchueler e WHERE e.Zwischenergebnis_ID = ?1 AND e.Blockung_Kurs_ID = ?2 AND e.Schueler_ID = ?3")
@NamedQuery(name="Rev8DTOGostBlockungZwischenergebnisKursSchueler.all.migration", query="SELECT e FROM Rev8DTOGostBlockungZwischenergebnisKursSchueler e WHERE e.Zwischenergebnis_ID IS NOT NULL AND e.Blockung_Kurs_ID IS NOT NULL AND e.Schueler_ID IS NOT NULL")
@JsonPropertyOrder({"Zwischenergebnis_ID","Blockung_Kurs_ID","Schueler_ID"})
public class Rev8DTOGostBlockungZwischenergebnisKursSchueler {

	/** Kurs-Schüler-Zuordnung eines Zwischenergebnisses: ID des Zwischenergebnisses */
	@Id
	@Column(name = "Zwischenergebnis_ID")
	@JsonProperty
	public Long Zwischenergebnis_ID;

	/** Kurs-Schüler-Zuordnung eines Zwischenergebnisses: ID des Kurses */
	@Id
	@Column(name = "Blockung_Kurs_ID")
	@JsonProperty
	public Long Blockung_Kurs_ID;

	/** Kurs-Schüler-Zuordnung eines Zwischenergebnisses: ID des Schülers */
	@Id
	@Column(name = "Schueler_ID")
	@JsonProperty
	public Long Schueler_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOGostBlockungZwischenergebnisKursSchueler ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev8DTOGostBlockungZwischenergebnisKursSchueler() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOGostBlockungZwischenergebnisKursSchueler ohne eine Initialisierung der Attribute.
	 * @param Zwischenergebnis_ID   der Wert für das Attribut Zwischenergebnis_ID
	 * @param Blockung_Kurs_ID   der Wert für das Attribut Blockung_Kurs_ID
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 */
	public Rev8DTOGostBlockungZwischenergebnisKursSchueler(final Long Zwischenergebnis_ID, final Long Blockung_Kurs_ID, final Long Schueler_ID) {
		if (Zwischenergebnis_ID == null) { 
			throw new NullPointerException("Zwischenergebnis_ID must not be null");
		}
		this.Zwischenergebnis_ID = Zwischenergebnis_ID;
		if (Blockung_Kurs_ID == null) { 
			throw new NullPointerException("Blockung_Kurs_ID must not be null");
		}
		this.Blockung_Kurs_ID = Blockung_Kurs_ID;
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
		Rev8DTOGostBlockungZwischenergebnisKursSchueler other = (Rev8DTOGostBlockungZwischenergebnisKursSchueler) obj;
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
		result = prime * result + ((Zwischenergebnis_ID == null) ? 0 : Zwischenergebnis_ID.hashCode());

		result = prime * result + ((Blockung_Kurs_ID == null) ? 0 : Blockung_Kurs_ID.hashCode());

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
		return "Rev8DTOGostBlockungZwischenergebnisKursSchueler(Zwischenergebnis_ID=" + this.Zwischenergebnis_ID + ", Blockung_Kurs_ID=" + this.Blockung_Kurs_ID + ", Schueler_ID=" + this.Schueler_ID + ")";
	}

}