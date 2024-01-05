package de.svws_nrw.db.dto.current.schild.schule;

import de.svws_nrw.db.DBEntityManager;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * Diese Klasse dient als DTO für die Datenbanktabelle EigeneSchule_Logo.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "EigeneSchule_Logo")
@NamedQuery(name = "DTOEigeneSchuleLogo.all", query = "SELECT e FROM DTOEigeneSchuleLogo e")
@NamedQuery(name = "DTOEigeneSchuleLogo.eigeneschule_id", query = "SELECT e FROM DTOEigeneSchuleLogo e WHERE e.EigeneSchule_ID = :value")
@NamedQuery(name = "DTOEigeneSchuleLogo.eigeneschule_id.multiple", query = "SELECT e FROM DTOEigeneSchuleLogo e WHERE e.EigeneSchule_ID IN :value")
@NamedQuery(name = "DTOEigeneSchuleLogo.logobase64", query = "SELECT e FROM DTOEigeneSchuleLogo e WHERE e.LogoBase64 = :value")
@NamedQuery(name = "DTOEigeneSchuleLogo.logobase64.multiple", query = "SELECT e FROM DTOEigeneSchuleLogo e WHERE e.LogoBase64 IN :value")
@NamedQuery(name = "DTOEigeneSchuleLogo.primaryKeyQuery", query = "SELECT e FROM DTOEigeneSchuleLogo e WHERE e.EigeneSchule_ID = ?1")
@NamedQuery(name = "DTOEigeneSchuleLogo.primaryKeyQuery.multiple", query = "SELECT e FROM DTOEigeneSchuleLogo e WHERE e.EigeneSchule_ID IN :value")
@NamedQuery(name = "DTOEigeneSchuleLogo.all.migration", query = "SELECT e FROM DTOEigeneSchuleLogo e WHERE e.EigeneSchule_ID IS NOT NULL")
@JsonPropertyOrder({"EigeneSchule_ID", "LogoBase64"})
public final class DTOEigeneSchuleLogo {

	/** ID des Datensatzes der eigenen Schule */
	@Id
	@Column(name = "EigeneSchule_ID")
	@JsonProperty
	public long EigeneSchule_ID;

	/** Das Logo der Schule als Bild im Base64-Format */
	@Column(name = "LogoBase64")
	@JsonProperty
	public String LogoBase64;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOEigeneSchuleLogo ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOEigeneSchuleLogo() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOEigeneSchuleLogo ohne eine Initialisierung der Attribute.
	 * @param EigeneSchule_ID   der Wert für das Attribut EigeneSchule_ID
	 */
	public DTOEigeneSchuleLogo(final long EigeneSchule_ID) {
		this.EigeneSchule_ID = EigeneSchule_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOEigeneSchuleLogo other = (DTOEigeneSchuleLogo) obj;
		return EigeneSchule_ID == other.EigeneSchule_ID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(EigeneSchule_ID);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOEigeneSchuleLogo(EigeneSchule_ID=" + this.EigeneSchule_ID + ", LogoBase64=" + this.LogoBase64 + ")";
	}

}
