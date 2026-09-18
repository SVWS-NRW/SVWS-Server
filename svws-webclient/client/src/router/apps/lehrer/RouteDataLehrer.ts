import { routeLehrerIndividualdaten } from "~/router/apps/lehrer/individualdaten/RouteLehrerIndividualdaten";
import { PendingStateManagerRegistry } from "~/router/PendingStateManagerRegistry";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { useLehrerAuswahlState } from "~/states/lehrer/LehrerAuswahlState";

import { PendingStateManagerLehrerIndividualdaten } from "./individualdaten/PendingStateManagerLehrerIndividualdaten";
import { routeLehrer } from "./RouteLehrer";


interface RouteStateDataLehrer extends RouteStateInterface {
	pendingStateManager: PendingStateManagerLehrerIndividualdaten | undefined;
}

export class RouteDataLehrer extends RouteData<RouteStateDataLehrer> {

	private readonly _pendingStateManagerRegistry: PendingStateManagerRegistry;

	public constructor() {
		super({
			pendingStateManager: undefined,
			view: routeLehrerIndividualdaten,
		});
		this._pendingStateManagerRegistry = new PendingStateManagerRegistry();
	}

	get pendingStateManagerRegistry(): PendingStateManagerRegistry {
		return this._pendingStateManagerRegistry;
	}

	get pendingStateManager(): PendingStateManagerLehrerIndividualdaten {
		if (this._state.value.pendingStateManager === undefined) {
			const lehrerAuswahlState = useLehrerAuswahlState();
			this._state.value.pendingStateManager = new PendingStateManagerLehrerIndividualdaten('id',
				() => lehrerAuswahlState.manager);
			routeLehrer.data.pendingStateManagerRegistry.addPendingStateManager(this._state.value.pendingStateManager);
		}
		return this._state.value.pendingStateManager;
	}

}

