import type { RouteLocationRaw, RouteParams, RouteParamsRawGeneric } from "vue-router";
import type { TabData } from "@ui";
import type { AppProps } from "~/components/SAppProps";
import { Schulform, BenutzerKompetenz, ServerMode, DeveloperNotificationException } from "@core";
import { api } from "~/router/Api";
import { RouteNode } from "~/router/RouteNode";
import { RouteManager } from "~/router/RouteManager";
import { RoutingStatus } from "~/router/RoutingStatus";
import { RouteDataApp } from "~/router/apps/RouteDataApp";
import { routeBenutzerprofil } from "./benutzerprofil/RouteBenutzerprofil";
import { routeSchule } from "~/router/apps/schule/RouteSchule";
import { routeSchuleJahrgaenge } from "~/router/apps/schule/jahrgaenge/RouteSchuleJahrgaenge";
import { routeSchuleFaecher } from "~/router/apps/schule/faecher/RouteSchuleFaecher";
import { routeStundenplanKataloge } from "./stundenplan/RouteStundenplanKataloge";
import { routeSchueler } from "~/router/apps/schueler/RouteSchueler";
import { routeLehrer } from "~/router/apps/lehrer/RouteLehrer";
import { routeKlassen } from "~/router/apps/klassen/RouteKlassen";
import { routeKurse } from "~/router/apps/kurse/RouteKurse";
import { routeGost } from "~/router/apps/gost/RouteGost";
import { routeStatistik } from "~/router/apps/statistik/RouteStatistik";
import { routeStundenplan } from "~/router/apps/stundenplan/RouteStundenplan";
import { routeLogin } from "~/router/login/RouteLogin";
import { routeError } from "../error/RouteError";
import { routeSchuleBetriebe } from "./schule/betriebe/RouteSchuleBetriebe";
import { routeKatalogEinwilligungsarten } from "./schule/einwilligungsarten/RouteKatalogEinwilligungsarten";
import { routeKatalogFoerderschwerpunkte } from "./schule/foerderschwerpunkte/RouteKatalogFoerderschwerpunkte";
import { routeKatalogReligionen } from "./schule/religionen/RouteKatalogReligionen";
import { routeKatalogSchulen } from "./schule/schulen/RouteKatalogSchulen";
import { routeKatalogVermerkarten } from "./schule/vermerke/RouteKatalogVermerkarten";
import { routeEinstellungen } from "./einstellungen/RouteEinstellungen";
import { routeEinstellungenBenutzer } from "~/router/apps/einstellungen/benutzer/RouteEinstellungenBenutzer";
import { routeEinstellungenBenutzergruppe } from "~/router/apps/einstellungen/benutzergruppen/RouteEinstellungenBenutzergruppe";
import { routeSchuleDatenaustauschKurs42 } from "./schule/datenaustausch/kurs42/RouteSchuleDatenaustauschKurs42";
import { routeSchuleDatenaustauschUntis } from "./schule/datenaustausch/untis/RouteSchuleDatenaustauschUntis";
import SApp from "~/components/SApp.vue";
import { routeSchuleDatenaustauschENM } from "./schule/datenaustausch/RouteSchuleDatenaustauschENM";
import { routeSchuleDatenaustauschLaufbahnplanung } from "./schule/datenaustausch/RouteSchuleDatenaustauschLupo";
import { routeSchuleDatenaustauschSchulbewerbung } from "./schule/datenaustausch/RouteSchuleDatenaustauschSchulbewerbung";
import { routeSchuleDatenaustauschWenom } from "./schule/datenaustausch/RouteSchuleDatenaustauschWenom";


