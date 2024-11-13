<template>
	<svws-ui-secondary-menu>
		<template #headline><span class="select-none">Stundenplan</span></template>
		<template #abschnitt>
			<abschnitt-auswahl :daten="schuljahresabschnittsauswahl" />
		</template>
		<template #content>
			<svws-ui-table :clicked="auswahl" clickable @update:clicked="gotoEintrag" :items="mapKatalogeintraege().values()" :unselectable :columns selectable v-model="selected" scroll-into-view enable-focus-switching>
				<template #actions>
					<svws-ui-button @click="doDeleteEintraege" type="trash" :disabled="selected.length === 0" />
					<svws-ui-button type="icon" @click="addEintrag" :has-focus="mapKatalogeintraege().size === 0">
						<span class="icon i-ri-add-line" />
					</svws-ui-button>
				</template>
			</svws-ui-table>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import { computed, ref } from "vue";
	import type { StundenplanAuswahlProps } from "./SStundenplanAuswahlProps";
	import type { DataTableColumn } from "@ui";
	import type { StundenplanListeEintrag } from "@core";

	const props = defineProps<StundenplanAuswahlProps>();
	const selected = ref<StundenplanListeEintrag[]>([]);

	const columns: DataTableColumn[] = [
		{ key: "bezeichnung", label: "Bezeichnung", span: 2, sortable: false },
		{ key: "gueltigAb", label: "von", span: 1, sortable: false, defaultSort: 'asc', type: 'date' },
		{ key: "gueltigBis", label: "bis", span: 1, sortable: false, type: 'date' }
	];

	const unselectable = computed(() => {
		const set = new Set<StundenplanListeEintrag>();
		const vorlage = props.mapKatalogeintraege().get(-1);
		if (vorlage !== undefined)
			set.add(vorlage);
		return set;
	})

	async function doDeleteEintraege() {
		await props.removeEintraege(selected.value);
		selected.value = [];
	}

</script>
