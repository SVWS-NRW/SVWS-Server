package de.nrw.schule.svws.db.dto.rev8.views.schildintern;

import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.converter.current.BooleanPlusMinusDefaultPlusConverter;


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
import de.nrw.schule.svws.csv.converter.current.BooleanPlusMinusDefaultPlusConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.BooleanPlusMinusDefaultPlusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbank-View Schildintern_Fachgruppen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Schildintern_Fachgruppen")
@NamedQuery(name="Rev8DTOSchildInternFachgruppen.all", query="SELECT e FROM Rev8DTOSchildInternFachgruppen e")
@NamedQuery(name="Rev8DTOSchildInternFachgruppen.fg_id", query="SELECT e FROM Rev8DTOSchildInternFachgruppen e WHERE e.FG_ID = :value")
@NamedQuery(name="Rev8DTOSchildInternFachgruppen.fg_id.multiple", query="SELECT e FROM Rev8DTOSchildInternFachgruppen e WHERE e.FG_ID IN :value")
@NamedQuery(name="Rev8DTOSchildInternFachgruppen.fg_sf", query="SELECT e FROM Rev8DTOSchildInternFachgruppen e WHERE e.FG_SF = :value")
@NamedQuery(name="Rev8DTOSchildInternFachgruppen.fg_sf.multiple", query="SELECT e FROM Rev8DTOSchildInternFachgruppen e WHERE e.FG_SF IN :value")
@NamedQuery(name="Rev8DTOSchildInternFachgruppen.fg_bezeichnung", query="SELECT e FROM Rev8DTOSchildInternFachgruppen e WHERE e.FG_Bezeichnung = :value")
@NamedQuery(name="Rev8DTOSchildInternFachgruppen.fg_bezeichnung.multiple", query="SELECT e FROM Rev8DTOSchildInternFachgruppen e WHERE e.FG_Bezeichnung IN :value")
@NamedQuery(name="Rev8DTOSchildInternFachgruppen.fg_farbe", query="SELECT e FROM Rev8DTOSchildInternFachgruppen e WHERE e.FG_Farbe = :value")
@NamedQuery(name="Rev8DTOSchildInternFachgruppen.fg_farbe.multiple", query="SELECT e FROM Rev8DTOSchildInternFachgruppen e WHERE e.FG_Farbe IN :value")
@NamedQuery(name="Rev8DTOSchildInternFachgruppen.fg_sortierung", query="SELECT e FROM Rev8DTOSchildInternFachgruppen e WHERE e.FG_Sortierung = :value")
@NamedQuery(name="Rev8DTOSchildInternFachgruppen.fg_sortierung.multiple", query="SELECT e FROM Rev8DTOSchildInternFachgruppen e WHERE e.FG_Sortierung IN :value")
@NamedQuery(name="Rev8DTOSchildInternFachgruppen.fg_kuerzel", query="SELECT e FROM Rev8DTOSchildInternFachgruppen e WHERE e.FG_Kuerzel = :value")
@NamedQuery(name="Rev8DTOSchildInternFachgruppen.fg_kuerzel.multiple", query="SELECT e FROM Rev8DTOSchildInternFachgruppen e WHERE e.FG_Kuerzel IN :value")
@NamedQuery(name="Rev8DTOSchildInternFachgruppen.fg_zeugnis", query="SELECT e FROM Rev8DTOSchildInternFachgruppen e WHERE e.FG_Zeugnis = :value")
@NamedQuery(name="Rev8DTOSchildInternFachgruppen.fg_zeugnis.multiple", query="SELECT e FROM Rev8DTOSchildInternFachgruppen e WHERE e.FG_Zeugnis IN :value")
@NamedQuery(name="Rev8DTOSchildInternFachgruppen.gueltigvon", query="SELECT e FROM Rev8DTOSchildInternFachgruppen e WHERE e.gueltigVon = :value")
@NamedQuery(name="Rev8DTOSchildInternFachgruppen.gueltigvon.multiple", query="SELECT e FROM Rev8DTOSchildInternFachgruppen e WHERE e.gueltigVon IN :value")
@NamedQuery(name="Rev8DTOSchildInternFachgruppen.gueltigbis", query="SELECT e FROM Rev8DTOSchildInternFachgruppen e WHERE e.gueltigBis = :value")
@NamedQuery(name="Rev8DTOSchildInternFachgruppen.gueltigbis.multiple", query="SELECT e FROM Rev8DTOSchildInternFachgruppen e WHERE e.gueltigBis IN :value")
@NamedQuery(name="Rev8DTOSchildInternFachgruppen.primaryKeyQuery", query="SELECT e FROM Rev8DTOSchildInternFachgruppen e WHERE e.FG_ID = ?1")
@JsonPropertyOrder({"FG_ID","FG_SF","FG_Bezeichnung","FG_Farbe","FG_Sortierung","FG_Kuerzel","FG_Zeugnis","gueltigVon","gueltigBis"})
public class Rev8DTOSchildInternFachgruppen {

	/** Schild-ID der Fachgruppe */
	@Id
	@Column(name = "FG_ID")
	@JsonProperty
	public Long FG_ID;

	/** Schulformen der Fachgruppe */
	@Column(name = "FG_SF")
	@JsonProperty
	public String FG_SF;

	/** Bezeichnung der Fachgruppe */
	@Column(name = "FG_Bezeichnung")
	@JsonProperty
	public String FG_Bezeichnung;

	/** Farbe der Fachgruppe (Deprecated) */
	@Column(name = "FG_Farbe")
	@JsonProperty
	public Long FG_Farbe;

	/** Standard-Sortierung der Fachgruppe (Deprecated) */
	@Column(name = "FG_Sortierung")
	@JsonProperty
	public Integer FG_Sortierung;

	/** Kürzel der Fachgruppe */
	@Column(name = "FG_Kuerzel")
	@JsonProperty
	public String FG_Kuerzel;

	/** Gibt an, ob die Fachgruppe für die Unterteilung auf Zeugnissen genutzt wird oder nicht */
	@Column(name = "FG_Zeugnis")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean FG_Zeugnis;

	/** Gibt das Schuljahr an, ab dem die Fachgruppe verwendet werden kann oder null, falls es keine Einschränkung gibt */
	@Column(name = "gueltigVon")
	@JsonProperty
	public Integer gueltigVon;

	/** Gibt das Schuljahr an, bis zu welchem die Fachgruppe verwendet werden kann oder null, falls es keine Einschränkung gibt */
	@Column(name = "gueltigBis")
	@JsonProperty
	public Integer gueltigBis;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOSchildInternFachgruppen ohne eine Initialisierung der Attribute.
	 */
	private Rev8DTOSchildInternFachgruppen() {
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev8DTOSchildInternFachgruppen other = (Rev8DTOSchildInternFachgruppen) obj;
		if (FG_ID == null) {
			if (other.FG_ID != null)
				return false;
		} else if (!FG_ID.equals(other.FG_ID))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((FG_ID == null) ? 0 : FG_ID.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "Rev8DTOSchildInternFachgruppen(FG_ID=" + this.FG_ID + ", FG_SF=" + this.FG_SF + ", FG_Bezeichnung=" + this.FG_Bezeichnung + ", FG_Farbe=" + this.FG_Farbe + ", FG_Sortierung=" + this.FG_Sortierung + ", FG_Kuerzel=" + this.FG_Kuerzel + ", FG_Zeugnis=" + this.FG_Zeugnis + ", gueltigVon=" + this.gueltigVon + ", gueltigBis=" + this.gueltigBis + ")";
	}

}