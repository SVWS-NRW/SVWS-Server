package de.svws_nrw.db.dto.current.svws.timestamps;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle TimestampsSchuelerLernabschnittsdaten.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "TimestampsSchuelerLernabschnittsdaten")
@JsonPropertyOrder({"ID", "tsSumFehlStd", "tsSumFehlStdU", "tsZeugnisBem", "tsASV", "tsAUE", "tsLELS", "tsESF", "tsBemerkungFSP", "tsBemerkungVersetzung"})
public final class DTOTimestampsSchuelerLernabschnittsdaten {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOTimestampsSchuelerLernabschnittsdaten e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOTimestampsSchuelerLernabschnittsdaten e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOTimestampsSchuelerLernabschnittsdaten e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOTimestampsSchuelerLernabschnittsdaten e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOTimestampsSchuelerLernabschnittsdaten e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOTimestampsSchuelerLernabschnittsdaten e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes tsSumFehlStd */
	public static final String QUERY_BY_TSSUMFEHLSTD = "SELECT e FROM DTOTimestampsSchuelerLernabschnittsdaten e WHERE e.tsSumFehlStd = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes tsSumFehlStd */
	public static final String QUERY_LIST_BY_TSSUMFEHLSTD = "SELECT e FROM DTOTimestampsSchuelerLernabschnittsdaten e WHERE e.tsSumFehlStd IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes tsSumFehlStdU */
	public static final String QUERY_BY_TSSUMFEHLSTDU = "SELECT e FROM DTOTimestampsSchuelerLernabschnittsdaten e WHERE e.tsSumFehlStdU = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes tsSumFehlStdU */
	public static final String QUERY_LIST_BY_TSSUMFEHLSTDU = "SELECT e FROM DTOTimestampsSchuelerLernabschnittsdaten e WHERE e.tsSumFehlStdU IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes tsZeugnisBem */
	public static final String QUERY_BY_TSZEUGNISBEM = "SELECT e FROM DTOTimestampsSchuelerLernabschnittsdaten e WHERE e.tsZeugnisBem = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes tsZeugnisBem */
	public static final String QUERY_LIST_BY_TSZEUGNISBEM = "SELECT e FROM DTOTimestampsSchuelerLernabschnittsdaten e WHERE e.tsZeugnisBem IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes tsASV */
	public static final String QUERY_BY_TSASV = "SELECT e FROM DTOTimestampsSchuelerLernabschnittsdaten e WHERE e.tsASV = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes tsASV */
	public static final String QUERY_LIST_BY_TSASV = "SELECT e FROM DTOTimestampsSchuelerLernabschnittsdaten e WHERE e.tsASV IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes tsAUE */
	public static final String QUERY_BY_TSAUE = "SELECT e FROM DTOTimestampsSchuelerLernabschnittsdaten e WHERE e.tsAUE = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes tsAUE */
	public static final String QUERY_LIST_BY_TSAUE = "SELECT e FROM DTOTimestampsSchuelerLernabschnittsdaten e WHERE e.tsAUE IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes tsLELS */
	public static final String QUERY_BY_TSLELS = "SELECT e FROM DTOTimestampsSchuelerLernabschnittsdaten e WHERE e.tsLELS = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes tsLELS */
	public static final String QUERY_LIST_BY_TSLELS = "SELECT e FROM DTOTimestampsSchuelerLernabschnittsdaten e WHERE e.tsLELS IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes tsESF */
	public static final String QUERY_BY_TSESF = "SELECT e FROM DTOTimestampsSchuelerLernabschnittsdaten e WHERE e.tsESF = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes tsESF */
	public static final String QUERY_LIST_BY_TSESF = "SELECT e FROM DTOTimestampsSchuelerLernabschnittsdaten e WHERE e.tsESF IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes tsBemerkungFSP */
	public static final String QUERY_BY_TSBEMERKUNGFSP = "SELECT e FROM DTOTimestampsSchuelerLernabschnittsdaten e WHERE e.tsBemerkungFSP = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes tsBemerkungFSP */
	public static final String QUERY_LIST_BY_TSBEMERKUNGFSP = "SELECT e FROM DTOTimestampsSchuelerLernabschnittsdaten e WHERE e.tsBemerkungFSP IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes tsBemerkungVersetzung */
	public static final String QUERY_BY_TSBEMERKUNGVERSETZUNG = "SELECT e FROM DTOTimestampsSchuelerLernabschnittsdaten e WHERE e.tsBemerkungVersetzung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes tsBemerkungVersetzung */
	public static final String QUERY_LIST_BY_TSBEMERKUNGVERSETZUNG = "SELECT e FROM DTOTimestampsSchuelerLernabschnittsdaten e WHERE e.tsBemerkungVersetzung IN ?1";

