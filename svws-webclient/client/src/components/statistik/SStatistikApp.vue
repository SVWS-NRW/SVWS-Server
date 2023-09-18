<template>
	<div v-if="data" class="page--flex page--statistik">
		<svws-ui-header>
			<div class="flex flex-col">
				<div class="inline-block">{{ schulname }}</div>
				<!--TODO: Dynamischer Schuljahresabschnitt-->
				<div class="opacity-50">
					<svws-ui-tooltip>
						{{ schulform }} {{ schulNr }}
						<template #content>
							Schulform und Nummer
						</template>
					</svws-ui-tooltip>
				</div>
			</div>
		</svws-ui-header>
		<svws-ui-router-tab-bar :routes="routes" v-model="selectedRoute" :hidden="hidden">
			<template v-if="selectedRoute.name === 'dashboard'">
				<div class="page--content--dashboard">
					<svws-ui-dashboard-tile :span="2" color="transparent" title="Adresse">
						<div class="mt-5 whitespace-pre-line">{{ adresse }}</div>
						<div class="mt-2 flex gap-1">
							<svws-ui-button type="secondary" size="small">Schuldaten bearbeiten</svws-ui-button>
						</div>
					</svws-ui-dashboard-tile>
					<svws-ui-dashboard-tile color="dark" title="Nächster Termin" number="1.9.2023" number-label="Frist zur Einreichung">
						Meldung der Statistik
					</svws-ui-dashboard-tile>
					<svws-ui-dashboard-tile title="Platzhalter">
						Lorem ipsum dolor sit amet, consectetur adipisicing elit.
						<template #number>
							<div class="flex gap-1">
								<svws-ui-button type="secondary">
									Button
								</svws-ui-button>
								<svws-ui-button>
									Anderer Button
								</svws-ui-button>
							</div>
						</template>
					</svws-ui-dashboard-tile>
					<!--<svws-ui-dashboard-tile title="Kontaktdaten der Schule">
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
							<div>
								<div v-if="email" title="E-Mail">
									{{ email }}
								</div>
								<div title="Web-Adresse">
									{{ webAdresse || 'https://schule.nrw.de' }}
								</div>
							</div>
						</div>
					</svws-ui-dashboard-tile>-->
					<svws-ui-dashboard-tile span="full" color="transparent" title="Hinweis" class="my-20">
						Die Statistik ist noch in Entwicklung und aktuell nur zur Vorschau.<br>
						Alle Zahlen, Fehler und andere Inhalte in diesem Bereich sind Beispiele und keine aktuellen Daten aus dem Client.
					</svws-ui-dashboard-tile>
					<!--TODO: Dynamic Anzahl Schüler*innen und andere Daten-->
					<svws-ui-dashboard-tile color="dark" number="516" number-label="Fehler insgesamt">
						Relevante Daten für die Statistik müssen noch korrigiert werden.
					</svws-ui-dashboard-tile>
					<svws-ui-dashboard-tile title="Schülerdaten" number="351" number-label="zu korrigieren" @click="() => selectedRoute = routes[1]" clickable>
						1.421 Schüler angemeldet
					</svws-ui-dashboard-tile>
					<svws-ui-dashboard-tile title="Lehrerdaten" number="51" number-label="zu korrigieren" @click="() => selectedRoute = routes[2]" clickable>
						121 Lehrkräfte angestellt
					</svws-ui-dashboard-tile>
					<svws-ui-dashboard-tile title="Unterrichtsdaten" number="32" number-label="zu korrigieren" @click="() => selectedRoute = routes[3]" clickable>
						80 Kurse angeboten
					</svws-ui-dashboard-tile>
				</div>
			</template>
			<template v-if="selectedRoute.name !== 'dashboard'">
				<div class="page--content--dashboard">
					<svws-ui-dashboard-tile :span="2" color="transparent" :title="`${selectedRoute.name}daten`">
						<p>
							Dieser Bereich ist aktuell nur eine Vorschau. Alle Inhalte sind Beispiele und keine aktuellen Daten aus dem Client.
						</p>
					</svws-ui-dashboard-tile>
					<svws-ui-dashboard-tile number="1.425" number-label="Datensätze" />
					<svws-ui-dashboard-tile color="dark" number="512" number-label="Fehler zu korrigieren" />
					<svws-ui-dashboard-tile span="full" color="transparent">
						<svws-ui-spacing :size="2" />
						<svws-ui-data-table :items="[]" :no-data="false" :columns="cols">
							<template #body>
								<svws-ui-table-row>
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
								<svws-ui-table-row v-for="index in 40" :key="index">
									<svws-ui-table-cell>{{ Math.floor(Math.random() * 9999) }}</svws-ui-table-cell>
									<svws-ui-table-cell><span class="opacity-25">—</span></svws-ui-table-cell>
									<svws-ui-table-cell>XYZ{{ Math.floor(Math.random() * 16) + 10 }}</svws-ui-table-cell>
									<svws-ui-table-cell>
										<span v-if="Math.floor(Math.random() * 16) % 2 === 1" class="line-clamp-1">Individualdaten l: Konfession fehlt/fehlerhaft.</span>
										<span v-else class="line-clamp-1">Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ab corporis cupiditate deleniti, eum ipsa iste molestiae nam nostrum praesentium quas qui quisquam soluta tempore, ullam vel veniam vitae voluptatem voluptatum!</span>
									</svws-ui-table-cell>
									<svws-ui-table-cell align="center">
										<svws-ui-tooltip v-if="Math.floor(Math.random() * 8) % 2 === 1">
											<i-ri-information-line />
											<template #content>
												Hinweis
											</template>
										</svws-ui-tooltip>
										<svws-ui-tooltip v-else>
											<i-ri-alert-fill />
											<template #content>
												Harter Fehler
											</template>
										</svws-ui-tooltip>
									</svws-ui-table-cell>
								</svws-ui-table-row>
								<!--<svws-ui-table-row>
									<svws-ui-table-cell span="full"><span class="opacity-50">+ 7 weitere Fehler</span></svws-ui-table-cell>
								</svws-ui-table-row>-->
							</template>
						</svws-ui-data-table>
					</svws-ui-dashboard-tile>
				</div>
			</template>
		</svws-ui-router-tab-bar>
	</div>
	<div v-else class="app--content--placeholder">
		<i-ri-bar-chart-2-line />
	</div>
