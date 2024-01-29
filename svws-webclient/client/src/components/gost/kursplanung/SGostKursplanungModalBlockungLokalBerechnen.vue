<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal :show="showModal" size="big" class="hidden">
		<template #modalTitle>Blockung lokal berechnen</template>
		<template #modalDescription>
			Zum Start auf „Berechnung starten“ klicken, sobald die Bedingungen erfüllt sind, mit denen die Berechnung durchgeführt wird,
			wird die Berechnung abgebrochen. Alternativ kann die Berechnung durch das Anklicken des „Berechnung beenden“ Knopfes beendet werden.
			Die Ergebnisse gehen dabei nicht verloren und können anschließend auf Wunsch in die Datenbank übernommen werden.
			<svws-ui-table clickable v-model="selected" :selectable="liste.length > 0 && !running" class="z-20 relative"
				:columns="[{ key: 'bewertung', label: 'Ergebnis' }]" :items="liste" :count="liste.length > 1">
				<template #header(bewertung)>
					<svws-ui-tooltip indicator="help">
						<span class="my-0.5">Ergebnis</span>
						<template #content>
							<div class="normal-case text-base rich-text">
								<div class="my-1">Bewertung durch:</div>
								<ul class="mb-1 list-disc pl-4">
									<li>Regelverletzungen</li>
									<li>Wahlkonflikte</li>
									<li>max. Kursdifferenz</li>
									<li>Fächerparallelität</li>
								</ul>
							</div>
						</template>
					</svws-ui-tooltip>
				</template>
				<template #cell(bewertung)="{ rowData: row }">
					<div class="inline-flex flex-wrap w-full gap-x-1 gap-y-2.5">
						<span class="flex gap-1 items-center ml-0.5">
							<span class="svws-ui-badge min-w-[2.75rem] text-center justify-center" :title="`${row.wert1} Regelverletzungen`" :style="{'background-color': row.color1}">{{ row.wert1 }}</span>
							<span class="svws-ui-badge min-w-[2.75rem] text-center justify-center" :title="`${row.wert2} Wahlkonflikte`" :style="{'background-color': row.color2}">{{ row.wert2 }}</span>
							<span class="svws-ui-badge min-w-[2.75rem] text-center justify-center" :title="`Maximale Kursdifferenz: ${row.wert3}`" :style="{'background-color': row.color3}">{{ row.wert3 }}</span>
							<span class="svws-ui-badge min-w-[2.75rem] text-center justify-center" :title="`${row.wert4} Fächer parallel`" :style="{'background-color': row.color4}">{{ row.wert4 }}</span>
						</span>
					</div>
				</template>
				<template #actions v-if="selected.length">
					<svws-ui-button @click="undefined" type="transparent" :disabled="selected.length === 0">
						<i-ri-download-2-line />
						<span>{{ selected.length }} {{ selected.length > 1 ? 'Ergebnisse' : 'Ergebnis' }} importieren</span>
					</svws-ui-button>
				</template>
			</svws-ui-table>
		</template>
		<template #modalActions>
			<svws-ui-button v-if="!running" type="secondary" @click="showModal().value = false">Abbrechen</svws-ui-button>
			<svws-ui-button v-if="!running" type="primary" @click="berechne">{{ running === undefined ? 'Berechnung starten' : 'Berechnung fortsetzen' }}</svws-ui-button>
			<svws-ui-button v-else type="primary" @click="beenden">Berechnung unterbrechen</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	type Message = { cmd: string; faecher?: string[], blockungsdaten?: string, result?: boolean | string[] | Bewertung[]};
	type Bewertung = {
		wert1: number;
		color1: string;
		wert2: number;
		color2: string;
		wert3: number;
		color3: string;
		wert4: number;
		color4: string;
	};

	import { computed, ref } from 'vue';
	import type { GostBlockungsdatenManager } from "@core";
	import { GostBlockungsdaten, GostFach } from "@core";

	const props = defineProps<{
		getDatenmanager: () => GostBlockungsdatenManager;
	}>();

	const _showModal = ref<boolean>(false);
	const showModal = () => _showModal;

	const running = ref<boolean|undefined>(undefined);
	const ready = ref<boolean>(false);
	const liste = ref<Bewertung[]>([]);
	const selected = ref<Bewertung[]>([]);

	const worker = new Worker(new URL('./berechne_lokal_worker.ts', import.meta.url), {type: 'module'});

	const faecher = computed(()=> {
		const arr = [];
		for (const f of props.getDatenmanager().faecherManager().faecher())
			arr.push(GostFach.transpilerToJSON(f));
		return arr;
	});

	const blockungsdaten = computed(() => GostBlockungsdaten.transpilerToJSON(props.getDatenmanager().daten()));

	const message: Message = {cmd: "init", faecher: faecher.value, blockungsdaten: blockungsdaten.value};
	worker.postMessage(message);

	worker.onmessage = (e) => {
		const data: Message = e.data;

		switch (data.cmd) {
			case 'init':
				if (data.result !== undefined && typeof data.result === 'boolean')
					ready.value = data.result;
				break;
			case 'next':
				if (running.value === true)
					worker.postMessage({cmd: 'next'});
				if (data.result === true)
					worker.postMessage({cmd: 'getErgbnisBewertungen'});
				break;
			case 'getErgbnisBewertungen':
				if (Array.isArray(data.result))
					liste.value = data.result as Bewertung[];
				break;
			case 'getErgebnisse':
				if (Array.isArray(data.result))
					console.log(data.result);
				break;
			default:
				break;
		}
	};

	async function berechne() {
		running.value = true;
		worker.postMessage({cmd: "next"});
	}

	async function beenden() {
		running.value = false;
		// worker.terminate();
		// worker.postMessage({name: 'stop'});
	}
	const log = computed(()=>console.log(liste.value))

	const openModal = () => {
		showModal().value = true;
	}

</script>
