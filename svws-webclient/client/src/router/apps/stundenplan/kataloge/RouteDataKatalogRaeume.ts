import type { Raum } from "@core";
import { ArrayList, UserNotificationException, DeveloperNotificationException, RaumListeManager } from "@core";

import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { RouteManager } from "~/router/RouteManager";

import { routeKatalogRaeume } from "./RouteKatalogRaeume";
import { routeApp } from "../../RouteApp";

interface RouteStateKatalogRaeume extends RouteStateInterface {
	raumListeManager: RaumListeManager;
}

const defaultState = <RouteStateKatalogRaeume> {
	raumListeManager: new RaumListeManager(-1, -1, new ArrayList(), null, new ArrayList()),
};

export class RouteDataKatalogRaeume extends RouteData<RouteStateKatalogRaeume> {

	public constructor() {
		super(defaultState);
	}

	get raumListeManager(): RaumListeManager {
		return this._state.value.raumListeManager;
	}

	public async ladeListe() {
		const listKatalogeintraege = await api.server.getRaeume(api.schema);
		const raumListeManager = new RaumListeManager(api.abschnitt.id, api.schuleStammdaten.idSchuljahresabschnitt, api.schuleStammdaten.abschnitte, api.schulform, listKatalogeintraege);
		raumListeManager.setFilterAuswahlPermitted(true);
		this.setPatchedDefaultState({ raumListeManager })
	}

	setEintrag = (raum: Raum | null) => {
		if ((raum === null) && (!this.raumListeManager.hasDaten()))
			return;
		const raumListeManager = this.raumListeManager;
		if ((raum === null) || (raumListeManager.liste.list().isEmpty())) {
			raumListeManager.setDaten(null);
			this.setPatchedState({ raumListeManager });
			return;
		}
		if ((raumListeManager.hasDaten() && (raum.id === raumListeManager.auswahl().id)))
			return;
		raumListeManager.setDaten(raum);
		this.setPatchedState({ raumListeManager });
	}

	gotoEintrag = async (eintrag: Raum) => {
		await RouteManager.doRoute({ name: routeKatalogRaeume.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt, id: eintrag.id } });
	}

	addEintrag = async (eintrag: Partial<Raum>) => {
		if ((eintrag.kuerzel === undefined) || (this.raumListeManager.getByKuerzelOrNull(eintrag.kuerzel) !== null))
			throw new UserNotificationException('Ein Raum mit diesem Kürzel existiert bereits');
		delete eintrag.id;
		const raum = await api.server.addRaum(eintrag, api.schema);
		this.raumListeManager.liste.add(raum);
		this.setPatchedState({ raumListeManager: this.raumListeManager });
		await this.gotoEintrag(raum);
	}

	deleteEintraege = async (eintraege: Iterable<Raum>) => {
		const raumListeManager = this.raumListeManager;
		const listID = new ArrayList<number>();
		for (const eintrag of eintraege)
			listID.add(eintrag.id);
		if (listID.isEmpty())
			return;
		const raeume = await api.server.deleteRaeume(listID, api.schema);
		raumListeManager.liste.removeAll(raeume);
		this.setPatchedState({ raumListeManager });
	}

	patch = async (eintrag : Partial<Raum>) => {
		const idRaum = this.raumListeManager.auswahlID();
		if (idRaum === null)
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode wurden keine gültigen Daten geladen.");
		if ((eintrag.groesse !== undefined) && (eintrag.groesse < 1))
			throw new DeveloperNotificationException("Ein Raum muss mindestens eine Größe von 1 haben.");
		const raumListeManager = this.raumListeManager;
		await api.server.patchRaum(eintrag, api.schema, idRaum);
		const alt = raumListeManager.auswahl();
		const neu = Object.assign(raumListeManager.daten(), eintrag);
		raumListeManager.liste.remove(alt);
		raumListeManager.liste.add(neu);
		this.setPatchedState({ raumListeManager });
	}

	setKatalogRaeumeImportJSON = api.call(async (formData: FormData) => {
		const jsonFile = formData.get("data");
		if (!(jsonFile instanceof File))
			return;
		const json = await jsonFile.text();
		const raeume: Partial<Raum>[] = JSON.parse(json);
		const list = new ArrayList<Partial<Raum>>();
		const raumListeManager = this.raumListeManager;
		for (const item of raeume)
			if ((item.kuerzel !== undefined) && (raumListeManager.getByKuerzelOrNull(item.kuerzel) === null)) {
				delete item.id;
				list.add(item);
			}
		if (list.isEmpty())
			return;
		const res = await api.server.addRaeume(list, api.schema);
		raumListeManager.liste.addAll(res);
		this.setPatchedState({ raumListeManager });
	})
}