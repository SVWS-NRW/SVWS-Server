<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal :show="showModal" size="medium" class="hidden h-full overflow-none" no-scroll>
		<template #modalTitle>Kurs-Schüler-Zuordnung</template>
		<template #modalContent>
			<div class="flex flex-row gap-8 h-full overflow-y-hidden">
				<!-- Die Tabelle mit den Kursschülern -->
				<div class="flex flex-col w-96 overflow-y-hidden">
					<div class="flex flex-row w-full place-content-center">
						<span class="text-headline-sm pb-2 pr-2">im Kurs</span>
						<span class="w-32">
							<svws-ui-select :items="schuelerFilter().getKurse()" :item-text="kurs => getErgebnismanager().getOfKursName(kurs.id)" :model-value="schuelerFilter().kurs || setKurs()" @update:model-value="kurs => schuelerFilter().kurs = kurs ?? undefined" headless />
						</span>
					</div>
					<svws-ui-table :items="schuelerFilter().filtered.value" :columns="[{key: 'pin', label: 'Fixierung aller Kurs-Schüler', fixedWidth: 2 }, {key: 'name', label: 'Name'}]"
						:no-data="schuelerFilter().filtered.value.length <= 0" scroll>
						<template #header(pin)>
							<div v-if="apiStatus.pending">
								<svws-ui-spinner spinning />
							</div>
							<div v-else @click="toggleFixierRegelAlleKursSchueler">
								<template v-if="kursSchuelerFixierungen === true">
									<i-ri-pushpin-fill class="w-5 -my-0.5" :class="{ 'hover:opacity-50': allowRegeln }" />
								</template>
								<template v-else-if="kursSchuelerFixierungen === null">
									<i-ri-pushpin-line class="w-5 -my-0.5" :class="{ 'hover:opacity-50': allowRegeln }" />
								</template>
								<template v-else-if="allowRegeln">
									<i-ri-pushpin-line class="w-5 -my-0.5 opacity-0 hover:opacity-75" />
								</template>
								<template v-else>
									&nbsp;
								</template>
							</div>
						</template>
						<template #cell(pin)="{ rowData }">
							<div @click="toggleFixierRegelKursSchueler(schuelerFilter().kurs?.id ?? null, rowData.id)">
								<template v-if="hatFixierRegelKurs(rowData.id).value">
									<i-ri-pushpin-fill class="w-5 -my-0.5" :class="{ 'hover:opacity-50': allowRegeln }" />
								</template>
								<template v-else-if="allowRegeln">
									<i-ri-pushpin-line class="w-5 -my-0.5 opacity-0 hover:opacity-75" />
								</template>
							</div>
						</template>
						<template #cell(name)="{ rowData }">
							<div @click="remove(rowData.id)" class="w-full text-left"
								:class="(schuelerFilter().kurs !== undefined) && (props.getDatenmanager().schuelerGetIstFixiertInKurs(rowData.id, schuelerFilter().kurs!.id)) ? [] : [ 'cursor-pointer', 'hover:font-bold' ]">
								{{ rowData.nachname }}, {{ rowData.vorname }}
							</div>
						</template>
						<template #actions>
							<svws-ui-button type="transparent" @click="leereKurs" :disabled="(schuelerFilter().filtered.value.length === 0) || (apiStatus.pending)">
								<i-ri-delete-bin-line /> Kurs leeren
							</svws-ui-button>
						</template>
					</svws-ui-table>
				</div>
				<!-- Die Tabelle mit den Schülern gleicher Fachwahl, aber nicht in diesem Kurs -->
				<div class="flex flex-col w-128 overflow-y-hidden">
					<span class="text-headline-sm pb-2">mit Fachwahl {{ fachname }} {{ kursart }}</span>
					<svws-ui-table :items="fachwahlschueler" :columns="[{key: 'pin', label: 'Fixierung', fixedWidth: 2 }, {key: 'name', label: 'Name'}, {key: 'kurs', label: 'andere Kurszuordnung'}]"
						:no-data="fachwahlschueler.length <= 0" scroll>
						<template #header(pin)="{ }">
							&nbsp;
						</template>
						<template #cell(pin)="{ rowData }">
							<div @click="toggleFixierRegelKursSchueler(andererKurs(rowData.id).value?.id ?? null, rowData.id)">
								<template v-if="hatFixierRegelAndererKurs(rowData.id).value">
									<i-ri-pushpin-fill class="w-5 -my-0.5" :class="{ 'hover:opacity-50': allowRegeln }" />
								</template>
								<template v-else-if="allowRegeln && (getKurs(rowData) !== undefined)">
									<i-ri-pushpin-line class="w-5 -my-0.5 opacity-0 hover:opacity-75" />
								</template>
							</div>
						</template>
						<template #cell(name)="{ rowData }">
							<div @click="move(rowData.id)" class="w-full text-left"
								:class="(andererKurs(rowData.id).value !== null) && (props.getDatenmanager().schuelerGetIstFixiertInKurs(rowData.id, andererKurs(rowData.id).value!.id)) ? [] : [ 'cursor-pointer', 'hover:font-bold' ]">
								{{ rowData.nachname }}, {{ rowData.vorname }}
							</div>
						</template>
						<template #cell(kurs)="{ rowData }">
							<svws-ui-select :key="rowData.id" title="Anderer Kurs" :items="kurse" :item-text="getKursBezeichnung"
								:model-value="getKurs(rowData)" @update:model-value="value => updateZuordnung(rowData, value)"
								class="w-full" headless removable />
						</template>
					</svws-ui-table>
				</div>
			</div>
			<!-- Der untere Teil des Modals mit den Gruppenaktionen -->
			<div class="flex flex-col items-center border-t mt-4 pt-4 border-b pb-4 mb-4">
				<div class="text-left">
					Übertragen der Kurs-Schüler-Zuordnung des aktuellen Kurses auf andere Kurse. Dabei werden die Schüler nur
					übertragen, wenn sie auch die entsprechende Fachwahl des jeweiligen Ziel-Kurses haben.
				</div>
				<div class="flex gap-2 m-2">
					<div class="mt-4 min-w-72 mb-4 flex flex-col gap-2">
						<svws-ui-multi-select title="Andere Kurse" :items="listeKurseZurUbertragung" :item-text="getKursBezeichnung"
							v-model="_kurseZurUebertragung" class="w-full" removable :disabled="apiStatus.pending" />
						<div class="text-center">
							<svws-ui-button type="secondary" @click="uebertragen" :disabled="(_kurseZurUebertragung.length === 0) || apiStatus.pending"><svws-ui-spinner v-if="apiStatus.pending" spinning /> Schülermenge übertragen</svws-ui-button>
						</div>
					</div>
					<div class="flex flex-col gap-2">
						<svws-ui-checkbox :model-value="fixierteVerschieben()" @update:model-value="setFixierteVerschieben">auch fixierte Schüler verschieben {{ allowRegeln ? 'und Fixierung anpassen':'' }}</svws-ui-checkbox>
						<svws-ui-checkbox v-if="allowRegeln" :model-value="inZielkursFixieren()" @update:model-value="setInZielkursFixieren">Schüler in Ziel-Kursen fixieren</svws-ui-checkbox>
						<svws-ui-checkbox v-model="zielkurseLeeren">Zielkurse leeren</svws-ui-checkbox>
						<svws-ui-checkbox v-model="regelnAufheben">auch Regeln in Zielkursen aufheben</svws-ui-checkbox>
					</div>
				</div>
			</div>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="showModal().value = false">Schließen</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { computed, ref } from 'vue';
	import type { GostKursplanungSchuelerFilter } from './GostKursplanungSchuelerFilter';
	import type { ApiStatus } from '~/components/ApiStatus';
	import type { GostBlockungsergebnisManager, Schueler, List, GostBlockungsdatenManager } from '@core';
	import { ArrayList, GostBlockungsergebnisKursSchuelerZuordnung, GostKursart, GostBlockungsergebnisKurs, GostBlockungKurs, GostBlockungsergebnisKursSchuelerZuordnungUpdate, GostBlockungRegelUpdate, SetUtils, HashSet, DTOUtils } from '@core';

	const props = defineProps<{
		updateKursSchuelerZuordnungen: (update: GostBlockungsergebnisKursSchuelerZuordnungUpdate) => Promise<boolean>;
		regelnUpdate: (update: GostBlockungRegelUpdate) => Promise<void>;
		allowRegeln: boolean;
		getDatenmanager: () => GostBlockungsdatenManager;
		getErgebnismanager: () => GostBlockungsergebnisManager;
		schuelerFilter: () => GostKursplanungSchuelerFilter;
		fixierteVerschieben: () => boolean;
		setFixierteVerschieben: (value: boolean) => Promise<void>;
		inZielkursFixieren: () => boolean;
		setInZielkursFixieren: (value: boolean) => Promise<void>;
		apiStatus: ApiStatus;
	}>();

	const _showModal = ref<boolean>(false);
	const showModal = () => _showModal;

	const _kurseZurUebertragung = ref<GostBlockungsergebnisKurs[]>([]);

	const zielkurseLeeren = ref<boolean>(false);
	const regelnAufheben = ref<boolean>(false);

	function setKurs() {
		const filter = props.schuelerFilter();
		if (filter.kurs === undefined) {
			if (filter.fach === undefined) {
				const kurse = props.getErgebnismanager().getKursmenge();
				const k = (kurse.size() > 0) ? kurse.getFirst() : null;
				if (k === null)
					filter.kurs = new GostBlockungKurs();
				else filter.kurs = props.getErgebnismanager().getKursG(k.id);
			} else {
				const kurse = props.getErgebnismanager().getOfFachKursmenge(filter.fach);
				const k = (kurse.size() > 0) ? kurse.getFirst() : null;
				if (k === null) {
					filter.fach = undefined;
					return setKurs();
				}
				else filter.kurs = props.getErgebnismanager().getKursG(k.id);
			}
			return filter.kurs;
		}
	}

	const kursart = computed(() => {
		const kurs = props.schuelerFilter().kurs;
		if (kurs === undefined)
			return '—';
		const kursart = GostKursart.fromIDorNull(kurs.kursart);
		return kursart?.kuerzel ?? "—";
	});

	const kurse = computed<List<GostBlockungsergebnisKurs>>(() => {
		const kurs = props.schuelerFilter().kurs;
		if (kurs === undefined)
			return new ArrayList<GostBlockungsergebnisKurs>();
		const fachart = GostKursart.getFachartID(kurs.fach_id, kurs.kursart);
		return props.getErgebnismanager().getOfFachartKursmenge(fachart);
	});

	const listeKurseZurUbertragung = computed<List<GostBlockungsergebnisKurs>>(() => {
		const result = new ArrayList<GostBlockungsergebnisKurs>();
		const kurs = props.schuelerFilter().kurs;
		if (kurs === undefined)
			return result;
		// Erstelle einen Filter für das Fach, welches im Modal angezeigt wird und aller Fächer, von denen bereits Kurse für die Übertragung ausgewöhlt wurden
		const setFachFilter = new Set<number>();
		const ausgewaehlteKurse = new Set<number>();
		setFachFilter.add(kurs.fach_id);
		for (const k of _kurseZurUebertragung.value) {
			setFachFilter.add(k.fachID);
			ausgewaehlteKurse.add(k.id);
		}
		// Erstelle die Liste aller Kurse, auf die noch übertragen werden kann, dass heißt Kurse aller Fächer, die nicht im zuvor erstellten Set sind
		const alleKurse = props.getErgebnismanager().getKursmenge();
		for (const k of alleKurse) {
			if (!setFachFilter.has(k.fachID) || ausgewaehlteKurse.has(k.id))
				result.add(k);
		}
		return result;
	});

	const fachname = computed(() => {
		const kurs = props.schuelerFilter().kurs;
		if (kurs === undefined)
			return '—';
		const fach = props.getErgebnismanager().getFach(kurs.fach_id);
		return fach.bezeichnung || '—';
	});

	const fachwahlschueler = computed(() => {
		const kurswahlschueler = props.schuelerFilter().filtered.value;
		const arr: Schueler[] = [];
		if (kurswahlschueler === undefined)
			return arr;
		const liste = new Set();
		for (const s of kurswahlschueler)
			liste.add(s.id);
		const kurs = props.schuelerFilter().kurs;
		if (kurs === undefined)
			return arr;
		const fachwahlen = props.getErgebnismanager().getOfSchuelerMengeGefiltert(-1, kurs.fach_id, kurs.kursart, 0, "");
		for (const w of fachwahlen)
			if (!liste.has(w.id))
				arr.push(w);
		return arr;
	})

	async function remove(id: number) {
		const kurs = props.schuelerFilter().kurs;
		if (kurs === undefined)
			return;
		const update = props.getErgebnismanager().kursSchuelerUpdate_02a_ENTFERNE_SCHUELERMENGE_AUS_KURS(SetUtils.create1(id), kurs.id, false);
		await props.updateKursSchuelerZuordnungen(update);
	}

	async function leereKurs() {
		const kurs = props.schuelerFilter().kurs;
		if (kurs === undefined)
			return;
		const update = props.getErgebnismanager().kursSchuelerUpdate_01b_LEERE_KURSMENGE(SetUtils.create1(kurs.id), false);
		await props.updateKursSchuelerZuordnungen(update);
	}

	async function move(id: number) {
		// Prüfe, ob der Schüler mit seiner Fachwahl bereits in einem anderen Kurs fixiert ist
		const kurszuordnung = andererKurs(id).value;
		if ((kurszuordnung !== null) && (props.getDatenmanager().schuelerGetIstFixiertInKurs(id, kurszuordnung.id)))
			return;
		// Ordne die Fachwahl dem aktuellen Kurs zu
		const kurs = props.schuelerFilter().kurs;
		if (kurs === undefined)
			return;
		const zuordnung = DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(kurs.id, id);
		const update = props.getErgebnismanager().kursSchuelerUpdate_03a_FUEGE_KURS_SCHUELER_PAARE_HINZU(SetUtils.create1(zuordnung));
		await props.updateKursSchuelerZuordnungen(update);
	}

	async function updateZuordnung(schueler: Schueler, neuer_kurs: GostBlockungsergebnisKurs | undefined | null) {
		const kurs = props.schuelerFilter().kurs;
		if (kurs === undefined)
			return;
		const alter_kurs = props.getErgebnismanager().getOfSchuelerOfFachZugeordneterKurs(schueler.id, kurs.fach_id);
		if (((neuer_kurs === undefined) || (neuer_kurs === null)) && (alter_kurs !== null)) {
			const update = props.getErgebnismanager().kursSchuelerUpdate_02a_ENTFERNE_SCHUELERMENGE_AUS_KURS(SetUtils.create1(schueler.id), alter_kurs.id, false);
			await props.updateKursSchuelerZuordnungen(update);
		} else if (neuer_kurs instanceof GostBlockungsergebnisKurs) {
			const zuordnung = DTOUtils.newGostBlockungsergebnisKursSchuelerZuordnung(neuer_kurs.id, schueler.id);
			const update = props.getErgebnismanager().kursSchuelerUpdate_03a_FUEGE_KURS_SCHUELER_PAARE_HINZU(SetUtils.create1(zuordnung));
			await props.updateKursSchuelerZuordnungen(update);
		}
	}

	function getKurs(schueler: Schueler) : GostBlockungsergebnisKurs | undefined {
		const kurs = props.schuelerFilter().kurs;
		if (kurs === undefined)
			return;
		const alter_kurs = props.getErgebnismanager().getOfSchuelerOfFachZugeordneterKurs(schueler.id, kurs.fach_id);
		return alter_kurs ?? undefined;
	}

	function getKursBezeichnung(k : GostBlockungsergebnisKurs | undefined) : string {
		if (k === undefined)
			return '———';
		return props.getErgebnismanager().getOfKursName(k.id);
	}

	const openModal = () => showModal().value = true;

	async function toggleFixierRegelKursSchueler(idKurs: number | null, idSchueler: number) : Promise<void> {
		if ((!props.allowRegeln) || (idKurs === null) || (props.apiStatus.pending))
			return;
		let update = new GostBlockungRegelUpdate();
		if (props.getDatenmanager().schuelerGetIstFixiertInKurs(idSchueler, idKurs))
			update.listEntfernen.add(props.getDatenmanager().schuelerGetRegelFixiertInKurs(idSchueler, idKurs));
		else
			update = props.getErgebnismanager().regelupdateCreate_04_SCHUELER_FIXIEREN_IN_KURS(SetUtils.create1(idSchueler), SetUtils.create1(idKurs));
		await props.regelnUpdate(update);
	}

	async function toggleFixierRegelAlleKursSchueler() {
		const kurs = props.schuelerFilter().kurs;
		const kursSchueler = props.schuelerFilter().filtered.value;
		if ((kurs === undefined) || (kursSchueler === undefined) || (kursSchueler.length === 0))
			return;
		const setSchueler = new HashSet<number>();
		for (const s of kursSchueler)
			setSchueler.add(s.id);
		const update = !kursSchuelerFixierungen.value
			? props.getErgebnismanager().regelupdateCreate_04_SCHUELER_FIXIEREN_IN_KURS(setSchueler, SetUtils.create1(kurs.id))
			: props.getErgebnismanager().regelupdateRemove_04_SCHUELER_FIXIEREN_IN_KURS(setSchueler, SetUtils.create1(kurs.id))
		await props.regelnUpdate(update);
	}

	const kursSchuelerFixierungen = computed<boolean | null>(() => {
		const kurs = props.schuelerFilter().kurs;
		if (kurs === undefined)
			return false;
		const kursSchueler = props.schuelerFilter().filtered.value;
		if (kursSchueler === undefined || kursSchueler.length === 0)
			return false;
		let i = 0;
		for (const schueler of kursSchueler)
			if (props.getDatenmanager().schuelerGetIstFixiertInKurs(schueler.id, kurs.id))
				i++;
		return (i === 0) ? false : (i === kursSchueler.length) ? true : null;
	});

	const hatFixierRegelKurs = (idSchueler: number) => computed<boolean>(() => {
		const kurs = props.schuelerFilter().kurs;
		if (kurs === undefined)
			return false;
		return props.getDatenmanager().schuelerGetIstFixiertInKurs(idSchueler, kurs.id);
	});

	const andererKurs = (idSchueler: number) => computed<GostBlockungsergebnisKurs | null>(() => {
		const kurs = props.schuelerFilter().kurs;
		if (kurs === undefined)
			return null;
		return props.getErgebnismanager().getOfSchuelerOfFachZugeordneterKurs(idSchueler, kurs.fach_id);
	});

	const hatFixierRegelAndererKurs = (idSchueler: number) => computed<boolean>(() => {
		const kurs = props.schuelerFilter().kurs;
		if (kurs === undefined)
			return false;
		if (props.getDatenmanager().schuelerGetIstFixiertInKurs(idSchueler, kurs.id))
			return false;
		const k = andererKurs(idSchueler).value;
		if (k === null)
			return false;
		return props.getDatenmanager().schuelerGetIstFixiertInKurs(idSchueler, k.id);
	});

	async function uebertragen() {
		const kurs = props.schuelerFilter().kurs;
		if (kurs === undefined)
			return;
		const kursSchueler = props.getErgebnismanager().getOfSchuelerMengeGefiltert(kurs.id, -1, -1, 0, "");
		const update = new GostBlockungsergebnisKursSchuelerZuordnungUpdate();
		const setRegelKurs = new HashSet<number>();
		for (const s of kursSchueler) {
			if (!props.fixierteVerschieben() && props.getDatenmanager().schuelerGetIstFixiertInKurs(s.id, kurs.id))
				continue;
			for (const k of _kurseZurUebertragung.value) {
				if (!props.getErgebnismanager().getOfSchuelerHatFachwahl(s.id, k.fachID, k.kursart))
					continue;
				const alter_kurs = props.getErgebnismanager().getOfSchuelerOfFachZugeordneterKurs(s.id, k.fachID);
				if (alter_kurs?.id === k.id)
					continue;
				if (alter_kurs !== null) {
					const zuordnungAlt = new GostBlockungsergebnisKursSchuelerZuordnung();
					zuordnungAlt.idSchueler = s.id;
					zuordnungAlt.idKurs = alter_kurs.id;
					update.listEntfernen.add(zuordnungAlt);
				}
				const zuordnung = new GostBlockungsergebnisKursSchuelerZuordnung();
				zuordnung.idSchueler = s.id;
				zuordnung.idKurs = k.id;
				update.listHinzuzufuegen.add(zuordnung);
				setRegelKurs.add(k.id);
			}
		}
		// nur wenn am Ende fixiert werden soll, Fixierung aktualisieren, sonst lösen
		update.regelUpdates = (props.inZielkursFixieren() && props.allowRegeln)
			? props.getErgebnismanager().regelupdateCreate_04b_SCHUELER_FIXIEREN_IN_DEN_KURSEN(setRegelKurs)
			: props.getErgebnismanager().regelupdateRemove_04b_SCHUELER_FIXIEREN_IN_DEN_KURSEN(setRegelKurs)
		// wenn Zielkurse geleert werden sollen, dann alle oder nur listRegelKurse, also die tatsächlich genutzten?
		if (zielkurseLeeren.value === true) {
			for (const k of _kurseZurUebertragung.value) {
				for (const s of k.schueler) {
					const zuordnung = new GostBlockungsergebnisKursSchuelerZuordnung();
					zuordnung.idKurs = k.id;
					zuordnung.idSchueler = s;
					update.listEntfernen.add(zuordnung);
				}
			}
		}
		// es sollen optional noch die Regeln der Zielkurse entfernt werden
		// sind das Schüler Sperrungen/Fixierungen?
		// Betrifft das dann auch nur die tatsächlich genutzten oder alle?
		// kann ich dann die weiteren GostBlockungRegelUpdate mergen?
		// Oder muss ich dafür eine zweite Anfrage an die Api schicken?
		await props.updateKursSchuelerZuordnungen(update);
	}

</script>
