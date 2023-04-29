package de.svws_nrw.db.dto.current.schild.schueler;

import de.svws_nrw.db.DBEntityManager;

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
@NamedQuery(name = "DTOSchuelerPSFachBemerkungen.all", query = "SELECT e FROM DTOSchuelerPSFachBemerkungen e")
@NamedQuery(name = "DTOSchuelerPSFachBemerkungen.id", query = "SELECT e FROM DTOSchuelerPSFachBemerkungen e WHERE e.ID = :value")
@NamedQuery(name = "DTOSchuelerPSFachBemerkungen.id.multiple", query = "SELECT e FROM DTOSchuelerPSFachBemerkungen e WHERE e.ID IN :value")
@NamedQuery(name = "DTOSchuelerPSFachBemerkungen.abschnitt_id", query = "SELECT e FROM DTOSchuelerPSFachBemerkungen e WHERE e.Abschnitt_ID = :value")
@NamedQuery(name = "DTOSchuelerPSFachBemerkungen.abschnitt_id.multiple", query = "SELECT e FROM DTOSchuelerPSFachBemerkungen e WHERE e.Abschnitt_ID IN :value")
@NamedQuery(name = "DTOSchuelerPSFachBemerkungen.asv", query = "SELECT e FROM DTOSchuelerPSFachBemerkungen e WHERE e.ASV = :value")
@NamedQuery(name = "DTOSchuelerPSFachBemerkungen.asv.multiple", query = "SELECT e FROM DTOSchuelerPSFachBemerkungen e WHERE e.ASV IN :value")
@NamedQuery(name = "DTOSchuelerPSFachBemerkungen.lels", query = "SELECT e FROM DTOSchuelerPSFachBemerkungen e WHERE e.LELS = :value")
@NamedQuery(name = "DTOSchuelerPSFachBemerkungen.lels.multiple", query = "SELECT e FROM DTOSchuelerPSFachBemerkungen e WHERE e.LELS IN :value")
@NamedQuery(name = "DTOSchuelerPSFachBemerkungen.aue", query = "SELECT e FROM DTOSchuelerPSFachBemerkungen e WHERE e.AUE = :value")
@NamedQuery(name = "DTOSchuelerPSFachBemerkungen.aue.multiple", query = "SELECT e FROM DTOSchuelerPSFachBemerkungen e WHERE e.AUE IN :value")
@NamedQuery(name = "DTOSchuelerPSFachBemerkungen.esf", query = "SELECT e FROM DTOSchuelerPSFachBemerkungen e WHERE e.ESF = :value")
@NamedQuery(name = "DTOSchuelerPSFachBemerkungen.esf.multiple", query = "SELECT e FROM DTOSchuelerPSFachBemerkungen e WHERE e.ESF IN :value")
@NamedQuery(name = "DTOSchuelerPSFachBemerkungen.bemerkungfsp", query = "SELECT e FROM DTOSchuelerPSFachBemerkungen e WHERE e.BemerkungFSP = :value")
@NamedQuery(name = "DTOSchuelerPSFachBemerkungen.bemerkungfsp.multiple", query = "SELECT e FROM DTOSchuelerPSFachBemerkungen e WHERE e.BemerkungFSP IN :value")
@NamedQuery(name = "DTOSchuelerPSFachBemerkungen.bemerkungversetzung", query = "SELECT e FROM DTOSchuelerPSFachBemerkungen e WHERE e.BemerkungVersetzung = :value")
@NamedQuery(name = "DTOSchuelerPSFachBemerkungen.bemerkungversetzung.multiple", query = "SELECT e FROM DTOSchuelerPSFachBemerkungen e WHERE e.BemerkungVersetzung IN :value")
@NamedQuery(name = "DTOSchuelerPSFachBemerkungen.primaryKeyQuery", query = "SELECT e FROM DTOSchuelerPSFachBemerkungen e WHERE e.ID = ?1")
@NamedQuery(name = "DTOSchuelerPSFachBemerkungen.all.migration", query = "SELECT e FROM DTOSchuelerPSFachBemerkungen e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Abschnitt_ID", "ASV", "LELS", "AUE", "ESF", "BemerkungFSP", "BemerkungVersetzung"})
public final class DTOSchuelerPSFachBemerkungen {

	/** ID des Bemerkungseintrags */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** ID des Lernabschnittes */
	@Column(name = "Abschnitt_ID")
	@JsonProperty
	public long Abschnitt_ID;

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
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerPSFachBemerkungen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSchuelerPSFachBemerkungen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerPSFachBemerkungen ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Abschnitt_ID   der Wert für das Attribut Abschnitt_ID
	 */
	public DTOSchuelerPSFachBemerkungen(final long ID, final long Abschnitt_ID) {
		this.ID = ID;
		this.Abschnitt_ID = Abschnitt_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOSchuelerPSFachBemerkungen other = (DTOSchuelerPSFachBemerkungen) obj;
		if (ID != other.ID)
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(ID);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOSchuelerPSFachBemerkungen(ID=" + this.ID + ", Abschnitt_ID=" + this.Abschnitt_ID + ", ASV=" + this.ASV + ", LELS=" + this.LELS + ", AUE=" + this.AUE + ", ESF=" + this.ESF + ", BemerkungFSP=" + this.BemerkungFSP + ", BemerkungVersetzung=" + this.BemerkungVersetzung + ")";
	}

}
