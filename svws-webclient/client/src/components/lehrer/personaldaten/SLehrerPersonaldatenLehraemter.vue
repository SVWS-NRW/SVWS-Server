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
					<div v-else> {{ getLehramtAnerkennung(row)?.daten(schuljahr)?.text ?? '—' }} </div>
				</td>
				<td class="text-left">
					<div v-if="hatUpdateKompetenz" class="inline-flex gap-1">
						<svws-ui-button @click="removeLehraemter(Arrays.asList(row))" type="trash" />
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
					<div v-else> {{ getLehrbefaehigungAnerkennung(row)?.daten(schuljahr)?.text ?? '—' }} </div>
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
					<div v-else> {{ getFachrichtungAnerkennung(row)?.daten(schuljahr)?.text ?? '—' }} </div>
				</td>
				<td class="text-left">
					<div v-if="hatUpdateKompetenz" class="inline-flex gap-4">
						<svws-ui-button @click="removeFachrichtungen(Arrays.asList(row))" type="trash" />
					</div>
				</td>
			</template>
			<template v-else-if="row.type === 'lehrbefaehigung'">
				<td />
				<td>
					<div class="w-fit flex flex-row items-center">
						<span class="inline-block icon-sm i-ri-add-line" />
						<svws-ui-button-select type="transparent" :dropdown-actions="actionsLehrbefaehigungenHinzufuegen.get(row.id)!" :default-action="{ text: 'Lehrbefähigung hinzufügen', action: () => {}}" no-default />
					</div>
				</td>
				<td class="col-span-2" />
			</template>
			<template v-else>
				<td />
				<td class="">
					<div class="w-fit flex flex-row items-center">
						<span class="inline-block icon-sm i-ri-add-line" />
						<svws-ui-button-select type="transparent" :dropdown-actions="actionsFachrichtungenHinzufuegen.get(row.id)!" :default-action="{ text: 'Fachrichtung hinzufügen', action: () => {}}" no-default />
					</div>
				</td>
				<td class="col-span-2" />
			</template>
		</template>
		<template #footer>
			<template v-if="hatUpdateKompetenz">
				<td class="col-span-2">
					<div class="w-fit flex flex-row items-center">
						<span class="inline-block icon-sm i-ri-add-line" />
						<svws-ui-button-select type="transparent" :dropdown-actions="actionsLehraemterHinzufuegen" :default-action="{ text: 'Lehramt hinzufügen', action: () => {}}" no-default />
					</div>
				</td>
				<td class="col-span-2" />
			</template>
			<template v-else>
				<td class="col-span-4" />
			</template>
		</template>
	</ui-table-grid>
</template>

