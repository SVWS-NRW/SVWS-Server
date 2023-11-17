package de.svws_nrw.db.dto.migration.coretypes;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.migration.MigrationBoolean01Converter;


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
import de.svws_nrw.csv.converter.migration.MigrationBoolean01ConverterSerializer;
import de.svws_nrw.csv.converter.migration.MigrationBoolean01ConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Noten.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Noten")
@NamedQuery(name = "MigrationDTONote.all", query = "SELECT e FROM MigrationDTONote e")
@NamedQuery(name = "MigrationDTONote.id", query = "SELECT e FROM MigrationDTONote e WHERE e.ID = :value")
@NamedQuery(name = "MigrationDTONote.id.multiple", query = "SELECT e FROM MigrationDTONote e WHERE e.ID IN :value")
@NamedQuery(name = "MigrationDTONote.kuerzel", query = "SELECT e FROM MigrationDTONote e WHERE e.Kuerzel = :value")
@NamedQuery(name = "MigrationDTONote.kuerzel.multiple", query = "SELECT e FROM MigrationDTONote e WHERE e.Kuerzel IN :value")
@NamedQuery(name = "MigrationDTONote.isttendenznote", query = "SELECT e FROM MigrationDTONote e WHERE e.IstTendenznote = :value")
@NamedQuery(name = "MigrationDTONote.isttendenznote.multiple", query = "SELECT e FROM MigrationDTONote e WHERE e.IstTendenznote IN :value")
@NamedQuery(name = "MigrationDTONote.text", query = "SELECT e FROM MigrationDTONote e WHERE e.Text = :value")
@NamedQuery(name = "MigrationDTONote.text.multiple", query = "SELECT e FROM MigrationDTONote e WHERE e.Text IN :value")
@NamedQuery(name = "MigrationDTONote.aufzeugnis", query = "SELECT e FROM MigrationDTONote e WHERE e.AufZeugnis = :value")
@NamedQuery(name = "MigrationDTONote.aufzeugnis.multiple", query = "SELECT e FROM MigrationDTONote e WHERE e.AufZeugnis IN :value")
@NamedQuery(name = "MigrationDTONote.notenpunkte", query = "SELECT e FROM MigrationDTONote e WHERE e.Notenpunkte = :value")
@NamedQuery(name = "MigrationDTONote.notenpunkte.multiple", query = "SELECT e FROM MigrationDTONote e WHERE e.Notenpunkte IN :value")
@NamedQuery(name = "MigrationDTONote.textlaufbahnsii", query = "SELECT e FROM MigrationDTONote e WHERE e.TextLaufbahnSII = :value")
@NamedQuery(name = "MigrationDTONote.textlaufbahnsii.multiple", query = "SELECT e FROM MigrationDTONote e WHERE e.TextLaufbahnSII IN :value")
@NamedQuery(name = "MigrationDTONote.auflaufbahnsii", query = "SELECT e FROM MigrationDTONote e WHERE e.AufLaufbahnSII = :value")
@NamedQuery(name = "MigrationDTONote.auflaufbahnsii.multiple", query = "SELECT e FROM MigrationDTONote e WHERE e.AufLaufbahnSII IN :value")
@NamedQuery(name = "MigrationDTONote.sortierung", query = "SELECT e FROM MigrationDTONote e WHERE e.Sortierung = :value")
@NamedQuery(name = "MigrationDTONote.sortierung.multiple", query = "SELECT e FROM MigrationDTONote e WHERE e.Sortierung IN :value")
@NamedQuery(name = "MigrationDTONote.gueltigvon", query = "SELECT e FROM MigrationDTONote e WHERE e.gueltigVon = :value")
@NamedQuery(name = "MigrationDTONote.gueltigvon.multiple", query = "SELECT e FROM MigrationDTONote e WHERE e.gueltigVon IN :value")
@NamedQuery(name = "MigrationDTONote.gueltigbis", query = "SELECT e FROM MigrationDTONote e WHERE e.gueltigBis = :value")
@NamedQuery(name = "MigrationDTONote.gueltigbis.multiple", query = "SELECT e FROM MigrationDTONote e WHERE e.gueltigBis IN :value")
@NamedQuery(name = "MigrationDTONote.primaryKeyQuery", query = "SELECT e FROM MigrationDTONote e WHERE e.ID = ?1")
@NamedQuery(name = "MigrationDTONote.primaryKeyQuery.multiple", query = "SELECT e FROM MigrationDTONote e WHERE e.ID IN :value")
@NamedQuery(name = "MigrationDTONote.all.migration", query = "SELECT e FROM MigrationDTONote e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Kuerzel", "IstTendenznote", "Text", "AufZeugnis", "Notenpunkte", "TextLaufbahnSII", "AufLaufbahnSII", "Sortierung", "gueltigVon", "gueltigBis"})
public final class MigrationDTONote {

