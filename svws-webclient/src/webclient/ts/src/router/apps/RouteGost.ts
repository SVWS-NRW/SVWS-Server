import { GostJahrgang } from "@svws-nrw/svws-core-ts";
import { computed, WritableComputedRef } from "vue";
import { RouteLocationNormalized, RouteRecordRaw, useRoute, useRouter } from "vue-router";
import { BaseList } from "~/apps/BaseList";
import { injectMainApp } from "~/apps/Main";
import { RouteAppMeta } from "~/router/RouteUtils";

const ROUTE_NAME: string = "gost";

export const RouteGost : RouteRecordRaw = {
	name: "gost",
	path: "/gost/:abiturjahr(-?\\d+)?",
	components: {
		default: () => import("~/components/gost/SGostApp.vue"),
		liste: () => import("~/components/gost/SGostAuswahl.vue")
	},
	props: {
		default: (route) => routePropsGostAuswahl(route, ROUTE_NAME, injectMainApp().apps.gost.auswahl),
		liste: (route) => routePropsGostAuswahl(route, ROUTE_NAME, injectMainApp().apps.gost.auswahl)
	},
	meta: <RouteAppMeta<GostJahrgang | undefined>> {
		auswahl: () => routeGostAuswahl(ROUTE_NAME, injectMainApp().apps.gost.auswahl)
	}
}


/**
 * Eine Methode zum Erzeugen der Properties "abiturjahr" und "item" zu einer Route bei 
 * der Gost-Auswahl-Liste, welche ein numerisches abiturjahr-Attribut hat.
 * 
 * @param route     die aktuelle Route, für die die Properties erzeugt werden sollen
 * @param name      der Name des Routen-Eintrages, für welche die Properties erzeugt werden sollen.
 *                  Dieser wird genutzt, um zu prüfen, ob die übergebene Route zu dem Routen-Eintrag passt
 * @param auswahl   die Liste der Auswahl (siehe auch BaseList)
 * 
 * @returns das Objekt mit den Werten für die Properties
 */
function routePropsGostAuswahl<TAuswahl extends BaseList<{ abiturjahr: number }, unknown>>(route: RouteLocationNormalized, name: string, auswahl: TAuswahl) {
	if ((route.name !== name) || (route.params.id === undefined))
		return { abiturjahr: undefined, item: undefined };
	const abiturjahr = parseInt(route.params.abiturjahr as string);
	const item = auswahl.liste.find(s => s.abiturjahr === abiturjahr);
	return { abiturjahr: abiturjahr, item: item };
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
			return auswahl.liste.find(s => s.abiturjahr.toString() === route.params.abiturjahr);
		},
		set(value: TItem | undefined) {
			auswahl.ausgewaehlt = value;
			router.push({ name: name, params: { abiturjahr: value?.abiturjahr } });
		}
	});
	return selected;
}
