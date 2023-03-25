package de.svws_nrw.db.dto.migration.schild.schule;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle EigeneSchule_Jahrgaenge.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "EigeneSchule_Jahrgaenge")
@NamedQuery(name="MigrationDTOJahrgang.all", query="SELECT e FROM MigrationDTOJahrgang e")
@NamedQuery(name="MigrationDTOJahrgang.id", query="SELECT e FROM MigrationDTOJahrgang e WHERE e.ID = :value")
@NamedQuery(name="MigrationDTOJahrgang.id.multiple", query="SELECT e FROM MigrationDTOJahrgang e WHERE e.ID IN :value")
@NamedQuery(name="MigrationDTOJahrgang.internkrz", query="SELECT e FROM MigrationDTOJahrgang e WHERE e.InternKrz = :value")
@NamedQuery(name="MigrationDTOJahrgang.internkrz.multiple", query="SELECT e FROM MigrationDTOJahrgang e WHERE e.InternKrz IN :value")
@NamedQuery(name="MigrationDTOJahrgang.gueltigvon", query="SELECT e FROM MigrationDTOJahrgang e WHERE e.GueltigVon = :value")
@NamedQuery(name="MigrationDTOJahrgang.gueltigvon.multiple", query="SELECT e FROM MigrationDTOJahrgang e WHERE e.GueltigVon IN :value")
@NamedQuery(name="MigrationDTOJahrgang.gueltigbis", query="SELECT e FROM MigrationDTOJahrgang e WHERE e.GueltigBis = :value")
@NamedQuery(name="MigrationDTOJahrgang.gueltigbis.multiple", query="SELECT e FROM MigrationDTOJahrgang e WHERE e.GueltigBis IN :value")
@NamedQuery(name="MigrationDTOJahrgang.asdjahrgang", query="SELECT e FROM MigrationDTOJahrgang e WHERE e.ASDJahrgang = :value")
@NamedQuery(name="MigrationDTOJahrgang.asdjahrgang.multiple", query="SELECT e FROM MigrationDTOJahrgang e WHERE e.ASDJahrgang IN :value")
@NamedQuery(name="MigrationDTOJahrgang.asdbezeichnung", query="SELECT e FROM MigrationDTOJahrgang e WHERE e.ASDBezeichnung = :value")
@NamedQuery(name="MigrationDTOJahrgang.asdbezeichnung.multiple", query="SELECT e FROM MigrationDTOJahrgang e WHERE e.ASDBezeichnung IN :value")
@NamedQuery(name="MigrationDTOJahrgang.sichtbar", query="SELECT e FROM MigrationDTOJahrgang e WHERE e.Sichtbar = :value")
@NamedQuery(name="MigrationDTOJahrgang.sichtbar.multiple", query="SELECT e FROM MigrationDTOJahrgang e WHERE e.Sichtbar IN :value")
@NamedQuery(name="MigrationDTOJahrgang.sortierung", query="SELECT e FROM MigrationDTOJahrgang e WHERE e.Sortierung = :value")
@NamedQuery(name="MigrationDTOJahrgang.sortierung.multiple", query="SELECT e FROM MigrationDTOJahrgang e WHERE e.Sortierung IN :value")
@NamedQuery(name="MigrationDTOJahrgang.istchronologisch", query="SELECT e FROM MigrationDTOJahrgang e WHERE e.IstChronologisch = :value")
@NamedQuery(name="MigrationDTOJahrgang.istchronologisch.multiple", query="SELECT e FROM MigrationDTOJahrgang e WHERE e.IstChronologisch IN :value")
@NamedQuery(name="MigrationDTOJahrgang.kurzbezeichnung", query="SELECT e FROM MigrationDTOJahrgang e WHERE e.Kurzbezeichnung = :value")
@NamedQuery(name="MigrationDTOJahrgang.kurzbezeichnung.multiple", query="SELECT e FROM MigrationDTOJahrgang e WHERE e.Kurzbezeichnung IN :value")
@NamedQuery(name="MigrationDTOJahrgang.sekundarstufe", query="SELECT e FROM MigrationDTOJahrgang e WHERE e.Sekundarstufe = :value")
@NamedQuery(name="MigrationDTOJahrgang.sekundarstufe.multiple", query="SELECT e FROM MigrationDTOJahrgang e WHERE e.Sekundarstufe IN :value")
@NamedQuery(name="MigrationDTOJahrgang.gliederung", query="SELECT e FROM MigrationDTOJahrgang e WHERE e.Gliederung = :value")
@NamedQuery(name="MigrationDTOJahrgang.gliederung.multiple", query="SELECT e FROM MigrationDTOJahrgang e WHERE e.Gliederung IN :value")
@NamedQuery(name="MigrationDTOJahrgang.anzahlrestabschnitte", query="SELECT e FROM MigrationDTOJahrgang e WHERE e.AnzahlRestabschnitte = :value")
@NamedQuery(name="MigrationDTOJahrgang.anzahlrestabschnitte.multiple", query="SELECT e FROM MigrationDTOJahrgang e WHERE e.AnzahlRestabschnitte IN :value")
@NamedQuery(name="MigrationDTOJahrgang.folgejahrgang_id", query="SELECT e FROM MigrationDTOJahrgang e WHERE e.Folgejahrgang_ID = :value")
@NamedQuery(name="MigrationDTOJahrgang.folgejahrgang_id.multiple", query="SELECT e FROM MigrationDTOJahrgang e WHERE e.Folgejahrgang_ID IN :value")
@NamedQuery(name="MigrationDTOJahrgang.schulnreigner", query="SELECT e FROM MigrationDTOJahrgang e WHERE e.SchulnrEigner = :value")
@NamedQuery(name="MigrationDTOJahrgang.schulnreigner.multiple", query="SELECT e FROM MigrationDTOJahrgang e WHERE e.SchulnrEigner IN :value")
@NamedQuery(name="MigrationDTOJahrgang.primaryKeyQuery", query="SELECT e FROM MigrationDTOJahrgang e WHERE e.ID = ?1")
@NamedQuery(name="MigrationDTOJahrgang.all.migration", query="SELECT e FROM MigrationDTOJahrgang e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","InternKrz","GueltigVon","GueltigBis","ASDJahrgang","ASDBezeichnung","Sichtbar","Sortierung","IstChronologisch","Kurzbezeichnung","Sekundarstufe","Gliederung","AnzahlRestabschnitte","Folgejahrgang_ID","SchulnrEigner"})
public class MigrationDTOJahrgang {

