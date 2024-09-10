<template>
	<svws-ui-content-card :title="`Pause am ${Wochentag.fromIDorException(selected.wochentag)}`">
		<div v-if="!selected.klassen.isEmpty()" class="text-sm -mt-1 mb-3 opacity-70 font-bold">Klassen:  {{ [...selected.klassen].map(k => " " + stundenplanManager().klasseGetByIdOrException(k).kuerzel).toString() }} </div>
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-text-input :model-value="DateUtils.getStringOfUhrzeitFromMinuten(selected.beginn ?? 0)" required placeholder="Pausenbeginn" @change="patchBeginn" />
			<svws-ui-text-input :model-value="DateUtils.getStringOfUhrzeitFromMinuten(selected.ende ?? 0)" placeholder="Pausenende" @change="patchEnde" />
			<div class="col-span-full">
				<svws-ui-button type="danger" @click="removePausenzeiten([selected])"> Pause entfernen </svws-ui-button>
			</div>
		</svws-ui-input-wrapper>
		<svws-ui-spacing :size="2" />
		<svws-ui-table :items="stundenplanManager().pausenaufsichtGetMengeByPausenzeitId(selected.id)" :columns="getColumns()">
			<template #cell(id)="{ rowData }">
				{{ stundenplanManager().lehrerGetByIdOrException(rowData.idLehrer).kuerzel }}
			</template>
			<template #cell(bereiche)="{ value }">
				<template v-for="bereich in value">{{ stundenplanManager().aufsichtsbereichGetByIdOrException(bereich.idAufsichtsbereich).kuerzel }} &nbsp;</template>
			</template>
			<template #cell(wochentyp)="{ value }" v-if="hatWochentypen">
				<span v-if="value === 0" class="opacity-25">—</span>
				<template v-else>{{ stundenplanManager().stundenplanGetWochenTypAsStringKurz(value) }}</template>
			</template>
		</svws-ui-table>
	</svws-ui-content-card>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { LehrerListeEintrag, List, StundenplanManager, StundenplanPausenzeit } from "@core";
	import { DateUtils, Wochentag } from "@core";

	const props = defineProps<{
		selected: StundenplanPausenzeit;
		stundenplanManager: () => StundenplanManager;
		patchPausenzeit: (data: Partial<StundenplanPausenzeit>, id: number) => Promise<void>;
		removePausenzeiten: (multi: Iterable<StundenplanPausenzeit>) => Promise<void>;
		listLehrer: List<LehrerListeEintrag>;
	}>();

	const hatWochentypen = computed<boolean>(() => (props.stundenplanManager().getWochenTypModell() > 0));

	function getColumns() {
		const cols = [
			{ key: "id", label: "Aufsicht" },
			{ key: 'bereiche', label: "Bereich", span: 1 }
		];
		if (hatWochentypen.value)
			cols.push({ key: 'wochentyp', label: "Wochentyp", span: 0.5 });
		return cols;
	}

	async function patchBeginn(event: string | null) {
		if (event === null)
			return;
		const beginn = DateUtils.gibMinutenOfZeitAsString(event);
		await props.patchPausenzeit({ beginn }, props.selected.id);
	}

	async function patchEnde(event: string | null) {
		if (event === null)
			return;
		const ende = DateUtils.gibMinutenOfZeitAsString(event);
		await props.patchPausenzeit({ ende }, props.selected.id);
	}

</script>
