import { inject, type InjectionKey, type Ref } from 'vue';

import type { UvPlanungsManager } from './UvPlanungsManager';

export const uvPlanungsManagerKey: InjectionKey<Readonly<Ref<UvPlanungsManager | null>>> = Symbol('uvPlanungsManager');

export function useUvPlanungsManager(): Readonly<Ref<UvPlanungsManager | null>> {
	const manager = inject(uvPlanungsManagerKey);
	if (manager === undefined) {
		throw new Error('Der UV-Planungsmanager ist nur innerhalb der Planungsarbeitsfläche verfügbar.');
	}
	return manager;
}
