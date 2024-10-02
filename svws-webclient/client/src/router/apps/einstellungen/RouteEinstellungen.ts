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
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "einstellungen", "einstellungen", SEinstellungen, new RouteDataEinstellungen());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Einstellungen";
		super.setView("liste", SEinstellungenAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [];
		super.menu = [
			routeEinstellungenBenutzer,
			routeEinstellungenBenutzergruppe,
		];
		super.defaultChild = undefined;
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
	}

	public async leave(from: RouteNode<any, any>, from_params: RouteParams): Promise<void> {
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
			setChild: this.setChild,
			child: this.getChild(),
			children: this.getChildData(),
			childrenHidden: this.children_hidden().value,
		};
	}

	private getChild(): TabData {
		return { name: this.data.view.name, text: this.data.view.text };
	}

	private getChildData(): TabData[] {
		const result: TabData[] = [];
		for (const c of this.menu)
			if (c.hatEineKompetenz() && c.hatSchulform())
				result.push({ name: c.name, text: c.text });
		return result;
	}

	private setChild = async (value: TabData) => {
		const node = RouteNode.getNodeByName(value.name);
		if (node === undefined)
			throw new DeveloperNotificationException("Unbekannte Route");
		await RouteManager.doRoute({ name: value.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt } });
		this.data.setView(node, routeEinstellungen.menu);
	}

	gotoEinstellungen = async () => await RouteManager.doRoute({ name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt } });

	public benutzerKompetenzen = (gruppe : BenutzerKompetenzGruppe) : List<BenutzerKompetenz> => {
		const schuljahr = routeApp.data.aktAbschnitt.value.schuljahr;
		const schulformEintrag = api.schulform.daten(schuljahr);
		const schulform = Schulform.data().getWertByID(schulformEintrag?.id ?? -1);
		return (schulform === null) ? new ArrayList() : BenutzerKompetenz.getKompetenzenMitSchulform(schuljahr, gruppe, schulform);
	}

}

export const routeEinstellungen = new RouteEinstellungen();
