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
					v-model="selected"
					:columns="cols"
					:data="rows"
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
			key: "kuerzel",
			label: "Kuerzel",
			width: "6em",
			sortable: true,
			defaultSort: "asc"
		},
		{
			key: "bezeichnung",
			label: "Bezeichnung",
			sortable: true,
			span: 3
		}
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
