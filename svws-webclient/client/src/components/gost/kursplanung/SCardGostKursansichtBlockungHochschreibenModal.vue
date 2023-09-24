<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal :show="showModal" size="small" class="hidden">
		<template #modalTitle>Blockungsergebnis hochschreiben</template>
		<template #modalContent>
			<p>Soll das Blockungsergebnis in das nächste Halbjahr ({{ getDatenmanager().getHalbjahr().next()?.kuerzel }}) hochgeschrieben werden?</p>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="showModal().value = false">Abbrechen</svws-ui-button>
			<svws-ui-button @click="hochschreiben_ergebnis">Ja</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import type { GostBlockungsdatenManager } from '@core';
	import { ref } from 'vue';

	const props = defineProps<{
		getDatenmanager: () => GostBlockungsdatenManager;
		ergebnisHochschreiben: () => Promise<void>;
	}>();

	const _showModal = ref<boolean>(false);
	const showModal = () => _showModal;

	async function hochschreiben_ergebnis() {
		showModal().value = false;
		await props.ergebnisHochschreiben();
	}

	const openModal = () => {
		showModal().value = true;
	}

</script>
