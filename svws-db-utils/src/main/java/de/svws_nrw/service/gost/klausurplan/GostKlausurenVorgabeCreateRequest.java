package de.svws_nrw.service.gost.klausurplan;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Die Klasse beschreibt das DTO für das Erstellen einer GOSt-Klausurvorgabe.
 */
@Schema(description = "die Informationen zum Erstellen einer GOSt-Klausurvorgabe.")
public class GostKlausurenVorgabeCreateRequest {

	/** Der Abiturjahrgang. */
	@Schema(description = "der Abiturjahrgang")
	@NotNull(message = "Der Abiturjahrgang muss gesetzt werden.")
	public Integer abiJahrgang;

	/** Das GOSt-Halbjahr. */
	@Schema(description = "das GOSt-Halbjahr")
	@NotNull(message = "Das GOSt-Halbjahr muss gesetzt werden.")
	public Integer halbjahr;

	/** Das Quartal. */
	@Schema(description = "das Quartal")
	@NotNull(message = "Das Quartal muss gesetzt werden.")
	public Integer quartal;

	/** Die Fach-ID. */
	@Schema(description = "die Fach-ID")
	@NotNull(message = "Die Fach-ID muss gesetzt werden.")
	public Long idFach;

	/** Die Kursart. */
	@Schema(description = "die Kursart")
	@NotNull(message = "Die Kursart muss gesetzt werden.")
	public String kursart;

	/** Die Klausurdauer. */
	@Schema(description = "die Klausurdauer")
	public Integer dauer;

	/** Die Auswahlzeit. */
	@Schema(description = "die Auswahlzeit")
	public Integer auswahlzeit;

	/** Gibt an, ob eine GKL möglich ist. */
	@Schema(description = "gibt an, ob eine GKL möglich ist")
	public Boolean istGklMoeglich;

	/** Gibt an, ob es sich um eine mündliche Prüfung handelt. */
	@Schema(description = "gibt an, ob es sich um eine mündliche Prüfung handelt")
	public Boolean istMdlPruefung;

	/** Gibt an, ob Audio benötigt wird. */
	@Schema(description = "gibt an, ob Audio benötigt wird")
	public Boolean istAudioNotwendig;

	/** Gibt an, ob Video benötigt wird. */
	@Schema(description = "gibt an, ob Video benötigt wird")
	public Boolean istVideoNotwendig;

	/** Die Bemerkung zur Klausurvorgabe. */
	@Schema(description = "die Bemerkung zur Klausurvorgabe")
	public String bemerkungVorgabe;

}
