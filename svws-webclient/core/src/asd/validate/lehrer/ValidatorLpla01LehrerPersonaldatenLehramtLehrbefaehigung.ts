import { LehrerLehramtEintrag } from '../../../asd/data/lehrer/LehrerLehramtEintrag';
import type { Supplier } from '../../../java/util/function/Supplier';
import type { List } from '../../../java/util/List';
import { LehrerLehrbefaehigung } from '../../../asd/types/lehrer/LehrerLehrbefaehigung';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { ValidatorLpla02LehrerPersonaldatenLehramtLehrbefaehigung } from '../../../asd/validate/lehrer/ValidatorLpla02LehrerPersonaldatenLehramtLehrbefaehigung';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLpla01LehrerPersonaldatenLehramtLehrbefaehigung extends Validator {

	/**
	 * Die Liste der Lehrämter.
	 */
	private readonly lehraemter: Supplier<List<LehrerLehramtEintrag> | null>;


	/**
	 * Erstellt einen neuen Validator zur Überprüfung der Lehrbefähigungseinträge.
	 *
	 * @param lehraemter         die Liste der Lehrämter
	 * @param kontext            der Kontext des Validators
	 */
	public constructor(lehraemter: Supplier<List<LehrerLehramtEintrag> | null>, kontext: ValidatorKontext) {
		super(kontext);
		this.lehraemter = lehraemter;
		this._validatoren.add(new ValidatorLpla02LehrerPersonaldatenLehramtLehrbefaehigung(lehraemter, kontext));
	}

	protected pruefe(): boolean {
		const liste: List<LehrerLehramtEintrag> | null = this.lehraemter.get();
		if (liste === null) {
			this.addFehler(0, "Das Feld 'Lehrbefaehigung' muss zulässig sein. ");
			return false;
		}
		for (const lehrerLehramtEintrag of liste) {
			for (const lehrerLehrbefaehigungEintrag of lehrerLehramtEintrag.lehrbefaehigungen) {
				if (LehrerLehrbefaehigung.data().getWertByIDOrNull(lehrerLehrbefaehigungEintrag.idLehrbefaehigung) === null) {
					this.addFehler(0, "Das Feld 'Lehrbefaehigung' muss zulässig sein. ");
					return false;
				}
			}
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLpla01LehrerPersonaldatenLehramtLehrbefaehigung';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.lehrer.ValidatorLpla01LehrerPersonaldatenLehramtLehrbefaehigung', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLpla01LehrerPersonaldatenLehramtLehrbefaehigung>('de.svws_nrw.asd.validate.lehrer.ValidatorLpla01LehrerPersonaldatenLehramtLehrbefaehigung');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLpla01LehrerPersonaldatenLehramtLehrbefaehigung(obj: unknown): ValidatorLpla01LehrerPersonaldatenLehramtLehrbefaehigung {
	return obj as ValidatorLpla01LehrerPersonaldatenLehramtLehrbefaehigung;
}
