package de.nrw.schule.svws.db.dto.rev8.schild.intern;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Schildintern_Laender.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Schildintern_Laender")
@NamedQuery(name="Rev8DTOInternLaender.all", query="SELECT e FROM Rev8DTOInternLaender e")
@NamedQuery(name="Rev8DTOInternLaender.kurztext", query="SELECT e FROM Rev8DTOInternLaender e WHERE e.Kurztext = :value")
@NamedQuery(name="Rev8DTOInternLaender.kurztext.multiple", query="SELECT e FROM Rev8DTOInternLaender e WHERE e.Kurztext IN :value")
@NamedQuery(name="Rev8DTOInternLaender.langtext", query="SELECT e FROM Rev8DTOInternLaender e WHERE e.Langtext = :value")
@NamedQuery(name="Rev8DTOInternLaender.langtext.multiple", query="SELECT e FROM Rev8DTOInternLaender e WHERE e.Langtext IN :value")
@NamedQuery(name="Rev8DTOInternLaender.sortierung", query="SELECT e FROM Rev8DTOInternLaender e WHERE e.Sortierung = :value")
@NamedQuery(name="Rev8DTOInternLaender.sortierung.multiple", query="SELECT e FROM Rev8DTOInternLaender e WHERE e.Sortierung IN :value")
@NamedQuery(name="Rev8DTOInternLaender.gueltigvon", query="SELECT e FROM Rev8DTOInternLaender e WHERE e.gueltigVon = :value")
@NamedQuery(name="Rev8DTOInternLaender.gueltigvon.multiple", query="SELECT e FROM Rev8DTOInternLaender e WHERE e.gueltigVon IN :value")
@NamedQuery(name="Rev8DTOInternLaender.gueltigbis", query="SELECT e FROM Rev8DTOInternLaender e WHERE e.gueltigBis = :value")
@NamedQuery(name="Rev8DTOInternLaender.gueltigbis.multiple", query="SELECT e FROM Rev8DTOInternLaender e WHERE e.gueltigBis IN :value")
@NamedQuery(name="Rev8DTOInternLaender.primaryKeyQuery", query="SELECT e FROM Rev8DTOInternLaender e WHERE e.Kurztext = ?1")
@NamedQuery(name="Rev8DTOInternLaender.all.migration", query="SELECT e FROM Rev8DTOInternLaender e WHERE e.Kurztext IS NOT NULL")
@JsonPropertyOrder({"Kurztext","Langtext","Sortierung","gueltigVon","gueltigBis"})
public class Rev8DTOInternLaender {

	/** Schildintern Tabelle: Bundesländer TextKurz */
	@Id
	@Column(name = "Kurztext")
	@JsonProperty
	public String Kurztext;

	/** Schildintern Tabelle: Bundesländer Langtext */
	@Column(name = "Langtext")
	@JsonProperty
	public String Langtext;

	/** Schildintern Tabelle: Bundesländer Sortierung */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Schildintern Tabelle: Gültig ab Schuljahr */
	@Column(name = "gueltigVon")
	@JsonProperty
	public Integer gueltigVon;

	/** Schildintern Tabelle: Gültig bis Schuljahr */
	@Column(name = "gueltigBis")
	@JsonProperty
	public Integer gueltigBis;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOInternLaender ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev8DTOInternLaender() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOInternLaender ohne eine Initialisierung der Attribute.
	 * @param Kurztext   der Wert für das Attribut Kurztext
	 */
	public Rev8DTOInternLaender(final String Kurztext) {
		if (Kurztext == null) { 
			throw new NullPointerException("Kurztext must not be null");
		}
		this.Kurztext = Kurztext;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev8DTOInternLaender other = (Rev8DTOInternLaender) obj;
		if (Kurztext == null) {
			if (other.Kurztext != null)
				return false;
		} else if (!Kurztext.equals(other.Kurztext))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Kurztext == null) ? 0 : Kurztext.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "Rev8DTOInternLaender(Kurztext=" + this.Kurztext + ", Langtext=" + this.Langtext + ", Sortierung=" + this.Sortierung + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ")";
	}

}