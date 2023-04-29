package de.svws_nrw.db.dto.current.schild;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultMinusConverter;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultPlusConverter;


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
@NamedQuery(name = "DTOSchuelerWiedervorlage.all", query = "SELECT e FROM DTOSchuelerWiedervorlage e")
@NamedQuery(name = "DTOSchuelerWiedervorlage.id", query = "SELECT e FROM DTOSchuelerWiedervorlage e WHERE e.ID = :value")
@NamedQuery(name = "DTOSchuelerWiedervorlage.id.multiple", query = "SELECT e FROM DTOSchuelerWiedervorlage e WHERE e.ID IN :value")
@NamedQuery(name = "DTOSchuelerWiedervorlage.schueler_id", query = "SELECT e FROM DTOSchuelerWiedervorlage e WHERE e.Schueler_ID = :value")
@NamedQuery(name = "DTOSchuelerWiedervorlage.schueler_id.multiple", query = "SELECT e FROM DTOSchuelerWiedervorlage e WHERE e.Schueler_ID IN :value")
@NamedQuery(name = "DTOSchuelerWiedervorlage.bemerkung", query = "SELECT e FROM DTOSchuelerWiedervorlage e WHERE e.Bemerkung = :value")
@NamedQuery(name = "DTOSchuelerWiedervorlage.bemerkung.multiple", query = "SELECT e FROM DTOSchuelerWiedervorlage e WHERE e.Bemerkung IN :value")
@NamedQuery(name = "DTOSchuelerWiedervorlage.angelegtam", query = "SELECT e FROM DTOSchuelerWiedervorlage e WHERE e.AngelegtAm = :value")
@NamedQuery(name = "DTOSchuelerWiedervorlage.angelegtam.multiple", query = "SELECT e FROM DTOSchuelerWiedervorlage e WHERE e.AngelegtAm IN :value")
@NamedQuery(name = "DTOSchuelerWiedervorlage.wiedervorlageam", query = "SELECT e FROM DTOSchuelerWiedervorlage e WHERE e.WiedervorlageAm = :value")
@NamedQuery(name = "DTOSchuelerWiedervorlage.wiedervorlageam.multiple", query = "SELECT e FROM DTOSchuelerWiedervorlage e WHERE e.WiedervorlageAm IN :value")
@NamedQuery(name = "DTOSchuelerWiedervorlage.erledigtam", query = "SELECT e FROM DTOSchuelerWiedervorlage e WHERE e.ErledigtAm = :value")
@NamedQuery(name = "DTOSchuelerWiedervorlage.erledigtam.multiple", query = "SELECT e FROM DTOSchuelerWiedervorlage e WHERE e.ErledigtAm IN :value")
@NamedQuery(name = "DTOSchuelerWiedervorlage.user_id", query = "SELECT e FROM DTOSchuelerWiedervorlage e WHERE e.User_ID = :value")
@NamedQuery(name = "DTOSchuelerWiedervorlage.user_id.multiple", query = "SELECT e FROM DTOSchuelerWiedervorlage e WHERE e.User_ID IN :value")
@NamedQuery(name = "DTOSchuelerWiedervorlage.sekretariat", query = "SELECT e FROM DTOSchuelerWiedervorlage e WHERE e.Sekretariat = :value")
@NamedQuery(name = "DTOSchuelerWiedervorlage.sekretariat.multiple", query = "SELECT e FROM DTOSchuelerWiedervorlage e WHERE e.Sekretariat IN :value")
@NamedQuery(name = "DTOSchuelerWiedervorlage.typ", query = "SELECT e FROM DTOSchuelerWiedervorlage e WHERE e.Typ = :value")
@NamedQuery(name = "DTOSchuelerWiedervorlage.typ.multiple", query = "SELECT e FROM DTOSchuelerWiedervorlage e WHERE e.Typ IN :value")
@NamedQuery(name = "DTOSchuelerWiedervorlage.nichtloeschen", query = "SELECT e FROM DTOSchuelerWiedervorlage e WHERE e.NichtLoeschen = :value")
@NamedQuery(name = "DTOSchuelerWiedervorlage.nichtloeschen.multiple", query = "SELECT e FROM DTOSchuelerWiedervorlage e WHERE e.NichtLoeschen IN :value")
@NamedQuery(name = "DTOSchuelerWiedervorlage.primaryKeyQuery", query = "SELECT e FROM DTOSchuelerWiedervorlage e WHERE e.ID = ?1")
@NamedQuery(name = "DTOSchuelerWiedervorlage.all.migration", query = "SELECT e FROM DTOSchuelerWiedervorlage e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Schueler_ID", "Bemerkung", "AngelegtAm", "WiedervorlageAm", "ErledigtAm", "User_ID", "Sekretariat", "Typ", "NichtLoeschen"})
public final class DTOSchuelerWiedervorlage {

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
		if (ID != other.ID)
			return false;
		return true;
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
