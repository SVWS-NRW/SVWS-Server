import type { InjectionKey } from "vue";
import { AppContext } from "../AppContext";
import { DeveloperNotificationException } from "../../../core/src/core/exceptions/DeveloperNotificationException";

/** Erlaubte Notification-Typen, analog zu {@link SvwsUiNotification} */
export type NotificationType = 'info' | 'success' | 'warning' | 'error' | 'bug';

/** Ein einzelner Notification-Eintrag im State */
export interface NotificationEintrag {
	/** Eindeutige ID, wird intern hochgezählt */
	id: number;
	/** Darstellungstyp */
	type: NotificationType;
	/** Kurze Überschrift */
	titel: string;
	/** Ausführliche Nachricht */
	nachricht: string;
	/**
	 * Optionale Anzahl Millisekunden, nach denen die Notification automatisch entfernt wird.
	 * Wird `undefined` übergeben, bleibt sie bis zum manuellen Schließen bestehen.
	 */
	autoDismissMs?: number;
}

/** Öffentliche Schnittstelle des Notification-States */
export interface NotificationsState {
	/** Alle aktuell sichtbaren Notifications (neueste zuerst) */
	readonly notifications: ReadonlyMap<number, NotificationEintrag>;

	/** Fügt eine neue Notification hinzu. */
	add(eintrag: Omit<NotificationEintrag, 'id'>): number;

	/** Fügt eine neue Info-Notification hinzu. */
	info(titel: string, nachricht: string, autoDismissMs?: number): number;
	/** Fügt eine neue Success-Notification hinzu. */
	success(titel: string, nachricht: string, autoDismissMs?: number): number;
	/** Fügt eine neue Warning-Notification hinzu. */
	warning(titel: string, nachricht: string, autoDismissMs?: number): number;
	/** Fügt eine neue Error-Notification hinzu. */
	error(titel: string, nachricht: string, autoDismissMs?: number): number;

	/** Entfernt eine Notification anhand ihrer ID */
	remove(id: number): void;

	/** Entfernt alle Notifications */
	removeAll(): void;
}

export const NotificationsStateKey: InjectionKey<NotificationsState> = Symbol('NotificationsState');

export function useNotificationsState(): NotificationsState {
	const state = AppContext.instance.inject(NotificationsStateKey);
	if (state === undefined) {
		throw new DeveloperNotificationException("Es wurde keine Instanz des NotificationsState über provide in der main.ts eingebunden");
	}
	return state;
}
