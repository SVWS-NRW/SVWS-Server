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
import jakarta.persistence.NamedQuery;
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
@NamedQuery(name="DTODavRessourceCollection.all", query="SELECT e FROM DTODavRessourceCollection e")
@NamedQuery(name="DTODavRessourceCollection.benutzer_id", query="SELECT e FROM DTODavRessourceCollection e WHERE e.Benutzer_ID = :value")
@NamedQuery(name="DTODavRessourceCollection.benutzer_id.multiple", query="SELECT e FROM DTODavRessourceCollection e WHERE e.Benutzer_ID IN :value")
@NamedQuery(name="DTODavRessourceCollection.id", query="SELECT e FROM DTODavRessourceCollection e WHERE e.ID = :value")
@NamedQuery(name="DTODavRessourceCollection.id.multiple", query="SELECT e FROM DTODavRessourceCollection e WHERE e.ID IN :value")
@NamedQuery(name="DTODavRessourceCollection.typ", query="SELECT e FROM DTODavRessourceCollection e WHERE e.Typ = :value")
@NamedQuery(name="DTODavRessourceCollection.typ.multiple", query="SELECT e FROM DTODavRessourceCollection e WHERE e.Typ IN :value")
@NamedQuery(name="DTODavRessourceCollection.anzeigename", query="SELECT e FROM DTODavRessourceCollection e WHERE e.Anzeigename = :value")
@NamedQuery(name="DTODavRessourceCollection.anzeigename.multiple", query="SELECT e FROM DTODavRessourceCollection e WHERE e.Anzeigename IN :value")
@NamedQuery(name="DTODavRessourceCollection.beschreibung", query="SELECT e FROM DTODavRessourceCollection e WHERE e.Beschreibung = :value")
@NamedQuery(name="DTODavRessourceCollection.beschreibung.multiple", query="SELECT e FROM DTODavRessourceCollection e WHERE e.Beschreibung IN :value")
@NamedQuery(name="DTODavRessourceCollection.synctoken", query="SELECT e FROM DTODavRessourceCollection e WHERE e.SyncToken = :value")
@NamedQuery(name="DTODavRessourceCollection.synctoken.multiple", query="SELECT e FROM DTODavRessourceCollection e WHERE e.SyncToken IN :value")
@NamedQuery(name="DTODavRessourceCollection.geloeschtam", query="SELECT e FROM DTODavRessourceCollection e WHERE e.geloeschtam = :value")
@NamedQuery(name="DTODavRessourceCollection.geloeschtam.multiple", query="SELECT e FROM DTODavRessourceCollection e WHERE e.geloeschtam IN :value")
@NamedQuery(name="DTODavRessourceCollection.primaryKeyQuery", query="SELECT e FROM DTODavRessourceCollection e WHERE e.ID = ?1")
@NamedQuery(name="DTODavRessourceCollection.all.migration", query="SELECT e FROM DTODavRessourceCollection e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"Benutzer_ID","ID","Typ","Anzeigename","Beschreibung","SyncToken","geloeschtam"})
public class DTODavRessourceCollection {

	/** Die ID des Benutzers, zu dem der Datensatz gehört */
	@Column(name = "Benutzer_ID")
	@JsonProperty
	public Long Benutzer_ID;

	/** ID der WebDav-Ressourcensammlung */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** Gibt den Typ dieser Sammlung an, bspw Adressbuch oder Kalender */
	@Column(name = "Typ")
	@JsonProperty
	@Convert(converter=DavRessourceCollectionTypConverter.class)
	@JsonSerialize(using=DavRessourceCollectionTypConverterSerializer.class)
	@JsonDeserialize(using=DavRessourceCollectionTypConverterDeserializer.class)
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
	@Convert(converter=DatumUhrzeitConverter.class)
	@JsonSerialize(using=DatumUhrzeitConverterSerializer.class)
	@JsonDeserialize(using=DatumUhrzeitConverterDeserializer.class)
	public String SyncToken;

	/** Der Zeitpunkt, an dem diese ggf. Ressource gelöscht wurde. */
	@Column(name = "geloeschtam")
	@JsonProperty
	@Convert(converter=DatumUhrzeitConverter.class)
	@JsonSerialize(using=DatumUhrzeitConverterSerializer.class)
	@JsonDeserialize(using=DatumUhrzeitConverterDeserializer.class)
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
	public DTODavRessourceCollection(final Long Benutzer_ID, final Long ID, final DavRessourceCollectionTyp Typ, final String Anzeigename, final String SyncToken) {
		if (Benutzer_ID == null) { 
			throw new NullPointerException("Benutzer_ID must not be null");
		}
		this.Benutzer_ID = Benutzer_ID;
		if (ID == null) { 
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Typ == null) { 
			throw new NullPointerException("Typ must not be null");
		}
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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTODavRessourceCollection other = (DTODavRessourceCollection) obj;
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
		return "DTODavRessourceCollection(Benutzer_ID=" + this.Benutzer_ID + ", ID=" + this.ID + ", Typ=" + this.Typ + ", Anzeigename=" + this.Anzeigename + ", Beschreibung=" + this.Beschreibung + ", SyncToken=" + this.SyncToken + ", geloeschtam=" + this.geloeschtam + ")";
	}

}