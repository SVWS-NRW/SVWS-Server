import type { NotificationEintrag, NotificationsState } from "@ui";
import { StateManager } from "@ui";

interface NotificationsReactiveState {
	/** Alle aktiven Notifications, geordnet nach ID */
	notifications: Map<number, NotificationEintrag>;
	/** Interner Zähler für eindeutige IDs */
	counter: number;
}

/**
 * Implementierung des Notifications-States.
 * Verwaltet anwendungsweite Notifications, die in SWrapper.vue dargestellt werden.
 */
export class NotificationsStateImpl extends StateManager<NotificationsReactiveState> implements NotificationsState {

	/** Laufende Timer-IDs für auto-dismiss, keyed by Notification-ID */
	private readonly _timers = new Map<number, ReturnType<typeof setTimeout>>();

	public constructor() {
		super({
			notifications: new Map(),
			counter: 0,
		});
	}

	/** Alle aktuell aktiven Notifications */
	public get notifications(): ReadonlyMap<number, NotificationEintrag> {
		return this.state.notifications;
	}

	/**
	 * Fügt eine neue Notification hinzu und startet ggf. den auto-dismiss Timer.
	 * @returns Die ID des neuen Eintrags
	 */
	public add(eintrag: Omit<NotificationEintrag, 'id'>): number {
		const id = this.state.counter + 1;
		const neuerEintrag: NotificationEintrag = { ...eintrag, id };

		const notifications = new Map(this.state.notifications);
		notifications.set(id, neuerEintrag);
		this.setPatchedState({ notifications, counter: id });

		if (neuerEintrag.autoDismissMs !== undefined) {
			const timer = setTimeout(() => this.remove(id), neuerEintrag.autoDismissMs);
			this._timers.set(id, timer);
		}

		return id;
	}

	/** Entfernt eine Notification und bricht einen laufenden Timer ab */
	public remove(id: number): void {
		const timer = this._timers.get(id);
		if (timer !== undefined) {
			clearTimeout(timer);
			this._timers.delete(id);
		}

		const notifications = new Map(this.state.notifications);
		notifications.delete(id);
		this.setPatchedState({ notifications });
	}

	/** Entfernt alle Notifications und bricht alle Timer ab */
	public removeAll(): void {
		for (const timer of this._timers.values()) {
			clearTimeout(timer);
		}
		this._timers.clear();
		this.setPatchedState({ notifications: new Map() });
	}

	// -----------------------------------------------------------------------------
	// Methoden zur schnellen Erstellung der häufigsten Notifications
	// -----------------------------------------------------------------------------

	/** Fügt eine Info-Notification mit optionalem auto-dismiss hinzu */
	public info(titel: string, nachricht: string, autoDismissMs = 5000): number {
		return this.add({ type: 'info', titel, nachricht, autoDismissMs });
	}

	/** Fügt eine Erfolgs-Notification mit optionalem auto-dismiss hinzu */
	public success(titel: string, nachricht: string, autoDismissMs = 5000): number {
		return this.add({ type: 'success', titel, nachricht, autoDismissMs });
	}

	/** Fügt eine Warn-Notification ohne auto-dismiss hinzu */
	public warning(titel: string, nachricht: string, autoDismissMs?: number): number {
		return this.add({ type: 'warning', titel, nachricht, autoDismissMs });
	}

	/** Fügt eine Fehler-Notification ohne auto-dismiss hinzu */
	public error(titel: string, nachricht: string, autoDismissMs?: number): number {
		return this.add({ type: 'error', titel, nachricht, autoDismissMs });
	}
}

export const notificationStateImpl = new NotificationsStateImpl();
