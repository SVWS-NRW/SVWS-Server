<template>
	<svws-ui-secondary-menu>
		<template #headline>Stundenplan</template>
		<template #abschnitt>
			<abschnitt-auswahl :akt-abschnitt="aktAbschnitt" :abschnitte="abschnitte" :set-abschnitt="setAbschnitt" :akt-schulabschnitt="aktSchulabschnitt" />
		</template>
		<template #content>
			<svws-ui-table :clicked="auswahl" clickable @update:clicked="gotoEintrag" :items="mapKatalogeintraege().values()" :columns="cols" selectable v-model="selected">
				<template #actions>
					<svws-ui-button @click="doDeleteEintraege()" type="trash" class="cursor-pointer" :disabled="selected.length === 0" />
					<svws-ui-button type="icon" @click="addEintrag">
						<i-ri-add-line />
					</svws-ui-button>
				</template>
			</svws-ui-table>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import type { StundenplanAuswahlProps } from "./SStundenplanAuswahlProps";
	import type { StundenplanListeEintrag } from "@core";
	import type { DataTableColumn } from "@ui";
	import { ref } from "vue";

	const props = defineProps<StundenplanAuswahlProps>();
	const selected = ref<StundenplanListeEintrag[]>([]);

	const cols: DataTableColumn[] = [
		{ key: "bezeichnung", label: "Bezeichnung", span: 2, sortable: false },
		{ key: "gueltigAb", label: "von", span: 1, sortable: false, defaultSort: 'asc', type: 'date' },
		{ key: "gueltigBis", label: "bis", span: 1, sortable: false, type: 'date' }
	];

	async function doDeleteEintraege() {
		await props.removeEintraege(selected.value);
		selected.value = [];
	}

</script>
