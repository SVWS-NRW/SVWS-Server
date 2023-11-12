package de.svws_nrw.db.dto.migration.schild.schueler.fhr;

import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverter;


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
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverterSerializer;
import de.svws_nrw.csv.converter.migration.MigrationBooleanPlusMinusDefaultMinusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerFHR.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden,
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerFHR")
@NamedQuery(name = "MigrationDTOSchuelerFHR.all", query = "SELECT e FROM MigrationDTOSchuelerFHR e")
@NamedQuery(name = "MigrationDTOSchuelerFHR.id", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.ID = :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.id.multiple", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.ID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.schueler_id", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.Schueler_ID = :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.schueler_id.multiple", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.Schueler_ID IN :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.fhrerreicht", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.FHRErreicht = :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.fhrerreicht.multiple", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.FHRErreicht IN :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.note", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.Note = :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.note.multiple", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.Note IN :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.gesamtpunktzahl", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.GesamtPunktzahl = :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.gesamtpunktzahl.multiple", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.GesamtPunktzahl IN :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.summegk", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.SummeGK = :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.summegk.multiple", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.SummeGK IN :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.summelk", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.SummeLK = :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.summelk.multiple", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.SummeLK IN :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.summenok", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.SummenOK = :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.summenok.multiple", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.SummenOK IN :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.anzrellk", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.AnzRelLK = :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.anzrellk.multiple", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.AnzRelLK IN :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.anzrelgk", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.AnzRelGK = :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.anzrelgk.multiple", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.AnzRelGK IN :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.anzrelok", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.AnzRelOK = :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.anzrelok.multiple", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.AnzRelOK IN :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.anzdeflk", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.AnzDefLK = :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.anzdeflk.multiple", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.AnzDefLK IN :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.anzdefgk", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.AnzDefGK = :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.anzdefgk.multiple", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.AnzDefGK IN :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.anzdefok", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.AnzDefOK = :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.anzdefok.multiple", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.AnzDefOK IN :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.jsii_2_1", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.JSII_2_1 = :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.jsii_2_1.multiple", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.JSII_2_1 IN :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.jsii_2_1_w", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.JSII_2_1_W = :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.jsii_2_1_w.multiple", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.JSII_2_1_W IN :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.jsii_2_2", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.JSII_2_2 = :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.jsii_2_2.multiple", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.JSII_2_2 IN :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.jsii_2_2_w", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.JSII_2_2_W = :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.jsii_2_2_w.multiple", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.JSII_2_2_W IN :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.jsii_3_1", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.JSII_3_1 = :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.jsii_3_1.multiple", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.JSII_3_1 IN :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.jsii_3_1_w", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.JSII_3_1_W = :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.jsii_3_1_w.multiple", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.JSII_3_1_W IN :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.jsii_3_2", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.JSII_3_2 = :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.jsii_3_2.multiple", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.JSII_3_2 IN :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.jsii_3_2_w", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.JSII_3_2_W = :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.jsii_3_2_w.multiple", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.JSII_3_2_W IN :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.asii_2_1", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.ASII_2_1 = :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.asii_2_1.multiple", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.ASII_2_1 IN :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.asii_2_2", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.ASII_2_2 = :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.asii_2_2.multiple", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.ASII_2_2 IN :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.asii_2_1_w", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.ASII_2_1_W = :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.asii_2_1_w.multiple", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.ASII_2_1_W IN :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.asii_2_2_w", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.ASII_2_2_W = :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.asii_2_2_w.multiple", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.ASII_2_2_W IN :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.asii_3_1", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.ASII_3_1 = :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.asii_3_1.multiple", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.ASII_3_1 IN :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.asii_3_2", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.ASII_3_2 = :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.asii_3_2.multiple", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.ASII_3_2 IN :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.asii_3_1_w", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.ASII_3_1_W = :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.asii_3_1_w.multiple", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.ASII_3_1_W IN :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.asii_3_2_w", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.ASII_3_2_W = :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.asii_3_2_w.multiple", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.ASII_3_2_W IN :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.wsii_2_1", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.WSII_2_1 = :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.wsii_2_1.multiple", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.WSII_2_1 IN :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.wsii_2_2", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.WSII_2_2 = :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.wsii_2_2.multiple", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.WSII_2_2 IN :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.wsii_2_1_w", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.WSII_2_1_W = :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.wsii_2_1_w.multiple", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.WSII_2_1_W IN :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.wsii_2_2_w", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.WSII_2_2_W = :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.wsii_2_2_w.multiple", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.WSII_2_2_W IN :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.wsii_3_1", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.WSII_3_1 = :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.wsii_3_1.multiple", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.WSII_3_1 IN :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.wsii_3_2", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.WSII_3_2 = :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.wsii_3_2.multiple", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.WSII_3_2 IN :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.wsii_3_1_w", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.WSII_3_1_W = :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.wsii_3_1_w.multiple", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.WSII_3_1_W IN :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.wsii_3_2_w", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.WSII_3_2_W = :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.wsii_3_2_w.multiple", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.WSII_3_2_W IN :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.schulnreigner", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.SchulnrEigner = :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.schulnreigner.multiple", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.SchulnrEigner IN :value")
@NamedQuery(name = "MigrationDTOSchuelerFHR.primaryKeyQuery", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.ID = ?1")
@NamedQuery(name = "MigrationDTOSchuelerFHR.primaryKeyQuery.multiple", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.ID IN ?1")
@NamedQuery(name = "MigrationDTOSchuelerFHR.all.migration", query = "SELECT e FROM MigrationDTOSchuelerFHR e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID", "Schueler_ID", "FHRErreicht", "Note", "GesamtPunktzahl", "SummeGK", "SummeLK", "SummenOK", "AnzRelLK", "AnzRelGK", "AnzRelOK", "AnzDefLK", "AnzDefGK", "AnzDefOK", "JSII_2_1", "JSII_2_1_W", "JSII_2_2", "JSII_2_2_W", "JSII_3_1", "JSII_3_1_W", "JSII_3_2", "JSII_3_2_W", "ASII_2_1", "ASII_2_2", "ASII_2_1_W", "ASII_2_2_W", "ASII_3_1", "ASII_3_2", "ASII_3_1_W", "ASII_3_2_W", "WSII_2_1", "WSII_2_2", "WSII_2_1_W", "WSII_2_2_W", "WSII_3_1", "WSII_3_2", "WSII_3_1_W", "WSII_3_2_W", "SchulnrEigner"})
public final class MigrationDTOSchuelerFHR {

	/** ID des FHR Datensatzes */
	@Id
	@Column(name = "ID")
	@JsonProperty
	public Long ID;

	/** ID des Schülers zum FHR Datensatz */
	@Column(name = "Schueler_ID")
	@JsonProperty
	public Long Schueler_ID;

	/** Gibt an ob das FHR erreicht wurde */
	@Column(name = "FHRErreicht")
	@JsonProperty
	@Convert(converter = MigrationBooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using = MigrationBooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using = MigrationBooleanPlusMinusDefaultMinusConverterDeserializer.class)
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

	/** Die Schulnummer zu welcher der Datensatz gehört – wird benötigt, wenn mehrere Schulen in einem Schema der Datenbank gespeichert werden */
	@Column(name = "SchulnrEigner")
	@JsonProperty
	public Integer SchulnrEigner;

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuelerFHR ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private MigrationDTOSchuelerFHR() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse MigrationDTOSchuelerFHR ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 */
	public MigrationDTOSchuelerFHR(final Long ID, final Long Schueler_ID) {
		if (ID == null) {
			throw new NullPointerException("ID must not be null");
		}
		this.ID = ID;
		if (Schueler_ID == null) {
			throw new NullPointerException("Schueler_ID must not be null");
		}
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
		MigrationDTOSchuelerFHR other = (MigrationDTOSchuelerFHR) obj;
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
		return "MigrationDTOSchuelerFHR(ID=" + this.ID + ", Schueler_ID=" + this.Schueler_ID + ", FHRErreicht=" + this.FHRErreicht + ", Note=" + this.Note + ", GesamtPunktzahl=" + this.GesamtPunktzahl + ", SummeGK=" + this.SummeGK + ", SummeLK=" + this.SummeLK + ", SummenOK=" + this.SummenOK + ", AnzRelLK=" + this.AnzRelLK + ", AnzRelGK=" + this.AnzRelGK + ", AnzRelOK=" + this.AnzRelOK + ", AnzDefLK=" + this.AnzDefLK + ", AnzDefGK=" + this.AnzDefGK + ", AnzDefOK=" + this.AnzDefOK + ", JSII_2_1=" + this.JSII_2_1 + ", JSII_2_1_W=" + this.JSII_2_1_W + ", JSII_2_2=" + this.JSII_2_2 + ", JSII_2_2_W=" + this.JSII_2_2_W + ", JSII_3_1=" + this.JSII_3_1 + ", JSII_3_1_W=" + this.JSII_3_1_W + ", JSII_3_2=" + this.JSII_3_2 + ", JSII_3_2_W=" + this.JSII_3_2_W + ", ASII_2_1=" + this.ASII_2_1 + ", ASII_2_2=" + this.ASII_2_2 + ", ASII_2_1_W=" + this.ASII_2_1_W + ", ASII_2_2_W=" + this.ASII_2_2_W + ", ASII_3_1=" + this.ASII_3_1 + ", ASII_3_2=" + this.ASII_3_2 + ", ASII_3_1_W=" + this.ASII_3_1_W + ", ASII_3_2_W=" + this.ASII_3_2_W + ", WSII_2_1=" + this.WSII_2_1 + ", WSII_2_2=" + this.WSII_2_2 + ", WSII_2_1_W=" + this.WSII_2_1_W + ", WSII_2_2_W=" + this.WSII_2_2_W + ", WSII_3_1=" + this.WSII_3_1 + ", WSII_3_2=" + this.WSII_3_2 + ", WSII_3_1_W=" + this.WSII_3_1_W + ", WSII_3_2_W=" + this.WSII_3_2_W + ", SchulnrEigner=" + this.SchulnrEigner + ")";
	}

}
