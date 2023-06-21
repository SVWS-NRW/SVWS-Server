<template>
	<div v-if="data" class="page--flex page--statistik">
		<svws-ui-header>
			<div class="flex flex-col">
				<div class="inline-block">Statistik</div>
				<!--TODO: Dynamischer Schuljahresabschnitt-->
				<div class="opacity-50">2023.1</div>
			</div>
		</svws-ui-header>
		<svws-ui-router-tab-bar :routes="routes" v-model="selectedRoute" :hidden="hidden">
			<template v-if="selectedRoute.name === 'dashboard'">
				<div class="page--content--dashboard">
					<svws-ui-dashboard-tile :span="2" color="transparent">
						<template #title>
							<svws-ui-tooltip>
								{{ schulform }} {{ schulNr }}
								<template #content>
									Schulform und Nummer
								</template>
							</svws-ui-tooltip>
						</template>
						<div>{{ schulname }}</div>
						<div class="mt-5 whitespace-pre-line">{{ adresse }}</div>
					</svws-ui-dashboard-tile>
					<svws-ui-dashboard-tile title="Schuldaten">
						<div class="flex flex-col gap-3">
							<div>
								<div v-if="telefon" title="Telefon">
									<span class="inline-block w-12">Tel</span>
									{{ telefon }}
								</div>
								<div v-if="fax" title="Fax">
									<span class="inline-block w-12">Fax</span>
									{{ fax }}
								</div>
							</div>
							<div v-if="email" title="E-Mail">
								{{ email }}
							</div>
							<div v-if="webAdresse" title="Web-Adresse">
								{{ webAdresse }}
							</div>
						</div>
						<div class="mt-3">
							<svws-ui-button type="secondary" size="small">Bearbeiten</svws-ui-button>
						</div>
					</svws-ui-dashboard-tile>
					<!--TODO: Dynamic Anzahl Schüler*innen und andere Daten-->
					<svws-ui-dashboard-tile title="An unserer Schule sind aktuell" color="dark" number="1.421" number-label="Schüler*innen" />
					<svws-ui-dashboard-tile span="full" color="transparent" title="Relevant für die Statistik" class="my-20">
						<div class="mb-5">10 Fehler müssen aktuell noch behoben werden.</div>
						<div class="mb-5">
							<svws-ui-button @click="() => selectedRoute = routes[1]">Alle Fehler anzeigen</svws-ui-button>
						</div>
						<svws-ui-data-table :items="[]" :no-data="false" :columns="cols">
							<template #body>
								<svws-ui-table-row>
									<svws-ui-table-cell>Lehrerdaten</svws-ui-table-cell>
									<svws-ui-table-cell>22</svws-ui-table-cell>
									<svws-ui-table-cell>ZUR</svws-ui-table-cell>
									<svws-ui-table-cell>LBA02</svws-ui-table-cell>
									<svws-ui-table-cell>
										<span class="line-clamp-1">Lehrkräfte/Basisdaten: Nachname muss linksbündig stehen, 1. Stelle muss ein Großbuchstabe sein.</span>
									</svws-ui-table-cell>
									<svws-ui-table-cell align="center">
										<svws-ui-tooltip>
											<i-ri-alert-line />
											<template #content>
												Muss-Fehler
											</template>
										</svws-ui-tooltip>
									</svws-ui-table-cell>
								</svws-ui-table-row>
								<svws-ui-table-row>
									<svws-ui-table-cell>Lehrerdaten</svws-ui-table-cell>
									<svws-ui-table-cell>254</svws-ui-table-cell>
									<svws-ui-table-cell>VAN</svws-ui-table-cell>
									<svws-ui-table-cell>LBA02</svws-ui-table-cell>
									<svws-ui-table-cell>
										<span class="line-clamp-1">Lehrkräfte/Basisdaten: Nachname muss linksbündig stehen, 1. Stelle muss ein Großbuchstabe sein.</span>
									</svws-ui-table-cell>
									<svws-ui-table-cell align="center">
										<svws-ui-tooltip>
											<i-ri-alert-fill />
											<template #content>
												Harter Fehler
											</template>
										</svws-ui-tooltip>
									</svws-ui-table-cell>
								</svws-ui-table-row>
								<svws-ui-table-row>
									<svws-ui-table-cell>Schülerdaten</svws-ui-table-cell>
									<svws-ui-table-cell>5687</svws-ui-table-cell>
									<svws-ui-table-cell><span class="opacity-25">—</span></svws-ui-table-cell>
									<svws-ui-table-cell>A03</svws-ui-table-cell>
									<svws-ui-table-cell>
										<span class="line-clamp-1">Individualdaten l: Konfession fehlt/fehlerhaft.</span>
									</svws-ui-table-cell>
									<svws-ui-table-cell align="center">
										<svws-ui-tooltip>
											<i-ri-information-line />
											<template #content>
												Hinweis
											</template>
										</svws-ui-tooltip>
									</svws-ui-table-cell>
								</svws-ui-table-row>
								<svws-ui-table-row>
									<svws-ui-table-cell span="full"><span class="opacity-50">+ 7 weitere Fehler</span></svws-ui-table-cell>
								</svws-ui-table-row>
							</template>
						</svws-ui-data-table>
					</svws-ui-dashboard-tile>
					<svws-ui-dashboard-tile title="Aktuell angestellt" number="104" number-label="Lehrkräfte" />
					<svws-ui-dashboard-tile title="Weitere Zahl" number="59.210" number-label="XYZ" />
					<svws-ui-dashboard-tile :span="2" color="dark" title="Hinweis">
						<p>
							Die Statistik ist noch in Entwicklung und aktuell nur zur Vorschau. <br>
							Alle Zahlen, Fehler und andere Inhalte in diesem Bereich sind Beispiele und keine aktuellen Daten aus dem Client.
						</p>
					</svws-ui-dashboard-tile>
				</div>
			</template>
			<template v-if="selectedRoute.name === 'pruefen'">
				<div class="page--content">
					<div class="col-span-full">
						<svws-ui-data-table :items="[]" :columns="cols" no-data-html="Der Bereich für die Statistik ist noch in Entwicklung und ist aktuell nur eine Vorschau." />
					</div>
				</div>
			</template>
		</svws-ui-router-tab-bar>
	</div>
	<div v-else class="app--content--placeholder">
		<i-ri-bar-chart-2-line />
	</div>
