import { getCurrentInstance, ref, computed } from "vue";

export default function useSafeVModel<P extends object, F, K extends keyof P>(props: P, fallbackValue: F, key?: K) {
	const instance = getCurrentInstance();
	const emit = instance?.emit;
	if (!key)
		key = 'modelValue' as K;
	const event = `update:${key.toString()}`;
	const fallback = ref<F>(fallbackValue)
	return computed<F>({
		get: () => {
			// @ts-expect-error fix later
			if (props[key] === undefined)
				return fallback.value;
			// @ts-expect-error fix later
			return props[key];
		},
		set: (value) => {
			// @ts-expect-error fix later
			if (props[key] === undefined) {
				// @ts-expect-error fix later
				fallback.value = value;
			}
			emit?.(event, value);
		},
	})
}
