<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<nav class="secondary-menu--breadcrumbs">
				<a title="Schule" @click="router.push({ name: routeSchule.name })">Schule</a>
				<span title="Benutzer">Benutzer</span>
			</nav>
		</template>
		<template #content>
			<svws-ui-data-table :clicked="auswahl()" @update:clicked="gotoBenutzer" v-model="selectedItems" :items="rowsFiltered.values()"
				:columns="cols" clickable selectable count filter :unique-key="String(auswahl()?.id)">
				<template #search>
					<svws-ui-text-input v-model="search" type="search" placeholder="Suche nach Namen oder Kürzel" />
				</template>
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

	import type { BenutzerListeEintrag} from "@svws-nrw/svws-core";
	import { List } from "@svws-nrw/svws-core";
	import type { ComputedRef, Ref} from "vue";
	import { computed, ref } from "vue";
	import { routeSchule } from "~/router/apps/RouteSchule";
	import { router } from "~/router/RouteManager";
	import type { BenutzerAuswahlProps } from "./SBenutzerAuswahlProps";

	const selectedItems: Ref<BenutzerListeEintrag[]> = ref([]);

	const props = defineProps<BenutzerAuswahlProps>();

	const cols = [
		{ key: "anzeigename", label: "Anzeigename", sortable: true, span: 2 },
		{ key: "name", label: "Name", sortable: true },
		{ key: "id", label: "ID", sortable: true, span: 0.5 }
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
