<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal :show="showModal" class="hidden">
		<template #modalTitle>Pausenzeit hinzufügen</template>
		<template #modalContent>
			<div class="flex justify-center flex-wrap items-center gap-2">
				<svws-ui-multi-select v-model="wochentage" :items="Wochentag.values()" :item-text="i => i.beschreibung" required placeholder="Wochentage" title="Wochentage" />
				<svws-ui-text-input :model-value="DateUtils.getStringOfUhrzeitFromMinuten(item.beginn ?? 0)" @change="patchBeginn" required placeholder="Beginn" :valid="() => !disabled" />
				<svws-ui-text-input :model-value="DateUtils.getStringOfUhrzeitFromMinuten(item.ende ?? 0)" @change="patchEnde" placeholder="Ende" :valid="() => !disabled" />
				<svws-ui-multi-select v-model="klassen" title="Klassen" :items="[...stundenplanManager().klasseGetMengeAsList()].map(k=>k.id)" :item-text="klasse => stundenplanManager?.().klasseGetByIdOrException(klasse).kuerzel || ''" />
			</div>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="showModal().value = false"> Abbrechen </svws-ui-button>
			<svws-ui-button type="secondary" @click="importer" :disabled="!item.beginn || !item.ende || (item.ende - item.beginn < 1) || !wochentage.length || disabled"> Pausenzeit Hinzufügen </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { ref, computed } from "vue";
	import type { StundenplanManager, StundenplanPausenzeit } from "@core";
	import { Wochentag, DateUtils, ArrayList } from "@core";

	const props = defineProps<{
		addPausenzeiten: (pausenzeiten: Iterable<Partial<StundenplanPausenzeit>>) => Promise<void>;
		stundenplanManager: () => StundenplanManager;
	}>();

	const _showModal = ref<boolean>(false);
	const showModal = () => _showModal;
	const wochentage = ref<Wochentag[]>(props.stundenplanManager().zeitrasterGetWochentageAlsEnumRange());

	const klassen = ref<number[]>([]);

	const item = ref<Partial<StundenplanPausenzeit>>({ beginn: 620, ende: 645, bezeichnung: 'Pause' });

	const openModal = () => {
		showModal().value = true;
	}

	const disabled = computed<boolean>(() => {
		for (const w of wochentage.value)
			if (props.stundenplanManager().pausenzeitExistsByWochentagAndBeginnAndEnde(w.id, item.value.beginn ?? null, item.value.ende ?? null))
				return true;
		return false;
	})

	async function importer() {
		const list = [];
		const listKlassen = new ArrayList<number>();
		for (const klasse of klassen.value)
			listKlassen.add(klasse);
		for (const tag of wochentage.value)
			list.push({wochentag: tag.id, beginn: item.value.beginn, ende: item.value.ende, bezeichnung: 'Pause', klassen: listKlassen})
		showModal().value = false;
		await props.addPausenzeiten(list);
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
