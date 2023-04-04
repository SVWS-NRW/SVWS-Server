package de.svws_nrw.db.dto.migration.schild.klassen;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverter;
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
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverterSerializer;
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverterDeserializer;
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverterSerializer;
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultPlusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Versetzung.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Versetzung")
@NamedQuery(name = "MigrationDTOVersetzung.all", query = "SELECT e FROM MigrationDTOVersetzung e")
@NamedQuery(name = "MigrationDTOVersetzung.id", query = "SELECT e FROM MigrationDTOVersetzung e WHERE e.ID = :value")
@NamedQuery(name = "MigrationDTOVersetzung.id.multiple", query = "SELECT e FROM MigrationDTOVersetzung e WHERE e.ID IN :value")
@NamedQuery(name = "MigrationDTOVersetzung.bezeichnung", query = "SELECT e FROM MigrationDTOVersetzung e WHERE e.Bezeichnung = :value")
@NamedQuery(name = "MigrationDTOVersetzung.bezeichnung.multiple", query = "SELECT e FROM MigrationDTOVersetzung e WHERE e.Bezeichnung IN :value")
@NamedQuery(name = "MigrationDTOVersetzung.asdklasse", query = "SELECT e FROM MigrationDTOVersetzung e WHERE e.ASDKlasse = :value")
@NamedQuery(name = "MigrationDTOVersetzung.asdklasse.multiple", query = "SELECT e FROM MigrationDTOVersetzung e WHERE e.ASDKlasse IN :value")
@NamedQuery(name = "MigrationDTOVersetzung.klasse", query = "SELECT e FROM MigrationDTOVersetzung e WHERE e.Klasse = :value")
@NamedQuery(name = "MigrationDTOVersetzung.klasse.multiple", query = "SELECT e FROM MigrationDTOVersetzung e WHERE e.Klasse IN :value")
@NamedQuery(name = "MigrationDTOVersetzung.jahrgang_id", query = "SELECT e FROM MigrationDTOVersetzung e WHERE e.Jahrgang_ID = :value")
@NamedQuery(name = "MigrationDTOVersetzung.jahrgang_id.multiple", query = "SELECT e FROM MigrationDTOVersetzung e WHERE e.Jahrgang_ID IN :value")
@NamedQuery(name = "MigrationDTOVersetzung.fklasse", query = "SELECT e FROM MigrationDTOVersetzung e WHERE e.FKlasse = :value")
@NamedQuery(name = "MigrationDTOVersetzung.fklasse.multiple", query = "SELECT e FROM MigrationDTOVersetzung e WHERE e.FKlasse IN :value")
@NamedQuery(name = "MigrationDTOVersetzung.vklasse", query = "SELECT e FROM MigrationDTOVersetzung e WHERE e.VKlasse = :value")
@NamedQuery(name = "MigrationDTOVersetzung.vklasse.multiple", query = "SELECT e FROM MigrationDTOVersetzung e WHERE e.VKlasse IN :value")
@NamedQuery(name = "MigrationDTOVersetzung.orgformkrz", query = "SELECT e FROM MigrationDTOVersetzung e WHERE e.OrgFormKrz = :value")
@NamedQuery(name = "MigrationDTOVersetzung.orgformkrz.multiple", query = "SELECT e FROM MigrationDTOVersetzung e WHERE e.OrgFormKrz IN :value")
@NamedQuery(name = "MigrationDTOVersetzung.klassenlehrerkrz", query = "SELECT e FROM MigrationDTOVersetzung e WHERE e.KlassenlehrerKrz = :value")
@NamedQuery(name = "MigrationDTOVersetzung.klassenlehrerkrz.multiple", query = "SELECT e FROM MigrationDTOVersetzung e WHERE e.KlassenlehrerKrz IN :value")
@NamedQuery(name = "MigrationDTOVersetzung.stvklassenlehrerkrz", query = "SELECT e FROM MigrationDTOVersetzung e WHERE e.StvKlassenlehrerKrz = :value")
@NamedQuery(name = "MigrationDTOVersetzung.stvklassenlehrerkrz.multiple", query = "SELECT e FROM MigrationDTOVersetzung e WHERE e.StvKlassenlehrerKrz IN :value")
@NamedQuery(name = "MigrationDTOVersetzung.restabschnitte", query = "SELECT e FROM MigrationDTOVersetzung e WHERE e.Restabschnitte = :value")
@NamedQuery(name = "MigrationDTOVersetzung.restabschnitte.multiple", query = "SELECT e FROM MigrationDTOVersetzung e WHERE e.Restabschnitte IN :value")
@NamedQuery(name = "MigrationDTOVersetzung.asdschulformnr", query = "SELECT e FROM MigrationDTOVersetzung e WHERE e.ASDSchulformNr = :value")
@NamedQuery(name = "MigrationDTOVersetzung.asdschulformnr.multiple", query = "SELECT e FROM MigrationDTOVersetzung e WHERE e.ASDSchulformNr IN :value")
@NamedQuery(name = "MigrationDTOVersetzung.fachklasse_id", query = "SELECT e FROM MigrationDTOVersetzung e WHERE e.Fachklasse_ID = :value")
@NamedQuery(name = "MigrationDTOVersetzung.fachklasse_id.multiple", query = "SELECT e FROM MigrationDTOVersetzung e WHERE e.Fachklasse_ID IN :value")
@NamedQuery(name = "MigrationDTOVersetzung.pruefordnung", query = "SELECT e FROM MigrationDTOVersetzung e WHERE e.PruefOrdnung = :value")
@NamedQuery(name = "MigrationDTOVersetzung.pruefordnung.multiple", query = "SELECT e FROM MigrationDTOVersetzung e WHERE e.PruefOrdnung IN :value")
@NamedQuery(name = "MigrationDTOVersetzung.sichtbar", query = "SELECT e FROM MigrationDTOVersetzung e WHERE e.Sichtbar = :value")
@NamedQuery(name = "MigrationDTOVersetzung.sichtbar.multiple", query = "SELECT e FROM MigrationDTOVersetzung e WHERE e.Sichtbar IN :value")
@NamedQuery(name = "MigrationDTOVersetzung.sortierung", query = "SELECT e FROM MigrationDTOVersetzung e WHERE e.Sortierung = :value")
@NamedQuery(name = "MigrationDTOVersetzung.sortierung.multiple", query = "SELECT e FROM MigrationDTOVersetzung e WHERE e.Sortierung IN :value")
@NamedQuery(name = "MigrationDTOVersetzung.klassenart", query = "SELECT e FROM MigrationDTOVersetzung e WHERE e.Klassenart = :value")
@NamedQuery(name = "MigrationDTOVersetzung.klassenart.multiple", query = "SELECT e FROM MigrationDTOVersetzung e WHERE e.Klassenart IN :value")
@NamedQuery(name = "MigrationDTOVersetzung.sommersem", query = "SELECT e FROM MigrationDTOVersetzung e WHERE e.SommerSem = :value")
@NamedQuery(name = "MigrationDTOVersetzung.sommersem.multiple", query = "SELECT e FROM MigrationDTOVersetzung e WHERE e.SommerSem IN :value")
@NamedQuery(name = "MigrationDTOVersetzung.notengesperrt", query = "SELECT e FROM MigrationDTOVersetzung e WHERE e.NotenGesperrt = :value")
@NamedQuery(name = "MigrationDTOVersetzung.notengesperrt.multiple", query = "SELECT e FROM MigrationDTOVersetzung e WHERE e.NotenGesperrt IN :value")
@NamedQuery(name = "MigrationDTOVersetzung.schulnreigner", query = "SELECT e FROM MigrationDTOVersetzung e WHERE e.SchulnrEigner = :value")
@NamedQuery(name = "MigrationDTOVersetzung.schulnreigner.multiple", query = "SELECT e FROM MigrationDTOVersetzung e WHERE e.SchulnrEigner IN :value")
@NamedQuery(name = "MigrationDTOVersetzung.adrmerkmal", query = "SELECT e FROM MigrationDTOVersetzung e WHERE e.AdrMerkmal = :value")
@NamedQuery(name = "MigrationDTOVersetzung.adrmerkmal.multiple", query = "SELECT e FROM MigrationDTOVersetzung e WHERE e.AdrMerkmal IN :value")
@NamedQuery(name = "MigrationDTOVersetzung.webnotengesperrt", query = "SELECT e FROM MigrationDTOVersetzung e WHERE e.WebNotenGesperrt = :value")
@NamedQuery(name = "MigrationDTOVersetzung.webnotengesperrt.multiple", query = "SELECT e FROM MigrationDTOVersetzung e WHERE e.WebNotenGesperrt IN :value")
@NamedQuery(name = "MigrationDTOVersetzung.koopklasse", query = "SELECT e FROM MigrationDTOVersetzung e WHERE e.KoopKlasse = :value")
@NamedQuery(name = "MigrationDTOVersetzung.koopklasse.multiple", query = "SELECT e FROM MigrationDTOVersetzung e WHERE e.KoopKlasse IN :value")
@NamedQuery(name = "MigrationDTOVersetzung.ankreuzzeugnisse", query = "SELECT e FROM MigrationDTOVersetzung e WHERE e.Ankreuzzeugnisse = :value")
@NamedQuery(name = "MigrationDTOVersetzung.ankreuzzeugnisse.multiple", query = "SELECT e FROM MigrationDTOVersetzung e WHERE e.Ankreuzzeugnisse IN :value")
@NamedQuery(name = "MigrationDTOVersetzung.primaryKeyQuery", query = "SELECT e FROM MigrationDTOVersetzung e WHERE e.ID = ?1")
@NamedQuery(name = "MigrationDTOVersetzung.all.migration", query = "SELECT e FROM MigrationDTOVersetzung e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Bezeichnung", "ASDKlasse", "Klasse", "Jahrgang_ID", "FKlasse", "VKlasse", "OrgFormKrz", "KlassenlehrerKrz", "StvKlassenlehrerKrz", "Restabschnitte", "ASDSchulformNr", "Fachklasse_ID", "PruefOrdnung", "Sichtbar", "Sortierung", "Klassenart", "SommerSem", "NotenGesperrt", "SchulnrEigner", "AdrMerkmal", "WebNotenGesperrt", "KoopKlasse", "Ankreuzzeugnisse"})
public final class MigrationDTOVersetzung {

