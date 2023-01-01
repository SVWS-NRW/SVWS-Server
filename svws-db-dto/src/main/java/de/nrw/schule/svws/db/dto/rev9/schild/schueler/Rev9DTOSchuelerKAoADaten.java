package de.nrw.schule.svws.db.dto.rev9.schild.schueler;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerKAoADaten.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerKAoADaten")
@NamedQuery(name="Rev9DTOSchuelerKAoADaten.all", query="SELECT e FROM Rev9DTOSchuelerKAoADaten e")
@NamedQuery(name="Rev9DTOSchuelerKAoADaten.id", query="SELECT e FROM Rev9DTOSchuelerKAoADaten e WHERE e.ID = :value")
@NamedQuery(name="Rev9DTOSchuelerKAoADaten.id.multiple", query="SELECT e FROM Rev9DTOSchuelerKAoADaten e WHERE e.ID IN :value")
@NamedQuery(name="Rev9DTOSchuelerKAoADaten.abschnitt_id", query="SELECT e FROM Rev9DTOSchuelerKAoADaten e WHERE e.Abschnitt_ID = :value")
@NamedQuery(name="Rev9DTOSchuelerKAoADaten.abschnitt_id.multiple", query="SELECT e FROM Rev9DTOSchuelerKAoADaten e WHERE e.Abschnitt_ID IN :value")
@NamedQuery(name="Rev9DTOSchuelerKAoADaten.jahrgang", query="SELECT e FROM Rev9DTOSchuelerKAoADaten e WHERE e.Jahrgang = :value")
@NamedQuery(name="Rev9DTOSchuelerKAoADaten.jahrgang.multiple", query="SELECT e FROM Rev9DTOSchuelerKAoADaten e WHERE e.Jahrgang IN :value")
@NamedQuery(name="Rev9DTOSchuelerKAoADaten.kategorieid", query="SELECT e FROM Rev9DTOSchuelerKAoADaten e WHERE e.KategorieID = :value")
@NamedQuery(name="Rev9DTOSchuelerKAoADaten.kategorieid.multiple", query="SELECT e FROM Rev9DTOSchuelerKAoADaten e WHERE e.KategorieID IN :value")
@NamedQuery(name="Rev9DTOSchuelerKAoADaten.merkmalid", query="SELECT e FROM Rev9DTOSchuelerKAoADaten e WHERE e.MerkmalID = :value")
@NamedQuery(name="Rev9DTOSchuelerKAoADaten.merkmalid.multiple", query="SELECT e FROM Rev9DTOSchuelerKAoADaten e WHERE e.MerkmalID IN :value")
@NamedQuery(name="Rev9DTOSchuelerKAoADaten.zusatzmerkmalid", query="SELECT e FROM Rev9DTOSchuelerKAoADaten e WHERE e.ZusatzmerkmalID = :value")
@NamedQuery(name="Rev9DTOSchuelerKAoADaten.zusatzmerkmalid.multiple", query="SELECT e FROM Rev9DTOSchuelerKAoADaten e WHERE e.ZusatzmerkmalID IN :value")
@NamedQuery(name="Rev9DTOSchuelerKAoADaten.anschlussoptionid", query="SELECT e FROM Rev9DTOSchuelerKAoADaten e WHERE e.AnschlussoptionID = :value")
@NamedQuery(name="Rev9DTOSchuelerKAoADaten.anschlussoptionid.multiple", query="SELECT e FROM Rev9DTOSchuelerKAoADaten e WHERE e.AnschlussoptionID IN :value")
@NamedQuery(name="Rev9DTOSchuelerKAoADaten.berufsfeldid", query="SELECT e FROM Rev9DTOSchuelerKAoADaten e WHERE e.BerufsfeldID = :value")
@NamedQuery(name="Rev9DTOSchuelerKAoADaten.berufsfeldid.multiple", query="SELECT e FROM Rev9DTOSchuelerKAoADaten e WHERE e.BerufsfeldID IN :value")
@NamedQuery(name="Rev9DTOSchuelerKAoADaten.sbo_ebene4id", query="SELECT e FROM Rev9DTOSchuelerKAoADaten e WHERE e.SBO_Ebene4ID = :value")
@NamedQuery(name="Rev9DTOSchuelerKAoADaten.sbo_ebene4id.multiple", query="SELECT e FROM Rev9DTOSchuelerKAoADaten e WHERE e.SBO_Ebene4ID IN :value")
@NamedQuery(name="Rev9DTOSchuelerKAoADaten.bemerkung", query="SELECT e FROM Rev9DTOSchuelerKAoADaten e WHERE e.Bemerkung = :value")
@NamedQuery(name="Rev9DTOSchuelerKAoADaten.bemerkung.multiple", query="SELECT e FROM Rev9DTOSchuelerKAoADaten e WHERE e.Bemerkung IN :value")
@NamedQuery(name="Rev9DTOSchuelerKAoADaten.primaryKeyQuery", query="SELECT e FROM Rev9DTOSchuelerKAoADaten e WHERE e.ID = ?1")
@NamedQuery(name="Rev9DTOSchuelerKAoADaten.all.migration", query="SELECT e FROM Rev9DTOSchuelerKAoADaten e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Abschnitt_ID","Jahrgang","KategorieID","MerkmalID","ZusatzmerkmalID","AnschlussoptionID","BerufsfeldID","SBO_Ebene4ID","Bemerkung"})
public class Rev9DTOSchuelerKAoADaten {

