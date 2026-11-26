<template>
	<div v-if="kurseAuswahlState.stammdaten.hasDaten" class="page page-grid-cards">
		<div class="flex flex-col gap-y-16 lg:gap-y-20">
			<svws-ui-content-card title="Allgemein">
				<template v-if="zeigeAlles" #actions>
					<svws-ui-checkbox v-model="model.proxy.istSichtbar" :readonly focus-class-content>Ist sichtbar</svws-ui-checkbox>
				</template>
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="Kürzel"
						v-model="model.proxy.kuerzel"
						:validation="() => model.getFehler('kuerzel')"
						@change="model.patch"
						:max-len="20" required :readonly />
					<ui-select label="Fach"
						v-model="model.fach.value"
						:manager="fachSelectManager"
						:validation="() => model.getFehler('idFach')"
						:readonly statistics :removable="false" required />
					<ui-select label="Kursart"
						v-model="model.kursartAllg.value"
						:manager="kursartSelectManager"
						:validation="() => model.getFehler('kursartAllg')"
						:readonly :removable="false" required statistics />
					<svws-ui-input-number placeholder="Wochenstunden des Kurses"
						v-model="model.proxy.wochenstunden"
						:validation="() => model.getFehler('wochenstunden')"
						@change="model.patch"
						:readonly statistics :min="0" :max="40" required />
					<ui-select-multi label="Jahrgänge"
						v-model="model.jahrgaenge.value"
						:manager="jahrgaengeSelectManager"
						statistics :readonly />
					<svws-ui-text-input placeholder="Zeugnisbezeichnung"
						v-model="model.proxy.bezeichnungZeugnis"
						:validation="() => model.getFehler('bezeichnungZeugnis')"
						@change="model.patch"
						:max-len="130" :readonly />
					<ui-select label="Fortschreibungsart"
						v-model="model.fortschreibungsart.value"
						:manager="fortschreibungsartManager"
						:validation="() => model.getFehler('idKursFortschreibungsart')"
						:readonly required :removable="false" />
					<ui-select-multi label="Schienen"
						v-model="model.schienenArray.value"
						:manager="schienenSelectManager"
						:readonly />
					<svws-ui-input-number placeholder="Sortierung"
						v-model="model.proxy.sortierung"
						:validation="() => model.getFehler('sortierung')"
						@change="model.patch"
						:min="0" :readonly required />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-content-card title="Lehrkraft">
				<svws-ui-input-wrapper :grid="2">
					<ui-select label="Lehrkraft" :readonly
						v-model="model.lehrer.value"
						:manager="lehrerSelectManager"
						statistics />
					<svws-ui-input-number placeholder="Wochenstunden der Lehrkraft"
						v-model="model.proxy.wochenstundenLehrer"
						:validation="() => model.getFehler('wochenstundenLehrer')"
						@change="model.patch"
						statistics :min="0" required :readonly
						:disabled="model.proxy.idLehrer === null" />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-content-card title="Zusätzliche Lehrkräfte">
				<kurs-daten-weitere-lehrer :hat-update-kompetenz="hatKompetenzUpdate" />
			</svws-ui-content-card>
		</div>
		<svws-ui-content-card title="Kursliste">
			<ui-select-multi label="Status"
				v-model="filterSchuelerStatus"
				:manager="schuelerStatusSelectManager"
				class="col-span-full" />
			<svws-ui-table :columns="colsSchueler" :items="model.schuelerListe.value">
				<template #cell(status)="{ value }: { value: number}">
					<span :class="{'opacity-25': value === 2}">{{ SchuelerStatus.data().getWertByID(value)?.daten(schuljahr)?.text || "—" }}</span>
				</template>
				<template #header(linkToSchueler)>
					<span class="icon i-ri-group-line" />
				</template>
				<template #cell(linkToSchueler)="{ rowData }">
					<button type="button" @click.stop="kurseAuswahlState.gotoSchueler(rowData)" class="button button--icon" title="Schüler ansehen">
						<span class="icon i-ri-link" />
					</button>
				</template>
			</svws-ui-table>
		</svws-ui-content-card>
	</div>
	<div v-else>
		<span class="icon i-ri-presentation-line" />
	</div>
</template>

