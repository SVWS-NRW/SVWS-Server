<template>
	<div class="h-full flex flex-col">
		<div class="secondary-menu--headline">
			<h1><span title="Benutzer">Benutzer</span></h1>
			<div><abschnitt-auswahl disabled /></div>
		</div>
		<div class="secondary-menu--header" />
		<div class="secondary-menu--content">
			<svws-ui-table :clicked="auswahl()" @update:clicked="gotoBenutzer" v-model="selectedItems" :items="rowsFiltered.values()"
				:columns clickable selectable count scroll scroll-into-view :focus-switching-enabled :focus-help-visible>
				<template #search>
					<svws-ui-text-input v-model="search" type="search" placeholder="Suchen" />
				</template>
				<template #actions>
					<s-modal-benutzer-neu :show-delete-icon="selectedItems.length > 0"
						:create-benutzer-allgemein
						:delete-benutzer-allgemein
						:has-focus="rowsFiltered.size === 0"
						:map-benutzer />
				</template>
			</svws-ui-table>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref } from "vue";
	import type { BenutzerAuswahlProps } from "./SBenutzerAuswahlProps";
	import type { BenutzerListeEintrag } from "@core/core/data/benutzer/BenutzerListeEintrag";
	import type { DataTableColumn } from "@ui/types";
	import { useRegionSwitch } from "@ui/ui/composables/useRegionSwitch";

	const selectedItems = ref<BenutzerListeEintrag[]>([]);

	const props = defineProps<BenutzerAuswahlProps>();
	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	const columns: DataTableColumn[] = [
		{ key: "name", label: "Benutzername", sortable: true },
		{ key: "anzeigename", label: "Anzeigename", sortable: true, span: 2 },
	];

	const search = ref<string>("");

	const rowsFiltered = computed<Map<number, BenutzerListeEintrag>>(() => {
		if (search.value === "") {
			return props.mapBenutzer;
		}
		const result = new Map<number, BenutzerListeEintrag>();
		for (const benutzer of props.mapBenutzer.values()) {
			if (benutzer.name.toLocaleLowerCase().includes(search.value.toLocaleLowerCase()) ||
				benutzer.anzeigename.toLocaleLowerCase().includes(search.value.toLocaleLowerCase())) {
				result.set(benutzer.id, benutzer);
			}
		}
		return result;
	});

	async function deleteBenutzerAllgemein() {
		const items = selectedItems.value;
		selectedItems.value = [];
		await props.deleteBenutzerMenge(items);
	}

</script>
