package de.svws_nrw.db.dto.migration.schild.schueler;

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
@NamedQuery(name = "MigrationDTOSchuelerKAoADaten.all", query = "SELECT e FROM MigrationDTOSchuelerKAoADaten e")
@NamedQuery(name = "MigrationDTOSchuelerKAoADaten.id", query = "SELECT e FROM MigrationDTOSchuelerKAoADaten e WHERE e.ID = :value")
@NamedQuery(name = "MigrationDTOSchuelerKAoADaten.id.multiple", query = "SELECT e FROM MigrationDTOSchuelerKAoADaten e WHERE e.ID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerKAoADaten.schueler_id", query = "SELECT e FROM MigrationDTOSchuelerKAoADaten e WHERE e.Schueler_ID = :value")
@NamedQuery(name = "MigrationDTOSchuelerKAoADaten.schueler_id.multiple", query = "SELECT e FROM MigrationDTOSchuelerKAoADaten e WHERE e.Schueler_ID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerKAoADaten.abschnitt_id", query = "SELECT e FROM MigrationDTOSchuelerKAoADaten e WHERE e.Abschnitt_ID = :value")
@NamedQuery(name = "MigrationDTOSchuelerKAoADaten.abschnitt_id.multiple", query = "SELECT e FROM MigrationDTOSchuelerKAoADaten e WHERE e.Abschnitt_ID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerKAoADaten.jahrgang", query = "SELECT e FROM MigrationDTOSchuelerKAoADaten e WHERE e.Jahrgang = :value")
@NamedQuery(name = "MigrationDTOSchuelerKAoADaten.jahrgang.multiple", query = "SELECT e FROM MigrationDTOSchuelerKAoADaten e WHERE e.Jahrgang IN :value")
@NamedQuery(name = "MigrationDTOSchuelerKAoADaten.kategorieid", query = "SELECT e FROM MigrationDTOSchuelerKAoADaten e WHERE e.KategorieID = :value")
@NamedQuery(name = "MigrationDTOSchuelerKAoADaten.kategorieid.multiple", query = "SELECT e FROM MigrationDTOSchuelerKAoADaten e WHERE e.KategorieID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerKAoADaten.merkmalid", query = "SELECT e FROM MigrationDTOSchuelerKAoADaten e WHERE e.MerkmalID = :value")
@NamedQuery(name = "MigrationDTOSchuelerKAoADaten.merkmalid.multiple", query = "SELECT e FROM MigrationDTOSchuelerKAoADaten e WHERE e.MerkmalID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerKAoADaten.zusatzmerkmalid", query = "SELECT e FROM MigrationDTOSchuelerKAoADaten e WHERE e.ZusatzmerkmalID = :value")
@NamedQuery(name = "MigrationDTOSchuelerKAoADaten.zusatzmerkmalid.multiple", query = "SELECT e FROM MigrationDTOSchuelerKAoADaten e WHERE e.ZusatzmerkmalID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerKAoADaten.anschlussoptionid", query = "SELECT e FROM MigrationDTOSchuelerKAoADaten e WHERE e.AnschlussoptionID = :value")
@NamedQuery(name = "MigrationDTOSchuelerKAoADaten.anschlussoptionid.multiple", query = "SELECT e FROM MigrationDTOSchuelerKAoADaten e WHERE e.AnschlussoptionID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerKAoADaten.berufsfeldid", query = "SELECT e FROM MigrationDTOSchuelerKAoADaten e WHERE e.BerufsfeldID = :value")
@NamedQuery(name = "MigrationDTOSchuelerKAoADaten.berufsfeldid.multiple", query = "SELECT e FROM MigrationDTOSchuelerKAoADaten e WHERE e.BerufsfeldID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerKAoADaten.sbo_ebene4id", query = "SELECT e FROM MigrationDTOSchuelerKAoADaten e WHERE e.SBO_Ebene4ID = :value")
@NamedQuery(name = "MigrationDTOSchuelerKAoADaten.sbo_ebene4id.multiple", query = "SELECT e FROM MigrationDTOSchuelerKAoADaten e WHERE e.SBO_Ebene4ID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerKAoADaten.bemerkung", query = "SELECT e FROM MigrationDTOSchuelerKAoADaten e WHERE e.Bemerkung = :value")
@NamedQuery(name = "MigrationDTOSchuelerKAoADaten.bemerkung.multiple", query = "SELECT e FROM MigrationDTOSchuelerKAoADaten e WHERE e.Bemerkung IN :value")
@NamedQuery(name = "MigrationDTOSchuelerKAoADaten.schulnreigner", query = "SELECT e FROM MigrationDTOSchuelerKAoADaten e WHERE e.SchulnrEigner = :value")
@NamedQuery(name = "MigrationDTOSchuelerKAoADaten.schulnreigner.multiple", query = "SELECT e FROM MigrationDTOSchuelerKAoADaten e WHERE e.SchulnrEigner IN :value")
@NamedQuery(name = "MigrationDTOSchuelerKAoADaten.jahr", query = "SELECT e FROM MigrationDTOSchuelerKAoADaten e WHERE e.Jahr = :value")
@NamedQuery(name = "MigrationDTOSchuelerKAoADaten.jahr.multiple", query = "SELECT e FROM MigrationDTOSchuelerKAoADaten e WHERE e.Jahr IN :value")
@NamedQuery(name = "MigrationDTOSchuelerKAoADaten.abschnitt", query = "SELECT e FROM MigrationDTOSchuelerKAoADaten e WHERE e.Abschnitt = :value")
@NamedQuery(name = "MigrationDTOSchuelerKAoADaten.abschnitt.multiple", query = "SELECT e FROM MigrationDTOSchuelerKAoADaten e WHERE e.Abschnitt IN :value")
@NamedQuery(name = "MigrationDTOSchuelerKAoADaten.primaryKeyQuery", query = "SELECT e FROM MigrationDTOSchuelerKAoADaten e WHERE e.ID = ?1")
@NamedQuery(name = "MigrationDTOSchuelerKAoADaten.all.migration", query = "SELECT e FROM MigrationDTOSchuelerKAoADaten e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Schueler_ID", "Abschnitt_ID", "Jahrgang", "KategorieID", "MerkmalID", "ZusatzmerkmalID", "AnschlussoptionID", "BerufsfeldID", "SBO_Ebene4ID", "Bemerkung", "SchulnrEigner", "Jahr", "Abschnitt"})
public final class MigrationDTOSchuelerKAoADaten {

