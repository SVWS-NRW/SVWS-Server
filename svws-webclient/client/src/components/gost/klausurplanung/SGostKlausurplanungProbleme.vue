<template>
	<Teleport to=".svws-ui-header--actions" v-if="isMounted">
		<svws-ui-modal-hilfe> <s-gost-klausurplanung-vorgaben-hilfe /> </svws-ui-modal-hilfe>
	</Teleport>
	<Teleport to=".router-tab-bar--subnav" v-if="isMounted">
		<s-gost-klausurplanung-quartal-auswahl :quartalsauswahl="quartalsauswahl" :halbjahr="halbjahr" />
	</Teleport>
	<div class="page--content">

		<svws-ui-action-button title="Fehlende Klausurvorgaben"
			:description="vorgaben().size() + ' fehlende Klausurvorgaben gefunden.'"
			v-if="!vorgaben().isEmpty()"
			:is-active="currentAction === 'vorgaben_fehlend'"
			@click="toggleAction('vorgaben_fehlend')"
			action-label="Zur Vorgabenansicht"
			:action-function="() => RouteManager.doRoute(routeGostKlausurplanungVorgaben.getRoute(jahrgangsdaten!.abiturjahr, halbjahr.id))"
			class="border-error"
			icon="i-ri-draft-line">
			<svws-ui-table :items="vorgaben()" :columns="colsVorgaben">
				<template #cell(idFach)="{ value }">
					<span class="svws-ui-badge" :style="{ '--background-color': getBgColor(kMan().getFaecherManager().get(value)?.kuerzel || null) }">{{ kMan().getFaecherManager().get(value)?.bezeichnung }}</span>
				</template>
				<template #cell(quartal)="{value}">
					{{ value }}
				</template>
				<template #actions>
					<svws-ui-button class="-mr-3" type="transparent" @click="RouteManager.doRoute(routeGostKlausurplanungVorgaben.getRoute(jahrgangsdaten!.abiturjahr, halbjahr.id))" title="Zur Vorgabenansicht"><span class="icon i-ri-upload-2-line" />Zur Vorgabenansicht</svws-ui-button>
				</template>
			</svws-ui-table>
		</svws-ui-action-button>

		<svws-ui-action-button title="Fehlende Kursklausuren"
			:description="kursklausuren().size() + ' fehlende Kursklausuren gefunden.'"
			v-if="!kursklausuren().isEmpty()"
			:is-active="currentAction === 'kursklausuren_fehlend'"
			@click="toggleAction('kursklausuren_fehlend')"
			action-label="Fehlende Kursklausuren erstellen"
			:action-function="erzeugeKursklausurenAusVorgabenOrModal"
			class="border-error"
			icon="i-ri-book-2-line">
			<svws-ui-table :items="kursklausuren()"
				:columns="colsKursklausuren">
				<template #cell(kurs)="{rowData}">
					{{ kMan().kursKurzbezeichnungByKursklausur(rowData) }}
				</template>
				<template #cell(lehrer)="{rowData}">
					{{ kMan().kursLehrerKuerzelByKursklausur(rowData) }}
				</template>
				<template #cell(quartal)="{rowData}">
					{{ kMan().vorgabeByKursklausur(rowData).quartal }}
				</template>
			</svws-ui-table>
		</svws-ui-action-button>

		<svws-ui-action-button title="Abweichende Schülerklausurmenge"
			:description="schuelerklausuren().size() + ' Abweichungen gefunden.'"
			v-if="!schuelerklausuren().isEmpty()"
			:is-active="currentAction === 'schuelerklausurmenge_abweichend'"
			@click="toggleAction('schuelerklausurmenge_abweichend')"
			action-disabled
			class="border-error"
			icon="i-ri-group-line">
			<svws-ui-table :items="schuelerklausuren()"
				:columns="addStatusColumn(colsSchuelerklausuren)">
				<template #cell(status)="{rowData}">
					<svws-ui-button v-if="rowData.id == -1" type="transparent" @click="erzeugeSchuelerklausuren(ListUtils.create1(rowData))" title="hinzufügen"><span class="icon i-ri-add-line" /> hinzufügen</svws-ui-button>
					<svws-ui-button v-else type="transparent" @click="loescheSchuelerklausuren(ListUtils.create1(rowData))" title="löschen"><span class="icon i-ri-delete-bin-line" /> löschen</svws-ui-button>
				</template>
				<template #cell(name)="{rowData}">
					{{ kMan().schuelerlisteeintragGetBySchuelerklausur(rowData).nachname }}, {{ kMan().schuelerlisteeintragGetBySchuelerklausur(rowData).vorname }}
				</template>
				<template #cell(kurs)="{rowData}">
					{{ kMan().kursdatenBySchuelerklausur(rowData).kuerzel }}
				</template>
				<template #cell(quartal)="{rowData}">
					{{ kMan().vorgabeBySchuelerklausur(rowData).quartal }}
				</template>
			</svws-ui-table>
		</svws-ui-action-button>

		<svws-ui-action-button title="Nicht verteilte Kursklausuren"
			:description="kursklausurenNichtVerteilt().size() + ' nicht verteilte Kursklausuren gefunden.'"
			v-if="!kursklausurenNichtVerteilt().isEmpty()"
			:is-active="currentAction === 'kursklausuren_nicht_verteilt'"
			@click="toggleAction('kursklausuren_nicht_verteilt')"
			action-label="Zur Schienenansicht"
			:action-function="() => RouteManager.doRoute(routeGostKlausurplanungSchienen.getRoute(jahrgangsdaten!.abiturjahr, halbjahr.id))"
			class="border-error"
			icon="i-ri-book-2-line">
			<svws-ui-table :items="kursklausurenNichtVerteilt()"
				:columns="colsKursklausuren">
				<template #cell(kurs)="{rowData}">
					{{ kMan().kursKurzbezeichnungByKursklausur(rowData) }}
				</template>
				<template #cell(lehrer)="{rowData}">
					{{ kMan().kursLehrerKuerzelByKursklausur(rowData) }}
				</template>
				<template #cell(quartal)="{rowData}">
					{{ kMan().vorgabeByKursklausur(rowData).quartal }}
				</template>
			</svws-ui-table>
		</svws-ui-action-button>

		<svws-ui-action-button title="Klausurtermine ohne Datum"
			:description="termineOhneDatum().size() + ' Klausurtermine ohne Datum gefunden.'"
			v-if="!termineOhneDatum().isEmpty()"
			:is-active="currentAction === 'termine_ohne_datum'"
			@click="toggleAction('termine_ohne_datum')"
			action-disabled
			class="border-yellow-300"
			icon="i-ri-calendar-event-line">
			<svws-ui-table :items="termineOhneDatum()"
				:columns="addStatusColumn(colsTermine)">
				<template #cell(status)="{rowData}">
					<svws-ui-button type="transparent" @click="RouteManager.doRoute(routeGostKlausurplanungKalender.getRoute( rowData.abijahr, rowData.halbjahr, undefined, rowData.id ))" title="Datum setzen" size="small"><span class="icon i-ri-link" /> datieren</svws-ui-button>
				</template>
				<template #cell(kurse)="{rowData}">
					{{ terminBezeichnung(rowData) }}
				</template>
				<template #cell(quartal)="{rowData}">
					{{ rowData.quartal }}
				</template>
			</svws-ui-table>
		</svws-ui-action-button>

		<svws-ui-action-button title="Klausurtermine mit Schülerkonflikten"
			:description="termineMitKonflikten().size() + ' Klausurtermine mit Schülerkonflikten gefunden.'"
			v-if="!termineMitKonflikten().isEmpty()"
			:is-active="currentAction === 'klausurtermine_mit_schuelerkonflikten'"
			@click="toggleAction('klausurtermine_mit_schuelerkonflikten')"
			action-label="Zur Schienenansicht"
			:action-function="() => RouteManager.doRoute(routeGostKlausurplanungSchienen.getRoute(jahrgangsdaten!.abiturjahr, halbjahr.id))"
			class="border-error"
			icon="i-ri-alert-line">
			<svws-ui-table :items="termineMitKonflikten()"
				:columns="colsTermine">
				<template #cell(kurse)="{rowData}">
					{{ terminBezeichnung(rowData) }}
				</template>
				<template #cell(quartal)="{rowData}">
					{{ rowData.quartal }}
				</template>
			</svws-ui-table>
		</svws-ui-action-button>

		<svws-ui-action-button title="Klausurtermine mit unvollständiger Raumplanung"
			:description="termineUnvollstaendigeRaumzuweisung().size() + ' Klausurtermine mit unvollständiger Raumplanung gefunden.'"
			v-if="!termineUnvollstaendigeRaumzuweisung().isEmpty()"
			:is-active="currentAction === 'termine_ohne_raumplanung'"
			@click="toggleAction('termine_ohne_raumplanung')"
			action-disabled
			class="border-yellow-300"
			icon="i-ri-team-line">
			<svws-ui-table :items="termineUnvollstaendigeRaumzuweisung()"
				:columns="addStatusColumn(colsTermine)">
				<template #cell(status)="{rowData}">
					<svws-ui-button type="transparent" @click="RouteManager.doRoute(routeGostKlausurplanungRaumzeit.getRoute( rowData.abijahr, rowData.halbjahr, rowData.id ))" title="Räume planen" size="small"><span class="icon i-ri-link" /> Planung</svws-ui-button>
				</template>
				<template #cell(kurse)="{rowData}">
					{{ terminBezeichnung(rowData) }}
				</template>
				<template #cell(quartal)="{rowData}">
					{{ rowData.quartal }}
				</template>
			</svws-ui-table>
		</svws-ui-action-button>

		<svws-ui-action-button title="Raumkapazität überschritten"
			:description="raumkapazitaetUeberschritten().size() + ' Klausurtermine mit überschrittener Raumkapazität gefunden.'"
			v-if="!raumkapazitaetUeberschritten().isEmpty()"
			:is-active="currentAction === 'termine_raumkapazität'"
			@click="toggleAction('termine_raumkapazität')"
			action-disabled
			class="border-yellow-300"
			icon="i-ri-team-line">
			<svws-ui-table :items="raumkapazitaetUeberschritten()"
				:columns="addStatusColumn(colsTermine)">
				<template #cell(status)="{rowData}">
					<svws-ui-button type="transparent" @click="RouteManager.doRoute(routeGostKlausurplanungRaumzeit.getRoute( rowData.abijahr, rowData.halbjahr, rowData.id ))" title="Räume planen" size="small"><span class="icon i-ri-link" /> Räume planen</svws-ui-button>
				</template>
				<template #cell(kurse)="{rowData}">
					{{ terminBezeichnung(rowData) }}
				</template>
				<template #cell(quartal)="{rowData}">
					{{ rowData.quartal }}
				</template>
			</svws-ui-table>
		</svws-ui-action-button>

		<svws-ui-action-button title="Nicht zugewiesene Nachschreibklausuren"
			:description="nachschreibklausurenNichtZugewiesen().size() + ' nicht zugewiesene Nachschreibklausuren gefunden.'"
			v-if="!nachschreibklausurenNichtZugewiesen().isEmpty()"
			:is-active="currentAction === 'nachschreibklausuren_nicht_zugewiesen'"
			@click="toggleAction('nachschreibklausuren_nicht_zugewiesen')"
			action-label="Zur Nachschreiberansicht"
			:action-function="() => RouteManager.doRoute(routeGostKlausurplanungNachschreiber.getRoute(jahrgangsdaten!.abiturjahr, halbjahr.id))"
			class="border-yellow-300"
			icon="i-ri-spam-3-line">
			<svws-ui-table :items="nachschreibklausurenNichtZugewiesen()"
				:columns="colsSchuelerklausuren">
				<template #cell(name)="{rowData}">
					<span>{{ kMan().schuelerlisteeintragGetBySchuelerklausurtermin(rowData).nachname }}, {{ kMan().schuelerlisteeintragGetBySchuelerklausurtermin(rowData).vorname }}</span>
				</template>
				<template #cell(kurs)="{rowData}">
					{{ kMan().kursdatenBySchuelerklausurTermin(rowData).kuerzel }}
				</template>
				<template #cell(quartal)="{rowData}">
					{{ kMan().vorgabeBySchuelerklausurTermin(rowData).quartal }}
				</template>
			</svws-ui-table>
		</svws-ui-action-button>

		<svws-ui-action-button title="Schüler mit drei Klausuren in einer Woche"
			:description="klausurenProKwWarning().size() + ' Probleme mit Schülern mit drei Klausuren in einer Woche gefunden.'"
			v-if="!klausurenProKwWarning().isEmpty()"
			:is-active="currentAction === 'konflikt_drei_wochenklausuren'"
			@click="toggleAction('konflikt_drei_wochenklausuren')"
			action-disabled
			class="border-yellow-300"
			icon="i-ri-alert-line">
			<svws-ui-table :items="klausurenProKwWarning()"
				:columns="colsKwKonflikte">
				<template #cell(kw)="{rowData}">
					<!-- TODO: kw <=9 um führende 0 ergänzen -->
					<svws-ui-button type="transparent" @click="RouteManager.doRoute(routeGostKlausurplanungKalender.getRoute(jahrgangsdaten!.abiturjahr, halbjahr.id, parseInt(kMan().getStundenplanManager().kalenderwochenzuordnungGetByDatum(kMan().terminOrExceptionBySchuelerklausurTermin(rowData.b.getFirst()!).datum!).jahr.toString() + rowData.a.a.toString()), undefined ))"	title="Springe zu Kalenderwoche" size="small"><span class="icon i-ri-link" /> {{ rowData.a.a }}</svws-ui-button>
				</template>
				<template #cell(schueler)="{rowData}">
					{{ kMan().getSchuelerMap().get(rowData.a.b)?.nachname }}, {{ kMan().getSchuelerMap().get(rowData.a.b)?.vorname }}
				</template>
				<template #cell(klausuren)="{rowData}">
					<span v-for="klausur in rowData.b" :key="klausur.id" class="svws-ui-badge text-center flex-col w-full" :style="`--background-color: ${kMan().fachHTMLFarbeRgbaByKursklausur(kMan().kursklausurBySchuelerklausurTermin(klausur))};`">
						<span class="text-button font-medium">{{ kMan().kursKurzbezeichnungByKursklausur(kMan().kursklausurBySchuelerklausurTermin(klausur)) }}</span>
						<span class="text-sm font-medium">{{ DateUtils.gibDatumGermanFormat(kMan().terminOrExceptionBySchuelerklausurTermin(klausur).datum!) }}</span>
					</span>
				</template>
			</svws-ui-table>
		</svws-ui-action-button>

		<svws-ui-action-button title="Schüler mit vier oder mehr Klausuren in einer Woche"
			:description="klausurenProKwError().size() + ' Probleme mit Schülern mit vier oder mehr Klausuren in einer Woche gefunden.'"
			v-if="!klausurenProKwError().isEmpty()"
			:is-active="currentAction === 'konflikt_vier_wochenklausuren'"
			@click="toggleAction('konflikt_vier_wochenklausuren')"
			action-disabled
			class="border-error"
			icon="i-ri-alert-fill">
			<svws-ui-table :items="klausurenProKwError()"
				:columns="colsKwKonflikte">
				<template #cell(kw)="{rowData}">
					<!-- TODO: kw <=9 um führende 0 ergänzen -->
					<svws-ui-button type="transparent" @click="RouteManager.doRoute(routeGostKlausurplanungKalender.getRoute( jahrgangsdaten!.abiturjahr, halbjahr.id, parseInt(kMan().getStundenplanManager().kalenderwochenzuordnungGetByDatum(kMan().terminOrExceptionBySchuelerklausurTermin(rowData.b.getFirst()!).datum!).jahr.toString() + rowData.a.a.toString()), undefined ))" title="Springe zu Kalenderwoche" size="small"><span class="icon i-ri-link" /> {{ rowData.a.a }}</svws-ui-button>
				</template>
				<template #cell(schueler)="{rowData}">
					<span >{{ kMan().getSchuelerMap().get(rowData.a.b)?.nachname }}, {{ kMan().getSchuelerMap().get(rowData.a.b)?.vorname }}</span>
				</template>
				<template #cell(klausuren)="{rowData}">
					<span v-for="klausur in rowData.b" :key="klausur.id" class="svws-ui-badge text-center flex-col w-full" :style="`--background-color: ${kMan().fachHTMLFarbeRgbaByKursklausur(kMan().kursklausurBySchuelerklausurTermin(klausur))};`">
						<span class="text-button font-medium">{{ kMan().kursKurzbezeichnungByKursklausur(kMan().kursklausurBySchuelerklausurTermin(klausur)) }}</span>
						<span class="text-sm font-medium">{{ DateUtils.gibDatumGermanFormat(kMan().terminOrExceptionBySchuelerklausurTermin(klausur).datum!) }}</span>
					</span>
				</template>
			</svws-ui-table>
		</svws-ui-action-button>

	</div>

	<s-gost-klausurplanung-modal :show="returnModalVorgaben()" :text="modalError" :jump-to="gotoVorgaben" jump-to_text="Zu den Klausurvorgaben" abbrechen_text="OK" />