	/** ID des KAOA-Eintrags beim Schüler */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** ID der zugehörigen Schülerlernabschnittsdaten */
	@Column(name = "Abschnitt_ID")
	@JsonProperty
	public Long Abschnitt_ID;

	/** Jahrgang des KAOA-Eintrags beim Schüler */
	@Column(name = "Jahrgang")
	@JsonProperty
	public String Jahrgang;

	/** ID der Kategorie des KAOA-Eintrags beim Schüler FK */
	@Column(name = "KategorieID")
	@JsonProperty
	public Long KategorieID;

	/** ID des Merkmal des KAOA-Eintrags beim Schüler FK */
	@Column(name = "MerkmalID")
	@JsonProperty
	public Long MerkmalID;

	/** ID des Zusatzmerkmal des KAOA-Eintrags beim Schüler FK */
	@Column(name = "ZusatzmerkmalID")
	@JsonProperty
	public Long ZusatzmerkmalID;

	/** ID der Anschlussoption des KAOA-Eintrags beim Schüler FK */
	@Column(name = "AnschlussoptionID")
	@JsonProperty
	public Long AnschlussoptionID;

	/** ID des Berufsfeld des KAOA-Eintrags beim Schüler FK */
	@Column(name = "BerufsfeldID")
	@JsonProperty
	public Long BerufsfeldID;

	/** ID der Ebene4 des KAOA-Eintrags beim Schüler FK */
	@Column(name = "SBO_Ebene4ID")
	@JsonProperty
	public Long SBO_Ebene4ID;

	/** Bemerkung des KAOA-Eintrags beim Schüler */
	@Column(name = "Bemerkung")
	@JsonProperty
	public String Bemerkung;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev9DTOSchuelerKAoADaten ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev9DTOSchuelerKAoADaten() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev9DTOSchuelerKAoADaten ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Abschnitt_ID   der Wert für das Attribut Abschnitt_ID
	 * @param KategorieID   der Wert für das Attribut KategorieID
	 */
	public Rev9DTOSchuelerKAoADaten(final Long ID, final Long Abschnitt_ID, final Long KategorieID) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Abschnitt_ID == null) { 
			throw new NullPointerException("Abschnitt_ID must not be null");
		}
		this.Abschnitt_ID = Abschnitt_ID;
		if (KategorieID == null) { 
			throw new NullPointerException("KategorieID must not be null");
		}
		this.KategorieID = KategorieID;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev9DTOSchuelerKAoADaten other = (Rev9DTOSchuelerKAoADaten) obj;
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
		return "Rev9DTOSchuelerKAoADaten(ID=" + this.ID + ", Abschnitt_ID=" + this.Abschnitt_ID + ", Jahrgang=" + this.Jahrgang + ", KategorieID=" + this.KategorieID + ", MerkmalID=" + this.MerkmalID + ", ZusatzmerkmalID=" + this.ZusatzmerkmalID + ", AnschlussoptionID=" + this.AnschlussoptionID + ", BerufsfeldID=" + this.BerufsfeldID + ", SBO_Ebene4ID=" + this.SBO_Ebene4ID + ", Bemerkung=" + this.Bemerkung + ")";
	}

}