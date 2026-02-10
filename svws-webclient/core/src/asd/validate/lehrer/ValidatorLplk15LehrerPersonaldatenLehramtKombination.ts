import { LehrerLehramtEintrag } from '../../../asd/data/lehrer/LehrerLehramtEintrag';
import { LehrerLehramt } from '../../../asd/types/lehrer/LehrerLehramt';
import { LehrerLehramtKatalogEintrag } from '../../../asd/data/lehrer/LehrerLehramtKatalogEintrag';
import type { Supplier } from '../../../java/util/function/Supplier';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLplk15LehrerPersonaldatenLehramtKombination extends Validator {

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
		let lehramtId59Vorhanden: boolean = false;
		let lehramtId62Vorhanden: boolean = false;
		const lehrerLehramtKatalogEintrag59: LehrerLehramtKatalogEintrag | null = LehrerLehramt.ID_59.daten(this.kontext().getSchuljahr());
		const lehrerLehramtKatalogEintrag62: LehrerLehramtKatalogEintrag | null = LehrerLehramt.ID_62.daten(this.kontext().getSchuljahr());
		if (lehrerLehramtKatalogEintrag59 !== null && lehrerLehramtKatalogEintrag62 !== null)
			for (const lehrerLehramtEintrag of this.lehraemter.get())
				if (lehrerLehramtKatalogEintrag59.id === LehrerLehramt.data().getEintragByIDOrException(lehrerLehramtEintrag.idKatalogLehramt).id)
					lehramtId59Vorhanden = true;
				else
					if (lehrerLehramtKatalogEintrag62.id === LehrerLehramt.data().getEintragByIDOrException(lehrerLehramtEintrag.idKatalogLehramt).id)
						lehramtId62Vorhanden = true;
		if (lehramtId59Vorhanden && lehramtId62Vorhanden) {
			this.addFehler(5, "Die Lehramtseinträge 'Sonstige pädagogische Unterrichtshilfe ohne sonderpädagogische Zusatzausbildung' und 'Sonstige pädagogische Unterrichtshilfe mit sonderpädagogischer Zusatzausbildung' sollten nicht zusammen vorliegen. Falls der Lehramtseintrag 'Sonstige pädagogische Unterrichtshilfe mit sonderpädagogischer Zusatzausbildung' korrekt ist, entfernen Sie bitte den Lehramtseintrag 'Sonstige pädagogische Unterrichtshilfe ohne sonderpädagogische Zusatzausbildung'.");
			return false;
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLplk15LehrerPersonaldatenLehramtKombination';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.lehrer.ValidatorLplk15LehrerPersonaldatenLehramtKombination', 'de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLplk15LehrerPersonaldatenLehramtKombination>('de.svws_nrw.asd.validate.lehrer.ValidatorLplk15LehrerPersonaldatenLehramtKombination');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLplk15LehrerPersonaldatenLehramtKombination(obj: unknown): ValidatorLplk15LehrerPersonaldatenLehramtKombination {
	return obj as ValidatorLplk15LehrerPersonaldatenLehramtKombination;
}
