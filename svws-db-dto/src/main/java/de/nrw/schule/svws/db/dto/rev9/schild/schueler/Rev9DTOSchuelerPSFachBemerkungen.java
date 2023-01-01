package de.nrw.schule.svws.db.dto.rev9.schild.schueler;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerLD_PSFachBem.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerLD_PSFachBem")
@NamedQuery(name="Rev9DTOSchuelerPSFachBemerkungen.all", query="SELECT e FROM Rev9DTOSchuelerPSFachBemerkungen e")
@NamedQuery(name="Rev9DTOSchuelerPSFachBemerkungen.id", query="SELECT e FROM Rev9DTOSchuelerPSFachBemerkungen e WHERE e.ID = :value")
@NamedQuery(name="Rev9DTOSchuelerPSFachBemerkungen.id.multiple", query="SELECT e FROM Rev9DTOSchuelerPSFachBemerkungen e WHERE e.ID IN :value")
@NamedQuery(name="Rev9DTOSchuelerPSFachBemerkungen.abschnitt_id", query="SELECT e FROM Rev9DTOSchuelerPSFachBemerkungen e WHERE e.Abschnitt_ID = :value")
@NamedQuery(name="Rev9DTOSchuelerPSFachBemerkungen.abschnitt_id.multiple", query="SELECT e FROM Rev9DTOSchuelerPSFachBemerkungen e WHERE e.Abschnitt_ID IN :value")
@NamedQuery(name="Rev9DTOSchuelerPSFachBemerkungen.asv", query="SELECT e FROM Rev9DTOSchuelerPSFachBemerkungen e WHERE e.ASV = :value")
@NamedQuery(name="Rev9DTOSchuelerPSFachBemerkungen.asv.multiple", query="SELECT e FROM Rev9DTOSchuelerPSFachBemerkungen e WHERE e.ASV IN :value")
@NamedQuery(name="Rev9DTOSchuelerPSFachBemerkungen.lels", query="SELECT e FROM Rev9DTOSchuelerPSFachBemerkungen e WHERE e.LELS = :value")
@NamedQuery(name="Rev9DTOSchuelerPSFachBemerkungen.lels.multiple", query="SELECT e FROM Rev9DTOSchuelerPSFachBemerkungen e WHERE e.LELS IN :value")
@NamedQuery(name="Rev9DTOSchuelerPSFachBemerkungen.aue", query="SELECT e FROM Rev9DTOSchuelerPSFachBemerkungen e WHERE e.AUE = :value")
@NamedQuery(name="Rev9DTOSchuelerPSFachBemerkungen.aue.multiple", query="SELECT e FROM Rev9DTOSchuelerPSFachBemerkungen e WHERE e.AUE IN :value")
@NamedQuery(name="Rev9DTOSchuelerPSFachBemerkungen.esf", query="SELECT e FROM Rev9DTOSchuelerPSFachBemerkungen e WHERE e.ESF = :value")
@NamedQuery(name="Rev9DTOSchuelerPSFachBemerkungen.esf.multiple", query="SELECT e FROM Rev9DTOSchuelerPSFachBemerkungen e WHERE e.ESF IN :value")
@NamedQuery(name="Rev9DTOSchuelerPSFachBemerkungen.bemerkungfsp", query="SELECT e FROM Rev9DTOSchuelerPSFachBemerkungen e WHERE e.BemerkungFSP = :value")
@NamedQuery(name="Rev9DTOSchuelerPSFachBemerkungen.bemerkungfsp.multiple", query="SELECT e FROM Rev9DTOSchuelerPSFachBemerkungen e WHERE e.BemerkungFSP IN :value")
@NamedQuery(name="Rev9DTOSchuelerPSFachBemerkungen.bemerkungversetzung", query="SELECT e FROM Rev9DTOSchuelerPSFachBemerkungen e WHERE e.BemerkungVersetzung = :value")
@NamedQuery(name="Rev9DTOSchuelerPSFachBemerkungen.bemerkungversetzung.multiple", query="SELECT e FROM Rev9DTOSchuelerPSFachBemerkungen e WHERE e.BemerkungVersetzung IN :value")
@NamedQuery(name="Rev9DTOSchuelerPSFachBemerkungen.primaryKeyQuery", query="SELECT e FROM Rev9DTOSchuelerPSFachBemerkungen e WHERE e.ID = ?1")
@NamedQuery(name="Rev9DTOSchuelerPSFachBemerkungen.all.migration", query="SELECT e FROM Rev9DTOSchuelerPSFachBemerkungen e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Abschnitt_ID","ASV","LELS","AUE","ESF","BemerkungFSP","BemerkungVersetzung"})
public class Rev9DTOSchuelerPSFachBemerkungen {

	/** ID des Bemerkungseintrags */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** ID des Lernabschnittes */
	@Column(name = "Abschnitt_ID")
	@JsonProperty
	public Long Abschnitt_ID;

	/** Text zum Arvbeits und Sozialverhalten */
	@Column(name = "ASV")
	@JsonProperty
	public String ASV;

	/** Text zur Lernentwicklung bei Grundschulen */
	@Column(name = "LELS")
	@JsonProperty
	public String LELS;

	/** Text zum Außerunterrichtlichen Engagement (früher in LELS integeriert, falls, die Schulform keine Grundschule ist) */
	@Column(name = "AUE")
	@JsonProperty
	public String AUE;

	/** Text für die "Empfehlung der Schulform" beim Übergang von Primar- nach SekI */
	@Column(name = "ESF")
	@JsonProperty
	public String ESF;

	/** Text für Förderschwerpunktbemerkung */
	@Column(name = "BemerkungFSP")
	@JsonProperty
	public String BemerkungFSP;

	/** Text für Verstungsentscheidung */
	@Column(name = "BemerkungVersetzung")
	@JsonProperty
	public String BemerkungVersetzung;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev9DTOSchuelerPSFachBemerkungen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev9DTOSchuelerPSFachBemerkungen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev9DTOSchuelerPSFachBemerkungen ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Abschnitt_ID   der Wert für das Attribut Abschnitt_ID
	 */
	public Rev9DTOSchuelerPSFachBemerkungen(final Long ID, final Long Abschnitt_ID) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Abschnitt_ID == null) { 
			throw new NullPointerException("Abschnitt_ID must not be null");
		}
		this.Abschnitt_ID = Abschnitt_ID;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev9DTOSchuelerPSFachBemerkungen other = (Rev9DTOSchuelerPSFachBemerkungen) obj;
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
		return "Rev9DTOSchuelerPSFachBemerkungen(ID=" + this.ID + ", Abschnitt_ID=" + this.Abschnitt_ID + ", ASV=" + this.ASV + ", LELS=" + this.LELS + ", AUE=" + this.AUE + ", ESF=" + this.ESF + ", BemerkungFSP=" + this.BemerkungFSP + ", BemerkungVersetzung=" + this.BemerkungVersetzung + ")";
	}

}