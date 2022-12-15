import { computed, WritableComputedRef } from 'vue';
import { BaseData } from '../../apps/BaseData';

export type Transformer<ToType, TData, K extends keyof TData> = {
	get(v: TData[K] | undefined): ToType,
	set(v: ToType | undefined): TData[K] | undefined
}

type ReturnValue<RV> = NonNullable<RV extends String ? string : RV extends Number ? number : RV extends Boolean ? boolean : RV >;

const NOOP = (v: any) => v
function createNoOpTransformer<TData, K extends keyof TData, ToType = TData[K]>() {
	return {
		get: NOOP,
		set: NOOP
	} as Transformer<ToType, TData, K>
}

/**
 * Erzeugt eine computed Property, die ein Input Feld mit einer property eines BaseData Objekts verbindet und den Wert dabei für die UI transformiert
 * Beim setzen seines Wertes wird dieser direkt an das Backend gesendet
 * @param base_data BaseData Objekt
 * @param prop Name der Property
 * @param transformer Transformer Objekt um den Wert er property für die UI zu transformieren (und zurück)
 * @returns Computed Property die über v-model an das Input Feld gebunden werden kann
 */
export const useInputForBaseData = <
	T, U, K extends keyof T = keyof T, 
	TBaseData extends BaseData<T, U> = BaseData<T, U>,
	ToType extends unknown = T[K]
>(
	base_data: TBaseData,
	prop: K,
	transformer?: Transformer<ToType, T, K>
): WritableComputedRef<ReturnValue<ToType> | undefined> => {
	const _transformer = transformer ?? createNoOpTransformer<T, K, ToType>()
	return computed({
		get(): ReturnValue<ToType> | undefined {
			const data = base_data.daten?.[prop];
			const newData = _transformer.get(data) as ReturnValue<ToType>
			return newData ?? undefined;
		},
		set(val: ReturnValue<ToType> | undefined) {
			const o: Partial<T> = {};
			o[prop] = _transformer.set(val as ToType) ?? undefined;
			base_data.patch(o);
		}
	});
}

export function useInputWithBaseData<T,U>(base_data: BaseData<T, U>) {
	return <K extends keyof T, ToType = T[K]>(prop: K, transformer?: Transformer<ToType, T, K>) => 
		useInputForBaseData(base_data, prop, transformer ?? createNoOpTransformer<T,K, ToType>())
}