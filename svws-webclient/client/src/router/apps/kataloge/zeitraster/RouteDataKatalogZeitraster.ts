import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";
import type { ZeitrasterAuswahlProps } from "~/components/kataloge/zeitraster/SZeitrasterAuswahlProps";
import type { ZeitrasterAppProps } from "~/components/kataloge/zeitraster/SZeitrasterAppProps";
import type { AuswahlChildData } from "~/components/AuswahlChildData";
import type { RouteApp } from "~/router/apps/RouteApp";
import type { List} from "@core";


import { shallowRef } from "vue";
import { StundenplanManager, StundenplanZeitraster, Wochentag, ArrayList, BenutzerKompetenz, Schulform, ServerMode, Stundenplan } from "@core";

import { api } from "~/router/Api";
import { RouteManager } from "~/router/RouteManager";
import { RouteNode } from "~/router/RouteNode";

import { routeApp } from "~/router/apps/RouteApp";
import { routeKataloge } from "~/router/apps/kataloge/RouteKataloge";
import { routeKatalogZeitrasterDaten } from "~/router/apps/kataloge/zeitraster/RouteKatalogZeitrasterDaten";


interface RouteStateKatalogZeitraster {
	listKatalogeintraege: List<StundenplanZeitraster>;
	stundenplanManager: StundenplanManager | undefined;
	view: RouteNode<any, any>;
}

export class RouteDataKatalogZeitraster {

	private static _defaultState: RouteStateKatalogZeitraster = {
		listKatalogeintraege: new ArrayList(),
		stundenplanManager: undefined,
		view: routeKatalogZeitrasterDaten,
	}
	private _state = shallowRef(RouteDataKatalogZeitraster._defaultState);

	private setPatchedDefaultState(patch: Partial<RouteStateKatalogZeitraster>) {
		this._state.value = Object.assign({ ... RouteDataKatalogZeitraster._defaultState }, patch);
	}

	private setPatchedState(patch: Partial<RouteStateKatalogZeitraster>) {
		this._state.value = Object.assign({ ... this._state.value }, patch);
	}

	private commit(): void {
		this._state.value = { ... this._state.value };
	}

	public async setView(view: RouteNode<any,any>) {
		if (routeKatalogZeitraster.children.includes(view))
			this.setPatchedState({ view: view });
		else
			throw new Error("Diese für die Räume gewählte Ansicht wird nicht unterstützt.");
	}

	public get view(): RouteNode<any,any> {
		return this._state.value.view;
	}

	get stundenplanManager(): StundenplanManager {
		if (this._state.value.stundenplanManager === undefined)
			throw new Error("Unerwarteter Fehler: Stundenplandaten nicht initialisiert");
		return this._state.value.stundenplanManager;
	}
	public async ladeListe() {
		const listZeitraster = await api.server.getZeitraster(api.schema);
		const stundenplan = new Stundenplan();
		stundenplan.zeitraster = listZeitraster;
		stundenplan.gueltigAb = "1979-07-10";
		stundenplan.gueltigBis = "2050-09-29";
		const stundenplanManager = new StundenplanManager(stundenplan, new ArrayList(), new ArrayList(), null);
		this.setPatchedDefaultState({ stundenplanManager });
	}

	patchZeitraster = async (daten: Partial<StundenplanZeitraster>, zeitraster: StundenplanZeitraster) => {
		await api.server.patchZeitrasterEintrag(daten, api.schema, zeitraster.id);
		this.commit();
	}

