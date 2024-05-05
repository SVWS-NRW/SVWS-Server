package de.svws_nrw.core.data.gost.klausurplanung;

import de.svws_nrw.core.transpiler.TranspilerDTO;
import de.svws_nrw.core.utils.gost.klausurplanung.GostKursklausurManager;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Informationen zu dem Stundenplan eines Schülers.
 */
@XmlRootElement
@Schema(description = "der Stundenplan eines Schülers.")
@TranspilerDTO
public class GostSchuelerklausurTerminRich {

	/** Die ID des Stundenplans. */
	@Schema(description = "die ID des Stundenplans", example = "815")
	public long id = -1;

	/** Die Startzeit der Klausur in Minuten seit 0 Uhr, sofern abweichend von Startzeit des gesamten Termins. */
	@Schema(description = "die Startzeit der Klausur in Minuten seit 0 Uhr, sofern abweichend von Startzeit des gesamten Termins", example = "540")
	public int startzeit = -1;

	/** Die textuelle Beschreibung des Stundenplans. */
	@Schema(description = "die textuelle Beschreibung des Stundenplans", example = "Stundenplan zum Schuljahresanfang")
	public long idKursklausur = -1;

	/** Die ID des Faches. */
	@Schema(description = "die ID des Faches", example = "2242")
	public long idFach = -1;

	/** Das Kürzel einer verallgemeinerten Kursart. */
	@Schema(description = "das Kürzel einer verallgemeinerten Kursart", example = "GK")
	public @NotNull String kursart = "";

	/** Die Dauer der Klausur in Minuten. */
	@Schema(description = "die Dauer der Klausur in Minuten", example = "135")
	public int dauer = 0;

	/** Die Auswahlzeit in Minuten, sofern vorhanden. */
	@Schema(description = "die Auswahlzeit in Minuten, sofern vorhanden", example = "30")
	public int auswahlzeit = 0;

	/** Die Information, ob es sich um eine mündliche Prüfung handelt. */
	@Schema(description = "die Information, ob es sich um eine mündliche Prüfung handelt", example = "false")
	public boolean istMdlPruefung = false;

	/** Die Information, ob Audioequipment nötig ist, z.B. für Klausuren mit Hörverstehensanteilen. */
	@Schema(description = "die Information, ob Audioequipment nötig ist, z.B. für Klasuren mit Hörverstehensanteilen", example = "false")
	public boolean istAudioNotwendig = false;

	/** Die Information, ob Videoequipment nötig ist, z.B. für Klausuren mit Videoanalyse. */
	@Schema(description = "die Information, ob Videoequipment nötig ist, z.B. für Klasuren mit Videoanalyse", example = "false")
	public boolean istVideoNotwendig = false;

	/**
	 * Konstruktor zur Erstellung des Rich-Objekts.
	 *
	 * @param termin     das zu vergleichende Objekt
	 * @param manager
	 *
	 */
	public GostSchuelerklausurTerminRich(final GostSchuelerklausurTermin termin, final GostKursklausurManager manager) {
		id = termin.id;
		final GostKursklausur kursklausur = manager.kursklausurBySchuelerklausurTermin(termin);
		startzeit = manager.startzeitByKursklausurOrException(kursklausur);
		idKursklausur = kursklausur.id;
		final GostKlausurvorgabe vorgabe = manager.vorgabeBySchuelerklausurTermin(termin);
		idFach = vorgabe.idFach;
		kursart = vorgabe.kursart;
		dauer = vorgabe.dauer;
		auswahlzeit = vorgabe.auswahlzeit;
		istMdlPruefung = vorgabe.istMdlPruefung;
		istAudioNotwendig = vorgabe.istAudioNotwendig;
		istVideoNotwendig = vorgabe.istVideoNotwendig;
	}

	/**
	 * Konstruktor für Transpiler.
	 */
	public GostSchuelerklausurTerminRich() {
	}

	/**
	 * Vergleicht, ob das aktuelle dasselbe Objekt, wie ein anderes übergebenes Objekt ist.
	 *
	 * @param another     das zu vergleichende Objekt
	 * @return true, falls die Objekte identisch sind, sonst false
	 */
	@Override
	public boolean equals(final Object another) {
		return another != null && another instanceof GostSchuelerklausurTerminRich && this.id == ((GostSchuelerklausurTerminRich) another).id;
	}

	/**
	 * Erzeugt den Hashcode zu Objekt auf Basis der id.
	 *
	 * @return den HashCode
	 */
	@Override
	public int hashCode() {
		return Long.hashCode(id);
	}

}
