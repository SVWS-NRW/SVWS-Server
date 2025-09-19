<template>
	<ui-table-grid name="Lehrämter" :manager="() => gridManager" hide-selection>
		<template #header>
			<th class="text-left col-span-2">
				<span class="cursor-pointer">
					<svws-ui-tooltip position="right">
						<span class="inline-flex items-center">
							<span class="icon i-ri-bar-chart-2-line text-input--statistic-icon" />
						</span>
						<template #content>
							Relevant für die Statistik
						</template>
					</svws-ui-tooltip>
				</span>
				Lehramt mit Lehrbefähigungen bzw. Fachrichtungen
			</th>
			<th class="text-left col-span-2">
				<span class="cursor-pointer">
					<svws-ui-tooltip position="right">
						<span class="inline-flex items-center">
							<span class="icon i-ri-bar-chart-2-line text-input--statistic-icon" />
						</span>
						<template #content>
							Relevant für die Statistik
						</template>
					</svws-ui-tooltip>
				</span>
				Anerkennungsgrund
			</th>
		</template>
		<template #default="{ row }">
			<template v-if="row instanceof LehrerLehramtEintrag">
				<td class="w-full text-left col-span-2">
					{{ getLehramt(row).daten(schuljahr)?.text ?? '—' }}
				</td>
				<td class="w-full">
					<svws-ui-select title="Anerkennungsgrund Lehramt" v-if="hatUpdateKompetenz" :model-value="getLehramtAnerkennung(row)"
						@update:model-value="anerkennung => patchLehramt(row, { idAnerkennungsgrund: anerkennung?.daten(schuljahr)?.id ?? null })"
						:items="LehrerLehramtAnerkennung.values()" :item-text="i => i.daten(schuljahr)?.text ?? '—'" headless />
					<div v-else class="text-left"> {{ getLehramtAnerkennung(row)?.daten(schuljahr)?.text ?? '—' }} </div>
				</td>
				<td class="text-left">
					<div v-if="hatUpdateKompetenz" class="inline-flex gap-1">
						<svws-ui-button @click="removeLehraemter(Arrays.asList(row))" type="trash" />
						<svws-ui-tooltip>
							<svws-ui-button type="icon" size="small">
								<span class="icon-sm i-ri-add-line" @click="() => openLehrbefFachrHinzufuegen(row)" />
							</svws-ui-button>
							<template #content>
								Lehrbefähigung oder Fachrichtung hinzufügen
							</template>
						</svws-ui-tooltip>
					</div>
				</td>
			</template>
			<template v-else-if="row instanceof LehrerLehrbefaehigungEintrag">
				<td />
				<td class="w-full text-left">
					{{ getLehrbefaehigungText(row) }}
				</td>
				<td class="w-full">
					<svws-ui-select title="Anerkennungsgrund Lehrbefähigung" v-if="hatUpdateKompetenz" :model-value="getLehrbefaehigungAnerkennung(row)"
						@update:model-value="anerkennung => patchLehrbefaehigung(row, { idAnerkennungsgrund: anerkennung?.daten(schuljahr)?.id ?? null })"
						:items="LehrerLehrbefaehigungAnerkennung.values()" :item-text="i => i.daten(schuljahr)?.text ?? '—'" headless />
					<div v-else class="text-left"> {{ getLehrbefaehigungAnerkennung(row)?.daten(schuljahr)?.text ?? '—' }} </div>
				</td>
				<td class="text-left">
					<div v-if="hatUpdateKompetenz" class="inline-flex gap-4">
						<svws-ui-button @click="removeLehrbefaehigungen(Arrays.asList(row))" type="trash" />
					</div>
				</td>
			</template>
			<template v-else-if="row instanceof LehrerFachrichtungEintrag">
				<td />
				<td class="w-full text-left">
					<span>Fachrichtung:</span> {{ getFachrichtung(row).daten(schuljahr)?.text ?? '—' }}
				</td>
				<td class="w-full">
					<svws-ui-select title="Anerkennungsgrund Fachrichtung" v-if="hatUpdateKompetenz" :model-value="getFachrichtungAnerkennung(row)"
						@update:model-value="anerkennung => patchFachrichtung(row, { idAnerkennungsgrund: anerkennung?.daten(schuljahr)?.id ?? null })"
						:items="LehrerFachrichtungAnerkennung.values()" :item-text="i => i.daten(schuljahr)?.text ?? '—'" headless />
					<div v-else class="text-left"> {{ getFachrichtungAnerkennung(row)?.daten(schuljahr)?.text ?? '—' }} </div>
				</td>
				<td class="text-left">
					<div v-if="hatUpdateKompetenz" class="inline-flex gap-4">
						<svws-ui-button @click="removeFachrichtungen(Arrays.asList(row))" type="trash" />
					</div>
				</td>
			</template>
		</template>
		<template #footer>
			<template v-if="hatUpdateKompetenz">
				<td class="col-span-4 text-right">
					<svws-ui-tooltip>
						<svws-ui-button type="icon" @click="openLehramtHinzufuegen">
							<span class="icon i-ri-add-line" />
						</svws-ui-button>
						<template #content>
							Lehramt hinzufügen
						</template>
					</svws-ui-tooltip>
				</td>
			</template>
			<template v-else>
				<td class="col-span-4" />
			</template>
		</template>
	</ui-table-grid>
	<svws-ui-modal v-model:show="showLehramtHinzufuegen" size="small" class="hidden">
		<template #modalTitle> Lehramt hinzufügen </template>
		<template #modalContent>
			<ui-select label="Lehrämter" v-model="auswahlLehramtNeu" :manager="lehraemterSelectManager" statistics required />
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="showLehramtHinzufuegen = false"> Abbrechen </svws-ui-button>
			<svws-ui-button @click="createLehramt"> Anlegen </svws-ui-button>
		</template>
	</svws-ui-modal>
	<svws-ui-modal v-model:show="showLehrbefFachrHinzufuegen" size="small" class="hidden">
		<template #modalTitle> Lehrbefähigung/Fachrichtung hinzufügen </template>
		<template #modalContent>
			<ui-select-multi label="Lehrbefähigungen" v-model="auswahlLehrbefaehigungenNeu" :manager="lehrbefaehigungenSelectManager" statistics required />
			<ui-select-multi label="Fachrichtungen" v-model="auswahlFachrichtungenNeu" :manager="fachrichtungenSelectManager" statistics required />
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="showLehrbefFachrHinzufuegen = false"> Abbrechen </svws-ui-button>
			<svws-ui-button @click="createLehrbefFachr"> Anlegen </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { computed, ref, shallowRef } from "vue";
	import type { List, LehrerLehramtKatalogEintrag, JavaSet, LehrerLehrbefaehigungKatalogEintrag, LehrerFachrichtungKatalogEintrag } from "@core";
	import { Arrays, ArrayList, HashSet } from "@core";
	import { LehrerLehramt, LehrerLehrbefaehigung, LehrerFachrichtung } from "@core";
	import { LehrerLehramtEintrag, LehrerLehrbefaehigungEintrag, LehrerFachrichtungEintrag } from "@core";
	import { LehrerLehramtAnerkennung, LehrerLehrbefaehigungAnerkennung, LehrerFachrichtungAnerkennung } from "@core";
	import type { LehrerListeManager } from "@ui";
	import { CoreTypeSelectManager, GridManager } from "@ui";

	const props = defineProps<{
		hatUpdateKompetenz: boolean;
		schuljahr: number;
		lehrerListeManager: () => LehrerListeManager;
		patchLehramt: (eintrag: LehrerLehramtEintrag, patch: Partial<LehrerLehramtEintrag>) => Promise<void>;
		addLehramt: (eintrag: Partial<LehrerLehramtEintrag>) => Promise<void>;
		removeLehraemter: (eintraege: List<LehrerLehramtEintrag>) => Promise<void>;
		patchLehrbefaehigung: (eintrag: LehrerLehrbefaehigungEintrag, patch: Partial<LehrerLehrbefaehigungEintrag>) => Promise<void>;
		addLehrbefaehigung: (eintrag: Partial<LehrerLehrbefaehigungEintrag>) => Promise<void>;
		removeLehrbefaehigungen: (eintraege: List<LehrerLehrbefaehigungEintrag>) => Promise<void>;
		patchFachrichtung: (eintrag: LehrerFachrichtungEintrag, patch : Partial<LehrerFachrichtungEintrag>) => Promise<void>;
		addFachrichtung: (eintrag: Partial<LehrerFachrichtungEintrag>) => Promise<void>;
		removeFachrichtungen: (eintraege: List<LehrerFachrichtungEintrag>) => Promise<void>;
	}>();

	function personaldaten() {
		return props.lehrerListeManager().personalDaten();
	}

	const showLehramtHinzufuegen = ref<boolean>(false);
	const auswahlLehramtNeu = shallowRef<LehrerLehramtKatalogEintrag | null>(null);
	const lehraemterSelectManager = computed(() => new CoreTypeSelectManager({
		clazz: LehrerLehramt.class, schuljahr: props.schuljahr, schulformen: props.lehrerListeManager().schulform(),
		filters: [ { key: 'vorhandene', apply: filterLehraemter } ],
		selectionDisplayText: 'text', optionDisplayText: 'kuerzelText',
	}));
	const lehraemterVorhanden = computed<JavaSet<number>>(() => {
		const vorhanden = new HashSet<number>();
		for (const lehramt of personaldaten().lehraemter)
			vorhanden.add(lehramt.idKatalogLehramt);
		return vorhanden;
	});

	function openLehramtHinzufuegen() {
		auswahlLehramtNeu.value = null;
		showLehramtHinzufuegen.value = true;
	}

	function filterLehraemter(options: List<LehrerLehramtKatalogEintrag>): List<LehrerLehramtKatalogEintrag> {
		const result = new ArrayList<LehrerLehramtKatalogEintrag>();
		for (const e of options)
			if (!lehraemterVorhanden.value.contains(e.id))
				result.add(e);
		return result;
	}

	async function createLehramt() {
		if ((auswahlLehramtNeu.value === null) || (lehraemterVorhanden.value.contains(auswahlLehramtNeu.value.id)))
			return;
		await props.addLehramt({ idLehrer: personaldaten().id, idKatalogLehramt: auswahlLehramtNeu.value.id, idAnerkennungsgrund: null });
		showLehramtHinzufuegen.value = false;
	}

	const showLehrbefFachrHinzufuegen = ref<boolean>(false);

	const auswahlLehrbefFachrNeuLehramt = shallowRef<LehrerLehramtEintrag | null>(null);
	const auswahlLehrbefaehigungenNeu = shallowRef<Array<LehrerLehrbefaehigungKatalogEintrag>>([]);
	const auswahlFachrichtungenNeu = shallowRef<Array<LehrerFachrichtungKatalogEintrag>>([]);

	const lehrbefaehigungenSelectManager = computed(() => new CoreTypeSelectManager({
		clazz: LehrerLehrbefaehigung.class, schuljahr: props.schuljahr, schulformen: props.lehrerListeManager().schulform(),
		filters: [ { key: 'vorhandene', apply: filterLehrbefaehigungen } ],
		selectionDisplayText: 'text', optionDisplayText: 'kuerzelText',
	}));

	const fachrichtungenSelectManager = computed(() => new CoreTypeSelectManager({
		clazz: LehrerFachrichtung.class, schuljahr: props.schuljahr, schulformen: props.lehrerListeManager().schulform(),
		filters: [ { key: 'vorhandene', apply: filterFachrichtungen } ],
		selectionDisplayText: 'text', optionDisplayText: 'kuerzelText',
	}));

	function openLehrbefFachrHinzufuegen(row: LehrerLehramtEintrag) {
		auswahlLehrbefaehigungenNeu.value = [];
		auswahlFachrichtungenNeu.value = [];
		auswahlLehrbefFachrNeuLehramt.value = row;
		showLehrbefFachrHinzufuegen.value = true;
	}

	const lehrbefaehigungenVorhanden = computed<JavaSet<number>>(() => {
		const vorhanden = new HashSet<number>();
		const lehramt = auswahlLehrbefFachrNeuLehramt.value;
		if (lehramt === null)
			return vorhanden;
		for (const lehrbef of lehramt.lehrbefaehigungen)
			vorhanden.add(lehrbef.idLehrbefaehigung);
		return vorhanden;
	});

	function filterLehrbefaehigungen(options: List<LehrerLehrbefaehigungKatalogEintrag>): List<LehrerLehrbefaehigungKatalogEintrag> {
		const result = new ArrayList<LehrerLehrbefaehigungKatalogEintrag>();
		for (const e of options)
			if (!lehrbefaehigungenVorhanden.value.contains(e.id))
				result.add(e);
		return result;
	}

	const fachrichtungenVorhanden = computed<JavaSet<number>>(() => {
		const vorhanden = new HashSet<number>();
		const lehramt = auswahlLehrbefFachrNeuLehramt.value;
		if (lehramt === null)
			return vorhanden;
		for (const fachr of lehramt.fachrichtungen)
			vorhanden.add(fachr.idFachrichtung);
		return vorhanden;
	});

	function filterFachrichtungen(options: List<LehrerFachrichtungKatalogEintrag>): List<LehrerFachrichtungKatalogEintrag> {
		const result = new ArrayList<LehrerFachrichtungKatalogEintrag>();
		for (const e of options)
			if (!fachrichtungenVorhanden.value.contains(e.id))
				result.add(e);
		return result;
	}

	async function createLehrbefFachr() {
		const lehramt = auswahlLehrbefFachrNeuLehramt.value;
		if (lehramt !== null) {
			for (const eintrag of auswahlLehrbefaehigungenNeu.value)
				if (!lehrbefaehigungenVorhanden.value.contains(eintrag.id))
					await props.addLehrbefaehigung({ idLehramt: lehramt.id, idLehrbefaehigung: eintrag.id, idAnerkennungsgrund: null });
			for (const eintrag of auswahlFachrichtungenNeu.value)
				if (!fachrichtungenVorhanden.value.contains(eintrag.id))
					await props.addFachrichtung({ idLehramt: lehramt.id, idFachrichtung: eintrag.id, idAnerkennungsgrund: null });
		}
		showLehrbefFachrHinzufuegen.value = false;
	}


	type GridDatenLehraemter = LehrerLehramtEintrag | LehrerLehrbefaehigungEintrag | LehrerFachrichtungEintrag;

	const gridManager = new GridManager<string, GridDatenLehraemter, List<GridDatenLehraemter>>({
		daten: computed<List<GridDatenLehraemter>>(() => {
			const result = new ArrayList<GridDatenLehraemter>();
			for (const lehramt of personaldaten().lehraemter) {
				result.add(lehramt);
				for (const l of lehramt.lehrbefaehigungen)
					result.add(l);
				for (const f of lehramt.fachrichtungen)
					result.add(f);
			}
			return result;
		}),
		getRowKey: row => {
			if (row instanceof LehrerLehramtEintrag)
				return "Lehramt_" + row.id;
			else if (row instanceof LehrerLehrbefaehigungEintrag)
				return "Lehrbefaehigung_" + row.id;
			else
				return "Fachrichtung_" + row.id;
		},
		columns: [
			{ kuerzel: "Indent", name: "Indent", width: "4rem", hideable: false },
			{ kuerzel: "Lehramt", name: "Lehramt", width: "minmax(40%,28rem)", hideable: false },
			{ kuerzel: "Anerkennungsgrund", name: "Anerkennungsgrund", width: "minmax(40%,28rem)", hideable: false },
			{ kuerzel: "Buttons", name: "Buttons", width: "4rem", hideable: false },
		],
	});

	function getLehramt(eintrag: LehrerLehramtEintrag) : LehrerLehramt {
		return LehrerLehramt.data().getWertByID(eintrag.idKatalogLehramt);
	}

	function getLehramtAnerkennung(eintrag: LehrerLehramtEintrag) : LehrerLehramtAnerkennung | null {
		return (eintrag.idAnerkennungsgrund === null) ? null : LehrerLehramtAnerkennung.data().getWertByID(eintrag.idAnerkennungsgrund);
	}

	function getLehrbefaehigungAnerkennung(eintrag: LehrerLehrbefaehigungEintrag) : LehrerLehrbefaehigungAnerkennung | null {
		return (eintrag.idAnerkennungsgrund === null) ? null : LehrerLehrbefaehigungAnerkennung.data().getWertByID(eintrag.idAnerkennungsgrund);
	}

	function getLehrbefaehigungText(eintrag: LehrerLehrbefaehigungEintrag) : string {
		const katalogEintrag = LehrerLehrbefaehigung.data().getEintragByID(eintrag.idLehrbefaehigung);
		return (katalogEintrag === null) ? '—' : katalogEintrag.kuerzel + ' - ' + katalogEintrag.text;
	}

	function getFachrichtung(eintrag: LehrerFachrichtungEintrag) : LehrerFachrichtung {
		return LehrerFachrichtung.data().getWertByID(eintrag.idFachrichtung);
	}

	function getFachrichtungAnerkennung(eintrag: LehrerFachrichtungEintrag) : LehrerFachrichtungAnerkennung | null {
		return (eintrag.idAnerkennungsgrund === null) ? null : LehrerFachrichtungAnerkennung.data().getWertByID(eintrag.idAnerkennungsgrund);
	}

</script>
