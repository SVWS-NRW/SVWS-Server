<template>
	<ui-table-grid v-if="gridManagerKursLehrer.daten.length !== 0" name="Zusätzliche Lehrkräfte" :manager="() => gridManagerKursLehrer">
		<template #header>
			<th class="text-left">Kürzel</th>
			<th class="text-left">Rufname</th>
			<th class="text-left">Nachname</th>
			<th>Wochenstunden</th>
			<th />
		</template>
		<template #default="{ row, index }">
			<td class="text-left">{{ kurseAuswahlState.lehrer.get(row.proxy.idLehrer)?.kuerzel ?? "-" }}</td>
			<td class="text-left">{{ kurseAuswahlState.lehrer.get(row.proxy.idLehrer)?.vorname ?? "-" }}</td>
			<td class="text-left">{{ kurseAuswahlState.lehrer.get(row.proxy.idLehrer)?.nachname ?? "-" }}</td>
			<td v-if="hatUpdateKompetenz" class="ui-table-grid-input" :ref="inputWochenstunden(row, index)" />
			<td v-else>{{ row.proxy.wochenstundenLehrer.toFixed(2).replace(".", ",") }}</td>
			<td class="pr-3 py-1">
				<ui-table-actions v-if="hatUpdateKompetenz" :actions="getRowActions(row)" />
			</td>
		</template>
		<template #footer v-if="hatUpdateKompetenz">
			<td class="col-span-full my-1 pr-3">
				<ui-table-actions :actions="footerActions" always-visible />
			</td>
		</template>
	</ui-table-grid>
	<div v-else>
		<svws-ui-button v-if="hatUpdateKompetenz" @click="addLehrer" type="secondary">Lehrkraft hinzufügen</svws-ui-button>
		<div v-else>Keine zusätzlichen Lehrkräfte zugeordnet.</div>
	</div>
	<svws-ui-modal :show="showModalKursLehrer" @update:show="closeModalKursLehrer">
		<template #modalTitle>Weitere Lehrkraft hinzufügen</template>
		<template #modalContent v-if="createModel !== null">
			<svws-ui-input-wrapper>
				<ui-select label="Lehrkraft"
					v-model="createModel.lehrer.value"
					:manager="lehrerFilteredSelectManager"
					required :removable="false" statistics />
				<svws-ui-input-number placeholder="Wochenstunden"
					v-model="createModel.proxy.wochenstundenLehrer"
					:validation="() => getFehlerWochenstunden()"
					statistics :min="0" required />
			</svws-ui-input-wrapper>
			<div class="mt-7 flex flex-row gap-4 justify-end">
				<svws-ui-button type="secondary" @click="closeModalKursLehrer">Abbrechen</svws-ui-button>
				<svws-ui-button @click="sendRequestKursLehrer" :disabled="createModel.hatBlockierendeFehler()">Speichern</svws-ui-button>
			</div>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">
	import type { ComponentPublicInstance } from "vue";
	import { computed, ref, shallowRef } from "vue";

	import { KursLehrer } from "@core/asd/data/kurse/KursLehrer";
	import type { ValidatorFehler } from "@core/asd/validate/ValidatorFehler";
	import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
	import { ArrayList } from "@core/java/util/ArrayList";
	import type { List } from "@core/java/util/List";
	import { useModelProxyList } from "@ui/model/useModelProxyList";
	import { SelectManager } from "@ui/ui/controls/select/manager/SelectManager";
	import { GridManager } from "@ui/ui/controls/tablegrid/GridManager";
	import type { TableAction } from "@ui/ui/controls/tablegrid/UiTableActions.vue";

	import { KursLehrerModelProxy } from "~/components/kurse/modelproxy/KursLehrerModelProxy";
	import { useKurseAuswahlState } from "~/states/kurse/KurseAuswahlState";

	const props = defineProps<{
		hatUpdateKompetenz: boolean;
	}>();

	const kurseAuswahlState = useKurseAuswahlState();

	const idKurs = computed<number>(() => kurseAuswahlState.stammdaten.datenOrException().id);

	// --- ModelProxies ---
	const kursLehrerModelProxies = useModelProxyList(
		() => kurseAuswahlState.stammdaten.datenOrException().weitereLehrer,
		(kursLehrer) => kursLehrer.idLehrer,
		(kursLehrer) => new KursLehrerModelProxy(
			() => kursLehrer,
			async (data: Partial<KursLehrer>) => {
				await kurseAuswahlState.stammdaten.patchLehrer(data, idKurs.value, kursLehrer.idLehrer);
				return true;
			}),
		{ deep: true }
	);

	// --- Grid ---

	const gridManagerKursLehrer = new GridManager<string, KursLehrerModelProxy, KursLehrerModelProxy[]>({
		daten: kursLehrerModelProxies,
		getRowKey: row => `kurslehrer-${row.proxy.idLehrer}`,
		columns: [
			{ kuerzel: "kuerzel", name: "Kürzel", width: "6rem", hideable: false },
			{ kuerzel: "vorname", name: "Rufname", width: "minmax(8rem,1fr)", hideable: false },
			{ kuerzel: "nachname", name: "Nachname", width: "minmax(8rem,1fr)", hideable: false },
			{ kuerzel: "wochenstunden", name: "Wochenstunden", width: "8rem", hideable: false },
			{ kuerzel: "buttons", name: "Buttons", width: "4rem", hideable: false },
		],
	});

	function updateWochenstunden(row: KursLehrerModelProxy, wochenstunden: number | null): void {
		if (wochenstunden === null) {
			return;
		}
		row.proxy.wochenstundenLehrer = wochenstunden;
		void row.patch();
	}

	function inputWochenstunden(row: KursLehrerModelProxy, index: number) {
		const key = `kurslehrer-${row.proxy.idLehrer}`;
		const setter = (value: number | null) => updateWochenstunden(row, value);
		return (element: Element | ComponentPublicInstance<unknown> | null) => {
			gridManagerKursLehrer.applyInputNumberFixed(key, 4, index, element, null, 2, setter);
			if (element !== null) {
				gridManagerKursLehrer.update(key, row.proxy.wochenstundenLehrer);
			}
		};
	}


	// --- Aktionen ---

	function getRowActions(row: KursLehrerModelProxy): TableAction[] {
		return [{ label: "Lehrkraft entfernen", action: () => void removeKursLehrer(row), trash: true }];
	}

	const footerActions = computed<TableAction[]>(() => [
		{ label: "Weitere Lehrkraft hinzufügen", action: addLehrer, iconClasses: "i-ri-add-line" },
	]);

	async function removeKursLehrer(row: KursLehrerModelProxy): Promise<void> {
		await kurseAuswahlState.stammdaten.removeLehrer(ArrayList.of(row.proxy.idLehrer), idKurs.value);
	}

	// --- Modal Hinzufügen ---
	const showModalKursLehrer = ref<boolean>(false);

	const lehrerFiltered = computed<LehrerListeEintrag[]>(() => {
		const idsAssignedLehrer = new Set<number>();
		for (const kl of kurseAuswahlState.stammdaten.datenOrException().weitereLehrer) {
			idsAssignedLehrer.add(kl.idLehrer);
		}
		const result = [];
		for (const l of kurseAuswahlState.lehrer.list()) {
			if (!idsAssignedLehrer.has(l.id) && l.istAktiv && l.istSichtbar) {
				result.push(l);
			}
		}
		return result;
	});

	const lehrerFilteredSelectManager = new SelectManager<LehrerListeEintrag>({
		options: lehrerFiltered,
		optionDisplayText: l => l.kuerzel + ' - ' + l.vorname + ' ' + l.nachname,
		selectionDisplayText: l => l.kuerzel + ' - ' + l.vorname + ' ' + l.nachname,
	});

	const createModel = shallowRef<KursLehrerModelProxy | null>(null);


	function createNewKursLehrerEntry(): KursLehrer {
		return Object.assign(new KursLehrer(), { wochenstundenLehrer: 0, idKurs: idKurs.value });
	}

	function addLehrer() {
		const eintrag = createNewKursLehrerEntry();
		createModel.value = new KursLehrerModelProxy((() => eintrag));
		showModalKursLehrer.value = true;
	}

	function closeModalKursLehrer() {
		showModalKursLehrer.value = false;
		createModel.value = null;
	}

	async function sendRequestKursLehrer() {
		if (createModel.value === null) {
			return;
		}
		await kurseAuswahlState.stammdaten.addLehrer(createModel.value.proxy, idKurs.value);
		closeModalKursLehrer();
	}

	function getFehlerWochenstunden(): List<ValidatorFehler> {
		return createModel.value?.getFehler("wochenstundenLehrer") ?? new ArrayList<ValidatorFehler>();
	}

</script>
