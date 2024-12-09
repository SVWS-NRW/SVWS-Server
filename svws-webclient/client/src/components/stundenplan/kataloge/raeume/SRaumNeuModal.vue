<template>
	<slot :open-modal />
	<svws-ui-modal v-model:show="show" class="hidden">
		<template #modalTitle>Raum hinzufügen</template>
		<template #modalContent>
			<svws-ui-input-wrapper :grid="2">
				<svws-ui-text-input v-model="item.kuerzel" required placeholder="Kürzel" />
				<svws-ui-input-number v-model="item.groesse" required placeholder="Raumgröße" :min="1" />
				<svws-ui-text-input v-model="item.beschreibung" placeholder="Beschreibung" span="full" />
			</svws-ui-input-wrapper>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="show = false"> Abbrechen </svws-ui-button>
			<svws-ui-button type="secondary" @click="importer()" :disabled="item.kuerzel.trim().length === 0 || (item.groesse < 1)"> Raum hinzufügen </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { ref } from "vue";
	import { Raum } from "@core";

	const props = defineProps<{
		addRaum: (raum: Raum) => Promise<void>;
	}>();

	const show = ref<boolean>(false);

	const item = ref<Raum>(new Raum());
	item.value.groesse = 1;

	const openModal = () => {
		show.value = true;
	}

	async function importer() {
		await props.addRaum(item.value);
		item.value = new Raum();
		item.value.groesse = 1;
		show.value = false;
	}

</script>
