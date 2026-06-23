import { ref } from "vue";
import type { ActivityState } from "./ActivityState";

/**
 * Die Implementierung für den Status der Authentifizierung des WeNoM-Clients
 */
class ActivityStateImpl implements ActivityState {

	// Die maximale Sitzungsdauer in Sekunden
	private static readonly MAX_REMAINING_SECONDS = 300;

	// Die Länge des Intervalls in Millisekunden, bis die Sitzungsdauer neu berechnet wird
	private static readonly INTERVAL_MILLI_SECONDS = 1_000;

	// Options für die Event-Listener
	private readonly _options: AddEventListenerOptions = { capture: true, passive: true };

	// Intervall-ID für die Aktivitätsprüfung
	private _intervalID: ReturnType<typeof setInterval> | null = null;

	// gibt an, ob eine Aktivität stattgefunden hat
	private _activity: boolean = false;

	// Gibt die letzte Aktivität in Millisekunden an
	private _lastActivity = 0;

	// Gibt als Ref die für die Sitzung verbleibenden Sekunden an
	private readonly _remainingSeconds = ref(0);

	// Die Funktion, die beim Logout ausgeführt wird
	private _logoutHandler = async () => {};

	/**
	 * Gibt die verbleibende Zeit der Sitzung in Sekunden zurück
	 *
	 * @returns die Sekunden
	 */
	public get remainingSeconds(): number {
		return this._remainingSeconds.value;
	}

	/**
	 * Setzt die Aktivität für die Dauer des nächsten Invervalls auf true
	 */
	private activityTracker() {
		this._activity = true;
	}

	/**
	 * Prüft, ob im letzten Intervall eine Aktivität vorgelegen hat und aktualisiert bei Bedarf
	 * den Zähler für die verbleibenden Sekunden
	 */
	private async checkActivity() {
		if (this._activity) {
			this._lastActivity = Date.now();
			this._activity = false;
			this._remainingSeconds.value = ActivityStateImpl.MAX_REMAINING_SECONDS;
		} else {
			const remaining = ActivityStateImpl.MAX_REMAINING_SECONDS - Math.floor((Date.now() - this._lastActivity) / 1_000);
			if (remaining <= 0) {
				await this._logoutHandler();
			}
			this._remainingSeconds.value = remaining;
		}
	}

	/**
	 * Started die Verfolgung von Aktivitäten im Client.
	 */
	public start(logoutHandler: () => Promise<void>): void {
		this._logoutHandler = logoutHandler;
		addEventListener("pointerdown", () => this.activityTracker(), this._options);
		addEventListener("wheel", () => this.activityTracker(), this._options);
		addEventListener("keydown", () => this.activityTracker(), this._options);
		this._activity = false;
		this._lastActivity = Date.now();
		this._remainingSeconds.value = ActivityStateImpl.MAX_REMAINING_SECONDS;
		this._intervalID = globalThis.setInterval(() => void this.checkActivity(), ActivityStateImpl.INTERVAL_MILLI_SECONDS);
	}

	/**
	 * Beendet die Verfolgung von Aktivitäten im Client.
	 */
	public stop(): void {
		if (this._intervalID !== null) {
			clearInterval(this._intervalID);
			this._intervalID = null;
		}
		this._activity = false;
		this._lastActivity = 0;
		this._remainingSeconds.value = 0;
		removeEventListener("pointerdown", () => this.activityTracker(), this._options);
		removeEventListener("wheel", () => this.activityTracker(), this._options);
		removeEventListener("keydown", () => this.activityTracker(), this._options);
		this._logoutHandler = async () => {};
	}

}

export const activityState = new ActivityStateImpl();
