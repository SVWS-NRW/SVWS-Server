<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal :show="showModal">
		<template #modalTitle>Raum hinzufügen</template>
		<template #modalContent>
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input v-model="item.kuerzel" required placeholder="Kürzel" />
				<svws-ui-text-input type="number" v-model="item.groesse" required placeholder="Raumgröße" />
				<svws-ui-text-input v-model="item.beschreibung" placeholder="Beschreibung" span="full" />
			</svws-ui-input-wrapper>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="showModal().value = false"> Abbrechen </svws-ui-button>
			<svws-ui-button type="secondary" @click="importer()" :disabled="!item.kuerzel || !item.groesse"> Raum Hinzufügen </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { StundenplanRaum } from "@core";
	import { ref } from "vue";

	const props = defineProps<{
		addRaum: (raum: StundenplanRaum) => Promise<void>;
	}>();

	const _showModal = ref<boolean>(false);
	const showModal = () => _showModal;

	const item = ref<StundenplanRaum>(new StundenplanRaum());

	const openModal = () => {
		showModal().value = true;
	}

	async function importer() {
		await props.addRaum(item.value);
		showModal().value = false;
	}

</script>
