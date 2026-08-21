import { ValidatorIfa02IntKatalogFoerderschwerpunkteAsdKatalog } from '../../../asd/validate/intKataloge/ValidatorIfa02IntKatalogFoerderschwerpunkteAsdKatalog';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Foerderschwerpunkt } from '../../../asd/types/schule/Foerderschwerpunkt';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorIfa01IntKatalogFoerderschwerpunkteAsdKatalog extends Validator {

	private readonly _idKatalog: Supplier<number>;


	/**
	 * @param idKatalog	IdKatalog
	 * @param kontext	Kontext
	 */
	public constructor(idKatalog: Supplier<number>, kontext: ValidatorKontext) {
		super(kontext);
		this._idKatalog = idKatalog;
		this._validatoren.add(new ValidatorIfa02IntKatalogFoerderschwerpunkteAsdKatalog(idKatalog, kontext));
	}

	protected pruefe(): boolean {
		const foerderschwerpunkt: Foerderschwerpunkt | null = Foerderschwerpunkt.data().getWertByIDOrNull(this._idKatalog.get());
		if (foerderschwerpunkt === null) {
			this.addFehler(0, "Foerderschwerpunkt des Schülers: Das Feld 'Förderschwerpunkt ASD-Kürzel' muss zulässig sein.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.intKataloge.ValidatorIfa01IntKatalogFoerderschwerpunkteAsdKatalog';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.intKataloge.ValidatorIfa01IntKatalogFoerderschwerpunkteAsdKatalog', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorIfa01IntKatalogFoerderschwerpunkteAsdKatalog>('de.svws_nrw.asd.validate.intKataloge.ValidatorIfa01IntKatalogFoerderschwerpunkteAsdKatalog');

}

export function cast_de_svws_nrw_asd_validate_intKataloge_ValidatorIfa01IntKatalogFoerderschwerpunkteAsdKatalog(obj: unknown): ValidatorIfa01IntKatalogFoerderschwerpunkteAsdKatalog {
	return obj as ValidatorIfa01IntKatalogFoerderschwerpunkteAsdKatalog;
}
