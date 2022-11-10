import { Geschlecht, SchuelerStammdaten } from "@svws-nrw/svws-core-ts";
import { computed } from "vue";
import { BaseData } from "~/apps/BaseData";

export class Use<T,U> {
	private base_data: BaseData<T,U>;

	constructor(base_data: BaseData<T,U>) {
		this.base_data = base_data;
		// return new Proxy(this, {
		// 	get: (target, key) => {
		// 		const k = key as keyof T
		// 		return target.input(k)
		// 	}
		// });
	}

	public input<K extends keyof T>(prop: K) {
		const base = this.base_data;
		return computed({
			get(): T[K] | undefined {
				const data = base.daten?.[prop];
				return data ?? undefined;
			},
			set(val: T[K] | undefined) {
				const o: Partial<T> = {};
				o[prop] = val ?? undefined;
				base.patch(o);
			}
		});
	}
	public inputGeschlecht<K extends keyof T>(prop?: K) {
		const base = this.base_data;
		return computed({
			get(): Geschlecht | undefined {
				prop ||= 'geschlecht' as K;
				const val = base.daten?.[prop];
				if (typeof val !== "number")
					return undefined;
				return (val) ? Geschlecht.fromValue(Number(val)) || undefined : Geschlecht.X;
			},
			set(val: Geschlecht | undefined) {
				prop ||= 'geschlecht' as K;
				let o: Partial<T> = {};
				if (typeof base.daten?.[prop] !== "number" && typeof base.daten?.[prop] !== "undefined")
					throw new Error("invalid type");
				o[prop] = val?.id as T[K]
				base.patch(o);
			}
		});
	}
}

export class UseSchuelerStammdaten<U> extends Use<SchuelerStammdaten, U> {
	get vorname() { return this.input("vorname") }
	get nachname() { return this.input("nachname") }
	get alleVornamen() { return this.input("alleVornamen") }
	get zusatzNachname() { return this.input("zusatzNachname") }
	get geschlecht() { return this.inputGeschlecht() }
}

// export const useInputGeschlecht = <T,U,K extends keyof T>(base_data: BaseData<T, U>, prop?: K) =>
// 	computed({
// 		get(): Geschlecht | undefined {
// 			prop ||= 'geschlecht' as K;
// 			const val = base_data.daten?.[prop];
// 			if (typeof val !== "number")
// 				return undefined;
// 			return (val) ? Geschlecht.fromValue(Number(val)) || undefined : Geschlecht.X;
// 		},
// 		set(val: Geschlecht | undefined) {
// 			prop ||= 'geschlecht' as K;
// 			let o: Partial<T> = {};
// 			if (typeof base_data.daten?.[prop] !== "number" && typeof base_data.daten?.[prop] !== "undefined")
// 				throw new Error("invalid type");
// 			o[prop] = val?.id as T[K]
// 			base_data.patch(o);
// 		}
// 	});

// export const useInput = <T,U,K extends keyof T>(base_data: BaseData<T,U>, prop: K) => 
// 	computed({
// 		get(): T[K] | undefined {
// 			const data = base_data.daten?.[prop];
// 			return data ?? undefined;
// 		},
// 		set(val: T[K] | undefined) {
// 			const o: Partial<T> = {};
// 			o[prop] = val ?? undefined;
// 			base_data.patch(o);
// 		}
// 	});

// export const useInputString = <T,U>(base_data: BaseData<T, U>, prop: keyof T) =>
// 	computed({
// 		get(): String|undefined {
// 			const val = base_data.daten?.[prop];
// 			if ((typeof val === "undefined") || (val instanceof String))
// 				return val;
// 			throw new Error("invalid type - not String " + (typeof val));
// 		},
// 		set(val: String|undefined) {
// 			const o: Partial<T> = {};
// 			const tmp = base_data.daten?.[prop];
// 			if (typeof tmp !== "undefined" && !(tmp instanceof String))
// 				throw new Error("invalid type - not String");
// 			o[prop] = val as T[keyof T];
// 			base_data.patch(o);
// 		}
// 	});
