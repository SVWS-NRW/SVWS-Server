<template>
	<div v-if="data" class="page page-flex-col gap-0 page--statistik">
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
		<svws-ui-tab-bar :tab-manager :focus-switching-enabled :focus-help-visible>
			<template v-if="tabManager().tab.name === 'dashboard'">
				<div class="page page-grid grid-cols-2 lg:grid-cols-4" style="grid-auto-rows: min-content">
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
					<svws-ui-dashboard-tile title="Schülerdaten" number="351" number-label="zu korrigieren" @click="setTab({ name: 'Schüler', text: 'Schüler' })" clickable>
						1.421 Schüler angemeldet
					</svws-ui-dashboard-tile>
					<svws-ui-dashboard-tile title="Lehrerdaten" number="51" number-label="zu korrigieren" @click="setTab({ name: 'Lehrer', text: 'Lehrer' })" clickable>
						121 Lehrkräfte angestellt
					</svws-ui-dashboard-tile>
					<svws-ui-dashboard-tile title="Unterrichtsdaten" number="32" number-label="zu korrigieren" @click="setTab({ name: 'Unterrichts', text: 'Unterricht' })" clickable>
						80 Kurse angeboten
					</svws-ui-dashboard-tile>
				</div>
			</template>
			<template v-if="tabManager().tab.name !== 'dashboard'">
				<div class="page page-grid-cards grid-cols-2 lg:grid-cols-4 gap-2">
					<svws-ui-dashboard-tile :span="2" color="transparent" :title="`${tabManager().tab.name}daten`">
						<p>
							Dieser Bereich ist aktuell nur eine Vorschau. Alle Inhalte sind Beispiele und keine aktuellen Daten aus dem Client.
						</p>
					</svws-ui-dashboard-tile>
					<svws-ui-dashboard-tile number="1.425" number-label="Datensätze" />
					<svws-ui-dashboard-tile color="dark" number="512" number-label="Fehler zu korrigieren" />
					<svws-ui-dashboard-tile span="full" color="transparent">
						<svws-ui-spacing :size="2" />
						<svws-ui-table :items="[]" :no-data="false" :columns="cols">
							<template #body>
								<div role="row" class="svws-ui-tr">
									<div role="cell" class="svws-ui-td">22</div>
									<div role="cell" class="svws-ui-td">ZUR</div>
									<div role="cell" class="svws-ui-td">LBA02</div>
									<div role="cell" class="svws-ui-td">
										<span class="line-clamp-1">Lehrkräfte/Basisdaten: Nachname muss linksbündig stehen, 1. Stelle muss ein Großbuchstabe sein.</span>
									</div>
									<div role="cell" class="svws-ui-td svws-align-center">
										<svws-ui-tooltip>
											<span class="icon i-ri-alert-line" />
											<template #content>
												Muss-Fehler
											</template>
										</svws-ui-tooltip>
									</div>
								</div>
								<div role="row" class="svws-ui-tr">
									<div role="cell" class="svws-ui-td">254</div>
									<div role="cell" class="svws-ui-td">VAN</div>
									<div role="cell" class="svws-ui-td">LBA02</div>
									<div role="cell" class="svws-ui-td">
										<span class="line-clamp-1">Lehrkräfte/Basisdaten: Nachname muss linksbündig stehen, 1. Stelle muss ein Großbuchstabe sein.</span>
									</div>
									<div role="cell" class="svws-ui-td svws-align-center">
										<svws-ui-tooltip>
											<span class="icon i-ri-alert-fill" />
											<template #content>
												Harter Fehler
											</template>
										</svws-ui-tooltip>
									</div>
								</div>
								<div role="row" class="svws-ui-tr" v-for="index in 40" :key="index">
									<div role="cell" class="svws-ui-td">{{ Math.floor(Math.random() * 9999) }}</div>
									<div role="cell" class="svws-ui-td"><span class="opacity-25">—</span></div>
									<div role="cell" class="svws-ui-td">XYZ{{ Math.floor(Math.random() * 16) + 10 }}</div>
									<div role="cell" class="svws-ui-td">
										<span v-if="Math.floor(Math.random() * 16) % 2 === 1" class="line-clamp-1">Individualdaten l: Konfession fehlt/fehlerhaft.</span>
										<span v-else class="line-clamp-1">Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ab corporis cupiditate deleniti, eum ipsa iste molestiae nam nostrum praesentium quas qui quisquam soluta tempore, ullam vel veniam vitae voluptatem voluptatum!</span>
									</div>
									<div role="cell" class="svws-ui-td svws-align-center">
										<svws-ui-tooltip v-if="Math.floor(Math.random() * 8) % 2 === 1">
											<span class="icon i-ri-information-line" />
											<template #content>
												Hinweis
											</template>
										</svws-ui-tooltip>
										<svws-ui-tooltip v-else>
											<span class="icon i-ri-alert-fill" />
											<template #content>
												Harter Fehler
											</template>
										</svws-ui-tooltip>
									</div>
								</div>
								<!--<div role="row" class="svws-ui-tr">
									<div role="cell" class="svws-ui-td" span="full"><span class="opacity-50">+ 7 weitere Fehler</span></div>
								</div>-->
							</template>
						</svws-ui-table>
					</svws-ui-dashboard-tile>
				</div>
			</template>
		</svws-ui-tab-bar>
	</div>
	<div v-else class="app--content--placeholder">
		<span class="icon i-ri-bar-chart-2-line" />
	</div>
