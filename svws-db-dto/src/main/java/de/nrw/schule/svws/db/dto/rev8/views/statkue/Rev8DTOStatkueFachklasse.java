package de.nrw.schule.svws.db.dto.rev8.views.statkue;

import de.nrw.schule.svws.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbank-View Statkue_Fachklasse.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(Rev8DTOStatkueFachklassePK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Statkue_Fachklasse")
@NamedQuery(name="Rev8DTOStatkueFachklasse.all", query="SELECT e FROM Rev8DTOStatkueFachklasse e")
@NamedQuery(name="Rev8DTOStatkueFachklasse.bkindex", query="SELECT e FROM Rev8DTOStatkueFachklasse e WHERE e.BKIndex = :value")
@NamedQuery(name="Rev8DTOStatkueFachklasse.bkindex.multiple", query="SELECT e FROM Rev8DTOStatkueFachklasse e WHERE e.BKIndex IN :value")
@NamedQuery(name="Rev8DTOStatkueFachklasse.fks", query="SELECT e FROM Rev8DTOStatkueFachklasse e WHERE e.FKS = :value")
@NamedQuery(name="Rev8DTOStatkueFachklasse.fks.multiple", query="SELECT e FROM Rev8DTOStatkueFachklasse e WHERE e.FKS IN :value")
@NamedQuery(name="Rev8DTOStatkueFachklasse.ap", query="SELECT e FROM Rev8DTOStatkueFachklasse e WHERE e.AP = :value")
@NamedQuery(name="Rev8DTOStatkueFachklasse.ap.multiple", query="SELECT e FROM Rev8DTOStatkueFachklasse e WHERE e.AP IN :value")
@NamedQuery(name="Rev8DTOStatkueFachklasse.flag", query="SELECT e FROM Rev8DTOStatkueFachklasse e WHERE e.Flag = :value")
@NamedQuery(name="Rev8DTOStatkueFachklasse.flag.multiple", query="SELECT e FROM Rev8DTOStatkueFachklasse e WHERE e.Flag IN :value")
@NamedQuery(name="Rev8DTOStatkueFachklasse.bgrp", query="SELECT e FROM Rev8DTOStatkueFachklasse e WHERE e.BGrp = :value")
@NamedQuery(name="Rev8DTOStatkueFachklasse.bgrp.multiple", query="SELECT e FROM Rev8DTOStatkueFachklasse e WHERE e.BGrp IN :value")
@NamedQuery(name="Rev8DTOStatkueFachklasse.bfeld", query="SELECT e FROM Rev8DTOStatkueFachklasse e WHERE e.BFeld = :value")
@NamedQuery(name="Rev8DTOStatkueFachklasse.bfeld.multiple", query="SELECT e FROM Rev8DTOStatkueFachklasse e WHERE e.BFeld IN :value")
@NamedQuery(name="Rev8DTOStatkueFachklasse.ebene1", query="SELECT e FROM Rev8DTOStatkueFachklasse e WHERE e.Ebene1 = :value")
@NamedQuery(name="Rev8DTOStatkueFachklasse.ebene1.multiple", query="SELECT e FROM Rev8DTOStatkueFachklasse e WHERE e.Ebene1 IN :value")
@NamedQuery(name="Rev8DTOStatkueFachklasse.ebene2", query="SELECT e FROM Rev8DTOStatkueFachklasse e WHERE e.Ebene2 = :value")
@NamedQuery(name="Rev8DTOStatkueFachklasse.ebene2.multiple", query="SELECT e FROM Rev8DTOStatkueFachklasse e WHERE e.Ebene2 IN :value")
@NamedQuery(name="Rev8DTOStatkueFachklasse.ebene3", query="SELECT e FROM Rev8DTOStatkueFachklasse e WHERE e.Ebene3 = :value")
@NamedQuery(name="Rev8DTOStatkueFachklasse.ebene3.multiple", query="SELECT e FROM Rev8DTOStatkueFachklasse e WHERE e.Ebene3 IN :value")
@NamedQuery(name="Rev8DTOStatkueFachklasse.sortierung", query="SELECT e FROM Rev8DTOStatkueFachklasse e WHERE e.Sortierung = :value")
@NamedQuery(name="Rev8DTOStatkueFachklasse.sortierung.multiple", query="SELECT e FROM Rev8DTOStatkueFachklasse e WHERE e.Sortierung IN :value")
@NamedQuery(name="Rev8DTOStatkueFachklasse.beschreibung_mw", query="SELECT e FROM Rev8DTOStatkueFachklasse e WHERE e.Beschreibung_MW = :value")
@NamedQuery(name="Rev8DTOStatkueFachklasse.beschreibung_mw.multiple", query="SELECT e FROM Rev8DTOStatkueFachklasse e WHERE e.Beschreibung_MW IN :value")
@NamedQuery(name="Rev8DTOStatkueFachklasse.beschreibung", query="SELECT e FROM Rev8DTOStatkueFachklasse e WHERE e.Beschreibung = :value")
@NamedQuery(name="Rev8DTOStatkueFachklasse.beschreibung.multiple", query="SELECT e FROM Rev8DTOStatkueFachklasse e WHERE e.Beschreibung IN :value")
@NamedQuery(name="Rev8DTOStatkueFachklasse.beschreibung_w", query="SELECT e FROM Rev8DTOStatkueFachklasse e WHERE e.Beschreibung_W = :value")
@NamedQuery(name="Rev8DTOStatkueFachklasse.beschreibung_w.multiple", query="SELECT e FROM Rev8DTOStatkueFachklasse e WHERE e.Beschreibung_W IN :value")
@NamedQuery(name="Rev8DTOStatkueFachklasse.status", query="SELECT e FROM Rev8DTOStatkueFachklasse e WHERE e.Status = :value")
@NamedQuery(name="Rev8DTOStatkueFachklasse.status.multiple", query="SELECT e FROM Rev8DTOStatkueFachklasse e WHERE e.Status IN :value")
@NamedQuery(name="Rev8DTOStatkueFachklasse.flag_apobk", query="SELECT e FROM Rev8DTOStatkueFachklasse e WHERE e.Flag_APOBK = :value")
@NamedQuery(name="Rev8DTOStatkueFachklasse.flag_apobk.multiple", query="SELECT e FROM Rev8DTOStatkueFachklasse e WHERE e.Flag_APOBK IN :value")
@NamedQuery(name="Rev8DTOStatkueFachklasse.bakl", query="SELECT e FROM Rev8DTOStatkueFachklasse e WHERE e.BAKL = :value")
@NamedQuery(name="Rev8DTOStatkueFachklasse.bakl.multiple", query="SELECT e FROM Rev8DTOStatkueFachklasse e WHERE e.BAKL IN :value")
@NamedQuery(name="Rev8DTOStatkueFachklasse.bagr", query="SELECT e FROM Rev8DTOStatkueFachklasse e WHERE e.BAGR = :value")
@NamedQuery(name="Rev8DTOStatkueFachklasse.bagr.multiple", query="SELECT e FROM Rev8DTOStatkueFachklasse e WHERE e.BAGR IN :value")
@NamedQuery(name="Rev8DTOStatkueFachklasse.baklalt", query="SELECT e FROM Rev8DTOStatkueFachklasse e WHERE e.BAKLALT = :value")
@NamedQuery(name="Rev8DTOStatkueFachklasse.baklalt.multiple", query="SELECT e FROM Rev8DTOStatkueFachklasse e WHERE e.BAKLALT IN :value")
@NamedQuery(name="Rev8DTOStatkueFachklasse.bagralt", query="SELECT e FROM Rev8DTOStatkueFachklasse e WHERE e.BAGRALT = :value")
@NamedQuery(name="Rev8DTOStatkueFachklasse.bagralt.multiple", query="SELECT e FROM Rev8DTOStatkueFachklasse e WHERE e.BAGRALT IN :value")
@NamedQuery(name="Rev8DTOStatkueFachklasse.geaendert", query="SELECT e FROM Rev8DTOStatkueFachklasse e WHERE e.geaendert = :value")
@NamedQuery(name="Rev8DTOStatkueFachklasse.geaendert.multiple", query="SELECT e FROM Rev8DTOStatkueFachklasse e WHERE e.geaendert IN :value")
@NamedQuery(name="Rev8DTOStatkueFachklasse.gueltigvon", query="SELECT e FROM Rev8DTOStatkueFachklasse e WHERE e.gueltigVon = :value")
@NamedQuery(name="Rev8DTOStatkueFachklasse.gueltigvon.multiple", query="SELECT e FROM Rev8DTOStatkueFachklasse e WHERE e.gueltigVon IN :value")
@NamedQuery(name="Rev8DTOStatkueFachklasse.gueltigbis", query="SELECT e FROM Rev8DTOStatkueFachklasse e WHERE e.gueltigBis = :value")
@NamedQuery(name="Rev8DTOStatkueFachklasse.gueltigbis.multiple", query="SELECT e FROM Rev8DTOStatkueFachklasse e WHERE e.gueltigBis IN :value")
@NamedQuery(name="Rev8DTOStatkueFachklasse.primaryKeyQuery", query="SELECT e FROM Rev8DTOStatkueFachklasse e WHERE e.BKIndex = ?1 AND e.FKS = ?2 AND e.AP = ?3")
@JsonPropertyOrder({"BKIndex","FKS","AP","Flag","BGrp","BFeld","Ebene1","Ebene2","Ebene3","Sortierung","Beschreibung_MW","Beschreibung","Beschreibung_W","Status","Flag_APOBK","BAKL","BAGR","BAKLALT","BAGRALT","geaendert","gueltigVon","gueltigBis"})
public class Rev8DTOStatkueFachklasse {

	/** Schild-ID der Fachgruppe */
	@Id
	@Column(name = "BKIndex")
	@JsonProperty
	public Integer BKIndex;

	/** Fachklassenschlüssel Teil 1 */
	@Id
	@Column(name = "FKS")
	@JsonProperty
	public String FKS;

	/** Fachklassenschlüssel Teil 2 */
	@Id
	@Column(name = "AP")
	@JsonProperty
	public String AP;

	/** Flag: 1 - KLD und SCD012, 2 - nur SCD012, 3 - Ausgelaufen, 4 - nur KLD im Jahr der Einführung */
	@Column(name = "Flag")
	@JsonProperty
	public String Flag;

	/** Die Gruppe des zugehörigen Berufsfelds */
	@Column(name = "BGrp")
	@JsonProperty
	public String BGrp;

	/** Das zugehörigen Berufsfelds */
	@Column(name = "BFeld")
	@JsonProperty
	public String BFeld;

	/** Die Berufsebene 1 */
	@Column(name = "Ebene1")
	@JsonProperty
	public String Ebene1;

	/** Die Berufsebene 2 */
	@Column(name = "Ebene2")
	@JsonProperty
	public String Ebene2;

	/** Die Berufsebene 3 */
	@Column(name = "Ebene3")
	@JsonProperty
	public String Ebene3;

	/** Die Sortierung (orientiert sich am Index) */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Die textuelle Beschreibung der Fachklasse (m/w) */
	@Column(name = "Beschreibung_MW")
	@JsonProperty
	public String Beschreibung_MW;

	/** Die textuelle Beschreibung der Fachklasse */
	@Column(name = "Beschreibung")
	@JsonProperty
	public String Beschreibung;

	/** Die textuelle Beschreibung der Fachklasse (w) */
	@Column(name = "Beschreibung_W")
	@JsonProperty
	public String Beschreibung_W;

	/** zur Kompatibilität (hier leer) */
	@Column(name = "Status")
	@JsonProperty
	public String Status;

	/** zur Kompatibilität (hier leer) */
	@Column(name = "Flag_APOBK")
	@JsonProperty
	public String Flag_APOBK;

	/** zur Kompatibilität (hier leer) */
	@Column(name = "BAKL")
	@JsonProperty
	public String BAKL;

	/** zur Kompatibilität (hier leer) */
	@Column(name = "BAGR")
	@JsonProperty
	public String BAGR;

	/** zur Kompatibilität (hier leer) */
	@Column(name = "BAKLALT")
	@JsonProperty
	public String BAKLALT;

	/** zur Kompatibilität (hier leer) */
	@Column(name = "BAGRALT")
	@JsonProperty
	public String BAGRALT;

	/** Datum der letzten Änderung (hier zur Kompatibilität vorhanden) */
	@Column(name = "geaendert")
	@JsonProperty
	public String geaendert;

	/** Gibt die Gültigkeit ab welchem Schuljahr an */
	@Column(name = "gueltigVon")
	@JsonProperty
	public String gueltigVon;

	/** Gibt die Gültigkeit bis zu welchem Schuljahr an */
	@Column(name = "gueltigBis")
	@JsonProperty
	public String gueltigBis;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOStatkueFachklasse ohne eine Initialisierung der Attribute.
	 */
	private Rev8DTOStatkueFachklasse() {
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev8DTOStatkueFachklasse other = (Rev8DTOStatkueFachklasse) obj;
		if (BKIndex == null) {
			if (other.BKIndex != null)
				return false;
		} else if (!BKIndex.equals(other.BKIndex))
			return false;

		if (FKS == null) {
			if (other.FKS != null)
				return false;
		} else if (!FKS.equals(other.FKS))
			return false;

		if (AP == null) {
			if (other.AP != null)
				return false;
		} else if (!AP.equals(other.AP))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((BKIndex == null) ? 0 : BKIndex.hashCode());

		result = prime * result + ((FKS == null) ? 0 : FKS.hashCode());

		result = prime * result + ((AP == null) ? 0 : AP.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "Rev8DTOStatkueFachklasse(BKIndex=" + this.BKIndex + ", FKS=" + this.FKS + ", AP=" + this.AP + ", Flag=" + this.Flag + ", BGrp=" + this.BGrp + ", BFeld=" + this.BFeld + ", Ebene1=" + this.Ebene1 + ", Ebene2=" + this.Ebene2 + ", Ebene3=" + this.Ebene3 + ", Sortierung=" + this.Sortierung + ", Beschreibung_MW=" + this.Beschreibung_MW + ", Beschreibung=" + this.Beschreibung + ", Beschreibung_W=" + this.Beschreibung_W + ", Status=" + this.Status + ", Flag_APOBK=" + this.Flag_APOBK + ", BAKL=" + this.BAKL + ", BAGR=" + this.BAGR + ", BAKLALT=" + this.BAKLALT + ", BAGRALT=" + this.BAGRALT + ", geaendert=" + this.geaendert + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ")";
	}

}