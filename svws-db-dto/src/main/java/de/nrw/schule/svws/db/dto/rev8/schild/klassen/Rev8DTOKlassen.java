package de.nrw.schule.svws.db.dto.rev8.schild.klassen;

import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.converter.current.BooleanPlusMinusDefaultMinusConverter;
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
import de.nrw.schule.svws.csv.converter.current.BooleanPlusMinusDefaultMinusConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.BooleanPlusMinusDefaultMinusConverterDeserializer;
import de.nrw.schule.svws.csv.converter.current.BooleanPlusMinusDefaultPlusConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.BooleanPlusMinusDefaultPlusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Klassen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Klassen")
@NamedQuery(name="Rev8DTOKlassen.all", query="SELECT e FROM Rev8DTOKlassen e")
@NamedQuery(name="Rev8DTOKlassen.id", query="SELECT e FROM Rev8DTOKlassen e WHERE e.ID = :value")
@NamedQuery(name="Rev8DTOKlassen.id.multiple", query="SELECT e FROM Rev8DTOKlassen e WHERE e.ID IN :value")
@NamedQuery(name="Rev8DTOKlassen.schuljahresabschnitts_id", query="SELECT e FROM Rev8DTOKlassen e WHERE e.Schuljahresabschnitts_ID = :value")
@NamedQuery(name="Rev8DTOKlassen.schuljahresabschnitts_id.multiple", query="SELECT e FROM Rev8DTOKlassen e WHERE e.Schuljahresabschnitts_ID IN :value")
@NamedQuery(name="Rev8DTOKlassen.bezeichnung", query="SELECT e FROM Rev8DTOKlassen e WHERE e.Bezeichnung = :value")
@NamedQuery(name="Rev8DTOKlassen.bezeichnung.multiple", query="SELECT e FROM Rev8DTOKlassen e WHERE e.Bezeichnung IN :value")
@NamedQuery(name="Rev8DTOKlassen.asdklasse", query="SELECT e FROM Rev8DTOKlassen e WHERE e.ASDKlasse = :value")
@NamedQuery(name="Rev8DTOKlassen.asdklasse.multiple", query="SELECT e FROM Rev8DTOKlassen e WHERE e.ASDKlasse IN :value")
@NamedQuery(name="Rev8DTOKlassen.klasse", query="SELECT e FROM Rev8DTOKlassen e WHERE e.Klasse = :value")
@NamedQuery(name="Rev8DTOKlassen.klasse.multiple", query="SELECT e FROM Rev8DTOKlassen e WHERE e.Klasse IN :value")
@NamedQuery(name="Rev8DTOKlassen.jahrgang_id", query="SELECT e FROM Rev8DTOKlassen e WHERE e.Jahrgang_ID = :value")
@NamedQuery(name="Rev8DTOKlassen.jahrgang_id.multiple", query="SELECT e FROM Rev8DTOKlassen e WHERE e.Jahrgang_ID IN :value")
@NamedQuery(name="Rev8DTOKlassen.fklasse", query="SELECT e FROM Rev8DTOKlassen e WHERE e.FKlasse = :value")
@NamedQuery(name="Rev8DTOKlassen.fklasse.multiple", query="SELECT e FROM Rev8DTOKlassen e WHERE e.FKlasse IN :value")
@NamedQuery(name="Rev8DTOKlassen.vklasse", query="SELECT e FROM Rev8DTOKlassen e WHERE e.VKlasse = :value")
@NamedQuery(name="Rev8DTOKlassen.vklasse.multiple", query="SELECT e FROM Rev8DTOKlassen e WHERE e.VKlasse IN :value")
@NamedQuery(name="Rev8DTOKlassen.orgformkrz", query="SELECT e FROM Rev8DTOKlassen e WHERE e.OrgFormKrz = :value")
@NamedQuery(name="Rev8DTOKlassen.orgformkrz.multiple", query="SELECT e FROM Rev8DTOKlassen e WHERE e.OrgFormKrz IN :value")
@NamedQuery(name="Rev8DTOKlassen.asdschulformnr", query="SELECT e FROM Rev8DTOKlassen e WHERE e.ASDSchulformNr = :value")
@NamedQuery(name="Rev8DTOKlassen.asdschulformnr.multiple", query="SELECT e FROM Rev8DTOKlassen e WHERE e.ASDSchulformNr IN :value")
@NamedQuery(name="Rev8DTOKlassen.fachklasse_id", query="SELECT e FROM Rev8DTOKlassen e WHERE e.Fachklasse_ID = :value")
@NamedQuery(name="Rev8DTOKlassen.fachklasse_id.multiple", query="SELECT e FROM Rev8DTOKlassen e WHERE e.Fachklasse_ID IN :value")
@NamedQuery(name="Rev8DTOKlassen.pruefordnung", query="SELECT e FROM Rev8DTOKlassen e WHERE e.PruefOrdnung = :value")
@NamedQuery(name="Rev8DTOKlassen.pruefordnung.multiple", query="SELECT e FROM Rev8DTOKlassen e WHERE e.PruefOrdnung IN :value")
@NamedQuery(name="Rev8DTOKlassen.sichtbar", query="SELECT e FROM Rev8DTOKlassen e WHERE e.Sichtbar = :value")
@NamedQuery(name="Rev8DTOKlassen.sichtbar.multiple", query="SELECT e FROM Rev8DTOKlassen e WHERE e.Sichtbar IN :value")
@NamedQuery(name="Rev8DTOKlassen.sortierung", query="SELECT e FROM Rev8DTOKlassen e WHERE e.Sortierung = :value")
@NamedQuery(name="Rev8DTOKlassen.sortierung.multiple", query="SELECT e FROM Rev8DTOKlassen e WHERE e.Sortierung IN :value")
@NamedQuery(name="Rev8DTOKlassen.klassenart", query="SELECT e FROM Rev8DTOKlassen e WHERE e.Klassenart = :value")
@NamedQuery(name="Rev8DTOKlassen.klassenart.multiple", query="SELECT e FROM Rev8DTOKlassen e WHERE e.Klassenart IN :value")
@NamedQuery(name="Rev8DTOKlassen.sommersem", query="SELECT e FROM Rev8DTOKlassen e WHERE e.SommerSem = :value")
@NamedQuery(name="Rev8DTOKlassen.sommersem.multiple", query="SELECT e FROM Rev8DTOKlassen e WHERE e.SommerSem IN :value")
@NamedQuery(name="Rev8DTOKlassen.notengesperrt", query="SELECT e FROM Rev8DTOKlassen e WHERE e.NotenGesperrt = :value")
@NamedQuery(name="Rev8DTOKlassen.notengesperrt.multiple", query="SELECT e FROM Rev8DTOKlassen e WHERE e.NotenGesperrt IN :value")
@NamedQuery(name="Rev8DTOKlassen.adrmerkmal", query="SELECT e FROM Rev8DTOKlassen e WHERE e.AdrMerkmal = :value")
@NamedQuery(name="Rev8DTOKlassen.adrmerkmal.multiple", query="SELECT e FROM Rev8DTOKlassen e WHERE e.AdrMerkmal IN :value")
@NamedQuery(name="Rev8DTOKlassen.koopklasse", query="SELECT e FROM Rev8DTOKlassen e WHERE e.KoopKlasse = :value")
@NamedQuery(name="Rev8DTOKlassen.koopklasse.multiple", query="SELECT e FROM Rev8DTOKlassen e WHERE e.KoopKlasse IN :value")
@NamedQuery(name="Rev8DTOKlassen.ankreuzzeugnisse", query="SELECT e FROM Rev8DTOKlassen e WHERE e.Ankreuzzeugnisse = :value")
@NamedQuery(name="Rev8DTOKlassen.ankreuzzeugnisse.multiple", query="SELECT e FROM Rev8DTOKlassen e WHERE e.Ankreuzzeugnisse IN :value")
@NamedQuery(name="Rev8DTOKlassen.primaryKeyQuery", query="SELECT e FROM Rev8DTOKlassen e WHERE e.ID = ?1")
@NamedQuery(name="Rev8DTOKlassen.all.migration", query="SELECT e FROM Rev8DTOKlassen e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Schuljahresabschnitts_ID","Bezeichnung","ASDKlasse","Klasse","Jahrgang_ID","FKlasse","VKlasse","OrgFormKrz","ASDSchulformNr","Fachklasse_ID","PruefOrdnung","Sichtbar","Sortierung","Klassenart","SommerSem","NotenGesperrt","AdrMerkmal","KoopKlasse","Ankreuzzeugnisse"})
public class Rev8DTOKlassen {

