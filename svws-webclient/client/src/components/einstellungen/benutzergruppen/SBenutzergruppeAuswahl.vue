<template>
	<div class="h-full flex flex-col">
		<div class="secondary-menu--headline">
			<h1><span title="Benutzergruppen">Benutzergruppen</span></h1>
			<div class="input--schule-abschnitte">
				<abschnitt-auswahl :daten="schuljahresabschnittsauswahl" />
			</div>
		</div>
		<div class="secondary-menu--header" />
		<div class="secondary-menu--content">
			<svws-ui-table :clicked="auswahl()" @update:clicked="gotoBenutzergruppe" v-model="selectedItems" :items="rowsFiltered.values()"
				:columns="cols" clickable selectable count :unique-key="String(auswahl()?.id)" scroll scroll-into-view :focus-switching-enabled :focus-help-visible>
				<template #search>
					<svws-ui-text-input v-model="search" type="search" placeholder="Suchen" removable />
				</template>
				<template #actions>
					<s-modal-benutzergruppe-neu :show-delete-icon="selectedItems.length > 0" :create-benutzergruppe
						:delete-benutzergruppen="deleteMultipleGroup" :has-focus="rowsFiltered.size === 0" />
				</template>
			</svws-ui-table>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref } from "vue";
	import { type DataTableColumn } from "@ui";
	import { type BenutzergruppeListeEintrag } from "@core";
	import { type BenutzergruppeAuswahlProps } from "./SBenutzergruppeAuswahlProps";
	import { useRegionSwitch } from "~/components/useRegionSwitch";

	const props = defineProps<BenutzergruppeAuswahlProps>();
	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	const selectedItems = ref<BenutzergruppeListeEintrag[]>([]);

	const cols : DataTableColumn[] = [
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true },
	];

	const search = ref<string>("");

	const rowsFiltered = computed<Map<number, BenutzergruppeListeEintrag>>(() => {
		if (search.value === "")
			return props.mapBenutzergruppe;
		const result = new Map<number, BenutzergruppeListeEintrag>();
		for (const l of props.mapBenutzergruppe.values())
			if (l.bezeichnung.toLocaleLowerCase().includes(search.value.toLocaleLowerCase()))
				result.set(l.id, l);
		return result;
	});

	async function deleteMultipleGroup() {
		await props.deleteBenutzergruppen(selectedItems.value);
		selectedItems.value = [];
	}

</script>
