package de.svws_nrw.module.reporting.builders;

import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.Set;

import de.svws_nrw.db.utils.ApiOperationException;


/**
 * Diese Klasse bietet eine Implementierung eines ReportBuilders zur Erstellung von PDF-Dokumenten.
 * Sie basiert auf HTML-Inhalten und zusätzlichen Konfigurationen, die durch die zugehörige Rendering-Strategie umgesetzt werden.
 */
public final class ReportBuilderPdf extends ReportBuilder<byte[]> {

	/** Der Kontext, der alle notwendigen Informationen für die Erstellung des ReportBuilderPdf enthält. */
	private final ReportBuilderContextPdf builderContext;

	/**
	 * Konstruktor, der eine neue Instanz von ReportBuilderPdf erstellt, basierend auf
	 * den Einstellungen des übergebenen {@link ReportBuilderContextPdf}.
	 *
	 * @param builderContext Der Kontext, der die Konfigurationen und Parameter für die PDF-Report-Erstellung enthält,
	 *                        einschließlich HTML-Inhalt, Dateinamen und Root-Pfad.
	 *
	 * @throws ApiOperationException Wird geworfen, wenn die im Kontext definierten Anforderungen (z. B. HTML-Inhalt,
	 *                                Dateiname, Root-Pfad) nicht erfüllt sind oder fehlerhaft konfiguriert wurden.
	 */
	public ReportBuilderPdf(final ReportBuilderContextPdf builderContext) throws ApiOperationException {
		super(builderContext.validiert(), "application/pdf", builderContext.getDateiname());
		this.builderContext = builderContext;
	}

	/**
	 * Gibt die Menge der IDs der Hauptdaten zurück, für die dieser Builder die PDF-Datei erzeugt. Die zurückgegebene Menge ist unveränderlich.
	 *
	 * @return eine unveränderliche Menge von IDs (Long-Werten)
	 */
	public Set<Long> getIds() {
		return Collections.unmodifiableSet(builderContext.getIds());
	}

	/**
	 * Gibt den vollständigen Dateinamen des PDF-Reports mit der Dateiendung ".pdf" zurück.
	 *
	 * @return der vollständige Dateiname des Reports als String mit der Endung ".pdf"
	 */
	@Override
	public String getDateinameMitEndung() {
		return dateiname + ".pdf";
	}

	/**
	 * Generiert ein Byte-Array, das den Inhalt des Reports darstellt.
	 * Diese Methode nutzt die Funktionalität der Oberklasse, um das generierte Byte-Array zurückzugeben.
	 *
	 * @return Ein Byte-Array, das den generierten Report repräsentiert
	 *
	 * @throws ApiOperationException Wird geworfen, wenn ein Fehler während der Generierung des PDF-Reports auftritt
	 */
	@Override
	public byte[] generate() throws ApiOperationException {
		return getByteArray();
	}

	/**
	 * Generiert ein Byte-Array, das den Inhalt eines PDF-Reports darstellt.
	 * Diese Methode nutzt eine Rendering-Strategie, um den PDF-Report basierend auf HTML-Inhalt zu rendern.
	 * Der gerenderte Inhalt wird in einen ByteArrayOutputStream geschrieben und als Byte-Array zurückgegeben.
	 *
	 * @return Ein Byte-Array, das den generierten PDF-Report repräsentiert
	 *
	 * @throws ApiOperationException Wird geworfen, wenn ein Fehler während der Generierung des PDF-Reports auftritt. Der Fehler des Renderers wird
	 *                               unverändert weitergegeben - mit seinem Status, seiner Meldung und ohne einen zusätzlichen Logeintrag: Der Builder
	 *                               kennt keinen Zusammenhang, den nicht der Renderer selbst, der Stacktrace oder die Sammelausgabe genauer benennen.
	 */
	@Override
	protected byte[] generateInternalByteArray() throws ApiOperationException {
		final int htmlLength = this.builderContext.getHtmlInput().length() * 2;
		final int initialSize = Math.clamp(htmlLength, 8 * 1024, 1024 * 1024);
		final ByteArrayOutputStream baoStream = new ByteArrayOutputStream(initialSize);
		this.builderContext.getRenderer().renderPdf(this.builderContext.getHtmlInput(), this.reportBuilderContext.getRootPfad(), baoStream);
		return baoStream.toByteArray();
	}

}
