<template>
	<Teleport to=".svws-ui-header--actions" v-if="isMounted">
		<svws-ui-modal-hilfe> <s-gost-klausurplanung-vorgaben-hilfe /> </svws-ui-modal-hilfe>
	</Teleport>
	<Teleport to=".router-tab-bar--subnav" v-if="isMounted">
		<s-gost-klausurplanung-quartal-auswahl :quartalsauswahl="quartalsauswahl" :halbjahr="halbjahr" />
	</Teleport>
	<div class="page page-flex-col min-w-128 max-w-256">
		<ui-card v-if="!kMan().stundenplanManagerGeladenAndExistsByAbschnitt(props.abschnitt!.id)" icon="i-ri-calendar-event-line"
			title="Kein Stundenplan" subtitle="Es existiert kein Stundenplan für diesen Schuljahresabschnitt." :is-open="currentAction === 'stundenplan_fehlend'"
			@update:is-open="(isOpen) => setCurrentAction('stundenplan_fehlend', isOpen)">
			<p>Zur Terminierung von Klausurschienen und Raumplanung muss zwingend ein Stundenplan definiert sein.</p>
			<template #buttonFooterLeft>
				<svws-ui-button title="Zur Stundenplandefinition" @click="gotoStundenplan" class="mt-2">
					<span class="icon i-ri-play-line" />
					Zur Stundenplandefinition
				</svws-ui-button>
			</template>
		</ui-card>

		<ui-card v-if="!vorgaben().isEmpty()" icon="i-ri-draft-line" title="Fehlende Klausurvorgaben"
			:subtitle="vorgaben().size() + ' fehlende Klausurvorgaben gefunden.'" :is-open="currentAction === 'vorgaben_fehlend'"
			@update:is-open="(isOpen) => setCurrentAction('vorgaben_fehlend', isOpen)">
			<svws-ui-table :items="vorgaben()" :columns="colsVorgaben">
				<template #cell(idFach)="{ value }">
					<span class="svws-ui-badge" :style="`color: var(--color-text-ui-static); background-color: ${getBgColor(kMan().getFaecherManager(jahrgangsdaten!.abiturjahr-1).get(value)?.kuerzel || null)}`">{{ kMan().getFaecherManager(jahrgangsdaten!.abiturjahr-1).get(value)?.bezeichnung }}</span>
				</template>
				<template #cell(quartal)="{ value }">
					{{ value }}
				</template>
			</svws-ui-table>
			<template #buttonFooterLeft>
				<svws-ui-button title="Zur Vorgabenansicht" @click="gotoVorgaben" class="mt-2">
					<span class="icon i-ri-play-line" />
					Zur Vorgabenansicht
				</svws-ui-button>
			</template>
		</ui-card>

		<ui-card v-if="!kursklausuren().isEmpty()" icon="i-ri-book-2-line" title="Fehlende Kursklausuren"
			:subtitle="kursklausuren().size() + ' fehlende Kursklausuren gefunden.'" :is-open="currentAction === 'kursklausuren_fehlend'"
			@update:is-open="(isOpen) => setCurrentAction('kursklausuren_fehlend', isOpen)">
			<svws-ui-table :items="kursklausuren()" :columns="colsKursklausuren">
				<template #cell(kurs)="{ rowData }">
					{{ kMan().kursKurzbezeichnungByKursklausur(rowData) }}
				</template>
				<template #cell(lehrer)="{ rowData }">
					{{ kMan().kursLehrerKuerzelByKursklausur(rowData) }}
				</template>
				<template #cell(quartal)="{ rowData }">
					{{ kMan().vorgabeByKursklausur(rowData).quartal }}
				</template>
			</svws-ui-table>
			<template #buttonFooterLeft>
				<svws-ui-button title="Fehlende Kursklausuren erstellen" @click="erzeugeKursklausurenAusVorgabenOrModal" class="mt-2">
					<span class="icon i-ri-play-line" />
					Fehlende Kursklausuren erstellen
				</svws-ui-button>
			</template>
		</ui-card>

		<ui-card v-if="!schuelerklausuren().isEmpty()" icon="i-ri-group-line" title="Abweichende Schülerklausurmenge"
			:subtitle="schuelerklausuren().size() + ' Abweichungen gefunden.'" :is-open="currentAction === 'schuelerklausurmenge_abweichend'"
			@update:is-open="(isOpen) => setCurrentAction('schuelerklausurmenge_abweichend', isOpen)">
			<svws-ui-table :items="schuelerklausuren()" :columns="addStatusColumn(colsSchuelerklausuren)">
				<template #cell(status)="{ rowData }">
					<svws-ui-button v-if="rowData.id === -1" type="transparent" @click="erzeugeSchuelerklausuren(ListUtils.create1(rowData))" title="hinzufügen">
						<span class="icon i-ri-add-line" /> hinzufügen
					</svws-ui-button>
					<svws-ui-button v-else type="transparent" @click="loescheSchuelerklausuren(ListUtils.create1(rowData))" title="löschen">
						<span class="icon i-ri-delete-bin-line" /> löschen
					</svws-ui-button>
				</template>
				<template #cell(name)="{ rowData }">
					{{ kMan().schuelerGetBySchuelerklausur(rowData).nachname }}, {{ kMan().schuelerGetBySchuelerklausur(rowData).vorname }}
				</template>
				<template #cell(kurs)="{ rowData }">
					{{ kMan().kursdatenBySchuelerklausur(rowData).kuerzel }}
				</template>
				<template #cell(quartal)="{ rowData }">
					{{ kMan().vorgabeBySchuelerklausur(rowData).quartal }}
				</template>
			</svws-ui-table>
		</ui-card>

		<ui-card v-if="!kursklausurenNichtVerteilt().isEmpty()" icon="i-ri-book-2-line" title="Nicht verteilte Kursklausuren"
			:subtitle="kursklausurenNichtVerteilt().size() + ' nicht verteilte Kursklausuren gefunden.'"
			:is-open="currentAction === 'kursklausuren_nicht_verteilt'" @update:is-open="(isOpen) => setCurrentAction('kursklausuren_nicht_verteilt', isOpen)">
			<svws-ui-table :items="kursklausurenNichtVerteilt()" :columns="colsKursklausuren">
				<template #cell(kurs)="{ rowData }">
					{{ kMan().kursKurzbezeichnungByKursklausur(rowData) }}
				</template>
				<template #cell(lehrer)="{ rowData }">
					{{ kMan().kursLehrerKuerzelByKursklausur(rowData) }}
				</template>
				<template #cell(quartal)="{ rowData }">
					{{ kMan().vorgabeByKursklausur(rowData).quartal }}
				</template>
			</svws-ui-table>
			<template #buttonFooterLeft>
				<svws-ui-button title="Zur Schienenansicht" @click="() => gotoSchienen(undefined)" class="mt-2">
					<span class="icon i-ri-play-line" />
					Zur Schienenansicht
				</svws-ui-button>
			</template>
		</ui-card>

		<ui-card v-if="!termineOhneDatum().isEmpty()" icon="i-ri-calendar-event-line" title="Klausurtermine ohne Datum"
			:subtitle="termineOhneDatum().size() + ' Klausurtermine ohne Datum gefunden.'" :is-open="currentAction === 'termine_ohne_datum'"
			@update:is-open="(isOpen) => setCurrentAction('termine_ohne_datum', isOpen)">
			<svws-ui-table :items="termineOhneDatum()" :columns="addStatusColumn(colsTermine)">
				<template #cell(status)="{ rowData }">
					<svws-ui-button type="transparent" @click="gotoKalenderdatum(undefined, rowData)"
						title="Datum setzen" size="small"
						:disabled="!kMan().stundenplanManagerExistsByAbschnitt(props.abschnitt!.id)">
						<span class="icon i-ri-link" /> datieren
					</svws-ui-button>
				</template>
				<template #cell(kurse)="{ rowData }">
					{{ terminBezeichnung(rowData) }}
				</template>
				<template #cell(quartal)="{ rowData }">
					{{ rowData.quartal }}
				</template>
			</svws-ui-table>
		</ui-card>

		<ui-card v-if="!termineMitKonflikten().isEmpty()" icon="i-ri-alert-line" title="Klausurtermine mit Schülerkonflikten"
			:subtitle="termineMitKonflikten().size() + ' Klausurtermine mit Schülerkonflikten gefunden.'"
			:is-open="currentAction === 'klausurtermine_mit_schuelerkonflikten'"
			@update:is-open="(isOpen) => setCurrentAction('klausurtermine_mit_schuelerkonflikten', isOpen)">
			<svws-ui-table :items="termineMitKonflikten()" :columns="addStatusColumn(colsTermine)">
				<template #cell(status)="{ rowData }">
					<svws-ui-button type="transparent" @click="gotoSchienen(rowData)"
						title="Schiene anzeigen" size="small">
						<span class="icon i-ri-link" /> anzeigen
					</svws-ui-button>
				</template>
				<template #cell(kurse)="{ rowData }">
					{{ terminBezeichnung(rowData) }}
				</template>
				<template #cell(quartal)="{ rowData }">
					{{ rowData.quartal }}
				</template>
			</svws-ui-table>
		</ui-card>

		<ui-card v-if="!termineUnvollstaendigeRaumzuweisung().isEmpty()" icon="i-ri-team-line" title="Klausurtermine mit unvollständiger Raumplanung"
			:subtitle="termineUnvollstaendigeRaumzuweisung().size() + ' Klausurtermine mit unvollständiger Raumplanung gefunden.'"
			:is-open="currentAction === 'termine_ohne_raumplanung'" @update:is-open="(isOpen) => setCurrentAction('termine_ohne_raumplanung', isOpen)">
			<svws-ui-table :items="termineUnvollstaendigeRaumzuweisung()" :columns="addStatusColumn(colsTermine)">
				<template #cell(status)="{ rowData }">
					<svws-ui-button type="transparent"
						@click="gotoRaumzeitTermin(rowData.abijahr, GostHalbjahr.fromIDorException(rowData.halbjahr), rowData.id)"
						title="Räume planen" size="small">
						<span class="icon i-ri-link" /> Planung
					</svws-ui-button>
				</template>
				<template #cell(kurse)="{ rowData }">
					{{ terminBezeichnung(rowData) }}
				</template>
				<template #cell(quartal)="{ rowData }">
					{{ rowData.quartal }}
				</template>
			</svws-ui-table>
		</ui-card>

		<ui-card v-if="!raumkapazitaetUeberschritten().isEmpty()" icon="i-ri-team-line" title="Raumkapazität überschritten"
			:subtitle="raumkapazitaetUeberschritten().size() + ' Klausurtermine mit überschrittener Raumkapazität gefunden.'"
			:is-open="currentAction === 'termine_raumkapazität'" @update:is-open="(isOpen) => setCurrentAction('termine_raumkapazität', isOpen)">
			<svws-ui-table :items="raumkapazitaetUeberschritten()" :columns="addStatusColumn(colsTermine)">
				<template #cell(status)="{ rowData }">
					<svws-ui-button type="transparent"
						@click="gotoRaumzeitTermin(rowData.abijahr, GostHalbjahr.fromIDorException(rowData.halbjahr), rowData.id)"
						title="Räume planen" size="small">
						<span class="icon i-ri-link" /> Räume planen
					</svws-ui-button>
				</template>
				<template #cell(kurse)="{ rowData }">
					{{ terminBezeichnung(rowData) }}
				</template>
				<template #cell(quartal)="{ rowData }">
					{{ rowData.quartal }}
				</template>
			</svws-ui-table>
		</ui-card>

		<ui-card v-if="!nachschreibklausurenNichtZugewiesen().isEmpty()" icon="i-ri-spam-3-line" title="Nicht zugewiesene Nachschreibklausuren"
			:subtitle="nachschreibklausurenNichtZugewiesen().size() + ' nicht zugewiesene Nachschreibklausuren gefunden.'"
			:is-open="currentAction === 'nachschreibklausuren_nicht_zugewiesen'"
			@update:is-open="(isOpen) => setCurrentAction('nachschreibklausuren_nicht_zugewiesen', isOpen)">
			<svws-ui-table :items="nachschreibklausurenNichtZugewiesen()" :columns="colsSchuelerklausuren">
				<template #cell(name)="{ rowData }">
					<span>{{ kMan().schuelerGetBySchuelerklausurtermin(rowData).nachname }},
						{{ kMan().schuelerGetBySchuelerklausurtermin(rowData).vorname }}</span>
				</template>
				<template #cell(kurs)="{ rowData }">
					{{ kMan().kursdatenBySchuelerklausurTermin(rowData).kuerzel }}
				</template>
				<template #cell(quartal)="{ rowData }">
					{{ kMan().vorgabeBySchuelerklausurTermin(rowData).quartal }}
				</template>
			</svws-ui-table>

			<template #buttonFooterLeft>
				<svws-ui-button title="Zur Nachschreiberansicht"
					@click="gotoNachschreiber(jahrgangsdaten!.abiturjahr, halbjahr)"
					class="mt-2">
					<span class="icon i-ri-play-line" />
					Zur Nachschreiberansicht
				</svws-ui-button>
			</template>
		</ui-card>

		<ui-card v-if="!klausurenProKwWarning().isEmpty()" icon="i-ri-alert-line" title="Schüler mit drei Klausuren in einer Woche"
			:subtitle="klausurenProKwWarning().size() + ' Probleme mit Schülern mit drei Klausuren in einer Woche gefunden.'"
			:is-open="currentAction === 'konflikt_drei_wochenklausuren'" @update:is-open="(isOpen) => setCurrentAction('konflikt_drei_wochenklausuren', isOpen)">
			<svws-ui-table :items="klausurenProKwWarning()" :columns="colsKwKonflikte">
				<template #cell(kw)="{ rowData }">
					<svws-ui-button type="transparent"
						@click="gotoKalenderdatum(kMan().terminOrExceptionBySchuelerklausurTermin(rowData.b.getFirst()!).datum!, undefined)"
						title="Springe zu Kalenderwoche" size="small">
						<span class="icon i-ri-link" /> {{ rowData.a.a }}
					</svws-ui-button>
				</template>
				<template #cell(schueler)="{ rowData }">
					{{ kMan().schuelerGetByIdOrException(rowData.a.b)?.nachname }},
					{{ kMan().schuelerGetByIdOrException(rowData.a.b)?.vorname }}
				</template>
				<template #cell(klausuren)="{rowData}">
					<span v-for="klausur in rowData.b" :key="klausur.id" class="svws-ui-badge text-center flex-col w-full" :style="`color: var(--color-text-ui-static); background-color: ${kMan().fachHTMLFarbeRgbaByKursklausur(kMan().kursklausurBySchuelerklausurTermin(klausur))};`">
						<span class="text-button font-medium">{{ kMan().kursKurzbezeichnungByKursklausur(kMan().kursklausurBySchuelerklausurTermin(klausur)) }}</span>
						<span class="text-sm font-medium">{{ DateUtils.gibDatumGermanFormat(kMan().terminOrExceptionBySchuelerklausurTermin(klausur).datum!) }}</span>
					</span>
				</template>
			</svws-ui-table>
		</ui-card>
		<ui-card v-if="!klausurenProKwError().isEmpty()" icon="i-ri-alert-fill" title="Schüler mit vier oder mehr Klausuren in einer Woche"
			:subtitle="klausurenProKwError().size() + ' Probleme mit Schülern mit vier oder mehr Klausuren in einer Woche gefunden.'"
			:is-open="currentAction === 'konflikt_vier_wochenklausuren'" @update:is-open="(isOpen) => setCurrentAction('konflikt_vier_wochenklausuren', isOpen)">
			<svws-ui-table :items="klausurenProKwError()" :columns="colsKwKonflikte">
				<template #cell(kw)="{ rowData }">
					<svws-ui-button type="transparent"
						@click="gotoKalenderdatum(kMan().terminOrExceptionBySchuelerklausurTermin(rowData.b.getFirst()!).datum!, undefined)"
						title="Springe zu Kalenderwoche" size="small">
						<span class="icon i-ri-link" /> {{ rowData.a.a }}
					</svws-ui-button>
				</template>
				<template #cell(schueler)="{ rowData }">
					<span>
						{{ kMan().schuelerGetByIdOrException(rowData.a.b)?.nachname }},
						{{ kMan().schuelerGetByIdOrException(rowData.a.b)?.vorname }}
					</span>
				</template>
				<template #cell(klausuren)="{rowData}">
					<span v-for="klausur in rowData.b" :key="klausur.id" class="svws-ui-badge text-center flex-col w-full" :style="`color: var(--color-text-ui-static); background-color: ${kMan().fachHTMLFarbeRgbaByKursklausur(kMan().kursklausurBySchuelerklausurTermin(klausur))};`">
						<span class="text-button font-medium">{{ kMan().kursKurzbezeichnungByKursklausur(kMan().kursklausurBySchuelerklausurTermin(klausur)) }}</span>
						<span class="text-sm font-medium">{{ DateUtils.gibDatumGermanFormat(kMan().terminOrExceptionBySchuelerklausurTermin(klausur).datum!) }}</span>
					</span>
				</template>
			</svws-ui-table>
		</ui-card>
	</div>

	<s-gost-klausurplanung-modal v-model:show="modalVorgaben" :text="modalError" :jump-to="gotoVorgaben" jump-to-text="Zu den Klausurvorgaben" abbrechen-text="OK" />
