<template>
	<svws-ui-secondary-menu>
		<template #headline>
			<nav class="secondary-menu--breadcrumbs">
				<a @click="returnToKataloge">Kataloge</a>
				<span title="Zeitraster">Zeitraster</span>
			</nav>
		</template>
		<template #abschnitt>
			<abschnitt-auswahl :akt-abschnitt="aktAbschnitt" :abschnitte="abschnitte" :set-abschnitt="setAbschnitt" :akt-schulabschnitt="aktSchulabschnitt" />
		</template>
		<template #header />
		<template #content>
			<div class="container">
				<svws-ui-data-table :clicked="auswahl" clickable @update:clicked="gotoEintrag" :items="mapKatalogeintraege().values()" :columns="cols" selectable v-model="selected">
					<template #cell(wochentag)="{ rowData }">
						{{ Wochentag.fromIDorException(rowData.wochentag).beschreibung }}
					</template>
					<template #cell(unterrichtstunde)="{ rowData }">
						{{ rowData.unterrichtstunde }}
					</template>
					<template #footerActions>
						<div v-if="selected.length > 0" class="flex items-center justify-end pr-1 h-full">
							<svws-ui-button @click="doDeleteEintraege()" type="trash" class="cursor-pointer"
								:disabled="selected.length === 0" />
						</div>
						<s-zeitraster-neu-modal v-slot="{ openModal }" :add-zeitraster="addEintrag">
							<button @click="openModal()" class="flex h-10 w-10 items-center justify-center">
								<svws-ui-icon><i-ri-add-line /></svws-ui-icon>
							</button>
						</s-zeitraster-neu-modal>
					</template>
				</svws-ui-data-table>
			</div>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import type { DataTableColumn } from "@ui";
	import type { ZeitrasterAuswahlProps } from "./SZeitrasterAuswahlProps";
	import { Wochentag } from "@core";
	import type { StundenplanZeitraster } from "@core";
	import { ref } from "vue";

	const props = defineProps<ZeitrasterAuswahlProps>();
	const selected = ref<StundenplanZeitraster[]>([]);

	const cols: DataTableColumn[] = [
		{ key: "wochentag", label: "Wochentag", sortable: true, defaultSort: 'asc' },
		{ key: "unterrichtstunde", label: "Stunde", sortable: true },
	];

	async function doDeleteEintraege() {
		await props.deleteEintraege(selected.value);
		selected.value = [];
	}

</script>
