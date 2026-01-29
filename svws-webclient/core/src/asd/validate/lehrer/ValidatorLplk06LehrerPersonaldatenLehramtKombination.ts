import { LehrerLehramtEintrag } from '../../../asd/data/lehrer/LehrerLehramtEintrag';
import { LehrerLehramt } from '../../../asd/types/lehrer/LehrerLehramt';
import { LehrerLehramtKatalogEintrag } from '../../../asd/data/lehrer/LehrerLehramtKatalogEintrag';
import type { Supplier } from '../../../java/util/function/Supplier';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLplk06LehrerPersonaldatenLehramtKombination extends Validator {

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
		let lehramtId57Vorhanden: boolean = false;
		let lehramtId60Vorhanden: boolean = false;
		let lehrerLehramtKatalogEintrag57: LehrerLehramtKatalogEintrag | null = LehrerLehramt.ID_57.daten(this.kontext().getSchuljahr());
		let lehrerLehramtKatalogEintrag60: LehrerLehramtKatalogEintrag | null = LehrerLehramt.ID_60.daten(this.kontext().getSchuljahr());
		if (lehrerLehramtKatalogEintrag57 !== null && lehrerLehramtKatalogEintrag60 !== null) {
			for (const lehrerLehramtEintrag of this.lehraemter.get()) {
				if (lehrerLehramtKatalogEintrag57.id === LehrerLehramt.data().getEintragByIDOrException(lehrerLehramtEintrag.idKatalogLehramt).id)
					lehramtId57Vorhanden = true;
				else
					if (lehrerLehramtKatalogEintrag60.id === LehrerLehramt.data().getEintragByIDOrException(lehrerLehramtEintrag.idKatalogLehramt).id)
						lehramtId60Vorhanden = true;
			}
		}
		if (lehramtId57Vorhanden && lehramtId60Vorhanden) {
			this.addFehler(6, "Die Lehramtseinträge 'Sozialarbeiter/-in, Sozialpädagoge/-in, Diplom-Pädagoge/-in (ohne sonderpädagogische Zusatzausbildung)' und 'Sozialarbeiter/-in, Sozialpädagoge/-in, Diplom-Pädagoge/-in (mit sonderpädagogische Zusatzausbildung) ' sollten nicht zusammen vorliegen. Falls der Lehramtseintrag 'Sozialarbeiter/-in, Sozialpädagoge/-in, Diplom-Pädagoge/-in (mit sonderpädagogische Zusatzausbildung) ' korrekt ist, entfernen Sie bitte den Lehramtseintrag 'Sozialarbeiter/-in, Sozialpädagoge/-in, Diplom-Pädagoge/-in (ohne sonderpädagogische Zusatzausbildung)'.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLplk06LehrerPersonaldatenLehramtKombination';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLplk06LehrerPersonaldatenLehramtKombination', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLplk06LehrerPersonaldatenLehramtKombination>('de.svws_nrw.asd.validate.lehrer.ValidatorLplk06LehrerPersonaldatenLehramtKombination');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLplk06LehrerPersonaldatenLehramtKombination(obj: unknown): ValidatorLplk06LehrerPersonaldatenLehramtKombination {
	return obj as ValidatorLplk06LehrerPersonaldatenLehramtKombination;
}
