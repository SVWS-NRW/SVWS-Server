import type { VermerkartEintrag } from "@core";
import { ArrayList, DeveloperNotificationException } from "@core";

import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { RouteManager } from "~/router/RouteManager";

import { routeKatalogVermerkartenDaten } from "./RouteKatalogVermerkartenDaten";
import { routeKatalogVermerkarten } from "./RouteKatalogVermerkarten";

interface RouteStateKatalogeVermerke extends RouteStateInterface {
	auswahl: VermerkartEintrag | undefined;
	mapKatalogeintraege: Map<number, VermerkartEintrag>;
}

const defaultState = <RouteStateKatalogeVermerke> {
	auswahl: undefined,
	mapKatalogeintraege: new Map(),
	view: routeKatalogVermerkartenDaten,
};

export class RouteDataKatalogVermerke extends RouteData<RouteStateKatalogeVermerke> {

	public constructor() {
		super(defaultState);
	}

	get auswahl(): VermerkartEintrag | undefined {
		return this._state.value.auswahl;
	}

	get mapKatalogeintraege(): Map<number, VermerkartEintrag> {
		return new Map(this._state.value.mapKatalogeintraege);
	}

	public async ladeListe() {
		const listKatalogeintraege = await api.server.getVermerkarten(api.schema);
		const mapKatalogeintraege = new Map<number, VermerkartEintrag>();
		const auswahl = listKatalogeintraege.size() > 0 ? listKatalogeintraege.get(0) : undefined;
		for (const l of listKatalogeintraege)
			mapKatalogeintraege.set(l.id, l);
		this.setPatchedDefaultState({ auswahl, mapKatalogeintraege })
	}

	setEintrag = (eintrag: VermerkartEintrag) => {
		this.setPatchedState({
			auswahl: this.mapKatalogeintraege.get(eintrag.id)
		})
	}

	gotoEintrag = async (eintrag: VermerkartEintrag) => {
		await RouteManager.doRoute(routeKatalogVermerkarten.getRoute(eintrag.id));
	}

	addEintrag = async (eintrag: Partial<VermerkartEintrag>) => {
		delete eintrag.id;
		const res = await api.server.createVermerkart(eintrag, api.schema);
		const mapKatalogeintraege = this.mapKatalogeintraege;
		mapKatalogeintraege.set(res.id, res);
		this.setPatchedState({ mapKatalogeintraege });
		await RouteManager.doRoute({ name: routeKatalogVermerkartenDaten.name, params: { id: res.id } });
	}

	patch = async (data : Partial<VermerkartEintrag>) => {
		if (this.auswahl === undefined)
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		const mapKatalogeintraege = this._state.value.mapKatalogeintraege;
		const auswahl =  this._state.value.auswahl;
		await api.server.patchVermerkart(data, api.schema, this.auswahl.id)
		if (auswahl !== undefined)
			Object.assign(auswahl, data);
		this.setPatchedState({ auswahl, mapKatalogeintraege });
	}

	deleteEintraege = async (eintraege: Iterable<VermerkartEintrag>) => {
		const mapKatalogeintraege = this.mapKatalogeintraege;
		const listID = new ArrayList<number>;
		for (const eintrag of eintraege)
			listID.add(eintrag.id);
		if (listID.isEmpty())
			return;
		const vermerke = await api.server.deleteVermerkartEintraege(listID, api.schema);
		for (const eintrag of vermerke)
			mapKatalogeintraege.delete(eintrag.id);
		let auswahl = this.auswahl;
		if (this.auswahl && mapKatalogeintraege.get(this.auswahl.id) === undefined)
			auswahl = mapKatalogeintraege.values().next().value;
		this.setPatchedState({mapKatalogeintraege, auswahl});
	}
	//
	deleteKlassenCheck = (eintraege: Iterable<VermerkartEintrag>): [boolean, ArrayList<string>] => {
			const errorLog: ArrayList<string> = new ArrayList();
			const deleteCandidates = new ArrayList<number>;

			let isEmtpy : boolean = false;

			for(const eintrag in eintraege) {
				isEmtpy = true;
			}
	
			if (isEmtpy)
 				errorLog.add('Es wurde keine Vermerkart zum Löschen ausgewählt.')
			return [errorLog.isEmpty(), errorLog]
		}
	}

	