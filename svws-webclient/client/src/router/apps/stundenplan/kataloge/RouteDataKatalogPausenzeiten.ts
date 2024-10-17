import { ArrayList, StundenplanKomplett, StundenplanManager, UserNotificationException, StundenplanPausenzeit, DeveloperNotificationException } from "@core";

import { api } from "~/router/Api";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";
import { RouteManager } from "~/router/RouteManager";

import { routeKatalogPausenzeiten } from "./RouteKatalogPausenzeiten";
import { routeStundenplan } from "../RouteStundenplan";

interface RouteStateKatalogPausenzeiten extends RouteStateInterface {
	auswahl: StundenplanPausenzeit | undefined;
	stundenplanManager: StundenplanManager | undefined;
}

const defaultState = <RouteStateKatalogPausenzeiten> {
	auswahl: undefined,
	stundenplanManager: undefined,
};

export class RouteDataKatalogPausenzeiten extends RouteData<RouteStateKatalogPausenzeiten> {

	public constructor() {
		super(defaultState);
	}

	get auswahl(): StundenplanPausenzeit | undefined {
		return this._state.value.auswahl;
	}

	get stundenplanManager(): StundenplanManager {
		if (this._state.value.stundenplanManager === undefined)
			throw new DeveloperNotificationException("Unerwarteter Fehler: Stundenplandaten nicht initialisiert");
		return this._state.value.stundenplanManager;
	}

	public async ladeListe() {
		const listKatalogeintraege = await api.server.getPausenzeiten(api.schema);
		const listZeitraster = await api.server.getZeitraster(api.schema);
		const auswahl = listKatalogeintraege.size() > 0 ? listKatalogeintraege.get(0) : undefined;
		const stundenplanKomplett = new StundenplanKomplett();
		stundenplanKomplett.daten.gueltigAb = '1999-01-01';
		stundenplanKomplett.daten.gueltigBis = '2999-01-01';
		const stundenplanManager = new StundenplanManager(stundenplanKomplett);
		stundenplanManager.pausenzeitAddAll(listKatalogeintraege);
		stundenplanManager.zeitrasterAddAll(listZeitraster);
		const defaults = routeStundenplan.data.settingsDefaults;
		stundenplanManager.stundenplanKonfigSet(defaults);
		this.setPatchedDefaultState({ auswahl, stundenplanManager })
	}

	setEintrag = async (auswahl: StundenplanPausenzeit | undefined) => this.setPatchedState({ auswahl });

	gotoEintrag = async (eintrag: StundenplanPausenzeit) => await RouteManager.doRoute(routeKatalogPausenzeiten.getRoute(eintrag.id));

	addPausenzeiten = async (eintraege: Iterable<Partial<StundenplanPausenzeit>>) => {
		const list = new ArrayList<Partial<StundenplanPausenzeit>>();
		for (const eintrag of eintraege) {
			if ((eintrag.wochentag === undefined) || (eintrag.beginn === undefined) || (eintrag.ende === undefined) || this.stundenplanManager.pausenzeitExistsByWochentagAndBeginnAndEnde(eintrag.wochentag, eintrag.beginn, eintrag.ende))
				throw new UserNotificationException('Eine Pausenzeit existiert bereits an diesem Tag und zu dieser Zeit');
			delete eintrag.id;
			delete eintrag.klassen;
			list.add(eintrag);
		}
		if (list.isEmpty())
			return;
		const pausenzeiten = await api.server.addPausenzeiten(list, api.schema);
		const stundenplanManager = this.stundenplanManager;
		stundenplanManager.pausenzeitAddAll(pausenzeiten);
		this.setPatchedState({stundenplanManager});
		await this.gotoEintrag(pausenzeiten.get(0));
	}

	deleteEintraege = async (eintraege: Iterable<StundenplanPausenzeit>) => {
		const stundenplanManager = this.stundenplanManager;
		const listID = new ArrayList<number>();
		for (const eintrag of eintraege)
			listID.add(eintrag.id);
		if (listID.isEmpty())
			return;
		const pausenzeiten = await api.server.deletePausenzeiten(listID, api.schema);
		stundenplanManager.pausenzeitRemoveAll(pausenzeiten);
		const list = stundenplanManager.pausenzeitGetMengeAsList();
		const auswahl = list.isEmpty() ? undefined : list.get(0);
		this.setPatchedState({auswahl, stundenplanManager});
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

	setKatalogRaeumeImportJSON = api.call(async (formData: FormData) => {
		const jsonFile = formData.get("data");
		if (!(jsonFile instanceof File))
			return;
		const json = await jsonFile.text();
		const pausenzeiten: Partial<StundenplanPausenzeit>[] = JSON.parse(json);
		const list = new ArrayList<Partial<StundenplanPausenzeit>>();
		for (const item of pausenzeiten)
			if ((item.wochentag !== undefined) && (item.beginn !== undefined) && (item.ende !== undefined) && !this.stundenplanManager.pausenzeitExistsByWochentagAndBeginnAndEnde(item.wochentag, item.beginn, item.ende)) {
				// Muss nach JSON umgewandelt werden und zurück nach Pausenzeit, weil das reguläre JSON.parse ein Array als Array einliest.
				const p = JSON.stringify(item);
				const pp: Partial<StundenplanPausenzeit> = StundenplanPausenzeit.transpilerFromJSON(p);
				delete pp.id;
				delete pp.klassen;
				list.add(pp);
			}
		if (list.isEmpty())
			return;
		const res = await api.server.addPausenzeiten(list, api.schema);
		this.stundenplanManager.pausenzeitAddAll(res);
		this.setPatchedState({stundenplanManager: this.stundenplanManager});
	})

}