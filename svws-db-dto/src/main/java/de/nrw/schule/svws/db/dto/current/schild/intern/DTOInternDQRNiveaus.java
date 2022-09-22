package de.nrw.schule.svws.db.dto.current.schild.intern;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Schildintern_DQR_Niveaus.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(DTOInternDQRNiveausPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Schildintern_DQR_Niveaus")
@NamedQuery(name="DTOInternDQRNiveaus.all", query="SELECT e FROM DTOInternDQRNiveaus e")
@NamedQuery(name="DTOInternDQRNiveaus.gliederung", query="SELECT e FROM DTOInternDQRNiveaus e WHERE e.Gliederung = :value")
@NamedQuery(name="DTOInternDQRNiveaus.gliederung.multiple", query="SELECT e FROM DTOInternDQRNiveaus e WHERE e.Gliederung IN :value")
@NamedQuery(name="DTOInternDQRNiveaus.fks", query="SELECT e FROM DTOInternDQRNiveaus e WHERE e.FKS = :value")
@NamedQuery(name="DTOInternDQRNiveaus.fks.multiple", query="SELECT e FROM DTOInternDQRNiveaus e WHERE e.FKS IN :value")
@NamedQuery(name="DTOInternDQRNiveaus.dqr_niveau", query="SELECT e FROM DTOInternDQRNiveaus e WHERE e.DQR_Niveau = :value")
@NamedQuery(name="DTOInternDQRNiveaus.dqr_niveau.multiple", query="SELECT e FROM DTOInternDQRNiveaus e WHERE e.DQR_Niveau IN :value")
@NamedQuery(name="DTOInternDQRNiveaus.gueltigvon", query="SELECT e FROM DTOInternDQRNiveaus e WHERE e.gueltigVon = :value")
@NamedQuery(name="DTOInternDQRNiveaus.gueltigvon.multiple", query="SELECT e FROM DTOInternDQRNiveaus e WHERE e.gueltigVon IN :value")
@NamedQuery(name="DTOInternDQRNiveaus.gueltigbis", query="SELECT e FROM DTOInternDQRNiveaus e WHERE e.gueltigBis = :value")
@NamedQuery(name="DTOInternDQRNiveaus.gueltigbis.multiple", query="SELECT e FROM DTOInternDQRNiveaus e WHERE e.gueltigBis IN :value")
@NamedQuery(name="DTOInternDQRNiveaus.primaryKeyQuery", query="SELECT e FROM DTOInternDQRNiveaus e WHERE e.Gliederung = ?1 AND e.FKS = ?2 AND e.DQR_Niveau = ?3")
@NamedQuery(name="DTOInternDQRNiveaus.all.migration", query="SELECT e FROM DTOInternDQRNiveaus e WHERE e.Gliederung IS NOT NULL AND e.FKS IS NOT NULL AND e.DQR_Niveau IS NOT NULL")
@JsonPropertyOrder({"Gliederung","FKS","DQR_Niveau","gueltigVon","gueltigBis"})
public class DTOInternDQRNiveaus {

	/** Schildintern Tabelle: DQR-Niveau für Gliederung */
	@Id
	@Column(name = "Gliederung")
	@JsonProperty
	public String Gliederung;

	/** Schildintern Tabelle: DQR-Niveau für die FAchklasse */
	@Id
	@Column(name = "FKS")
	@JsonProperty
	public String FKS;

	/** Schildintern Tabelle: DQR-Niveau als Nummer */
	@Id
	@Column(name = "DQR_Niveau")
	@JsonProperty
	public Integer DQR_Niveau;

	/** Schildintern Tabelle: Gültig ab Schuljahr */
	@Column(name = "gueltigVon")
	@JsonProperty
	public Integer gueltigVon;

	/** Schildintern Tabelle: Gültig bis Schuljahr */
	@Column(name = "gueltigBis")
	@JsonProperty
	public Integer gueltigBis;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOInternDQRNiveaus ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOInternDQRNiveaus() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOInternDQRNiveaus ohne eine Initialisierung der Attribute.
	 * @param Gliederung   der Wert für das Attribut Gliederung
	 * @param FKS   der Wert für das Attribut FKS
	 * @param DQR_Niveau   der Wert für das Attribut DQR_Niveau
	 */
	public DTOInternDQRNiveaus(final String Gliederung, final String FKS, final Integer DQR_Niveau) {
		if (Gliederung == null) { 
			throw new NullPointerException("Gliederung must not be null");
		}
		this.Gliederung = Gliederung;
		if (FKS == null) { 
			throw new NullPointerException("FKS must not be null");
		}
		this.FKS = FKS;
		if (DQR_Niveau == null) { 
			throw new NullPointerException("DQR_Niveau must not be null");
		}
		this.DQR_Niveau = DQR_Niveau;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOInternDQRNiveaus other = (DTOInternDQRNiveaus) obj;
		if (Gliederung == null) {
			if (other.Gliederung != null)
				return false;
		} else if (!Gliederung.equals(other.Gliederung))
			return false;

		if (FKS == null) {
			if (other.FKS != null)
				return false;
		} else if (!FKS.equals(other.FKS))
			return false;

		if (DQR_Niveau == null) {
			if (other.DQR_Niveau != null)
				return false;
		} else if (!DQR_Niveau.equals(other.DQR_Niveau))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Gliederung == null) ? 0 : Gliederung.hashCode());

		result = prime * result + ((FKS == null) ? 0 : FKS.hashCode());

		result = prime * result + ((DQR_Niveau == null) ? 0 : DQR_Niveau.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOInternDQRNiveaus(Gliederung=" + this.Gliederung + ", FKS=" + this.FKS + ", DQR_Niveau=" + this.DQR_Niveau + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ")";
	}

}