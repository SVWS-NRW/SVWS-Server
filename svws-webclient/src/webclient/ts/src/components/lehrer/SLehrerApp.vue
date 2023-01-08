<template>
	<div v-if="visible">
		<svws-ui-header>
			<div class="flex items-center">
				<div class="w-16 mr-4 -ml-2">
					<svws-ui-avatar :src="'data:image/png;base64, ' + foto" :alt="foto ? 'Foto ' + inputVorname + ' ' + inputNachname : ''" />
				</div>
				<div>
					<span class="inline-block mr-3">{{ inputTitel }} {{ inputVorname }} {{ inputNachname }}</span>
					<svws-ui-badge type="light">ID: {{ item?.id }}</svws-ui-badge>
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
	import { computed, ComputedRef, ShallowRef } from "vue";
	import { DataLehrerStammdaten } from "~/apps/lehrer/DataLehrerStammdaten";
	import { DataSchuleStammdaten } from "~/apps/schule/DataSchuleStammdaten";
	import { routeLehrer } from "~/router/apps/RouteLehrer";

	const { item, stammdaten } = defineProps<{ 
		item: ShallowRef<LehrerListeEintrag | undefined>;
		stammdaten: DataLehrerStammdaten;
		schule: DataSchuleStammdaten;
	}>();

	const selectedRoute = routeLehrer.getChildRouteSelector();

	const foto: ComputedRef<String | undefined> = computed(() => stammdaten.daten?.foto || undefined);

	const inputNachname: ComputedRef<string | undefined> = computed(() => item.value?.nachname.toString());
	const inputVorname: ComputedRef<string | undefined> = computed(() => item.value?.vorname.toString());
	const inputKuerzel: ComputedRef<string | undefined> = computed(() => item.value?.kuerzel.toString());
	const inputTitel: ComputedRef<string | undefined> = computed(() => item.value?.titel?.toString());

	const visible: ComputedRef<boolean> = computed(() => {
		return (item.value !== undefined) && (!routeLehrer.hidden);
	});

</script>
