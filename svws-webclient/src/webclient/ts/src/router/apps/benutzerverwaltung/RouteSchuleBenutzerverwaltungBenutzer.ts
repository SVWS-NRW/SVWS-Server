import { BenutzerListeEintrag } from "@svws-nrw/svws-core-ts";
import { computed, WritableComputedRef } from "vue";
import { RouteRecordRaw, useRoute, useRouter } from "vue-router";
import { BaseList } from "~/apps/BaseList";
import { injectMainApp } from "~/apps/Main";
import { RouteAppMeta, routePropsAuswahlID } from "~/router/RouteUtils";

const ROUTE_NAME: string = "benutzer";

export const RouteSchuleBenutzerverwaltungBenutzer : RouteRecordRaw = {
	name: ROUTE_NAME,
	path: "/schule/benutzerverwaltung/:id(\\d+)?/benutzer",
	component: () => import("~/components/schule/benutzerverwaltung/daten/SBenutzer.vue"),
	props: (route) => routePropsAuswahlID(route, injectMainApp().apps.benutzer.auswahl),
	meta: <RouteAppMeta<BenutzerListeEintrag  | undefined>> {
		auswahl: () => routeBenutzerverwaltungBenutzerAuswahlID(ROUTE_NAME, injectMainApp().apps.benutzer.auswahl),
		text: "Benutzer"
	}
}


/**
 * Eine Methode für Routen-Einträge zum Erzeugen der Computed-Property bei der
 * Auswahl einer Route eines Routen-Eintrags.
 * 
 * Dies ist eine Hilfsmethode für die Nutzung beim Definieren von Routen-Einträgen.
 * 
 * @param name      der Name des Routen-Eintrags, so dass dieser beim Setzen einer Route aktualisiert werden kann
 * @param auswahl   die Liste der Auswahl (siehe auch BaseList)
 * 
 * @returns die Computed-Property
 */
export function routeBenutzerverwaltungBenutzerAuswahlID<TItem extends { id: number }, TAuswahl extends BaseList<TItem, unknown>>(name: string, auswahl: TAuswahl) : WritableComputedRef<TItem | undefined> {
	const router = useRouter();
	const route = useRoute();

	const selected = computed({
		get(): TItem | undefined {
			if (route.params.id === undefined)
				return undefined;
			if (route.name?.toString() !== name)
				return undefined;
			if ((auswahl.ausgewaehlt === undefined) || (auswahl.ausgewaehlt.id.toString() !== route.params.id))
				auswahl.ausgewaehlt = auswahl.liste.find(s => s.id.toString() === route.params.id);
			return auswahl.ausgewaehlt;
		},
		set(value: TItem | undefined) {
			auswahl.ausgewaehlt = value;
			router.push({ name: name, params: { id: value?.id } });
		}
	});
	return selected;
}
