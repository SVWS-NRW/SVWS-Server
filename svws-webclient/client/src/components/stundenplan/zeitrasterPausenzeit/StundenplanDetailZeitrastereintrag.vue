<template>
	<svws-ui-content-card :title="`${Wochentag.fromIDorException(selected.wochentag)} ${selected.unterrichtstunde}. Stunde`">
		<svws-ui-input-wrapper :grid="2">
			<svws-ui-text-input :model-value="DateUtils.getStringOfUhrzeitFromMinuten(selected.stundenbeginn ?? 0)" required placeholder="Stundenbeginn" @change="patchBeginn" ref="inputBeginn" />
			<svws-ui-text-input :model-value="DateUtils.getStringOfUhrzeitFromMinuten(selected.stundenende ?? 0)" placeholder="Stundenende" @change="patchEnde" ref="inputEnde" />
			<div class="col-span-full">
				<svws-ui-button type="danger" @click="removeZeitraster([selected])"><span class="icon i-ri-delete-bin-line" /> Eintrag entfernen </svws-ui-button>
			</div>
		</svws-ui-input-wrapper>
		<svws-ui-spacing :size="2" />
		<svws-ui-table :items="[]" :columns :no-data="false">
			<template #body>
				<div v-for="rowData in stundenplanManager().unterrichtGetMengeByZeitrasterIdAndWochentypOrEmptyList(selected.id, 0)" :key="rowData.id" class="svws-ui-tr" role="row" :style="`--background-color: ${getBgColor(stundenplanManager().fachGetByIdOrException(rowData.idFach).kuerzelStatistik)}`">
					<div class="svws-ui-td" role="cell">
						<span class="svws-ui-badge">{{ stundenplanManager().unterrichtGetByIDStringOfFachOderKursKuerzel(rowData.id) }}</span>
					</div>
					<div class="svws-ui-td" role="cell">
						<span v-for="jahrgang in stundenplanManager().jahrgangGetMengeByUnterrichtIdAsList(rowData.id)" :key="jahrgang.id" class="line-clamp-1 break-all leading-tight -my-0.5">{{ jahrgang.kuerzel }}</span>
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
	import type { ComponentExposed } from "vue-component-type-helpers";
	import type { StundenplanManager } from "@core";
	import { DateUtils, Wochentag, Fach , StundenplanZeitraster, ListUtils } from "@core";
	import { SvwsUiTextInput } from "@ui";

	const props = defineProps<{
		selected: StundenplanZeitraster;
		stundenplanManager: () => StundenplanManager;
		patchZeitraster: (zeitraster: Iterable<StundenplanZeitraster>) => Promise<void>;
		removeZeitraster: (multi: Iterable<StundenplanZeitraster>) => Promise<void>;
	}>();

	const schuljahr = computed<number>(() => props.stundenplanManager().getSchuljahr());

	const inputBeginn = ref<ComponentExposed<typeof SvwsUiTextInput>>();
	const inputEnde = ref<ComponentExposed<typeof SvwsUiTextInput>>();

	const ueberschneidung = ref<boolean>(false);

	const columns = [
		{ key: 'idFach', label: 'Unterricht' },
		{ key: 'jahrgang', label: 'Jahrgang' },
		{ key: 'klassen', label: 'Klassen' },
		{ key: 'raeume', label: 'Räume'}
	];

	function getBgColor(fach: string): string {
		return Fach.getBySchluesselOrDefault(fach).getHMTLFarbeRGB(schuljahr.value);
	}

	async function patchBeginn(start: string | null) {
		if ((start === null) || (inputBeginn.value?.input === null) || (inputBeginn.value?.input === undefined) || (inputEnde.value?.input === null) || (inputEnde.value?.input === undefined))
			return;
		ueberschneidung.value = false;
		const dauer = (props.selected.stundenende ?? 0) - (props.selected.stundenbeginn ?? 0);
		const stundenbeginn = DateUtils.gibMinutenOfZeitAsString(start);
		const zeitraster = new StundenplanZeitraster();
		Object.assign(zeitraster, props.selected);
		zeitraster.stundenbeginn = stundenbeginn;
		zeitraster.stundenende = stundenbeginn + dauer;
		const list = ListUtils.create1(zeitraster);
		const ignoreList = ListUtils.create1(props.selected);
		if (!props.stundenplanManager().zeitrasterGetSchneidenSichListeMitIgnore(list, ignoreList))
			await props.patchZeitraster(list);
		else
			ueberschneidung.value = true;
		inputBeginn.value.input.value = props.stundenplanManager().zeitrasterGetByIdStringOfUhrzeitBeginn(props.selected.id);
		inputEnde.value.input.value = props.stundenplanManager().zeitrasterGetByIdStringOfUhrzeitEnde(props.selected.id);
	}

	async function patchEnde(ende: string | null) {
		if ((ende === null) || (inputEnde.value?.input === undefined) || inputEnde.value.input === null)
			return;
		ueberschneidung.value = false;
		const stundenende = DateUtils.gibMinutenOfZeitAsString(ende);
		const zeitraster = new StundenplanZeitraster();
		Object.assign(zeitraster, props.selected);
		zeitraster.stundenende = stundenende;
		const list = ListUtils.create1(zeitraster);
		const ignoreList = ListUtils.create1(props.selected);
		if (!props.stundenplanManager().zeitrasterGetSchneidenSichListeMitIgnore(list, ignoreList))
			await props.patchZeitraster(list);
		else
			ueberschneidung.value = true;
		inputEnde.value.input.value = props.stundenplanManager().zeitrasterGetByIdStringOfUhrzeitEnde(props.selected.id);
	}

</script>
