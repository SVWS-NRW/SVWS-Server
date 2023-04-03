package de.svws_nrw.db;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Diese Klasse stellt Hilfsmethoden für die spezielle
 * Handhabung von SQL für SQLite zur Verfügung.
 */
public class SQLiteUtils {

	private static final Pattern patternConcat = Pattern.compile(Pattern.quote("concat"), Pattern.CASE_INSENSITIVE);
	private static final Pattern patternBraceCommaOrQuote = Pattern.compile("[\'\\(\\),]");

	/**
	 * Ersetze alle Vorkommnisse der concat-Methode in dem SQL-String
	 * mit dem || - Operator
	 *
	 * @param sql   der SQL-String
	 *
	 * @return der SQL-String mit dem ersetzten concat
	 */
	public static String replaceConcat(final String sql) {
		String tmpSql = sql;
		while (true) {
			Matcher matcher = patternConcat.matcher(tmpSql);
			if (!matcher.find())
				break;
			final StringBuilder result = new StringBuilder();
			result.append(tmpSql.substring(0, matcher.start()));
			int countBraces = 0;
			int countQuotes = 0;
			String tmp = tmpSql.substring(matcher.end());
			// Prüfe auf beginnende Klammer
			matcher = patternBraceCommaOrQuote.matcher(tmp);
			if (!matcher.find())
				throw new RuntimeException("Error: Cannot replace concat in SQL-Query: " + sql);
			char found = tmp.charAt(matcher.start());
			if (found != '(')
				throw new RuntimeException("Error: Missing ( after concat in SQL-Query: " + sql);
			countBraces++;
			tmp = tmp.substring(matcher.end());
			// Prüfe auf Anführungszeichen, Klammer oder Komma
			while (true) {
				matcher = patternBraceCommaOrQuote.matcher(tmp);
				if (!matcher.find())
					throw new RuntimeException("Error: Cannot replace concat in SQL-Query: " + sql);
				found = tmp.charAt(matcher.start());
				switch (found) {
					case '(' -> {
						if (countQuotes == 0)
							countBraces++;
						result.append(tmp.substring(0, matcher.end()));
					}
					case ')' -> {
						if (countQuotes == 0)
							countBraces--;
						if (countBraces > 0)
							result.append(tmp.substring(0, matcher.end()));
						else
							result.append(tmp.substring(0, matcher.start()));
					}
					case '\'' -> {
						countQuotes = (countQuotes == 0) ? 1 : 0;
						result.append(tmp.substring(0, matcher.end()));
					}
					case ',' -> {
						if ((countQuotes == 0) && (countBraces == 1)) {
							result.append(tmp.substring(0, matcher.start())).append(" || ");
						} else
							result.append(tmp.substring(0, matcher.end()));
					}
				}
				tmp = tmp.substring(matcher.end());
				if (countBraces == 0) {
					result.append(tmp);
					break;
				}
			}
			tmpSql = result.toString();
		}
		return tmpSql;
	}

}
