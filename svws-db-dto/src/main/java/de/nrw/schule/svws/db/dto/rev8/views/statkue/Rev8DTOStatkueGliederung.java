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
 * Diese Klasse dient als DTO für die Datenbank-View Statkue_Gliederung.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(Rev8DTOStatkueGliederungPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Statkue_Gliederung")
@NamedQuery(name="Rev8DTOStatkueGliederung.all", query="SELECT e FROM Rev8DTOStatkueGliederung e")
@NamedQuery(name="Rev8DTOStatkueGliederung.sf", query="SELECT e FROM Rev8DTOStatkueGliederung e WHERE e.SF = :value")
@NamedQuery(name="Rev8DTOStatkueGliederung.sf.multiple", query="SELECT e FROM Rev8DTOStatkueGliederung e WHERE e.SF IN :value")
@NamedQuery(name="Rev8DTOStatkueGliederung.flag", query="SELECT e FROM Rev8DTOStatkueGliederung e WHERE e.Flag = :value")
@NamedQuery(name="Rev8DTOStatkueGliederung.flag.multiple", query="SELECT e FROM Rev8DTOStatkueGliederung e WHERE e.Flag IN :value")
@NamedQuery(name="Rev8DTOStatkueGliederung.bkanlage", query="SELECT e FROM Rev8DTOStatkueGliederung e WHERE e.BKAnlage = :value")
@NamedQuery(name="Rev8DTOStatkueGliederung.bkanlage.multiple", query="SELECT e FROM Rev8DTOStatkueGliederung e WHERE e.BKAnlage IN :value")
@NamedQuery(name="Rev8DTOStatkueGliederung.bktyp", query="SELECT e FROM Rev8DTOStatkueGliederung e WHERE e.BKTyp = :value")
@NamedQuery(name="Rev8DTOStatkueGliederung.bktyp.multiple", query="SELECT e FROM Rev8DTOStatkueGliederung e WHERE e.BKTyp IN :value")
@NamedQuery(name="Rev8DTOStatkueGliederung.bkindex", query="SELECT e FROM Rev8DTOStatkueGliederung e WHERE e.BKIndex = :value")
@NamedQuery(name="Rev8DTOStatkueGliederung.bkindex.multiple", query="SELECT e FROM Rev8DTOStatkueGliederung e WHERE e.BKIndex IN :value")
@NamedQuery(name="Rev8DTOStatkueGliederung.beschreibung", query="SELECT e FROM Rev8DTOStatkueGliederung e WHERE e.Beschreibung = :value")
@NamedQuery(name="Rev8DTOStatkueGliederung.beschreibung.multiple", query="SELECT e FROM Rev8DTOStatkueGliederung e WHERE e.Beschreibung IN :value")
@NamedQuery(name="Rev8DTOStatkueGliederung.geaendert", query="SELECT e FROM Rev8DTOStatkueGliederung e WHERE e.geaendert = :value")
@NamedQuery(name="Rev8DTOStatkueGliederung.geaendert.multiple", query="SELECT e FROM Rev8DTOStatkueGliederung e WHERE e.geaendert IN :value")
@NamedQuery(name="Rev8DTOStatkueGliederung.gueltigvon", query="SELECT e FROM Rev8DTOStatkueGliederung e WHERE e.gueltigVon = :value")
@NamedQuery(name="Rev8DTOStatkueGliederung.gueltigvon.multiple", query="SELECT e FROM Rev8DTOStatkueGliederung e WHERE e.gueltigVon IN :value")
@NamedQuery(name="Rev8DTOStatkueGliederung.gueltigbis", query="SELECT e FROM Rev8DTOStatkueGliederung e WHERE e.gueltigBis = :value")
@NamedQuery(name="Rev8DTOStatkueGliederung.gueltigbis.multiple", query="SELECT e FROM Rev8DTOStatkueGliederung e WHERE e.gueltigBis IN :value")
@NamedQuery(name="Rev8DTOStatkueGliederung.primaryKeyQuery", query="SELECT e FROM Rev8DTOStatkueGliederung e WHERE e.SF = ?1 AND e.Flag = ?2 AND e.BKAnlage = ?3 AND e.BKTyp = ?4")
@JsonPropertyOrder({"SF","Flag","BKAnlage","BKTyp","BKIndex","Beschreibung","geaendert","gueltigVon","gueltigBis"})
public class Rev8DTOStatkueGliederung {

	/** Zulässige Schulform der Gliederung */
	@Id
	@Column(name = "SF")
	@JsonProperty
	public String SF;

	/** Ein Flag (hier zur Kompatibilität vorhanden) */
	@Id
	@Column(name = "Flag")
	@JsonProperty
	public String Flag;

	/** Die Anlage bei einem Bildungsgang des Berufskollegs */
	@Id
	@Column(name = "BKAnlage")
	@JsonProperty
	public String BKAnlage;

	/** Der Typ der Anlage bei einem Bildungsgang des Berufskollegs */
	@Id
	@Column(name = "BKTyp")
	@JsonProperty
	public String BKTyp;

	/** Der Index in die Fachklassen-Tabelle einem Bildungsgang des Berufskollegs */
	@Column(name = "BKIndex")
	@JsonProperty
	public String BKIndex;

	/** Textuelle Beschreibung der Schulgliederung */
	@Column(name = "Beschreibung")
	@JsonProperty
	public String Beschreibung;

	/** Das Datum der letzten Änderung (hier zur Kompatibilität vorhanden) */
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
	 * Erstellt ein neues Objekt der Klasse Rev8DTOStatkueGliederung ohne eine Initialisierung der Attribute.
	 */
	private Rev8DTOStatkueGliederung() {
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev8DTOStatkueGliederung other = (Rev8DTOStatkueGliederung) obj;
		if (SF == null) {
			if (other.SF != null)
				return false;
		} else if (!SF.equals(other.SF))
			return false;

		if (Flag == null) {
			if (other.Flag != null)
				return false;
		} else if (!Flag.equals(other.Flag))
			return false;

		if (BKAnlage == null) {
			if (other.BKAnlage != null)
				return false;
		} else if (!BKAnlage.equals(other.BKAnlage))
			return false;

		if (BKTyp == null) {
			if (other.BKTyp != null)
				return false;
		} else if (!BKTyp.equals(other.BKTyp))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((SF == null) ? 0 : SF.hashCode());

		result = prime * result + ((Flag == null) ? 0 : Flag.hashCode());

		result = prime * result + ((BKAnlage == null) ? 0 : BKAnlage.hashCode());

		result = prime * result + ((BKTyp == null) ? 0 : BKTyp.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "Rev8DTOStatkueGliederung(SF=" + this.SF + ", Flag=" + this.Flag + ", BKAnlage=" + this.BKAnlage + ", BKTyp=" + this.BKTyp + ", BKIndex=" + this.BKIndex + ", Beschreibung=" + this.Beschreibung + ", geaendert=" + this.geaendert + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ")";
	}

}