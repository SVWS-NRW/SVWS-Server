<template>
	<svws-ui-secondary-menu>
		<template #headline>Kurse</template>
		<template #header> </template>
		<template #content>
			<div class="container">
				<svws-ui-table v-model="selected" :columns="cols" :data="rows" :footer="false" />
			</div>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";
	import { KursListeEintrag } from "@svws-nrw/svws-core-ts";
	import { routeKurse } from "~/router/apps/RouteKurse";

	const props = defineProps<{ id?: number; item?: KursListeEintrag, routename: string }>();

	const cols = [
		{ key: "kuerzel", label: "Kürzel", width: "6em", sortable: true, defaultSort: "asc" },
		{ key: "lehrer_name", label: "Fachlehrer", sortable: true },
		{ key: "jahrgang", label: "Jahrgang", sortable: true }
	];
	const main: Main = injectMainApp();
	const app = main.apps.kurse;
	const appLehrer = main.apps.lehrer;
	const appJahrgaenge = main.apps.jahrgaenge;

	// FIXME: Typing: const rows: ComputedRef<KursEintrag[] | undefined> = computed(() => {
	const rows = computed(() => {
		return app.auswahl.liste.map((e: KursListeEintrag) => ({
			...e,
			lehrer_name: appLehrer.auswahl.liste.find(l => l.id === e.lehrer)?.kuerzel || "",
			jahrgang: appJahrgaenge.auswahl.liste.find(j => e.idJahrgaenge.toArray(new Array<number>()).includes(j.id))?.kuerzel?.toString() || ""
		}));
	});

	const selected = routeKurse.auswahl;

</script>
