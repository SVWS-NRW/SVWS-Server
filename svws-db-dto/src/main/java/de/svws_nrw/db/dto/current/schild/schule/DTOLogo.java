package de.svws_nrw.db.dto.current.schild.schule;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.ReportingBildDefinitionConverter;

import de.svws_nrw.core.types.reporting.ReportingBildDefinition;


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
import de.svws_nrw.csv.converter.current.ReportingBildDefinitionConverterSerializer;
import de.svws_nrw.csv.converter.current.ReportingBildDefinitionConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Logo.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Logo")
@JsonPropertyOrder({"id", "kennung", "logoBase64", "hinzugefuegtAm"})
public final class DTOLogo {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOLogo e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOLogo e WHERE e.id = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOLogo e WHERE e.id IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOLogo e WHERE e.id IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes id */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOLogo e WHERE e.id = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes id */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOLogo e WHERE e.id IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes kennung */
	public static final String QUERY_BY_KENNUNG = "SELECT e FROM DTOLogo e WHERE e.kennung = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes kennung */
	public static final String QUERY_LIST_BY_KENNUNG = "SELECT e FROM DTOLogo e WHERE e.kennung IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes logoBase64 */
	public static final String QUERY_BY_LOGOBASE64 = "SELECT e FROM DTOLogo e WHERE e.logoBase64 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes logoBase64 */
	public static final String QUERY_LIST_BY_LOGOBASE64 = "SELECT e FROM DTOLogo e WHERE e.logoBase64 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes hinzugefuegtAm */
	public static final String QUERY_BY_HINZUGEFUEGTAM = "SELECT e FROM DTOLogo e WHERE e.hinzugefuegtAm = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes hinzugefuegtAm */
	public static final String QUERY_LIST_BY_HINZUGEFUEGTAM = "SELECT e FROM DTOLogo e WHERE e.hinzugefuegtAm IN ?1";

	/** Die ID des Logos */
	@Id
	@Column(name = "id")
	@JsonProperty
	public long id;

	/** Kennung des Logos */
	@Column(name = "kennung")
	@JsonProperty
	@Convert(converter = ReportingBildDefinitionConverter.class)
	@JsonSerialize(using = ReportingBildDefinitionConverterSerializer.class)
	@JsonDeserialize(using = ReportingBildDefinitionConverterDeserializer.class)
	public ReportingBildDefinition kennung;

	/** Das Logo der Schule als Bild im Base64-Format */
	@Column(name = "logoBase64")
	@JsonProperty
	public String logoBase64;

	/** Datum des Hinzufügens des Bildes */
	@Column(name = "hinzugefuegtAm")
	@JsonProperty
	public String hinzugefuegtAm;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOLogo ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOLogo() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOLogo ohne eine Initialisierung der Attribute.
	 * @param id   der Wert für das Attribut id
	 * @param kennung   der Wert für das Attribut kennung
	 * @param logoBase64   der Wert für das Attribut logoBase64
	 * @param hinzugefuegtAm   der Wert für das Attribut hinzugefuegtAm
	 */
	public DTOLogo(final long id, final ReportingBildDefinition kennung, final String logoBase64, final String hinzugefuegtAm) {
		this.id = id;
		if (kennung == null) {
			throw new NullPointerException("kennung must not be null");
		}
		this.kennung = kennung;
		if (logoBase64 == null) {
			throw new NullPointerException("logoBase64 must not be null");
		}
		this.logoBase64 = logoBase64;
		if (hinzugefuegtAm == null) {
			throw new NullPointerException("hinzugefuegtAm must not be null");
		}
		this.hinzugefuegtAm = hinzugefuegtAm;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		DTOLogo other = (DTOLogo) obj;
		return id == other.id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Long.hashCode(id);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOLogo(id=" + this.id + ", kennung=" + this.kennung + ", logoBase64=" + this.logoBase64 + ", hinzugefuegtAm=" + this.hinzugefuegtAm + ")";
	}

}
