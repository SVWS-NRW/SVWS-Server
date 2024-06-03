package de.svws_nrw.db.dto.current.schild.schueler.fhr;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.current.BooleanPlusMinusDefaultMinusConverter;


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
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultMinusConverterSerializer;
import de.svws_nrw.csv.converter.current.BooleanPlusMinusDefaultMinusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerFHR.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerFHR")
@JsonPropertyOrder({"ID", "Schueler_ID", "FHRErreicht", "Note", "GesamtPunktzahl", "SummeGK", "SummeLK", "SummenOK", "AnzRelLK", "AnzRelGK", "AnzRelOK", "AnzDefLK", "AnzDefGK", "AnzDefOK", "JSII_2_1", "JSII_2_1_W", "JSII_2_2", "JSII_2_2_W", "JSII_3_1", "JSII_3_1_W", "JSII_3_2", "JSII_3_2_W", "ASII_2_1", "ASII_2_2", "ASII_2_1_W", "ASII_2_2_W", "ASII_3_1", "ASII_3_2", "ASII_3_1_W", "ASII_3_2_W", "WSII_2_1", "WSII_2_2", "WSII_2_1_W", "WSII_2_2_W", "WSII_3_1", "WSII_3_2", "WSII_3_1_W", "WSII_3_2_W"})
public final class DTOSchuelerFHR {

	/** Die Datenbankabfrage für alle DTOs */
	public static final String QUERY_ALL = "SELECT e FROM DTOSchuelerFHR e";

