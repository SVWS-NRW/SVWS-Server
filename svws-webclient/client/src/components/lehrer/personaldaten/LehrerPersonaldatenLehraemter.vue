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
			<template v-if="row instanceof LehrerLehramtEintragModelProxy">
				<td class="w-full text-left col-span-2">
					{{ row.lehramt.value?.text ?? '—' }}
				</td>
				<td class="w-full">
					<ui-select v-if="hatUpdateKompetenz"
						label="Anerkennungsgrund Lehramt"
						v-model="row.anerkennung.value"
						:manager="lehramtAnerkennungSelectManager"
						headless :removable="false" />
					<div v-else class="text-left"> {{ row.anerkennung.value?.text ?? '—' }} </div>
				</td>
				<td class="pr-3">
					<ui-table-actions :actions="rowActions(row)" :items="row" />
				</td>
			</template>
			<template v-else-if="row instanceof LehrerLehrbefaehigungEintragModelProxy">
				<td />
				<td class="w-full text-left">
					{{ getLehrbefaehigungText(row.lehrbefaehigung.value) }}
				</td>
				<td class="w-full">
					<ui-select v-if="hatUpdateKompetenz"
						label="Anerkennungsgrund Lehrbefähigung"
						v-model="row.anerkennung.value"
						:manager="lehrbefaehigungAnerkennungSelectManager"
						headless :removable="false" />
					<div v-else class="text-left"> {{ row.anerkennung.value?.text ?? '—' }} </div>
				</td>
				<td class="pr-9.5">
					<ui-table-actions :actions="rowActions(row)" :items="row" />
				</td>
			</template>
			<template v-else-if="row instanceof LehrerFachrichtungEintragModelProxy">
				<td />
				<td class="w-full text-left">
					<span>Fachrichtung:</span> {{ row.fachrichtung.value?.text ?? '—' }}
				</td>
				<td class="w-full">
					<ui-select v-if="hatUpdateKompetenz"
						label="Anerkennungsgrund Fachrichtung"
						v-model="row.anerkennung.value"
						:manager="fachrichtungAnerkennungSelectManager"
						headless :removable="false" />
					<div v-else class="text-left"> {{ row.anerkennung.value?.text ?? '—' }} </div>
				</td>
				<td class="pr-5">
					<ui-table-actions :actions="rowActions(row)" :items="row" />
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
	<svws-ui-modal v-model:show="showLehramtHinzufuegen" v-if="createLehramtModel !== null" size="small" class="hidden">
		<template #modalTitle> Lehramt hinzufügen </template>
		<template #modalContent>
			<ui-select label="Lehrämter"
				v-model="createLehramtModel.lehramt.value"
				:validation="() => createLehramtModel?.getFehler('idKatalogLehramt') ?? new ArrayList()"
				:manager="lehraemterSelectManager"
				statistics required />
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="closeCreateLehramt()"> Abbrechen </svws-ui-button>
			<svws-ui-button @click="createLehramt" :disabled="createLehramtModel.getFehler('idKatalogLehramt').size() > 0"> Anlegen </svws-ui-button>
		</template>
	</svws-ui-modal>
	<svws-ui-modal v-model:show="showLehrbefFachrHinzufuegen" size="small" class="hidden">
		<template #modalTitle> Lehrbefähigung/Fachrichtung hinzufügen </template>
		<template #modalContent>
			<div class="flex flex-col">
				<ui-select-multi label="Lehrbefähigungen"
					v-model="auswahlLehrbefaehigungenNeu"
					@update:model-value="groupValidator.run()"
					:validation="() => groupValidator.getFehler()"
					:manager="lehrbefaehigungenSelectManager"
					statistics />
				<ui-select-multi label="Fachrichtungen"
					v-model="auswahlFachrichtungenNeu"
					@update:model-value="groupValidator.run()"
					:validation="() => groupValidator.getFehler()"
					:manager="fachrichtungenSelectManager"
					statistics />
			</div>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="closeCreateLehrbefFachr()"> Abbrechen </svws-ui-button>
			<svws-ui-button @click="createLehrbefFachr" :disabled="groupValidator.getFehler().size() > 0"> Anlegen </svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { computed, ref, shallowRef, watch } from "vue";
	import type { List, LehrerLehramtKatalogEintrag, JavaSet, LehrerLehrbefaehigungKatalogEintrag, LehrerFachrichtungKatalogEintrag,
		LehrerFachrichtungEintrag } from "@core";
	import { Arrays, ArrayList, HashSet, LehrerLehramt, LehrerLehrbefaehigung, LehrerFachrichtung, LehrerLehramtEintrag, LehrerLehrbefaehigungEintrag,
		LehrerLehramtAnerkennung, LehrerLehrbefaehigungAnerkennung, LehrerFachrichtungAnerkennung } from "@core";
	import type { LehrerListeManager, TableActions } from "@ui";
	import { CoreTypeSelectManager, GridManager, useAbschnittState, ValidatorInputGroupRequired, ValidatorInputGroupRequiredModus } from "@ui";
	import { LehrerLehramtEintragModelProxy } from "./modelproxy/LehrerLehramtEintragModelProxy";
	import type { LehrerPersonaldatenModelProxy } from "./modelproxy/LehrerPersonaldatenModelProxy";
	import { LehrerLehrbefaehigungEintragModelProxy } from "./modelproxy/LehrerLehrbefaehigungEintragModelProxy";
	import { LehrerFachrichtungEintragModelProxy } from "./modelproxy/LehrerFachrichtungEintragModelProxy";

	const props = defineProps<{
		hatUpdateKompetenz: boolean;
		personaldatenModelProxy: () => LehrerPersonaldatenModelProxy,
		lehrerListeManager: () => LehrerListeManager;
		patchLehramt: (eintrag: LehrerLehramtEintrag, patch: Partial<LehrerLehramtEintrag>) => Promise<boolean>;
		addLehramt: (eintrag: Partial<LehrerLehramtEintrag>) => Promise<void>;
		removeLehraemter: (eintraege: List<LehrerLehramtEintrag>) => Promise<void>;
		patchLehrbefaehigung: (eintrag: LehrerLehrbefaehigungEintrag, patch: Partial<LehrerLehrbefaehigungEintrag>) => Promise<boolean>;
		addLehrbefaehigung: (eintrag: Partial<LehrerLehrbefaehigungEintrag>) => Promise<void>;
		removeLehrbefaehigungen: (eintraege: List<LehrerLehrbefaehigungEintrag>) => Promise<void>;
		patchFachrichtung: (eintrag: LehrerFachrichtungEintrag, patch: Partial<LehrerFachrichtungEintrag>) => Promise<boolean>;
		addFachrichtung: (eintrag: Partial<LehrerFachrichtungEintrag>) => Promise<void>;
		removeFachrichtungen: (eintraege: List<LehrerFachrichtungEintrag>) => Promise<void>;
	}>();
	const abschnittState = useAbschnittState();

	const showLehramtHinzufuegen = ref<boolean>(false);
	const lehraemterSelectManager = computed(() => new CoreTypeSelectManager({
		clazz: LehrerLehramt.class, schuljahr: abschnittState.auswahl.schuljahr, schulformen: props.lehrerListeManager().schulform(),
		filters: [{ key: 'vorhandene', apply: filterLehraemter }],
		selectionDisplayText: 'text', optionDisplayText: 'kuerzelText',
	}));
	const createLehramtModel = shallowRef<LehrerLehramtEintragModelProxy | null>(null);

	const lehramtAnerkennungSelectManager = computed(() => new CoreTypeSelectManager({
		clazz: LehrerLehramtAnerkennung.class,
		schuljahr: abschnittState.auswahl.schuljahr,
		schulformen: props.lehrerListeManager().schulform(),
		optionDisplayText: "text",
		selectionDisplayText: "text",
	}));

	const lehraemterVorhanden = ref<JavaSet<number>>(new HashSet<number>());

	watch(() => props.personaldatenModelProxy(), (model) => {
		const vorhanden = new HashSet<number>();
		for (const lehramt of model.proxy.lehraemter) {
			vorhanden.add(lehramt.idKatalogLehramt);
		}
		lehraemterVorhanden.value = vorhanden;
	}, { immediate: true, deep: true });

	function openLehramtHinzufuegen() {
		showLehramtHinzufuegen.value = true;
		createLehramtModel.value = new LehrerLehramtEintragModelProxy(() => new LehrerLehramtEintrag());
	}

	function filterLehraemter(options: List<LehrerLehramtKatalogEintrag>): List<LehrerLehramtKatalogEintrag> {
		const result = new ArrayList<LehrerLehramtKatalogEintrag>();
		for (const e of options) {
			if (!lehraemterVorhanden.value.contains(e.id)) {
				result.add(e);
			}
		}
		return result;
	}

	async function createLehramt() {
		if (createLehramtModel.value === null) {
			return;
		}
		createLehramtModel.value.proxy.idLehrer = props.personaldatenModelProxy().proxy.id;
		await props.addLehramt(createLehramtModel.value.pending);
		showLehramtHinzufuegen.value = false;
		createLehramtModel.value = null;
		props.personaldatenModelProxy().validate();
	}

	const showLehrbefFachrHinzufuegen = ref<boolean>(false);

	const auswahlLehrbefFachrNeuLehramt = shallowRef<LehrerLehramtEintrag | null>(null);
	const auswahlLehrbefaehigungenNeu = shallowRef<Array<LehrerLehrbefaehigungKatalogEintrag>>([]);
	const auswahlFachrichtungenNeu = shallowRef<Array<LehrerFachrichtungKatalogEintrag>>([]);
	const groupValidator = new ValidatorInputGroupRequired([
			{ fieldName: "Lehrbefähigungen", fieldData: () => auswahlLehrbefaehigungenNeu.value },
			{ fieldName: "Fachrichtungen", fieldData: () => auswahlFachrichtungenNeu.value }],
		ValidatorInputGroupRequiredModus.AT_LEAST_ONE);

	const lehrbefaehigungenSelectManager = computed(() => new CoreTypeSelectManager({
		clazz: LehrerLehrbefaehigung.class, schuljahr: abschnittState.auswahl.schuljahr, schulformen: props.lehrerListeManager().schulform(),
		filters: [{ key: 'vorhandene', apply: filterLehrbefaehigungen }],
		selectionDisplayText: 'text', optionDisplayText: 'kuerzelText',
	}));

	const fachrichtungenSelectManager = computed(() => new CoreTypeSelectManager({
		clazz: LehrerFachrichtung.class, schuljahr: abschnittState.auswahl.schuljahr, schulformen: props.lehrerListeManager().schulform(),
		filters: [{ key: 'vorhandene', apply: filterFachrichtungen }],
		selectionDisplayText: 'text', optionDisplayText: 'kuerzelText',
	}));

	const lehrbefaehigungAnerkennungSelectManager = computed(() => new CoreTypeSelectManager({
		clazz: LehrerLehrbefaehigungAnerkennung.class,
		schuljahr: abschnittState.auswahl.schuljahr,
		schulformen: props.lehrerListeManager().schulform(),
		optionDisplayText: "text",
		selectionDisplayText: "text",
	}));

	const fachrichtungAnerkennungSelectManager = computed(() => new CoreTypeSelectManager({
		clazz: LehrerFachrichtungAnerkennung.class,
		schuljahr: abschnittState.auswahl.schuljahr,
		schulformen: props.lehrerListeManager().schulform(),
		optionDisplayText: "text",
		selectionDisplayText: "text",
	}));

	function openLehrbefFachrHinzufuegen(row: LehrerLehramtEintrag) {
		auswahlLehrbefaehigungenNeu.value = [];
		auswahlFachrichtungenNeu.value = [];
		auswahlLehrbefFachrNeuLehramt.value = row;
		showLehrbefFachrHinzufuegen.value = true;
		groupValidator.run();
	}

	const lehrbefaehigungenVorhanden = computed<JavaSet<number>>(() => {
		const vorhanden = new HashSet<number>();
		const lehramt = auswahlLehrbefFachrNeuLehramt.value;
		if (lehramt === null) {
			return vorhanden;
		}
		for (const lehrbef of lehramt.lehrbefaehigungen) {
			vorhanden.add(lehrbef.idLehrbefaehigung);
		}
		return vorhanden;
	});

	function filterLehrbefaehigungen(options: List<LehrerLehrbefaehigungKatalogEintrag>): List<LehrerLehrbefaehigungKatalogEintrag> {
		const result = new ArrayList<LehrerLehrbefaehigungKatalogEintrag>();
		for (const e of options) {
			if (!lehrbefaehigungenVorhanden.value.contains(e.id)) {
				result.add(e);
			}
		}
		return result;
	}

	const fachrichtungenVorhanden = computed<JavaSet<number>>(() => {
		const vorhanden = new HashSet<number>();
		const lehramt = auswahlLehrbefFachrNeuLehramt.value;
		if (lehramt === null) {
			return vorhanden;
		}
		for (const fachr of lehramt.fachrichtungen) {
			vorhanden.add(fachr.idFachrichtung);
		}
		return vorhanden;
	});

	function filterFachrichtungen(options: List<LehrerFachrichtungKatalogEintrag>): List<LehrerFachrichtungKatalogEintrag> {
		const result = new ArrayList<LehrerFachrichtungKatalogEintrag>();
		for (const e of options) {
			if (!fachrichtungenVorhanden.value.contains(e.id)) {
				result.add(e);
			}
		}
		return result;
	}

	async function createLehrbefFachr() {
		const lehramt = auswahlLehrbefFachrNeuLehramt.value;
		if (lehramt !== null) {
			for (const eintrag of auswahlLehrbefaehigungenNeu.value) {
				if (!lehramt.lehrbefaehigungen.contains(eintrag)) {
					await props.addLehrbefaehigung({ idLehramt: lehramt.id, idLehrbefaehigung: eintrag.id, idAnerkennungsgrund: null });
				}
			}
			for (const eintrag of auswahlFachrichtungenNeu.value) {
				if (!lehramt.fachrichtungen.contains(eintrag)) {
					await props.addFachrichtung({ idLehramt: lehramt.id, idFachrichtung: eintrag.id, idAnerkennungsgrund: null });
				}
			}
		}
		closeCreateLehrbefFachr();
		props.personaldatenModelProxy().validate();
	}

	type GridDatenLehraemter = LehrerLehramtEintragModelProxy | LehrerLehrbefaehigungEintragModelProxy | LehrerFachrichtungEintragModelProxy;

	function closeCreateLehrbefFachr() {
		showLehrbefFachrHinzufuegen.value = false;
		auswahlLehrbefFachrNeuLehramt.value = null;
	}

	function closeCreateLehramt() {
		showLehramtHinzufuegen.value = false;
		createLehramtModel.value = null;
	}

	function rowActions(rowModel: GridDatenLehraemter): TableActions<GridDatenLehraemter>[] {
		const rowActions: TableActions<GridDatenLehraemter>[] = [];
		let removeFn;
		if (rowModel instanceof LehrerLehramtEintragModelProxy) {
			removeFn = () => props.removeLehraemter(Arrays.asList(rowModel.data));
			rowActions.push({ label: "Eintrag löschen", action: removeFn, trash: true }, {
				label: "Lehrbefähigung oder Fachrichtung hinzufügen",
				action: () => openLehrbefFachrHinzufuegen(rowModel.data),
				iconClasses: "i-ri-add-line",
			});
		} else if (rowModel instanceof LehrerLehrbefaehigungEintragModelProxy) {
			removeFn = () => props.removeLehrbefaehigungen(Arrays.asList(rowModel.data));
			rowActions.push({ label: "Eintrag löschen", action: removeFn, trash: true });
		} else {
			removeFn = () => props.removeFachrichtungen(Arrays.asList(rowModel.data));
			rowActions.push({ label: "Eintrag löschen", action: removeFn, trash: true });
		}
		return rowActions;
	}

	const gridManager = new GridManager<string, GridDatenLehraemter, List<GridDatenLehraemter>>({
		daten: computed<List<GridDatenLehraemter>>(() => {
			const result = new ArrayList<GridDatenLehraemter>();
			for (const lehramt of props.personaldatenModelProxy().data.lehraemter) {
				const modelProxy = new LehrerLehramtEintragModelProxy(() => lehramt,
					(lehramtPatch: Partial<LehrerLehramtEintrag>) => props.patchLehramt(lehramt, lehramtPatch));
				result.add(modelProxy);
				for (const lehrbefaehigung of lehramt.lehrbefaehigungen) {
					const modelProxy = new LehrerLehrbefaehigungEintragModelProxy(() => lehrbefaehigung,
						(lehrbefaehigungPatch: Partial<LehrerLehrbefaehigungEintrag>) => props.patchLehrbefaehigung(lehrbefaehigung, lehrbefaehigungPatch));
					result.add(modelProxy);
				}
				for (const fachrichtung of lehramt.fachrichtungen) {
					const modelProxy = new LehrerFachrichtungEintragModelProxy(() => fachrichtung,
						(fachrichtungPatch: Partial<LehrerFachrichtungEintrag>) => props.patchFachrichtung(fachrichtung, fachrichtungPatch));
					result.add(modelProxy);
				}
			}
			return result;
		}),
		getRowKey: row => {
			const { proxy } = row;
			if (proxy instanceof LehrerLehramtEintrag) {
				return "Lehramt_" + proxy.id;
			} else if (proxy instanceof LehrerLehrbefaehigungEintrag) {
				return "Lehrbefaehigung_" + proxy.id;
			} else {
				return "Fachrichtung_" + proxy.id;
			}
		},
		columns: [
			{ kuerzel: "Indent", name: "Indent", width: "4rem", hideable: false },
			{ kuerzel: "Lehramt", name: "Lehramt", width: "minmax(40%,28rem)", hideable: false },
			{ kuerzel: "Anerkennungsgrund", name: "Anerkennungsgrund", width: "minmax(40%,28rem)", hideable: false },
			{ kuerzel: "", name: "Row-Actions", width: '4rem' },
		],
	});

	function getLehrbefaehigungText(lehrbefaehigung: LehrerLehrbefaehigungKatalogEintrag | null): string {
		return (lehrbefaehigung === null) ? '—' : lehrbefaehigung.kuerzel + ' - ' + lehrbefaehigung.text;
	}

</script>
