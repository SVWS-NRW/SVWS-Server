import { GostJahrgang } from "@svws-nrw/svws-core-ts";
import { computed, ref, WritableComputedRef } from "vue";
import { RouteLocationNormalized, RouteLocationNormalizedLoaded, RouteRecordRaw, useRoute, useRouter } from "vue-router";
import { BaseList } from "~/apps/BaseList";
import { DataGostJahrgang } from "~/apps/gost/DataGostJahrgang";
import { injectMainApp } from "~/apps/Main";
import { routeAppData, routeAppMeta, RouteAppMeta } from "~/router/RouteUtils";
import { RouteGostFachwahlen } from "./gost/RouteGostFachwahlen";
import { RouteGostFaecher } from "./gost/RouteGostFaecher";
import { RouteGostJahrgangsdaten } from "./gost/RouteGostJahrgangsdaten";
import { RouteGostKlausurplanung } from "./gost/RouteGostKlausurplanung";
import { RouteGostKursplanung } from "./gost/RouteGostKursplanung";

const ROUTE_NAME: string = "gost";

export const RouteGostChildren: RouteRecordRaw[] = [
	RouteGostJahrgangsdaten,
	RouteGostFaecher,
	RouteGostFachwahlen,
	RouteGostKursplanung,
	RouteGostKlausurplanung
];


export interface RouteDataGost {
	item: GostJahrgang | undefined;
	jahrgangsdaten: DataGostJahrgang;
}

export const RouteGost : RouteRecordRaw = {
	name: "gost",
	path: "/gost/:abiturjahr(-?\\d+)?",
	components: {
		default: () => import("~/components/gost/SGostApp.vue"),
		liste: () => import("~/components/gost/SGostAuswahl.vue")
	},
	props: {
		default: (route) => {
			const prop = routePropsGostAuswahl(route, injectMainApp().apps.gost.auswahl);
			const data: RouteDataGost = routeAppData(RouteGost);
			if (prop.item !== data.item) {
				if (prop.item === undefined) {
					data.item = undefined;
					data.jahrgangsdaten.unselect();
				} else {
					data.item = prop.item as GostJahrgang;
					data.jahrgangsdaten.select(data.item);
				}
			}
			return prop;
		},
		liste: (route) => routePropsGostAuswahl(route, injectMainApp().apps.gost.auswahl)
	},
	meta: <RouteAppMeta<GostJahrgang | undefined, unknown>> {
		auswahl: () => routeGostAuswahl(ROUTE_NAME, injectMainApp().apps.gost.auswahl),
		hidden: () => false,
		redirect: ref(RouteGostJahrgangsdaten),
		data: <RouteDataGost>{
			item: undefined,
			jahrgangsdaten: new DataGostJahrgang()
		}
	},
	redirect: to => {
		return to.path + "/" + routeAppMeta(RouteGost)?.redirect.value.path;
	},
	children: RouteGostChildren
}


export function routeGostSetRedirect(route : RouteLocationNormalizedLoaded) {
	const meta = RouteGost.meta as RouteAppMeta<GostJahrgang | undefined, RouteDataGost>;
	if ((RouteGost.children === undefined) || (meta === undefined))
		return;
	for (var child of RouteGost.children) {
		if (route.name === child.name) {
			meta.redirect.value = child;
			return;
		}
	}
	meta.redirect.value = RouteGostJahrgangsdaten;
}


/**
 * Eine Methode zum Erzeugen der Properties "abiturjahr" und "item" zu einer Route bei 
 * der Gost-Auswahl-Liste, welche ein numerisches abiturjahr-Attribut hat.
 * 
 * @param route     die aktuelle Route, für die die Properties erzeugt werden sollen
 * @param auswahl   die Liste der Auswahl (siehe auch BaseList)
 * 
 * @returns das Objekt mit den Werten für die Properties
 */
export function routePropsGostAuswahl<TAuswahl extends BaseList<{ abiturjahr: number }, unknown>>(route: RouteLocationNormalized, auswahl: TAuswahl) {
	if ((auswahl === undefined) || (route.params.abiturjahr === undefined))
		return { id: undefined, item: undefined, routename: route.name?.toString() };
	const abiturjahr = parseInt(route.params.abiturjahr as string);
	const item = auswahl.liste.find(s => s.abiturjahr === abiturjahr);
	return { id: abiturjahr, item: item, routename: route.name?.toString() };
}


/**
 * Eine Methode für Routen-Einträge zum Erzeugen der Computed-Property bei der
 * Gost-Auswahl einer Route eines Routen-Eintrags.
 * 
 * @param name      der Name des Routen-Eintrags, so dass dieser beim Setzen einer Route aktualisiert werden kann
 * @param auswahl   die Liste der Auswahl (siehe auch BaseList)
 * 
 * @returns die Computed-Property
 */
function routeGostAuswahl<TItem extends { abiturjahr: number }, TAuswahl extends BaseList<TItem, unknown>>(name: string, auswahl: TAuswahl) : WritableComputedRef<TItem | undefined> {
	const router = useRouter();
	const route = useRoute();

	const selected = computed({
		get(): TItem | undefined {
			if (route.params.abiturjahr === undefined)
				return undefined;
			let tmp = auswahl.ausgewaehlt;
			if ((tmp === undefined) || (tmp.abiturjahr.toString() !== route.params.abiturjahr))
				tmp = auswahl.liste.find(s => s.abiturjahr.toString() === route.params.abiturjahr);
			return tmp;
		},
		set(value: TItem | undefined) {
			auswahl.ausgewaehlt = value;
			router.push({ name: name, params: { abiturjahr: value?.abiturjahr } });
		}
	});
	return selected;
}
