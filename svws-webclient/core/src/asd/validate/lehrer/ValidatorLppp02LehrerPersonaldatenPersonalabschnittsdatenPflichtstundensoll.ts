import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { LehrerEinsatzstatus } from '../../../asd/types/lehrer/LehrerEinsatzstatus';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLppp02LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll extends Validator {

	/**
	 * Das Pflichtstundensoll
	 */
	private readonly _pflichtstundensoll: Supplier<number | null>;

	/**
	 * Der Einsatzstatus
	 */
	private readonly _idEinsatzstatus: Supplier<number | null>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param pflichtstundensoll    das Pflichtstundensoll
	 * @param idEinsatzstatus       der Einsatzstatus
	 * @param kontext               der Kontext des Validators
	 */
	public constructor(pflichtstundensoll: Supplier<number | null>, idEinsatzstatus: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._pflichtstundensoll = pflichtstundensoll;
		this._idEinsatzstatus = idEinsatzstatus;
	}

	protected pruefe(): boolean {
		const pflichtstundensoll: number | null = this._pflichtstundensoll.get();
		const idEinsatzstatus: number | null = this._idEinsatzstatus.get();
		const einsatzstatus: LehrerEinsatzstatus | null = (idEinsatzstatus === null) ? null : LehrerEinsatzstatus.data().getWertByID(idEinsatzstatus);
		if ((einsatzstatus as unknown === LehrerEinsatzstatus.B as unknown) && (pflichtstundensoll === 0.0)) {
			this.addFehler(2, "Bei Lehrkräften, die von einer anderen Schule abgeordnet wurden (Einsatzstatus = 'B'), darf das Pflichtstundensoll nicht 0,00 betragen.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLppp02LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLppp02LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLppp02LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll>('de.svws_nrw.asd.validate.lehrer.ValidatorLppp02LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLppp02LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll(obj: unknown): ValidatorLppp02LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll {
	return obj as ValidatorLppp02LehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll;
}
