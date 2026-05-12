import { LehrerLehramtEintrag } from '../../../asd/data/lehrer/LehrerLehramtEintrag';
import type { JavaSet } from '../../../java/util/JavaSet';
import { java_util_Set_of } from '../../../java/util/JavaSet';
import { LehrerLehramt } from '../../../asd/types/lehrer/LehrerLehramt';
import type { Supplier } from '../../../java/util/function/Supplier';
import type { List } from '../../../java/util/List';
import { LehrerLehrbefaehigung } from '../../../asd/types/lehrer/LehrerLehrbefaehigung';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLpla13LehrerPersonaldatenLehramtLehrbefaehigung extends Validator {

	/**
	 * Die Liste der Lehrämter.
	 */
	private readonly lehraemter: Supplier<List<LehrerLehramtEintrag> | null>;

	private static readonly zulaessigeLehraemter: JavaSet<LehrerLehramt> = java_util_Set_of(LehrerLehramt.ID_04, LehrerLehramt.ID_08, LehrerLehramt.ID_90, LehrerLehramt.ID_98);

	private static readonly zuPruefendeLehrbefaehigungen: JavaSet<LehrerLehrbefaehigung> = java_util_Set_of(LehrerLehrbefaehigung.AE, LehrerLehrbefaehigung.MG, LehrerLehrbefaehigung.NG, LehrerLehrbefaehigung.SB);


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
				if (!ValidatorLpla13LehrerPersonaldatenLehramtLehrbefaehigung.zulaessigeLehraemter.contains(zuueberpruefendesLehramt)) {
					for (const lehrerLehrbefaehigungEintrag of lehrerLehramtEintrag.lehrbefaehigungen) {
						if (ValidatorLpla13LehrerPersonaldatenLehramtLehrbefaehigung.zuPruefendeLehrbefaehigungen.contains((LehrerLehrbefaehigung.data().getWertByIDOrNull(lehrerLehrbefaehigungEintrag.idLehrbefaehigung)))) {
							this.addFehler(0, "Bei den Lehrbefähigungen 'AE - Ästhetische Erziehung', 'MG - Mathematische Grundbildung', 'NG - Natur- und Gesellschaftswissenschaften' und 'SB - Sprachliche Grundbildung' muss das Lehramt 'Grundschule' oder 'Sonderpädagogische Förderung' bzw. die Lehramtseinträge 'Studierende' oder 'Lehramtsanwärter/-in / Studienreferendar/-in' angegeben werden.");
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLpla13LehrerPersonaldatenLehramtLehrbefaehigung';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.lehrer.ValidatorLpla13LehrerPersonaldatenLehramtLehrbefaehigung', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLpla13LehrerPersonaldatenLehramtLehrbefaehigung>('de.svws_nrw.asd.validate.lehrer.ValidatorLpla13LehrerPersonaldatenLehramtLehrbefaehigung');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLpla13LehrerPersonaldatenLehramtLehrbefaehigung(obj: unknown): ValidatorLpla13LehrerPersonaldatenLehramtLehrbefaehigung {
	return obj as ValidatorLpla13LehrerPersonaldatenLehramtLehrbefaehigung;
}
