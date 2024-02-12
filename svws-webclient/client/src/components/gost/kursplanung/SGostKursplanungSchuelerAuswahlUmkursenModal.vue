<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal :show="showModal" size="medium" class="hidden h-full overflow-none" no-scroll>
		<template #modalTitle>Kurs-Schüler-Zuordnung</template>
		<template #modalContent>
			<div class="flex flex-row gap-8 h-full overflow-y-hidden">
				<!-- Die Tabelle mit den Kursschülern -->
				<div class="flex flex-col w-96 overflow-y-hidden">
					<span class="text-headline-sm pb-2">im Kurs {{ kursname }}</span>
					<svws-ui-table :items="schuelerFilter().filtered.value" :columns="[{key: 'name', label: 'Name'}]"
						:no-data="schuelerFilter().filtered.value.length <= 0">
						<template #cell(name)="{ rowData }">
							<div @click="remove(rowData.id)" class="w-full cursor-pointer text-left">{{ rowData.nachname }}, {{ rowData.vorname }}</div>
						</template>
					</svws-ui-table>
				</div>
				<!-- Die Tabelle mit den Schülern gleicher Fachwahl, aber nicht in diesem Kurs -->
				<div class="flex flex-col w-128 overflow-y-hidden">
					<span class="text-headline-sm pb-2">mit Fachwahl {{ fachname }} {{ kursart }}</span>
					<svws-ui-table :items="fachwahlschueler" :columns="[{key: 'name', label: 'Name'}, {key: 'kurs', label: 'andere Kurszuordnung'}]"
						:no-data="fachwahlschueler.length <= 0" scroll>
						<template #cell(name)="{ rowData }">
							<div @click="move(rowData.id)" class="w-full cursor-copy text-left">{{ rowData.nachname }}, {{ rowData.vorname }}</div>
						</template>
						<template #cell(kurs)="{ rowData }">
							<svws-ui-select title="Anderer Kurs" :items="kurse" :item-text="getKursBezeichnung"
								:model-value="getKurs(rowData)" @update:model-value="value => updateZuordnung(rowData, value)"
								class="w-full" headless removable />
						</template>
					</svws-ui-table>
				</div>
			</div>
			<div class="flex flex-col items-center border-t mt-4 pt-4 border-b pb-4 mb-4">
				<div class="text-left">
					Übertragen der Kurs-Schüler-Zuordnung des aktuellen Kurses auf andere Kurse. Dabei werden die Schüler nur
					übertragen, wenn sie auch die entsprechende Fachwahl des jeweiligen Ziel-Kurses haben.
				</div>
				<div class="mt-4 min-w-72 mb-4">
					<svws-ui-multi-select title="Andere Kurse" :items="listeKurseZurUbertragung" :item-text="getKursBezeichnung"
						v-model="_kurseZurUebertragung" class="w-full" removable />
				</div>
				<div class="text-center">
					<svws-ui-button type="secondary" @click="uebertragen()">Schülermenge übertragen</svws-ui-button>
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
	import type { GostBlockungsergebnisManager, Schueler, List } from '@core';
	import { ArrayList, GostBlockungsergebnisKursSchuelerZuordnung, GostKursart, GostBlockungsergebnisKurs } from '@core';

	const props = defineProps<{
		updateKursSchuelerZuordnung: (idSchueler: number, idKursNeu: number, idKursAlt: number | undefined) => Promise<boolean>;
		removeKursSchuelerZuordnung: (zuordnungen: Iterable<GostBlockungsergebnisKursSchuelerZuordnung>) => Promise<boolean>;
		getErgebnismanager: () => GostBlockungsergebnisManager;
		schuelerFilter: () => GostKursplanungSchuelerFilter;
	}>();

	const _showModal = ref<boolean>(false);
	const showModal = () => _showModal;

	const _kurseZurUebertragung = ref<GostBlockungsergebnisKurs[]>([]);

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

	const listeKurseZurUbertragung = computed<List<GostBlockungsergebnisKurs>>(() => {
		const result = new ArrayList<GostBlockungsergebnisKurs>();
		const kurs = props.schuelerFilter().kurs;
		if (kurs === undefined)
			return result;
		// Erstelle einen Filter für das Fach, welches im Modal angezeigt wird und aller Fächer, von denen bereits Kurse für die Übertragung ausgewöhlt wurden
		const setFachFilter = new Set<number>();
		const ausgewaehlteKurse = new Set<number>();
		setFachFilter.add(kurs.fach_id);
		for (const k of _kurseZurUebertragung.value) {
			setFachFilter.add(k.fachID);
			ausgewaehlteKurse.add(k.id);
		}
		// Erstelle die Liste aller Kurse, auf die noch übertragen werden kann, dass heißt Kurse aller Fächer, die nicht im zuvor erstellten Set sind
		const alleKurse = props.getErgebnismanager().getKursmenge();
		for (const k of alleKurse) {
			if (!setFachFilter.has(k.fachID) || ausgewaehlteKurse.has(k.id))
				result.add(k);
		}
		return result;
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

	async function updateZuordnung(schueler: Schueler, neuer_kurs: GostBlockungsergebnisKurs | undefined | null) {
		const kurs = props.schuelerFilter().kurs;
		if (kurs === undefined)
			return;
		const alter_kurs = props.getErgebnismanager().getOfSchuelerOfFachZugeordneterKurs(schueler.id, kurs.fach_id);
		if (((neuer_kurs === undefined) || (neuer_kurs === null)) && (alter_kurs !== null)) {
			const zuordnung = new GostBlockungsergebnisKursSchuelerZuordnung();
			zuordnung.idSchueler = schueler.id;
			zuordnung.idKurs = alter_kurs.id;
			await props.removeKursSchuelerZuordnung([ zuordnung ]);
		} else if (neuer_kurs instanceof GostBlockungsergebnisKurs) {
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

	async function uebertragen() {
		const kurs = props.schuelerFilter().kurs;
		if (kurs === undefined)
			return;
		const kursSchueler = props.getErgebnismanager().getOfSchuelerMengeGefiltert(kurs.id, -1, -1, 0, "");
		for (const s of kursSchueler) {
			for (const k of _kurseZurUebertragung.value) {
				if (!props.getErgebnismanager().getOfSchuelerHatFachwahl(s.id, k.fachID, k.kursart))
					continue;
				const alter_kurs = props.getErgebnismanager().getOfSchuelerOfFachZugeordneterKurs(s.id, k.fachID);
				if (alter_kurs?.id === k.id)
					continue;
				await props.updateKursSchuelerZuordnung(s.id, k.id, alter_kurs?.id ?? undefined);
			}
		}
	}

</script>
