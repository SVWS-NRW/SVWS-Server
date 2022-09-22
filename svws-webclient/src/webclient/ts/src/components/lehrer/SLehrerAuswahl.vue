<template>
	<svws-ui-secondary-menu>
		<template #headline> Lehrerauswahl </template>
		<template #header>
			<div class="px-6 pt-3">
				<div class="mt-4">
					<svws-ui-text-input
						v-model="search"
						type="search"
						placeholder="Suche nach Namen oder Kürzel"
						><i-ri-search-line
					/></svws-ui-text-input>
				</div>
			</div>
		</template>
		<template #content>
			<div class="container">
				<svws-ui-table
					v-model:selected="selected"
					:cols="cols"
					:rows="rowsFiltered"
					:footer="false"
					:actions="actions"
					:asc="true"
					:multi-select="true"
					@action="onAction"
				/>
			</div>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">
	import type { LehrerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, Ref, ref } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";
	import { useAuswahlViaRoute } from '~/router/auswahlViaRoute';

	const cols = [
		{
			id: "kuerzel",
			title: "Kuerzel",
			width: "10%",
			sortable: true
		},
		{
			id: "nachname",
			title: "Nachname",
			width: "45%",
			sortable: true
		},
		{
			id: "vorname",
			title: "Vorname",
			width: "45%",
			sortable: true
		}
	];

	const actions = [
		{ label: "Löschen", action: "delete" },
		{ label: "Kopieren", action: "copy" }
	];

	const search: Ref<string> = ref("");

	const main: Main = injectMainApp();
	const app = main.apps.lehrer;

	const rows: ComputedRef<LehrerListeEintrag[] | undefined> = computed(() => {
		return app.auswahl.liste;
	});

	const rowsFiltered: ComputedRef<LehrerListeEintrag[] | undefined> =
		computed(() => {
			if (rows.value) {
				const rowsValue: LehrerListeEintrag[] = rows.value;
				if (search.value) {
					return rowsValue.filter(
						(e: LehrerListeEintrag) =>
							e.nachname
								.toLocaleLowerCase()
								.includes(search.value.toLocaleLowerCase()) ||
							e.vorname
								.toLocaleLowerCase()
								.includes(search.value.toLocaleLowerCase())
					);
				}
				return rowsValue;
			}
			return undefined;
		});

	const selected = useAuswahlViaRoute('lehrer');

	function onAction(props: Event) {
		console.log(props);
	}
</script>
