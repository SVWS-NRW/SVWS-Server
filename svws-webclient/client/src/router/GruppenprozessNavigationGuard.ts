import { ViewType } from "@ui/ui/nav/ViewType";

import type { NavigationGuard } from "~/router/NavigationGuard";
import type { RouteNode } from "~/router/RouteNode";
import type { GenericGruppenprozessState } from "~/states/GenericGruppenprozessState";

/**
 * Der Guard für die Gruppenprozesse einer Auswahl: Liegen in einem Bereich der Gruppenprozesse ausstehende Änderungen
 * vor, so wird beim Verlassen nachgefragt, ob diese verworfen oder gespeichert werden sollen. Ein Wechsel zu einer
 * anderen Gruppenprozess-Route (Tab) der gleichen Auswahl wird nicht unterbrochen, die Änderungen bleiben dann erhalten.
 * Ein Wechsel auf den gleichen Knoten (z.B. Wechsel des Schuljahresabschnitts) wird dagegen unterbrochen.
 */
export class GruppenprozessNavigationGuard implements NavigationGuard {

	/** Die Route der Auswahl, zu der die Gruppenprozess-Routen als Kinder gehören */
	private readonly _auswahlRoute: RouteNode<any, any>;

	/** Der Orchestrator der Gruppenprozesse dieser Auswahl */
	private readonly _gruppenprozesse: GenericGruppenprozessState<any>;


	/**
	 * Erzeugt einen neuen Guard.
	 *
	 * @param auswahlRoute      die Route der Auswahl, zu der die Gruppenprozess-Routen gehören
	 * @param gruppenprozesse   der Orchestrator der Gruppenprozesse
	 */
	public constructor(auswahlRoute: RouteNode<any, any>, gruppenprozesse: GenericGruppenprozessState<any>) {
		this._auswahlRoute = auswahlRoute;
		this._gruppenprozesse = gruppenprozesse;
	}

	public isBlocked(to: RouteNode<any, any>, from: RouteNode<any, any>): boolean {
		if (!this._gruppenprozesse.hasPending) {
			return false;
		}
		const wechselDerTabs = (to.name !== from.name) && to.hasType(ViewType.GRUPPENPROZESSE) && (to.parent === this._auswahlRoute);
		return !wechselDerTabs;
	}

	public async queryConfirmation(to: RouteNode<any, any>): Promise<boolean> {
		const reply = await this._gruppenprozesse.query.query({
			bereiche: this._gruppenprozesse.bereicheMitPending,
			isPatchAllowed: this._gruppenprozesse.isPatchAllowed,
			patchHinweise: this._gruppenprozesse.patchHinweise,
		});
		if (reply === "discard") {
			this._gruppenprozesse.clearPending();
			return true;
		}
		if (reply === "save") {
			// Bei gesperrtem Speichern wird das Speichern nicht angeboten, hier wird es zur Sicherheit ebenfalls verhindert
			if (!this._gruppenprozesse.isPatchAllowed) {
				return false;
			}
			await this._gruppenprozesse.patchMultiple();
			return true;
		}
		return false;
	}

}
