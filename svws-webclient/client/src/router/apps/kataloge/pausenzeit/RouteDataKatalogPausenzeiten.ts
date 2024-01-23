import { ArrayList, StundenplanKomplett, StundenplanManager, UserNotificationException, type StundenplanPausenzeit, DeveloperNotificationException } from "@core";

import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { RouteManager } from "~/router/RouteManager";

import { routeKatalogPausenzeitDaten } from "./RouteKatalogPausenzeitDaten";
import { routeKatalogPausenzeiten } from "./RouteKatalogPausenzeiten";

interface RouteStateKatalogPausenzeiten extends RouteStateInterface {
	auswahl: StundenplanPausenzeit | undefined;
	daten: StundenplanPausenzeit | undefined;
	mapKatalogeintraege: Map<number, StundenplanPausenzeit>;
	stundenplanManager: StundenplanManager | undefined;
}

const defaultState = <RouteStateKatalogPausenzeiten> {
	auswahl: undefined,
	daten: undefined,
	mapKatalogeintraege: new Map(),
	stundenplanManager: undefined,
	view: routeKatalogPausenzeitDaten,
};

export class RouteDataKatalogPausenzeiten extends RouteData<RouteStateKatalogPausenzeiten> {

	public constructor() {
		super(defaultState);
	}

	get auswahl(): StundenplanPausenzeit | undefined {
		return this._state.value.auswahl;
	}

	get mapKatalogeintraege(): Map<number, StundenplanPausenzeit> {
		return new Map(this._state.value.mapKatalogeintraege);
	}

	get stundenplanManager(): StundenplanManager {
		if (this._state.value.stundenplanManager === undefined)
			throw new Error("Unerwarteter Fehler: Stundenplandaten nicht initialisiert");
		return this._state.value.stundenplanManager;
	}

	get daten(): StundenplanPausenzeit {
		if (this._state.value.daten === undefined)
			throw new Error("Unerwarteter Fehler: Pausenzeitdaten nicht initialisiert");
		return this._state.value.daten;
	}

	public async ladeListe() {
		const listKatalogeintraege = await api.server.getPausenzeiten(api.schema);
		const mapKatalogeintraege = new Map<number, StundenplanPausenzeit>();
		const auswahl = listKatalogeintraege.size() > 0 ? listKatalogeintraege.get(0) : undefined;
		const stundenplanKomplett = new StundenplanKomplett();
		stundenplanKomplett.daten.gueltigAb = '1999-01-01';
		stundenplanKomplett.daten.gueltigBis = '2999-01-01';
		const stundenplanManager = new StundenplanManager(stundenplanKomplett);
		stundenplanManager.pausenzeitAddAll(listKatalogeintraege);
		for (const l of listKatalogeintraege)
			mapKatalogeintraege.set(l.id, l);
		this.setPatchedDefaultState({ auswahl, mapKatalogeintraege, stundenplanManager })
	}

	setEintrag = async (auswahl: StundenplanPausenzeit) => {
		const daten = this.mapKatalogeintraege.get(auswahl.id);
		this.setPatchedState({ auswahl, daten })
	}

	gotoEintrag = async (eintrag: StundenplanPausenzeit) => {
		await RouteManager.doRoute(routeKatalogPausenzeiten.getRoute(eintrag.id));
	}

	addEintrag = async (eintrag: Partial<StundenplanPausenzeit>) => {
		if (!eintrag.wochentag || !eintrag.beginn || !eintrag.ende || this.stundenplanManager.pausenzeitExistsByWochentagAndBeginnAndEnde(eintrag.wochentag, eintrag.beginn, eintrag.ende))
			throw new UserNotificationException('Eine Pausenzeit existiert bereits an diesem Tag und zu dieser Zeit');
		delete eintrag.id;
		const pausenzeit = await api.server.addPausenzeit(eintrag, api.schema);
		const stundenplanManager = this.stundenplanManager;
		const mapKatalogeintraege = this.mapKatalogeintraege;
		stundenplanManager.pausenzeitAdd(pausenzeit);
		mapKatalogeintraege.set(pausenzeit.id, pausenzeit);
		this.setPatchedState({mapKatalogeintraege, stundenplanManager});
		await this.gotoEintrag(pausenzeit);
	}

	deleteEintraege = async (eintraege: Iterable<StundenplanPausenzeit>) => {
		const mapKatalogeintraege = this.mapKatalogeintraege;
		const stundenplanManager = this.stundenplanManager;
		const list = new ArrayList<number>();
		for (const eintrag of eintraege) {
			list.add(eintrag.id)
			mapKatalogeintraege.delete(eintrag.id);
		}
		const pausenzeiten = await api.server.deletePausenzeiten(list, api.schema);
		stundenplanManager.pausenzeitRemoveAll(pausenzeiten);
		let auswahl;
		if (this.auswahl && mapKatalogeintraege.get(this.auswahl.id) === undefined)
			auswahl = mapKatalogeintraege.values().next().value;
		this.setPatchedState({mapKatalogeintraege, auswahl, stundenplanManager});
	}

	patch = async (eintrag : Partial<StundenplanPausenzeit>) => {
		if (this.auswahl === undefined)
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		await api.server.patchPausenzeit(eintrag, api.schema, this.auswahl.id);
		const auswahl = this.auswahl;
		Object.assign(auswahl, eintrag);
		this.stundenplanManager.pausenzeitPatchAttributes(auswahl);
		this.commit();
	}
}