package de.svws_nrw.module.reporting.types.schule;

import de.svws_nrw.core.types.reporting.ReportingBildDefinition;
import de.svws_nrw.module.reporting.types.ReportingBaseType;
import de.svws_nrw.module.reporting.utils.ReportingBildquelle;

/**
 * Ein Bild aus der Logoverwaltung, aufbereitet für die Nutzung in den Report-Vorlagen.
 * Neben dem Bild selbst kennt das Objekt die in der Bilddefinition geforderten Maße, so dass eine Vorlage
 * die Größe daraus ableiten kann.
 * Auch zu einer Bilddefinition ohne hinterlegtes Bild entsteht ein Objekt mit leeren Werten. Die Vorlagen
 * treffen damit nie auf {@code null}, was in den Templates mangels Safe Navigation zu einem Fehler führte.
 */
public class ReportingBild extends ReportingBaseType {

	/** Die Bilddefinition, zu der das Bild gehört. */
	private final ReportingBildDefinition bildDefinition;

	/** Die Bildquelle für die Vorlagen; leer, wenn kein darstellbares Bild vorliegt. */
	private final String htmlImageSource;


	/**
	 * Erstellt ein Bild zu der übergebenen Bilddefinition. Daten, aus denen sich keine Bildquelle bilden lässt, gelten wie ein fehlendes Bild:
	 * Was kein Renderer anzeigen kann, ist für eine Vorlage dasselbe wie nichts.
	 *
	 * @param bildDefinition Die Bilddefinition, zu der das Bild gehört. Bei einer unbekannten Definition {@code null}.
	 * @param bildDaten      Das Bild im Base64-Format, mit oder ohne den Kopf einer Data-URL. Ohne hinterlegtes Bild ein leerer String oder {@code null}.
	 */
	public ReportingBild(final ReportingBildDefinition bildDefinition, final String bildDaten) {
		this.bildDefinition = bildDefinition;
		this.htmlImageSource = ReportingBildquelle.ausBase64(ersetzeNullBlankTrim(bildDaten));
	}


	// ##### Getter #####

	/**
	 * Gibt an, ob eine anzeigbare Bildquelle vorliegt.
	 *
	 * @return true, wenn ein anzeigbares Bild vorliegt, andernfalls false.
	 */
	public boolean vorhanden() {
		return !htmlImageSource.isEmpty();
	}

	/**
	 * Das Bild als HTML-ImageSource inklusive MIME-Type.
	 *
	 * @return Die HTML-ImageSource im Base64-Format mit MIME-Type oder ein leerer String, wenn kein darstellbares Bild vorliegt.
	 */
	public String htmlImageSource() {
		return htmlImageSource;
	}

	/**
	 * Die in der Bilddefinition geforderte Breite des Bildes in Millimetern.
	 *
	 * @return Die Breite in mm oder 0 bei unbekannter Bilddefinition.
	 */
	public int breiteMM() {
		return (bildDefinition == null) ? 0 : bildDefinition.getBreite();
	}

	/**
	 * Die in der Bilddefinition geforderte Höhe des Bildes in Millimetern.
	 *
	 * @return Die Höhe in mm oder 0 bei unbekannter Bilddefinition.
	 */
	public int hoeheMM() {
		return (bildDefinition == null) ? 0 : bildDefinition.getHoehe();
	}

}
