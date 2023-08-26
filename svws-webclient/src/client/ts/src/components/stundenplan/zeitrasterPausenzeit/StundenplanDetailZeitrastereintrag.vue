<template>
	<svws-ui-content-card :title="`${Wochentag.fromIDorException(item.wochentag)} ${item.unterrichtstunde}. Stunde`">
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-text-input :model-value="DateUtils.getStringOfUhrzeitFromMinuten(item.stundenbeginn ?? 0)" required placeholder="Stundenbeginn" @update:model-value="patchBeginn" />
			<svws-ui-text-input :model-value="DateUtils.getStringOfUhrzeitFromMinuten(item.stundenende ?? 0)" placeholder="Stundenende" @update:model-value="patchEnde" />
			<div class="col-span-full">
				<svws-ui-button type="danger" @click="removeZeitraster([item])"><i-ri-delete-bin-line /> Eintrag entfernen </svws-ui-button>
			</div>
		</svws-ui-input-wrapper>
		<svws-ui-spacing :size="2" />
		<svws-ui-data-table :items="stundenplanManager().unterrichtGetMengeByZeitrasterIdAndWochentypOrEmptyList(item.id, 0)" :columns="cols" no-data-html="Kein Unterricht zu diesem Eintrag gefunden.">
			<template #cell(idFach)="{ rowData }">
				<div class="rounded -m-1 px-1.5 py-0.5" :style="`background-color: ${getBgColor(stundenplanManager().unterrichtGetByIDStringOfFachOderKursKuerzel(rowData.id).split('-')[0])}`">
					{{ stundenplanManager().unterrichtGetByIDStringOfFachOderKursKuerzel(rowData.id) }}
				</div>
			</template>
		</svws-ui-data-table>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import type { StundenplanManager, StundenplanZeitraster} from "@core";
	import { DateUtils, Wochentag, ZulaessigesFach } from "@core";

	const props = defineProps<{
		item: StundenplanZeitraster;
		stundenplanManager: () => StundenplanManager;
		patchZeitraster: (data: Partial<StundenplanZeitraster>, zeitraster: StundenplanZeitraster) => Promise<void>;
		removeZeitraster: (multi: Iterable<StundenplanZeitraster>) => Promise<void>;
	}>();

	function getBgColor(fach: string): string {
		return ZulaessigesFach.getByKuerzelASD(fach).getHMTLFarbeRGB();
	}

	const cols = [
		{ key: "idFach", label: "Unterricht", sortable: false },
	];

	async function patchBeginn(event: string | number) {
		if (typeof event === 'number')
			return;
		const stundenbeginn = DateUtils.gibMinutenOfZeitAsString(event);
		await props.patchZeitraster({stundenbeginn}, props.item);
	}

	async function patchEnde(event: string | number) {
		if (typeof event === 'number')
			return;
		const stundenende = DateUtils.gibMinutenOfZeitAsString(event);
		await props.patchZeitraster({stundenende}, props.item);
	}


</script>
