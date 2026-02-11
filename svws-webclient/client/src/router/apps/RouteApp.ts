import type { RouteLocationRaw, RouteParams, RouteParamsRawGeneric } from "vue-router";
import type { AppProps } from "~/components/SAppProps";
import { AppMenuManager, ViewType, type TabData, type TabManager } from "@ui";
import { Schulform, BenutzerKompetenz, ServerMode, DeveloperNotificationException } from "@core";
import { RouteNode } from "~/router/RouteNode";
import { RouteManager, routerManager } from "~/router/RouteManager";
import { RoutingStatus } from "~/router/RoutingStatus";
import { RouteDataApp } from "~/router/apps/RouteDataApp";
import { api } from "~/router/Api";
import { routeBenutzerprofil } from "./benutzerprofil/RouteBenutzerprofil";
import { routeSchueler } from "~/router/apps/schueler/RouteSchueler";
import { routeLehrer } from "~/router/apps/lehrer/RouteLehrer";
import { routeKlassen } from "~/router/apps/klassen/RouteKlassen";
import { routeKurse } from "~/router/apps/kurse/RouteKurse";
import { routeGost } from "~/router/apps/gost/RouteGost";
import { routeStatistik } from "~/router/apps/statistik/RouteStatistik";
import { routeStundenplan } from "~/router/apps/stundenplan/RouteStundenplan";
import { routeLogin } from "~/router/login/RouteLogin";
import { routeError } from "../error/RouteError";
import { routeSchule } from "~/router/apps/schule/RouteSchule";
import { routeJahrgaenge } from "~/router/apps/schule/kataloge/jahrgaenge/RouteJahrgaenge";
import { routeFaecher } from "~/router/apps/schule/kataloge/faecher/RouteFaecher";
import { routeEinwilligungsarten } from "~/router/apps/schule/kataloge/einwilligungsarten/RouteEinwilligungsarten";
import { routeKonfessionen } from "~/router/apps/schule/kataloge/konfessionen/RouteKonfessionen";
import { routeSchulen } from "~/router/apps/schule/kataloge/schulen/RouteSchulen";
import { routeTelefonarten } from "~/router/apps/schule/kataloge/telefonarten/RouteTelefonarten";
import { routeErzieherarten } from "~/router/apps/schule/kataloge/erzieherarten/RouteErzieherarten";
import { routeVermerkarten } from "~/router/apps/schule/kataloge/vermerkarten/RouteVermerkarten";
import { routeLernplattformen } from "~/router/apps/schule/kataloge/lernplattformen/RouteLernplattformen";
import { routeEinstellungen } from "./einstellungen/RouteEinstellungen";
import { routeEinstellungenBenutzer } from "~/router/apps/einstellungen/benutzer/RouteEinstellungenBenutzer";
import { routeEinstellungenBenutzergruppe } from "~/router/apps/einstellungen/benutzergruppen/RouteEinstellungenBenutzergruppe";
import { routeSchuleDatenaustauschKurs42 } from "./schule/datenaustausch/kurs42/RouteSchuleDatenaustauschKurs42";
import { routeSchuleDatenaustauschUntis } from "./schule/datenaustausch/untis/RouteSchuleDatenaustauschUntis";
import { routeSchuleDatenaustauschENM } from "./schule/datenaustausch/enmNotenmanager/RouteSchuleDatenaustauschENM";
import { routeSchuleDatenaustauschLaufbahnplanung } from "./schule/datenaustausch/laufbahnplanung/RouteSchuleDatenaustauschLupo";
import { routeSchuleDatenaustauschSchulwechsel } from "./schule/datenaustausch/schulwechsel/RouteSchuleDatenaustauschSchulwechsel";
import { routeSchuleDatenaustauschLernplattformen } from "~/router/apps/schule/datenaustausch/lernplattformenExport/RouteSchuleDatenaustauschLernplattformen";
import { routeSchuleStammdaten } from "~/router/apps/schule/stammdaten/RouteSchuleStammdaten";
import { routeSchuleReporting } from "./schule/reporting/RouteSchuleReporting";
import { routeAbteilungen } from "~/router/apps/schule/kataloge/abteilungen/RouteAbteilungen";
import { routeEntlassgruende } from "~/router/apps/schule/kataloge/entlassgruende/RouteEntlassgruende";
import { routeKindergaerten } from "~/router/apps/schule/kataloge/kindergaerten/RouteKindergaerten";
import { routeFoerderschwerpunkte } from "~/router/apps/schule/kataloge/foerderschwerpunkte/RouteFoerderschwerpunkte";
import { routeNotenmodul } from "./notenmodul/RouteNotenmodul";
import { routeNotenmodulLeistungen } from "./notenmodul/RouteNotenmodulLeistungen";
import { routeNotenmodulKlassenleitung } from "./notenmodul/RouteNotenmodulKlassenleitung";
import { routeNotenmodulTeilleistungen } from "./notenmodul/RouteNotenmodulTeilleistungen";
import { routeFahrschuelerarten } from "~/router/apps/schule/kataloge/fahrschuelerarten/RouteFahrschuelerarten";
import { routeHaltestellen } from "~/router/apps/schule/kataloge/haltestellen/RouteHaltestellen";
import { routeBeschaeftigungsarten } from "~/router/apps/schule/kataloge/beschaeftigungsarten/RouteBeschaeftigungsarten";
import { routeFloskelgruppen } from "~/router/apps/schule/kataloge/floskelgruppen/RouteFloskelgruppen";
import { routeFloskeln } from "~/router/apps/schule/kataloge/floskeln/RouteFloskeln";
import { routeNotenmodulAdministration } from "./notenmodul/RouteNotenmodulAdministration";
import { routeNotenmodulZugangsdaten } from "./notenmodul/RouteNotenmodulZugangsdaten";
import { routeBetriebe } from "~/router/apps/schule/kataloge/betriebe/RouteBetriebe";
import { routeOrte } from "~/router/apps/schule/kataloge/orte/RouteOrte";
import { routeOrtsteile } from "~/router/apps/schule/kataloge/ortsteile/RouteOrtsteile";