	/** ID des KAOA-Eintrags beim Schüler */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** DEPRECATED: Schüler-ID des KAOA-Eintrags beim Schüler, in Abschnitt_ID enthalten */
	@Column(name = "Schueler_ID")
	@JsonProperty
	public Long Schueler_ID;

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

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/** Schuljahr des KAOA-Eintrags beim Schüler */
	@Column(name = "Jahr")
	@JsonProperty
	public Integer Jahr;

	/** Abschnitt des KAOA-Eintrags beim Schüler */
	@Column(name = "Abschnitt")
	@JsonProperty
	public Integer Abschnitt;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuelerKAoADaten ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOSchuelerKAoADaten() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuelerKAoADaten ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 * @param Abschnitt_ID   der Wert für das Attribut Abschnitt_ID
	 * @param KategorieID   der Wert für das Attribut KategorieID
	 * @param SchulnrEigner   der Wert für das Attribut SchulnrEigner
	 * @param Jahr   der Wert für das Attribut Jahr
	 * @param Abschnitt   der Wert für das Attribut Abschnitt
	 */
	public MigrationDTOSchuelerKAoADaten(final Long ID, final Long Schueler_ID, final Long Abschnitt_ID, final Long KategorieID, final Integer SchulnrEigner, final Integer Jahr, final Integer Abschnitt) {
		if (ID == null) {
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Schueler_ID == null) {
			throw new NullPointerException("Schueler_ID must not be null");
		}
		this.Schueler_ID = Schueler_ID;
		if (Abschnitt_ID == null) {
			throw new NullPointerException("Abschnitt_ID must not be null");
		}
		this.Abschnitt_ID = Abschnitt_ID;
		if (KategorieID == null) {
			throw new NullPointerException("KategorieID must not be null");
		}
		this.KategorieID = KategorieID;
		if (SchulnrEigner == null) {
			throw new NullPointerException("SchulnrEigner must not be null");
		}
		this.SchulnrEigner = SchulnrEigner;
		if (Jahr == null) {
			throw new NullPointerException("Jahr must not be null");
		}
		this.Jahr = Jahr;
		if (Abschnitt == null) {
			throw new NullPointerException("Abschnitt must not be null");
		}
		this.Abschnitt = Abschnitt;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOSchuelerKAoADaten other = (MigrationDTOSchuelerKAoADaten) obj;
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
		return "MigrationDTOSchuelerKAoADaten(ID=" + this.ID + ", Schueler_ID=" + this.Schueler_ID + ", Abschnitt_ID=" + this.Abschnitt_ID + ", Jahrgang=" + this.Jahrgang + ", KategorieID=" + this.KategorieID + ", MerkmalID=" + this.MerkmalID + ", ZusatzmerkmalID=" + this.ZusatzmerkmalID + ", AnschlussoptionID=" + this.AnschlussoptionID + ", BerufsfeldID=" + this.BerufsfeldID + ", SBO_Ebene4ID=" + this.SBO_Ebene4ID + ", Bemerkung=" + this.Bemerkung + ", SchulnrEigner=" + this.SchulnrEigner + ", Jahr=" + this.Jahr + ", Abschnitt=" + this.Abschnitt + ")";
	}

}