<script setup lang="ts">

	import { computed } from "vue";

	import { SchuelerStatus } from "@core/asd/types/schueler/SchuelerStatus";
	import type { FachDaten } from "@core/core/data/fach/FachDaten";
	import type { JahrgangsDaten } from "@core/core/data/jahrgang/JahrgangsDaten";
	import type { LehrerListeEintrag } from "@core/core/data/lehrer/LehrerListeEintrag";
	import { BenutzerKompetenz } from "@core/core/types/benutzer/BenutzerKompetenz";
	import { KursFortschreibungsart } from "@core/core/types/KursFortschreibungsart";
	import { ArrayList } from "@core/java/util/ArrayList";
	import { useBenutzerState } from "@ui/states/BenutzerState";
	import { useSchuleState } from "@ui/states/SchuleState";
	import type { DataTableColumn } from "@ui/types";
	import { SelectManager } from "@ui/ui/controls/select/manager/SelectManager";

	import { KursModelProxy } from "~/components/kurse/modelproxy/KursModelProxy";
	import { useKurseAuswahlState } from "~/states/kurse/KurseAuswahlState";
	import { getKursarten } from "~/utils/helfer";

	import type { KursDatenProps } from "./KursDatenProps";

	const props = defineProps<KursDatenProps>();
	const benutzerState = useBenutzerState();
	const schuleState = useSchuleState();
	const kurseAuswahlState = useKurseAuswahlState();

	const readonly = computed(() => !hatKompetenzUpdate.value);

	const schuljahr = computed<number>(() => schuleState.schuljahr);

	const hatKompetenzUpdate = computed<boolean>(() => benutzerState.benutzerHatKompetenz(BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ALLGEMEIN_AENDERN));


	const model = new KursModelProxy(() => kurseAuswahlState.stammdaten.datenOrException(), (patchData) => kurseAuswahlState.stammdaten.patch(patchData));

	const kursarten = computed<Map<string, string>>(() => getKursarten(schuljahr.value));

	// --- manager ---

	const kursartSelectManager = new SelectManager({
		options: computed(() => [...kursarten.value.keys()]),
		optionDisplayText: k => `${k} (${kursarten.value.get(k) ?? '???'})`,
		selectionDisplayText: k => `${k} (${kursarten.value.get(k) ?? '???'})`,
	});

	const fachSelectManager = new SelectManager({
		options: computed(() => {
			const result = new ArrayList<FachDaten>();
			for (const f of kurseAuswahlState.faecher.list()) {
				if (f.istSichtbar) {
					result.add(f);
				}
			}
			return result;
		}),
		optionDisplayText: fach => `${fach.kuerzel} (${fach.bezeichnung})`,
		selectionDisplayText: fach => `${fach.kuerzel} (${fach.bezeichnung})`,
	});

	const jahrgaengeSelectManager = new SelectManager<JahrgangsDaten>({
		options: computed(() => kurseAuswahlState.jahrgaenge.list()),
		optionDisplayText: jg => jg.kuerzel ?? '',
		selectionDisplayText: jg => jg.kuerzel ?? '',
	});

	const schuelerStatusSelectManager = new SelectManager<SchuelerStatus>({
		options: computed(() => kurseAuswahlState.schuelerstatus.options),
		optionDisplayText: status => status.daten(schuljahr.value)?.text ?? '—',
		selectionDisplayText: status => status.daten(schuljahr.value)?.text ?? '—',
	});

	const schienenSelectManager = new SelectManager<number>({
		options: Array.from({ length: 40 }, (_, i) => i + 1),
		optionDisplayText: i => 'Schiene ' + i,
		selectionDisplayText: i => 'Schiene ' + i,
		sort: (a, b) => a - b,
	});

	const fortschreibungsartManager = new SelectManager<KursFortschreibungsart>({
		options: computed(() => KursFortschreibungsart.values()),
		optionDisplayText: v => v.beschreibung,
		selectionDisplayText: v => v.beschreibung,
	});

	const lehrerSelectManager = new SelectManager<LehrerListeEintrag>({
		options: computed(() => {
			const result = new ArrayList<LehrerListeEintrag>();
			for (const l of kurseAuswahlState.lehrer.list()) {
				if (l.istAktiv && l.istSichtbar) {
					result.add(l);
				}
			}
			return result;
		}),
		optionDisplayText: l => lehrerText(l),
		selectionDisplayText: l => lehrerText(l),
	});

	function lehrerText(l: LehrerListeEintrag): string {
		return `${l.kuerzel} - ${l.vorname} ${l.nachname}${l.istAktiv ? '' : ' – inaktiv!'}`;
	}

	const colsSchueler: DataTableColumn[] = [
		{ key: "linkToSchueler", label: " ", fixedWidth: 1.75, align: "center" },
		{ key: "nachname", label: "Nachname", sortable: true },
		{ key: "vorname", label: "Rufname", sortable: true },
		{ key: "status", label: "Status", sortable: true, span: 0.5 },
	];

	const filterSchuelerStatus = computed<SchuelerStatus[]>({
		get: () => [...kurseAuswahlState.schuelerstatus.values],
		set: (value) => kurseAuswahlState.schuelerstatus.setValues(value),
	});
</script>