export class RouteApp extends RouteNode<RouteDataApp, any> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "app", "/:schema?/:idSchuljahresabschnitt(\\d+)?", SApp, new RouteDataApp());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps();
		super.text = "SVWS-Client";
		super.children = [
			routeBenutzerprofil,
			routeSchuleJahrgaenge,
			routeSchuleFaecher,
			routeKatalogReligionen,
			routeKatalogEinwilligungsarten,
			routeKatalogVermerkarten,
			routeSchuleBetriebe,
			routeKatalogFoerderschwerpunkte,
			routeKatalogSchulen,
			routeSchueler,
			routeLehrer,
			routeKlassen,
			routeKurse,
			routeGost,
			routeStatistik,
			routeStundenplan,
			routeStundenplanKataloge,
			routeEinstellungen,
			routeEinstellungenBenutzer,
			routeEinstellungenBenutzergruppe,
			routeSchule,
			routeSchuleDatenaustauschKurs42,
			routeSchuleDatenaustauschUntis,
			routeSchuleDatenaustauschLaufbahnplanung,
			routeSchuleDatenaustauschSchulbewerbung,
			routeSchuleDatenaustauschENM,
			routeSchuleDatenaustauschWenom,
		];
		super.menu = [
			routeSchule,
			routeSchueler,
			routeLehrer,
			routeKlassen,
			routeKurse,
			routeGost,
			routeStatistik,
			routeStundenplan,
			routeEinstellungen,
		];
		super.defaultChild = routeSchueler;
	}

	public async beforeEach(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams) : Promise<boolean | void | Error | RouteLocationRaw> {
		return this.getRouteDefaultChild({ idSchuljahresabschnitt : undefined });
	}

	public async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
		try {
			if (isEntering)
				await this.data.init();
			const { idSchuljahresabschnitt } = RouteNode.getIntParams(to_params, ["idSchuljahresabschnitt"]);
			// Prüfe, ob der Schuljahresabschnitt gültig gesetzt ist
			if (idSchuljahresabschnitt === undefined)
				return this.getRouteDefaultChild({ idSchuljahresabschnitt: this.data.aktAbschnitt.value.id });
			// Prüfe, ob der Schuljahresabschnitt gesetzt werden soll
			await this.data.setSchuljahresabschnitt(idSchuljahresabschnitt);
			// Prüfe, ob die View aktualisiert werden muss
			let cur: RouteNode<any, any> = to;
			while (cur.parent !== this)
				cur = cur.parent;
			if (cur !== this.data.view)
				this.data.setView(cur, this.children);
		} catch (e) {
			return routeError.getErrorRoute(e as DeveloperNotificationException);
		}
	}

	public async leave(from: RouteNode<any, any>, from_params: RouteParams): Promise<void> {
		await this.data.leave();
	}

	public addRouteParamsFromState() : RouteParamsRawGeneric {
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
			schuleStammdaten: api.schuleStammdaten,
			// Props für die Navigation
			setApp: this.setApp,
			app: this.getApp(),
			selectedChild: this.getSelectedChild(),
			benutzerprofilApp: { name: routeBenutzerprofil.name, text: routeBenutzerprofil.text, hide: true },
			apps: this.getApps(),
			appsHidden: this.children_hidden().value,
			apiStatus: api.status,
		};
	}

	private getSelectedChild(): TabData {
		const child = this.selectedChild!;
		return { name: child.name, text: child.text, hide: false };
	}

	private getApp(): TabData {
		return { name: this.data.view.name, text: this.data.view.text, hide: !this.data.view.hasView('liste') && !this.data.view.hasView('submenu') };
	}

	private getApps(): TabData[] {
		const result: TabData[] = [];
		for (const c of super.menu) {
			if (c.hatEineKompetenz() && c.hatSchulform())
				result.push({ name: c.name, text: c.text });
		}
		return result;
	}

	private setApp = async (value: TabData) => {
		if (value.name === this.data.view.name)
			return;
		const node = RouteNode.getNodeByName(value.name);
		if (node === undefined)
			throw new DeveloperNotificationException("Unbekannte Route");
		const result = await RouteManager.doRoute({ name: value.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt } });
		if (result === RoutingStatus.SUCCESS)
			this.data.setView(node, this.children);
	}

}

export const routeApp = new RouteApp();
