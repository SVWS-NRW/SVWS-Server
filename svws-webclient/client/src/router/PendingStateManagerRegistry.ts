import { ArrayList, type List } from "@core";
import type { PendingStateManager } from "@ui";

/**
 * Diese Klasse verwaltet eine Liste von {@link PendingStateManager} Objekten, um ausstehende Änderungen aus den Gruppenprozessen übergreifend tracken zu können.
 */
export class PendingStateManagerRegistry {

	/** Eine Liste, um Instanzen von {@link PendingStateManager} zu halten */
	protected pendingStateManagers: List<PendingStateManager<any>> = new ArrayList()

	/**
	 * Prüft, ob es mindestens einen PendingState gibt der Pending Values bzw. ausstehende Änderungen hat
	 *
	 * @return Liefert <code>true</code> wenn es ausstehende Änderungen gibt, ansonsten <code>false</code>
	 */
	public pendingStateExists(): boolean {
		for (const ps of this.pendingStateManagers)
			if (ps.pendingStateExists())
				return true;
		return false;
	}

	/**
	 * Entfernt alle {@link PendingStateManager} Objekte im Register.
	 */
	public removeAllPendingStateManager(): void {
		this.pendingStateManagers.clear();
	}

	/**
	 * Fügt ein {@link PendingStateManager} Objekt zum Register hinzu.
	 */
	public addPendingStateManager(pendingStateManager: PendingStateManager<any>): void {
		this.pendingStateManagers.add(pendingStateManager)
	}

}
