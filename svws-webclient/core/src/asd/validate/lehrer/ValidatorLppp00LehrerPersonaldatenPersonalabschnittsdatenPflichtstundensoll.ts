import { LehrerBeschaeftigungsart } from '../../../asd/types/lehrer/LehrerBeschaeftigungsart';
import { ValidatorLppp02LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll } from '../../../asd/validate/lehrer/ValidatorLppp02LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll';
import { ValidatorLppp10LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll } from '../../../asd/validate/lehrer/ValidatorLppp10LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { LehrerEinsatzstatus } from '../../../asd/types/lehrer/LehrerEinsatzstatus';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { ValidatorLppp11LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll } from '../../../asd/validate/lehrer/ValidatorLppp11LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLppp00LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll extends Validator {

	/**
	 * Das Pflichtstundensoll
	 */
	private readonly _pflichtstundensoll: Supplier<number | null>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param pflichtstundensoll     das Pflichtstundensoll
	 * @param einsatzstatus        der Einsatzstatus
	 * @param beschaeftigungsart   die Beschäftigungsart
	 * @param kontext                der Kontext des Validators
	 */
	public constructor(pflichtstundensoll: Supplier<number | null>, einsatzstatus: Supplier<LehrerEinsatzstatus | null>, beschaeftigungsart: Supplier<LehrerBeschaeftigungsart | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._pflichtstundensoll = pflichtstundensoll;
		const pflichtstundensollNotNull: Supplier<number> = this.getNotNullSupplierDouble(pflichtstundensoll);
		this._validatoren.add(new ValidatorLppp02LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll(pflichtstundensollNotNull, einsatzstatus, kontext));
		this._validatoren.add(new ValidatorLppp10LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll(pflichtstundensollNotNull, kontext));
		this._validatoren.add(new ValidatorLppp11LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll(pflichtstundensollNotNull, einsatzstatus, beschaeftigungsart, kontext));
	}

	protected pruefe(): boolean {
		const pflichtstundensoll: number | null = this._pflichtstundensoll.get();
		if (pflichtstundensoll === null) {
			this.addFehler(0, "Kein Wert im Feld 'pflichtstundensoll'.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLppp00LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLppp00LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLppp00LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll>('de.svws_nrw.asd.validate.lehrer.ValidatorLppp00LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLppp00LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll(obj: unknown): ValidatorLppp00LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll {
	return obj as ValidatorLppp00LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll;
}
