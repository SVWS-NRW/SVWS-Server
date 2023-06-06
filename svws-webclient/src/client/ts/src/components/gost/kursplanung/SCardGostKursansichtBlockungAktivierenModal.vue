<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal ref="modal" size="small" class="hidden">
		<template #modalTitle>Blockungsergebnis aktivieren</template>
		<template #modalDescription>
			Soll {{ blockungsname }} aktiviert werden?
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="modal.closeModal()">Abbrechen</svws-ui-button>
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

	const modal = ref();

	async function activate_ergebnis() {
		modal.value.closeModal();
		await props.ergebnisAktivieren();
	}

	const openModal = () => {
		modal.value.openModal();
	}
</script>
