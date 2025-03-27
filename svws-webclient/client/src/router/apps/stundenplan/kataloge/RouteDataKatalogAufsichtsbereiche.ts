import type { StundenplanAufsichtsbereich} from "@core";
import { StundenplanKomplett, StundenplanManager, ArrayList, UserNotificationException, DeveloperNotificationException } from "@core";

import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";
import { RouteData, type RouteStateInterface } from "~/router/RouteData";

import { routeKatalogAufsichtsbereiche } from "./RouteKatalogAufsichtsbereiche";
import { routeStundenplan } from "../RouteStundenplan";

interface RouteStateKatalogAufsichtsbereiche extends RouteStateInterface {
	auswahl: StundenplanAufsichtsbereich | undefined;
	stundenplanManager: StundenplanManager | undefined;
}

const defaultState = <RouteStateKatalogAufsichtsbereiche> {
	auswahl: undefined,
	stundenplanManager: undefined,
};

export class RouteDataKatalogAufsichtsbereiche extends RouteData<RouteStateKatalogAufsichtsbereiche> {

	public constructor() {
		super(defaultState);
	}

	get auswahl(): StundenplanAufsichtsbereich | undefined {
		return this._state.value.auswahl;
	}

	get stundenplanManager(): StundenplanManager {
		if (this._state.value.stundenplanManager === undefined)
			throw new DeveloperNotificationException("Unerwarteter Fehler: Stundenplandaten nicht initialisiert");
		return this._state.value.stundenplanManager;
	}

	public async ladeListe() {
		const listKatalogeintraege = await api.server.getAufsichtsbereiche(api.schema);
		const auswahl = listKatalogeintraege.size() > 0 ? listKatalogeintraege.get(0) : undefined;
		const stundenplanKomplett = new StundenplanKomplett();
		stundenplanKomplett.daten.gueltigAb = '1999-01-01';
		stundenplanKomplett.daten.gueltigBis = '2999-01-01';
		const stundenplanManager = new StundenplanManager(stundenplanKomplett);
		stundenplanManager.aufsichtsbereichAddAll(listKatalogeintraege);
		this.setPatchedDefaultState({ auswahl, stundenplanManager })
	}

	setEintrag = async (auswahl: StundenplanAufsichtsbereich) => this.setPatchedState({ auswahl });

	gotoEintrag = async (eintrag: StundenplanAufsichtsbereich) => await RouteManager.doRoute(routeKatalogAufsichtsbereiche.getRoute({ idAufsichtsbereich: eintrag.id }));

	addEintrag = async (eintrag: Partial<StundenplanAufsichtsbereich>) => {
		if ((eintrag.kuerzel === undefined) || this.stundenplanManager.aufsichtsbereichExistsByKuerzel(eintrag.kuerzel))
			throw new UserNotificationException('Eine Aufsichtsbereich mit diesem Kürzel existiert bereits');
		delete eintrag.id;
		const aufsichtsbereich = await api.server.addAufsichtsbereich(eintrag, api.schema);
		const stundenplanManager = this.stundenplanManager;
		stundenplanManager.aufsichtsbereichAdd(aufsichtsbereich);
		this.setPatchedState({stundenplanManager});
		await routeStundenplan.data.reloadVorlagen();
		await this.gotoEintrag(aufsichtsbereich);
	}

	deleteEintraege = async (eintraege: Iterable<StundenplanAufsichtsbereich>) => {
		const stundenplanManager = this.stundenplanManager;
		const listID = new ArrayList<number>();
		for (const eintrag of eintraege)
			listID.add(eintrag.id);
		if (listID.isEmpty())
			return;
		const aufsichtsbereiche = await api.server.deleteAufsichtsbereiche(listID, api.schema);
		stundenplanManager.aufsichtsbereichRemoveAll(aufsichtsbereiche);
		const liste = this.stundenplanManager.aufsichtsbereichGetMengeAsList();
		const auswahl = liste.isEmpty() ? undefined : liste.get(0);
		await routeStundenplan.data.reloadVorlagen();
		this.setPatchedState({auswahl, stundenplanManager});
	}

	patch = async (eintrag : Partial<StundenplanAufsichtsbereich>) => {
		if (this.auswahl === undefined)
			throw new DeveloperNotificationException("Beim Aufruf der Patch-Methode sind keine gültigen Daten geladen.");
		await api.server.patchAufsichtsbereich(eintrag, api.schema, this.auswahl.id);
		const auswahl = this.auswahl;
		Object.assign(auswahl, eintrag);
		this.stundenplanManager.aufsichtsbereichPatchAttributes(auswahl);
		await routeStundenplan.data.reloadVorlagen();
		this.commit();
	}

}