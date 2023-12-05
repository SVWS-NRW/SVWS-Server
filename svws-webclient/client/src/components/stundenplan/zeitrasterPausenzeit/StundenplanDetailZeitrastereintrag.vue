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
		<svws-ui-table :items="[]" :columns="[{ key: 'idFach', label: 'Unterricht', span: 0.5 }, { key: 'bezeichnung', label: ' ' }]" :no-data="false">
			<template #body>
				<div class="svws-ui-tr" role="row" v-for="rowData in stundenplanManager().unterrichtGetMengeByZeitrasterIdAndWochentypOrEmptyList(item.id, 0)" :key="rowData.id" :style="`--background-color: ${getBgColor(stundenplanManager().fachGetByIdOrException(rowData.idFach).kuerzelStatistik)}`">
					<div class="svws-ui-td" role="cell">
						<span class="svws-ui-badge">{{ stundenplanManager().unterrichtGetByIDStringOfFachOderKursKuerzel(rowData.id) }}</span>
					</div>
					<div class="svws-ui-td" role="cell">
						<span class="line-clamp-1 break-all leading-tight -my-0.5">{{ stundenplanManager().fachGetByIdOrException(rowData.idFach).bezeichnung }}</span>
					</div>
				</div>
			</template>
		</svws-ui-table>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import type { StundenplanManager } from "@core";
	import { ArrayList, DateUtils, Wochentag, ZulaessigesFach , StundenplanZeitraster } from "@core";

	const props = defineProps<{
		item: StundenplanZeitraster;
		stundenplanManager: () => StundenplanManager;
		patchZeitraster: (zeitraster : Iterable<StundenplanZeitraster>) => Promise<void>;
		removeZeitraster: (multi: Iterable<StundenplanZeitraster>) => Promise<void>;
	}>();

	function getBgColor(fach: string): string {
		return ZulaessigesFach.getByKuerzelASD(fach).getHMTLFarbeRGB();
	}

	async function patchBeginn(start: string | null) {
		if (start === null)
			return;
		const stundenbeginn = DateUtils.gibMinutenOfZeitAsString(start);
		const zeitraster = new StundenplanZeitraster();
		Object.assign(zeitraster, props.item);
		zeitraster.stundenbeginn = stundenbeginn;
		const list = new ArrayList<StundenplanZeitraster>();
		list.add(zeitraster);
		await props.patchZeitraster(list);
	}

	async function patchEnde(ende: string | null) {
		if (ende === null)
			return;
		const stundenende = DateUtils.gibMinutenOfZeitAsString(ende);
		const zeitraster = new StundenplanZeitraster();
		Object.assign(zeitraster, props.item);
		zeitraster.stundenende = stundenende;
		const list = new ArrayList<StundenplanZeitraster>();
		list.add(zeitraster);
		await props.patchZeitraster(list);
	}


</script>
