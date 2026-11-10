package de.svws_nrw.service.kurse;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

/**
 * Die Daten zum Anlegen eines Kurses. Nicht angegebene optionale Felder werden mit den Standardwerten belegt.
 */
@Schema(description = "Die Daten zum Anlegen eines Kurses.")
public class KursCreateRequest {

	/** Die ID des Schuljahresabschnitts */
	@Schema(description = "die ID des Schuljahresabschnitts", example = "4711")
	@NotNull
	public Long idSchuljahresabschnitt;

	/** Das Kürzel des Kurses */
	@Schema(description = "das Kürzel des Kurses", example = "D-GK1")
	@NotEmpty
	@Size(max = 20)
	public String kuerzel;

	/** Die ID des Faches */
	@Schema(description = "die ID des Faches", example = "42")
	@NotNull
	public Long idFach;

	/** Die allgemeine Kursart */
	@Schema(description = "die allgemeine Kursart", example = "GK")
	@NotNull
	@Size(max = 10)
	public String kursartAllg;

	/** Die ID des Lehrers */
	@Schema(description = "die ID des Lehrers", example = "17")
	public Long idLehrer;

	/** Die Sortierung des Kurses */
	@Schema(description = "die Sortierung des Kurses", example = "32000")
	@PositiveOrZero
	public Integer sortierung;

	/** Gibt an, ob der Kurs sichtbar ist */
	@Schema(description = "gibt an, ob der Kurs sichtbar ist", example = "true")
	public Boolean istSichtbar;

	/** Die Wochenstunden des Kurses */
	@Schema(description = "die Wochenstunden des Kurses", example = "3")
	@Min(0)
	@Max(40)
	public Integer wochenstunden;

	/** Die Wochenstunden des Lehrers */
	@Schema(description = "die Wochenstunden des Lehrers", example = "3.0")
	public Double wochenstundenLehrer;

	/** Die ID der Fortschreibungsart */
	@Schema(description = "die ID der Fortschreibungsart", example = "0")
	@Min(0)
	@Max(3)
	public Integer idKursFortschreibungsart;

	/** Die Schulnummer der Schule, an der der Kurs stattfindet */
	@Schema(description = "die Schulnummer der Schule, an der der Kurs stattfindet", example = "123456")
	@Min(100000)
	@Max(999999)
	public Integer schulnummer;

	/** Gibt an, ob es sich um Epochalunterricht handelt */
	@Schema(description = "gibt an, ob es sich um Epochalunterricht handelt", example = "false")
	public Boolean istEpochalunterricht;

	/** Die Bezeichnung des Kurses auf dem Zeugnis */
	@Schema(description = "die Bezeichnung des Kurses auf dem Zeugnis", example = "Deutsch")
	@Size(max = 130)
	public String bezeichnungZeugnis;

	/** Die Nummern der Schienen */
	@Schema(description = "die Nummern der Schienen")
	public List<@NotNull @PositiveOrZero Integer> schienen;

	/** Die IDs der Jahrgänge */
	@Schema(description = "die IDs der Jahrgänge")
	public List<@NotNull Long> idJahrgaenge;

}
