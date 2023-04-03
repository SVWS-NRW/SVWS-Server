package de.svws_nrw.core.data.kalender;

import java.util.List;
import java.util.Vector;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import jakarta.xml.bind.annotation.XmlRootElement;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle
 * verwendet. Sie beschreibt ein Kalender.
 */
@XmlRootElement
@Schema(description = "Ein Kalender.")
@TranspilerDTO
public class Kalender {

	/** ID des Kalenders */
	@Schema(description = "die ID des Kalenders", example = "global")
	public @NotNull String id = "";

	/** Anzeigename des Kalenders */
	@Schema(description = "Name des Kalenders", example = "Globaler Kalender")
	public String displayname;

	/** Beschreibung des Kalenders */
	@Schema(description = "Beschreibung des Kalenders", example = "Globaler Kalender für öffentliche Kalender.")
	public String beschreibung;

	/** Versionskennzeichen des Kalenders */
	@Schema(description = "Versionskennzeichen des Kalenders", example = "98")
	public long synctoken;

	/**
	 * der Typ des Kalenders
	 */
	@Schema(description = "der Typ des Kalenders", example = "GENERIERT")
	public @NotNull String kalenderTyp = "";

	/**
	 * eine Liste der Einträge des Kalenders
	 */
	@ArraySchema(schema = @Schema(description = "eine Liste der Einträge des Kalenders", example = "..."))
	public @NotNull List<@NotNull KalenderEintrag> kalenderEintraege = new Vector<>();

	/** ob der angemeldete Nutzer Schreibrecht auf dem Kalender hat */
	@Schema(description = "Schreibrecht des angemeldeten Nutzers", example = "true")
	public boolean darfSchreiben;

	/** ob der angemeldete Nutzer Leserecht auf dem Kalender hat */
	@Schema(description = "Leserecht des angemeldeten Nutzers", example = "true")
	public boolean darfLesen;

	/** BenutzerId des Besitzers dieses Kalenders */
	@Schema(description = "BenutzerId des Besitzers dieses Kalenders", example = "1")
	public long besitzer;
}
