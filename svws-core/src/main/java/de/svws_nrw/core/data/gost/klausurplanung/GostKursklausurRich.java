package de.svws_nrw.core.data.gost.klausurplanung;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die mit zur Blockung benötigten Daten angereicherten Informationen zu einer Gost-Kursklausur.
 */
@XmlRootElement
@Schema(description = "die mit zur Blockung benötigten Daten angereicherten Informationen zu einer Gost-Kursklausur")
@TranspilerDTO
public class GostKursklausurRich {

	/** Die ID der Kursklausur. */
	@Schema(description = "die ID der Kursklausur", example = "815")
	public long id = -1;

	/** Die ID der Klausur-Vorgabe. */
	@Schema(description = "die ID der Klausur-Vorgabe", example = "4711")
	public long idVorgabe = -1;

	/** Das Jahr, in welchem der Jahrgang Abitur machen wird. */
	@Schema(description = "das Jahr, in welchem der Jahrgang Abitur machen wird", example = "2025")
	public int abijahr = -1;

	/** Das Gost-Halbjahr, in dem die Klausur geschrieben wird. */
	@Schema(description = "das Gost-Halbjahr, in dem die Klausurg geschrieben wird", example = "3")
	public int halbjahr = -1;

	/** Das Quartal, in welchem die Klausur geschrieben wird. */
	@Schema(description = "das Quartal, in welchem die Klausur gechrieben wird", example = "1")
	public int quartal = -1;

	/** Die ID des Faches. */
	@Schema(description = "die ID des Faches", example = "2242")
	public long idFach = -1;

	/** Das Kürzel einer verallgemeinerten Kursart. */
	@Schema(description = "das Kürzel einer verallgemeinerten Kursart", example = "GK")
	public @NotNull String kursart = "";

	/** Die ID des Klausurkurses. */
	@Schema(description = "die ID des Klausurkurses", example = "9876")
	public long idKurs = -1;

	/** Die Kurzbezeichnung des Klausurkurses. */
	@Schema(description = "die Kurzbezeichnung des Klausurkurses", example = "BI-GK1")
	public String kursKurzbezeichnung = "";

	/** Die Schiene des Kurses. */
	@Schema(description = "die Schienen des Kurses", example = "{5, 4, 1}")
	public @NotNull int[] kursSchiene = {};

	/** Die ID des Kurslehrers. */
	@Schema(description = "die ID des Kurslehrers", example = "175")
	public Long idLehrer = null;

	/** Die ID des Klausurtermins, sofern schon gesetzt. */
	@Schema(description = "die ID des Klausurtermins, sofern schon gesetzt", example = "5555")
	public Long idTermin = null;

	/** Die Startzeit der Klausur in Minuten seit 0 Uhr, sofern abweichend von Startzeit des gesamten Termins. */
	@Schema(description = "die Startzeit der Klausur in Minuten seit 0 Uhr, sofern abweichend von Startzeit des gesamten Termins", example = "540")
	public Integer startzeit = null;

	/** Ein Array mit den IDs der zugehörigen Schüler. */
	@ArraySchema(schema = @Schema(implementation = GostSchuelerklausurterminraumstunde.class, description = "Ein Array mit den IDs der zugehörigen Schüler."))
	public @NotNull List<Long> schuelerIds = new ArrayList<>();

	/** Die textuelle Bemerkung zur Kursklausur, sofern vorhanden. */
	@Schema(description = "die textuelle Bemerkung zur Kursklausur, sofern vorhanden", example = "Zentrale Vergleichsklausur")
	public String bemerkung = null;

	/**
	 * Vergleicht, ob das aktuelle dasselbe Objekt, wie ein anderes übergebenes Objekt ist.
	 *
	 * @param another     das zu vergleichende Objekt
	 * @return true, falls die Objekte identisch sind, sonst false
	 */
	@Override
	public boolean equals(final Object another) {
		return (another != null) && (another instanceof GostKursklausurRich) && (this.id == ((GostKursklausurRich) another).id);
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

	/**
	 * Default-Konstruktor
	 */
	public GostKursklausurRich() {
		super();
	}

}
