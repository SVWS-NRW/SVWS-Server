package de.nrw.schule.svws.db.dto.rev8.coretypes;

import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.converter.current.Boolean01Converter;


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
import de.nrw.schule.svws.csv.converter.current.Boolean01ConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.Boolean01ConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Noten.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Noten")
@NamedQuery(name="Rev8DTONote.all", query="SELECT e FROM Rev8DTONote e")
@NamedQuery(name="Rev8DTONote.id", query="SELECT e FROM Rev8DTONote e WHERE e.ID = :value")
@NamedQuery(name="Rev8DTONote.id.multiple", query="SELECT e FROM Rev8DTONote e WHERE e.ID IN :value")
@NamedQuery(name="Rev8DTONote.kuerzel", query="SELECT e FROM Rev8DTONote e WHERE e.Kuerzel = :value")
@NamedQuery(name="Rev8DTONote.kuerzel.multiple", query="SELECT e FROM Rev8DTONote e WHERE e.Kuerzel IN :value")
@NamedQuery(name="Rev8DTONote.isttendenznote", query="SELECT e FROM Rev8DTONote e WHERE e.IstTendenznote = :value")
@NamedQuery(name="Rev8DTONote.isttendenznote.multiple", query="SELECT e FROM Rev8DTONote e WHERE e.IstTendenznote IN :value")
@NamedQuery(name="Rev8DTONote.text", query="SELECT e FROM Rev8DTONote e WHERE e.Text = :value")
@NamedQuery(name="Rev8DTONote.text.multiple", query="SELECT e FROM Rev8DTONote e WHERE e.Text IN :value")
@NamedQuery(name="Rev8DTONote.aufzeugnis", query="SELECT e FROM Rev8DTONote e WHERE e.AufZeugnis = :value")
@NamedQuery(name="Rev8DTONote.aufzeugnis.multiple", query="SELECT e FROM Rev8DTONote e WHERE e.AufZeugnis IN :value")
@NamedQuery(name="Rev8DTONote.notenpunkte", query="SELECT e FROM Rev8DTONote e WHERE e.Notenpunkte = :value")
@NamedQuery(name="Rev8DTONote.notenpunkte.multiple", query="SELECT e FROM Rev8DTONote e WHERE e.Notenpunkte IN :value")
@NamedQuery(name="Rev8DTONote.textlaufbahnsii", query="SELECT e FROM Rev8DTONote e WHERE e.TextLaufbahnSII = :value")
@NamedQuery(name="Rev8DTONote.textlaufbahnsii.multiple", query="SELECT e FROM Rev8DTONote e WHERE e.TextLaufbahnSII IN :value")
@NamedQuery(name="Rev8DTONote.auflaufbahnsii", query="SELECT e FROM Rev8DTONote e WHERE e.AufLaufbahnSII = :value")
@NamedQuery(name="Rev8DTONote.auflaufbahnsii.multiple", query="SELECT e FROM Rev8DTONote e WHERE e.AufLaufbahnSII IN :value")
@NamedQuery(name="Rev8DTONote.sortierung", query="SELECT e FROM Rev8DTONote e WHERE e.Sortierung = :value")
@NamedQuery(name="Rev8DTONote.sortierung.multiple", query="SELECT e FROM Rev8DTONote e WHERE e.Sortierung IN :value")
@NamedQuery(name="Rev8DTONote.gueltigvon", query="SELECT e FROM Rev8DTONote e WHERE e.gueltigVon = :value")
@NamedQuery(name="Rev8DTONote.gueltigvon.multiple", query="SELECT e FROM Rev8DTONote e WHERE e.gueltigVon IN :value")
@NamedQuery(name="Rev8DTONote.gueltigbis", query="SELECT e FROM Rev8DTONote e WHERE e.gueltigBis = :value")
@NamedQuery(name="Rev8DTONote.gueltigbis.multiple", query="SELECT e FROM Rev8DTONote e WHERE e.gueltigBis IN :value")
@NamedQuery(name="Rev8DTONote.primaryKeyQuery", query="SELECT e FROM Rev8DTONote e WHERE e.ID = ?1")
@NamedQuery(name="Rev8DTONote.all.migration", query="SELECT e FROM Rev8DTONote e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Kuerzel","IstTendenznote","Text","AufZeugnis","Notenpunkte","TextLaufbahnSII","AufLaufbahnSII","Sortierung","gueltigVon","gueltigBis"})
public class Rev8DTONote {

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
	@Convert(converter=Boolean01Converter.class)
	@JsonSerialize(using=Boolean01ConverterSerializer.class)
	@JsonDeserialize(using=Boolean01ConverterDeserializer.class)
	public Boolean IstTendenznote;

	/** Die Bezeichnung der Note */
	@Column(name = "Text")
	@JsonProperty
	public String Text;

	/** Gibt an, ob die Note auf einem Zeugnis als erteilte Note gedruckt wird oder nicht. */
	@Column(name = "AufZeugnis")
	@JsonProperty
	@Convert(converter=Boolean01Converter.class)
	@JsonSerialize(using=Boolean01ConverterSerializer.class)
	@JsonDeserialize(using=Boolean01ConverterDeserializer.class)
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
	@Convert(converter=Boolean01Converter.class)
	@JsonSerialize(using=Boolean01ConverterSerializer.class)
	@JsonDeserialize(using=Boolean01ConverterDeserializer.class)
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
	 * Erstellt ein neues Objekt der Klasse Rev8DTONote ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev8DTONote() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTONote ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Kuerzel   der Wert für das Attribut Kuerzel
	 * @param IstTendenznote   der Wert für das Attribut IstTendenznote
	 * @param AufZeugnis   der Wert für das Attribut AufZeugnis
	 * @param AufLaufbahnSII   der Wert für das Attribut AufLaufbahnSII
	 * @param Sortierung   der Wert für das Attribut Sortierung
	 */
	public Rev8DTONote(final Long ID, final String Kuerzel, final Boolean IstTendenznote, final Boolean AufZeugnis, final Boolean AufLaufbahnSII, final Integer Sortierung) {
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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev8DTONote other = (Rev8DTONote) obj;
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
		return "Rev8DTONote(ID=" + this.ID + ", Kuerzel=" + this.Kuerzel + ", IstTendenznote=" + this.IstTendenznote + ", Text=" + this.Text + ", AufZeugnis=" + this.AufZeugnis + ", Notenpunkte=" + this.Notenpunkte + ", TextLaufbahnSII=" + this.TextLaufbahnSII + ", AufLaufbahnSII=" + this.AufLaufbahnSII + ", Sortierung=" + this.Sortierung + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ")";
	}

}