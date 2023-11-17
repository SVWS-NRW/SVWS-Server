package de.svws_nrw.db.dto.migration.schild;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverter;
import de.svws_nrw.db.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverter;


import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverterSerializer;
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverterDeserializer;
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverterSerializer;
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerWiedervorlage.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerWiedervorlage")
@NamedQuery(name = "MigrationDTOSchuelerWiedervorlage.all", query = "SELECT e FROM MigrationDTOSchuelerWiedervorlage e")
@NamedQuery(name = "MigrationDTOSchuelerWiedervorlage.id", query = "SELECT e FROM MigrationDTOSchuelerWiedervorlage e WHERE e.ID = :value")
@NamedQuery(name = "MigrationDTOSchuelerWiedervorlage.id.multiple", query = "SELECT e FROM MigrationDTOSchuelerWiedervorlage e WHERE e.ID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerWiedervorlage.schueler_id", query = "SELECT e FROM MigrationDTOSchuelerWiedervorlage e WHERE e.Schueler_ID = :value")
@NamedQuery(name = "MigrationDTOSchuelerWiedervorlage.schueler_id.multiple", query = "SELECT e FROM MigrationDTOSchuelerWiedervorlage e WHERE e.Schueler_ID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerWiedervorlage.schulnreigner", query = "SELECT e FROM MigrationDTOSchuelerWiedervorlage e WHERE e.SchulnrEigner = :value")
@NamedQuery(name = "MigrationDTOSchuelerWiedervorlage.schulnreigner.multiple", query = "SELECT e FROM MigrationDTOSchuelerWiedervorlage e WHERE e.SchulnrEigner IN :value")
@NamedQuery(name = "MigrationDTOSchuelerWiedervorlage.bemerkung", query = "SELECT e FROM MigrationDTOSchuelerWiedervorlage e WHERE e.Bemerkung = :value")
@NamedQuery(name = "MigrationDTOSchuelerWiedervorlage.bemerkung.multiple", query = "SELECT e FROM MigrationDTOSchuelerWiedervorlage e WHERE e.Bemerkung IN :value")
@NamedQuery(name = "MigrationDTOSchuelerWiedervorlage.angelegtam", query = "SELECT e FROM MigrationDTOSchuelerWiedervorlage e WHERE e.AngelegtAm = :value")
@NamedQuery(name = "MigrationDTOSchuelerWiedervorlage.angelegtam.multiple", query = "SELECT e FROM MigrationDTOSchuelerWiedervorlage e WHERE e.AngelegtAm IN :value")
@NamedQuery(name = "MigrationDTOSchuelerWiedervorlage.wiedervorlageam", query = "SELECT e FROM MigrationDTOSchuelerWiedervorlage e WHERE e.WiedervorlageAm = :value")
@NamedQuery(name = "MigrationDTOSchuelerWiedervorlage.wiedervorlageam.multiple", query = "SELECT e FROM MigrationDTOSchuelerWiedervorlage e WHERE e.WiedervorlageAm IN :value")
@NamedQuery(name = "MigrationDTOSchuelerWiedervorlage.erledigtam", query = "SELECT e FROM MigrationDTOSchuelerWiedervorlage e WHERE e.ErledigtAm = :value")
@NamedQuery(name = "MigrationDTOSchuelerWiedervorlage.erledigtam.multiple", query = "SELECT e FROM MigrationDTOSchuelerWiedervorlage e WHERE e.ErledigtAm IN :value")
@NamedQuery(name = "MigrationDTOSchuelerWiedervorlage.user_id", query = "SELECT e FROM MigrationDTOSchuelerWiedervorlage e WHERE e.User_ID = :value")
@NamedQuery(name = "MigrationDTOSchuelerWiedervorlage.user_id.multiple", query = "SELECT e FROM MigrationDTOSchuelerWiedervorlage e WHERE e.User_ID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerWiedervorlage.sekretariat", query = "SELECT e FROM MigrationDTOSchuelerWiedervorlage e WHERE e.Sekretariat = :value")
@NamedQuery(name = "MigrationDTOSchuelerWiedervorlage.sekretariat.multiple", query = "SELECT e FROM MigrationDTOSchuelerWiedervorlage e WHERE e.Sekretariat IN :value")
@NamedQuery(name = "MigrationDTOSchuelerWiedervorlage.typ", query = "SELECT e FROM MigrationDTOSchuelerWiedervorlage e WHERE e.Typ = :value")
@NamedQuery(name = "MigrationDTOSchuelerWiedervorlage.typ.multiple", query = "SELECT e FROM MigrationDTOSchuelerWiedervorlage e WHERE e.Typ IN :value")
@NamedQuery(name = "MigrationDTOSchuelerWiedervorlage.nichtloeschen", query = "SELECT e FROM MigrationDTOSchuelerWiedervorlage e WHERE e.NichtLoeschen = :value")
@NamedQuery(name = "MigrationDTOSchuelerWiedervorlage.nichtloeschen.multiple", query = "SELECT e FROM MigrationDTOSchuelerWiedervorlage e WHERE e.NichtLoeschen IN :value")
@NamedQuery(name = "MigrationDTOSchuelerWiedervorlage.primaryKeyQuery", query = "SELECT e FROM MigrationDTOSchuelerWiedervorlage e WHERE e.ID = ?1")
@NamedQuery(name = "MigrationDTOSchuelerWiedervorlage.primaryKeyQuery.multiple", query = "SELECT e FROM MigrationDTOSchuelerWiedervorlage e WHERE e.ID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerWiedervorlage.all.migration", query = "SELECT e FROM MigrationDTOSchuelerWiedervorlage e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Schueler_ID", "SchulnrEigner", "Bemerkung", "AngelegtAm", "WiedervorlageAm", "ErledigtAm", "User_ID", "Sekretariat", "Typ", "NichtLoeschen"})
public final class MigrationDTOSchuelerWiedervorlage {

