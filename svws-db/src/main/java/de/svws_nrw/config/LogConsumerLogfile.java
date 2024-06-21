package de.svws_nrw.config;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;
import java.util.function.Consumer;

import de.svws_nrw.core.logger.LogData;
import jakarta.validation.constraints.NotNull;


/**
 * Diese Klasse implementiert das Funktionale Interface java.util.function.Consumer
 * für Objekt vom Typ LogData. Die über das Interface empfangeben Log-Daten
 * werden auf der Konsole ausgegeben.
 */
public class LogConsumerLogfile implements Consumer<@NotNull LogData> {

	/** Gibt an, ob die Zeit beim Loggen ausgegeben wird oder nicht. */
	public final boolean printTime;

	/** Gibt an, ob das Log-Level beim Loggen ausgegeben wird oder nicht. */
	public final boolean printLevel;

	/** Der Output-Stream für die Datei */
	public final PrintStream out;

	/** Gibt an, ob mit den letzten Log-Daten eine neue Zeile angefangen wurde. */
	private boolean lastLogDataHadNewLine = true;


	/**
	 * Erzeugt einen neuen Consumer für Log-Informationen, mit den Standardeinstellungen,
	 * das weder Zeit noch Log-Level mit ausgegeben werden.
	 *
	 * @param filename   der Dateiname der Log-Datei
	 *
	 * @throws IOException falls ein Fehler beim Datei-Zugriff auftritt
	 */
	public LogConsumerLogfile(final String filename) throws IOException {
		this(filename, false, false);
	}


	/**
	 * Erzeugt einen neuen Consumer für Log-Informationen.
	 *
	 * @param filename   der Dateiname der Log-Datei
	 * @param printTime     gibt an, ob die Zeit beim Loggen ausgegeben wird oder nicht
	 * @param printLevel    gibt an, ob das Log-Level beim Loggen ausgegeben wird oder nicht
	 *
	 * @throws IOException falls ein Fehler beim Datei-Zugriff auftritt
	 */
	public LogConsumerLogfile(final String filename, final boolean printTime, final boolean printLevel) throws IOException {
		this.printTime = printTime;
		this.printLevel = printLevel;
		final Path path = Paths.get(SVWSKonfiguration.get().getLoggingPath(), filename);
		out = new PrintStream(Files.newOutputStream(path, StandardOpenOption.CREATE, StandardOpenOption.APPEND, StandardOpenOption.SYNC));
	}


	/**
	 * Diese Methode implementiert das funktionale Interface java.util.function.Consumer
	 * und gibt die empfangenen Log-Informationen auf der Kommandozeile aus.
	 *
	 * @param t   die auszugebenden Log-Informationen
	 */
	@Override
	public void accept(final @NotNull LogData t) {
		if (t == null)
			return;
		String s = "";
		if ((lastLogDataHadNewLine && printTime))
			s += LocalDateTime.ofInstant(Instant.ofEpochMilli(t.getTime()), TimeZone.getDefault().toZoneId()) + " ";
		if (lastLogDataHadNewLine && printLevel)
			s += String.format("%-8.8s ", t.getLevel());
		s += t.getText();
		if (t.isNewLine())
			out.println(s);
		else
			out.print(s);
		out.flush();
		lastLogDataHadNewLine = t.isNewLine();
	}

}
