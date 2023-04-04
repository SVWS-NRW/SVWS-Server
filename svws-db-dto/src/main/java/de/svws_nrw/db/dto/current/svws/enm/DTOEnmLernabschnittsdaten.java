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
 * Diese Klasse dient als DTO für die Datenbanktabelle EnmLernabschnittsdaten.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "EnmLernabschnittsdaten")
@NamedQuery(name = "DTOEnmLernabschnittsdaten.all", query = "SELECT e FROM DTOEnmLernabschnittsdaten e")
@NamedQuery(name = "DTOEnmLernabschnittsdaten.id", query = "SELECT e FROM DTOEnmLernabschnittsdaten e WHERE e.ID = :value")
@NamedQuery(name = "DTOEnmLernabschnittsdaten.id.multiple", query = "SELECT e FROM DTOEnmLernabschnittsdaten e WHERE e.ID IN :value")
@NamedQuery(name = "DTOEnmLernabschnittsdaten.tssumfehlstd", query = "SELECT e FROM DTOEnmLernabschnittsdaten e WHERE e.tsSumFehlStd = :value")
@NamedQuery(name = "DTOEnmLernabschnittsdaten.tssumfehlstd.multiple", query = "SELECT e FROM DTOEnmLernabschnittsdaten e WHERE e.tsSumFehlStd IN :value")
@NamedQuery(name = "DTOEnmLernabschnittsdaten.tssumfehlstdu", query = "SELECT e FROM DTOEnmLernabschnittsdaten e WHERE e.tsSumFehlStdU = :value")
@NamedQuery(name = "DTOEnmLernabschnittsdaten.tssumfehlstdu.multiple", query = "SELECT e FROM DTOEnmLernabschnittsdaten e WHERE e.tsSumFehlStdU IN :value")
@NamedQuery(name = "DTOEnmLernabschnittsdaten.tszeugnisbem", query = "SELECT e FROM DTOEnmLernabschnittsdaten e WHERE e.tsZeugnisBem = :value")
@NamedQuery(name = "DTOEnmLernabschnittsdaten.tszeugnisbem.multiple", query = "SELECT e FROM DTOEnmLernabschnittsdaten e WHERE e.tsZeugnisBem IN :value")
@NamedQuery(name = "DTOEnmLernabschnittsdaten.tsasv", query = "SELECT e FROM DTOEnmLernabschnittsdaten e WHERE e.tsASV = :value")
@NamedQuery(name = "DTOEnmLernabschnittsdaten.tsasv.multiple", query = "SELECT e FROM DTOEnmLernabschnittsdaten e WHERE e.tsASV IN :value")
@NamedQuery(name = "DTOEnmLernabschnittsdaten.tsaue", query = "SELECT e FROM DTOEnmLernabschnittsdaten e WHERE e.tsAUE = :value")
@NamedQuery(name = "DTOEnmLernabschnittsdaten.tsaue.multiple", query = "SELECT e FROM DTOEnmLernabschnittsdaten e WHERE e.tsAUE IN :value")
@NamedQuery(name = "DTOEnmLernabschnittsdaten.tsbemerkungversetzung", query = "SELECT e FROM DTOEnmLernabschnittsdaten e WHERE e.tsBemerkungVersetzung = :value")
@NamedQuery(name = "DTOEnmLernabschnittsdaten.tsbemerkungversetzung.multiple", query = "SELECT e FROM DTOEnmLernabschnittsdaten e WHERE e.tsBemerkungVersetzung IN :value")
@NamedQuery(name = "DTOEnmLernabschnittsdaten.primaryKeyQuery", query = "SELECT e FROM DTOEnmLernabschnittsdaten e WHERE e.ID = ?1")
@NamedQuery(name = "DTOEnmLernabschnittsdaten.all.migration", query = "SELECT e FROM DTOEnmLernabschnittsdaten e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "tsSumFehlStd", "tsSumFehlStdU", "tsZeugnisBem", "tsASV", "tsAUE", "tsBemerkungVersetzung"})
public final class DTOEnmLernabschnittsdaten {

	/** ID der Lernabschnittsdaten */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Der Zeitstempel der letzten Änderung an der Summe der Fehlstunden. */
	@Column(name = "tsSumFehlStd")
	@JsonProperty
	public String tsSumFehlStd;

	/** Der Zeitstempel der letzten Änderung an der Summe der unentschuldigten Fehlstunden. */
	@Column(name = "tsSumFehlStdU")
	@JsonProperty
	public String tsSumFehlStdU;

	/** Der Zeitstempel der letzten Änderung an den Zeugnisbemerkungen. */
	@Column(name = "tsZeugnisBem")
	@JsonProperty
	public String tsZeugnisBem;

	/** Der Zeitstempel der letzten Änderung an den Bemerkungen zum Arbeits- und Sozialverhalten. */
	@Column(name = "tsASV")
	@JsonProperty
	public String tsASV;

	/** Der Zeitstempel der letzten Änderung an den Bemerkungen zum außerunterrichtlichen Engagement. */
	@Column(name = "tsAUE")
	@JsonProperty
	public String tsAUE;

	/** Der Zeitstempel der letzten Änderung an den Bemerkungen zur Versetzung. */
	@Column(name = "tsBemerkungVersetzung")
	@JsonProperty
	public String tsBemerkungVersetzung;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOEnmLernabschnittsdaten ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOEnmLernabschnittsdaten() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOEnmLernabschnittsdaten ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param tsSumFehlStd   der Wert für das Attribut tsSumFehlStd
	 * @param tsSumFehlStdU   der Wert für das Attribut tsSumFehlStdU
	 * @param tsZeugnisBem   der Wert für das Attribut tsZeugnisBem
	 * @param tsASV   der Wert für das Attribut tsASV
	 * @param tsAUE   der Wert für das Attribut tsAUE
	 * @param tsBemerkungVersetzung   der Wert für das Attribut tsBemerkungVersetzung
	 */
	public DTOEnmLernabschnittsdaten(final Long ID, final String tsSumFehlStd, final String tsSumFehlStdU, final String tsZeugnisBem, final String tsASV, final String tsAUE, final String tsBemerkungVersetzung) {
		if (ID == null) {
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (tsSumFehlStd == null) {
			throw new NullPointerException("tsSumFehlStd must not be null");
		}
		this.tsSumFehlStd = tsSumFehlStd;
		if (tsSumFehlStdU == null) {
			throw new NullPointerException("tsSumFehlStdU must not be null");
		}
		this.tsSumFehlStdU = tsSumFehlStdU;
		if (tsZeugnisBem == null) {
			throw new NullPointerException("tsZeugnisBem must not be null");
		}
		this.tsZeugnisBem = tsZeugnisBem;
		if (tsASV == null) {
			throw new NullPointerException("tsASV must not be null");
		}
		this.tsASV = tsASV;
		if (tsAUE == null) {
			throw new NullPointerException("tsAUE must not be null");
		}
		this.tsAUE = tsAUE;
		if (tsBemerkungVersetzung == null) {
			throw new NullPointerException("tsBemerkungVersetzung must not be null");
		}
		this.tsBemerkungVersetzung = tsBemerkungVersetzung;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOEnmLernabschnittsdaten other = (DTOEnmLernabschnittsdaten) obj;
		if (ID == null) {
			if (other.ID != null)
				return false;
		} else if (!ID.equals(other.ID))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ID == null) ? 0 : ID.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOEnmLernabschnittsdaten(ID=" + this.ID + ", tsSumFehlStd=" + this.tsSumFehlStd + ", tsSumFehlStdU=" + this.tsSumFehlStdU + ", tsZeugnisBem=" + this.tsZeugnisBem + ", tsASV=" + this.tsASV + ", tsAUE=" + this.tsAUE + ", tsBemerkungVersetzung=" + this.tsBemerkungVersetzung + ")";
	}

}
