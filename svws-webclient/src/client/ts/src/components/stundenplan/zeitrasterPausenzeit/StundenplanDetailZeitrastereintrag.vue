<template>
	<svws-ui-content-card :title="`${Wochentag.fromIDorException(item.wochentag)} ${item.unterrichtstunde}. Stunde`">
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-text-input :model-value="DateUtils.getStringOfUhrzeitFromMinuten(item.stundenbeginn ?? 0)" required placeholder="Stundenbeginn" @change="patchBeginn" />
			<svws-ui-text-input :model-value="DateUtils.getStringOfUhrzeitFromMinuten(item.stundenende ?? 0)" placeholder="Stundenende" @change="patchEnde" />
			<div class="col-span-full">
				<svws-ui-button type="danger" @click="removeZeitraster([item])"><i-ri-delete-bin-line /> Eintrag entfernen </svws-ui-button>
			</div>
		</svws-ui-input-wrapper>
		<svws-ui-spacing :size="2" />
		<svws-ui-data-table :items="[]" :columns="cols" :no-data="false">
			<template #body>
				<svws-ui-table-row v-for="rowData in stundenplanManager().unterrichtGetMengeByZeitrasterIdAndWochentypOrEmptyList(item.id, 0)" :key="rowData.id" :style="`background-color: ${getBgColor(stundenplanManager().fachGetByIdOrException(rowData.idFach).kuerzelStatistik)}`">
					<svws-ui-table-cell> {{ stundenplanManager().unterrichtGetByIDStringOfFachOderKursKuerzel(rowData.id) }} </svws-ui-table-cell>
				</svws-ui-table-row>
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