import SApp from "~/components/SApp.vue";
import { routeBetriebsarten } from "./schule/kataloge/betriebsarten/RouteBetriebsarten";


export class RouteApp extends RouteNode<RouteDataApp, any> {

	/** Die Knoten, welche im Haupt-Menu zur Verfügung gestellt werden */
	private readonly _menuMain: RouteNode<any, any>[];

	public menuHidden(): boolean[] {
		return super.menu.map(c => c.hidden(routerManager.getRouteParams()) !== false);
	}

	/** Die Knoten, welche im Menu Einstellungen zur Verfügung gestellt werden */
	// TODO in abstrahierter Form in RouteNode integrieren...
	private readonly _menuEinstellungen: RouteNode<any, any>[];
	public get menuEinstellungen(): RouteNode<any, any>[] {
		const result: RouteNode<any, any>[] = [];
		for (const node of this._menuEinstellungen) {
			if (api.authenticated && (!node.mode.checkServerMode(api.mode) || !node.hatSchulform() || !node.hatEineKompetenz())) {
				continue;
			}
			result.push(node);
		}
		return result;
	}
	public menuEinstellungenHidden(): boolean[] {
		return this.menuEinstellungen.map(c => c.hidden(routerManager.getRouteParams()) !== false);
	}

	/** Die Knoten, welche im Menu Schule zur Verfügung gestellt werden */
	// TODO in abstrahierter Form in RouteNode integrieren...
	private readonly _menuSchule: RouteNode<any, any>[];
	public get menuSchule(): RouteNode<any, any>[] {
		const result: RouteNode<any, any>[] = [];
		for (const node of this._menuSchule) {
			if (api.authenticated && (!node.mode.checkServerMode(api.mode) || !node.hatSchulform() || !node.hatEineKompetenz())) {
				continue;
			}
			result.push(node);
		}
		return result;
	}
	public menuSchuleHidden(): boolean[] {
		return this.menuSchule.map(c => c.hidden(routerManager.getRouteParams()) !== false);
	}

