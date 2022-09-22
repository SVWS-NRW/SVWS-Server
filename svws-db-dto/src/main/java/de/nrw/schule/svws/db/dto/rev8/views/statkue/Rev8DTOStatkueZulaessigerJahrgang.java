package de.nrw.schule.svws.db.dto.rev8.views.statkue;

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
 * Diese Klasse dient als DTO für die Datenbank-View Statkue_ZulJahrgaenge.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Statkue_ZulJahrgaenge")
@NamedQuery(name="Rev8DTOStatkueZulaessigerJahrgang.all", query="SELECT e FROM Rev8DTOStatkueZulaessigerJahrgang e")
@NamedQuery(name="Rev8DTOStatkueZulaessigerJahrgang.id", query="SELECT e FROM Rev8DTOStatkueZulaessigerJahrgang e WHERE e.ID = :value")
@NamedQuery(name="Rev8DTOStatkueZulaessigerJahrgang.id.multiple", query="SELECT e FROM Rev8DTOStatkueZulaessigerJahrgang e WHERE e.ID IN :value")
@NamedQuery(name="Rev8DTOStatkueZulaessigerJahrgang.schulform", query="SELECT e FROM Rev8DTOStatkueZulaessigerJahrgang e WHERE e.Schulform = :value")
@NamedQuery(name="Rev8DTOStatkueZulaessigerJahrgang.schulform.multiple", query="SELECT e FROM Rev8DTOStatkueZulaessigerJahrgang e WHERE e.Schulform IN :value")
@NamedQuery(name="Rev8DTOStatkueZulaessigerJahrgang.snr", query="SELECT e FROM Rev8DTOStatkueZulaessigerJahrgang e WHERE e.SNR = :value")
@NamedQuery(name="Rev8DTOStatkueZulaessigerJahrgang.snr.multiple", query="SELECT e FROM Rev8DTOStatkueZulaessigerJahrgang e WHERE e.SNR IN :value")
@NamedQuery(name="Rev8DTOStatkueZulaessigerJahrgang.fsp", query="SELECT e FROM Rev8DTOStatkueZulaessigerJahrgang e WHERE e.FSP = :value")
@NamedQuery(name="Rev8DTOStatkueZulaessigerJahrgang.fsp.multiple", query="SELECT e FROM Rev8DTOStatkueZulaessigerJahrgang e WHERE e.FSP IN :value")
@NamedQuery(name="Rev8DTOStatkueZulaessigerJahrgang.jahrgang", query="SELECT e FROM Rev8DTOStatkueZulaessigerJahrgang e WHERE e.Jahrgang = :value")
@NamedQuery(name="Rev8DTOStatkueZulaessigerJahrgang.jahrgang.multiple", query="SELECT e FROM Rev8DTOStatkueZulaessigerJahrgang e WHERE e.Jahrgang IN :value")
@NamedQuery(name="Rev8DTOStatkueZulaessigerJahrgang.kz_bereich", query="SELECT e FROM Rev8DTOStatkueZulaessigerJahrgang e WHERE e.KZ_Bereich = :value")
@NamedQuery(name="Rev8DTOStatkueZulaessigerJahrgang.kz_bereich.multiple", query="SELECT e FROM Rev8DTOStatkueZulaessigerJahrgang e WHERE e.KZ_Bereich IN :value")
@NamedQuery(name="Rev8DTOStatkueZulaessigerJahrgang.beschreibung", query="SELECT e FROM Rev8DTOStatkueZulaessigerJahrgang e WHERE e.Beschreibung = :value")
@NamedQuery(name="Rev8DTOStatkueZulaessigerJahrgang.beschreibung.multiple", query="SELECT e FROM Rev8DTOStatkueZulaessigerJahrgang e WHERE e.Beschreibung IN :value")
@NamedQuery(name="Rev8DTOStatkueZulaessigerJahrgang.geaendert", query="SELECT e FROM Rev8DTOStatkueZulaessigerJahrgang e WHERE e.geaendert = :value")
@NamedQuery(name="Rev8DTOStatkueZulaessigerJahrgang.geaendert.multiple", query="SELECT e FROM Rev8DTOStatkueZulaessigerJahrgang e WHERE e.geaendert IN :value")
@NamedQuery(name="Rev8DTOStatkueZulaessigerJahrgang.gueltigvon", query="SELECT e FROM Rev8DTOStatkueZulaessigerJahrgang e WHERE e.gueltigVon = :value")
@NamedQuery(name="Rev8DTOStatkueZulaessigerJahrgang.gueltigvon.multiple", query="SELECT e FROM Rev8DTOStatkueZulaessigerJahrgang e WHERE e.gueltigVon IN :value")
@NamedQuery(name="Rev8DTOStatkueZulaessigerJahrgang.gueltigbis", query="SELECT e FROM Rev8DTOStatkueZulaessigerJahrgang e WHERE e.gueltigBis = :value")
@NamedQuery(name="Rev8DTOStatkueZulaessigerJahrgang.gueltigbis.multiple", query="SELECT e FROM Rev8DTOStatkueZulaessigerJahrgang e WHERE e.gueltigBis IN :value")
@NamedQuery(name="Rev8DTOStatkueZulaessigerJahrgang.primaryKeyQuery", query="SELECT e FROM Rev8DTOStatkueZulaessigerJahrgang e WHERE e.ID = ?1")
@JsonPropertyOrder({"ID","Schulform","SNR","FSP","Jahrgang","KZ_Bereich","Beschreibung","geaendert","gueltigVon","gueltigBis"})
public class Rev8DTOStatkueZulaessigerJahrgang {

	/** Eine eindeutige ID des Jahrgangs */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Die Schulform, für welche der Jahrgang definiert ist */
	@Column(name = "Schulform")
	@JsonProperty
	public String Schulform;

	/** Die Schulnummer, für welche der Jahrgang definiert wurde, falls die Definition auf eine schule eingeschränkt ist. */
	@Column(name = "SNR")
	@JsonProperty
	public String SNR;

	/** Der Förderschwerpunkt, fallse die Jahrgangsdefintion nur auf einen FSP eingeschränkt ist */
	@Column(name = "FSP")
	@JsonProperty
	public String FSP;

	/** Das zweistellige Kürzel des Jahrgangs */
	@Column(name = "Jahrgang")
	@JsonProperty
	public String Jahrgang;

	/** Statkue - Feld KZ_Bereich */
	@Column(name = "KZ_Bereich")
	@JsonProperty
	public Integer KZ_Bereich;

	/** die textuelle Beschreibung des Jahrgangs */
	@Column(name = "Beschreibung")
	@JsonProperty
	public String Beschreibung;

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
	 * Erstellt ein neues Objekt der Klasse Rev8DTOStatkueZulaessigerJahrgang ohne eine Initialisierung der Attribute.
	 */
	private Rev8DTOStatkueZulaessigerJahrgang() {
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev8DTOStatkueZulaessigerJahrgang other = (Rev8DTOStatkueZulaessigerJahrgang) obj;
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
		return "Rev8DTOStatkueZulaessigerJahrgang(ID=" + this.ID + ", Schulform=" + this.Schulform + ", SNR=" + this.SNR + ", FSP=" + this.FSP + ", Jahrgang=" + this.Jahrgang + ", KZ_Bereich=" + this.KZ_Bereich + ", Beschreibung=" + this.Beschreibung + ", geaendert=" + this.geaendert + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ")";
	}

}