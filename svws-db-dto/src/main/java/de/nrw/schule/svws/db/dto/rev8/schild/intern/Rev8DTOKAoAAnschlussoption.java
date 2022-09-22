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
 * Diese Klasse dient als DTO für die Datenbanktabelle Schildintern_KAoA_Anschlussoption.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Schildintern_KAoA_Anschlussoption")
@NamedQuery(name="Rev8DTOKAoAAnschlussoption.all", query="SELECT e FROM Rev8DTOKAoAAnschlussoption e")
@NamedQuery(name="Rev8DTOKAoAAnschlussoption.id", query="SELECT e FROM Rev8DTOKAoAAnschlussoption e WHERE e.ID = :value")
@NamedQuery(name="Rev8DTOKAoAAnschlussoption.id.multiple", query="SELECT e FROM Rev8DTOKAoAAnschlussoption e WHERE e.ID IN :value")
@NamedQuery(name="Rev8DTOKAoAAnschlussoption.gueltigvon", query="SELECT e FROM Rev8DTOKAoAAnschlussoption e WHERE e.gueltigVon = :value")
@NamedQuery(name="Rev8DTOKAoAAnschlussoption.gueltigvon.multiple", query="SELECT e FROM Rev8DTOKAoAAnschlussoption e WHERE e.gueltigVon IN :value")
@NamedQuery(name="Rev8DTOKAoAAnschlussoption.gueltigbis", query="SELECT e FROM Rev8DTOKAoAAnschlussoption e WHERE e.gueltigBis = :value")
@NamedQuery(name="Rev8DTOKAoAAnschlussoption.gueltigbis.multiple", query="SELECT e FROM Rev8DTOKAoAAnschlussoption e WHERE e.gueltigBis IN :value")
@NamedQuery(name="Rev8DTOKAoAAnschlussoption.ao_kuerzel", query="SELECT e FROM Rev8DTOKAoAAnschlussoption e WHERE e.AO_Kuerzel = :value")
@NamedQuery(name="Rev8DTOKAoAAnschlussoption.ao_kuerzel.multiple", query="SELECT e FROM Rev8DTOKAoAAnschlussoption e WHERE e.AO_Kuerzel IN :value")
@NamedQuery(name="Rev8DTOKAoAAnschlussoption.ao_beschreibung", query="SELECT e FROM Rev8DTOKAoAAnschlussoption e WHERE e.AO_Beschreibung = :value")
@NamedQuery(name="Rev8DTOKAoAAnschlussoption.ao_beschreibung.multiple", query="SELECT e FROM Rev8DTOKAoAAnschlussoption e WHERE e.AO_Beschreibung IN :value")
@NamedQuery(name="Rev8DTOKAoAAnschlussoption.ao_stufen", query="SELECT e FROM Rev8DTOKAoAAnschlussoption e WHERE e.AO_Stufen = :value")
@NamedQuery(name="Rev8DTOKAoAAnschlussoption.ao_stufen.multiple", query="SELECT e FROM Rev8DTOKAoAAnschlussoption e WHERE e.AO_Stufen IN :value")
@NamedQuery(name="Rev8DTOKAoAAnschlussoption.zusatzmerkmal_anzeige", query="SELECT e FROM Rev8DTOKAoAAnschlussoption e WHERE e.Zusatzmerkmal_Anzeige = :value")
@NamedQuery(name="Rev8DTOKAoAAnschlussoption.zusatzmerkmal_anzeige.multiple", query="SELECT e FROM Rev8DTOKAoAAnschlussoption e WHERE e.Zusatzmerkmal_Anzeige IN :value")
@NamedQuery(name="Rev8DTOKAoAAnschlussoption.primaryKeyQuery", query="SELECT e FROM Rev8DTOKAoAAnschlussoption e WHERE e.ID = ?1")
@NamedQuery(name="Rev8DTOKAoAAnschlussoption.all.migration", query="SELECT e FROM Rev8DTOKAoAAnschlussoption e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","gueltigVon","gueltigBis","AO_Kuerzel","AO_Beschreibung","AO_Stufen","Zusatzmerkmal_Anzeige"})
public class Rev8DTOKAoAAnschlussoption {

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

	/** Schildintern Tabelle: Kürzel der Anschlussoption */
	@Column(name = "AO_Kuerzel")
	@JsonProperty
	public String AO_Kuerzel;

	/** Schildintern Tabelle: Beschreibung (Klartext) der Anschlussoption */
	@Column(name = "AO_Beschreibung")
	@JsonProperty
	public String AO_Beschreibung;

	/** Schildintern Tabelle: Jahrgangsstufen in denen der Eintrag gemacht werden darf */
	@Column(name = "AO_Stufen")
	@JsonProperty
	public String AO_Stufen;

	/** Schildintern Tabelle: Gibt an bei welchen Anschlussvereinbarungen SBO10.7 die Optionen angezeigt werden */
	@Column(name = "Zusatzmerkmal_Anzeige")
	@JsonProperty
	public String Zusatzmerkmal_Anzeige;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOKAoAAnschlussoption ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev8DTOKAoAAnschlussoption() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOKAoAAnschlussoption ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param AO_Kuerzel   der Wert für das Attribut AO_Kuerzel
	 */
	public Rev8DTOKAoAAnschlussoption(final Long ID, final String AO_Kuerzel) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (AO_Kuerzel == null) { 
			throw new NullPointerException("AO_Kuerzel must not be null");
		}
		this.AO_Kuerzel = AO_Kuerzel;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev8DTOKAoAAnschlussoption other = (Rev8DTOKAoAAnschlussoption) obj;
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
		return "Rev8DTOKAoAAnschlussoption(ID=" + this.ID + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ", AO_Kuerzel=" + this.AO_Kuerzel + ", AO_Beschreibung=" + this.AO_Beschreibung + ", AO_Stufen=" + this.AO_Stufen + ", Zusatzmerkmal_Anzeige=" + this.Zusatzmerkmal_Anzeige + ")";
	}

}