<template>
	<Teleport to=".svws-ui-header--actions" v-if="isMounted">
		<svws-ui-modal-hilfe> <s-gost-klausurplanung-vorgaben-hilfe /> </svws-ui-modal-hilfe>
	</Teleport>
	<Teleport to=".router-tab-bar--subnav" v-if="isMounted">
		<s-gost-klausurplanung-quartal-auswahl :quartalsauswahl="quartalsauswahl" :halbjahr="halbjahr" />
	</Teleport>
	<div class="page--content page--content--full">
		<div class="flex flex-col space-y-10">

			<svws-ui-content-card title="Fehlende Klausurvorgaben" v-if="!vorgaben().isEmpty()" class="border border-yellow-300 rounded-lg p-5">
				<svws-ui-table :items="vorgaben()" :columns="colsVorgaben">
					<template #cell(idFach)="{ value }">
						<span class="svws-ui-badge" :style="{ '--background-color': getBgColor(kMan().getFaecherManager().get(value)?.kuerzel || null) }">{{ kMan().getFaecherManager().get(value)?.bezeichnung }}</span>
					</template>
					<template #cell(quartal)="{value}">
						{{ value }}.
					</template>
				</svws-ui-table>
			</svws-ui-content-card>

			<svws-ui-content-card title="Fehlende Kursklausuren" v-if="!kursklausuren().isEmpty()" class="border border-error rounded-lg p-5">
				<svws-ui-table :items="kursklausuren()"
					:columns="colsKursklausuren">
					<template #header(linkToSchueler)>
						<span class="icon i-ri-group-line" />
					</template>
					<template #header(ergebnis)>
						<svws-ui-tooltip class="w-6">
							<span class="icon i-ri-alert-line -my-1 -mx-0.5" />
							<template #content>
								Anzahl der Fehler insgesamt
							</template>
						</svws-ui-tooltip>
					</template>
					<template #cell(linkToSchueler)="{ rowData }">
					</template>
					<template #cell(kurs)="{rowData}">
						{{ kMan().kursKurzbezeichnungByKursklausur(rowData) }}
					</template>
					<template #cell(lehrer)="{rowData}">
						{{ kMan().kursLehrerKuerzelByKursklausur(rowData) }}
					</template>
					<template #cell(quartal)="{rowData}">
						{{ kMan().vorgabeByKursklausur(rowData).quartal }}.
					</template>
					<template #actions>
						<svws-ui-button class="-mr-3" type="transparent" @click="erzeugeKursklausurenAusVorgabenOrModal" title="Erstelle Klausuren aus den Vorgaben"><span class="icon i-ri-upload-2-line" />Aus Vorgaben erstellen</svws-ui-button>
					</template>
				</svws-ui-table>
			</svws-ui-content-card>

			<svws-ui-content-card title="Abweichende Schülerklausurmenge" v-if="!schuelerklausuren().isEmpty()" class="border border-error rounded-lg p-5">
				<svws-ui-table :items="schuelerklausuren()"
					:columns="colsSchuelerklausuren">
					<template #header(linkToSchueler)>
						<span class="icon i-ri-group-line" />
					</template>
					<template #header(ergebnis)>
						<svws-ui-tooltip class="w-6">
							<span class="icon i-ri-alert-line -my-1 -mx-0.5" />
							<template #content>
								Anzahl der Fehler insgesamt
							</template>
						</svws-ui-tooltip>
					</template>
					<template #cell(linkToSchueler)="{ rowData }">
					</template>
					<template #cell(status)="{rowData}">
						<svws-ui-button v-if="rowData.id == -1" type="transparent" @click="erzeugeSchuelerklausuren(ListUtils.create1(rowData))" title="Schülerklausur anlegen"><span class="icon i-ri-add-line" /> Schülerklausur anlegen</svws-ui-button>
						<svws-ui-button v-else type="transparent" @click="loescheSchuelerklausuren(ListUtils.create1(rowData))" title="Schülerklausur löschen"><span class="icon i-ri-delete-bin-line" /> Schülerklausur löschen</svws-ui-button>
					</template>
					<template #cell(name)="{rowData}">
						{{ kMan().schuelerlisteeintragGetBySchuelerklausur(rowData).nachname }}, {{ kMan().schuelerlisteeintragGetBySchuelerklausur(rowData).vorname }}
					</template>
					<template #cell(kurs)="{rowData}">
						{{ kMan().kursdatenBySchuelerklausur(rowData).kuerzel }}
					</template>
					<template #cell(quartal)="{rowData}">
						{{ kMan().vorgabeBySchuelerklausur(rowData).quartal }}.
					</template>
				</svws-ui-table>
			</svws-ui-content-card>

			<svws-ui-content-card title="Nicht verteilte Kursklausuren" v-if="!kursklausurenNichtVerteilt().isEmpty()" class="border border-yellow-300 rounded-lg p-5">
				<svws-ui-table :items="kursklausurenNichtVerteilt()"
					:columns="colsKursklausuren">
					<template #header(linkToSchueler)>
						<span class="icon i-ri-group-line" />
					</template>
					<template #header(ergebnis)>
						<svws-ui-tooltip class="w-6">
							<span class="icon i-ri-alert-line -my-1 -mx-0.5" />
							<template #content>
								Anzahl der Fehler insgesamt
							</template>
						</svws-ui-tooltip>
					</template>
					<template #cell(linkToSchueler)="{ rowData }">
					</template>
					<template #cell(kurs)="{rowData}">
						{{ kMan().kursKurzbezeichnungByKursklausur(rowData) }}
					</template>
					<template #cell(lehrer)="{rowData}">
						{{ kMan().kursLehrerKuerzelByKursklausur(rowData) }}
					</template>
					<template #cell(quartal)="{rowData}">
						{{ kMan().vorgabeByKursklausur(rowData).quartal }}.
					</template>
					<template #actions>
						<svws-ui-button class="-mr-3" type="transparent" @click="RouteManager.doRoute(routeGostKlausurplanungSchienen.getRoute(jahrgangsdaten!.abiturjahr, halbjahr.id))" title="Zur Schienenansicht"><span class="icon i-ri-upload-2-line" />Zur Schienenansicht</svws-ui-button>
					</template>
				</svws-ui-table>
			</svws-ui-content-card>

			<svws-ui-content-card title="Klausurtermine ohne Datum" v-if="!termineOhneDatum().isEmpty()" class="border border-yellow-300 rounded-lg p-5">
				<svws-ui-table :items="termineOhneDatum()"
					:columns="colsTermine">
					<template #header(linkToSchueler)>
						<span class="icon i-ri-group-line" />
					</template>
					<template #header(ergebnis)>
						<svws-ui-tooltip class="w-6">
							<span class="icon i-ri-alert-line -my-1 -mx-0.5" />
							<template #content>
								Anzahl der Fehler insgesamt
							</template>
						</svws-ui-tooltip>
					</template>
					<template #cell(status)="{rowData}">
						<svws-ui-button type="transparent" @click="RouteManager.doRoute(routeGostKlausurplanungKalender.getRoute( rowData.abijahr, rowData.halbjahr, undefined, rowData.id ))" title="Datum setzen" size="small"><span class="icon i-ri-link" /> Datum setzen</svws-ui-button>
					</template>
					<template #cell(kurse)="{rowData}">
						{{ terminBezeichnung(rowData) }}
					</template>
					<template #cell(quartal)="{rowData}">
						{{ rowData.quartal }}.
					</template>
				</svws-ui-table>
			</svws-ui-content-card>

			<svws-ui-content-card title="Klausurtermine mit Schülerkonflikten" v-if="!termineMitKonflikten().isEmpty()" class="border border-error rounded-lg p-5">
				<svws-ui-table :columns="colsTermine">
					<template #body>
						<div v-for="termin in termineMitKonflikten()"
							:key="termin.abijahr"
							class="svws-ui-tr" role="row">
							<div class="svws-ui-td" role="cell">
								{{ terminBezeichnung(termin) }}
							</div>
							<div class="svws-ui-td" role="cell">
								{{ termin.quartal }}.
							</div>
						</div>
					</template>
				</svws-ui-table>
			</svws-ui-content-card>

			<svws-ui-content-card title="Klausurtermine mit unvollständiger Raumplanung" v-if="!termineUnvollstaendigeRaumzuweisung().isEmpty()" class="border border-yellow-300 rounded-lg p-5">
				<svws-ui-table :items="termineUnvollstaendigeRaumzuweisung()"
					:columns="colsTermine">
					<template #header(linkToSchueler)>
						<span class="icon i-ri-group-line" />
					</template>
					<template #header(ergebnis)>
						<svws-ui-tooltip class="w-6">
							<span class="icon i-ri-alert-line -my-1 -mx-0.5" />
							<template #content>
								Anzahl der Fehler insgesamt
							</template>
						</svws-ui-tooltip>
					</template>
					<template #cell(status)="{rowData}">
						<svws-ui-button type="transparent" @click="RouteManager.doRoute(routeGostKlausurplanungRaumzeit.getRoute( rowData.abijahr, rowData.halbjahr, rowData.id ))" title="Räume planen" size="small"><span class="icon i-ri-link" /> Räume planen</svws-ui-button>
					</template>
					<template #cell(kurse)="{rowData}">
						{{ terminBezeichnung(rowData) }}
					</template>
					<template #cell(quartal)="{rowData}">
						{{ rowData.quartal }}.
					</template>
				</svws-ui-table>
			</svws-ui-content-card>

			<svws-ui-content-card title="Raumkapazität überschritten" v-if="!raumkapazitaetUeberschritten().isEmpty()" class="border border-yellow-300 rounded-lg p-5">
				<svws-ui-table :items="raumkapazitaetUeberschritten()"
					:columns="colsTermine">
					<template #header(linkToSchueler)>
						<span class="icon i-ri-group-line" />
					</template>
					<template #header(ergebnis)>
						<svws-ui-tooltip class="w-6">
							<span class="icon i-ri-alert-line -my-1 -mx-0.5" />
							<template #content>
								Anzahl der Fehler insgesamt
							</template>
						</svws-ui-tooltip>
					</template>
					<template #cell(status)="{rowData}">
						<svws-ui-button type="transparent" @click="RouteManager.doRoute(routeGostKlausurplanungRaumzeit.getRoute( rowData.abijahr, rowData.halbjahr, rowData.id ))" title="Räume planen" size="small"><span class="icon i-ri-link" /> Räume planen</svws-ui-button>
					</template>
					<template #cell(kurse)="{rowData}">
						{{ terminBezeichnung(rowData) }}
					</template>
					<template #cell(quartal)="{rowData}">
						{{ rowData.quartal }}.
					</template>
				</svws-ui-table>
			</svws-ui-content-card>

			<svws-ui-content-card title="Nicht zugewiesene Nachschreibklausuren" v-if="!nachschreibklausurenNichtZugewiesen().isEmpty()" class="border border-yellow-300 rounded-lg p-5">
				<svws-ui-table :items="nachschreibklausurenNichtZugewiesen()"
					:columns="colsSchuelerklausuren">
					<template #header(linkToSchueler)>
						<span class="icon i-ri-group-line" />
					</template>
					<template #header(ergebnis)>
						<svws-ui-tooltip class="w-6">
							<span class="icon i-ri-alert-line -my-1 -mx-0.5" />
							<template #content>
								Anzahl der Fehler insgesamt
							</template>
						</svws-ui-tooltip>
					</template>
					<template #cell(status)="{rowData}">
						<svws-ui-button type="transparent" @click="RouteManager.doRoute(routeGostKlausurplanungNachschreiber.getRoute( kMan().vorgabeBySchuelerklausurTermin(rowData).abiJahrgang, kMan().vorgabeBySchuelerklausurTermin(rowData).halbjahr ))" title="Termin zuweisen" size="small"><span class="icon i-ri-link" /> Termin zuweisen</svws-ui-button>
					</template>
					<template #cell(name)="{rowData}">
						<span>{{ kMan().schuelerlisteeintragGetBySchuelerklausurtermin(rowData).nachname }}, {{ kMan().schuelerlisteeintragGetBySchuelerklausurtermin(rowData).vorname }}</span>
					</template>
					<template #cell(kurs)="{rowData}">
						{{ kMan().kursdatenBySchuelerklausurTermin(rowData).kuerzel }}
					</template>
					<template #cell(quartal)="{rowData}">
						{{ kMan().vorgabeBySchuelerklausurTermin(rowData).quartal }}.
					</template>
				</svws-ui-table>
			</svws-ui-content-card>

			<svws-ui-content-card title="Schüler mit drei Klausuren in einer Woche" v-if="!klausurenProKwWarning().isEmpty()" class="border border-yellow-300 rounded-lg p-5">
				<svws-ui-table :items="klausurenProKwWarning()"
					:columns="colsKwKonflikte">
					<template #header(linkToSchueler)>
						<span class="icon i-ri-group-line" />
					</template>
					<template #header(ergebnis)>
						<svws-ui-tooltip class="w-6">
							<span class="icon i-ri-alert-line -my-1 -mx-0.5" />
							<template #content>
								Anzahl der Fehler insgesamt
							</template>
						</svws-ui-tooltip>
					</template>
					<template #cell(kw)="{rowData}">
						<!-- TODO: kw <=9 um führende 0 ergänzen -->
						<svws-ui-button type="transparent" @click="RouteManager.doRoute(routeGostKlausurplanungKalender.getRoute(jahrgangsdaten!.abiturjahr, halbjahr.id, parseInt(kMan().getStundenplanManager().kalenderwochenzuordnungGetByDatum(kMan().terminOrExceptionBySchuelerklausurTermin(rowData.b.getFirst()!).datum!).jahr.toString() + rowData.a.a.toString()), undefined ))"	title="Springe zu Kalenderwoche" size="small"><span class="icon i-ri-link" /> {{ rowData.a.a }}</svws-ui-button>
					</template>
					<template #cell(schueler)="{rowData}">
						{{ kMan().getSchuelerMap().get(rowData.a.b)?.nachname }}, {{ kMan().getSchuelerMap().get(rowData.a.b)?.vorname }}
					</template>
					<template #cell(klausuren)="{rowData}">
						<template v-for="(klausur, index) in rowData.b"><template v-if="index > 0">, </template>{{ kMan().kursdatenBySchuelerklausurTermin(klausur).kuerzel }}</template>
					</template>
				</svws-ui-table>
			</svws-ui-content-card>

			<svws-ui-content-card title="Schüler mit vier oder mehr Klausuren in einer Woche" v-if="!klausurenProKwError().isEmpty()" class="border border-error rounded-lg p-5">
				<svws-ui-table :items="klausurenProKwError()"
					:columns="colsKwKonflikte">
					<template #header(linkToSchueler)>
						<span class="icon i-ri-group-line" />
					</template>
					<template #header(ergebnis)>
						<svws-ui-tooltip class="w-6">
							<span class="icon i-ri-alert-line -my-1 -mx-0.5" />
							<template #content>
								Anzahl der Fehler insgesamt
							</template>
						</svws-ui-tooltip>
					</template>
					<template #cell(kw)="{rowData}">
						<!-- TODO: kw <=9 um führende 0 ergänzen -->
						<svws-ui-button type="transparent" @click="RouteManager.doRoute(routeGostKlausurplanungKalender.getRoute( jahrgangsdaten!.abiturjahr, halbjahr.id, parseInt(kMan().getStundenplanManager().kalenderwochenzuordnungGetByDatum(kMan().terminOrExceptionBySchuelerklausurTermin(rowData.b.getFirst()!).datum!).jahr.toString() + rowData.a.a.toString()), undefined ))" title="Springe zu Kalenderwoche" size="small"><span class="icon i-ri-link" /> {{ rowData.a.a }}</svws-ui-button>
					</template>
					<template #cell(schueler)="{rowData}">
						{{ kMan().getSchuelerMap().get(rowData.a.b)?.nachname }}, {{ kMan().getSchuelerMap().get(rowData.a.b)?.vorname }}
					</template>
					<template #cell(klausuren)="{rowData}">
						<template v-for="(klausur, index) in rowData.b"><template v-if="index > 0">, </template>{{ kMan().kursdatenBySchuelerklausurTermin(klausur).kuerzel }}</template>
					</template>
				</svws-ui-table>
			</svws-ui-content-card>

		</div>
	</div>

	<s-gost-klausurplanung-modal :show="returnModalVorgaben()" :text="modalError" :jump-to="gotoVorgaben" jump-to_text="Zu den Klausurvorgaben" abbrechen_text="OK" />
