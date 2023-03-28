<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<nav class="secondary-menu--breadcrumbs">
				<a title="Schule" @click="router.push({ name: routeSchule.name })">Schule</a>
				<span title="Benutzer">Benutzer</span>
			</nav>
		</template>
		<template #header>
			<div class="mb-2">
				<svws-ui-text-input v-model="search" type="search" placeholder="Suche nach Namen oder Kürzel">
					<i-ri-search-line />
				</svws-ui-text-input>
			</div>
		</template>
		<template #content>
			<svws-ui-data-table :clicked="auswahl()" @update:clicked="gotoBenutzer" v-model="selectedItems" :items="rowsFiltered.values()"
				:columns="cols" clickable selectable :footer="true" :unique-key="String(auswahl()?.id)">
				<!-- Footer mit Button zum Hinzufügen einer Zeile -->
				<template #footerActions>
					<s-modal-benutzer-neu :show-delete-icon="selectedItems.length > 0" :create-benutzer-allgemein="createBenutzerAllgemein"
						:delete-benutzer-allgemein="deleteMultipleUser" />
				</template>
			</svws-ui-data-table>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import { BenutzerListeEintrag, List } from "@svws-nrw/svws-core";
	import { computed, ComputedRef, Ref, ref } from "vue";
	import { routeSchule } from "~/router/apps/RouteSchule";
	import { router } from "~/router/RouteManager";
	import { BenutzerAuswahlProps } from "./SBenutzerAuswahlProps";

	const selectedItems: Ref<BenutzerListeEintrag[]> = ref([]);

	const props = defineProps<BenutzerAuswahlProps>();

	const cols = [
		{ key: "id", label: "ID", sortable: true, align: "right", span: 0.5 },
		{ key: "anzeigename", label: "Anzeigename", sortable: true, span: 2 },
		{ key: "name", label: "Name", sortable: true, span: 2}
	];

	const search: Ref<string> = ref("");

	const rowsFiltered: ComputedRef<Map<number, BenutzerListeEintrag>> = computed(() => {
		console.log("rowsFiltered--");
		if (!search.value)
			return props.mapBenutzer;
		const result = new Map<number, BenutzerListeEintrag>();
		for (const l of props.mapBenutzer.values()) {
			if (l.name.toLocaleLowerCase().includes(search.value.toLocaleLowerCase()) ||
				l.anzeigename.toLocaleLowerCase().includes(search.value.toLocaleLowerCase()))
				result.set(l.id, l);
		}
		return result;
	});

	async function deleteMultipleUser() {
		const items = selectedItems.value;
		selectedItems.value = [];
		await props.deleteBenutzerAllgemein(items);
	}

</script>
