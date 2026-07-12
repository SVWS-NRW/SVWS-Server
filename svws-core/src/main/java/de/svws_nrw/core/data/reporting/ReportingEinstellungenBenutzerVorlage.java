package de.svws_nrw.core.data.reporting;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.transpiler.TranspilerDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse definiert das Speicherformat der benutzerspezifischen Einstellungen einer einzelnen Reportvorlage in der
 * Client-Konfiguration (Key-Schema {@code reporting.einstellungen.benutzer.vorlage.<bezeichnung>}). Gespeichert werden
 * ausschließlich Werte bzw. Referenzen auf Katalog-Optionen — nie die Options-Metadaten selbst, damit die Einstellungen
 * automatisch robust gegen künftige Katalog-Änderungen bleiben (unbekannte Referenzen werden beim Anwenden ignoriert).
 * Das Ausgabeformat gehört bewusst nicht zu diesen Einstellungen, da es je Ausgabeweg über einen eigenen API-Endpunkt
 * bestimmt wird.
 */
@XmlRootElement
@Schema(description = "Die benutzerspezifischen Einstellungen einer Reportvorlage.")
@TranspilerDTO
public class ReportingEinstellungenBenutzerVorlage {

	/** Die Version des Speicherformats, um künftige Format-Migrationen zu ermöglichen. */
	@Schema(description = "Die Version des Speicherformats, um künftige Format-Migrationen zu ermöglichen.", example = "1")
	public int version = 1;

	/** Die gespeicherten Werte der vorlagenspezifischen Parameter. Fehlt ein Parameter, so gilt sein Katalog-Default. */
	@Schema(description = "Die gespeicherten Werte der vorlagenspezifischen Parameter.")
	public @NotNull List<ReportingEinstellungenBenutzerVorlagenParameterWert> parameterWerte = new ArrayList<>();

	/**
	 * Die gespeicherten Sortierungsauswahlen je Sortiergruppe. Verwendet dieselbe Struktur wie {@link #filterungsauswahlen},
	 * da beide Fälle identisch sind: eine Gruppe (identifiziert über ihre {@code bezeichnung}) und die darin ausgewählten
	 * Katalog-Einträge als geordnete Liste ihrer {@code bezeichnung}en.
	 */
	@Schema(description = "Die gespeicherten Sortierungsauswahlen je Sortiergruppe.")
	public @NotNull List<ReportingEinstellungenBenutzerVorlageGruppe> sortierungsauswahlen = new ArrayList<>();

	/** Die gespeicherten Filterauswahlen je Filtergruppe. */
	@Schema(description = "Die gespeicherten Filterauswahlen je Filtergruppe.")
	public @NotNull List<ReportingEinstellungenBenutzerVorlageGruppe> filterungsauswahlen = new ArrayList<>();


	/**
	 * Diese Klasse definiert das Speicherformat der benutzerspezifischen Einstellungen einer einzelnen Reportvorlage.
	 */
	public ReportingEinstellungenBenutzerVorlage() {
		super();
	}
}
