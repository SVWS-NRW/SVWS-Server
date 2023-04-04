package de.svws_nrw.db.dto.migration.schild.schueler;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerSprachpruefungen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerSprachpruefungen")
@NamedQuery(name = "MigrationDTOSchuelerSprachpruefungen.all", query = "SELECT e FROM MigrationDTOSchuelerSprachpruefungen e")
@NamedQuery(name = "MigrationDTOSchuelerSprachpruefungen.id", query = "SELECT e FROM MigrationDTOSchuelerSprachpruefungen e WHERE e.ID = :value")
@NamedQuery(name = "MigrationDTOSchuelerSprachpruefungen.id.multiple", query = "SELECT e FROM MigrationDTOSchuelerSprachpruefungen e WHERE e.ID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerSprachpruefungen.schueler_id", query = "SELECT e FROM MigrationDTOSchuelerSprachpruefungen e WHERE e.Schueler_ID = :value")
@NamedQuery(name = "MigrationDTOSchuelerSprachpruefungen.schueler_id.multiple", query = "SELECT e FROM MigrationDTOSchuelerSprachpruefungen e WHERE e.Schueler_ID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerSprachpruefungen.sprache", query = "SELECT e FROM MigrationDTOSchuelerSprachpruefungen e WHERE e.Sprache = :value")
@NamedQuery(name = "MigrationDTOSchuelerSprachpruefungen.sprache.multiple", query = "SELECT e FROM MigrationDTOSchuelerSprachpruefungen e WHERE e.Sprache IN :value")
@NamedQuery(name = "MigrationDTOSchuelerSprachpruefungen.asdjahrgang", query = "SELECT e FROM MigrationDTOSchuelerSprachpruefungen e WHERE e.ASDJahrgang = :value")
@NamedQuery(name = "MigrationDTOSchuelerSprachpruefungen.asdjahrgang.multiple", query = "SELECT e FROM MigrationDTOSchuelerSprachpruefungen e WHERE e.ASDJahrgang IN :value")
@NamedQuery(name = "MigrationDTOSchuelerSprachpruefungen.anspruchsniveau", query = "SELECT e FROM MigrationDTOSchuelerSprachpruefungen e WHERE e.Anspruchsniveau = :value")
@NamedQuery(name = "MigrationDTOSchuelerSprachpruefungen.anspruchsniveau.multiple", query = "SELECT e FROM MigrationDTOSchuelerSprachpruefungen e WHERE e.Anspruchsniveau IN :value")
@NamedQuery(name = "MigrationDTOSchuelerSprachpruefungen.pruefungsdatum", query = "SELECT e FROM MigrationDTOSchuelerSprachpruefungen e WHERE e.Pruefungsdatum = :value")
@NamedQuery(name = "MigrationDTOSchuelerSprachpruefungen.pruefungsdatum.multiple", query = "SELECT e FROM MigrationDTOSchuelerSprachpruefungen e WHERE e.Pruefungsdatum IN :value")
@NamedQuery(name = "MigrationDTOSchuelerSprachpruefungen.ersetztesprache", query = "SELECT e FROM MigrationDTOSchuelerSprachpruefungen e WHERE e.ErsetzteSprache = :value")
@NamedQuery(name = "MigrationDTOSchuelerSprachpruefungen.ersetztesprache.multiple", query = "SELECT e FROM MigrationDTOSchuelerSprachpruefungen e WHERE e.ErsetzteSprache IN :value")
@NamedQuery(name = "MigrationDTOSchuelerSprachpruefungen.isthsupruefung", query = "SELECT e FROM MigrationDTOSchuelerSprachpruefungen e WHERE e.IstHSUPruefung = :value")
@NamedQuery(name = "MigrationDTOSchuelerSprachpruefungen.isthsupruefung.multiple", query = "SELECT e FROM MigrationDTOSchuelerSprachpruefungen e WHERE e.IstHSUPruefung IN :value")
@NamedQuery(name = "MigrationDTOSchuelerSprachpruefungen.istfeststellungspruefung", query = "SELECT e FROM MigrationDTOSchuelerSprachpruefungen e WHERE e.IstFeststellungspruefung = :value")
@NamedQuery(name = "MigrationDTOSchuelerSprachpruefungen.istfeststellungspruefung.multiple", query = "SELECT e FROM MigrationDTOSchuelerSprachpruefungen e WHERE e.IstFeststellungspruefung IN :value")
@NamedQuery(name = "MigrationDTOSchuelerSprachpruefungen.kannerstepflichtfremdspracheersetzen", query = "SELECT e FROM MigrationDTOSchuelerSprachpruefungen e WHERE e.KannErstePflichtfremdspracheErsetzen = :value")
@NamedQuery(name = "MigrationDTOSchuelerSprachpruefungen.kannerstepflichtfremdspracheersetzen.multiple", query = "SELECT e FROM MigrationDTOSchuelerSprachpruefungen e WHERE e.KannErstePflichtfremdspracheErsetzen IN :value")
@NamedQuery(name = "MigrationDTOSchuelerSprachpruefungen.kannzweitepflichtfremdspracheersetzen", query = "SELECT e FROM MigrationDTOSchuelerSprachpruefungen e WHERE e.KannZweitePflichtfremdspracheErsetzen = :value")
@NamedQuery(name = "MigrationDTOSchuelerSprachpruefungen.kannzweitepflichtfremdspracheersetzen.multiple", query = "SELECT e FROM MigrationDTOSchuelerSprachpruefungen e WHERE e.KannZweitePflichtfremdspracheErsetzen IN :value")
@NamedQuery(name = "MigrationDTOSchuelerSprachpruefungen.kannwahlpflichtfremdspracheersetzen", query = "SELECT e FROM MigrationDTOSchuelerSprachpruefungen e WHERE e.KannWahlpflichtfremdspracheErsetzen = :value")
@NamedQuery(name = "MigrationDTOSchuelerSprachpruefungen.kannwahlpflichtfremdspracheersetzen.multiple", query = "SELECT e FROM MigrationDTOSchuelerSprachpruefungen e WHERE e.KannWahlpflichtfremdspracheErsetzen IN :value")
@NamedQuery(name = "MigrationDTOSchuelerSprachpruefungen.kannbelegungalsfortgefuehrtespracheerlauben", query = "SELECT e FROM MigrationDTOSchuelerSprachpruefungen e WHERE e.KannBelegungAlsFortgefuehrteSpracheErlauben = :value")
@NamedQuery(name = "MigrationDTOSchuelerSprachpruefungen.kannbelegungalsfortgefuehrtespracheerlauben.multiple", query = "SELECT e FROM MigrationDTOSchuelerSprachpruefungen e WHERE e.KannBelegungAlsFortgefuehrteSpracheErlauben IN :value")
@NamedQuery(name = "MigrationDTOSchuelerSprachpruefungen.referenzniveau", query = "SELECT e FROM MigrationDTOSchuelerSprachpruefungen e WHERE e.Referenzniveau = :value")
@NamedQuery(name = "MigrationDTOSchuelerSprachpruefungen.referenzniveau.multiple", query = "SELECT e FROM MigrationDTOSchuelerSprachpruefungen e WHERE e.Referenzniveau IN :value")
@NamedQuery(name = "MigrationDTOSchuelerSprachpruefungen.notepruefung", query = "SELECT e FROM MigrationDTOSchuelerSprachpruefungen e WHERE e.NotePruefung = :value")
@NamedQuery(name = "MigrationDTOSchuelerSprachpruefungen.notepruefung.multiple", query = "SELECT e FROM MigrationDTOSchuelerSprachpruefungen e WHERE e.NotePruefung IN :value")
@NamedQuery(name = "MigrationDTOSchuelerSprachpruefungen.primaryKeyQuery", query = "SELECT e FROM MigrationDTOSchuelerSprachpruefungen e WHERE e.ID = ?1")
@NamedQuery(name = "MigrationDTOSchuelerSprachpruefungen.all.migration", query = "SELECT e FROM MigrationDTOSchuelerSprachpruefungen e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Schueler_ID", "Sprache", "ASDJahrgang", "Anspruchsniveau", "Pruefungsdatum", "ErsetzteSprache", "IstHSUPruefung", "IstFeststellungspruefung", "KannErstePflichtfremdspracheErsetzen", "KannZweitePflichtfremdspracheErsetzen", "KannWahlpflichtfremdspracheErsetzen", "KannBelegungAlsFortgefuehrteSpracheErlauben", "Referenzniveau", "NotePruefung"})
public final class MigrationDTOSchuelerSprachpruefungen {

