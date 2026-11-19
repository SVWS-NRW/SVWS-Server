import { ValidatorSbe01SchuelerSchulbesuchsdatenEinschulungsart } from '../../../asd/validate/schueler/ValidatorSbe01SchuelerSchulbesuchsdatenEinschulungsart';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorSbe00SchuelerSchulbesuchsdatenEinschulungsart extends Validator {

	/**
	 * Die ID der Einschulungsart an der Grundschule
	 */
	private readonly _idEinschulungsartGrundschule: Supplier<number | null>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem Kontext.
	 *
	 * @param idEinschulungsartGrundschule   die ID der Einschulungsart an der Grundschule
	 * @param kontext                        der Kontext des Validators
	 */
	public constructor(idEinschulungsartGrundschule: Supplier<number | null>, kontext: ValidatorKontext) {
		super(kontext);
		this._idEinschulungsartGrundschule = idEinschulungsartGrundschule;
		this._validatoren.add(new ValidatorSbe01SchuelerSchulbesuchsdatenEinschulungsart(idEinschulungsartGrundschule, kontext));
	}

	protected pruefe(): boolean {
		const idEinschulungsartGrundschule: number | null = this._idEinschulungsartGrundschule.get();
		if ((idEinschulungsartGrundschule === null) || (idEinschulungsartGrundschule < 0)) {
			this.addFehler(0, "Einschulungsart des Schülers: Kein Wert vorhanden.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.schueler.ValidatorSbe00SchuelerSchulbesuchsdatenEinschulungsart';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.schueler.ValidatorSbe00SchuelerSchulbesuchsdatenEinschulungsart', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorSbe00SchuelerSchulbesuchsdatenEinschulungsart>('de.svws_nrw.asd.validate.schueler.ValidatorSbe00SchuelerSchulbesuchsdatenEinschulungsart');

}

export function cast_de_svws_nrw_asd_validate_schueler_ValidatorSbe00SchuelerSchulbesuchsdatenEinschulungsart(obj: unknown): ValidatorSbe00SchuelerSchulbesuchsdatenEinschulungsart {
	return obj as ValidatorSbe00SchuelerSchulbesuchsdatenEinschulungsart;
}
