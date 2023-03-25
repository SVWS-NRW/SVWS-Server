package de.svws_nrw.db.dto.current.schild.faecher;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.Boolean01Converter;


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
import de.svws_nrw.csv.converter.current.Boolean01ConverterSerializer;
import de.svws_nrw.csv.converter.current.Boolean01ConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Fachgruppen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Fachgruppen")
@NamedQuery(name="DTOFachgruppen.all", query="SELECT e FROM DTOFachgruppen e")
@NamedQuery(name="DTOFachgruppen.id", query="SELECT e FROM DTOFachgruppen e WHERE e.ID = :value")
@NamedQuery(name="DTOFachgruppen.id.multiple", query="SELECT e FROM DTOFachgruppen e WHERE e.ID IN :value")
@NamedQuery(name="DTOFachgruppen.fachbereich", query="SELECT e FROM DTOFachgruppen e WHERE e.Fachbereich = :value")
@NamedQuery(name="DTOFachgruppen.fachbereich.multiple", query="SELECT e FROM DTOFachgruppen e WHERE e.Fachbereich IN :value")
@NamedQuery(name="DTOFachgruppen.schildfgid", query="SELECT e FROM DTOFachgruppen e WHERE e.SchildFgID = :value")
@NamedQuery(name="DTOFachgruppen.schildfgid.multiple", query="SELECT e FROM DTOFachgruppen e WHERE e.SchildFgID IN :value")
@NamedQuery(name="DTOFachgruppen.fg_bezeichnung", query="SELECT e FROM DTOFachgruppen e WHERE e.FG_Bezeichnung = :value")
@NamedQuery(name="DTOFachgruppen.fg_bezeichnung.multiple", query="SELECT e FROM DTOFachgruppen e WHERE e.FG_Bezeichnung IN :value")
@NamedQuery(name="DTOFachgruppen.fg_kuerzel", query="SELECT e FROM DTOFachgruppen e WHERE e.FG_Kuerzel = :value")
@NamedQuery(name="DTOFachgruppen.fg_kuerzel.multiple", query="SELECT e FROM DTOFachgruppen e WHERE e.FG_Kuerzel IN :value")
@NamedQuery(name="DTOFachgruppen.schulformen", query="SELECT e FROM DTOFachgruppen e WHERE e.Schulformen = :value")
@NamedQuery(name="DTOFachgruppen.schulformen.multiple", query="SELECT e FROM DTOFachgruppen e WHERE e.Schulformen IN :value")
@NamedQuery(name="DTOFachgruppen.farber", query="SELECT e FROM DTOFachgruppen e WHERE e.FarbeR = :value")
@NamedQuery(name="DTOFachgruppen.farber.multiple", query="SELECT e FROM DTOFachgruppen e WHERE e.FarbeR IN :value")
@NamedQuery(name="DTOFachgruppen.farbeg", query="SELECT e FROM DTOFachgruppen e WHERE e.FarbeG = :value")
@NamedQuery(name="DTOFachgruppen.farbeg.multiple", query="SELECT e FROM DTOFachgruppen e WHERE e.FarbeG IN :value")
@NamedQuery(name="DTOFachgruppen.farbeb", query="SELECT e FROM DTOFachgruppen e WHERE e.FarbeB = :value")
@NamedQuery(name="DTOFachgruppen.farbeb.multiple", query="SELECT e FROM DTOFachgruppen e WHERE e.FarbeB IN :value")
@NamedQuery(name="DTOFachgruppen.sortierung", query="SELECT e FROM DTOFachgruppen e WHERE e.Sortierung = :value")
@NamedQuery(name="DTOFachgruppen.sortierung.multiple", query="SELECT e FROM DTOFachgruppen e WHERE e.Sortierung IN :value")
@NamedQuery(name="DTOFachgruppen.fuerzeugnis", query="SELECT e FROM DTOFachgruppen e WHERE e.FuerZeugnis = :value")
@NamedQuery(name="DTOFachgruppen.fuerzeugnis.multiple", query="SELECT e FROM DTOFachgruppen e WHERE e.FuerZeugnis IN :value")
@NamedQuery(name="DTOFachgruppen.gueltigvon", query="SELECT e FROM DTOFachgruppen e WHERE e.gueltigVon = :value")
@NamedQuery(name="DTOFachgruppen.gueltigvon.multiple", query="SELECT e FROM DTOFachgruppen e WHERE e.gueltigVon IN :value")
@NamedQuery(name="DTOFachgruppen.gueltigbis", query="SELECT e FROM DTOFachgruppen e WHERE e.gueltigBis = :value")
@NamedQuery(name="DTOFachgruppen.gueltigbis.multiple", query="SELECT e FROM DTOFachgruppen e WHERE e.gueltigBis IN :value")
@NamedQuery(name="DTOFachgruppen.primaryKeyQuery", query="SELECT e FROM DTOFachgruppen e WHERE e.ID = ?1")
@NamedQuery(name="DTOFachgruppen.all.migration", query="SELECT e FROM DTOFachgruppen e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Fachbereich","SchildFgID","FG_Bezeichnung","FG_Kuerzel","Schulformen","FarbeR","FarbeG","FarbeB","Sortierung","FuerZeugnis","gueltigVon","gueltigBis"})
public class DTOFachgruppen {

