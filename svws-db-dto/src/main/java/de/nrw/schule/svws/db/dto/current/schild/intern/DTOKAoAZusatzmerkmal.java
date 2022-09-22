package de.nrw.schule.svws.db.dto.current.schild.intern;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Schildintern_KAoA_Zusatzmerkmal.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Schildintern_KAoA_Zusatzmerkmal")
@NamedQuery(name="DTOKAoAZusatzmerkmal.all", query="SELECT e FROM DTOKAoAZusatzmerkmal e")
@NamedQuery(name="DTOKAoAZusatzmerkmal.id", query="SELECT e FROM DTOKAoAZusatzmerkmal e WHERE e.ID = :value")
@NamedQuery(name="DTOKAoAZusatzmerkmal.id.multiple", query="SELECT e FROM DTOKAoAZusatzmerkmal e WHERE e.ID IN :value")
@NamedQuery(name="DTOKAoAZusatzmerkmal.gueltigvon", query="SELECT e FROM DTOKAoAZusatzmerkmal e WHERE e.gueltigVon = :value")
@NamedQuery(name="DTOKAoAZusatzmerkmal.gueltigvon.multiple", query="SELECT e FROM DTOKAoAZusatzmerkmal e WHERE e.gueltigVon IN :value")
@NamedQuery(name="DTOKAoAZusatzmerkmal.gueltigbis", query="SELECT e FROM DTOKAoAZusatzmerkmal e WHERE e.gueltigBis = :value")
@NamedQuery(name="DTOKAoAZusatzmerkmal.gueltigbis.multiple", query="SELECT e FROM DTOKAoAZusatzmerkmal e WHERE e.gueltigBis IN :value")
@NamedQuery(name="DTOKAoAZusatzmerkmal.zm_kuerzel", query="SELECT e FROM DTOKAoAZusatzmerkmal e WHERE e.ZM_Kuerzel = :value")
@NamedQuery(name="DTOKAoAZusatzmerkmal.zm_kuerzel.multiple", query="SELECT e FROM DTOKAoAZusatzmerkmal e WHERE e.ZM_Kuerzel IN :value")
@NamedQuery(name="DTOKAoAZusatzmerkmal.merkmal_id", query="SELECT e FROM DTOKAoAZusatzmerkmal e WHERE e.Merkmal_ID = :value")
@NamedQuery(name="DTOKAoAZusatzmerkmal.merkmal_id.multiple", query="SELECT e FROM DTOKAoAZusatzmerkmal e WHERE e.Merkmal_ID IN :value")
@NamedQuery(name="DTOKAoAZusatzmerkmal.zm_beschreibung", query="SELECT e FROM DTOKAoAZusatzmerkmal e WHERE e.ZM_Beschreibung = :value")
@NamedQuery(name="DTOKAoAZusatzmerkmal.zm_beschreibung.multiple", query="SELECT e FROM DTOKAoAZusatzmerkmal e WHERE e.ZM_Beschreibung IN :value")
@NamedQuery(name="DTOKAoAZusatzmerkmal.zm_option", query="SELECT e FROM DTOKAoAZusatzmerkmal e WHERE e.ZM_Option = :value")
@NamedQuery(name="DTOKAoAZusatzmerkmal.zm_option.multiple", query="SELECT e FROM DTOKAoAZusatzmerkmal e WHERE e.ZM_Option IN :value")
@NamedQuery(name="DTOKAoAZusatzmerkmal.zm_merkmal", query="SELECT e FROM DTOKAoAZusatzmerkmal e WHERE e.ZM_Merkmal = :value")
@NamedQuery(name="DTOKAoAZusatzmerkmal.zm_merkmal.multiple", query="SELECT e FROM DTOKAoAZusatzmerkmal e WHERE e.ZM_Merkmal IN :value")
@NamedQuery(name="DTOKAoAZusatzmerkmal.primaryKeyQuery", query="SELECT e FROM DTOKAoAZusatzmerkmal e WHERE e.ID = ?1")
@NamedQuery(name="DTOKAoAZusatzmerkmal.all.migration", query="SELECT e FROM DTOKAoAZusatzmerkmal e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","gueltigVon","gueltigBis","ZM_Kuerzel","Merkmal_ID","ZM_Beschreibung","ZM_Option","ZM_Merkmal"})
public class DTOKAoAZusatzmerkmal {

	/** Schildintern Tabelle: Eindeutige ID für den Datensatz */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Schildintern Tabelle: Der Datensatz ist gültig ab dem Datum */
	@Column(name = "gueltigVon")
	@JsonProperty
	public Integer gueltigVon;

	/** Schildintern Tabelle: Der Datensatz ist gültig bis zu dem Datum */
	@Column(name = "gueltigBis")
	@JsonProperty
	public Integer gueltigBis;

	/** Schildintern Tabelle: Kürzel des Zusatzmerkmals */
	@Column(name = "ZM_Kuerzel")
	@JsonProperty
	public String ZM_Kuerzel;

	/** Schildintern Tabelle: ID des übergeordneten Merkmal zum Zusatzmerkmal */
	@Column(name = "Merkmal_ID")
	@JsonProperty
	public Long Merkmal_ID;

	/** Schildintern Tabelle: Beschreibung (Klartext) des Zusatzmerkmal */
	@Column(name = "ZM_Beschreibung")
	@JsonProperty
	public String ZM_Beschreibung;

	/** Schildintern Tabelle: Zusatz Option die zum Zusatzmerkmal eintragbar ist */
	@Column(name = "ZM_Option")
	@JsonProperty
	public String ZM_Option;

	/** Schildintern Tabelle: DEPRECATED: Übergeordnetes Merkmal zum Zusatzmerkmal */
	@Column(name = "ZM_Merkmal")
	@JsonProperty
	public String ZM_Merkmal;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOKAoAZusatzmerkmal ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOKAoAZusatzmerkmal() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOKAoAZusatzmerkmal ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param ZM_Kuerzel   der Wert für das Attribut ZM_Kuerzel
	 * @param Merkmal_ID   der Wert für das Attribut Merkmal_ID
	 */
	public DTOKAoAZusatzmerkmal(final Long ID, final String ZM_Kuerzel, final Long Merkmal_ID) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (ZM_Kuerzel == null) { 
			throw new NullPointerException("ZM_Kuerzel must not be null");
		}
		this.ZM_Kuerzel = ZM_Kuerzel;
		if (Merkmal_ID == null) { 
			throw new NullPointerException("Merkmal_ID must not be null");
		}
		this.Merkmal_ID = Merkmal_ID;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOKAoAZusatzmerkmal other = (DTOKAoAZusatzmerkmal) obj;
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
		return "DTOKAoAZusatzmerkmal(ID=" + this.ID + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ", ZM_Kuerzel=" + this.ZM_Kuerzel + ", Merkmal_ID=" + this.Merkmal_ID + ", ZM_Beschreibung=" + this.ZM_Beschreibung + ", ZM_Option=" + this.ZM_Option + ", ZM_Merkmal=" + this.ZM_Merkmal + ")";
	}

}