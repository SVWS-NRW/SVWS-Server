import { JavaObject } from '@svws-nrw/svws-core-ts';
import { useDebounceFn } from '@vueuse/shared';
import { Ref, shallowRef } from 'vue';

export interface DebouncedPatchReturn<TData extends JavaObject, TPatch extends Partial<TData>> {
    doPatch: (patchData: TPatch) => Promise<any>,
    error: Ref<Error | undefined>
}

export interface DebouncedPatchOptions {
    delay?: number
    maxWait?: number
}
export function useDebouncedPatch<
    TData extends JavaObject,
    TPatch  extends Partial<TData> = Partial<TData>,
    PatchFn extends (patchData: TPatch) => Promise<any> = (patchData: TPatch) => Promise<any>
>(data: TData, patchFn: PatchFn, options: DebouncedPatchOptions = {}): DebouncedPatchReturn<TData, TPatch> {
	const {
		delay = 500,
		maxWait = 500
	} = options

	const error = shallowRef<Error>()
	function handleError (err: Error): never {
		error.value = err
		throw err
	}

	let cache: TPatch = {} as TPatch;
	// eslint-disable-next-line @typescript-eslint/no-misused-promises
	const _doPatch = useDebounceFn(async (): Promise<any>  => {
		const payload = cache
		cache = {} as TPatch
		return patchFn(payload).catch(handleError)
	}, delay, { maxWait })

	async function doPatch (patchData: TPatch): Promise<any> {
		Object.assign(cache, patchData) // cache patch
		Object.assign(data, patchData) // apply patch to data optimistically
		return _doPatch() // call the internal, debounced patch function
	}

	return {
		doPatch,
		error,
	}
}