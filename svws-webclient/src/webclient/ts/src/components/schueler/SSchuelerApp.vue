<template>
	<div class="flex h-full flex-row">
		<div v-if="app?.stammdaten.daten" class="flex w-full flex-col">
			<svws-ui-header :badge="inputId" badge-variant="light" badge-size="normal">
				<span> {{ inputVorname }} {{ inputNachname }} </span>
				<svws-ui-badge variant="highlight" size="normal"> {{ inputKlasse }} </svws-ui-badge>
			</svws-ui-header>
			<svws-ui-tab-bar v-model="app.selectedTab.value">
				<template #tabs>
					<svws-ui-tab-button>Individualdaten</svws-ui-tab-button>
					<svws-ui-tab-button>Erziehungsberechtigte</svws-ui-tab-button>
					<svws-ui-tab-button>Adressen / Betriebe</svws-ui-tab-button>
					<svws-ui-tab-button>Schulbesuch</svws-ui-tab-button>
					<svws-ui-tab-button>Aktuelles Halbjahr</svws-ui-tab-button>
					<svws-ui-tab-button>Leistungsdaten</svws-ui-tab-button>
					<svws-ui-tab-button :hidden="!hatAbiturjahrgang">Laufbahnplanung</svws-ui-tab-button>
					<svws-ui-tab-button>Stundenplan</svws-ui-tab-button>
				</template>
				<template #panels>
					<svws-ui-tab-panel>
						<s-schueler-individualdaten />
					</svws-ui-tab-panel>
					<svws-ui-tab-panel>
						<s-schueler-erziehungsberechtigte />
					</svws-ui-tab-panel>
					<svws-ui-tab-panel>
						<s-schueler-adressen />
					</svws-ui-tab-panel>
					<svws-ui-tab-panel>
						<s-schueler-schulbesuch />
					</svws-ui-tab-panel>
					<svws-ui-tab-panel>
						<s-schueler-halbjahr />
					</svws-ui-tab-panel>
					<svws-ui-tab-panel>
						<s-schueler-leistungsdaten />
					</svws-ui-tab-panel>
					<svws-ui-tab-panel :hidden="!hatAbiturjahrgang">
						<s-schueler-laufbahnplanung />
					</svws-ui-tab-panel>
					<svws-ui-tab-panel>
						<s-schueler-stundenplan />
					</svws-ui-tab-panel>
				</template>
			</svws-ui-tab-bar>
		</div>
	</div>
</template>

<script setup lang="ts">

	import { computed, ComputedRef } from "vue";
	import { App } from "~/apps/BaseApp";
	import { Klassen } from "~/apps/klassen/Klassen";
	import { Schueler } from "~/apps/schueler/Schueler";
	import SSchuelerStundenplan from "./stundenplan/SSchuelerStundenplan.vue";

	import { injectMainApp, Main } from "~/apps/Main";

	const main: Main = injectMainApp();
	const app: Schueler = main.apps.schueler;

	const appKlassen: Klassen = main.apps.klassen;

	const hatAbiturjahrgang: ComputedRef<boolean> = computed(()=> {
		const abiturjahr = main.apps.schueler.dataGostLaufbahndaten?.daten?.abiturjahr;
		const jahrgang = App.apps.gost.auswahl.liste.find(j => (j.abiturjahr === abiturjahr));
		return jahrgang ? true : false;
	})

	const gost: ComputedRef<boolean> = computed(() => {
		return !!App.apps.schule.schulform?.daten.hatGymOb;
	});

	const inputNachname: ComputedRef<string | false> = computed(() => {
		return (app.auswahl.ausgewaehlt === undefined) ? false : app.auswahl.ausgewaehlt.nachname.toString();
	});

	const inputVorname: ComputedRef<string | false> = computed(() => {
		return (app.auswahl.ausgewaehlt === undefined) ? false : app.auswahl.ausgewaehlt.vorname.toString();
	});

	const inputId: ComputedRef<string> = computed(() => {
		return (app.auswahl.ausgewaehlt === undefined) ? "" : "ID: " + app.auswahl.ausgewaehlt.id;
	});

	const inputKlasse: ComputedRef<string | false> = computed(() => {
		if (app.auswahl.ausgewaehlt === undefined)
			return false;
		const id = app.auswahl.ausgewaehlt.idKlasse;
		const klasse = appKlassen.auswahl.liste.find(k => k.id === id);
		return klasse?.kuerzel?.toString() || false;
	});

</script>
