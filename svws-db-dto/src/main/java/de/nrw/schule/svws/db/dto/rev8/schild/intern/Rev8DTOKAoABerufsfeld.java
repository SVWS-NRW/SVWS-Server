package de.nrw.schule.svws.db.dto.rev8.schild.intern;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Schildintern_KAoA_Berufsfeld.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Schildintern_KAoA_Berufsfeld")
@NamedQuery(name="Rev8DTOKAoABerufsfeld.all", query="SELECT e FROM Rev8DTOKAoABerufsfeld e")
@NamedQuery(name="Rev8DTOKAoABerufsfeld.id", query="SELECT e FROM Rev8DTOKAoABerufsfeld e WHERE e.ID = :value")
@NamedQuery(name="Rev8DTOKAoABerufsfeld.id.multiple", query="SELECT e FROM Rev8DTOKAoABerufsfeld e WHERE e.ID IN :value")
@NamedQuery(name="Rev8DTOKAoABerufsfeld.gueltigvon", query="SELECT e FROM Rev8DTOKAoABerufsfeld e WHERE e.gueltigVon = :value")
@NamedQuery(name="Rev8DTOKAoABerufsfeld.gueltigvon.multiple", query="SELECT e FROM Rev8DTOKAoABerufsfeld e WHERE e.gueltigVon IN :value")
@NamedQuery(name="Rev8DTOKAoABerufsfeld.gueltigbis", query="SELECT e FROM Rev8DTOKAoABerufsfeld e WHERE e.gueltigBis = :value")
@NamedQuery(name="Rev8DTOKAoABerufsfeld.gueltigbis.multiple", query="SELECT e FROM Rev8DTOKAoABerufsfeld e WHERE e.gueltigBis IN :value")
@NamedQuery(name="Rev8DTOKAoABerufsfeld.bf_kuerzel", query="SELECT e FROM Rev8DTOKAoABerufsfeld e WHERE e.BF_Kuerzel = :value")
@NamedQuery(name="Rev8DTOKAoABerufsfeld.bf_kuerzel.multiple", query="SELECT e FROM Rev8DTOKAoABerufsfeld e WHERE e.BF_Kuerzel IN :value")
@NamedQuery(name="Rev8DTOKAoABerufsfeld.bf_beschreibung", query="SELECT e FROM Rev8DTOKAoABerufsfeld e WHERE e.BF_Beschreibung = :value")
@NamedQuery(name="Rev8DTOKAoABerufsfeld.bf_beschreibung.multiple", query="SELECT e FROM Rev8DTOKAoABerufsfeld e WHERE e.BF_Beschreibung IN :value")
@NamedQuery(name="Rev8DTOKAoABerufsfeld.primaryKeyQuery", query="SELECT e FROM Rev8DTOKAoABerufsfeld e WHERE e.ID = ?1")
@NamedQuery(name="Rev8DTOKAoABerufsfeld.all.migration", query="SELECT e FROM Rev8DTOKAoABerufsfeld e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","gueltigVon","gueltigBis","BF_Kuerzel","BF_Beschreibung"})
public class Rev8DTOKAoABerufsfeld {

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

	/** Schildintern Tabelle: Kürzel des Berufsfeldes */
	@Column(name = "BF_Kuerzel")
	@JsonProperty
	public String BF_Kuerzel;

	/** Schildintern Tabelle: Beschreibung (Klartext) des Berufsfeldes */
	@Column(name = "BF_Beschreibung")
	@JsonProperty
	public String BF_Beschreibung;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOKAoABerufsfeld ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev8DTOKAoABerufsfeld() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOKAoABerufsfeld ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param BF_Kuerzel   der Wert für das Attribut BF_Kuerzel
	 */
	public Rev8DTOKAoABerufsfeld(final Long ID, final String BF_Kuerzel) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (BF_Kuerzel == null) { 
			throw new NullPointerException("BF_Kuerzel must not be null");
		}
		this.BF_Kuerzel = BF_Kuerzel;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev8DTOKAoABerufsfeld other = (Rev8DTOKAoABerufsfeld) obj;
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
		return "Rev8DTOKAoABerufsfeld(ID=" + this.ID + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ", BF_Kuerzel=" + this.BF_Kuerzel + ", BF_Beschreibung=" + this.BF_Beschreibung + ")";
	}

}