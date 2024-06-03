package de.svws_nrw.db.dto.current.schild.schule;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultPlusConverter;
import de.svws_nrw.db.converter.current.statkue.SchulgliederungKuerzelConverter;

import de.svws_nrw.core.types.schule.Schulgliederung;


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
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultPlusConverterSerializer;
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultPlusConverterDeserializer;
import de.svws_nrw.csv.converter.current.statkue.SchulgliederungKuerzelConverterSerializer;
import de.svws_nrw.csv.converter.current.statkue.SchulgliederungKuerzelConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle EigeneSchule_Jahrgaenge.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "EigeneSchule_Jahrgaenge")
@JsonPropertyOrder({"ID", "InternKrz", "GueltigVon", "GueltigBis", "ASDJahrgang", "ASDBezeichnung", "Sichtbar", "Sortierung", "IstChronologisch", "Kurzbezeichnung", "Sekundarstufe", "Gliederung", "AnzahlRestabschnitte", "Folgejahrgang_ID"})
public final class DTOJahrgang {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOJahrgang e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOJahrgang e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOJahrgang e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOJahrgang e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOJahrgang e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOJahrgang e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes InternKrz */
	public static final String QUERY_BY_INTERNKRZ = "SELECT e FROM DTOJahrgang e WHERE e.InternKrz = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes InternKrz */
	public static final String QUERY_LIST_BY_INTERNKRZ = "SELECT e FROM DTOJahrgang e WHERE e.InternKrz IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes GueltigVon */
	public static final String QUERY_BY_GUELTIGVON = "SELECT e FROM DTOJahrgang e WHERE e.GueltigVon = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes GueltigVon */
	public static final String QUERY_LIST_BY_GUELTIGVON = "SELECT e FROM DTOJahrgang e WHERE e.GueltigVon IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes GueltigBis */
	public static final String QUERY_BY_GUELTIGBIS = "SELECT e FROM DTOJahrgang e WHERE e.GueltigBis = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes GueltigBis */
	public static final String QUERY_LIST_BY_GUELTIGBIS = "SELECT e FROM DTOJahrgang e WHERE e.GueltigBis IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ASDJahrgang */
	public static final String QUERY_BY_ASDJAHRGANG = "SELECT e FROM DTOJahrgang e WHERE e.ASDJahrgang = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ASDJahrgang */
	public static final String QUERY_LIST_BY_ASDJAHRGANG = "SELECT e FROM DTOJahrgang e WHERE e.ASDJahrgang IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ASDBezeichnung */
	public static final String QUERY_BY_ASDBEZEICHNUNG = "SELECT e FROM DTOJahrgang e WHERE e.ASDBezeichnung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ASDBezeichnung */
	public static final String QUERY_LIST_BY_ASDBEZEICHNUNG = "SELECT e FROM DTOJahrgang e WHERE e.ASDBezeichnung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Sichtbar */
	public static final String QUERY_BY_SICHTBAR = "SELECT e FROM DTOJahrgang e WHERE e.Sichtbar = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Sichtbar */
	public static final String QUERY_LIST_BY_SICHTBAR = "SELECT e FROM DTOJahrgang e WHERE e.Sichtbar IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Sortierung */
	public static final String QUERY_BY_SORTIERUNG = "SELECT e FROM DTOJahrgang e WHERE e.Sortierung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Sortierung */
	public static final String QUERY_LIST_BY_SORTIERUNG = "SELECT e FROM DTOJahrgang e WHERE e.Sortierung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes IstChronologisch */
	public static final String QUERY_BY_ISTCHRONOLOGISCH = "SELECT e FROM DTOJahrgang e WHERE e.IstChronologisch = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes IstChronologisch */
	public static final String QUERY_LIST_BY_ISTCHRONOLOGISCH = "SELECT e FROM DTOJahrgang e WHERE e.IstChronologisch IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Kurzbezeichnung */
	public static final String QUERY_BY_KURZBEZEICHNUNG = "SELECT e FROM DTOJahrgang e WHERE e.Kurzbezeichnung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Kurzbezeichnung */
	public static final String QUERY_LIST_BY_KURZBEZEICHNUNG = "SELECT e FROM DTOJahrgang e WHERE e.Kurzbezeichnung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Sekundarstufe */
	public static final String QUERY_BY_SEKUNDARSTUFE = "SELECT e FROM DTOJahrgang e WHERE e.Sekundarstufe = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Sekundarstufe */
	public static final String QUERY_LIST_BY_SEKUNDARSTUFE = "SELECT e FROM DTOJahrgang e WHERE e.Sekundarstufe IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Gliederung */
	public static final String QUERY_BY_GLIEDERUNG = "SELECT e FROM DTOJahrgang e WHERE e.Gliederung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Gliederung */
	public static final String QUERY_LIST_BY_GLIEDERUNG = "SELECT e FROM DTOJahrgang e WHERE e.Gliederung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AnzahlRestabschnitte */
	public static final String QUERY_BY_ANZAHLRESTABSCHNITTE = "SELECT e FROM DTOJahrgang e WHERE e.AnzahlRestabschnitte = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AnzahlRestabschnitte */
	public static final String QUERY_LIST_BY_ANZAHLRESTABSCHNITTE = "SELECT e FROM DTOJahrgang e WHERE e.AnzahlRestabschnitte IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Folgejahrgang_ID */
	public static final String QUERY_BY_FOLGEJAHRGANG_ID = "SELECT e FROM DTOJahrgang e WHERE e.Folgejahrgang_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Folgejahrgang_ID */
	public static final String QUERY_LIST_BY_FOLGEJAHRGANG_ID = "SELECT e FROM DTOJahrgang e WHERE e.Folgejahrgang_ID IN ?1";

