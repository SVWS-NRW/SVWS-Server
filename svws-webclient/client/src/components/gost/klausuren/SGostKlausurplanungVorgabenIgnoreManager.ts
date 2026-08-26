import type { List } from "@core";
import { ArrayList, GostKlausurvorgabe } from "@core";
import { CONFIG_KEY_GOST_KLAUSURPLAN_PREFIX } from "@ui";

export const CONFIG_KEY_GOST_KLAUSURPLAN_VORGABENTOIGNORE = CONFIG_KEY_GOST_KLAUSURPLAN_PREFIX + "vorgabenToIgnore";

export class SGostKlausurplanungVorgabenIgnoreManager {

	public static readonly CONFIG_KEY_IGNORE = CONFIG_KEY_GOST_KLAUSURPLAN_VORGABENTOIGNORE;

	public constructor(
		private readonly getObjectValue: <T>(
			key: string,
			fromJSON: (json: string) => T
		) => T | null,

		private readonly setObjectValue?: <T>(
			key: string,
			value: T | null,
			toJSON: (obj: T) => string
		) => Promise<void>
	) {}

	private equals(a: GostKlausurvorgabe, b: GostKlausurvorgabe): boolean {
		return (a.halbjahr === b.halbjahr)
			&& (a.quartal === b.quartal)
			&& (a.idFach === b.idFach)
			&& (a.kursart === b.kursart);
	}

	private parse(json: string): GostKlausurvorgabe[] {
		const arr = JSON.parse(json);
		if (!Array.isArray(arr)) {
			return [];
		}
		const result: GostKlausurvorgabe[] = [];
		for (const e of arr) {
			if ((typeof e !== "object") || (e === null)) {
				continue;
			}
			const obj = e as Partial<GostKlausurvorgabe>;
			if ((obj.halbjahr === undefined) || (obj.quartal === undefined) || (obj.idFach === undefined) || (obj.kursart === undefined)) {
				continue;
			}
			const vorgabe = new GostKlausurvorgabe();
			vorgabe.halbjahr = obj.halbjahr;
			vorgabe.quartal = obj.quartal;
			vorgabe.idFach = obj.idFach;
			vorgabe.kursart = obj.kursart;
			result.push(vorgabe);
		}
		return result;
	}

	private stringify(list: GostKlausurvorgabe[]): string {
		return JSON.stringify(list.map(v => ({
			halbjahr: v.halbjahr,
			quartal: v.quartal,
			idFach: v.idFach,
			kursart: v.kursart,
		})));
	}

	public getAll(): List<GostKlausurvorgabe> {
		const arr = this.getObjectValue(
			SGostKlausurplanungVorgabenIgnoreManager.CONFIG_KEY_IGNORE,
			(json) => this.parse(json)
		) ?? [];
		const list = new ArrayList<GostKlausurvorgabe>();
		for (const e of arr) {
			list.add(e);
		}
		return list;
	}

	public async add(v: GostKlausurvorgabe): Promise<void> {
		if (!this.setObjectValue) {
			throw new Error("Write operation not allowed: setObjectValue is undefined.");
		}
		const list = this.getAll();
		let exists = false;
		for (const x of list) {
			if (this.equals(x, v)) {
				exists = true;
			}
		}
		if (!exists) {
			list.add(v);
			const arr: GostKlausurvorgabe[] = [];
			for (const x of list) {
				arr.push(x);
			}
			await this.setObjectValue(
				SGostKlausurplanungVorgabenIgnoreManager.CONFIG_KEY_IGNORE,
				arr,
				(obj) => this.stringify(obj)
			);
		}
	}

	public async remove(v: GostKlausurvorgabe): Promise<void> {
		if (!this.setObjectValue) {
			throw new Error("Write operation not allowed: setObjectValue is undefined.");
		}
		const list = this.getAll();
		const newArr: GostKlausurvorgabe[] = [];
		for (const x of list) {
			if (!this.equals(x, v)) {
				newArr.push(x);
			}
		}
		await this.setObjectValue(
			SGostKlausurplanungVorgabenIgnoreManager.CONFIG_KEY_IGNORE,
			newArr,
			(obj) => this.stringify(obj)
		);
	}

	public contains(v: GostKlausurvorgabe): boolean {
		const list = this.getAll();
		for (const x of list) {
			if (this.equals(x, v)) {
				return true;
			}
		}
		return false;
	}

	public countContained(vorgaben: Iterable<GostKlausurvorgabe>): number {
		const ignored = this.getAll();
		let count = 0;
		for (const v of vorgaben) {
			for (const i of ignored) {
				if (this.equals(v, i)) {
					count++;
					break;
				}
			}
		}
		return count;
	}
}
