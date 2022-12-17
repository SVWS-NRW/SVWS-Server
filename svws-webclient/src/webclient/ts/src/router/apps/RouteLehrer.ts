import { LehrerListeEintrag } from "@svws-nrw/svws-core-ts";
import { WritableComputedRef, computed } from "vue";
import { RouteLocationNormalized, RouteRecordRaw, useRoute, useRouter } from "vue-router";
import { injectMainApp } from "~/apps/Main";
import { RouteAppMeta } from "~/router/RouteAppMeta";


export const RouteLehrer : RouteRecordRaw = {
	name: "lehrer",
	path: "/lehrer/:id(\\d+)?",
	components: {
		default: () => import("~/components/lehrer/SLehrerApp.vue"),
		liste: () => import("~/components/lehrer/SLehrerAuswahl.vue")
	},
	props: {
		default: getRouteLehrerProps,
		liste: getRouteLehrerProps
	},
	meta: <RouteAppMeta<LehrerListeEintrag | undefined>> {
		auswahl: routeLehrerAuswahl
	}
};

function getRouteLehrerProps(route: RouteLocationNormalized) {
	if ((route.name !== "lehrer") || (route.params.id === undefined))
		return { id: undefined, item: undefined };
	const id = parseInt(route.params.id as string);
	const app = injectMainApp().apps.lehrer;
	const item = app.auswahl.liste.find(s => s.id === id);
	return { id: id, item: item };
}

function routeLehrerAuswahl(): WritableComputedRef<LehrerListeEintrag | undefined> {
	const router = useRouter();
	const route = useRoute();
	const app = injectMainApp().apps.lehrer;

	const selected = computed({
		get(): LehrerListeEintrag | undefined {
			if (route.params.id === undefined)
				return undefined;
			return app.auswahl.liste.find(s => s.id.toString() === route.params.id);
		},
		set(value: LehrerListeEintrag | undefined) {
			app.auswahl.ausgewaehlt = value;
			router.push({ name: "lehrer", params: { id: value?.id } });
		}
	});
	return selected;
}
