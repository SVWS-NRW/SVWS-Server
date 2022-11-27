package de.nrw.schule.svws.db.dto.rev8.svws.enm;

import de.nrw.schule.svws.db.DBEntityManager;

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
@NamedQuery(name="Rev8DTOEnmLernabschnittsdaten.all", query="SELECT e FROM Rev8DTOEnmLernabschnittsdaten e")
@NamedQuery(name="Rev8DTOEnmLernabschnittsdaten.id", query="SELECT e FROM Rev8DTOEnmLernabschnittsdaten e WHERE e.ID = :value")
@NamedQuery(name="Rev8DTOEnmLernabschnittsdaten.id.multiple", query="SELECT e FROM Rev8DTOEnmLernabschnittsdaten e WHERE e.ID IN :value")
@NamedQuery(name="Rev8DTOEnmLernabschnittsdaten.tssumfehlstd", query="SELECT e FROM Rev8DTOEnmLernabschnittsdaten e WHERE e.tsSumFehlStd = :value")
@NamedQuery(name="Rev8DTOEnmLernabschnittsdaten.tssumfehlstd.multiple", query="SELECT e FROM Rev8DTOEnmLernabschnittsdaten e WHERE e.tsSumFehlStd IN :value")
@NamedQuery(name="Rev8DTOEnmLernabschnittsdaten.tssumfehlstdu", query="SELECT e FROM Rev8DTOEnmLernabschnittsdaten e WHERE e.tsSumFehlStdU = :value")
@NamedQuery(name="Rev8DTOEnmLernabschnittsdaten.tssumfehlstdu.multiple", query="SELECT e FROM Rev8DTOEnmLernabschnittsdaten e WHERE e.tsSumFehlStdU IN :value")
@NamedQuery(name="Rev8DTOEnmLernabschnittsdaten.tszeugnisbem", query="SELECT e FROM Rev8DTOEnmLernabschnittsdaten e WHERE e.tsZeugnisBem = :value")
@NamedQuery(name="Rev8DTOEnmLernabschnittsdaten.tszeugnisbem.multiple", query="SELECT e FROM Rev8DTOEnmLernabschnittsdaten e WHERE e.tsZeugnisBem IN :value")
@NamedQuery(name="Rev8DTOEnmLernabschnittsdaten.tsasv", query="SELECT e FROM Rev8DTOEnmLernabschnittsdaten e WHERE e.tsASV = :value")
@NamedQuery(name="Rev8DTOEnmLernabschnittsdaten.tsasv.multiple", query="SELECT e FROM Rev8DTOEnmLernabschnittsdaten e WHERE e.tsASV IN :value")
@NamedQuery(name="Rev8DTOEnmLernabschnittsdaten.tsaue", query="SELECT e FROM Rev8DTOEnmLernabschnittsdaten e WHERE e.tsAUE = :value")
@NamedQuery(name="Rev8DTOEnmLernabschnittsdaten.tsaue.multiple", query="SELECT e FROM Rev8DTOEnmLernabschnittsdaten e WHERE e.tsAUE IN :value")
@NamedQuery(name="Rev8DTOEnmLernabschnittsdaten.tsbemerkungversetzung", query="SELECT e FROM Rev8DTOEnmLernabschnittsdaten e WHERE e.tsBemerkungVersetzung = :value")
@NamedQuery(name="Rev8DTOEnmLernabschnittsdaten.tsbemerkungversetzung.multiple", query="SELECT e FROM Rev8DTOEnmLernabschnittsdaten e WHERE e.tsBemerkungVersetzung IN :value")
@NamedQuery(name="Rev8DTOEnmLernabschnittsdaten.primaryKeyQuery", query="SELECT e FROM Rev8DTOEnmLernabschnittsdaten e WHERE e.ID = ?1")
@NamedQuery(name="Rev8DTOEnmLernabschnittsdaten.all.migration", query="SELECT e FROM Rev8DTOEnmLernabschnittsdaten e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","tsSumFehlStd","tsSumFehlStdU","tsZeugnisBem","tsASV","tsAUE","tsBemerkungVersetzung"})
public class Rev8DTOEnmLernabschnittsdaten {

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
	 * Erstellt ein neues Objekt der Klasse Rev8DTOEnmLernabschnittsdaten ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev8DTOEnmLernabschnittsdaten() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOEnmLernabschnittsdaten ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param tsSumFehlStd   der Wert für das Attribut tsSumFehlStd
	 * @param tsSumFehlStdU   der Wert für das Attribut tsSumFehlStdU
	 * @param tsZeugnisBem   der Wert für das Attribut tsZeugnisBem
	 * @param tsASV   der Wert für das Attribut tsASV
	 * @param tsAUE   der Wert für das Attribut tsAUE
	 * @param tsBemerkungVersetzung   der Wert für das Attribut tsBemerkungVersetzung
	 */
	public Rev8DTOEnmLernabschnittsdaten(final Long ID, final String tsSumFehlStd, final String tsSumFehlStdU, final String tsZeugnisBem, final String tsASV, final String tsAUE, final String tsBemerkungVersetzung) {
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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev8DTOEnmLernabschnittsdaten other = (Rev8DTOEnmLernabschnittsdaten) obj;
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
		return "Rev8DTOEnmLernabschnittsdaten(ID=" + this.ID + ", tsSumFehlStd=" + this.tsSumFehlStd + ", tsSumFehlStdU=" + this.tsSumFehlStdU + ", tsZeugnisBem=" + this.tsZeugnisBem + ", tsASV=" + this.tsASV + ", tsAUE=" + this.tsAUE + ", tsBemerkungVersetzung=" + this.tsBemerkungVersetzung + ")";
	}

}