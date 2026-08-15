package de.svws_nrw.module.reporting.diagnose;

import java.util.Objects;

/**
 * Das Ergebnis eines Datenzugriffs für einen einzelnen Wert: geladen, nicht vorhanden oder fehlgeschlagen.
 * <p>Der Zustand wird explizit geführt, weil {@code null} allein nicht sagt, warum ein Wert fehlt - ein nicht existierender Datensatz und ein gescheiterter
 * Zugriff sähen gleich aus. Eine leere Collection ist ein geladener Wert; ein geladener Wert ist nie {@code null}; ein fehlender Cache-Eintrag heißt "noch
 * nicht geladen". HTTP-Status und fachlicher Schlüssel gehören nicht hierher, ebenso wenig der Benutzerfilter - der ist eine Auswahlentscheidung.</p>
 *
 * @param <T> Der Typ des geladenen Wertes.
 */
public final class ReportingLadezustand<T> {

	/** Der geladene Wert oder null, wenn der Zugriff keinen Wert ergeben hat. */
	private final T wert;

	/** Die Ursache, aus der kein Wert vorliegt, oder null bei einem geladenen Wert. */
	private final ReportingProblemursache ursache;

	/** Der Fehler, an dem der Ladevorgang gescheitert ist, oder null. */
	private final Exception fehler;


	/**
	 * Erzeugt einen Zustand. Der Konstruktor ist privat; die Fabrikmethoden benennen den jeweiligen Zustand und sichern seine Invarianten.
	 *
	 * @param wert    Der geladene Wert oder null.
	 * @param ursache Die Ursache, aus der kein Wert vorliegt, oder null.
	 * @param fehler  Der Fehler, an dem der Ladevorgang gescheitert ist, oder null.
	 */
	private ReportingLadezustand(final T wert, final ReportingProblemursache ursache, final Exception fehler) {
		this.wert = wert;
		this.ursache = ursache;
		this.fehler = fehler;
	}


	/**
	 * Erzeugt den Zustand eines erfolgreich geladenen Wertes.
	 *
	 * @param <T>  Der Typ des geladenen Wertes.
	 * @param wert Der geladene Wert. Eine leere Collection ist zulässig, {@code null} ist es nicht.
	 *
	 * @return Der Zustand mit dem geladenen Wert.
	 *
	 * @throws IllegalArgumentException Wenn kein Wert übergeben wurde - ein fehlender Wert gehört in einen der anderen Zustände.
	 */
	public static <T> ReportingLadezustand<T> geladen(final T wert) {
		if (wert == null) {
			throw new IllegalArgumentException("Ein geladener Wert darf nicht null sein.");
		}
		return new ReportingLadezustand<>(wert, null, null);
	}

	/**
	 * Erzeugt den Zustand eines Datensatzes, den es nicht gibt.
	 *
	 * @param <T> Der Typ des geladenen Wertes.
	 *
	 * @return Der Zustand ohne Wert mit der Ursache {@link ReportingProblemursache#NICHT_VORHANDEN}.
	 */
	public static <T> ReportingLadezustand<T> nichtVorhanden() {
		return new ReportingLadezustand<>(null, ReportingProblemursache.NICHT_VORHANDEN, null);
	}

	/**
	 * Erzeugt den Zustand eines gescheiterten Ladevorgangs. Der auslösende Fehler bleibt erhalten, damit er später mit Stacktrace protokolliert oder als
	 * {@code cause} weitergegeben werden kann.
	 *
	 * @param <T>     Der Typ des geladenen Wertes.
	 * @param ursache Die Ursache des Fehlschlags nach {@link ReportingProblemursache#istFehlschlagUrsache()}.
	 * @param fehler  Der auslösende Fehler oder {@code null}, wenn der Zugriff ohne Exception erfolglos blieb.
	 *
	 * @return Der Zustand ohne Wert mit der übergebenen Ursache.
	 *
	 * @throws IllegalArgumentException Wenn die Ursache keine Ladefehler-Ursache ist.
	 */
	public static <T> ReportingLadezustand<T> fehlgeschlagen(final ReportingProblemursache ursache, final Exception fehler) {
		if (!Objects.requireNonNull(ursache, "Ein gescheiterter Ladevorgang braucht eine Ursache.").istFehlschlagUrsache()) {
			throw new IllegalArgumentException("Die Ursache %s beschreibt keinen gescheiterten Ladevorgang.".formatted(ursache.name()));
		}
		return new ReportingLadezustand<>(null, ursache, fehler);
	}

	/**
	 * Erzeugt einen Zustand über einem anderen Werttyp, der Ursache und Fehler des übergebenen Zustands übernimmt - etwa wenn ein Datensatz entfällt, weil
	 * seine Stammdaten fehlen. Ohne die Übernahme würde aus einem gescheiterten Zugriff eine fachlich fehlende Akte.
	 *
	 * @param <T>     Der Werttyp des erzeugten Zustands.
	 * @param zustand Der Zustand, dessen Ursache und Fehler übernommen werden.
	 *
	 * @return Der Zustand ohne Wert mit derselben Ursache und demselben Fehler.
	 *
	 * @throws IllegalArgumentException Wenn der übergebene Zustand geladen ist - ein geladener Zustand erklärt kein Fehlen.
	 */
	public static <T> ReportingLadezustand<T> uebernimmUrsache(final ReportingLadezustand<?> zustand) {
		if (Objects.requireNonNull(zustand, "Ohne Zustand liegt keine Ursache vor, die zu übernehmen wäre.").istGeladen()) {
			throw new IllegalArgumentException("Ein geladener Zustand erklärt kein Fehlen und trägt keine Ursache.");
		}
		if (zustand.ursache().istFehlschlagUrsache()) {
			return fehlgeschlagen(zustand.ursache(), zustand.fehler());
		}
		return nichtVorhanden();
	}


	/**
	 * Gibt an, ob ein Wert geladen werden konnte.
	 *
	 * @return true, wenn ein Wert vorliegt, sonst false.
	 */
	public boolean istGeladen() {
		return this.wert != null;
	}

	/**
	 * Gibt den geladenen Wert zurück.
	 *
	 * @return Der geladene Wert oder null, wenn keiner vorliegt. Warum er fehlt, sagt {@link #ursache()}.
	 */
	public T wert() {
		return this.wert;
	}

	/**
	 * Gibt die Ursache zurück, aus der kein Wert vorliegt.
	 *
	 * @return Die Ursache oder null, wenn ein Wert geladen wurde.
	 */
	public ReportingProblemursache ursache() {
		return this.ursache;
	}

	/**
	 * Gibt den Fehler zurück, an dem der Ladevorgang gescheitert ist.
	 *
	 * @return Der Fehler oder null, wenn der Zugriff erfolgreich war oder ohne Exception erfolglos blieb.
	 */
	public Exception fehler() {
		return this.fehler;
	}

}
