<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal :show="showModal" size="big" class="hidden">
		<template #modalTitle>Blockung lokal berechnen</template>
		<template #modalDescription>
			Zum Start auf „Berechnung starten“ klicken, sobald die Bedingungen erfüllt sind, mit denen die Berechnung durchgeführt wird,
			wird die Berechnung abgebrochen. Alternativ kann die Berechnung durch das Anklicken des „Berechnung beenden“ Knopfes beendet werden.
			Die Ergebnisse gehen dabei nicht verloren und können anschließend auf Wunsch in die Datenbank übernommen werden.
			<svws-ui-table :items="liste" />
		</template>
		<template #modalActions>
			<svws-ui-button v-if="!running" type="secondary" @click="showModal().value = false">Abbrechen</svws-ui-button>
			<svws-ui-button v-if="!running" type="primary" @click="berechne">Berechnung starten</svws-ui-button>
			<svws-ui-button v-else type="primary" @click="beenden">Berechnung beenden</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { computed, ref } from 'vue';
	import type { GostBlockungsdatenManager, List , GostBlockungsergebnisManager} from "@core";
	import { ArrayList, GostBlockungsdaten, GostFach} from "@core";

	const props = defineProps<{
		getDatenmanager: () => GostBlockungsdatenManager;
	}>();

	const _showModal = ref<boolean>(false);
	const showModal = () => _showModal;

	const running = ref(false);
	const liste = ref<List<GostBlockungsergebnisManager>>(new ArrayList());

	const worker = new Worker(new URL('./berechne_lokal_worker.ts', import.meta.url), {type: 'module'})
	// eslint-disable-next-line vue/no-setup-props-destructure
	const faecher = props.getDatenmanager().faecherManager().faecher();
	const arr: string[] = [];
	for (const f of faecher)
		arr.push(GostFach.transpilerToJSON(f));
	// eslint-disable-next-line vue/no-setup-props-destructure
	const blockungsdaten = GostBlockungsdaten.transpilerToJSON(props.getDatenmanager().daten());

	worker.postMessage({name: "init", faecher: arr, blockungsdaten});

	worker.onmessage = (e) => {
		const ergebnisse = e.data;
		const newList: List<GostBlockungsergebnisManager> = new ArrayList();
		// for (const e of ergebnisse)
		// 	newList.add(new GostBlockungsergebnisManager(props.getDatenmanager(), GostBlockungsergebnis.transpilerFromJSON(e)));
		// liste.value = newList;
		console.log(ergebnisse.length, e);
	};

	async function berechne() {
		running.value = true;
		worker.postMessage({name: "run"});
	}

	async function beenden() {
		running.value = false;
		worker.terminate();
		// worker.postMessage({name: 'stop'});
	}
	const log = computed(()=>console.log(liste.value))

	const openModal = () => {
		showModal().value = true;
	}

</script>
