<template>
	<svws-ui-secondary-menu>
		<template #headline
			><div>
				<i-ri-arrow-left-line
					class="inline-block cursor-pointer"
					@click="router.push({ name: 'kataloge' })"
				/>
				Jahrgangsauswahl
			</div>
		</template>
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
	import { JahrgangsListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";
	import { router } from "~/router"
	import { useAuswahlViaRoute } from '~/router/auswahlViaRoute';

	const cols = [
		{
			id: "kuerzel",
			title: "Kuerzel",
			width: "6em",
			sortable: true
		},
		{ id: "bezeichnung", title: "Bezeichnung", sortable: true }
	];
	const main: Main = injectMainApp();
	const app = main.apps.jahrgaenge;

	const rows: ComputedRef<JahrgangsListeEintrag[] | undefined> = computed(
		() => {
			return app.auswahl.liste;
		}
	);

	const selected = useAuswahlViaRoute('jahrgaenge');
</script>