	/** ID der Noten */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Das Kürzel der Note */
	@Column(name = "Kuerzel")
	@JsonProperty
	public String Kuerzel;

	/** Gibt an, ob es sich um eine Tendenznote (plus) oder (minus) handelt */
	@Column(name = "IstTendenznote")
	@JsonProperty
	@Convert(converter = MigrationBoolean01Converter.class)
	@JsonSerialize(using = MigrationBoolean01ConverterSerializer.class)
	@JsonDeserialize(using = MigrationBoolean01ConverterDeserializer.class)
	public Boolean IstTendenznote;

	/** Die Bezeichnung der Note */
	@Column(name = "Text")
	@JsonProperty
	public String Text;

	/** Gibt an, ob die Note auf einem Zeugnis als erteilte Note gedruckt wird oder nicht. */
	@Column(name = "AufZeugnis")
	@JsonProperty
	@Convert(converter = MigrationBoolean01Converter.class)
	@JsonSerialize(using = MigrationBoolean01ConverterSerializer.class)
	@JsonDeserialize(using = MigrationBoolean01ConverterDeserializer.class)
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
	@Convert(converter = MigrationBoolean01Converter.class)
	@JsonSerialize(using = MigrationBoolean01ConverterSerializer.class)
	@JsonDeserialize(using = MigrationBoolean01ConverterDeserializer.class)
	public Boolean AufLaufbahnSII;

	/** Eine Default-Sortierung der Noten */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Der Datensatz ist gültig ab dem Schuljahr */
	@Column(name = "gueltigVon")
	@JsonProperty
	public Integer gueltigVon;

	/** Der Datensatz ist gültig bis zu dem Schuljahr */
	@Column(name = "gueltigBis")
	@JsonProperty
	public Integer gueltigBis;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTONote ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTONote() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTONote ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Kuerzel   der Wert für das Attribut Kuerzel
	 * @param IstTendenznote   der Wert für das Attribut IstTendenznote
	 * @param AufZeugnis   der Wert für das Attribut AufZeugnis
	 * @param AufLaufbahnSII   der Wert für das Attribut AufLaufbahnSII
	 * @param Sortierung   der Wert für das Attribut Sortierung
	 */
	public MigrationDTONote(final Long ID, final String Kuerzel, final Boolean IstTendenznote, final Boolean AufZeugnis, final Boolean AufLaufbahnSII, final Integer Sortierung) {
		if (ID == null) {
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Kuerzel == null) {
			throw new NullPointerException("Kuerzel must not be null");
		}
		this.Kuerzel = Kuerzel;
		if (IstTendenznote == null) {
			throw new NullPointerException("IstTendenznote must not be null");
		}
		this.IstTendenznote = IstTendenznote;
		if (AufZeugnis == null) {
			throw new NullPointerException("AufZeugnis must not be null");
		}
		this.AufZeugnis = AufZeugnis;
		if (AufLaufbahnSII == null) {
			throw new NullPointerException("AufLaufbahnSII must not be null");
		}
		this.AufLaufbahnSII = AufLaufbahnSII;
		if (Sortierung == null) {
			throw new NullPointerException("Sortierung must not be null");
		}
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
		MigrationDTONote other = (MigrationDTONote) obj;
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
		return "MigrationDTONote(ID=" + this.ID + ", Kuerzel=" + this.Kuerzel + ", IstTendenznote=" + this.IstTendenznote + ", Text=" + this.Text + ", AufZeugnis=" + this.AufZeugnis + ", Notenpunkte=" + this.Notenpunkte + ", TextLaufbahnSII=" + this.TextLaufbahnSII + ", AufLaufbahnSII=" + this.AufLaufbahnSII + ", Sortierung=" + this.Sortierung + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ")";
	}

}