	/** ID des Sprachprüfungseintrags */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** ID des Schülers des Sprachprüfungseintrags */
	@Column(name = "Schueler_ID")
	@JsonProperty
	public Long Schueler_ID;

	/** Atomares Sprachkürzel aus StatKue_SVWS_ZulaessigeFaecher */
	@Column(name = "Sprache")
	@JsonProperty
	public String Sprache;

	/** ASDJahrgangsbezeichnung, in der die Sprachprüfung abgelegt wurde */
	@Column(name = "ASDJahrgang")
	@JsonProperty
	public String ASDJahrgang;

	/** Das Anspruchsniveau der Sprachprüfung (angelehnt an einen entsprechenden Schulabschluss) */
	@Column(name = "Anspruchsniveau_ID")
	@JsonProperty
	public Long Anspruchsniveau;

	/** Datum der Sprachprüfung */
	@Column(name = "Pruefungsdatum")
	@JsonProperty
	public String Pruefungsdatum;

	/** Atomares Sprachkürzel aus StatKue_SVWS_ZulaessigeFaecher für die ersetzte Sprache */
	@Column(name = "ErsetzteSprache")
	@JsonProperty
	public String ErsetzteSprache;

	/** Gibt an, dass die Prüfung eine Prüfung in der Herkunftssprache ist (BASS 13-61 Nr. 2). Entspricht dem Eintrag P in Schild 2 */
	@Column(name = "IstHSUPruefung")
	@JsonProperty
	@Convert(converter = MigrationBoolean01Converter.class)
	@JsonSerialize(using = MigrationBoolean01ConverterSerializer.class)
	@JsonDeserialize(using = MigrationBoolean01ConverterDeserializer.class)
	public Boolean IstHSUPruefung;

