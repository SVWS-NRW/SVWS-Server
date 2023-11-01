package de.svws_nrw.transpiler.test;

import jakarta.xml.bind.annotation.XmlRootElement;
import de.svws_nrw.core.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die gültigen Statistikwerte für den Katalog der Schulformen.
 */
@XmlRootElement
@Schema(description = "ein Eintrag in dem Katalog der Schulformen.")
@TranspilerDTO
public class SchulformKatalogEintrag extends CoreTypeData {


	/** Die ID des Katalog-Eintrags. */
	@Schema(description = "die ID des Katalog-Eintrags", example = "4711")
	public long id;

	/** Das Kürzel der Schulform, welches im Rahmen der amtlichen Schulstatistik verwendet wird */
	@Schema(description = "das Kürzel der Schulform, welches im Rahmen der amtlichen Schulstatistik verwendet wird", example = "GE")
	public @NotNull String kuerzel = "";

	/**
	 * Die Nummer, welche im Rahmen der amtlichen Schulstatistik verwendet wird. Diese wird zwar
	 * in der SVWS-DB bei der Schule gespeichert, aber dort aus dem Schulverzeichnis genommen.
	 * Der Wert hier sollte i.A. nicht benötigt werden, da eine Unterscheidung anhand des Kürzels
	 * stattfindet.
	 * Stand 4.1.2021: Bei der "Hibernia"-Schulform und der Schulform "Schule für Kranke" ist der
	 *                 Wert der Nummer fehlerhaft, da dort eine Doppelung beim Kürzel vorliegt und
	 *                 diese somit nicht korrekt erfasst werden.
	 */
	@Schema(description = "die Nummer der Schulform, welche im Rahmen der amtlichen Schulstatistik verwendet wird", example = "17")
	public @NotNull String nummer = "";

	/** Die Bezeichnung der Schulform. */
	@Schema(description = "die Bezeichnung der Schulform", example = "Gesamtschule")
	public @NotNull String bezeichnung = "";

	/** Gibt an, ob eine Schule der Schulform eine gymnasiale Oberstufe haben kann oder nicht. */
	@Schema(description = "gibt an, ob eine Schule der Schulform eine gymnasiale Oberstufe haben kann oder nicht", example = "true")
	public boolean hatGymOb;

	/** Gibt an, in welchem Schuljahr die Schulform einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(description = "gibt an, in welchem Schuljahr die Schulform einführt wurde. Ist kein Schuljahr bekannt, so ist null gesetzt", example = "null")
	public Integer gueltigVon = null;

	/** Gibt an, bis zu welchem Schuljahr die Schulform gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt. */
	@Schema(description = "gibt an, bis zu welchem Schuljahr die Schulform gültig ist. Ist kein Schuljahr bekannt, so ist null gesetzt", example = "2025")
	public Integer gueltigBis = null;


	/**
	 * Erstellt einen Schulform-Eintrag mit Standardwerten
	 */
	public SchulformKatalogEintrag() {
	}


	/**
	 * Erstellt einen Schulform-Eintrag mit den angegebenen Werten
	 *
	 * @param id           die ID
	 * @param kuerzel      das Kürzel
	 * @param nummer       die Nummer
	 * @param bezeichnung  die Bezeichnung
	 * @param hatGymOb     gibt an, ob die Schulform eien gymnasiale Oberstufe hat
	 * @param gueltigVon   das Schuljahr, wann der Eintrag eingeführt wurde oder null, falls es nicht bekannt ist und "schon immer gültig war"
	 * @param gueltigBis   das Schuljahr, bis zu welchem der Eintrag gültig ist
	 */
	public SchulformKatalogEintrag(final long id, final @NotNull String kuerzel, final @NotNull String nummer, final @NotNull String bezeichnung, final boolean hatGymOb, final Integer gueltigVon, final Integer gueltigBis) {
		this.id = id;
		this.kuerzel = kuerzel;
		this.nummer = nummer;
		this.bezeichnung = bezeichnung;
		this.hatGymOb = hatGymOb;
		this.gueltigVon = gueltigVon;
		this.gueltigBis = gueltigBis;
	}

}
