<template>
	<svws-ui-secondary-menu>
		<template #headline>Abiturjahrgänge</template>
		<template #abschnitt>
			<abschnitt-auswahl :akt-abschnitt="aktAbschnitt" :abschnitte="abschnitte" :set-abschnitt="setAbschnitt" :akt-schulabschnitt="aktSchulabschnitt" />
		</template>
		<template #content>
			<div class="flex flex-col gap-12">
				<svws-ui-data-table :clicked="auswahl" clickable @update:clicked="gotoAbiturjahrgang" :items="rows" :columns="cols">
					<template #cell(abiturjahr)="{ value }">
						{{ value.abiturjahr === -1 ? '' : value.abiturjahr }}
						<svws-ui-spinner :spinning="(pending && value.abiturjahr === auswahl?.abiturjahr)" />
					</template>
					<template #footerActions>
						<s-gost-auswahl-abiturjahrgang-modal v-slot="{ openModal }" :map-jahrgaenge-ohne-abi-jahrgang="mapJahrgaengeOhneAbiJahrgang" :add-abiturjahrgang="addAbiturjahrgang">
							<svws-ui-button @click="openModal()" type="icon" title="Abiturjahr hinzufügen"> <i-ri-add-line /> </svws-ui-button>
						</s-gost-auswahl-abiturjahrgang-modal>
					</template>
				</svws-ui-data-table>
				<router-view name="gost_child_auswahl" />
			</div>
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import { GostJahrgang } from "@svws-nrw/svws-core";
	import { DataTableColumn } from "@ui";
	import { computed, ComputedRef } from "vue";
	import { GostAuswahlProps } from "./SGostAuswahlProps";

	const props = defineProps<GostAuswahlProps>();

	const cols: DataTableColumn[] = [
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true, span: 2 },
		{ key: "abiturjahr", label: "Abiturjahr", sortable: true },
		{ key: "jahrgang", label: "Stufe", sortable: true }];

	const rows: ComputedRef<GostJahrgang[]> = computed(() => {
		const list = [...props.mapAbiturjahrgaenge.values()];
		return list.sort((a, b) => (a?.bezeichnung || "") < (b?.bezeichnung || "") ? 1 : -1)
	});

	const pending: ComputedRef<boolean> = computed(() => props.apiStatus.pending);
</script>