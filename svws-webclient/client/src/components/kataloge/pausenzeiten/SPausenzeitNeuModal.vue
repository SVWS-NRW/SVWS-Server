<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal :show="showModal" class="hidden">
		<template #modalTitle>Pausenzeit hinzufügen</template>
		<template #modalContent>
			<div class="flex justify-center flex-wrap items-center gap-1">
				<svws-ui-multi-select v-model="wochentage" :items="Wochentag.values()" :item-text="i => i.beschreibung" required placeholder="Wochentage" />
				<svws-ui-text-input :model-value="DateUtils.getStringOfUhrzeitFromMinuten(item.beginn ?? 0)" @change="patchBeginn" required placeholder="Beginn" />
				<svws-ui-text-input :model-value="DateUtils.getStringOfUhrzeitFromMinuten(item.ende ?? 0)" @change="patchEnde" placeholder="Ende" />
			</div>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="showModal().value = false"> Abbrechen </svws-ui-button>
			<svws-ui-button type="secondary" @click="importer" :disabled="!item.beginn || !item.ende || (item.ende - item.beginn < 1) || !wochentage.length"> Pausenzeit Hinzufügen </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">
	import { ref } from "vue";
	import type { StundenplanPausenzeit} from "@core";
	import { Wochentag, DateUtils } from "@core";

	const props = defineProps<{
		addEintraege: (pausenzeit: Iterable<Partial<StundenplanPausenzeit>>) => Promise<void>;
	}>();

	const _showModal = ref<boolean>(false);
	const showModal = () => _showModal;
	const wochentage = ref<Wochentag[]>([Wochentag.MONTAG]);

	const item = ref<Partial<StundenplanPausenzeit>>({ beginn: 620, ende: 645, bezeichnung: 'Pause' });

	const openModal = () => {
		showModal().value = true;
	}

	async function importer() {
		const list = [];
		for (const tag of wochentage.value)
			list.push({wochentag: tag.id, beginn: item.value.beginn, ende: item.value.ende, bezeichnung: 'Pause'})
		await props.addEintraege(list);
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
