<template>
	<slot :open-modal />
	<svws-ui-modal v-model:show="show" size="small" class="hidden">
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
			<svws-ui-button type="secondary" @click="show = false">Abbrechen</svws-ui-button>
			<svws-ui-button type="primary" @click="removeZuordnung">OK</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import type { Schueler } from '@core/asd/data/schueler/Schueler';
	import type { GostBlockungRegelUpdate } from '@core/core/data/gost/GostBlockungRegelUpdate';
	import type { GostBlockungsergebnisKursSchuelerZuordnungUpdate } from '@core/core/data/gost/GostBlockungsergebnisKursSchuelerZuordnungUpdate';
	import type { GostBlockungsergebnisManager } from '@core/core/utils/gost/GostBlockungsergebnisManager';
	import { HashSet } from '@core/java/util/HashSet';
	import { shallowRef } from 'vue';

	const props = defineProps<{
		getErgebnismanager: () => GostBlockungsergebnisManager;
		updateKursSchuelerZuordnungen: (update: GostBlockungsergebnisKursSchuelerZuordnungUpdate) => Promise<boolean>;
		regelnUpdate: (update: GostBlockungRegelUpdate) => Promise<void>;
	}>();

	const selected = shallowRef<Schueler[]>([]);
	const show = shallowRef<boolean>(false);

	async function removeZuordnung() {
		const set = new HashSet<number>();
		for (const z of selected.value) {
			set.add(z.id);
		}
		show.value = false;
		if (!set.isEmpty()) {
			const kursUpdate = props.getErgebnismanager().kursSchuelerUpdateEntferneSchuelermengeAusAllenKursen(set);
			await props.updateKursSchuelerZuordnungen(kursUpdate);
			const regelUpdate = props.getErgebnismanager().regelupdateCreateSchuelermengeEntfernen(set);
			await props.regelnUpdate(regelUpdate);
		}
		selected.value = [];
	}

	function openModal() {
		selected.value = props.getErgebnismanager().getOfSchuelerMengeMitAbweichendemAbijahrgang().toArray() as Schueler[];
		show.value = true;
	}

</script>