	addZeitraster = async (wochentag: Wochentag | undefined, stunde : number | undefined) => {
		if ((wochentag === undefined) && (stunde === undefined)) {
			return;
		} else if ((wochentag === undefined) && (stunde !== undefined)) {
			api.status.start();
			// Füge Zeile im Stundenplan hinzu
			console.log("Stunde hinten anfügen: ", stunde);
			for (const wochentag of this.stundenplanManager.zeitrasterGetWochentageAlsEnumRange()) {
				const z = this.stundenplanManager.zeitrasterGetByWochentagAndStundeOrNull(wochentag.id, stunde);
				if (z !== null) {
					const zeitraster: Partial<StundenplanZeitraster> = new StundenplanZeitraster();
					delete zeitraster.id;
					zeitraster.wochentag = wochentag.id;
					zeitraster.unterrichtstunde = stunde + 1;
					zeitraster.stundenbeginn = (z.stundenende ?? 0) + 5;
					zeitraster.stundenende = zeitraster.stundenbeginn + ((z.stundenende ?? 0) - (z.stundenbeginn ?? 0));
					const _item = await api.server.addZeitrasterEintrag(zeitraster, api.schema)
					this.stundenplanManager.zeitrasterAdd(_item);
				}
			}
		} else if ((wochentag !== undefined) && (stunde === undefined)) {
			// Füge Spalte im Stundenplan hinzu
			api.status.start();
			console.log("Wochentag hinten hinzufügen: ", wochentag.kuerzel);
			const ersteStunde = this.stundenplanManager.zeitrasterGetStundeMin();
			const letzterTag = this.stundenplanManager.zeitrasterGetWochentagMax();
			const zeitraster: Partial<StundenplanZeitraster> = new StundenplanZeitraster();
			delete zeitraster.id;
			zeitraster.wochentag = Wochentag.fromIDorException(letzterTag + 1).id;
			zeitraster.unterrichtstunde = ersteStunde;
			zeitraster.stundenbeginn = this.stundenplanManager.zeitrasterGetMinutenMinDerStunde(ersteStunde);
			zeitraster.stundenende = this.stundenplanManager.zeitrasterGetMinutenMaxDerStunde(ersteStunde);
			const _item = await api.server.addZeitrasterEintrag(zeitraster, api.schema)
			this.stundenplanManager.zeitrasterAdd(_item);
		} else if ((wochentag !== undefined) && (stunde !== undefined)) {
			api.status.start();
			// Füge Zelle im Stundenplan hinzu
			const zeitraster: Partial<StundenplanZeitraster> = new StundenplanZeitraster();
			delete zeitraster.id;
			zeitraster.wochentag = wochentag.id;
			zeitraster.unterrichtstunde = stunde;
			zeitraster.stundenbeginn = this.stundenplanManager.zeitrasterGetMinutenMinDerStunde(stunde);
			zeitraster.stundenende = this.stundenplanManager.zeitrasterGetMinutenMaxDerStunde(stunde);
			console.log("Zeitrastereintrag ergänzen", wochentag.kuerzel, stunde);
			const _item = await api.server.addZeitrasterEintrag(zeitraster, api.schema)
			this.stundenplanManager.zeitrasterAdd(_item);
		}
		this.commit();
		api.status.stop();
	}

	removeZeitraster = async (multi: StundenplanZeitraster[]) => {
		for (const zeitraster of multi) {
			await api.server.deleteZeitrasterEintrag(api.schema, zeitraster.id);
		}
		this.commit();
	}

	patch = async (eintrag : StundenplanZeitraster) => {
		await api.server.patchZeitrasterEintrag(eintrag, api.schema, eintrag.id);
	}
}

const SZeitrasterAuswahl = () => import("~/components/kataloge/zeitraster/SZeitrasterAuswahl.vue")
const SZeitrasterApp = () => import("~/components/kataloge/zeitraster/SZeitrasterApp.vue")

export class RouteKatalogZeitraster extends RouteNode<RouteDataKatalogZeitraster, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "kataloge.zeitraster", "/kataloge/zeitraster", SZeitrasterApp, new RouteDataKatalogZeitraster());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Zeitraster";
		super.setView("liste", SZeitrasterAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
			routeKatalogZeitrasterDaten
		];
		super.defaultChild = routeKatalogZeitrasterDaten;
	}

	public async beforeEach(to: RouteNode<unknown, any>, to_params: RouteParams, from: RouteNode<unknown, any> | undefined, from_params: RouteParams) : Promise<boolean | void | Error | RouteLocationRaw> {
		return this.getRoute();
	}

	public async enter(to: RouteNode<unknown, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		await this.data.ladeListe();
	}

	public getRoute() : RouteLocationRaw {
		return { name: this.defaultChild!.name};
	}

	public getAuswahlProps(to: RouteLocationNormalized): ZeitrasterAuswahlProps {
		return {
			abschnitte: api.mapAbschnitte.value,
			aktAbschnitt: routeApp.data.aktAbschnitt.value,
			aktSchulabschnitt: api.schuleStammdaten.idSchuljahresabschnitt,
			setAbschnitt: routeApp.data.setAbschnitt,
			returnToKataloge: routeKataloge.returnToKataloge
		};
	}

	public getProps(to: RouteLocationNormalized): ZeitrasterAppProps {
		return {
			// Props für die Navigation
			setTab: this.setTab,
			tab: this.getTab(),
			tabs: this.getTabs(),
			tabsHidden: this.children_hidden().value,
		};
	}

	private getTab(): AuswahlChildData {
		return { name: this.data.view.name, text: this.data.view.text };
	}

	private getTabs(): AuswahlChildData[] {
		const result: AuswahlChildData[] = [];
		for (const c of super.children)
			if (c.hatEineKompetenz() && c.hatSchulform())
				result.push({ name: c.name, text: c.text });
		return result;
	}

	private setTab = async (value: AuswahlChildData) => {
		if (value.name === this.data.view.name)
			return;
		const node = RouteNode.getNodeByName(value.name);
		if (node === undefined)
			throw new Error("Unbekannte Route");
		await RouteManager.doRoute({ name: value.name });
		await this.data.setView(node);
	}
}

export const routeKatalogZeitraster = new RouteKatalogZeitraster();
