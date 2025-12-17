package de.svws_nrw.core.data.schule;


import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse wird bei der Kommunikation über die Open-API-Schnittstelle verwendet.
 * Sie beschreibt, wie die Daten des schulspezifischen Betriebs übergeben werden.
 */
@XmlRootElement
@Schema(description = "Ein Eintrag im Katalog der schulspezifischen Betriebe.")
@TranspilerDTO
public class Betrieb {

	/** Die ID des Betriebs. */
	@Schema(description = "die ID des Betriebs", example = "4711", accessMode = Schema.AccessMode.READ_ONLY)
	public long id;

	/** Das Name des Betriebs. */
	@Schema(description = "Das Name des Betriebs.", example = "Rewe Wuppertal")
	public String name;

	/** Die Namenszusatz des Betriebs. */
	@Schema(description = "Die Namenszusatz des Betriebs.", example = "sehr guter Betrieb")
	public String nameZusatz;

	/** Bemerkungen */
	@Schema(description = "Bemerkungen", example = "ein wirklich sehr guter Betrieb")
	public String bemerkungen;

	/** Die Branche des Betriebs. */
	@Schema(description = "Die Branche des Betriebs.", example = "sehr guter Betrieb")
	public String branche;

	/** Die ID der Betriebsart des Betriebs. */
	@Schema(description = "Die ID der Betriebsart des Betriebs.", example = "1")
	public Long idBetriebsart;

	/** Gibt an, ob es sich bei dem Betrieb um einen Ausbildungsbetrieb handelt. */
	@Schema(description = "Gibt an, ob es sich bei dem Betrieb um einen Ausbildungsbetrieb handelt.", example = "true")
	public boolean istAusbildungsbetrieb;

	/** Gibt an, ob es sich bei dem Betrieb um einen Maßnahmenträger handelt. */
	@Schema(description = "Gibt an, ob es sich bei dem Betrieb um einen Maßnahmenträger handelt.", example = "true")
	public boolean istMassnahmentraeger;

	/** Gibt an, ob bei dem Betrieb eine Belehrung nach Infektionsschutzgesetz erforderlich ist. */
	@Schema(description = "Gibt an, ob bei dem Betrieb eine Belehrung nach Infektionsschutzgesetz erforderlich ist.", example = "true")
	public boolean belehrungNachISGErforderlich;

	/** Gibt an, ob bei dem Betrieb eine erweitertes Führungszeugnis erforderlich ist. */
	@Schema(description = "Gibt an, ob bei dem Betrieb eine erweitertes Führungszeugnis erforderlich ist.", example = "true")
	public boolean erweitertesFuehrungszeugnisErforderlich;

	/** Gibt an, ob der Betrieb Praktikumsplätze anbietet. */
	@Schema(description = "Gibt an, ob der Betrieb Praktikumsplätze anbietet.", example = "true")
	public boolean bietetPraktikumsplaetzeAn;

	/** Die Straße des Betriebs. */
	@Schema(description = "Die Straße des Betriebs.", example = "4711")
	public String strasse;

	/** Die Hausnummer des Betriebs. */
	@Schema(description = "Die Hausnummer des Betriebs.", example = "4711")
	public String hausnummer;

	/** Der Hausnummerzusatz des Betriebs. */
	@Schema(description = "Der Hausnummerzusatz des Betriebs.", example = "4711")
	public String hausnummerZusatz;

	/** Die ID des Betriebsortes. */
	@Schema(description = "Die ID des Betriebsortes.", example = "4711")
	public Long idOrt;

	/** Erste Telefonnummer des Betriebs. */
	@Schema(description = "Erste Telefonnummer des Betriebs.", example = "4711")
	public String telefon1;

	/** Zweite Telefonnummer des Betriebs. */
	@Schema(description = "Zweite Telefonnummer des Betriebs.", example = "4711")
	public String telefon2;

	/** Faxnummer des Betriebs. */
	@Schema(description = "Faxnummer des Betriebs", example = "4711")
	public String fax;

	/** E-Mail des Betriebs. */
	@Schema(description = "E-Mail des Betriebs.", example = "abc@betrieb.de")
	public String eMail;

	/** Gibt an, ob der Betrieb in der Anwendung sichtbar sein soll oder nicht. */
	@Schema(description = "gibt an, ob der Betrieb in der Anwendung sichtbar sein soll oder nicht", example = "true")
	public boolean istSichtbar;

	/** Die Sortierreihenfolge des Betriebs. */
	@Schema(description = "Die Sortierreihenfolge des Betriebs", example = "1")
	public int sortierung;

	/** Gibt an, ob der Betrieb in anderen Datenbanktabellen referenziert ist oder nicht. */
	@Schema(description = "Gibt an, ob der Betrieb in anderen Datenbanktabellen referenziert ist oder nicht.", example = "true", accessMode = Schema.AccessMode.READ_ONLY)
	public boolean referenziertInAnderenTabellen;

	/** Die Ansprechpartner des Betriebs. */
	@ArraySchema(
			schema = @Schema(implementation = BetriebeAnsprechpartner.class,
			description = "Die Ansprechpartner des Betriebs.",
			accessMode = Schema.AccessMode.READ_ONLY))
	public @NotNull List<BetriebeAnsprechpartner> ansprechpartner = new ArrayList<>();
}
