<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<nav class="secondary-menu--breadcrumbs">
				<a @click="router.push({name: 'kataloge' })">Kataloge</a>
				<span>Jahrgänge</span>
			</nav>
		</template>
		<template #header> </template>
		<template #content>
			<div class="container">
				<svws-ui-table v-model="selected" :columns="cols" :data="rows" />
			</div>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import { JahrgangsListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";
	import { router } from "~/router"
	import { routeKatalogJahrgaenge } from "~/router/apps/RouteKatalogJahrgaenge";

	const props = defineProps<{ id?: number; item?: JahrgangsListeEintrag, routename: string }>();

	const selected = routeKatalogJahrgaenge.auswahl;

	const cols = [
		{ key: "kuerzel", label: "Kürzel", width: "6em", sortable: true, defaultSort: "asc" },
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true, span: 3 }
	];

	const main: Main = injectMainApp();
	const app = main.apps.jahrgaenge;

	const rows: ComputedRef<JahrgangsListeEintrag[] | undefined> = computed(() => { 
		return app.auswahl.liste; 
	});

</script>