</template>

<script setup lang="ts">
	import type { ComputedRef } from "vue";
	import type {StatistikAppProps} from "./SStatistikAppProps";
	import type { DataTableColumn } from "@ui";
	import {computed, h, ref} from "vue";

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
		{ key: "id", label: "ID", span: 0.25, fixedWidth: 5 },
		{ key: "kuerzel", label: "Kürzel", span: 0.25 },
		{ key: "fehler", label: "Fehler", span: 0.25 },
		{ key: "fehlertext", label: "Erläuterung", span: 2 },
		{ key: "fehlerart", label: "Typ", tooltip: "Harter Fehler, Muss-Fehler oder Hinweis", span: 0.25, fixedWidth: 4, align: "center" },
	];

	const routes = [
		{ path: "/", name: "dashboard", component: { render: () => h("h1", "Dashboard") }, meta: { text: "Übersicht" } },
		{ path: "/schueler", name: "Schüler", component: { render: () => h("h1", "Schüler") }, meta: { text: "Schüler" } },
		{ path: "/lehrkraefte", name: "Lehrer", component: { render: () => h("h1", "Lehrkräfte") }, meta: { text: "Lehrkräfte" } },
		{ path: "/unterricht", name: "Unterrichts", component: { render: () => h("h1", "Unterricht") }, meta: { text: "Unterricht" } },
	];

	const hidden = ref([false, false, false]);
	const selectedRoute = ref(routes[0]);

</script>

<style lang="postcss" scoped>
</style>
