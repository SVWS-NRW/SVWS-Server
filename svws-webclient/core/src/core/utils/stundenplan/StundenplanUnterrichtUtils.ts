import { JavaObject } from '../../../java/lang/JavaObject';
import { IllegalStateException } from '../../../java/lang/IllegalStateException';
import { StundenplanKlasse } from '../../../core/data/stundenplan/StundenplanKlasse';
import { StundenplanKurs } from '../../../core/data/stundenplan/StundenplanKurs';
import { JavaString } from '../../../java/lang/JavaString';
import { StundenplanZeitraster } from '../../../core/data/stundenplan/StundenplanZeitraster';
import type { Comparator } from '../../../java/util/Comparator';
import { StundenplanSchueler } from '../../../core/data/stundenplan/StundenplanSchueler';
import { StundenplanRaum } from '../../../core/data/stundenplan/StundenplanRaum';
import { StundenplanLehrer } from '../../../core/data/stundenplan/StundenplanLehrer';
import { StundenplanSchiene } from '../../../core/data/stundenplan/StundenplanSchiene';
import { StundenplanFach } from '../../../core/data/stundenplan/StundenplanFach';
import { JavaLong } from '../../../java/lang/JavaLong';
import { StundenplanUnterricht } from '../../../core/data/stundenplan/StundenplanUnterricht';
import { Class } from '../../../java/lang/Class';
import { Wochentag } from '../../../core/types/Wochentag';

export class StundenplanUnterrichtUtils extends JavaObject {

	/**
	 * Ein Default-Comparator für den Vergleich von Unterrichten in Unterrichtelisten.
	 */
	public static readonly comparator : Comparator<StundenplanUnterricht> = { compare : (a: StundenplanUnterricht, b: StundenplanUnterricht) => JavaLong.compare(a.id, b.id) };

	/**
	 * Ein Default-Comparator für den Vergleich von Fächern in Listen.
	 */
	public static readonly comparatorFaecher : Comparator<StundenplanFach> = { compare : (a: StundenplanFach, b: StundenplanFach) => {
		const cmp : number = JavaString.compareTo(a.kuerzel, b.kuerzel);
		return (cmp === 0) ? JavaLong.compare(a.id, b.id) : cmp;
	} };

	/**
	 * Ein Default-Comparator für den Vergleich von Kurs in Listen.
	 */
	public static readonly comparatorKurse : Comparator<StundenplanKurs> = { compare : (a: StundenplanKurs, b: StundenplanKurs) => {
		const cmp : number = JavaLong.compare(a.id, b.id);
		return (cmp === 0) ? JavaLong.compare(a.id, b.id) : cmp;
	} };

	/**
	 * Ein Default-Comparator für den Vergleich von Räumen in Listen.
	 */
	public static readonly comparatorRaeume : Comparator<StundenplanRaum> = { compare : (a: StundenplanRaum, b: StundenplanRaum) => {
		const cmp : number = JavaString.compareTo(a.kuerzel, b.kuerzel);
		return (cmp === 0) ? JavaLong.compare(a.id, b.id) : cmp;
	} };

	/**
	 * Ein Default-Comparator für den Vergleich von Räumen in Listen.
	 */
	public static readonly comparatorSchienen : Comparator<StundenplanSchiene> = { compare : (a: StundenplanSchiene, b: StundenplanSchiene) => JavaLong.compare(a.nummer, b.nummer) };

	/**
	 * Ein Default-Comparator für den Vergleich von Wochentagen in Listen.
	 */
	public static readonly comparatorWochentage : Comparator<Wochentag> = { compare : (a: Wochentag, b: Wochentag) => JavaLong.compare(a.id, b.id) };

	/**
	 * Ein Default-Comparator für den Vergleich von Zeitrastern in Listen.
	 */
	public static readonly comparatorZeitraster : Comparator<StundenplanZeitraster> = { compare : (a: StundenplanZeitraster, b: StundenplanZeitraster) => JavaLong.compare(a.id, b.id) };

	/**
	 * Ein Default-Comparator für den Vergleich von Stunden in Listen.
	 */
	public static readonly comparatorStunden : Comparator<number> = { compare : (a: number, b: number) => JavaLong.compare(a!, b!) };

	/**
	 * Ein Default-Comparator für den Vergleich von Wochentypen in Listen.
	 */
	public static readonly comparatorWochentypen : Comparator<number> = { compare : (a: number, b: number) => JavaLong.compare(a!, b!) };

	/**
	 * Ein Default-Comparator für den Vergleich von Klassen in Listen.
	 */
	public static readonly comparatorKlassen : Comparator<StundenplanKlasse> = { compare : (a: StundenplanKlasse, b: StundenplanKlasse) => {
		let cmp : number = a.sortierung - b.sortierung;
		if (cmp !== 0)
			return cmp;
		cmp = JavaString.compareTo(a.kuerzel, b.kuerzel);
		return (cmp === 0) ? JavaLong.compare(a.id, b.id) : cmp;
	} };

	/**
	 * Ein Default-Comparator für den Vergleich von Lehrern in Listen.
	 */
	public static readonly comparatorLehrer : Comparator<StundenplanLehrer> = { compare : (a: StundenplanLehrer, b: StundenplanLehrer) => {
		const cmp : number = JavaString.compareTo(a.kuerzel, b.kuerzel);
		return (cmp === 0) ? JavaLong.compare(a.id, b.id) : cmp;
	} };

	/**
	 * Ein Default-Comparator für den Vergleich von Schülern in Listen.
	 */
	public static readonly comparatorSchueler : Comparator<StundenplanSchueler> = { compare : (a: StundenplanSchueler, b: StundenplanSchueler) => {
		const cmp : number = JavaString.compareTo(a.nachname, b.nachname);
		return (cmp === 0) ? JavaLong.compare(a.id, b.id) : cmp;
	} };


	private constructor() {
		super();
		throw new IllegalStateException("Instantiation not allowed")
	}

	transpilerCanonicalName(): string {
		return 'de.svws_nrw.core.utils.stundenplan.StundenplanUnterrichtUtils';
	}

	isTranspiledInstanceOf(name : string): boolean {
		return ['de.svws_nrw.core.utils.stundenplan.StundenplanUnterrichtUtils'].includes(name);
	}

	public static class = new Class<StundenplanUnterrichtUtils>('de.svws_nrw.core.utils.stundenplan.StundenplanUnterrichtUtils');

}

export function cast_de_svws_nrw_core_utils_stundenplan_StundenplanUnterrichtUtils(obj : unknown) : StundenplanUnterrichtUtils {
	return obj as StundenplanUnterrichtUtils;
}
