<template>
	<svws-ui-secondary-menu>
		<template #headline><span class="select-none">Stundenplan</span></template>
		<template #abschnitt>
			<abschnitt-auswahl :daten="schuljahresabschnittsauswahl" />
		</template>
		<template #content>
			<div class="secondary-menu--navigation">
				<svws-ui-menu-item @click="gotoVorgaben">
					<template #label> <span>Kataloge</span> </template>
				</svws-ui-menu-item>
				<svws-ui-spacing />
			</div>
			<svws-ui-table :clicked="auswahl" clickable @update:clicked="gotoEintrag" :items="mapKatalogeintraege().values()" :columns selectable v-model="selected">
				<template #actions>
					<svws-ui-button @click="doDeleteEintraege()" type="trash" class="cursor-pointer" :disabled="selected.length === 0" />
					<svws-ui-button type="icon" @click="addEintrag">
						<span class="icon i-ri-add-line" />
					</svws-ui-button>
				</template>
			</svws-ui-table>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import { ref } from "vue";
	import type { StundenplanAuswahlProps } from "./SStundenplanAuswahlProps";
	import type { StundenplanListeEintrag } from "@core";
	import type { DataTableColumn } from "@ui";

	const props = defineProps<StundenplanAuswahlProps>();
	const selected = ref<StundenplanListeEintrag[]>([]);

	const columns: DataTableColumn[] = [
		{ key: "bezeichnung", label: "Bezeichnung", span: 2, sortable: false },
		{ key: "gueltigAb", label: "von", span: 1, sortable: false, defaultSort: 'asc', type: 'date' },
		{ key: "gueltigBis", label: "bis", span: 1, sortable: false, type: 'date' }
	];

	async function doDeleteEintraege() {
		await props.removeEintraege(selected.value);
		selected.value = [];
	}

</script>
