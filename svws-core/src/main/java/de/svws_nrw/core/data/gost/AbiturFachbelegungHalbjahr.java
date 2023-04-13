package de.svws_nrw.core.data.gost;

import jakarta.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;



/**
 * Diese Klasse enthält Informationen zu der Halbjahres-Fachbelegung eines Schülers für das Abitur der gymnasialen Oberstufe.
 */
@XmlRootElement
@Schema(description = "enthält die Informationen zu der Halbjahres-Fachbelegung eines Schülers für das Abitur der gymnasialen Oberstufe.")
@JsonPropertyOrder({ "halbjahrKuerzel", "kursartKuerzel", "schriftlich", "biliSprache", "lehrerKuerzel", "wochenstunden",
	"fehlstundenGesamt", "fehlstundenUnentschuldigt", "notenkuerzel", "block1gewertet", "block1kursAufZeugnis" })
@TranspilerDTO
public class AbiturFachbelegungHalbjahr {

	/** Das Kürzel des Halbjahres der Fachbelegung */
	public @NotNull String halbjahrKuerzel = "";

	/** Das Kürzel der Kursart der Gymnasialen Oberstufe dieser Fachbelegung  */
	public @NotNull String kursartKuerzel = "";

	/** Gibt an, ob das Fach schriftlich belegt wurde oder nicht. */
	public boolean schriftlich = false;

	/** Das einstellige Kürzel der bilingualen Sprache, sofern das Fach bilingual unterrichtet wurde. */
	public String biliSprache = null;

	/** Die ID des unterrichtenden Lehrers, welcher die Note erteilt. */
	public Long lehrer;

	/** Die Wochenstundenzahl, mir der das Fach belegt wurde */
	public int wochenstunden;

	/** Die Anzahl der Fehlstunden. */
	public int fehlstundenGesamt;

	/** Die Anzahl der unentschuldigten Fehlstunden. */
	public int fehlstundenUnentschuldigt;

	/** Das Notenkürzel der erteilten Note */
	public String notenkuerzel = null;

	/** Gibt an, ob die Belegung für den Block I gewertet wird oder nicht - nicht alle Kursbelegungen müssen laut Prüfungsordnung in die Abiturnote einfliessen */
	public Boolean block1gewertet = null;

	/** Gibt an, ob die Belegung des Kurses auf dem Abiturzeugnis angezeigt werden soll oder nicht. Eine Belegung kann auf Wunsch des Prüflings bei nicht gewerteten Kursen nicht auf dem Zeugnis erscheinen. */
	public Boolean block1kursAufZeugnis = null;

}