	/** Die Datenbankabfrage für DTOs anhand der Primärschlüsselattribute */
	public static final String QUERY_PK = "SELECT e FROM DTOSchuelerFHR e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Primärschlüsselattributwerten */
	public static final String QUERY_LIST_PK = "SELECT e FROM DTOSchuelerFHR e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für alle DTOs im Rahmen der Migration, wobei die Einträge entfernt werden, die nicht der Primärschlüssel-Constraint entsprechen */
	public static final String QUERY_MIGRATION_ALL = "SELECT e FROM DTOSchuelerFHR e WHERE e.ID IS NOT NULL";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ID */
	public static final String QUERY_BY_ID = "SELECT e FROM DTOSchuelerFHR e WHERE e.ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ID */
	public static final String QUERY_LIST_BY_ID = "SELECT e FROM DTOSchuelerFHR e WHERE e.ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Schueler_ID */
	public static final String QUERY_BY_SCHUELER_ID = "SELECT e FROM DTOSchuelerFHR e WHERE e.Schueler_ID = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Schueler_ID */
	public static final String QUERY_LIST_BY_SCHUELER_ID = "SELECT e FROM DTOSchuelerFHR e WHERE e.Schueler_ID IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes FHRErreicht */
	public static final String QUERY_BY_FHRERREICHT = "SELECT e FROM DTOSchuelerFHR e WHERE e.FHRErreicht = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes FHRErreicht */
	public static final String QUERY_LIST_BY_FHRERREICHT = "SELECT e FROM DTOSchuelerFHR e WHERE e.FHRErreicht IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes Note */
	public static final String QUERY_BY_NOTE = "SELECT e FROM DTOSchuelerFHR e WHERE e.Note = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes Note */
	public static final String QUERY_LIST_BY_NOTE = "SELECT e FROM DTOSchuelerFHR e WHERE e.Note IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes GesamtPunktzahl */
	public static final String QUERY_BY_GESAMTPUNKTZAHL = "SELECT e FROM DTOSchuelerFHR e WHERE e.GesamtPunktzahl = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes GesamtPunktzahl */
	public static final String QUERY_LIST_BY_GESAMTPUNKTZAHL = "SELECT e FROM DTOSchuelerFHR e WHERE e.GesamtPunktzahl IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SummeGK */
	public static final String QUERY_BY_SUMMEGK = "SELECT e FROM DTOSchuelerFHR e WHERE e.SummeGK = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SummeGK */
	public static final String QUERY_LIST_BY_SUMMEGK = "SELECT e FROM DTOSchuelerFHR e WHERE e.SummeGK IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SummeLK */
	public static final String QUERY_BY_SUMMELK = "SELECT e FROM DTOSchuelerFHR e WHERE e.SummeLK = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SummeLK */
	public static final String QUERY_LIST_BY_SUMMELK = "SELECT e FROM DTOSchuelerFHR e WHERE e.SummeLK IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes SummenOK */
	public static final String QUERY_BY_SUMMENOK = "SELECT e FROM DTOSchuelerFHR e WHERE e.SummenOK = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes SummenOK */
	public static final String QUERY_LIST_BY_SUMMENOK = "SELECT e FROM DTOSchuelerFHR e WHERE e.SummenOK IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AnzRelLK */
	public static final String QUERY_BY_ANZRELLK = "SELECT e FROM DTOSchuelerFHR e WHERE e.AnzRelLK = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AnzRelLK */
	public static final String QUERY_LIST_BY_ANZRELLK = "SELECT e FROM DTOSchuelerFHR e WHERE e.AnzRelLK IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AnzRelGK */
	public static final String QUERY_BY_ANZRELGK = "SELECT e FROM DTOSchuelerFHR e WHERE e.AnzRelGK = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AnzRelGK */
	public static final String QUERY_LIST_BY_ANZRELGK = "SELECT e FROM DTOSchuelerFHR e WHERE e.AnzRelGK IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AnzRelOK */
	public static final String QUERY_BY_ANZRELOK = "SELECT e FROM DTOSchuelerFHR e WHERE e.AnzRelOK = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AnzRelOK */
	public static final String QUERY_LIST_BY_ANZRELOK = "SELECT e FROM DTOSchuelerFHR e WHERE e.AnzRelOK IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AnzDefLK */
	public static final String QUERY_BY_ANZDEFLK = "SELECT e FROM DTOSchuelerFHR e WHERE e.AnzDefLK = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AnzDefLK */
	public static final String QUERY_LIST_BY_ANZDEFLK = "SELECT e FROM DTOSchuelerFHR e WHERE e.AnzDefLK IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AnzDefGK */
	public static final String QUERY_BY_ANZDEFGK = "SELECT e FROM DTOSchuelerFHR e WHERE e.AnzDefGK = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AnzDefGK */
	public static final String QUERY_LIST_BY_ANZDEFGK = "SELECT e FROM DTOSchuelerFHR e WHERE e.AnzDefGK IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes AnzDefOK */
	public static final String QUERY_BY_ANZDEFOK = "SELECT e FROM DTOSchuelerFHR e WHERE e.AnzDefOK = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes AnzDefOK */
	public static final String QUERY_LIST_BY_ANZDEFOK = "SELECT e FROM DTOSchuelerFHR e WHERE e.AnzDefOK IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes JSII_2_1 */
	public static final String QUERY_BY_JSII_2_1 = "SELECT e FROM DTOSchuelerFHR e WHERE e.JSII_2_1 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes JSII_2_1 */
	public static final String QUERY_LIST_BY_JSII_2_1 = "SELECT e FROM DTOSchuelerFHR e WHERE e.JSII_2_1 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes JSII_2_1_W */
	public static final String QUERY_BY_JSII_2_1_W = "SELECT e FROM DTOSchuelerFHR e WHERE e.JSII_2_1_W = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes JSII_2_1_W */
	public static final String QUERY_LIST_BY_JSII_2_1_W = "SELECT e FROM DTOSchuelerFHR e WHERE e.JSII_2_1_W IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes JSII_2_2 */
	public static final String QUERY_BY_JSII_2_2 = "SELECT e FROM DTOSchuelerFHR e WHERE e.JSII_2_2 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes JSII_2_2 */
	public static final String QUERY_LIST_BY_JSII_2_2 = "SELECT e FROM DTOSchuelerFHR e WHERE e.JSII_2_2 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes JSII_2_2_W */
	public static final String QUERY_BY_JSII_2_2_W = "SELECT e FROM DTOSchuelerFHR e WHERE e.JSII_2_2_W = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes JSII_2_2_W */
	public static final String QUERY_LIST_BY_JSII_2_2_W = "SELECT e FROM DTOSchuelerFHR e WHERE e.JSII_2_2_W IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes JSII_3_1 */
	public static final String QUERY_BY_JSII_3_1 = "SELECT e FROM DTOSchuelerFHR e WHERE e.JSII_3_1 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes JSII_3_1 */
	public static final String QUERY_LIST_BY_JSII_3_1 = "SELECT e FROM DTOSchuelerFHR e WHERE e.JSII_3_1 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes JSII_3_1_W */
	public static final String QUERY_BY_JSII_3_1_W = "SELECT e FROM DTOSchuelerFHR e WHERE e.JSII_3_1_W = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes JSII_3_1_W */
	public static final String QUERY_LIST_BY_JSII_3_1_W = "SELECT e FROM DTOSchuelerFHR e WHERE e.JSII_3_1_W IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes JSII_3_2 */
	public static final String QUERY_BY_JSII_3_2 = "SELECT e FROM DTOSchuelerFHR e WHERE e.JSII_3_2 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes JSII_3_2 */
	public static final String QUERY_LIST_BY_JSII_3_2 = "SELECT e FROM DTOSchuelerFHR e WHERE e.JSII_3_2 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes JSII_3_2_W */
	public static final String QUERY_BY_JSII_3_2_W = "SELECT e FROM DTOSchuelerFHR e WHERE e.JSII_3_2_W = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes JSII_3_2_W */
	public static final String QUERY_LIST_BY_JSII_3_2_W = "SELECT e FROM DTOSchuelerFHR e WHERE e.JSII_3_2_W IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ASII_2_1 */
	public static final String QUERY_BY_ASII_2_1 = "SELECT e FROM DTOSchuelerFHR e WHERE e.ASII_2_1 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ASII_2_1 */
	public static final String QUERY_LIST_BY_ASII_2_1 = "SELECT e FROM DTOSchuelerFHR e WHERE e.ASII_2_1 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ASII_2_2 */
	public static final String QUERY_BY_ASII_2_2 = "SELECT e FROM DTOSchuelerFHR e WHERE e.ASII_2_2 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ASII_2_2 */
	public static final String QUERY_LIST_BY_ASII_2_2 = "SELECT e FROM DTOSchuelerFHR e WHERE e.ASII_2_2 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ASII_2_1_W */
	public static final String QUERY_BY_ASII_2_1_W = "SELECT e FROM DTOSchuelerFHR e WHERE e.ASII_2_1_W = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ASII_2_1_W */
	public static final String QUERY_LIST_BY_ASII_2_1_W = "SELECT e FROM DTOSchuelerFHR e WHERE e.ASII_2_1_W IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ASII_2_2_W */
	public static final String QUERY_BY_ASII_2_2_W = "SELECT e FROM DTOSchuelerFHR e WHERE e.ASII_2_2_W = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ASII_2_2_W */
	public static final String QUERY_LIST_BY_ASII_2_2_W = "SELECT e FROM DTOSchuelerFHR e WHERE e.ASII_2_2_W IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ASII_3_1 */
	public static final String QUERY_BY_ASII_3_1 = "SELECT e FROM DTOSchuelerFHR e WHERE e.ASII_3_1 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ASII_3_1 */
	public static final String QUERY_LIST_BY_ASII_3_1 = "SELECT e FROM DTOSchuelerFHR e WHERE e.ASII_3_1 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ASII_3_2 */
	public static final String QUERY_BY_ASII_3_2 = "SELECT e FROM DTOSchuelerFHR e WHERE e.ASII_3_2 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ASII_3_2 */
	public static final String QUERY_LIST_BY_ASII_3_2 = "SELECT e FROM DTOSchuelerFHR e WHERE e.ASII_3_2 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ASII_3_1_W */
	public static final String QUERY_BY_ASII_3_1_W = "SELECT e FROM DTOSchuelerFHR e WHERE e.ASII_3_1_W = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ASII_3_1_W */
	public static final String QUERY_LIST_BY_ASII_3_1_W = "SELECT e FROM DTOSchuelerFHR e WHERE e.ASII_3_1_W IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes ASII_3_2_W */
	public static final String QUERY_BY_ASII_3_2_W = "SELECT e FROM DTOSchuelerFHR e WHERE e.ASII_3_2_W = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes ASII_3_2_W */
	public static final String QUERY_LIST_BY_ASII_3_2_W = "SELECT e FROM DTOSchuelerFHR e WHERE e.ASII_3_2_W IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes WSII_2_1 */
	public static final String QUERY_BY_WSII_2_1 = "SELECT e FROM DTOSchuelerFHR e WHERE e.WSII_2_1 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes WSII_2_1 */
	public static final String QUERY_LIST_BY_WSII_2_1 = "SELECT e FROM DTOSchuelerFHR e WHERE e.WSII_2_1 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes WSII_2_2 */
	public static final String QUERY_BY_WSII_2_2 = "SELECT e FROM DTOSchuelerFHR e WHERE e.WSII_2_2 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes WSII_2_2 */
	public static final String QUERY_LIST_BY_WSII_2_2 = "SELECT e FROM DTOSchuelerFHR e WHERE e.WSII_2_2 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes WSII_2_1_W */
	public static final String QUERY_BY_WSII_2_1_W = "SELECT e FROM DTOSchuelerFHR e WHERE e.WSII_2_1_W = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes WSII_2_1_W */
	public static final String QUERY_LIST_BY_WSII_2_1_W = "SELECT e FROM DTOSchuelerFHR e WHERE e.WSII_2_1_W IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes WSII_2_2_W */
	public static final String QUERY_BY_WSII_2_2_W = "SELECT e FROM DTOSchuelerFHR e WHERE e.WSII_2_2_W = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes WSII_2_2_W */
	public static final String QUERY_LIST_BY_WSII_2_2_W = "SELECT e FROM DTOSchuelerFHR e WHERE e.WSII_2_2_W IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes WSII_3_1 */
	public static final String QUERY_BY_WSII_3_1 = "SELECT e FROM DTOSchuelerFHR e WHERE e.WSII_3_1 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes WSII_3_1 */
	public static final String QUERY_LIST_BY_WSII_3_1 = "SELECT e FROM DTOSchuelerFHR e WHERE e.WSII_3_1 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes WSII_3_2 */
	public static final String QUERY_BY_WSII_3_2 = "SELECT e FROM DTOSchuelerFHR e WHERE e.WSII_3_2 = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes WSII_3_2 */
	public static final String QUERY_LIST_BY_WSII_3_2 = "SELECT e FROM DTOSchuelerFHR e WHERE e.WSII_3_2 IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes WSII_3_1_W */
	public static final String QUERY_BY_WSII_3_1_W = "SELECT e FROM DTOSchuelerFHR e WHERE e.WSII_3_1_W = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes WSII_3_1_W */
	public static final String QUERY_LIST_BY_WSII_3_1_W = "SELECT e FROM DTOSchuelerFHR e WHERE e.WSII_3_1_W IN ?1";

