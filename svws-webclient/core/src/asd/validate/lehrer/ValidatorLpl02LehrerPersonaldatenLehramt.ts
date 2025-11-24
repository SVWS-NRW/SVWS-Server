import { LehrerPersonaldaten } from '../../../asd/data/lehrer/LehrerPersonaldaten';
import { LehrerLehramtEintrag } from '../../../asd/data/lehrer/LehrerLehramtEintrag';
import { HashMap } from '../../../java/util/HashMap';
import { LehrerLehramt } from '../../../asd/types/lehrer/LehrerLehramt';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import type { JavaMap } from '../../../java/util/JavaMap';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLpl02LehrerPersonaldatenLehramt extends Validator {

	/**
	 * Die Lehrer-Personalabschnittsdaten
	 */
	private readonly lehrerPersonaldaten: LehrerPersonaldaten;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param lehrerPersonaldaten   die Lehrer-Personaldaten, die geprüft werden sollen
	 * @param kontext               der Kontext des Validators
	 */
	public constructor(lehrerPersonaldaten: LehrerPersonaldaten, kontext: ValidatorKontext) {
		super(kontext);
		this.lehrerPersonaldaten = lehrerPersonaldaten;
	}

	protected pruefe(): boolean {
		const lehramtMap: JavaMap<number, LehrerLehramtEintrag> = new HashMap<number, LehrerLehramtEintrag>();
		for (const lehrerLehramtEintrag of this.lehrerPersonaldaten.lehraemter) {
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
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLpl02LehrerPersonaldatenLehramt';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLpl02LehrerPersonaldatenLehramt', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static class = new Class<ValidatorLpl02LehrerPersonaldatenLehramt>('de.svws_nrw.asd.validate.lehrer.ValidatorLpl02LehrerPersonaldatenLehramt');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLpl02LehrerPersonaldatenLehramt(obj: unknown): ValidatorLpl02LehrerPersonaldatenLehramt {
	return obj as ValidatorLpl02LehrerPersonaldatenLehramt;
}
