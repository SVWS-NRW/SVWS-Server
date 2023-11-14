<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal :show="showModal" class="hidden">
		<template #modalTitle>Pausenzeit hinzufügen</template>
		<template #modalContent>
			<div class="flex justify-center flex-wrap items-center gap-1">
				<svws-ui-select :model-value="Wochentag.fromIDorException(item.wochentag!)" @update:model-value="wt => item.wochentag=wt!.id" :items="Wochentag.values()" :item-text="i => i.beschreibung" required placeholder="Wochentag" />
				<svws-ui-text-input :model-value="DateUtils.getStringOfUhrzeitFromMinuten(item.beginn ?? 0)" @change="patchBeginn" required placeholder="Stundenbeginn" />
				<svws-ui-text-input :model-value="DateUtils.getStringOfUhrzeitFromMinuten(item.ende ?? 0)" @change="patchEnde" placeholder="Stundenende" />
			</div>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="showModal().value = false"> Abbrechen </svws-ui-button>
			<svws-ui-button type="secondary" @click="importer()" :disabled="!item.wochentag || (!item.beginn || !item.ende)"> Pausenzeit Hinzufügen </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">
	import { StundenplanPausenzeit, Wochentag, DateUtils } from "@core";
	import { ref } from "vue";

	const props = defineProps<{
		addPausenzeit: (raum: Partial<StundenplanPausenzeit>) => Promise<void>;
	}>();

	const _showModal = ref<boolean>(false);
	const showModal = () => _showModal;

	const item = ref<Partial<StundenplanPausenzeit>>({ wochentag: 1, beginn: 620, ende: 645, bezeichnung: 'Pause' });

	const openModal = () => {
		showModal().value = true;
	}

	async function importer() {
		await props.addPausenzeit(item.value);
		item.value = new StundenplanPausenzeit();
		showModal().value = false;
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

</script>
