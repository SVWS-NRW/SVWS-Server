<template>
	<svws-ui-content-card :title="`Pause am ${Wochentag.fromIDorException(item.wochentag)}`">
		<div v-if="!item.klassen.isEmpty()" class="text-sm -mt-1 mb-3 opacity-70 font-bold">Klassen:  {{ [...item.klassen].map(k => " " + stundenplanManager().klasseGetByIdOrException(k).kuerzel).toString() }} </div>
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-text-input :model-value="DateUtils.getStringOfUhrzeitFromMinuten(item.beginn ?? 0)" required placeholder="Pausenbeginn" @change="patchBeginn" />
			<svws-ui-text-input :model-value="DateUtils.getStringOfUhrzeitFromMinuten(item.ende ?? 0)" placeholder="Pausenende" @change="patchEnde" />
			<div class="col-span-full">
				<svws-ui-button type="danger" @click="removePausenzeiten([item])"> Pause entfernen </svws-ui-button>
			</div>
		</svws-ui-input-wrapper>
		<svws-ui-spacing :size="2" />
		<svws-ui-table :items="stundenplanManager().pausenaufsichtGetMengeByPausenzeitId(item.id)" :columns>
			<template #cell(id)="{ rowData }">
				{{ stundenplanManager().lehrerGetByIdOrException(rowData.idLehrer).kuerzel }}
			</template>
			<template #cell(bereiche)="{ value }">
				<template v-for="bereich in value">{{ stundenplanManager().aufsichtsbereichGetByIdOrException(bereich).kuerzel }} &nbsp;</template>
			</template>
			<template #cell(wochentyp)="{ value }">
				<span v-if="value === 0" class="opacity-25">—</span>
				<template v-else>{{ String.fromCharCode(64 + value) }}</template>
			</template>
		</svws-ui-table>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import type { LehrerListeEintrag, List, StundenplanAufsichtsbereich, StundenplanManager, StundenplanPausenzeit } from "@core";
	import { DateUtils, Wochentag } from "@core";

	const props = defineProps<{
		item: StundenplanPausenzeit;
		stundenplanManager: () => StundenplanManager;
		patchPausenzeit: (data: Partial<StundenplanPausenzeit>, id: number) => Promise<void>;
		removePausenzeiten: (multi: Iterable<StundenplanPausenzeit>) => Promise<void>;
		listLehrer: List<LehrerListeEintrag>;
		listAufsichtsbereiche: List<StundenplanAufsichtsbereich>;
	}>();

	const columns = [
		{ key: "id", label: "Aufsicht" },
		{ key: 'bereiche', label: "Bereich", span: 1 },
		{ key: 'wochentyp', label: "Typ", tooltip: "Wochentyp", span: 0.5 }
	];

	async function patchBeginn(event: string | number) {
		if (typeof event === 'number')
			return;
		const beginn = DateUtils.gibMinutenOfZeitAsString(event);
		await props.patchPausenzeit({beginn}, props.item.id);
	}

	async function patchEnde(event: string | number) {
		if (typeof event === 'number')
			return;
		const ende = DateUtils.gibMinutenOfZeitAsString(event);
		await props.patchPausenzeit({ende}, props.item.id);
	}

</script>
