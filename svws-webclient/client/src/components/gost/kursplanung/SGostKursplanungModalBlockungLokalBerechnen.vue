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
			<svws-ui-button v-else type="primary" @click="running = false">Berechnung beenden</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import type { GostBlockungsdatenManager, GostBlockungsergebnisManager, List } from "@core";
	import { ArrayList, KursblockungAlgorithmusPermanent } from "@core";
	import { ref, computed } from 'vue';

	const props = defineProps<{
		getDatenmanager: () => GostBlockungsdatenManager;
	}>();

	const _showModal = ref<boolean>(false);
	const showModal = () => _showModal;

	const running = ref(false);
	const liste = ref<List<GostBlockungsergebnisManager>>(new ArrayList());

	async function berechne() {
		running.value = true;
		const algo = new KursblockungAlgorithmusPermanent(props.getDatenmanager());
		while (running.value)
			if (algo.next(100))
				liste.value = algo.getBlockungsergebnisse();
	}

	const log = computed(()=>console.log(liste.value))

	const openModal = () => {
		showModal().value = true;
	}

</script>
