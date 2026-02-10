import { LehrerLehramtEintrag } from '../../../asd/data/lehrer/LehrerLehramtEintrag';
import { LehrerLehramt } from '../../../asd/types/lehrer/LehrerLehramt';
import { LehrerLehramtKatalogEintrag } from '../../../asd/data/lehrer/LehrerLehramtKatalogEintrag';
import type { Supplier } from '../../../java/util/function/Supplier';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLplk14LehrerPersonaldatenLehramtKombination extends Validator {

	/**
	 * Die Lehrämter
	 */
	private readonly lehraemter: Supplier<List<LehrerLehramtEintrag>>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param lehraemter   			die Lehrämter, die geprüft werden sollen
	 * @param kontext               der Kontext des Validators
	 */
	public constructor(lehraemter: Supplier<List<LehrerLehramtEintrag>>, kontext: ValidatorKontext) {
		super(kontext);
		this.lehraemter = lehraemter;
	}

	protected pruefe(): boolean {
		let lehramtId58Vorhanden: boolean = false;
		let lehramtId61Vorhanden: boolean = false;
		const lehrerLehramtKatalogEintrag58: LehrerLehramtKatalogEintrag | null = LehrerLehramt.ID_58.daten(this.kontext().getSchuljahr());
		const lehrerLehramtKatalogEintrag61: LehrerLehramtKatalogEintrag | null = LehrerLehramt.ID_61.daten(this.kontext().getSchuljahr());
		if (lehrerLehramtKatalogEintrag58 !== null && lehrerLehramtKatalogEintrag61 !== null)
			for (const lehrerLehramtEintrag of this.lehraemter.get())
				if (lehrerLehramtKatalogEintrag58.id === LehrerLehramt.data().getEintragByIDOrException(lehrerLehramtEintrag.idKatalogLehramt).id)
					lehramtId58Vorhanden = true;
				else
					if (lehrerLehramtKatalogEintrag61.id === LehrerLehramt.data().getEintragByIDOrException(lehrerLehramtEintrag.idKatalogLehramt).id)
						lehramtId61Vorhanden = true;
		if (lehramtId58Vorhanden && lehramtId61Vorhanden) {
			this.addFehler(4, "Die Lehramtseinträge 'Erzieher/-in (ohne sonderpädagogische Zusatzausbildung)' und 'Erzieher/-in (mit sonderpädagogischer Zusatzausbildung)' sollten nicht zusammen vorliegen. Falls der Lehramtseintrag 'Erzieher/-in mit sonderpädagogischer Zusatzausbildung' korrekt ist, entfernen Sie bitte den Lehramtseintrag 'Erzieher/-in ohne sonderpädagogische Zusatzausbildung'.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLplk14LehrerPersonaldatenLehramtKombination';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLplk14LehrerPersonaldatenLehramtKombination', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLplk14LehrerPersonaldatenLehramtKombination>('de.svws_nrw.asd.validate.lehrer.ValidatorLplk14LehrerPersonaldatenLehramtKombination');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLplk14LehrerPersonaldatenLehramtKombination(obj: unknown): ValidatorLplk14LehrerPersonaldatenLehramtKombination {
	return obj as ValidatorLplk14LehrerPersonaldatenLehramtKombination;
}
