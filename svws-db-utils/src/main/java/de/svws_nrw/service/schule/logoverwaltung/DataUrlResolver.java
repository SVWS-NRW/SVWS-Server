package de.svws_nrw.service.schule.logoverwaltung;

import java.util.Base64;
import java.util.Collection;
import java.util.Optional;

import jakarta.annotation.Nonnull;
import org.apache.tika.Tika;
import org.apache.tika.mime.MediaType;
import org.apache.tika.mime.MimeTypes;

public final class DataUrlResolver {

	private static final String DATA_URL_FORMAT = "data:%s;base64,%s";
	private static final Tika TIKA = new Tika();

	private DataUrlResolver() {
		/* This utility class should not be instantiated */
	}

	/**
	 * Ermittelt die DATA-URL anhand eines übergebenen Base64-String.
	 * Der Base64-String kann ein reiner Base64-String oder eine bereits vollständige DATA-URL sein.
	 *
	 * @param base64 der Base64-String
	 *
	 * @return ein Optional.of({@link DataUrl}) oder wenn der übergebene Base64-String keine valide DATA-URL darstellt ein Optional.empty()
	 */
	public static Optional<DataUrl> resolve(final String base64) {
		if (base64 == null) {
			return Optional.empty();
		}

		final String payload = extractPayload(base64);
		if (payload == null) {
			return Optional.empty();
		}

		final MediaType mimeType = determineMimeType(payload);
		if (mimeType == null) {
			return Optional.empty();
		}

		final String dataUrl = DATA_URL_FORMAT.formatted(mimeType, payload);
		final double sizeInKB = getSizeInKB(payload);
		final String fileExtension = getFileExtensionByMimeType(mimeType.toString());

		return Optional.of(new ResolvedDataUrl(dataUrl, mimeType.toString(), payload, sizeInKB, fileExtension));
	}

	/**
	 * Extrahiert den Base64-Payload aus einer DATA-URL.
	 *
	 * @param dataUrl die DATA-URL
	 * @return der Base64-Payload oder {@code null}, wenn kein Payload gefunden wurde
	 */
	private static String extractPayload(final String dataUrl) {
		final int commaIndex = dataUrl.indexOf(',');
		if (commaIndex == -1) {
			return dataUrl;
		}

		if (dataUrl.length() <= (commaIndex + 1)) {
			return null;
		}

		return dataUrl.substring(commaIndex + 1);
	}

	/**
	 * Berechnet die Größe des Base64 in KB.
	 *
	 * @param base64Payload Base64-String
	 *
	 * @return Größe in KB
	 */
	private static double getSizeInKB(final String base64Payload) {
		final byte[] imageBytes = Base64.getDecoder().decode(base64Payload);
		return imageBytes.length / 1024.0;
	}

	/**
	 * Ermittelt den MIME-Type aus einem Base64-String.
	 *
	 * @param base64 der Base64-String
	 * @return der MIME-Type oder {@code null}, wenn der MIME-Type nicht ermittelt werden konnte
	 */
	private static MediaType determineMimeType(final String base64) {
		try {
			final byte[] imageBytes = Base64.getDecoder().decode(base64);
			final String mimeType = TIKA.detect(imageBytes);
			/* Aus org.apache.tika.detect.Detector: "Returns application/octet-stream if the type of the document can not be detected."
			   Bei einem (für TIKA) unbekannten MIME-Type würde application/octet-stream als valider MIME-Type zurückgegeben statt null. */
			if (MediaType.OCTET_STREAM.toString().equals(mimeType)) {
				return null;
			}
			return MediaType.parse(mimeType);
		} catch (final Exception e) {
			return null;
		}
	}

	/**
	 * Liefert zu einem MimeType, die passende Dateinamenendung (Bsp. <code>image/png</code> -> <code>.png</code>). Falls ein unbekannter MimeType übergeben
	 * wird, liefert die Methode <code>null</code> zurück.
	 *
	 * @param mimeType MimeType
	 *
	 * @return Dateinamenendung
	 */
	private static String getFileExtensionByMimeType(final String mimeType) {
		try {
			return MimeTypes.getDefaultMimeTypes()
					.forName(mimeType)
					.getExtension();
		} catch (final Exception e) {
			return null;
		}
	}

	/**
	 * Dieses Objekt beinhaltet eine bereits validierte DATA-URL mit MIME-Type und Base64-Payload.
	 * Objekte dieser Klasse können nur innerhalb des {@link DataUrlResolver} erzeugt werden.
	 *
	 * @param value DATA-URL mit MIME-Type Header und Base64-Payload
	 * @param mimeType MIME-Type des Base64-Payloads
	 * @param payload Base64 ohne DATA-URL-Header
	 * @param sizeInKB Größe des Base64 Payloads in KB
	 * @param fileExtension die Dateinamenendung
	 */
	private record ResolvedDataUrl(@Nonnull String value, @Nonnull String mimeType, @Nonnull String payload, double sizeInKB, String fileExtension)
			implements DataUrl {

		@Override
		public boolean hasAnyMimeTypeOf(final Collection<String> mimeTypes) {
			if ((mimeTypes == null) || mimeTypes.isEmpty()) {
				return false;
			}

			return mimeTypes.stream().anyMatch(type -> type.toLowerCase().equals(this.mimeType));
		}

	}
}
