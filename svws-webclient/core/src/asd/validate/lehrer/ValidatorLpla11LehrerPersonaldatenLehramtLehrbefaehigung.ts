import { JavaObject } from '../../../java/lang/JavaObject';
import { LehrerLehramtEintrag } from '../../../asd/data/lehrer/LehrerLehramtEintrag';
import { LehrerLehramt } from '../../../asd/types/lehrer/LehrerLehramt';
import type { Supplier } from '../../../java/util/function/Supplier';
import type { List } from '../../../java/util/List';
import { LehrerLehrbefaehigung } from '../../../asd/types/lehrer/LehrerLehrbefaehigung';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLpla11LehrerPersonaldatenLehramtLehrbefaehigung extends Validator {

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
		const liste: List<LehrerLehramtEintrag> | null = this.lehraemter.get();
		if (liste !== null) {
			for (const lehrerLehramtEintrag of liste) {
				const zuueberpruefendesLehramt: LehrerLehramt | null = LehrerLehramt.data().getWertByIDOrNull(lehrerLehramtEintrag.idKatalogLehramt);
				if (JavaObject.equalsTranspiler(LehrerLehramt.ID_63, (zuueberpruefendesLehramt)) || JavaObject.equalsTranspiler(LehrerLehramt.ID_64, (zuueberpruefendesLehramt)) || JavaObject.equalsTranspiler(LehrerLehramt.ID_65, (zuueberpruefendesLehramt))) {
					for (const lehrerLehrbefaehigungEintrag of lehrerLehramtEintrag.lehrbefaehigungen) {
						let zuueberprufendeLehrbefaehigunhg: LehrerLehrbefaehigung | null = LehrerLehrbefaehigung.data().getWertByID(lehrerLehrbefaehigungEintrag.idLehrbefaehigung);
						if (!JavaObject.equalsTranspiler(LehrerLehrbefaehigung.BE, (zuueberprufendeLehrbefaehigunhg))) {
							this.addFehler(0, "Für die Lehrämter 'Alltagshelfer/-in', 'Handwerksmeister/-in' und 'Heilpädagoge/-in' ist nur die Lehrbefähigung 'Betreuung' zulässig.");
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLpla11LehrerPersonaldatenLehramtLehrbefaehigung';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLpla11LehrerPersonaldatenLehramtLehrbefaehigung', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLpla11LehrerPersonaldatenLehramtLehrbefaehigung>('de.svws_nrw.asd.validate.lehrer.ValidatorLpla11LehrerPersonaldatenLehramtLehrbefaehigung');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLpla11LehrerPersonaldatenLehramtLehrbefaehigung(obj: unknown): ValidatorLpla11LehrerPersonaldatenLehramtLehrbefaehigung {
	return obj as ValidatorLpla11LehrerPersonaldatenLehramtLehrbefaehigung;
}
