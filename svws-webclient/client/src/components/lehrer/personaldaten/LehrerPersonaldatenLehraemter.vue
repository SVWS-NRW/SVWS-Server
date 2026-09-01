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
		<template #default="{ row: { proxy, data } }">
			<template v-if="(proxy instanceof LehrerLehramtEintrag) && (data instanceof LehrerLehramtEintrag)">
				<td class="w-full text-left col-span-2">
					{{ getLehramt(proxy).daten(abschnittState.auswahl.schuljahr)?.text ?? '—' }}
				</td>
				<td class="w-full">
					<svws-ui-select title="Anerkennungsgrund Lehramt" v-if="hatUpdateKompetenz" :model-value="getLehramtAnerkennung(proxy)"
						@update:model-value="anerkennung => patchLehramt(proxy, { idAnerkennungsgrund: anerkennung?.daten(abschnittState.auswahl.schuljahr)?.id ?? null })"
						:items="LehrerLehramtAnerkennung.values()" :item-text="i => i.daten(abschnittState.auswahl.schuljahr)?.text ?? '—'" headless />
					<div v-else class="text-left"> {{ getLehramtAnerkennung(proxy)?.daten(abschnittState.auswahl.schuljahr)?.text ?? '—' }} </div>
				</td>
				<td class="text-left">
					<div v-if="hatUpdateKompetenz" class="inline-flex gap-1">
						<svws-ui-button @click="removeLehraemter(Arrays.asList(data))" type="trash" />
						<svws-ui-tooltip>
							<svws-ui-button type="icon" size="small">
								<span class="icon-sm i-ri-add-line" @click="() => openLehrbefFachrHinzufuegen(proxy)" />
							</svws-ui-button>
							<template #content>
								Lehrbefähigung oder Fachrichtung hinzufügen
							</template>
						</svws-ui-tooltip>
					</div>
				</td>
			</template>
			<template v-else-if="(proxy instanceof LehrerLehrbefaehigungEintrag) && (data instanceof LehrerLehrbefaehigungEintrag)">
				<td />
				<td class="w-full text-left">
					{{ getLehrbefaehigungText(proxy) }}
				</td>
				<td class="w-full">
					<svws-ui-select title="Anerkennungsgrund Lehrbefähigung" v-if="hatUpdateKompetenz" :model-value="getLehrbefaehigungAnerkennung(proxy)"
						@update:model-value="anerkennung => patchLehrbefaehigung(proxy, { idAnerkennungsgrund: anerkennung?.daten(abschnittState.auswahl.schuljahr)?.id ?? null })"
						:items="LehrerLehrbefaehigungAnerkennung.values()" :item-text="i => i.daten(abschnittState.auswahl.schuljahr)?.text ?? '—'" headless />
					<div v-else class="text-left"> {{ getLehrbefaehigungAnerkennung(proxy)?.daten(abschnittState.auswahl.schuljahr)?.text ?? '—' }} </div>
				</td>
				<td class="text-left">
					<div v-if="hatUpdateKompetenz" class="inline-flex gap-4">
						<svws-ui-button @click="removeLehrbefaehigungen(Arrays.asList(data))" type="trash" />
					</div>
				</td>
			</template>
			<template v-else-if="(proxy instanceof LehrerFachrichtungEintrag) && (data instanceof LehrerFachrichtungEintrag)">
				<td />
				<td class="w-full text-left">
					<span>Fachrichtung:</span> {{ getFachrichtung(proxy).daten(abschnittState.auswahl.schuljahr)?.text ?? '—' }}
				</td>
				<td class="w-full">
					<svws-ui-select title="Anerkennungsgrund Fachrichtung" v-if="hatUpdateKompetenz" :model-value="getFachrichtungAnerkennung(proxy)"
						@update:model-value="anerkennung => patchFachrichtung(proxy, { idAnerkennungsgrund: anerkennung?.daten(abschnittState.auswahl.schuljahr)?.id ?? null })"
						:items="LehrerFachrichtungAnerkennung.values()" :item-text="i => i.daten(abschnittState.auswahl.schuljahr)?.text ?? '—'" headless />
					<div v-else class="text-left"> {{ getFachrichtungAnerkennung(proxy)?.daten(abschnittState.auswahl.schuljahr)?.text ?? '—' }} </div>
				</td>
				<td class="text-left">
					<div v-if="hatUpdateKompetenz" class="inline-flex gap-4">
						<svws-ui-button @click="removeFachrichtungen(Arrays.asList(proxy))" type="trash" />
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
	<svws-ui-modal v-model:show="showLehrbefFachrHinzufuegen" size="medium" class="hidden">
		<template #modalTitle> Lehrbefähigung/Fachrichtung hinzufügen </template>
		<template #modalContent>
			<div class="flex flex-row">
				<div class="basis-3/4">
					<ui-select-multi label="Lehrbefähigungen" v-model="auswahlLehrbefaehigungenNeu" :manager="lehrbefaehigungenSelectManager" statistics required />
					<ui-select-multi label="Fachrichtungen" v-model="auswahlFachrichtungenNeu" :manager="fachrichtungenSelectManager" statistics required />
				</div>
				<div class="basis-1/4 flex flex-row justify-evenly items-end">
					<svws-ui-button type="secondary" @click="showLehrbefFachrHinzufuegen = false"> Abbrechen </svws-ui-button>
					<svws-ui-button @click="createLehrbefFachr"> Anlegen </svws-ui-button>
				</div>
			</div>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { computed, ref, shallowRef } from "vue";
	import type { List, LehrerLehramtKatalogEintrag, JavaSet, LehrerLehrbefaehigungKatalogEintrag, LehrerFachrichtungKatalogEintrag } from "@core";
	import { Arrays, ArrayList, HashSet, LehrerLehramt, LehrerLehrbefaehigung, LehrerFachrichtung, LehrerLehramtEintrag, LehrerLehrbefaehigungEintrag,
		LehrerFachrichtungEintrag, LehrerLehramtAnerkennung, LehrerLehrbefaehigungAnerkennung, LehrerFachrichtungAnerkennung } from "@core";
	import type { LehrerListeManager } from "@ui";
	import { CoreTypeSelectManager, GridManager, useAbschnittState } from "@ui";
	import { LehrerLehramtEintragModelProxy } from "./modelproxy/LehrerLehramtEintragModelProxy";
	import type { LehrerPersonaldatenModelProxy } from "./modelproxy/LehrerPersonaldatenModelProxy";
	import { LehrerLehrbefaehigungEintragModelProxy } from "./modelproxy/LehrerLehrbefaehigungEintragModelProxy";
	import { LehrerFachrichtungEintragModelProxy } from "./modelproxy/LehrerFachrichtungEintragModelProxy";

	const props = defineProps<{
		hatUpdateKompetenz: boolean;
		personaldatenModelProxy: () => LehrerPersonaldatenModelProxy,
		lehrerListeManager: () => LehrerListeManager;
		patchLehramt: (eintrag: LehrerLehramtEintrag, patch: Partial<LehrerLehramtEintrag>) => Promise<void>;
		addLehramt: (eintrag: Partial<LehrerLehramtEintrag>) => Promise<void>;
		removeLehraemter: (eintraege: List<LehrerLehramtEintrag>) => Promise<void>;
		patchLehrbefaehigung: (eintrag: LehrerLehrbefaehigungEintrag, patch: Partial<LehrerLehrbefaehigungEintrag>) => Promise<void>;
		addLehrbefaehigung: (eintrag: Partial<LehrerLehrbefaehigungEintrag>) => Promise<void>;
		removeLehrbefaehigungen: (eintraege: List<LehrerLehrbefaehigungEintrag>) => Promise<void>;
		patchFachrichtung: (eintrag: LehrerFachrichtungEintrag, patch: Partial<LehrerFachrichtungEintrag>) => Promise<void>;
		addFachrichtung: (eintrag: Partial<LehrerFachrichtungEintrag>) => Promise<void>;
		removeFachrichtungen: (eintraege: List<LehrerFachrichtungEintrag>) => Promise<void>;
	}>();
	const abschnittState = useAbschnittState();

	const showLehramtHinzufuegen = ref<boolean>(false);
	const auswahlLehramtNeu = shallowRef<LehrerLehramtKatalogEintrag | null>(null);
	const lehraemterSelectManager = computed(() => new CoreTypeSelectManager({
		clazz: LehrerLehramt.class, schuljahr: abschnittState.auswahl.schuljahr, schulformen: props.lehrerListeManager().schulform(),
		filters: [{ key: 'vorhandene', apply: filterLehraemter }],
		selectionDisplayText: 'text', optionDisplayText: 'kuerzelText',
	}));
	const lehraemterVorhanden = computed<JavaSet<number>>(() => {
		const vorhanden = new HashSet<number>();
		for (const lehramt of props.personaldatenModelProxy().proxy.lehraemter) {
			vorhanden.add(lehramt.idKatalogLehramt);
		}
		return vorhanden;
	});

	function openLehramtHinzufuegen() {
		auswahlLehramtNeu.value = null;
		showLehramtHinzufuegen.value = true;
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
		if ((auswahlLehramtNeu.value === null) || (lehraemterVorhanden.value.contains(auswahlLehramtNeu.value.id))) {
			return;
		}
		await props.addLehramt({ idLehrer: props.personaldatenModelProxy().proxy.id, idKatalogLehramt: auswahlLehramtNeu.value.id, idAnerkennungsgrund: null });
		showLehramtHinzufuegen.value = false;
		props.personaldatenModelProxy().validate();
	}

	const showLehrbefFachrHinzufuegen = ref<boolean>(false);

	const auswahlLehrbefFachrNeuLehramt = shallowRef<LehrerLehramtEintrag | null>(null);
	const auswahlLehrbefaehigungenNeu = shallowRef<Array<LehrerLehrbefaehigungKatalogEintrag>>([]);
	const auswahlFachrichtungenNeu = shallowRef<Array<LehrerFachrichtungKatalogEintrag>>([]);

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

	function openLehrbefFachrHinzufuegen(row: LehrerLehramtEintrag) {
		auswahlLehrbefaehigungenNeu.value = [];
		auswahlFachrichtungenNeu.value = [];
		auswahlLehrbefFachrNeuLehramt.value = row;
		showLehrbefFachrHinzufuegen.value = true;
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
				if (!lehrbefaehigungenVorhanden.value.contains(eintrag.id)) {
					await props.addLehrbefaehigung({ idLehramt: lehramt.id, idLehrbefaehigung: eintrag.id, idAnerkennungsgrund: null });
				}
			}
			for (const eintrag of auswahlFachrichtungenNeu.value) {
				if (!fachrichtungenVorhanden.value.contains(eintrag.id)) {
					await props.addFachrichtung({ idLehramt: lehramt.id, idFachrichtung: eintrag.id, idAnerkennungsgrund: null });
				}
			}
		}
		showLehrbefFachrHinzufuegen.value = false;
		props.personaldatenModelProxy().validate();
	}

	type GridDatenLehraemter = LehrerLehramtEintragModelProxy | LehrerLehrbefaehigungEintragModelProxy | LehrerFachrichtungEintragModelProxy;

	const gridManager = new GridManager<string, GridDatenLehraemter, List<GridDatenLehraemter>>({
		daten: computed<List<GridDatenLehraemter>>(() => {
			const result = new ArrayList<GridDatenLehraemter>();
			for (const lehramt of props.personaldatenModelProxy().data.lehraemter) {
				const modelProxy = new LehrerLehramtEintragModelProxy(() => lehramt);
				result.add(modelProxy);
				for (const lehrbefaehigung of lehramt.lehrbefaehigungen) {
					const modelProxy = new LehrerLehrbefaehigungEintragModelProxy(() => lehrbefaehigung);
					result.add(modelProxy);
				}
				for (const fachrichtung of lehramt.fachrichtungen) {
					const modelProxy = new LehrerFachrichtungEintragModelProxy(() => fachrichtung);
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
			{ kuerzel: "Buttons", name: "Buttons", width: "4rem", hideable: false },
		],
	});

	function getLehramt(eintrag: LehrerLehramtEintrag): LehrerLehramt {
		return LehrerLehramt.data().getWertByID(eintrag.idKatalogLehramt);
	}

	function getLehramtAnerkennung(eintrag: LehrerLehramtEintrag): LehrerLehramtAnerkennung | null {
		return (eintrag.idAnerkennungsgrund === null) ? null : LehrerLehramtAnerkennung.data().getWertByID(eintrag.idAnerkennungsgrund);
	}

	function getLehrbefaehigungAnerkennung(eintrag: LehrerLehrbefaehigungEintrag): LehrerLehrbefaehigungAnerkennung | null {
		return (eintrag.idAnerkennungsgrund === null) ? null : LehrerLehrbefaehigungAnerkennung.data().getWertByID(eintrag.idAnerkennungsgrund);
	}

	function getLehrbefaehigungText(eintrag: LehrerLehrbefaehigungEintrag): string {
		const katalogEintrag = LehrerLehrbefaehigung.data().getEintragByID(eintrag.idLehrbefaehigung);
		return (katalogEintrag === null) ? '—' : katalogEintrag.kuerzel + ' - ' + katalogEintrag.text;
	}

	function getFachrichtung(eintrag: LehrerFachrichtungEintrag): LehrerFachrichtung {
		return LehrerFachrichtung.data().getWertByID(eintrag.idFachrichtung);
	}

	function getFachrichtungAnerkennung(eintrag: LehrerFachrichtungEintrag): LehrerFachrichtungAnerkennung | null {
		return (eintrag.idAnerkennungsgrund === null) ? null : LehrerFachrichtungAnerkennung.data().getWertByID(eintrag.idAnerkennungsgrund);
	}

</script>