	/** ID des Wiedervorlageeitrags beim Schüler */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** SchülerID des Wiedervorlageeintrags */
	@Column(name = "Schueler_ID")
	@JsonProperty
	public Long Schueler_ID;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

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
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sekretariat;

	/** Typ des Wiedervorlageeintrags */
	@Column(name = "Typ")
	@JsonProperty
	public String Typ;

	/** Steuert die automatische Löschung der Einträge */
	@Column(name = "NichtLoeschen")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean NichtLoeschen;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuelerWiedervorlage ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOSchuelerWiedervorlage() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuelerWiedervorlage ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 * @param SchulnrEigner   der Wert für das Attribut SchulnrEigner
	 */
	public MigrationDTOSchuelerWiedervorlage(final Long ID, final Long Schueler_ID, final Integer SchulnrEigner) {
		if (ID == null) {
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Schueler_ID == null) {
			throw new NullPointerException("Schueler_ID must not be null");
		}
		this.Schueler_ID = Schueler_ID;
		if (SchulnrEigner == null) {
			throw new NullPointerException("SchulnrEigner must not be null");
		}
		this.SchulnrEigner = SchulnrEigner;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOSchuelerWiedervorlage other = (MigrationDTOSchuelerWiedervorlage) obj;
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
		return "MigrationDTOSchuelerWiedervorlage(ID=" + this.ID + ", Schueler_ID=" + this.Schueler_ID + ", SchulnrEigner=" + this.SchulnrEigner + ", Bemerkung=" + this.Bemerkung + ", AngelegtAm=" + this.AngelegtAm + ", WiedervorlageAm=" + this.WiedervorlageAm + ", ErledigtAm=" + this.ErledigtAm + ", User_ID=" + this.User_ID + ", Sekretariat=" + this.Sekretariat + ", Typ=" + this.Typ + ", NichtLoeschen=" + this.NichtLoeschen + ")";
	}

}