	/** ID der Klasse in der Klassen- Versetzuungstabelle */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** ID des Schuljahresabschnittes aus der Tabelle Schuljahresabschnitte */
	@Column(name = "Schuljahresabschnitts_ID")
	@JsonProperty
	public Long Schuljahresabschnitts_ID;

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
	@Convert(converter=BooleanPlusMinusDefaultPlusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultPlusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultPlusConverterDeserializer.class)
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
	@Convert(converter=BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean SommerSem;

	/** Noteneingabe für die Klasse gesperrt */
	@Column(name = "NotenGesperrt")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean NotenGesperrt;

	/** Adressmerkmal des Teilstandorts für die Klasse */
	@Column(name = "AdrMerkmal")
	@JsonProperty
	public String AdrMerkmal;

	/** Gibt an ob die Klasse eine KOOP-Klasse ist */
	@Column(name = "KoopKlasse")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean KoopKlasse;

	/** Gibt an ob in der Klasse Ankreuzeugnisse (GS) oder Kompentenzschreiben (andere) verwendet werden */
	@Column(name = "Ankreuzzeugnisse")
	@JsonProperty
	@Convert(converter=BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean Ankreuzzeugnisse;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOKlassen ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev8DTOKlassen() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOKlassen ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Schuljahresabschnitts_ID   der Wert für das Attribut Schuljahresabschnitts_ID
	 * @param Klasse   der Wert für das Attribut Klasse
	 */
	public Rev8DTOKlassen(final Long ID, final Long Schuljahresabschnitts_ID, final String Klasse) {
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Schuljahresabschnitts_ID == null) { 
			throw new NullPointerException("Schuljahresabschnitts_ID must not be null");
		}
		this.Schuljahresabschnitts_ID = Schuljahresabschnitts_ID;
		if (Klasse == null) { 
			throw new NullPointerException("Klasse must not be null");
		}
		this.Klasse = Klasse;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev8DTOKlassen other = (Rev8DTOKlassen) obj;
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
		return "Rev8DTOKlassen(ID=" + this.ID + ", Schuljahresabschnitts_ID=" + this.Schuljahresabschnitts_ID + ", Bezeichnung=" + this.Bezeichnung + ", ASDKlasse=" + this.ASDKlasse + ", Klasse=" + this.Klasse + ", Jahrgang_ID=" + this.Jahrgang_ID + ", FKlasse=" + this.FKlasse + ", VKlasse=" + this.VKlasse + ", OrgFormKrz=" + this.OrgFormKrz + ", ASDSchulformNr=" + this.ASDSchulformNr + ", Fachklasse_ID=" + this.Fachklasse_ID + ", PruefOrdnung=" + this.PruefOrdnung + ", Sichtbar=" + this.Sichtbar + ", Sortierung=" + this.Sortierung + ", Klassenart=" + this.Klassenart + ", SommerSem=" + this.SommerSem + ", NotenGesperrt=" + this.NotenGesperrt + ", AdrMerkmal=" + this.AdrMerkmal + ", KoopKlasse=" + this.KoopKlasse + ", Ankreuzzeugnisse=" + this.Ankreuzzeugnisse + ")";
	}

}