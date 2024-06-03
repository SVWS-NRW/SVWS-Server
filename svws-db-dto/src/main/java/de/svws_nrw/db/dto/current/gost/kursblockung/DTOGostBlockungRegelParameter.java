package de.svws_nrw.db.dto.current.gost.kursblockung;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Gost_Blockung_Regelparameter.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(DTOGostBlockungRegelParameterPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Gost_Blockung_Regelparameter")
@JsonPropertyOrder({"Regel_ID", "Nummer", "Parameter"})
public final class DTOGostBlockungRegelParameter {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOGostBlockungRegelParameter e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOGostBlockungRegelParameter e WHERE e.Regel_ID = ?1 AND e.Nummer = ?2";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOGostBlockungRegelParameter e WHERE e.Regel_ID IS NOT NULL AND e.Nummer IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Regel_ID */
	public static final String QUERY_BY_REGEL_ID = "SELECT e FROM DTOGostBlockungRegelParameter e WHERE e.Regel_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Regel_ID */
	public static final String QUERY_LIST_BY_REGEL_ID = "SELECT e FROM DTOGostBlockungRegelParameter e WHERE e.Regel_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Nummer */
	public static final String QUERY_BY_NUMMER = "SELECT e FROM DTOGostBlockungRegelParameter e WHERE e.Nummer = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Nummer */
	public static final String QUERY_LIST_BY_NUMMER = "SELECT e FROM DTOGostBlockungRegelParameter e WHERE e.Nummer IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Parameter */
	public static final String QUERY_BY_PARAMETER = "SELECT e FROM DTOGostBlockungRegelParameter e WHERE e.Parameter = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Parameter */
	public static final String QUERY_LIST_BY_PARAMETER = "SELECT e FROM DTOGostBlockungRegelParameter e WHERE e.Parameter IN ?1";

	/** ID des Regel-Parameters */
	@Id
	@Column(name = "Regel_ID")
	@JsonProperty
	public long Regel_ID;

	/** Die Nummer des Parameters der Regel, beginnend bei 1 */
	@Id
	@Column(name = "Nummer")
	@JsonProperty
	public int Nummer;

	/** Der Wert des Parameters der Regel (hängt vom Typ der Regel ab) */
	@Column(name = "Parameter")
	@JsonProperty
	public long Parameter;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostBlockungRegelParameter ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOGostBlockungRegelParameter() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostBlockungRegelParameter ohne eine Initialisierung der Attribute.
	 * @param Regel_ID   der Wert für das Attribut Regel_ID
	 * @param Nummer   der Wert für das Attribut Nummer
	 * @param Parameter   der Wert für das Attribut Parameter
	 */
	public DTOGostBlockungRegelParameter(final long Regel_ID, final int Nummer, final long Parameter) {
		this.Regel_ID = Regel_ID;
		this.Nummer = Nummer;
		this.Parameter = Parameter;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOGostBlockungRegelParameter other = (DTOGostBlockungRegelParameter) obj;
		if (Regel_ID != other.Regel_ID)
			return false;
		return Nummer == other.Nummer;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(Regel_ID);

		result = prime * result + Integer.hashCode(Nummer);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOGostBlockungRegelParameter(Regel_ID=" + this.Regel_ID + ", Nummer=" + this.Nummer + ", Parameter=" + this.Parameter + ")";
	}

}
