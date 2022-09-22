package de.nrw.schule.svws.db.dto.current.statkue;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Statkue_SVWS_SprachpruefungNiveaus.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Statkue_SVWS_SprachpruefungNiveaus")
@NamedQuery(name="DTOSVWSSprachpruefungNiveaus.all", query="SELECT e FROM DTOSVWSSprachpruefungNiveaus e")
@NamedQuery(name="DTOSVWSSprachpruefungNiveaus.id", query="SELECT e FROM DTOSVWSSprachpruefungNiveaus e WHERE e.ID = :value")
@NamedQuery(name="DTOSVWSSprachpruefungNiveaus.id.multiple", query="SELECT e FROM DTOSVWSSprachpruefungNiveaus e WHERE e.ID IN :value")
@NamedQuery(name="DTOSVWSSprachpruefungNiveaus.bezeichnung", query="SELECT e FROM DTOSVWSSprachpruefungNiveaus e WHERE e.Bezeichnung = :value")
@NamedQuery(name="DTOSVWSSprachpruefungNiveaus.bezeichnung.multiple", query="SELECT e FROM DTOSVWSSprachpruefungNiveaus e WHERE e.Bezeichnung IN :value")
@NamedQuery(name="DTOSVWSSprachpruefungNiveaus.beschreibung", query="SELECT e FROM DTOSVWSSprachpruefungNiveaus e WHERE e.Beschreibung = :value")
@NamedQuery(name="DTOSVWSSprachpruefungNiveaus.beschreibung.multiple", query="SELECT e FROM DTOSVWSSprachpruefungNiveaus e WHERE e.Beschreibung IN :value")
@NamedQuery(name="DTOSVWSSprachpruefungNiveaus.sortierung", query="SELECT e FROM DTOSVWSSprachpruefungNiveaus e WHERE e.Sortierung = :value")
@NamedQuery(name="DTOSVWSSprachpruefungNiveaus.sortierung.multiple", query="SELECT e FROM DTOSVWSSprachpruefungNiveaus e WHERE e.Sortierung IN :value")
@NamedQuery(name="DTOSVWSSprachpruefungNiveaus.gueltigvon", query="SELECT e FROM DTOSVWSSprachpruefungNiveaus e WHERE e.gueltigVon = :value")
@NamedQuery(name="DTOSVWSSprachpruefungNiveaus.gueltigvon.multiple", query="SELECT e FROM DTOSVWSSprachpruefungNiveaus e WHERE e.gueltigVon IN :value")
@NamedQuery(name="DTOSVWSSprachpruefungNiveaus.gueltigbis", query="SELECT e FROM DTOSVWSSprachpruefungNiveaus e WHERE e.gueltigBis = :value")
@NamedQuery(name="DTOSVWSSprachpruefungNiveaus.gueltigbis.multiple", query="SELECT e FROM DTOSVWSSprachpruefungNiveaus e WHERE e.gueltigBis IN :value")
@NamedQuery(name="DTOSVWSSprachpruefungNiveaus.primaryKeyQuery", query="SELECT e FROM DTOSVWSSprachpruefungNiveaus e WHERE e.ID = ?1")
@NamedQuery(name="DTOSVWSSprachpruefungNiveaus.all.migration", query="SELECT e FROM DTOSVWSSprachpruefungNiveaus e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Bezeichnung","Beschreibung","Sortierung","gueltigVon","gueltigBis"})
public class DTOSVWSSprachpruefungNiveaus {

	/** ID des Niveaus der Sprachprüfung */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Bezeichnung des Niveaus der Sprachprüfung */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** Beschreibung des Niveaus der Sprachprüfung */
	@Column(name = "Beschreibung")
	@JsonProperty
	public String Beschreibung;

	/** Sortierung der Niveaus nach Anspruchshöhe */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Das Schuljahr, ab dem das Niveau eingeführt wurde */
	@Column(name = "gueltigVon")
	@JsonProperty
	public Integer gueltigVon;

	/** Das Schuljahr, bis wann das Niveau gültig ist */
	@Column(name = "gueltigBis")
	@JsonProperty
	public Integer gueltigBis;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSVWSSprachpruefungNiveaus ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSVWSSprachpruefungNiveaus() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSVWSSprachpruefungNiveaus ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Bezeichnung   der Wert für das Attribut Bezeichnung
	 * @param Beschreibung   der Wert für das Attribut Beschreibung
	 * @param Sortierung   der Wert für das Attribut Sortierung
	 */
	public DTOSVWSSprachpruefungNiveaus(final Long ID, final String Bezeichnung, final String Beschreibung, final Integer Sortierung) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Bezeichnung == null) { 
			throw new NullPointerException("Bezeichnung must not be null");
		}
		this.Bezeichnung = Bezeichnung;
		if (Beschreibung == null) { 
			throw new NullPointerException("Beschreibung must not be null");
		}
		this.Beschreibung = Beschreibung;
		if (Sortierung == null) { 
			throw new NullPointerException("Sortierung must not be null");
		}
		this.Sortierung = Sortierung;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOSVWSSprachpruefungNiveaus other = (DTOSVWSSprachpruefungNiveaus) obj;
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
		return "DTOSVWSSprachpruefungNiveaus(ID=" + this.ID + ", Bezeichnung=" + this.Bezeichnung + ", Beschreibung=" + this.Beschreibung + ", Sortierung=" + this.Sortierung + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ")";
	}

}