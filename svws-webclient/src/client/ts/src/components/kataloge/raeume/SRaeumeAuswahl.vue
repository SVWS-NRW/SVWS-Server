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
				<svws-ui-data-table :clicked="auswahl" clickable @update:clicked="gotoEintrag" :items="mapKatalogeintraege.values()" :columns="cols" selectable v-model="selected">
					<template #footerActions>
						<s-raum-neu-modal v-slot="{ openModal }" :add-raum="addEintrag">
							<button @click="openModal()" class="flex h-10 w-10 items-center justify-center">
								<svws-ui-icon><i-ri-add-line /></svws-ui-icon>
							</button>
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
	import type { Raum } from "@svws-nrw/svws-core";
	import { ref } from "vue";

	const props = defineProps<RaeumeAuswahlProps>();
	const selected = ref<Raum[]>([]);

	const cols: DataTableColumn[] = [
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: 'asc' },
		{ key: "beschreibung", label: "Beschreibung", sortable: true },
		{ key: "groesse", label: "Größe", sortable: true },
	];
</script>
