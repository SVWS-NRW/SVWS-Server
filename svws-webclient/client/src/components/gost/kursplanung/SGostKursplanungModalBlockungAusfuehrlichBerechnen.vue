<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal :show="showModal" size="big" class="hidden" :auto-close="false" :close-in-title="false">
		<template #modalTitle>Ausführliche Berechnung lokal im Browser</template>
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
			<svws-ui-table clickable v-model="selected" :selectable="liste.size() > 0 && !running" class="z-20 relative" :columns="cols" :items="liste" :count="!liste.isEmpty()">
				<template #cell(wert1)="{ rowData: row, rowIndex }">
					<div class="table-cell">
						<svws-ui-tooltip v-if="getBewertungWert(row, 1) > 0" autosize>
							<span class="svws-ui-badge min-w-[2.75rem] text-center justify-center" :style="{'background-color': getBewertungColor(row, 1)}">{{ getBewertungWert(row, 1) }}</span>
							<template #content>
								{{ getBewertungWert(row, 1) }} Regelverletzungen
								<template v-for="typ in listErgebnismanager.get(rowIndex).regelGetMengeVerletzterTypen()" :key="typ.id">
									<template v-for="text in listErgebnismanager.get(rowIndex).regelGetMengeAnVerletzungen(typ)" :key="text+i">
										<br>{{ text }}
									</template>
								</template>
							</template>
						</svws-ui-tooltip>
						<span v-else class="svws-ui-badge min-w-[2.75rem] text-center justify-center" style="background-color: rgb(128, 255, 128)">0</span>
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
						<svws-ui-tooltip>
							<span class="svws-ui-badge min-w-[2.75rem] text-center justify-center" :style="{'background-color': getBewertungColor(row, 3)}">{{ getBewertungWert(row, 3) }}</span>
							<template #content>
								Maximale Kursdifferenz: {{ getBewertungWert(row, 3) }}
								<template v-for="d, i in row.bewertung.kursdifferenzHistogramm" :key="`${d}${i}`">
									<template v-if="(i === 1 && row.bewertung.kursdifferenzHistogramm[0] + row.bewertung.kursdifferenzHistogramm[1] > 0)"><br>Optimal 0/1: {{ row.bewertung.kursdifferenzHistogramm[0] + row.bewertung.kursdifferenzHistogramm[1] }}x</template>
									<template v-if="(d > 0) && (i >= 2)"><br>Differenz {{ i }}: {{ d }}x</template>
								</template>
							</template>
						</svws-ui-tooltip>
					</div>
				</template>
				<template #cell(wert4)="{ rowData: row, rowIndex }">
					<div class="table-cell">
						<svws-ui-tooltip v-if="getBewertungWert(row, 4) > 0" autosize>
							<span class="svws-ui-badge min-w-[2.75rem] text-center justify-center" :style="{'background-color': getBewertungColor(row, 4)}">{{ getBewertungWert(row, 4) }}</span>
							<template #content>
								<pre>{{ listErgebnismanager.get(rowIndex).regelGetTooltipFuerFaecherparallelitaet() }}</pre>
							</template>
						</svws-ui-tooltip>
						<span v-else class="svws-ui-badge min-w-[2.75rem] text-center justify-center" style="background-color: rgb(128, 255, 128)">0</span>
					</div>
				</template>
			</svws-ui-table>
			<div class="flex flex-row gap-2 pt-4">
				<template v-if="workerManager !== undefined">
					<svws-ui-button v-if="!running" type="primary" @click="berechne">{{ (workerManager.isInitialized() === false) ? 'Berechnung starten' : 'Berechnung fortsetzen' }}</svws-ui-button>
					<svws-ui-button v-else type="primary" @click="pause"><svws-ui-spinner spinning />&nbsp;Berechnung pausieren</svws-ui-button>
					<svws-ui-button v-if="selected.length > 0" @click="ergebnisseUebernehmen" type="secondary" :disabled="selected.length === 0">
						<i-ri-download-2-line />
						<span>{{ selected.length }} {{ selected.length !== 1 ? 'Ergebnisse' : 'Ergebnis' }} importieren und beenden</span>
					</svws-ui-button>
					<svws-ui-button v-if="!nachfragen" type="danger" @click="liste.size() > 0 ? nachfragen = true : closeModal()">Abbrechen</svws-ui-button>
					<svws-ui-button v-else type="danger" @click="closeModal">Alle berechneten Ergebnisse verwerfen und schließen</svws-ui-button>
				</template>
				<template v-else>
					<div class="flex gap-2 w-full">
						<svws-ui-button v-if="!nachfragen" type="danger" @click="liste.size() > 0 ? nachfragen = true : closeModal()">Abbrechen</svws-ui-button>
						<div>Der Worker zum Berechnen der Blockungen konnte nicht erstellt werden, bitte Fehlermeldungen überprüfen.</div>
					</div>
				</template>
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

	const liste = computed<List<GostBlockungsergebnis>>(() => workerManager.value?.getErgebnisse() ?? new ArrayList<GostBlockungsergebnis>());
	const listErgebnismanager = computed<List<GostBlockungsergebnisManager>>(() => workerManager.value?.getErgebnisManager() ?? new ArrayList<GostBlockungsergebnisManager>());

	const nachfragen = ref(false);

	async function getWaiting() {
		const p = workerManager.value?.waiting;
		if (p !== undefined)
			await Promise.allSettled(p);
	}

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
		await getWaiting();
		selected.value = [...liste.value];
	}

	async function ergebnisseUebernehmen() {
		if (selected.value.length === 0)
			return;
		const ergebnisse = new ArrayList<GostBlockungsergebnis>();
		for (const ergebnis of selected.value)
			ergebnisse.add(ergebnis);
		await props.addErgebnisse(ergebnisse);
		closeModal();
	}

	function getBewertungWert(ergebnis: GostBlockungsergebnis, value: number) : number {
		switch(value) {
			case 1: return GostBlockungsergebnisManager.getOfBewertung1WertStatic(ergebnis.bewertung);
			case 2: return GostBlockungsergebnisManager.getOfBewertung2WertStatic(ergebnis.bewertung);
			case 3: return GostBlockungsergebnisManager.getOfBewertung3WertStatic(ergebnis.bewertung);
			case 4: return GostBlockungsergebnisManager.getOfBewertung4WertStatic(ergebnis.bewertung);
			default: return Number.MAX_SAFE_INTEGER;
		}
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
			default: return 1;
		}
	}

	const openModal = () => showModal().value = true;
	const closeModal = () => (showModal().value = false) && (nachfragen.value = false);

</script>
