package de.svws_nrw.module.reporting.diagnose;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.LongFunction;

/**
 * Das Ergebnis der Auswahl der Hauptdaten eines Reports: welche Datensätze angefordert waren, welche in die Ausgabe gelangen und was mit den übrigen
 * geschehen ist. Die Auswahl reicht bis zur Ausgabefactory - sonst würde dort erneut über die Request-IDs geladen und das Auslassen wäre wirkungslos.
 * <p>Ausgelassen und ausgefiltert sind getrennt: Ein ausgelassener Datensatz ist ein gemeldetes Ausgabeproblem, ein ausgefilterter eine gewollte
 * Auswahlentscheidung. Das Ergebnis ist unveränderlich; Einschränkungen entstehen über {@link #nurMitGeladenen} als neues Ergebnis.</p>
 *
 * @param <T> Der Reporting-Typ der ausgewählten Hauptdaten.
 */
public final class ReportingAuswahlergebnis<T> {

	/** Die angeforderten IDs, bereinigt um Duplikate und leere Einträge, in der Reihenfolge der Anfrage. */
	private final List<Long> idsAngefordert;

	/** Die ausgewählten Objekte je ID, in der Reihenfolge, in der sie ausgegeben werden. */
	private final LinkedHashMap<Long, T> objekte;

	/** Die ausgelassenen IDs mit dem Zustand, der ihr Fehlen erklärt. */
	private final LinkedHashMap<Long, ReportingLadezustand<T>> ausgelassen;

	/** Die IDs, die der Benutzerfilter ausschließt. */
	private final List<Long> idsAusgefiltert;


	/**
	 * Erzeugt ein Auswahlergebnis. Der Konstruktor ist privat; erzeugt wird über {@link #aus} und {@link #nurMitGeladenen}.
	 *
	 * @param idsAngefordert  Die bereinigten angeforderten IDs.
	 * @param objekte         Die ausgewählten Objekte je ID.
	 * @param ausgelassen     Die ausgelassenen IDs mit ihrem Zustand.
	 * @param idsAusgefiltert Die vom Benutzerfilter ausgeschlossenen IDs.
	 */
	private ReportingAuswahlergebnis(final List<Long> idsAngefordert, final LinkedHashMap<Long, T> objekte,
			final LinkedHashMap<Long, ReportingLadezustand<T>> ausgelassen, final List<Long> idsAusgefiltert) {
		this.idsAngefordert = List.copyOf(idsAngefordert);
		this.objekte = objekte;
		this.ausgelassen = ausgelassen;
		this.idsAusgefiltert = List.copyOf(idsAusgefiltert);
	}


	/**
	 * Erzeugt das Ergebnis einer abgeschlossenen Auswahl.
	 *
	 * @param <T>             Der Reporting-Typ der ausgewählten Hauptdaten.
	 * @param idsAngefordert  Die angeforderten IDs, bereits um Duplikate und leere Einträge bereinigt.
	 * @param objekte         Die ausgewählten Objekte je ID, in Ausgabereihenfolge.
	 * @param ausgelassen     Die ausgelassenen IDs mit dem Zustand, der ihr Fehlen erklärt.
	 * @param idsAusgefiltert Die vom Benutzerfilter ausgeschlossenen IDs.
	 *
	 * @return Das Auswahlergebnis.
	 */
	public static <T> ReportingAuswahlergebnis<T> aus(final List<Long> idsAngefordert, final Map<Long, T> objekte,
			final Map<Long, ReportingLadezustand<T>> ausgelassen, final Collection<Long> idsAusgefiltert) {
		return new ReportingAuswahlergebnis<>(
				Objects.requireNonNull(idsAngefordert, "Eine Auswahl setzt angeforderte IDs voraus."),
				new LinkedHashMap<>(Objects.requireNonNull(objekte, "Eine Auswahl setzt eine - notfalls leere - Menge ausgewählter Objekte voraus.")),
				new LinkedHashMap<>(Objects.requireNonNull(ausgelassen, "Eine Auswahl setzt eine - notfalls leere - Menge ausgelassener IDs voraus.")),
				List.copyOf(Objects.requireNonNull(idsAusgefiltert, "Eine Auswahl setzt eine - notfalls leere - Menge ausgefilterter IDs voraus.")));
	}

