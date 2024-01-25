<template>
	<svws-ui-secondary-menu>
		<template #headline>Oberstufe</template>
		<template #abschnitt>
			<abschnitt-auswahl :akt-abschnitt="aktAbschnitt" :abschnitte="abschnitte" :set-abschnitt="setAbschnitt" :akt-schulabschnitt="aktSchulabschnitt" />
		</template>
		<template #content>
			<svws-ui-table :clicked="auswahl" clickable @update:clicked="gotoAbiturjahrgang" :items="rows" :columns="cols">
				<template #cell(abiturjahr)="{ value }">
					<span v-if="value === -1" class="opacity-25">
						—
					</span>
					<span v-else>
						{{ value }}
					</span>
					<svws-ui-spinner :spinning="(pending && value === auswahl?.abiturjahr)" />
				</template>
				<template #cell(jahrgang)="{ value, rowData }">
					<div class="flex justify-between w-full">
						<div>
							<span v-if="!value" class="opacity-25">
								—
							</span>
							<span v-else>
								{{ value }}
							</span>
						</div>
						<div v-if="isRemovable(rowData)" class="-my-1 ml-auto inline-flex">
							<s-gost-auswahl-abiturjahrgang-remove-modal :remove-abiturjahrgang="removeAbiturjahrgang" :gost-jahrgang="rowData" v-slot="{ openModal : openRemoveModal }">
								<svws-ui-button type="icon" @click.stop="openRemoveModal()" title="Abiturjahrgang löschen" :disabled="apiStatus.pending" class="text-black dark:text-white">
									<i-ri-delete-bin-line class="-mx-0.5" />
								</svws-ui-button>
							</s-gost-auswahl-abiturjahrgang-remove-modal>
						</div>
					</div>
				</template>
				<template #actions>
					<s-gost-auswahl-abiturjahrgang-modal v-slot="{ openModal }" :map-jahrgaenge-ohne-abi-jahrgang="mapJahrgaengeOhneAbiJahrgang"
						:add-abiturjahrgang="addAbiturjahrgang" :get-abiturjahr-fuer-jahrgang="getAbiturjahrFuerJahrgang">
						<svws-ui-button @click="openModal()" type="icon" title="Abiturjahr hinzufügen" :disabled="!mapJahrgaengeOhneAbiJahrgang().size"> <i-ri-add-line /> </svws-ui-button>
					</s-gost-auswahl-abiturjahrgang-modal>
				</template>
			</svws-ui-table>
			<router-view name="gost_child_auswahl" />
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import type { GostJahrgang } from "@core";
	import type { ComputedRef } from "vue";
	import { computed } from "vue";
	import type { GostAuswahlProps } from "./SGostAuswahlProps";

	const props = defineProps<GostAuswahlProps>();

	const cols = [
		{ key: "bezeichnung", label: "Abiturjahrgang", sortable: true, span: 2 },
		{ key: "abiturjahr", label: "Jahr", sortable: true },
		{ key: "jahrgang", label: "Stufe", sortable: true }];

	const rows: ComputedRef<GostJahrgang[]> = computed(() => {
		const list = [...props.mapAbiturjahrgaenge().values()];
		return list.sort((a, b) => (a?.bezeichnung || "") < (b?.bezeichnung || "") ? 1 : -1)
	});

	const pending: ComputedRef<boolean> = computed(() => props.apiStatus.pending);

	function isRemovable(value : GostJahrgang) : boolean {
		if ((value.abiturjahr < 0) || (value.istAbgeschlossen))
			return false;
		const jahrgangsdaten = props.jahrgangsdaten();
		if ((jahrgangsdaten === undefined) || (jahrgangsdaten.abiturjahr !== value.abiturjahr) || (jahrgangsdaten.istBlockungFestgelegt[0]))
			return false;
		return true;
	}

</script>
