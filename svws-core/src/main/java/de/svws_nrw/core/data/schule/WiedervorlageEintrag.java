package de.svws_nrw.core.data.schule;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie liefert die Daten für einen Eintrag in der Wiedervorlage.
 */
@XmlRootElement
@Schema(description = "ein Eintrag in der Wiedervorlage.")
@TranspilerDTO
public class WiedervorlageEintrag {

	/** Die ID des Eintrags. */
	@Schema(description = "die ID des Eintrags", example = "42", accessMode = Schema.AccessMode.READ_ONLY)
	public long id = -1;

	/** Die ID des Benutzers, der den Eintrag erstellt hat. */
	@Schema(description = "die ID des Benutzers, der den Eintrag erstellt hat.", example = "42", accessMode = Schema.AccessMode.READ_ONLY)
	public long idBenutzer = -1;

	/** Der Name des Benutzers, der die Wiedervorlage angelegt hat. */
	@Schema(description = "Der Name des Benutzers, der die Wiedervorlage angelegt hat.", example = "Maria Musterfrau",
			accessMode = Schema.AccessMode.READ_ONLY)
	public String nameBenutzerAngelegt;

	/** Die ID der Benutzergruppe, welcher der Eintrag zugeordnet ist, oder null, falls keine Zuordnung vorliegt. */
	@Schema(description = "die ID der Benutzergruppe, welcher der Eintrag zugeordnet ist, oder null, falls keine Zuordnung vorliegt", example = "null")
	public Long idBenutzergruppe;

	/** Die ID des Person-Typs, auf den sich die Wiedervorlage bezieht (1 = Lehrer, 2 = Schueler, 3 = Erzieher oder NULL für keinen) */
	@Schema(description = "die ID des Person-Typs, auf den sich die Wiedervorlage bezieht (1 = Lehrer, 2 = Schueler, 3 = Erzieher oder NULL für keinen)",
			example = "1")
	public Integer typPerson;

	/** Die ID der Person in Abhängigkeit des Person-Typs, auf den sich die Wiedervorlage bezieht (ggf. Lehrer-ID, Schüler-ID oder Erzieher-ID) */
	@Schema(description = "die ID der Person in Abhängigkeit des Person-Typs, auf den sich die Wiedervorlage bezieht (ggf. Lehrer-ID, Schüler-ID oder Erzieher-ID)",
			example = "4711")
	public Long idPerson;

	/** Der vollständige Name der Person, auf die sich die Wiedervorlage bezieht. */
	@Schema(description = "Der vollständige Name der Person, auf die sich die Wiedervorlage bezieht.", example = "Maria Musterfrau",
			accessMode = Schema.AccessMode.READ_ONLY)
	public String namePerson;

	/** Die Bemerkung der Wiedervorlage, die bem Benutzer angezeigt wird. */
	@Schema(description = "die Bemerkung der Wiedervorlage, die bem Benutzer angezeigt wird",
			example = "die Laufbahnbescheinigung muss noch ausgedruckt werden")
	public @NotNull String bemerkung = "";

	/** Gibt den Zeitstempel an, wann der Eintrag angelegt wurde. */
	@Schema(description = "Gibt den Zeitstempel an, wann der Eintrag angelegt wurde.", example = "2013-11-14 13:12:48.774",
			accessMode = Schema.AccessMode.READ_ONLY)
	public String tsAngelegt;

	/** Gibt den Zeitstempel an, wann der Eintrag angelegt wurde. */
	@Schema(description = "Gibt den Zeitstempel an, ab wann der Eintrag zur Wiedervorlage angezeigt werden soll.", example = "2013-11-14 13:12:48.774")
	public String tsWiedervorlage;

	/** Gibt den Zeitstempel an, wann der Eintrag als erledigt markiert wurde. */
	@Schema(description = "Gibt den Zeitstempel an, wann der Eintrag als erledigt markiert wurde.", example = "2013-11-14 13:12:48.774",
			accessMode = Schema.AccessMode.READ_ONLY)
	public String tsErledigt;

	/** Die ID des Benutzers, der den Eintrag als erledigt markiert hat, oder null. */
	@Schema(description = "die ID des Benutzers, der den Eintrag als erledigt markiert hat, oder null.", example = "null",
			accessMode = Schema.AccessMode.READ_ONLY)
	public Long idBenutzerErledigt;

	/** Der Name des Benutzers, der die Wiedervorlage erledigt hat. */
	@Schema(description = "Der Name des Benutzers, der die Wiedervorlage erledigt hat.", example = "Maria Musterfrau",
			accessMode = Schema.AccessMode.READ_ONLY)
	public String nameBenutzerErledigt;

	/** Gibt an, ob der Eintrag automatisch als erledigt markiert werden soll, wenn er einem Benutzer nach dem Wiedervorlage-Zeitpunkt angezeigt wurde. */
	@Schema(description = "gibt an, ob der Eintrag automatisch als erledigt markiert werden soll, wenn er einem Benutzer nach dem Wiedervorlage-Zeitpunkt angezeigt wurde",
			example = "null")
	public boolean automatischErledigt;

}
