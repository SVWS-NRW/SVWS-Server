import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { routeSchueler } from "~/router/apps/schueler/RouteSchueler";
import { PendingStateManagerSchuelerIndividualdaten } from "~/router/apps/schueler/individualdaten/PendingStateManagerSchuelerIndividualdaten";
import { routeApp } from "~/router/apps/RouteApp";
import { schuleStateImpl } from "~/states/SchuleStateImpl";
import type { SchulformKatalogEintrag } from "@core/asd/data/schule/SchulformKatalogEintrag";
import { Schulform } from "@core/asd/types/schule/Schulform";
import type { SchulEintrag } from "@core/core/data/kataloge/SchulEintrag";


interface RouteStateDataSchuelerIndividualdaten extends RouteStateInterface {
	pendingStateManager: PendingStateManagerSchuelerIndividualdaten | undefined;
	mapSchulen: Map<string, SchulEintrag>;
}

export class RouteDataSchuelerIndividualdaten extends RouteData<RouteStateDataSchuelerIndividualdaten> {

	public constructor() {
		super({
			pendingStateManager: undefined,
			mapSchulen: new Map<string, SchulEintrag>(),
		});
	}

	get pendingStateManager(): PendingStateManagerSchuelerIndividualdaten {
		if (this._state.value.pendingStateManager === undefined) {
			this._state.value.pendingStateManager = new PendingStateManagerSchuelerIndividualdaten('id',
				() => routeSchueler.data.manager, this._state.value.mapSchulen);
			routeSchueler.data.pendingStateManagerRegistry.addPendingStateManager(this._state.value.pendingStateManager);
		}
		return this._state.value.pendingStateManager;
	}


	public async ladeListe() {
		const schulen = routeApp.cache.kataloge.schulenById.values();

		// Ermittle den Katalog der Schulen, welche ein Kürzel haben und als Stammschulen für Schüler in Frage kommen
		const mapSchulen = new Map<string, SchulEintrag>();
		for (const schule of schulen) {
			if (schule.schulnummerStatistik === null) {
				continue;
			}
			const sfEintrag: SchulformKatalogEintrag | null = schule.idSchulform === null ? null : Schulform.data().getEintragByID(schule.idSchulform);
			const sf: Schulform | null = sfEintrag === null ? null : Schulform.data().getWertBySchluessel(sfEintrag.schluessel);
			if (sf === schuleStateImpl.schulform) {
				mapSchulen.set(schule.schulnummerStatistik, schule);
			}
		}
		this.setPatchedDefaultState({ mapSchulen });
	}

	get mapSchulen(): Map<string, SchulEintrag> {
		return this._state.value.mapSchulen;
	}

}
