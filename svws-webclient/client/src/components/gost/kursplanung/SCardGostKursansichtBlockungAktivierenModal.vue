<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal :show="showModal" size="small" class="hidden">
		<template #modalTitle>Blockungsergebnis aktivieren</template>
		<template #modalDescription>
			Soll {{ blockungsname }} aktiviert werden?
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="showModal().value = false">Abbrechen</svws-ui-button>
			<svws-ui-button type="primary" @click="activate_ergebnis">Ja</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import type { GostBlockungsdatenManager } from '@core';
	import { ref } from 'vue';

	const props = defineProps<{
		blockungsname: string;
		getDatenmanager: () => GostBlockungsdatenManager;
		ergebnisAktivieren: () => Promise<boolean>;
	}>();

	const _showModal = ref<boolean>(false);
	const showModal = () => _showModal;

	async function activate_ergebnis() {
		showModal().value = false;
		await props.ergebnisAktivieren();
	}

	const openModal = () => {
		showModal().value = true;
	}

</script>
