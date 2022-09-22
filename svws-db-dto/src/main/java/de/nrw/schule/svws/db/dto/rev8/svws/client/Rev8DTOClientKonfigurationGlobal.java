package de.nrw.schule.svws.db.dto.rev8.svws.client;

import de.nrw.schule.svws.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SVWS_Client_Konfiguration_Global.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@IdClass(Rev8DTOClientKonfigurationGlobalPK.class)
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SVWS_Client_Konfiguration_Global")
@NamedQuery(name="Rev8DTOClientKonfigurationGlobal.all", query="SELECT e FROM Rev8DTOClientKonfigurationGlobal e")
@NamedQuery(name="Rev8DTOClientKonfigurationGlobal.appname", query="SELECT e FROM Rev8DTOClientKonfigurationGlobal e WHERE e.AppName = :value")
@NamedQuery(name="Rev8DTOClientKonfigurationGlobal.appname.multiple", query="SELECT e FROM Rev8DTOClientKonfigurationGlobal e WHERE e.AppName IN :value")
@NamedQuery(name="Rev8DTOClientKonfigurationGlobal.schluessel", query="SELECT e FROM Rev8DTOClientKonfigurationGlobal e WHERE e.Schluessel = :value")
@NamedQuery(name="Rev8DTOClientKonfigurationGlobal.schluessel.multiple", query="SELECT e FROM Rev8DTOClientKonfigurationGlobal e WHERE e.Schluessel IN :value")
@NamedQuery(name="Rev8DTOClientKonfigurationGlobal.wert", query="SELECT e FROM Rev8DTOClientKonfigurationGlobal e WHERE e.Wert = :value")
@NamedQuery(name="Rev8DTOClientKonfigurationGlobal.wert.multiple", query="SELECT e FROM Rev8DTOClientKonfigurationGlobal e WHERE e.Wert IN :value")
@NamedQuery(name="Rev8DTOClientKonfigurationGlobal.primaryKeyQuery", query="SELECT e FROM Rev8DTOClientKonfigurationGlobal e WHERE e.AppName = ?1 AND e.Schluessel = ?2")
@NamedQuery(name="Rev8DTOClientKonfigurationGlobal.all.migration", query="SELECT e FROM Rev8DTOClientKonfigurationGlobal e WHERE e.AppName IS NOT NULL AND e.Schluessel IS NOT NULL")
@JsonPropertyOrder({"AppName","Schluessel","Wert"})
public class Rev8DTOClientKonfigurationGlobal {

	/** Der Name der Client-Anwendung, für die der Konfigurationsdatensatz gespeichert ist */
	@Id
	@Column(name = "AppName")
	@JsonProperty
	public String AppName;

	/** Der Schlüsselname des Konfigurationsdatensatzes */
	@Id
	@Column(name = "Schluessel")
	@JsonProperty
	public String Schluessel;

	/** Der Wert des Konfigurationsdatensatzes */
	@Column(name = "Wert")
	@JsonProperty
	public String Wert;

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOClientKonfigurationGlobal ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private Rev8DTOClientKonfigurationGlobal() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse Rev8DTOClientKonfigurationGlobal ohne eine Initialisierung der Attribute.
	 * @param AppName   der Wert für das Attribut AppName
	 * @param Schluessel   der Wert für das Attribut Schluessel
	 * @param Wert   der Wert für das Attribut Wert
	 */
	public Rev8DTOClientKonfigurationGlobal(final String AppName, final String Schluessel, final String Wert) {
		if (AppName == null) { 
			throw new NullPointerException("AppName must not be null");
		}
		this.AppName = AppName;
		if (Schluessel == null) { 
			throw new NullPointerException("Schluessel must not be null");
		}
		this.Schluessel = Schluessel;
		if (Wert == null) { 
			throw new NullPointerException("Wert must not be null");
		}
		this.Wert = Wert;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rev8DTOClientKonfigurationGlobal other = (Rev8DTOClientKonfigurationGlobal) obj;
		if (AppName == null) {
			if (other.AppName != null)
				return false;
		} else if (!AppName.equals(other.AppName))
			return false;

		if (Schluessel == null) {
			if (other.Schluessel != null)
				return false;
		} else if (!Schluessel.equals(other.Schluessel))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((AppName == null) ? 0 : AppName.hashCode());

		result = prime * result + ((Schluessel == null) ? 0 : Schluessel.hashCode());
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "Rev8DTOClientKonfigurationGlobal(AppName=" + this.AppName + ", Schluessel=" + this.Schluessel + ", Wert=" + this.Wert + ")";
	}

}