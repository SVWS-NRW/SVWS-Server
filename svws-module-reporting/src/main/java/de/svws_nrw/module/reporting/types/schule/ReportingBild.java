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

	/** Das Bild im Base64-Format, ohne den Kopf einer Data-URL. */
	private final String base64;

	/** Die Bildquelle, beim ersten Zugriff aus dem Bild abgeleitet. */
	private String htmlImageSource;


	/**
	 * Erstellt ein Bild zu der übergebenen Bilddefinition.
	 *
	 * @param bildDefinition Die Bilddefinition, zu der das Bild gehört. Bei einer unbekannten Definition {@code null}.
	 * @param base64         Das Bild im Base64-Format. Ohne hinterlegtes Bild ein leerer String oder {@code null}.
	 */
	public ReportingBild(final ReportingBildDefinition bildDefinition, final String base64) {
		this.bildDefinition = bildDefinition;
		this.base64 = ersetzeNullBlankTrim(base64);
	}


	// ##### Getter #####

	/**
	 * Gibt an, ob eine anzeigbare Bildquelle vorliegt. Bilddaten, aus denen sich keine Data-URL bilden lässt, gelten als nicht vorhanden: Ein Bild, das
	 * kein Renderer darstellen kann, ist für eine Vorlage dasselbe wie ein fehlendes.
	 *
	 * @return true, wenn ein anzeigbares Bild vorliegt, andernfalls false.
	 */
	public boolean vorhanden() {
		return !htmlImageSource().isEmpty();
	}

	/**
	 * Das Bild als HTML-ImageSource inklusive MIME-Type. Den Typ bestimmt {@link ReportingBildquelle} aus den Bilddaten.
	 * Die Zeichenkette entsteht nur beim ersten Zugriff: Eine Ausgabe in einzelne Dateien rendert die Vorlage je Datei erneut und baute sie sonst jedes Mal
	 * neu auf, obwohl sich das Bild über den gesamten Aufruf nicht ändert.
	 *
	 * @return Die HTML-ImageSource im Base64-Format mit MIME-Type oder ein leerer String, wenn kein oder ein nicht auflösbares Bild vorliegt.
	 */
	public String htmlImageSource() {
		if (htmlImageSource == null) {
			htmlImageSource = ReportingBildquelle.ausBase64(base64);
		}
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