</template>

<script setup lang="ts">
	import { ref, onMounted } from 'vue';
	import type { DataTableColumn } from "@ui";
	import type {GostKlausurtermin } from "@core";
	import { DateUtils, GostHalbjahr, ListUtils, OpenApiError} from "@core";
	import { Fach } from "@core";
	import type { GostKlausurplanungProblemeProps } from "./SGostKlausurplanungProblemeProps";

	const props = defineProps<GostKlausurplanungProblemeProps>();

	const vorgaben = () => props.kMan().vorgabefehlendGetMengeByHalbjahrAndQuartal(props.jahrgangsdaten === undefined ? -1 : props.jahrgangsdaten.abiturjahr, props.halbjahr, props.quartalsauswahl.value);
	const kursklausuren = () => props.kMan().kursklausurfehlendGetMengeByHalbjahrAndQuartal(props.jahrgangsdaten === undefined ? -1 : props.jahrgangsdaten.abiturjahr, props.halbjahr, props.quartalsauswahl.value);
	const kursklausurenNichtVerteilt = () => props.kMan().kursklausurOhneTerminGetMengeByAbijahrAndHalbjahrAndQuartal(props.jahrgangsdaten === undefined ? -1 : props.jahrgangsdaten.abiturjahr, props.halbjahr, props.quartalsauswahl.value);
	const schuelerklausuren = () => props.kMan().schuelerklausurfehlendGetMengeByHalbjahrAndQuartal(props.jahrgangsdaten === undefined ? -1 : props.jahrgangsdaten.abiturjahr, props.halbjahr, props.quartalsauswahl.value);
	const termineOhneDatum = () => props.kMan().terminOhneDatumGetMengeByAbijahrAndHalbjahrAndQuartal(props.jahrgangsdaten === undefined ? -1 : props.jahrgangsdaten.abiturjahr, props.halbjahr, props.quartalsauswahl.value);
	const termineMitKonflikten = () => props.kMan().terminMitKonfliktGetMengeByAbijahrAndHalbjahrAndQuartal(props.jahrgangsdaten === undefined ? -1 : props.jahrgangsdaten.abiturjahr, props.halbjahr, props.quartalsauswahl.value);
	const termineUnvollstaendigeRaumzuweisung = () => props.kMan().terminUnvollstaendigeRaumzuweisungGetMengeByAbijahrAndHalbjahrAndQuartal(props.jahrgangsdaten === undefined ? -1 : props.jahrgangsdaten.abiturjahr, props.halbjahr, props.quartalsauswahl.value);
	const raumkapazitaetUeberschritten = () => props.kMan().terminUnzureichendePlatzkapazitaetGetMengeByAbijahrAndHalbjahrAndQuartal(props.jahrgangsdaten === undefined ? -1 : props.jahrgangsdaten.abiturjahr, props.halbjahr, props.quartalsauswahl.value);
	const nachschreibklausurenNichtZugewiesen = () => props.kMan().schuelerklausurterminNtAktuellOhneTerminGetMengeByHalbjahrAndQuartal(props.jahrgangsdaten === undefined ? -1 : props.jahrgangsdaten.abiturjahr, props.halbjahr, props.quartalsauswahl.value);
	const klausurenProKwWarning = () => props.kMan().klausurenProSchueleridExceedingKWThresholdByAbijahrAndHalbjahrAndThreshold(props.jahrgangsdaten === undefined ? -1 : props.jahrgangsdaten.abiturjahr, props.halbjahr, props.quartalsauswahl.value, 3, true);
	const klausurenProKwError = () => props.kMan().klausurenProSchueleridExceedingKWThresholdByAbijahrAndHalbjahrAndThreshold(props.jahrgangsdaten === undefined ? -1 : props.jahrgangsdaten.abiturjahr, props.halbjahr, props.quartalsauswahl.value, 4, false);

	const oldAction = ref({
		name: "",
		open: false,
	});
	const currentAction = ref<string>('');

	function setCurrentAction(newAction: string, open: boolean) {
		if(newAction === oldAction.value.name && !open)
			return;
		oldAction.value.name = currentAction.value;
		oldAction.value.open = (currentAction.value === "") ? false : true;
		if(open === true)
			currentAction.value= newAction;
		else
			currentAction.value = "";
	}

	// Check if component is mounted
	const isMounted = ref(false);
	onMounted(() => {
		isMounted.value = true;
	});

	const colsVorgaben: DataTableColumn[] = [
		{key: 'idFach', label: 'Fach', span: 1.25, sortable: true},
		{key: 'kursart', label: 'Kursart', span: 0.5, sortable: true},
		{key: 'quartal', label: 'Quartal', span: 0.1, align: 'center'},
	];

	const colsKursklausuren: DataTableColumn[] = [
		{key: 'kurs', label: 'Kurs', span: 1.25, sortable: true},
		{key: 'lehrer', label: 'Lehrer', span: 0.25, sortable: true},
		{key: 'quartal', label: 'Quartal', span: 0.1, align: 'center'},
	];

	const colsSchuelerklausuren: DataTableColumn[] = [
		{key: 'name', label: 'Name', span: 1.5, sortable: true},
		{key: 'kurs', label: 'Kurs', span: 0.4, sortable: true},
		{key: 'quartal', label: 'Quartal', span: 0.1, align: 'center'},
	];

	const colsTermine: DataTableColumn[] = [
		{key: 'kurse', label: 'Titel', span: 1.25, sortable: true},
		{key: 'quartal', label: 'Quartal', span: 0.1, align: 'center'},
	];

	const colsKwKonflikte: DataTableColumn[] = [
		{key: 'kw', label: 'KW', span: 0.25, sortable: true},
		{key: 'schueler', label: 'Schüler', span: 0.75, sortable: true},
		{key: 'klausuren', label: 'Klausuren', sortable: true},
	];

	function addStatusColumn(columns: DataTableColumn[]) {
		const newColumns = Array.from(columns);
		newColumns.push({key: 'status', label: 'Korrektur', span: 0.5, align: 'right'});
		return newColumns;
	}

	function getBgColor(kuerzel: string | null) {
		if (kuerzel === null)
			return 'rgb(220,220,220)';
		return Fach.getBySchluesselOrDefault(kuerzel).getHMTLFarbeRGBA(props.jahrgangsdaten!.abiturjahr - 1, 1.0);
	}

	const terminBezeichnung = (termin: GostKlausurtermin) => {
		if (termin.bezeichnung !== null && termin.bezeichnung.length > 0)
			return termin.bezeichnung;
		if (!termin.istHaupttermin)
			return "Nachschreibtermin";
		if (props.kMan().kursklausurGetMengeByTermin(termin).size() > 0)
			return [...props.kMan().kursklausurGetMengeByTermin(termin)].map(k => props.kMan().kursKurzbezeichnungByKursklausur(k)).join(", ")
		return "Leerer Klausurtermin";
	}

	const modalVorgaben = ref<boolean>(false);
	const modalError = ref<string | undefined>(undefined);

	async function erzeugeKursklausurenAusVorgabenOrModal() {
		try {
			await props.erzeugeKursklausurenAusVorgaben(props.quartalsauswahl.value);
		} catch(err) {
			if (err instanceof OpenApiError) {
				modalError.value = await err.response?.text();
				modalVorgaben.value = true;
			} else
				throw err;
		}
	}

</script>

