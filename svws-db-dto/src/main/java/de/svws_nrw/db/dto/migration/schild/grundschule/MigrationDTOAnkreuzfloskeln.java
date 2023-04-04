package de.svws_nrw.db.dto.migration.schild.grundschule;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverter;


import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverterSerializer;
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle K_Ankreuzfloskeln.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "K_Ankreuzfloskeln")
@NamedQuery(name = "MigrationDTOAnkreuzfloskeln.all", query = "SELECT e FROM MigrationDTOAnkreuzfloskeln e")
@NamedQuery(name = "MigrationDTOAnkreuzfloskeln.id", query = "SELECT e FROM MigrationDTOAnkreuzfloskeln e WHERE e.ID = :value")
@NamedQuery(name = "MigrationDTOAnkreuzfloskeln.id.multiple", query = "SELECT e FROM MigrationDTOAnkreuzfloskeln e WHERE e.ID IN :value")
@NamedQuery(name = "MigrationDTOAnkreuzfloskeln.schulnreigner", query = "SELECT e FROM MigrationDTOAnkreuzfloskeln e WHERE e.SchulnrEigner = :value")
@NamedQuery(name = "MigrationDTOAnkreuzfloskeln.schulnreigner.multiple", query = "SELECT e FROM MigrationDTOAnkreuzfloskeln e WHERE e.SchulnrEigner IN :value")
@NamedQuery(name = "MigrationDTOAnkreuzfloskeln.fach_id", query = "SELECT e FROM MigrationDTOAnkreuzfloskeln e WHERE e.Fach_ID = :value")
@NamedQuery(name = "MigrationDTOAnkreuzfloskeln.fach_id.multiple", query = "SELECT e FROM MigrationDTOAnkreuzfloskeln e WHERE e.Fach_ID IN :value")
@NamedQuery(name = "MigrationDTOAnkreuzfloskeln.istasv", query = "SELECT e FROM MigrationDTOAnkreuzfloskeln e WHERE e.IstASV = :value")
@NamedQuery(name = "MigrationDTOAnkreuzfloskeln.istasv.multiple", query = "SELECT e FROM MigrationDTOAnkreuzfloskeln e WHERE e.IstASV IN :value")
@NamedQuery(name = "MigrationDTOAnkreuzfloskeln.jahrgang", query = "SELECT e FROM MigrationDTOAnkreuzfloskeln e WHERE e.Jahrgang = :value")
@NamedQuery(name = "MigrationDTOAnkreuzfloskeln.jahrgang.multiple", query = "SELECT e FROM MigrationDTOAnkreuzfloskeln e WHERE e.Jahrgang IN :value")
@NamedQuery(name = "MigrationDTOAnkreuzfloskeln.gliederung", query = "SELECT e FROM MigrationDTOAnkreuzfloskeln e WHERE e.Gliederung = :value")
@NamedQuery(name = "MigrationDTOAnkreuzfloskeln.gliederung.multiple", query = "SELECT e FROM MigrationDTOAnkreuzfloskeln e WHERE e.Gliederung IN :value")
@NamedQuery(name = "MigrationDTOAnkreuzfloskeln.floskeltext", query = "SELECT e FROM MigrationDTOAnkreuzfloskeln e WHERE e.FloskelText = :value")
@NamedQuery(name = "MigrationDTOAnkreuzfloskeln.floskeltext.multiple", query = "SELECT e FROM MigrationDTOAnkreuzfloskeln e WHERE e.FloskelText IN :value")
@NamedQuery(name = "MigrationDTOAnkreuzfloskeln.sortierung", query = "SELECT e FROM MigrationDTOAnkreuzfloskeln e WHERE e.Sortierung = :value")
@NamedQuery(name = "MigrationDTOAnkreuzfloskeln.sortierung.multiple", query = "SELECT e FROM MigrationDTOAnkreuzfloskeln e WHERE e.Sortierung IN :value")
@NamedQuery(name = "MigrationDTOAnkreuzfloskeln.fachsortierung", query = "SELECT e FROM MigrationDTOAnkreuzfloskeln e WHERE e.FachSortierung = :value")
@NamedQuery(name = "MigrationDTOAnkreuzfloskeln.fachsortierung.multiple", query = "SELECT e FROM MigrationDTOAnkreuzfloskeln e WHERE e.FachSortierung IN :value")
@NamedQuery(name = "MigrationDTOAnkreuzfloskeln.abschnitt", query = "SELECT e FROM MigrationDTOAnkreuzfloskeln e WHERE e.Abschnitt = :value")
@NamedQuery(name = "MigrationDTOAnkreuzfloskeln.abschnitt.multiple", query = "SELECT e FROM MigrationDTOAnkreuzfloskeln e WHERE e.Abschnitt IN :value")
@NamedQuery(name = "MigrationDTOAnkreuzfloskeln.sichtbar", query = "SELECT e FROM MigrationDTOAnkreuzfloskeln e WHERE e.Sichtbar = :value")
@NamedQuery(name = "MigrationDTOAnkreuzfloskeln.sichtbar.multiple", query = "SELECT e FROM MigrationDTOAnkreuzfloskeln e WHERE e.Sichtbar IN :value")
@NamedQuery(name = "MigrationDTOAnkreuzfloskeln.aktiv", query = "SELECT e FROM MigrationDTOAnkreuzfloskeln e WHERE e.Aktiv = :value")
@NamedQuery(name = "MigrationDTOAnkreuzfloskeln.aktiv.multiple", query = "SELECT e FROM MigrationDTOAnkreuzfloskeln e WHERE e.Aktiv IN :value")
@NamedQuery(name = "MigrationDTOAnkreuzfloskeln.primaryKeyQuery", query = "SELECT e FROM MigrationDTOAnkreuzfloskeln e WHERE e.ID = ?1")
@NamedQuery(name = "MigrationDTOAnkreuzfloskeln.all.migration", query = "SELECT e FROM MigrationDTOAnkreuzfloskeln e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "SchulnrEigner", "Fach_ID", "IstASV", "Jahrgang", "Gliederung", "FloskelText", "Sortierung", "FachSortierung", "Abschnitt", "Sichtbar", "Aktiv"})
public final class MigrationDTOAnkreuzfloskeln {

	/** ID der Ankreuzfloskel */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/** FachID zu der die Ankreuzfloskel gehört, null für individuelle Ankreuzfloskeln bzw. siehe Spalte IstASV */
	@Column(name = "Fach_ID")
	@JsonProperty
	public Long Fach_ID;

	/** Gibt an, falls die Fach_ID null ist, ob es sich bei der Ankreuzfloskel um eine Floskel zum Arbeits- und Sozialverhalten handelt (1) oder nicht (0). */
	@Column(name = "IstASV")
	@JsonProperty
	public Integer IstASV;

	/** Jahrgang zu der die Ankreuzfloskel gehört */
	@Column(name = "Jahrgang")
	@JsonProperty
	public String Jahrgang;

	/** Schulgliederung zu der die Ankreuzkompetenz gehört (nur wichtig bei BK) */
	@Column(name = "Gliederung")
	@JsonProperty
	public String Gliederung;

	/** Text der Ankreuzfloskel */
	@Column(name = "FloskelText")
	@JsonProperty
	public String FloskelText;

	/** Sortierung der Ankreuzfloskel */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Sortierung des Faches der Ankreuzfloskel */
	@Column(name = "FachSortierung")
	@JsonProperty
	public Integer FachSortierung;

	/** Wird in welchen Abschnitten verwendet 1Hj 2HJ beide */
	@Column(name = "Abschnitt")
	@JsonProperty
	public Integer Abschnitt;

	/** Sichtbarkeit der Ankreuzfloskel */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** Gibt an ob die Ankreuzfloskel aktiv ist */
	@Column(name = "Aktiv")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Aktiv;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOAnkreuzfloskeln ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOAnkreuzfloskeln() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOAnkreuzfloskeln ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param SchulnrEigner   der Wert für das Attribut SchulnrEigner
	 * @param IstASV   der Wert für das Attribut IstASV
	 * @param Jahrgang   der Wert für das Attribut Jahrgang
	 * @param FloskelText   der Wert für das Attribut FloskelText
	 */
	public MigrationDTOAnkreuzfloskeln(final Long ID, final Integer SchulnrEigner, final Integer IstASV, final String Jahrgang, final String FloskelText) {
		if (ID == null) {
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (SchulnrEigner == null) {
			throw new NullPointerException("SchulnrEigner must not be null");
		}
		this.SchulnrEigner = SchulnrEigner;
		if (IstASV == null) {
			throw new NullPointerException("IstASV must not be null");
		}
		this.IstASV = IstASV;
		if (Jahrgang == null) {
			throw new NullPointerException("Jahrgang must not be null");
		}
		this.Jahrgang = Jahrgang;
		if (FloskelText == null) {
			throw new NullPointerException("FloskelText must not be null");
		}
		this.FloskelText = FloskelText;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOAnkreuzfloskeln other = (MigrationDTOAnkreuzfloskeln) obj;
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
		return "MigrationDTOAnkreuzfloskeln(ID=" + this.ID + ", SchulnrEigner=" + this.SchulnrEigner + ", Fach_ID=" + this.Fach_ID + ", IstASV=" + this.IstASV + ", Jahrgang=" + this.Jahrgang + ", Gliederung=" + this.Gliederung + ", FloskelText=" + this.FloskelText + ", Sortierung=" + this.Sortierung + ", FachSortierung=" + this.FachSortierung + ", Abschnitt=" + this.Abschnitt + ", Sichtbar=" + this.Sichtbar + ", Aktiv=" + this.Aktiv + ")";
	}

}
