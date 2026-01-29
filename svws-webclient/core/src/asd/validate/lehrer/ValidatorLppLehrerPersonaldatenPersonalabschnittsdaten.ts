import { ValidatorLpppLehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll } from '../../../asd/validate/lehrer/ValidatorLpppLehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll';
import { ValidatorLppbbLehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsartBlockmodell } from '../../../asd/validate/lehrer/ValidatorLppbbLehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsartBlockmodell';
import { ArrayList } from '../../../java/util/ArrayList';
import { LehrerPersonalabschnittsdatenAnrechnungsstunden } from '../../../asd/data/lehrer/LehrerPersonalabschnittsdatenAnrechnungsstunden';
import { ValidatorLppbLehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart } from '../../../asd/validate/lehrer/ValidatorLppbLehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart';
import { ValidatorLpprLehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis } from '../../../asd/validate/lehrer/ValidatorLpprLehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis';
import { DateManager } from '../../../asd/validate/DateManager';
import type { List } from '../../../java/util/List';
import type { Supplier } from '../../../java/util/function/Supplier';
import { Class } from '../../../java/lang/Class';
import { ValidatorKontext } from '../../../asd/validate/ValidatorKontext';
import { Validator } from '../../../asd/validate/Validator';

export class ValidatorLppLehrerPersonaldatenPersonalabschnittsdaten extends Validator {

	/**
	 * Eine Liste von Validatoren, die bei diesem Validator mitgeprüft werden.
	 */
	protected readonly validatoren: List<Validator> = new ArrayList<Validator>();

	/**
	 * das Geburtsdatum des Lehrers
	 */
	protected readonly geburtsdatum: Supplier<string | null>;

	/**
	 * Die ID des Schuljahresabschnittes
	 */
	protected readonly idSchuljahresabschnitt: Supplier<number>;

	/**
	 * Das Rechtsverhältnis
	 */
	protected readonly rechtsverhaeltnis: Supplier<string | null>;


	/**
	 * Erstellt einen neuen Validator mit den übergebenen Daten und dem übergebenen Kontext
	 *
	 * @param idSchuljahresabschnitt  die ID des Schuljahresabschnittes
	 * @param rechtsverhaeltnis       das Rechtsverhältnis
	 * @param pflichtstundensoll      der Pflichtstundensoll
	 * @param einsatzstatus           der Einsatz-Status
	 * @param beschaeftigungsart      die Beschäftigungsart
	 * @param geburtsdatum            das Geburtsdatum des Lehrers
	 * @param mehrleistungen          die Liste mit den Einträgen zu Mehrleistungen
	 * @param minderleistungen        die Liste mit den Einträgen zu Minderleistungen
	 * @param kontext                 der Kontext des Validators
	 */
	public constructor(idSchuljahresabschnitt: Supplier<number>, rechtsverhaeltnis: Supplier<string | null>, pflichtstundensoll: Supplier<number | null>, einsatzstatus: Supplier<string | null>, beschaeftigungsart: Supplier<string | null>, geburtsdatum: Supplier<string | null>, mehrleistungen: Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>>, minderleistungen: Supplier<List<LehrerPersonalabschnittsdatenAnrechnungsstunden>>, kontext: ValidatorKontext) {
		super(kontext);
		this.geburtsdatum = geburtsdatum;
		this.idSchuljahresabschnitt = idSchuljahresabschnitt;
		this.rechtsverhaeltnis = rechtsverhaeltnis;
		this.validatoren.add(new ValidatorLpppLehrerPersonaldatenPersonalabschnittsdatenPflichtstundensoll(pflichtstundensoll, einsatzstatus, beschaeftigungsart, kontext));
		this.validatoren.add(new ValidatorLppbLehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsart(this.getNotNullSupplier(beschaeftigungsart), this.getNotNullSupplier(einsatzstatus), pflichtstundensoll, kontext));
		this.validatoren.add(new ValidatorLppbbLehrerPersonaldatenPersonalabschnittsdatenBeschaeftigungsartBlockmodell(beschaeftigungsart, pflichtstundensoll, einsatzstatus, mehrleistungen, minderleistungen, kontext));
	}

	protected pruefe(): boolean {
		this._validatoren.clear();
		this._validatoren.addAll(this.validatoren);
		try {
			const datum: DateManager = DateManager.from(this.geburtsdatum.get());
			const supplierGeburtsdatumNullable: Supplier<DateManager | null> = { get: () => datum };
			const supplierGeburtsdatum: Supplier<DateManager> = this.getNotNullObjectSupplier(supplierGeburtsdatumNullable);
			this._validatoren.add(new ValidatorLpprLehrerPersonaldatenPersonalabschnittsdatenRechtsverhaeltnis(this.idSchuljahresabschnitt, this.rechtsverhaeltnis, supplierGeburtsdatum, this.kontext()));
		} catch(e : any) {
			// empty block
		}
		return true;
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.asd.validate.lehrer.ValidatorLppLehrerPersonaldatenPersonalabschnittsdaten';
	}

	isTranspiledInstanceOf(name: string): boolean {
		return ['de.svws_nrw.asd.validate.BasicValidator', 'de.svws_nrw.asd.validate.lehrer.ValidatorLppLehrerPersonaldatenPersonalabschnittsdaten', 'de.svws_nrw.asd.validate.Validator'].includes(name);
	}

	public static readonly class = new Class<ValidatorLppLehrerPersonaldatenPersonalabschnittsdaten>('de.svws_nrw.asd.validate.lehrer.ValidatorLppLehrerPersonaldatenPersonalabschnittsdaten');

}

export function cast_de_svws_nrw_asd_validate_lehrer_ValidatorLppLehrerPersonaldatenPersonalabschnittsdaten(obj: unknown): ValidatorLppLehrerPersonaldatenPersonalabschnittsdaten {
	return obj as ValidatorLppLehrerPersonaldatenPersonalabschnittsdaten;
}
