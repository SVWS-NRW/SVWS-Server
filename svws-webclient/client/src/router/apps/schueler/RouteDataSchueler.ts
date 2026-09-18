import { routeSchuelerIndividualdaten } from "~/router/apps/schueler/individualdaten/RouteSchuelerIndividualdaten";
import { PendingStateManagerRegistry } from "~/router/PendingStateManagerRegistry";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";

const defaultState = <RouteStateInterface> {
	view: routeSchuelerIndividualdaten,
};

export class RouteDataSchueler extends RouteData<RouteStateInterface> {

	private readonly _pendingStateManagerRegistry: PendingStateManagerRegistry;

	public constructor() {
		super(defaultState);
		this._pendingStateManagerRegistry = new PendingStateManagerRegistry();
	}

	get pendingStateManagerRegistry(): PendingStateManagerRegistry {
		return this._pendingStateManagerRegistry;
	}

}
