package de.svws_nrw.db.dto.current.gost.kursblockung;

import de.svws_nrw.db.DBEntityManager;

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
@IdClass(DTOGostBlockungZwischenergebnisKursSchuelerPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Gost_Blockung_Zwischenergebnisse_Kurs_Schueler")
@NamedQuery(name = "DTOGostBlockungZwischenergebnisKursSchueler.all", query = "SELECT e FROM DTOGostBlockungZwischenergebnisKursSchueler e")
@NamedQuery(name = "DTOGostBlockungZwischenergebnisKursSchueler.zwischenergebnis_id", query = "SELECT e FROM DTOGostBlockungZwischenergebnisKursSchueler e WHERE e.Zwischenergebnis_ID = :value")
@NamedQuery(name = "DTOGostBlockungZwischenergebnisKursSchueler.zwischenergebnis_id.multiple", query = "SELECT e FROM DTOGostBlockungZwischenergebnisKursSchueler e WHERE e.Zwischenergebnis_ID IN :value")
@NamedQuery(name = "DTOGostBlockungZwischenergebnisKursSchueler.blockung_kurs_id", query = "SELECT e FROM DTOGostBlockungZwischenergebnisKursSchueler e WHERE e.Blockung_Kurs_ID = :value")
@NamedQuery(name = "DTOGostBlockungZwischenergebnisKursSchueler.blockung_kurs_id.multiple", query = "SELECT e FROM DTOGostBlockungZwischenergebnisKursSchueler e WHERE e.Blockung_Kurs_ID IN :value")
@NamedQuery(name = "DTOGostBlockungZwischenergebnisKursSchueler.schueler_id", query = "SELECT e FROM DTOGostBlockungZwischenergebnisKursSchueler e WHERE e.Schueler_ID = :value")
@NamedQuery(name = "DTOGostBlockungZwischenergebnisKursSchueler.schueler_id.multiple", query = "SELECT e FROM DTOGostBlockungZwischenergebnisKursSchueler e WHERE e.Schueler_ID IN :value")
@NamedQuery(name = "DTOGostBlockungZwischenergebnisKursSchueler.primaryKeyQuery", query = "SELECT e FROM DTOGostBlockungZwischenergebnisKursSchueler e WHERE e.Zwischenergebnis_ID = ?1 AND e.Blockung_Kurs_ID = ?2 AND e.Schueler_ID = ?3")
@NamedQuery(name = "DTOGostBlockungZwischenergebnisKursSchueler.all.migration", query = "SELECT e FROM DTOGostBlockungZwischenergebnisKursSchueler e WHERE e.Zwischenergebnis_ID IS NOT NULL AND e.Blockung_Kurs_ID IS NOT NULL AND e.Schueler_ID IS NOT NULL")
@JsonPropertyOrder({"Zwischenergebnis_ID", "Blockung_Kurs_ID", "Schueler_ID"})
public final class DTOGostBlockungZwischenergebnisKursSchueler {

	/** Kurs-Schüler-Zuordnung eines Zwischenergebnisses: ID des Zwischenergebnisses */
	@Id
	@Column(name = "Zwischenergebnis_ID")
	@JsonProperty
	public long Zwischenergebnis_ID;

	/** Kurs-Schüler-Zuordnung eines Zwischenergebnisses: ID des Kurses */
	@Id
	@Column(name = "Blockung_Kurs_ID")
	@JsonProperty
	public long Blockung_Kurs_ID;

	/** Kurs-Schüler-Zuordnung eines Zwischenergebnisses: ID des Schülers */
	@Id
	@Column(name = "Schueler_ID")
	@JsonProperty
	public long Schueler_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostBlockungZwischenergebnisKursSchueler ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOGostBlockungZwischenergebnisKursSchueler() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostBlockungZwischenergebnisKursSchueler ohne eine Initialisierung der Attribute.
	 * @param Zwischenergebnis_ID   der Wert für das Attribut Zwischenergebnis_ID
	 * @param Blockung_Kurs_ID   der Wert für das Attribut Blockung_Kurs_ID
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 */
	public DTOGostBlockungZwischenergebnisKursSchueler(final long Zwischenergebnis_ID, final long Blockung_Kurs_ID, final long Schueler_ID) {
		this.Zwischenergebnis_ID = Zwischenergebnis_ID;
		this.Blockung_Kurs_ID = Blockung_Kurs_ID;
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
		DTOGostBlockungZwischenergebnisKursSchueler other = (DTOGostBlockungZwischenergebnisKursSchueler) obj;
		if (Zwischenergebnis_ID != other.Zwischenergebnis_ID)
			return false;
		if (Blockung_Kurs_ID != other.Blockung_Kurs_ID)
			return false;
		return Schueler_ID == other.Schueler_ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(Zwischenergebnis_ID);

		result = prime * result + Long.hashCode(Blockung_Kurs_ID);

		result = prime * result + Long.hashCode(Schueler_ID);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOGostBlockungZwischenergebnisKursSchueler(Zwischenergebnis_ID=" + this.Zwischenergebnis_ID + ", Blockung_Kurs_ID=" + this.Blockung_Kurs_ID + ", Schueler_ID=" + this.Schueler_ID + ")";
	}

}
