<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<div>
				<i-ri-arrow-left-line class="inline-block cursor-pointer" @click="router.push({name: RouteKataloge.name })" />
				Fächerauswahl
			</div>
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

	import type { FaecherListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, ref } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";
	import { router } from "~/router"
	import { RouteKataloge } from "~/router/apps/RouteKataloge";
	import { routeAppAuswahl } from "~/router/RouteUtils";
	import { RouteKatalogFaecher } from "~/router/apps/RouteKatalogFaecher";

	const selected = routeAppAuswahl(RouteKatalogFaecher);
	const main: Main = injectMainApp();
	const app = main.apps.faecher;

	const cols = ref([
		{ key: "kuerzel", label: "Kuerzel", sortable: true, defaultSort: 'asc' },
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true, span: 3 }
	]);

	const rows: ComputedRef<FaecherListeEintrag[] | undefined> = computed(() => app.auswahl.liste);

</script>
