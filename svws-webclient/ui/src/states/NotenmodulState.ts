import { type InjectionKey } from "vue";
import { DeveloperNotificationException } from "../../../core/src/core/exceptions/DeveloperNotificationException";
import { AppContext } from "../AppContext";

/**
 * Die Schnittstelle für den Zustand des Notenmoduls
 */
export interface NotenmodulState {

	get istAdminLehrer(): boolean | null;
	toggleAdmin(): Promise<void>;

}

export const NotenmodulStateKey: InjectionKey<NotenmodulState> = Symbol('NotenmodulState');

export function useNotenmodulState(): NotenmodulState {
	const state = AppContext.instance.inject(NotenmodulStateKey);
	if (state === undefined) {
		throw new DeveloperNotificationException("Es wurden keine Informationen des NotenmodulState über provide in der main.ts eingebunden");
	}
	return state;
}
