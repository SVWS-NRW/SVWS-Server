<template>
	<div v-if="manager().hasDaten()" class="page page-grid-cards">
		<div class="flex flex-col gap-y-16 lg:gap-y-20">
			<svws-ui-content-card title="Allgemein">
				<template #actions>
					<svws-ui-checkbox v-model="istSichtbar" :readonly focus-class-content> Ist sichtbar </svws-ui-checkbox>
				</template>
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-text-input placeholder="Kürzel" :readonly :model-value="data().kuerzel" @change="kuerzel => patch({ kuerzel: kuerzel ?? '' })" type="text" />
					<svws-ui-select title="Fach" :readonly v-model="fach" :items="manager().faecher.list()"
						:item-text="f => f.kuerzel + ' (' + f.bezeichnung + ')'" statistics />
					<svws-ui-select title="Kursart" :readonly :items="kursarten.keys()" :item-text="k => k + ' (' + (kursarten.get(k) ?? '???') + ')'"
						:model-value="data().kursartAllg" @update:model-value="value => patch({ kursartAllg: value ?? '' })" statistics />
					<svws-ui-input-number placeholder="Wochenstunden des Kurses" :readonly :model-value="data().wochenstunden" statistics
						@change="v => patch({ wochenstunden: ((v !== null) && (v >= 0)) ? v : data().wochenstunden })" :min="0" />
					<svws-ui-multi-select title="Jahrgänge" :readonly v-model="jahrgaenge" :items="jahrgangsListe"
						:item-text="jg => jg?.kuerzel ?? ''" statistics />
					<svws-ui-text-input placeholder="Zeugnisbezeichnung" :readonly :model-value="data().bezeichnungZeugnis" @change="b => patch({ bezeichnungZeugnis : b })" type="text" />
					<svws-ui-select title="Fortschreibungsart" :readonly :model-value="KursFortschreibungsart.fromID(data().idKursFortschreibungsart)"
						@update:model-value="value => patch({ idKursFortschreibungsart: value?.id ?? 0 })"
						:items="KursFortschreibungsart.values()" :item-text="f => f.beschreibung" />
					<svws-ui-multi-select title="Schienen" :readonly v-model="schienen" :items="Array.from({length: 40}, (_, i) => i + 1)" :item-text="s => 'Schiene ' + s" />
					<svws-ui-input-number placeholder="Sortierung" :readonly :model-value="data().sortierung" :min="0" :max="32000"
						@change="sortierung=> sortierung && patch({ sortierung })" />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-content-card title="Lehrkraft">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-select title="Lehrkraft" :readonly v-model="lehrer" :items="lehrerAktiv" :item-text="getLehrerText"
						:empty-text="() => '---'" removable statistics />
					<svws-ui-input-number placeholder="Wochenstunden der Lehrkraft" :readonly :model-value="data().wochenstundenLehrer" statistics :min="0"
						@change="v => patch({ wochenstundenLehrer: ((v !== null) && (v >= 0)) ? v : data().wochenstundenLehrer })" />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<svws-ui-content-card title="zusätzliche Lehrkräfte" v-if="serverMode === ServerMode.DEV">
				<svws-ui-table clickable @update:clicked="v => patchLehrer(v)" :columns="columnsKursLehrer" :items="weitereLehrer" :readonly
					v-model="auswahlKursLehrer" :selectable="hatKompetenzUpdate">
					<template #cell(kuerzel)="{ rowData: s }">
						<span>{{ manager().lehrer.get(s.idLehrer ?? -1)?.kuerzel ?? "-" }}</span>
					</template>
					<template #cell(vorname)="{ rowData: s }">
						<span>{{ manager().lehrer.get(s.idLehrer ?? -1)?.vorname ?? "-" }}</span>
					</template>
					<template #cell(nachname)="{ rowData: s }">
						<span>{{ manager().lehrer.get(s.idLehrer ?? -1)?.nachname ?? "-" }}</span>
					</template>
					<template #cell(wochenstunden)="{ rowData: s }">
						<span>{{ s.wochenstundenLehrer }}</span>
					</template>
					<template #actions v-if="hatKompetenzUpdate">
						<svws-ui-button @click="deleteAuswahlKursLehrer" type="trash" :disabled="(auswahlKursLehrer.length === 0) || !hatKompetenzUpdate" />
						<svws-ui-button @click="addLehrer" :readonly type="icon" title="weitere Lehrkraft hinzufügen">
							<span class="icon i-ri-add-line" />
						</svws-ui-button>
					</template>
				</svws-ui-table>
				<svws-ui-modal :show="showModalKursLehrer" @update:show="closeModalKursLehrer">
					<template v-if="currentMode === Mode.ADD" #modalTitle>Weitere Lehrkraft hinzufügen</template>
					<template v-else-if="currentMode === Mode.PATCH" #modalTitle>Wochenstunden bearbeiten</template>
					<template #modalContent>
						<svws-ui-select v-if="currentMode === Mode.ADD" title="Lehrkraft" :items="lehrerFiltered" :item-text="getLehrerText" removable
							required @update:model-value="v => newEntryKursLehrer.idLehrer = v?.id ?? -1" statistics
							:readonly :model-value="manager().lehrer.get(newEntryKursLehrer.idLehrer ?? -1)" />
						<svws-ui-input-number placeholder="Wochenstunden" :readonly v-model="newEntryKursLehrer.wochenstundenLehrer" statistics :min="0" />
						<div class="mt-7 flex flex-row gap-4 justify end">
							<svws-ui-button type="secondary" @click="closeModalKursLehrer">Abbrechen</svws-ui-button>
							<svws-ui-button @click="sendRequestKursLehrer(currentMode)" :disabled="!newEntryKursLehrer.idLehrer || !hatKompetenzUpdate">
								Speichern
							</svws-ui-button>
						</div>
					</template>
				</svws-ui-modal>
			</svws-ui-content-card>
		</div>
		<svws-ui-content-card title="Kursliste">
			<svws-ui-multi-select v-model="filterSchuelerStatus" title="Status" :items="manager().schuelerstatus.list()" :item-text="status => status.daten(schuljahr)?.text ?? '—'" class="col-span-full" />
			<svws-ui-table :columns="colsSchueler" :items="manager().getSchuelerListe()">
				<template #cell(status)="{ value }: { value: number}">
					<span :class="{'opacity-25': value === 2}">{{ SchuelerStatus.data().getWertByID(value)?.daten(schuljahr)?.text || "—" }}</span>
				</template>
				<template #header(linkToSchueler)>
					<span class="icon i-ri-group-line" />
				</template>
				<template #cell(linkToSchueler)="{ rowData }">
					<button type="button" @click.stop="gotoSchueler(rowData)" class="button button--icon" title="Schüler ansehen">
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

	import { computed, ref } from "vue";
	import type { DataTableColumn } from "@ui";
	import type { KursDatenProps } from "./SKursDatenProps";
	import type { JahrgangsDaten, LehrerListeEintrag, List } from "@core";
	import { SchuelerStatus, ZulaessigeKursart, KursFortschreibungsart, ArrayList, BenutzerKompetenz, ServerMode, FaecherListeEintrag, KursLehrer } from "@core";

	const props = defineProps<KursDatenProps>();
	const readonly = computed(() => !hatKompetenzUpdate.value);

	const schuljahr = computed<number>(() => props.manager().getSchuljahr());

	// TODO auch UNTERRICHTSVERTEILUNG_PLANUNG_ANSEHEN verwenden und hier unterscheiden zu UNTERRICHTSVERTEILUNG_ANSEHEN
	const hatKompetenzAnsehen = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ANSEHEN));
	// TODO auch UNTERRICHTSVERTEILUNG_FUNKTIONSBEZOGEN_AENDERN berücksichtigen in Bezug auf Abteilungsleitungen / Koordinationen (API muss dafür noch erweitert werden)
	const hatKompetenzUpdate = computed<boolean>(() => props.benutzerKompetenzen.has(BenutzerKompetenz.UNTERRICHTSVERTEILUNG_ALLGEMEIN_AENDERN));

	const data = () => props.manager().daten();
	const idKurs = computed<number>(() => props.manager().daten().id);

	const lehrer = computed<LehrerListeEintrag | null>({
		get: () => {
			const idLehrer = data().lehrer;
			return (idLehrer === null) ? null : props.manager().lehrer.get(idLehrer);
		},
		set: (value) => void props.patch({ lehrer: value?.id ?? null }),
	});

	const lehrerAktiv = computed<List<LehrerListeEintrag>>(() => {
		const result = new ArrayList<LehrerListeEintrag>();
		for (const l of props.manager().lehrer.list())
			if (l.istAktiv)
				result.add(l);
		return result;
	})

	function getLehrerText(lehrer : LehrerListeEintrag) : string {
		return lehrer.kuerzel + ' - ' + lehrer.vorname + ' ' + lehrer.nachname;
	}

	const fach = computed<FaecherListeEintrag>({
		get: () => props.manager().faecher.get(data().idFach) ?? new FaecherListeEintrag(),
		set: (value) => void props.patch({ idFach: value.id }),
	});


	const jahrgangsListe = computed<List<JahrgangsDaten>>(() => {
		const result = new ArrayList<JahrgangsDaten>();
		for (const jg of props.manager().jahrgaenge.list())
			result.add(jg);
		return result;
	});


	const jahrgaenge = computed<JahrgangsDaten[]>({
		get: () => {
			const arr = [];
			for (const id of data().idJahrgaenge) {
				const e = props.manager().jahrgaenge.get(id);
				if (e !== null)
					arr.push(e);
			}
			return arr;
		},
		set: (value) => {
			const result = new ArrayList<number>();
			value.forEach(j => result.add(j.id));
			void props.patch({ idJahrgaenge: result });
		},
	});

	const schienen = computed<number[]>({
		get: () => {
			return data().schienen.toArray(new Array<number>);
		},
		set: (value) => {
			const result = new ArrayList<number>();
			let changed = false;
			for (const s of value) {
				if (!data().schienen.contains(s))
					changed = true;
				result.add(s);
			}
			if (!changed)
				changed = (data().schienen.size() !== result.size());
			if (changed)
				void props.patch({ schienen : result });
		},
	});

	const kursarten = computed<Map<string, string>>(() => {
		const arten = new Map<string, string>();
		for (const art of ZulaessigeKursart.data().getWerteBySchuljahr(schuljahr.value)) {
			const daten = art.daten(schuljahr.value);
			if (daten === null)
				continue;
			if (daten.kuerzel === "PUK")
				continue;
			if ((daten.kuerzelAllg !== null) && (daten.bezeichnungAllg !== null))
				arten.set(daten.kuerzelAllg, daten.bezeichnungAllg);
			else
				arten.set(daten.kuerzel, daten.text);
			if (daten.kuerzelAllg === "DK")
				arten.set(daten.kuerzel, daten.text);
		}
		return new Map([...arten.entries()].sort());
	});

	const istSichtbar = computed<boolean>({
		get: () => data().istSichtbar,
		set: (value) => void props.patch({ istSichtbar: value }),
	});

	const colsSchueler: DataTableColumn[] = [
		{ key: "linkToSchueler", label: " ", fixedWidth: 1.75, align: "center" },
		{ key: "nachname", label: "Nachname", sortable: true },
		{ key: "vorname", label: "Vorname", sortable: true },
		{ key: "status", label: "Status", sortable: true, span: 0.5 },
	];

	const filterSchuelerStatus = computed<SchuelerStatus[]>({
		get: () => [...props.manager().schuelerstatus.auswahl()],
		set: (value) => {
			props.manager().schuelerstatus.auswahlClear();
			for (const v of value)
				props.manager().schuelerstatus.auswahlAdd(v);
			void props.setFilter();
		},
	});

	// --- Tabelle Kurslehrer ---
	const weitereLehrer = computed(() => [...props.manager().daten().weitereLehrer]);
	const columnsKursLehrer: DataTableColumn[] = [
		{key: "kuerzel", label: "Kürzel", sortable: true},
		{key: "vorname", label: "Vorname", sortable: true},
		{key: "nachname", label: "Nachname", sortable: true},
		{key: "wochenstundenLehrer", label: "Wochenstunden", sortable: true},
	];

	// --- delete ---
	const auswahlKursLehrer = ref<KursLehrer[]>([]);
	async function deleteAuswahlKursLehrer() {
		if (auswahlKursLehrer.value.length === 0)
			return;
		const ids = new ArrayList<number>();
		for (const k of auswahlKursLehrer.value)
			ids.add(k.idLehrer);
		await props.deleteKursLehrer(ids,idKurs.value);
		auswahlKursLehrer.value = [];
	}

	// --- ADD/PATCH Modal KursLehrer ---

	const showModalKursLehrer = ref<boolean>(false);
	const lehrerFiltered = computed<LehrerListeEintrag[]>(() => {
		const idsAssignedLehrer = new Set<number>();
		for (const kl of weitereLehrer.value)
			idsAssignedLehrer.add(kl.idLehrer);
		const result = [];
		for (const l of props.manager().lehrer.list())
			if (!idsAssignedLehrer.has(l.id) && (l.istAktiv))
				result.push(l);
		return result;
	});
	const newEntryKursLehrer = ref<KursLehrer>(createNewKursLehrerEntry());

	function createNewKursLehrerEntry() {
		return Object.assign(new KursLehrer(), { wochenstundenLehrer: 0, idKurs: idKurs.value });
	}

	function openModalKursLehrer() {
		showModalKursLehrer.value = true;
	}

	function closeModalKursLehrer() {
		resetKursLehrer();
		setMode(Mode.DEFAULT);
		showModalKursLehrer.value = false;
	}

	function resetKursLehrer() {
		newEntryKursLehrer.value = createNewKursLehrerEntry();
	}

	function addLehrer() {
		resetKursLehrer();
		setMode(Mode.ADD);
		openModalKursLehrer();
	}

	function patchLehrer(kursLehrer: KursLehrer) {
		resetKursLehrer();
		setMode(Mode.PATCH);
		newEntryKursLehrer.value.idLehrer = kursLehrer.idLehrer;
		newEntryKursLehrer.value.idKurs = kursLehrer.idKurs;
		newEntryKursLehrer.value.wochenstundenLehrer = kursLehrer.wochenstundenLehrer;
		openModalKursLehrer();
	}

	async function sendRequestKursLehrer(type: Mode) {
		if (type === Mode.ADD) {
			if (newEntryKursLehrer.value.wochenstundenLehrer < 0)
				newEntryKursLehrer.value.wochenstundenLehrer = 0;
			await props.addKursLehrer(newEntryKursLehrer.value, idKurs.value);
		}
		if ((type === Mode.PATCH) && (newEntryKursLehrer.value.wochenstundenLehrer >= 0))
			await props.patchKursLehrer({ wochenstundenLehrer: newEntryKursLehrer.value.wochenstundenLehrer }, idKurs.value, newEntryKursLehrer.value.idLehrer);
		enterDefaultMode();
	}

	// --- Mode ---
	enum Mode { ADD, PATCH , DEFAULT };
	const currentMode = ref<Mode>(Mode.DEFAULT);
	function setMode(newMode: Mode) {
		currentMode.value = newMode;
	}

	function enterDefaultMode() {
		setMode(Mode.DEFAULT);
		resetKursLehrer();
		closeModalKursLehrer();
	}

</script>
