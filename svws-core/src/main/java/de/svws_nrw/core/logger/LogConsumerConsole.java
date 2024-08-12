package de.svws_nrw.core.logger;

import java.util.function.Consumer;

import de.svws_nrw.core.utils.DateUtils;
import jakarta.validation.constraints.NotNull;


/**
 * Diese Klasse implementiert das Funktionale Interface java.util.function.Consumer
 * für Objekt vom Typ LogData. Die über das Interface empfangeben Log-Daten
 * werden auf der Konsole ausgegeben.
 */
public class LogConsumerConsole implements Consumer<LogData> {

	/** Gibt an, ob die Zeit beim Loggen ausgegeben wird oder nicht. */
	public final boolean printTime;

	/** Gibt an, ob das Log-Level beim Loggen ausgegeben wird oder nicht. */
	public final boolean printLevel;

	/** Gibt an, ob mit den letzten Log-Daten eine neue Zeile angefangen wurde. */
	private boolean lastLogDataHadNewLine = true;


	/**
	 * Erzeugt einen neuen Consumer für Log-Informationen, mit den Standardeinstellungen,
	 * das weder Zeit noch Log-Level mit ausgegeben werden.
	 */
	public LogConsumerConsole() {
		printTime = false;
		printLevel = false;
	}


	/**
	 * Erzeugt einen neuen Consumer für Log-Informationen.
	 *
	 * @param printTime     gibt an, ob die Zeit beim Loggen ausgegeben wird oder nicht
	 * @param printLevel    gibt an, ob das Log-Level beim Loggen ausgegeben wird oder nicht
	 */
	public LogConsumerConsole(final boolean printTime, final boolean printLevel) {
		this.printTime = printTime;
		this.printLevel = printLevel;
	}


	/**
	 * Diese Methode implementiert das funktionale Interface java.util.function.Consumer
	 * und gibt die empfangenen Log-Informationen auf der Kommandozeile aus.
	 *
	 * @param t   die auszugebenden Log-Informationen
	 */
	@Override
	public void accept(final @NotNull LogData t) {
		final String s = ((lastLogDataHadNewLine && printTime) ? DateUtils.toISO8601(t.getTime()) + " " : "")
				+ ((lastLogDataHadNewLine && printLevel) ? t.getLevel() + " " : "")
				+ t.getText();
		if (t.isNewLine())
			System.out.println(s);
		else
			System.out.print(s);
		System.out.flush();
		lastLogDataHadNewLine = t.isNewLine();
	}

}
