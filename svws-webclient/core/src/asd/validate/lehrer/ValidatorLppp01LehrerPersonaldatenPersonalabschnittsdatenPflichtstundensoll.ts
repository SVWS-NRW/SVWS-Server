import { LehrerPersonalabschnittsdaten } from '../../../asd/data/lehrer/LehrerPersonalabschnittsdaten';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLppp01LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll extends Validator {

	/**
	 * Die Lehrer-Personalabschnittsdaten
	 */
	private readonly daten: LehrerPersonalabschnittsdaten;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param daten     die Daten des Validators
	 * @param kontext   der Kontext des Validators
	 */
	public constructor(daten: LehrerPersonalabschnittsdaten, kontext: ValidatorKontext) {
		super(kontext);
		this.daten = daten;
	}

	protected pruefe(): boolean {
		const pflichtstundensoll: number | null = this.daten.pflichtstundensoll;
		if (pflichtstundensoll !== null && (pflichtstundensoll < 0.0 || pflichtstundensoll > 41.0)) {
			this.addFehler(1, "Unzulässiger Wert im Feld 'pflichtstundensoll'. Zulässig sind im Stundenmodell Werte im Bereich von 0,00 bis 41,00 Wochenstunden. Im Minutenmodell zwischen 0,00 und 1845,00 Minuten.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLppp01LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLppp01LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLppp01LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll>('de.svws_nrw.asd.validate.lehrer.ValidatorLppp01LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLppp01LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll(obj: unknown): ValidatorLppp01LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll {
	return obj as ValidatorLppp01LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll;
}
