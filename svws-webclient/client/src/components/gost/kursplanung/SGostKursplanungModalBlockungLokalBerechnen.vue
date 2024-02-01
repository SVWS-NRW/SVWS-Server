<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal :show="showModal" size="big" class="hidden">
		<template #modalTitle>Blockung lokal berechnen</template>
		<template #modalDescription>
			<div class="text-left pb-4">
				Zum Start auf „Berechnung starten“ klicken, sobald die Bedingungen erfüllt sind, mit denen die Berechnung durchgeführt wird,
				wird die Berechnung abgebrochen. Alternativ kann die Berechnung durch das Anklicken des Berechnung pausieren“ Knopfes unterbrochen werden.
				Die Ergebnisse können anschließend auf Wunsch in die Datenbank importiert werden. Der „Abbrechen“ Knopf beendet und löscht alle Berechnungen.
			</div>
			<div v-if="workerManager !== undefined" class="text-left pb-4 flex flex-row">
				Anzahl der parallelen Berechnungen:
				<div class="pl-4 pr-2"><svws-ui-button type="secondary" size="small" title="" @click="removeWorker" :disabled="workerManager.threads === 1"> <i-ri-subtract-line /> </svws-ui-button></div>
				{{ workerManager.threads }}
				<div class="pl-2"><svws-ui-button type="secondary" size="small" title="" @click="addWorker" :disabled="workerManager.threads === WorkerManagerKursblockung.MAX_WORKER"> <i-ri-add-line /> </svws-ui-button></div>
				<div class="pl-4"><svws-ui-button type="secondary" size="small" title="Maximum" @click="setWorkerMaximum" :disabled="workerManager.threads === WorkerManagerKursblockung.MAX_WORKER"> Maximum </svws-ui-button></div>
			</div>
			<svws-ui-table clickable v-model="selected" :selectable="liste.size() > 0 && !running" class="z-20 relative"
				:columns="cols" :items="liste" :count="!liste.isEmpty()">
				<template #cell(wert1)="{ rowData: row }">
					<div class="table-cell">
						<span class="svws-ui-badge min-w-[2.75rem] text-center justify-center" :title="`${getBewertungWert(row, 1)} Regelverletzungen`"
							:style="{'background-color': getBewertungColor(row, 1)}">{{ getBewertungWert(row, 1) }}</span>
					</div>
				</template>
				<template #cell(wert2)="{ rowData: row }">
					<div class="table-cell">
						<span class="svws-ui-badge min-w-[2.75rem] text-center justify-center" :title="`${getBewertungWert(row, 2)} Wahlkonflikte`"
							:style="{'background-color': getBewertungColor(row, 2)}">{{ getBewertungWert(row, 2) }}</span>
					</div>
				</template>
				<template #cell(wert3)="{ rowData: row }">
					<div class="table-cell">
						<span class="svws-ui-badge min-w-[2.75rem] text-center justify-center" :title="`Maximale Kursdifferenz: ${getBewertungWert(row, 3)}`"
							:style="{'background-color': getBewertungColor(row, 3)}">{{ getBewertungWert(row, 3) }}</span>
					</div>
				</template>
				<template #cell(wert4)="{ rowData: row }">
					<div class="table-cell">
						<span class="svws-ui-badge min-w-[2.75rem] text-center justify-center" :title="`${getBewertungWert(row, 4)} Fächer parallel`"
							:style="{'background-color': getBewertungColor(row, 4)}">{{ getBewertungWert(row, 4) }}</span>
					</div>
				</template>
			</svws-ui-table>
			<div class="flex flex-row gap-2 pt-4">
				<svws-ui-button v-if="!running" type="primary" @click="berechne">{{ (workerManager === undefined || !workerManager?.isInitialized()) ? 'Berechnung starten' : 'Berechnung fortsetzen' }}</svws-ui-button>
				<svws-ui-button v-else type="primary" @click="pause"><svws-ui-spinner spinning />&nbsp;Berechnung pausieren</svws-ui-button>
				<svws-ui-button v-if="selected.length > 0" @click="ergebnisseUebernehmen" type="secondary" :disabled="selected.length === 0">
					<i-ri-download-2-line />
					<span>{{ selected.length }} {{ selected.length !== 1 ? 'Ergebnisse' : 'Ergebnis' }} importieren und beenden</span>
				</svws-ui-button>
				<svws-ui-button type="danger" @click="showModal().value = false">Abbrechen</svws-ui-button>
			</div>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { computed, ref, shallowRef, watch } from 'vue';
	import { ArrayList, type GostBlockungsergebnis, type GostBlockungsdatenManager, GostBlockungsergebnisManager, type List } from "@core";
	import { WorkerManagerKursblockung } from './WorkerManagerKursblockung';

	const props = defineProps<{
		getDatenmanager: () => GostBlockungsdatenManager;
		addErgebnisse: (ergebnisse: List<GostBlockungsergebnis>) => Promise<void>;
	}>();

	const cols = [
		{ key: 'wert1', label: 'Regelverletzungen' },
		{ key: 'wert2', label: 'Wahlkonflikte' },
		{ key: 'wert3', label: 'Maximale Kursdifferenz' },
		{ key: 'wert4', label: 'Fächer parallel' },
	];

	const _showModal = ref<boolean>(false);
	const showModal = () => _showModal;

	const workerManager = shallowRef<WorkerManagerKursblockung | undefined>(undefined);

	watch(_showModal, neu => {
		if (workerManager.value !== undefined) {
			workerManager.value.terminate();
			workerManager.value = undefined;
			selected.value = [];
		}
		if (neu === true)
			workerManager.value = new WorkerManagerKursblockung(props.getDatenmanager().faecherManager().faecher(), props.getDatenmanager().daten());
	});

	const selected = ref<GostBlockungsergebnis[]>([]);

	const running = computed<boolean>(() => workerManager.value?.isRunning() ?? false);

	const liste = computed(() => workerManager.value?.getErgebnisse() ?? new ArrayList<GostBlockungsergebnis>());

	function addWorker() {
		if ((workerManager.value === undefined) || (workerManager.value.threads === WorkerManagerKursblockung.MAX_WORKER))
			return;
		workerManager.value.threads = workerManager.value.threads + 1;
	}

	function removeWorker() {
		if ((workerManager.value === undefined) || (workerManager.value.threads === 1))
			return;
		workerManager.value.threads = workerManager.value.threads - 1;
	}

	function setWorkerMaximum() {
		if (workerManager.value === undefined)
			return;
		workerManager.value.threads = WorkerManagerKursblockung.MAX_WORKER;
	}

	async function berechne() {
		if (workerManager.value !== undefined) {
			if (!workerManager.value.isInitialized())
				workerManager.value.init();
			workerManager.value.interval = 100;
			workerManager.value.start();
		}
	}

	async function pause() {
		workerManager.value?.pause();
		selected.value = [...liste.value];
	}

	async function ergebnisseUebernehmen() {
		if (selected.value.length === 0)
			return;
		const ergebnisse = new ArrayList<GostBlockungsergebnis>();
		for (const ergebnis of selected.value)
			ergebnisse.add(ergebnis);
		await props.addErgebnisse(ergebnisse);
		showModal().value = false;
	}

	function getBewertungWert(ergebnis: GostBlockungsergebnis, value: number) : number {
		switch(value) {
			case 1: return GostBlockungsergebnisManager.getOfBewertung1WertStatic(ergebnis.bewertung);
			case 2: return GostBlockungsergebnisManager.getOfBewertung2WertStatic(ergebnis.bewertung);
			case 3: return GostBlockungsergebnisManager.getOfBewertung3WertStatic(ergebnis.bewertung);
			case 4: return GostBlockungsergebnisManager.getOfBewertung4WertStatic(ergebnis.bewertung);
		}
		return Number.MAX_SAFE_INTEGER;
	}

	function getBewertungColor(ergebnis: GostBlockungsergebnis, value: number) : string {
		const h = Math.round((1 - (getBewertungCode(ergebnis, value)||0)) * 120);
		return `hsl(${h},100%,75%)`;
	}

	function getBewertungCode(ergebnis: GostBlockungsergebnis, value: number) : number {
		switch(value) {
			case 1: return GostBlockungsergebnisManager.getOfBewertung1FarbcodeStatic(ergebnis.bewertung);
			case 2: return GostBlockungsergebnisManager.getOfBewertung2FarbcodeStatic(ergebnis.bewertung);
			case 3: return GostBlockungsergebnisManager.getOfBewertung3FarbcodeStatic(ergebnis.bewertung);
			case 4: return GostBlockungsergebnisManager.getOfBewertung4FarbcodeStatic(ergebnis.bewertung);
		}
		return 1;
	}

	const openModal = () => {
		showModal().value = true;
	}

</script>
