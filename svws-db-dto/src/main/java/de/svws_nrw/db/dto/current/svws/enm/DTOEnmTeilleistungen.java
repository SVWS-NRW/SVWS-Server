package de.svws_nrw.db.dto.current.svws.enm;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle EnmTeilleistungen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "EnmTeilleistungen")
@NamedQuery(name = "DTOEnmTeilleistungen.all", query = "SELECT e FROM DTOEnmTeilleistungen e")
@NamedQuery(name = "DTOEnmTeilleistungen.id", query = "SELECT e FROM DTOEnmTeilleistungen e WHERE e.ID = :value")
@NamedQuery(name = "DTOEnmTeilleistungen.id.multiple", query = "SELECT e FROM DTOEnmTeilleistungen e WHERE e.ID IN :value")
@NamedQuery(name = "DTOEnmTeilleistungen.tsdatum", query = "SELECT e FROM DTOEnmTeilleistungen e WHERE e.tsDatum = :value")
@NamedQuery(name = "DTOEnmTeilleistungen.tsdatum.multiple", query = "SELECT e FROM DTOEnmTeilleistungen e WHERE e.tsDatum IN :value")
@NamedQuery(name = "DTOEnmTeilleistungen.tslehrer_id", query = "SELECT e FROM DTOEnmTeilleistungen e WHERE e.tsLehrer_ID = :value")
@NamedQuery(name = "DTOEnmTeilleistungen.tslehrer_id.multiple", query = "SELECT e FROM DTOEnmTeilleistungen e WHERE e.tsLehrer_ID IN :value")
@NamedQuery(name = "DTOEnmTeilleistungen.tsart_id", query = "SELECT e FROM DTOEnmTeilleistungen e WHERE e.tsArt_ID = :value")
@NamedQuery(name = "DTOEnmTeilleistungen.tsart_id.multiple", query = "SELECT e FROM DTOEnmTeilleistungen e WHERE e.tsArt_ID IN :value")
@NamedQuery(name = "DTOEnmTeilleistungen.tsbemerkung", query = "SELECT e FROM DTOEnmTeilleistungen e WHERE e.tsBemerkung = :value")
@NamedQuery(name = "DTOEnmTeilleistungen.tsbemerkung.multiple", query = "SELECT e FROM DTOEnmTeilleistungen e WHERE e.tsBemerkung IN :value")
@NamedQuery(name = "DTOEnmTeilleistungen.tsnotenkrz", query = "SELECT e FROM DTOEnmTeilleistungen e WHERE e.tsNotenKrz = :value")
@NamedQuery(name = "DTOEnmTeilleistungen.tsnotenkrz.multiple", query = "SELECT e FROM DTOEnmTeilleistungen e WHERE e.tsNotenKrz IN :value")
@NamedQuery(name = "DTOEnmTeilleistungen.primaryKeyQuery", query = "SELECT e FROM DTOEnmTeilleistungen e WHERE e.ID = ?1")
@NamedQuery(name = "DTOEnmTeilleistungen.primaryKeyQuery.multiple", query = "SELECT e FROM DTOEnmTeilleistungen e WHERE e.ID IN :value")
@NamedQuery(name = "DTOEnmTeilleistungen.all.migration", query = "SELECT e FROM DTOEnmTeilleistungen e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "tsDatum", "tsLehrer_ID", "tsArt_ID", "tsBemerkung", "tsNotenKrz"})
public final class DTOEnmTeilleistungen {

	/** ID der Teilleistungen */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Der Zeitstempel der letzten Änderung an dem Datum der Teilleistung, wann diese erbracht wurde. */
	@Column(name = "tsDatum")
	@JsonProperty
	public String tsDatum;

	/** Der Zeitstempel der letzten Änderung an der Lehrer-ID. */
	@Column(name = "tsLehrer_ID")
	@JsonProperty
	public String tsLehrer_ID;

	/** Der Zeitstempel der letzten Änderung der Teilleistungsart. */
	@Column(name = "tsArt_ID")
	@JsonProperty
	public String tsArt_ID;

	/** Der Zeitstempel der letzten Änderung an der Bemerkung. */
	@Column(name = "tsBemerkung")
	@JsonProperty
	public String tsBemerkung;

	/** Der Zeitstempel der letzten Änderung an der Note. */
	@Column(name = "tsNotenKrz")
	@JsonProperty
	public String tsNotenKrz;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOEnmTeilleistungen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOEnmTeilleistungen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOEnmTeilleistungen ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param tsDatum   der Wert für das Attribut tsDatum
	 * @param tsLehrer_ID   der Wert für das Attribut tsLehrer_ID
	 * @param tsArt_ID   der Wert für das Attribut tsArt_ID
	 * @param tsBemerkung   der Wert für das Attribut tsBemerkung
	 * @param tsNotenKrz   der Wert für das Attribut tsNotenKrz
	 */
	public DTOEnmTeilleistungen(final long ID, final String tsDatum, final String tsLehrer_ID, final String tsArt_ID, final String tsBemerkung, final String tsNotenKrz) {
		this.ID = ID;
		if (tsDatum == null) {
			throw new NullPointerException("tsDatum must not be null");
		}
		this.tsDatum = tsDatum;
		if (tsLehrer_ID == null) {
			throw new NullPointerException("tsLehrer_ID must not be null");
		}
		this.tsLehrer_ID = tsLehrer_ID;
		if (tsArt_ID == null) {
			throw new NullPointerException("tsArt_ID must not be null");
		}
		this.tsArt_ID = tsArt_ID;
		if (tsBemerkung == null) {
			throw new NullPointerException("tsBemerkung must not be null");
		}
		this.tsBemerkung = tsBemerkung;
		if (tsNotenKrz == null) {
			throw new NullPointerException("tsNotenKrz must not be null");
		}
		this.tsNotenKrz = tsNotenKrz;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOEnmTeilleistungen other = (DTOEnmTeilleistungen) obj;
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
		return "DTOEnmTeilleistungen(ID=" + this.ID + ", tsDatum=" + this.tsDatum + ", tsLehrer_ID=" + this.tsLehrer_ID + ", tsArt_ID=" + this.tsArt_ID + ", tsBemerkung=" + this.tsBemerkung + ", tsNotenKrz=" + this.tsNotenKrz + ")";
	}

}
