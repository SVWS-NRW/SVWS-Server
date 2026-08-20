import { Religion } from '../../../asd/types/schule/Religion';
import { ValidatorIka02IntKatalogKonfessionenAsdKatalog } from '../../../asd/validate/intKataloge/ValidatorIka02IntKatalogKonfessionenAsdKatalog';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorIka01IntKatalogKonfessionenAsdKatalog extends Validator {

	/**
	 * Die Katalog-ID der Fachrichtung.
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
		this._validatoren.add(new ValidatorIka02IntKatalogKonfessionenAsdKatalog(idKatalog, kontext));
	}

	protected pruefe(): boolean {
		const idKatalog: number | null = this._idKatalog.get();
		if (Religion.data().getSchluesselByIDOrNull(idKatalog) === null) {
			this.addFehler(0, "Konfession des Schülers: Das Feld 'Konfession ASD-Kürzel' muss zulässig sein.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.intKataloge.ValidatorIka01IntKatalogKonfessionenAsdKatalog';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.intKataloge.ValidatorIka01IntKatalogKonfessionenAsdKatalog', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorIka01IntKatalogKonfessionenAsdKatalog>('de.svws_nrw.asd.validate.intKataloge.ValidatorIka01IntKatalogKonfessionenAsdKatalog');

}

export function cast_de_svws_nrw_asd_validate_intKataloge_ValidatorIka01IntKatalogKonfessionenAsdKatalog(obj: unknown): ValidatorIka01IntKatalogKonfessionenAsdKatalog {
	return obj as ValidatorIka01IntKatalogKonfessionenAsdKatalog;
}