	/** Die Datenbankabfrage für DTOs anhand des Attributes WSII_3_2_W */
	public static final String QUERY_BY_WSII_3_2_W = "SELECT e FROM DTOSchuelerFHR e WHERE e.WSII_3_2_W = ?1";

	/** Die Datenbankabfrage für DTOs anhand einer Liste von Werten des Attributes WSII_3_2_W */
	public static final String QUERY_LIST_BY_WSII_3_2_W = "SELECT e FROM DTOSchuelerFHR e WHERE e.WSII_3_2_W IN ?1";

	/** ID des FHR Datensatzes */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public long ID;

	/** ID des Schülers zum FHR Datensatz */
	@Column(name = "Schueler_ID")
	@JsonProperty
	public long Schueler_ID;

	/** Gibt an ob das FHR erreicht wurde */
	@Column(name = "FHRErreicht")
	@JsonProperty
	@Convert(converter = BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = BooleanPlusMinusDefaultMinusConverterDeserializer.class)
	public Boolean FHRErreicht;

	/** Gesamtnote FHR */
	@Column(name = "Note")
	@JsonProperty
	public String Note;

	/** Gesamtpunktzahl FHR */
	@Column(name = "GesamtPunktzahl")
	@JsonProperty
	public Integer GesamtPunktzahl;

