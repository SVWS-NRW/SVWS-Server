package de.svws_nrw.module.reporting.builders;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.db.utils.ApiOperationException;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.multipdf.LayerUtility;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.pdmodel.interactive.viewerpreferences.PDViewerPreferences;
import org.apache.pdfbox.util.Matrix;

import jakarta.ws.rs.core.Response;

/**
 * Baut ein Dokument im Format DIN A4 zu einer Broschüre im Format DIN A3 quer um. Vier A4-Seiten liegen dabei auf einem A3-Bogen: zwei auf der Vorderseite,
 * zwei auf der Rückseite. Die Reihenfolge ist so gewählt, dass ein beidseitiger Druck mit Wendung über die kurze Seite gefaltet das gelesene Dokument ergibt.
 * <p>Ein A4-Blatt füllt eine A3-Hälfte genau aus. Der Umbau skaliert daher nicht, und die Seitenränder bleiben unverändert.</p>
 * <p>Reicht die Seitenzahl nicht für ganze Bogen, bleiben die letzten Hälften leer. Weil jedes Dokument eigene Bogen füllt, teilen sich zwei Personen auch
 * dann kein Blatt, wenn ihre Zeugnisse unterschiedlich lang sind.</p>
 */
public final class ReportPdfBroschuere {

	/** Die Ergänzung, die den Dateinamen einer Broschürenausgabe von dem der A4-Ausgabe unterscheidet. */
	public static final String DATEINAME_ERGAENZUNG = "-DIN-A3";

	/** Die Zahl der A4-Seiten, die auf einen A3-Bogen passen. */
	private static final int SEITEN_JE_BOGEN = 4;

	/**
	 * Die Toleranz in Punkten, innerhalb der zwei Seitenmaße als gleich gelten. Die Maße entstehen aus Millimeterangaben des Stylesheets und treffen
	 * deshalb nicht bitgenau aufeinander.
	 */
	private static final float TOLERANZ_SEITENMASS = 1.0f;

	private ReportPdfBroschuere() {
		// Utility
	}


	/**
	 * Die beiden A4-Seiten, die auf einer A3-Seite nebeneinander liegen.
	 *
	 * @param links  Die Nummer der A4-Seite in der linken Hälfte, beginnend bei 1.
	 * @param rechts Die Nummer der A4-Seite in der rechten Hälfte, beginnend bei 1.
	 */
	record A3Seite(int links, int rechts) {
		// Reiner Datenträger der Seitenfolge.
	}


	/**
	 * Baut das übergebene A4-Dokument zu einer A3-Broschüre um und hinterlegt die Druckeinstellungen im Ergebnis.
	 *
	 * @param pdfA4 Das Dokument im Format DIN A4.
	 *
	 * @return Das Dokument im Format DIN A3 quer, in der Seitenfolge einer Broschüre.
	 *
	 * @throws ApiOperationException Bei einem Abbruch; die Exception trägt den Abbruchgrund als Meldung. Ein Dokument mit unterschiedlich großen Seiten
	 *                               lässt sich nicht falzen und wird mit {@code BAD_REQUEST} abgelehnt.
	 */
	public static byte[] umbauAufA3(final byte[] pdfA4) throws ApiOperationException {
		try (PDDocument quelle = Loader.loadPDF(pdfA4);
				PDDocument ziel = new PDDocument();
				ByteArrayOutputStream ausgabe = new ByteArrayOutputStream(pdfA4.length)) {
			uebernimmMetadaten(quelle, ziel);
			setzeDruckeinstellungen(ziel);
			legeSeitenAufBogen(quelle, ziel);
			ziel.save(ausgabe);
			return ausgabe.toByteArray();
		} catch (final ApiOperationException e) {
			// Der abgelehnte Seitenmix trägt seinen eigenen Status; sonst würde der allgemeine Catch daraus einen Serverfehler machen.
			throw e;
		} catch (final Exception e) {
			throw new ApiOperationException(Response.Status.INTERNAL_SERVER_ERROR, e,
					"### FEHLER: Das Dokument konnte für den Druck auf DIN A3 nicht umgebaut werden.");
		}
	}


