<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<nav class="secondary-menu--breadcrumbs">
				<a @click="returnToKataloge">Kataloge</a>
				<span title="Aufsichtsbereiche">Aufsichtsbereiche</span>
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
						<s-aufsichtsbereich-neu-modal v-slot="{ openModal }" :add-aufsichtsbereich="addEintrag">
							<svws-ui-button type="icon" @click="openModal()">
								<i-ri-add-line />
							</svws-ui-button>
						</s-aufsichtsbereich-neu-modal>
					</template>
				</svws-ui-data-table>
			</div>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import type { DataTableColumn } from "@ui";
	import type { AufsichtsbereicheAuswahlProps } from "./SAufsichtsbereicheAuswahlProps";
	import type { Aufsichtsbereich } from "@core";
	import { ref } from "vue";

	const props = defineProps<AufsichtsbereicheAuswahlProps>();
	const selected = ref<Aufsichtsbereich[]>([]);

	const cols: DataTableColumn[] = [
		{ key: "kuerzel", label: "Kürzel", sortable: true, defaultSort: 'asc', span: 0.5 },
		{ key: "beschreibung", label: "Beschreibung", sortable: true },
	];

	async function doDeleteEintraege() {
		await props.deleteEintraege(selected.value);
		selected.value = [];
	}

</script>
