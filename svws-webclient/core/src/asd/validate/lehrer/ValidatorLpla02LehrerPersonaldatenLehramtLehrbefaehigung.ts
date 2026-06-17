import { LehrerLehramtEintrag } from '../../../asd/data/lehrer/LehrerLehramtEintrag';
import { ValidatorLpla10LehrerPersonaldatenLehramtLehrbefaehigung } from '../../../asd/validate/lehrer/ValidatorLpla10LehrerPersonaldatenLehramtLehrbefaehigung';
import { ValidatorLpla13LehrerPersonaldatenLehramtLehrbefaehigung } from '../../../asd/validate/lehrer/ValidatorLpla13LehrerPersonaldatenLehramtLehrbefaehigung';
import type { Supplier } from '../../../java/util/function/Supplier';
import type { List } from '../../../java/util/List';
import { LehrerLehrbefaehigung } from '../../../asd/types/lehrer/LehrerLehrbefaehigung';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { ValidatorLpla12LehrerPersonaldatenLehramtLehrbefaehigung } from '../../../asd/validate/lehrer/ValidatorLpla12LehrerPersonaldatenLehramtLehrbefaehigung';
import { Validator } from '../../../asd/validate/Validator';
import { ValidatorLpla11LehrerPersonaldatenLehramtLehrbefaehigung } from '../../../asd/validate/lehrer/ValidatorLpla11LehrerPersonaldatenLehramtLehrbefaehigung';

export class ValidatorLpla02LehrerPersonaldatenLehramtLehrbefaehigung extends Validator {

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
		this._validatoren.add(new ValidatorLpla10LehrerPersonaldatenLehramtLehrbefaehigung(lehraemter, kontext));
		this._validatoren.add(new ValidatorLpla11LehrerPersonaldatenLehramtLehrbefaehigung(lehraemter, kontext));
		this._validatoren.add(new ValidatorLpla12LehrerPersonaldatenLehramtLehrbefaehigung(lehraemter, kontext));
		this._validatoren.add(new ValidatorLpla13LehrerPersonaldatenLehramtLehrbefaehigung(lehraemter, kontext));
	}

	protected pruefe(): boolean {
		let fehlerVorhanden: boolean = false;
		const lehrerLehramtEintragList: List<LehrerLehramtEintrag> | null = this.lehraemter.get();
		if (lehrerLehramtEintragList !== null) {
			for (const lehrerLehramtEintrag of lehrerLehramtEintragList) {
				for (const lehrerLehrbefaehigungEintrag of lehrerLehramtEintrag.lehrbefaehigungen) {
					if (!LehrerLehrbefaehigung.data().isGueltig(lehrerLehrbefaehigungEintrag.idLehrbefaehigung, this.kontext().getSchuljahr())) {
						fehlerVorhanden = true;
						this.addFehler(0, "Der eingetragene Wert für das Feld 'Lehrbefähigungen' ist für das ausgewählte Schuljahr nicht gültig. Bitte prüfen.");
					}
				}
				if (fehlerVorhanden) {
					return false;
				}
			}
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLpla02LehrerPersonaldatenLehramtLehrbefaehigung';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLpla02LehrerPersonaldatenLehramtLehrbefaehigung', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLpla02LehrerPersonaldatenLehramtLehrbefaehigung>('de.svws_nrw.asd.validate.lehrer.ValidatorLpla02LehrerPersonaldatenLehramtLehrbefaehigung');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLpla02LehrerPersonaldatenLehramtLehrbefaehigung(obj: unknown): ValidatorLpla02LehrerPersonaldatenLehramtLehrbefaehigung {
	return obj as ValidatorLpla02LehrerPersonaldatenLehramtLehrbefaehigung;
}
