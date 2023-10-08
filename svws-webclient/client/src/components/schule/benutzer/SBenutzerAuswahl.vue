<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<nav class="secondary-menu--breadcrumbs">
				<a title="Schule" @click="gotoSchule">Schule</a>
				<span title="Benutzer">Benutzer</span>
			</nav>
		</template>
		<template #content>
			<svws-ui-table :clicked="auswahl()" @update:clicked="gotoBenutzer" v-model="selectedItems" :items="rowsFiltered.values()"
				:columns="cols" clickable selectable count :unique-key="String(auswahl()?.id)" scroll>
				<template #search>
					<svws-ui-text-input v-model="search" type="search" placeholder="Suchen" />
				</template>
				<template #actions>
					<s-modal-benutzer-neu :show-delete-icon="selectedItems.length > 0" :create-benutzer-allgemein="createBenutzerAllgemein"
						:delete-benutzer-allgemein="deleteMultipleUser" />
				</template>
			</svws-ui-table>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import type { BenutzerListeEintrag} from "@core";
	import type { BenutzerAuswahlProps } from "./SBenutzerAuswahlProps";
	import type { DataTableColumn } from "@ui";
	import { computed, ref } from "vue";

	const selectedItems = ref<BenutzerListeEintrag[]>([]);

	const props = defineProps<BenutzerAuswahlProps>();

	const cols: DataTableColumn[] = [
		{ key: "anzeigename", label: "Anzeigename", sortable: true, span: 2 },
		{ key: "name", label: "Name", sortable: true },
	];

	const search = ref<string>("");

	const rowsFiltered = computed<Map<number, BenutzerListeEintrag>>(() => {
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
		await props.deleteBenutzerMenge(items);
	}

</script>
