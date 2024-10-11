import type { RouteLocationNormalized, RouteLocationRaw, RouteParams } from "vue-router";

import type { BenutzerKompetenzGruppe, List } from "@core";
import { ArrayList, BenutzerKompetenz, DeveloperNotificationException, Schulform, ServerMode } from "@core";

import { api } from "~/router/Api";
import { RouteNode } from "~/router/RouteNode";
import { RouteManager } from "~/router/RouteManager";

import { routeApp, type RouteApp } from "~/router/apps/RouteApp";
import { routeSchuleJahrgaenge } from "~/router/apps/schule/jahrgaenge/RouteSchuleJahrgaenge";
import { routeSchuleFaecher } from "~/router/apps/schule/faecher/RouteSchuleFaecher";

import { RouteDataSchule } from "~/router/apps/schule/RouteDataSchule";

import type { SchuleAuswahlProps } from "~/components/schule/SSchuleAuswahlProps";
import type { TabData } from "@ui";
import type { SchuleAppProps } from "~/components/schule/SSchuleAppProps";
import { routeSchuleBetriebe } from "./betriebe/RouteSchuleBetriebe";
import { routeKatalogReligionen } from "./religionen/RouteKatalogReligionen";
import { routeKatalogEinwilligungsarten } from "./einwilligungsarten/RouteKatalogEinwilligungsarten";
import { routeKatalogVermerkarten } from "./vermerke/RouteKatalogVermerkarten";
import { routeKatalogFoerderschwerpunkte } from "./foerderschwerpunkte/RouteKatalogFoerderschwerpunkte";
import { routeKatalogSchulen } from "./schulen/RouteKatalogSchulen";
import { routeSchuleDatenaustauschKurs42 } from "./datenaustausch/kurs42/RouteSchuleDatenaustauschKurs42";
import { routeSchuleDatenaustauschENM } from "./datenaustausch/RouteSchuleDatenaustauschENM";
import { routeSchuleDatenaustauschLaufbahnplanung } from "./datenaustausch/RouteSchuleDatenaustauschLupo";
import { routeSchuleDatenaustauschSchulbewerbung } from "./datenaustausch/RouteSchuleDatenaustauschSchulbewerbung";
import { routeSchuleDatenaustauschWenom } from "./datenaustausch/RouteSchuleDatenaustauschWenom";
import { routeSchuleDatenaustauschUntis } from "./datenaustausch/untis/RouteSchuleDatenaustauschUntis";
import { routeSchuleStammdaten } from "./RouteSchuleStammdaten";

const SSchuleAuswahl = () => import("~/components/schule/SSchuleAuswahl.vue")
const SSchuleApp = () => import("~/components/schule/SSchuleApp.vue")

export class RouteSchule extends RouteNode<RouteDataSchule, RouteApp> {

	public constructor() {
		super(Schulform.values(), [ BenutzerKompetenz.KEINE ], "schule", "schule", SSchuleApp, new RouteDataSchule());
		super.mode = ServerMode.STABLE;
		super.propHandler = (route) => this.getProps(route);
		super.text = "Schule";
		super.setView("submenu", SSchuleAuswahl, (route) => this.getAuswahlProps(route));
		super.children = [
			routeSchuleStammdaten,
		];
		super.menu = [
			// Schulbezogen
			routeSchuleStammdaten,
			routeSchuleBetriebe,
			routeKatalogEinwilligungsarten,
			routeSchuleFaecher,
			routeKatalogFoerderschwerpunkte,
			routeSchuleJahrgaenge,
			routeKatalogVermerkarten,
			// Allgemein
			routeKatalogReligionen,
			routeKatalogSchulen,
			// Datenaustausch
			routeSchuleDatenaustauschENM,
			routeSchuleDatenaustauschWenom,
			routeSchuleDatenaustauschSchulbewerbung,
			routeSchuleDatenaustauschLaufbahnplanung,
			routeSchuleDatenaustauschKurs42,
			routeSchuleDatenaustauschUntis,
		];
		super.defaultChild = routeSchuleStammdaten;
	}

	protected async update(to: RouteNode<any, any>, to_params: RouteParams, from: RouteNode<any, any> | undefined, from_params: RouteParams, isEntering: boolean) : Promise<void | Error | RouteLocationRaw> {
		if (isEntering)
			await this.data.ladeDaten();
		if (to.name === this.name)
			return this.getRoute();
		if (!to.name.startsWith(this.data.view.name))
			for (const child of this.children)
				if (to.name.startsWith(child.name))
					this.data.setView(child, this.children);
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
			tabManager: () => this.createTabManagerByMenu(this.data.view.name, this.setTab),
			schule: () => api.schuleStammdaten,
		};
	}

	private setTab = async (value: TabData) => {
		const node = RouteNode.getNodeByName(value.name);
		if (node === undefined)
			throw new DeveloperNotificationException("Unbekannte Route");
		await RouteManager.doRoute({ name: value.name, params: { idSchuljahresabschnitt: routeApp.data.idSchuljahresabschnitt } });
		this.data.setView(node, routeSchule.menu);
	}

	public benutzerKompetenzen = (gruppe : BenutzerKompetenzGruppe) : List<BenutzerKompetenz> => {
		const schuljahr = routeApp.data.aktAbschnitt.value.schuljahr;
		const schulformEintrag = api.schulform.daten(schuljahr);
		const schulform = Schulform.data().getWertByID(schulformEintrag?.id ?? -1);
		return (schulform === null) ? new ArrayList() : BenutzerKompetenz.getKompetenzenMitSchulform(schuljahr, gruppe, schulform);
	}

}

export const routeSchule = new RouteSchule();
