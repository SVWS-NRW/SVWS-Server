package de.svws_nrw.db.dto.current.schild.schule;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Berufskolleg_Fachklassen_Keys.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(DTOBerufskollegFachklassenKeysPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Berufskolleg_Fachklassen_Keys")
@NamedQuery(name = "DTOBerufskollegFachklassenKeys.all", query = "SELECT e FROM DTOBerufskollegFachklassenKeys e")
@NamedQuery(name = "DTOBerufskollegFachklassenKeys.fachklassenindex", query = "SELECT e FROM DTOBerufskollegFachklassenKeys e WHERE e.FachklassenIndex = :value")
@NamedQuery(name = "DTOBerufskollegFachklassenKeys.fachklassenindex.multiple", query = "SELECT e FROM DTOBerufskollegFachklassenKeys e WHERE e.FachklassenIndex IN :value")
@NamedQuery(name = "DTOBerufskollegFachklassenKeys.schluessel", query = "SELECT e FROM DTOBerufskollegFachklassenKeys e WHERE e.Schluessel = :value")
@NamedQuery(name = "DTOBerufskollegFachklassenKeys.schluessel.multiple", query = "SELECT e FROM DTOBerufskollegFachklassenKeys e WHERE e.Schluessel IN :value")
@NamedQuery(name = "DTOBerufskollegFachklassenKeys.schluessel2", query = "SELECT e FROM DTOBerufskollegFachklassenKeys e WHERE e.Schluessel2 = :value")
@NamedQuery(name = "DTOBerufskollegFachklassenKeys.schluessel2.multiple", query = "SELECT e FROM DTOBerufskollegFachklassenKeys e WHERE e.Schluessel2 IN :value")
@NamedQuery(name = "DTOBerufskollegFachklassenKeys.primaryKeyQuery", query = "SELECT e FROM DTOBerufskollegFachklassenKeys e WHERE e.FachklassenIndex = ?1 AND e.Schluessel = ?2 AND e.Schluessel2 = ?3")
@NamedQuery(name = "DTOBerufskollegFachklassenKeys.all.migration", query = "SELECT e FROM DTOBerufskollegFachklassenKeys e WHERE e.FachklassenIndex IS NOT NULL AND e.Schluessel IS NOT NULL AND e.Schluessel2 IS NOT NULL")
@JsonPropertyOrder({"FachklassenIndex", "Schluessel", "Schluessel2"})
public final class DTOBerufskollegFachklassenKeys {

	/** Der Index, welcher der Fachklasse für die Zuordnung zum Bildungsgang zugeordnet ist */
	@Id
	@Column(name = "FachklassenIndex")
	@JsonProperty
	public int FachklassenIndex;

	/** Der erste Teil des Fachklassenschlüssels (FKS, dreistellig)  */
	@Id
	@Column(name = "Schluessel")
	@JsonProperty
	public String Schluessel;

	/** Der zweite Teil des Fachklassenschlüssels (AP, zweistellig) */
	@Id
	@Column(name = "Schluessel2")
	@JsonProperty
	public String Schluessel2;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOBerufskollegFachklassenKeys ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOBerufskollegFachklassenKeys() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOBerufskollegFachklassenKeys ohne eine Initialisierung der Attribute.
	 * @param FachklassenIndex   der Wert für das Attribut FachklassenIndex
	 * @param Schluessel   der Wert für das Attribut Schluessel
	 * @param Schluessel2   der Wert für das Attribut Schluessel2
	 */
	public DTOBerufskollegFachklassenKeys(final int FachklassenIndex, final String Schluessel, final String Schluessel2) {
		this.FachklassenIndex = FachklassenIndex;
		if (Schluessel == null) {
			throw new NullPointerException("Schluessel must not be null");
		}
		this.Schluessel = Schluessel;
		if (Schluessel2 == null) {
			throw new NullPointerException("Schluessel2 must not be null");
		}
		this.Schluessel2 = Schluessel2;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOBerufskollegFachklassenKeys other = (DTOBerufskollegFachklassenKeys) obj;
		if (FachklassenIndex != other.FachklassenIndex)
			return false;

		if (Schluessel == null) {
			if (other.Schluessel != null)
				return false;
		} else if (!Schluessel.equals(other.Schluessel))
			return false;

		if (Schluessel2 == null) {
			if (other.Schluessel2 != null)
				return false;
		} else if (!Schluessel2.equals(other.Schluessel2))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Integer.hashCode(FachklassenIndex);

		result = prime * result + ((Schluessel == null) ? 0 : Schluessel.hashCode());

		result = prime * result + ((Schluessel2 == null) ? 0 : Schluessel2.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOBerufskollegFachklassenKeys(FachklassenIndex=" + this.FachklassenIndex + ", Schluessel=" + this.Schluessel + ", Schluessel2=" + this.Schluessel2 + ")";
	}

}
