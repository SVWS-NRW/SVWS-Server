<template>
	<slot :open-modal />
	<svws-ui-modal v-model:show="show" size="medium" class="hidden" type="danger">
		<template #modalTitle>Übertragung rückgängig machen</template>
		<template #modalDescription>
			<div class="text-justify space-y-4">
				<div>
					Soll die Übertragung der Blockung für dieses Halbjahr rückgängig gemacht werden?
					<br>Dies bedeutet, dass die Leistungsdaten und Kursbelegungen der Schüler für dieses Halbjahr entfernt werden.
					<br>Ebenso wird die Klausurplanung auf Basis der jeweiligen Blockung gelöscht
				</div>
			</div>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="show = false">Abbrechen</svws-ui-button>
			<svws-ui-button type="danger" @click="activate_revert">Ja</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { ref } from 'vue';

	const props = defineProps<{
		revertBlockung: () => Promise<void>;
	}>();

	const show = ref<boolean>(false);

	async function activate_revert() {
		show.value = false;
		await props.revertBlockung();
	}

	const openModal = () => {
		show.value = true;
	}

</script>
