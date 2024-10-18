import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import type { BenutzerKompetenzGruppe, List} from "@core";
import { ArrayList, BenutzerKompetenz, DeveloperNotificationException, Schulform, ServerMode } from "@core";

import { RouteNode } from "~/router/RouteNode";
import { RouteManager } from "~/router/RouteManager";

import { routeApp, type RouteApp } from "~/router/apps/RouteApp";
import { routeEinstellungenBenutzer } from "~/router/apps/einstellungen/benutzer/RouteEinstellungenBenutzer";
import { routeEinstellungenBenutzergruppe } from "~/router/apps/einstellungen/benutzergruppen/RouteEinstellungenBenutzergruppe";

import { RouteDataEinstellungen } from "~/router/apps/einstellungen/RouteDataEinstellungen";

import type { EinstellungenAuswahlProps } from "~/components/einstellungen/EinstellungenAuswahlProps";
import type { TabData } from "@ui";
import type { EinstellungenProps } from "~/components/einstellungen/EinstellungenProps";
import { api } from "~/router/Api";

const SEinstellungenAuswahl = () => import("~/components/einstellungen/SEinstellungenAuswahl.vue")
const SEinstellungen = () => import("~/components/einstellungen/SEinstellungen.vue")

export class RouteEinstellungen extends RouteNode<RouteDataEinstellungen, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.ADMIN ], "einstellungen", "einstellungen", SEinstellungen, new RouteDataEinstellungen());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Einstellungen";
		super.setView("submenu", SEinstellungenAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [];
		super.menu = [
			// Benutzerverwaltung
			routeEinstellungenBenutzer,
			routeEinstellungenBenutzergruppe,
		];
		super.defaultChild = routeEinstellungenBenutzer;
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
		if (to.name === this.name)
			return this.data.view.getRoute();
	}

	public getRoute() : RouteLocationRaw {
		return { name: this.defaultChild!.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt }};
	}

	public getProps(to: RouteLocationNormalized): EinstellungenProps {
		return <EinstellungenProps>{
			schule: () => api.schuleStammdaten,
			benutzerdaten: () => api.benutzerdaten,
		};
	}

	public getAuswahlProps(to: RouteLocationNormalized): EinstellungenAuswahlProps {
		return {
			tabManager: () => this.createTabManagerByMenu(this.data.view.name, this.setTab),
		};
	}

	private setTab = async (value: TabData) => {
		const node = RouteNode.getNodeByName(value.name);
		if (node === undefined)
			throw new DeveloperNotificationException("Unbekannte Route");
		await RouteManager.doRoute({ name: value.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt } });
		this.data.setView(node, this.menu);
	}

	public benutzerKompetenzen = (gruppe : BenutzerKompetenzGruppe) : List<BenutzerKompetenz> => {
		const schuljahr = routeApp.data.aktAbschnitt.value.schuljahr;
		const schulformEintrag = api.schulform.daten(schuljahr);
		const schulform = Schulform.data().getWertByID(schulformEintrag?.id ?? -1);
		return (schulform === null) ? new ArrayList() : BenutzerKompetenz.getKompetenzenMitSchulform(schuljahr, gruppe, schulform);
	}

}

export const routeEinstellungen = new RouteEinstellungen();
