<template>
	<div v-if="app?.stammdaten.daten" class="flex w-full flex-col h-full">
		<svws-ui-header>
			<div class="flex items-center">
				<div class="w-16 mr-4 -ml-2">
					<svws-ui-avatar :src="'data:image/png;base64, ' + foto" :alt="foto ? 'Foto ' + vorname + ' ' + nachname : ''" />
				</div>
				<div>
					<span class="inline-block mr-3"> {{ vorname }} {{ nachname }} </span>
					<svws-ui-badge variant="light"> {{ props.id }} </svws-ui-badge>
					<br/>
					<span class="opacity-50"> {{ inputKlasse }} </span>
				</div>
			</div>
		</svws-ui-header>
		<svws-ui-router-tab-bar :routes="RouteSchuelerChildren" :hidden="routeAppAreHidden(RouteSchuelerChildren)" v-model="selectedRoute">
			<router-view />
		</svws-ui-router-tab-bar>
	</div>
	<div v-else class="app-layout--main--placeholder">
		<i-ri-briefcase-line/>
	</div>
</template>

<script setup lang="ts">

	import { computed, ComputedRef, onMounted, WritableComputedRef } from "vue";
	import { RouteRecordRaw, useRoute, useRouter } from "vue-router";

	import { injectMainApp, Main } from "~/apps/Main";
	import { RouteSchueler, RouteSchuelerChildren, RouteDataSchueler, routeSchuelerSetRedirect } from "~/router/apps/RouteSchueler";
	import { routeAppData, routeAppMeta, routeAppAreHidden } from "~/router/RouteUtils";
	import { SchuelerListeEintrag } from "@svws-nrw/svws-core-ts";

	const main: Main = injectMainApp();
	const app = main.apps.schueler;

	const props = defineProps<{ id?: number; item?: SchuelerListeEintrag, routename: string }>();

	// Initialisiere die Sub-Routen
	const router = useRouter();
	const route = useRoute();
	const redirect = routeAppMeta(RouteSchueler).redirect;
	const data: RouteDataSchueler = routeAppData(RouteSchueler);
	routeSchuelerSetRedirect(route);
	
	onMounted(() => {
		if (((route.params.id === undefined) || (route.params.id === "")) && (app.auswahl.liste.length > 0))
			router.push({ name: redirect.value.name?.toString(), params: { id: app.auswahl.liste[0].id } });
	});

	const selectedRoute: WritableComputedRef<RouteRecordRaw> = computed({
		get(): RouteRecordRaw {
			return redirect.value;
		},
		set(value: RouteRecordRaw) {
			redirect.value = value;
			router.push({ name: value.name, params: { id: props.id } });
		}
	});


	const foto: ComputedRef<String | undefined> = computed(() => {
		return data.stammdaten.daten?.foto || undefined;
	});

	const nachname: ComputedRef<string | undefined> = computed(() => {
		return props.item?.nachname.toString();
	});

	const vorname: ComputedRef<string | undefined> = computed(() => {
		return props.item?.vorname.toString();
	});

	const inputKlasse: ComputedRef<string | false> = computed(() => {
		if (props.item === undefined)
			return false;
		const id = props.item.idKlasse;
		const klasse = main.apps.klassen.auswahl.liste.find(k => k.id === id);
		return klasse?.kuerzel?.toString() || false;
	});

</script>
