import type { RouteLocationRaw } from "vue-router";

export class Checkpoint {

	/** Dieser Callback wird genutzt, um Aktionen in der Vue Komponente aus der {@link Checkpoint.doCheckpoint} Hook heraus auslösen zu können. */
	private _callback: (() => Promise<void>) | undefined = undefined;

	/**
	 * Gibt an, ob der Checkpoint gerade aktiv ist.
	 * Hinweis: Nachdem ein Checkpoint betreten wurde, wird er automatisch wieder deaktivert. Sollte gewollt sein, dass der Checkpoint nach der
	 * {@link Checkpoint.doCheckpoint} Hook weiterhin aktiv bleibt, muss dieser erneut aktiviert werden.
	 */
	private _active: boolean = false;

	/**
	 * Ursprüngliches Routing Ziel, bevor ein Checkpoint aktiviert wurde.
	 * Hinweis: Das Routing zum ursprünglichen Ziel kann mit der Methode {@link RouteManager.continueRoutingAfterCheckpoint} fortgeführt werden.
	 */
	private _originallyDestinationRoute: RouteLocationRaw | undefined = undefined;


	/**
	 * Gibt den definierten callback für diesen Checkpoint zurück.
	 */
	public get callback() : (() => Promise<void>) | undefined {
		return this._callback;
	}

	/**
	 * Setzt eine Callback-Funktion für diesen Checkpoint. Der Callback wird aufgerufen, wenn der Checkpoint im aktiven Zustand betreten wird.
	 *
	 * @param callback Callback Funktion für diesen Checkpoint
	 */
	public set callback(callback: (() => Promise<void>) | undefined) {
		this._callback = callback;
	}

	/**
	 * Setzt den Checkpoint aktiv oder inaktiv.
	 *
	 * @param active aktiv Flag
	 */
	public set active(active: boolean) {
		this._active = active;
	}

	/**
	 * Gibt zurück, ob der Checkpoint gerade aktiv ist.
	 *
	 * @return <code>true</code> Checkpoint aktiv oder <code>false</code> Checkpoint inaktiv
	 */
	public get active() : boolean {
		return this._active;
	}

	/**
	 * Gibt das ursprüngliche Ziel des Routings vor dem Checkpoint zurück.
	 */
	public get originallyDestinationRoute() : RouteLocationRaw | undefined {
		return this._originallyDestinationRoute;
	}

	/**
	 * Setzt das ursprüngliche Ziel des Routings, das vor dem Betreten des Checkpoints gesetzt war.
	 */
	public set originallyDestinationRoute(originallyDestinationRoute: RouteLocationRaw | undefined) {
		this._originallyDestinationRoute = originallyDestinationRoute;
	}

	/**
	 * Hook die ausgeführt wird, nach dem ein aktiver Checkpoint betreten wurde.
	 *
	 * @param destination Ziel Route zu der ursprünglich navigiert werden sollte.
	 */
	public async doCheckpoint(destination: RouteLocationRaw) {
		this._active = false;
		this._originallyDestinationRoute = destination;
		if (this._callback !== undefined)
			await this._callback();
	}

}
