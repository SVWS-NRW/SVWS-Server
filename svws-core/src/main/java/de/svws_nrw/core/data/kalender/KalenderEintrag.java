package de.svws_nrw.core.data.kalender;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.core.transpiler.TranspilerDTO;
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
	@Schema(description = "die ID des Kalendereintrag", example = "123")
	public @NotNull String id = "";

	/** ID des Kalenders */
	@Schema(description = "die ID des Kalenders, zu dem der Kalendereintrag gehört", example = "global")
	public @NotNull String kalenderId = "";

	/**
	 * URI der ICS-Repräsentation des Kalendereintrags
	 */
	@Schema(description = "uid des kalenderEintrags", example = "exf80")
	public @NotNull String uid = "";

	/**
	 * Versionskennzeichen des Kontaks
	 */
	@Schema(description = "Versionskennzeichen des Kontakts", example = "78")
	public @NotNull String version = "";

	/** das serialisierte .ics dieses Kalendereintrags */
	@Schema(description = "das serialisierte .ics dieses Kalendereintrags", example = "...")
	public @NotNull String data = "";

	/** der Startzeitpunkt dieses Kalendereintrags als SQL-Timestamp */
	@Schema(description = "der Startzeitpunkt dieses Kalendereintrags ", example = "2022-10-13 14:00:00.000")
	public String kalenderStart;

	/** der Endzeitpunkt dieses Kalendereintrags als SQL-Timestamp */
	@Schema(description = "der Endzeitpunkt dieses Kalendereintrags ", example = "2022-10-13 14:00:00.000")
	public String kalenderEnde;

	/** ob der angemeldete Nutzer Schreibrecht auf dem Kalender hat */
	@Schema(description = "Schreibrecht des angemeldeten Nutzers", example = "true")
	public boolean darfSchreiben;

	/** ob der angemeldete Nutzer Leserecht auf dem Kalender hat */
	@Schema(description = "Leserecht des angemeldeten Nutzers", example = "true")
	public boolean darfLesen;

	/** ob der angemeldete Benutzer Besitzer des Kalenders ist, zu dem dieser Eintrag gehört */
	@Schema(description = "ob der angemeldete Benutzer Besitzer des Kalenders ist, zu dem dieser Eintrag gehört", example = "true")
	public boolean istBesitzer;

	/** der Typ des Kalendereintrags */
	@Schema(description = "der Typ des Kalendereintrags ", example = "VEVENT")
	public @NotNull String kalenderTyp = "VEVENT";
}
