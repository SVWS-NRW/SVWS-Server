package de.nrw.schule.svws.db.dto.migration.schild.intern;

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
@NamedQuery(name="MigrationDTOInternLaender.all", query="SELECT e FROM MigrationDTOInternLaender e")
@NamedQuery(name="MigrationDTOInternLaender.kurztext", query="SELECT e FROM MigrationDTOInternLaender e WHERE e.Kurztext = :value")
@NamedQuery(name="MigrationDTOInternLaender.kurztext.multiple", query="SELECT e FROM MigrationDTOInternLaender e WHERE e.Kurztext IN :value")
@NamedQuery(name="MigrationDTOInternLaender.langtext", query="SELECT e FROM MigrationDTOInternLaender e WHERE e.Langtext = :value")
@NamedQuery(name="MigrationDTOInternLaender.langtext.multiple", query="SELECT e FROM MigrationDTOInternLaender e WHERE e.Langtext IN :value")
@NamedQuery(name="MigrationDTOInternLaender.sortierung", query="SELECT e FROM MigrationDTOInternLaender e WHERE e.Sortierung = :value")
@NamedQuery(name="MigrationDTOInternLaender.sortierung.multiple", query="SELECT e FROM MigrationDTOInternLaender e WHERE e.Sortierung IN :value")
@NamedQuery(name="MigrationDTOInternLaender.gueltigvon", query="SELECT e FROM MigrationDTOInternLaender e WHERE e.gueltigVon = :value")
@NamedQuery(name="MigrationDTOInternLaender.gueltigvon.multiple", query="SELECT e FROM MigrationDTOInternLaender e WHERE e.gueltigVon IN :value")
@NamedQuery(name="MigrationDTOInternLaender.gueltigbis", query="SELECT e FROM MigrationDTOInternLaender e WHERE e.gueltigBis = :value")
@NamedQuery(name="MigrationDTOInternLaender.gueltigbis.multiple", query="SELECT e FROM MigrationDTOInternLaender e WHERE e.gueltigBis IN :value")
@NamedQuery(name="MigrationDTOInternLaender.primaryKeyQuery", query="SELECT e FROM MigrationDTOInternLaender e WHERE e.Kurztext = ?1")
@NamedQuery(name="MigrationDTOInternLaender.all.migration", query="SELECT e FROM MigrationDTOInternLaender e WHERE e.Kurztext IS NOT NULL")
@JsonPropertyOrder({"Kurztext","Langtext","Sortierung","gueltigVon","gueltigBis"})
public class MigrationDTOInternLaender {

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
	 * Erstellt ein neues Objekt der Klasse MigrationDTOInternLaender ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOInternLaender() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOInternLaender ohne eine Initialisierung der Attribute.
	 * @param Kurztext   der Wert für das Attribut Kurztext
	 */
	public MigrationDTOInternLaender(final String Kurztext) {
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
		MigrationDTOInternLaender other = (MigrationDTOInternLaender) obj;
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
		return "MigrationDTOInternLaender(Kurztext=" + this.Kurztext + ", Langtext=" + this.Langtext + ", Sortierung=" + this.Sortierung + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ")";
	}

}