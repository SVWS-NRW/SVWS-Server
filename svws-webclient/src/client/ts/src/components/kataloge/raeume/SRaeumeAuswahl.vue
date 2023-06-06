<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<nav class="secondary-menu--breadcrumbs">
				<a @click="returnToKataloge">Kataloge</a>
				<span title="Räume">Räume</span>
			</nav>
		</template>
		<template #abschnitt>
			<abschnitt-auswahl :akt-abschnitt="aktAbschnitt" :abschnitte="abschnitte" :set-abschnitt="setAbschnitt" :akt-schulabschnitt="aktSchulabschnitt" />
		</template>
		<template #header />
		<template #content>
			<div class="container">
				<svws-ui-data-table :clicked="auswahl" clickable @update:clicked="gotoEintrag" :items="mapKatalogeintraege().values()" :columns="cols" selectable v-model="selected">
					<template #footerActions>
						<div v-if="selected.length > 0" class="flex items-center justify-end pr-1 h-full">
							<svws-ui-button @click="doDeleteEintraege()" type="trash" class="cursor-pointer"
								:disabled="selected.length === 0" />
						</div>
						<s-raum-neu-modal v-slot="{ openModal }" :add-raum="addEintrag">
							<svws-ui-button type="icon" @click="openModal()">
								<i-ri-add-line />
							</svws-ui-button>
						</s-raum-neu-modal>
					</template>
				</svws-ui-data-table>
			</div>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import type { DataTableColumn } from "@ui";
	import type { RaeumeAuswahlProps } from "./SRaeumeAuswahlProps";
	import type { Raum } from "@core";
	import { ref } from "vue";

	const props = defineProps<RaeumeAuswahlProps>();
	const selected = ref<Raum[]>([]);

	const cols: DataTableColumn[] = [
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: 'asc' },
		{ key: "beschreibung", label: "Beschreibung", sortable: true },
		{ key: "groesse", label: "Größe", sortable: true },
	];

	async function doDeleteEintraege() {
		await props.deleteEintraege(selected.value);
		selected.value = [];
	}

</script>