	/**
	 * Fügt mehrere Broschüren zu einem Dokument zusammen und hinterlegt die Druckeinstellungen im Ergebnis. Die Broschüren folgen einander in der
	 * übergebenen Reihenfolge; jede belegt ganze Bogen, sodass der beidseitige Druck die Blätter einer Broschüre nicht mit denen der nächsten mischt.
	 *
	 * @param broschueren Die Broschüren, jede bereits im Format DIN A3 quer.
	 *
	 * @return Das zusammengefügte Dokument.
	 *
	 * @throws ApiOperationException Bei einem Abbruch; die Exception trägt den Abbruchgrund als Meldung.
	 */
	public static byte[] fuegeZusammen(final List<byte[]> broschueren) throws ApiOperationException {
		// Die Teile bleiben bis nach dem Speichern geöffnet: Das Zusammenfügen greift beim Schreiben noch auf ihre Inhalte zu.
		final List<PDDocument> teile = new ArrayList<>(broschueren.size());
		try (PDDocument ziel = new PDDocument();
				ByteArrayOutputStream ausgabe = new ByteArrayOutputStream()) {
			final PDFMergerUtility zusammenfuehrung = new PDFMergerUtility();
			for (final byte[] broschuere : broschueren) {
				final PDDocument teil = Loader.loadPDF(broschuere);
				teile.add(teil);
				zusammenfuehrung.appendDocument(ziel, teil);
			}
			// Nach dem Zusammenfügen, damit keine Angabe eines Teils die Angaben des Ergebnisses überschreibt.
			if (!teile.isEmpty()) {
				uebernimmMetadaten(teile.getFirst(), ziel);
			}
			setzeDruckeinstellungen(ziel);
			ziel.save(ausgabe);
			return ausgabe.toByteArray();
		} catch (final Exception e) {
			throw new ApiOperationException(Response.Status.INTERNAL_SERVER_ERROR, e,
					"### FEHLER: Die Broschüren konnten nicht zu einem Dokument zusammengestellt werden.");
		} finally {
			teile.forEach(ReportPdfBroschuere::schliesseStill);
		}
	}


	/**
	 * Gibt die Folge der A3-Seiten zurück, die die übergebene Zahl an A4-Seiten ergibt: erst Vorder- und Rückseite des äußeren Bogens, dann die des nächsten.
	 * Die Zahl wird dafür auf ein Vielfaches von vier aufgerundet; die überzähligen Seitennummern bezeichnen die leeren Hälften.
	 *
	 * @param anzahlSeiten Die Zahl der A4-Seiten des Dokuments.
	 *
	 * @return Die A3-Seiten in der Reihenfolge, in der sie gedruckt werden.
	 */
	static List<A3Seite> seitenfolge(final int anzahlSeiten) {
		final int anzahlBogen = ((anzahlSeiten + SEITEN_JE_BOGEN) - 1) / SEITEN_JE_BOGEN;
		final int seitenGesamt = anzahlBogen * SEITEN_JE_BOGEN;
		final List<A3Seite> folge = new ArrayList<>(2 * anzahlBogen);
		for (int bogen = 0; bogen < anzahlBogen; bogen++) {
			// Je weiter innen der Bogen liegt, desto weiter rücken beide Hälften von den Rändern der Seitenfolge zur Mitte.
			final int versatz = 2 * bogen;
			folge.add(new A3Seite(seitenGesamt - versatz, 1 + versatz));
			folge.add(new A3Seite(2 + versatz, (seitenGesamt - 1) - versatz));
		}
		return folge;
	}


	/**
	 * Legt die Seiten der Quelle paarweise auf die Bogen des Zieldokuments.
	 *
	 * @param quelle Das Dokument im Format DIN A4.
	 * @param ziel   Das Dokument, das die A3-Bogen aufnimmt.
	 *
	 * @throws ApiOperationException Wenn die Seiten der Quelle unterschiedlich groß sind.
	 * @throws IOException           Wenn eine Seite nicht übernommen oder gezeichnet werden kann.
	 */
	private static void legeSeitenAufBogen(final PDDocument quelle, final PDDocument ziel) throws ApiOperationException, IOException {
		final PDRectangle seitenformatA4 = seitenformat(quelle);
		final PDRectangle seitenformatA3 = new PDRectangle(2 * seitenformatA4.getWidth(), seitenformatA4.getHeight());
		final LayerUtility uebernahme = new LayerUtility(ziel);

		for (final A3Seite bogenseite : seitenfolge(quelle.getNumberOfPages())) {
			final PDFormXObject linkeHaelfte = uebernommeneSeite(uebernahme, quelle, bogenseite.links());
			final PDFormXObject rechteHaelfte = uebernommeneSeite(uebernahme, quelle, bogenseite.rechts());
			final PDPage bogen = new PDPage(seitenformatA3);
			ziel.addPage(bogen);
			try (PDPageContentStream inhalt = new PDPageContentStream(ziel, bogen)) {
				zeichneHaelfte(inhalt, linkeHaelfte, 0.0f);
				zeichneHaelfte(inhalt, rechteHaelfte, seitenformatA4.getWidth());
			}
		}
	}


	/**
	 * Gibt das Seitenformat der Quelle zurück und stellt sicher, dass alle Seiten es teilen. Ein Dokument mit unterschiedlich großen Seiten ergäbe
	 * verschobene oder überstehende Hälften und lässt sich nicht als Broschüre falzen.
	 *
	 * @param quelle Das Dokument im Format DIN A4.
	 *
	 * @return Das Format der Seiten.
	 *
	 * @throws ApiOperationException Wenn das Dokument keine Seite enthält oder die Seiten unterschiedlich groß sind.
	 */
	private static PDRectangle seitenformat(final PDDocument quelle) throws ApiOperationException {
		if (quelle.getNumberOfPages() == 0) {
			throw new ApiOperationException(Response.Status.INTERNAL_SERVER_ERROR,
					"### FEHLER: Das Dokument für den Druck auf DIN A3 enthält keine Seite.");
		}
		final PDRectangle format = quelle.getPage(0).getCropBox();
		for (int seite = 1; seite < quelle.getNumberOfPages(); seite++) {
			final PDRectangle weiteresFormat = quelle.getPage(seite).getCropBox();
			if ((Math.abs(weiteresFormat.getWidth() - format.getWidth()) > TOLERANZ_SEITENMASS)
					|| (Math.abs(weiteresFormat.getHeight() - format.getHeight()) > TOLERANZ_SEITENMASS)) {
				throw new ApiOperationException(Response.Status.BAD_REQUEST,
						"### FEHLER: Der Druck auf DIN A3 verlangt, dass alle Seiten des Dokuments dasselbe Format und dieselbe Ausrichtung haben.");
			}
		}
		return format;
	}


