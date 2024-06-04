package de.svws_nrw.db.dto.current.schild.stundenplan;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Stundenplan_PausenaufsichtenBereich.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Stundenplan_PausenaufsichtenBereich")
@JsonPropertyOrder({"ID", "Pausenaufsicht_ID", "Aufsichtsbereich_ID", "Wochentyp"})
public final class DTOStundenplanPausenaufsichtenBereiche {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOStundenplanPausenaufsichtenBereiche e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOStundenplanPausenaufsichtenBereiche e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOStundenplanPausenaufsichtenBereiche e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOStundenplanPausenaufsichtenBereiche e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOStundenplanPausenaufsichtenBereiche e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOStundenplanPausenaufsichtenBereiche e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Pausenaufsicht_ID */
	public static final String QUERY_BY_PAUSENAUFSICHT_ID = "SELECT e FROM DTOStundenplanPausenaufsichtenBereiche e WHERE e.Pausenaufsicht_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Pausenaufsicht_ID */
	public static final String QUERY_LIST_BY_PAUSENAUFSICHT_ID = "SELECT e FROM DTOStundenplanPausenaufsichtenBereiche e WHERE e.Pausenaufsicht_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Aufsichtsbereich_ID */
	public static final String QUERY_BY_AUFSICHTSBEREICH_ID = "SELECT e FROM DTOStundenplanPausenaufsichtenBereiche e WHERE e.Aufsichtsbereich_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Aufsichtsbereich_ID */
	public static final String QUERY_LIST_BY_AUFSICHTSBEREICH_ID = "SELECT e FROM DTOStundenplanPausenaufsichtenBereiche e WHERE e.Aufsichtsbereich_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Wochentyp */
	public static final String QUERY_BY_WOCHENTYP = "SELECT e FROM DTOStundenplanPausenaufsichtenBereiche e WHERE e.Wochentyp = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Wochentyp */
	public static final String QUERY_LIST_BY_WOCHENTYP = "SELECT e FROM DTOStundenplanPausenaufsichtenBereiche e WHERE e.Wochentyp IN ?1";

	/** Die eindeutige ID für die Zuordnung des Aufsichtsbereichs zur Pausenaufsicht */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Die ID des Pausenaufsicht-Eintrages im Stundenplan */
	@Column(name = "Pausenaufsicht_ID")
	@JsonProperty
	public long Pausenaufsicht_ID;

	/** Die ID des zugewiesenen Aufsichtsbereichs. Sollten ggf. mehrere Aufsichtsbereiche zugwiesen werden, so müssen für eine Pausenaufsicht_ID mehrere Datensätze vorliegen */
	@Column(name = "Aufsichtsbereich_ID")
	@JsonProperty
	public long Aufsichtsbereich_ID;

	/** Gibt an, ob es sich um einen Eintrag für jede Woche handelt (0) oder ob es sich um einen unterschiedlichen (!) Eintrag für eine A- bzw. B-Wochen (1 bzw. 2) handelt */
	@Column(name = "Wochentyp")
	@JsonProperty
	public int Wochentyp;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOStundenplanPausenaufsichtenBereiche ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOStundenplanPausenaufsichtenBereiche() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOStundenplanPausenaufsichtenBereiche ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Pausenaufsicht_ID   der Wert für das Attribut Pausenaufsicht_ID
	 * @param Aufsichtsbereich_ID   der Wert für das Attribut Aufsichtsbereich_ID
	 * @param Wochentyp   der Wert für das Attribut Wochentyp
	 */
	public DTOStundenplanPausenaufsichtenBereiche(final long ID, final long Pausenaufsicht_ID, final long Aufsichtsbereich_ID, final int Wochentyp) {
		this.ID = ID;
		this.Pausenaufsicht_ID = Pausenaufsicht_ID;
		this.Aufsichtsbereich_ID = Aufsichtsbereich_ID;
		this.Wochentyp = Wochentyp;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOStundenplanPausenaufsichtenBereiche other = (DTOStundenplanPausenaufsichtenBereiche) obj;
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
		return "DTOStundenplanPausenaufsichtenBereiche(ID=" + this.ID + ", Pausenaufsicht_ID=" + this.Pausenaufsicht_ID + ", Aufsichtsbereich_ID=" + this.Aufsichtsbereich_ID + ", Wochentyp=" + this.Wochentyp + ")";
	}

}
