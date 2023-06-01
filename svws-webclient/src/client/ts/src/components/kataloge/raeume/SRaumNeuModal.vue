<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal ref="modal">
		<template #modalTitle>Raum hinzufügen</template>
		<template #modalContent>
			<div class="flex justify-center flex-wrap items-center gap-1">
				<SvwsUiTextInput v-model="item.kuerzel" required placeholder="Kürzel" />
				<SvwsUiTextInput type="number" v-model="item.groesse" required placeholder="Raumgröße" />
				<SvwsUiTextInput v-model="item.beschreibung" placeholder="Beschreibung" />
			</div>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="modal.closeModal"> Abbrechen </svws-ui-button>
			<svws-ui-button type="secondary" @click="importer()" :disabled="!item.kuerzel || !item.groesse"> Raum Hinzufügen </svws-ui-button>
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
