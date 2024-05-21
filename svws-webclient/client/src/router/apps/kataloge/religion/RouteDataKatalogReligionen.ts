import type { ReligionEintrag} from "@core";
import { ReligionListeManager } from "@core";
import { ArrayList, DeveloperNotificationException } from "@core";

import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { RouteManager } from "~/router/RouteManager";

import { routeKatalogReligionDaten } from "./RouteKatalogReligionDaten";
import { routeKatalogReligion } from "./RouteKatalogReligionen";

interface RouteStateKatalogeReligionen extends RouteStateInterface {
	religionListeManager: ReligionListeManager;
}

const defaultState = <RouteStateKatalogeReligionen> {
	religionListeManager: new ReligionListeManager(-1, -1, new ArrayList(), null, new ArrayList()),
	view: routeKatalogReligionDaten,
};

export class RouteDataKatalogReligionen extends RouteData<RouteStateKatalogeReligionen> {

	public constructor() {
		super(defaultState);
	}

	get religionListeManager(): ReligionListeManager {
		return this._state.value.religionListeManager;
	}

	public async ladeListeIntern() {
		const listKatalogeintraege = await api.server.getReligionen(api.schema);
		// Bestimme den Eintrag von vorher, um ggf. eine neue ID für das Routing zurückzugeben
		const hatteAuswahl = (this.religionListeManager.auswahlID() !== null) ? this.religionListeManager.auswahl() : null;
		const religionListeManager = new ReligionListeManager(-1, api.schuleStammdaten.idSchuljahresabschnitt, api.schuleStammdaten.abschnitte,	api.schulform, listKatalogeintraege);
		// Wählen nun einen Eintrag aus, dabei wird sich ggf. an der alten Auswahl orientiert
		if (hatteAuswahl && (hatteAuswahl.kuerzel !== null)) {
			let auswahl = religionListeManager.getByKuerzelOrNull(hatteAuswahl.kuerzel);
			if ((auswahl === null) && (religionListeManager.liste.size() > 0))
				auswahl = religionListeManager.liste.list().get(0);
			if (auswahl !== null)
				religionListeManager.setDaten(auswahl);
		} else {
			if (religionListeManager.liste.size() > 0) {
				const auswahl = religionListeManager.liste.list().get(0);
				religionListeManager.setDaten(auswahl);
			}
		}
		return { religionListeManager };
	}

	public async ladeListe() : Promise<number | null> {
		const o = await this.ladeListeIntern();
		this.setPatchedDefaultState(o);
		return o.religionListeManager.auswahlID();
	}

	setEintrag = (eintrag: ReligionEintrag) => {
		const religionListeManager = this.religionListeManager;
		religionListeManager.setDaten(eintrag);
		this.setPatchedState({ religionListeManager });
	}

	gotoEintrag = async (eintrag: ReligionEintrag) => {
		await RouteManager.doRoute(routeKatalogReligion.getRoute(eintrag.id));
	}

	addEintrag = async (eintrag: Partial<ReligionEintrag>) => {
		delete eintrag.id;
		const res = await api.server.createReligion(eintrag, api.schema);
		const religionListeManager = this.religionListeManager;
		religionListeManager.liste.auswahlAdd(res);
		this.setPatchedState({ religionListeManager });
		await RouteManager.doRoute({ name: routeKatalogReligionDaten.name, params: { id: res.id } });
	}

	patch = async (data : Partial<ReligionEintrag>) => {
		const id = this.religionListeManager.auswahlID();
		if (id === null)
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		await api.server.patchReligion(data, api.schema, id);
		const auswahl = this.religionListeManager.auswahl();
		Object.assign(auswahl, data);
		this.religionListeManager.setDaten(auswahl);
		this.setPatchedState({ religionListeManager: this.religionListeManager });
	}

	deleteEintraege = async (eintraege: Iterable<ReligionEintrag>) => {
		const listID = new ArrayList<number>;
		for (const eintrag of eintraege)
			listID.add(eintrag.id);
		if (listID.isEmpty())
			return;
		const religionen = await api.server.deleteReligionEintraege(listID, api.schema);
		const religionListeManager = this.religionListeManager;
		for (const eintrag of religionen)
			religionListeManager.liste.auswahlRemoveByKey(eintrag.id);
		const id = religionListeManager.auswahlID();
		if (id !== null && !religionListeManager.liste.auswahlHasKey(id))
			await this.setFilter();
		this.setPatchedState({religionListeManager});
	}

	setFilter = async () => {
		if (!this.religionListeManager.hasDaten()) {
			const listFiltered = this.religionListeManager.filtered();
			if (!listFiltered.isEmpty()) {
				await this.gotoEintrag(listFiltered.get(0));
				return;
			}
		}
		const religionListeManager = this.religionListeManager;
		this.setPatchedState({ religionListeManager });
	}

}