</template>

<script setup lang="ts">

	import type { Ref } from 'vue';
	import { ref, onMounted } from 'vue';
	import type { DataTableColumn } from "@ui";
	import type {GostKlausurtermin } from "@core";
	import { ListUtils, OpenApiError} from "@core";
	import { ZulaessigesFach} from "@core";
	import type { GostKlausurplanungProblemeProps } from "./SGostKlausurplanungProblemeProps";
	import { RouteManager } from '~/router/RouteManager';
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

	// Check if component is mounted
	const isMounted = ref(false);
	onMounted(() => {
		isMounted.value = true;
	});

	const colsVorgaben: DataTableColumn[] = [
		{key: 'idFach', label: 'Fach', span: 1.25, sortable: true},
		{key: 'kursart', label: 'Kursart', span: 0.5, sortable: true},
		{key: 'quartal', label: 'Quartal', span: 0.5, sortable: true},
	];

	const colsKursklausuren: DataTableColumn[] = [
		{key: 'kurs', label: 'Kurs', span: 1.25, sortable: true},
		{key: 'lehrer', label: 'Lehrer', span: 0.5, sortable: true},
		{key: 'quartal', label: 'Quartal', span: 0.25, sortable: true},
	];

	const colsSchuelerklausuren: DataTableColumn[] = [
		{key: 'name', label: 'Name', span: 1.25, sortable: true},
		{key: 'kurs', label: 'Kurs', span: 0.5, sortable: true},
		{key: 'quartal', label: 'Quartal', span: 0.25, sortable: true},
		{key: 'status', label: 'Korrektur', span: 0.45, sortable: true},
	];

	const colsTermine: DataTableColumn[] = [
		{key: 'kurse', label: 'Titel', span: 1.25, sortable: true},
		{key: 'quartal', label: 'Quartal', span: 0.25, sortable: true},
		{key: 'status', label: 'Korrektur', span: 0.25, sortable: true},
	];

	const colsKwKonflikte: DataTableColumn[] = [
		{key: 'kw', label: 'KW', span: 0.25, sortable: true},
		{key: 'schueler', label: 'Schüler', span: 0.75, sortable: true},
		{key: 'klausuren', label: 'Klausuren', span: 2.25, sortable: true},
	];

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

<style lang="postcss" scoped>
.page--content {
  @apply grid;
  grid-template-columns: 1fr minmax(20rem, 0.25fr);
}
</style>
