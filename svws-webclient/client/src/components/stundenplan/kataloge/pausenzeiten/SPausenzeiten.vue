<template>
	<svws-ui-content-card v-if="auswahl" title="Allgemein" class="min-w-128">
		<div class="input-wrapper">
			<svws-ui-select :model-value="Wochentag.fromIDorException(auswahl.wochentag)" @update:model-value="wt => wt && patch({wochentag: wt.id})" :items="Wochentag.values()" :item-text="i=>i.beschreibung" />
			<svws-ui-text-input :model-value="DateUtils.getStringOfUhrzeitFromMinuten(auswahl.beginn ?? 0)" @change="patchBeginn" required placeholder="Stundenbeginn" />
			<svws-ui-text-input :model-value="DateUtils.getStringOfUhrzeitFromMinuten(auswahl.ende ?? 0)" @change="patchEnde" placeholder="Stundenende" />
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import type { PausenzeitenProps } from "./SPausenzeitenProps";
	import { Wochentag, DateUtils } from "@core";

	const props = defineProps<PausenzeitenProps>();

	async function patchBeginn(start: string | null) {
		if (start === null)
			return;
		const beginn = DateUtils.gibMinutenOfZeitAsString(start);
		await props.patch({beginn});
	}

	async function patchEnde(stop: string | null) {
		if (stop === null)
			return;
		const ende = DateUtils.gibMinutenOfZeitAsString(stop);
		await props.patch({ende});
	}

</script>
