<template>
	<div v-if="manager().hasDaten()" class="page">
		<div class="h-full overflow-y-auto w-full flex flex-col gap-8">
			<svws-ui-content-card>
				<template #title>
					<div class="content-card--header"><h3 class="content-card--headline" title="Allgemein">Allgemein</h3>&nbsp;<svws-ui-badge type="light" title="ID" class="font-mono" size="big"> ID: {{ manager().auswahl().id }} </svws-ui-badge></div>
				</template>
				<div class="flex gap-1"><svws-ui-checkbox :readonly type="toggle" :disabled="loading || (!manager().auswahl().aktiv && (!validVon || !validBis || !manager().istKonfliktfreiZuAktivenPlanungsabschnitten(gueltigData.gueltigVon, gueltigData.gueltigBis)))" :model-value="manager().auswahl().aktiv" @update:model-value="handleChangeAktiv" />Planungsabschnitt aktiv <span v-if="validateGueltigVon(gueltigData.gueltigVon) && validateGueltigBis(gueltigData.gueltigBis) && !manager().istKonfliktfreiZuAktivenPlanungsabschnitten(gueltigData.gueltigVon, gueltigData.gueltigBis, false)" class="text-ui-caution"><span class="icon icon-ui-caution i-ri-alert-line" /> Konflikt mit anderem Planungsabschnitt</span></div>
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input class="contentFocusField" span="full" :readonly :disabled="loading" placeholder="Bezeichnung" :model-value="manager().auswahl().beschreibung" :valid="UvPlanungsabschnitteListeManager.validateBezeichnung" @change="handleChangeBezeichnung" />
					<div class="col-span-full grid min-w-0 grid-cols-2 gap-4">
						<svws-ui-text-input :readonly :disabled="loading" placeholder="Gültig ab" :model-value="manager().auswahl().gueltigVon" :valid="validateGueltigVon" @change="handleChangeGueltigVon" type="date" :fehlerart="manager().auswahl().aktiv ? ValidatorFehlerart.MUSS : ValidatorFehlerart.HINWEIS" />
						<svws-ui-text-input :readonly :disabled="loading" placeholder="Gültig bis" :model-value="manager().auswahl().gueltigBis" :valid="validateGueltigBis" @change="handleChangeGueltigBis" type="date" :fehlerart="manager().auswahl().aktiv ? ValidatorFehlerart.MUSS : ValidatorFehlerart.HINWEIS" />
					</div>
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-content-card title="Zugeordnete Zeitraster">
				<p v-if="listZeitraster.length === 0" class="text-ui-secondary">Es gibt kein passendes Zeitraster. Unter Grunddaten → Zeitraster kann eines angelegt oder dessen Gültigkeitszeitraum angepasst werden.</p>
				<svws-ui-table v-else :items="listZeitraster" :columns clickable :clicked="selectedZeitraster" @update:clicked="handleZeitrasterClick">
					<template #cell(bezeichnung)="{ value, rowData }">
						{{ value }} <svws-ui-button type="icon" title="Zeitraster öffnen" @click="gotoZeitraster(rowData.id)"><span class="icon i-ri-link" /></svws-ui-button>
					</template>
					<template #cell(zugeordnet)="{ rowData }">
						<svws-ui-checkbox :readonly type="toggle" :model-value="zeitrasterIsZugeordnet(rowData)" @update:model-value="(value: boolean) => handleZeitrasterZuordnung(rowData.id, value)" />
					</template>
					<template #cell(jahrgaenge)="{ rowData }">
						<svws-ui-multi-select :readonly :disabled="loading" v-if="zeitrasterIsZugeordnet(rowData)" label="Jahrgänge" @update:model-value="ids => patchZeitrasterJahrgaenge(rowData.id, ids)" :model-value="[...state.uvManager.jahrgangsdatenGetMengeByPlanungsabschnittAndZeitraster(manager().auswahl(), rowData)]" :items="state.uvManager.jahrgangsdatenGetMenge()" :item-text="item => item.kuerzel ?? ''" headless />
					</template>
				</svws-ui-table>
			</svws-ui-content-card>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed, onMounted, ref, watch } from "vue";

	import { ValidatorFehlerart } from "@core/asd/validate/ValidatorFehlerart";
	import type { JahrgangsDaten } from "@core/core/data/jahrgang/JahrgangsDaten";
	import type { UvPlanungsabschnitt } from "@core/core/data/uv/UvPlanungsabschnitt";
	import type { UvZeitraster } from "@core/core/data/uv/UvZeitraster";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { useUvState } from "@ui/states/UvState";
	import { UvPlanungsabschnitteListeManager } from "@ui/ui/manager/unterrichtsverteilung/UvPlanungsabschnitteListeManager";

	const props = defineProps<{
		benutzerKompetenzen: Set<BenutzerKompetenz>;
		manager: () => UvPlanungsabschnitteListeManager;
		patch: (data: Partial<UvPlanungsabschnitt>) => Promise<boolean>;
		gotoZeitraster: (id: number) => Promise<void>;
	}>();
	const state = useUvState();
	const manager = () => props.manager();

	const isMounted = ref(false);
	onMounted(() => isMounted.value = true);

	const readonly = computed<boolean>(() => !(props.benutzerKompetenzen.has(BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ALLGEMEIN_AENDERN)));

	const loading = ref(false);
	function canChange(): boolean {
		if (loading.value || readonly.value || state.planungsabschnitt === null) {
			return false;
		}
		return true;
	}

	async function savePatch(data: Partial<UvPlanungsabschnitt>): Promise<void> {
		if (!canChange()) {
			return;
		}
		loading.value = true;
		try {
			await props.patch(data);
		} finally {
			loading.value = false;
		}
	}

	const gueltigData = ref<{ gueltigVon: string, gueltigBis: string | null, aktiv: boolean }>({ gueltigVon: "", gueltigBis: "", aktiv: false });
	const validVon = ref<boolean>(true);
	const validBis = ref<boolean>(true);
	const selectedZeitraster = ref<UvZeitraster | undefined>(undefined);

	const listZeitraster = computed<UvZeitraster[]>(() => [...state.uvManager.zeitrasterGetMengeGueltigByPlanungsabschnitt(manager().auswahl())]);

	const columns = [
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true, defaultSort: "asc" as const },
		{ key: "zugeordnet", label: "Zugeordnet", sortable: false },
		{ key: "jahrgaenge", label: "Jahrgänge", sortable: false },
	];

	function zeitrasterIsZugeordnet(zeitraster: UvZeitraster): boolean {
		const planungsabschnitt = state.planungsabschnitt;
		if (planungsabschnitt === null) {
			return false;
		}
		return state.uvManager.zeitrasterIsInPlanungsabschnitt(zeitraster, planungsabschnitt);
	}

	onMounted(() => {
		watch(() => manager().auswahl(), () => {
			gueltigData.value = {
				gueltigVon: manager().auswahl().gueltigVon,
				gueltigBis: manager().auswahl().gueltigBis,
				aktiv: manager().auswahl().aktiv,
			};
		}, { immediate: true, deep: false });
	});

	function validateGueltigVon(gueltigVon: string | null): boolean {
		validVon.value = manager().validateGueltigVon(gueltigVon, gueltigData.value.gueltigBis, gueltigData.value.aktiv, false, false);
		return manager().validateGueltigVon(gueltigVon, gueltigData.value.gueltigBis, gueltigData.value.aktiv, true, false);
	}

	function validateGueltigBis(gueltigBis: string | null): boolean {
		validBis.value = manager().validateGueltigBis(gueltigData.value.gueltigVon, gueltigBis, gueltigData.value.aktiv, false, false);
		return manager().validateGueltigBis(gueltigData.value.gueltigVon, gueltigBis, gueltigData.value.aktiv, true, false);
	}

	async function handleChangeAktiv(aktiv: boolean) {
		gueltigData.value.aktiv = aktiv;
		const patch: Partial<UvPlanungsabschnitt> = { aktiv };
		if (!aktiv) {
			if (manager().validateGueltigBis(gueltigData.value.gueltigVon, gueltigData.value.gueltigBis, aktiv, false, false)) {
				patch.gueltigBis = gueltigData.value.gueltigBis;
			}
			if (manager().validateGueltigVon(gueltigData.value.gueltigVon, gueltigData.value.gueltigBis, aktiv, false, false)) {
				patch.gueltigVon = gueltigData.value.gueltigVon;
			}
		}
		await savePatch(patch);
	}

	async function handleChangeBezeichnung(beschreibung: string | null) {
		if (beschreibung !== null && beschreibung !== '') {
			await savePatch({ beschreibung });
		}
	}

	async function handleChangeGueltigVon(gueltigVon: string | null) {
		if (gueltigVon === null || gueltigVon === gueltigData.value.gueltigVon) {
			return;
		}
		gueltigData.value.gueltigVon = gueltigVon;
		if (validVon.value === true && validBis.value === true) {
			await savePatch({ gueltigVon, gueltigBis: gueltigData.value.gueltigBis });
		}
	}

	async function handleChangeGueltigBis(gueltigBis: string | null) {
		if (gueltigBis === null || gueltigBis === gueltigData.value.gueltigBis) {
			return;
		}
		gueltigData.value.gueltigBis = gueltigBis;
		if (validVon.value === true && validBis.value === true) {
			await savePatch({ gueltigVon: gueltigData.value.gueltigVon, gueltigBis });
		}
	}

	function handleZeitrasterClick(zeitraster: UvZeitraster | undefined) {
		selectedZeitraster.value = zeitraster;
	}

	async function handleZeitrasterZuordnung(idZeitraster: number, zugeordnet: boolean) {
		if (!canChange()) {
			return;
		}
		loading.value = true;
		try {
			if (zugeordnet) {
				await state.addZeitrasterZuordnung(idZeitraster);
			} else {
				await state.removeZeitrasterZuordnung(idZeitraster);
			}
		} finally {
			loading.value = false;
		}
	}

	async function patchZeitrasterJahrgaenge(idZeitraster: number, jahrgaenge: JahrgangsDaten[]) {
		if (!canChange()) {
			return;
		}
		loading.value = true;
		try {
			const zuordnung = state.uvManager.planungsabschnittZeitrasterGetByIdOrException(manager().auswahl().id, idZeitraster);
			await state.patchZeitrasterZuordnung(zuordnung, jahrgaenge);
		} finally {
			loading.value = false;
		}
	}

</script>
