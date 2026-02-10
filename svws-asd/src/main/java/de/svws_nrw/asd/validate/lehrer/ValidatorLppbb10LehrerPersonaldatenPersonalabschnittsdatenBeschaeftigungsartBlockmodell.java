package de.svws_nrw.asd.validate.lehrer;

import java.util.List;
import java.util.function.Supplier;

import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdatenAnrechnungsstunden;
import de.svws_nrw.asd.types.lehrer.LehrerBeschaeftigungsart;
import de.svws_nrw.asd.types.lehrer.LehrerEinsatzstatus;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Validator für Beschäftigungsart = "TS" (Teilzeit im Blockmodell).
 * <p>
 * Prüft: Bei Pflichtstundensoll &gt; 0 und Einsatzstatus = " " (Leerzeichen) oder "A"
 * muss <b>entweder</b> der Mehrleistungsgrund 100 <b>oder</b> einer der
 * Minderleistungsgründe 240/290 gesetzt sein.
 * </p>
 *
 * <p><b>Fehlerhärte:</b> Default</p>
 * <p><b>Fehlertext:</b> Bei BA="TS" muss Grund 100 (Mehrleistung) oder 240/290 (Minderleistung) gesetzt sein.</p>
 */
public final class ValidatorLppbb10LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsartBlockmodell extends Validator {

	/** Die Lehrer-Personalabschnittsdaten, die geprüft werden. */
	private final @NotNull Supplier<@AllowNull Double> pflichtstundensoll;
	private final @NotNull Supplier<@AllowNull String> beschaeftigungsart;
	private final @NotNull Supplier<@AllowNull String> einsatzstatus;
	private final @NotNull Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>> mehrleistungen;
	private final @NotNull Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>> minderleistungen;

	/**
	 * Erstellt einen neuen Validator.
	 *
	 * @param pflichtstundensoll   der Pflichtstundensoll
	 * @param beschaeftigungsart   die Beschäftigungsart
	 * @param einsatzstatus        der Einsatz-Status
	 * @param mehrleistungen       die Liste mit den Einträgen zu Mehrleistungen
	 * @param minderleistungen     die Liste mit den Einträgen zu Minderleistungen
	 *
	 * @param kontext  der Kontext der Validierung
	 */
	public ValidatorLppbb10LehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsartBlockmodell(
			final @NotNull Supplier<@AllowNull Double> pflichtstundensoll,
			final @NotNull Supplier<@AllowNull String> beschaeftigungsart,
			final @NotNull Supplier<@AllowNull String> einsatzstatus,
			final @NotNull Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>> mehrleistungen,
			final @NotNull Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>> minderleistungen,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		this.pflichtstundensoll = pflichtstundensoll;
		this.beschaeftigungsart = beschaeftigungsart;
		this.einsatzstatus = einsatzstatus;
		this.mehrleistungen = mehrleistungen;
		this.minderleistungen = minderleistungen;
	}



	/**
	 * Prüft, ob eine Liste von Anrechnungsstunden einen bestimmten Grund enthält.
	 *
	 * @param liste    die zu prüfende Liste (kann {@code null} sein)
	 * @param idGrund  die gesuchte Grund-ID
	 *
	 * @return {@code true}, wenn ein Eintrag mit {@code idGrund} enthalten ist; sonst {@code false}
	 */
	private static boolean hatGrund(final List<LehrerPersonalabschnittsdatenAnrechnungsstunden> liste, final long idGrund) {
		if (liste == null || liste.isEmpty())
			return false;
		for (final LehrerPersonalabschnittsdatenAnrechnungsstunden lpa : liste)
			if (lpa != null && lpa.idGrund == idGrund)
				return true;
		return false;
	}

	/**
	 * Prüft die Regel für Teilzeit im Blockmodell (Beschäftigungsart = "TS"):
	 * <p>
	 * Gilt nur, wenn das Pflichtstundensoll > 0 ist und der Einsatzstatus
	 * " " (Leerzeichen) oder "A" lautet. Die Prüfung ist erfüllt, wenn
	 * mindestens einer der folgenden Gründe gesetzt ist:
	 * <ul>
	 *   <li>Mehrleistung: 100</li>
	 *   <li>Minderleistung: 240 oder 290</li>
	 * </ul>
	 *
	 * @return {@code true}, wenn die Regel erfüllt oder nicht anwendbar ist, sonst {@code false}
	 */
	@Override
	protected boolean pruefe() {
		final Double pss = pflichtstundensoll.get();
		if (pss == null || pss <= 0.0)
			return true;

		String ba = beschaeftigungsart.get();
		if (ba == null)
			ba = "";
		ba = ba.trim();
		if (LehrerBeschaeftigungsart.data().getWertBySchluessel(ba) != LehrerBeschaeftigungsart.TS)
			return true;

		String es = einsatzstatus.get();
		if (es == null)
			es = "";
		es = es.trim();
		if (!"".equals(es.trim()) && LehrerEinsatzstatus.data().getWertBySchluessel(es) != LehrerEinsatzstatus.A)
			return true;

		final boolean hatMehr100 = hatGrund(mehrleistungen.get(), 100L);
		final boolean hatMinder240 = hatGrund(minderleistungen.get(), 240L);
		final boolean hatMinder290 = hatGrund(minderleistungen.get(), 290L);

		final boolean hatMehrMinderGrund = hatMehr100 || hatMinder240 || hatMinder290;

		final String fehlertext =
				"\"Bei einer Lehrkraft mit 'Beschäftigungsart' = TS (Teilzeitbeschäftigung im Blockmodell) muss entweder der Mehrleistungsgrund '100' Ansparphase, Phase mit erhöhter Arbeitszeit \"Teilzeitbeschäftigung im Blockmodell\" (§ 65 LBG) (vormals Sabbatjahr) oder der Minderleistungsgrund '290' (Ermäßigungs-/Freistellungsphase 'Teilzeitbeschäftigung im Blockmodell') eingetragen sein.\"))";

		if (!hatMehrMinderGrund) {
			this.addFehler(1, fehlertext);
			return false;
		}

		return true;
	}
}
