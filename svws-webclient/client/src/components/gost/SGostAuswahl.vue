<template>
	<svws-ui-secondary-menu>
		<template #headline><span class="select-none">Oberstufe</span></template>
		<template #abschnitt>
			<abschnitt-auswahl :daten="schuljahresabschnittsauswahl" />
		</template>
		<template #content>
			<svws-ui-table :clicked="auswahl" clickable @update:clicked="gotoAbiturjahrgang" :items :columns :filter-open="false">
				<template #filterAdvanced>
					<div class="col-span-full flex flex-wrap gap-x-5">
						<svws-ui-checkbox type="toggle" :model-value="filterNurAktuelle()" @update:model-value="setFilterNurAktuelle">Nur Aktuelle Jahrgänge</svws-ui-checkbox>
					</div>
				</template>
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
									<span class="icon i-ri-delete-bin-line -mx-0.5" />
								</svws-ui-button>
							</s-gost-auswahl-abiturjahrgang-remove-modal>
						</div>
					</div>
				</template>
				<template #actions>
					<s-gost-auswahl-abiturjahrgang-modal v-slot="{ openModal }" :map-jahrgaenge-ohne-abi-jahrgang="mapJahrgaengeOhneAbiJahrgang"
						:add-abiturjahrgang="addAbiturjahrgang" :get-abiturjahr-fuer-jahrgang="getAbiturjahrFuerJahrgang">
						<svws-ui-button @click="openModal()" type="icon" title="Abiturjahr hinzufügen" :disabled="!mapJahrgaengeOhneAbiJahrgang().size"> <span class="icon i-ri-add-line" /> </svws-ui-button>
					</s-gost-auswahl-abiturjahrgang-modal>
				</template>
			</svws-ui-table>
			<router-view name="gost_child_auswahl" />
		</template>
	</svws-ui-secondary-menu>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { GostAuswahlProps } from "./SGostAuswahlProps";
	import type { GostJahrgang } from "@core";

	const props = defineProps<GostAuswahlProps>();

	const columns = [
		{ key: "bezeichnung", label: "Abiturjahrgang", sortable: true, span: 2 },
		{ key: "abiturjahr", label: "Jahr", sortable: true },
		{ key: "jahrgang", label: "Stufe", sortable: true }];

	const items = computed<GostJahrgang[]>(() => {
		const list = [...props.mapAbiturjahrgaenge().values()];
		const filtern = props.filterNurAktuelle();
		return list.filter(a => filtern && !a.istAbgeschlossen).sort((a, b) => (a?.bezeichnung || "") < (b?.bezeichnung || "") ? 1 : -1)
	});

	const pending = computed<boolean>(() => props.apiStatus.pending);

	function isRemovable(value : GostJahrgang) : boolean {
		if ((value.abiturjahr < 0) || (value.istAbgeschlossen))
			return false;
		const jahrgangsdaten = props.jahrgangsdaten();
		if ((jahrgangsdaten === undefined) || (jahrgangsdaten.abiturjahr !== value.abiturjahr) || (jahrgangsdaten.istBlockungFestgelegt[0]))
			return false;
		return true;
	}

</script>
