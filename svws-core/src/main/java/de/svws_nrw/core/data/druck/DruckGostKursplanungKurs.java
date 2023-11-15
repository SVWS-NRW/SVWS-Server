package de.svws_nrw.core.data.druck;

import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;


/**
 * Core-DTO mit den Daten für Kurse für den Druck von Kursen aus der Gost-Kursplanung mit Schülerliste.
 */
@XmlRootElement
@Schema(description = "Die Daten für Kurse für den Druck von Kursen aus der Gost-Kursplanung mit Schülerliste.")
@TranspilerDTO
public class DruckGostKursplanungKurs {

	/** ID des Kurses */
	@Schema(description = "ID des Kurses", example = "42")
	public long Id = -1;

	/** Halbjahr der Oberstufe für den Kurs gemäß Blockungsergebnis. */
	@Schema(description = "Halbjahr der gymnasialen Oberstufe", example = "Q1.1")
	public String GostHalbjahr = "";

	/** Bezeichnung des Kurses. */
	@Schema(description = "Bezeichnung des Kurses", example = "D-GK1")
	public @NotNull String Bezeichnung = "";

	/** Kommaseparierte Liste der Lehrkräfte des Kurses. */
	@Schema(description = "Kurslehrkraft oder kommaseparierte Liste der Kurslehrkräfte", example = "ABC")
	public String Lehrkraefte = "";

	/** Kursart des Kurses. */
	@Schema(description = "Kursart des Kurses", example = "GK")
	public String Kursart = "";

	/** Anzahl der Schülerinnen und Schüler im Kurs. */
	@Schema(description = "Anzahl der Schülerinnen und Schüler im Kurs", example = "21")
	public int AnzahlTeilnehmer = -1;

	/** Anzahl der Schülerinnen und Schüler mit Status extern. */
	@Schema(description = "Anzahl der Schülerinnen und Schüler mit Status extern", example = "9")
	public int AnzahlExterneTeilnehmer = -1;

	/** Anzahl der Klausurschreiber. */
	@Schema(description = "Anzahl der Klausurschreiber", example = "15")
	public int AnzahlKlausurteilnehmer = -1;

	/** Anzahl der Schülerinnen und Schüler für das Fach des Kurses erstes oder zweites Abiturfach ist. */
	@Schema(description = "Anzahl der Schülerinnen und Schüler für das Fach des Kurses erstes oder zweites Abiturfach ist", example = "17")
	public int AnzahlAB12 = -1;

	/** Anzahl der Schülerinnen und Schüler für das Fach des Kurses drittes Abiturfach ist. */
	@Schema(description = "Anzahl der Schülerinnen und Schüler für das Fach des Kurses drittes Abiturfach ist", example = "6")
	public int AnzahlAB3 = -1;

	/** Anzahl der Schülerinnen und Schüler für das Fach des Kurses viertes Abiturfach ist. */
	@Schema(description = "Anzahl der Schülerinnen und Schüler für das Fach des Kurses viertes Abiturfach ist", example = "4")
	public int AnzahlAB4 = -1;

	/** Eine Liste vom Typ Kursschueler, die alle Schülerinnen und Schüler des Kurses mit ihrer Kursbelegung enthält. */
	@Schema(description = "Liste der Schülerinnen und Schüler, die den Kurs belegen, mit ihren Kursbezogenen Informationen", example = "ID, Nachname, ...")
	public @NotNull List<@NotNull DruckGostKursplanungKursSchueler> Kursschueler = new ArrayList<>();
}
