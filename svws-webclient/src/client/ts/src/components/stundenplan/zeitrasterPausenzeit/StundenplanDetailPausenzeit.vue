<template>
	<svws-ui-content-card :title="`Pause am ${Wochentag.fromIDorException(item.wochentag)}`">
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-text-input :model-value="DateUtils.getStringOfUhrzeitFromMinuten(item.beginn ?? 0)" required placeholder="Pausenbeginn" @update:model-value="patchBeginn" />
			<svws-ui-text-input :model-value="DateUtils.getStringOfUhrzeitFromMinuten(item.ende ?? 0)" placeholder="Pausenende" @update:model-value="patchEnde" />
			<div class="col-span-full">
				<svws-ui-button type="danger" @click="removePausenzeiten([item])"> Pause entfernen </svws-ui-button>
			</div>
		</svws-ui-input-wrapper>
		<svws-ui-spacing :size="2" />
		<svws-ui-data-table :items="stundenplanManager().pausenaufsichtGetMengeByPausenzeitId(item.id)" :columns="cols">
			<template #cell(id)="{ rowData }">
				<div>
					{{ stundenplanManager().lehrerGetByIdOrException(rowData.idLehrer).kuerzel }}
				</div>
			</template>
		</svws-ui-data-table>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import type { StundenplanManager, StundenplanPausenzeit } from "@core";
	import { DateUtils, Wochentag } from "@core";

	const props = defineProps<{
		item: StundenplanPausenzeit;
		stundenplanManager: () => StundenplanManager;
		patchPausenzeit: (data: Partial<StundenplanPausenzeit>, id: number) => Promise<void>;
		removePausenzeiten: (multi: Iterable<StundenplanPausenzeit>) => Promise<void>;
	}>();

	const cols = [
		{ key: "id", label: "Aufsicht", span: 2, sortable: false },
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