	/** Die Knoten, welche im Menu Notenmodul zur Verfügung gestellt werden */
	// TODO in abstrahierter Form in RouteNode integrieren...
	private readonly _menuNotenmodul: RouteNode<any, any>[];
	public get menuNotenmodul(): RouteNode<any, any>[] {
		const result: RouteNode<any, any>[] = [];
		for (const node of this._menuNotenmodul) {
			if (api.authenticated && (!node.mode.checkServerMode(api.mode) || !node.hatSchulform() || !node.hatEineKompetenz())) {
				continue;
			}
			result.push(node);
		}
		return result;
	}
	public menuNotenmodulHidden(): boolean[] {
		return this.menuNotenmodul.map(c => c.hidden(routerManager.getRouteParams()) !== false);
	}

	public constructor() {
		super(Schulform.values(), [BenutzerKompetenz.KEINE], "app", String.raw`/:schema?/:idSchuljahresabschnitt(\d+)?`, SApp, new RouteDataApp());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps();
		super.text = "SVWS-Client";
		this._menuMain = [
			routeBenutzerprofil,
			routeSchule,
			routeSchueler,
			routeLehrer,
			routeKlassen,
			routeKurse,
			routeNotenmodul,
			routeGost,
			routeStatistik,
			routeStundenplan,
			routeEinstellungen,
		];
		this._menuEinstellungen = [
			routeEinstellungenBenutzer,
			routeEinstellungenBenutzergruppe,
		];
		this._menuSchule = [
			// Stammdaten
			routeSchuleStammdaten,
			// Kataloge
			routeAbteilungen,
			routeBetriebe,
			routeBeschaeftigungsarten,
			routeBetriebsarten,
			routeEinwilligungsarten,
			routeEntlassgruende,
			routeErzieherarten,
			routeFaecher,
			routeFahrschuelerarten,
			routeFloskelgruppen,
			routeFloskeln,
			routeFoerderschwerpunkte,
			routeHaltestellen,
			routeJahrgaenge,
			routeKindergaerten,
			routeKonfessionen,
			routeLernplattformen,
			routeOrte,
			routeOrtsteile,
			routeSchulen,
			routeTelefonarten,
			routeVermerkarten,
			// Datenaustausch
			routeSchuleDatenaustauschENM,
			routeSchuleDatenaustauschSchulwechsel,
			routeSchuleDatenaustauschLaufbahnplanung,
			routeSchuleDatenaustauschKurs42,
			routeSchuleDatenaustauschUntis,
			routeSchuleDatenaustauschLernplattformen,
			// Reporting
			routeSchuleReporting,
		];
		this._menuNotenmodul = [
			routeNotenmodulAdministration,
			routeNotenmodulZugangsdaten,
			routeNotenmodulLeistungen,
			routeNotenmodulTeilleistungen,
			routeNotenmodulKlassenleitung,
		];
		super.children = [
			...this._menuMain,
			...this._menuSchule,
			...this._menuNotenmodul,
			...this._menuEinstellungen,
		];
		super.menu = this._menuMain;
		super.defaultChild = routeSchueler;
	}