	/** Summer der Grundkurse FHR */
	@Column(name = "SummeGK")
	@JsonProperty
	public Integer SummeGK;

	/** Summer der Leistungskurse FHR */
	@Column(name = "SummeLK")
	@JsonProperty
	public Integer SummeLK;

	/** Gibt an ob die Anzahl der eingebrachten Kurse ok ist */
	@Column(name = "SummenOK")
	@JsonProperty
	public Integer SummenOK;

	/** Anzahl der eingebrachten LKs */
	@Column(name = "AnzRelLK")
	@JsonProperty
	public Integer AnzRelLK;

	/** Anzahl der eingebrachten Gks */
	@Column(name = "AnzRelGK")
	@JsonProperty
	public Integer AnzRelGK;

	/** Gibt an ob die Anzahl GK ok ist */
	@Column(name = "AnzRelOK")
	@JsonProperty
	public Integer AnzRelOK;

	/** Gibt an ob die Anzahl Defizite LK ok ist */
	@Column(name = "AnzDefLK")
	@JsonProperty
	public Integer AnzDefLK;

	/** Anzahl der Defizite in den Gks */
	@Column(name = "AnzDefGK")
	@JsonProperty
	public Integer AnzDefGK;

	/** Gibt an ob die Anzahl Defizite GK ok ist */
	@Column(name = "AnzDefOK")
	@JsonProperty
	public Integer AnzDefOK;

