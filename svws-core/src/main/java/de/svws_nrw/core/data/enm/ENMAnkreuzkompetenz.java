package de.svws_nrw.core.data.enm;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse spezifiziert die grundlegende Struktur von JSON-Daten zu
 * dem Katalog der Ankreuzkompetenzen für das Externe-Noten-Modul ENM.
 */
@XmlRootElement
@Schema(description = "Spezifiziert die grundlegende Struktur von JSON-Daten zu dem Katalog der Ankreuzkompetenzen für das Externe-Noten-Modul ENM.")
@TranspilerDTO
public class ENMAnkreuzkompetenz {

	/** Die ID der Ankreuzkompetenz aus der SVWS-DB */
	@Schema(description = "die ID der Ankreuzkompetenz aus der SVWS-DB", example = "12345")
	public long id = -1;

	/** Gibt an, ob es sich um eine Fach-Ankreuzkompetenzen (true) handelt oder um eine Ankreuzkompetenz im Bereich Arbeits- und Sozialverhalten (ASV, false) */
	@Schema(description = "gibt an, ob es sich um eine Fach-Ankreuzkompetenzen (true) handelt oder um eine Ankreuzkompetenz im Bereich"
			+ " Arbeits- und Sozialverhalten (ASV, false)", example = "true")
	public boolean istFachkompetenz = true;

	/** Die ID des Faches, auf die sich die Ankreuzkompetenz bezieht, NULL bei einer Ankreuzkompetenz im Bereich Arbeits- und Sozialverhalten (ASV) */
	@Schema(description = "die ID des Faches, auf die sich die Ankreuzkompetenz bezieht, NULL bei einer Ankreuzkompetenz im Bereich"
			+ " Arbeits- und Sozialverhalten (ASV)", example = "13")
	public Long fachID = null;

	/** Das Statistik-Kürzel des Jahrgangs zu der die Ankreuzfloskel gehört. */
	@Schema(description = "das Statistik-Kürzel des Jahrgangs zu der die Ankreuzfloskel gehört", example = "03")
	public @NotNull String jahrgang = "";

	/** Der Text der Ankreuzkompetenz. */
	@Schema(description = "der Text der Ankreuzkompetenz", example = " - arbeitet auch längere Zeit konzentriert und zielstrebig.")
	public @NotNull String text = "";

	/** Die Sortier-Reihenfolge der Ankreuzkompetenzen. Bei gleichen Werten sollte nach dem Text-Atrtribut sortiert werden. */
	@Schema(description = "die Sortier-Reihenfolge der Ankreuzkompetenzen. Bei gleichen Werten sollte nach dem Text-Atrtribut sortiert werden.", example = "1")
	public int sortierung = 1;

}