	/**
	 * Erzeugt das Ergebnis einer Auswahl gegen bereits geladene Objekte - etwa die Fächer oder Räume eines Stundenplans. Ein Datenzugriff findet nicht statt:
	 * Eine ID, zu der der Auflöser kein Objekt liefert, wird mit der Ursache "nicht vorhanden" ausgelassen. Die übergebenen IDs werden zuvor um Duplikate und
	 * leere Einträge bereinigt.
	 *
	 * @param <T>       Der Reporting-Typ der ausgewählten Hauptdaten.
	 * @param ids       Die angeforderten IDs; die Liste darf null-Einträge und Duplikate enthalten.
	 * @param aufloeser Liefert zur übergebenen ID das bereits geladene Objekt oder {@code null}.
	 *
	 * @return Das Auswahlergebnis; ausgefilterte IDs gibt es bei dieser Auswahl nicht.
	 */
	public static <T> ReportingAuswahlergebnis<T> ausVorhandenen(final List<Long> ids, final LongFunction<T> aufloeser) {
		final List<Long> idsBereinigt = ((ids == null) ? List.<Long>of() : ids).stream().filter(Objects::nonNull).distinct().toList();
		final LinkedHashMap<Long, T> objekte = new LinkedHashMap<>();
		final LinkedHashMap<Long, ReportingLadezustand<T>> ausgelassen = new LinkedHashMap<>();
		for (final Long id : idsBereinigt) {
			final T objekt = aufloeser.apply(id);
			if (objekt != null) {
				objekte.put(id, objekt);
			} else {
				ausgelassen.put(id, ReportingLadezustand.nichtVorhanden());
			}
		}
		return new ReportingAuswahlergebnis<>(idsBereinigt, objekte, ausgelassen, List.of());
	}

	/**
	 * Schränkt die Auswahl auf die Datensätze ein, deren zusätzlich geprüfte Daten geladen werden konnten - etwa "zum Schüler liegen Abiturdaten vor".
	 * Die Ursache der Übrigen wird aus deren Ladezustand übernommen, nicht neu gesetzt: Sonst würde ein technischer Fehler zur fachlich fehlenden Akte.
	 * IDs ohne Eintrag in den Zuständen bleiben in der Auswahl.
	 *
	 * @param <S>       Der Werttyp der geprüften Zusatzdaten.
	 * @param zustaende Der Ladezustand der zusätzlich benötigten Daten je ID.
	 *
	 * @return Das eingeschränkte Auswahlergebnis; dieses Ergebnis bleibt unverändert.
	 */
	public <S> ReportingAuswahlergebnis<T> nurMitGeladenen(final Map<Long, ReportingLadezustand<S>> zustaende) {
		final LinkedHashMap<Long, T> verbleibend = new LinkedHashMap<>();
		final LinkedHashMap<Long, ReportingLadezustand<T>> nunAusgelassen = new LinkedHashMap<>(this.ausgelassen);
		for (final Map.Entry<Long, T> eintrag : this.objekte.entrySet()) {
			final ReportingLadezustand<S> zustand = zustaende.get(eintrag.getKey());
			if ((zustand == null) || zustand.istGeladen()) {
				verbleibend.put(eintrag.getKey(), eintrag.getValue());
			} else {
				nunAusgelassen.put(eintrag.getKey(), ReportingLadezustand.uebernimmUrsache(zustand));
			}
		}
		return new ReportingAuswahlergebnis<>(this.idsAngefordert, verbleibend, nunAusgelassen, this.idsAusgefiltert);
	}


	/**
	 * Gibt die ausgewählten Objekte in Ausgabereihenfolge zurück.
	 *
	 * @return Die unveränderliche Liste der Objekte.
	 */
	public List<T> objekte() {
		return List.copyOf(this.objekte.values());
	}

	/**
	 * Gibt die IDs der ausgewählten Objekte in Ausgabereihenfolge zurück.
	 *
	 * @return Die unveränderliche Liste der IDs.
	 */
	public List<Long> idsAusgewaehlt() {
		return List.copyOf(this.objekte.keySet());
	}

	/**
	 * Gibt die angeforderten IDs zurück, bereinigt um Duplikate und leere Einträge.
	 *
	 * @return Die unveränderliche Liste der angeforderten IDs.
	 */
	public List<Long> idsAngefordert() {
		return this.idsAngefordert;
	}

	/**
	 * Gibt die ausgelassenen IDs mit dem Zustand zurück, der ihr Fehlen erklärt. Aus ihm entsteht die Meldung des Ausgabeproblems.
	 *
	 * @return Die unveränderliche Zuordnung von ID auf Zustand, in der Reihenfolge des Auslassens.
	 */
	public Map<Long, ReportingLadezustand<T>> ausgelassen() {
		return Collections.unmodifiableMap(this.ausgelassen);
	}

	/**
	 * Gibt die IDs zurück, die der Benutzerfilter ausschließt. Sie werden geführt, damit sie nicht als fehlend gemeldet werden - der Anwender hat sie
	 * selbst ausgeschlossen.
	 *
	 * @return Die unveränderliche Liste der ausgefilterten IDs.
	 */
	public List<Long> idsAusgefiltert() {
		return this.idsAusgefiltert;
	}

	/**
	 * Gibt an, ob Datensätze angefordert waren und keiner übrig blieb. Nur dieses Kennzeichen rechtfertigt eine Ausgabe ohne Dokument; aus dem Vorliegen
	 * irgendeines Ausgabeproblems folgt es nicht.
	 *
	 * @return true, wenn Datensätze angefordert waren und keiner übrig blieb, sonst false.
	 */
	public boolean bewusstLeer() {
		return this.objekte.isEmpty() && !this.idsAngefordert.isEmpty();
	}

}
