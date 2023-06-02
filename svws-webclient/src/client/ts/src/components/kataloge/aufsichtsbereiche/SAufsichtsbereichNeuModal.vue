<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal ref="modal">
		<template #modalTitle>Aufsichtsbereich hinzufügen</template>
		<template #modalContent>
			<svws-ui-input-wrapper>
				<svws-ui-text-input v-model="item.kuerzel" required placeholder="Kürzel" />
				<svws-ui-text-input v-model="item.beschreibung" placeholder="Beschreibung" />
			</svws-ui-input-wrapper>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="modal.closeModal"> Abbrechen </svws-ui-button>
			<svws-ui-button type="secondary" @click="importer()" :disabled="!item.kuerzel"> Hinzufügen </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">
	import { Aufsichtsbereich } from "@svws-nrw/svws-core";
	import { ref } from "vue";

	const props = defineProps<{
		addAufsichtsbereich: (raum: Aufsichtsbereich) => Promise<void>;
	}>();

	const modal = ref();
	const item = ref<Aufsichtsbereich>(new Aufsichtsbereich());

	const openModal = () => {
		modal.value.openModal();
	}

	async function importer() {
		await props.addAufsichtsbereich(item.value);
		item.value = new Aufsichtsbereich();
		modal.value.closeModal();
	}
</script>