	/** J: Jahr des betreffenden Abschnittes (z.B. für Anzeige im Grid auf der Seite FHR)  W=wiederholt */
	@Column(name = "JSII_2_1")
	@JsonProperty
	public Integer JSII_2_1;

	/** J: Jahr des betreffenden Abschnittes (z.B. für Anzeige im Grid auf der Seite FHR)  */
	@Column(name = "JSII_2_1_W")
	@JsonProperty
	public Integer JSII_2_1_W;

	/** J: Jahr des betreffenden Abschnittes (z.B. für Anzeige im Grid auf der Seite FHR)  */
	@Column(name = "JSII_2_2")
	@JsonProperty
	public Integer JSII_2_2;

	/** J: Jahr des betreffenden Abschnittes (z.B. für Anzeige im Grid auf der Seite FHR)  W=wiederholt */
	@Column(name = "JSII_2_2_W")
	@JsonProperty
	public Integer JSII_2_2_W;

	/** J: Jahr des betreffenden Abschnittes (z.B. für Anzeige im Grid auf der Seite FHR)  */
	@Column(name = "JSII_3_1")
	@JsonProperty
	public Integer JSII_3_1;

	/** J: Jahr des betreffenden Abschnittes (z.B. für Anzeige im Grid auf der Seite FHR)  W=wiederholt */
	@Column(name = "JSII_3_1_W")
	@JsonProperty
	public Integer JSII_3_1_W;

	/** J: Jahr des betreffenden Abschnittes (z.B. für Anzeige im Grid auf der Seite FHR)  */
	@Column(name = "JSII_3_2")
	@JsonProperty
	public Integer JSII_3_2;

