import { LehrerLehramtEintrag } from '../../../asd/data/lehrer/LehrerLehramtEintrag';
import { HashMap } from '../../../java/util/HashMap';
import { LehrerLehramt } from '../../../asd/types/lehrer/LehrerLehramt';
import type { Supplier } from '../../../java/util/function/Supplier';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import type { JavaMap } from '../../../java/util/JavaMap';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLpl10LehrerPersonaldatenLehramt extends Validator {

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
		const lehramtMap: JavaMap<number, LehrerLehramtEintrag> = new HashMap<number, LehrerLehramtEintrag>();
		for (const lehrerLehramtEintrag of this.lehraemter.get()) {
			if (lehramtMap.put(lehrerLehramtEintrag.idKatalogLehramt, lehrerLehramtEintrag) !== null) {
				try {
					this.addFehler(2, "Das Lehramt '" + LehrerLehramt.data().getEintragByIDOrException(lehrerLehramtEintrag.idKatalogLehramt).text + "' ist mehrfach eingetragen. Bitte löschen Sie die überflüssigen Einträge.");
				} catch(e : any) {
					this.addFehler(2, "Das Lehramt '" + lehrerLehramtEintrag.idKatalogLehramt + "' ist mehrfach eingetragen. Bitte löschen Sie die überflüssigen Einträge.");
				}
				return false;
			}
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLpl10LehrerPersonaldatenLehramt';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLpl10LehrerPersonaldatenLehramt', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLpl10LehrerPersonaldatenLehramt>('de.svws_nrw.asd.validate.lehrer.ValidatorLpl10LehrerPersonaldatenLehramt');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLpl10LehrerPersonaldatenLehramt(obj: unknown): ValidatorLpl10LehrerPersonaldatenLehramt {
	return obj as ValidatorLpl10LehrerPersonaldatenLehramt;
}
