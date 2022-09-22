<template>
	<svws-ui-secondary-menu>
		<template #headline> Kursauswahl </template>
		<template #header> </template>
		<template #content>
			<div class="container">
				<svws-ui-table
					v-model:selected="selected"
					:cols="cols"
					:rows="rows"
					:footer="false"
					asc="true"
				/>
			</div>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">
	import { computed } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";
	import { KursListeEintrag } from "@svws-nrw/svws-core-ts";
	import { useAuswahlViaRoute } from '~/router/auswahlViaRoute';

	const cols = [
		{
			id: "kuerzel",
			title: "Kuerzel",
			width: "6em",
			sortable: true
		},
		{ id: "lehrer_name", title: "Fachlehrer", sortable: true },
		{ id: "jahrgang", title: "Jahrgang", sortable: true }
	];
	const main: Main = injectMainApp();
	const app = main.apps.kurse;
	const appLehrer = main.apps.lehrer;
	const appJahrgaenge = main.apps.jahrgaenge;

	// FIXME: Typing: const rows: ComputedRef<KursEintrag[] | undefined> = computed(() => {
	const rows = computed(() => {
		return app.auswahl.liste.map((e: KursListeEintrag) => ({
			...e,
			lehrer_name:
				appLehrer.auswahl.liste.find(l => l.id === e.lehrer)?.kuerzel ||
				"",
			jahrgang:
				appJahrgaenge.auswahl.liste
					.find(j =>
						e.idJahrgaenge
							.toArray(new Array<number>())
							.includes(j.id)
					)
					?.kuerzel?.toString() || ""
		}));
	});

	const selected = useAuswahlViaRoute('kurse')
</script>
