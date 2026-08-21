import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Foerderschwerpunkt } from '../../../asd/types/schule/Foerderschwerpunkt';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorIfa02IntKatalogFoerderschwerpunkteAsdKatalog extends Validator {

	private readonly _idKatalog: Supplier<number>;


	/**
	 * @param idKatalog   ID
	 * @param kontext	  Kontext
	 */
	public constructor(idKatalog: Supplier<number>, kontext: ValidatorKontext) {
		super(kontext);
		this._idKatalog = idKatalog;
	}

	protected pruefe(): boolean {
		if (!Foerderschwerpunkt.data().isGueltig(this._idKatalog.get(), this.kontext().getSchuljahr())) {
			this.addFehler(0, "Foerderschwerpunkt des Schülers: Der eingetragene Wert für das Feld 'Förderschwerpunkt ASD-Kürzel' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.intKataloge.ValidatorIfa02IntKatalogFoerderschwerpunkteAsdKatalog';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.intKataloge.ValidatorIfa02IntKatalogFoerderschwerpunkteAsdKatalog', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorIfa02IntKatalogFoerderschwerpunkteAsdKatalog>('de.svws_nrw.asd.validate.intKataloge.ValidatorIfa02IntKatalogFoerderschwerpunkteAsdKatalog');

}

export function cast_de_svws_nrw_asd_validate_intKataloge_ValidatorIfa02IntKatalogFoerderschwerpunkteAsdKatalog(obj: unknown): ValidatorIfa02IntKatalogFoerderschwerpunkteAsdKatalog {
	return obj as ValidatorIfa02IntKatalogFoerderschwerpunkteAsdKatalog;
}
