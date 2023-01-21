package de.nrw.schule.svws.db.dto.dev.schild.schueler.fhr;

import de.nrw.schule.svws.db.DBEntityManager;
import de.nrw.schule.svws.db.converter.current.BooleanPlusMinusDefaultMinusConverter;


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
import de.nrw.schule.svws.csv.converter.current.BooleanPlusMinusDefaultMinusConverterSerializer;
import de.nrw.schule.svws.csv.converter.current.BooleanPlusMinusDefaultMinusConverterDeserializer;

/**
 * Diese Klasse dient als DTO für die Datenbanktabelle SchuelerFHR.
 * Sie wurde automatisch per Skript generiert und sollte nicht verändert werden, 
 * da sie aufgrund von Änderungen am DB-Schema ggf. neu generiert und überschrieben wird.
 */
@Entity
@Cacheable(DBEntityManager.use_db_caching)
@Table(name = "SchuelerFHR")
@NamedQuery(name="DevDTOSchuelerFHR.all", query="SELECT e FROM DevDTOSchuelerFHR e")
@NamedQuery(name="DevDTOSchuelerFHR.id", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.ID = :value")
@NamedQuery(name="DevDTOSchuelerFHR.id.multiple", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.ID IN :value")
@NamedQuery(name="DevDTOSchuelerFHR.schueler_id", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.Schueler_ID = :value")
@NamedQuery(name="DevDTOSchuelerFHR.schueler_id.multiple", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.Schueler_ID IN :value")
@NamedQuery(name="DevDTOSchuelerFHR.fhrerreicht", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.FHRErreicht = :value")
@NamedQuery(name="DevDTOSchuelerFHR.fhrerreicht.multiple", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.FHRErreicht IN :value")
@NamedQuery(name="DevDTOSchuelerFHR.note", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.Note = :value")
@NamedQuery(name="DevDTOSchuelerFHR.note.multiple", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.Note IN :value")
@NamedQuery(name="DevDTOSchuelerFHR.gesamtpunktzahl", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.GesamtPunktzahl = :value")
@NamedQuery(name="DevDTOSchuelerFHR.gesamtpunktzahl.multiple", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.GesamtPunktzahl IN :value")
@NamedQuery(name="DevDTOSchuelerFHR.summegk", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.SummeGK = :value")
@NamedQuery(name="DevDTOSchuelerFHR.summegk.multiple", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.SummeGK IN :value")
@NamedQuery(name="DevDTOSchuelerFHR.summelk", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.SummeLK = :value")
@NamedQuery(name="DevDTOSchuelerFHR.summelk.multiple", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.SummeLK IN :value")
@NamedQuery(name="DevDTOSchuelerFHR.summenok", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.SummenOK = :value")
@NamedQuery(name="DevDTOSchuelerFHR.summenok.multiple", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.SummenOK IN :value")
@NamedQuery(name="DevDTOSchuelerFHR.anzrellk", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.AnzRelLK = :value")
@NamedQuery(name="DevDTOSchuelerFHR.anzrellk.multiple", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.AnzRelLK IN :value")
@NamedQuery(name="DevDTOSchuelerFHR.anzrelgk", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.AnzRelGK = :value")
@NamedQuery(name="DevDTOSchuelerFHR.anzrelgk.multiple", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.AnzRelGK IN :value")
@NamedQuery(name="DevDTOSchuelerFHR.anzrelok", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.AnzRelOK = :value")
@NamedQuery(name="DevDTOSchuelerFHR.anzrelok.multiple", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.AnzRelOK IN :value")
@NamedQuery(name="DevDTOSchuelerFHR.anzdeflk", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.AnzDefLK = :value")
@NamedQuery(name="DevDTOSchuelerFHR.anzdeflk.multiple", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.AnzDefLK IN :value")
@NamedQuery(name="DevDTOSchuelerFHR.anzdefgk", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.AnzDefGK = :value")
@NamedQuery(name="DevDTOSchuelerFHR.anzdefgk.multiple", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.AnzDefGK IN :value")
@NamedQuery(name="DevDTOSchuelerFHR.anzdefok", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.AnzDefOK = :value")
@NamedQuery(name="DevDTOSchuelerFHR.anzdefok.multiple", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.AnzDefOK IN :value")
@NamedQuery(name="DevDTOSchuelerFHR.jsii_2_1", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.JSII_2_1 = :value")
@NamedQuery(name="DevDTOSchuelerFHR.jsii_2_1.multiple", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.JSII_2_1 IN :value")
@NamedQuery(name="DevDTOSchuelerFHR.jsii_2_1_w", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.JSII_2_1_W = :value")
@NamedQuery(name="DevDTOSchuelerFHR.jsii_2_1_w.multiple", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.JSII_2_1_W IN :value")
@NamedQuery(name="DevDTOSchuelerFHR.jsii_2_2", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.JSII_2_2 = :value")
@NamedQuery(name="DevDTOSchuelerFHR.jsii_2_2.multiple", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.JSII_2_2 IN :value")
@NamedQuery(name="DevDTOSchuelerFHR.jsii_2_2_w", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.JSII_2_2_W = :value")
@NamedQuery(name="DevDTOSchuelerFHR.jsii_2_2_w.multiple", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.JSII_2_2_W IN :value")
@NamedQuery(name="DevDTOSchuelerFHR.jsii_3_1", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.JSII_3_1 = :value")
@NamedQuery(name="DevDTOSchuelerFHR.jsii_3_1.multiple", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.JSII_3_1 IN :value")
@NamedQuery(name="DevDTOSchuelerFHR.jsii_3_1_w", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.JSII_3_1_W = :value")
@NamedQuery(name="DevDTOSchuelerFHR.jsii_3_1_w.multiple", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.JSII_3_1_W IN :value")
@NamedQuery(name="DevDTOSchuelerFHR.jsii_3_2", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.JSII_3_2 = :value")
@NamedQuery(name="DevDTOSchuelerFHR.jsii_3_2.multiple", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.JSII_3_2 IN :value")
@NamedQuery(name="DevDTOSchuelerFHR.jsii_3_2_w", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.JSII_3_2_W = :value")
@NamedQuery(name="DevDTOSchuelerFHR.jsii_3_2_w.multiple", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.JSII_3_2_W IN :value")
@NamedQuery(name="DevDTOSchuelerFHR.asii_2_1", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.ASII_2_1 = :value")
@NamedQuery(name="DevDTOSchuelerFHR.asii_2_1.multiple", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.ASII_2_1 IN :value")
@NamedQuery(name="DevDTOSchuelerFHR.asii_2_2", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.ASII_2_2 = :value")
@NamedQuery(name="DevDTOSchuelerFHR.asii_2_2.multiple", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.ASII_2_2 IN :value")
@NamedQuery(name="DevDTOSchuelerFHR.asii_2_1_w", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.ASII_2_1_W = :value")
@NamedQuery(name="DevDTOSchuelerFHR.asii_2_1_w.multiple", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.ASII_2_1_W IN :value")
@NamedQuery(name="DevDTOSchuelerFHR.asii_2_2_w", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.ASII_2_2_W = :value")
@NamedQuery(name="DevDTOSchuelerFHR.asii_2_2_w.multiple", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.ASII_2_2_W IN :value")
@NamedQuery(name="DevDTOSchuelerFHR.asii_3_1", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.ASII_3_1 = :value")
@NamedQuery(name="DevDTOSchuelerFHR.asii_3_1.multiple", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.ASII_3_1 IN :value")
@NamedQuery(name="DevDTOSchuelerFHR.asii_3_2", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.ASII_3_2 = :value")
@NamedQuery(name="DevDTOSchuelerFHR.asii_3_2.multiple", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.ASII_3_2 IN :value")
@NamedQuery(name="DevDTOSchuelerFHR.asii_3_1_w", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.ASII_3_1_W = :value")
@NamedQuery(name="DevDTOSchuelerFHR.asii_3_1_w.multiple", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.ASII_3_1_W IN :value")
@NamedQuery(name="DevDTOSchuelerFHR.asii_3_2_w", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.ASII_3_2_W = :value")
@NamedQuery(name="DevDTOSchuelerFHR.asii_3_2_w.multiple", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.ASII_3_2_W IN :value")
@NamedQuery(name="DevDTOSchuelerFHR.wsii_2_1", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.WSII_2_1 = :value")
@NamedQuery(name="DevDTOSchuelerFHR.wsii_2_1.multiple", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.WSII_2_1 IN :value")
@NamedQuery(name="DevDTOSchuelerFHR.wsii_2_2", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.WSII_2_2 = :value")
@NamedQuery(name="DevDTOSchuelerFHR.wsii_2_2.multiple", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.WSII_2_2 IN :value")
@NamedQuery(name="DevDTOSchuelerFHR.wsii_2_1_w", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.WSII_2_1_W = :value")
@NamedQuery(name="DevDTOSchuelerFHR.wsii_2_1_w.multiple", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.WSII_2_1_W IN :value")
@NamedQuery(name="DevDTOSchuelerFHR.wsii_2_2_w", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.WSII_2_2_W = :value")
@NamedQuery(name="DevDTOSchuelerFHR.wsii_2_2_w.multiple", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.WSII_2_2_W IN :value")
@NamedQuery(name="DevDTOSchuelerFHR.wsii_3_1", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.WSII_3_1 = :value")
@NamedQuery(name="DevDTOSchuelerFHR.wsii_3_1.multiple", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.WSII_3_1 IN :value")
@NamedQuery(name="DevDTOSchuelerFHR.wsii_3_2", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.WSII_3_2 = :value")
@NamedQuery(name="DevDTOSchuelerFHR.wsii_3_2.multiple", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.WSII_3_2 IN :value")
@NamedQuery(name="DevDTOSchuelerFHR.wsii_3_1_w", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.WSII_3_1_W = :value")
@NamedQuery(name="DevDTOSchuelerFHR.wsii_3_1_w.multiple", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.WSII_3_1_W IN :value")
@NamedQuery(name="DevDTOSchuelerFHR.wsii_3_2_w", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.WSII_3_2_W = :value")
@NamedQuery(name="DevDTOSchuelerFHR.wsii_3_2_w.multiple", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.WSII_3_2_W IN :value")
@NamedQuery(name="DevDTOSchuelerFHR.primaryKeyQuery", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.ID = ?1")
@NamedQuery(name="DevDTOSchuelerFHR.all.migration", query="SELECT e FROM DevDTOSchuelerFHR e WHERE e.ID IS NOT NULL")
@JsonPropertyOrder({"ID","Schueler_ID","FHRErreicht","Note","GesamtPunktzahl","SummeGK","SummeLK","SummenOK","AnzRelLK","AnzRelGK","AnzRelOK","AnzDefLK","AnzDefGK","AnzDefOK","JSII_2_1","JSII_2_1_W","JSII_2_2","JSII_2_2_W","JSII_3_1","JSII_3_1_W","JSII_3_2","JSII_3_2_W","ASII_2_1","ASII_2_2","ASII_2_1_W","ASII_2_2_W","ASII_3_1","ASII_3_2","ASII_3_1_W","ASII_3_2_W","WSII_2_1","WSII_2_2","WSII_2_1_W","WSII_2_2_W","WSII_3_1","WSII_3_2","WSII_3_1_W","WSII_3_2_W"})
public class DevDTOSchuelerFHR {

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
	@Convert(converter=BooleanPlusMinusDefaultMinusConverter.class)
	@JsonSerialize(using=BooleanPlusMinusDefaultMinusConverterSerializer.class)
	@JsonDeserialize(using=BooleanPlusMinusDefaultMinusConverterDeserializer.class)
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
	 * Erstellt ein neues Objekt der Klasse DevDTOSchuelerFHR ohne eine Initialisierung der Attribute.
	 */
	@SuppressWarnings("unused")
	private DevDTOSchuelerFHR() {
	}

	/**
	 * Erstellt ein neues Objekt der Klasse DevDTOSchuelerFHR ohne eine Initialisierung der Attribute.
	 * @param ID   der Wert für das Attribut ID
	 * @param Schueler_ID   der Wert für das Attribut Schueler_ID
	 */
	public DevDTOSchuelerFHR(final Long ID, final Long Schueler_ID) {
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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DevDTOSchuelerFHR other = (DevDTOSchuelerFHR) obj;
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
		return "DevDTOSchuelerFHR(ID=" + this.ID + ", Schueler_ID=" + this.Schueler_ID + ", FHRErreicht=" + this.FHRErreicht + ", Note=" + this.Note + ", GesamtPunktzahl=" + this.GesamtPunktzahl + ", SummeGK=" + this.SummeGK + ", SummeLK=" + this.SummeLK + ", SummenOK=" + this.SummenOK + ", AnzRelLK=" + this.AnzRelLK + ", AnzRelGK=" + this.AnzRelGK + ", AnzRelOK=" + this.AnzRelOK + ", AnzDefLK=" + this.AnzDefLK + ", AnzDefGK=" + this.AnzDefGK + ", AnzDefOK=" + this.AnzDefOK + ", JSII_2_1=" + this.JSII_2_1 + ", JSII_2_1_W=" + this.JSII_2_1_W + ", JSII_2_2=" + this.JSII_2_2 + ", JSII_2_2_W=" + this.JSII_2_2_W + ", JSII_3_1=" + this.JSII_3_1 + ", JSII_3_1_W=" + this.JSII_3_1_W + ", JSII_3_2=" + this.JSII_3_2 + ", JSII_3_2_W=" + this.JSII_3_2_W + ", ASII_2_1=" + this.ASII_2_1 + ", ASII_2_2=" + this.ASII_2_2 + ", ASII_2_1_W=" + this.ASII_2_1_W + ", ASII_2_2_W=" + this.ASII_2_2_W + ", ASII_3_1=" + this.ASII_3_1 + ", ASII_3_2=" + this.ASII_3_2 + ", ASII_3_1_W=" + this.ASII_3_1_W + ", ASII_3_2_W=" + this.ASII_3_2_W + ", WSII_2_1=" + this.WSII_2_1 + ", WSII_2_2=" + this.WSII_2_2 + ", WSII_2_1_W=" + this.WSII_2_1_W + ", WSII_2_2_W=" + this.WSII_2_2_W + ", WSII_3_1=" + this.WSII_3_1 + ", WSII_3_2=" + this.WSII_3_2 + ", WSII_3_1_W=" + this.WSII_3_1_W + ", WSII_3_2_W=" + this.WSII_3_2_W + ")";
	}

}