<template>
	<div v-if="data.stammdaten.daten && props?.id">
		<svws-ui-header>
			<div class="flex items-center">
				<div class="w-16 mr-4 -ml-2">
					<svws-ui-avatar :src="'data:image/png;base64, ' + foto" :alt="foto ? 'Foto ' + inputVorname + ' ' + inputNachname : ''" />
				</div>
				<div>
					<span class="inline-block mr-3">{{ inputTitel }} {{ inputVorname }} {{ inputNachname }}</span>
					<svws-ui-badge variant="light">ID: {{ props.id }}</svws-ui-badge>
					<br/>
					<span class="opacity-50">{{ inputKuerzel }}</span>
				</div>
			</div>
		</svws-ui-header>
		<svws-ui-router-tab-bar :routes="routeLehrer.children_records" :hidden="routeLehrer.children_hidden" v-model="selectedRoute">
			<router-view />
		</svws-ui-router-tab-bar>
	</div>
	<div v-else class="app-layout--main--placeholder">
		<i-ri-briefcase-line/>
	</div>
</template>

<script setup lang="ts">

	import { LehrerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, onMounted, WritableComputedRef } from "vue";
	import { RouteRecordRaw, useRoute, useRouter } from "vue-router";

	import { injectMainApp, Main } from "~/apps/Main";
	import { routeLehrerIndividualdaten } from "~/router/apps/lehrer/RouteLehrerIndividualdaten";
	import { routeLehrer, RouteDataLehrer } from "~/router/apps/RouteLehrer";

	const main: Main = injectMainApp();
	const app = main.apps.lehrer;

	const props = defineProps<{ id?: number; item?: LehrerListeEintrag, routename: string }>();

	// Initialisiere die Sub-Routen
	const router = useRouter();
	const route = useRoute();
//	const redirect = routeAppMeta(RouteLehrer).redirect;
	const data: RouteDataLehrer = routeLehrer.data;
/*	routeLehrerSetRedirect(route);
	
	onMounted(() => {
		if (((route.params.id === undefined) || (route.params.id === "")) && (app.auswahl.liste.length > 0))
			router.push({ name: redirect.value.name?.toString(), params: { id: app.auswahl.liste[0].id } });
	});
*/
	const selectedRoute: WritableComputedRef<RouteRecordRaw> = computed({
		get(): RouteRecordRaw {
			return routeLehrer.selectedChildRecord || routeLehrerIndividualdaten.record;
		},
		set(value: RouteRecordRaw) {
			routeLehrer.selectedChildRecord = value;
			router.push({ name: value.name, params: { id: props.id } });
		}
	});

	const foto: ComputedRef<String | undefined> = computed(() => {
		return data.stammdaten.daten?.foto || undefined;
	});

	const inputNachname: ComputedRef<string | undefined> = computed(() => {
		return props.item?.nachname.toString();
	});

	const inputVorname: ComputedRef<string | undefined> = computed(() => {
		return props.item?.vorname.toString();
	});

	const inputKuerzel: ComputedRef<string | undefined> = computed(() => {
		return props.item?.kuerzel.toString();
	});

	const inputTitel: ComputedRef<string | undefined> = computed(() => {
		return props.item?.titel?.toString();
	});

</script>
