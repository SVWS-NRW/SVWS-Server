<template>
	<slot :open-modal />
	<svws-ui-modal v-model:show="show" class="hidden">
		<template #modalTitle>Aufsichtsbereich hinzufügen</template>
		<template #modalContent>
			<svws-ui-input-wrapper>
				<svws-ui-text-input v-model="item.kuerzel" required placeholder="Kürzel" />
				<svws-ui-text-input v-model="item.beschreibung" placeholder="Beschreibung" />
			</svws-ui-input-wrapper>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="show = false"> Abbrechen </svws-ui-button>
			<svws-ui-button type="secondary" @click="importer()" :disabled="!item.kuerzel"> Hinzufügen </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { ref } from "vue";
	import { Aufsichtsbereich } from "@core";

	const props = defineProps<{
		addAufsichtsbereich: (raum: Aufsichtsbereich) => Promise<void>;
	}>();

	const show = ref<boolean>(false);

	const item = ref<Aufsichtsbereich>(new Aufsichtsbereich());

	const openModal = () => {
		show.value = true;
	}

	async function importer() {
		await props.addAufsichtsbereich(item.value);
		item.value = new Aufsichtsbereich();
		show.value = false;
	}

</script>
