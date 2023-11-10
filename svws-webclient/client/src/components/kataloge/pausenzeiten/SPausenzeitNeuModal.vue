<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal :show="showModal" class="hidden">
		<template #modalTitle>Pausenzeit hinzufügen</template>
		<template #modalContent>
			<div class="flex justify-center flex-wrap items-center gap-1">
				<svws-ui-select :model-value="Wochentag.fromIDorException(item.wochentag)" @update:model-value="wt => item.wochentag=wt!.id" :items="Wochentag.values()" :item-text="i=>i.beschreibung" required placeholder="Wochentag" />
				<svws-ui-text-input type="number" v-model="item.beginn" required placeholder="Beginn" />
				<svws-ui-text-input type="number" v-model="item.ende" required placeholder="Ende" />
			</div>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="showModal().value = false"> Abbrechen </svws-ui-button>
			<svws-ui-button type="secondary" @click="importer()" :disabled="!item.wochentag || !item.beginn || !item.ende"> Pausenzeit Hinzufügen </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">
	import { StundenplanPausenzeit, Wochentag } from "@core";
	import { ref } from "vue";

	const props = defineProps<{
		addPausenzeit: (raum: StundenplanPausenzeit) => Promise<void>;
	}>();

	const _showModal = ref<boolean>(false);
	const showModal = () => _showModal;

	const item = ref<StundenplanPausenzeit>(new StundenplanPausenzeit());

	const openModal = () => {
		showModal().value = true;
	}

	async function importer() {
		await props.addPausenzeit(item.value);
		item.value = new StundenplanPausenzeit();
		showModal().value = false;
	}
</script>
