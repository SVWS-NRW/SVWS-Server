package de.svws_nrw.core.data.stundenplan;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Informationen zu dem Zeitraster eines Stundenplans.
 */
@XmlRootElement
@Schema(description = "das Zeitraster eines Stundenplans.")
@TranspilerDTO
public class StundenplanZeitraster {

	/** Die ID des Zeitraster-Eintrages. */
	@Schema(description = "die ID des Zeitraster-Eintrages", example = "4711")
	public long id = -1;

	/** Der Wochentag an dem der Unterricht stattfindet (1=Montag, 2=Dienstag, ..., 7=Sonntag) */
	@Schema(description = "der Wochentag an dem der Unterricht stattfindet (1=Montag, 2=Dienstag, ..., 7=Sonntag)", example = "1")
	public int wochentag = -1;

	/** Die Nummer der Unterrichtsstunde an dem Wochentag */
	@Schema(description = "die Nummer der Unterrichtsstunde an dem Wochentag", example = "1")
	public int unterrichtstunde = -1;

	/** Die Uhrzeit, wann die Unterrichtsstunde beginnt. */
	@Schema(description = "die Uhrzeit, wann die Unterrichtsstunde beginnt", example = "7:10:00")
	public @NotNull String stundenbeginn = "";

	/** Die Uhrzeit, wann die Unterrichtsstunde endet. */
	@Schema(description = "die Uhrzeit, wann die Unterrichtsstunde endet", example = "7:55:00")
	public @NotNull String stundenende = "";


	/**
	 * Erstellt einen Eintrag mit Standardwerten
	 */
	public StundenplanZeitraster() {
	}


	/**
	 * Erstellt einen Eintrag mit den angegebenen Werten
	 *
	 * @param id                 die ID
	 * @param wochentag          der Wochentag an dem der Unterricht stattfindet (1=Montag, 2=Dienstag, ..., 7=Sonntag)
	 * @param unterrichtstunde   die Nummer der Unterrichtsstunde an dem Wochentag
	 * @param stundenbeginn      die Uhrzeit, wann die Unterrichtsstunde beginnt
	 * @param stundenende        die Uhrzeit, wann die Unterrichtsstunde endet
	 */
	public StundenplanZeitraster(final long id, final int wochentag, final int unterrichtstunde, final @NotNull String stundenbeginn, final @NotNull String stundenende) {
		this.id = id;
		this.wochentag = wochentag;
		this.unterrichtstunde = unterrichtstunde;
		this.stundenbeginn = stundenbeginn;
		this.stundenende = stundenende;
	}

}
