package de.nrw.schule.svws.db.dto.rev8.schild;

import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.converter.current.BooleanPlusMinusDefaultMinusConverter;
import de.nrw.schule.svws.db.converter.current.BooleanPlusMinusDefaultPlusConverter;


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
import de.nrw.schule.svws.csv.converter.current.BooleanPlusMinusDefaultMinusConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.BooleanPlusMinusDefaultMinusConverterDeserializer;
import de.nrw.schule.svws.csv.converter.current.BooleanPlusMinusDefaultPlusConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.BooleanPlusMinusDefaultPlusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerWiedervorlage.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerWiedervorlage")
@NamedQuery(name="Rev8DTOSchuelerWiedervorlage.all", query="SELECT e FROM Rev8DTOSchuelerWiedervorlage e")
@NamedQuery(name="Rev8DTOSchuelerWiedervorlage.id", query="SELECT e FROM Rev8DTOSchuelerWiedervorlage e WHERE e.ID = :value")
@NamedQuery(name="Rev8DTOSchuelerWiedervorlage.id.multiple", query="SELECT e FROM Rev8DTOSchuelerWiedervorlage e WHERE e.ID IN :value")
@NamedQuery(name="Rev8DTOSchuelerWiedervorlage.schueler_id", query="SELECT e FROM Rev8DTOSchuelerWiedervorlage e WHERE e.Schueler_ID = :value")
@NamedQuery(name="Rev8DTOSchuelerWiedervorlage.schueler_id.multiple", query="SELECT e FROM Rev8DTOSchuelerWiedervorlage e WHERE e.Schueler_ID IN :value")
@NamedQuery(name="Rev8DTOSchuelerWiedervorlage.bemerkung", query="SELECT e FROM Rev8DTOSchuelerWiedervorlage e WHERE e.Bemerkung = :value")
@NamedQuery(name="Rev8DTOSchuelerWiedervorlage.bemerkung.multiple", query="SELECT e FROM Rev8DTOSchuelerWiedervorlage e WHERE e.Bemerkung IN :value")
@NamedQuery(name="Rev8DTOSchuelerWiedervorlage.angelegtam", query="SELECT e FROM Rev8DTOSchuelerWiedervorlage e WHERE e.AngelegtAm = :value")
@NamedQuery(name="Rev8DTOSchuelerWiedervorlage.angelegtam.multiple", query="SELECT e FROM Rev8DTOSchuelerWiedervorlage e WHERE e.AngelegtAm IN :value")
@NamedQuery(name="Rev8DTOSchuelerWiedervorlage.wiedervorlageam", query="SELECT e FROM Rev8DTOSchuelerWiedervorlage e WHERE e.WiedervorlageAm = :value")
@NamedQuery(name="Rev8DTOSchuelerWiedervorlage.wiedervorlageam.multiple", query="SELECT e FROM Rev8DTOSchuelerWiedervorlage e WHERE e.WiedervorlageAm IN :value")
@NamedQuery(name="Rev8DTOSchuelerWiedervorlage.erledigtam", query="SELECT e FROM Rev8DTOSchuelerWiedervorlage e WHERE e.ErledigtAm = :value")
@NamedQuery(name="Rev8DTOSchuelerWiedervorlage.erledigtam.multiple", query="SELECT e FROM Rev8DTOSchuelerWiedervorlage e WHERE e.ErledigtAm IN :value")
@NamedQuery(name="Rev8DTOSchuelerWiedervorlage.user_id", query="SELECT e FROM Rev8DTOSchuelerWiedervorlage e WHERE e.User_ID = :value")
@NamedQuery(name="Rev8DTOSchuelerWiedervorlage.user_id.multiple", query="SELECT e FROM Rev8DTOSchuelerWiedervorlage e WHERE e.User_ID IN :value")
@NamedQuery(name="Rev8DTOSchuelerWiedervorlage.sekretariat", query="SELECT e FROM Rev8DTOSchuelerWiedervorlage e WHERE e.Sekretariat = :value")
@NamedQuery(name="Rev8DTOSchuelerWiedervorlage.sekretariat.multiple", query="SELECT e FROM Rev8DTOSchuelerWiedervorlage e WHERE e.Sekretariat IN :value")
@NamedQuery(name="Rev8DTOSchuelerWiedervorlage.typ", query="SELECT e FROM Rev8DTOSchuelerWiedervorlage e WHERE e.Typ = :value")
@NamedQuery(name="Rev8DTOSchuelerWiedervorlage.typ.multiple", query="SELECT e FROM Rev8DTOSchuelerWiedervorlage e WHERE e.Typ IN :value")
@NamedQuery(name="Rev8DTOSchuelerWiedervorlage.nichtloeschen", query="SELECT e FROM Rev8DTOSchuelerWiedervorlage e WHERE e.NichtLoeschen = :value")
@NamedQuery(name="Rev8DTOSchuelerWiedervorlage.nichtloeschen.multiple", query="SELECT e FROM Rev8DTOSchuelerWiedervorlage e WHERE e.NichtLoeschen IN :value")
@NamedQuery(name="Rev8DTOSchuelerWiedervorlage.primaryKeyQuery", query="SELECT e FROM Rev8DTOSchuelerWiedervorlage e WHERE e.ID = ?1")
@NamedQuery(name="Rev8DTOSchuelerWiedervorlage.all.migration", query="SELECT e FROM Rev8DTOSchuelerWiedervorlage e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Schueler_ID","Bemerkung","AngelegtAm","WiedervorlageAm","ErledigtAm","User_ID","Sekretariat","Typ","NichtLoeschen"})
public class Rev8DTOSchuelerWiedervorlage {

	/** ID des Wiedervorlageeitrags beim Schüler */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** SchülerID des Wiedervorlageeintrags */
	@Column(name = "Schueler_ID")
	@JsonProperty
	public Long Schueler_ID;

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
	@Convert(converter=BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sekretariat;

	/** Typ des Wiedervorlageeintrags */
	@Column(name = "Typ")
	@JsonProperty
	public String Typ;

	/** Steuert die automatische Löschung der Einträge */
	@Column(name = "NichtLoeschen")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean NichtLoeschen;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOSchuelerWiedervorlage ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev8DTOSchuelerWiedervorlage() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOSchuelerWiedervorlage ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 */
	public Rev8DTOSchuelerWiedervorlage(final Long ID, final Long Schueler_ID) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Schueler_ID == null) { 
			throw new NullPointerException("Schueler_ID must not be null");
		}
		this.Schueler_ID = Schueler_ID;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev8DTOSchuelerWiedervorlage other = (Rev8DTOSchuelerWiedervorlage) obj;
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
		return "Rev8DTOSchuelerWiedervorlage(ID=" + this.ID + ", Schueler_ID=" + this.Schueler_ID + ", Bemerkung=" + this.Bemerkung + ", AngelegtAm=" + this.AngelegtAm + ", WiedervorlageAm=" + this.WiedervorlageAm + ", ErledigtAm=" + this.ErledigtAm + ", User_ID=" + this.User_ID + ", Sekretariat=" + this.Sekretariat + ", Typ=" + this.Typ + ", NichtLoeschen=" + this.NichtLoeschen + ")";
	}

}