<template>
	<slot :open-modal />
	<svws-ui-modal v-model:show="show" class="hidden">
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
			<svws-ui-button type="secondary" @click="show = false"> Abbrechen </svws-ui-button>
			<svws-ui-button type="secondary" @click="importer" :disabled="!item.beginn || !item.ende || (item.ende - item.beginn < 1) || !wochentage.length || disabled"> Pausenzeit Hinzufügen </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import type { StundenplanPausenzeit } from "@core/core/data/stundenplan/StundenplanPausenzeit";
	import { Wochentag } from "@core/core/types/Wochentag";
	import { DateUtils } from "@core/core/utils/DateUtils";
	import type { StundenplanManager } from "@core/core/utils/stundenplan/StundenplanManager";
	import { ArrayList } from "@core/java/util/ArrayList";
	import { ref, computed } from "vue";

	const props = defineProps<{
		addPausenzeiten: (pausenzeiten: Iterable<Partial<StundenplanPausenzeit>>) => Promise<void>;
		stundenplanManager: () => StundenplanManager;
	}>();

	const show = ref<boolean>(false);
	// eslint-disable-next-line vue/no-setup-props-reactivity-loss
	const wochentage = ref<Wochentag[]>(props.stundenplanManager().zeitrasterGetWochentageAlsEnumRange());

	const klassen = ref<number[]>([]);

	const item = ref<Partial<StundenplanPausenzeit>>({ beginn: 620, ende: 645, bezeichnung: 'Pause' });

	const openModal = () => {
		show.value = true;
	};

	const disabled = computed<boolean>(() => {
		for (const w of wochentage.value) {
			if (props.stundenplanManager().pausenzeitExistsByWochentagAndBeginnAndEnde(w.id, item.value.beginn ?? null, item.value.ende ?? null)) {
				return true;
			}
		}
		return false;
	});

	async function importer() {
		const list = [];
		const listKlassen = new ArrayList<number>();
		for (const klasse of klassen.value) {
			listKlassen.add(klasse);
		}
		for (const tag of wochentage.value) {
			list.push({ wochentag: tag.id, beginn: item.value.beginn, ende: item.value.ende, bezeichnung: 'Pause', klassen: listKlassen });
		}
		show.value = false;
		await props.addPausenzeiten(list);
	}

	async function patchBeginn(start: string | null) {
		if (start === null) {
			return;
		}
		const stundenbeginn = DateUtils.gibMinutenOfZeitAsString(start);
		item.value.beginn = stundenbeginn;
	}

	async function patchEnde(ende: string | null) {
		if (ende === null) {
			return;
		}
		const stundenende = DateUtils.gibMinutenOfZeitAsString(ende);
		item.value.ende = stundenende;
	}

</script>
