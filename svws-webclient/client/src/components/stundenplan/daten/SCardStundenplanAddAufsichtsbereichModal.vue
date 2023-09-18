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
			<svws-ui-button type="secondary" @click="importer()" :disabled="!item.kuerzel">Aufsichtsbereich hinzufügen </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">
	import { StundenplanAufsichtsbereich } from "@core";
	import { ref } from "vue";

	const props = defineProps<{
		addAufsichtsbereich: (raum: StundenplanAufsichtsbereich) => Promise<void>;
	}>();

	const modal = ref();
	const item = ref<StundenplanAufsichtsbereich>(new StundenplanAufsichtsbereich());

	const openModal = () => {
		modal.value.openModal();
	}

	async function importer() {
		await props.addAufsichtsbereich(item.value);
		console.log("Fertig")
		modal.value.closeModal();
	}
</script>
