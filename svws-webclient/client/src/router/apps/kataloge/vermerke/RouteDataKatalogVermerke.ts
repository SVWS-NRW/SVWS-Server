import type {VermerkartEintrag, List} from "@core";
import { ArrayList, DeveloperNotificationException } from "@core";

import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { RouteManager } from "~/router/RouteManager";

import { VermerkartenManager } from "@core";

import { routeKatalogVermerkartenDaten } from "./RouteKatalogVermerkartenDaten";
import { routeKatalogVermerkarten } from "./RouteKatalogVermerkarten";

interface RouteStateKatalogeVermerke extends RouteStateInterface {
	vermerkartenManager: VermerkartenManager;
}

const defaultState = <RouteStateKatalogeVermerke> {
	auswahl: undefined,
	view: routeKatalogVermerkartenDaten,
	vermerkartenManager: new VermerkartenManager(new ArrayList(), new ArrayList())
};


export class RouteDataKatalogVermerke extends RouteData<RouteStateKatalogeVermerke> {

	public constructor() {
		super(defaultState);
	}

	get vermerkartenManager(): VermerkartenManager {
		return this._state.value.vermerkartenManager
	}

	public async ladeListe() {
		const listKatalogeintraege = await api.server.getVermerkarten(api.schema);
		const auswahl = listKatalogeintraege.size() > 0 ? listKatalogeintraege.get(0) : undefined;

		if (auswahl !== undefined)
			await this.initManager(auswahl, listKatalogeintraege);

	}

	initManager = async (auswahl: VermerkartEintrag, listKatalogeintraege: List<VermerkartEintrag>) => {
		const listSchuelerVermerkartZusammenfassung = await api.server.getSchuelerByVermerkartID(api.schema, auswahl.id);

		const vermerkartenManager : VermerkartenManager = new VermerkartenManager(listKatalogeintraege, listSchuelerVermerkartZusammenfassung);
		vermerkartenManager.setDaten(auswahl ?? vermerkartenManager.liste.list().get(0));
		this.setPatchedDefaultState({ vermerkartenManager})
	}

	setEintrag = async (eintrag: VermerkartEintrag | null) => {
		if ((eintrag === null) && (!this.vermerkartenManager.hasDaten()))
			return;


		if ((eintrag === null) || (this.vermerkartenManager.liste.list().isEmpty())) {
			this.vermerkartenManager.setDaten(null);
			this.commit();
			return;
		}

		if (this.vermerkartenManager.hasDaten() && (eintrag.id === this.vermerkartenManager.auswahl().id))
			return;


		let neueAuswahl = this.vermerkartenManager.liste.get(eintrag.id);

		if (neueAuswahl === null){
			neueAuswahl = this.vermerkartenManager.filtered().get(0);
		}

		const listSchuelerVermerkartZusammenfassung = await api.server.getSchuelerByVermerkartID(api.schema, neueAuswahl.id);
		this.vermerkartenManager.setListSchuelerVermerkartZusammenfassung(listSchuelerVermerkartZusammenfassung)
		this.vermerkartenManager.setDaten(neueAuswahl);

		this.commit();
	}

	gotoEintrag = async (eintrag: VermerkartEintrag) => {
		if (this._state.value.vermerkartenManager.liste.has(eintrag.id)) {
			await RouteManager.doRoute(routeKatalogVermerkarten.getRoute(eintrag.id));
			await this.setEintrag(this._state.value.vermerkartenManager.liste.get(eintrag.id))
		}
	}

	enforceCommit = async () => {
		this.commit();
	}


	addEintrag = async (eintrag: Partial<VermerkartEintrag>) => {
		delete eintrag.id;
		const res = await api.server.createVermerkart(eintrag, api.schema);
		const listKatalogeintraege = await api.server.getVermerkarten(api.schema);
		await this.initManager(this.vermerkartenManager.auswahl(), listKatalogeintraege)
		await RouteManager.doRoute({ name: routeKatalogVermerkartenDaten.name, params: { id: res.id } });
	}

	patch = async (data : Partial<VermerkartEintrag>) => {

		const idVermerk = this._state.value.vermerkartenManager.auswahlID()

		if (idVermerk === null) {
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		}

		const daten = this.vermerkartenManager.daten();

		await api.server.patchVermerkart(data, api.schema, idVermerk);
		Object.assign(daten, data);

		const vermerkArten = this.vermerkartenManager.liste.list();

		vermerkArten.remove(this.vermerkartenManager.auswahl());
		vermerkArten.add(daten);
		await this.initManager(this.vermerkartenManager.auswahl(), vermerkArten);
		this.commit();
	}

	deleteEintraege = async (eintraege: Iterable<VermerkartEintrag>) => {
		const listID = new ArrayList<number>;

		for (const eintrag of eintraege)
			listID.add(eintrag.id);

		if (listID.isEmpty())
			return;

		const _ = await api.server.deleteVermerkartEintraege(listID, api.schema);
		const listKatalogeintraege = await api.server.getVermerkarten(api.schema);

		const neueAuswahl = listID.contains(this._state.value.vermerkartenManager.auswahl().id) ? this.vermerkartenManager.filtered().get(0) : this._state.value.vermerkartenManager.auswahl();

		await this.initManager(neueAuswahl, listKatalogeintraege);
	}

}
