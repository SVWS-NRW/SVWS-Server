<template>
	<slot :open-modal />
	<svws-ui-modal v-model:show="show" size="small" class="hidden">
		<template #modalTitle>Ergebnis hochschreiben</template>
		<template #modalDescription>
			<p>Das Blockungsergebnis wird mit dieser Aktion in das nächste Halbjahr ({{ getDatenmanager().getHalbjahr().next()?.kuerzel || '(kein Folgehalbjahr)' }}) hochgeschrieben.</p>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="show = false">Abbrechen</svws-ui-button>
			<svws-ui-button @click="hochschreiben_ergebnis">Hochschreiben</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { ref } from 'vue';
	import type { GostBlockungsdatenManager } from '@core';

	const props = defineProps<{
		getDatenmanager: () => GostBlockungsdatenManager;
		ergebnisHochschreiben: () => Promise<void>;
	}>();

	const show = ref<boolean>(false);

	async function hochschreiben_ergebnis() {
		show.value = false;
		await props.ergebnisHochschreiben();
	}

	const openModal = () => {
		show.value = true;
	}

</script>