	/** ID des Fachgruppen-Core-Type, welcher auch ein Mapping zu den Fachgruppen von SchildNRW und Lupo bereitstellt */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Fachbereich (Nr) der Fachgruppe aus Lupo */
	@Column(name = "Fachbereich")
	@JsonProperty
	public Integer Fachbereich;

	/** Fachgruppen ID aus SchildNRW */
	@Column(name = "SchildFgID")
	@JsonProperty
	public Long SchildFgID;

	/** Bezeichnung der Fachgruppe */
	@Column(name = "FG_Bezeichnung")
	@JsonProperty
	public String FG_Bezeichnung;

	/** Kürzel der Fachgruppe */
	@Column(name = "FG_Kuerzel")
	@JsonProperty
	public String FG_Kuerzel;

	/** Gibt an in welchen Schulformen die Fachgruppe zulässig ist */
	@Column(name = "Schulformen")
	@JsonProperty
	public String Schulformen;

	/** Default-Fachgruppenfarbe (Rot-Wert) */
	@Column(name = "FarbeR")
	@JsonProperty
	public Integer FarbeR;

	/** Default-Fachgruppenfarbe (Grün-Wert) */
	@Column(name = "FarbeG")
	@JsonProperty
	public Integer FarbeG;

	/** Default-Fachgruppenfarbe (Blau-Wert) */
	@Column(name = "FarbeB")
	@JsonProperty
	public Integer FarbeB;

	/** Eine Standard-Sortierreihenfolge für die Fachgruppen */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Gibt an, ob die Fachgruppe für Unterteilungen auf Zeugnissen verwendet wird */
	@Column(name = "FuerZeugnis")
	@JsonProperty
	@Convert(converter=Boolean01Converter.class)
	@JsonSerialize(using=Boolean01ConverterSerializer.class)
	@JsonDeserialize(using=Boolean01ConverterDeserializer.class)
	public Boolean FuerZeugnis;

	/** Das Schuljahr, ab dem die Fachgruppe eingeführt wurde */
	@Column(name = "gueltigVon")
	@JsonProperty
	public Integer gueltigVon;

	/** Das Schuljahr, bis wann die Fachgruppe gültig ist */
	@Column(name = "gueltigBis")
	@JsonProperty
	public Integer gueltigBis;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOFachgruppen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOFachgruppen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOFachgruppen ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param FG_Bezeichnung   der Wert für das Attribut FG_Bezeichnung
	 * @param FuerZeugnis   der Wert für das Attribut FuerZeugnis
	 */
	public DTOFachgruppen(final Long ID, final String FG_Bezeichnung, final Boolean FuerZeugnis) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (FG_Bezeichnung == null) { 
			throw new NullPointerException("FG_Bezeichnung must not be null");
		}
		this.FG_Bezeichnung = FG_Bezeichnung;
		if (FuerZeugnis == null) { 
			throw new NullPointerException("FuerZeugnis must not be null");
		}
		this.FuerZeugnis = FuerZeugnis;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOFachgruppen other = (DTOFachgruppen) obj;
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
		return "DTOFachgruppen(ID=" + this.ID + ", Fachbereich=" + this.Fachbereich + ", SchildFgID=" + this.SchildFgID + ", FG_Bezeichnung=" + this.FG_Bezeichnung + ", FG_Kuerzel=" + this.FG_Kuerzel + ", Schulformen=" + this.Schulformen + ", FarbeR=" + this.FarbeR + ", FarbeG=" + this.FarbeG + ", FarbeB=" + this.FarbeB + ", Sortierung=" + this.Sortierung + ", FuerZeugnis=" + this.FuerZeugnis + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ")";
	}

}