package de.svws_nrw.db.dto.current.gost;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.Boolean01Converter;


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
import de.svws_nrw.csv.converter.current.Boolean01ConverterSerializer;
import de.svws_nrw.csv.converter.current.Boolean01ConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle Gost_Jahrgangsdaten.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "Gost_Jahrgangsdaten")
@JsonPropertyOrder({"Abi_Jahrgang", "ZusatzkursGEVorhanden", "ZusatzkursGEErstesHalbjahr", "ZusatzkursSWVorhanden", "ZusatzkursSWErstesHalbjahr", "TextBeratungsbogen", "TextMailversand"})
public final class DTOGostJahrgangsdaten {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOGostJahrgangsdaten e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOGostJahrgangsdaten e WHERE e.Abi_Jahrgang = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOGostJahrgangsdaten e WHERE e.Abi_Jahrgang IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOGostJahrgangsdaten e WHERE e.Abi_Jahrgang IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Abi_Jahrgang */
	public static final String QUERY_BY_ABI_JAHRGANG = "SELECT e FROM DTOGostJahrgangsdaten e WHERE e.Abi_Jahrgang = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Abi_Jahrgang */
	public static final String QUERY_LIST_BY_ABI_JAHRGANG = "SELECT e FROM DTOGostJahrgangsdaten e WHERE e.Abi_Jahrgang IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ZusatzkursGEVorhanden */
	public static final String QUERY_BY_ZUSATZKURSGEVORHANDEN = "SELECT e FROM DTOGostJahrgangsdaten e WHERE e.ZusatzkursGEVorhanden = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ZusatzkursGEVorhanden */
	public static final String QUERY_LIST_BY_ZUSATZKURSGEVORHANDEN = "SELECT e FROM DTOGostJahrgangsdaten e WHERE e.ZusatzkursGEVorhanden IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ZusatzkursGEErstesHalbjahr */
	public static final String QUERY_BY_ZUSATZKURSGEERSTESHALBJAHR = "SELECT e FROM DTOGostJahrgangsdaten e WHERE e.ZusatzkursGEErstesHalbjahr = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ZusatzkursGEErstesHalbjahr */
	public static final String QUERY_LIST_BY_ZUSATZKURSGEERSTESHALBJAHR = "SELECT e FROM DTOGostJahrgangsdaten e WHERE e.ZusatzkursGEErstesHalbjahr IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ZusatzkursSWVorhanden */
	public static final String QUERY_BY_ZUSATZKURSSWVORHANDEN = "SELECT e FROM DTOGostJahrgangsdaten e WHERE e.ZusatzkursSWVorhanden = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ZusatzkursSWVorhanden */
	public static final String QUERY_LIST_BY_ZUSATZKURSSWVORHANDEN = "SELECT e FROM DTOGostJahrgangsdaten e WHERE e.ZusatzkursSWVorhanden IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ZusatzkursSWErstesHalbjahr */
	public static final String QUERY_BY_ZUSATZKURSSWERSTESHALBJAHR = "SELECT e FROM DTOGostJahrgangsdaten e WHERE e.ZusatzkursSWErstesHalbjahr = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ZusatzkursSWErstesHalbjahr */
	public static final String QUERY_LIST_BY_ZUSATZKURSSWERSTESHALBJAHR = "SELECT e FROM DTOGostJahrgangsdaten e WHERE e.ZusatzkursSWErstesHalbjahr IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes TextBeratungsbogen */
	public static final String QUERY_BY_TEXTBERATUNGSBOGEN = "SELECT e FROM DTOGostJahrgangsdaten e WHERE e.TextBeratungsbogen = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes TextBeratungsbogen */
	public static final String QUERY_LIST_BY_TEXTBERATUNGSBOGEN = "SELECT e FROM DTOGostJahrgangsdaten e WHERE e.TextBeratungsbogen IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes TextMailversand */
	public static final String QUERY_BY_TEXTMAILVERSAND = "SELECT e FROM DTOGostJahrgangsdaten e WHERE e.TextMailversand = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes TextMailversand */
	public static final String QUERY_LIST_BY_TEXTMAILVERSAND = "SELECT e FROM DTOGostJahrgangsdaten e WHERE e.TextMailversand IN ?1";

	/** Schuljahr, in welchem der Jahrgang das Abitur macht, oder -1 für die Vorlage für das Anlegen neuer Abiturjahrgänge. */
	@Id
	@Column(name = "Abi_Jahrgang")
	@JsonProperty
	public int Abi_Jahrgang;

	/** Legt fest, ob ein Zusatzkurs in Geschichte angeboten wird */
	@Column(name = "ZusatzkursGEVorhanden")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean ZusatzkursGEVorhanden;

	/** Halbjahr, in welchem ein Zusatzkurs in Geschichte beginnt (z.B. Q2.1) */
	@Column(name = "ZusatzkursGEErstesHalbjahr")
	@JsonProperty
	public String ZusatzkursGEErstesHalbjahr;

	/** Legt fest, ob ein Zusatzkurs in Sozialwissenschaften angeboten wird */
	@Column(name = "ZusatzkursSWVorhanden")
	@JsonProperty
	@Convert(converter = Boolean01Converter.class)
	@JsonSerialize(using = Boolean01ConverterSerializer.class)
	@JsonDeserialize(using = Boolean01ConverterDeserializer.class)
	public Boolean ZusatzkursSWVorhanden;

	/** Halbjahr, in welchem ein Zusatzkurs in Sozialwissenschaften beginnt (z.B. Q2.1) */
	@Column(name = "ZusatzkursSWErstesHalbjahr")
	@JsonProperty
	public String ZusatzkursSWErstesHalbjahr;

	/** Text, welcher auf dem Ausdruck eines Beratungsbogens gedruckt wird */
	@Column(name = "TextBeratungsbogen")
	@JsonProperty
	public String TextBeratungsbogen;

	/** Text, welcher in einer Mail beim Versenden von Beratungsdateien verwendet wird */
	@Column(name = "TextMailversand")
	@JsonProperty
	public String TextMailversand;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostJahrgangsdaten ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOGostJahrgangsdaten() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOGostJahrgangsdaten ohne eine Initialisierung der Attribute.
	 * @param Abi_Jahrgang   der Wert für das Attribut Abi_Jahrgang
	 */
	public DTOGostJahrgangsdaten(final int Abi_Jahrgang) {
		this.Abi_Jahrgang = Abi_Jahrgang;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOGostJahrgangsdaten other = (DTOGostJahrgangsdaten) obj;
		return Abi_Jahrgang == other.Abi_Jahrgang;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Integer.hashCode(Abi_Jahrgang);
		return result;
	}


	/**
	 * Konvertiert das Objekt in einen String. Dieser kann z.B. für Debug-Ausgaben genutzt werden.
	 *
	 * @return die String-Repräsentation des Objektes
	 */
	@Override
	public String toString() {
		return "DTOGostJahrgangsdaten(Abi_Jahrgang=" + this.Abi_Jahrgang + ", ZusatzkursGEVorhanden=" + this.ZusatzkursGEVorhanden + ", ZusatzkursGEErstesHalbjahr=" + this.ZusatzkursGEErstesHalbjahr + ", ZusatzkursSWVorhanden=" + this.ZusatzkursSWVorhanden + ", ZusatzkursSWErstesHalbjahr=" + this.ZusatzkursSWErstesHalbjahr + ", TextBeratungsbogen=" + this.TextBeratungsbogen + ", TextMailversand=" + this.TextMailversand + ")";
	}

}
