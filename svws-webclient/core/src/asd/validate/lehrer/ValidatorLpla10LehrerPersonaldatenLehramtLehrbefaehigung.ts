import { JavaObject } from '../../../java/lang/JavaObject';
import { LehrerLehramtEintrag } from '../../../asd/data/lehrer/LehrerLehramtEintrag';
import { LehrerLehramt } from '../../../asd/types/lehrer/LehrerLehramt';
import type { Supplier } from '../../../java/util/function/Supplier';
import type { List } from '../../../java/util/List';
import { LehrerLehrbefaehigung } from '../../../asd/types/lehrer/LehrerLehrbefaehigung';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLpla10LehrerPersonaldatenLehramtLehrbefaehigung extends Validator {

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
	}

	protected pruefe(): boolean {
		let lehrerLehramtEintragList: List<LehrerLehramtEintrag> | null = this.lehraemter.get();
		if (lehrerLehramtEintragList !== null) {
			for (const lehrerLehramtEintrag of lehrerLehramtEintragList) {
				let lehrerLehramt: LehrerLehramt | null = LehrerLehramt.data().getWertByID(lehrerLehramtEintrag.idKatalogLehramt);
				if (JavaObject.equalsTranspiler(LehrerLehramt.ID_70, (lehrerLehramt))) {
					for (const lehrerLehrbefaehigungEintrag of lehrerLehramtEintrag.lehrbefaehigungen) {
						if (!JavaObject.equalsTranspiler(LehrerLehrbefaehigung.OA, (LehrerLehrbefaehigung.data().getWertByID(lehrerLehrbefaehigungEintrag.idLehrbefaehigung)))) {
							this.addFehler(0, "Für das Lehramt 'Schulverwaltungsassistent/-in' ist nur die Lehrbefähigung 'ohne Angabe' zulässig.");
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLpla10LehrerPersonaldatenLehramtLehrbefaehigung';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.lehrer.ValidatorLpla10LehrerPersonaldatenLehramtLehrbefaehigung', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLpla10LehrerPersonaldatenLehramtLehrbefaehigung>('de.svws_nrw.asd.validate.lehrer.ValidatorLpla10LehrerPersonaldatenLehramtLehrbefaehigung');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLpla10LehrerPersonaldatenLehramtLehrbefaehigung(obj: unknown): ValidatorLpla10LehrerPersonaldatenLehramtLehrbefaehigung {
	return obj as ValidatorLpla10LehrerPersonaldatenLehramtLehrbefaehigung;
}