	/** Eindeutige ID zur Kennzeichnung des Jahrgangs-Datensatzes */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

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
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** Ein Zahlwert, welcher bei für eine Sortierung der Jahrgänge bei der Darstellung verwendet wird. */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Gibt an ob ein Jahrgang zu einer chronologischen Reihenfolge gehört */
	@Column(name = "IstChronologisch")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultPlusConverterDeserializer.class)
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
	@Convert(converter = SchulgliederungKuerzelConverter.class)
	@JsonSerialize(using = SchulgliederungKuerzelConverterSerializer.class)
	@JsonDeserialize(using = SchulgliederungKuerzelConverterDeserializer.class)
	public Schulgliederung Gliederung;

	/** Gibt die Anzahl der Restabschnitte an, die für den Verbleib an dieser Schulform üblich ist. */
	@Column(name = "Restabschnitte")
	@JsonProperty
	public Integer AnzahlRestabschnitte;

	/** Die eindeutige ID des Jahrgangs, welcher auf diesen folgt – verweist auf diese Tabelle oder NULL, falls es sich um den letzten Jahrgang handelt */
	@Column(name = "Folgejahrgang_ID")
	@JsonProperty
	public Long Folgejahrgang_ID;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOJahrgang ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOJahrgang() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOJahrgang ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 */
	public DTOJahrgang(final long ID) {
		this.ID = ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOJahrgang other = (DTOJahrgang) obj;
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
		return "DTOJahrgang(ID=" + this.ID + ", InternKrz=" + this.InternKrz + ", GueltigVon=" + this.GueltigVon + ", GueltigBis=" + this.GueltigBis + ", ASDJahrgang=" + this.ASDJahrgang + ", ASDBezeichnung=" + this.ASDBezeichnung + ", Sichtbar=" + this.Sichtbar + ", Sortierung=" + this.Sortierung + ", IstChronologisch=" + this.IstChronologisch + ", Kurzbezeichnung=" + this.Kurzbezeichnung + ", Sekundarstufe=" + this.Sekundarstufe + ", Gliederung=" + this.Gliederung + ", AnzahlRestabschnitte=" + this.AnzahlRestabschnitte + ", Folgejahrgang_ID=" + this.Folgejahrgang_ID + ")";
	}

}
