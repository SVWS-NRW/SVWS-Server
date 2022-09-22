package de.nrw.schule.svws.db.dto.migration.schild.intern;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Schildintern_AbiturInfos.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(MigrationDTOInternAbiturInfosPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Schildintern_AbiturInfos")
@NamedQuery(name="MigrationDTOInternAbiturInfos.all", query="SELECT e FROM MigrationDTOInternAbiturInfos e")
@NamedQuery(name="MigrationDTOInternAbiturInfos.prfordnung", query="SELECT e FROM MigrationDTOInternAbiturInfos e WHERE e.PrfOrdnung = :value")
@NamedQuery(name="MigrationDTOInternAbiturInfos.prfordnung.multiple", query="SELECT e FROM MigrationDTOInternAbiturInfos e WHERE e.PrfOrdnung IN :value")
@NamedQuery(name="MigrationDTOInternAbiturInfos.abifach", query="SELECT e FROM MigrationDTOInternAbiturInfos e WHERE e.AbiFach = :value")
@NamedQuery(name="MigrationDTOInternAbiturInfos.abifach.multiple", query="SELECT e FROM MigrationDTOInternAbiturInfos e WHERE e.AbiFach IN :value")
@NamedQuery(name="MigrationDTOInternAbiturInfos.bedingung", query="SELECT e FROM MigrationDTOInternAbiturInfos e WHERE e.Bedingung = :value")
@NamedQuery(name="MigrationDTOInternAbiturInfos.bedingung.multiple", query="SELECT e FROM MigrationDTOInternAbiturInfos e WHERE e.Bedingung IN :value")
@NamedQuery(name="MigrationDTOInternAbiturInfos.abiinfokrz", query="SELECT e FROM MigrationDTOInternAbiturInfos e WHERE e.AbiInfoKrz = :value")
@NamedQuery(name="MigrationDTOInternAbiturInfos.abiinfokrz.multiple", query="SELECT e FROM MigrationDTOInternAbiturInfos e WHERE e.AbiInfoKrz IN :value")
@NamedQuery(name="MigrationDTOInternAbiturInfos.abiinfobeschreibung", query="SELECT e FROM MigrationDTOInternAbiturInfos e WHERE e.AbiInfoBeschreibung = :value")
@NamedQuery(name="MigrationDTOInternAbiturInfos.abiinfobeschreibung.multiple", query="SELECT e FROM MigrationDTOInternAbiturInfos e WHERE e.AbiInfoBeschreibung IN :value")
@NamedQuery(name="MigrationDTOInternAbiturInfos.abiinfotext", query="SELECT e FROM MigrationDTOInternAbiturInfos e WHERE e.AbiInfoText = :value")
@NamedQuery(name="MigrationDTOInternAbiturInfos.abiinfotext.multiple", query="SELECT e FROM MigrationDTOInternAbiturInfos e WHERE e.AbiInfoText IN :value")
@NamedQuery(name="MigrationDTOInternAbiturInfos.gueltigvon", query="SELECT e FROM MigrationDTOInternAbiturInfos e WHERE e.gueltigVon = :value")
@NamedQuery(name="MigrationDTOInternAbiturInfos.gueltigvon.multiple", query="SELECT e FROM MigrationDTOInternAbiturInfos e WHERE e.gueltigVon IN :value")
@NamedQuery(name="MigrationDTOInternAbiturInfos.gueltigbis", query="SELECT e FROM MigrationDTOInternAbiturInfos e WHERE e.gueltigBis = :value")
@NamedQuery(name="MigrationDTOInternAbiturInfos.gueltigbis.multiple", query="SELECT e FROM MigrationDTOInternAbiturInfos e WHERE e.gueltigBis IN :value")
@NamedQuery(name="MigrationDTOInternAbiturInfos.primaryKeyQuery", query="SELECT e FROM MigrationDTOInternAbiturInfos e WHERE e.AbiFach = ?1 AND e.AbiInfoKrz = ?2 AND e.Bedingung = ?3 AND e.PrfOrdnung = ?4")
@NamedQuery(name="MigrationDTOInternAbiturInfos.all.migration", query="SELECT e FROM MigrationDTOInternAbiturInfos e WHERE e.AbiFach IS NOT NULL AND e.AbiInfoKrz IS NOT NULL AND e.Bedingung IS NOT NULL AND e.PrfOrdnung IS NOT NULL")
@JsonPropertyOrder({"PrfOrdnung","AbiFach","Bedingung","AbiInfoKrz","AbiInfoBeschreibung","AbiInfoText","gueltigVon","gueltigBis"})
public class MigrationDTOInternAbiturInfos {

	/** Schildintern Tabelle: Hier stehen neben Regeln für die alte u.a. einige Regeln für die FHR-Berechnung an GY und GE drin */
	@Id
	@Column(name = "PrfOrdnung")
	@JsonProperty
	public String PrfOrdnung;

	/** Schildintern Tabelle: Hier stehen neben Regeln für die alte u.a. einige Regeln für die FHR-Berechnung an GY und GE drin */
	@Id
	@Column(name = "AbiFach")
	@JsonProperty
	public String AbiFach;

	/** Schildintern Tabelle: Hier stehen neben Regeln für die alte u.a. einige Regeln für die FHR-Berechnung an GY und GE drin */
	@Id
	@Column(name = "Bedingung")
	@JsonProperty
	public String Bedingung;

	/** Schildintern Tabelle: Hier stehen neben Regeln für die alte u.a. einige Regeln für die FHR-Berechnung an GY und GE drin */
	@Id
	@Column(name = "AbiInfoKrz")
	@JsonProperty
	public String AbiInfoKrz;

	/** Schildintern Tabelle: Hier stehen neben Regeln für die alte u.a. einige Regeln für die FHR-Berechnung an GY und GE drin */
	@Column(name = "AbiInfoBeschreibung")
	@JsonProperty
	public String AbiInfoBeschreibung;

	/** Schildintern Tabelle: Hier stehen neben Regeln für die alte u.a. einige Regeln für die FHR-Berechnung an GY und GE drin */
	@Column(name = "AbiInfoText")
	@JsonProperty
	public String AbiInfoText;

	/** Schildintern Tabelle: Gültig ab Schuljahr */
	@Column(name = "gueltigVon")
	@JsonProperty
	public Integer gueltigVon;

	/** Schildintern Tabelle: Gültig bis Schuljahr */
	@Column(name = "gueltigBis")
	@JsonProperty
	public Integer gueltigBis;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOInternAbiturInfos ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOInternAbiturInfos() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOInternAbiturInfos ohne eine Initialisierung der Attribute.
	 * @param PrfOrdnung   der Wert für das Attribut PrfOrdnung
	 * @param AbiFach   der Wert für das Attribut AbiFach
	 */
	public MigrationDTOInternAbiturInfos(final String PrfOrdnung, final String AbiFach) {
		if (PrfOrdnung == null) { 
			throw new NullPointerException("PrfOrdnung must not be null");
		}
		this.PrfOrdnung = PrfOrdnung;
		if (AbiFach == null) { 
			throw new NullPointerException("AbiFach must not be null");
		}
		this.AbiFach = AbiFach;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOInternAbiturInfos other = (MigrationDTOInternAbiturInfos) obj;
		if (AbiFach == null) {
			if (other.AbiFach != null)
				return false;
		} else if (!AbiFach.equals(other.AbiFach))
			return false;

		if (AbiInfoKrz == null) {
			if (other.AbiInfoKrz != null)
				return false;
		} else if (!AbiInfoKrz.equals(other.AbiInfoKrz))
			return false;

		if (Bedingung == null) {
			if (other.Bedingung != null)
				return false;
		} else if (!Bedingung.equals(other.Bedingung))
			return false;

		if (PrfOrdnung == null) {
			if (other.PrfOrdnung != null)
				return false;
		} else if (!PrfOrdnung.equals(other.PrfOrdnung))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((AbiFach == null) ? 0 : AbiFach.hashCode());

		result = prime * result + ((AbiInfoKrz == null) ? 0 : AbiInfoKrz.hashCode());

		result = prime * result + ((Bedingung == null) ? 0 : Bedingung.hashCode());

		result = prime * result + ((PrfOrdnung == null) ? 0 : PrfOrdnung.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "MigrationDTOInternAbiturInfos(PrfOrdnung=" + this.PrfOrdnung + ", AbiFach=" + this.AbiFach + ", Bedingung=" + this.Bedingung + ", AbiInfoKrz=" + this.AbiInfoKrz + ", AbiInfoBeschreibung=" + this.AbiInfoBeschreibung + ", AbiInfoText=" + this.AbiInfoText + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ")";
	}

}