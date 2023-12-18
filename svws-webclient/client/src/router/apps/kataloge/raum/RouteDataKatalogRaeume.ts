import { ArrayList, StundenplanKomplett, StundenplanManager, type Raum, UserNotificationException, DeveloperNotificationException } from "@core";

import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { RouteManager } from "~/router/RouteManager";

import { routeKatalogRaeume } from "./RouteKatalogRaeume";
import { routeKatalogRaumDaten } from "./RouteKatalogRaumDaten";

interface RouteStateKatalogRaeume extends RouteStateInterface {
	auswahl: Raum | undefined;
	daten: Raum | undefined;
	mapKatalogeintraege: Map<number, Raum>;
	stundenplanManager: StundenplanManager | undefined;
}

const defaultState = <RouteStateKatalogRaeume> {
	auswahl: undefined,
	daten: undefined,
	mapKatalogeintraege: new Map(),
	stundenplanManager: undefined,
	view: routeKatalogRaumDaten,
};

export class RouteDataKatalogRaeume extends RouteData<RouteStateKatalogRaeume> {

	private stundenplanKomplett = new StundenplanKomplett();

	public constructor() {
		super(defaultState);
	}

	get auswahl(): Raum | undefined {
		return this._state.value.auswahl;
	}

	get mapKatalogeintraege(): Map<number, Raum> {
		return new Map(this._state.value.mapKatalogeintraege);
	}

	get stundenplanManager(): StundenplanManager {
		if (this._state.value.stundenplanManager === undefined)
			throw new Error("Unerwarteter Fehler: Stundenplandaten nicht initialisiert");
		return this._state.value.stundenplanManager;
	}

	get daten(): Raum {
		if (this._state.value.daten === undefined)
			throw new Error("Unerwarteter Fehler: Raumdaten nicht initialisiert");
		return this._state.value.daten;
	}

	public async ladeListe() {
		const listKatalogeintraege = await api.server.getRaeume(api.schema);
		const mapKatalogeintraege = new Map<number, Raum>();
		const auswahl = listKatalogeintraege.size() > 0 ? listKatalogeintraege.get(0) : undefined;
		const stundenplanKomplett = new StundenplanKomplett();
		stundenplanKomplett.daten.gueltigAb = '1999-01-01';
		stundenplanKomplett.daten.gueltigBis = '2999-01-01';
		const stundenplanManager = new StundenplanManager(stundenplanKomplett);
		stundenplanManager.raumAddAll(listKatalogeintraege);
		for (const l of listKatalogeintraege)
			mapKatalogeintraege.set(l.id, l);
		this.setPatchedDefaultState({ auswahl, mapKatalogeintraege, stundenplanManager })
	}

	setEintrag = async (auswahl: Raum) => {
		const daten = this.mapKatalogeintraege.get(auswahl.id);
		this.setPatchedState({ auswahl, daten })
	}

	gotoEintrag = async (eintrag: Raum) => {
		await RouteManager.doRoute(routeKatalogRaeume.getRoute(eintrag.id));
	}

	addEintrag = async (eintrag: Partial<Raum>) => {
		if (!eintrag.kuerzel || this.stundenplanManager.raumExistsByKuerzel(eintrag.kuerzel))
			throw new UserNotificationException('Ein Raum mit diesem Kürzel existiert bereits');
		delete eintrag.id;
		const raum = await api.server.addRaum(eintrag, api.schema);
		this.stundenplanManager.raumAdd(raum);
		const mapKatalogeintraege = this.mapKatalogeintraege;
		mapKatalogeintraege.set(raum.id, raum);
		this.setPatchedState({mapKatalogeintraege});
		await this.gotoEintrag(raum);
	}

	deleteEintraege = async (eintraege: Raum[]) => {
		const mapKatalogeintraege = this.mapKatalogeintraege;
		const listID = new ArrayList<number>;
		for (const eintrag of eintraege) {
			listID.add(eintrag.id);
			mapKatalogeintraege.delete(eintrag.id);
		}
		let auswahl;
		const raeume = await api.server.deleteRaeume(listID, api.schema);
		if (this.auswahl && mapKatalogeintraege.get(this.auswahl.id) === undefined)
			auswahl = mapKatalogeintraege.values().next().value;
		this.setPatchedState({mapKatalogeintraege, auswahl});
	}

	patch = async (eintrag : Partial<Raum>) => {
		if (this.auswahl === undefined)
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		if (eintrag.groesse && eintrag.groesse < 1)
			throw new DeveloperNotificationException("Ein Raum muss mindestens eine Größe von 1 haben.");
		await api.server.patchRaum(eintrag, api.schema, this.auswahl.id);
		const auswahl = this.auswahl;
		Object.assign(auswahl, eintrag);
		this.stundenplanManager.raumPatchAttributes(auswahl);
		this.commit();
	}
}