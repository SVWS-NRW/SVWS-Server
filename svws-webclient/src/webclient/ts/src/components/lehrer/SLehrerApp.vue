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
	import { computed, ComputedRef } from "vue";

	import { routeLehrer, RouteDataLehrer } from "~/router/apps/RouteLehrer";

	const props = defineProps<{ id?: number; item?: LehrerListeEintrag, routename: string }>();

	const data: RouteDataLehrer = routeLehrer.data;
	const selectedRoute = routeLehrer.getChildRouteSelector();

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
