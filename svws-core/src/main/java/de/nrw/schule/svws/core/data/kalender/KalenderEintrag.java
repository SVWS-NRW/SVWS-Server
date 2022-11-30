package de.nrw.schule.svws.core.data.kalender;

import jakarta.xml.bind.annotation.XmlRootElement;

import de.nrw.schule.svws.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Eintrag in einem Kalender
 */
@XmlRootElement
@Schema(description = "Ein Eintrag in einem Kalender.")
@TranspilerDTO
public class KalenderEintrag {
	/** ID des Kalendereintrags */
	@Schema(required = true, description = "die ID des Kalendereintrag", example = "123")
	public @NotNull String id = "";

	/** ID des Kalenders */
	@Schema(required = true, description = "die ID des Kalenders, zu dem der Kalendereintrag gehört", example = "global")
	public @NotNull String kalenderId = "";

	/**
	 * URI der ICS-Repräsentation des Kalendereintrags
	 */
	@Schema(required = true, description = "uid des kalenderEintrags", example = "exf80")
	public @NotNull String uid = "";

	/**
	 * Versionskennzeichen des Kontaks
	 */
	@Schema(required = true, description = "Versionskennzeichen des Kontakts", example = "78")
	public @NotNull String version = "";

	/** das serialisierte .ics dieses Kalendereintrags */
	@Schema(required = true, description = "das serialisierte .ics dieses Kalendereintrags", example = "...")
	public @NotNull String data = "";

	/** der Startzeitpunkt dieses Kalendereintrags als SQL-Timestamp */
	@Schema(required = false, description = "der Startzeitpunkt dieses Kalendereintrags ", example = "2022-10-13 14:00:00.000")
	public String kalenderStart;

	/** der Endzeitpunkt dieses Kalendereintrags als SQL-Timestamp */
	@Schema(required = false, description = "der Endzeitpunkt dieses Kalendereintrags ", example = "2022-10-13 14:00:00.000")
	public String kalenderEnde;

	/** ob der angemeldete Nutzer Schreibrecht auf dem Kalender hat */
	@Schema(required = true, description = "Schreibrecht des angemeldeten Nutzers", example = "true")
	public boolean darfSchreiben;

	/** ob der angemeldete Nutzer Leserecht auf dem Kalender hat */
	@Schema(required = true, description = "Leserecht des angemeldeten Nutzers", example = "true")
	public boolean darfLesen;

	/** ob der angemeldete Benutzer Besitzer des Kalenders ist, zu dem dieser Eintrag gehört */
	@Schema(required = true, description = "ob der angemeldete Benutzer Besitzer des Kalenders ist, zu dem dieser Eintrag gehört", example = "true")
	public boolean istBesitzer;

	/** der Typ des Kalendereintrags */
	@Schema(required = true, description = "der Typ des Kalendereintrags ", example = "VEVENT")
	public @NotNull String kalenderTyp = "VEVENT";
}
