import { Geschlecht } from '@svws-nrw/svws-core-ts';
import { computed, type WritableComputedRef } from 'vue';
import { BaseData } from '../../apps/BaseData';

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
				const o: Partial<T> = {};
				if (typeof base.daten?.[prop] !== "number" && typeof base.daten?.[prop] !== "undefined")
					throw new Error("invalid type");
				o[prop] = val?.id as T[K]
				base.patch(o);
			}
		});
	}
}
