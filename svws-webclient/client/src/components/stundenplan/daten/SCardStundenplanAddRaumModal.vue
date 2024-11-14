<template>
	<slot :open-modal />
	<svws-ui-modal v-model:show="show">
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

	const show = ref<boolean>(false);

	const item = ref<StundenplanRaum>(new StundenplanRaum());
	item.value.groesse = 30;

	const openModal = () => {
		show.value = true;
	}

	async function importer() {
		await props.addRaum(item.value);
		show.value = false;
	}

</script>
