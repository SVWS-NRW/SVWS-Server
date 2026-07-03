import { DeveloperNotificationException } from '@core/core/exceptions/DeveloperNotificationException';
import { AppContext } from '@ui/AppContext';
import { type InjectionKey } from 'vue';

/**
 *  Die Schnittstelle für den Zustand der Authentifizierung im Client
 */
export interface ActivityState {

	/**
	 * Gibt die verbleibende Zeit der Sitzung in Sekunden zurück
	 *
	 * @returns die Sekunden
	 */
	get remainingSeconds(): number;

}

export const ActivityStateKey: InjectionKey<ActivityState> = Symbol('ActivityState');

export function useActivityState(): ActivityState {
	const activityState = AppContext.instance.inject(ActivityStateKey);
	if (activityState === undefined) {
		throw new DeveloperNotificationException("Es wurde keine Instanz des ActivityState über provide in der main.ts eingebunden.");
	}
	return activityState;
}
