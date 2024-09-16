import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import type { BenutzerKompetenzGruppe, List } from "@core";
import { ArrayList, BenutzerKompetenz, DeveloperNotificationException, Schulform, ServerMode } from "@core";

import { api } from "~/router/Api";
import { RouteNode } from "~/router/RouteNode";
import { RouteManager } from "~/router/RouteManager";

import { routeApp, type RouteApp } from "~/router/apps/RouteApp";
import { routeSchuleBenutzer } from "~/router/apps/schule/benutzer/RouteSchuleBenutzer";
import { routeSchuleBenutzergruppe } from "~/router/apps/schule/benutzergruppen/RouteSchuleBenutzergruppe";
import { routeSchuleJahrgaenge } from "~/router/apps/schule/jahrgaenge/RouteSchuleJahrgaenge";
import { routeSchuleFaecher } from "~/router/apps/schule/faecher/RouteSchuleFaecher";
import { routeSchuleDatenaustausch } from "~/router/apps/schule/datenaustausch/RouteSchuleDatenaustausch";

import { RouteDataSchule } from "~/router/apps/schule/RouteDataSchule";

import type { SchuleAuswahlProps } from "~/components/schule/SSchuleAuswahlProps";
import type { AuswahlChildData } from "~/components/AuswahlChildData";
import type { SchuleAppProps } from "~/components/schule/SSchuleAppProps";
import { routeSchuleBetriebe } from "./betriebe/RouteSchuleBetriebe";
import { routeSchuleKataloge } from "./kataloge/RouteSchuleKataloge";

const SSchuleAuswahl = () => import("~/components/schule/SSchuleAuswahl.vue")
const SSchuleApp = () => import("~/components/schule/SSchuleApp.vue")

export class RouteSchule extends RouteNode<RouteDataSchule, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "schule", "schule", SSchuleApp, new RouteDataSchule());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Schule";
		super.setView("liste", SSchuleAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [];
		super.menu = [
			// TODO { title: "Schule bearbeiten", value: "schule_bearbeiten" },
			// TODO { title: "Einstellungen", value: "einstellungen" },
			// TODO { title: "Datensicherung", value: "datensicherung" },
			// TODO { title: "Schuljahreswechsel", value: "schuljahreswechsel" },
			// TODO { title: "Werkzeuge", value: "werkzeuge" },
			routeSchuleBenutzer,
			routeSchuleBenutzergruppe,
			routeSchuleJahrgaenge,
			routeSchuleFaecher,
			routeSchuleBetriebe,
			routeSchuleKataloge,
			routeSchuleDatenaustausch,
		];
		super.defaultChild = undefined;
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
		if (isEntering)
			await this.data.ladeDaten();
	}

	public async leave(from: RouteNode<any, any>, from_params: RouteParams): Promise<void> {
		await this.data.entferneDaten();
	}

	public getRoute() : RouteLocationRaw {
		return { name: this.defaultChild!.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt }};
	}

	public getProps(to: RouteLocationNormalized): SchuleAppProps {
		return {
			schule: () => api.schuleStammdaten,
			patch: this.data.patch,
			smptServerKonfiguration: () => this.data.smtpServerKonfiguration,
			patchSMTPServerKonfiguration: this.data.patchSMTServerKonfiguration,
			benutzerIstAdmin: api.benutzerIstAdmin,
			benutzerKompetenzen: api.benutzerKompetenzen,
		};
	}

	public getAuswahlProps(to: RouteLocationNormalized): SchuleAuswahlProps {
		return {
			schuljahresabschnittsauswahl: () => routeApp.data.getSchuljahresabschnittsauswahl(false),
			setChild: this.setChild,
			child: this.getChild(),
			children: this.getChildData(),
			childrenHidden: this.children_hidden().value,
			schule: () => api.schuleStammdaten,
		};
	}

	private getChild(): AuswahlChildData {
		return { name: this.data.view.name, text: this.data.view.text };
	}

	private getChildData(): AuswahlChildData[] {
		const result: AuswahlChildData[] = [];
		for (const c of this.menu)
			if (c.hatEineKompetenz() && c.hatSchulform())
				result.push({ name: c.name, text: c.text });
		return result;
	}

	private setChild = async (value: AuswahlChildData) => {
		const node = RouteNode.getNodeByName(value.name);
		if (node === undefined)
			throw new DeveloperNotificationException("Unbekannte Route");
		await RouteManager.doRoute({ name: value.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt } });
		this.data.setView(node, routeSchule.menu);
	}

	gotoSchule = async () => await RouteManager.doRoute({ name: this.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt } });

	public benutzerKompetenzen = (gruppe : BenutzerKompetenzGruppe) : List<BenutzerKompetenz> => {
		const schuljahr = routeApp.data.aktAbschnitt.value.schuljahr;
		const schulformEintrag = api.schulform.daten(schuljahr);
		const schulform = Schulform.data().getWertByID(schulformEintrag?.id ?? -1);
		return (schulform === null) ? new ArrayList() : BenutzerKompetenz.getKompetenzenMitSchulform(schuljahr, gruppe, schulform);
	}

}

export const routeSchule = new RouteSchule();
