<template>
	<div class="page--content page--content--full gap-2">
		<svws-ui-content-card title="Übersicht aller Unterrichte im Zeitraster" class="page--content-flex-column">
			<svws-ui-table :items="[]" :no-data="false" has-background>
				<template #header>
					<div role="row" class="svws-ui-tr select-none">
						<div class="svws-ui-td svws-divider svws-align-center" role="columnheader" />
						<div v-for="wochentag in stundenplanManager().zeitrasterGetWochentageAlsEnumRange()" :key="wochentag.id" class="svws-ui-td cursor-pointer svws-divider svws-align-center" role="columnheader" @click="selected = wochentag" :class="{ 'svws-selected bg-red-200': toRaw(selected) === wochentag }">
							{{ wochentag.kuerzel }}
						</div>
					</div>
					<div role="row" class="svws-ui-tr select-none">
						<div class="svws-ui-td svws-divider svws-align-center" :class="[`col-span-${stundenplanManager().getWochenTypModell()+1}`]" role="columnheader" />
						<template v-for="wochentag in stundenplanManager().zeitrasterGetWochentageAlsEnumRange()" :key="wochentag.id">
							<div class="svws-ui-td cursor-pointer svws-align-center svws-divider" role="columnheader" @click="selected = 0" :class="{ 'svws-selected': toRaw(selected) === 0, }">
								{{ stundenplanManager().stundenplanGetWochenTypAsStringKurz(0) }}
							</div>
							<div v-for="wochentyp in stundenplanManager().getWochenTypModell()" :key="wochentyp" class="svws-ui-td cursor-pointer svws-align-center svws-divider" role="columnheader">
								{{ stundenplanManager().stundenplanGetWochenTypAsStringKurz(wochentyp) }}
							</div>
						</template>
					</div>
				</template>
				<template #body>
					<div v-for="stunde in stundenplanManager().zeitrasterGetStundenRange()" :key="stunde" role="row" class="svws-ui-tr select-none cursor-pointer">
						<div class="svws-ui-td select-none svws-align-center svws-divider svws-selectable" role="cell" @click="selected = stunde" :class="{ 'svws-selected bg-red-200': toRaw(selected) === stunde, }">
							{{ stunde }}
						</div>
						<div v-for="wochentag in stundenplanManager().zeitrasterGetWochentageAlsEnumRange()" :key="wochentag.id" class="svws-ui-td select-none svws-align-center svws-selectable" role="cell" @click="selected = stunde" :class="{ 'svws-selected': selected === stunde, }">
							<div v-for="wochentyp, i in stundenplanManager().getWochenTypModell()+1" :key="wochentyp" class="cursor-pointer svws-align-center svws-divider">
								{{ stundenplanManager().unterrichtGetMengeByWochentagAndStundeAndWochentypOrEmptyList(wochentag, stunde, i).size() }}
							</div>
						</div>
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
					{{ rowData.idKurs ? stundenplanManager().kursGetByIdOrException(rowData.idKurs).bezeichnung : stundenplanManager().fachGetByIdOrException(rowData.idFach).bezeichnung }}
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

	import { ref, effect, toRaw, computed } from "vue";
	import type { List, StundenplanKlasse, StundenplanLehrer, StundenplanRaum, StundenplanSchiene, StundenplanUnterricht} from "@core";
	import { ArrayList, Wochentag } from "@core";
	import type { StundenplanUnterrichteProps } from "./SStundenplanUnterrichteProps";

	const props = defineProps<StundenplanUnterrichteProps>();

	const selected = ref<number | Wochentag>();
	const wochentage = [null, 'Mo', 'Di', 'Mi', 'Do', 'Fr', 'Sa', 'So'];

	const columns = [{key: 'idZeitraster', label: 'Stunde'}, {key: 'idKurs', label: 'Kurs oder Fach'}, {key: 'lehrer', label: 'Lehrer'}, {key: 'klassen', label: 'Klassen'}, {key: 'raeume', label: 'Räume'}, {key: 'schienen', label: 'Schienen'}];

	effect(() => console.log(selected.value))

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

	const find = (items: Iterable<StundenplanLehrer | StundenplanKlasse | StundenplanRaum>, search: string) => {
		const list = [];
		for (const i of items)
			if ((i.kuerzel?.toLocaleLowerCase().includes(search.toLocaleLowerCase())))
				// || (i.nachname.toLocaleLowerCase().includes(search.toLocaleLowerCase())) || (i.vorname.toLocaleLowerCase().includes(search.toLocaleLowerCase())))
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
		const list: List<StundenplanUnterricht> = new ArrayList();
		if (selected.value instanceof Wochentag)
			for (const stunde of props.stundenplanManager().zeitrasterGetStundenRange())
				for (const wochentyp of wochentyprange.value)
					list.addAll(props.stundenplanManager().unterrichtGetMengeByWochentagAndStundeAndWochentypOrEmptyList(selected.value, stunde, wochentyp));
		else if (typeof selected.value === 'number')
			for (const wochentag of props.stundenplanManager().zeitrasterGetWochentageAlsEnumRange())
				for (const wochentyp of wochentyprange.value)
					list.addAll(props.stundenplanManager().unterrichtGetMengeByWochentagAndStundeAndWochentypOrEmptyList(wochentag, selected.value, wochentyp));
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
