import { SchuelerListeEintrag } from "@svws-nrw/svws-core-ts";
import { computed, WritableComputedRef } from "vue";
import { RouteLocationNormalized, RouteRecordRaw, useRoute, useRouter } from "vue-router";
import { injectMainApp } from "~/apps/Main";


function getRouteSchuelerProps(route: RouteLocationNormalized) {
	if (route.params.id === undefined)
		return { id: undefined, item: undefined };
	const id = parseInt(route.params.id as string);
	const app = injectMainApp().apps.schueler;
	const item = app.auswahl.liste.find(s => s.id === id);
	return { id: id, item: item };
}

export const RouteSchueler : RouteRecordRaw = {
	name: "schueler",
	path: "/schueler/:id(\\d+)?",
	components: {
		default: () => import("~/components/schueler/SSchuelerApp.vue"),
		liste: () => import("~/components/schueler/SSchuelerAuswahl.vue")
	},
	props: {
		default: getRouteSchuelerProps,
		liste: getRouteSchuelerProps
	}
}

export function routeSchuelerAuswahl(): WritableComputedRef<SchuelerListeEintrag | undefined> {
	const router = useRouter();
	const route = useRoute();
	const app = injectMainApp().apps.schueler;
	const selected = computed({
		get(): SchuelerListeEintrag | undefined {
			if (route.params.id === undefined)
				return undefined;
			return app.auswahl.liste.find(s => s.id.toString() === route.params.id);
		},
		set(value: SchuelerListeEintrag | undefined) {
			app.auswahl.ausgewaehlt = value;
			router.push({ name: "schueler", params: { id: value?.id } });
		}
	});
	return selected;
}