	/** J: Jahr des betreffenden Abschnittes (z.B. für Anzeige im Grid auf der Seite FHR)  W=wiederholt */
	@Column(name = "JSII_3_2_W")
	@JsonProperty
	public Integer JSII_3_2_W;

	/** A: Abschnitt des betreffenden Abschnittes (z.B. für Anzeige im Grid auf der Seite FHR)  */
	@Column(name = "ASII_2_1")
	@JsonProperty
	public Integer ASII_2_1;

	/** A: Abschnitt des betreffenden Abschnittes (z.B. für Anzeige im Grid auf der Seite FHR)  */
	@Column(name = "ASII_2_2")
	@JsonProperty
	public Integer ASII_2_2;

	/** A: Abschnitt des betreffenden Abschnittes (z.B. für Anzeige im Grid auf der Seite FHR)  W=wiederholt */
	@Column(name = "ASII_2_1_W")
	@JsonProperty
	public Integer ASII_2_1_W;

	/** A: Abschnitt des betreffenden Abschnittes (z.B. für Anzeige im Grid auf der Seite FHR)  W=wiederholt */
	@Column(name = "ASII_2_2_W")
	@JsonProperty
	public Integer ASII_2_2_W;

	/** A: Abschnitt des betreffenden Abschnittes (z.B. für Anzeige im Grid auf der Seite FHR)  */
	@Column(name = "ASII_3_1")
	@JsonProperty
	public Integer ASII_3_1;

	/** A: Abschnitt des betreffenden Abschnittes (z.B. für Anzeige im Grid auf der Seite FHR)  */
	@Column(name = "ASII_3_2")
	@JsonProperty
	public Integer ASII_3_2;

	/** A: Abschnitt des betreffenden Abschnittes (z.B. für Anzeige im Grid auf der Seite FHR)  W=wiederholt */
	@Column(name = "ASII_3_1_W")
	@JsonProperty
	public Integer ASII_3_1_W;

	/** A: Abschnitt des betreffenden Abschnittes (z.B. für Anzeige im Grid auf der Seite FHR)  W=wiederholt */
	@Column(name = "ASII_3_2_W")
	@JsonProperty
	public Integer ASII_3_2_W;

	/** W: Wertung des betreffenden Abschnittes für FHR. Damit ist gemeint: Wird der betreffende Abschnitt für die FHR-Berechnung herangezogen? Im Grid auf Karteireiter FHR wird die betreffende Spalte dann mit fetter Überschrift angezeigt. */
	@Column(name = "WSII_2_1")
	@JsonProperty
	public String WSII_2_1;

	/** W: Wertung des betreffenden Abschnittes für FHR. Damit ist gemeint: Wird der betreffende Abschnitt für die FHR-Berechnung herangezogen? Im Grid auf Karteireiter FHR wird die betreffende Spalte dann mit fetter Überschrift angezeigt. */
	@Column(name = "WSII_2_2")
	@JsonProperty
	public String WSII_2_2;

	/** W: Wertung des betreffenden Abschnittes für FHR. Damit ist gemeint: Wird der betreffende Abschnitt für die FHR-Berechnung herangezogen? Im Grid auf Karteireiter FHR wird die betreffende Spalte dann mit fetter Überschrift angezeigt.  W=wiederholt */
	@Column(name = "WSII_2_1_W")
	@JsonProperty
	public String WSII_2_1_W;

	/** W: Wertung des betreffenden Abschnittes für FHR. Damit ist gemeint: Wird der betreffende Abschnitt für die FHR-Berechnung herangezogen? Im Grid auf Karteireiter FHR wird die betreffende Spalte dann mit fetter Überschrift angezeigt. W=wiederholt */
	@Column(name = "WSII_2_2_W")
	@JsonProperty
	public String WSII_2_2_W;

	/** W: Wertung des betreffenden Abschnittes für FHR. Damit ist gemeint: Wird der betreffende Abschnitt für die FHR-Berechnung herangezogen? Im Grid auf Karteireiter FHR wird die betreffende Spalte dann mit fetter Überschrift angezeigt. */
	@Column(name = "WSII_3_1")
	@JsonProperty
	public String WSII_3_1;

