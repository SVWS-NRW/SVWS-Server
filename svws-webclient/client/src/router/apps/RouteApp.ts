import type { RouteLocationRaw, RouteParams } from "vue-router";
import type { AuswahlChildData } from "~/components/AuswahlChildData";
import type { AppProps } from "~/components/SAppProps";
import { Schulform, BenutzerKompetenz, ServerMode, DeveloperNotificationException } from "@core";
import { api } from "~/router/Api";
import { RouteNode } from "~/router/RouteNode";
import { RouteManager } from "~/router/RouteManager";
import { RouteDataApp } from "~/router/apps/RouteDataApp";
import { routeBenutzerprofil } from "./benutzerprofil/RouteBenutzerprofil";
import { routeSchule } from "~/router/apps/schule/RouteSchule";
import { routeSchuleBenutzer } from "~/router/apps/schule/benutzer/RouteSchuleBenutzer";
import { routeSchuleBenutzergruppe } from "~/router/apps/schule/benutzergruppen/RouteSchuleBenutzergruppe";
import { routeSchuleDatenaustausch } from "~/router/apps/schule/datenaustausch/RouteSchuleDatenaustausch";
import { routeKataloge } from "~/router/apps/kataloge/RouteKataloge"
import { routeKatalogFaecher } from "~/router/apps/kataloge/faecher/RouteKatalogFaecher";
import { routeKatalogBetriebe } from "~/router/apps/kataloge/betriebe/RouteKatalogBetriebe";
import { routeKatalogReligion } from "~/router/apps/kataloge/religion/RouteKatalogReligionen";
import { routeKatalogVermerkarten } from "~/router/apps/kataloge/vermerke/RouteKatalogVermerkarten";
import { routeKatalogJahrgaenge } from "~/router/apps/kataloge/jahrgaenge/RouteKatalogJahrgaenge";
import { routeKatalogFoerderschwerpunkte } from "~/router/apps/kataloge/foerderschwerpunkte/RouteKatalogFoerderschwerpunkte";
import { routeKatalogRaeume } from "~/router/apps/kataloge/raum/RouteKatalogRaeume";
import { routeKatalogAufsichtsbereiche } from "~/router/apps/kataloge/aufsichtsbereich/RouteKatalogAufsichtsbereiche";
import { routeKatalogPausenzeiten } from "~/router/apps/kataloge/pausenzeit/RouteKatalogPausenzeiten";
import { routeKatalogZeitraster } from "~/router/apps/kataloge/zeitraster/RouteKatalogZeitraster";
import { routeSchueler } from "~/router/apps/schueler/RouteSchueler";
import { routeLehrer } from "~/router/apps/lehrer/RouteLehrer";
import { routeKlassen } from "~/router/apps/klassen/RouteKlassen";
import { routeKurse } from "~/router/apps/kurse/RouteKurse";
import { routeGost } from "~/router/apps/gost/RouteGost";
import { routeStatistik } from "~/router/apps/statistik/RouteStatistik";
import { routeStundenplan } from "~/router/apps/stundenplan/RouteStundenplan";
import { routeLogin } from "~/router/login/RouteLogin";
import { ConfigElement } from "~/components/Config";
import SApp from "~/components/SApp.vue";
import { routeKatalogSchulen } from "./kataloge/schulen/RouteKatalogSchulen";
import { routeError } from "../error/RouteError";


export class RouteApp extends RouteNode<RouteDataApp, any> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "app", "/:idSchuljahresabschnitt(\\d+)?", SApp, new RouteDataApp());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps();
		super.text = "SVWS-Client";
		super.children = [
			routeBenutzerprofil,
			routeSchule,
			routeSchuleBenutzer,
			routeSchuleBenutzergruppe,
			routeSchuleDatenaustausch,
			routeKataloge,
			routeKatalogFaecher,
			routeKatalogReligion,
			routeKatalogVermerkarten,
			routeKatalogBetriebe,
			routeKatalogJahrgaenge,
			routeKatalogFoerderschwerpunkte,
			routeKatalogRaeume,
			routeKatalogAufsichtsbereiche,
			routeKatalogPausenzeiten,
			routeKatalogZeitraster,
			routeKatalogSchulen,
			routeSchueler,
			routeLehrer,
			routeKlassen,
			routeKurse,
			routeGost,
			routeStatistik,
			routeStundenplan,
		];
		super.menu = [
			routeSchule,
			routeKataloge,
			routeSchueler,
			routeLehrer,
			routeKlassen,
			routeKurse,
			routeGost,
			routeStatistik,
			routeStundenplan,
		];
		super.defaultChild = routeSchueler;
	}

	public async beforeEach(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams) : Promise<boolean | void | Error | RouteLocationRaw> {
		return this.getRoute();
	}

	public async enter(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams): Promise<void | RouteLocationRaw | Error> {
		await this.data.init();
	}

	public async update(to: RouteNode<any, any>, to_params: RouteParams) : Promise<void | Error | RouteLocationRaw> {
		const idSchuljahresabschnitt = RouteNode.getIntParam(to_params, "idSchuljahresabschnitt");
		if (idSchuljahresabschnitt instanceof Error)
			return routeError.getRoute(idSchuljahresabschnitt);
		// Prüfe, ob der Schuljahresabschnitt gültig gesetzt ist
		if (idSchuljahresabschnitt === undefined)
			return this.getRoute(this.data.aktAbschnitt.value.id);
		// Prüfe, ob der Schuljahresabschnitt gesetzt werden soll
		await this.data.setSchuljahresabschnitt(idSchuljahresabschnitt);
		// Prüfe, ob die View aktualisiert werden muss
		let cur: RouteNode<any, any> = to;
		while (cur.parent !== this)
		  cur = cur.parent;
		if (cur !== this.data.view)
			this.data.setView(cur, this.children);
	}

	public async leave(from: RouteNode<any, any>, from_params: RouteParams): Promise<void> {
		await this.data.leave();
	}

	public getRoute(idSchuljahresabschnitt?: number): RouteLocationRaw {
		return { name: this.defaultChild!.name, params: { idSchuljahresabschnitt } };
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
			benutzerprofilApp: { name: routeBenutzerprofil.name, text: routeBenutzerprofil.text, hideAuswahl: true },
			apps: this.getApps(),
			appsHidden: this.children_hidden().value,
			apiStatus: api.status,
			backticks: () => routeBenutzerprofil.data.backticks,
		};
	}

	private getApp(): AuswahlChildData {
		return { name: this.data.view.name, text: this.data.view.text, hideAuswahl: !this.data.view.hasView('liste') };
	}

	private getApps(): AuswahlChildData[] {
		const result: AuswahlChildData[] = [];
		for (const c of super.menu) {
			if (c.hatEineKompetenz() && c.hatSchulform())
				result.push({ name: c.name, text: c.text });
		}
		return result;
	}

	private setApp = async (value: AuswahlChildData) => {
		if (value.name === this.data.view.name)
			return;
		const node = RouteNode.getNodeByName(value.name);
		if (node === undefined)
			throw new DeveloperNotificationException("Unbekannte Route");
		await RouteManager.doRoute({ name: value.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt } });
		this.data.setView(node, this.children);
	}

}

export const routeApp = new RouteApp();
