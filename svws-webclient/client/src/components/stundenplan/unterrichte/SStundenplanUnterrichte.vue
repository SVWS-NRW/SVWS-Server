<template>
	<div class="page--content page--content--full gap-2">
		<svws-ui-content-card title="Übersicht aller Unterrichte im Zeitraster" class="page--content-flex-column">
			<svws-ui-table :items="[]" :no-data="false" has-background :filterReset :filter-open="true" :filtered>
				<template #filterAdvanced>
					<svws-ui-multi-select v-model="filterSchueler" title="Schüler" :items="stundenplanUnterrichtListeManager().schueler.list()" :item-text="schueler => `${schueler.nachname}, ${schueler.vorname}`" :item-filter="findSchueler" />
					<svws-ui-multi-select v-model="filterLehrer" title="Lehrer" :items="stundenplanUnterrichtListeManager().lehrer.list()" :item-text="lehrer => `${lehrer.nachname}, ${lehrer.vorname}`" :item-filter="findLehrer" />
					<svws-ui-multi-select v-model="filterKlassen" title="Klasse" :items="stundenplanUnterrichtListeManager().klassen.list()" :item-text="klasse => klasse.kuerzel" :item-filter="find" />
					<svws-ui-multi-select v-model="filterKurse" title="Kurs" :items="stundenplanUnterrichtListeManager().kurse.list()" :item-text="kurs => kurs.bezeichnung" :item-filter="findKurs" />
					<svws-ui-multi-select v-model="filterWochentypen" title="Wochentyp" :items="stundenplanUnterrichtListeManager().wochentypen.list()" :item-text="stundenplanManager().stundenplanGetWochenTypAsStringKurz" />
					<svws-ui-multi-select v-model="filterFaecher" title="Fach" :items="stundenplanUnterrichtListeManager().faecher.list()" :item-text="fach => fach.bezeichnung" />
					<svws-ui-multi-select v-model="filterSchienen" title="Schiene" :items="stundenplanUnterrichtListeManager().schienen.list()" :item-text="schiene => `${stundenplanManager().schieneGetByIdOrException(schiene.id).nummer.toString()}: ${stundenplanManager().jahrgangGetByIdOrException(schiene.idJahrgang).kuerzel}`" />
				</template>
				<template #header>
					<div role="row" class="svws-ui-tr select-none">
						<div class="svws-ui-td svws-divider svws-align-center cursor-pointer" role="columnheader" @click="filterReset(true)"><span class="icon-sm i-ri-arrow-go-back-line inline-block w-full" /></div>
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
								@click="!stundenplanUnterrichtListeManager().stunden.auswahlHas(stunde) && !stundenplanUnterrichtListeManager().wochentage.auswahlHas(wochentag) && filterZeiraster(stundenplanManager().zeitrasterGetByWochentagAndStundeOrNull(wochentag.id, stunde))"
								:class="{ 'svws-selected bg-success/20':
									stundenplanManager().zeitrasterExistsByWochentagAndStunde(wochentag.id, stunde) &&
									(stundenplanUnterrichtListeManager().zeitraster.auswahlHas(stundenplanManager().zeitrasterGetByWochentagAndStundeOrException(wochentag.id, stunde))
										|| stundenplanUnterrichtListeManager().stunden.auswahlHas(stunde)
										|| stundenplanUnterrichtListeManager().wochentage.auswahlHas(wochentag))
								}">
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
				<template #cell(lehrer)="{ rowData, value }">
					<svws-ui-multi-select v-if="checkFocusMultiselect({ type: 'lehrer', id: rowData.id })"
						:model-value="modelValueLehrer(value)" @update:model-value="lehrer => patchLehrer(lehrer, rowData)"
						autofocus @blur="setFocusMultiselect({type: null, id: null})" :item-text="i => i.kuerzel" :item-filter="findLehrer" headless
						title="Fachlehrer" :items="stundenplanManager().lehrerGetMengeAsList()" />
					<button v-else @click="setFocusMultiselect({ type: 'lehrer', id: rowData.id })" class="w-full h-full text-left">
						<span v-if="value.size() > 0" class="decoration-dotted underline">
							{{ modelValueLehrer(value).map(i => i.kuerzel ?? '&mdash;').join(', ') }}
						</span>
						<span v-else class="underline decoration-dotted">&nbsp;&nbsp;&nbsp;</span>
					</button>
				</template>
				<template #cell(klassen)="{ rowData, value }">
					<template v-if="rowData.idKurs === null">
						<svws-ui-multi-select v-if="checkFocusMultiselect({ type: 'klassen', id: rowData.id })"
							:model-value="modelValueKlassen(value)" @update:model-value="klassen => patchKlassen(klassen, rowData)"
							autofocus @blur="setFocusMultiselect({type: null, id: null})" :item-text="i => i.kuerzel" :item-filter="find" headless
							title="Klassen" :items="stundenplanManager().klasseGetMengeAsList()" />
						<button v-else @click="setFocusMultiselect({ type: 'klassen', id: rowData.id })" class="w-full h-full text-left">
							<span v-if="value.size() > 0" class="decoration-dotted underline">
								{{ modelValueKlassen(value).map(i => i.kuerzel ?? '&mdash;').join(', ') }}
							</span>
							<span v-else class="underline decoration-dotted">&nbsp;&nbsp;&nbsp;</span>
						</button>
					</template>
					<span v-else class="opacity-50 pointer-events-none">&mdash;</span>
				</template>
				<template #cell(raeume)="{ rowData, value }">
					<svws-ui-multi-select v-if="checkFocusMultiselect({ type: 'raeume', id: rowData.id })"
						:model-value="modelValueRaeume(value)" @update:model-value="raeume => patchRaeume(raeume, rowData)"
						autofocus @blur="setFocusMultiselect({type: null, id: null})" :item-text="i => i.kuerzel" :item-filter="find" headless
						title="Räume" :items="stundenplanManager().raumGetMengeAsList()" />
					<button v-else @click="setFocusMultiselect({ type: 'raeume', id: rowData.id })" class="w-full h-full text-left">
						<span v-if="value.size() > 0" class="decoration-dotted underline">
							{{ modelValueRaeume(value).map(i => i.kuerzel ?? '&mdash;').join(', ') }}
						</span>
						<span v-else class="underline decoration-dotted">&nbsp;&nbsp;&nbsp;</span>
					</button>
				</template>
				<template #cell(schienen)="{ rowData, value }">
					<svws-ui-multi-select v-if="checkFocusMultiselect({ type: 'schienen', id: rowData.id })"
						:model-value="modelValueSchienen(value)" @update:model-value="schienen => patchSchienen(schienen, rowData)"
						autofocus @blur="setFocusMultiselect({type: null, id: null})" :item-text="i => i.nummer.toString()" headless
						title="Schienen" :items="stundenplanManager().schieneGetMengeAsList()" />
					<button v-else @click="setFocusMultiselect({ type: 'schienen', id: rowData.id })" class="w-full h-full text-left">
						<span v-if="value.size() > 0" class="decoration-dotted underline">
							{{ modelValueSchienen(value).map(i => i.nummer ?? '&mdash;').join(', ') }}
						</span>
						<span v-else class="underline decoration-dotted">&nbsp;&nbsp;&nbsp;</span>
					</button>
				</template>
			</svws-ui-table>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref } from "vue";
	import type { StundenplanUnterrichteProps } from "./SStundenplanUnterrichteProps";
	import type { List, StundenplanKlasse, StundenplanKurs, StundenplanRaum, StundenplanSchiene, StundenplanSchueler, StundenplanZeitraster, Wochentag, StundenplanLehrer, StundenplanFach, StundenplanUnterricht } from "@core";
	import { ArrayList, ListUtils, ZulaessigesFach } from "@core";

	type FokusType = { type: 'lehrer' | 'klassen' | 'raeume' | 'schienen' | null, id: number | null };

	const focusMultiselect = ref<FokusType>({ type: null, id : null });

	function setFocusMultiselect(fokusType : FokusType) : void {
		focusMultiselect.value = fokusType;
	}

	function checkFocusMultiselect(fokusType : FokusType) : boolean {
		return (focusMultiselect.value.type === fokusType.type) && (focusMultiselect.value.id === fokusType.id);
	}

	const props = defineProps<StundenplanUnterrichteProps>();

	const wochentage = [null, 'Mo', 'Di', 'Mi', 'Do', 'Fr', 'Sa', 'So'];

	// const hatUpdateKompetenz = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.STUNDENPLAN_AENDERN));

	const wochentyprange = computed(() => {
		const range = [];
		const modell = props.stundenplanManager().stundenplanGetWochenTypModell();
		for (let i = 0; i <= modell; i++)
			range.push(i);
		return range;
	})

	const columns = [
		{key: 'idZeitraster', label: 'Stunde'},
		...(wochentyprange.value.length > 1 ? [{key: 'wochentyp', label: 'Wochentyp'}] : []),
		{key: 'idKurs', label: 'Kurs oder Fach', span: 2},
		{key: 'lehrer', label: 'Lehrer'},
		{key: 'klassen', label: 'Klassen'},
		{key: 'raeume', label: 'Räume'},
		{key: 'schienen', label: 'Schienen'}
	];

	function getBgColor(kuerzel: string | null) {
		return ZulaessigesFach.getByKuerzelASD(kuerzel).getHMTLFarbeRGBA(1.0);
	}

	const filtered = computed(() => {
		return props.stundenplanUnterrichtListeManager().faecher.auswahlExists()
			|| props.stundenplanUnterrichtListeManager().kurse.auswahlExists()
			|| props.stundenplanUnterrichtListeManager().klassen.auswahlExists()
			|| props.stundenplanUnterrichtListeManager().lehrer.auswahlExists()
			|| props.stundenplanUnterrichtListeManager().raeume.auswahlExists()
			|| props.stundenplanUnterrichtListeManager().schienen.auswahlExists()
			|| props.stundenplanUnterrichtListeManager().schueler.auswahlExists()
			|| props.stundenplanUnterrichtListeManager().wochentypen.auswahlExists();
		// || props.stundenplanUnterrichtListeManager().wochentage.auswahlExists()
		// || props.stundenplanUnterrichtListeManager().stunden.auswahlExists()
		// || props.stundenplanUnterrichtListeManager().zeitraster.auswahlExists()
	})

	async function filterReset(table?: true) {
		if (!table) {
			props.stundenplanUnterrichtListeManager().klassen.auswahlClear();
			props.stundenplanUnterrichtListeManager().faecher.auswahlClear();
			props.stundenplanUnterrichtListeManager().kurse.auswahlClear();
			props.stundenplanUnterrichtListeManager().klassen.auswahlClear();
			props.stundenplanUnterrichtListeManager().lehrer.auswahlClear();
			props.stundenplanUnterrichtListeManager().raeume.auswahlClear();
			props.stundenplanUnterrichtListeManager().schienen.auswahlClear();
			props.stundenplanUnterrichtListeManager().schueler.auswahlClear();
			props.stundenplanUnterrichtListeManager().wochentypen.auswahlClear();
		}
		props.stundenplanUnterrichtListeManager().wochentage.auswahlClear();
		props.stundenplanUnterrichtListeManager().stunden.auswahlClear();
		props.stundenplanUnterrichtListeManager().zeitraster.auswahlClear();
		await props.setFilter();
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

	function filterZeiraster(zeitraster: StundenplanZeitraster|null) {
		if (zeitraster === null)
			return;
		props.stundenplanUnterrichtListeManager().zeitraster.auswahlToggle(zeitraster);
		void props.setFilter();
	}

	function modelValueLehrer(list: List<number>) {
		const res: StundenplanLehrer[] = [];
		for (const l of list)
			res.push(props.stundenplanManager().lehrerGetByIdOrException(l));
		return res;
	}

	async function patchLehrer(lehrer: StundenplanLehrer[], unterricht: StundenplanUnterricht) {
		const list = new ArrayList<number>();
		unterricht.lehrer.clear();
		for (const l of lehrer)
			unterricht.lehrer.add(l.id);
		await props.patchUnterricht(ListUtils.create1(unterricht));
	}


	function modelValueKlassen(list: List<number>) {
		const res: StundenplanKlasse[] = [];
		for (const k of list)
			res.push(props.stundenplanManager().klasseGetByIdOrException(k));
		return res;
	}

	async function patchKlassen(klassen: (StundenplanKlasse | StundenplanRaum)[], unterricht: StundenplanUnterricht) {
		const list = new ArrayList<number>();
		unterricht.klassen.clear();
		for (const k of klassen)
			unterricht.klassen.add(k.id);
		await props.patchUnterricht(ListUtils.create1(unterricht));
	}

	function modelValueRaeume(list: List<number>) {
		const res: StundenplanRaum[] = [];
		for (const r of list)
			res.push(props.stundenplanManager().raumGetByIdOrException(r));
		return res;
	}

	async function patchRaeume(raeume: (StundenplanKlasse | StundenplanRaum)[], unterricht: StundenplanUnterricht) {
		const list = new ArrayList<number>();
		unterricht.raeume.clear();
		for (const r of raeume)
			unterricht.raeume.add(r.id);
		await props.patchUnterricht(ListUtils.create1(unterricht));
	}

	function modelValueSchienen(list: List<number>) {
		const res: StundenplanSchiene[] = [];
		for (const s of list)
			res.push(props.stundenplanManager().schieneGetByIdOrException(s));
		return res;
	}

	async function patchSchienen(schienen: StundenplanSchiene[], unterricht: StundenplanUnterricht) {
		const list = new ArrayList<number>();
		unterricht.schienen.clear();
		for (const s of schienen)
			unterricht.schienen. add(s.id);
		await props.patchUnterricht(ListUtils.create1(unterricht));
	}


	function find(items: Iterable<StundenplanKlasse | StundenplanRaum>, search: string) {
		const list = [];
		for (const i of items)
			if (i.kuerzel.toLocaleLowerCase().includes(search.toLocaleLowerCase()))
				list.push(i);
		return list;
	}

	function findLehrer(items: Iterable<StundenplanLehrer>, search: string) {
		const list = [];
		const lcSearch = search.toLocaleLowerCase();
		for (const i of items)
			if (i.kuerzel.toLocaleLowerCase().includes(lcSearch)	|| i.nachname.toLocaleLowerCase().includes(lcSearch) || i.vorname.toLocaleLowerCase().includes(lcSearch))
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

</script>

<style lang="postcss" scoped>

	.page--content {
		@apply overflow-y-hidden overflow-x-auto h-full pb-3 pt-6 flex flex-row
	}

	.page--content-flex-column {
		@apply h-full overflow-y-auto w-full flex flex-col gap-8
	}

</style>