	/** Eindeutige ID zur Kennzeichnung des Jahrgangs-Datensatzes */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Ein Kürzel für den Jahrgang, welches bei der Darstellung genutzt wird */
	@Column(name = "InternKrz")
	@JsonProperty
	public String InternKrz;

	/** Gibt an, von welchem Schuljahresabschnitt an der Jahrgang gültig ist (einschließlich), NULL bedeutet von dem ersten Abschnitt an */
	@Column(name = "GueltigVon")
	@JsonProperty
	public Long GueltigVon;

	/** Gibt an, bis zu welchem Schuljahresabschnitt an der Jahrgang gültig ist (einschließlich), NULL bedeutet bis zum letzten Abschnitt, Ende offen */
	@Column(name = "GueltigBis")
	@JsonProperty
	public Long GueltigBis;

	/** Ein Kürzel für den Jahrgang, welches bei der Statistik verwendet wird */
	@Column(name = "ASDJahrgang")
	@JsonProperty
	public String ASDJahrgang;

	/** Die Bezeichnung für den Jahrgang, welche bei der Statistik verwendet wird */
	@Column(name = "ASDBezeichnung")
	@JsonProperty
	public String ASDBezeichnung;

	/** true, falls der Jahrgang bei Auswahlen angezeigt werden soll oder nicht. */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter=MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** Ein Zahlwert, welcher bei für eine Sortierung der Jahrgänge bei der Darstellung verwendet wird. */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Gibt an ob ein Jahrgang zu einer chronologischen Reihenfolge gehört */
	@Column(name = "IstChronologisch")
	@JsonProperty
	@Convert(converter=MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean IstChronologisch;

	/** Wird in der Übersicht benutzt um den Spaltentitel anzuzeigen. (Kurzbezeichnung in Jahrgangstabelle) */
	@Column(name = "Spaltentitel")
	@JsonProperty
	public String Kurzbezeichnung;

	/** Gibt die Primar- bzw. Sekundarstufe des Jahrgang an (Pr, SI, SII-1, SII-2, SII-3) */
	@Column(name = "SekStufe")
	@JsonProperty
	public String Sekundarstufe;

	/** Schulgliederung des Jahrgangs */
	@Column(name = "SGL")
	@JsonProperty
	public String Gliederung;

	/** Gibt die Anzahl der Restabschnitte an, die für den Verbleib an dieser Schulform üblich ist. */
	@Column(name = "Restabschnitte")
	@JsonProperty
	public Integer AnzahlRestabschnitte;

	/** Die eindeutige ID des Jahrgangs, welcher auf diesen folgt – verweist auf diese Tabelle oder NULL, falls es sich um den letzten Jahrgang handelt */
	@Column(name = "Folgejahrgang_ID")
	@JsonProperty
	public Long Folgejahrgang_ID;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOJahrgang ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOJahrgang() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOJahrgang ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 */
	public MigrationDTOJahrgang(final Long ID) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOJahrgang other = (MigrationDTOJahrgang) obj;
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
		return "MigrationDTOJahrgang(ID=" + this.ID + ", InternKrz=" + this.InternKrz + ", GueltigVon=" + this.GueltigVon + ", GueltigBis=" + this.GueltigBis + ", ASDJahrgang=" + this.ASDJahrgang + ", ASDBezeichnung=" + this.ASDBezeichnung + ", Sichtbar=" + this.Sichtbar + ", Sortierung=" + this.Sortierung + ", IstChronologisch=" + this.IstChronologisch + ", Kurzbezeichnung=" + this.Kurzbezeichnung + ", Sekundarstufe=" + this.Sekundarstufe + ", Gliederung=" + this.Gliederung + ", AnzahlRestabschnitte=" + this.AnzahlRestabschnitte + ", Folgejahrgang_ID=" + this.Folgejahrgang_ID + ", SchulnrEigner=" + this.SchulnrEigner + ")";
	}

}