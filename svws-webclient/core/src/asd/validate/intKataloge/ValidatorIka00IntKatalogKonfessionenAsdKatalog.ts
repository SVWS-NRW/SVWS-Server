import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { ValidatorIka01IntKatalogKonfessionenAsdKatalog } from '../../../asd/validate/intKataloge/ValidatorIka01IntKatalogKonfessionenAsdKatalog';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorIka00IntKatalogKonfessionenAsdKatalog extends Validator {

	/**
	 * Die Katalog-ID.
	 */
	private readonly _idKatalog: Supplier<number>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext.
	 *
	 * @param idKatalog   die Katalog-ID
	 * @param kontext     der Kontext des Validators
	 */
	public constructor(idKatalog: Supplier<number>, kontext: ValidatorKontext) {
		super(kontext);
		this._idKatalog = idKatalog;
		this._validatoren.add(new ValidatorIka01IntKatalogKonfessionenAsdKatalog(idKatalog, kontext));
	}

	protected pruefe(): boolean {
		const idKatalog: number | null = this._idKatalog.get();
		if (idKatalog === null) {
			this.addFehler(0, "Konfession des Schülers: Kein Wert vorhanden.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.intKataloge.ValidatorIka00IntKatalogKonfessionenAsdKatalog';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.intKataloge.ValidatorIka00IntKatalogKonfessionenAsdKatalog', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorIka00IntKatalogKonfessionenAsdKatalog>('de.svws_nrw.asd.validate.intKataloge.ValidatorIka00IntKatalogKonfessionenAsdKatalog');

}

export function cast_de_svws_nrw_asd_validate_intKataloge_ValidatorIka00IntKatalogKonfessionenAsdKatalog(obj: unknown): ValidatorIka00IntKatalogKonfessionenAsdKatalog {
	return obj as ValidatorIka00IntKatalogKonfessionenAsdKatalog;
}
