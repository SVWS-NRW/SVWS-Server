import type { ModelProxy } from "./ModelProxy";

/**
 * Der Proxy-Handler, welcher in der Klasse ModelProxy verwendet wird.
 * Er fängt die Zugriffe auf get und set ab.
 *
 * Beim get liefert der Handler dann die zusammengesetzten Daten aus "Original"-Daten und dem Pending-State.
 *
 * Beim set werden die Daten erstmal in den Pending-State geschrieben. Sind Validatoren gesetzt,
 * so werden diese angewandt und es wird die Liste mit den zugehörigen ValidatorFehler-Objekten bereitgestellt.
 * Ist direktes Patchen eingestellt, so wird die patch-Methode der Klasse ModelProxy aufgerufen.
 */
export class ModelProxyHandler<T extends object> implements ProxyHandler<T> {

	/** Die Instanz der Klasse ModelProxy, welcher dieser Handler zugeordnet ist */
	private readonly modelProxy: ModelProxy<T>;

	/**
	 * Erstellt einen neuen Handler, welcher den übergebenen ModelProxy bedient.
	 *
	 * @param modelProxy   der zugehörige ModelProxy
	 */
	constructor(modelProxy: ModelProxy<T>) {
		this.modelProxy = modelProxy;
	}


	/**
	 * Die Methode get, welche die lesenden Zugriffe auf das Proxy-Objekt handhabt.
	 *
	 * @param target     das Ziel des Proxies, in diesem Fall die "Original"-Daten
	 * @param prop       die Property des Proxies, welche angesprochen wird
	 * @param receiver   das Objekt, auf welches sich der lesende Zugriff ursprünglich bezogen hat.
	 *
	 * @returns bei Attributen der ggf. angepasste Wert des Attributs und bei Funktionen der Rückgabewert der Funktion
	 */
	public get(target: T, prop: string | symbol, receiver: any): any {
		const value = target[prop as keyof T];
		if (typeof value === "function") {
			return (...args: any) => value.apply((this === receiver) ? target : this, args);
		}
		if (typeof prop === "string") {
			// Prüfe, ob ein pending value vorliegt und geben ggf. diesen zurück
			const pending = this.modelProxy.pending;
			const pendingValue = pending[prop as keyof T];
			if (pendingValue !== undefined) {
				return pendingValue;
			}
		}
		return value;
	}


	/**
	 * Die Methode set, welche den schreibenden Zugriff auf das Proxy-Objekt handhabt.
	 *
	 * @param target     das Ziel des Proxies, in diesem Fall die "Original"-Daten
	 * @param prop       die Property des Proxies, welche angesprochen wird
	 * @param newValue   der neue Wert für die Property
	 * @param receiver   das Objekt, auf welches sich der schreibende Zugriff ursprünglich bezogen hat.
	 *
	 * @returns ein Boolean-Wert, welcher angibt, ob der Wert gesetzt wurde oder nicht
	 */
	public set(target: T, prop: string | symbol, newValue: any, receiver: any): boolean {
		let oldValue = target[prop as keyof T];
		// Prüfe, ob ein pending value vorliegt und berücksichtige diesen ggf.
		const pending = this.modelProxy.pending;
		const pendingValue = pending[prop as keyof T];
		if (pendingValue !== undefined) {
			oldValue = pendingValue;
		}
		if ((typeof prop === "string") && (oldValue !== newValue)) {
			// Erstelle einen Patch
			const update = <Partial<T>>{};
			update[prop as keyof T] = newValue;
			void this.modelProxy.applyToPending(update, prop as keyof T);
		} else {
			target[prop as keyof T] = newValue;
		}
		return true;
	}

}