	/** ID der Lernabschnittsdaten */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

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

	/** Der Zeitstempel der letzten Änderung zu der Lern und Leistungsentwicklung (LELS) in den Fächern. */
	@Column(name = "tsLELS")
	@JsonProperty
	public String tsLELS;

	/** Der Zeitstempel der letzten Änderung an der "Empfehlung der Schulform" beim Übergang von Primar- nach SekI. */
	@Column(name = "tsESF")
	@JsonProperty
	public String tsESF;

	/** Der Zeitstempel der letzten Änderung an der Förderschwerpunktbemerkung. */
	@Column(name = "tsBemerkungFSP")
	@JsonProperty
	public String tsBemerkungFSP;

	/** Der Zeitstempel der letzten Änderung an den Bemerkungen zur Versetzung. */
	@Column(name = "tsBemerkungVersetzung")
	@JsonProperty
	public String tsBemerkungVersetzung;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOTimestampsSchuelerLernabschnittsdaten ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOTimestampsSchuelerLernabschnittsdaten() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOTimestampsSchuelerLernabschnittsdaten ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param tsSumFehlStd   der Wert für das Attribut tsSumFehlStd
	 * @param tsSumFehlStdU   der Wert für das Attribut tsSumFehlStdU
	 * @param tsZeugnisBem   der Wert für das Attribut tsZeugnisBem
	 * @param tsASV   der Wert für das Attribut tsASV
	 * @param tsAUE   der Wert für das Attribut tsAUE
	 * @param tsLELS   der Wert für das Attribut tsLELS
	 * @param tsESF   der Wert für das Attribut tsESF
	 * @param tsBemerkungFSP   der Wert für das Attribut tsBemerkungFSP
	 * @param tsBemerkungVersetzung   der Wert für das Attribut tsBemerkungVersetzung
	 */
	public DTOTimestampsSchuelerLernabschnittsdaten(final long ID, final String tsSumFehlStd, final String tsSumFehlStdU, final String tsZeugnisBem, final String tsASV, final String tsAUE, final String tsLELS, final String tsESF, final String tsBemerkungFSP, final String tsBemerkungVersetzung) {
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
		if (tsLELS == null) {
			throw new NullPointerException("tsLELS must not be null");
		}
		this.tsLELS = tsLELS;
		if (tsESF == null) {
			throw new NullPointerException("tsESF must not be null");
		}
		this.tsESF = tsESF;
		if (tsBemerkungFSP == null) {
			throw new NullPointerException("tsBemerkungFSP must not be null");
		}
		this.tsBemerkungFSP = tsBemerkungFSP;
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
		DTOTimestampsSchuelerLernabschnittsdaten other = (DTOTimestampsSchuelerLernabschnittsdaten) obj;
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
		return "DTOTimestampsSchuelerLernabschnittsdaten(ID=" + this.ID + ", tsSumFehlStd=" + this.tsSumFehlStd + ", tsSumFehlStdU=" + this.tsSumFehlStdU + ", tsZeugnisBem=" + this.tsZeugnisBem + ", tsASV=" + this.tsASV + ", tsAUE=" + this.tsAUE + ", tsLELS=" + this.tsLELS + ", tsESF=" + this.tsESF + ", tsBemerkungFSP=" + this.tsBemerkungFSP + ", tsBemerkungVersetzung=" + this.tsBemerkungVersetzung + ")";
	}

}
