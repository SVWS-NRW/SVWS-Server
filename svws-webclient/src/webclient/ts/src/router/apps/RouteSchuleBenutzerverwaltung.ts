import { RouteLocationNormalized, RouteRecordRaw } from "vue-router";
import { injectMainApp } from "~/apps/Main";
import { RouteAppMeta } from "~/router/RouteUtils";
import { RouteSchuleBenutzerverwaltungBenutzer } from "~/router/apps/benutzerverwaltung/RouteSchuleBenutzerverwaltungBenutzer";
import { RouteSchuleBenutzerverwaltungBenutzergruppe } from "~/router/apps/benutzerverwaltung/RouteSchuleBenutzerverwaltungBenutzergruppe";

const ROUTE_NAME: string = "benutzerverwaltung";


export const RoutesBenutzerverwaltungChildren: RouteRecordRaw[] = [
	RouteSchuleBenutzerverwaltungBenutzer,
	RouteSchuleBenutzerverwaltungBenutzergruppe
];

export const RouteSchuleBenutzerverwaltung : RouteRecordRaw = {
	name: ROUTE_NAME,
	path: "/schule/benutzerverwaltung/:id(\\d+)?",
	components: {
		default: () => import("~/components/schule/benutzerverwaltung/SBenutzerverwaltungApp.vue"),
		liste: () => import("~/components/schule/benutzerverwaltung/SBenutzerverwaltungAuswahl.vue")
	},
	props: {
		default: (route) => routeSchuleBenutzerverwaltungPropsAuswahlID(route),
		liste: (route) => routeSchuleBenutzerverwaltungPropsAuswahlID(route)
	},
	meta: <RouteAppMeta<unknown, unknown>> {
		auswahl: () => {}
	},
	redirect: to => to.path + "/benutzer",
	children: RoutesBenutzerverwaltungChildren
}



/**
 * EineMethode zum Erzeugen der Properties Route in der Benutzerverwaltung
 * 
 * @param route     die aktuelle Route, für die die Properties erzeugt werden sollen
 * 
 * @returns das Objekt mit den Werten für die Properties
 */
export function routeSchuleBenutzerverwaltungPropsAuswahlID(route: RouteLocationNormalized) {
	const routename = route.name?.toString();
	if ((routename === undefined) || (route.params.id === undefined))
		return { id: undefined, item: undefined, routename: routename };
	const id = parseInt(route.params.id as string);
	const item = (routename === "benutzer")
		? injectMainApp().apps.benutzer.auswahl.liste.find(s => s.id === id)
		: injectMainApp().apps.benutzergruppe.auswahl.liste.find(s => s.id === id);
	if (item === undefined)
		return { id: undefined, item: undefined, routename: routename };
	return { id: id, item: item, routename: routename };
}
