<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<nav class="secondary-menu--breadcrumbs">
				<a @click="router.push({name: 'kataloge' })">Kataloge</a>
				<span>Fächer</span>
			</nav>
		</template>
		<template #header> </template>
		<template #content>
			<div class="container">
				<svws-ui-table v-model="selected" :columns="cols" :data="rows" :footer="false" />
			</div>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import { FaecherListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, ref } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";
	import { router } from "~/router"
	import { routeKatalogFaecher } from "~/router/apps/RouteKatalogFaecher";

	const props = defineProps<{ id?: number; item?: FaecherListeEintrag, routename: string }>();
	
	const selected = routeKatalogFaecher.auswahl;

	const main: Main = injectMainApp();
	const app = main.apps.faecher;

	const cols = ref([
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: 'asc' },
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true, span: 3 }
	]);

	const rows: ComputedRef<FaecherListeEintrag[] | undefined> = computed(() => app.auswahl.liste);

</script>
