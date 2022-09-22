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
 * Diese Klasse dient als DTO für die Datenbanktabelle Schildintern_PrfSemAbschl.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(DTOInternPrfSemesterAbschlussPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Schildintern_PrfSemAbschl")
@NamedQuery(name="DTOInternPrfSemesterAbschluss.all", query="SELECT e FROM DTOInternPrfSemesterAbschluss e")
@NamedQuery(name="DTOInternPrfSemesterAbschluss.nr", query="SELECT e FROM DTOInternPrfSemesterAbschluss e WHERE e.Nr = :value")
@NamedQuery(name="DTOInternPrfSemesterAbschluss.nr.multiple", query="SELECT e FROM DTOInternPrfSemesterAbschluss e WHERE e.Nr IN :value")
@NamedQuery(name="DTOInternPrfSemesterAbschluss.klartext", query="SELECT e FROM DTOInternPrfSemesterAbschluss e WHERE e.Klartext = :value")
@NamedQuery(name="DTOInternPrfSemesterAbschluss.klartext.multiple", query="SELECT e FROM DTOInternPrfSemesterAbschluss e WHERE e.Klartext IN :value")
@NamedQuery(name="DTOInternPrfSemesterAbschluss.statistikkrz", query="SELECT e FROM DTOInternPrfSemesterAbschluss e WHERE e.StatistikKrz = :value")
@NamedQuery(name="DTOInternPrfSemesterAbschluss.statistikkrz.multiple", query="SELECT e FROM DTOInternPrfSemesterAbschluss e WHERE e.StatistikKrz IN :value")
@NamedQuery(name="DTOInternPrfSemesterAbschluss.sortierung", query="SELECT e FROM DTOInternPrfSemesterAbschluss e WHERE e.Sortierung = :value")
@NamedQuery(name="DTOInternPrfSemesterAbschluss.sortierung.multiple", query="SELECT e FROM DTOInternPrfSemesterAbschluss e WHERE e.Sortierung IN :value")
@NamedQuery(name="DTOInternPrfSemesterAbschluss.schulform", query="SELECT e FROM DTOInternPrfSemesterAbschluss e WHERE e.Schulform = :value")
@NamedQuery(name="DTOInternPrfSemesterAbschluss.schulform.multiple", query="SELECT e FROM DTOInternPrfSemesterAbschluss e WHERE e.Schulform IN :value")
@NamedQuery(name="DTOInternPrfSemesterAbschluss.statistikkrzneu", query="SELECT e FROM DTOInternPrfSemesterAbschluss e WHERE e.StatistikKrzNeu = :value")
@NamedQuery(name="DTOInternPrfSemesterAbschluss.statistikkrzneu.multiple", query="SELECT e FROM DTOInternPrfSemesterAbschluss e WHERE e.StatistikKrzNeu IN :value")
@NamedQuery(name="DTOInternPrfSemesterAbschluss.gueltigvon", query="SELECT e FROM DTOInternPrfSemesterAbschluss e WHERE e.gueltigVon = :value")
@NamedQuery(name="DTOInternPrfSemesterAbschluss.gueltigvon.multiple", query="SELECT e FROM DTOInternPrfSemesterAbschluss e WHERE e.gueltigVon IN :value")
@NamedQuery(name="DTOInternPrfSemesterAbschluss.gueltigbis", query="SELECT e FROM DTOInternPrfSemesterAbschluss e WHERE e.gueltigBis = :value")
@NamedQuery(name="DTOInternPrfSemesterAbschluss.gueltigbis.multiple", query="SELECT e FROM DTOInternPrfSemesterAbschluss e WHERE e.gueltigBis IN :value")
@NamedQuery(name="DTOInternPrfSemesterAbschluss.primaryKeyQuery", query="SELECT e FROM DTOInternPrfSemesterAbschluss e WHERE e.Nr = ?1 AND e.Schulform = ?2")
@NamedQuery(name="DTOInternPrfSemesterAbschluss.all.migration", query="SELECT e FROM DTOInternPrfSemesterAbschluss e WHERE e.Nr IS NOT NULL AND e.Schulform IS NOT NULL")
@JsonPropertyOrder({"Nr","Klartext","StatistikKrz","Sortierung","Schulform","StatistikKrzNeu","gueltigVon","gueltigBis"})
public class DTOInternPrfSemesterAbschluss {

	/** Schildintern Tabelle: Nummer des Versetzungsvermerk */
	@Id
	@Column(name = "Nr")
	@JsonProperty
	public String Nr;

	/** Schildintern Tabelle: Klartext des Versetzungsvermerk */
	@Column(name = "Klartext")
	@JsonProperty
	public String Klartext;

	/** Schildintern Tabelle: Statistikkürzel des Versetzungsvermerk (DEPRECATED) */
	@Column(name = "StatistikKrz")
	@JsonProperty
	public String StatistikKrz;

	/** Schildintern Tabelle: Sortierung des Versetzungsvermerk */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Schildintern Tabelle: Schulform des Versetzungsvermerk */
	@Id
	@Column(name = "Schulform")
	@JsonProperty
	public String Schulform;

	/** Schildintern Tabelle: Neue Statistikkürzel  des Versetzungsvermerk */
	@Column(name = "StatistikKrzNeu")
	@JsonProperty
	public String StatistikKrzNeu;

	/** Schildintern Tabelle: Gültig ab Schuljahr */
	@Column(name = "gueltigVon")
	@JsonProperty
	public Integer gueltigVon;

	/** Schildintern Tabelle: Gültig bis Schuljahr */
	@Column(name = "gueltigBis")
	@JsonProperty
	public Integer gueltigBis;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOInternPrfSemesterAbschluss ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOInternPrfSemesterAbschluss() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOInternPrfSemesterAbschluss ohne eine Initialisierung der Attribute.
	 * @param Nr   der Wert für das Attribut Nr
	 * @param Schulform   der Wert für das Attribut Schulform
	 */
	public DTOInternPrfSemesterAbschluss(final String Nr, final String Schulform) {
		if (Nr == null) { 
			throw new NullPointerException("Nr must not be null");
		}
		this.Nr = Nr;
		if (Schulform == null) { 
			throw new NullPointerException("Schulform must not be null");
		}
		this.Schulform = Schulform;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOInternPrfSemesterAbschluss other = (DTOInternPrfSemesterAbschluss) obj;
		if (Nr == null) {
			if (other.Nr != null)
				return false;
		} else if (!Nr.equals(other.Nr))
			return false;

		if (Schulform == null) {
			if (other.Schulform != null)
				return false;
		} else if (!Schulform.equals(other.Schulform))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Nr == null) ? 0 : Nr.hashCode());

		result = prime * result + ((Schulform == null) ? 0 : Schulform.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOInternPrfSemesterAbschluss(Nr=" + this.Nr + ", Klartext=" + this.Klartext + ", StatistikKrz=" + this.StatistikKrz + ", Sortierung=" + this.Sortierung + ", Schulform=" + this.Schulform + ", StatistikKrzNeu=" + this.StatistikKrzNeu + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ")";
	}

}