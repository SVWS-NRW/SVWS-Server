package de.svws_nrw.core.data.schild3.reporting;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Die Klasse enthält den Core-DTO für die Schild-Reporting-Datenquelle SchuelerGOStLaufbahnplanungSummen.
 */
@XmlRootElement
@Schema(description = "Datenquelle SchuelerGOStLaufbahnplanungSummen")
@TranspilerDTO
public class SchildReportingSchuelerGOStLaufbahnplanungSummen {

    /** Die ID des Schülers, zu dem die Laufbahnplanungsdaten gehören. */
    @Schema(description = "die ID des Schülers, zu dem die Laufbahnplanungsdaten gehören", example = "4711")
    public long schuelerID;


    /** Kursanzahl in der EF.1 */
	@Schema(description = "Kursanzahl in der EF.1", example = "12")
	public int kursanzahlEF1;

	/** Kursanzahl in der EF.2 */
	@Schema(description = "Kursanzahl in der EF.2", example = "11")
	public int kursanzahlEF2;

	/** Kursanzahl in der Q1.1 */
	@Schema(description = "Kursanzahl in der Q1.1", example = "10")
	public int kursanzahlQ11;

	/** Kursanzahl in der Q1.2 */
	@Schema(description = "Kursanzahl in der Q1.2", example = "10")
	public int kursanzahlQ12;

	/** Kursanzahl in der Q2.1 */
	@Schema(description = "Kursanzahl in der Q2.1", example = "10")
	public int kursanzahlQ21;

	/** Kursanzahl in der Q2.2 */
	@Schema(description = "Kursanzahl in der Q2.2", example = "10")
	public int kursanzahlQ22;

	/** Kursanzahl in der Qualifikationsphase */
	@Schema(description = "Kursanzahl in der Qualifikationsphase", example = "40")
	public int kursanzahlQPh;


	/** Wochenstundensumme in der EF.1 */
	@Schema(description = "Wochenstundensumme aller Fächer in der EF.1", example = "36")
	public int wochenstundenEF1;

	/** Wochenstundensumme in der EF.2 */
	@Schema(description = "Wochenstundensumme aller Fächer in der EF.2", example = "33")
	public int wochenstundenEF2;

	/** Wochenstundensumme in der Q1.1 */
	@Schema(description = "Wochenstundensumme aller Fächer in der Q1.1", example = "34")
	public int wochenstundenQ11;

	/** Wochenstundensumme in der Q1.2 */
	@Schema(description = "Wochenstundensumme aller Fächer in der Q1.2", example = "34")
	public int wochenstundenQ12;

	/** Wochenstundensumme in der Q2.1 */
	@Schema(description = "Wochenstundensumme aller Fächer in der Q2.1", example = "34")
	public int wochenstundenQ21;

	/** Wochenstundensumme in der Q2.2 */
	@Schema(description = "Wochenstundensumme aller Fächer in der Q2.2", example = "34")
	public int wochenstundenQ22;

	/** Wochenstundendurchschnitt in der EF */
	@Schema(description = "Wochenstundendurchschnitt in der EF", example = "34.5")
	public double wochenstundenDurchschnittEF;

	/** Wochenstundendurchschnitt in der EF */
	@Schema(description = "Wochenstundendurchschnitt in der Q1", example = "37")
	public double wochenstundenDurchschnittQ1;

	/** Wochenstundendurchschnitt in der EF */
	@Schema(description = "Wochenstundendurchschnitt in der Q2", example = "34")
	public double wochenstundenDurchschnittQ2;

	/** Wochenstundendurchschnitt in der Qualifikationsphase */
	@Schema(description = "Wochenstundendurchschnitt in der Qualifikationsphase", example = "34")
	public double wochenstundenDurchschnittQPh;

	/** Wochenstundensumme der gesamten Laufbahn */
	@Schema(description = "Wochenstundensumme aller Fächer der gesamten Laufbahn", example = "103.5")
	public double wochenstundenGesamt;

}