	/**
	 * Übernimmt eine Seite der Quelle als zeichenbares Objekt des Zieldokuments.
	 *
	 * @param uebernahme   Die Übernahme, die an das Zieldokument gebunden ist.
	 * @param quelle       Das Dokument im Format DIN A4.
	 * @param seitennummer Die Nummer der Seite, beginnend bei 1.
	 *
	 * @return Die übernommene Seite, oder {@code null} für eine leere Hälfte.
	 *
	 * @throws IOException Wenn die Seite nicht übernommen werden kann.
	 */
	private static PDFormXObject uebernommeneSeite(final LayerUtility uebernahme, final PDDocument quelle, final int seitennummer) throws IOException {
		if (seitennummer > quelle.getNumberOfPages()) {
			return null;
		}
		return uebernahme.importPageAsForm(quelle, seitennummer - 1);
	}


	/**
	 * Zeichnet eine übernommene Seite in eine Hälfte des Bogens. Eine leere Hälfte zeichnet nichts.
	 *
	 * @param inhalt   Der Inhalt des Bogens.
	 * @param haelfte  Die übernommene Seite, oder {@code null} für eine leere Hälfte.
	 * @param versatzX Der waagerechte Versatz der Hälfte in Punkten.
	 *
	 * @throws IOException Wenn die Seite nicht gezeichnet werden kann.
	 */
	private static void zeichneHaelfte(final PDPageContentStream inhalt, final PDFormXObject haelfte, final float versatzX) throws IOException {
		if (haelfte == null) {
			return;
		}
		inhalt.saveGraphicsState();
		inhalt.transform(Matrix.getTranslateInstance(versatzX, 0.0f));
		inhalt.drawForm(haelfte);
		inhalt.restoreGraphicsState();
	}


	/**
	 * Hinterlegt im Dokument, wie es gedruckt werden soll: beidseitig mit Wendung über die kurze Seite, ohne Anpassung der Größe und auf Papier im Format
	 * der Seiten. Die Angaben sind ein Vorschlag an das anzeigende Programm; ob es ihn übernimmt, entscheidet es selbst.
	 *
	 * @param ziel Das Dokument im Format DIN A3 quer.
	 */
	private static void setzeDruckeinstellungen(final PDDocument ziel) {
		final PDViewerPreferences einstellungen = new PDViewerPreferences(new COSDictionary());
		einstellungen.setDuplex(PDViewerPreferences.DUPLEX.DuplexFlipShortEdge);
		einstellungen.setPrintScaling(PDViewerPreferences.PRINT_SCALING.None);
		// Für diese Angabe führt PDFBox keine eigene Methode; sie veranlasst die Papierwahl nach dem Seitenformat und damit nach DIN A3.
		einstellungen.getCOSObject().setBoolean(COSName.getPDFName("PickTrayByPDFSize"), true);
		ziel.getDocumentCatalog().setViewerPreferences(einstellungen);
	}


	/**
	 * Übernimmt die Angaben zum Dokument, damit das Ergebnis dieselbe Herkunft nennt wie die A4-Ausgabe.
	 *
	 * @param quelle Das Dokument, dessen Angaben gelesen werden.
	 * @param ziel   Das Dokument, das die Angaben erhält.
	 */
	private static void uebernimmMetadaten(final PDDocument quelle, final PDDocument ziel) {
		final PDDocumentInformation herkunft = quelle.getDocumentInformation();
		final PDDocumentInformation angaben = ziel.getDocumentInformation();
		angaben.setAuthor(herkunft.getAuthor());
		angaben.setCreator(herkunft.getCreator());
		angaben.setProducer(herkunft.getProducer());
		angaben.setCreationDate(herkunft.getCreationDate());
		angaben.setModificationDate(herkunft.getModificationDate());
	}


	/**
	 * Schließt ein geöffnetes Dokument. Ein Fehler dabei bleibt unbeachtet: Er beträfe nur noch den Speicher eines Dokuments, dessen Inhalt bereits
	 * geschrieben ist, und verdeckte einen echten Abbruch des Zusammenfügens.
	 *
	 * @param dokument Das zu schließende Dokument.
	 */
	private static void schliesseStill(final PDDocument dokument) {
		try {
			dokument.close();
		} catch (final IOException e) {
			// Siehe JavaDoc: Das Schließen eines bereits ausgewerteten Dokuments trägt keine Aussage für den Anwender.
		}
	}

}
