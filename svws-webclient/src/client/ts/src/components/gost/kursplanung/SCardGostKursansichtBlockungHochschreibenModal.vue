<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal ref="modal" size="small">
		<template #modalTitle>Blockungsergebnis hochschreiben</template>
		<template #modalContent>
			<p>Soll das Blockungsergebnis in das nächste Halbjahr ({{ getDatenmanager().getHalbjahr().next()?.kuerzel }}) hochgeschrieben werden?</p>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="modal.closeModal()">Abbrechen</svws-ui-button>
			<svws-ui-button @click="hochschreiben_ergebnis">Ja</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">
	import type { GostBlockungsdatenManager } from '@svws-nrw/svws-core';
	import { ref } from 'vue';

	const props = defineProps<{
		getDatenmanager: () => GostBlockungsdatenManager;
		ergebnisHochschreiben: () => Promise<void>;
	}>();

	const modal = ref();

	async function hochschreiben_ergebnis() {
		modal.value.closeModal();
		await props.ergebnisHochschreiben();
	}

	const openModal = () => {
		modal.value.openModal();
	}
</script>