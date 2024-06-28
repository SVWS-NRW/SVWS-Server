<template>
	<div class="page--content page--content--full gap-2">
		<svws-ui-content-card title="Übersicht aller Unterrichte im Zeitraster" class="page--content-flex-column">
			<svws-ui-table :items="[]" :no-data="false" has-background :filterReset :filter-open="true">
				<template #filterAdvanced>
					<svws-ui-multi-select v-model="filterSchueler" title="Schüler" :items="stundenplanUnterrichtListeManager().schueler.list()" :item-text="schueler => `${schueler.nachname}, ${schueler.vorname}`" :item-filter="findSchueler" />
					<svws-ui-multi-select v-model="filterLehrer" title="Lehrer" :items="stundenplanUnterrichtListeManager().lehrer.list()" :item-text="lehrer => `${lehrer.nachname}, ${lehrer.vorname}`" :item-filter="findLehrer" />
					<svws-ui-multi-select v-model="filterKlassen" title="Klasse" :items="stundenplanUnterrichtListeManager().klassen.list()" :item-text="klasse => klasse.kuerzel" :item-filter="find" />
					<svws-ui-multi-select v-model="filterKurse" title="Kurs" :items="stundenplanUnterrichtListeManager().kurse.list()" :item-text="kurs => kurs.bezeichnung" :item-filter="findKurs" />
					<svws-ui-multi-select v-model="filterWochentypen" title="Wochentyp" :items="stundenplanUnterrichtListeManager().wochentypen.list()" :item-text="stundenplanManager().stundenplanGetWochenTypAsStringKurz" />
					<svws-ui-multi-select v-model="filterFaecher" title="Fach" :items="stundenplanUnterrichtListeManager().faecher.list()" :item-text="fach => fach.bezeichnung" />
					<svws-ui-multi-select v-model="filterSchienen" title="Schiene" :items="stundenplanUnterrichtListeManager().schienen.list()" :item-text="schiene => stundenplanManager().schieneGetByIdOrException(schiene.id).nummer.toString()" />
				</template>
				<template #header>
					<div role="row" class="svws-ui-tr select-none">
						<div class="svws-ui-td svws-divider" role="columnheader" />
						<div v-for="wochentag in stundenplanManager().zeitrasterGetWochentageAlsEnumRange()" :key="wochentag.id" class="svws-ui-td cursor-pointer svws-divider svws-align-center" role="columnheader"
							@click="filterWochentag(wochentag)"
							:class="{ 'svws-selected bg-success/20': (stundenplanUnterrichtListeManager().wochentage.auswahlHas(wochentag)) }">
							{{ wochentag.kuerzel }}
						</div>
					</div>
				</template>
				<template #body>
					<div v-for="stunde in stundenplanManager().zeitrasterGetStundenRange()" :key="stunde" role="row" class="svws-ui-tr select-none cursor-pointer">
						<div class="svws-ui-td select-none svws-align-center svws-divider svws-selectable" role="cell"
							@click="filterStunde(stunde)"
							:class="{ 'svws-selected bg-success/20': stundenplanUnterrichtListeManager().stunden.auswahlHas(stunde) }">
							{{ stunde }}
						</div>
						<template v-for="wochentag in stundenplanManager().zeitrasterGetWochentageAlsEnumRange()" :key="wochentag.id">
							<div class="svws-ui-td select-none svws-align-center svws-selectable svws-divider" role="cell"
								@click="filterZeiraster(stundenplanManager().zeitrasterGetByWochentagAndStundeOrException(wochentag.id, stunde))"
								:class="{ 'svws-selected bg-success/20': stundenplanUnterrichtListeManager().zeitraster.auswahlHas(stundenplanManager().zeitrasterGetByWochentagAndStundeOrException(wochentag.id, stunde)) }">
								<template v-for="wt in wochentyprange" :key="wt">
									{{ stundenplanManager().unterrichtGetMengeByWochentagAndStundeAndWochentypOrEmptyList(wochentag, stunde, wt).size() || '' }}{{ stundenplanManager().unterrichtGetMengeByWochentagAndStundeAndWochentypOrEmptyList(wochentag, stunde, wt+1).size() && wt !== wochentyprange.at(-1) ? ", ": "" }}
								</template>
							</div>
						</template>
					</div>
				</template>
			</svws-ui-table>
		</svws-ui-content-card>
		<svws-ui-content-card title="Unterrichtsliste" class="page--content-flex-column">
			<svws-ui-table :items="stundenplanUnterrichtListeManager().filtered()" :columns :no-data="false" has-background>
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
					<svws-ui-multi-select :model-value="modelValueLehrer(value)" title="Fachlehrer" :items="stundenplanManager().lehrerGetMengeAsList()" :item-text="i => i.kuerzel" :item-filter="findLehrer" headless />
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

	import { computed } from "vue";
	import type { StundenplanUnterrichteProps } from "./SStundenplanUnterrichteProps";
	import type { List, StundenplanKlasse, StundenplanKurs, StundenplanRaum, StundenplanSchiene, StundenplanSchueler, StundenplanZeitraster, Wochentag, StundenplanLehrer, StundenplanFach } from "@core";
	import { ZulaessigesFach } from "@core";

	type Selected = { wochentag?: Wochentag, stunde?: number, wochentyp?: number, zeitraster?: StundenplanZeitraster };

	const props = defineProps<StundenplanUnterrichteProps>();

	const wochentage = [null, 'Mo', 'Di', 'Mi', 'Do', 'Fr', 'Sa', 'So'];

	const columns = [{key: 'idZeitraster', label: 'Stunde'}, {key: 'wochentyp', label: 'Wochentyp'}, {key: 'idKurs', label: 'Kurs oder Fach'}, {key: 'lehrer', label: 'Lehrer'}, {key: 'klassen', label: 'Klassen'}, {key: 'raeume', label: 'Räume'}, {key: 'schienen', label: 'Schienen'}];

	function getBgColor(kuerzel: string | null) {
		return ZulaessigesFach.getByKuerzelASD(kuerzel).getHMTLFarbeRGBA(1.0);
	}

	const filterSchueler = computed<StundenplanSchueler[]>({
		get: () => [...props.stundenplanUnterrichtListeManager().schueler.auswahl()],
		set: (value) => {
			props.stundenplanUnterrichtListeManager().schueler.auswahlClear();
			for (const v of value)
				props.stundenplanUnterrichtListeManager().schueler.auswahlAdd(v);
			void props.setFilter();
		}
	});

	const filterLehrer = computed<StundenplanLehrer[]>({
		get: () => [...props.stundenplanUnterrichtListeManager().lehrer.auswahl()],
		set: (value) => {
			props.stundenplanUnterrichtListeManager().lehrer.auswahlClear();
			for (const v of value)
				props.stundenplanUnterrichtListeManager().lehrer.auswahlAdd(v);
			void props.setFilter();
		}
	});

	const filterKlassen = computed<StundenplanKlasse[]>({
		get: () => [...props.stundenplanUnterrichtListeManager().klassen.auswahl()],
		set: (value) => {
			props.stundenplanUnterrichtListeManager().klassen.auswahlClear();
			for (const v of value)
				props.stundenplanUnterrichtListeManager().klassen.auswahlAdd(v);
			void props.setFilter();
		}
	});

	const filterKurse = computed<StundenplanKurs[]>({
		get: () => [...props.stundenplanUnterrichtListeManager().kurse.auswahl()],
		set: (value) => {
			props.stundenplanUnterrichtListeManager().kurse.auswahlClear();
			for (const v of value)
				props.stundenplanUnterrichtListeManager().kurse.auswahlAdd(v);
			void props.setFilter();
		}
	});

	const filterWochentypen = computed<number[]>({
		get: () => [...props.stundenplanUnterrichtListeManager().wochentypen.auswahl()],
		set: (value) => {
			props.stundenplanUnterrichtListeManager().wochentypen.auswahlClear();
			for (const v of value)
				props.stundenplanUnterrichtListeManager().wochentypen.auswahlAdd(v);
			void props.setFilter();
		}
	});

	const filterFaecher = computed<StundenplanFach[]>({
		get: () => [...props.stundenplanUnterrichtListeManager().faecher.auswahl()],
		set: (value) => {
			props.stundenplanUnterrichtListeManager().faecher.auswahlClear();
			for (const v of value)
				props.stundenplanUnterrichtListeManager().faecher.auswahlAdd(v);
			void props.setFilter();
		}
	});

	const filterSchienen = computed<StundenplanSchiene[]>({
		get: () => [...props.stundenplanUnterrichtListeManager().schienen.auswahl()],
		set: (value) => {
			props.stundenplanUnterrichtListeManager().schienen.auswahlClear();
			for (const v of value)
				props.stundenplanUnterrichtListeManager().schienen.auswahlAdd(v);
			void props.setFilter();
		}
	});

	function filterWochentag(wochentag: Wochentag) {
		props.stundenplanUnterrichtListeManager().wochentage.auswahlToggle(wochentag);
		void props.setFilter();
	}

	function filterStunde(stunde: number) {
		props.stundenplanUnterrichtListeManager().stunden.auswahlToggle(stunde);
		void props.setFilter();
	}

	function filterZeiraster(zeitraster: StundenplanZeitraster) {
		props.stundenplanUnterrichtListeManager().zeitraster.auswahlToggle(zeitraster);
		void props.setFilter();
	}

	async function filterReset() {
		props.stundenplanUnterrichtListeManager().klassen.auswahlClear();
		props.stundenplanUnterrichtListeManager().kurse.auswahlClear();
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
		for (const s of list)
			res.push(props.stundenplanManager().schieneGetByIdOrException(s));
		return res;
	}

	function find(items: Iterable<StundenplanKlasse | StundenplanRaum>, search: string) {
		const list = [];
		for (const i of items)
			if (i.kuerzel?.toLocaleLowerCase().includes(search.toLocaleLowerCase()))
				list.push(i);
		return list;
	}

	function findLehrer(items: Iterable<StundenplanLehrer>, search: string) {
		const list = [];
		const lcSearch = search.toLocaleLowerCase();
		for (const i of items)
			if (i.kuerzel?.toLocaleLowerCase().includes(lcSearch)	|| i.nachname.toLocaleLowerCase().includes(lcSearch) || i.vorname.toLocaleLowerCase().includes(lcSearch))
				list.push(i);
		return list;
	}

	function findSchueler(items: Iterable<StundenplanSchueler>, search: string) {
		const list = [];
		const lcSearch = search.toLocaleLowerCase();
		for (const i of items)
			if (i.nachname.toLocaleLowerCase().includes(lcSearch) || i.vorname.toLocaleLowerCase().includes(lcSearch))
				list.push(i);
		return list;
	}

	function findKurs(items: Iterable<StundenplanKurs>, search: string) {
		const list = [];
		for (const i of items)
			if (i.bezeichnung.toLocaleLowerCase().includes(search.toLocaleLowerCase()))
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

</script>

<style lang="postcss" scoped>

	.page--content {
		@apply overflow-y-hidden overflow-x-auto h-full pb-3 pt-6 flex flex-row
	}

	.page--content-flex-column {
		@apply h-full overflow-y-auto w-full flex flex-col gap-8
	}

</style>