</template>

<script setup lang="ts">
	import type { ComputedRef, WritableComputedRef } from "vue";
	import type {StatistikAppProps} from "./SStatistikAppProps";
	import {computed, h, ref} from "vue";
	import type {DataTableColumn} from "@ui";

	const props = defineProps<StatistikAppProps>();

	const schulname: ComputedRef<string> = computed(() => {
		const name = props.schule?.bezeichnung1;
		return name ? name : "Schule";
	});

	const schulNr: ComputedRef<string> = computed(() => {
		const nr = props.schule?.schulNr;
		return nr ? nr.toString() : "#";
	});

	const schulform: ComputedRef<string> = computed(() => {
		const form = props.schule?.schulform;
		return form ? form : "";
	});

	const adresse: ComputedRef<string> = computed(() => {
		const strassenname = props.schule?.strassenname;
		const hausnummer = props.schule?.hausnummer;
		const hausnummerZusatz = props.schule?.hausnummerZusatz;
		const plz = props.schule?.plz;
		const ort = props.schule?.ort;

		return `${strassenname} ${hausnummer}${hausnummerZusatz ? " " + hausnummerZusatz : ""}\n${plz} ${ort}`;
	});

	const telefon: ComputedRef<string> = computed(() => {
		const telefon = props.schule?.telefon;
		return telefon ? telefon : "";
	});

	const fax: ComputedRef<string> = computed(() => {
		const fax = props.schule?.fax;
		return fax ? fax : "";
	});

	const email: ComputedRef<string> = computed(() => {
		const email = props.schule?.email;
		return email ? email : "";
	});

	const webAdresse: ComputedRef<string> = computed(() => {
		const webAdresse = props.schule?.webAdresse;
		return webAdresse ? webAdresse : "";
	});

	const data = true;

	const cols: DataTableColumn[] = [
		{ key: "in", label: "Bereich", span: 0.5 },
		{ key: "id", label: "ID", span: 0.25 },
		{ key: "kuerzel", label: "Kürzel", span: 0.25 },
		{ key: "fehler", label: "Fehler", span: 0.25 },
		{ key: "fehlertext", label: "Erläuterung", span: 2 },
		{ key: "fehlerart", label: "Typ", tooltip: "Harter Fehler, Muss-Fehler oder Hinweis", span: 0.25, fixedWidth: 4, align: "center" },
	];

	const routes = [
		{ path: "/", name: "dashboard", component: { render: () => h("h1", "Dashboard") }, meta: { text: "Dashboard" } },
		{ path: "/pruefen", name: "pruefen", component: { render: () => h("h1", "Prüfen") }, meta: { text: "Prüfen" } },
	];

	const hidden = ref([false, false, false]);
	const selectedRoute = ref(routes[0]);

</script>

<style lang="postcss" scoped>
</style>
