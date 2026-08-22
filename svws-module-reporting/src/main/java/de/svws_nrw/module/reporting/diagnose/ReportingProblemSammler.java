package de.svws_nrw.module.reporting.diagnose;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.module.reporting.utils.ReportingExceptionUtils;

/**
 * Sammelt die hingenommenen Ausgabeprobleme eines Reporting-Aufrufs und protokolliert sie in einem Schritt - getrennt befüllt liefen Log und Sammlung
 * auseinander. Aufgerufen wird er über die Fassade {@code ReportingContext.meldeAusgabeproblem(...)}; der Context gibt ihn nicht heraus.
 * <p>Das Log-Level ist nicht wählbar: Ein hingenommenes Problem erzeugt höchstens eine {@link LogLevel#WARNING}, denn ein {@code ERROR} würde die Ausgabe
 * beenden. Der Sammler ist aufrufbezogen und hält Diagnosedaten, keinen fachlichen Cache. Der Fehlerblock aus Typ, Ursachenkette und Stacktrace steht je
 * Fehler-Instanz nur einmal im Log, auch wenn sie mit mehreren Befunden reist.</p>
 */
public final class ReportingProblemSammler {

	/** Der Logger, in den die Probleme protokolliert werden. */
	private final Logger logger;

	/** Die gemeldeten Probleme in der Reihenfolge ihres ersten Auftretens. */
	private final Set<ReportingProblem> probleme = new LinkedHashSet<>();

	/**
	 * Die Fehler-Instanzen, deren Fehlerblock bereits im Log steht. Verglichen wird über die Objektidentität: Zwei gleichartige Fehler verschiedener
	 * Zugriffe sind zwei Fehler mit je eigenem Block.
	 */
	private final Set<Exception> protokollierteFehler = Collections.newSetFromMap(new IdentityHashMap<>());


	/**
	 * Erzeugt einen leeren Sammler für einen Reporting-Aufruf.
	 *
	 * @param logger Der Logger, in den die Probleme protokolliert werden. Er ist zwingend: Ohne ihn entstünde eine Sammlung ohne Logeinträge, und die beiden
	 *               Kanäle liefen genau dort auseinander, wo sie gemeinsam entstehen sollen.
	 */
	public ReportingProblemSammler(final Logger logger) {
		this.logger = Objects.requireNonNull(logger, "Der Sammler protokolliert jedes gemeldete Problem und braucht dafür einen Logger.");
	}


	/**
	 * Meldet ein hingenommenes Ausgabeproblem: registrieren, und nur wenn es neu ist, protokollieren. Dedupliziert wird über Ursache, Auswirkung und
	 * Schlüssel - derselbe Fehler steht so nicht dreißigmal im Log.
	 *
	 * @param ursache      Woran es liegt; eine abbrechende Ursache ist unzulässig.
	 * @param auswirkung   Was daraus in der Ausgabe folgt.
	 * @param schluessel   Welches Objekt betroffen ist.
	 * @param beschreibung Der Sachverhalt für das Log.
	 * @param fehler       Der auslösende Fehler oder {@code null}.
	 *
	 * @throws IllegalArgumentException Wenn die Ursache die Ausgabe abbricht - eine Störung wird geworfen, nicht gesammelt.
	 */
	public void melde(final ReportingProblemursache ursache, final ReportingProblemauswirkung auswirkung, final ReportingProblemSchluessel schluessel,
			final String beschreibung, final Exception fehler) {
		if (!probleme.add(new ReportingProblem(ursache, auswirkung, schluessel))) {
			return;
		}

		protokolliere(schluessel, beschreibung, fehler);
	}

	/**
	 * Protokolliert ein neu gemeldetes Problem mit {@link LogLevel#WARNING}. Ein vorhandener Fehler wird samt Stacktrace ausgegeben - die meldende Stelle
	 * wirft nicht, dieser Eintrag ist die einzige Gelegenheit dafür. Reist dieselbe Fehler-Instanz mit einem weiteren Befund - etwa eine nicht ladbare
	 * Lehrkraft, die als angefordertes Hauptobjekt ausgelassen und zusätzlich als Klassenleitung gemeldet wird -, genügt die Meldung mit einem Verweis: Ihr Block stünde
	 * sonst je Befund erneut im Log.
	 *
	 * @param schluessel   Welches Objekt betroffen ist.
	 * @param beschreibung Der Sachverhalt für das Log.
	 * @param fehler       Der auslösende Fehler oder null.
	 */
	private void protokolliere(final ReportingProblemSchluessel schluessel, final String beschreibung, final Exception fehler) {
		final String meldung = "%s [%s]".formatted((beschreibung == null) ? "" : beschreibung, schluessel.beschreibung()).strip();
		if (fehler == null) {
			logger.logLn(LogLevel.WARNING, 0, meldung);
		} else if (protokollierteFehler.add(fehler)) {
			ReportingExceptionUtils.logException(meldung, fehler, logger, LogLevel.WARNING, 0);
		} else {
			logger.logLn(LogLevel.WARNING, 0, meldung + " (Fehlerdetails stehen beim ersten Eintrag zu diesem Fehler.)");
		}
	}


	/**
	 * Gibt die Anzahl der gemeldeten Ausgabeprobleme zurück. Mehrfach gemeldete Probleme zählen einmal.
	 *
	 * @return Die Anzahl.
	 */
	public int anzahl() {
		return probleme.size();
	}

	/**
	 * Gibt an, ob der Aufruf bislang ohne Ausgabeproblem geblieben ist.
	 *
	 * @return true, wenn kein Problem gemeldet wurde, sonst false.
	 */
	public boolean istLeer() {
		return probleme.isEmpty();
	}

	/**
	 * Gibt die gemeldeten Ausgabeprobleme in der Reihenfolge ihres ersten Auftretens zurück.
	 *
	 * @return Die unveränderliche Liste der Probleme.
	 */
	public List<ReportingProblem> probleme() {
		return List.copyOf(probleme);
	}

}
