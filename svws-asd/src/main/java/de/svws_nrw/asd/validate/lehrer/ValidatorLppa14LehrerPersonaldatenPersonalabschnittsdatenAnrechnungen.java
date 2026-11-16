package de.svws_nrw.asd.validate.lehrer;

import java.util.List;
import java.util.function.Supplier;

import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdatenAnrechnungsstunden;
import de.svws_nrw.asd.types.lehrer.LehrerBeschaeftigungsart;
import de.svws_nrw.asd.types.lehrer.LehrerEinsatzstatus;
import de.svws_nrw.asd.types.lehrer.LehrerMehrleistungsarten;
import de.svws_nrw.asd.validate.Validator;
import de.svws_nrw.asd.validate.ValidatorKontext;
import de.svws_nrw.transpiler.annotations.AllowNull;
import jakarta.validation.constraints.NotNull;

/**
 * Prüft, ob bei Teilzeitbeschäftigung im Blockmodell (TS) in der Ansparphase (ID 100)
 * eine Stundenanzahl größer 0 eingetragen wurde.
 */
public final class ValidatorLppa14LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen extends Validator {

	/** Die Liste der Mehrleistungsstunden. */
	private final @NotNull Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>> _mehrleistung;

	/** Die Beschäftigungsart der Lehrkraft. */
	private final @NotNull Supplier<@AllowNull Long> _idBeschaeftigungsart;

	/** Der Einsatzstatus der Lehrkraft. */
	private final @NotNull Supplier<@AllowNull Long> _idEinsatzstatus;

	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param mehrleistung           die Liste der Mehrleistungsstunden
	 * @param idBeschaeftigungsart   die ID der Beschäftigungsart
	 * @param idEinsatzstatus        die ID des Einsatzstatus
	 * @param kontext                der Kontext des Validators
	 */
	public ValidatorLppa14LehrerPersonaldatenPersonalabschnittsdatenAnrechnungen(
			final @NotNull Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>> mehrleistung,
			final @NotNull Supplier<@AllowNull Long> idBeschaeftigungsart,
			final @NotNull Supplier<@AllowNull Long> idEinsatzstatus,
			final @NotNull ValidatorKontext kontext) {
		super(kontext);
		_mehrleistung = mehrleistung;
		_idBeschaeftigungsart = idBeschaeftigungsart;
		_idEinsatzstatus = idEinsatzstatus;
	}

	@Override
	protected boolean pruefe() {
		final List<LehrerPersonalabschnittsdatenAnrechnungsstunden> listeMehrleistung = _mehrleistung.get();
		final Long idBeschaeftigung = _idBeschaeftigungsart.get();
		final Long idEinsatz = _idEinsatzstatus.get();

		if ((listeMehrleistung == null) || (idBeschaeftigung == null)) {
			return true;
		}

		final LehrerBeschaeftigungsart beschaeftigung = LehrerBeschaeftigungsart.data().getWertByIDOrNull(idBeschaeftigung);
		if (beschaeftigung != LehrerBeschaeftigungsart.TS) {
			return true;
		}

		final LehrerEinsatzstatus einsatz = (idEinsatz == null) ? null : LehrerEinsatzstatus.data().getWertByIDOrNull(idEinsatz);
		if ((idEinsatz != null) && (einsatz != LehrerEinsatzstatus.A)) {
			return true;
		}

		final LehrerMehrleistungsarten grund100 = LehrerMehrleistungsarten.data().getWertByBezeichner("ID_100");
		for (final LehrerPersonalabschnittsdatenAnrechnungsstunden eintrag : listeMehrleistung) {
			if (eintrag.idGrund == null) {
				continue;
			}
			final LehrerMehrleistungsarten art = LehrerMehrleistungsarten.data().getWertByIDOrNull(eintrag.idGrund);
			if ((art == grund100) && (Math.abs(eintrag.anzahl) < 0.001)) {
				addFehler(0,
						"Anrechnungsgründe des Lehrers: Den Angaben nach befindet sich die ausgewählte Lehrkraft in der 'Ansparphase' bzw. 'Phase mit erhöhter Arbeitszeit' der 'Teilzeitbeschäftigung im Blockmodell'. Bitte erfassen Sie bei der Mehrleistung 'Ansparphase, Phase mit erhöhter Arbeitszeit 'Teilzeitbeschäftigung im Blockmodell' im Feld 'Stunden' die Differenz aus der tatsächlich geleisteten Arbeitszeit und dem Pflichtstundensoll. Das Pflichtstundensoll ist in jeder Phase der Teilzeitbeschäftigung im Blockmodell gleich.");
				return false;
			}
		}

		return true;
	}

}
