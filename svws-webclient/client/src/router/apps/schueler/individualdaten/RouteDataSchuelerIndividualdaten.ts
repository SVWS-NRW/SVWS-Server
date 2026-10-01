import { PendingStateManagerSchuelerIndividualdaten } from "~/router/apps/schueler/individualdaten/PendingStateManagerSchuelerIndividualdaten";
import { routeSchueler } from "~/router/apps/schueler/RouteSchueler";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { useSchuelerAuswahlState } from "~/states/schueler/SchuelerAuswahlState";


interface RouteStateDataSchuelerIndividualdaten extends RouteStateInterface {
	pendingStateManager: PendingStateManagerSchuelerIndividualdaten | undefined;
}

export class RouteDataSchuelerIndividualdaten extends RouteData<RouteStateDataSchuelerIndividualdaten> {

	public constructor() {
		super({
			pendingStateManager: undefined,
		});
	}

	get pendingStateManager(): PendingStateManagerSchuelerIndividualdaten {
		const pendingStateManager = this._state.value.pendingStateManager;

		if (pendingStateManager === undefined) {
			return this.initialisierePendingStateManager();
		}

		return pendingStateManager;
	}

	private initialisierePendingStateManager(): PendingStateManagerSchuelerIndividualdaten {
		const schuelerAuswahlState = useSchuelerAuswahlState();
		const pendingStateManager =
			new PendingStateManagerSchuelerIndividualdaten(
				'id',
				() => schuelerAuswahlState.manager
			);

		this._state.value.pendingStateManager = pendingStateManager;

		routeSchueler.data.pendingStateManagerRegistry.addPendingStateManager(
			pendingStateManager
		);

		return pendingStateManager;
	}

}
