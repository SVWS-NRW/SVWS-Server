<template>
	<slot :open-modal />
	<svws-ui-modal v-model:show="show" size="small" class="hidden" :type="hatUngueltigeKurszuordnungen ? 'danger' : 'default'">
		<template #modalTitle>Blockungsergebnis {{ hatUngueltigeKurszuordnungen ? 'trotzdem':'' }} übertragen</template>
		<template #modalDescription>
			Soll {{ blockungsname }} übertragen werden{{ hatUngueltigeKurszuordnungen ? ', obwohl ungültige Kurszuordnungen vorliegen':'' }}?
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="show = false">Abbrechen</svws-ui-button>
			<svws-ui-button type="primary" @click="activate_ergebnis">Ja</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { ref } from 'vue';

	const props = defineProps<{
		blockungsname: string;
		hatUngueltigeKurszuordnungen: boolean;
		ergebnisAktivieren: () => Promise<boolean>;
	}>();

	const show = ref<boolean>(false);

	async function activate_ergebnis() {
		show.value = false;
		await props.ergebnisAktivieren();
	}

	const openModal = () => {
		show.value = true;
	}

</script>