<script setup lang="ts">

	import { computed } from "vue";
	import type { List, JavaMap } from "@core";
	import { Arrays, ArrayList, HashSet, HashMap } from "@core";
	import { LehrerLehramt, LehrerLehrbefaehigung, LehrerFachrichtung } from "@core";
	import { LehrerLehramtEintrag, LehrerLehrbefaehigungEintrag, LehrerFachrichtungEintrag } from "@core";
	import { LehrerLehramtAnerkennung, LehrerLehrbefaehigungAnerkennung, LehrerFachrichtungAnerkennung } from "@core";
	import type { LehrerListeManager } from "@ui";
	import { GridManager } from "@ui";

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

	const actionsLehraemterHinzufuegen = computed<Array<{ text: string; action: () => void | Promise<any>; }>>(() => {
		const result = new Array<{ text: string; action: () => void | Promise<any>; }>();
		const vorhanden = new HashSet<number>();
		for (const lehramt of personaldaten().lehraemter)
			vorhanden.add(lehramt.idKatalogLehramt);
		for (const lehramt of LehrerLehramt.data().getWerteBySchuljahr(props.schuljahr)) {
			const eintrag = lehramt.daten(props.schuljahr);
			if ((eintrag === null) || (vorhanden.contains(eintrag.id)))
				continue;
			result.push({
				text: eintrag.text,
				action: async () => await props.addLehramt({
					idLehrer: personaldaten().id,
					idKatalogLehramt: eintrag.id,
					idAnerkennungsgrund: null,
				}),
			});
		}
		return result;
	});

	const actionsLehrbefaehigungenHinzufuegen = computed<JavaMap<number, Array<{ text: string; action: () => void | Promise<any>; }>>>(() => {
		const result = new HashMap<number, Array<{ text: string; action: () => void | Promise<any>; }>>();
		for (const lehramt of personaldaten().lehraemter) {
			const list = new Array<{ text: string; action: () => void | Promise<any>; }>();
			for (const lehrbefaehigung of LehrerLehrbefaehigung.data().getWerteBySchuljahr(props.schuljahr)) {
				const eintrag = lehrbefaehigung.daten(props.schuljahr);
				if (eintrag === null)
					continue;
				list.push({
					text: eintrag.text,
					action: async () => await props.addLehrbefaehigung({
						idLehramt: lehramt.id,
						idLehrbefaehigung: eintrag.id,
						idAnerkennungsgrund: null,
					}),
				});
			}
			result.put(lehramt.id, list);
		}
		return result;
	});

	const actionsFachrichtungenHinzufuegen = computed<JavaMap<number, Array<{ text: string; action: () => void | Promise<any>; }>>>(() => {
		const result = new HashMap<number, Array<{ text: string; action: () => void | Promise<any>; }>>();
		for (const lehramt of personaldaten().lehraemter) {
			const list = new Array<{ text: string; action: () => void | Promise<any>; }>();
			for (const fachrichtung of LehrerFachrichtung.data().getWerteBySchuljahr(props.schuljahr)) {
				const eintrag = fachrichtung.daten(props.schuljahr);
				if (eintrag === null)
					continue;
				list.push({
					text: eintrag.text,
					action: async () => await props.addFachrichtung({
						idLehramt: lehramt.id,
						idFachrichtung: eintrag.id,
						idAnerkennungsgrund: null,
					}),
				});
			}
			result.put(lehramt.id, list);
		}
		return result;
	});

	type GridDatenLehraemter = LehrerLehramtEintrag | LehrerLehrbefaehigungEintrag | LehrerFachrichtungEintrag
		| { type: 'lehrbefaehigung' | 'fachrichtung', id: number };

	const gridManager = new GridManager<string, GridDatenLehraemter, List<GridDatenLehraemter>>({
		daten: computed<List<GridDatenLehraemter>>(() => {
			const result = new ArrayList<GridDatenLehraemter>();
			for (const lehramt of personaldaten().lehraemter) {
				result.add(lehramt);
				for (const l of lehramt.lehrbefaehigungen)
					result.add(l);
				result.add({ type: 'lehrbefaehigung', id: lehramt.id });
				for (const f of lehramt.fachrichtungen)
					result.add(f);
				result.add({ type: 'fachrichtung', id: lehramt.id });
			}
			return result;
		}),
		getRowKey: row => {
			if (row instanceof LehrerLehramtEintrag)
				return "Lehramt_" + row.id;
			else if (row instanceof LehrerLehrbefaehigungEintrag)
				return "Lehrbefaehigung_" + row.id;
			else if (row instanceof LehrerFachrichtungEintrag)
				return "Fachrichtung_" + row.id;
			else if (row.type === 'lehrbefaehigung')
				return "Add_Lehrbefaehigung_" + row.id;
			return "Add_Fachrichtung_" + row.id;
		},
		columns: [
			{ kuerzel: "Indent", name: "Indent", width: "4rem", hideable: false },
			{ kuerzel: "Lehramt", name: "Lehramt", width: "28rem", hideable: false },
			{ kuerzel: "Anerkennungsgrund", name: "Anerkennungsgrund", width: "28rem", hideable: false },
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
