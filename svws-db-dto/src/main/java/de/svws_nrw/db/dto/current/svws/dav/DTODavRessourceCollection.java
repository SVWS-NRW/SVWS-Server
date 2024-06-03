package de.svws_nrw.db.dto.current.svws.dav;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.DatumUhrzeitConverter;
import de.svws_nrw.db.converter.current.DavRessourceCollectionTypConverter;

import de.svws_nrw.core.types.dav.DavRessourceCollectionTyp;


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
import de.svws_nrw.csv.converter.current.DatumUhrzeitConverterSerializer;
import de.svws_nrw.csv.converter.current.DatumUhrzeitConverterDeserializer;
import de.svws_nrw.csv.converter.current.DavRessourceCollectionTypConverterSerializer;
import de.svws_nrw.csv.converter.current.DavRessourceCollectionTypConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle DavRessourceCollections.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "DavRessourceCollections")
@JsonPropertyOrder({"Benutzer_ID", "ID", "Typ", "Anzeigename", "Beschreibung", "SyncToken", "geloeschtam"})
public final class DTODavRessourceCollection {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTODavRessourceCollection e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTODavRessourceCollection e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTODavRessourceCollection e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTODavRessourceCollection e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Benutzer_ID */
	public static final String QUERY_BY_BENUTZER_ID = "SELECT e FROM DTODavRessourceCollection e WHERE e.Benutzer_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Benutzer_ID */
	public static final String QUERY_LIST_BY_BENUTZER_ID = "SELECT e FROM DTODavRessourceCollection e WHERE e.Benutzer_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTODavRessourceCollection e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTODavRessourceCollection e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Typ */
	public static final String QUERY_BY_TYP = "SELECT e FROM DTODavRessourceCollection e WHERE e.Typ = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Typ */
	public static final String QUERY_LIST_BY_TYP = "SELECT e FROM DTODavRessourceCollection e WHERE e.Typ IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Anzeigename */
	public static final String QUERY_BY_ANZEIGENAME = "SELECT e FROM DTODavRessourceCollection e WHERE e.Anzeigename = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Anzeigename */
	public static final String QUERY_LIST_BY_ANZEIGENAME = "SELECT e FROM DTODavRessourceCollection e WHERE e.Anzeigename IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Beschreibung */
	public static final String QUERY_BY_BESCHREIBUNG = "SELECT e FROM DTODavRessourceCollection e WHERE e.Beschreibung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Beschreibung */
	public static final String QUERY_LIST_BY_BESCHREIBUNG = "SELECT e FROM DTODavRessourceCollection e WHERE e.Beschreibung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SyncToken */
	public static final String QUERY_BY_SYNCTOKEN = "SELECT e FROM DTODavRessourceCollection e WHERE e.SyncToken = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SyncToken */
	public static final String QUERY_LIST_BY_SYNCTOKEN = "SELECT e FROM DTODavRessourceCollection e WHERE e.SyncToken IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes geloeschtam */
	public static final String QUERY_BY_GELOESCHTAM = "SELECT e FROM DTODavRessourceCollection e WHERE e.geloeschtam = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes geloeschtam */
	public static final String QUERY_LIST_BY_GELOESCHTAM = "SELECT e FROM DTODavRessourceCollection e WHERE e.geloeschtam IN ?1";

	/** Die ID des Benutzers, zu dem der Datensatz gehört */
	@Column(name = "Benutzer_ID")
	@JsonProperty
	public long Benutzer_ID;

	/** ID der WebDav-Ressourcensammlung */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** Gibt den Typ dieser Sammlung an, bspw Adressbuch oder Kalender */
	@Column(name = "Typ")
	@JsonProperty
	@Convert(converter = DavRessourceCollectionTypConverter.class)
	@JsonSerialize(using = DavRessourceCollectionTypConverterSerializer.class)
	@JsonDeserialize(using = DavRessourceCollectionTypConverterDeserializer.class)
	public DavRessourceCollectionTyp Typ;

	/** Der Anzeigename der Ressourcensammlung */
	@Column(name = "Anzeigename")
	@JsonProperty
	public String Anzeigename;

	/** Die Beschreibung der Ressourcensammlung */
	@Column(name = "Beschreibung")
	@JsonProperty
	public String Beschreibung;

	/** Das SyncToken der Ressourcensammlung */
	@Column(name = "SyncToken")
	@JsonProperty
	@Convert(converter = DatumUhrzeitConverter.class)
	@JsonSerialize(using = DatumUhrzeitConverterSerializer.class)
	@JsonDeserialize(using = DatumUhrzeitConverterDeserializer.class)
	public String SyncToken;

	/** Der Zeitpunkt, an dem diese ggf. Ressource gelöscht wurde. */
	@Column(name = "geloeschtam")
	@JsonProperty
	@Convert(converter = DatumUhrzeitConverter.class)
	@JsonSerialize(using = DatumUhrzeitConverterSerializer.class)
	@JsonDeserialize(using = DatumUhrzeitConverterDeserializer.class)
	public String geloeschtam;

	/**
	 * Erstellt ein neues Objekt der Klasse DTODavRessourceCollection ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTODavRessourceCollection() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTODavRessourceCollection ohne eine Initialisierung der Attribute.
	 * @param Benutzer_ID   der Wert für das Attribut Benutzer_ID
	 * @param ID   der Wert für das Attribut ID
	 * @param Typ   der Wert für das Attribut Typ
	 * @param Anzeigename   der Wert für das Attribut Anzeigename
	 * @param SyncToken   der Wert für das Attribut SyncToken
	 */
	public DTODavRessourceCollection(final long Benutzer_ID, final long ID, final DavRessourceCollectionTyp Typ, final String Anzeigename, final String SyncToken) {
		this.Benutzer_ID = Benutzer_ID;
		this.ID = ID;
		this.Typ = Typ;
		if (Anzeigename == null) {
			throw new NullPointerException("Anzeigename must not be null");
		}
		this.Anzeigename = Anzeigename;
		if (SyncToken == null) {
			throw new NullPointerException("SyncToken must not be null");
		}
		this.SyncToken = SyncToken;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTODavRessourceCollection other = (DTODavRessourceCollection) obj;
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
		return "DTODavRessourceCollection(Benutzer_ID=" + this.Benutzer_ID + ", ID=" + this.ID + ", Typ=" + this.Typ + ", Anzeigename=" + this.Anzeigename + ", Beschreibung=" + this.Beschreibung + ", SyncToken=" + this.SyncToken + ", geloeschtam=" + this.geloeschtam + ")";
	}

}
