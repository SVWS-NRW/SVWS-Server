<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal :show="showModal" size="medium" class="hidden">
		<template #modalTitle>Kurs-Schüler-Zuordnung</template>
		<template #modalDescription>
			<div class="flex gap-8">
				<!-- Die Tabelle mit den Kursschülern -->
				<div class="w-96">
					im Kurs {{ kursname }}
					<svws-ui-table :items="schuelerFilter().filtered.value" :columns="[{key: 'name', label: 'Name'}]" :no-data="schuelerFilter().filtered.value.length <= 0">
						<template #cell(name)="{ rowData }">
							<div @click="remove(rowData.id)" class="cursor-pointer">{{ rowData.nachname }}, {{ rowData.vorname }}</div>
						</template>
					</svws-ui-table>
				</div>
				<!-- Die Tabelle mit den Schülern gleicher Fachwahl, aber nicht in diesem Kurs -->
				<div class="w-128">
					mit Fachwahl {{ fachname }} {{ kursart }}
					<svws-ui-table :items="fachwahlschueler" :columns="[{key: 'name', label: 'Name'}, {key: 'kurs', label: 'andere Kurszuordnung'}]" :no-data="fachwahlschueler.length <= 0">
						<template #cell(name)="{ rowData }">
							<div @click="move(rowData.id)" class="cursor-pointer">{{ rowData.nachname }}, {{ rowData.vorname }}</div>
						</template>
						<template #cell(kurs)="{ rowData }">
							<svws-ui-select title="Anderer Kurs" :items="kurse" :item-text="getKursBezeichnung"
								:model-value="getKurs(rowData)" @update:model-value="(value : GostBlockungsergebnisKurs | undefined) => updateZuordnung(rowData, value)"
								class="w-full" headless removable />
						</template>
					</svws-ui-table>
				</div>
			</div>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="showModal().value = false">Schließen</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { computed, ref } from 'vue';
	import type { GostKursplanungSchuelerFilter } from './GostKursplanungSchuelerFilter';
	import type { GostBlockungsergebnisKurs, GostBlockungsergebnisManager, Schueler, List } from '@core';
	import { ArrayList, GostBlockungsergebnisKursSchuelerZuordnung, GostKursart } from '@core';

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
			return '—';
		return props.getErgebnismanager().getOfKursName(kurs.id);
	});

	const kursart = computed(() => {
		const kurs = props.schuelerFilter().kurs;
		if (kurs === undefined)
			return '—';
		const kursart = GostKursart.fromIDorNull(kurs.kursart);
		return kursart?.kuerzel ?? "—";
	});

	const kurse = computed<List<GostBlockungsergebnisKurs>>(() => {
		const kurs = props.schuelerFilter().kurs;
		if (kurs === undefined)
			return new ArrayList<GostBlockungsergebnisKurs>();
		const fachart = GostKursart.getFachartID(kurs.fach_id, kurs.kursart);
		return props.getErgebnismanager().getOfFachartKursmenge(fachart);
	});

	const fachname = computed(() => {
		const kurs = props.schuelerFilter().kurs;
		if (kurs === undefined)
			return '—';
		const fach = props.getErgebnismanager().getFach(kurs.fach_id);
		return fach.bezeichnung || '—';
	});

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
			return '———';
		const kurswahl = props.getErgebnismanager().getOfSchuelerOfFachZugeordneterKurs(id, kurs.fach_id);
		if (kurswahl === null)
			return '———';
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

	async function updateZuordnung(schueler: Schueler, neuer_kurs: GostBlockungsergebnisKurs | undefined) {
		const kurs = props.schuelerFilter().kurs;
		if (kurs === undefined)
			return;
		const alter_kurs = props.getErgebnismanager().getOfSchuelerOfFachZugeordneterKurs(schueler.id, kurs.fach_id);
		if ((neuer_kurs === undefined) && (alter_kurs !== null)) {
			const zuordnung = new GostBlockungsergebnisKursSchuelerZuordnung();
			zuordnung.idSchueler = schueler.id;
			zuordnung.idKurs = alter_kurs.id;
			await props.removeKursSchuelerZuordnung([ zuordnung ]);
		} else if (neuer_kurs !== undefined) {
			await props.updateKursSchuelerZuordnung(schueler.id, neuer_kurs.id, alter_kurs?.id ?? undefined);
		}
	}

	function getKurs(schueler: Schueler) : GostBlockungsergebnisKurs | undefined {
		const kurs = props.schuelerFilter().kurs;
		if (kurs === undefined)
			return;
		const alter_kurs = props.getErgebnismanager().getOfSchuelerOfFachZugeordneterKurs(schueler.id, kurs.fach_id);
		return alter_kurs ?? undefined;
	}

	function getKursBezeichnung(k : GostBlockungsergebnisKurs | undefined) : string {
		if (k === undefined)
			return '———';
		return props.getErgebnismanager().getOfKursName(k.id);
	}

	const openModal = () => {
		showModal().value = true;
	}

</script>