<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<nav class="secondary-menu--breadcrumbs">
				<a title="Schule" @click="gotoSchule">Schule</a>
				<span title="Benutzergruppen">Benutzergruppen</span>
			</nav>
		</template>
		<template #content>
			<svws-ui-table :clicked="auswahl()" @update:clicked="gotoBenutzergruppe" v-model="selectedItems" :items="rowsFiltered.values()"
				:columns="cols" clickable selectable count :unique-key="String(auswahl()?.id)" scroll>
				<template #search>
					<svws-ui-text-input v-model="search" type="search" placeholder="Suchen" />
				</template>
				<template #header(id)>
					<span class="font-mono">ID</span>
				</template>
				<template #actions>
					<s-modal-benutzergruppe-neu :show-delete-icon="selectedItems.length > 0" :create-benutzergruppe="createBenutzergruppe"
						:delete-benutzergruppe_n="deleteMultipleGroup" />
				</template>
			</svws-ui-table>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import type { BenutzergruppeListeEintrag } from "@core";
	import type { ComputedRef, Ref} from "vue";
	import { computed, ref } from "vue";
	import type { BenutzergruppeAuswahlProps } from "./SBenutzergruppeAuswahlProps";

	const props = defineProps<BenutzergruppeAuswahlProps>();

	const selectedItems: Ref<BenutzergruppeListeEintrag[]> = ref([]);

	const cols = [
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true, span: 2 },
		{ key: "id", label: "ID", sortable: true, span: 0.5, align: "right" }
	];

	const search: Ref<string> = ref("");

	const rowsFiltered: ComputedRef<Map<number, BenutzergruppeListeEintrag>> = computed(() => {
		if (!search.value)
			return props.mapBenutzergruppe;
		const result = new Map<number, BenutzergruppeListeEintrag>();
		for (const l of props.mapBenutzergruppe.values()) {
			if (l.bezeichnung.toLocaleLowerCase().includes(search.value.toLocaleLowerCase()))
				result.set(l.id, l);
		}
		return result;
	});

	async function deleteMultipleGroup() {
		const items = selectedItems.value;
		selectedItems.value = [];
		await props.deleteBenutzergruppen(items);
	}

</script>
