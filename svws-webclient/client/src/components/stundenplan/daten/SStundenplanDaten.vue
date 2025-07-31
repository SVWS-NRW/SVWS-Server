<template>
	<div class="page page-flex-row max-w-480">
		<div class="h-full overflow-y-auto w-full xl:w-1/2 flex flex-col gap-8 pr-4">
			<div class="content-card" v-if="manager().hasDaten()">
				<div class="content-card--header content-card--headline">Allgemein</div>
				<div class="content-card--content input-wrapper grid-cols-2">
					<div class="flex gap-1"><svws-ui-checkbox type="toggle" :disabled="(!manager().auswahl().aktiv && !manager().istKonfliktfreiZuAktivenStundenplaenen(gueltigData.gueltigAb, gueltigData.gueltigBis))" :model-value="manager().daten().isAktiv()" @update:model-value="handleChangeAktiv" />Stundenplan aktiv</div>
					<svws-ui-text-input class="contentFocusField" :disabled="!hatUpdateKompetenz" placeholder="Bezeichnung" :model-value="manager().daten().getBezeichnungStundenplan()" :valid="StundenplanListeManager.validateBezeichnung" @change="bezeichnungStundenplan=> bezeichnungStundenplan && patch({ bezeichnungStundenplan })" />
					<div v-if="hatUpdateKompetenz" :class="{'flex gap-2': showExtraWTM}">
						<svws-ui-select title="Wochentypmodell" :items="[0,2,3,4,5]" :item-text="i=> wochenTypModell[i] || ''" :model-value="manager().daten().getWochenTypModell()" @update:model-value="modell => doPatch(modell)" ref="select" />
						<svws-ui-input-number v-if="showExtraWTM" placeholder="Wochentypmodell" :model-value="manager().daten().getWochenTypModell() < 5 ? 5 : manager().daten().getWochenTypModell()" @change="modell => doPatch(modell)" :min="5" :max="100" />
					</div>
					<svws-ui-text-input :disabled="!hatUpdateKompetenz" placeholder="Gültig ab" :model-value="manager().daten().getGueltigAb()" :valid="validateGueltigAb" @change="handleChangeGueltigAb" type="date" :fehlerart="manager().auswahl().aktiv ? ValidatorFehlerart.MUSS : ValidatorFehlerart.HINWEIS" />
					<svws-ui-text-input :disabled="!hatUpdateKompetenz" placeholder="Gültig bis" :model-value="manager().daten().getGueltigBis()" :valid="validateGueltigBis" @change="handleChangeGueltigBis" type="date" :fehlerart="manager().auswahl().aktiv ? ValidatorFehlerart.MUSS : ValidatorFehlerart.HINWEIS" />
				</div>
			</div>
			<s-card-stundenplan-warnung-wochentypmodell v-if="wtmOK === false" :wochen-typ-modell="newWTM" @change="ok => (wtmOK = ok) && doPatch(newWTM)" :stundenplan-manager="() => manager().daten()" />
			<ui-card icon="i-ri-archive-line" title="Jahrgänge" :is-open="actionJahrgaenge" @update:is-open="(isOpen) => actionJahrgaenge = isOpen">
				<svws-ui-table :items="listJahrgaenge" :no-data="false" :columns="cols">
					<template #cell(id)="{value}">
						<svws-ui-checkbox v-if="hatUpdateKompetenz" type="toggle" :model-value="jahrgaenge.includes(value)" headless @update:model-value="updateJahrgaenge(value)" />
					</template>
				</svws-ui-table>
			</ui-card>
			<ui-card icon="i-ri-archive-line" title="Räume" :is-open="actionRaeume" @update:is-open="isOpen => actionRaeume = isOpen">
				<svws-ui-table :columns="colsRaeume" :items="listStundenplanRaeume" v-model:clicked="raum" :selectable="hatUpdateKompetenz" v-model="selectedRaeume" :count="listStundenplanRaeume.length > 0">
					<template #cell(kuerzel)="{ rowData }">
						<svws-ui-text-input :disabled="!hatUpdateKompetenz" :model-value="rowData.kuerzel" @change="kuerzel => (kuerzel !== null) && patchRaum({kuerzel}, rowData.id)" headless required />
					</template>
					<template #cell(groesse)="{ rowData }">
						<svws-ui-input-number :disabled="!hatUpdateKompetenz" :model-value="rowData.groesse" @change="groesse => (groesse !== null) && patchRaum({groesse}, rowData.id)" headless required />
					</template>
					<template #cell(beschreibung)="{ rowData }">
						<svws-ui-text-input :disabled="!hatUpdateKompetenz" :model-value="rowData.beschreibung" @change="beschreibung => (beschreibung !== null) && patchRaum({beschreibung}, rowData.id)" headless />
					</template>
					<template #actions v-if="hatUpdateKompetenz">
						<svws-ui-button @click="gotoKatalog('raeume')" type="transparent" title="Räume im Katalog bearbeiten"><span class="icon i-ri-link" /> Katalog bearbeiten</svws-ui-button>
						<s-card-stundenplan-import-raeume-modal v-slot="{ openModal }" :raeume-sync-to-stundenplan :raeume-sync-to-vorlage :list-raeume :manager>
							<svws-ui-button @click="openModal" type="transparent" title="Räume importieren"><span class="icon i-ri-archive-line" /> Aus Katalog importieren</svws-ui-button>
						</s-card-stundenplan-import-raeume-modal>
						<svws-ui-button @click="delRaeume" type="trash" :disabled="!selectedRaeume.length" />
						<s-card-stundenplan-add-raum-modal v-slot="{ openModal }" :add-raum="addRaum">
							<svws-ui-button @click="openModal" type="icon" title="Raum hinzufügen"> <span class="icon i-ri-add-line" /> </svws-ui-button>
						</s-card-stundenplan-add-raum-modal>
					</template>
				</svws-ui-table>
			</ui-card>
		</div>
		<div class="h-full overflow-y-auto w-full xl:w-1/2 flex flex-col gap-8 pr-4">
			<ui-card icon="i-ri-archive-line" title="Pausenzeiten" :is-open="actionPausenzeiten" @update:is-open="isOpen => actionPausenzeiten = isOpen">
				<div class="flex flex-col gap-4">
					<svws-ui-table :columns="colsPausenzeiten" :items="pausenzeitenSorted" v-model:clicked="zeit" :selectable="hatUpdateKompetenz" v-model="selectedPausenzeiten" :count="pausenzeitenSorted.length > 0" v-model:sort-by-and-order="sortByAndOrder">
						<template #cell(wochentag)="{ rowData }">
							<svws-ui-select :disabled="!hatUpdateKompetenz" :model-value="Wochentag.fromIDorException(rowData.wochentag)" @update:model-value="wochentag => patchPausenzeit({wochentag: Number(wochentag?.id || -1)}, rowData.id)" :items="Wochentag.values()" :item-text="i=>i.beschreibung" headless />
						</template>
						<template #cell(beginn)="{ rowData }">
							<svws-ui-text-input :disabled="!hatUpdateKompetenz" :model-value="DateUtils.getStringOfUhrzeitFromMinuten(rowData.beginn ?? 0)" @change="beginn => (beginn !== null) && patchPausenBeginn(beginn, rowData.id)" headless />
						</template>
						<template #cell(ende)="{ rowData }">
							<svws-ui-text-input :disabled="!hatUpdateKompetenz" :model-value="DateUtils.getStringOfUhrzeitFromMinuten(rowData.ende ?? 0)" @change="ende => (ende !== null) && patchPausenEnde(ende, rowData.id)" headless />
						</template>
						<template #cell(klassen)="{ rowData }">
							<svws-ui-multi-select :disabled="!hatUpdateKompetenz" :model-value="[...rowData.klassen].sort()" @update:model-value="klassen => patchPausenKlassen(klassen, rowData.id)" title="Klassen" :items="[...manager().daten().klasseGetMengeAsList()].map(k=>k.id)" :item-text="klasse => manager().daten().klasseGetByIdOrException(klasse).kuerzel" headless />
						</template>
						<template #actions v-if="hatUpdateKompetenz">
							<svws-ui-button @click="gotoKatalog('pausenzeiten')" type="transparent" title="Pausenzeiten im Katalog bearbeiten"><span class="icon i-ri-link" /> Katalog bearbeiten</svws-ui-button>
							<s-card-stundenplan-import-pausenzeiten-modal v-slot="{ openModal }" :list-pausenzeiten :manager :pausenzeiten-sync-to-vorlage :pausenzeiten-sync-to-stundenplan>
								<svws-ui-button @click="openModal" type="transparent" title="Pausenzeiten importieren"><span class="icon i-ri-archive-line" /> Aus Katalog importieren</svws-ui-button>
							</s-card-stundenplan-import-pausenzeiten-modal>
							<svws-ui-button @click="delPausenzeiten" type="trash" :disabled="!selectedPausenzeiten.length" />
							<s-pausenzeit-neu-modal v-slot="{ openModal }" :add-pausenzeiten :stundenplan-manager="() => manager().daten()">
								<svws-ui-button @click="openModal" type="icon" title="Pausenzeit hinzufügen"> <span class="icon i-ri-add-line" /> </svws-ui-button>
							</s-pausenzeit-neu-modal>
						</template>
					</svws-ui-table>
					<ui-card icon="i-ri-add-line" title="Alle Pausenzeiten erstellen" :is-open="subActionPausenzeiten" @update:is-open="isOpen => subActionPausenzeiten = isOpen">
						<stundenplan-zeitraster-einstellungen :manager="() => manager().daten()" :set-settings-defaults>
							<svws-ui-button type="secondary" @click="addBlock" title="Alle Pausenzeiten erstellen">
								<span class="icon i-ri-calendar-event-line" />
								<span class="icon i-ri-add-line -ml-1" />Alle Pausenzeiten erstellen
							</svws-ui-button>
						</stundenplan-zeitraster-einstellungen>
					</ui-card>
				</div>
			</ui-card>
			<ui-card icon="i-ri-archive-line" title="Aufsichtsbereiche" :is-open="actionAufsichtsbereiche" @update:is-open="isOpen => actionAufsichtsbereiche = isOpen">
				<svws-ui-table :columns="colsAufsichtsbereiche" :items="listStundenplanAufsichtsbereiche" v-model:clicked="bereich" :selectable="hatUpdateKompetenz" v-model="selectedAufsichtsbereiche" :count="listAufsichtsbereiche.length > 0">
					<template #cell(kuerzel)="{ rowData }">
						<svws-ui-text-input :disabled="!hatUpdateKompetenz" :model-value="rowData.kuerzel" @change="kuerzel => (kuerzel !== null) && patchAufsichtsbereich({kuerzel}, rowData.id)" headless />
					</template>
					<template #cell(beschreibung)="{ rowData }">
						<svws-ui-text-input :disabled="!hatUpdateKompetenz" :model-value="rowData.beschreibung" @change="beschreibung => (beschreibung !== null) && patchAufsichtsbereich({beschreibung}, rowData.id)" headless />
					</template>
					<template #actions v-if="hatUpdateKompetenz">
						<svws-ui-button @click="gotoKatalog('aufsichtsbereiche')" type="transparent" title="Aufsichtsbereiche im Katalog bearbeiten"><span class="icon i-ri-link" /> Katalog bearbeiten</svws-ui-button>
						<s-card-stundenplan-import-aufsichtsbereiche-modal v-slot="{ openModal }" :list-aufsichtsbereiche :manager :aufsichtsbereiche-sync-to-vorlage :aufsichtsbereiche-sync-to-stundenplan>
							<svws-ui-button @click="openModal" type="transparent" title="Aufsichtsbereiche importieren"><span class="icon i-ri-archive-line" /> Aus Katalog importieren</svws-ui-button>
						</s-card-stundenplan-import-aufsichtsbereiche-modal>
						<svws-ui-button @click="delAufsichtsbereiche" type="trash" :disabled="!selectedAufsichtsbereiche.length" />
						<s-card-stundenplan-add-aufsichtsbereich-modal v-slot="{ openModal }" :add-aufsichtsbereich>
							<svws-ui-button @click="openModal" type="icon" title="Aufsichtsbereich hinzufügen"> <span class="icon i-ri-add-line" /> </svws-ui-button>
						</s-card-stundenplan-add-aufsichtsbereich-modal>
					</template>
				</svws-ui-table>
			</ui-card>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed, onMounted, ref, watch } from "vue";
	import type { ComponentExposed } from "vue-component-type-helpers";
	import type { StundenplanDatenProps } from "./SStundenplanDatenProps";
	import type { DataTableColumn, SortByAndOrder } from "@ui";
	import { SvwsUiSelect, StundenplanListeManager } from "@ui";
	import type { StundenplanRaum, StundenplanAufsichtsbereich, StundenplanPausenzeit, Stundenplan } from "@core";
	import { ArrayList, BenutzerKompetenz, DateUtils, Wochentag, ValidatorFehlerart } from "@core";

	const props = defineProps<StundenplanDatenProps>();
	const select = ref<ComponentExposed<typeof SvwsUiSelect<typeof wochenTypModell>>>();
	const subActionPausenzeiten = ref<boolean>(false);

	const gueltigData = ref<{gueltigAb: string , gueltigBis: string, aktiv: boolean }>({ gueltigAb: "", gueltigBis: "", aktiv: false });
	const validAb = ref<boolean>(true);
	const validBis = ref<boolean>(true);

	onMounted(() => {
		watch(() => props.manager().auswahl(), () => {
			gueltigData.value = {
				gueltigAb: props.manager().auswahl().gueltigAb,
				gueltigBis: props.manager().auswahl().gueltigBis,
				aktiv: props.manager().auswahl().aktiv,
			};
		}, { immediate: true, deep: false });
	})

	const cols: DataTableColumn[] = [
		{ key: "kuerzel", label: "Jahrgang", sortable: true, defaultSort: "asc", span: 0.25 },
		{ key: "bezeichnung", label: "Bezeichnung", sortable: true },
		{ key: "id", label: "Gültig", fixedWidth: 5, align: "center" },
	];

	const actionJahrgaenge = ref<boolean>(false);
	const actionRaeume = ref<boolean>(false);
	const actionPausenzeiten = ref<boolean>(false);
	const actionAufsichtsbereiche = ref<boolean>(false);

	const hatUpdateKompetenz = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.STUNDENPLAN_AENDERN));

	const jahrgaenge = computed(() => {
		const list = props.manager().daten().jahrgangGetMengeAsList();
		const a = [];
		for (const j of list)
			a.push(j.id);
		return a;
	})

	async function updateJahrgaenge(id: number) {
		if (jahrgaenge.value.includes(id))
			await props.removeJahrgang(id);
		else
			await props.addJahrgang(id);
	}

	function validateGueltigAb(gueltigAb: string | null): boolean {
		validAb.value = props.manager().validateGueltigAb(gueltigAb, gueltigData.value.gueltigBis, gueltigData.value.aktiv, gueltigData.value.aktiv);
		return props.manager().validateGueltigAb(gueltigAb, gueltigData.value.gueltigBis, true, props.manager().validateGueltigBis(gueltigData.value.gueltigAb, gueltigData.value.gueltigBis, true));
	}

	function validateGueltigBis(gueltigBis: string | null): boolean {
		validBis.value = props.manager().validateGueltigBis(gueltigData.value.gueltigAb, gueltigBis, gueltigData.value.aktiv);
		return props.manager().validateGueltigBis(gueltigData.value.gueltigAb, gueltigBis, true);
	}

	async function handleChangeAktiv(aktiv: boolean) {
		gueltigData.value.aktiv = aktiv;
		const patch: Partial<Stundenplan> = { aktiv };
		if (aktiv === false) {
			if (props.manager().validateGueltigBis(gueltigData.value.gueltigAb, gueltigData.value.gueltigBis, false))
				patch.gueltigBis = gueltigData.value.gueltigBis;
			if (props.manager().validateGueltigAb(gueltigData.value.gueltigAb, gueltigData.value.gueltigBis, false, false))
				patch.gueltigAb = gueltigData.value.gueltigAb;
		}
		await props.patch(patch);
	}

	async function handleChangeGueltigAb(gueltigAb: string | null) {
		if (gueltigAb === null)
			return;
		gueltigData.value.gueltigAb = gueltigAb;
		if (validAb.value === true && validBis.value === true)
			await props.patch({ gueltigAb, gueltigBis: gueltigData.value.gueltigBis });
	}

	async function handleChangeGueltigBis(gueltigBis: string | null) {
		if (gueltigBis === null)
			return;
		gueltigData.value.gueltigBis = gueltigBis;
		if (validAb.value === true && validBis.value === true)
			await props.patch({ gueltigAb: gueltigData.value.gueltigAb, gueltigBis });
	}

	async function doPatch(wochenTypModell: number | null | undefined) {
		if ((wochenTypModell === null) || (wochenTypModell === undefined) || (wochenTypModell === 1))
			return;
		if (((props.manager().daten().stundenplanGetWochenTypModellSimulation(wochenTypModell) === 0)) || (wtmOK.value === true)) {
			await props.patch({wochenTypModell});
			// wtmOK.value === undefined;
			newWTM.value = -1;
		}
		else if (wtmOK.value === false) {
			select.value?.reset(true);
			wtmOK.value = undefined;
			newWTM.value = -1;
		} else {
			newWTM.value = wochenTypModell;
			wtmOK.value = false;
		}
	}

	const wochenTypModell = ['keins', null, 'AB-Wochen', 'ABC-Wochen', 'ABCD-Wochen', 'weitere'];
	const showExtraWTM = computed(() => props.manager().daten().getWochenTypModell() > 4);
	const wtmOK = ref<boolean | undefined>(undefined);
	const newWTM = ref<number>(-1);

	const raum = ref<StundenplanRaum | undefined>();
	const selectedRaeume = ref<StundenplanRaum[]>([]);
	const listStundenplanRaeume = computed(() => [...props.manager().daten().raumGetMengeAsList()]);
	const listStundenplanAufsichtsbereiche = computed(() => props.manager().daten().aufsichtsbereichGetMengeAsList());

	async function delRaeume() {
		await props.removeRaeume(selectedRaeume.value);
		selectedRaeume.value = [];
	}

	const colsRaeume = [
		{key: 'kuerzel', label: 'Kürzel', span: 1},
		{key: 'beschreibung', label: 'Beschreibung', span: 3},
		{key: 'groesse', label: 'Größe', span: 1},
	]

	const zeit = ref<StundenplanPausenzeit | undefined>();
	const selectedPausenzeiten = ref<StundenplanPausenzeit[]>([]);

	const itemsPausenzeit = computed(() => [...props.manager().daten().pausenzeitGetMengeAsList()]);

	const colsPausenzeiten = [
		{key: 'wochentag', label: 'Wochentag', span: 1, sortable: true },
		{key: 'beginn', label: 'Beginn', span: 1, sortable: true },
		{key: 'ende', label: 'Ende', span: 1},
		{key: 'klassen', label: 'Nur in Klassen', span: 2},
	]

	async function patchPausenBeginn(minuten: string, id: number) {
		const beginn = DateUtils.gibMinutenOfZeitAsString(minuten);
		await props.patchPausenzeit({beginn}, id);
	}

	async function patchPausenEnde(minuten: string, id: number) {
		const ende = DateUtils.gibMinutenOfZeitAsString(minuten);
		await props.patchPausenzeit({ende}, id);
	}

	async function patchPausenKlassen(ids: number[], id: number) {
		const klassen = new ArrayList<number>();
		for (const klassenID of ids)
			klassen.add(klassenID);
		await props.patchPausenzeit({klassen}, id);
	}

	async function delPausenzeiten() {
		await props.removePausenzeiten(selectedPausenzeiten.value);
		selectedPausenzeiten.value = [];
	}

	async function addBlock() {
		const list = props.manager().daten().pausenzeitGetDummyListe(props.manager().daten().zeitrasterGetWochentagMin(), props.manager().daten().zeitrasterGetWochentagMax());
		await props.addPausenzeiten(list);
	}

	const listPausenzeitenRest = computed(() => {
		const moeglich = new ArrayList<StundenplanPausenzeit>();
		for (const e of props.listPausenzeiten())
			if (!props.manager().daten().pausenzeitExistsByWochentagAndBeginnAndEnde(e.wochentag, e.beginn, e.ende))
				moeglich.add(e);
		return moeglich;
	})

	const sortByAndOrder = ref<SortByAndOrder | undefined>();

	const pausenzeitenSorted = computed(() => {
		const temp = sortByAndOrder.value;
		if (temp === undefined)
			return itemsPausenzeit.value;
		const arr = [...itemsPausenzeit.value];
		arr.sort((a, b) => {
			switch (temp.key) {
				case 'beginn':
					return (a.beginn ?? 0) - (b.beginn ?? 0);
				case 'wochentag':
					return a.wochentag - b.wochentag;
				default:
					return 0;
			}
		})
		return temp.order === true ? arr : arr.reverse();
	})

	const bereich = ref<StundenplanAufsichtsbereich | undefined>();
	const selectedAufsichtsbereiche = ref<StundenplanAufsichtsbereich[]>([]);

	const colsAufsichtsbereiche = [
		{key: 'kuerzel', label: 'Kürzel', span: 1},
		{key: 'beschreibung', label: 'Beschreibung', span: 3},
	]

	async function delAufsichtsbereiche() {
		await props.removeAufsichtsbereiche(selectedAufsichtsbereiche.value);
		selectedAufsichtsbereiche.value = [];
	}

</script>