	public async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean): Promise<void | Error | RouteLocationRaw> {
		try {
			if (isEntering) {
				await this.data.init();
			}
			const { idSchuljahresabschnitt } = RouteNode.getIntParams(to_params, ["idSchuljahresabschnitt"]);
			// Prüfe, ob der Schuljahresabschnitt gültig gesetzt ist
			if (idSchuljahresabschnitt === undefined) {
				return this.getRouteDefaultChild({ idSchuljahresabschnitt: this.data.aktAbschnitt.value.id });
			}
			// Prüfe, ob der Schuljahresabschnitt gesetzt werden soll
			await this.data.setSchuljahresabschnitt(idSchuljahresabschnitt);
			// Prüfe, ob die View aktualisiert werden muss
			let cur: RouteNode<any, any> = to;
			while (cur.parent !== this) {
				cur = cur.parent;
			}
			if (cur !== this.data.view) {
				this.data.setView(cur, this.children);
			}
		} catch (e) {
			return await routeError.getErrorRoute(e as DeveloperNotificationException);
		}
	}

	public async leave(from: RouteNode<any, any>, from_params: RouteParams): Promise<void> {
		await this.data.leave();
	}

	public addRouteParamsFromState(): RouteParamsRawGeneric {
		const schema = encodeURIComponent(api.schema);
		const idSchuljahresabschnitt = this.data.idSchuljahresabschnitt;
		return { schema, idSchuljahresabschnitt };
	}

	public getProps(): AppProps {
		return {
			logout: routeLogin.logout,
			username: api.username,
			schemaname: api.schema,
			schulform: api.schulform,
			servermode: api.mode,
			schuleStammdaten: api.schuleStammdaten,
			// Props für die Navigation
			menu: this.getMenuManager(),
			benutzerprofilApp: { name: routeBenutzerprofil.name, text: routeBenutzerprofil.text, hide: true },
			apiStatus: api.status,
			tabManagerSchule: this.getTabManagerSchule,
			tabManagerEinstellungen: this.getTabManagerEinstellungen,
			tabManagerNotenmodul: this.getTabManagerNotenmodul,
			schuljahresabschnittsauswahl: () => this.data.getSchuljahresabschnittsauswahl(true),
		};
	}

	private getSelectedChild(): TabData {
		const child = this.selectedChild!;
		return { name: child.name, text: child.text, hide: false };
	}

	private getApp(): TabData {
		return { name: (this.data.view.name === "stundenplan.kataloge") ? "stundenplan" : this.data.view.name, text: this.data.view.text, hide: !this.data.view.hasView('liste') };
	}

	private getApps(): TabData[] {
		const result: TabData[] = [];
		for (const c of super.menu) {
			if (c.hatEineKompetenz() && c.hatSchulform()) {
				result.push({ name: c.name, text: c.text });
			}
		}
		return result;
	}

	private readonly setApp = async (value: TabData) => {
		if (value.name === this.data.view.name) {
			return;
		}
		let node = RouteNode.getNodeByName(value.name);
		if (node === undefined) {
			throw new DeveloperNotificationException("Unbekannte Route");
		}
		if (node === routeEinstellungen) {
			node = this.menuEinstellungen.at(0);
		} else if (node === routeSchule) {
			node = this.menuSchule.at(0);
		} else if (node === routeNotenmodul) {
			node = this.menuNotenmodul.at(0);
		}
		if (node === undefined) {
			return;
		}
		const result = await RouteManager.doRoute(node.getRoute());
		if (result === RoutingStatus.SUCCESS) {
			this.data.setView(node, this.children);
		}
	};

	private getMenuManager(): AppMenuManager {
		const submenuManager = new Array<{ name: string, manager: TabManager }>();
		if (routeSchule.hidden() === false) {
			submenuManager.push({ name: "schule", manager: this.getTabManagerSchule() });
		}
		if ((routeNotenmodul.hidden() === false) && ((api.mode === ServerMode.DEV) || (api.mode === ServerMode.ALPHA))) {
			submenuManager.push({ name: "notenmodul", manager: this.getTabManagerNotenmodul() });
		}
		if (routeEinstellungen.hidden() === false) {
			submenuManager.push({ name: "einstellungen", manager: this.getTabManagerEinstellungen() });
		}
		return new AppMenuManager(this.getTabManager(), submenuManager, this.getApp());
	}

	private getTabManager(): TabManager {
		return this.createTabManager(super.menu, this.menuHidden(), this.data.view.name, this.setApp, ViewType.DEFAULT);
	}

	private readonly getTabManagerEinstellungen = (): TabManager => {
		return this.createTabManager(this.menuEinstellungen, this.menuEinstellungenHidden(), this.data.view.name, this.setTab, ViewType.DEFAULT);
	};

	private readonly getTabManagerSchule = (): TabManager => {
		return this.createTabManager(this.menuSchule, this.menuSchuleHidden(), this.data.view.name, this.setTab, ViewType.DEFAULT);
	};

	private readonly getTabManagerNotenmodul = (): TabManager => {
		return this.createTabManager(this.menuNotenmodul, this.menuNotenmodulHidden(), this.data.view.name, this.setTab, ViewType.DEFAULT);
	};

	private readonly setTab = async (value: TabData) => {
		const node = RouteNode.getNodeByName(value.name);
		if (node === undefined) {
			throw new DeveloperNotificationException("Unbekannte Route");
		}
		const routingStatus = await RouteManager.doRoute(node.getRoute());
		if (routingStatus === RoutingStatus.SUCCESS) {
			this.data.setView(node, this.children);
		}
	};

}

export const routeApp = new RouteApp();
