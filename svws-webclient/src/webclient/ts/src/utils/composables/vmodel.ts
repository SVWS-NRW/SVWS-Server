import type { WritableComputedRef } from 'vue';
import { useVModel as _useVModel, type UseVModelOptions } from '@vueuse/core';

// wir wrappen die implementierung von VueUse/Core,
//  weil die der Return type sonst mit unseren core-ts Klassen clasht.
export function useVModel<
  P extends object,
  K extends keyof P,
  Name extends string
>(
  props: P,
  key?: K,
  emit?: (name: Name, ...args: any[]) => void,
  options?:UseVModelOptions<P[K]>
) {
  return _useVModel(props, key, emit, options) as WritableComputedRef<P[K]>;
}