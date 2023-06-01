<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal ref="modal">
		<template #modalTitle>Pausenzeit hinzufügen</template>
		<template #modalContent>
			<div class="flex justify-center flex-wrap items-center gap-1">
				<svws-ui-multi-select :model-value="Wochentag.MONTAG" @update:model-value="item.wochentag=$event.id" :items="Wochentag.values()" :item-text="i=>i.beschreibung" required placeholder="Wochentag" />
				<SvwsUiTextInput type="number" v-model="item.beginn" required placeholder="Beginn" />
				<SvwsUiTextInput type="number" v-model="item.ende" required placeholder="Ende" />
			</div>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="modal.closeModal"> Abbrechen </svws-ui-button>
			<svws-ui-button type="secondary" @click="importer()" :disabled="!item.wochentag || !item.beginn || !item.ende"> Pausenzeit Hinzufügen </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">
	import { StundenplanPausenzeit, Wochentag } from "@svws-nrw/svws-core";
	import { ref } from "vue";

	const props = defineProps<{
		addPausenzeit: (raum: StundenplanPausenzeit) => Promise<void>;
	}>();

	const modal = ref();
	const item = ref<StundenplanPausenzeit>(new StundenplanPausenzeit());

	const openModal = () => {
		modal.value.openModal();
	}

	async function importer() {
		await props.addPausenzeit(item.value);
		item.value = new StundenplanPausenzeit();
		modal.value.closeModal();
	}
</script>