	/** W: Wertung des betreffenden Abschnittes für FHR. Damit ist gemeint: Wird der betreffende Abschnitt für die FHR-Berechnung herangezogen? Im Grid auf Karteireiter FHR wird die betreffende Spalte dann mit fetter Überschrift angezeigt. */
	@Column(name = "WSII_3_2")
	@JsonProperty
	public String WSII_3_2;

	/** W: Wertung des betreffenden Abschnittes für FHR. Damit ist gemeint: Wird der betreffende Abschnitt für die FHR-Berechnung herangezogen? Im Grid auf Karteireiter FHR wird die betreffende Spalte dann mit fetter Überschrift angezeigt. W=wiederholt */
	@Column(name = "WSII_3_1_W")
	@JsonProperty
	public String WSII_3_1_W;

	/** W: Wertung des betreffenden Abschnittes für FHR. Damit ist gemeint: Wird der betreffende Abschnitt für die FHR-Berechnung herangezogen? Im Grid auf Karteireiter FHR wird die betreffende Spalte dann mit fetter Überschrift angezeigt. W=wiederholt */
	@Column(name = "WSII_3_2_W")
	@JsonProperty
	public String WSII_3_2_W;

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerFHR ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DTOSchuelerFHR() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DTOSchuelerFHR ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 */
	public DTOSchuelerFHR(final long ID, final long Schueler_ID) {
		this.ID = ID;
		this.Schueler_ID = Schueler_ID;
	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOSchuelerFHR other = (DTOSchuelerFHR) obj;
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
		return "DTOSchuelerFHR(ID=" + this.ID + ", Schueler_ID=" + this.Schueler_ID + ", FHRErreicht=" + this.FHRErreicht + ", Note=" + this.Note + ", GesamtPunktzahl=" + this.GesamtPunktzahl + ", SummeGK=" + this.SummeGK + ", SummeLK=" + this.SummeLK + ", SummenOK=" + this.SummenOK + ", AnzRelLK=" + this.AnzRelLK + ", AnzRelGK=" + this.AnzRelGK + ", AnzRelOK=" + this.AnzRelOK + ", AnzDefLK=" + this.AnzDefLK + ", AnzDefGK=" + this.AnzDefGK + ", AnzDefOK=" + this.AnzDefOK + ", JSII_2_1=" + this.JSII_2_1 + ", JSII_2_1_W=" + this.JSII_2_1_W + ", JSII_2_2=" + this.JSII_2_2 + ", JSII_2_2_W=" + this.JSII_2_2_W + ", JSII_3_1=" + this.JSII_3_1 + ", JSII_3_1_W=" + this.JSII_3_1_W + ", JSII_3_2=" + this.JSII_3_2 + ", JSII_3_2_W=" + this.JSII_3_2_W + ", ASII_2_1=" + this.ASII_2_1 + ", ASII_2_2=" + this.ASII_2_2 + ", ASII_2_1_W=" + this.ASII_2_1_W + ", ASII_2_2_W=" + this.ASII_2_2_W + ", ASII_3_1=" + this.ASII_3_1 + ", ASII_3_2=" + this.ASII_3_2 + ", ASII_3_1_W=" + this.ASII_3_1_W + ", ASII_3_2_W=" + this.ASII_3_2_W + ", WSII_2_1=" + this.WSII_2_1 + ", WSII_2_2=" + this.WSII_2_2 + ", WSII_2_1_W=" + this.WSII_2_1_W + ", WSII_2_2_W=" + this.WSII_2_2_W + ", WSII_3_1=" + this.WSII_3_1 + ", WSII_3_2=" + this.WSII_3_2 + ", WSII_3_1_W=" + this.WSII_3_1_W + ", WSII_3_2_W=" + this.WSII_3_2_W + ")";
	}

}