	/** Gibt an, ob die Prüfung eine Sprachfeststellungsprüfung ist (BASS 13-61 Nr. 1). Entspricht N in Schild 2 */
	@Column(name = "IstFeststellungspruefung")
	@JsonProperty
	@Convert(converter = MigrationBoolean01Converter.class)
	@JsonSerialize(using = MigrationBoolean01ConverterSerializer.class)
	@JsonDeserialize(using = MigrationBoolean01ConverterDeserializer.class)
	public Boolean IstFeststellungspruefung;

	/** Gibt an, ob die Sprachprüfung an die Stelle der ersten Pflichtfremdsprache treten kann */
	@Column(name = "KannErstePflichtfremdspracheErsetzen")
	@JsonProperty
	@Convert(converter = MigrationBoolean01Converter.class)
	@JsonSerialize(using = MigrationBoolean01ConverterSerializer.class)
	@JsonDeserialize(using = MigrationBoolean01ConverterDeserializer.class)
	public Boolean KannErstePflichtfremdspracheErsetzen;

	/** Gibt an, ob die Sprachprüfung an die Stelle der zweiten Pflichtfremdsprache treten kann */
	@Column(name = "KannZweitePflichtfremdspracheErsetzen")
	@JsonProperty
	@Convert(converter = MigrationBoolean01Converter.class)
	@JsonSerialize(using = MigrationBoolean01ConverterSerializer.class)
	@JsonDeserialize(using = MigrationBoolean01ConverterDeserializer.class)
	public Boolean KannZweitePflichtfremdspracheErsetzen;

