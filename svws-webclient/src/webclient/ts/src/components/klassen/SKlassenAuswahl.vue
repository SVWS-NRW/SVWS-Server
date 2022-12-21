<template>
	<svws-ui-secondary-menu>
		<template #headline>Klassen</template>
		<template #header> </template>
		<template #content>
			<div class="container">
				<svws-ui-table v-model="selected" :columns="cols" :data="rows" :footer="false" />
			</div>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import type { KlassenListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, ref } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";
	import { RouteKlassen } from "~/router/apps/RouteKlassen";
	import { routeAppAuswahl } from "~/router/RouteUtils";

	const cols = ref([
		{ key: "kuerzel", label: "Kürzel", width: "6em", sortable: true, defaultSort: "asc" },
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true, span: 2 }
	]);
	const main: Main = injectMainApp();
	const app = main.apps.klassen;

	const rows: ComputedRef<KlassenListeEintrag[] | undefined> = computed(() => { return app.auswahl.liste; });

	const selected = routeAppAuswahl(RouteKlassen);

</script>