</template>

<script setup lang="ts">

	import { computed, shallowRef } from "vue";
	import { type TabData, type DataTableColumn, TabManager, useRegionSwitch } from "@ui";
	import type { StatistikAppProps } from "./SStatistikAppProps";

	const props = defineProps<StatistikAppProps>();
	const { focusHelpVisible, focusSwitchingEnabled } = useRegionSwitch();

	const schulname = computed(() => {
		const name = props.schule.bezeichnung1;
		return (name.length > 0) ? name : "Schule";
	});

	const schulNr = computed(() => {
		const nr = props.schule.schulNr;
		return nr.toString();
	});

	const schulform = computed(() => {
		const form = props.schule.schulform;
		return form;
	});

	const adresse = computed(() => {
		const strassenname = props.schule.strassenname;
		const hausnummer = props.schule.hausnummer;
		const hausnummerZusatz = props.schule.hausnummerZusatz;
		const plz = props.schule.plz;
		const ort = props.schule.ort;

		return `${strassenname} ${hausnummer}${hausnummerZusatz !== null ? " " + hausnummerZusatz : ""}\n${plz} ${ort}`;
	});

	const telefon = computed(() => {
		const telefon = props.schule.telefon;
		return telefon;
	});

	const fax = computed(() => {
		const fax = props.schule.fax;
		return fax;
	});

	const email = computed(() => {
		const email = props.schule.email;
		return email;
	});

	const webAdresse = computed(() => {
		const webAdresse = props.schule.webAdresse;
		return webAdresse;
	});

	const data = true;

	const cols: DataTableColumn[] = [
		{ key: "id", label: "ID", span: 0.25, fixedWidth: 5 },
		{ key: "kuerzel", label: "Kürzel", span: 0.25 },
		{ key: "fehler", label: "Fehler", span: 0.25 },
		{ key: "fehlertext", label: "Erläuterung", span: 2 },
		{ key: "fehlerart", label: "Typ", tooltip: "Harter Fehler, Muss-Fehler oder Hinweis", span: 0.25, fixedWidth: 4 },
	];

	async function setTab(tab: TabData) {
	}

	const refTabManager = shallowRef<TabManager>(new TabManager([
		{ name: "dashboard", text: "Übersicht" },
		{ name: "Schüler", text: "Schüler" },
		{ name: "Lehrer", text: "Lehrkräfte" },
		{ name: "Unterrichts", text: "Unterricht" },
	], { name: "dashboard", text: "Übersicht" }, setTab));

	function tabManager() : TabManager {
		return refTabManager.value;
	}


</script>

<style scoped>

	.svws-ui-tr {
		grid-template-columns: minmax(5rem, 5rem) minmax(4rem, 0.25fr) minmax(4rem, 0.25fr) minmax(4rem, 2fr) minmax(4rem, 4rem);
	}

</style>