	/** Gibt an, ob die Sprachprüfung an die Stelle einer Wahlpflichtfremdsprache der Klassen 05-07 treten kann */
	@Column(name = "KannWahlpflichtfremdspracheErsetzen")
	@JsonProperty
	@Convert(converter = MigrationBoolean01Converter.class)
	@JsonSerialize(using = MigrationBoolean01ConverterSerializer.class)
	@JsonDeserialize(using = MigrationBoolean01ConverterDeserializer.class)
	public Boolean KannWahlpflichtfremdspracheErsetzen;

	/** Gibt an, ob die Sprachprüfung nachweist, dass die Sprache als fortgeführte Fremdsprache in der Oberstufe belegt werden kann (BASS 13-61 Nr. 1 Abs. 11) */
	@Column(name = "KannBelegungAlsFortgefuehrteSpracheErlauben")
	@JsonProperty
	@Convert(converter = MigrationBoolean01Converter.class)
	@JsonSerialize(using = MigrationBoolean01ConverterSerializer.class)
	@JsonDeserialize(using = MigrationBoolean01ConverterDeserializer.class)
	public Boolean KannBelegungAlsFortgefuehrteSpracheErlauben;

	/** Das Sprachreferenzniveau der Sprachprüfung gemäß GeR */
	@Column(name = "Referenzniveau")
	@JsonProperty
	public String Referenzniveau;

	/** Note der Sprachprüfung, die herangezogen werden kann, falls die Note der Sprachprüfung an die Stelle einer Fremdsprachennote tritt */
	@Column(name = "NotePruefung")
	@JsonProperty
	public Integer NotePruefung;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuelerSprachpruefungen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOSchuelerSprachpruefungen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuelerSprachpruefungen ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 * @param Sprache   der Wert für das Attribut Sprache
	 */
	public MigrationDTOSchuelerSprachpruefungen(final Long ID, final Long Schueler_ID, final String Sprache) {
		if (ID == null) {
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Schueler_ID == null) {
			throw new NullPointerException("Schueler_ID must not be null");
		}
		this.Schueler_ID = Schueler_ID;
		if (Sprache == null) {
			throw new NullPointerException("Sprache must not be null");
		}
		this.Sprache = Sprache;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOSchuelerSprachpruefungen other = (MigrationDTOSchuelerSprachpruefungen) obj;
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
		return "MigrationDTOSchuelerSprachpruefungen(ID=" + this.ID + ", Schueler_ID=" + this.Schueler_ID + ", Sprache=" + this.Sprache + ", ASDJahrgang=" + this.ASDJahrgang + ", Anspruchsniveau=" + this.Anspruchsniveau + ", Pruefungsdatum=" + this.Pruefungsdatum + ", ErsetzteSprache=" + this.ErsetzteSprache + ", IstHSUPruefung=" + this.IstHSUPruefung + ", IstFeststellungspruefung=" + this.IstFeststellungspruefung + ", KannErstePflichtfremdspracheErsetzen=" + this.KannErstePflichtfremdspracheErsetzen + ", KannZweitePflichtfremdspracheErsetzen=" + this.KannZweitePflichtfremdspracheErsetzen + ", KannWahlpflichtfremdspracheErsetzen=" + this.KannWahlpflichtfremdspracheErsetzen + ", KannBelegungAlsFortgefuehrteSpracheErlauben=" + this.KannBelegungAlsFortgefuehrteSpracheErlauben + ", Referenzniveau=" + this.Referenzniveau + ", NotePruefung=" + this.NotePruefung + ")";
	}

}
