import { LehrerLehramtEintrag } from '../../../asd/data/lehrer/LehrerLehramtEintrag';
import { ValidatorLplLehrerPersonaldatenLehramt } from '../../../asd/validate/lehrer/ValidatorLplLehrerPersonaldatenLehramt';
import { LehrerPersonalabschnittsdatenAnrechnungsstunden } from '../../../asd/data/lehrer/LehrerPersonalabschnittsdatenAnrechnungsstunden';
import type { Supplier } from '../../../java/util/function/Supplier';
import type { List } from '../../../java/util/List';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { ValidatorLppLehrerPersonaldatenPersonalabschnittsdaten } from '../../../asd/validate/lehrer/ValidatorLppLehrerPersonaldatenPersonalabschnittsdaten';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLpLehrerPersonaldaten extends Validator {


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param lehrerId                die LehrerId
	 * @param idSchuljahresabschnitt  die ID des Schuljahresabschnittes
	 * @param rechtsverhaeltnis       das Rechtsverhältnis
	 * @param pflichtstundensoll      der Pflichtstundensoll
	 * @param einsatzstatus           der Einsatz-Status
	 * @param beschaeftigungsart      die Beschäftigungsart
	 * @param geburtsdatum            das Geburtsdatum des Lehrers
	 * @param lehraemter              die Liste der Lehrämter, die geprüft werden sollen
	 * @param mehrleistungen          die Liste mit den Einträgen zu Mehrleistungen
	 * @param minderleistungen        die Liste mit den Einträgen zu Minderleistungen
	 * @param kontext                 der Kontext des Validators
	 */
	public constructor(lehrerId: Supplier<number>, idSchuljahresabschnitt: Supplier<number>, rechtsverhaeltnis: Supplier<string | null>, pflichtstundensoll: Supplier<number | null>, einsatzstatus: Supplier<string | null>, beschaeftigungsart: Supplier<string | null>, geburtsdatum: Supplier<string | null>, lehraemter: Supplier<List<LehrerLehramtEintrag>>, mehrleistungen: Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>>, minderleistungen: Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>>, kontext: ValidatorKontext) {
		super(kontext);
		this._validatoren.add(new ValidatorLppLehrerPersonaldatenPersonalabschnittsdaten(idSchuljahresabschnitt, rechtsverhaeltnis, pflichtstundensoll, einsatzstatus, beschaeftigungsart, geburtsdatum, mehrleistungen, minderleistungen, kontext));
		this._validatoren.add(new ValidatorLplLehrerPersonaldatenLehramt(lehraemter, lehrerId, this.getDateManagerSupplier(geburtsdatum), kontext));
	}

	protected pruefe(): boolean {
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLpLehrerPersonaldaten';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLpLehrerPersonaldaten', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLpLehrerPersonaldaten>('de.svws_nrw.asd.validate.lehrer.ValidatorLpLehrerPersonaldaten');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLpLehrerPersonaldaten(obj: unknown): ValidatorLpLehrerPersonaldaten {
	return obj as ValidatorLpLehrerPersonaldaten;
}
