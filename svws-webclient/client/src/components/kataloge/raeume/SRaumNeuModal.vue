<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal :show="showModal" class="hidden">
		<template #modalTitle>Raum hinzufügen</template>
		<template #modalContent>
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input v-model="item.kuerzel" required placeholder="Kürzel" />
				<svws-ui-input-number v-model="item.groesse" required placeholder="Raumgröße" :min="1" />
				<svws-ui-text-input v-model="item.beschreibung" placeholder="Beschreibung" span="full" />
			</svws-ui-input-wrapper>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="showModal().value = false"> Abbrechen </svws-ui-button>
			<svws-ui-button type="secondary" @click="importer()" :disabled="!item.kuerzel || !item.groesse"> Raum hinzufügen </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { Raum } from "@core";
	import { ref } from "vue";

	const props = defineProps<{
		addRaum: (raum: Raum) => Promise<void>;
	}>();

	const _showModal = ref<boolean>(false);
	const showModal = () => _showModal;

	const item = ref<Raum>(new Raum());
	item.value.groesse = 1;

	const openModal = () => {
		showModal().value = true;
	}

	async function importer() {
		await props.addRaum(item.value);
		item.value = new Raum();
		showModal().value = false;
	}

</script>
