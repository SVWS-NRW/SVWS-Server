import { ArrayList, StundenplanKomplett, StundenplanManager, type Raum, UserNotificationException, DeveloperNotificationException } from "@core";

import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { RouteManager } from "~/router/RouteManager";

import { routeKatalogRaeume } from "./RouteKatalogRaeume";
import { routeKatalogRaumDaten } from "./RouteKatalogRaumDaten";

interface RouteStateKatalogRaeume extends RouteStateInterface {
	auswahl: Raum | undefined;
	stundenplanManager: StundenplanManager | undefined;
}

const defaultState = <RouteStateKatalogRaeume> {
	auswahl: undefined,
	stundenplanManager: undefined,
	view: routeKatalogRaumDaten,
};

export class RouteDataKatalogRaeume extends RouteData<RouteStateKatalogRaeume> {

	public constructor() {
		super(defaultState);
	}

	get auswahl(): Raum | undefined {
		return this._state.value.auswahl;
	}

	get stundenplanManager(): StundenplanManager {
		if (this._state.value.stundenplanManager === undefined)
			throw new DeveloperNotificationException("Unerwarteter Fehler: Stundenplandaten nicht initialisiert");
		return this._state.value.stundenplanManager;
	}

	public async ladeListe() {
		const listKatalogeintraege = await api.server.getRaeume(api.schema);
		const auswahl = listKatalogeintraege.size() > 0 ? listKatalogeintraege.get(0) : undefined;
		const stundenplanKomplett = new StundenplanKomplett();
		stundenplanKomplett.daten.gueltigAb = '1999-01-01';
		stundenplanKomplett.daten.gueltigBis = '2999-01-01';
		const stundenplanManager = new StundenplanManager(stundenplanKomplett);
		stundenplanManager.raumAddAll(listKatalogeintraege);
		this.setPatchedDefaultState({ auswahl, stundenplanManager })
	}

	setEintrag = async (auswahl: Raum) => {
		this.setPatchedState({ auswahl })
	}

	gotoEintrag = async (eintrag: Raum) => {
		await RouteManager.doRoute(routeKatalogRaeume.getRoute(eintrag.id));
	}

	addEintrag = async (eintrag: Partial<Raum>) => {
		if (!eintrag.kuerzel || this.stundenplanManager.raumExistsByKuerzel(eintrag.kuerzel))
			throw new UserNotificationException('Ein Raum mit diesem Kürzel existiert bereits');
		delete eintrag.id;
		const raum = await api.server.addRaum(eintrag, api.schema);
		const stundenplanManager = this.stundenplanManager;
		stundenplanManager.raumAdd(raum);
		this.setPatchedState({stundenplanManager});
		await this.gotoEintrag(raum);
	}

	deleteEintraege = async (eintraege: Iterable<Raum>) => {
		const stundenplanManager = this.stundenplanManager;
		const listID = new ArrayList<number>();
		for (const eintrag of eintraege)
			listID.add(eintrag.id);
		if (listID.isEmpty())
			return;
		const raeume = await api.server.deleteRaeume(listID, api.schema);
		stundenplanManager.raumRemoveAll(raeume);
		const list = stundenplanManager.raumGetMengeAsList();
		const auswahl  = list.isEmpty() ? undefined : list.get(0);
		this.setPatchedState({auswahl, stundenplanManager});
	}

	patch = async (eintrag : Partial<Raum>) => {
		if (this.auswahl === undefined)
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode wurden keine gültigen Daten geladen.");
		if (eintrag.groesse && eintrag.groesse < 1)
			throw new DeveloperNotificationException("Ein Raum muss mindestens eine Größe von 1 haben.");
		await api.server.patchRaum(eintrag, api.schema, this.auswahl.id);
		const auswahl = this.auswahl;
		Object.assign(auswahl, eintrag);
		this.stundenplanManager.raumPatchAttributes(auswahl);
		this.commit();
	}

	setKatalogRaeumeImportJSON = api.call(async (formData: FormData) => {
		const jsonFile = formData.get("data");
		if (!(jsonFile instanceof File))
			return;
		const json = await jsonFile.text();
		const raeume: Partial<Raum>[] = JSON.parse(json);
		const list = new ArrayList<Partial<Raum>>();
		for (const item of raeume)
			if (item.kuerzel && !this.stundenplanManager.raumExistsByKuerzel(item.kuerzel)) {
				delete item.id;
				list.add(item);
			}
		if (list.isEmpty())
			return;
		const res = await api.server.addRaeume(list, api.schema);
		this.setPatchedState({stundenplanManager: this.stundenplanManager});
	})
}