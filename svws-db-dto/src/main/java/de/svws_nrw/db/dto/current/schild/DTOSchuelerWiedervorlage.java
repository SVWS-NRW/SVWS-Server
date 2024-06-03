package de.svws_nrw.db.dto.current.schild;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultMinusConverter;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultPlusConverter;


import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultMinusConverterSerializer;
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultMinusConverterDeserializer;
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultPlusConverterSerializer;
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultPlusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerWiedervorlage.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerWiedervorlage")
@JsonPropertyOrder({"ID", "Schueler_ID", "Bemerkung", "AngelegtAm", "WiedervorlageAm", "ErledigtAm", "User_ID", "Sekretariat", "Typ", "NichtLoeschen"})
public final class DTOSchuelerWiedervorlage {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOSchuelerWiedervorlage e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOSchuelerWiedervorlage e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOSchuelerWiedervorlage e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOSchuelerWiedervorlage e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOSchuelerWiedervorlage e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOSchuelerWiedervorlage e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schueler_ID */
	public static final String QUERY_BY_SCHUELER_ID = "SELECT e FROM DTOSchuelerWiedervorlage e WHERE e.Schueler_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schueler_ID */
	public static final String QUERY_LIST_BY_SCHUELER_ID = "SELECT e FROM DTOSchuelerWiedervorlage e WHERE e.Schueler_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Bemerkung */
	public static final String QUERY_BY_BEMERKUNG = "SELECT e FROM DTOSchuelerWiedervorlage e WHERE e.Bemerkung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Bemerkung */
	public static final String QUERY_LIST_BY_BEMERKUNG = "SELECT e FROM DTOSchuelerWiedervorlage e WHERE e.Bemerkung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AngelegtAm */
	public static final String QUERY_BY_ANGELEGTAM = "SELECT e FROM DTOSchuelerWiedervorlage e WHERE e.AngelegtAm = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AngelegtAm */
	public static final String QUERY_LIST_BY_ANGELEGTAM = "SELECT e FROM DTOSchuelerWiedervorlage e WHERE e.AngelegtAm IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes WiedervorlageAm */
	public static final String QUERY_BY_WIEDERVORLAGEAM = "SELECT e FROM DTOSchuelerWiedervorlage e WHERE e.WiedervorlageAm = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes WiedervorlageAm */
	public static final String QUERY_LIST_BY_WIEDERVORLAGEAM = "SELECT e FROM DTOSchuelerWiedervorlage e WHERE e.WiedervorlageAm IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ErledigtAm */
	public static final String QUERY_BY_ERLEDIGTAM = "SELECT e FROM DTOSchuelerWiedervorlage e WHERE e.ErledigtAm = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ErledigtAm */
	public static final String QUERY_LIST_BY_ERLEDIGTAM = "SELECT e FROM DTOSchuelerWiedervorlage e WHERE e.ErledigtAm IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes User_ID */
	public static final String QUERY_BY_USER_ID = "SELECT e FROM DTOSchuelerWiedervorlage e WHERE e.User_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes User_ID */
	public static final String QUERY_LIST_BY_USER_ID = "SELECT e FROM DTOSchuelerWiedervorlage e WHERE e.User_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Sekretariat */
	public static final String QUERY_BY_SEKRETARIAT = "SELECT e FROM DTOSchuelerWiedervorlage e WHERE e.Sekretariat = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Sekretariat */
	public static final String QUERY_LIST_BY_SEKRETARIAT = "SELECT e FROM DTOSchuelerWiedervorlage e WHERE e.Sekretariat IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Typ */
	public static final String QUERY_BY_TYP = "SELECT e FROM DTOSchuelerWiedervorlage e WHERE e.Typ = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Typ */
	public static final String QUERY_LIST_BY_TYP = "SELECT e FROM DTOSchuelerWiedervorlage e WHERE e.Typ IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes NichtLoeschen */
	public static final String QUERY_BY_NICHTLOESCHEN = "SELECT e FROM DTOSchuelerWiedervorlage e WHERE e.NichtLoeschen = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes NichtLoeschen */
	public static final String QUERY_LIST_BY_NICHTLOESCHEN = "SELECT e FROM DTOSchuelerWiedervorlage e WHERE e.NichtLoeschen IN ?1";

	/** ID des Wiedervorlageeitrags beim Schüler */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** SchülerID des Wiedervorlageeintrags */
	@Column(name = "Schueler_ID")
	@JsonProperty
	public long Schueler_ID;

	/** Bemerkung des Wiedervorlageeintrags */
	@Column(name = "Bemerkung")
	@JsonProperty
	public String Bemerkung;

	/** Datum Anlage des Wiedervorlageeintrags */
	@Column(name = "AngelegtAm")
	@JsonProperty
	public String AngelegtAm;

	/** Datum Wiedervorlage am */
	@Column(name = "WiedervorlageAm")
	@JsonProperty
	public String WiedervorlageAm;

	/** Datum Erledigung  des Wiedervorlageeintrags */
	@Column(name = "ErledigtAm")
	@JsonProperty
	public String ErledigtAm;

	/** UserID  des Wiedervorlageeintrags */
	@Column(name = "User_ID")
	@JsonProperty
	public Long User_ID;

	/** Steuert die Sichtbarkeit für den User Sekretariat */
	@Column(name = "Sekretariat")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sekretariat;

	/** Typ des Wiedervorlageeintrags */
	@Column(name = "Typ")
	@JsonProperty
	public String Typ;

	/** Steuert die automatische Löschung der Einträge */
	@Column(name = "NichtLoeschen")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean NichtLoeschen;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerWiedervorlage ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSchuelerWiedervorlage() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerWiedervorlage ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 */
	public DTOSchuelerWiedervorlage(final long ID, final long Schueler_ID) {
		this.ID = ID;
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
		DTOSchuelerWiedervorlage other = (DTOSchuelerWiedervorlage) obj;
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
		return "DTOSchuelerWiedervorlage(ID=" + this.ID + ", Schueler_ID=" + this.Schueler_ID + ", Bemerkung=" + this.Bemerkung + ", AngelegtAm=" + this.AngelegtAm + ", WiedervorlageAm=" + this.WiedervorlageAm + ", ErledigtAm=" + this.ErledigtAm + ", User_ID=" + this.User_ID + ", Sekretariat=" + this.Sekretariat + ", Typ=" + this.Typ + ", NichtLoeschen=" + this.NichtLoeschen + ")";
	}

}
