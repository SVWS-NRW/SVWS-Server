<template>
	<svws-ui-content-card :title="`Pause am ${Wochentag.fromIDorException(item.wochentag)}`">
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-text-input :model-value="DateUtils.getStringOfUhrzeitFromMinuten(item.beginn ?? 0)" required placeholder="Pausenbeginn" @change="patchBeginn" />
			<svws-ui-text-input :model-value="DateUtils.getStringOfUhrzeitFromMinuten(item.ende ?? 0)" placeholder="Pausenende" @change="patchEnde" />
			<div class="col-span-full">
				<svws-ui-button type="danger" @click="removePausenzeiten([item])"> Pause entfernen </svws-ui-button>
			</div>
		</svws-ui-input-wrapper>
		<svws-ui-spacing :size="2" />
		<svws-ui-data-table :items="stundenplanManager().pausenaufsichtGetMengeByPausenzeitId(item.id)" :columns="cols" selectable v-model="selected">
			<template #cell(id)="{ rowData }">
				{{ stundenplanManager().lehrerGetByIdOrException(rowData.idLehrer).kuerzel }}
			</template>
			<template #cell(bereiche)="{ value }">
				<template v-for="bereich in value">{{ stundenplanManager().aufsichtsbereichGetByIdOrException(bereich).kuerzel }} &nbsp;</template>
			</template>
			<template #cell(wochentyp)="{ value }">
				{{ value === 0 ? '': String.fromCharCode(64 + value) }}
			</template>
			<template #footerActions>
				<div v-if="selected.length > 0" class="flex items-center justify-end pr-1 h-full">
					<svws-ui-button @click="remove" type="trash" class="cursor-pointer"
						:disabled="selected.length === 0" />
				</div>
				<stundenplan-detail-pausenzeit-modal v-slot="{ openModal }" :pausenzeit="item" :list-lehrer="listLehrer" :list-aufsichtsbereiche="listAufsichtsbereiche" :add-aufsicht-und-bereich="addAufsichtUndBereich" :wochentypen="stundenplanManager().getWochenTypModell()">
					<svws-ui-button @click="openModal()" type="icon" title="Pausenaufsicht hinzufügen"> <i-ri-add-line /> </svws-ui-button>
				</stundenplan-detail-pausenzeit-modal>
			</template>
		</svws-ui-data-table>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import type { LehrerListeEintrag, List, StundenplanAufsichtsbereich, StundenplanManager, StundenplanPausenaufsicht, StundenplanPausenzeit } from "@core";
	import { DateUtils, Wochentag } from "@core";
	import { ref } from "vue";

	const props = defineProps<{
		item: StundenplanPausenzeit;
		stundenplanManager: () => StundenplanManager;
		patchPausenzeit: (data: Partial<StundenplanPausenzeit>, id: number) => Promise<void>;
		removePausenzeiten: (multi: Iterable<StundenplanPausenzeit>) => Promise<void>;
		listLehrer: List<LehrerListeEintrag>;
		listAufsichtsbereiche: List<StundenplanAufsichtsbereich>;
		addAufsichtUndBereich: (pausenaufsicht: Partial<StundenplanPausenaufsicht>) => Promise<void>;
	}>();

	const selected = ref<StundenplanPausenaufsicht[]>([]);

	const cols = [
		{ key: "id", label: "Aufsicht", sortable: false },
		{ key: 'bereiche', label: "Bereiche", span: 2 },
		{ key: 'wochentyp', label: "Typ", span: 0.5 }
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

	async function remove() {
		// TODO remove
		console.log("entferne Pausenaufsichten: ", selected.value);
	}

</script>
