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
 * Diese Klasse dient als DTO für die Datenbanktabelle Schildintern_KAoA_SBO_Ebene4.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Schildintern_KAoA_SBO_Ebene4")
@NamedQuery(name="DTOKAoASBOEB4.all", query="SELECT e FROM DTOKAoASBOEB4 e")
@NamedQuery(name="DTOKAoASBOEB4.id", query="SELECT e FROM DTOKAoASBOEB4 e WHERE e.ID = :value")
@NamedQuery(name="DTOKAoASBOEB4.id.multiple", query="SELECT e FROM DTOKAoASBOEB4 e WHERE e.ID IN :value")
@NamedQuery(name="DTOKAoASBOEB4.gueltigvon", query="SELECT e FROM DTOKAoASBOEB4 e WHERE e.gueltigVon = :value")
@NamedQuery(name="DTOKAoASBOEB4.gueltigvon.multiple", query="SELECT e FROM DTOKAoASBOEB4 e WHERE e.gueltigVon IN :value")
@NamedQuery(name="DTOKAoASBOEB4.gueltigbis", query="SELECT e FROM DTOKAoASBOEB4 e WHERE e.gueltigBis = :value")
@NamedQuery(name="DTOKAoASBOEB4.gueltigbis.multiple", query="SELECT e FROM DTOKAoASBOEB4 e WHERE e.gueltigBis IN :value")
@NamedQuery(name="DTOKAoASBOEB4.kuerzel_eb4", query="SELECT e FROM DTOKAoASBOEB4 e WHERE e.Kuerzel_EB4 = :value")
@NamedQuery(name="DTOKAoASBOEB4.kuerzel_eb4.multiple", query="SELECT e FROM DTOKAoASBOEB4 e WHERE e.Kuerzel_EB4 IN :value")
@NamedQuery(name="DTOKAoASBOEB4.beschreibung_eb4", query="SELECT e FROM DTOKAoASBOEB4 e WHERE e.Beschreibung_EB4 = :value")
@NamedQuery(name="DTOKAoASBOEB4.beschreibung_eb4.multiple", query="SELECT e FROM DTOKAoASBOEB4 e WHERE e.Beschreibung_EB4 IN :value")
@NamedQuery(name="DTOKAoASBOEB4.zusatzmerkmal", query="SELECT e FROM DTOKAoASBOEB4 e WHERE e.Zusatzmerkmal = :value")
@NamedQuery(name="DTOKAoASBOEB4.zusatzmerkmal.multiple", query="SELECT e FROM DTOKAoASBOEB4 e WHERE e.Zusatzmerkmal IN :value")
@NamedQuery(name="DTOKAoASBOEB4.zusatzmerkmal_id", query="SELECT e FROM DTOKAoASBOEB4 e WHERE e.Zusatzmerkmal_ID = :value")
@NamedQuery(name="DTOKAoASBOEB4.zusatzmerkmal_id.multiple", query="SELECT e FROM DTOKAoASBOEB4 e WHERE e.Zusatzmerkmal_ID IN :value")
@NamedQuery(name="DTOKAoASBOEB4.primaryKeyQuery", query="SELECT e FROM DTOKAoASBOEB4 e WHERE e.ID = ?1")
@NamedQuery(name="DTOKAoASBOEB4.all.migration", query="SELECT e FROM DTOKAoASBOEB4 e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","gueltigVon","gueltigBis","Kuerzel_EB4","Beschreibung_EB4","Zusatzmerkmal","Zusatzmerkmal_ID"})
public class DTOKAoASBOEB4 {

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

	/** Schildintern Tabelle: Kürzel der übergeorneten SBO */
	@Column(name = "Kuerzel_EB4")
	@JsonProperty
	public String Kuerzel_EB4;

	/** Schildintern Tabelle: Bezeichnung des übergeordneten Zusatzmerkmal */
	@Column(name = "Beschreibung_EB4")
	@JsonProperty
	public String Beschreibung_EB4;

	/** Schildintern Tabelle: ID des übergeordneten Zusatzmerkmal */
	@Column(name = "Zusatzmerkmal")
	@JsonProperty
	public String Zusatzmerkmal;

	/** Schildintern Tabelle: DEPRECATED: Übergeordnetes Merkmal zum Zusatzmerkmal */
	@Column(name = "Zusatzmerkmal_ID")
	@JsonProperty
	public Long Zusatzmerkmal_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOKAoASBOEB4 ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOKAoASBOEB4() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOKAoASBOEB4 ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Kuerzel_EB4   der Wert für das Attribut Kuerzel_EB4
	 */
	public DTOKAoASBOEB4(final Long ID, final String Kuerzel_EB4) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Kuerzel_EB4 == null) { 
			throw new NullPointerException("Kuerzel_EB4 must not be null");
		}
		this.Kuerzel_EB4 = Kuerzel_EB4;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOKAoASBOEB4 other = (DTOKAoASBOEB4) obj;
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
		return "DTOKAoASBOEB4(ID=" + this.ID + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ", Kuerzel_EB4=" + this.Kuerzel_EB4 + ", Beschreibung_EB4=" + this.Beschreibung_EB4 + ", Zusatzmerkmal=" + this.Zusatzmerkmal + ", Zusatzmerkmal_ID=" + this.Zusatzmerkmal_ID + ")";
	}

}