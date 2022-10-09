<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<div>
				<i-ri-arrow-left-line
					class="inline-block cursor-pointer"
					@click="router.push({ name: name })"
				/>Benutzergruppenauswahl
			</div>
		</template>
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
				<svws-ui-new-table
					v-model="selected"
					v-model:selection="selection"
					:data="rowsFiltered"
					:columns="cols"
					is-multi-select
				/>
				<!-- <svws-ui-table
					v-model:selected="selected"
					:cols="cols"
					:rows="rowsFiltered"
					:footer="false"
					:actions="actions"
					:asc="true"
					:multi-select="true"
					@action="onAction"
				/> -->
			</div>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">
	import type {
		BenutzergruppeListeEintrag,
		BenutzerListeEintrag
	} from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, onMounted, onUpdated, Ref, ref } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";
	import { useAuswahlViaRoute } from "~/router/auswahlViaRoute";
	import { router } from "~/router";

	const cols = [
		{
			key: "id",
			label: "ID",
			sortable: true
		},
		{
			key: "bezeichnung",
			label: "Bezeichnung",
			sortable: true
		}
	];

	const actions = [
		{ label: "Löschen", action: "delete" },
		{ label: "Kopieren", action: "copy" }
	];

	const search: Ref<string> = ref("");

	const main: Main = injectMainApp();
	const app = main.apps.benutzergruppe;
	const name = ref("benutzerverwaltung");
	const rows: ComputedRef<BenutzergruppeListeEintrag[] | undefined> =
		computed(() => {
			return app.auswahl.liste;
		});

	const rowsFiltered: ComputedRef<BenutzergruppeListeEintrag[] | undefined> =
		computed(() => {
			if (rows.value) {
				const rowsValue: BenutzergruppeListeEintrag[] = rows.value;
				if (search.value) {
					return rowsValue.filter((e: BenutzergruppeListeEintrag) =>
						e.bezeichnung
							.toLocaleLowerCase()
							.includes(search.value.toLocaleLowerCase())
					);
				}
				return rowsValue;
			}
			return undefined;
		});

	const selected = useAuswahlViaRoute("benutzergruppe");
	const selection = ref([]);
</script>
