<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<span>Einwilligungsarten</span>
		</template>
		<template #abschnitt>
			<abschnitt-auswahl :daten="schuljahresabschnittsauswahl" />
		</template>
		<template #header />
		<template #content>
			<div class="container">
				<svws-ui-table :clicked="auswahl()" @update:clicked="gotoEintrag" :items="mapKatalogeintraege.values()" :columns clickable selectable v-model="selected" scroll-into-view :focus-switching-enabled :focus-help-visible>
					<template #actions>
						<svws-ui-button @click="doDeleteEintraege()" type="trash" :disabled="selected.length === 0" />
						<s-einwilligungsarten-neu-modal v-slot="{ openModal }" :add-eintrag :map-katalogeintraege>
							<svws-ui-button type="icon" @click="openModal()" :has-focus="mapKatalogeintraege.size === 0">
								<span class="icon i-ri-add-line" />
							</svws-ui-button>
						</s-einwilligungsarten-neu-modal>
					</template>
				</svws-ui-table>
			</div>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import { computed, shallowRef } from "vue";
	import type { SEinwilligungsartenAuswahlProps } from "./SEinwilligungsartenAuswahlProps";
	import type { Einwilligungsart } from "@core";
	import { useRegionSwitch } from "~/components/useRegionSwitch";

	const props = defineProps<SEinwilligungsartenAuswahlProps>();
	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();
	const selected = shallowRef<Einwilligungsart[]>([]);

	const columns = [
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true, defaultSort: "asc" }
	];

	const deleteCandidates = computed(() => selected.value);

	async function doDeleteEintraege() {
		await props.deleteEintraege(deleteCandidates.value);
		selected.value = [];
	}

</script>
