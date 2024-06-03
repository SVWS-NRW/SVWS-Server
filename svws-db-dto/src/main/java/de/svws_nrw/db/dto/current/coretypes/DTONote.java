package de.svws_nrw.db.dto.current.coretypes;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.Boolean01Converter;


import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.svws_nrw.csv.converter.current.Boolean01ConverterSerializer;
import de.svws_nrw.csv.converter.current.Boolean01ConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Noten.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Noten")
@JsonPropertyOrder({"ID", "Kuerzel", "IstTendenznote", "Text", "AufZeugnis", "Notenpunkte", "TextLaufbahnSII", "AufLaufbahnSII", "Sortierung", "gueltigVon", "gueltigBis"})
public final class DTONote {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTONote e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTONote e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTONote e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTONote e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTONote e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTONote e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Kuerzel */
	public static final String QUERY_BY_KUERZEL = "SELECT e FROM DTONote e WHERE e.Kuerzel = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Kuerzel */
	public static final String QUERY_LIST_BY_KUERZEL = "SELECT e FROM DTONote e WHERE e.Kuerzel IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes IstTendenznote */
	public static final String QUERY_BY_ISTTENDENZNOTE = "SELECT e FROM DTONote e WHERE e.IstTendenznote = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes IstTendenznote */
	public static final String QUERY_LIST_BY_ISTTENDENZNOTE = "SELECT e FROM DTONote e WHERE e.IstTendenznote IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Text */
	public static final String QUERY_BY_TEXT = "SELECT e FROM DTONote e WHERE e.Text = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Text */
	public static final String QUERY_LIST_BY_TEXT = "SELECT e FROM DTONote e WHERE e.Text IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AufZeugnis */
	public static final String QUERY_BY_AUFZEUGNIS = "SELECT e FROM DTONote e WHERE e.AufZeugnis = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AufZeugnis */
	public static final String QUERY_LIST_BY_AUFZEUGNIS = "SELECT e FROM DTONote e WHERE e.AufZeugnis IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Notenpunkte */
	public static final String QUERY_BY_NOTENPUNKTE = "SELECT e FROM DTONote e WHERE e.Notenpunkte = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Notenpunkte */
	public static final String QUERY_LIST_BY_NOTENPUNKTE = "SELECT e FROM DTONote e WHERE e.Notenpunkte IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes TextLaufbahnSII */
	public static final String QUERY_BY_TEXTLAUFBAHNSII = "SELECT e FROM DTONote e WHERE e.TextLaufbahnSII = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes TextLaufbahnSII */
	public static final String QUERY_LIST_BY_TEXTLAUFBAHNSII = "SELECT e FROM DTONote e WHERE e.TextLaufbahnSII IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AufLaufbahnSII */
	public static final String QUERY_BY_AUFLAUFBAHNSII = "SELECT e FROM DTONote e WHERE e.AufLaufbahnSII = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AufLaufbahnSII */
	public static final String QUERY_LIST_BY_AUFLAUFBAHNSII = "SELECT e FROM DTONote e WHERE e.AufLaufbahnSII IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Sortierung */
	public static final String QUERY_BY_SORTIERUNG = "SELECT e FROM DTONote e WHERE e.Sortierung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Sortierung */
	public static final String QUERY_LIST_BY_SORTIERUNG = "SELECT e FROM DTONote e WHERE e.Sortierung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes gueltigVon */
	public static final String QUERY_BY_GUELTIGVON = "SELECT e FROM DTONote e WHERE e.gueltigVon = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes gueltigVon */
	public static final String QUERY_LIST_BY_GUELTIGVON = "SELECT e FROM DTONote e WHERE e.gueltigVon IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes gueltigBis */
	public static final String QUERY_BY_GUELTIGBIS = "SELECT e FROM DTONote e WHERE e.gueltigBis = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes gueltigBis */
	public static final String QUERY_LIST_BY_GUELTIGBIS = "SELECT e FROM DTONote e WHERE e.gueltigBis IN ?1";

	/** ID der Noten */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Das Kürzel der Note */
	@Column(name = "Kuerzel")
	@JsonProperty
	public String Kuerzel;

	/** Gibt an, ob es sich um eine Tendenznote (plus) oder (minus) handelt */
	@Column(name = "IstTendenznote")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean IstTendenznote;

	/** Die Bezeichnung der Note */
	@Column(name = "Text")
	@JsonProperty
	public String Text;

	/** Gibt an, ob die Note auf einem Zeugnis als erteilte Note gedruckt wird oder nicht. */
	@Column(name = "AufZeugnis")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean AufZeugnis;

	/** Die Notenpunkte der Note in der Sekundarstufe II */
	@Column(name = "Notenpunkte")
	@JsonProperty
	public Integer Notenpunkte;

	/** Die Bezeichnung der Note in der Sekundarstufe II, die für die Laufbahn zum Abitur verwendet wird */
	@Column(name = "TextLaufbahnSII")
	@JsonProperty
	public String TextLaufbahnSII;

	/** Gibt an, ob die Note bei der Laufbahn in der Sekundarstufe II gedruckt wird oder nicht. */
	@Column(name = "AufLaufbahnSII")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean AufLaufbahnSII;

	/** Eine Default-Sortierung der Noten */
	@Column(name = "Sortierung")
	@JsonProperty
	public int Sortierung;

	/** Der Datensatz ist gültig ab dem Schuljahr */
	@Column(name = "gueltigVon")
	@JsonProperty
	public Integer gueltigVon;

	/** Der Datensatz ist gültig bis zu dem Schuljahr */
	@Column(name = "gueltigBis")
	@JsonProperty
	public Integer gueltigBis;

	/**
	 * Erstellt ein neues Objekt der Klasse DTONote ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTONote() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTONote ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Kuerzel   der Wert für das Attribut Kuerzel
	 * @param IstTendenznote   der Wert für das Attribut IstTendenznote
	 * @param AufZeugnis   der Wert für das Attribut AufZeugnis
	 * @param AufLaufbahnSII   der Wert für das Attribut AufLaufbahnSII
	 * @param Sortierung   der Wert für das Attribut Sortierung
	 */
	public DTONote(final long ID, final String Kuerzel, final Boolean IstTendenznote, final Boolean AufZeugnis, final Boolean AufLaufbahnSII, final int Sortierung) {
		this.ID = ID;
		if (Kuerzel == null) {
			throw new NullPointerException("Kuerzel must not be null");
		}
		this.Kuerzel = Kuerzel;
		this.IstTendenznote = IstTendenznote;
		this.AufZeugnis = AufZeugnis;
		this.AufLaufbahnSII = AufLaufbahnSII;
		this.Sortierung = Sortierung;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTONote other = (DTONote) obj;
		return ID == other.ID;
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
		return "DTONote(ID=" + this.ID + ", Kuerzel=" + this.Kuerzel + ", IstTendenznote=" + this.IstTendenznote + ", Text=" + this.Text + ", AufZeugnis=" + this.AufZeugnis + ", Notenpunkte=" + this.Notenpunkte + ", TextLaufbahnSII=" + this.TextLaufbahnSII + ", AufLaufbahnSII=" + this.AufLaufbahnSII + ", Sortierung=" + this.Sortierung + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ")";
	}

}
