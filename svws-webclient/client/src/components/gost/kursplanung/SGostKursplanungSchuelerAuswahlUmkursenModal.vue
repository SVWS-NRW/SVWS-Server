<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal :show="showModal" size="medium" class="hidden">
		<template #modalTitle>Umkursen</template>
		<template #modalDescription>
			<div class="flex gap-2">
				<!-- Die Tabelle mit den Kursschülern	 -->
				<div>
					{{ kursname }}
					<svws-ui-table :items="schuelerFilter().filtered.value" :columns="[{key: 'name', label: 'Name'}]" :no-data="schuelerFilter().filtered.value.length <= 0">
						<template #cell(name)="{ rowData }">
							<div @click="remove(rowData.id)" class="cursor-pointer">{{ rowData.nachname }}, {{ rowData.vorname }}</div>
						</template>
					</svws-ui-table>
				</div>
				<!-- Die Tabelle mit den Schülern gleicher Fachwahl, aber nicht in diesem Kurs	 -->
				<div>
					{{ fachname }}
					<svws-ui-table :items="fachwahlschueler" :columns="[{key: 'name', label: 'Name'}, {key: 'kurs', label: 'Kurs'}]" :no-data="fachwahlschueler.length <= 0">
						<template #cell(name)="{ rowData }">
							<div @click="move(rowData.id)" class="cursor-pointer">{{ rowData.nachname }}, {{ rowData.vorname }}</div>
						</template>
						<template #cell(kurs)="{ rowData }">
							<div @click="move(rowData.id)" class="cursor-pointer">{{ kurswahl(rowData.id).value }}</div>
						</template>
					</svws-ui-table>
				</div>
			</div>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="showModal().value = false">Abbrechen</svws-ui-button>
			<svws-ui-button type="primary" @click="true">Ja</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { computed, ref } from 'vue';
	import type { GostKursplanungSchuelerFilter } from './GostKursplanungSchuelerFilter';
	import type { GostBlockungsergebnisManager, Schueler } from '@core';
	import { GostBlockungsergebnisKursSchuelerZuordnung } from '@core';

	const props = defineProps<{
		updateKursSchuelerZuordnung: (idSchueler: number, idKursNeu: number, idKursAlt: number | undefined) => Promise<boolean>;
		removeKursSchuelerZuordnung: (zuordnungen: Iterable<GostBlockungsergebnisKursSchuelerZuordnung>) => Promise<boolean>;
		getErgebnismanager: () => GostBlockungsergebnisManager;
		schuelerFilter: () => GostKursplanungSchuelerFilter;
	}>();

	const _showModal = ref<boolean>(false);
	const showModal = () => _showModal;

	const kursname = computed(() => {
		const kurs = props.schuelerFilter().kurs;
		if (kurs === undefined)
			return '??';
		return props.getErgebnismanager().getOfKursName(kurs.id);
	})

	const fachname = computed(() => {
		const kurs = props.schuelerFilter().kurs;
		if (kurs === undefined)
			return '??';
		const fach = props.getErgebnismanager().getFach(kurs.fach_id);
		return fach.bezeichnung || '??';
	})

	const fachwahlschueler = computed(() => {
		const kurswahlschueler = props.schuelerFilter().filtered.value;
		const liste = new Set();
		for (const s of kurswahlschueler)
			liste.add(s.id);
		const arr: Schueler[] = [];
		const kurs = props.schuelerFilter().kurs;
		if (kurs === undefined)
			return arr;
		const fachwahlen = props.getErgebnismanager().getOfSchuelerMengeGefiltert(-1, kurs.fach_id, kurs.kursart, 0, "");
		for (const w of fachwahlen)
			if (!liste.has(w.id))
				arr.push(w);
		return arr;
	})

	const kurswahl = (id: number) => computed(() => {
		const kurs = props.schuelerFilter().kurs;
		if (kurs === undefined)
			return 'ohne Kurszuordnung';
		const kurswahl = props.getErgebnismanager().getOfSchuelerOfFachZugeordneterKurs(id, kurs.fach_id);
		if (kurswahl === null)
			return 'ohne Kurszuordnung';
		return props.getErgebnismanager().getOfKursName(kurswahl.id);
	})

	async function remove(id: number) {
		const kurs = props.schuelerFilter().kurs;
		if (kurs === undefined)
			return;
		const zuordnung = new GostBlockungsergebnisKursSchuelerZuordnung();
		zuordnung.idSchueler = id;
		zuordnung.idKurs = kurs.id;
		await props.removeKursSchuelerZuordnung([zuordnung]);
	}

	async function move(id: number) {
		const kurs = props.schuelerFilter().kurs;
		if (kurs === undefined)
			return;
		const kurswahl = props.getErgebnismanager().getOfSchuelerOfFachZugeordneterKurs(id, kurs.fach_id);
		await props.updateKursSchuelerZuordnung(id, kurs.id, kurswahl?.id);
	}

	const openModal = () => {
		showModal().value = true;
	}

</script>