	/** ID der Klasse in der Klassen- Versetzuungstabelle */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Bezeichnender Text für die Klasse */
	@Column(name = "Bezeichnung")
	@JsonProperty
	public String Bezeichnung;

	/** ASD-Jahrgang der Klasse */
	@Column(name = "ASDKlasse")
	@JsonProperty
	public String ASDKlasse;

	/** Kürzel der Klasse */
	@Column(name = "Klasse")
	@JsonProperty
	public String Klasse;

	/** ID des ASD-Jahrgangs */
	@Column(name = "Jahrgang_ID")
	@JsonProperty
	public Long Jahrgang_ID;

	/** Folgeklasse */
	@Column(name = "FKlasse")
	@JsonProperty
	public String FKlasse;

	/** Vorgängerklasse */
	@Column(name = "VKlasse")
	@JsonProperty
	public String VKlasse;

	/** Organisationsform der Klasse Kürzel IT.NRW */
	@Column(name = "OrgFormKrz")
	@JsonProperty
	public String OrgFormKrz;

	/** Kürzel des Klassenlehrers */
	@Column(name = "KlassenlehrerKrz")
	@JsonProperty
	public String KlassenlehrerKrz;

	/** Kürzel des stellvertretenden Klassenlehrers */
	@Column(name = "StvKlassenlehrerKrz")
	@JsonProperty
	public String StvKlassenlehrerKrz;

