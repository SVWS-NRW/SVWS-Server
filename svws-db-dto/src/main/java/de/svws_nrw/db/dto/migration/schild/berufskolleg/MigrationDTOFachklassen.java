package de.svws_nrw.db.dto.migration.schild.berufskolleg;

import de.svws_nrw.db.DBEntityManager;
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
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverterSerializer;
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle EigeneSchule_Fachklassen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "EigeneSchule_Fachklassen")
@NamedQuery(name = "MigrationDTOFachklassen.all", query = "SELECT e FROM MigrationDTOFachklassen e")
@NamedQuery(name = "MigrationDTOFachklassen.id", query = "SELECT e FROM MigrationDTOFachklassen e WHERE e.ID = :value")
@NamedQuery(name = "MigrationDTOFachklassen.id.multiple", query = "SELECT e FROM MigrationDTOFachklassen e WHERE e.ID IN :value")
@NamedQuery(name = "MigrationDTOFachklassen.bkindex", query = "SELECT e FROM MigrationDTOFachklassen e WHERE e.BKIndex = :value")
@NamedQuery(name = "MigrationDTOFachklassen.bkindex.multiple", query = "SELECT e FROM MigrationDTOFachklassen e WHERE e.BKIndex IN :value")
@NamedQuery(name = "MigrationDTOFachklassen.fks", query = "SELECT e FROM MigrationDTOFachklassen e WHERE e.FKS = :value")
@NamedQuery(name = "MigrationDTOFachklassen.fks.multiple", query = "SELECT e FROM MigrationDTOFachklassen e WHERE e.FKS IN :value")
@NamedQuery(name = "MigrationDTOFachklassen.ap", query = "SELECT e FROM MigrationDTOFachklassen e WHERE e.AP = :value")
@NamedQuery(name = "MigrationDTOFachklassen.ap.multiple", query = "SELECT e FROM MigrationDTOFachklassen e WHERE e.AP IN :value")
@NamedQuery(name = "MigrationDTOFachklassen.bezeichnung", query = "SELECT e FROM MigrationDTOFachklassen e WHERE e.Bezeichnung = :value")
@NamedQuery(name = "MigrationDTOFachklassen.bezeichnung.multiple", query = "SELECT e FROM MigrationDTOFachklassen e WHERE e.Bezeichnung IN :value")
@NamedQuery(name = "MigrationDTOFachklassen.sortierung", query = "SELECT e FROM MigrationDTOFachklassen e WHERE e.Sortierung = :value")
@NamedQuery(name = "MigrationDTOFachklassen.sortierung.multiple", query = "SELECT e FROM MigrationDTOFachklassen e WHERE e.Sortierung IN :value")
@NamedQuery(name = "MigrationDTOFachklassen.sichtbar", query = "SELECT e FROM MigrationDTOFachklassen e WHERE e.Sichtbar = :value")
@NamedQuery(name = "MigrationDTOFachklassen.sichtbar.multiple", query = "SELECT e FROM MigrationDTOFachklassen e WHERE e.Sichtbar IN :value")
@NamedQuery(name = "MigrationDTOFachklassen.aenderbar", query = "SELECT e FROM MigrationDTOFachklassen e WHERE e.Aenderbar = :value")
@NamedQuery(name = "MigrationDTOFachklassen.aenderbar.multiple", query = "SELECT e FROM MigrationDTOFachklassen e WHERE e.Aenderbar IN :value")
@NamedQuery(name = "MigrationDTOFachklassen.kennung", query = "SELECT e FROM MigrationDTOFachklassen e WHERE e.Kennung = :value")
@NamedQuery(name = "MigrationDTOFachklassen.kennung.multiple", query = "SELECT e FROM MigrationDTOFachklassen e WHERE e.Kennung IN :value")
@NamedQuery(name = "MigrationDTOFachklassen.fks_ap_sim", query = "SELECT e FROM MigrationDTOFachklassen e WHERE e.FKS_AP_SIM = :value")
@NamedQuery(name = "MigrationDTOFachklassen.fks_ap_sim.multiple", query = "SELECT e FROM MigrationDTOFachklassen e WHERE e.FKS_AP_SIM IN :value")
@NamedQuery(name = "MigrationDTOFachklassen.bkindextyp", query = "SELECT e FROM MigrationDTOFachklassen e WHERE e.BKIndexTyp = :value")
@NamedQuery(name = "MigrationDTOFachklassen.bkindextyp.multiple", query = "SELECT e FROM MigrationDTOFachklassen e WHERE e.BKIndexTyp IN :value")
@NamedQuery(name = "MigrationDTOFachklassen.beschreibung_w", query = "SELECT e FROM MigrationDTOFachklassen e WHERE e.Beschreibung_W = :value")
@NamedQuery(name = "MigrationDTOFachklassen.beschreibung_w.multiple", query = "SELECT e FROM MigrationDTOFachklassen e WHERE e.Beschreibung_W IN :value")
@NamedQuery(name = "MigrationDTOFachklassen.status", query = "SELECT e FROM MigrationDTOFachklassen e WHERE e.Status = :value")
@NamedQuery(name = "MigrationDTOFachklassen.status.multiple", query = "SELECT e FROM MigrationDTOFachklassen e WHERE e.Status IN :value")
@NamedQuery(name = "MigrationDTOFachklassen.schulnreigner", query = "SELECT e FROM MigrationDTOFachklassen e WHERE e.SchulnrEigner = :value")
@NamedQuery(name = "MigrationDTOFachklassen.schulnreigner.multiple", query = "SELECT e FROM MigrationDTOFachklassen e WHERE e.SchulnrEigner IN :value")
@NamedQuery(name = "MigrationDTOFachklassen.lernfelder", query = "SELECT e FROM MigrationDTOFachklassen e WHERE e.Lernfelder = :value")
@NamedQuery(name = "MigrationDTOFachklassen.lernfelder.multiple", query = "SELECT e FROM MigrationDTOFachklassen e WHERE e.Lernfelder IN :value")
@NamedQuery(name = "MigrationDTOFachklassen.dqr_niveau", query = "SELECT e FROM MigrationDTOFachklassen e WHERE e.DQR_Niveau = :value")
@NamedQuery(name = "MigrationDTOFachklassen.dqr_niveau.multiple", query = "SELECT e FROM MigrationDTOFachklassen e WHERE e.DQR_Niveau IN :value")
@NamedQuery(name = "MigrationDTOFachklassen.ebene1klartext", query = "SELECT e FROM MigrationDTOFachklassen e WHERE e.Ebene1Klartext = :value")
@NamedQuery(name = "MigrationDTOFachklassen.ebene1klartext.multiple", query = "SELECT e FROM MigrationDTOFachklassen e WHERE e.Ebene1Klartext IN :value")
@NamedQuery(name = "MigrationDTOFachklassen.ebene2klartext", query = "SELECT e FROM MigrationDTOFachklassen e WHERE e.Ebene2Klartext = :value")
@NamedQuery(name = "MigrationDTOFachklassen.ebene2klartext.multiple", query = "SELECT e FROM MigrationDTOFachklassen e WHERE e.Ebene2Klartext IN :value")
@NamedQuery(name = "MigrationDTOFachklassen.ebene3klartext", query = "SELECT e FROM MigrationDTOFachklassen e WHERE e.Ebene3Klartext = :value")
@NamedQuery(name = "MigrationDTOFachklassen.ebene3klartext.multiple", query = "SELECT e FROM MigrationDTOFachklassen e WHERE e.Ebene3Klartext IN :value")
@NamedQuery(name = "MigrationDTOFachklassen.primaryKeyQuery", query = "SELECT e FROM MigrationDTOFachklassen e WHERE e.ID = ?1")
@NamedQuery(name = "MigrationDTOFachklassen.all.migration", query = "SELECT e FROM MigrationDTOFachklassen e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "BKIndex", "FKS", "AP", "Bezeichnung", "Sortierung", "Sichtbar", "Aenderbar", "Kennung", "FKS_AP_SIM", "BKIndexTyp", "Beschreibung_W", "Status", "SchulnrEigner", "Lernfelder", "DQR_Niveau", "Ebene1Klartext", "Ebene2Klartext", "Ebene3Klartext"})
public final class MigrationDTOFachklassen {

	/** ID der Fachklasse im schulinternen Katalog der Fachklassen nur BK und SBK */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** BKIndex aus der Statkue bildet mit FKS und AP eine eindeutige Kombination IT.NRW */
	@Column(name = "BKIndex")
	@JsonProperty
	public Integer BKIndex;

	/** Fachklassenschlüssel bildet mit BKIndex und AP eine eindeutige Kombination IT.NRW */
	@Column(name = "FKS")
	@JsonProperty
	public String FKS;

	/** Laufende Nummer zum FKS bildet mit FKS und BKIndex eine eindeutige Kombination IT.NRW */
	@Column(name = "AP")
	@JsonProperty
	public String AP;

	/** Bezeichnung der Fachklasse Text */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Sortiernummer der Fachklasse */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** steuert die Sichtbarkeit der Fachklasse */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** Gibt an ob die Fachklasse änderbar ist */
	@Column(name = "Aenderbar")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Aenderbar;

	/** Kennung der Fachklasse */
	@Column(name = "Kennung")
	@JsonProperty
	public String Kennung;

	/** Kombination aus FKS und AP mit Minuszeichen */
	@Column(name = "FKS_AP_SIM")
	@JsonProperty
	public String FKS_AP_SIM;

	/** Typ des BKIndex IT.NW */
	@Column(name = "BKIndexTyp")
	@JsonProperty
	public String BKIndexTyp;

	/** Weiblicher Beschreibungstext für die Fachklassenbezeichnung */
	@Column(name = "Beschreibung_W")
	@JsonProperty
	public String Beschreibung_W;

	/** Status der Fachklasse kann auslaufend sein */
	@Column(name = "Status")
	@JsonProperty
	public String Status;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/** Text für die Lernfelder die zur Fachklasse auf dem Zeugnis ausgewiesen werden */
	@Column(name = "Lernfelder")
	@JsonProperty
	public String Lernfelder;

	/** DQR-Niveau der Fachklasse Deutscher Qualitätsrahmen */
	@Column(name = "DQR_Niveau")
	@JsonProperty
	public Integer DQR_Niveau;

	/** Berufsebene 1 */
	@Column(name = "Ebene1Klartext")
	@JsonProperty
	public String Ebene1Klartext;

	/** Berufsebene 2 */
	@Column(name = "Ebene2Klartext")
	@JsonProperty
	public String Ebene2Klartext;

	/** Berufsebene 3 */
	@Column(name = "Ebene3Klartext")
	@JsonProperty
	public String Ebene3Klartext;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOFachklassen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOFachklassen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOFachklassen ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 */
	public MigrationDTOFachklassen(final Long ID) {
		if (ID == null) {
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOFachklassen other = (MigrationDTOFachklassen) obj;
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
		return "MigrationDTOFachklassen(ID=" + this.ID + ", BKIndex=" + this.BKIndex + ", FKS=" + this.FKS + ", AP=" + this.AP + ", Bezeichnung=" + this.Bezeichnung + ", Sortierung=" + this.Sortierung + ", Sichtbar=" + this.Sichtbar + ", Aenderbar=" + this.Aenderbar + ", Kennung=" + this.Kennung + ", FKS_AP_SIM=" + this.FKS_AP_SIM + ", BKIndexTyp=" + this.BKIndexTyp + ", Beschreibung_W=" + this.Beschreibung_W + ", Status=" + this.Status + ", SchulnrEigner=" + this.SchulnrEigner + ", Lernfelder=" + this.Lernfelder + ", DQR_Niveau=" + this.DQR_Niveau + ", Ebene1Klartext=" + this.Ebene1Klartext + ", Ebene2Klartext=" + this.Ebene2Klartext + ", Ebene3Klartext=" + this.Ebene3Klartext + ")";
	}

}
