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
 * Diese Klasse dient als DTO für die Datenbanktabelle EnmLeistungsdaten.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "EnmLeistungsdaten")
@NamedQuery(name = "DTOEnmLeistungsdaten.all", query = "SELECT e FROM DTOEnmLeistungsdaten e")
@NamedQuery(name = "DTOEnmLeistungsdaten.id", query = "SELECT e FROM DTOEnmLeistungsdaten e WHERE e.ID = :value")
@NamedQuery(name = "DTOEnmLeistungsdaten.id.multiple", query = "SELECT e FROM DTOEnmLeistungsdaten e WHERE e.ID IN :value")
@NamedQuery(name = "DTOEnmLeistungsdaten.tsnotenkrz", query = "SELECT e FROM DTOEnmLeistungsdaten e WHERE e.tsNotenKrz = :value")
@NamedQuery(name = "DTOEnmLeistungsdaten.tsnotenkrz.multiple", query = "SELECT e FROM DTOEnmLeistungsdaten e WHERE e.tsNotenKrz IN :value")
@NamedQuery(name = "DTOEnmLeistungsdaten.tsfehlstd", query = "SELECT e FROM DTOEnmLeistungsdaten e WHERE e.tsFehlStd = :value")
@NamedQuery(name = "DTOEnmLeistungsdaten.tsfehlstd.multiple", query = "SELECT e FROM DTOEnmLeistungsdaten e WHERE e.tsFehlStd IN :value")
@NamedQuery(name = "DTOEnmLeistungsdaten.tsufehlstd", query = "SELECT e FROM DTOEnmLeistungsdaten e WHERE e.tsuFehlStd = :value")
@NamedQuery(name = "DTOEnmLeistungsdaten.tsufehlstd.multiple", query = "SELECT e FROM DTOEnmLeistungsdaten e WHERE e.tsuFehlStd IN :value")
@NamedQuery(name = "DTOEnmLeistungsdaten.tslernentw", query = "SELECT e FROM DTOEnmLeistungsdaten e WHERE e.tsLernentw = :value")
@NamedQuery(name = "DTOEnmLeistungsdaten.tslernentw.multiple", query = "SELECT e FROM DTOEnmLeistungsdaten e WHERE e.tsLernentw IN :value")
@NamedQuery(name = "DTOEnmLeistungsdaten.tswarnung", query = "SELECT e FROM DTOEnmLeistungsdaten e WHERE e.tsWarnung = :value")
@NamedQuery(name = "DTOEnmLeistungsdaten.tswarnung.multiple", query = "SELECT e FROM DTOEnmLeistungsdaten e WHERE e.tsWarnung IN :value")
@NamedQuery(name = "DTOEnmLeistungsdaten.primaryKeyQuery", query = "SELECT e FROM DTOEnmLeistungsdaten e WHERE e.ID = ?1")
@NamedQuery(name = "DTOEnmLeistungsdaten.primaryKeyQuery.multiple", query = "SELECT e FROM DTOEnmLeistungsdaten e WHERE e.ID IN ?1")
@NamedQuery(name = "DTOEnmLeistungsdaten.all.migration", query = "SELECT e FROM DTOEnmLeistungsdaten e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "tsNotenKrz", "tsFehlStd", "tsuFehlStd", "tsLernentw", "tsWarnung"})
public final class DTOEnmLeistungsdaten {

	/** ID der Leistungsdaten */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Der Zeitstempel der letzten Änderung an der Note. */
	@Column(name = "tsNotenKrz")
	@JsonProperty
	public String tsNotenKrz;

	/** Der Zeitstempel der letzten Änderung an den Fehlstunden. */
	@Column(name = "tsFehlStd")
	@JsonProperty
	public String tsFehlStd;

	/** Der Zeitstempel der letzten Änderung an den unentschuldigten Fehlstunden. */
	@Column(name = "tsuFehlStd")
	@JsonProperty
	public String tsuFehlStd;

	/** Der Zeitstempel der letzten Änderung an den fachbezogenen Bemerkungen. */
	@Column(name = "tsLernentw")
	@JsonProperty
	public String tsLernentw;

	/** Der Zeitstempel der letzten Änderung, ob gemahnt wird. */
	@Column(name = "tsWarnung")
	@JsonProperty
	public String tsWarnung;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOEnmLeistungsdaten ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOEnmLeistungsdaten() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOEnmLeistungsdaten ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param tsNotenKrz   der Wert für das Attribut tsNotenKrz
	 * @param tsFehlStd   der Wert für das Attribut tsFehlStd
	 * @param tsuFehlStd   der Wert für das Attribut tsuFehlStd
	 * @param tsLernentw   der Wert für das Attribut tsLernentw
	 * @param tsWarnung   der Wert für das Attribut tsWarnung
	 */
	public DTOEnmLeistungsdaten(final long ID, final String tsNotenKrz, final String tsFehlStd, final String tsuFehlStd, final String tsLernentw, final String tsWarnung) {
		this.ID = ID;
		if (tsNotenKrz == null) {
			throw new NullPointerException("tsNotenKrz must not be null");
		}
		this.tsNotenKrz = tsNotenKrz;
		if (tsFehlStd == null) {
			throw new NullPointerException("tsFehlStd must not be null");
		}
		this.tsFehlStd = tsFehlStd;
		if (tsuFehlStd == null) {
			throw new NullPointerException("tsuFehlStd must not be null");
		}
		this.tsuFehlStd = tsuFehlStd;
		if (tsLernentw == null) {
			throw new NullPointerException("tsLernentw must not be null");
		}
		this.tsLernentw = tsLernentw;
		if (tsWarnung == null) {
			throw new NullPointerException("tsWarnung must not be null");
		}
		this.tsWarnung = tsWarnung;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOEnmLeistungsdaten other = (DTOEnmLeistungsdaten) obj;
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
		return "DTOEnmLeistungsdaten(ID=" + this.ID + ", tsNotenKrz=" + this.tsNotenKrz + ", tsFehlStd=" + this.tsFehlStd + ", tsuFehlStd=" + this.tsuFehlStd + ", tsLernentw=" + this.tsLernentw + ", tsWarnung=" + this.tsWarnung + ")";
	}

}
