<template>
	<svws-ui-secondary-menu>
		<template #headline> Klassenauswahl </template>
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
	import type { KlassenListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, ref } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";
	import { useAuswahlViaRoute } from '~/router/auswahlViaRoute';

	const cols = ref([
		{
			id: "kuerzel",
			title: "Kuerzel",
			width: "6em",
			sortable: true
		},
		{
			id: "bezeichnung",
			title: "Bezeichnung",
			sortable: true
		}
	]);
	const main: Main = injectMainApp();
	const app = main.apps.klassen;

	const rows: ComputedRef<KlassenListeEintrag[] | undefined> = computed(
		() => {
			return app.auswahl.liste;
		}
	);

	const selected = useAuswahlViaRoute('klassen')
</script>
