<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal :show="showModal" size="small" class="hidden">
		<template #modalTitle>Ungültige Abiturjahrgänge für das Abiturjahr {{ getErgebnismanager().getParent().daten().abijahrgang }}</template>
		<template #modalDescription>
			Sollen folgende Schüler mit einem falschen Abiturjahrgang aus ihren Kursen entfernt werden?
			<svws-ui-table selectable v-model="selected" :items="getErgebnismanager().getOfSchuelerMengeMitAbweichendemAbijahrgang()" disable-footer :columns="[{key: 'id', label: 'Schüler',}, {key: 'abschlussjahrgang', label: 'Abiturjahr'}]">
				<template #cell(id)="{rowData: s}">
					{{ s.nachname }}, {{ s.vorname }}
				</template>
				<template #cell(abschlussjahrgang)="{value}">
					Abi {{ value }}
				</template>
			</svws-ui-table>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="showModal().value = false">Abbrechen</svws-ui-button>
			<svws-ui-button type="primary" @click="removeZuordnung">OK</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { shallowRef } from 'vue';
	import type { GostBlockungRegelUpdate, GostBlockungsergebnisKursSchuelerZuordnungUpdate, GostBlockungsergebnisManager, Schueler } from '@core';
	import { HashSet } from '@core';

	const props = defineProps<{
		getErgebnismanager: () => GostBlockungsergebnisManager;
		updateKursSchuelerZuordnungen: (update: GostBlockungsergebnisKursSchuelerZuordnungUpdate) => Promise<boolean>;
		regelnUpdate: (update: GostBlockungRegelUpdate) => Promise<void>;
	}>();

	const selected = shallowRef<Schueler[]>([]);
	const _showModal = shallowRef<boolean>(false);
	const showModal = () => _showModal;

	async function removeZuordnung() {
		const set = new HashSet<number>();
		for (const z of selected.value)
			set.add(z.id);
		showModal().value = false;
		if (!set.isEmpty()) {
			const kursUpdate = props.getErgebnismanager().kursSchuelerUpdate_02b_ENTFERNE_SCHUELERMENGE_AUS_ALLEN_KURSEN(set);
			await props.updateKursSchuelerZuordnungen(kursUpdate);
			const regelUpdate = props.getErgebnismanager().regelupdateCreate_19_SCHUELERMENGE_ENTFERNEN(set);
			await props.regelnUpdate(regelUpdate);
		}
		selected.value = [];
	}

	function openModal() {
		selected.value = props.getErgebnismanager().getOfSchuelerMengeMitAbweichendemAbijahrgang().toArray() as Schueler[];
		showModal().value = true;
	}

</script>
