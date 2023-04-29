package de.svws_nrw.db.dto.current.gost;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.Boolean01Converter;


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
@NamedQuery(name = "DTOGostJahrgangsdaten.all", query = "SELECT e FROM DTOGostJahrgangsdaten e")
@NamedQuery(name = "DTOGostJahrgangsdaten.abi_jahrgang", query = "SELECT e FROM DTOGostJahrgangsdaten e WHERE e.Abi_Jahrgang = :value")
@NamedQuery(name = "DTOGostJahrgangsdaten.abi_jahrgang.multiple", query = "SELECT e FROM DTOGostJahrgangsdaten e WHERE e.Abi_Jahrgang IN :value")
@NamedQuery(name = "DTOGostJahrgangsdaten.zusatzkursgevorhanden", query = "SELECT e FROM DTOGostJahrgangsdaten e WHERE e.ZusatzkursGEVorhanden = :value")
@NamedQuery(name = "DTOGostJahrgangsdaten.zusatzkursgevorhanden.multiple", query = "SELECT e FROM DTOGostJahrgangsdaten e WHERE e.ZusatzkursGEVorhanden IN :value")
@NamedQuery(name = "DTOGostJahrgangsdaten.zusatzkursgeersteshalbjahr", query = "SELECT e FROM DTOGostJahrgangsdaten e WHERE e.ZusatzkursGEErstesHalbjahr = :value")
@NamedQuery(name = "DTOGostJahrgangsdaten.zusatzkursgeersteshalbjahr.multiple", query = "SELECT e FROM DTOGostJahrgangsdaten e WHERE e.ZusatzkursGEErstesHalbjahr IN :value")
@NamedQuery(name = "DTOGostJahrgangsdaten.zusatzkursswvorhanden", query = "SELECT e FROM DTOGostJahrgangsdaten e WHERE e.ZusatzkursSWVorhanden = :value")
@NamedQuery(name = "DTOGostJahrgangsdaten.zusatzkursswvorhanden.multiple", query = "SELECT e FROM DTOGostJahrgangsdaten e WHERE e.ZusatzkursSWVorhanden IN :value")
@NamedQuery(name = "DTOGostJahrgangsdaten.zusatzkursswersteshalbjahr", query = "SELECT e FROM DTOGostJahrgangsdaten e WHERE e.ZusatzkursSWErstesHalbjahr = :value")
@NamedQuery(name = "DTOGostJahrgangsdaten.zusatzkursswersteshalbjahr.multiple", query = "SELECT e FROM DTOGostJahrgangsdaten e WHERE e.ZusatzkursSWErstesHalbjahr IN :value")
@NamedQuery(name = "DTOGostJahrgangsdaten.textberatungsbogen", query = "SELECT e FROM DTOGostJahrgangsdaten e WHERE e.TextBeratungsbogen = :value")
@NamedQuery(name = "DTOGostJahrgangsdaten.textberatungsbogen.multiple", query = "SELECT e FROM DTOGostJahrgangsdaten e WHERE e.TextBeratungsbogen IN :value")
@NamedQuery(name = "DTOGostJahrgangsdaten.textmailversand", query = "SELECT e FROM DTOGostJahrgangsdaten e WHERE e.TextMailversand = :value")
@NamedQuery(name = "DTOGostJahrgangsdaten.textmailversand.multiple", query = "SELECT e FROM DTOGostJahrgangsdaten e WHERE e.TextMailversand IN :value")
@NamedQuery(name = "DTOGostJahrgangsdaten.primaryKeyQuery", query = "SELECT e FROM DTOGostJahrgangsdaten e WHERE e.Abi_Jahrgang = ?1")
@NamedQuery(name = "DTOGostJahrgangsdaten.all.migration", query = "SELECT e FROM DTOGostJahrgangsdaten e WHERE e.Abi_Jahrgang IS NOT NULL")
@JsonPropertyOrder({"Abi_Jahrgang", "ZusatzkursGEVorhanden", "ZusatzkursGEErstesHalbjahr", "ZusatzkursSWVorhanden", "ZusatzkursSWErstesHalbjahr", "TextBeratungsbogen", "TextMailversand"})
public final class DTOGostJahrgangsdaten {

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
