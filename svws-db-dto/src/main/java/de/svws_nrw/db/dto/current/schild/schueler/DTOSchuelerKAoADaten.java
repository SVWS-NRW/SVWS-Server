package de.svws_nrw.db.dto.current.schild.schueler;

import de.svws_nrw.db.DBEntityManager;

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
@NamedQuery(name = "DTOSchuelerKAoADaten.all", query = "SELECT e FROM DTOSchuelerKAoADaten e")
@NamedQuery(name = "DTOSchuelerKAoADaten.id", query = "SELECT e FROM DTOSchuelerKAoADaten e WHERE e.ID = :value")
@NamedQuery(name = "DTOSchuelerKAoADaten.id.multiple", query = "SELECT e FROM DTOSchuelerKAoADaten e WHERE e.ID IN :value")
@NamedQuery(name = "DTOSchuelerKAoADaten.abschnitt_id", query = "SELECT e FROM DTOSchuelerKAoADaten e WHERE e.Abschnitt_ID = :value")
@NamedQuery(name = "DTOSchuelerKAoADaten.abschnitt_id.multiple", query = "SELECT e FROM DTOSchuelerKAoADaten e WHERE e.Abschnitt_ID IN :value")
@NamedQuery(name = "DTOSchuelerKAoADaten.jahrgang", query = "SELECT e FROM DTOSchuelerKAoADaten e WHERE e.Jahrgang = :value")
@NamedQuery(name = "DTOSchuelerKAoADaten.jahrgang.multiple", query = "SELECT e FROM DTOSchuelerKAoADaten e WHERE e.Jahrgang IN :value")
@NamedQuery(name = "DTOSchuelerKAoADaten.kategorieid", query = "SELECT e FROM DTOSchuelerKAoADaten e WHERE e.KategorieID = :value")
@NamedQuery(name = "DTOSchuelerKAoADaten.kategorieid.multiple", query = "SELECT e FROM DTOSchuelerKAoADaten e WHERE e.KategorieID IN :value")
@NamedQuery(name = "DTOSchuelerKAoADaten.merkmalid", query = "SELECT e FROM DTOSchuelerKAoADaten e WHERE e.MerkmalID = :value")
@NamedQuery(name = "DTOSchuelerKAoADaten.merkmalid.multiple", query = "SELECT e FROM DTOSchuelerKAoADaten e WHERE e.MerkmalID IN :value")
@NamedQuery(name = "DTOSchuelerKAoADaten.zusatzmerkmalid", query = "SELECT e FROM DTOSchuelerKAoADaten e WHERE e.ZusatzmerkmalID = :value")
@NamedQuery(name = "DTOSchuelerKAoADaten.zusatzmerkmalid.multiple", query = "SELECT e FROM DTOSchuelerKAoADaten e WHERE e.ZusatzmerkmalID IN :value")
@NamedQuery(name = "DTOSchuelerKAoADaten.anschlussoptionid", query = "SELECT e FROM DTOSchuelerKAoADaten e WHERE e.AnschlussoptionID = :value")
@NamedQuery(name = "DTOSchuelerKAoADaten.anschlussoptionid.multiple", query = "SELECT e FROM DTOSchuelerKAoADaten e WHERE e.AnschlussoptionID IN :value")
@NamedQuery(name = "DTOSchuelerKAoADaten.berufsfeldid", query = "SELECT e FROM DTOSchuelerKAoADaten e WHERE e.BerufsfeldID = :value")
@NamedQuery(name = "DTOSchuelerKAoADaten.berufsfeldid.multiple", query = "SELECT e FROM DTOSchuelerKAoADaten e WHERE e.BerufsfeldID IN :value")
@NamedQuery(name = "DTOSchuelerKAoADaten.sbo_ebene4id", query = "SELECT e FROM DTOSchuelerKAoADaten e WHERE e.SBO_Ebene4ID = :value")
@NamedQuery(name = "DTOSchuelerKAoADaten.sbo_ebene4id.multiple", query = "SELECT e FROM DTOSchuelerKAoADaten e WHERE e.SBO_Ebene4ID IN :value")
@NamedQuery(name = "DTOSchuelerKAoADaten.bemerkung", query = "SELECT e FROM DTOSchuelerKAoADaten e WHERE e.Bemerkung = :value")
@NamedQuery(name = "DTOSchuelerKAoADaten.bemerkung.multiple", query = "SELECT e FROM DTOSchuelerKAoADaten e WHERE e.Bemerkung IN :value")
@NamedQuery(name = "DTOSchuelerKAoADaten.primaryKeyQuery", query = "SELECT e FROM DTOSchuelerKAoADaten e WHERE e.ID = ?1")
@NamedQuery(name = "DTOSchuelerKAoADaten.primaryKeyQuery.multiple", query = "SELECT e FROM DTOSchuelerKAoADaten e WHERE e.ID IN :value")
@NamedQuery(name = "DTOSchuelerKAoADaten.all.migration", query = "SELECT e FROM DTOSchuelerKAoADaten e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Abschnitt_ID", "Jahrgang", "KategorieID", "MerkmalID", "ZusatzmerkmalID", "AnschlussoptionID", "BerufsfeldID", "SBO_Ebene4ID", "Bemerkung"})
public final class DTOSchuelerKAoADaten {

	/** ID des KAOA-Eintrags beim Schüler */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** ID der zugehörigen Schülerlernabschnittsdaten */
	@Column(name = "Abschnitt_ID")
	@JsonProperty
	public long Abschnitt_ID;

	/** Jahrgang des KAOA-Eintrags beim Schüler */
	@Column(name = "Jahrgang")
	@JsonProperty
	public String Jahrgang;

	/** ID der Kategorie des KAOA-Eintrags beim Schüler FK */
	@Column(name = "KategorieID")
	@JsonProperty
	public long KategorieID;

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
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerKAoADaten ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSchuelerKAoADaten() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerKAoADaten ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Abschnitt_ID   der Wert für das Attribut Abschnitt_ID
	 * @param KategorieID   der Wert für das Attribut KategorieID
	 */
	public DTOSchuelerKAoADaten(final long ID, final long Abschnitt_ID, final long KategorieID) {
		this.ID = ID;
		this.Abschnitt_ID = Abschnitt_ID;
		this.KategorieID = KategorieID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOSchuelerKAoADaten other = (DTOSchuelerKAoADaten) obj;
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
		return "DTOSchuelerKAoADaten(ID=" + this.ID + ", Abschnitt_ID=" + this.Abschnitt_ID + ", Jahrgang=" + this.Jahrgang + ", KategorieID=" + this.KategorieID + ", MerkmalID=" + this.MerkmalID + ", ZusatzmerkmalID=" + this.ZusatzmerkmalID + ", AnschlussoptionID=" + this.AnschlussoptionID + ", BerufsfeldID=" + this.BerufsfeldID + ", SBO_Ebene4ID=" + this.SBO_Ebene4ID + ", Bemerkung=" + this.Bemerkung + ")";
	}

}
