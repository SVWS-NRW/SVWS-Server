<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal ref="modal">
		<template #modalTitle>Aufsichtsbereich hinzufügen</template>
		<template #modalContent>
			<div class="flex justify-center flex-wrap items-center gap-1">
				<SvwsUiTextInput v-model="item.kuerzel" required placeholder="Kürzel" />
				<SvwsUiTextInput v-model="item.beschreibung" placeholder="Beschreibung" />
			</div>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="modal.closeModal"> Abbrechen </svws-ui-button>
			<svws-ui-button type="secondary" @click="importer()" :disabled="!item.kuerzel">Aufsichtsbereich Hinzufügen </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">
	import { StundenplanAufsichtsbereich } from "@svws-nrw/svws-core";
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
