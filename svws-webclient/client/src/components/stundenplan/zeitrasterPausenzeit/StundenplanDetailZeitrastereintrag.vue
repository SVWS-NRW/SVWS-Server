<template>
	<svws-ui-content-card :title="`${Wochentag.fromIDorException(item.wochentag)} ${item.unterrichtstunde}. Stunde`">
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-text-input :model-value="DateUtils.getStringOfUhrzeitFromMinuten(item.stundenbeginn ?? 0)" required placeholder="Stundenbeginn" @change="patchBeginn" />
			<svws-ui-text-input :model-value="DateUtils.getStringOfUhrzeitFromMinuten(item.stundenende ?? 0)" placeholder="Stundenende" @change="patchEnde" />
			<div class="col-span-full">
				<svws-ui-button type="danger" @click="removeZeitraster([item])"><span class="icon i-ri-delete-bin-line" /> Eintrag entfernen </svws-ui-button>
			</div>
		</svws-ui-input-wrapper>
		<svws-ui-spacing :size="2" />
		<svws-ui-table :items="[]" :columns :no-data="false">
			<template #body>
				<div v-for="rowData in stundenplanManager().unterrichtGetMengeByZeitrasterIdAndWochentypOrEmptyList(item.id, 0)" :key="rowData.id" class="svws-ui-tr" role="row" :style="`--background-color: ${getBgColor(stundenplanManager().fachGetByIdOrException(rowData.idFach).kuerzelStatistik)}`">
					<div class="svws-ui-td" role="cell">
						<span class="svws-ui-badge">{{ stundenplanManager().unterrichtGetByIDStringOfFachOderKursKuerzel(rowData.id) }}</span>
					</div>
					<div class="svws-ui-td" role="cell">
						<span v-for="klasse in rowData.klassen" :key="klasse" class="line-clamp-1 break-all leading-tight -my-0.5">{{ stundenplanManager().klasseGetByIdOrException(klasse).kuerzel }}</span>
					</div>
					<div class="svws-ui-td" role="cell">
						<span v-for="raum in rowData.raeume" :key="raum" class="line-clamp-1 break-all leading-tight -my-0.5">{{ stundenplanManager().raumGetByIdOrException(raum).kuerzel }}</span>
					</div>
				</div>
			</template>
		</svws-ui-table>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import { computed, ref } from "vue";
	import type { StundenplanManager } from "@core";
	import { DateUtils, Wochentag, ZulaessigesFach , StundenplanZeitraster, ListUtils, ArrayList } from "@core";

	const props = defineProps<{
		item: StundenplanZeitraster;
		stundenplanManager: () => StundenplanManager;
		patchZeitraster: (zeitraster: Iterable<StundenplanZeitraster>) => Promise<void>;
		removeZeitraster: (multi: Iterable<StundenplanZeitraster>) => Promise<void>;
	}>();

	const ueberschneidung = ref<boolean>(false);

	const columns = [{ key: 'idFach', label: 'Unterricht' }, { key: 'klassen', label: 'Klassen' }, { key: 'raeume', label: 'Räume'}];

	function getBgColor(fach: string): string {
		return ZulaessigesFach.getByKuerzelASD(fach).getHMTLFarbeRGB();
	}

	async function patchBeginn(start: string | null) {
		if (start === null)
			return;
		ueberschneidung.value = false;
		const stundenbeginn = DateUtils.gibMinutenOfZeitAsString(start);
		const zeitraster = new StundenplanZeitraster();
		Object.assign(zeitraster, props.item);
		zeitraster.stundenbeginn = stundenbeginn;
		const list = ListUtils.create1(zeitraster);
		const ignoreList = ListUtils.create1(props.item);
		if (!props.stundenplanManager().zeitrasterGetSchneidenSichListeMitIgnore(list, ignoreList))
			await props.patchZeitraster(list);
		else
			ueberschneidung.value = true;
	}

	async function patchEnde(ende: string | null) {
		if (ende === null)
			return;
		ueberschneidung.value = false;
		const stundenende = DateUtils.gibMinutenOfZeitAsString(ende);
		const zeitraster = new StundenplanZeitraster();
		Object.assign(zeitraster, props.item);
		zeitraster.stundenende = stundenende;
		const list = ListUtils.create1(zeitraster);
		const ignoreList = ListUtils.create1(props.item);
		if (!props.stundenplanManager().zeitrasterGetSchneidenSichListeMitIgnore(list, ignoreList))
			await props.patchZeitraster(list);
		else
			ueberschneidung.value = true;
	}


</script>
