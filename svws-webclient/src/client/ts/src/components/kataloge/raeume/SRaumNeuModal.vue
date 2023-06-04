<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal ref="modal">
		<template #modalTitle>Raum hinzufügen</template>
		<template #modalContent>
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input v-model="item.kuerzel" required placeholder="Kürzel" />
				<svws-ui-text-input type="number" v-model="item.groesse" required placeholder="Raumgröße" />
				<svws-ui-text-input v-model="item.beschreibung" placeholder="Beschreibung" span="full" />
			</svws-ui-input-wrapper>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="modal.closeModal"> Abbrechen </svws-ui-button>
			<svws-ui-button type="secondary" @click="importer()" :disabled="!item.kuerzel || !item.groesse"> Raum hinzufügen </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">
	import { Raum } from "@svws-nrw/svws-core";
	import { ref } from "vue";

	const props = defineProps<{
		addRaum: (raum: Raum) => Promise<void>;
	}>();

	const modal = ref();
	const item = ref<Raum>(new Raum());

	const openModal = () => {
		modal.value.openModal();
	}

	async function importer() {
		await props.addRaum(item.value);
		item.value = new Raum();
		modal.value.closeModal();
	}
</script>