</template>

<script setup lang="ts">

	import type { Ref } from 'vue';
	import { ref, onMounted } from 'vue';
	import type { DataTableColumn } from "@ui";
	import type {GostKlausurtermin } from "@core";
	import { DateUtils, ListUtils, OpenApiError} from "@core";
	import { ZulaessigesFach} from "@core";
	import type { GostKlausurplanungProblemeProps } from "./SGostKlausurplanungProblemeProps";
	import { RouteManager } from '~/router/RouteManager';
	import { routeGostKlausurplanungVorgaben } from "~/router/apps/gost/klausurplanung/RouteGostKlausurplanungVorgaben";
	import { routeGostKlausurplanungKalender } from "~/router/apps/gost/klausurplanung/RouteGostKlausurplanungKalender";
	import { routeGostKlausurplanungRaumzeit } from "~/router/apps/gost/klausurplanung/RouteGostKlausurplanungRaumzeit";
	import { routeGostKlausurplanungSchienen } from "~/router/apps/gost/klausurplanung/RouteGostKlausurplanungSchienen";
	import { routeGostKlausurplanungNachschreiber } from "~/router/apps/gost/klausurplanung/RouteGostKlausurplanungNachschreiber";

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

	const currentAction = ref<string>('');
	function toggleAction(action: string) {
		currentAction.value = action;
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

	const getBgColor = (kuerzel: string | null) => ZulaessigesFach.getByKuerzelASD(kuerzel).getHMTLFarbeRGBA(1.0);

	const terminBezeichnung = (termin: GostKlausurtermin) => {
		if (termin.bezeichnung !== null && termin.bezeichnung.length > 0)
			return termin.bezeichnung;
		if (!termin.istHaupttermin)
			return "Nachschreibtermin";
		if (props.kMan().kursklausurGetMengeByTermin(termin).size())
			return [...props.kMan().kursklausurGetMengeByTermin(termin)].map(k => props.kMan().kursKurzbezeichnungByKursklausur(k)).join(", ")
		return "Leerer Klausurtermin";
	}

	const modalVorgaben = ref<boolean>(false);
	function returnModalVorgaben(): () => Ref<boolean> {
		return () => modalVorgaben;
	}
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

