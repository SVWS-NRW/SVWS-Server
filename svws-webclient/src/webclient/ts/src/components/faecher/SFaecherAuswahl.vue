<template>
	<svws-ui-secondary-menu>
		<template #headline
			><div>
				<i-ri-arrow-left-line
					class="inline-block cursor-pointer"
					@click="router.push({name: 'kataloge' })"
				/>
				Fächerauswahl
			</div>
		</template>
		<template #header> </template>
		<template #content>
			<div class="container">
				<svws-ui-table
					v-model="selected"
					:columns="cols"
					:data="rows"
					:footer="false"
				/>
			</div>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">
	import type { FaecherListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, ref } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";
	import { useAuswahlViaRoute } from '~/router/auswahlViaRoute';
	import { router } from "~/router"
	
	const selected = useAuswahlViaRoute('faecher')
	const main: Main = injectMainApp();
	const app = main.apps.faecher;

	const cols = ref([
		{
			key: "kuerzel",
			label: "Kuerzel",
			sortable: true,
			defaultSort: 'asc'
		},
		{
			key: "bezeichnung",
			label: "Bezeichnung",
			sortable: true
		}
	]);

	const rows: ComputedRef<FaecherListeEintrag[] | undefined> =
		computed(() => app.auswahl.liste);
</script>
