import { LehrerLehramtEintrag } from '../../../asd/data/lehrer/LehrerLehramtEintrag';
import type { Supplier } from '../../../java/util/function/Supplier';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { ValidatorLpla01LehrerPersonaldatenLehramtLehrbefaehigung } from '../../../asd/validate/lehrer/ValidatorLpla01LehrerPersonaldatenLehramtLehrbefaehigung';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLplaLehrerPersonaldatenLehramtLehrbefaehigung extends Validator {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param lehraemter            die Lehrämter, die geprüft werden sollen
	 * @param kontext               der Kontext des Validators
	 */
	public constructor(lehraemter: Supplier<List<LehrerLehramtEintrag>>, kontext: ValidatorKontext) {
		super(kontext);
		this._validatoren.add(new ValidatorLpla01LehrerPersonaldatenLehramtLehrbefaehigung(lehraemter, kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLplaLehrerPersonaldatenLehramtLehrbefaehigung';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLplaLehrerPersonaldatenLehramtLehrbefaehigung', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLplaLehrerPersonaldatenLehramtLehrbefaehigung>('de.svws_nrw.asd.validate.lehrer.ValidatorLplaLehrerPersonaldatenLehramtLehrbefaehigung');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLplaLehrerPersonaldatenLehramtLehrbefaehigung(obj: unknown): ValidatorLplaLehrerPersonaldatenLehramtLehrbefaehigung {
	return obj as ValidatorLplaLehrerPersonaldatenLehramtLehrbefaehigung;
}
