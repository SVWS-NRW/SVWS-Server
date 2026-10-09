<template>
	<ui-table-grid name="Unterrichtsfächer" v-if="gridManager.daten.length !== 0" :manager="() => gridManager" hide-selection>
		<template #header>
			<th class="text-left">Fach</th>
			<th class="text-center">Sek I</th>
			<th class="text-center">Sek II</th>
			<th class="text-left">Bemerkung</th>
			<th />
		</template>
		<template #default="{ row }">
			<td class="text-left">
				{{ getFachText(row.proxy) }}
			</td>
			<td class="flex items-center justify-center">
				<svws-ui-checkbox v-if="hatUpdateKompetenz" v-model="row.proxy.istSek1" />
				<span v-else>{{ row.proxy.istSek1 ? 'Ja' : 'Nein' }}</span>
			</td>
			<td class="flex items-center justify-center">
				<svws-ui-checkbox v-if="hatUpdateKompetenz" v-model="row.proxy.istSek2" />
				<span v-else>{{ row.proxy.istSek2 ? 'Ja' : 'Nein' }}</span>
			</td>
			<td class="text-left">
				<svws-ui-text-input v-if="hatUpdateKompetenz"
					v-model="row.proxy.bemerkung"
					:validation="() => row.getFehler('bemerkung')"
					@change="row.patch"
					headless />
				<span v-else>{{ row.proxy.bemerkung ?? '' }}</span>
			</td>
			<td class="pr-3">
				<ui-table-actions :actions="rowActions(row)" />
			</td>
		</template>
		<template #footer>
			<template v-if="hatUpdateKompetenz">
				<td class="col-span-full my-1 pr-3">
					<ui-table-actions :actions="footerActions" always-visible />
				</td>
			</template>
			<template v-else>
				<td class="col-span-5" />
			</template>
		</template>
	</ui-table-grid>
	<div v-else>
		<svws-ui-button v-if="hatUpdateKompetenz" @click="openHinzufuegen" type="secondary">Fach hinzufügen</svws-ui-button>
		<div v-else>Keine Unterrichtsfächer zugeordnet.</div>
	</div>
	<svws-ui-modal v-if="createUnterrichtsfachModel !== null" v-model:show="showHinzufuegen" size="small" class="hidden">
		<template #modalTitle> Unterrichtsfach hinzufügen </template>
		<template #modalContent>
			<ui-select label="Fach"
				v-model="createUnterrichtsfachModel.unterrichtsfach.value"
				:manager="fachSelectManager"
				:validation="() => createUnterrichtsfachModel?.getFehler('idFach') ?? new ArrayList()"
				required :removable="false" />
			<div class="mt-4 text-left">
				<span class="text-headline-sm mb-2 block">wird unterrichtet in</span>
				<div class="flex gap-4">
					<svws-ui-checkbox v-model="createUnterrichtsfachModel.proxy.istSek1"> Sekundarstufe I </svws-ui-checkbox>
					<svws-ui-checkbox v-model="createUnterrichtsfachModel.proxy.istSek2"> Sekundarstufe II </svws-ui-checkbox>
				</div>
			</div>
			<svws-ui-text-input placeholder="Bemerkung"
				v-model="createUnterrichtsfachModel.proxy.bemerkung"
				:validation="() => createUnterrichtsfachModel?.getFehler('bemerkung') ?? new ArrayList()"
				:max-len="255" />
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="showHinzufuegen = false"> Abbrechen </svws-ui-button>
			<svws-ui-button :disabled="createUnterrichtsfachModel.hatBlockierendeFehler()"
				@click="createLehrerUnterrichtsfach">
				Anlegen
			</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { computed, ref, shallowRef } from "vue";

	import type { FachDaten } from "@core/core/data/fach/FachDaten";
	import { LehrerUnterrichtsfach } from "@core/core/data/lehrer/LehrerUnterrichtsfach";
	import { ArrayList } from "@core/java/util/ArrayList";
	import { HashSet } from "@core/java/util/HashSet";
	import { useModelProxyList } from "@ui/model/useModelProxyList";
	import { SelectManager } from "@ui/ui/controls/select/manager/SelectManager";
	import { GridManager } from "@ui/ui/controls/tablegrid/GridManager";
	import type { TableAction } from "@ui/ui/controls/tablegrid/UiTableActions.vue";

	import { useLehrerAuswahlState } from "~/states/lehrer/LehrerAuswahlState";

	import { LehrerUnterrichtsfachModelProxy } from "./modelproxy/LehrerUnterrichtsfachModelProxy";

	const props = defineProps<{
		hatUpdateKompetenz: boolean;
	}>();

	const lehrerAuswahlState = useLehrerAuswahlState();

	const createUnterrichtsfachModel = shallowRef<LehrerUnterrichtsfachModelProxy | null>(null);

	const faecherModels = useModelProxyList(
		() => lehrerAuswahlState.lehrerUnterrichtsfaecher,
		(lehrerUnterrichtsfach) => lehrerUnterrichtsfach.id,
		(lehrerUnterrichtsfach) => new LehrerUnterrichtsfachModelProxy(() => lehrerUnterrichtsfach,
			lehrerAuswahlState.mapFaecher,
			(data: Partial<LehrerUnterrichtsfach>) => lehrerAuswahlState.patchLehrerUnterrichtsfach(lehrerUnterrichtsfach, data)),
		{ deep: true }
	);

	const gridManager = new GridManager<string, LehrerUnterrichtsfachModelProxy, LehrerUnterrichtsfachModelProxy[]>({
		daten: faecherModels,
		getRowKey: row => `fach-${row.data.id}`,
		columns: [
			{ kuerzel: "Fach", name: "Fach", width: "minmax(30%,20rem)", hideable: false },
			{ kuerzel: "Sek1", name: "Sek I", width: "5rem", hideable: false },
			{ kuerzel: "Sek2", name: "Sek II", width: "5rem", hideable: false },
			{ kuerzel: "Bemerkung", name: "Bemerkung", width: "minmax(30%,20rem)", hideable: false },
			{ kuerzel: "Buttons", name: "Buttons", width: "4rem", hideable: false },
		],
	});

	const faecherVorhanden = computed(() => {
		const vorhanden = new HashSet<number>();
		for (const fach of lehrerAuswahlState.lehrerUnterrichtsfaecher) {
			vorhanden.add(fach.idFach);
		}
		return vorhanden;
	});

	const faecherVerfuegbar = computed<FachDaten[]>(() => {
		const result: FachDaten[] = [];
		for (const fach of lehrerAuswahlState.mapFaecher.values()) {
			if (!faecherVorhanden.value.contains(fach.id)) {
				result.push(fach);
			}
		}
		return result;
	});

	function rowActions(fachModel: LehrerUnterrichtsfachModelProxy): TableAction[] {
		return [{ label: "Fach Löschen", action: () => lehrerAuswahlState.removeLehrerUnterrichtsfach(fachModel.data), trash: true }];
	}

	const footerActions = computed(() => {
		return [
			{ label: "Fach hinzufügen", action: openHinzufuegen, iconClasses: "i-ri-add-line" },
		];
	});

	const fachDisplayText = (f: FachDaten) => `${f.kuerzel} - ${f.bezeichnung}`;
	const fachSelectManager = new SelectManager<FachDaten>({
		options: faecherVerfuegbar,
		optionDisplayText: fachDisplayText,
		selectionDisplayText: fachDisplayText,
		sort: (a, b) => a.kuerzel.localeCompare(b.kuerzel),
	});

	function getFachText(eintrag: LehrerUnterrichtsfach): string {
		const fach = lehrerAuswahlState.mapFaecher.get(eintrag.idFach);
		return fach ? `${fach.kuerzel} - ${fach.bezeichnung}` : '—';
	}

	const showHinzufuegen = ref<boolean>(false);

	function openHinzufuegen() {
		createUnterrichtsfachModel.value = new LehrerUnterrichtsfachModelProxy(() => new LehrerUnterrichtsfach(), lehrerAuswahlState.mapFaecher);
		showHinzufuegen.value = true;
	}

	async function createLehrerUnterrichtsfach() {
		if (createUnterrichtsfachModel.value === null || faecherVorhanden.value.contains(createUnterrichtsfachModel.value.proxy.idFach)) {
			return;
		}
		const { istSek1, istSek2, idFach, bemerkung } = createUnterrichtsfachModel.value.proxy;
		await lehrerAuswahlState.addLehrerUnterrichtsfach({ istSek1, istSek2, idFach, bemerkung });

		showHinzufuegen.value = false;
		createUnterrichtsfachModel.value = null;
	}

</script>
