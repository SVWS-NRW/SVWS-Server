<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal :show="showModal">
		<template #modalTitle>Pausenzeit hinzufügen</template>
		<template #modalContent>
			<svws-ui-input-wrapper>
				<svws-ui-select :model-value="Wochentag.fromIDorException(item.id)" @update:model-value="wt => item.wochentag=wt!.id" :items="Wochentag.values()" :item-text="i=>i.beschreibung" required placeholder="Wochentag" />
				<svws-ui-text-input :model-value="DateUtils.getStringOfUhrzeitFromMinuten(item.beginn ?? 0)" @change="patchBeginn" required placeholder="Beginn" />
				<svws-ui-text-input :model-value="DateUtils.getStringOfUhrzeitFromMinuten(item.ende ?? 0)" @change="patchEnde" placeholder="Ende" />
				<svws-ui-text-input v-model="item.beginn" required placeholder="Beginn" />
				<svws-ui-text-input v-model="item.ende" required placeholder="Ende" />
			</svws-ui-input-wrapper>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="showModal().value = false"> Abbrechen </svws-ui-button>
			<svws-ui-button type="secondary" @click="importer()" :disabled="!item.wochentag || !item.beginn || !item.ende"> Pausenzeit hinzufügen </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { DateUtils, StundenplanPausenzeit, Wochentag } from "@core";
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
	async function patchBeginn(start: string | null) {
		if (start === null)
			return;
		const stundenbeginn = DateUtils.gibMinutenOfZeitAsString(start);
		item.value.beginn = stundenbeginn;
	}

	async function patchEnde(ende: string | null) {
		if (ende === null)
			return;
		const stundenende = DateUtils.gibMinutenOfZeitAsString(ende);
		item.value.ende = stundenende;
	}

	async function importer() {
		await props.addPausenzeit(item.value);
		showModal().value = false;
	}

</script>
