import { JavaObject } from '../../../java/lang/JavaObject';
import { DateManager } from '../../../asd/validate/DateManager';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { LehrerRechtsverhaeltnis } from '../../../asd/types/lehrer/LehrerRechtsverhaeltnis';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLppr13LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis extends Validator {

	/**
	 * Das Geburtsdatum des Lehrers
	 */
	private readonly _geburtsdatum: Supplier<DateManager>;

	/**
	 * Das aktuelle Schuljahr des Lehrers
	 */
	private readonly _schuljahr: Supplier<number>;

	/**
	 * Das Rechtsverhältnis
	 */
	private readonly _rechtsverhaeltnis: Supplier<LehrerRechtsverhaeltnis>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param schuljahr                  Schuljahr
	 * @param rechtsverhaeltnisNotNull   das Rechtsverhältnis
	 * @param geburtsdatum               das Geburtsdatum des Lehrers
	 * @param kontext                    der Kontext des Validators
	 */
	public constructor(schuljahr: Supplier<number>, rechtsverhaeltnisNotNull: Supplier<LehrerRechtsverhaeltnis>, geburtsdatum: Supplier<DateManager>, kontext: ValidatorKontext) {
		super(kontext);
		this._schuljahr = schuljahr;
		this._rechtsverhaeltnis = rechtsverhaeltnisNotNull;
		this._geburtsdatum = geburtsdatum;
	}

	protected pruefe(): boolean {
		const schuljahr: number = this._schuljahr.get().valueOf();
		const rv: LehrerRechtsverhaeltnis | null = this._rechtsverhaeltnis.get();
		if (!JavaObject.equalsTranspiler(rv, (LehrerRechtsverhaeltnis.L)) && !JavaObject.equalsTranspiler(rv, (LehrerRechtsverhaeltnis.P)) && !JavaObject.equalsTranspiler(rv, (LehrerRechtsverhaeltnis.W))) {
			const minJahr: number = schuljahr - 80;
			const maxJahr: number = schuljahr - 18;
			if (!this._geburtsdatum.get().istInJahren(minJahr, maxJahr)) {
				this.addFehler(4, "Der Wert für das Geburtsjahr sollte bei sonstigen Rechtsverhältnissen zwischen " + minJahr + " und " + maxJahr + " liegen. Bitte prüfen!");
			}
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLppr13LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLppr13LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLppr13LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis>('de.svws_nrw.asd.validate.lehrer.ValidatorLppr13LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLppr13LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(obj: unknown): ValidatorLppr13LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis {
	return obj as ValidatorLppr13LehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis;
}
