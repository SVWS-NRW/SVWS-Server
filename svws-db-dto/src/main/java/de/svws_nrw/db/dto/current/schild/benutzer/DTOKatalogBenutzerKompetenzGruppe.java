package de.svws_nrw.db.dto.current.schild.benutzer;

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
 * Diese Klasse dient als DTO für die Datenbanktabelle Kompetenzgruppen.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Kompetenzgruppen")
@NamedQuery(name = "DTOKatalogBenutzerKompetenzGruppe.all", query = "SELECT e FROM DTOKatalogBenutzerKompetenzGruppe e")
@NamedQuery(name = "DTOKatalogBenutzerKompetenzGruppe.kg_id", query = "SELECT e FROM DTOKatalogBenutzerKompetenzGruppe e WHERE e.KG_ID = :value")
@NamedQuery(name = "DTOKatalogBenutzerKompetenzGruppe.kg_id.multiple", query = "SELECT e FROM DTOKatalogBenutzerKompetenzGruppe e WHERE e.KG_ID IN :value")
@NamedQuery(name = "DTOKatalogBenutzerKompetenzGruppe.kg_bezeichnung", query = "SELECT e FROM DTOKatalogBenutzerKompetenzGruppe e WHERE e.KG_Bezeichnung = :value")
@NamedQuery(name = "DTOKatalogBenutzerKompetenzGruppe.kg_bezeichnung.multiple", query = "SELECT e FROM DTOKatalogBenutzerKompetenzGruppe e WHERE e.KG_Bezeichnung IN :value")
@NamedQuery(name = "DTOKatalogBenutzerKompetenzGruppe.kg_spalte", query = "SELECT e FROM DTOKatalogBenutzerKompetenzGruppe e WHERE e.KG_Spalte = :value")
@NamedQuery(name = "DTOKatalogBenutzerKompetenzGruppe.kg_spalte.multiple", query = "SELECT e FROM DTOKatalogBenutzerKompetenzGruppe e WHERE e.KG_Spalte IN :value")
@NamedQuery(name = "DTOKatalogBenutzerKompetenzGruppe.kg_zeile", query = "SELECT e FROM DTOKatalogBenutzerKompetenzGruppe e WHERE e.KG_Zeile = :value")
@NamedQuery(name = "DTOKatalogBenutzerKompetenzGruppe.kg_zeile.multiple", query = "SELECT e FROM DTOKatalogBenutzerKompetenzGruppe e WHERE e.KG_Zeile IN :value")
@NamedQuery(name = "DTOKatalogBenutzerKompetenzGruppe.primaryKeyQuery", query = "SELECT e FROM DTOKatalogBenutzerKompetenzGruppe e WHERE e.KG_ID = ?1")
@NamedQuery(name = "DTOKatalogBenutzerKompetenzGruppe.all.migration", query = "SELECT e FROM DTOKatalogBenutzerKompetenzGruppe e WHERE e.KG_ID IS NOT NULL")
@JsonPropertyOrder({"KG_ID", "KG_Bezeichnung", "KG_Spalte", "KG_Zeile"})
public final class DTOKatalogBenutzerKompetenzGruppe {

	/** ID der Kompetenzgruppe */
	@Id
	@Column(name = "KG_ID")
	@JsonProperty
	public long KG_ID;

	/** Bezeichnung der Kompetenzgruppe */
	@Column(name = "KG_Bezeichnung")
	@JsonProperty
	public String KG_Bezeichnung;

	/** Spalte in der Benutzerverwaltung für die Kompetenzgruppe */
	@Column(name = "KG_Spalte")
	@JsonProperty
	public long KG_Spalte;

	/** Zeile in der Benutzerverwaltung für die Kompetenzgruppe */
	@Column(name = "KG_Zeile")
	@JsonProperty
	public long KG_Zeile;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOKatalogBenutzerKompetenzGruppe ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOKatalogBenutzerKompetenzGruppe() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOKatalogBenutzerKompetenzGruppe ohne eine Initialisierung der Attribute.
	 * @param KG_ID   der Wert für das Attribut KG_ID
	 * @param KG_Bezeichnung   der Wert für das Attribut KG_Bezeichnung
	 * @param KG_Spalte   der Wert für das Attribut KG_Spalte
	 * @param KG_Zeile   der Wert für das Attribut KG_Zeile
	 */
	public DTOKatalogBenutzerKompetenzGruppe(final long KG_ID, final String KG_Bezeichnung, final long KG_Spalte, final long KG_Zeile) {
		this.KG_ID = KG_ID;
		if (KG_Bezeichnung == null) {
			throw new NullPointerException("KG_Bezeichnung must not be null");
		}
		this.KG_Bezeichnung = KG_Bezeichnung;
		this.KG_Spalte = KG_Spalte;
		this.KG_Zeile = KG_Zeile;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOKatalogBenutzerKompetenzGruppe other = (DTOKatalogBenutzerKompetenzGruppe) obj;
		if (KG_ID != other.KG_ID)
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(KG_ID);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOKatalogBenutzerKompetenzGruppe(KG_ID=" + this.KG_ID + ", KG_Bezeichnung=" + this.KG_Bezeichnung + ", KG_Spalte=" + this.KG_Spalte + ", KG_Zeile=" + this.KG_Zeile + ")";
	}

}
