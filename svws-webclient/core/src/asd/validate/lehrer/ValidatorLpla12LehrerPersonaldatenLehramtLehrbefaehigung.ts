import { JavaObject } from '../../../java/lang/JavaObject';
import { LehrerLehramtEintrag } from '../../../asd/data/lehrer/LehrerLehramtEintrag';
import { LehrerLehramt } from '../../../asd/types/lehrer/LehrerLehramt';
import type { Supplier } from '../../../java/util/function/Supplier';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLpla12LehrerPersonaldatenLehramtLehrbefaehigung extends Validator {

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
				if (!JavaObject.equalsTranspiler(LehrerLehramt.ID_30, (zuueberpruefendesLehramt)) && !JavaObject.equalsTranspiler(LehrerLehramt.ID_32, (zuueberpruefendesLehramt)) && !JavaObject.equalsTranspiler(LehrerLehramt.ID_35, (zuueberpruefendesLehramt))) {
					if (lehrerLehramtEintrag.lehrbefaehigungen.isEmpty()) {
						this.addFehler(0, "Das Feld 'Lehrbefähigung' darf nur bei den Lehrämtern 'Berufsbildende Schulen - altes Lehramt -', 'Sekundarstufe II (mit beruflicher Fachrichtung)' und 'Berufskolleg' leer sein.");
						return false;
					}
				}
			}
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLpla12LehrerPersonaldatenLehramtLehrbefaehigung';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLpla12LehrerPersonaldatenLehramtLehrbefaehigung', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLpla12LehrerPersonaldatenLehramtLehrbefaehigung>('de.svws_nrw.asd.validate.lehrer.ValidatorLpla12LehrerPersonaldatenLehramtLehrbefaehigung');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLpla12LehrerPersonaldatenLehramtLehrbefaehigung(obj: unknown): ValidatorLpla12LehrerPersonaldatenLehramtLehrbefaehigung {
	return obj as ValidatorLpla12LehrerPersonaldatenLehramtLehrbefaehigung;
}
