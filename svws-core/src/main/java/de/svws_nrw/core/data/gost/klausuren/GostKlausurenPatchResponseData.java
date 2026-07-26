package de.svws_nrw.core.data.gost.klausuren;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Sammlung von Objekten, die durch eine Raumzuweisung, Termin-/Zeitverschiebung
 * oder Nachschreiberänderung geändert werden.
 */
@XmlRootElement
@Schema(description = "die Sammlung von Objekten, die durch eine Raumzuweisung, Termin-/Zeitverschiebung "
		+ "oder Nachschreiberänderung geändert werden.")
@TranspilerDTO
public class GostKlausurenPatchResponseData {

	/** Die gepatchte Kursklausur. */
	@Schema(implementation = GostKursklausur.class,
			description = "Die gepatchte Kursklausur.")
	public GostKursklausur kursklausurPatched = null;

	/** Der gepatchte Klausurtermin. */
	@Schema(implementation = GostKlausurtermin.class,
			description = "Der gepatchte Klausurtermin.")
	public GostKlausurtermin terminPatched = null;

	/** Ein Array mit den gepatchten Schülerklausurterminen. */
	@ArraySchema(schema = @Schema(implementation = GostSchuelerklausurtermin.class,
			description = "Ein Array mit den gepatchten Schülerklausurterminen."))
	public @NotNull List<GostSchuelerklausurtermin> schuelerklausurterminePatched = new ArrayList<>();

	/** Die enthaltenen Raumdaten werden durch die Veränderung neu erzeugt. */
	@Schema(implementation = GostKlausurenRaumdaten.class,
			description = "Die enthaltenen Raumdaten werden durch die Veränderung neu erzeugt.")
	public @NotNull GostKlausurenRaumdaten raumdaten = new GostKlausurenRaumdaten();

	/** Ein Array mit den Klausurraumstunden, die durch die Veränderung gelöscht wurden. */
	@ArraySchema(schema = @Schema(implementation = GostKlausurraumstunde.class,
			description = "Ein Array mit den Klausurraumstunden, die durch die Veränderung gelöscht wurden."))
	public @NotNull List<GostKlausurraumstunde> raumstundenGeloescht = new ArrayList<>();

	/** Ein Array mit den Schülerklausurterminraumstunden, die durch die Veränderung gelöscht wurden. */
	@ArraySchema(schema = @Schema(implementation = GostSchuelerklausurterminraumstunde.class,
			description = "Ein Array mit den Schülerklausurterminraumstunden, die durch die Veränderung gelöscht wurden."))
	public @NotNull List<GostSchuelerklausurterminraumstunde> schuelerklausurterminraumstundenGeloescht = new ArrayList<>();

	/**
	 * Default-Konstruktor
	 */
	public GostKlausurenPatchResponseData() {
		super();
	}

	/**
	 * Fügt die Daten der übergebenen Instanz zu den aktuellen Daten hinzu.
	 * @param data die zu hinzuzufügenden Daten
	 */
	public void addAll(final @NotNull GostKlausurenPatchResponseData data) {
		if (data.kursklausurPatched != null) {
			kursklausurPatched = data.kursklausurPatched;
		}
		if (data.terminPatched != null) {
			terminPatched = data.terminPatched;
		}
		schuelerklausurterminePatched.addAll(data.schuelerklausurterminePatched);
		raumdaten.addAll(data.raumdaten);
		raumstundenGeloescht.addAll(data.raumstundenGeloescht);
		schuelerklausurterminraumstundenGeloescht.addAll(data.schuelerklausurterminraumstundenGeloescht);
	}
}
