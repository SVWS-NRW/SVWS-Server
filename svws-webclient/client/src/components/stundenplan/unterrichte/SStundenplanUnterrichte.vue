<template>
	<div class="page--content page--content--full gap-2">
		<svws-ui-content-card title="Übersicht aller Unterrichte im Zeitraster" class="page--content-flex-column">
			<svws-ui-table :items="[]" :no-data="false" has-background>
				<!-- <template #filterAdvanced>
					<svws-ui-multi-select v-model="filterKlassen" title="Klasse" :items="schuelerListeManager().klassen.list()" :item-text="klasse => klasse.kuerzel ?? ''" :item-filter="find" />
					<svws-ui-multi-select v-model="filterJahrgaenge" title="Jahrgang" :items="schuelerListeManager().jahrgaenge.list()" :item-text="jahrgang => jahrgang.kuerzel ?? ''" :item-filter="find" />
					<svws-ui-multi-select v-model="filterKurse" title="Kurs" :items="schuelerListeManager().kurse.list()" :item-text="textKurs" :item-filter="findKurs" />
				</template> -->
				<template #header>
					<div role="row" class="svws-ui-tr select-none">
						<div class="svws-ui-td svws-divider svws-align-center col-span-1" role="columnheader" />
						<div v-for="wochentag in stundenplanManager().zeitrasterGetWochentageAlsEnumRange()" :key="wochentag.id" class="svws-ui-td cursor-pointer svws-divider svws-align-center" role="columnheader"
							@click="selected = (selected?.wochentag?.id === wochentag.id) ? { wochentag: undefined } : { wochentag }"
							:class="{ 'svws-selected bg-success/20': (selected?.wochentag?.id === wochentag.id), [`col-span-${stundenplanManager().getWochenTypModell()+1}`]: true }">
							{{ wochentag.kuerzel }}
						</div>
					</div>
					<div role="row" class="svws-ui-tr select-none">
						<div class="svws-ui-td svws-divider svws-align-center" role="columnheader" />
						<template v-for="wochentag in stundenplanManager().zeitrasterGetWochentageAlsEnumRange()" :key="wochentag.id">
							<div class="svws-ui-td cursor-pointer svws-align-center svws-divider" role="columnheader"
								@click="selected = (selected?.wochentag?.id === wochentag.id) && (selected?.wochentyp === 0) ? { wochentag, wochentyp: undefined } : { wochentag, wochentyp: 0 }"
								:class="{ 'svws-selected bg-success/20': ((selected?.wochentag?.id === wochentag.id) && (selected.wochentyp === 0 || selected.wochentyp === undefined)) || ((selected?.wochentag?.id === wochentag.id) && (selected.wochentyp === 0)), }">
								{{ stundenplanManager().stundenplanGetWochenTypAsStringKurz(0) }}
							</div>
							<div v-for="wochentyp in stundenplanManager().getWochenTypModell()" :key="wochentyp" class="svws-ui-td cursor-pointer svws-align-center svws-divider" role="columnheader"
								@click="selected = (selected?.wochentag?.id === wochentag.id) && (selected?.wochentyp === wochentyp) ? { wochentag, wochentyp: undefined } : { wochentag, wochentyp }"
								:class="{ 'svws-selected bg-success/20': ((selected?.wochentag?.id === wochentag.id) && (selected.wochentyp === wochentyp || selected.wochentyp === undefined)) || ((selected?.wochentag?.id === wochentag.id) && (selected.wochentyp === wochentyp)), }">
								{{ stundenplanManager().stundenplanGetWochenTypAsStringKurz(wochentyp) }}
							</div>
						</template>
					</div>
				</template>
				<template #body>
					<div v-for="stunde in stundenplanManager().zeitrasterGetStundenRange()" :key="stunde" role="row" class="svws-ui-tr select-none cursor-pointer">
						<div class="svws-ui-td select-none svws-align-center svws-divider svws-selectable" role="cell"
							@click="selected = (selected?.stunde === stunde) ? { stunde: undefined }:{ stunde }"
							:class="{ 'svws-selected bg-success/20': (selected?.stunde === stunde) }">
							{{ stunde }}
						</div>
						<template v-for="wochentag in stundenplanManager().zeitrasterGetWochentageAlsEnumRange()" :key="wochentag.id">
							<div v-for="wochentyp, i in stundenplanManager().getWochenTypModell()+1" :key="wochentyp" class="svws-ui-td select-none svws-align-center svws-selectable svws-divider" role="cell"
								@click="selected = (selected?.zeitraster?.id === stundenplanManager().zeitrasterGetByWochentagAndStundeOrException(wochentag.id, stunde).id && selected.wochentyp === wochentyp) ? { zeitraster: undefined, wochentyp: undefined } : { zeitraster: stundenplanManager().zeitrasterGetByWochentagAndStundeOrException(wochentag.id, stunde), wochentyp}"
								:class="{ 'svws-selected bg-success/20': (selected?.stunde === stunde) || (selected?.wochentag?.id === wochentag.id && (selected.wochentyp === wochentyp-1 || selected.wochentyp === undefined)) || (selected?.zeitraster?.id === stundenplanManager().zeitrasterGetByWochentagAndStundeOrException(wochentag.id, stunde).id && selected.wochentyp === wochentyp), }">
								{{ stundenplanManager().unterrichtGetMengeByWochentagAndStundeAndWochentypOrEmptyList(wochentag, stunde, i).size() || '' }}
							</div>
						</template>
					</div>
				</template>
			</svws-ui-table>
		</svws-ui-content-card>
		<svws-ui-content-card title="Unterrichtsliste" class="page--content-flex-column">
			<svws-ui-table :items :columns :no-data="false" has-background>
				<template #cell(idZeitraster)="{ value }">
					{{ wochentage[stundenplanManager().zeitrasterGetByIdOrException(value).wochentag] }} {{ stundenplanManager().zeitrasterGetByIdOrException(value).unterrichtstunde }}.
				</template>
				<template #cell(wochentyp)="{ value }">
					{{ stundenplanManager().stundenplanGetWochenTypAsStringKurz(value) }}
				</template>
				<template #cell(idKurs)="{ rowData }">
					<div class="svws-ui-badge" :style="{'--background-color': getBgColor(stundenplanManager().fachGetByIdOrException(rowData.idFach).kuerzelStatistik)}">{{ rowData.idKurs ? stundenplanManager().kursGetByIdOrException(rowData.idKurs).bezeichnung : stundenplanManager().fachGetByIdOrException(rowData.idFach).bezeichnung }}</div>
				</template>
				<template #cell(lehrer)="{ value }">
					<svws-ui-multi-select :model-value="modelValueLehrer(value)" title="Fachlehrer" :items="stundenplanManager().lehrerGetMengeAsList()" :item-text="i => i.kuerzel" :item-filter="find" headless />
				</template>
				<template #cell(klassen)="{ value }">
					<svws-ui-multi-select :model-value="modelValueKlassen(value)" title="Klassen" :items="stundenplanManager().klasseGetMengeAsList()" :item-text="i => i.kuerzel" :item-filter="find" headless />
				</template>
				<template #cell(raeume)="{ value }">
					<svws-ui-multi-select :model-value="modelValueRaeume(value)" title="Räume" :items="stundenplanManager().raumGetMengeAsList()" :item-text="i => i.kuerzel" :item-filter="find" headless />
				</template>
				<template #cell(schienen)="{ value }">
					<svws-ui-multi-select :model-value="modelValueSchienen(value)" title="Schienen" :items="stundenplanManager().schieneGetMengeAsList()" :item-text="i => i.nummer.toString()" headless />
				</template>
			</svws-ui-table>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { ref, computed } from "vue";
	import type { StundenplanUnterrichteProps } from "./SStundenplanUnterrichteProps";
	import type { List, StundenplanKlasse, StundenplanRaum, StundenplanSchiene, StundenplanUnterricht, StundenplanZeitraster, Wochentag} from "@core";
	import { ArrayList, ZulaessigesFach, StundenplanLehrer } from "@core";

	type Selected = { wochentag?: Wochentag, stunde?: number, wochentyp?: number, zeitraster?: StundenplanZeitraster };

	const props = defineProps<StundenplanUnterrichteProps>();

	const selected = ref<Selected>();
	const wochentage = [null, 'Mo', 'Di', 'Mi', 'Do', 'Fr', 'Sa', 'So'];

	const columns = [{key: 'idZeitraster', label: 'Stunde'}, {key: 'wochentyp', label: 'Wochentyp'}, {key: 'idKurs', label: 'Kurs oder Fach'}, {key: 'lehrer', label: 'Lehrer'}, {key: 'klassen', label: 'Klassen'}, {key: 'raeume', label: 'Räume'}, {key: 'schienen', label: 'Schienen'}];

	function getBgColor(kuerzel: string | null) {
		return ZulaessigesFach.getByKuerzelASD(kuerzel).getHMTLFarbeRGBA(1.0);
	}

	function modelValueLehrer(list: List<number>) {
		const res: StundenplanLehrer[] = [];
		for (const l of list)
			res.push(props.stundenplanManager().lehrerGetByIdOrException(l));
		return res;
	}

	function modelValueKlassen(list: List<number>) {
		const res: StundenplanKlasse[] = [];
		for (const k of list)
			res.push(props.stundenplanManager().klasseGetByIdOrException(k));
		return res;
	}

	function modelValueRaeume(list: List<number>) {
		const res: StundenplanRaum[] = [];
		for (const r of list)
			res.push(props.stundenplanManager().raumGetByIdOrException(r));
		return res;
	}

	function modelValueSchienen(list: List<number>) {
		const res: StundenplanSchiene[] = [];
		// for (const s of list)
		// 	res.push(props.stundenplanManager().schieneGetByIdOrException(s));
		return res;
	}

	function find(items: Iterable<StundenplanLehrer | StundenplanKlasse | StundenplanRaum>, search: string) {
		const list = [];
		for (const i of items)
			if ((i.kuerzel?.toLocaleLowerCase().includes(search.toLocaleLowerCase()))
				|| ((i instanceof StundenplanLehrer) && (i.nachname.toLocaleLowerCase().includes(search.toLocaleLowerCase()) || (i.vorname.toLocaleLowerCase().includes(search.toLocaleLowerCase())))))
				list.push(i);
		return list;
	}

	const wochentyprange = computed(() => {
		const range = [];
		const modell = props.stundenplanManager().stundenplanGetWochenTypModell();
		for (let i = 0; i <= modell; i++)
			range.push(i);
		return range;
	})

	const items = computed(() => {
		if (selected.value === undefined)
			return props.stundenplanManager().unterrichtGetMengeAsList();
		if ((selected.value?.zeitraster !== undefined) && (selected.value.wochentyp !== undefined))
			return props.stundenplanManager().unterrichtGetMengeByZeitrasterIdAndWochentypOrEmptyList(selected.value.zeitraster.id, selected.value.wochentyp-1);
		const list: List<StundenplanUnterricht> = new ArrayList();
		if (selected.value?.wochentag !== undefined)
			for (const stunde of props.stundenplanManager().zeitrasterGetStundenRange())
				if (selected.value.wochentyp === undefined)
					for (const wochentyp of wochentyprange.value)
						list.addAll(props.stundenplanManager().unterrichtGetMengeByWochentagAndStundeAndWochentypOrEmptyList(selected.value.wochentag, stunde, wochentyp));
				else
					list.addAll(props.stundenplanManager().unterrichtGetMengeByWochentagAndStundeAndWochentypOrEmptyList(selected.value.wochentag, stunde, selected.value.wochentyp));
		else if (selected.value?.stunde !== undefined)
			for (const wochentag of props.stundenplanManager().zeitrasterGetWochentageAlsEnumRange())
				for (const wochentyp of wochentyprange.value)
					list.addAll(props.stundenplanManager().unterrichtGetMengeByWochentagAndStundeAndWochentypOrEmptyList(wochentag, selected.value.stunde, wochentyp));
		return list;
	})

</script>

<style lang="postcss" scoped>

	.page--content {
		@apply overflow-y-hidden overflow-x-auto h-full pb-3 pt-6 flex flex-row
	}

	.page--content-flex-column {
		@apply h-full overflow-y-auto w-full flex flex-col gap-8
	}

</style>
