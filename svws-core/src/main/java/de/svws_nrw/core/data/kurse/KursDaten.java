package de.svws_nrw.core.data.kurse;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.core.data.schueler.Schueler;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt die grundlegenden Daten eines Kurses.
 */
@XmlRootElement
@Schema(description = "Die Daten eines Kurses.")
@TranspilerDTO
public class KursDaten {

	/** Die ID des Kurses. */
	@Schema(description = "die ID des Kurses", example = "4711")
	public long id;

	/** Die ID des Schuljahresabschnittes des Kurses. */
	@Schema(description = "die ID des Schuljahresabschnittes des Kurses", example = "14")
	public long idSchuljahresabschnitt;

	/** Das Kürzel des Kurses. */
	@Schema(description = "das Kürzel des Kurses", example = "IF-LK1")
	public @NotNull String kuerzel = "";

	/** Die IDs der Jahrgänge, denen der Kurs zugeordnet ist */
	@Schema(description = "die IDs der Jahrgänge, denen der Kurs zugeordnet ist")
	public @NotNull List<@NotNull Long> idJahrgaenge = new ArrayList<>();

	/** Die ID des Faches, dem der Kurs zugeordnet ist */
	@Schema(description = "die ID des Faches, dem der Kurs zugeordnet ist", example = "815")
	public long idFach;

	/** Die ID des Kurslehrers. */
	@Schema(description = "die ID des Kurslehrers", example = "42")
	public Long lehrer;

	/** Die allgemeine Kursart, welche zur Filterung der speziellen Kursarten verwendet wird. */
	@Schema(description = "die allgemeine Kursart, welche zur Filterung der speziellen Kursarten verwendet wird", example = "GK")
	public @NotNull String kursartAllg = "";

	/** Die Sortierreihenfolge des Jahrgangslisten-Eintrags. */
	@Schema(description = "die Sortierreihenfolge des Jahrgangslisten-Eintrags", example = "1")
	public int sortierung;

	/** Gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht. */
	@Schema(description = "gibt an, ob der Eintrag in der Anwendung sichtbar sein soll oder nicht", example = "true")
	public boolean istSichtbar;

	/** Die Schüler des Kurses. */
	public @NotNull List<@NotNull Schueler> schueler = new ArrayList<>();

	/** Die Nummern der Kurs-Schienen, in welchen sich der Kurs befindet - sofern eine Schiene zugeordnet wurde */
	public @NotNull List<@NotNull Integer> schienen = new ArrayList<>();


	/** Die Wochenstunden des Kurses. */
	@Schema(description = "die Wochenstunden des Kurses", example = "3")
	public int wochenstunden = -1;

	/** Die Wochenstunden des Kurslehrers in dem Kurs. */
	@Schema(description = "die Wochenstunden des Kurslehrers in dem Kurs", example = "3")
	public double wochenstundenLehrer = -1;

	/** Die Fortschreibungsart des Kurses (Keine, nur Definition mit Jahrgang behalten oder hochschreiben oder komplett) */
	@Schema(description = "die Fortschreibungsart des Kurses (Keine, nur Definition mit Jahrgang behalten oder hochschreiben oder komplett)", example = "0")
	public int idKursFortschreibungsart = 0;

	/** Die Schulnummer des Kurses, falls der Kurs an einer anderes Schule stattfindet (z.B. im Rahmen einer Kooperation). */
	@Schema(description = "die Schulnummer des Kurses, falls der Kurs an einer anderes Schule stattfindet (z.B. im Rahmen einer Kooperation)", example = "100001")
	public Integer schulnummer = null;

	/** Gibt an, ob der Kurs epochal unterrichtet wird. */
	@Schema(description = "gibt an, ob der Kurs epochal unterrichtet wird", example = "false")
	public boolean istEpochalunterricht = false;

	/** Ggf. die Zeugnisbezeichnung des Kurses */
	@Schema(description = "ggf. die die Zeugnisbezeichnung des Kurses", example = "false")
	public String bezeichnungZeugnis = null;

}
