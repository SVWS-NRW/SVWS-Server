import { ValidatorSbe00SchuelerSchulbesuchsdatenEinschulungsart } from '../../../asd/validate/schueler/ValidatorSbe00SchuelerSchulbesuchsdatenEinschulungsart';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSbeSchuelerSchulbesuchsdatenEinschulungsart extends Validator {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext.
	 *
	 * @param idEinschulungsartGrundschule   die ID der Einschulungsart an der Grundschule
	 * @param kontext                        der Kontext des Validators
	 */
	public constructor(idEinschulungsartGrundschule: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._validatoren.add(new ValidatorSbe00SchuelerSchulbesuchsdatenEinschulungsart(idEinschulungsartGrundschule, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSbeSchuelerSchulbesuchsdatenEinschulungsart';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSbeSchuelerSchulbesuchsdatenEinschulungsart', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSbeSchuelerSchulbesuchsdatenEinschulungsart>('de.svws_nrw.asd.validate.schueler.ValidatorSbeSchuelerSchulbesuchsdatenEinschulungsart');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSbeSchuelerSchulbesuchsdatenEinschulungsart(obj: unknown): ValidatorSbeSchuelerSchulbesuchsdatenEinschulungsart {
	return obj as ValidatorSbeSchuelerSchulbesuchsdatenEinschulungsart;
}
