import type { JavaObject } from "@svws-nrw/svws-core";
import { useDebounceFn } from '@vueuse/shared';
import { type Ref, shallowRef, watch } from 'vue';

export interface DebouncedPatchReturn<TData extends JavaObject> {
    doPatch: (patchData: Partial<TData>) => Promise<any>,
    error: Ref<Error | undefined>
}

export interface DebouncedPatchOptions {
    delay?: number
    maxWait?: number
}

/**
 * Erzeugt eine Funktion die Patchesfür den Datensatz sammelt und diesne optimistisch aktualisiert,
 * den Patch aber erst nach einer gewissen Wartezeit Zeit als Batch an den Server sendet.
 *
 * @export
 * @param {Ref<TData>} data Ref auf den Datensatz
 * @param {PatchFn} patchFn Asynchrone Funktion zum senden des Upadtes ans Backend
 * @param {DebouncedPatchOptions} [options={}]
 * @returns {DebouncedPatchReturn<TData, TPatch>}
 */
export function useDebouncedPatch<
    TData extends JavaObject,
    // TPatch  extends Partial<TData> = Partial<TData>,
    PatchFn extends (patchData: Partial<TData>) => Promise<any> = (patchData: Partial<TData>) => Promise<any>
>(data: Ref<TData>, patchFn: PatchFn, options: DebouncedPatchOptions = {}): DebouncedPatchReturn<TData> {
	const {
		delay = 1000,
		maxWait = 5000
	} = options
	const error = shallowRef<Error>()
	function handleError (err: Error): never {
		error.value = err
		throw err
	}

	let cache: Partial<TData> = {}
	// cache zurücksetzen wenn (i.d.R. durch Routenwechsel) der Datensatz gewechselt wird.
	watch(data, () => {
		if (Object.keys(cache).length) {
			// Bevor wir den Cache leeren, senden wir ggf. noch gecachte Patches ab
			patchFn(cache).catch(handleError)
		}
		cache = {}
	})

	// eslint-disable-next-line @typescript-eslint/no-misused-promises
	const _doPatch = useDebounceFn(async (): Promise<any>  => {
		const payload = cache
		cache = {}
		return patchFn(payload).catch(handleError)
	}, delay, { maxWait })

	async function doPatch (patchData: Partial<TData>): Promise<any> {
		Object.assign(cache, patchData) // cache patch
		Object.assign(data.value, patchData) // apply patch to data optimistically
		return _doPatch() // call the internal, debounced patch function
	}

	return {
		doPatch,
		error,
	}
}