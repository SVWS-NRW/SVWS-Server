<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<div>
				<i-ri-arrow-left-line
					class="inline-block cursor-pointer"
					@click="router.push({ name: name })"
				/>Benutzerauswahl
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
	import type { BenutzerListeEintrag } from "@svws-nrw/svws-core-ts";
	import { computed, ComputedRef, Ref, ref } from "vue";
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
			key: "anzeigename",
			label: "Anzeigename",
			sortable: true
		},
		{
			key: "name",
			label: "Name",
			sortable: true
		}
	];

	const actions = [
		{ label: "Löschen", action: "delete" },
		{ label: "Kopieren", action: "copy" }
	];

	const search: Ref<string> = ref("");

	const main: Main = injectMainApp();
	const app = main.apps.benutzer;
	const name = ref("benutzerverwaltung");
	const rows: ComputedRef<BenutzerListeEintrag[] | undefined> = computed(
		() => {
			return app.auswahl.liste;
		}
	);

	const rowsFiltered: ComputedRef<BenutzerListeEintrag[] | undefined> =
		computed(() => {
			if (rows.value) {
				const rowsValue: BenutzerListeEintrag[] = rows.value;
				if (search.value) {
					return rowsValue.filter((e: BenutzerListeEintrag) =>
						e.name
							.toLocaleLowerCase()
							.includes(search.value.toLocaleLowerCase())
					);
				}
				return rowsValue;
			}
			return undefined;
		});

	const selected = useAuswahlViaRoute("benutzer");

	function onAction(props: Event) {
		console.log(props);
	}
</script>