	/** DEPRECATED: Restabschnitte der Klasse */
	@Column(name = "Restabschnitte")
	@JsonProperty
	public Integer Restabschnitte;

	/** Schulgliederung in der Klasse */
	@Column(name = "ASDSchulformNr")
	@JsonProperty
	public String ASDSchulformNr;

	/** FID des Fachklasse nur BK SBK */
	@Column(name = "Fachklasse_ID")
	@JsonProperty
	public Long Fachklasse_ID;

	/** Prüfungsordnung für die Klasse */
	@Column(name = "PruefOrdnung")
	@JsonProperty
	public String PruefOrdnung;

	/** Gibt an ob eine Klasse sichtbar ist */
	@Column(name = "Sichtbar")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultPlusConverterDeserializer.class)
	public Boolean Sichtbar;

	/** Sortierungnummer der Klasse */
	@Column(name = "Sortierung")
	@JsonProperty
	public Integer Sortierung;

	/** Klassenart */
	@Column(name = "Klassenart")
	@JsonProperty
	public String Klassenart;

	/** Beginn im Sommersemester nur WBK */
	@Column(name = "SommerSem")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean SommerSem;

	/** Noteneingabe für die Klasse gesperrt */
	@Column(name = "NotenGesperrt")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean NotenGesperrt;

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/** Adressmerkmal des Teilstandorts für die Klasse */
	@Column(name = "AdrMerkmal")
	@JsonProperty
	public String AdrMerkmal;

	/** DEPRECATED: nicht mehr genutzt SchildWeb */
	@Column(name = "WebNotenGesperrt")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean WebNotenGesperrt;

	/** Gibt an ob die Klasse eine KOOP-Klasse ist */
	@Column(name = "KoopKlasse")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean KoopKlasse;

	/** Gibt an ob in der Klasse Ankreuzeugnisse (GS) oder Kompentenzschreiben (andere) verwendet werden */
	@Column(name = "Ankreuzzeugnisse")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Ankreuzzeugnisse;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOVersetzung ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOVersetzung() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOVersetzung ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Klasse   der Wert für das Attribut Klasse
	 */
	public MigrationDTOVersetzung(final Long ID, final String Klasse) {
		if (ID == null) {
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Klasse == null) {
			throw new NullPointerException("Klasse must not be null");
		}
		this.Klasse = Klasse;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MigrationDTOVersetzung other = (MigrationDTOVersetzung) obj;
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
		return "MigrationDTOVersetzung(ID=" + this.ID + ", Bezeichnung=" + this.Bezeichnung + ", ASDKlasse=" + this.ASDKlasse + ", Klasse=" + this.Klasse + ", Jahrgang_ID=" + this.Jahrgang_ID + ", FKlasse=" + this.FKlasse + ", VKlasse=" + this.VKlasse + ", OrgFormKrz=" + this.OrgFormKrz + ", KlassenlehrerKrz=" + this.KlassenlehrerKrz + ", StvKlassenlehrerKrz=" + this.StvKlassenlehrerKrz + ", Restabschnitte=" + this.Restabschnitte + ", ASDSchulformNr=" + this.ASDSchulformNr + ", Fachklasse_ID=" + this.Fachklasse_ID + ", PruefOrdnung=" + this.PruefOrdnung + ", Sichtbar=" + this.Sichtbar + ", Sortierung=" + this.Sortierung + ", Klassenart=" + this.Klassenart + ", SommerSem=" + this.SommerSem + ", NotenGesperrt=" + this.NotenGesperrt + ", SchulnrEigner=" + this.SchulnrEigner + ", AdrMerkmal=" + this.AdrMerkmal + ", WebNotenGesperrt=" + this.WebNotenGesperrt + ", KoopKlasse=" + this.KoopKlasse + ", Ankreuzzeugnisse=" + this.Ankreuzzeugnisse + ")";
	}

}
